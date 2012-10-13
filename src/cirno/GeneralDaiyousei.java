package cirno;

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

	@Override
	//This block of code. It makes my eyes bleed and suffer from pain.
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("imap") || command.getName().equalsIgnoreCase("imgmap")){
			if(args.length <= 0){
				sender.sendMessage(ChatColor.GREEN + "ImgMap by Cirno");
				sender.sendMessage(ChatColor.GREEN + "Version 1.8");
				return true;
			}
			if(sender.hasPermission("imgmap.admin")){
				if(args[0].equalsIgnoreCase("reload")){
					cirno.saveConfig();
					cirno.reloadConfig();
					sender.sendMessage(ChatColor.GREEN + "[ImgMap] Reloaded configuration!");
				} else if(args[0].equalsIgnoreCase("config")){
					if(args.length <= 1){
						sender.sendMessage(ChatColor.RED + "[ImgMap] Requires another argument!");
						sender.sendMessage(ChatColor.RED + "[ImgMap] Avaliable arguments: LoadImgOnStartup, MapsDefaultPermament, perm");
						return true;
					} else if(args[1].equalsIgnoreCase("LoadImgOnStartup")){
						if(args.length == 2){
							sender.sendMessage(ChatColor.GREEN + "[ImgMap] Load Images on Startup? : " + cirno.getConfig().getBoolean("LoadImgOnStartup"));
						} else {
							if(args[2].equalsIgnoreCase("yes") || args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("y")){
								sender.sendMessage(ChatColor.GREEN + "[ImgMap] Set LoadImgOnStartup to true");
								cirno.getConfig().set("LoadImgOnStartup", true);
								cirno.saveConfig();
							} else if(args[2].equalsIgnoreCase("no") || args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("n")){
								sender.sendMessage(ChatColor.GREEN + "[ImgMap] Set LoadImgOnStartup to false");
								cirno.getConfig().set("LoadImgOnStartup", false);
								cirno.saveConfig();
							}
						}
					} else if(args[1].equalsIgnoreCase("MapsDefaultPermament")){
						if(args.length == 2){
							sender.sendMessage(ChatColor.GREEN + "[ImgMap] Maps are defaulted to be permament? : " + cirno.getConfig().getBoolean("MapsDefaultPermament"));
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
							Integer[] maps = cirno.getConfig().getList("PermMaps").toArray(new Integer[0]);;
							for(int i=0; i < (maps.length < 10 ? maps.length : 10); i++){
								if(maps.length > 0){
									sender.sendMessage(maps[i] + " : " + ds.getMapData(i));
								} else {
									sender.sendMessage(ChatColor.RED + "No permament maps set!");
								}
							}
							return true;
						}
					} /*Dummy command.*/ else if(args[1].equalsIgnoreCase("cirno")){
						cirno.getServer().broadcastMessage(ChatColor.BLUE + "[Cirno] Eye'm the strongest!");
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "[ImgMap] You don't have permission!");
			}
		}
		return false;
	}
}
