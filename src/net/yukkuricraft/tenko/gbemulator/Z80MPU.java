package net.yukkuricraft.tenko.gbemulator;

import static net.yukkuricraft.tenko.gbemulator.BitConstants.*;

import org.bukkit.entity.Player;

public class Z80MPU extends Thread {
	
	private RAM ram;
	private ROM currentROM;
	private GPU gpu;
	private InputHandler input = new InputHandler();
	private SaveManager manager = new SaveManager();
	private Interpreter interpreter;
	
	// Used to be 114
	public static final int CYCLES_PER_LINE = 114; // (1048576 Hz/ 9198 Hz)
	private static final long nsPerFrame = (long) (1000000000 / 59.73D);
	protected final int[] F_ADD = new int[257 * 256];
	protected final int[] F_SUB = new int[257 * 256];
	protected final int[] F_INC = new int[256];
	protected final int[] F_DEC = new int[256];
	
	private long previousFrame = System.nanoTime();
	private long startFrame = 0;
	
	protected int scanline = 0; // GB uses Scanlines for interrupts... Weird.
	protected int cpuCycles = 0;
	protected int nextHBlank = CYCLES_PER_LINE; // Horizontal blanking
	protected int frameCount = 0;
	
	private int[] hRAM;
	
	protected boolean ime /*
						 * IME? Assuming something to do with interruption.
						 */= false;
	private boolean halt;
	private boolean joypadInputRecieved = false;
	private boolean threadAlive = false;
	
	// PC = Program Counter register
	// SP = Segment <something> register
	// According to htf.
	protected int aReg, bReg, cReg, dReg, eReg, fReg, hReg, lReg, pcReg, spReg;
	
	private boolean requestSave, requestLoad;
	private String uuid;
	
	public Z80MPU() {
		super("Z80MPU - No ROM Loaded");
	}
	
	public void save(Player plyr){
		uuid = plyr.getUniqueId().toString();
		requestSave = true;
	}
	
	public void load(Player plyr){
		uuid = plyr.getUniqueId().toString();
		requestLoad = true;
	}
	
	public RAM getRAM(){
		return ram;
	}
	
	public ROM getROM(){
		return currentROM;
	}
	
	public InputHandler getInputHandler(){
		return input;
	}
	
	public GPU getGPU(){
		return gpu;
	}
	
	public boolean isIME(){
		return ime;
	}
	
	public void loadROM(String path) throws Exception{
		currentROM = new ROM(path);
		currentROM.loadROM();
		this.setName("Z80MPU - " + currentROM.getTitle());
	}
	
	public void initialize(){
		pcReg = 0x0100;
		spReg = 0xFFFE;
		aReg = 0x01;
		bReg = 0xB0;
		cReg = 0x00;
		dReg = 0x13;
		eReg = 0x00;
		fReg = 0xD8;
		hReg = 0x01;
		lReg = 0x4D;
		
		ram = new RAM(this);
		gpu = new GPU();
		
		int[][] memory = new int[8][0x2000];
		memory[0] = currentROM.getDefaultROM(0);
		memory[1] = currentROM.getDefaultROM(1);
		memory[2] = currentROM.getROM(1, 0);
		memory[3] = currentROM.getROM(1, 1);
		memory[4] = new int[0x2000]; // VRAM
		memory[5] = currentROM.getRAM(0);
		if(memory[5] == null){
			memory[5] = new int[0x2000];
		}
		
		memory[6] = new int[0x2000];
		memory[7] = hRAM = new int[0x2000]; // HRAM
		
		ram.setRAMs(hRAM, memory[4]);
		ram.setBanks(memory);
		gpu.init(ram);
		
		generateFlagTable();
		
		ram.writeDefaults();
		
		interpreter = new Interpreter(this);
	}
	
	@Override
	public void run(){
		threadAlive = true;
		while(threadAlive){
			cycle();
		}
	}
	
	public ROM getCurrentROM(){
		return currentROM;
	}
	
	public void incrementCycleCount(int amount){
		this.cpuCycles += amount;
	}
	
	public void generateFlagTable(){
		int flag;
		for(int i = 0; i <= 256; i++){
			for(int j = 0; j <= 255; j++){
				int result = i + j;
				flag = 0;
				
				if((i & 0x0F) + (j & 0x0F) > 0x0F){
					flag |= HALF_CARRY;
				}
				
				if(result > 0xFF){
					result &= 0xFF;
					flag |= CARRY;
				}
				
				if(result == 0){
					flag |= ZERO;
				}
				
				F_ADD[(i << 8) | j] = flag;
				
				result = j - i;
				flag = SUBTRACT;
				if((j & 0x0F) < (i & 0x0F)){
					flag |= HALF_CARRY;
				}
				
				if(result < 0){
					result &= 0xFF;
					flag |= CARRY;
				}
				
				if(result == 0){
					flag |= ZERO;
				}
				
				F_SUB[(i << 8) | j] = flag;
			}
		}
		
		for(int i = 0; i <= 255; i++){
			flag = 0;
			if((i & 0x0F) == 0x0F){
				flag |= HALF_CARRY;
			}
			
			if(i == 255){
				flag |= ZERO;
			}
			
			F_INC[i] = flag;
			flag = SUBTRACT;
			
			if((i & 0x0F) == 0){
				flag |= HALF_CARRY;
			}
			
			if(i == 1){
				flag |= ZERO;
			}
			
			F_DEC[i] = flag;
		}
	}
	
	public void halt(){
		this.halt = true;
	}
	
	public void cycle(){
		if(requestSave){
			manager.saveROMState(this, uuid);
			requestSave = false;
			return;
		}
		
		if(requestLoad){
			manager.loadROMState(this, uuid);
			requestLoad = false;
			return;
		}
		
		// System.out.println("Cycle #" + this.cpuCycles + " - GPU Drawing BG");
		gpu.drawBackground();
		// System.out.println("Cycle #" + this.cpuCycles + " - Interrupts, Interpreter, and screen[] render");
		while(scanline <= 153){
			handleInterrupts();
			// do opcode handling aka rip in peaches
			interpreter.interpret();
			// actually write to the screen[] field. use GPU.
			gpu.render(nextHBlank - cpuCycles, scanline);
		}
		
		// System.out.println("Cycle #" + this.cpuCycles + " - Joypad");
		if(joypadInputRecieved){
			hRAM[0x1F0F] |= hRAM[0x1FFF] & BIT4;
			joypadInputRecieved = false;
		}
		
		hRAM[0x1F44] = 0;
		scanline = 0;
		gpu.resetWindowOffset();
		frameCount++;
		
		try{
			long waitNano = (previousFrame + nsPerFrame) - System.nanoTime();
			if(waitNano >= 1000000){
				Thread.sleep(waitNano / 1000000);
			}
		}catch (InterruptedException e){
		}
		
		previousFrame = System.nanoTime();
		
		if(frameCount == 100){
			double secPer100 = (System.nanoTime() - startFrame) / 1000000000D;
			System.out.println((secPer100 * 10) + " ms per frame (" + (167.42 / secPer100) + "% full speed)");
			frameCount = 0;
			startFrame = System.nanoTime();
		}
	}
	
	public int getNextHBlank(){
		return nextHBlank;
	}
	
	public int getCycleCount(){
		return cpuCycles;
	}
	
	public void setCycles(int i){
		cpuCycles = i;
	}
	
	public void setNextHBlank(int i){
		nextHBlank = i;
	}
	
	public void handleInterrupts(){
		// from 144 to 153 is v-blank period
		if(ime){
			switch(hRAM[0x1FFF] & hRAM[0x1F0F]){
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
				if(halt){
					pcReg++;
					halt = false;
				}
				ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg >> 8);
				ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg & 0x00FF);
				pcReg = 0x0040;
				break;
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
				if(halt){
					pcReg++;
					halt = false;
				}
				ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg >> 8);
				ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg & 0x00FF);
				pcReg = 0x0048;
				break;
			case 4:
			case 12:
			case 20:
			case 28:
				hRAM[0x1F0F] &= ~BIT2;
				ime = false;
				if(halt){
					pcReg++;
					halt = false;
				}
				ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg >> 8);
				ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg & 0x00FF);
				pcReg = 0x0050;
				break;
			case 8:
			case 24:
				hRAM[0x1F0F] &= ~BIT3;
				ime = false;
				if(halt){
					pcReg++;
					halt = false;
				}
				ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg >> 8);
				ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg & 0x00FF);
				pcReg = 0x0058;
				break;
			case 16:
				hRAM[0x1F0F] &= ~BIT4;
				ime = false;
				if(halt){
					pcReg++;
					halt = false;
				}
				ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg >> 8);
				ram.writeMemory(spReg = (spReg - 1) & 0xFFFF, pcReg & 0x00FF);
				pcReg = 0x0060;
				break;
			case 0:
				break;
			
			default:
				throw new AssertionError("Flag register has extra bits set");
			}
		}
	}
	
	public int incrementScanline(){
		return scanline += 1;
	}
}
