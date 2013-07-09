package com.tenko.Gunvarrel.Parts;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.tenko.Gunvarrel.Function;
import com.tenko.test.SaveImageToDisk;

public class TestCommand extends Function {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf(".") + 1);

		try {
			Class <?> thing= Class.forName("net.minecraft.server." + version + ".NBTTagCompound");			
			Object o = thing.newInstance();
			SaveImageToDisk.setData(o, ImageIO.read(new URL("http://fc03.deviantart.net/fs28/f/2009/251/c/d/Happy_Cirno_Day_by_shingenjitsu.png").openStream()), (byte)0, ((Player)cs).getWorld().getName(), "map_" + (((Player)cs).getItemInHand().getDurability()+1));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
