package com.tenko.test;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.map.MapPalette;

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

	static Class<?> worldBase, NBTTag, NBTCompress, CraftWorld;
	
	static {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf(".") + 1);
		try { 
			worldBase = Class.forName("net.minecraft.server." + version + ".WorldMapBase");
			NBTTag = Class.forName("net.minecraft.server." + version + ".NBTTagCompound");
			NBTCompress = Class.forName("net.minecraft.server." + version + ".NBTCompressedStreamTools");
			CraftWorld = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void tryTest(){
		
//		try {
//			Class<?> nmsItemStack = Class.forName("net.minecraft.server." + version + ".ItemStack");
//			Class<?> nmsItem = Class.forName("net.minecraft.server." + version + ".Item");
//			
//			Object worldMap = nmsItem.getField("MAP").get(null);
//			Object o = nmsItemStack.getConstructor(nmsItem, int.class, int.class).newInstance(worldMap, 1, 1);
//			System.out.println(o);
//			Object nbt = o.getClass().getMethod("getTag").invoke(o);
//
//			System.out.println(nbt);
//			System.out.println(nbt.getClass());
//			System.out.println(nbt.toString());
//			System.out.println(nbt.hashCode());
//			
//			//setData(nbt, ImageIO.read(new URL("http://fc03.deviantart.net/fs28/f/2009/251/c/d/Happy_Cirno_Day_by_shingenjitsu.png").openStream()), (byte) 0);
//		} catch (ClassNotFoundException e){
//			e.printStackTrace();
//		} catch (IllegalArgumentException e){
//			e.printStackTrace();
//		} catch (SecurityException e){
//			e.printStackTrace();
//		} catch (IllegalAccessException e){
//			e.printStackTrace();
//		} catch (NoSuchFieldException e){
//			e.printStackTrace();
//		} catch (InstantiationException e){
//			e.printStackTrace();
//		} catch (InvocationTargetException e){
//			e.printStackTrace();
//		} catch (NoSuchMethodException e){
//			e.printStackTrace();
//		}
	}
	
//	//I don't know why I would need this. Maybe for later.
//	//Deobfuscated "a" method. The one that takes an NBTTagCompound under WorldMap.
//	@SuppressWarnings("unused")
//	public static void getNBTData(Object nbttag) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException{
//		if(nbttag.getClass() == NBTTag){
//			Method getByte = NBTTag.getMethod("getByte", String.class);
//			Method getLong = NBTTag.getMethod("getLong", String.class);
//			Method getInt = NBTTag.getMethod("getInt", String.class);
//			Method getShort = NBTTag.getMethod("getShort", String.class);
//			Method getByteArray = NBTTag.getMethod("getByteArray", String.class);
//			
//			byte dimension = (Byte)getByte.invoke(nbttag, "dimension");
//			byte map;
//			
//			byte[] colors = new byte[16384];
//			
//			//CraftBukkit code begin
//			if(dimension >= 10){
//				long least = (Long)getLong.invoke(nbttag, "UUIDLeast");
//				long most = (Long)getLong.invoke(nbttag, "UUIDMost");
//				
//				UUID id = null;
//				
//				if(least != 0L && most != 0L){
//					id = new UUID(most, least);
//					
//					Object world = CraftWorld.cast(Bukkit.getWorld(id));
//					
//					if(world == null){
//						dimension = 127;
//					} else {
//						Object handle = CraftWorld.getMethod("").invoke(world, (Object[])null);
//						dimension = (Byte)handle.getClass().getField("dimension").get(handle);
//					}
//				}
//				
//				map = dimension;	
//			}
//			//CraftBukkit code end
//
//			int centerX = (Integer)getInt.invoke(nbttag, "xCenter");
//			int centerZ = (Integer)getInt.invoke(nbttag, "zCenter");
//			byte scale = (Byte)getByte.invoke(nbttag, "scale");
//			
//			if(scale < 0){
//				scale = 0;
//			} else if (scale > 4){
//				scale = 4;
//			}
//			
//			short width = (Short)getShort.invoke(nbttag, "width");
//			short height = (Short)getShort.invoke(nbttag, "height");
//			
//			if(width == 128 && height == 128){
//				colors = (byte[])getByteArray.invoke(nbttag, "colors");
//			} else {
//				//This part made me pull my hairs out.
//				byte[] newColors = (byte[])getByteArray.invoke(nbttag, "colors");
//				colors = new byte[16384];
//				
//				int newWidth = (128 - width) / 2;
//				int newHeight = (128 - height) / 2;
//				
//				for(int looper1=0; looper1 < newHeight; looper1++){
//					int x = looper1 + newHeight;
//					
//					if(x >= 0 || x < 128){
//						for(int looper2=0; looper2 < newWidth; looper2++){
//							int y = looper2+looper1;
//							
//							if(y >= 0 || y < 128){
//								colors[y + x * 128] = newColors[looper2 + looper1 * width];
//							}
//						}
//					}
//				}
//			}
//		}
//	}
	
    public static void setData(Object nbtTag, Image io, byte dimension, String world, String mapid) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException{
    	setByte(nbtTag, "dimension", (byte)dimension);
    	setInt(nbtTag, "xCenter", (int)0);
    	setInt(nbtTag, "zCenter", (int)0);
    	setByte(nbtTag, "scale", (byte)1);
    	setShort(nbtTag, "width", (short)128);
    	setShort(nbtTag, "height", (short)128);
    	setByteArray(nbtTag, "colors", MapPalette.imageToBytes(MapPalette.resizeImage(io)));
    	
        FileOutputStream fileoutputstream = new FileOutputStream(getDataFile(world, mapid));
    	NBTCompress.getMethod("a", NBTTag, OutputStream.class).invoke(null, nbtTag, (OutputStream)fileoutputstream);
        fileoutputstream.close();
    }
    

	public static File getDataFile(String worldName, String mapId) {
		return new File(Bukkit.getWorld(worldName).getWorldFolder().getAbsolutePath() + "/data/", mapId + ".dat");
	}
	
	public static void setByte(Object nbtTag, String property, byte b) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		if(nbtTag.getClass() == NBTTag){
			NBTTag.getMethod("setByte", String.class, byte.class).invoke(nbtTag, property, b);
		}
	}
	
	public static void setInt(Object nbtTag, String property, int i) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		if(nbtTag.getClass() == NBTTag){
			NBTTag.getMethod("setInt", String.class, int.class).invoke(nbtTag, property, i);
		}
	}
	
	public static void setShort(Object nbtTag, String property, short s) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		if(nbtTag.getClass() == NBTTag){
			NBTTag.getMethod("setShort", String.class, short.class).invoke(nbtTag, property, s);
		}
	}
	
	public static void setByteArray(Object nbtTag, String property, byte[] ba) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		if(nbtTag.getClass() == NBTTag){
			NBTTag.getMethod("setByteArray", String.class, byte[].class).invoke(nbtTag, property, ba);
		}
	}
}
