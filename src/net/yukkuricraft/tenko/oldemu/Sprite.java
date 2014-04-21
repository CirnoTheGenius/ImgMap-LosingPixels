package net.yukkuricraft.tenko.oldemu;

public final class Sprite implements Comparable<Sprite> {
	public final int xCoord, yCoord, tileNum, flags;
	
	public Sprite(int xCoord, int yCoord, int tileNum, int flags) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.tileNum = tileNum;
		this.flags = flags;
	}
	
	@Override
	public int compareTo(Sprite other){
		return other.xCoord - this.xCoord;
	}
}
