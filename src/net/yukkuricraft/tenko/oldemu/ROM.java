package net.yukkuricraft.tenko.oldemu;

/* GameGuha
 * ROM Loader class
 */

import java.io.*;

public final class ROM{	
	//public static final int bankSize = 0x4000;
	//public static final int ramSize = 0x2000;
	public int numROMBanks;
	private int numRAMBanks;
	private int rom[][];
	private int ram[][];
	private String path;
	
	public ROM(String filename){
		this(new File(filename));
	}
	
	public ROM(File file){
		path = file.getPath();
		load(file);
		printTitle();
		getCartType(true);
		System.out.print("Color: ");
		if(isCGB())
			System.out.println("Yes");
		else
			System.out.println("No");
	}
	
	// Loads the ROM into memory
	private void load(File file){
		int firstBank[] = new int[0x2000];
		try{
			BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
			for(int i = 0; i < 0x2000; i++)
				firstBank[i] = buf.read();
			
			numROMBanks = getROMSize(firstBank, true);
			rom = new int[numROMBanks << 1][0x2000];
			
			for(int i = 0; i < 0x2000; i++)
				rom[0][i] = firstBank[i];
			
			for(int j = 1; j < rom.length; j++)
				for(int i = 0; i < 0x2000; i++)
					rom[j][i] = buf.read();
				
			buf.close();
			
			numRAMBanks = getRAMSize(true);
			if(numRAMBanks != 0)
				ram = new int[numRAMBanks][0x2000];
		}
		catch(Exception e){ e.printStackTrace(); }
	}
	
	public int[] getDefaultROM(int lsb)
	{
		return rom[lsb];
	}
	
	public int[] getROM(int bank, int lsb)
	{
		return rom[(bank << 1) | lsb];
	}
	
	public int[] getRAM(int bank)
	{
		if(ram == null)
			return null;
		return ram[bank];
	}
	
	// Returns true if the ROM is a Color GB
	public boolean isCGB(){
		if(rom[0][0x0143]==0){
			return false;
		}
		return true;
	}
	
	// Prints cartridge type, as specified in docs
	public int getCartType(boolean print){
		String out="";
		int mbc = -1;
		switch(rom[0][0x0147]){
			case 0x0: out="0 ROM ONLY"; mbc = 0; break;
			case 0x1: out="1 ROM+MBC1"; mbc = 1; break;
			case 0x2: out="2 ROM+MBC1+RAM"; mbc = 1; break;
			case 0x3: out="3 ROM+MBC1+RAM+BATT"; mbc = 1; break;
			case 0x5: out="5 ROM+MBC2"; mbc = 2; break;
			case 0x6: out="6 ROM+MBC2+BATTERY"; mbc = 2; break;
			case 0x8: out="8 ROM+RAM"; mbc = 0; break;
			case 0x9: out="9 ROM+RAM+BATTERY"; mbc = 0; break;
			case 0xB: out="B ROM+MMM01"; break;
			case 0xC: out="C ROM+MMM01+SRAM"; break;
			case 0xD: out="D ROM+MMM01+SRAM+BATT"; break;
			case 0x12: out="12 ROM+MBC3+RAM"; mbc = 3; break;
			case 0x13: out="13 ROM+MBC3+RAM+BATT"; mbc = 3; break;
			case 0x19: out="19 ROM+MBC5"; mbc = 5; break;
			case 0x1A: out="1A ROM+MBC5+RAM"; mbc = 5; break;
			case 0x1B: out="1B ROM+MBC5+RAM+BATT"; mbc = 5; break;
			case 0x1C: out="1C ROM+MBC5+RUMBLE"; mbc = 5; break;
			case 0x1D: out="1D ROM+MBC5+RUMBLE+SRAM"; mbc = 5; break;
			case 0x1E: out="1E ROM+MBC5+RUMBLE+SRAM+BATT"; mbc = 5; break;
			case 0x1F: out="1F Pocket Camera"; break;
			case 0xFD: out="FD Bandai TAMA5"; break;
			case 0xFE: out="FE Hudson HuC-3"; break;
		}
		if (print)
			System.out.println("Cartridge type: "+out);
		return mbc;
	}
	
	// Returns ROM size, as specified in docs
	public int getROMSize(int bank[], boolean print){
		String out="";
		int numBanks=0;
		switch(bank[0x148]){
			case 0x0: out="32KB (2 banks)"; numBanks = 2; break;
			case 0x1: out="64KB (4 bank)"; numBanks = 4; break;
			case 0x2: out="128KB (8 banks)"; numBanks = 8; break;
			case 0x3: out="256KB (16 banks)"; numBanks = 16; break;
			case 0x4: out="512KB (32 banks)"; numBanks = 32; break;
			case 0x5: out="1MB (64 banks)"; numBanks = 64; break;
			case 0x6: out="2MB (128 banks)"; numBanks = 128; break;
			case 0x7: out="4MB (256 banks)"; numBanks = 256; break;
			case 0x52: out="1.1MB (72 banks)"; numBanks = 72; break;
			case 0x53: out="1.2MB (80 banks)"; numBanks = 80; break;
			case 0x54: out="1.5MB (96 banks)"; numBanks = 96; break;
			
		}
		if (print)
			System.out.println("ROM Size: "+out);
		return numBanks;
	}

	public int getRAMSize(boolean print){
		String out="";
		int numBanks=0;
		switch(rom[0][0x149]){
		case 0x0: out="no RAM"; break;
		case 0x1: out="2KB RAM"; numBanks=1; break;
		case 0x2: out="8KB RAM"; numBanks=1; break;
		case 0x3: out="32KB RAM (8x4)"; numBanks=4; break;
		case 0x4: out="128KB RAM (8x16)"; numBanks=16; break;
	
		}
		if (print)
			System.out.println("RAM Size: "+out);
		return numBanks;
	}

	// Verify checksum of ROM, prints result
	// Returns true if checksum is valid, otherwise false
	public boolean verifyChecksum(){
		final int[] nintyBitmap = 
		{0xCE,0xED,0x66,0x66,0xCC,0x0D,0x00,0x0B,0x03,
		 0x73,0x00,0x83,0x00,0x0C,0x00,0x0D,0x00,0x08,
		 0x11,0x1F,0x88,0x89,0x00,0x0E,0xDC,0xCC,0x6E,
		 0xE6,0xDD,0xDD,0xD9,0x99,0xBB,0xBB,0x67,0x63,
		 0x6E,0x0E,0xEC,0xCC,0xDD,0xDC,0x99,0x9F,0xBB, 
		 0xB9,0x33,0x3E};
	  
		int ptr=0x0104;
		int x=0;
		
		while (ptr <= 0x0133){
			if(rom[0][ptr]!=nintyBitmap[ptr-0x0104])
				{
					System.out.println("Bitmap Invalid");
					System.out.println("Header Checksum Invalid");
					break;
				}
		ptr++;
		}
		if(ptr==0x0134)
		{
			//x=0:FOR i=0134h TO 014Ch:x=x-MEM[i]-1:NEXT
	  		for(;ptr<=0x014C;ptr++)
			{
				x=x-rom[0][ptr]-1; //checksum algorithm
			}
			if((x&0xFF) == rom[0][ptr]){
				System.out.println("Header Checksum Valid");
				return true;
			}
			// else
			System.out.println("Header Checksum Invalid");
		}
		return false;
	}
	
	// Prints game title
	public void printTitle(){
		System.out.printf("ROM Title: %s\n",getTitle());
	}
	
	public String getTitle(){
		StringBuffer sb = new StringBuffer();
		int ptr=0x0134;
		while(ptr <0x0143 && rom[0][ptr]!=0){
			sb.append((char)rom[0][ptr]);
			ptr++;
		}
		return sb.toString();
	}
	
	public String getPath(){
		return path;
	}
	public void writeInt(BufferedOutputStream out, int x) throws IOException{
		out.write(x>>24);
		out.write((x>>16)&0xFF);
		out.write((x>>8)&0xFF);
		out.write(x&0xFF);
	}
	public int readInt(BufferedInputStream in) throws IOException{
		int ret=0;
		ret = (in.read())<<24;
		ret |= (in.read())<<16;
		ret |= (in.read())<<8;
		ret |= in.read();
		return ret;
	}
}
