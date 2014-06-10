package net.yukkuricraft.tenko.gbemulator;

import static net.yukkuricraft.tenko.gbemulator.BitConstants.*;

class RAM {
	
	private int[][] banks;
	private final int[] dirtyTiles1 = new int[16];
	private final int[] dirtyTiles2 = new int[16];
	private final int[] color = { 0xFFFFFFFF, 0xFFC0C0C0, 0xFF404040, 0xFF000000 };
	private final int[] colorBG = new int[4];
	private final int[] colorSP0 = new int[4];
	private final int[] colorSP1 = new int[4];
	
	private int[] hRAM;
	private int[] vRAM;
	
	private ROM currentROM;
	protected int workingROMBank;
	protected int workingRAMBank;
	
	private int mbcSig;
	private int mbcMode;
	private int mbcLower;
	
	private Z80MPU cpu;
	
	public RAM(Z80MPU mpu) {
		this.cpu = mpu;
		this.currentROM = mpu.getCurrentROM();
	}
	
	public Z80MPU getMPU(){
		return cpu;
	}
	
	public int[] getHRAM(){
		return hRAM;
	}
	
	public int[] getVRAM(){
		return vRAM;
	}
	
	public int[] getBGColors(){
		return colorBG;
	}
	
	public int[] getSPColor0(){
		return colorSP0;
	}
	
	public int[] getSPColor1(){
		return colorSP1;
	}
	
	public int[] getDirtyTiles1(){
		return dirtyTiles1;
	}
	
	public int[] getDirtyTiles2(){
		return dirtyTiles2;
	}
	
	public void setRAMs(int[]... rams){
		this.hRAM = rams[0];
		this.vRAM = rams[1];
	}
	
	public void setBanks(int[][] banks){
		this.banks = banks;
	}
	
	public int[][] getMemory(){
		return banks;
	}
	
	public int readMemory(int address){
		return banks[address >> 13][address & 0x1FFF];
	}
	
	public int writeMemory(int address, int value){
		switch(address >> 8){
		
		case 0x00:
		case 0x01:
		case 0x02:
		case 0x03:
		case 0x04:
		case 0x05:
		case 0x06:
		case 0x07:
		case 0x08:
		case 0x09:
		case 0x0A:
		case 0x0B:
		case 0x0C:
		case 0x0D:
		case 0x0E:
		case 0x0F:
		case 0x10:
		case 0x11:
		case 0x12:
		case 0x13:
		case 0x14:
		case 0x15:
		case 0x16:
		case 0x17:
		case 0x18:
		case 0x19:
		case 0x1A:
		case 0x1B:
		case 0x1C:
		case 0x1D:
		case 0x1E:
		case 0x1F:
			return banks[0][address & 0x1FFF];
			
		case 0x20:
		case 0x21:
		case 0x22:
		case 0x23:
		case 0x24:
		case 0x25:
		case 0x26:
		case 0x27:
		case 0x28:
		case 0x29:
		case 0x2A:
		case 0x2B:
		case 0x2C:
		case 0x2D:
		case 0x2E:
		case 0x2F:
		case 0x30:
		case 0x31:
		case 0x32:
		case 0x33:
		case 0x34:
		case 0x35:
		case 0x36:
		case 0x37:
		case 0x38:
		case 0x39:
		case 0x3A:
		case 0x3B:
		case 0x3C:
		case 0x3D:
		case 0x3E:
		case 0x3F:
			switch(currentROM.getCartType()){
			case 0:
				break;
			
			case 1:
				int prev = this.mbcLower;
				this.mbcLower = (value & 0x1F);
				if(this.mbcLower == 0){
					this.mbcLower = 1;
				}
				if((this.mbcSig | this.mbcLower) < currentROM.getNumROMBanks()){
					workingROMBank = (this.mbcSig | this.mbcLower);
					banks[2] = currentROM.getROM(workingROMBank, 0);
					banks[3] = currentROM.getROM(workingROMBank, 1);
				}else{
					this.mbcLower = prev;
				}
				break;
			
			case 2:
				if((address & 0x0100) == 0){
					break;
				}
				prev = workingROMBank;
				workingROMBank = (value & 0x0F);
				if(workingROMBank == 0){
					workingROMBank = 1;
				}
				if(workingROMBank < currentROM.getNumROMBanks()){
					banks[2] = currentROM.getROM(workingROMBank, 0);
					banks[3] = currentROM.getROM(workingROMBank, 1);
				}else{
					workingROMBank = prev;
				}
				break;
			
			case 3:
				prev = workingROMBank;
				workingROMBank = (value & 0x7F);
				if(workingROMBank == 0){
					workingROMBank = 1;
				}
				if(workingROMBank < currentROM.getNumROMBanks()){
					banks[2] = currentROM.getROM(workingROMBank, 0);
					banks[3] = currentROM.getROM(workingROMBank, 1);
				}else{
					workingROMBank = prev;
				}
				break;
			
			case 5:
				// ...
				break;
			
			default:
				throw new AssertionError("Illegal memory access.");
			}
			return banks[1][address & 0x1FFF];
		case 0x40:
		case 0x41:
		case 0x42:
		case 0x43:
		case 0x44:
		case 0x45:
		case 0x46:
		case 0x47:
		case 0x48:
		case 0x49:
		case 0x4A:
		case 0x4B:
		case 0x4C:
		case 0x4D:
		case 0x4E:
		case 0x4F:
		case 0x50:
		case 0x51:
		case 0x52:
		case 0x53:
		case 0x54:
		case 0x55:
		case 0x56:
		case 0x57:
		case 0x58:
		case 0x59:
		case 0x5A:
		case 0x5B:
		case 0x5C:
		case 0x5D:
		case 0x5E:
		case 0x5F:
			switch(currentROM.getCartType()){
			
			case 0:
				break;
			
			case 1:
				if(mbcMode == 0){
					int previousLower = mbcSig;
					mbcSig = (value & 0x03) << 5;
					if((mbcSig | mbcLower) < currentROM.getNumROMBanks()){
						workingROMBank = mbcSig | mbcLower;
						banks[2] = currentROM.getROM(workingROMBank, 0);
						banks[3] = currentROM.getROM(workingROMBank, 1);
					}else{
						mbcSig = previousLower;
					}
				}else{
					if((value & 0x03) < currentROM.getRAMSize()){
						workingRAMBank = value & 0x03;
						banks[5] = currentROM.getRAM(workingRAMBank);
					}
				}
				break;
			
			case 2:
				break;
			
			case 3:
				if((value & 0x03) < currentROM.getRAMSize()){
					workingRAMBank = value & 0x03;
					banks[5] = currentROM.getRAM(workingRAMBank);
				}
				break;
			
			case 5:
				// wat.
				break;
			
			default:
				throw new AssertionError("Invalid bank type.");
			}
			
			return banks[2][address & 0x1FFF];
		case 0x60:
		case 0x61:
		case 0x62:
		case 0x63:
		case 0x64:
		case 0x65:
		case 0x66:
		case 0x67:
		case 0x68:
		case 0x69:
		case 0x6A:
		case 0x6B:
		case 0x6C:
		case 0x6D:
		case 0x6E:
		case 0x6F:
		case 0x70:
		case 0x71:
		case 0x72:
		case 0x73:
		case 0x74:
		case 0x75:
		case 0x76:
		case 0x77:
		case 0x78:
		case 0x79:
		case 0x7A:
		case 0x7B:
		case 0x7C:
		case 0x7D:
		case 0x7E:
		case 0x7F:
			switch(currentROM.getCartType()){
			
			case 0:
				break;
			
			case 1:
				mbcMode = (value & 0x01);
				if(mbcMode == 0){
					workingRAMBank = 0;
					banks[5] = currentROM.getRAM(0);
				}else{
					mbcSig = 0;
					workingROMBank = mbcLower;
					banks[2] = currentROM.getROM(workingROMBank, 0);
					banks[3] = currentROM.getROM(workingROMBank, 1);
				}
				break;
			
			case 2:
			case 3:
				break;
			
			case 5:
				// wat.
				break;
			default:
				throw new AssertionError("Invalid bank type.");
				
			}
			return banks[3][address & 0x1FFF];
		case 0x80:
			dirtyTiles1[0] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x81:
			dirtyTiles1[1] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x82:
			dirtyTiles1[2] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x83:
			dirtyTiles1[3] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x84:
			dirtyTiles1[4] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x85:
			dirtyTiles1[5] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x86:
			dirtyTiles1[6] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x87:
			dirtyTiles1[7] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x88:
			dirtyTiles1[8] |= 1 << ((address & 0x00F0) >> 4);
			dirtyTiles2[0] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x89:
			dirtyTiles1[9] |= 1 << ((address & 0x00F0) >> 4);
			dirtyTiles2[1] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x8A:
			dirtyTiles1[10] |= 1 << ((address & 0x00F0) >> 4);
			dirtyTiles2[2] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x8B:
			dirtyTiles1[11] |= 1 << ((address & 0x00F0) >> 4);
			dirtyTiles2[3] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x8C:
			dirtyTiles1[12] |= 1 << ((address & 0x00F0) >> 4);
			dirtyTiles2[4] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x8D:
			dirtyTiles1[13] |= 1 << ((address & 0x00F0) >> 4);
			dirtyTiles2[5] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x8E:
			dirtyTiles1[14] |= 1 << ((address & 0x00F0) >> 4);
			dirtyTiles2[6] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x8F:
			dirtyTiles1[15] |= 1 << ((address & 0x00F0) >> 4);
			dirtyTiles2[7] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x90:
			dirtyTiles2[8] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x91:
			dirtyTiles2[9] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x92:
			dirtyTiles2[10] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x93:
			dirtyTiles2[11] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x94:
			dirtyTiles2[12] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x95:
			dirtyTiles2[13] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x96:
			dirtyTiles2[14] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x97:
			dirtyTiles2[15] |= 1 << ((address & 0x00F0) >> 4);
			return banks[4][address & 0x1FFF] = value;
		case 0x98:
		case 0x99:
		case 0x9A:
		case 0x9B:
		case 0x9C:
		case 0x9D:
		case 0x9E:
		case 0x9F:
			return banks[4][address & 0x1FFF] = value;
			
		case 0xA0:
		case 0xA1:
		case 0xA2:
		case 0xA3:
		case 0xA4:
		case 0xA5:
		case 0xA6:
		case 0xA7:
		case 0xA8:
		case 0xA9:
		case 0xAA:
		case 0xAB:
		case 0xAC:
		case 0xAD:
		case 0xAE:
		case 0xAF:
		case 0xB0:
		case 0xB1:
		case 0xB2:
		case 0xB3:
		case 0xB4:
		case 0xB5:
		case 0xB6:
		case 0xB7:
		case 0xB8:
		case 0xB9:
		case 0xBA:
		case 0xBB:
		case 0xBC:
		case 0xBD:
		case 0xBE:
		case 0xBF:
			return banks[5][address & 0x1FFF] = value;
			
		case 0xC0:
		case 0xC1:
		case 0xC2:
		case 0xC3:
		case 0xC4:
		case 0xC5:
		case 0xC6:
		case 0xC7:
		case 0xC8:
		case 0xC9:
		case 0xCA:
		case 0xCB:
		case 0xCC:
		case 0xCD:
		case 0xCE:
		case 0xCF:
		case 0xD0:
		case 0xD1:
		case 0xD2:
		case 0xD3:
		case 0xD4:
		case 0xD5:
		case 0xD6:
		case 0xD7:
		case 0xD8:
		case 0xD9:
		case 0xDA:
		case 0xDB:
		case 0xDC:
		case 0xDD:
		case 0xDE:
		case 0xDF:
			return banks[6][address & 0x1FFF] = value;
			
		case 0xE0:
		case 0xE1:
		case 0xE2:
		case 0xE3:
		case 0xE4:
		case 0xE5:
		case 0xE6:
		case 0xE7:
		case 0xE8:
		case 0xE9:
		case 0xEA:
		case 0xEB:
		case 0xEC:
		case 0xED:
		case 0xEE:
		case 0xEF:
		case 0xF0:
		case 0xF1:
		case 0xF2:
		case 0xF3:
		case 0xF4:
		case 0xF5:
		case 0xF6:
		case 0xF7:
		case 0xF8:
		case 0xF9:
		case 0xFA:
		case 0xFB:
		case 0xFC:
		case 0xFD:
		case 0xFE:
			return banks[7][address & 0x1FFF] = value;
			
		case 0xFF:
			switch(address){
			
			case 0xFF00:
				return cpu.getInputHandler().handle(banks, value);
			case 0xFF01:
				return banks[7][0x1F01];
			case 0xFF02:
				// Don't use serial port communication. There's no point.
				// if((value & BIT7) != 0 && (value & BIT0) != 0){
				// serial = 10;
				// }
				return banks[7][0x1F02] = value;
			case 0xFF04:
				return banks[7][0x1F04] = value;
			case 0xFF05:
				return banks[7][0x1F05] = value;
			case 0xFF06:
				return banks[7][0x1F06] = value;
			case 0xFF07:
				return banks[7][0x1F07] = value;
			case 0xFF0F:
				return banks[7][0x1F0F] = (value & 0x1F);
			case 0xFF10:
				return banks[7][0x1F10] = value;
			case 0xFF11:
				return banks[7][0x1F11] = value;
			case 0xFF12:
				return banks[7][0x1F12] = value;
				// Note: No sound because MC doesn't support sound.
			case 0xFF13: // Channel 1 Frequency Lo (W)
				return banks[7][0x1F13] = value;
			case 0xFF14: // Channel 1 Frequency Hi (R/W)
				return banks[7][0x1F14] = value;
			case 0xFF16: // Channel 2 Sound Length/Wave Pattern Duty (W)
				return banks[7][0x1F16] = value;
			case 0xFF17: // Channel 2 Volume Envelope (R/W)
				return banks[7][0x1F17] = value;
			case 0xFF18: // Channel 2 Frequency Lo (W)
				return banks[7][0x1F18] = value;
			case 0xFF19: // Channel 2 Frequency Hi (R/W)
				return banks[7][0x1F19] = value;
			case 0xFF1A:
				return banks[7][0x1F1A] = value;
			case 0xFF1B:
				return banks[7][0x1F1B] = value;
			case 0xFF1C:
				return banks[7][0x1F1C] = value;
			case 0XFF1D:
				return banks[7][0x1F1D] = value;
			case 0xFF1E:
				return banks[7][0x1F1E] = value;
			case 0xFF20:
				return banks[7][0x1F20] = value;
			case 0xFF21: // Channel 4 Volume Envelope
				return banks[7][0x1F21] = value;
			case 0xFF22:
				return banks[7][0x1F22] = value;
			case 0xFF23:
				return banks[7][0x1F23] = value;
			case 0xFF25: // Stereo select
				return banks[7][0x1F25] = value;
				
			case 0xFF30:
			case 0xFF31:
			case 0xFF32:
			case 0xFF33:
			case 0xFF34:
			case 0xFF35:
			case 0xFF36:
			case 0xFF37:
			case 0xFF38:
			case 0xFF39:
			case 0xFF3A:
			case 0xFF3B:
			case 0xFF3C:
			case 0xFF3D:
			case 0xFF3E:
			case 0xFF3F:
				return banks[7][address & 0x1FFF] = value;
			case 0xFF40:
				return banks[7][0x1F40] = value;
			case 0xFF41:
				return banks[7][0x1F41] = ((value & 0xF8) | (banks[7][0x1F41] & 0x07));
			case 0xFF42:
				return banks[7][0x1F42] = value;
			case 0xFF43:
				return banks[7][0x1F43] = value;
			case 0xFF44:
				return banks[7][0x1F44]; // "Writing into this register resets it."
											// So, effectivly read-only?
			case 0xFF45:
				return banks[7][0x1F45] = value;
			case 0xFF46:
				int start = value << 8;
				for(int i = 0; i < 0xA0; i++){
					banks[7][0x1E00 | i] = readMemory(start | i);
				}
				return banks[7][0x1F46] = value;
			case 0xFF47:
				colorBG[0] = color[value & (BIT1 | BIT0)];
				colorBG[1] = color[(value & (BIT3 | BIT2)) >> 2];
				colorBG[2] = color[(value & (BIT5 | BIT4)) >> 4];
				colorBG[3] = color[value >> 6];
				return banks[7][0x1F47] = value;
			case 0xFF48:
				colorSP0[1] = color[(value & (BIT3 | BIT2)) >> 2];
				colorSP0[2] = color[(value & (BIT5 | BIT4)) >> 4];
				colorSP0[3] = color[value >> 6];
				return banks[7][0x1F48] = value;
			case 0xFF49:
				colorSP1[1] = color[(value & (BIT3 | BIT2)) >> 2];
				colorSP1[2] = color[(value & (BIT5 | BIT4)) >> 4];
				colorSP1[3] = color[value >> 6];
				return banks[7][0x1F49] = value;
			case 0xFF4A:
				return banks[7][0x1F4A] = value;
			case 0xFF4B:
				return banks[7][0x1F4B] = value;
			case 0xFFFF:
				return banks[7][0x1FFF] = value;
			default:
				return (banks[7][address & 0x1FFF] = value);
			}
			
		default:
			throw new AssertionError("Illegal memory access.");
		}
	}
	
	public void writeDefaults(){
		// Set default values on boot
		writeMemory(0xFF05, 0x00); // TIMA
		writeMemory(0xFF06, 0x00); // TMA
		writeMemory(0xFF07, 0x00); // TAC
		writeMemory(0xFF10, 0x80); // NR10
		writeMemory(0xFF11, 0xBF); // NR11
		writeMemory(0xFF12, 0xF3); // NR12
		writeMemory(0xFF14, 0xBF); // NR14
		writeMemory(0xFF16, 0x3F); // NR21
		writeMemory(0xFF17, 0x00); // NR22
		writeMemory(0xFF19, 0xBF); // NR24
		writeMemory(0xFF1A, 0x7F); // NR30
		writeMemory(0xFF1B, 0xFF); // NR31
		writeMemory(0xFF1C, 0x9F); // NR32
		writeMemory(0xFF1E, 0xBF); // NR33
		writeMemory(0xFF20, 0xFF); // NR41
		writeMemory(0xFF21, 0x00); // NR42
		writeMemory(0xFF22, 0x00); // NR43
		writeMemory(0xFF23, 0xBF); // NR30
		writeMemory(0xFF24, 0x77); // NR50
		writeMemory(0xFF25, 0xF3); // NR51
		writeMemory(0xFF26, 0xF1); // NR52
		writeMemory(0xFF40, 0x91); // LCDC
		writeMemory(0xFF42, 0x00); // SCY
		writeMemory(0xFF43, 0x00); // SCX
		writeMemory(0xFF45, 0x00); // LYC
		writeMemory(0xFF47, 0xFC); // BGP
		writeMemory(0xFF48, 0xFF); // OBP0
		writeMemory(0xFF49, 0xFF); // OBP1
		writeMemory(0xFF4A, 0x00); // WY
		writeMemory(0xFF4B, 0x00); // WX
		writeMemory(0xFFFF, 0x00); // IE
	}
	
}