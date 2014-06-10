package net.yukkuricraft.tenko.gbemulator;

import static net.yukkuricraft.tenko.gbemulator.BitConstants.*;

public class InputHandler {
	
	private Input input = new Input();
	
	public int handle(int[][] memory, int value){
		int memInput = value | 0x0F;
		
		if((value & BIT5) == 0){
			if(input.start){
				memInput &= ~BIT3;
				input.start = false;
			}
			
			if(input.select){
				memInput &= ~BIT2;
				input.select = false;
			}
			
			if(input.b){
				memInput &= ~BIT1;
				input.b = false;
			}
			
			if(input.a){
				memInput &= ~BIT0;
				input.a = false;
			}
		}else if((value & BIT4) == 0){
			if(input.down){
				memInput &= ~BIT3;
				if(!input.hdown){
					input.down = false;
				}
			}
			
			if(input.up){
				memInput &= ~BIT2;
				if(!input.hup){
					input.up = false;
				}
			}
			
			if(input.left){
				memInput &= ~BIT1;
				if(!input.hleft){
					input.left = false;
				}
			}
			
			if(input.right){
				memInput &= ~BIT0;
				if(!input.hright){
					input.right = false;
				}
			}
		}
		
		return memory[7][0x1F00] = memInput;
	}
	
	public Input getInput(){
		return input;
	}
	
}
