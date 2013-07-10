package com.tenko.test;

import java.awt.Image;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.map.MapPalette;

import com.tenko.volatileNMS.NBTDataGroup;

/**
 * NOTE:
 * THIS CLASS DOES NOT WORK. REMOVE IF COMPILING FROM SOURCE MANUALLY.
 * 
 * To be more specific, this class is going to actually write to the
 * .dat files by re-using same code from CB/mc-dev repos. Therefore,
 * this class has errors, will not compile, and blow up if used.
 * 
 * @author Tsunko
 *
 */
public class SaveImageToDisk {

	//CB things.
	static Class<?> cbCraftWorld; 
	//NMS things.
	static Class<?> nmsWorldBase, nmsNBTTagCompound, nmsNBTCompress, nmsNBTTagList, nmsNBTBase;

	static String packageName = Bukkit.getServer().getClass().getPackage().getName();
	static String version = packageName.substring(packageName.lastIndexOf(".") + 1);

	static {
		try { 
			nmsWorldBase = Class.forName("net.minecraft.server." + version + ".WorldMapBase");
			nmsNBTTagCompound = Class.forName("net.minecraft.server." + version + ".NBTTagCompound");
			nmsNBTCompress = Class.forName("net.minecraft.server." + version + ".NBTCompressedStreamTools");
			nmsNBTTagList = Class.forName("net.minecraft.server." + version + ".NBTTagList");
			nmsNBTBase = Class.forName("net.minecraft.server." + version + ".NBTBase");
			cbCraftWorld = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static File getDataFile(String worldName, String mapId) {
		return new File(Bukkit.getWorld(worldName).getWorldFolder().getAbsolutePath() + "/data/", mapId + ".dat");
	}

	//I don't know why I would need this. Maybe for later.
	//De-obfuscated "a" method. The one that takes an NBTTagCompound under WorldMap.
	public static NBTDataGroup getNBTData(Object nbttag) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(nbttag.getClass() == nmsNBTTagCompound){
			Method getByte = nmsNBTTagCompound.getMethod("getByte", String.class);
			Method getInt = nmsNBTTagCompound.getMethod("getInt", String.class);
			Method getShort = nmsNBTTagCompound.getMethod("getShort", String.class);
			Method getByteArray = nmsNBTTagCompound.getMethod("getByteArray", String.class);

			byte dimension = (Byte)getByte.invoke(nbttag, "dimension");
			byte[] colors = new byte[16384];

			int centerX = (Integer)getInt.invoke(nbttag, "xCenter");
			int centerZ = (Integer)getInt.invoke(nbttag, "zCenter");
			byte scale = (Byte)getByte.invoke(nbttag, "scale");

			if(scale < 0){
				scale = 0;
			} else if (scale > 4){
				scale = 4;
			}

			short width = (Short)getShort.invoke(nbttag, "width");
			short height = (Short)getShort.invoke(nbttag, "height");

			if(width == 128 && height == 128){
				colors = (byte[])getByteArray.invoke(nbttag, "colors");
			} else {
				//This part made me pull my hairs out.
				byte[] newColors = (byte[])getByteArray.invoke(nbttag, "colors");
				colors = new byte[16384];

				int newWidth = (128 - width) / 2;
				int newHeight = (128 - height) / 2;

				for(int looper1=0; looper1 < newHeight; looper1++){
					int x = looper1 + newHeight;

					if(x >= 0 || x < 128){
						for(int looper2=0; looper2 < newWidth; looper2++){
							int y = looper2+looper1;

							if(y >= 0 || y < 128){
								colors[y + x * 128] = newColors[looper2 + looper1 * width];
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
		
		return null;
	}
	
	//Thank you, Comphenix :3
	public static void setData(Image io, byte dimension, String world, String mapid) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, InstantiationException{
		System.out.println("things");
		Object pack = nmsNBTTagCompound.newInstance();
		Object data = nmsNBTTagCompound.newInstance();

		setByte(data, "dimension", dimension);
		setInt(data, "xCenter", 10);
		setInt(data, "zCenter", 20);
		setByte(data, "scale", (byte)5);

		setShort(data, "width", (short)128);
		setShort(data, "height", (short)128);
		setByteArray(data, "colors", MapPalette.imageToBytes(MapPalette.resizeImage(io)));
		
		nmsNBTTagCompound.getMethod("set", String.class, nmsNBTBase).invoke(pack, "data", data);
		
		FileOutputStream fos = new FileOutputStream(getDataFile(world, mapid));
		saveNbt(pack, fos);
		fos.close();
	}
	
	private static <T extends OutputStream> T saveNbt(Object base, T output) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		DataOutputStream pipe = new DataOutputStream(new GZIPOutputStream(output));
		 
		// This is the method you should use
		nmsNBTBase.getMethod("a", nmsNBTBase, DataOutput.class).invoke(null, base, pipe);
		pipe.close();
		return output;
	}

	public static void setByte(Object nbtTag, String property, byte b) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		nmsNBTTagCompound.getMethod("setByte", String.class, byte.class).invoke(nbtTag, property, b);
	}

	public static void setByteArray(Object nbtTag, String property, byte[] ba) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		nmsNBTTagCompound.getMethod("setByteArray", String.class, byte[].class).invoke(nbtTag, property, ba);
	}
	
	public static void setInt(Object nbtTag, String property, int i) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		nmsNBTTagCompound.getMethod("setInt", String.class, int.class).invoke(nbtTag, property, i);
	}

	public static void setLong(Object nbtTag, String property, long l) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		nmsNBTTagCompound.getMethod("setLong", String.class, long.class).invoke(nbtTag, property, l);
	}

	public static void setShort(Object nbtTag, String property, short s) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		nmsNBTTagCompound.getMethod("setShort", String.class, short.class).invoke(nbtTag, property, s);
	}

}
