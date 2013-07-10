package com.tenko.Gunvarrel.Parts;

import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.tenko.Gunvarrel.Function;
import com.tenko.test.SaveImageToDisk;
import com.tenko.utils.MapDataUtils;

public class TestCommand extends Function {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		try {
			SaveImageToDisk.setData(ImageIO.read(new URL("http://fc03.deviantart.net/fs28/f/2009/251/c/d/Happy_Cirno_Day_by_shingenjitsu.png").openStream()), (byte)0, "world", "map_" + args[0]);
			MapDataUtils.add(Integer.valueOf(args[0]), "http://fc03.deviantart.net/fs28/f/2009/251/c/d/Happy_Cirno_Day_by_shingenjitsu.png");
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

}
