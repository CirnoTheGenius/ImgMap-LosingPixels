package com.tenko.cmdexe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

import com.google.common.io.Files;
import com.tenko.ImgMap;
import com.tenko.rendering.SlideshowRenderer;
import com.tenko.utils.DataUtils;
import com.tenko.utils.PlayerUtils;
import com.tenko.utils.URLUtils;

public class SlideshowCommandExe implements CommandExe {
	
	public SlideshowCommandExe(CommandSender cs, String[] args) throws IOException{
		Execute(cs, args);
	}
	
	@Override
	public void Execute(CommandSender cs, String[] args) throws IOException {
		Player thePlayer = PlayerUtils.resolveToPlayer(cs);
		ItemStack equipped = thePlayer.getItemInHand();

  // Check if waitTime is valid and give a message if not.
  float waitTime;
  try {
    waitTime = Float.valueOf(args[0]);
  }
  catch (NumberFormatException e) {
    waitTime=-1;
  }
  if (waitTime<=0) {
    cs.sendMessage( ChatColor.RED+
        "[ImgMap] Invalid value for <time>. Must be a positive number.");
    return;
  }

  // read the URLs and check if the slideshow is to be permanent
  boolean isPermanent=false;
  ArrayList<String> urls = new ArrayList<String>();
		for(String arg : args){
			if(!arg.startsWith("-")){
     urls.add(new String(arg));
			}
   else {
     if (arg.equals("-p"))
       isPermanent=true;
   }
		}
  // remove waitTime
  urls.remove(0);

		//Yow. This may cause a lot of bandwith issues and lag.
		for(String url : urls){
			if(!URLUtils.compatibleImage(url)){
    // Tell the user which image is not compatible.
				cs.sendMessage(ChatColor.RED + "[ImgMap] The image: '"+url+"' is not compatible!");
				return;
			}
		}
		
		MapView viewport = Bukkit.getServer().getMap(equipped.getDurability());
		
		for(MapRenderer mr : viewport.getRenderers()){
			viewport.removeRenderer(mr);
		}
		
		viewport.setScale(Scale.FARTHEST);
		viewport.addRenderer(new SlideshowRenderer(urls.toArray(new String[urls.size()]), waitTime));
		
		StringBuffer listOfURLs = new StringBuffer();
  Iterator iter = urls.iterator();
  while (iter.hasNext()) {
			listOfURLs.append(iter.next());
   if (iter.hasNext())
     listOfURLs.append(", ");
		}
		
		cs.sendMessage(ChatColor.GREEN +"[ImgMap] Rendering "+ listOfURLs.toString());
		
		if(isPermanent){
			File slideshowFile = ImgMap.getSlideshowFile(equipped.getDurability());

			Files.touch(slideshowFile);

   String[] lines=new String[1+urls.size()];
   lines[0]=String.valueOf(waitTime);
   int i=1;
   for(String url : urls) {
     lines[i]=url;
     ++i;
   }
   DataUtils.writeArray(slideshowFile, lines);

			cs.sendMessage(ChatColor.BLUE + "[ImgMap] Successfully saved this map's data!");
		}
	}

	@Override
	public String getCommand() {
		return "slideshow";
	}
}
