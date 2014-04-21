package net.yukkuricraft.tenko.emunew;

public final class BitConstants {
	
	private BitConstants() {
	}
	
	// Look at bit-shifty to see button->code.
	public static final int BIT7 = 1 << 7; // I don't see why
	public static final int BIT6 = 1 << 6; // these two exist.
	public static final int BIT5 = 1 << 5; // Select Button
	public static final int BIT4 = 1 << 4; // Select / D-PAD Direction
	public static final int BIT3 = 1 << 3; // Down / Start
	public static final int BIT2 = 1 << 2; // Up / Select
	public static final int BIT1 = 1 << 1; // B / Left
	public static final int BIT0 = 1 << 0; // A / Right
	public static final int ZERO = BIT7;
	public static final int SUBTRACT = BIT6;
	public static final int HALF_CARRY = BIT5;
	public static final int CARRY = BIT4;
	
}
