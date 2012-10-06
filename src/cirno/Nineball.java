package cirno;

import java.io.IOException;
import org.bukkit.plugin.java.JavaPlugin;


public class Nineball extends JavaPlugin {

	protected DataSaver ds;
	protected CommanderCirno cc;
	protected static ThreadGroup tg = new ThreadGroup("Cirno Group");
	protected static SlideshowThreadGroup sr = new SlideshowThreadGroup("SlideshowRenderers");

	public void onEnable(){
		try {
			ds = new DataSaver(this);
			ds.initializeFile();
			ds.setGlobalMaps();
			cc = new CommanderCirno(this);
			getCommand("map").setExecutor(cc);
			getCommand("restoremap").setExecutor(cc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onDisable(){
		sr.stopRunning();
		tg.interrupt();
	}
}
