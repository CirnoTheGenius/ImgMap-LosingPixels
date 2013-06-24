package com.tenko.Gunvarrel.Parts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.tenko.Gunvarrel.Function;
import com.tenko.rendering.AnimatedRenderer;

public class AniCommand extends Function {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		validateInput(cs);
		this.getData().getMap().addRenderer(new AnimatedRenderer(args[0]));
		result = "[ImgMap] Rendering " + args[0];
		this.successful = true;
		return end(cs);
	}

}
