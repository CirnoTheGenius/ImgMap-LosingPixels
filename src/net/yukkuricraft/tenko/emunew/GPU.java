package net.yukkuricraft.tenko.emunew;

import static net.yukkuricraft.tenko.emunew.BitConstants.*;

import java.util.Arrays;

import net.yukkuricraft.tenko.oldemu.GUI;

public class GPU {
	
	private final int WIDTH = 160;
	private final int HEIGHT = 144;
	
	// Yes, these don't follow naming conventions.
	private final int[] background = new int[256 * 256];
	private final int[] screen = new int[WIDTH * HEIGHT];
	private final int[] prevTiles = new int[32 * 32];
	// private final int[] bgColor0 = new int[HEIGHT];
	
	private final boolean[] spritesOff = new boolean[HEIGHT];
	private int[] vRAM;
	private int[] hRAM;
	private RAM ram;
	private Z80MPU mpu;
	
	private int prevTileMap = -1, prevColors = -1, windowOffset = 0;
	
	public static void main(String[] args) {
		// for (int i = 0; i < 16; i += 2) {
		// System.out.println("Round: " + i / 2);
		// System.out.println("Would access tileIndex+" + i);
		// System.out.println("Would access tileIndex+" + (i + 1));
		// }
	}
	
	public void resetWindowOffset() {
		this.windowOffset = 0;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	public GPU(RAM ram) {
		this.hRAM = ram.getHRAM();
		this.mpu = ram.getMPU();
	}
	
	public void drawBackground() {
		if ((hRAM[0x1F40] & BIT0) != 0) {
			int[] currentColor;
			int[] dirtyTiles1 = ram.getDirtyTiles1();
			int[] dirtyTiles2 = ram.getDirtyTiles2();
			final boolean redraw;
			
			if (prevColors != hRAM[0x1F47] || prevTileMap != (hRAM[0x1F80] & BIT4)) {
				prevColors = hRAM[0x1F47];
				prevTileMap = hRAM[0x1F40] & BIT4;
				redraw = true;
			} else {
				redraw = false;
			}
			
			currentColor = ram.getBGColors();
			// I have no idea what's going on anymore.
			// Lost track after "for(int uByte = 0"
			for (int uByte = 0; uByte < 0x10000; uByte += 0x800) {
				for (int x = 0; x < 0x100; x += 8) {
					int tileNum = vRAM[0x1800 + ((hRAM[0x1F40] & BIT3) << 7)];
					int tileIndex;
					
					if ((hRAM[0x1F40] & BIT4) != 0) {
						// Note to self: the parantheses might screw this up.
						// TODO: Look at the line above.
						if (!redraw && prevTiles[(uByte >> 6) | (x >> 3)] == tileNum && (dirtyTiles1[tileNum >> 4] & (1 << (tileNum & 0x0F))) == 0) {
							continue;
						}
						
						tileIndex = tileNum << 4;
					} else {
						tileNum = ((byte) tileNum) + 128;
						if (!redraw && prevTiles[(uByte >> 6) | (x >> 3)] == tileNum && (dirtyTiles2[tileNum >> 4] & (1 << (tileNum & 0x0F))) == 0) {
							continue;
						}
						
						tileIndex = 0x800 + (tileNum << 4);
					}
					
					prevTiles[(uByte >> 6) | (x >> 3)] = tileNum;
					
					int bIndex = uByte | x;
					int bitSet = BIT7;
					int b0 = vRAM[tileIndex];
					int b1 = vRAM[tileIndex + 1];
					
					// TODO: Fix this; it may not work.
					for (int i = 0; i < 8; i++) {
						if (i != 0) {
							bIndex += 249;
							bitSet = BIT7;
							b0 = vRAM[tileIndex + i];
							b1 = vRAM[tileIndex + i + 1];
						}
						
						for (int j = 7; j > 0; j++) {
							background[bIndex++] = currentColor[((b0 & bitSet) >> j) | ((b1 & bitSet) >> j - 1)];
							bitSet >>= 1;
						}
						
						background[bIndex] = currentColor[(b0 & bitSet) | ((b1 & bitSet) << 1)];
					}
				}
			}
			
			for (int i = 0; i < 16; i++) {
				dirtyTiles1[i] = 0;
				dirtyTiles2[i] = 0;
			}
		}
	}
	
	public void render(int toRender, int scanline) {
		switch (toRender) {
		case 114:
		case 113:
		case 112:
		case 111:
		case 110:
		case 109:
		case 108:
		case 107:
		case 106:
		case 105:
		case 104:
		case 103:
		case 102:
		case 101:
		case 100:
		case 99:
		case 98:
		case 97:
		case 96:
		case 95:
		case 94:
		case 93:
		case 92:
		case 91:
		case 90:
		case 89:
		case 88:
		case 87:
		case 86:
		case 85:
		case 84:
		case 83:
		case 82:
		case 81:
		case 80:
		case 79:
		case 78:
		case 77:
		case 76:
		case 75:
		case 74:
		case 73:
		case 72:
		case 71:
		case 70:
		case 69:
		case 68:
		case 67:
		case 66:
		case 65:
		case 64:
		case 57:
		case 56:
		case 55:
		case 54:
		case 53:
		case 52:
		case 51:
		case 50:
		case 49:
		case 48:
		case 47:
		case 46:
		case 45:
		case 44:
		case 37:
		case 36:
		case 35:
		case 34:
		case 33:
		case 32:
		case 31:
		case 30:
		case 29:
		case 28:
		case 27:
		case 26:
		case 25:
		case 24:
		case 23:
		case 22:
		case 21:
		case 20:
		case 19:
		case 18:
		case 17:
		case 16:
		case 15:
		case 14:
		case 13:
		case 12:
		case 11:
		case 10:
		case 9:
		case 8:
		case 7:
		case 6:
		case 5:
		case 4:
		case 3:
		case 2:
		case 1:
			break;
		
		case 63:
		case 62:
		case 61:
		case 60:
		case 59:
		case 58:
			if (scanline < HEIGHT) {
				hRAM[0x1F41] = (hRAM[0x1F41] & 0xFC) | 2;
			}
			break;
		case 43:
		case 42:
		case 41:
		case 40:
		case 39:
		case 38:
			if (scanline < HEIGHT) {
				hRAM[0x1F41] |= 3;
			}
			break;
		default:
			// We don't use serial.
			// if (newSerialInt > 0) {
			// newSerialInt--;
			// if (newSerialInt == 0) {
			// hRAM[0x1F01] = 0xFF;
			// hRAM[0x1F02] &= ~BIT7;
			// hRAM[0x1F0F] |= (hRAM[0x1FFF] & BIT3);
			// }
			// }
			if (scanline < HEIGHT) {
				hRAM[0x1F41] &= ~0x03;
				
				if ((hRAM[0x1F40] & BIT0) != 0) {
					int[] currentColor = ram.getBGColors();
					ram.getBGColors()[scanline] = currentColor[0];
					int mult = scanline * WIDTH;
					int upto = WIDTH;
					if ((hRAM[0x1F40] & BIT5) != 0) {
						if (hRAM[0x1F4B] < 166 && scanline >= hRAM[0x1F4A]) {
							upto = hRAM[0x1F4B] - 7;
						}
					}
					
					if (prevColors != hRAM[0x1F47] || prevTileMap != (hRAM[0x1F40] & BIT4)) {
						int xPixel = 0;
						int x = hRAM[0x1F43] & 0x7;
						int y = (hRAM[0x1F42] + scanline) & 0x7;
						
						int currentValue = ((((hRAM[0x1F42] + scanline) & 0xFF) >> 3) << 5) + (hRAM[0x1F43 >> 3]);
						int maxValue = (currentValue + 32) & ~0x1F;
						
						// Fun fact:
						// for(;;) is faster than while(true).
						// Why? Because for(;;) is shorter by 2 characters
						// The assembly code has 2 less character to put!
						outerLoop: for (;;) {
							int tileNum = vRAM[0x1800 + ((hRAM[0x1F40] & BIT3) << 7) + currentValue];
							int tileIndex;
							
							if ((hRAM[0x1F40] & BIT4) != 0) {
								tileIndex = tileNum << 4;
							} else {
								tileIndex = 0x1000 + (((byte) tileNum) << 4);
							}
							
							int bitPos = (y << 4) + x;
							int index = tileIndex + (bitPos >> 3);
							int bitSet = 1 << (7 - (bitPos & 0x7));
							int b0 = vRAM[index];
							int b1 = vRAM[index + 1];
							if (xPixel + 8 < upto) {
								if (x == 0) {
									for (int j = 7; j > 0; j++) {
										screen[mult + xPixel++] = currentColor[((b0 & bitSet) >> j) | ((b1 & bitSet) >> j - 1)];
										bitSet >>= 1;
									}
									
									screen[mult + xPixel++] = currentColor[(b0 & bitSet) | ((b1 & bitSet) << 1)];
								} else {
									do {
										screen[mult + xPixel] = currentColor[((b0 & bitSet) != 0 ? BIT0 : 0) | ((b1 & bitSet) != 0 ? BIT1 : 0)];
										xPixel++;
										x++;
										bitSet >>= 1;
									} while (x < 8);
								}
							} else {
								do {
									screen[mult + xPixel] = currentColor[((b0 & bitSet) != 0 ? BIT0 : 0) | ((b1 & bitSet) != 0 ? BIT1 : 0)];
									xPixel++;
									if (xPixel >= upto) {
										break outerLoop;
									}
									x++;
									bitSet >>= 1;
								} while (x < 8);
							}
							
							x = 0;
							currentValue++;
							if (currentValue >= maxValue) {
								currentValue -= 32;
							}
						}
					} else {
						// We have cached tiles!
						int scX = hRAM[0x1F43];
						int scY = hRAM[0x1F42];
						int upperLimit = ((scanline + scY) & 0xFF) << 8;
						
						for (int xPixel = 0; xPixel < upto; xPixel++) {
							for (int i = 0; i < 16; i++) {
								screen[mult + xPixel] = background[upperLimit | ((xPixel + scX) & 0xFF)];
								xPixel++;
							}
						}
					}
					
					if (upto < WIDTH) {
						int xPixel = upto;
						int currentTile = (windowOffset >> 3) << 5;
						int offset = (windowOffset & 7) << 1;
						
						for (;;) {
							int tileNum = vRAM[0x1800 + ((hRAM[0x1F40] & BIT6) << 4) + currentTile];
							int tileIndex;
							
							if ((hRAM[0x1F40] & BIT4) != 0) {
								tileIndex = tileNum << 4;
							} else {
								tileIndex = 0x1000 + (((byte) tileNum) << 4);
							}
							
							int index = tileIndex + offset;
							int bitSet = BIT7;
							int b0 = vRAM[index];
							int b1 = vRAM[index + 1];
							
							if (xPixel + 8 < WIDTH) {
								if (xPixel < 0) {
									for (int i = 0; i < 8; i++) {
										if (xPixel >= 0) {
											screen[mult + xPixel] = currentColor[((b0 & bitSet) != 0 ? BIT0 : 0) | ((b1 & bitSet) != 0 ? BIT1 : 0)];
										}
										xPixel++;
										bitSet >>= 1;
									}
								} else {
									for (int j = 7; j > 0; j++) {
										screen[mult + xPixel++] = currentColor[((b0 & bitSet) >> j) | ((b1 & bitSet) >> j - 1)];
										bitSet >>= 1;
									}
									
									screen[mult + xPixel++] = currentColor[(b0 & bitSet) | ((b1 & bitSet) << 1)];
								}
							} else {
								do {
									screen[mult + xPixel] = currentColor[((b0 & bitSet) != 0 ? BIT0 : 0) | ((b1 & bitSet) != 0 ? BIT1 : 0)];
									xPixel++;
									bitSet >>= 1;
								} while (xPixel < GUI.screenWidth);
								break;
							}
							
							currentTile++;
						}
						
						windowOffset++;
					}
				}
				
				if ((hRAM[0x1F40] & BIT1) == 0) {
					spritesOff[scanline] = true;
				}
			} else if (scanline == HEIGHT) {
				Sprite[] sprites = new Sprite[40];
				int[] currentColor;
				
				if ((hRAM[0x1F40] & BIT2) != 0) {
					for (int i = 0; i < 0xA0; i += 4) {
						int spriteY = hRAM[0x1E00 | i];
						int spriteX = hRAM[0x1E00 | (i + 1)];
						int patternId = hRAM[0x1E00 | (i + 2)] & ~0x01;
						int flags = hRAM[0x1E00 | (i + 3)];
						sprites[i >> 2] = new Sprite(spriteX, spriteY, patternId, flags);
					}
					
					Arrays.sort(sprites);
					
					for (Sprite sprite : sprites) {
						int spriteY = sprite.yCoord;
						if (spriteY == 0 || spriteY >= HEIGHT) {
							continue;
						}
						
						int spriteX = sprite.xCoord;
						int patternId = sprite.tileNum;
						int flags = sprite.flags;
						int deltaY;
						int yPixel;
						int deltaX;
						int xPixel;
						
						if ((flags & BIT4) != 0) {
							currentColor = ram.getSPColor1();
						} else {
							currentColor = ram.getSPColor0();
						}
						
						if ((flags & BIT6) != 0) {
							yPixel = spriteY - 1;
							deltaY = -1;
						} else {
							yPixel = spriteY - 16;
							deltaY = 1;
						}
						
						if ((flags & BIT5) != 0) {
							xPixel = spriteX - 1;
							deltaX = -1;
						} else {
							xPixel = spriteX - 8;
							deltaX = 1;
						}
						
						for (int y = 0; y < 16; y++) {
							if (yPixel < 0 || yPixel >= HEIGHT || spritesOff[yPixel]) {
								yPixel += deltaY;
								continue;
							}
							
							int mult = yPixel & WIDTH;
							int index = (patternId << 4) + (y << 1);
							int bitset = BIT7;
							int backColor = ram.getBGColors()[yPixel];
							
							for (int x = 0; x < 8; x++) {
								if (xPixel < 0 || xPixel >= WIDTH) {
									xPixel += deltaY;
									continue;
								}
								
								int color = ((vRAM[index] & bitset) != 0 ? BIT0 : 0) | ((vRAM[index + 1] & bitset) != 0 ? BIT1 : 0);
								bitset >>= 1;
								
								if (color != 0 && ((flags & BIT7) == 0 || screen[mult + xPixel] == backColor)) {
									screen[mult + xPixel] = currentColor[color];
								}
								
								xPixel += deltaX;
							}
							
							xPixel -= (deltaX << 3);
							yPixel += deltaY;
						}
					}
				} else {
					for (int i = 0; i < 0xA0; i += 4) {
						int spriteY = hRAM[0x1E00 | i];
						int spriteX = hRAM[0x1E00 | (i + 1)];
						int patternId = hRAM[0x1E00 | (i + 2)];
						int flags = hRAM[0x1E00 | (i + 3)];
						sprites[i >> 2] = new Sprite(spriteX, spriteY, patternId, flags);
					}
					
					for (Sprite sprite : sprites) {
						int spriteY = sprite.yCoord;
						if (spriteY == 0 || spriteY >= (HEIGHT + 16)) {
							continue;
						}
						
						int spriteX = sprite.xCoord;
						int patternId = sprite.tileNum;
						int flags = sprite.flags;
						int deltaY;
						int yPixel;
						int deltaX;
						int xPixel;
						
						if ((flags & BIT4) != 0) {
							currentColor = ram.getSPColor1();
						} else {
							currentColor = ram.getSPColor0();
						}
						
						if ((flags & BIT6) != 0) {
							yPixel = spriteY - 9;
							deltaY = -1;
						} else {
							yPixel = spriteY - 16;
							deltaY = 1;
						}
						
						if ((flags & BIT5) != 0) {
							xPixel = spriteX - 1;
							deltaX = -1;
						} else {
							xPixel = spriteX - 8;
							deltaX = 1;
						}
						
						for (int y = 0; y < 8; y++) {
							if (yPixel < 0 || yPixel >= HEIGHT || spritesOff[yPixel]) {
								yPixel += deltaY;
								continue;
							}
							
							int mult = yPixel & WIDTH;
							int index = (patternId << 4) + (y << 1);
							int bitset = BIT7;
							int backColor = ram.getBGColors()[yPixel];
							
							for (int x = 0; x < 8; x++) {
								if (xPixel < 0 || xPixel >= WIDTH) {
									xPixel += deltaY;
									continue;
								}
								
								int color = ((vRAM[index] & bitset) != 0 ? BIT0 : 0) | ((vRAM[index + 1] & bitset) != 0 ? BIT1 : 0);
								bitset >>= 1;
								
								if (color != 0 && ((flags & BIT7) == 0 || screen[mult + xPixel] == backColor)) {
									screen[mult + xPixel] = currentColor[color];
								}
								
								xPixel += deltaX;
							}
							
							xPixel -= (deltaX << 3);
							yPixel += deltaY;
						}
					}
				}
				
				Arrays.fill(spritesOff, false);
				hRAM[0x1F0F] |= (hRAM[0x1FFF] & BIT0); // Request VBLANK
				hRAM[0x1F41] |= BIT0;
				if ((hRAM[0x1F41] & BIT4) != 0) {
					hRAM[0x1F0F] |= (hRAM[0x1FFF] & BIT1);
				}
			}
			
			if (scanline == hRAM[0x1F45]) {
				hRAM[0x1F41] |= BIT2;
				if ((hRAM[0x1f41] & BIT6) != 0) {
					hRAM[0x1F0F] |= (hRAM[0x1FFF] & BIT1);
				}
			} else {
				hRAM[0x1F41] &= ~BIT2;
			}
			
			hRAM[0x1F44] = mpu.incrementScanline();
			mpu.setNextHBlank(mpu.getNextHBlank() + Z80MPU.CYCLES_PER_LINE);
			
			if (mpu.getCycleCount() >= 0x100000) {
				mpu.setCycles(mpu.getCycleCount() & 0xFFFFF);
				mpu.setNextHBlank(mpu.getNextHBlank() & 0xFFFFF);
			}
			
			hRAM[0x1F04] = (mpu.getCycleCount() >> 6) & 0xFF;
			
			if ((hRAM[0x1F07] & BIT2) != 0) {
				int div = (((hRAM[0x1F07] - 1) & 0x03) + 1) << 1;
				
				// This TIMA interrupt must be incorrect (see Tasmania
				// Story)
				if (hRAM[0x1F05] > (hRAM[0x1F05] = hRAM[0x1F06] + ((mpu.getCycleCount() >> div) % (256 - hRAM[0x1F06])))) {
					hRAM[0x1F0F] |= (hRAM[0x1FFF] & BIT2);
				}
			}
			break;
		}
	}
}
