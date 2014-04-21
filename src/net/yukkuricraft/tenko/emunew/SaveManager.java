package net.yukkuricraft.tenko.emunew;

public class SaveManager {
	
	public void saveROMState(Z80MPU mpu) {
		// TODO: Make this work.
		// int[] registers = mpu.getRegisters();
		// ROM rom = mpu.getCurrentROM();
		//
		// String path = rom.getPath(), savepath;
		// if (path.lastIndexOf('.') > -1) {
		// savepath = path.substring(0, path.lastIndexOf('.')) + ".sav";
		// } else {
		// savepath = path + ".sav";
		// }
		//
		// try (BufferedOutputStream stream = new BufferedOutputStream(new
		// FileOutputStream(new File(savepath)))) {
		// stream.write(registers[0] >> 8);
		// stream.write(registers[0] & 0xFF);
		// stream.write(registers[1] >> 8);
		// stream.write(registers[1] & 0xFF);
		// for (int i = 2; i < registers.length; i++) {
		// stream.write(registers[i]);
		// }
		// stream.write(mpu.isIME() ? 1 : 0);
		//
		// } catch (IOException e) {
		//
		// }
	}
	
	public void loadROMState(Z80MPU mpu) {
		// TODO: Make this work.
	}
}
