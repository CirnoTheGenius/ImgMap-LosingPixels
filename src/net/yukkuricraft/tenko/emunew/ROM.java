package net.yukkuricraft.tenko.emunew;

public class ROM {
	
	private int[] data;
	private int[][] ram;
	private int bankController = -1;
	private int bankCount = -1;
	private int bankControllerLower = 1;
	private int bankControllerSignature = 0;
	private int bankMode;
	private int ramSize = -1;
	private String title, path;
	
	public ROM(String path, int[] data) {
		this.data = data;
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setRAM(int[][] ram) {
		this.ram = ram;
	}
	
	public int[] getData() {
		return data;
	}
	
	public int getBankMode() {
		return bankMode;
	}
	
	public String getTitle() {
		if (title == null) {
			StringBuilder builder = new StringBuilder();
			int pointer = 0x0134;
			while (pointer < 0x0143 && data[pointer] != 0) {
				builder.append((char) data[pointer]);
				pointer++;
			}
			
			return title = builder.toString().trim();
		}
		
		return title;
	}
	
	public int getRAMSize() {
		if (ramSize == -1) {
			switch (data[0x149]) {
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
		}
		
		return ramSize;
	}
	
	public int getBankCount() {
		if (bankCount == -1) {
			switch (data[0x148]) {
			case 0x0:
				bankCount = 2;
				break;
			case 0x1:
				bankCount = 4;
				break;
			case 0x2:
				bankCount = 8;
				break;
			case 0x3:
				bankCount = 16;
				break;
			case 0x4:
				bankCount = 32;
				break;
			case 0x5:
				bankCount = 64;
				break;
			case 0x6:
				bankCount = 128;
				break;
			case 0x7:
				bankCount = 256;
				break;
			case 0x52:
				bankCount = 72;
				break;
			case 0x53:
				bankCount = 80;
				break;
			case 0x54:
				bankCount = 96;
				break;
			}
		}
		return bankCount;
	}
	
	public int getBankType() {
		if (bankController == -1) {
			switch (data[0x147]) {
			case 0x0:
				bankController = 0;
				break;
			case 0x1:
				bankController = 1;
				break;
			case 0x2:
				bankController = 1;
				break;
			case 0x3:
				bankController = 1;
				break;
			case 0x5:
				bankController = 2;
				break;
			case 0x6:
				bankController = 2;
				break;
			case 0x8:
				bankController = 0;
				break;
			case 0x9:
				bankController = 0;
				break;
			case 0x12:
				bankController = 3;
				break;
			case 0x13:
				bankController = 3;
				break;
			case 0x19:
				bankController = 5;
				break;
			case 0x1A:
				bankController = 5;
				break;
			case 0x1B:
				bankController = 5;
				break;
			case 0x1C:
				bankController = 5;
				break;
			case 0x1D:
				bankController = 5;
				break;
			case 0x1E:
				bankController = 5;
				break;
			}
		}
		
		return bankController;
	}
	
	public int getLBank() {
		return bankControllerLower;
	}
	
	public int getBankSig() {
		return bankControllerSignature;
	}
	
	public void setLBank(int lower) {
		this.bankControllerLower = lower;
	}
	
	public void setBankSig(int sig) {
		this.bankControllerSignature = sig;
	}
	
	public void setBankMode(int mode) {
		this.bankMode = mode;
	}
	
	public boolean isColor() {
		return data[0x0143] != 0;
	}
	
	public int[][] getRAM() {
		return ram;
	}
	
}
