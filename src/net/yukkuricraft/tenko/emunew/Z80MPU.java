package net.yukkuricraft.tenko.emunew;

import static net.yukkuricraft.tenko.emunew.BitConstants.*;

public class Z80MPU {
	
	private RAM ram;
	private ROMManager manager = new ROMManager();
	private ROM currentROM;
	private GPU gpu;
	private InputHandler input;
	
	public static final int CYCLES_PER_LINE = 114; // (1048576 Hz/ 9198 Hz)
	private static final long nsPerFrame = (long) (1000000000 / 59.73);
	public final int[] F_ADD = new int[257 * 256];
	public final int[] F_SUB = new int[257 * 256];
	public final int[] F_INC = new int[256];
	public final int[] F_DEC = new int[256];
	
	private long previousFrame = System.nanoTime();
	private long startTime = System.nanoTime();
	
	private int scanline = 0; // GB uses Scanlines for interrupts... Weird.
	private int cpuCycles = 0;
	private int nextHBlank = CYCLES_PER_LINE; // Horizontal blanking
	
	private int[] hRAM;
	
	private short frameCount = 0; // This only goes up to 100 max.
	
	public boolean ime /*
						 * IME? Assuming something to do with interruption.
						 */= false;
	private boolean halt;
	private boolean joypadInputRecieved = false;
	
	// PC = Program Counter register
	// SP = Segment <something> register
	// According to htf.
	public int aReg, bReg, cReg, dReg, eReg, fReg, hReg, lReg, pcReg, spReg;
	
	public ROMManager getManager() {
		return manager;
	}
	
	public RAM getRAM() {
		return ram;
	}
	
	public InputHandler getInputHandler() {
		return input;
	}
	
	public boolean isIME() {
		return ime;
	}
	
	public void initialize() {
		ram = new RAM(this);
		gpu = new GPU(ram);
		
		int[][] memory = ram.getMemory();
		memory[0] = manager.getDefaultRom(0).getData();
		memory[1] = manager.getDefaultRom(1).getData();
		memory[2] = manager.getROM(1, 0).getData();
		memory[3] = manager.getROM(1, 1).getData();
		memory[4] = new int[0x2000]; // VRAM
		memory[5] = currentROM.getRAM()[0]; // TODO: Figure out how to make this
		// a single dimension.
		if (memory[5] == null) {
			memory[5] = new int[0x2000];
		}
		
		memory[6] = new int[0x2000];
		hRAM = memory[7] = new int[0x2000]; // HRAM
		
		ram.setRAMs(memory[4], memory[7]);
		
		generateFlagTable();
		
		ram.writeDefaults();
	}
	
	public int[] getRegisters() {
		return new int[] { pcReg, spReg, aReg, bReg, cReg, dReg, eReg, fReg, hReg, lReg };
	}
	
	public ROM getCurrentROM() {
		return currentROM;
	}
	
	public void incrementCycleCount(int amount) {
		this.cpuCycles += amount;
	}
	
	public void generateFlagTable() {
		int flag;
		for (int i = 0; i <= 256; i++) {
			for (int j = 0; j <= 255; j++) {
				int result = i + j;
				flag = 0;
				
				if ((i & 0x0F) + (j & 0x0F) > 0x0F) {
					flag |= HALF_CARRY;
				}
				
				if (result > 0xFF) {
					result &= 0xFF;
					flag |= CARRY;
				}
				
				if (result == 0) {
					flag |= ZERO;
				}
				
				F_ADD[(i << 8) | j] = flag;
				
				result = j - i;
				flag = SUBTRACT;
				if ((j & 0x0F) < (i & 0x0F)) {
					flag |= HALF_CARRY;
				}
				
				if (result < 0) {
					result &= 0xFF;
					flag |= CARRY;
				}
				
				if (result == 0) {
					flag |= ZERO;
				}
				
				F_SUB[(i << 8) | j] = flag;
			}
		}
		
		for (int i = 0; i <= 255; i++) {
			flag = 0;
			if ((i & 0x0F) == 0x0F) {
				flag |= HALF_CARRY;
			}
			
			if (i == 255) {
				flag |= ZERO;
			}
			
			F_INC[i] = flag;
			flag = SUBTRACT;
			
			if ((i & 0x0F) == 0) {
				flag |= HALF_CARRY;
			}
			
			if (i == 1) {
				flag |= ZERO;
			}
			
			F_DEC[i] = flag;
		}
	}
	
	public void halt() {
		this.halt = true;
	}
	
	public void cycle() {
		gpu.drawBackground();
		handleInterrupts();
		// do opcode handling aka rip in peaches
		// actually write to the screen[] field. use GPU.
		gpu.render(nextHBlank - cpuCycles, scanline);
		if (joypadInputRecieved) {
			hRAM[0x1F0F] |= hRAM[0x1FFF] & BIT4;
			joypadInputRecieved = false;
		}
		
		hRAM[0x1F44] = scanline = 0;
		gpu.resetWindowOffset();
		
		try {
			long waitNano = (previousFrame + nsPerFrame) - System.nanoTime();
			if (waitNano >= 1000000) {
				Thread.sleep(waitNano / 1000000);
			}
		} catch (InterruptedException e) {
		}
		
		previousFrame = System.nanoTime();
		
		if (frameCount >= 100) {
			double secPer100 = (System.nanoTime() - startTime) / 1000000000.0;
			System.out.println((secPer100 * 10) + " ms per frame (" + (167.42 / secPer100) + "% full speed)");
			frameCount = 0;
			startTime = System.nanoTime();
		}
	}
	
	public int getNextHBlank() {
		return nextHBlank;
	}
	
	public int getCycleCount() {
		return cpuCycles;
	}
	
	public void setCycles(int i) {
		cpuCycles = i;
	}
	
	public void setNextHBlank(int i) {
		nextHBlank = i;
	}
	
	public void handleInterrupts() {
		// from 144 to 153 is v-blank period
		while (scanline <= 153) {
			if (ime) {
				switch (hRAM[0x1FFF] & hRAM[0x1F0F]) {
				case 1:
				case 3:
				case 5:
				case 7:
				case 9:
				case 11:
				case 13:
				case 15:
				case 17:
				case 19:
				case 21:
				case 23:
				case 25:
				case 27:
				case 29:
				case 31:
					hRAM[0x1F0F] &= ~BIT0;
					ime = false;
					if (halt) {
						pcReg++;
						halt = false;
					}
					ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg >> 8);
					ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg & 0x00FF);
					pcReg = 0x0040;
					
				case 2:
				case 6:
				case 10:
				case 14:
				case 18:
				case 22:
				case 26:
				case 30:
					hRAM[0x1F0F] &= ~BIT1;
					ime = false;
					if (halt) {
						pcReg++;
						halt = false;
					}
					ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg >> 8);
					ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg & 0x00FF);
					pcReg = 0x0048;
					
				case 4:
				case 12:
				case 20:
				case 28:
					hRAM[0x1F0F] &= ~BIT2;
					ime = false;
					if (halt) {
						pcReg++;
						halt = false;
					}
					ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg >> 8);
					ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg & 0x00FF);
					pcReg = 0x0050;
					
				case 8:
				case 24:
					hRAM[0x1F0F] &= ~BIT3;
					ime = false;
					if (halt) {
						pcReg++;
						halt = false;
					}
					ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg >> 8);
					ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg & 0x00FF);
					pcReg = 0x0058;
					
				case 16:
					hRAM[0x1F0F] &= ~BIT4;
					ime = false;
					if (halt) {
						pcReg++;
						halt = false;
					}
					ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg >> 8);
					ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg & 0x00FF);
					pcReg = 0x0060;
					
				case 0:
					break;
				
				default:
					throw new AssertionError("Flag register has extra bits set");
				}
			}
		}
	}
	
	public int incrementScanline() {
		scanline++;
		return scanline;
	}
}
