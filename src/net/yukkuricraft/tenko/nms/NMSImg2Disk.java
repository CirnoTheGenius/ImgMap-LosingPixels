package net.yukkuricraft.tenko.nms;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

import net.minecraft.server.v1_7_R3.NBTBase;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.yukkuricraft.tenko.render.RenderUtils;

import org.bukkit.Bukkit;
import org.bukkit.map.MapPalette;

/**
 * NOTE: THIS CLASS DOES NOT WORK. REMOVE IF COMPILING FROM SOURCE MANUALLY.
 * 
 * To be more specific, this class is going to actually write to the .dat files by re-using same code from CB/mc-dev repos. Therefore, this class has errors, will not compile, and blow up if used. It's a rather intresting result; it creates a background for maps.
 * 
 * @author Tsunko
 * 
 */
public class NMSImg2Disk {
	
	public static File getDataFile(String worldName, String mapId){
		return new File(Bukkit.getWorld(worldName).getWorldFolder().getAbsolutePath() + "/data/", mapId + ".dat");
	}
	
	// I don't know why I would need this. Maybe for later.
	// De-obfuscated "a" method. The one that takes an NBTTagCompound under WorldMap.
	public static NBTDataGroup getNBTData(NBTTagCompound nbtTag) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		byte dimension = nbtTag.getByte("dimension");
		byte[] colors = new byte[16384];
		
		int centerX = nbtTag.getInt("xCenter");
		int centerZ = nbtTag.getInt("zCenter");
		byte scale = nbtTag.getByte("scale");
		
		if(scale < 0){
			scale = 0;
		}else if(scale > 4){
			scale = 4;
		}
		
		short width = nbtTag.getShort("width");
		short height = nbtTag.getShort("height");
		
		if((width == 128) && (height == 128)){
			colors = nbtTag.getByteArray("colors");
		}else{
			// This part made me pull my hairs out.
			byte[] newColors = nbtTag.getByteArray("colors");
			colors = new byte[16384];
			
			int newWidth = (128 - width) / 2;
			int newHeight = (128 - height) / 2;
			
			for(int looper1 = 0; looper1 < newHeight; looper1++){
				int x = looper1 + newHeight;
				
				if((x >= 0) || (x < 128)){
					for(int looper2 = 0; looper2 < newWidth; looper2++){
						int y = looper2 + looper1;
						
						if((y >= 0) || (y < 128)){
							colors[y + (x * 128)] = newColors[looper2 + (looper1 * width)];
						}
					}
				}
			}
		}
		
		NBTDataGroup data = new NBTDataGroup();
		data.storeVariable("dimension", dimension);
		data.storeVariable("xCenter", centerX);
		data.storeVariable("zCenter", centerZ);
		data.storeVariable("scale", scale);
		data.storeVariable("width", width);
		data.storeVariable("height", height);
		data.storeVariable("colors", colors);
		return data;
	}
	
	// Thank you, Comphenix :3
	@SuppressWarnings("deprecation")
	public static void setData(BufferedImage io, byte dimension, String world, String mapid) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, InstantiationException{
		NBTTagCompound pack = new NBTTagCompound();
		NBTTagCompound data = new NBTTagCompound();
		
		data.setByte("dimension", dimension);
		data.setInt("xCenter", 10);
		data.setInt("zCenter", 20);
		data.setByte("scale", (byte) 5);
		data.setShort("width", (short) 128);
		data.setShort("height", (short) 128);
		RenderUtils.resizeImage(io);
		data.setByteArray("colors", MapPalette.imageToBytes(io));
		data.set("data", data);
		
		FileOutputStream fos = new FileOutputStream(NMSImg2Disk.getDataFile(world, mapid));
		NMSImg2Disk.saveNbt(pack, fos);
		fos.close();
	}
	
	private static <T extends OutputStream> void saveNbt(NBTBase base, T output) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		DataOutputStream pipe = new DataOutputStream(new GZIPOutputStream(output));
		
		// This is the method you should use
		NBTTagCompound.class.getMethod("a", NBTBase.class, DataOutput.class).invoke(null, base, pipe);
		pipe.close();
	}
	
	public static class NBTDataGroup {
		
		private HashMap<String, Object> values;
		
		public NBTDataGroup() {
			this.values = new HashMap<String, Object>();
		}
		
		public boolean storeVariable(String name, Object value){
			if(!this.values.containsKey(name)){
				this.values.put(name, value);
				return true;
			}
			
			return false;
		}
	}
	
}