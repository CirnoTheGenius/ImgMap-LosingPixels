package com.tenko.test;

import java.io.File;
import org.bukkit.Bukkit;

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

	static Class<?> worldBase, NBTTag, NBTCompress;

	static {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf(".") + 1);
		try {
			worldBase = Class.forName("net.minecraft.server." + version + ".WorldMapBase");
			NBTTag = Class.forName("net.minecraft.server." + version + ".NBTTagCompound");
			NBTCompress = Class.forName("net.minecraft.server." + version + ".NBTCompressedStreamTools");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//Just when I thought I was able to avoid NMS code.
	public static void a(int id, String worldName) {
		//worldmapbase is an abstract class. Something extends WorldMapBase.
		
//		try {
//			if(worldBase.isAssignableFrom(worldmapbase.getClass())){
//				Field id = worldBase.getField("id");
//				File dataFile = getDataFile(worldName, (String)id.get(worldmapbase));
//
//				if (dataFile != null) {
//					Object nbttagcompound = NBTTag.newInstance();
//
//					worldBase.getMethod("a", NBTTag).invoke(worldmapbase, nbttagcompound);
//					Object nbtData = NBTTag.newInstance();
//
//					NBTTag.getMethod("setCompound", String.class, NBTTag).invoke(nbtData, "data", nbttagcompound);
//					FileOutputStream fileoutputstream = new FileOutputStream(dataFile);
//
//					NBTCompress.getMethod("a", NBTTag, FileOutputStream.class).invoke(null, nbtData, fileoutputstream);
//					fileoutputstream.close();
//				}
//			}
//		} catch (Exception exception) {
//			exception.printStackTrace();
//		}
	}

	//Work on this later. Direct copy/paste from CB code. Unsafe; volatile.
	public void dur(){
//		// CraftBukkit start
//		byte dimension = nbttagcompound.getByte("dimension");
//
//		if (dimension >= 10) {
//			long least = nbttagcompound.getLong("UUIDLeast");
//			long most = nbttagcompound.getLong("UUIDMost");
//
//			if (least != 0L && most != 0L) {
//				this.uniqueId = new UUID(most, least);
//
//				CraftWorld world = (CraftWorld) server.getWorld(this.uniqueId);
//				// Check if the stored world details are correct.
//				if (world == null) {
//					/* All Maps which do not have their valid world loaded are set to a dimension which hopefully won't be reached.
//This is to prevent them being corrupted with the wrong map data. */
//					dimension = 127;
//				} else {
//					dimension = (byte) world.getHandle().dimension;
//				}
//			}
//		}
//
//		this.map = dimension;
//		// CraftBukkit end
//		this.centerX = nbttagcompound.getInt("xCenter");
//		this.centerZ = nbttagcompound.getInt("zCenter");
//		this.scale = nbttagcompound.getByte("scale");
//		if (this.scale < 0) {
//			this.scale = 0;
//		}
//
//		if (this.scale > 4) {
//			this.scale = 4;
//		}
//
//		short short1 = nbttagcompound.getShort("width");
//		short short2 = nbttagcompound.getShort("height");
//
//		if (short1 == 128 && short2 == 128) {
//			this.colors = nbttagcompound.getByteArray("colors");
//		} else {
//			byte[] abyte = nbttagcompound.getByteArray("colors");
//
//			this.colors = new byte[16384];
//			int i = (128 - short1) / 2;
//			int j = (128 - short2) / 2;
//
//			for (int k = 0; k < short2; ++k) {
//				int l = k + j;
//
//				if (l >= 0 || l < 128) {
//					for (int i1 = 0; i1 < short1; ++i1) {
//						int j1 = i1 + i;
//
//						if (j1 >= 0 || j1 < 128) {
//							this.colors[j1 + l * 128] = abyte[i1 + k * short1];
//						}
//					}
//				}
//			}
//		}
	}

	public static File getDataFile(String worldName, String mapId) {
		return new File(Bukkit.getWorld(worldName).getWorldFolder().getAbsolutePath() + "/data/", mapId + ".dat");
	}

}
