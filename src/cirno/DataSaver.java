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

	Nineball cirno;
	boolean FileSafeForUse;

	public DataSaver(Nineball r){
		try {
			cirno = r;
			initializeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initializeFile() throws IOException{
		File f = new File(cirno.getDataFolder().getAbsoluteFile(), "/MapData.list");
		File folder = new File(cirno.getDataFolder().getAbsoluteFile().getAbsolutePath());
		if(!folder.exists()){
			folder.mkdir();
		}
		if(!f.exists()){
			f.createNewFile();
		}
		FileSafeForUse = true;
	}

	public void setGlobalMaps(){
		new CirnoThread(cirno.tg, "Global Map Updator"){
			public void run(){
				for(long i=0; i < 65536; i++){
					if(running){
						if(FileSafeForUse == false){
							this.stopRunning();
							break;
						}
						MapView map = Bukkit.getServer().getMap((short)i);
						String url = getMapData((int)i);
						if(url != null && !url.isEmpty()){
							map.getRenderers().clear();
							map.addRenderer(new ImgRenderer(url, cirno));
						}
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
			BufferedReader eyes = new BufferedReader(new FileReader(cirno.getDataFolder().getAbsoluteFile() + "/MapData.list"));
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
			BufferedWriter typewriter = new BufferedWriter(new FileWriter(cirno.getDataFolder().getAbsoluteFile() + "/MapData.list", false));
			for(String line : tempArray){
				typewriter.write(line + "\n");
			}
			typewriter.close();
			initializeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void delMapData(int id){
		try {
			ArrayList<String> tempArray = new ArrayList<String>();
			BufferedReader eyes = new BufferedReader(new FileReader(cirno.getDataFolder().getAbsoluteFile() + "/MapData.list"));
			String l;

			while((l = eyes.readLine()) != null){
				String[] line = l.split(" > ");
				if(!line[0].equalsIgnoreCase(String.valueOf(id))){
					tempArray.add(l);
				}
			}

			eyes.close();
			FileSafeForUse = false;
			BufferedWriter typewriter = new BufferedWriter(new FileWriter(cirno.getDataFolder().getAbsoluteFile() + "/MapData.list", false));
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
				BufferedReader eyes = new BufferedReader(new FileReader(cirno.getDataFolder().getAbsoluteFile() + "/MapData.list"));
				String l;
				while((l = eyes.readLine()) != null){
					if(l.startsWith(String.valueOf(id))){
						eyes.close();
						return l.split(" > ")[1].trim();
					}
				}
				eyes.close();
				return null;
			} catch (IOException e){
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
}
