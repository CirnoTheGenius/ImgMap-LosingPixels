package cirno;

import java.io.IOException;
import org.bukkit.plugin.java.JavaPlugin;


public class Nineball extends JavaPlugin {

	/*
	 * Good shall prevail. Evil sucks.
	 */
	
	private DataSaver ds;
	private CommanderCirno cc;
	private GeneralDaiyousei gd;
	public CirnoThreadGroup tg = new CirnoThreadGroup("Cirno Group");

	public void onEnable(){
		tg.stopRunning();
		try {
			saveDefaultConfig();
			ds = new DataSaver(this);
			ds.initializeFile();
			ds.setGlobalMaps();
			cc = new CommanderCirno(this);
			gd = new GeneralDaiyousei(this);
			getCommand("map").setExecutor(cc);
			getCommand("restoremap").setExecutor(cc);
			getCommand("imgmap").setExecutor(gd);
			getCommand("imap").setExecutor(gd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onDisable(){
		saveConfig();
		tg.stopRunning();
	}
}
