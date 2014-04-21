package net.yukkuricraft.tenko.oldemu;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

public final class GUI {
	
	public static final int screenWidth = 160;
	public static final int screenHeight = 144;
	private BufferedImage screen;
	private GameBoyInput lastInput;
	
	public GUI(){
		screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(screenWidth, screenHeight);
	}
	
	public void input(GameBoyInput input){
		this.lastInput = input;
	}
	
	public boolean getLeft(){
		return lastInput == GameBoyInput.LEFT;
	}
	
	public boolean getRight(){
		return lastInput == GameBoyInput.RIGHT;
	}
	
	public boolean getUp(){
		return lastInput == GameBoyInput.UP;
	}
	
	public boolean getDown(){
		return lastInput == GameBoyInput.DOWN;
	}
	
	public boolean getA(){
		return lastInput == GameBoyInput.A;
	}

	public boolean getB(){
		return lastInput == GameBoyInput.B;
	}
	
	public boolean getStart(){
		return lastInput == GameBoyInput.START;
	}
	
	public boolean getSelect(){
		return lastInput == GameBoyInput.SELECT;
	}
	
	public BufferedImage getFrame(){
		return this.screen;
	}
	
}