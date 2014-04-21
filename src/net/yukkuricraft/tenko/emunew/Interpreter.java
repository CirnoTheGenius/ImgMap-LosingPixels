package net.yukkuricraft.tenko.emunew;

import static net.yukkuricraft.tenko.emunew.BitConstants.*;

public class Interpreter {
	
	private Z80MPU mpu;
	private RAM ram;
	private int val;
	private int memval;
	private int index;
	
	public Interpreter(Z80MPU mpu) {
		this.mpu = mpu;
		this.ram = mpu.getRAM();
	}
	
	public void interpret() {
		switch (ram.readMemory(mpu.pcReg++)) {
		case 0x00: // NOP
			mpu.incrementCycleCount(1);
			break;
		
		case 0x01: // LD BC,nn
			mpu.incrementCycleCount(3);
			mpu.cReg = ram.readMemory(mpu.pcReg++);
			mpu.bReg = ram.readMemory(mpu.pcReg++);
			break;
		
		case 0x02: // LD (BC),A
			mpu.incrementCycleCount(2);
			ram.writeMemory((mpu.bReg << 8) | mpu.cReg, mpu.aReg);
			break;
		
		case 0x03: // INC BC
			mpu.incrementCycleCount(2);
			mpu.cReg++;
			if (mpu.cReg > 0xFF) {
				mpu.cReg = 0;
				mpu.bReg = (mpu.bReg + 1) & 0xFF;
			}
			break;
		
		case 0x04: // INC B
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_INC[mpu.bReg] | (mpu.fReg & CARRY);
			mpu.bReg = (mpu.bReg + 1) & 0xFF;
			break;
		
		case 0x05: // DEC B
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_DEC[mpu.bReg] | (mpu.fReg & CARRY);
			mpu.bReg = (mpu.bReg - 1) & 0xFF;
			break;
		
		case 0x06: // LD B,n
			mpu.incrementCycleCount(2);
			mpu.bReg = ram.readMemory(mpu.pcReg++);
			break;
		
		case 0x07: // RLCA
			mpu.incrementCycleCount(1);
			mpu.fReg = (mpu.aReg & BIT7) >> 3;
			mpu.aReg = ((mpu.aReg << 1) | (mpu.aReg >> 7)) & 0xFF;
			break;
		
		case 0x08: // LD (nn),mpu.spReg
			mpu.incrementCycleCount(5);
			index = ram.readMemory(mpu.pcReg++) | (ram.readMemory(mpu.pcReg++) << 8);
			ram.writeMemory(index, mpu.spReg & 0x00FF);
			ram.writeMemory(index + 1, mpu.spReg >> 8);
			break;
		
		case 0x09: // ADD HL,BC
			mpu.incrementCycleCount(2);
			mpu.fReg &= ZERO;
			mpu.lReg += mpu.cReg;
			mpu.hReg += mpu.bReg + (mpu.lReg >> 8);
			mpu.lReg &= 0xFF;
			if (mpu.hReg > 0xFF) {
				mpu.hReg &= 0xFF;
				mpu.fReg |= CARRY;
			}
			break;
		
		case 0x0A: // LD A,(BC)
			mpu.incrementCycleCount(2);
			mpu.aReg = ram.readMemory((mpu.bReg << 8) | mpu.cReg);
			break;
		
		case 0x0B: // DEC BC
			mpu.incrementCycleCount(2);
			mpu.cReg--;
			if (mpu.cReg < 0) {
				mpu.cReg = 0xFF;
				mpu.bReg = (mpu.bReg - 1) & 0xFF;
			}
			break;
		
		case 0x0C: // INC C
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_INC[mpu.cReg] | (mpu.fReg & CARRY);
			mpu.cReg = (mpu.cReg + 1) & 0xFF;
			break;
		
		case 0x0D: // DEC C
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_DEC[mpu.cReg] | (mpu.fReg & CARRY);
			mpu.cReg = (mpu.cReg - 1) & 0xFF;
			break;
		
		case 0x0E: // LD C,n
			mpu.incrementCycleCount(2);
			mpu.cReg = ram.readMemory(mpu.pcReg++);
			break;
		
		case 0x0F: // RRCA
			mpu.incrementCycleCount(1);
			mpu.fReg = (mpu.aReg & BIT0) << 4;
			mpu.aReg = ((mpu.aReg >> 1) | (mpu.aReg << 7)) & 0xFF;
			break;
		
		case 0x10: // STOP
			mpu.incrementCycleCount(1);
			// mpu.pcReg++? (not all assemblers insert 0x00)
			break;
		
		case 0x11: // LD DE,nn
			mpu.incrementCycleCount(3);
			mpu.eReg = ram.readMemory(mpu.pcReg++);
			mpu.dReg = ram.readMemory(mpu.pcReg++);
			break;
		
		case 0x12: // LD (DE),A
			mpu.incrementCycleCount(2);
			ram.writeMemory((mpu.dReg << 8) | mpu.eReg, mpu.aReg);
			break;
		
		case 0x13: // INC DE
			mpu.incrementCycleCount(2);
			mpu.eReg++;
			if (mpu.eReg > 0xFF) {
				mpu.eReg = 0;
				mpu.dReg = (mpu.dReg + 1) & 0xFF;
			}
			break;
		
		case 0x14: // INC D
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_INC[mpu.dReg] | (mpu.fReg & CARRY);
			mpu.dReg = (mpu.dReg + 1) & 0xFF;
			break;
		
		case 0x15: // DEC D
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_DEC[mpu.dReg] | (mpu.fReg & CARRY);
			mpu.dReg = (mpu.dReg - 1) & 0xFF;
			break;
		
		case 0x16: // LD D,n
			mpu.incrementCycleCount(2);
			mpu.dReg = ram.readMemory(mpu.pcReg++);
			break;
		
		case 0x17: // RLA
			mpu.incrementCycleCount(1);
			val = (mpu.fReg & CARRY) >> 4;
			mpu.fReg = (mpu.aReg & BIT7) >> 3;
			mpu.aReg = ((mpu.aReg << 1) | val) & 0xFF;
			break;
		
		case 0x18: // JR n
			mpu.incrementCycleCount(3);
			mpu.pcReg += (byte) ram.readMemory(mpu.pcReg) + 1; // signed
																// immediate
			break;
		
		case 0x19: // ADD HL,DE
			mpu.incrementCycleCount(2);
			mpu.fReg &= ZERO;
			mpu.lReg += mpu.eReg;
			mpu.hReg += mpu.dReg + (mpu.lReg >> 8);
			mpu.lReg &= 0xFF;
			if (mpu.hReg > 0xFF) {
				mpu.hReg &= 0xFF;
				mpu.fReg |= CARRY;
			}
			break;
		
		case 0x1A: // LD A,(DE)
			mpu.incrementCycleCount(2);
			mpu.aReg = ram.readMemory((mpu.dReg << 8) | mpu.eReg);
			break;
		
		case 0x1B: // DEC DE
			mpu.incrementCycleCount(2);
			mpu.eReg--;
			if (mpu.eReg < 0) {
				mpu.eReg = 0xFF;
				mpu.dReg = (mpu.dReg - 1) & 0xFF;
			}
			break;
		
		case 0x1C: // INC E
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_INC[mpu.eReg] | (mpu.fReg & CARRY);
			mpu.eReg = (mpu.eReg + 1) & 0xFF;
			break;
		
		case 0x1D: // DEC E
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_DEC[mpu.eReg] | (mpu.fReg & CARRY);
			mpu.eReg = (mpu.eReg - 1) & 0xFF;
			break;
		
		case 0x1E: // LD E,n
			mpu.incrementCycleCount(2);
			mpu.eReg = ram.readMemory(mpu.pcReg++);
			break;
		
		case 0x1F: // RRA
			mpu.incrementCycleCount(1);
			val = (mpu.fReg & CARRY) << 3;
			mpu.fReg = (mpu.aReg & BIT0) << 4;
			mpu.aReg = (mpu.aReg >> 1) | val;
			break;
		
		case 0x20: // JR NZ,n
			if ((mpu.fReg & ZERO) == 0) {
				mpu.incrementCycleCount(3);
				mpu.pcReg += (byte) ram.readMemory(mpu.pcReg) + 1; // signed
																	// immediate
			} else {
				mpu.incrementCycleCount(2);
				mpu.pcReg++;
			}
			break;
		
		case 0x21: // LD HL,nn
			mpu.incrementCycleCount(3);
			mpu.lReg = ram.readMemory(mpu.pcReg++);
			mpu.hReg = ram.readMemory(mpu.pcReg++);
			break;
		
		case 0x22: // LDI (HL),A
			mpu.incrementCycleCount(2);
			index = (mpu.hReg << 8) | mpu.lReg;
			ram.writeMemory(index, mpu.aReg);
			index = (index + 1) & 0xFFFF;
			mpu.hReg = index >> 8;
			mpu.lReg = index & 0x00FF;
			break;
		
		case 0x23: // INC HL
			mpu.incrementCycleCount(2);
			mpu.lReg++;
			if (mpu.lReg > 0xFF) {
				mpu.lReg = 0;
				mpu.hReg = (mpu.hReg + 1) & 0xFF;
			}
			break;
		
		case 0x24: // INC H
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_INC[mpu.hReg] | (mpu.fReg & CARRY);
			mpu.hReg = (mpu.hReg + 1) & 0xFF;
			break;
		
		case 0x25: // DEC H
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_DEC[mpu.hReg] | (mpu.fReg & CARRY);
			mpu.hReg = (mpu.hReg - 1) & 0xFF;
			break;
		
		case 0x26: // LD H,n
			mpu.incrementCycleCount(2);
			mpu.hReg = ram.readMemory(mpu.pcReg++);
			break;
		
		case 0x27: // DAA
			mpu.incrementCycleCount(1);
			if ((mpu.fReg & SUBTRACT) != 0) {
				if ((mpu.aReg & 0x0F) > 0x09 || (mpu.fReg & HALF_CARRY) != 0) {
					mpu.aReg -= 0x06;
				}
				if ((mpu.aReg & 0xF0) > 0x90 || (mpu.fReg & CARRY) != 0) {
					mpu.aReg -= 0x60;
					mpu.fReg |= CARRY;
				} else {
					mpu.fReg &= ~CARRY;
				}
			} else {
				if ((mpu.aReg & 0x0F) > 0x09 || (mpu.fReg & HALF_CARRY) != 0) {
					mpu.aReg += 0x06;
				}
				if ((mpu.aReg & 0xF0) > 0x90 || (mpu.fReg & CARRY) != 0) {
					mpu.aReg += 0x60;
					mpu.fReg |= CARRY;
				} else {
					mpu.fReg &= ~CARRY;
				}
			}
			mpu.fReg &= ~HALF_CARRY;
			mpu.aReg &= 0xFF;
			if (mpu.aReg == 0) {
				mpu.fReg |= ZERO;
			} else {
				mpu.fReg &= ~ZERO;
			}
			break;
		
		case 0x28: // JR Z,n
			if ((mpu.fReg & ZERO) != 0) {
				mpu.incrementCycleCount(3);
				mpu.pcReg += (byte) ram.readMemory(mpu.pcReg) + 1; // signed
																	// immediate
			} else {
				mpu.incrementCycleCount(2);
				mpu.pcReg++;
			}
			break;
		
		case 0x29: // ADD HL,HL
			mpu.incrementCycleCount(2);
			mpu.fReg &= ZERO;
			mpu.lReg += mpu.lReg;
			mpu.hReg += mpu.hReg + (mpu.lReg >> 8);
			mpu.lReg &= 0xFF;
			if (mpu.hReg > 0xFF) {
				mpu.hReg &= 0xFF;
				mpu.fReg |= CARRY;
			}
			break;
		
		case 0x2A: // LDI A,(HL)
			mpu.incrementCycleCount(2);
			index = (mpu.hReg << 8) | mpu.lReg;
			mpu.aReg = ram.readMemory(index);
			index = (index + 1) & 0xFFFF;
			mpu.hReg = index >> 8;
			mpu.lReg = index & 0x00FF;
			break;
		
		case 0x2B: // DEC HL
			mpu.incrementCycleCount(2);
			mpu.lReg--;
			if (mpu.lReg < 0) {
				mpu.lReg = 0xFF;
				mpu.hReg = (mpu.hReg - 1) & 0xFF;
			}
			break;
		
		case 0x2C: // INC L
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_INC[mpu.lReg] | (mpu.fReg & CARRY);
			mpu.lReg = (mpu.lReg + 1) & 0xFF;
			break;
		
		case 0x2D: // DEC L
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_DEC[mpu.lReg] | (mpu.fReg & CARRY);
			mpu.lReg = (mpu.lReg - 1) & 0xFF;
			break;
		
		case 0x2E: // LD L,n
			mpu.incrementCycleCount(2);
			mpu.lReg = ram.readMemory(mpu.pcReg++);
			break;
		
		case 0x2F: // CPL
			mpu.incrementCycleCount(1);
			mpu.fReg |= (SUBTRACT | HALF_CARRY);
			mpu.aReg ^= 0xFF; // faster than (~mpu.aReg) & 0xFF
			break;
		
		case 0x30: // JR NC,n
			if ((mpu.fReg & CARRY) == 0) {
				mpu.incrementCycleCount(3);
				mpu.pcReg += (byte) ram.readMemory(mpu.pcReg) + 1; // signed
																	// immediate
			} else {
				mpu.incrementCycleCount(2);
				mpu.pcReg++;
			}
			break;
		
		case 0x31: // LD mpu.spReg,nn
			mpu.incrementCycleCount(3);
			mpu.spReg = ram.readMemory(mpu.pcReg++) | (ram.readMemory(mpu.pcReg++) << 8);
			break;
		
		case 0x32: // LDD (HL),A
			mpu.incrementCycleCount(2);
			index = (mpu.hReg << 8) | mpu.lReg;
			ram.writeMemory(index, mpu.aReg);
			index = (index - 1) & 0xFFFF;
			mpu.hReg = index >> 8;
			mpu.lReg = index & 0x00FF;
			break;
		
		case 0x33: // INC mpu.spReg
			mpu.incrementCycleCount(2);
			mpu.spReg = (mpu.spReg + 1) & 0xFFFF;
			break;
		
		case 0x34: // INC (HL)
			mpu.incrementCycleCount(3);
			index = (mpu.hReg << 8) | mpu.lReg;
			memval = ram.readMemory(index);
			mpu.fReg = mpu.F_INC[memval] | (mpu.fReg & CARRY);
			ram.writeMemory(index, (memval + 1) & 0xFF);
			break;
		
		case 0x35: // DEC (HL)
			mpu.incrementCycleCount(3);
			index = (mpu.hReg << 8) | mpu.lReg;
			memval = ram.readMemory(index);
			mpu.fReg = mpu.F_DEC[memval] | (mpu.fReg & CARRY);
			ram.writeMemory(index, (memval - 1) & 0xFF);
			break;
		
		case 0x36: // LD (HL),n
			mpu.incrementCycleCount(3);
			ram.writeMemory((mpu.hReg << 8) | mpu.lReg, ram.readMemory(mpu.pcReg++));
			break;
		
		case 0x37: // SCF
			mpu.incrementCycleCount(1);
			mpu.fReg &= ~(SUBTRACT | HALF_CARRY);
			mpu.fReg |= CARRY;
			break;
		
		case 0x38: // JR C,n
			if ((mpu.fReg & CARRY) != 0) {
				mpu.incrementCycleCount(3);
				mpu.pcReg += (byte) ram.readMemory(mpu.pcReg) + 1; // signed
																	// immediate
			} else {
				mpu.incrementCycleCount(2);
				mpu.pcReg++;
			}
			break;
		
		case 0x39: // ADD HL,mpu.spReg
			mpu.incrementCycleCount(2);
			mpu.fReg &= ZERO;
			val = ((mpu.hReg << 8) | mpu.lReg) + mpu.spReg;
			if (val > 0xFFFF) {
				val &= 0xFFFF;
				mpu.fReg |= CARRY;
			}
			mpu.hReg = val >> 8;
			mpu.lReg = val & 0xFF;
			break;
		
		case 0x3A: // LDD A,(HL)
			mpu.incrementCycleCount(2);
			index = (mpu.hReg << 8) | mpu.lReg;
			mpu.aReg = ram.readMemory(index);
			index = (index - 1) & 0xFFFF;
			mpu.hReg = index >> 8;
			mpu.lReg = index & 0x00FF;
			break;
		
		case 0x3B: // DEC mpu.spReg
			mpu.incrementCycleCount(2);
			mpu.spReg = (mpu.spReg - 1) & 0xFFFF;
			break;
		
		case 0x3C: // INC A
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_INC[mpu.aReg] | (mpu.fReg & CARRY);
			mpu.aReg = (mpu.aReg + 1) & 0xFF;
			break;
		
		case 0x3D: // DEC A
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_DEC[mpu.aReg] | (mpu.fReg & CARRY);
			mpu.aReg = (mpu.aReg - 1) & 0xFF;
			break;
		
		case 0x3E: // LD A,n
			mpu.incrementCycleCount(2);
			mpu.aReg = ram.readMemory(mpu.pcReg++);
			break;
		
		case 0x3F: // CCF
			mpu.incrementCycleCount(1);
			mpu.fReg &= ~(SUBTRACT | HALF_CARRY);
			mpu.fReg ^= CARRY;
			break;
		
		case 0x40: // LD B,B
			mpu.incrementCycleCount(1);
			break;
		
		case 0x41: // LD B,C
			mpu.incrementCycleCount(1);
			mpu.bReg = mpu.cReg;
			break;
		
		case 0x42: // LD B,D
			mpu.incrementCycleCount(1);
			mpu.bReg = mpu.dReg;
			break;
		
		case 0x43: // LD B,E
			mpu.incrementCycleCount(1);
			mpu.bReg = mpu.eReg;
			break;
		
		case 0x44: // LD B,H
			mpu.incrementCycleCount(1);
			mpu.bReg = mpu.hReg;
			break;
		
		case 0x45: // LD B,L
			mpu.incrementCycleCount(1);
			mpu.bReg = mpu.lReg;
			break;
		
		case 0x46: // LD B,(HL)
			mpu.incrementCycleCount(2);
			mpu.bReg = ram.readMemory((mpu.hReg << 8) | mpu.lReg);
			break;
		
		case 0x47: // LD B,A
			mpu.incrementCycleCount(1);
			mpu.bReg = mpu.aReg;
			break;
		
		case 0x48: // LD C,B
			mpu.incrementCycleCount(1);
			mpu.cReg = mpu.bReg;
			break;
		
		case 0x49: // LD C,C
			mpu.incrementCycleCount(1);
			break;
		
		case 0x4A: // LD C,D
			mpu.incrementCycleCount(1);
			mpu.cReg = mpu.dReg;
			break;
		
		case 0x4B: // LD C,E
			mpu.incrementCycleCount(1);
			mpu.cReg = mpu.eReg;
			break;
		
		case 0x4C: // LD C,H
			mpu.incrementCycleCount(1);
			mpu.cReg = mpu.hReg;
			break;
		
		case 0x4D: // LD C,L
			mpu.incrementCycleCount(1);
			mpu.cReg = mpu.lReg;
			break;
		
		case 0x4E: // LD C,(HL)
			mpu.incrementCycleCount(2);
			mpu.cReg = ram.readMemory((mpu.hReg << 8) | mpu.lReg);
			break;
		
		case 0x4F: // LD C,A
			mpu.incrementCycleCount(1);
			mpu.cReg = mpu.aReg;
			break;
		
		case 0x50: // LD D,B
			mpu.incrementCycleCount(1);
			mpu.dReg = mpu.bReg;
			break;
		
		case 0x51: // LD D,C
			mpu.incrementCycleCount(1);
			mpu.dReg = mpu.cReg;
			break;
		
		case 0x52: // LD D,D
			mpu.incrementCycleCount(1);
			break;
		
		case 0x53: // LD D,E
			mpu.incrementCycleCount(1);
			mpu.dReg = mpu.eReg;
			break;
		
		case 0x54: // LD D,H
			mpu.incrementCycleCount(1);
			mpu.dReg = mpu.hReg;
			break;
		
		case 0x55: // LD D,L
			mpu.incrementCycleCount(1);
			mpu.dReg = mpu.lReg;
			break;
		
		case 0x56: // LD D,(HL)
			mpu.incrementCycleCount(2);
			mpu.dReg = ram.readMemory((mpu.hReg << 8) | mpu.lReg);
			break;
		
		case 0x57: // LD D,A
			mpu.incrementCycleCount(1);
			mpu.dReg = mpu.aReg;
			break;
		
		case 0x58: // LD E,B
			mpu.incrementCycleCount(1);
			mpu.eReg = mpu.bReg;
			break;
		
		case 0x59: // LD E,C
			mpu.incrementCycleCount(1);
			mpu.eReg = mpu.cReg;
			break;
		
		case 0x5A: // LD E,D
			mpu.incrementCycleCount(1);
			mpu.eReg = mpu.dReg;
			break;
		
		case 0x5B: // LD E,E
			mpu.incrementCycleCount(1);
			break;
		
		case 0x5C: // LD E,H
			mpu.incrementCycleCount(1);
			mpu.eReg = mpu.hReg;
			break;
		
		case 0x5D: // LD E,L
			mpu.incrementCycleCount(1);
			mpu.eReg = mpu.lReg;
			break;
		
		case 0x5E: // LD E,(HL)
			mpu.incrementCycleCount(2);
			mpu.eReg = ram.readMemory((mpu.hReg << 8) | mpu.lReg);
			break;
		
		case 0x5F: // LD E,A
			mpu.incrementCycleCount(1);
			mpu.eReg = mpu.aReg;
			break;
		
		case 0x60: // LD H,B
			mpu.incrementCycleCount(1);
			mpu.hReg = mpu.bReg;
			break;
		
		case 0x61: // LD H,C
			mpu.incrementCycleCount(1);
			mpu.hReg = mpu.cReg;
			break;
		
		case 0x62: // LD H,D
			mpu.incrementCycleCount(1);
			mpu.hReg = mpu.dReg;
			break;
		
		case 0x63: // LD H,E
			mpu.incrementCycleCount(1);
			mpu.hReg = mpu.eReg;
			break;
		
		case 0x64: // LD H,H
			mpu.incrementCycleCount(1);
			break;
		
		case 0x65: // LD H,L
			mpu.incrementCycleCount(1);
			mpu.hReg = mpu.lReg;
			break;
		
		case 0x66: // LD H,(HL)
			mpu.incrementCycleCount(2);
			mpu.hReg = ram.readMemory((mpu.hReg << 8) | mpu.lReg);
			break;
		
		case 0x67: // LD H,A
			mpu.incrementCycleCount(1);
			mpu.hReg = mpu.aReg;
			break;
		
		case 0x68: // LD L,B
			mpu.incrementCycleCount(1);
			mpu.lReg = mpu.bReg;
			break;
		
		case 0x69: // LD L,C
			mpu.incrementCycleCount(1);
			mpu.lReg = mpu.cReg;
			break;
		
		case 0x6A: // LD L,D
			mpu.incrementCycleCount(1);
			mpu.lReg = mpu.dReg;
			break;
		
		case 0x6B: // LD L,E
			mpu.incrementCycleCount(1);
			mpu.lReg = mpu.eReg;
			break;
		
		case 0x6C: // LD L,H
			mpu.incrementCycleCount(1);
			mpu.lReg = mpu.hReg;
			break;
		
		case 0x6D: // LD L,L
			mpu.incrementCycleCount(1);
			break;
		
		case 0x6E: // LD L,(HL)
			mpu.incrementCycleCount(2);
			mpu.lReg = ram.readMemory((mpu.hReg << 8) | mpu.lReg);
			break;
		
		case 0x6F: // LD L,A
			mpu.incrementCycleCount(1);
			mpu.lReg = mpu.aReg;
			break;
		
		case 0x70: // LD (HL),B
			mpu.incrementCycleCount(2);
			ram.writeMemory((mpu.hReg << 8) | mpu.lReg, mpu.bReg);
			break;
		
		case 0x71: // LD (HL),C
			mpu.incrementCycleCount(2);
			ram.writeMemory((mpu.hReg << 8) | mpu.lReg, mpu.cReg);
			break;
		
		case 0x72: // LD (HL),D
			mpu.incrementCycleCount(2);
			ram.writeMemory((mpu.hReg << 8) | mpu.lReg, mpu.dReg);
			break;
		
		case 0x73: // LD (HL),E
			mpu.incrementCycleCount(2);
			ram.writeMemory((mpu.hReg << 8) | mpu.lReg, mpu.eReg);
			break;
		
		case 0x74: // LD (HL),H
			mpu.incrementCycleCount(2);
			ram.writeMemory((mpu.hReg << 8) | mpu.lReg, mpu.hReg);
			break;
		
		case 0x75: // LD (HL),L
			mpu.incrementCycleCount(2);
			ram.writeMemory((mpu.hReg << 8) | mpu.lReg, mpu.lReg);
			break;
		
		case 0x76: // HALT
			if (mpu.ime) {
				mpu.pcReg--;
				mpu.halt();
				mpu.setCycles(mpu.getNextHBlank());
			} else {
				mpu.incrementCycleCount(1);
			}
			break;
		
		case 0x77: // LD (HL),A
			mpu.incrementCycleCount(2);
			ram.writeMemory((mpu.hReg << 8) | mpu.lReg, mpu.aReg);
			break;
		
		case 0x78: // LD A,B
			mpu.incrementCycleCount(1);
			mpu.aReg = mpu.bReg;
			break;
		
		case 0x79: // LD A,C
			mpu.incrementCycleCount(1);
			mpu.aReg = mpu.cReg;
			break;
		
		case 0x7A: // LD A,D
			mpu.incrementCycleCount(1);
			mpu.aReg = mpu.dReg;
			break;
		
		case 0x7B: // LD A,E
			mpu.incrementCycleCount(1);
			mpu.aReg = mpu.eReg;
			break;
		
		case 0x7C: // LD A,H
			mpu.incrementCycleCount(1);
			mpu.aReg = mpu.hReg;
			break;
		
		case 0x7D: // LD A,L
			mpu.incrementCycleCount(1);
			mpu.aReg = mpu.lReg;
			break;
		
		case 0x7E: // LD A,(HL)
			mpu.incrementCycleCount(2);
			mpu.aReg = ram.readMemory((mpu.hReg << 8) | mpu.lReg);
			break;
		
		case 0x7F: // LD A,A
			mpu.incrementCycleCount(1);
			break;
		
		case 0x80: // ADD A,B
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_ADD[(mpu.bReg << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + mpu.bReg) & 0xFF;
			break;
		
		case 0x81: // ADD A,C
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_ADD[(mpu.cReg << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + mpu.cReg) & 0xFF;
			break;
		
		case 0x82: // ADD A,D
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_ADD[(mpu.dReg << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + mpu.dReg) & 0xFF;
			break;
		
		case 0x83: // ADD A,E
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_ADD[(mpu.eReg << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + mpu.eReg) & 0xFF;
			break;
		
		case 0x84: // ADD A,H
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_ADD[(mpu.hReg << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + mpu.hReg) & 0xFF;
			break;
		
		case 0x85: // ADD A,L
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_ADD[(mpu.lReg << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + mpu.lReg) & 0xFF;
			break;
		
		case 0x86: // ADD A,(HL)
			mpu.incrementCycleCount(2);
			memval = ram.readMemory((mpu.hReg << 8) | mpu.lReg);
			mpu.fReg = mpu.F_ADD[(memval << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + memval) & 0xFF;
			break;
		
		case 0x87: // ADD A,A
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_ADD[(mpu.aReg << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + mpu.aReg) & 0xFF;
			break;
		
		case 0x88: // ADC A,B
			mpu.incrementCycleCount(1);
			val = mpu.bReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_ADD[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + val) & 0xFF;
			break;
		
		case 0x89: // ADC A,C
			mpu.incrementCycleCount(1);
			val = mpu.cReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_ADD[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + val) & 0xFF;
			break;
		
		case 0x8A: // ADC A,D
			mpu.incrementCycleCount(1);
			val = mpu.dReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_ADD[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + val) & 0xFF;
			break;
		
		case 0x8B: // ADC A,E
			mpu.incrementCycleCount(1);
			val = mpu.eReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_ADD[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + val) & 0xFF;
			break;
		
		case 0x8C: // ADC A,H
			mpu.incrementCycleCount(1);
			val = mpu.hReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_ADD[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + val) & 0xFF;
			break;
		
		case 0x8D: // ADC A,L
			mpu.incrementCycleCount(1);
			val = mpu.lReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_ADD[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + val) & 0xFF;
			break;
		
		case 0x8E: // ADC A,(HL)
			mpu.incrementCycleCount(2);
			memval = ram.readMemory((mpu.hReg << 8) | mpu.lReg) + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_ADD[(memval << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + memval) & 0xFF;
			break;
		
		case 0x8F: // ADC A,A
			mpu.incrementCycleCount(1);
			val = mpu.aReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_ADD[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + val) & 0xFF;
			break;
		
		case 0x90: // SUB A,B
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_SUB[(mpu.bReg << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - mpu.bReg) & 0xFF;
			break;
		
		case 0x91: // SUB A,C
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_SUB[(mpu.cReg << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - mpu.cReg) & 0xFF;
			break;
		
		case 0x92: // SUB A,D
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_SUB[(mpu.dReg << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - mpu.dReg) & 0xFF;
			break;
		
		case 0x93: // SUB A,E
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_SUB[(mpu.eReg << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - mpu.eReg) & 0xFF;
			break;
		
		case 0x94: // SUB A,H
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_SUB[(mpu.hReg << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - mpu.hReg) & 0xFF;
			break;
		
		case 0x95: // SUB A,L
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_SUB[(mpu.lReg << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - mpu.lReg) & 0xFF;
			break;
		
		case 0x96: // SUB A,(HL)
			mpu.incrementCycleCount(2);
			memval = ram.readMemory((mpu.hReg << 8) | mpu.lReg);
			mpu.fReg = mpu.F_SUB[(memval << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - memval) & 0xFF;
			break;
		
		case 0x97: // SUB A,A
			mpu.incrementCycleCount(1);
			mpu.fReg = SUBTRACT | ZERO;
			mpu.aReg = 0;
			break;
		
		case 0x98: // SBC A,B
			mpu.incrementCycleCount(1);
			val = mpu.bReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_SUB[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - val) & 0xFF;
			break;
		
		case 0x99: // SBC A,C
			mpu.incrementCycleCount(1);
			val = mpu.cReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_SUB[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - val) & 0xFF;
			break;
		
		case 0x9A: // SBC A,D
			mpu.incrementCycleCount(1);
			val = mpu.dReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_SUB[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - val) & 0xFF;
			break;
		
		case 0x9B: // SBC A,E
			mpu.incrementCycleCount(1);
			val = mpu.eReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_SUB[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - val) & 0xFF;
			break;
		
		case 0x9C: // SBC A,H
			mpu.incrementCycleCount(1);
			val = mpu.hReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_SUB[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - val) & 0xFF;
			break;
		
		case 0x9D: // SBC A,L
			mpu.incrementCycleCount(1);
			val = mpu.lReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_SUB[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - val) & 0xFF;
			
			break;
		
		case 0x9E: // SBC A,(HL)
			mpu.incrementCycleCount(2);
			memval = ram.readMemory((mpu.hReg << 8) | mpu.lReg) + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_SUB[(memval << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - memval) & 0xFF;
			break;
		
		case 0x9F: // SBC A,A
			mpu.incrementCycleCount(1);
			val = mpu.aReg + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_SUB[(val << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - val) & 0xFF;
			break;
		
		case 0xA0: // AND B
			mpu.incrementCycleCount(1);
			mpu.aReg &= mpu.bReg;
			mpu.fReg = HALF_CARRY | (((mpu.aReg - 1) >> 24) & ZERO);
			break;
		
		case 0xA1: // AND C
			mpu.incrementCycleCount(1);
			mpu.aReg &= mpu.cReg;
			mpu.fReg = HALF_CARRY | (((mpu.aReg - 1) >> 24) & ZERO);
			break;
		
		case 0xA2: // AND D
			mpu.incrementCycleCount(1);
			mpu.aReg &= mpu.dReg;
			mpu.fReg = HALF_CARRY | (((mpu.aReg - 1) >> 24) & ZERO);
			break;
		
		case 0xA3: // AND E
			mpu.incrementCycleCount(1);
			mpu.aReg &= mpu.eReg;
			mpu.fReg = HALF_CARRY | (((mpu.aReg - 1) >> 24) & ZERO);
			break;
		
		case 0xA4: // AND H
			mpu.incrementCycleCount(1);
			mpu.aReg &= mpu.hReg;
			mpu.fReg = HALF_CARRY | (((mpu.aReg - 1) >> 24) & ZERO);
			break;
		
		case 0xA5: // AND L
			mpu.incrementCycleCount(1);
			mpu.aReg &= mpu.lReg;
			mpu.fReg = HALF_CARRY | (((mpu.aReg - 1) >> 24) & ZERO);
			break;
		
		case 0xA6: // AND (HL)
			mpu.incrementCycleCount(2);
			mpu.aReg &= ram.readMemory((mpu.hReg << 8) | mpu.lReg);
			mpu.fReg = HALF_CARRY | (((mpu.aReg - 1) >> 24) & ZERO);
			break;
		
		case 0xA7: // AND A
			// A&A = A, no change
			mpu.incrementCycleCount(1);
			mpu.fReg = HALF_CARRY | (((mpu.aReg - 1) >> 24) & ZERO);
			break;
		
		case 0xA8: // XOR B
			mpu.incrementCycleCount(1);
			mpu.aReg ^= mpu.bReg;
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xA9: // XOR C
			mpu.incrementCycleCount(1);
			mpu.aReg ^= mpu.cReg;
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xAA: // XOR D
			mpu.incrementCycleCount(1);
			mpu.aReg ^= mpu.dReg;
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xAB: // XOR E
			mpu.incrementCycleCount(1);
			mpu.aReg ^= mpu.eReg;
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xAC: // XOR H
			mpu.incrementCycleCount(1);
			mpu.aReg ^= mpu.hReg;
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xAD: // XOR L
			mpu.incrementCycleCount(1);
			mpu.aReg ^= mpu.lReg;
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xAE: // XOR (HL)
			mpu.incrementCycleCount(2);
			mpu.aReg ^= ram.readMemory((mpu.hReg << 8) | mpu.lReg);
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xAF: // XOR A
			// A^A = 0
			mpu.incrementCycleCount(1);
			mpu.aReg = 0;
			mpu.fReg = ZERO;
			break;
		
		case 0xB0: // OR B
			mpu.incrementCycleCount(1);
			mpu.aReg |= mpu.bReg;
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xB1: // OR C
			mpu.incrementCycleCount(1);
			mpu.aReg |= mpu.cReg;
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xB2: // OR D
			mpu.incrementCycleCount(1);
			mpu.aReg |= mpu.dReg;
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xB3: // OR E
			mpu.incrementCycleCount(1);
			mpu.aReg |= mpu.eReg;
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xB4: // OR H
			mpu.incrementCycleCount(1);
			mpu.aReg |= mpu.hReg;
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xB5: // OR L
			mpu.incrementCycleCount(1);
			mpu.aReg |= mpu.lReg;
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xB6: // OR (HL)
			mpu.incrementCycleCount(2);
			mpu.aReg |= ram.readMemory((mpu.hReg << 8) | mpu.lReg);
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xB7: // OR A
			// A|A = A, no change
			mpu.incrementCycleCount(1);
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xB8: // CP B
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_SUB[(mpu.bReg << 8) | mpu.aReg];
			break;
		
		case 0xB9: // CP C
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_SUB[(mpu.cReg << 8) | mpu.aReg];
			break;
		
		case 0xBA: // CP D
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_SUB[(mpu.dReg << 8) | mpu.aReg];
			break;
		
		case 0xBB: // CP E
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_SUB[(mpu.eReg << 8) | mpu.aReg];
			break;
		
		case 0xBC: // CP H
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_SUB[(mpu.hReg << 8) | mpu.aReg];
			break;
		
		case 0xBD: // CP L
			mpu.incrementCycleCount(1);
			mpu.fReg = mpu.F_SUB[(mpu.lReg << 8) | mpu.aReg];
			break;
		
		case 0xBE: // CP (HL)
			mpu.incrementCycleCount(2);
			mpu.fReg = mpu.F_SUB[(ram.readMemory((mpu.hReg << 8) | mpu.lReg) << 8) | mpu.aReg];
			break;
		
		case 0xBF: // CP A
			mpu.incrementCycleCount(1);
			mpu.fReg = SUBTRACT | ZERO;
			break;
		
		case 0xC0: // RET NZ
			if ((mpu.fReg & ZERO) == 0) {
				mpu.incrementCycleCount(5);
				mpu.pcReg = ram.readMemory(mpu.spReg++) | (ram.readMemory(mpu.spReg++) << 8);
			} else {
				mpu.incrementCycleCount(2);
			}
			break;
		
		case 0xC1: // POP BC
			mpu.incrementCycleCount(3);
			mpu.cReg = ram.readMemory(mpu.spReg++);
			mpu.bReg = ram.readMemory(mpu.spReg++);
			break;
		
		case 0xC2: // JP NZ,nn
			if ((mpu.fReg & ZERO) == 0) {
				mpu.incrementCycleCount(4);
				mpu.pcReg = ram.readMemory(mpu.pcReg) | (ram.readMemory(mpu.pcReg + 1) << 8);
			} else {
				mpu.incrementCycleCount(3);
				mpu.pcReg += 2;
			}
			break;
		
		case 0xC3: // JP nn
			mpu.incrementCycleCount(4);
			mpu.pcReg = ram.readMemory(mpu.pcReg) | (ram.readMemory(mpu.pcReg + 1) << 8);
			break;
		
		case 0xC4: // CALL NZ,nn
			if ((mpu.fReg & ZERO) == 0) {
				mpu.incrementCycleCount(6);
				ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, (mpu.pcReg + 2) >> 8);
				ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, (mpu.pcReg + 2) & 0x00FF);
				mpu.pcReg = ram.readMemory(mpu.pcReg) | (ram.readMemory(mpu.pcReg + 1) << 8);
			} else {
				mpu.incrementCycleCount(3);
				mpu.pcReg += 2;
			}
			break;
		
		case 0xC5: // PUSH BC
			mpu.incrementCycleCount(4);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.bReg);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.cReg);
			break;
		
		case 0xC6: // ADD A,n
			mpu.incrementCycleCount(2);
			memval = ram.readMemory(mpu.pcReg++);
			mpu.fReg = mpu.F_ADD[(memval << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + memval) & 0xFF;
			break;
		
		case 0xC7: // RST 00H
			mpu.incrementCycleCount(4);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg >> 8);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg & 0x00FF);
			mpu.pcReg = 0x0000;
			break;
		
		case 0xC8: // RET Z
			if ((mpu.fReg & ZERO) != 0) {
				mpu.incrementCycleCount(5);
				mpu.pcReg = ram.readMemory(mpu.spReg++) | (ram.readMemory(mpu.spReg++) << 8);
			} else {
				mpu.incrementCycleCount(2);
			}
			break;
		
		case 0xC9: // RET
			mpu.incrementCycleCount(4);
			mpu.pcReg = ram.readMemory(mpu.spReg++) | (ram.readMemory(mpu.spReg++) << 8);
			break;
		
		case 0xCA: // JP Z,nn
			if ((mpu.fReg & ZERO) != 0) {
				mpu.incrementCycleCount(4);
				mpu.pcReg = ram.readMemory(mpu.pcReg) | (ram.readMemory(mpu.pcReg + 1) << 8);
			} else {
				mpu.incrementCycleCount(3);
				mpu.pcReg += 2;
			}
			break;
		
		case 0xCB: // 2-byte ompu.pcRegodes
			switch (ram.readMemory(mpu.pcReg++)) {
			case 0x00: // RLC B
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.bReg & BIT7) >> 3;
				mpu.bReg = ((mpu.bReg << 1) | (mpu.bReg >> 7)) & 0xFF;
				if (mpu.bReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x01: // RLC C
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.cReg & BIT7) >> 3;
				mpu.cReg = ((mpu.cReg << 1) | (mpu.cReg >> 7)) & 0xFF;
				if (mpu.cReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x02: // RLC D
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.dReg & BIT7) >> 3;
				mpu.dReg = ((mpu.dReg << 1) | (mpu.dReg >> 7)) & 0xFF;
				if (mpu.dReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x03: // RLC E
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.eReg & BIT7) >> 3;
				mpu.eReg = ((mpu.eReg << 1) | (mpu.eReg >> 7)) & 0xFF;
				if (mpu.eReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x04: // RLC H
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.hReg & BIT7) >> 3;
				mpu.hReg = ((mpu.hReg << 1) | (mpu.hReg >> 7)) & 0xFF;
				if (mpu.hReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x05: // RLC L
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.lReg & BIT7) >> 3;
				mpu.lReg = ((mpu.lReg << 1) | (mpu.lReg >> 7)) & 0xFF;
				if (mpu.lReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x06: // RLC (HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				memval = ram.readMemory(index);
				mpu.fReg = (memval & BIT7) >> 3;
				if (ram.writeMemory(index, ((memval << 1) | (memval >> 7)) & 0xFF) == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x07: // RLC A
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.aReg & BIT7) >> 3;
				mpu.aReg = ((mpu.aReg << 1) | (mpu.aReg >> 7)) & 0xFF;
				if (mpu.aReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x08: // RRC B
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.bReg & BIT0) << 4;
				mpu.bReg = ((mpu.bReg >> 1) | (mpu.bReg << 7)) & 0xFF;
				if (mpu.bReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x09: // RRC C
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.cReg & BIT0) << 4;
				mpu.cReg = ((mpu.cReg >> 1) | (mpu.cReg << 7)) & 0xFF;
				if (mpu.cReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x0A: // RRC D
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.dReg & BIT0) << 4;
				mpu.dReg = ((mpu.dReg >> 1) | (mpu.dReg << 7)) & 0xFF;
				if (mpu.dReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x0B: // RRC E
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.eReg & BIT0) << 4;
				mpu.eReg = ((mpu.eReg >> 1) | (mpu.eReg << 7)) & 0xFF;
				if (mpu.eReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x0C: // RRC H
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.hReg & BIT0) << 4;
				mpu.hReg = ((mpu.hReg >> 1) | (mpu.hReg << 7)) & 0xFF;
				if (mpu.hReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x0D: // RRC L
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.lReg & BIT0) << 4;
				mpu.lReg = ((mpu.lReg >> 1) | (mpu.lReg << 7)) & 0xFF;
				if (mpu.lReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x0E: // RRC (HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				memval = ram.readMemory(index);
				mpu.fReg = (memval & BIT0) << 4;
				if (ram.writeMemory(index, ((memval >> 1) | (memval << 7)) & 0xFF) == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x0F: // RRC A
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.aReg & BIT0) << 4;
				mpu.aReg = ((mpu.aReg >> 1) | (mpu.aReg << 7)) & 0xFF;
				if (mpu.aReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x10: // RL B
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) >> 4;
				mpu.fReg = (mpu.bReg & BIT7) >> 3;
				mpu.bReg = ((mpu.bReg << 1) | val) & 0xFF;
				if (mpu.bReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x11: // RL C
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) >> 4;
				mpu.fReg = (mpu.cReg & BIT7) >> 3;
				mpu.cReg = ((mpu.cReg << 1) | val) & 0xFF;
				if (mpu.cReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x12: // RL D
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) >> 4;
				mpu.fReg = (mpu.dReg & BIT7) >> 3;
				mpu.dReg = ((mpu.dReg << 1) | val) & 0xFF;
				if (mpu.dReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x13: // RL E
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) >> 4;
				mpu.fReg = (mpu.eReg & BIT7) >> 3;
				mpu.eReg = ((mpu.eReg << 1) | val) & 0xFF;
				if (mpu.eReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x14: // RL H
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) >> 4;
				mpu.fReg = (mpu.hReg & BIT7) >> 3;
				mpu.hReg = ((mpu.hReg << 1) | val) & 0xFF;
				if (mpu.hReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x15: // RL L
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) >> 4;
				mpu.fReg = (mpu.lReg & BIT7) >> 3;
				mpu.lReg = ((mpu.lReg << 1) | val) & 0xFF;
				if (mpu.lReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x16: // RL (HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				val = (mpu.fReg & CARRY) >> 4;
				memval = ram.readMemory(index);
				mpu.fReg = (memval & BIT7) >> 3;
				if (ram.writeMemory(index, ((memval << 1) | val) & 0xFF) == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x17: // RL A
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) >> 4;
				mpu.fReg = (mpu.aReg & BIT7) >> 3;
				mpu.aReg = ((mpu.aReg << 1) | val) & 0xFF;
				if (mpu.aReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x18: // RR B
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) << 3;
				mpu.fReg = (mpu.bReg & BIT0) << 4;
				mpu.bReg = (mpu.bReg >> 1) | val;
				if (mpu.bReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x19: // RR C
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) << 3;
				mpu.fReg = (mpu.cReg & BIT0) << 4;
				mpu.cReg = (mpu.cReg >> 1) | val;
				if (mpu.cReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x1A: // RR D
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) << 3;
				mpu.fReg = (mpu.dReg & BIT0) << 4;
				mpu.dReg = (mpu.dReg >> 1) | val;
				if (mpu.dReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x1B: // RR E
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) << 3;
				mpu.fReg = (mpu.eReg & BIT0) << 4;
				mpu.eReg = (mpu.eReg >> 1) | val;
				if (mpu.eReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x1C: // RR H
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) << 3;
				mpu.fReg = (mpu.hReg & BIT0) << 4;
				mpu.hReg = (mpu.hReg >> 1) | val;
				if (mpu.hReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x1D: // RR L
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) << 3;
				mpu.fReg = (mpu.lReg & BIT0) << 4;
				mpu.lReg = (mpu.lReg >> 1) | val;
				if (mpu.lReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x1E: // RR (HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				val = (mpu.fReg & CARRY) << 3;
				memval = ram.readMemory(index);
				mpu.fReg = (memval & BIT0) << 4;
				if (ram.writeMemory(index, (memval >> 1) | val) == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x1F: // RR A
				mpu.incrementCycleCount(2);
				val = (mpu.fReg & CARRY) << 3;
				mpu.fReg = (mpu.aReg & BIT0) << 4;
				mpu.aReg = (mpu.aReg >> 1) | val;
				if (mpu.aReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x20: // SLA B
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.bReg & BIT7) >> 3;
				mpu.bReg = (mpu.bReg << 1) & 0xFF;
				if (mpu.bReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x21: // SLA C
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.cReg & BIT7) >> 3;
				mpu.cReg = (mpu.cReg << 1) & 0xFF;
				if (mpu.cReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x22: // SLA D
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.dReg & BIT7) >> 3;
				mpu.dReg = (mpu.dReg << 1) & 0xFF;
				if (mpu.dReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x23: // SLA E
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.eReg & BIT7) >> 3;
				mpu.eReg = (mpu.eReg << 1) & 0xFF;
				if (mpu.eReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x24: // SLA H
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.hReg & BIT7) >> 3;
				mpu.hReg = (mpu.hReg << 1) & 0xFF;
				if (mpu.hReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x25: // SLA L
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.lReg & BIT7) >> 3;
				mpu.lReg = (mpu.lReg << 1) & 0xFF;
				if (mpu.lReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x26: // SLA (HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				memval = ram.readMemory(index);
				mpu.fReg = (memval & BIT7) >> 3;
				if (ram.writeMemory(index, (memval << 1) & 0xFF) == 0) {
					mpu.fReg |= 0;
				}
				break;
			
			case 0x27: // SLA A
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.aReg & BIT7) >> 3;
				mpu.aReg = (mpu.aReg << 1) & 0xFF;
				if (mpu.aReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x28: // SRA B
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.bReg & BIT0) << 4;
				mpu.bReg = (mpu.bReg & BIT7) | (mpu.bReg >> 1);
				if (mpu.bReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x29: // SRA C
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.cReg & BIT0) << 4;
				mpu.cReg = (mpu.cReg & BIT7) | (mpu.cReg >> 1);
				if (mpu.cReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x2A: // SRA D
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.dReg & BIT0) << 4;
				mpu.dReg = (mpu.dReg & BIT7) | (mpu.dReg >> 1);
				if (mpu.dReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x2B: // SRA E
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.eReg & BIT0) << 4;
				mpu.eReg = (mpu.eReg & BIT7) | (mpu.eReg >> 1);
				if (mpu.eReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x2C: // SRA H
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.hReg & BIT0) << 4;
				mpu.hReg = (mpu.hReg & BIT7) | (mpu.hReg >> 1);
				if (mpu.hReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x2D: // SRA L
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.lReg & BIT0) << 4;
				mpu.lReg = (mpu.lReg & BIT7) | (mpu.lReg >> 1);
				if (mpu.lReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x2E: // SRA (HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				memval = ram.readMemory(index);
				mpu.fReg = (memval & BIT0) << 4;
				if (ram.writeMemory(index, (memval & BIT7) | (memval >> 1)) == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x2F: // SRA A
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.aReg & BIT0) << 4;
				mpu.aReg = (mpu.aReg & BIT7) | (mpu.aReg >> 1);
				if (mpu.aReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x30: // SWAP B
				mpu.incrementCycleCount(2);
				val = mpu.bReg & 0x0F;
				mpu.bReg >>= 4;
				mpu.bReg |= (val << 4);
				mpu.fReg = ((mpu.bReg - 1) >> 24) & ZERO;
				break;
			
			case 0x31: // SWAP C
				mpu.incrementCycleCount(2);
				val = mpu.cReg & 0x0F;
				mpu.cReg >>= 4;
				mpu.cReg |= (val << 4);
				mpu.fReg = ((mpu.cReg - 1) >> 24) & ZERO;
				break;
			
			case 0x32: // SWAP D
				mpu.incrementCycleCount(2);
				val = mpu.dReg & 0x0F;
				mpu.dReg >>= 4;
				mpu.dReg |= (val << 4);
				mpu.fReg = ((mpu.dReg - 1) >> 24) & ZERO;
				break;
			
			case 0x33: // SWAP E
				mpu.incrementCycleCount(2);
				val = mpu.eReg & 0x0F;
				mpu.eReg >>= 4;
				mpu.eReg |= (val << 4);
				mpu.fReg = ((mpu.eReg - 1) >> 24) & ZERO;
				break;
			
			case 0x34: // SWAP H
				mpu.incrementCycleCount(2);
				val = mpu.hReg & 0x0F;
				mpu.hReg >>= 4;
				mpu.hReg |= (val << 4);
				mpu.fReg = ((mpu.hReg - 1) >> 24) & ZERO;
				break;
			
			case 0x35: // SWAP L
				mpu.incrementCycleCount(2);
				val = mpu.lReg & 0x0F;
				mpu.lReg >>= 4;
				mpu.lReg |= (val << 4);
				mpu.fReg = ((mpu.lReg - 1) >> 24) & ZERO;
				break;
			
			case 0x36: // SWAP (HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				memval = ram.readMemory(index);
				val = memval >> 4;
				val |= ((memval & 0x0F) << 4);
				ram.writeMemory(index, val);
				mpu.fReg = ((val - 1) >> 24) & ZERO;
				break;
			
			case 0x37: // SWAP A
				mpu.incrementCycleCount(2);
				val = mpu.aReg & 0x0F;
				mpu.aReg >>= 4;
				mpu.aReg |= (val << 4);
				mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
				break;
			
			case 0x38: // SRL B
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.bReg & BIT0) << 4;
				mpu.bReg >>= 1;
				if (mpu.bReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x39: // SRL C
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.cReg & BIT0) << 4;
				mpu.cReg >>= 1;
				if (mpu.cReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x3A: // SRL D
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.dReg & BIT0) << 4;
				mpu.dReg >>= 1;
				if (mpu.dReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x3B: // SRL E
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.eReg & BIT0) << 4;
				mpu.eReg >>= 1;
				if (mpu.eReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x3C: // SRL H
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.hReg & BIT0) << 4;
				mpu.hReg >>= 1;
				if (mpu.hReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x3D: // SRL L
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.lReg & BIT0) << 4;
				mpu.lReg >>= 1;
				if (mpu.lReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x3E: // SRL (HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				memval = ram.readMemory(index);
				mpu.fReg = (memval & BIT0) << 4;
				if (ram.writeMemory(index, memval >> 1) == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x3F: // SRL A
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.aReg & BIT0) << 4;
				mpu.aReg >>= 1;
				if (mpu.aReg == 0) {
					mpu.fReg |= ZERO;
				}
				break;
			
			case 0x40: // BIT 0,B
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.bReg & BIT0) << 7);
				break;
			
			case 0x41: // BIT 0,C
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.cReg & BIT0) << 7);
				break;
			
			case 0x42: // BIT 0,D
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.dReg & BIT0) << 7);
				break;
			
			case 0x43: // BIT 0,E
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.eReg & BIT0) << 7);
				break;
			
			case 0x44: // BIT 0,H
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.hReg & BIT0) << 7);
				break;
			
			case 0x45: // BIT 0,L
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.lReg & BIT0) << 7);
				break;
			
			case 0x46: // BIT 0,(HL)
				mpu.incrementCycleCount(4);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~ram.readMemory((mpu.hReg << 8) | mpu.lReg) & BIT0) << 7);
				break;
			
			case 0x47: // BIT 0,A
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.aReg & BIT0) << 7);
				break;
			
			case 0x48: // BIT 1,B
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.bReg & BIT1) << 6);
				break;
			
			case 0x49: // BIT 1,C
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.cReg & BIT1) << 6);
				break;
			
			case 0x4A: // BIT 1,D
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.dReg & BIT1) << 6);
				break;
			
			case 0x4B: // BIT 1,E
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.eReg & BIT1) << 6);
				break;
			
			case 0x4C: // BIT 1,H
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.hReg & BIT1) << 6);
				break;
			
			case 0x4D: // BIT 1,L
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.lReg & BIT1) << 6);
				break;
			
			case 0x4E: // BIT 1,(HL)
				mpu.incrementCycleCount(4);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~ram.readMemory((mpu.hReg << 8) | mpu.lReg) & BIT1) << 6);
				break;
			
			case 0x4F: // BIT 1,A
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.aReg & BIT1) << 6);
				break;
			
			case 0x50: // BIT 2,B
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.bReg & BIT2) << 5);
				break;
			
			case 0x51: // BIT 2,C
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.cReg & BIT2) << 5);
				break;
			
			case 0x52: // BIT 2,D
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.dReg & BIT2) << 5);
				break;
			
			case 0x53: // BIT 2,E
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.eReg & BIT2) << 5);
				break;
			
			case 0x54: // BIT 2,H
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.hReg & BIT2) << 5);
				break;
			
			case 0x55: // BIT 2,L
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.lReg & BIT2) << 5);
				break;
			
			case 0x56: // BIT 2,(HL)
				mpu.incrementCycleCount(4);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~ram.readMemory((mpu.hReg << 8) | mpu.lReg) & BIT2) << 5);
				break;
			
			case 0x57: // BIT 2,A
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.aReg & BIT2) << 5);
				break;
			
			case 0x58: // BIT 3,B
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.bReg & BIT3) << 4);
				break;
			
			case 0x59: // BIT 3,C
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.cReg & BIT3) << 4);
				break;
			
			case 0x5A: // BIT 3,D
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.dReg & BIT3) << 4);
				break;
			
			case 0x5B: // BIT 3,E
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.eReg & BIT3) << 4);
				break;
			
			case 0x5C: // BIT 3,H
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.hReg & BIT3) << 4);
				break;
			
			case 0x5D: // BIT 3,L
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.lReg & BIT3) << 4);
				break;
			
			case 0x5E: // BIT 3,(HL)
				mpu.incrementCycleCount(4);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~ram.readMemory((mpu.hReg << 8) | mpu.lReg) & BIT3) << 4);
				break;
			
			case 0x5F: // BIT 3,A
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.aReg & BIT3) << 4);
				break;
			
			case 0x60: // BIT 4,B
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.bReg & BIT4) << 3);
				break;
			
			case 0x61: // BIT 4,C
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.cReg & BIT4) << 3);
				break;
			
			case 0x62: // BIT 4,D
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.dReg & BIT4) << 3);
				break;
			
			case 0x63: // BIT 4,E
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.eReg & BIT4) << 3);
				break;
			
			case 0x64: // BIT 4,H
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.hReg & BIT4) << 3);
				break;
			
			case 0x65: // BIT 4,L
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.lReg & BIT4) << 3);
				break;
			
			case 0x66: // BIT 4,(HL)
				mpu.incrementCycleCount(4);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~ram.readMemory((mpu.hReg << 8) | mpu.lReg) & BIT4) << 3);
				break;
			
			case 0x67: // BIT 4,A
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.aReg & BIT4) << 3);
				break;
			
			case 0x68: // BIT 5,B
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.bReg & BIT5) << 2);
				break;
			
			case 0x69: // BIT 5,C
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.cReg & BIT5) << 2);
				break;
			
			case 0x6A: // BIT 5,D
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.dReg & BIT5) << 2);
				break;
			
			case 0x6B: // BIT 5,E
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.eReg & BIT5) << 2);
				break;
			
			case 0x6C: // BIT 5,H
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.hReg & BIT5) << 2);
				break;
			
			case 0x6D: // BIT 5,L
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.lReg & BIT5) << 2);
				break;
			
			case 0x6E: // BIT 5,(HL)
				mpu.incrementCycleCount(4);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~ram.readMemory((mpu.hReg << 8) | mpu.lReg) & BIT5) << 2);
				break;
			
			case 0x6F: // BIT 5,A
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.aReg & BIT5) << 2);
				break;
			
			case 0x70: // BIT 6,B
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.bReg & BIT6) << 1);
				break;
			
			case 0x71: // BIT 6,C
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.cReg & BIT6) << 1);
				break;
			
			case 0x72: // BIT 6,D
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.dReg & BIT6) << 1);
				break;
			
			case 0x73: // BIT 6,E
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.eReg & BIT6) << 1);
				break;
			
			case 0x74: // BIT 6,H
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.hReg & BIT6) << 1);
				break;
			
			case 0x75: // BIT 6,L
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.lReg & BIT6) << 1);
				break;
			
			case 0x76: // BIT 6,(HL)
				mpu.incrementCycleCount(4);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~ram.readMemory((mpu.hReg << 8) | mpu.lReg) & BIT6) << 1);
				break;
			
			case 0x77: // BIT 6,A
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | ((~mpu.aReg & BIT6) << 1);
				break;
			
			case 0x78: // BIT 7,B
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | (~mpu.bReg & BIT7);
				break;
			
			case 0x79: // BIT 7,C
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | (~mpu.cReg & BIT7);
				break;
			
			case 0x7A: // BIT 7,D
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | (~mpu.dReg & BIT7);
				break;
			
			case 0x7B: // BIT 7,E
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | (~mpu.eReg & BIT7);
				break;
			
			case 0x7C: // BIT 7,H
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | (~mpu.hReg & BIT7);
				break;
			
			case 0x7D: // BIT 7,L
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | (~mpu.lReg & BIT7);
				break;
			
			case 0x7E: // BIT 7,(HL)
				mpu.incrementCycleCount(4);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | (~ram.readMemory((mpu.hReg << 8) | mpu.lReg) & BIT7);
				break;
			
			case 0x7F: // BIT 7,A
				mpu.incrementCycleCount(2);
				mpu.fReg = (mpu.fReg & CARRY) | HALF_CARRY | (~mpu.aReg & BIT7);
				break;
			
			case 0x80: // RES 0,B
				mpu.incrementCycleCount(2);
				mpu.bReg &= ~BIT0;
				break;
			
			case 0x81: // RES 0,C
				mpu.incrementCycleCount(2);
				mpu.cReg &= ~BIT0;
				break;
			
			case 0x82: // RES 0,D
				mpu.incrementCycleCount(2);
				mpu.dReg &= ~BIT0;
				break;
			
			case 0x83: // RES 0,E
				mpu.incrementCycleCount(2);
				mpu.eReg &= ~BIT0;
				break;
			
			case 0x84: // RES 0,H
				mpu.incrementCycleCount(2);
				mpu.hReg &= ~BIT0;
				break;
			
			case 0x85: // RES 0,L
				mpu.incrementCycleCount(2);
				mpu.lReg &= ~BIT0;
				break;
			
			case 0x86: // RES 0,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) & ~BIT0);
				break;
			
			case 0x87: // RES 0,A
				mpu.incrementCycleCount(2);
				mpu.aReg &= ~BIT0;
				break;
			
			case 0x88: // RES 1,B
				mpu.incrementCycleCount(2);
				mpu.bReg &= ~BIT1;
				break;
			
			case 0x89: // RES 1,C
				mpu.incrementCycleCount(2);
				mpu.cReg &= ~BIT1;
				break;
			
			case 0x8A: // RES 1,D
				mpu.incrementCycleCount(2);
				mpu.dReg &= ~BIT1;
				break;
			
			case 0x8B: // RES 1,E
				mpu.incrementCycleCount(2);
				mpu.eReg &= ~BIT1;
				break;
			
			case 0x8C: // RES 1,H
				mpu.incrementCycleCount(2);
				mpu.hReg &= ~BIT1;
				break;
			
			case 0x8D: // RES 1,L
				mpu.incrementCycleCount(2);
				mpu.lReg &= ~BIT1;
				break;
			
			case 0x8E: // RES 1,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) & ~BIT1);
				break;
			
			case 0x8F: // RES 1,A
				mpu.incrementCycleCount(2);
				mpu.aReg &= ~BIT1;
				break;
			
			case 0x90: // RES 2,B
				mpu.incrementCycleCount(2);
				mpu.bReg &= ~BIT2;
				break;
			
			case 0x91: // RES 2,C
				mpu.incrementCycleCount(2);
				mpu.cReg &= ~BIT2;
				break;
			
			case 0x92: // RES 2,D
				mpu.incrementCycleCount(2);
				mpu.dReg &= ~BIT2;
				break;
			
			case 0x93: // RES 2,E
				mpu.incrementCycleCount(2);
				mpu.eReg &= ~BIT2;
				break;
			
			case 0x94: // RES 2,H
				mpu.incrementCycleCount(2);
				mpu.hReg &= ~BIT2;
				break;
			
			case 0x95: // RES 2,L
				mpu.incrementCycleCount(2);
				mpu.lReg &= ~BIT2;
				break;
			
			case 0x96: // RES 2,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) & ~BIT2);
				break;
			
			case 0x97: // RES 2,A
				mpu.incrementCycleCount(2);
				mpu.aReg &= ~BIT2;
				break;
			
			case 0x98: // RES 3,B
				mpu.incrementCycleCount(2);
				mpu.bReg &= ~BIT3;
				break;
			
			case 0x99: // RES 3,C
				mpu.incrementCycleCount(2);
				mpu.cReg &= ~BIT3;
				break;
			
			case 0x9A: // RES 3,D
				mpu.incrementCycleCount(2);
				mpu.dReg &= ~BIT3;
				break;
			
			case 0x9B: // RES 3,E
				mpu.incrementCycleCount(2);
				mpu.eReg &= ~BIT3;
				break;
			
			case 0x9C: // RES 3,H
				mpu.incrementCycleCount(2);
				mpu.hReg &= ~BIT3;
				break;
			
			case 0x9D: // RES 3,L
				mpu.incrementCycleCount(2);
				mpu.lReg &= ~BIT3;
				break;
			
			case 0x9E: // RES 3,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) & ~BIT3);
				break;
			
			case 0x9F: // RES 3,A
				mpu.incrementCycleCount(2);
				mpu.aReg &= ~BIT3;
				break;
			
			case 0xA0: // RES 4,B
				mpu.incrementCycleCount(2);
				mpu.bReg &= ~BIT4;
				break;
			
			case 0xA1: // RES 4,C
				mpu.incrementCycleCount(2);
				mpu.cReg &= ~BIT4;
				break;
			
			case 0xA2: // RES 4,D
				mpu.incrementCycleCount(2);
				mpu.dReg &= ~BIT4;
				break;
			
			case 0xA3: // RES 4,E
				mpu.incrementCycleCount(2);
				mpu.eReg &= ~BIT4;
				break;
			
			case 0xA4: // RES 4,H
				mpu.incrementCycleCount(2);
				mpu.hReg &= ~BIT4;
				break;
			
			case 0xA5: // RES 4,L
				mpu.incrementCycleCount(2);
				mpu.lReg &= ~BIT4;
				break;
			
			case 0xA6: // RES 4,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) & ~BIT4);
				break;
			
			case 0xA7: // RES 4,A
				mpu.incrementCycleCount(2);
				mpu.aReg &= ~BIT4;
				break;
			
			case 0xA8: // RES 5,B
				mpu.incrementCycleCount(2);
				mpu.bReg &= ~BIT5;
				break;
			
			case 0xA9: // RES 5,C
				mpu.incrementCycleCount(2);
				mpu.cReg &= ~BIT5;
				break;
			
			case 0xAA: // RES 5,D
				mpu.incrementCycleCount(2);
				mpu.dReg &= ~BIT5;
				break;
			
			case 0xAB: // RES 5,E
				mpu.incrementCycleCount(2);
				mpu.eReg &= ~BIT5;
				break;
			
			case 0xAC: // RES 5,H
				mpu.incrementCycleCount(2);
				mpu.hReg &= ~BIT5;
				break;
			
			case 0xAD: // RES 5,L
				mpu.incrementCycleCount(2);
				mpu.lReg &= ~BIT5;
				break;
			
			case 0xAE: // RES 5,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) & ~BIT5);
				break;
			
			case 0xAF: // RES 5,A
				mpu.incrementCycleCount(2);
				mpu.aReg &= ~BIT5;
				break;
			
			case 0xB0: // RES 6,B
				mpu.incrementCycleCount(2);
				mpu.bReg &= ~BIT6;
				break;
			
			case 0xB1: // RES 6,C
				mpu.incrementCycleCount(2);
				mpu.cReg &= ~BIT6;
				break;
			
			case 0xB2: // RES 6,D
				mpu.incrementCycleCount(2);
				mpu.dReg &= ~BIT6;
				break;
			
			case 0xB3: // RES 6,E
				mpu.incrementCycleCount(2);
				mpu.eReg &= ~BIT6;
				break;
			
			case 0xB4: // RES 6,H
				mpu.incrementCycleCount(2);
				mpu.hReg &= ~BIT6;
				break;
			
			case 0xB5: // RES 6,L
				mpu.incrementCycleCount(2);
				mpu.lReg &= ~BIT6;
				break;
			
			case 0xB6: // RES 6,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) & ~BIT6);
				break;
			
			case 0xB7: // RES 6,A
				mpu.incrementCycleCount(2);
				mpu.aReg &= ~BIT6;
				break;
			
			case 0xB8: // RES 7,B
				mpu.incrementCycleCount(2);
				mpu.bReg &= ~BIT7;
				break;
			
			case 0xB9: // RES 7,C
				mpu.incrementCycleCount(2);
				mpu.cReg &= ~BIT7;
				break;
			
			case 0xBA: // RES 7,D
				mpu.incrementCycleCount(2);
				mpu.dReg &= ~BIT7;
				break;
			
			case 0xBB: // RES 7,E
				mpu.incrementCycleCount(2);
				mpu.eReg &= ~BIT7;
				break;
			
			case 0xBC: // RES 7,H
				mpu.incrementCycleCount(2);
				mpu.hReg &= ~BIT7;
				break;
			
			case 0xBD: // RES 7,L
				mpu.incrementCycleCount(2);
				mpu.lReg &= ~BIT7;
				break;
			
			case 0xBE: // RES 7,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) & ~BIT7);
				break;
			
			case 0xBF: // RES 7,A
				mpu.incrementCycleCount(2);
				mpu.aReg &= ~BIT7;
				break;
			
			case 0xC0: // SET 0,B
				mpu.incrementCycleCount(2);
				mpu.bReg |= BIT0;
				break;
			
			case 0xC1: // SET 0,C
				mpu.incrementCycleCount(2);
				mpu.cReg |= BIT0;
				break;
			
			case 0xC2: // SET 0,D
				mpu.incrementCycleCount(2);
				mpu.dReg |= BIT0;
				break;
			
			case 0xC3: // SET 0,E
				mpu.incrementCycleCount(2);
				mpu.eReg |= BIT0;
				break;
			
			case 0xC4: // SET 0,H
				mpu.incrementCycleCount(2);
				mpu.hReg |= BIT0;
				break;
			
			case 0xC5: // SET 0,L
				mpu.incrementCycleCount(2);
				mpu.lReg |= BIT0;
				break;
			
			case 0xC6: // SET 0,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) | BIT0);
				break;
			
			case 0xC7: // SET 0,A
				mpu.incrementCycleCount(2);
				mpu.aReg |= BIT0;
				break;
			
			case 0xC8: // SET 1,B
				mpu.incrementCycleCount(2);
				mpu.bReg |= BIT1;
				break;
			
			case 0xC9: // SET 1,C
				mpu.incrementCycleCount(2);
				mpu.cReg |= BIT1;
				break;
			
			case 0xCA: // SET 1,D
				mpu.incrementCycleCount(2);
				mpu.dReg |= BIT1;
				break;
			
			case 0xCB: // SET 1,E
				mpu.incrementCycleCount(2);
				mpu.eReg |= BIT1;
				break;
			
			case 0xCC: // SET 1,H
				mpu.incrementCycleCount(2);
				mpu.hReg |= BIT1;
				break;
			
			case 0xCD: // SET 1,L
				mpu.incrementCycleCount(2);
				mpu.lReg |= BIT1;
				break;
			
			case 0xCE: // SET 1,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) | BIT1);
				break;
			
			case 0xCF: // SET 1,A
				mpu.incrementCycleCount(2);
				mpu.aReg |= BIT1;
				break;
			
			case 0xD0: // SET 2,B
				mpu.incrementCycleCount(2);
				mpu.bReg |= BIT2;
				break;
			
			case 0xD1: // SET 2,C
				mpu.incrementCycleCount(2);
				mpu.cReg |= BIT2;
				break;
			
			case 0xD2: // SET 2,D
				mpu.incrementCycleCount(2);
				mpu.dReg |= BIT2;
				break;
			
			case 0xD3: // SET 2,E
				mpu.incrementCycleCount(2);
				mpu.eReg |= BIT2;
				break;
			
			case 0xD4: // SET 2,H
				mpu.incrementCycleCount(2);
				mpu.hReg |= BIT2;
				break;
			
			case 0xD5: // SET 2,L
				mpu.incrementCycleCount(2);
				mpu.lReg |= BIT2;
				break;
			
			case 0xD6: // SET 2,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) | BIT2);
				break;
			
			case 0xD7: // SET 2,A
				mpu.incrementCycleCount(2);
				mpu.aReg |= BIT2;
				break;
			
			case 0xD8: // SET 3,B
				mpu.incrementCycleCount(2);
				mpu.bReg |= BIT3;
				break;
			
			case 0xD9: // SET 3,C
				mpu.incrementCycleCount(2);
				mpu.cReg |= BIT3;
				break;
			
			case 0xDA: // SET 3,D
				mpu.incrementCycleCount(2);
				mpu.dReg |= BIT3;
				break;
			
			case 0xDB: // SET 3,E
				mpu.incrementCycleCount(2);
				mpu.eReg |= BIT3;
				break;
			
			case 0xDC: // SET 3,H
				mpu.incrementCycleCount(2);
				mpu.hReg |= BIT3;
				break;
			
			case 0xDD: // SET 3,L
				mpu.incrementCycleCount(2);
				mpu.lReg |= BIT3;
				break;
			
			case 0xDE: // SET 3,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) | BIT3);
				break;
			
			case 0xDF: // SET 3,A
				mpu.incrementCycleCount(2);
				mpu.aReg |= BIT3;
				break;
			
			case 0xE0: // SET 4,B
				mpu.incrementCycleCount(2);
				mpu.bReg |= BIT4;
				break;
			
			case 0xE1: // SET 4,C
				mpu.incrementCycleCount(2);
				mpu.cReg |= BIT4;
				break;
			
			case 0xE2: // SET 4,D
				mpu.incrementCycleCount(2);
				mpu.dReg |= BIT4;
				break;
			
			case 0xE3: // SET 4,E
				mpu.incrementCycleCount(2);
				mpu.eReg |= BIT4;
				break;
			
			case 0xE4: // SET 4,H
				mpu.incrementCycleCount(2);
				mpu.hReg |= BIT4;
				break;
			
			case 0xE5: // SET 4,L
				mpu.incrementCycleCount(2);
				mpu.lReg |= BIT4;
				break;
			
			case 0xE6: // SET 4,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) | BIT4);
				break;
			
			case 0xE7: // SET 4,A
				mpu.incrementCycleCount(2);
				mpu.aReg |= BIT4;
				break;
			
			case 0xE8: // SET 5,B
				mpu.incrementCycleCount(2);
				mpu.bReg |= BIT5;
				break;
			
			case 0xE9: // SET 5,C
				mpu.incrementCycleCount(2);
				mpu.cReg |= BIT5;
				break;
			
			case 0xEA: // SET 5,D
				mpu.incrementCycleCount(2);
				mpu.dReg |= BIT5;
				break;
			
			case 0xEB: // SET 5,E
				mpu.incrementCycleCount(2);
				mpu.eReg |= BIT5;
				break;
			
			case 0xEC: // SET 5,H
				mpu.incrementCycleCount(2);
				mpu.hReg |= BIT5;
				break;
			
			case 0xED: // SET 5,L
				mpu.incrementCycleCount(2);
				mpu.lReg |= BIT5;
				break;
			
			case 0xEE: // SET 5,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) | BIT5);
				break;
			
			case 0xEF: // SET 5,A
				mpu.incrementCycleCount(2);
				mpu.aReg |= BIT5;
				break;
			
			case 0xF0: // SET 6,B
				mpu.incrementCycleCount(2);
				mpu.bReg |= BIT6;
				break;
			
			case 0xF1: // SET 6,C
				mpu.incrementCycleCount(2);
				mpu.cReg |= BIT6;
				break;
			
			case 0xF2: // SET 6,D
				mpu.incrementCycleCount(2);
				mpu.dReg |= BIT6;
				break;
			
			case 0xF3: // SET 6,E
				mpu.incrementCycleCount(2);
				mpu.eReg |= BIT6;
				break;
			
			case 0xF4: // SET 6,H
				mpu.incrementCycleCount(2);
				mpu.hReg |= BIT6;
				break;
			
			case 0xF5: // SET 6,L
				mpu.incrementCycleCount(2);
				mpu.lReg |= BIT6;
				break;
			
			case 0xF6: // SET 6,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) | BIT6);
				break;
			
			case 0xF7: // SET 6,A
				mpu.incrementCycleCount(2);
				mpu.aReg |= BIT6;
				break;
			
			case 0xF8: // SET 7,B
				mpu.incrementCycleCount(2);
				mpu.bReg |= BIT7;
				break;
			
			case 0xF9: // SET 7,C
				mpu.incrementCycleCount(2);
				mpu.cReg |= BIT7;
				break;
			
			case 0xFA: // SET 7,D
				mpu.incrementCycleCount(2);
				mpu.dReg |= BIT7;
				break;
			
			case 0xFB: // SET 7,E
				mpu.incrementCycleCount(2);
				mpu.eReg |= BIT7;
				break;
			
			case 0xFC: // SET 7,H
				mpu.incrementCycleCount(2);
				mpu.hReg |= BIT7;
				break;
			
			case 0xFD: // SET 7,L
				mpu.incrementCycleCount(2);
				mpu.lReg |= BIT7;
				break;
			
			case 0xFE: // SET 7,(HL)
				mpu.incrementCycleCount(4);
				index = (mpu.hReg << 8) | mpu.lReg;
				ram.writeMemory(index, ram.readMemory(index) | BIT7);
				break;
			
			case 0xFF: // SET 7,A
				mpu.incrementCycleCount(2);
				mpu.aReg |= BIT7;
				break;
			
			default:
				throw new AssertionError("Invalid byte following CB ompu.pcRegode");
			}
			break;
		
		case 0xCC: // CALL Z,nn
			if ((mpu.fReg & ZERO) != 0) {
				mpu.incrementCycleCount(6);
				ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, (mpu.pcReg + 2) >> 8);
				ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, (mpu.pcReg + 2) & 0x00FF);
				mpu.pcReg = ram.readMemory(mpu.pcReg) | (ram.readMemory(mpu.pcReg + 1) << 8);
			} else {
				mpu.incrementCycleCount(3);
				mpu.pcReg += 2;
			}
			break;
		
		case 0xCD: // CALL nn
			mpu.incrementCycleCount(6);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, (mpu.pcReg + 2) >> 8);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, (mpu.pcReg + 2) & 0x00FF);
			mpu.pcReg = ram.readMemory(mpu.pcReg) | (ram.readMemory(mpu.pcReg + 1) << 8);
			break;
		
		case 0xCE: // ADC A,n
			mpu.incrementCycleCount(2);
			memval = ram.readMemory(mpu.pcReg++) + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_ADD[(memval << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg + memval) & 0xFF;
			break;
		
		case 0xCF: // RST 08H
			mpu.incrementCycleCount(4);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg >> 8);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg & 0x00FF);
			mpu.pcReg = 0x0008;
			break;
		
		case 0xD0: // RET NC
			if ((mpu.fReg & CARRY) == 0) {
				mpu.incrementCycleCount(5);
				mpu.pcReg = ram.readMemory(mpu.spReg++) | (ram.readMemory(mpu.spReg++) << 8);
			} else {
				mpu.incrementCycleCount(2);
			}
			break;
		
		case 0xD1: // POP DE
			mpu.incrementCycleCount(3);
			mpu.eReg = ram.readMemory(mpu.spReg++);
			mpu.dReg = ram.readMemory(mpu.spReg++);
			break;
		
		case 0xD2: // JP NC,nn
			if ((mpu.fReg & CARRY) == 0) {
				mpu.incrementCycleCount(4);
				mpu.pcReg = ram.readMemory(mpu.pcReg) | (ram.readMemory(mpu.pcReg + 1) << 8);
			} else {
				mpu.incrementCycleCount(3);
				mpu.pcReg += 2;
			}
			break;
		
		case 0xD4: // CALL NC,nn
			if ((mpu.fReg & CARRY) == 0) {
				mpu.incrementCycleCount(6);
				ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, (mpu.pcReg + 2) >> 8);
				ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, (mpu.pcReg + 2) & 0x00FF);
				mpu.pcReg = ram.readMemory(mpu.pcReg) | (ram.readMemory(mpu.pcReg + 1) << 8);
			} else {
				mpu.incrementCycleCount(3);
				mpu.pcReg += 2;
			}
			break;
		
		case 0xD5: // PUSH DE
			mpu.incrementCycleCount(4);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.dReg);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.eReg);
			break;
		
		case 0xD6: // SUB A,n
			mpu.incrementCycleCount(2);
			memval = ram.readMemory(mpu.pcReg++);
			mpu.fReg = mpu.F_SUB[(memval << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - memval) & 0xFF;
			break;
		
		case 0xD7: // RST 10H
			mpu.incrementCycleCount(4);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg >> 8);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg & 0x00FF);
			mpu.pcReg = 0x0010;
			break;
		
		case 0xD8: // RET C
			if ((mpu.fReg & CARRY) != 0) {
				mpu.incrementCycleCount(5);
				mpu.pcReg = ram.readMemory(mpu.spReg++) | (ram.readMemory(mpu.spReg++) << 8);
			} else {
				mpu.incrementCycleCount(2);
			}
			break;
		
		case 0xD9: // RETI
			mpu.incrementCycleCount(4);
			mpu.pcReg = ram.readMemory(mpu.spReg++) | (ram.readMemory(mpu.spReg++) << 8);
			mpu.ime = true;
			break;
		
		case 0xDA: // JP C,nn
			if ((mpu.fReg & CARRY) != 0) {
				mpu.incrementCycleCount(4);
				mpu.pcReg = ram.readMemory(mpu.pcReg) | (ram.readMemory(mpu.pcReg + 1) << 8);
			} else {
				mpu.incrementCycleCount(3);
				mpu.pcReg += 2;
			}
			break;
		
		case 0xDC: // CALL C,nn
			if ((mpu.fReg & CARRY) != 0) {
				mpu.incrementCycleCount(6);
				ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, (mpu.pcReg + 2) >> 8);
				ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, (mpu.pcReg + 2) & 0x00FF);
				mpu.pcReg = ram.readMemory(mpu.pcReg) | (ram.readMemory(mpu.pcReg + 1) << 8);
			} else {
				mpu.incrementCycleCount(3);
				mpu.pcReg += 2;
			}
			break;
		
		case 0xDE: // SBC A,n
			mpu.incrementCycleCount(2);
			memval = ram.readMemory(mpu.pcReg++) + ((mpu.fReg & CARRY) >> 4);
			mpu.fReg = mpu.F_SUB[(memval << 8) | mpu.aReg];
			mpu.aReg = (mpu.aReg - memval) & 0xFF;
			break;
		
		case 0xDF: // RST 18H
			mpu.incrementCycleCount(4);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg >> 8);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg & 0x00FF);
			mpu.pcReg = 0x0018;
			break;
		
		case 0xE0: // LDH (n),A **WRITE TO ADDRESS N**
			mpu.incrementCycleCount(3);
			ram.writeMemory(0xFF00 | ram.readMemory(mpu.pcReg++), mpu.aReg);
			break;
		
		case 0xE1: // POP HL
			mpu.incrementCycleCount(3);
			mpu.lReg = ram.readMemory(mpu.spReg++);
			mpu.hReg = ram.readMemory(mpu.spReg++);
			break;
		
		case 0xE2: // LD (C),A **WRITE TO IO C**
			mpu.incrementCycleCount(2);
			ram.writeMemory(0xFF00 | mpu.cReg, mpu.aReg);
			break;
		
		case 0xE5: // PUSH HL
			mpu.incrementCycleCount(4);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.hReg);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.lReg);
			break;
		
		case 0xE6: // AND n
			mpu.incrementCycleCount(2);
			mpu.aReg &= ram.readMemory(mpu.pcReg++);
			mpu.fReg = HALF_CARRY | (((mpu.aReg - 1) >> 24) & ZERO);
			break;
		
		case 0xE7: // RST 20H
			mpu.incrementCycleCount(4);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg >> 8);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg & 0x00FF);
			mpu.pcReg = 0x0020;
			break;
		
		case 0xE8: // ADD mpu.spReg,n **ignores half-carry**
			mpu.incrementCycleCount(4);
			mpu.spReg += (byte) ram.readMemory(mpu.pcReg++); // signed immediate
			if (mpu.spReg > 0xFFFF) {
				mpu.spReg &= 0xFFFF;
				mpu.fReg = CARRY;
			} else {
				mpu.fReg = 0;
			}
			break;
		
		case 0xE9: // JP (HL)
			mpu.incrementCycleCount(1);
			mpu.pcReg = (mpu.hReg << 8) | mpu.lReg;
			break;
		
		case 0xEA: // LD (nn),A
			mpu.incrementCycleCount(4);
			ram.writeMemory(ram.readMemory(mpu.pcReg++) | (ram.readMemory(mpu.pcReg++) << 8), mpu.aReg);
			break;
		
		case 0xEE: // XOR n
			mpu.incrementCycleCount(2);
			mpu.aReg ^= ram.readMemory(mpu.pcReg++);
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xEF: // RST 28H
			mpu.incrementCycleCount(4);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg >> 8);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg & 0x00FF);
			mpu.pcReg = 0x0028;
			break;
		
		case 0xF0: // LDH (n),A **READ FROM ADDRESS N**
			mpu.incrementCycleCount(3);
			mpu.aReg = ram.readMemory(0xFF00 | ram.readMemory(mpu.pcReg++));
			break;
		
		case 0xF1: // POP AF
			mpu.incrementCycleCount(3);
			mpu.fReg = ram.readMemory(mpu.spReg++);
			mpu.aReg = ram.readMemory(mpu.spReg++);
			break;
		
		case 0xF2: // LD A,(C) **READ FROM IO C**
			mpu.incrementCycleCount(2);
			mpu.aReg = ram.readMemory(0xFF00 | mpu.cReg);
			break;
		
		case 0xF3: // DI
			mpu.incrementCycleCount(1);
			mpu.ime = false;
			// *Officially should occur 1 instruction later. We'll see
			// how it works...*
			break;
		
		case 0xF5: // PUSH AF
			mpu.incrementCycleCount(4);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.aReg);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.fReg);
			break;
		
		case 0xF6: // OR n
			mpu.incrementCycleCount(2);
			mpu.aReg |= ram.readMemory(mpu.pcReg++);
			mpu.fReg = ((mpu.aReg - 1) >> 24) & ZERO;
			break;
		
		case 0xF7: // RST 30H
			mpu.incrementCycleCount(4);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg >> 8);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg & 0x00FF);
			mpu.pcReg = 0x0030;
			break;
		
		case 0xF8: // LDHL mpu.spReg,n **ignores half-carry**
			mpu.incrementCycleCount(3);
			val = mpu.spReg + (byte) ram.readMemory(mpu.pcReg++); // signed
																	// immediate
			if (val > 0xFFFF) {
				val &= 0xFFFF;
				mpu.fReg = CARRY;
			} else {
				mpu.fReg = 0;
			}
			mpu.hReg = (val >> 8);
			mpu.lReg = (val & 0x00FF);
			break;
		
		case 0xF9: // LD mpu.spReg,HL
			mpu.incrementCycleCount(2);
			mpu.spReg = ((mpu.hReg << 8) | mpu.lReg);
			break;
		
		case 0xFA: // LD A,(nn)
			mpu.incrementCycleCount(4);
			mpu.aReg = ram.readMemory(ram.readMemory(mpu.pcReg++) | (ram.readMemory(mpu.pcReg++) << 8));
			break;
		
		case 0xFB: // EI
			mpu.incrementCycleCount(1);
			mpu.ime = true;
			// *Officially should occur 1 instruction later. We'll see
			// how it works...*
			break;
		
		case 0xFE: // CP n
			mpu.incrementCycleCount(2);
			mpu.fReg = mpu.F_SUB[(ram.readMemory(mpu.pcReg++) << 8) | mpu.aReg];
			break;
		
		case 0xFF: // RST 38H
			mpu.incrementCycleCount(4);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg >> 8);
			ram.writeMemory(mpu.spReg = (mpu.spReg - 1) & 0xFFFF, mpu.pcReg & 0x00FF);
			mpu.pcReg = 0x0038;
			break;
		
		default:
			throw new AssertionError("Unsupported ompu.pcRegode");
		}
	}
}
