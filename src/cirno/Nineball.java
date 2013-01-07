package cirno;

import org.bukkit.plugin.java.JavaPlugin;

import Threading.CirnoThreadGroup;

import CommandListeners.CommanderCirno;
import CommandListeners.GeneralDaiyousei;

public class Nineball extends JavaPlugin {
	public DataSaver ds;
	private CommanderCirno cc;
	private GeneralDaiyousei gd;
	private CirnoThreadGroup tg = new CirnoThreadGroup("Cirno Group");

	public void onEnable() {
		tg.stopRunning();
		saveDefaultConfig();
		this.ds = new DataSaver(this);
		this.ds.initializeFile();
		this.ds.setGlobalMaps();
		this.cc = new CommanderCirno(this);
		this.gd = new GeneralDaiyousei(this);
		getCommand("map").setExecutor(this.cc);
		getCommand("restoremap").setExecutor(this.cc);
		getCommand("imgmap").setExecutor(this.gd);
		getCommand("imap").setExecutor(this.gd);
	}

	public void onDisable(){
		saveConfig();
		tg.stopRunning();
	}
	
	public CirnoThreadGroup getCThreadGroup(){
		return tg;
	}
}