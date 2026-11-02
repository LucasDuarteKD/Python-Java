package entities;

import java.awt.image.BufferedImage;

import Main.Game;


public class Lava extends Entity {

	public static BufferedImage[] LAVA_WALL;
	
	
	public Lava(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width+16, height+14, sprite);
		depth = 0;
		
		LAVA_WALL = new BufferedImage[4];
	    for(int i = 0; i < 4; i++) {
	    	LAVA_WALL[i] = Game.spritesheet.GetSprite(32 + (i*16), 64, 16, 16);
	    	if(i == 4) {
	    		i = 0;
	    	}
	    }
	}

		 
	

}
