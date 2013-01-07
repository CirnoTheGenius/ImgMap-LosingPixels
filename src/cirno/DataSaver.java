package cirno;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.map.MapView;

import Rendering.ImgRenderer;
import Threading.CirnoThread;


//Dear god, the "wonderful" file API for MapData.
public class DataSaver {

	Nineball Main;
	boolean FileSafeForUse;

	public DataSaver(Nineball main){
		this.Main = main;
		initializeFile();
	}

	public void initializeFile(){
		try {
			File f = new File(Main.getDataFolder().getAbsoluteFile(), "/MapData.list");
			File folder = new File(Main.getDataFolder().getAbsoluteFile().getAbsolutePath());
			if (!folder.exists()) {
				folder.mkdir();
			}
			if (!f.exists()) {
				f.createNewFile();
			}
			this.FileSafeForUse = true;	  
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void setGlobalMaps() {
		Main.getCThreadGroup().addToList(new CirnoThread("Global Map Updator"){
			@SuppressWarnings("deprecation")
			public void run(){
				HashMap<Long, String> data = DataSaver.this.getMapDataAsArray();
				for (long i = 0L; i < 65536L; i += 1L) {
					if (!DataSaver.this.FileSafeForUse || !this.running){
						break;
					}
					MapView map = Bukkit.getServer().getMap((short)i);
					String url = null;
					if (data.containsKey(i)){
						url = DataSaver.this.getMapData((int)i);
						if ((url != null) && (!url.isEmpty())) {
							map.getRenderers().clear();
							map.addRenderer(new ImgRenderer(url, Main));
						}
					}
				}
				stop();
			}
		});
		Main.getCThreadGroup().start();
	}

	public void setMapData(int id, String url){
		try {
			boolean found = false;

			ArrayList<String> tempArray = new ArrayList<String>();
			BufferedReader eyes = new BufferedReader(new FileReader(Main.getDataFolder().getAbsoluteFile() + "/MapData.list"));
			String l;
			while ((l = eyes.readLine()) != null){
				String[] line = l.split(" > ");
				if (line[0].equalsIgnoreCase(String.valueOf(id))) {
					found = true;
					tempArray.add(line[0] + " > " + url);
				} else {
					tempArray.add(l);
				}
			}
			if (!found){
				tempArray.add(id + " > " + url);
			}
			
			eyes.close();
			this.FileSafeForUse = false;
			BufferedWriter typewriter = new BufferedWriter(new FileWriter(Main.getDataFolder().getAbsoluteFile() + "/MapData.list", false));
			for (String line : tempArray) {
				typewriter.write(line + "\n");
			}
			typewriter.close();
			initializeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void delMapData(int id)
	{
		try {
			ArrayList<String> tempArray = new ArrayList<String>();
			BufferedReader eyes = new BufferedReader(new FileReader(Main.getDataFolder().getAbsoluteFile() + "/MapData.list"));
			String l;
			while ((l = eyes.readLine()) != null)
			{
				String[] line = l.split(" > ");
				if (!line[0].equalsIgnoreCase(String.valueOf(id))) {
					tempArray.add(l);
				}
			}

			eyes.close();
			this.FileSafeForUse = false;
			BufferedWriter typewriter = new BufferedWriter(new FileWriter(Main.getDataFolder().getAbsoluteFile() + "/MapData.list", false));
			for (String line : tempArray) {
				typewriter.write(line);
			}
			typewriter.close();
			initializeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getMapData(int id)
	{
		if (this.FileSafeForUse) {
			try {
				BufferedReader eyes = new BufferedReader(new FileReader(Main.getDataFolder().getAbsoluteFile() + "/MapData.list"));
				String l;
				while ((l = eyes.readLine()) != null){
					if (l.startsWith(String.valueOf(id))) {
						eyes.close();
						return l.split(" > ")[1].trim();
					}
				}
				eyes.close();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	public int countMaps() {
		int i = 0;
		if (this.FileSafeForUse) {
			try {
				BufferedReader eyes = new BufferedReader(new FileReader(Main.getDataFolder().getAbsoluteFile() + "/MapData.list"));
				while (eyes.readLine() != null){
					i++;
				}
				eyes.close();
				return i;
			} catch (IOException e) {
				e.printStackTrace();
				return 0;
			}
		}
		return i;
	}

	public ArrayList<Integer> countMapsArray() {
		ArrayList<Integer> i = new ArrayList<Integer>();
		if (this.FileSafeForUse) {
			try {
				BufferedReader eyes = new BufferedReader(new FileReader(Main.getDataFolder().getAbsoluteFile() + "/MapData.list"));
				String l;
				while ((l = eyes.readLine()) != null)
				{
					i.add(Integer.valueOf(l.split(" > ")[0].trim()));
				}
				eyes.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return i;
	}

	public HashMap<Long, String> getMapDataAsArray() {
		if (this.FileSafeForUse) {
			HashMap<Long, String> data = new HashMap<Long, String>();
			try {
				BufferedReader eyes = new BufferedReader(new FileReader(Main.getDataFolder().getAbsoluteFile() + "/MapData.list"));
				String l;
				while ((l = eyes.readLine()) != null)
				{
					data.put(Long.valueOf(l.split(" > ")[0].trim()), l.split(" > ")[1].trim());
				}
				eyes.close();
				return data;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}