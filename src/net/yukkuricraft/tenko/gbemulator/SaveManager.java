package net.yukkuricraft.tenko.gbemulator;

import java.io.*;

public class SaveManager {
	
	public void saveROMState(Z80MPU mpu, String prefix){
		File rom = mpu.getROM().getPath();
		File saveFile = new File(rom.getParentFile(), prefix + "_" + rom.getName().substring(0, rom.getName().lastIndexOf('.')) + ".sav");
		RAM ram = mpu.getRAM();
		try{
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
			
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile, false));
			out.write(mpu.pcReg >> 8);
			out.write(mpu.pcReg & 0xFF);
			out.write(mpu.spReg >> 8);
			out.write(mpu.spReg & 0xFF);
			out.write(mpu.aReg);
			out.write(mpu.bReg);
			out.write(mpu.cReg);
			out.write(mpu.dReg);
			out.write(mpu.eReg);
			out.write(mpu.fReg);
			out.write(mpu.hReg);
			out.write(mpu.lReg);
			out.write((mpu.ime ? 1 : 0));
			out.write(ram.workingROMBank);
			out.write(ram.workingRAMBank);
			writeInt(out, mpu.frameCount);
			writeInt(out, mpu.cpuCycles);
			writeInt(out, mpu.scanline);
			writeInt(out, mpu.nextHBlank);
			
			for(int i = 0; i < 4; i++){
				writeInt(out, ram.getBGColors()[i]);
			}
			
			for(int i = 0; i < 4; i++){
				writeInt(out, ram.getSPColor0()[i]);
			}
			
			for(int i = 0; i < 4; i++){
				writeInt(out, ram.getSPColor1()[i]);
			}
			
			for(int i = 0; i < 0x2000; i++){
				out.write(ram.getVRAM()[i]);
			}
			
			for(int i = 0; i < 0x2000; i++){
				out.write(ram.getHRAM()[i]);
			}
			
			for(int i = 0; i < 0x2000; i++){
				// No idea what bank 6 holds, to be honest.
				out.write(ram.getMemory()[6][i]);
			}
			
			int ramSize = mpu.getCurrentROM().getRAMSize();
			
			for(int i = 0; i < ramSize; i++){
				for(int k : mpu.getCurrentROM().getRAM(i)){
					out.write(k);
				}
			}
			
			out.flush();
			out.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void loadROMState(Z80MPU mpu, String prefix){
		File rom = mpu.getROM().getPath();
		File saveFile = new File(rom.getParentFile(), prefix + "_" + rom.getName().substring(0, rom.getName().lastIndexOf('.')) + ".sav");
		RAM ram = mpu.getRAM();
		try{
			if(!saveFile.exists()){
				return;
			}
			
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(saveFile));
			
			mpu.pcReg = (in.read()) << 8;
			mpu.pcReg |= in.read();
			mpu.spReg = (in.read()) << 8;
			mpu.spReg |= in.read();
			mpu.aReg = in.read();
			System.out.println(mpu.aReg);
			mpu.bReg = in.read();
			mpu.cReg = in.read();
			mpu.dReg = in.read();
			mpu.eReg = in.read();
			mpu.fReg = in.read();
			mpu.hReg = in.read();
			mpu.lReg = in.read();
			mpu.ime = (in.read() == 1);
			
			ram.workingROMBank = in.read();
			ram.workingRAMBank = in.read();
			ram.getMemory()[2] = mpu.getCurrentROM().getROM(ram.workingROMBank, 0);
			ram.getMemory()[3] = mpu.getCurrentROM().getROM(ram.workingROMBank, 1);
			ram.getMemory()[5] = mpu.getCurrentROM().getRAM(ram.workingRAMBank);
			mpu.getGPU().resetAll();
			mpu.frameCount = readInt(in);
			mpu.cpuCycles = readInt(in);
			mpu.scanline = readInt(in);
			mpu.nextHBlank = readInt(in);
			for(int i = 0; i < 4; i++){
				ram.getBGColors()[i] = readInt(in);
			}
			
			for(int i = 0; i < 4; i++){
				ram.getSPColor0()[i] = readInt(in);
			}
			
			for(int i = 0; i < 4; i++){
				ram.getSPColor1()[i] = readInt(in);
			}
			
			for(int i = 0; i < 0x2000; i++){
				ram.getVRAM()[i] = readInt(in);
			}
			
			for(int i = 0; i < 0x2000; i++){
				ram.getHRAM()[i] = readInt(in);
			}
			
			for(int i = 0; i < 0x2000; i++){
				// Still don't know what 6 holds.
				ram.getMemory()[6][i] = readInt(in);
			}
			
			int ramSize = mpu.getCurrentROM().getRAMSize();
			
			for(int i = 0; i < ramSize; i++){
				int[] currentNode = mpu.getCurrentROM().getRAM(i);
				for(int j = 0; j < currentNode.length; j++){
					currentNode[j] = in.read();
				}
			}
			
			in.close();
			
			mpu.stop();
			mpu.start();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void writeInt(OutputStream out, int i) throws IOException{
		out.write(i >> 24);
		out.write((i >> 16) & 0xFF);
		out.write((i >> 8) & 0xFF);
		out.write(i & 0xFF);
	}
	
	public int readInt(InputStream in) throws IOException{
		int ret = (in.read()) << 24;
		ret |= (in.read()) << 16;
		ret |= (in.read()) << 8;
		ret |= in.read();
		return ret;
	}
}
