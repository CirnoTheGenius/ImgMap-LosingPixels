package cirno;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.map.MapView;

public class DataSaver {

	Nineball root;
	boolean FileSafeForUse;

	public DataSaver(Nineball r){
		try {
			root = r;
			initializeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initializeFile() throws IOException{
		File f = new File(root.getDataFolder().getAbsoluteFile(), "\\MapData.list");
		File folder = new File(root.getDataFolder().getAbsoluteFile().getAbsolutePath());
		if(!folder.exists()){
			folder.mkdir();
		}
		if(!f.exists()){
			f.createNewFile();
		}
		FileSafeForUse = true;
	}

	public void setGlobalMaps(){
		new Thread(){
			public void run(){
				for(short i=0; i < 65536; i++){
					if(FileSafeForUse == false){
						break;
					}
					MapView map = Bukkit.getServer().getMap(i);
					String url = getMapData(i);
					if(url != null && !url.isEmpty()){
						try { map.removeRenderer(map.getRenderers().get(0)); } catch(Exception e){}
						map.addRenderer(new ImgRenderer(url));
					}
				}
			}
		}.start();
	}

	public void setMapData(int id, String url){
		try {
			boolean found = false;
			//WSTFGL. Tactical failure inbound! Prepare for blindness!
			ArrayList<String> tempArray = new ArrayList<String>();
			BufferedReader eyes = new BufferedReader(new FileReader(root.getDataFolder().getAbsoluteFile() + "\\MapData.list"));
			String l;

			while((l = eyes.readLine()) != null){
				String[] line = l.split(" > ");
				if(line[0].equalsIgnoreCase(String.valueOf(id))){
					found = true;
					tempArray.add(line[0] + " > " + url);
				} else {
					tempArray.add(l);
				}
			}
			
			if(!found){
				tempArray.add(id + " > " + url);
			}
			eyes.close();
			FileSafeForUse = false;
			BufferedWriter typewriter = new BufferedWriter(new FileWriter(root.getDataFolder().getAbsoluteFile() + "\\MapData.list", false));
			for(String line : tempArray){
				typewriter.write(line);
			}
			typewriter.close();
			initializeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public String getMapData(int id){
		if(FileSafeForUse){
			try {
				BufferedReader eyes = new BufferedReader(new FileReader(root.getDataFolder().getAbsoluteFile() + "\\MapData.list"));
				String l;
				while((l = eyes.readLine()) != null){
					if(l.startsWith(String.valueOf(id))){
						return l.split(" > ")[1].trim();
					}
				}
				return null;
			} catch (IOException e){
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
}
