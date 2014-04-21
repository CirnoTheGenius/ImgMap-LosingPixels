package net.yukkuricraft.tenko.emunew;

import java.io.*;

public class ROMManager {
	
	private ROM[] roms;
	private int romCount = 0;
	
	public ROM loadROM(File file) {
		int[] romData = new int[0x2000];
		
		try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file))) {
			for (int i = 0; i < 0x2000; i++) {
				romData[i] = stream.read();
			}
			stream.close();
			
			ROM rom = new ROM(file.getPath(), romData);
			if (romData[0x149] != 0) {
				int[][] ram = new int[rom.getBankCount()][0x2000];
				rom.setRAM(ram);
			}
			
			roms[romCount] = rom;
			romCount++;
			return rom;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ROM getDefaultRom(int index) {
		return roms[index];
	}
	
	// lsb == little significant bit
	public ROM getROM(int bank, int lsb) {
		return roms[(bank << 1) | lsb];
	}
	
}
