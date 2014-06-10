package net.yukkuricraft.tenko.gbemulator;

import java.io.*;

import net.yukkuricraft.tenko.ImgMap;

public class ROM {

	private int[][] rom;
	private int[][] ram;
	private int numROMBanks;
	private int numRAMBanks;
	private String title;
	private File path;

	public ROM(String path) {
		this.path = new File(ImgMap.getLocalROMsDir(), path);
	}

	public File getPath(){
		return path;
	}

	public int[] getDefaultROM(int lsb){
		return rom[lsb];
	}

	public int[] getROM(int bank, int lsb){
		return rom[(bank << 1) | lsb];
	}

	public int[] getRAM(int bank){
		if(ram == null){
			return null;
		}
		return ram[bank];
	}

	public void loadROM() throws Exception {
		int[] firstBank = new int[0x2000];
		BufferedInputStream buf = new BufferedInputStream(new FileInputStream(path));
		for(int i = 0; i < 0x2000; i++){
			firstBank[i] = buf.read();
		}

		numROMBanks = getSize(firstBank);
		rom = new int[numROMBanks << 1][0x2000];

		for(int i = 0; i < 0x2000; i++){
			rom[0][i] = firstBank[i];
		}

		for(int j = 1; j < rom.length; j++){
			for(int i = 0; i < 0x2000; i++){
				rom[j][i] = buf.read();
			}
		}

		buf.close();

		numRAMBanks = getRAMSize();
		if(numRAMBanks != 0){
			ram = new int[numRAMBanks][0x2000];
		}
	}

	public String getTitle(){
		if(title == null){
			StringBuilder builder = new StringBuilder();
			int pointer = 0x0134;
			while(pointer < 0x0143 && rom[0][pointer] != 0){
				builder.append((char) rom[0][pointer]);
				pointer++;
			}

			return title = builder.toString().trim();
		}

		return title;
	}

	public int getNumROMBanks(){
		return numROMBanks;
	}

	public int getRAMSize(){
		int ramSize = 0;
		switch(rom[0][0x149]){
		case 0x0:
			ramSize = 0;
			break;
		case 0x1:
			ramSize = 1;
			break;
		case 0x2:
			ramSize = 1;
			break;
		case 0x3:
			ramSize = 4;
			break;
		case 0x4:
			ramSize = 16;
			break;
		}
		return ramSize;
	}

	public int getSize(int bank[]){
		int romSize = 0;
		switch(bank[0x148]){
		case 0x0:
			romSize = 2;
			break;
		case 0x1:
			romSize = 4;
			break;
		case 0x2:
			romSize = 8;
			break;
		case 0x3:
			romSize = 16;
			break;
		case 0x4:
			romSize = 32;
			break;
		case 0x5:
			romSize = 64;
			break;
		case 0x6:
			romSize = 128;
			break;
		case 0x7:
			romSize = 256;
			break;
		case 0x52:
			romSize = 72;
			break;
		case 0x53:
			romSize = 80;
			break;
		case 0x54:
			romSize = 96;
			break;
		}
		return romSize;
	}

	public int getCartType(){
		int mbc = -1;
		switch(rom[0][0x0147]){
		case 0x0:
			mbc = 0;
			break;
		case 0x1:
			mbc = 1;
			break;
		case 0x2:
			mbc = 1;
			break;
		case 0x3:
			mbc = 1;
			break;
		case 0x5:
			mbc = 2;
			break;
		case 0x6:
			mbc = 2;
			break;
		case 0x8:
			mbc = 0;
			break;
		case 0x9:
			mbc = 0;
			break;
		case 0x12:
			mbc = 3;
			break;
		case 0x13:
			mbc = 3;
			break;
		case 0x19:
			mbc = 5;
			break;
		case 0x1A:
			mbc = 5;
			break;
		case 0x1B:
			mbc = 5;
			break;
		case 0x1C:
			mbc = 5;
			break;
		case 0x1D:
			mbc = 5;
			break;
		case 0x1E:
			mbc = 5;
			break;
		}
		return mbc;
	}

	public boolean isColor(){
		return rom[0][0x0143] != 0;
	}

}
