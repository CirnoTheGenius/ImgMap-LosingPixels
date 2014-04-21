package net.yukkuricraft.tenko.emunew;

import static net.yukkuricraft.tenko.emunew.BitConstants.*;

public class InputHandler {

	private Input input;

	public int handle(int[][] memory, int value) {
		int memInput = value | 0x0F;

		if ((value & BIT5) == 0) {
			if (input.start) {
				memInput &= ~BIT3;
			}

			if (input.select) {
				memInput &= ~BIT2;
			}

			if (input.b) {
				memInput &= ~BIT1;
			}

			if (input.a) {
				memInput &= ~BIT0;
			}
		} else if ((value & BIT4) == 0) {
			if (input.down) {
				memInput &= ~BIT3;
			}

			if (input.up) {
				memInput &= ~BIT2;
			}

			if (input.left) {
				memInput &= ~BIT1;
			}

			if (input.right) {
				memInput &= ~BIT0;
			}
		}

		return memory[7][0x1F00] = memInput;
	}

}
