package net.yukkuricraft.tenko.oldemu;

import java.awt.Color;
import java.awt.image.BufferedImage;

import net.minecraft.server.v1_7_R2.PacketPlayOutMap;
import net.minecraft.util.io.netty.channel.Channel;

import org.bukkit.map.MapPalette;

public class ScreenRenderer extends Thread {

	private BufferedImage frame;
	private Channel channel;
	private int id;

	public ScreenRenderer(BufferedImage image, Channel channel, int id){
		this.frame = image;
		this.channel = channel;
		this.id = id;
	}

	@SuppressWarnings("deprecation")
	public void run(){
		for(;;){
			for(int x = 0; x < 128; x++){
				byte[] packetData = new byte[131];
				packetData[1] = (byte)x;
				
				for(int y = 0; y < 128; y++){
					try{
						packetData[y + 3] = MapPalette.matchColor(new Color(frame.getRGB(x, y)));
					}catch (ArrayIndexOutOfBoundsException e){
						packetData[y + 3] = MapPalette.TRANSPARENT;
					}
				}

				channel.write(new PacketPlayOutMap(id, packetData));
			}

			channel.flush();
		}
	}

}
