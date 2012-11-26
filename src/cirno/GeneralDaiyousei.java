package cirno;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GeneralDaiyousei implements CommandExecutor {

	Nineball cirno;
	DataSaver ds;

	public GeneralDaiyousei(Nineball c){
		cirno = c;
		ds = new DataSaver(c);
	}


	//This block of code. It makes my eyes bleed and suffer from pain.
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("imap") || command.getName().equalsIgnoreCase("imgmap")){
			if(args.length == 0){
				sender.sendMessage(ChatColor.GREEN + "ImgMap by Cirno");
				sender.sendMessage(ChatColor.GREEN + "Version 1.8");
				return true;
			}
			if(sender.hasPermission("imgmap.admin") || sender.isOp()){
				if(args[0].equalsIgnoreCase("reload")){
					cirno.saveConfig();
					cirno.reloadConfig();
					sender.sendMessage(ChatColor.GREEN + "[ImgMap] Reloaded configuration!");
					return true;
				} else if(args[0].equalsIgnoreCase("config")){
					if(args.length == 1){
						sender.sendMessage(ChatColor.RED + "[ImgMap] Requires another argument!");
						sender.sendMessage(ChatColor.RED + "[ImgMap] Avaliable arguments: MapsDefaultPermament, perm");
						return true;
					} else if(args[1].equalsIgnoreCase("MapsDefaultPermament")){
						if(args.length == 2){
							sender.sendMessage(ChatColor.GREEN + "[ImgMap] Maps are defaulted to be permament? : " + cirno.getConfig().getBoolean("MapsDefaultPermament"));
							return true;
						} else {
							if(args[2].equalsIgnoreCase("yes") || args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("y")){
								sender.sendMessage(ChatColor.GREEN + "[ImgMap] Set MapsDefaultPermament to true");
								cirno.getConfig().set("MapsDefaultPermament", true);
								cirno.saveConfig();
								return true;
							} else if(args[2].equalsIgnoreCase("no") || args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("n")){
								sender.sendMessage(ChatColor.GREEN + "[ImgMap] Set MapsDefaultPermament to false");
								cirno.getConfig().set("MapsDefaultPermament", false);
								cirno.saveConfig();
								return true;
							}
						}
					} else if(args[1].equalsIgnoreCase("perm")){
						if(args.length == 2){
							sender.sendMessage(ChatColor.GREEN + "[ImgMap] Permament Map IDs");
							ArrayList<Integer> maps = ds.countMapsArray();
							if(maps.size() > 0){
								for(int i=0; i < (ds.countMaps() < 10 ? ds.countMaps() : 10); i++){
									sender.sendMessage(ChatColor.YELLOW + new String(maps.get(i) + " : " + ds.getMapData(i)));
								}
							} else {
								sender.sendMessage(ChatColor.RED + "No permament maps set!");
							}
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.RED + "[ImgMap] Invalid argument!");
						sender.sendMessage(ChatColor.RED + "[ImgMap] Avaliable arguments: MapsDefaultPermament, perm");
						return true;
					}
				} /*Dummy command.*/ else if(args[0].equalsIgnoreCase("cirno")){
					cirno.getServer().broadcastMessage(ChatColor.BLUE + "[Cirno] Eye'm the strongest!");
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "[ImgMap] Invalid argument!");
					sender.sendMessage(ChatColor.RED + "[ImgMap] Avaliable arguments: config");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "[ImgMap] You don't have permission!");
				return true;
			}
		}
		return false;
	}
}
