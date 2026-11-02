package World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Main.Game;

public class WallLava extends Tile {
	
	private int index;
	private BufferedImage[] Lava;

	public WallLava(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);	
		Lava = new BufferedImage[4];
		for(index = 0; index < 4; index++) {
			Lava[index] = Game.spritesheet.GetSprite(32 + (index*16), 64, 16, 16);
		}
	}

	public void tick() {
		
	}
	
	public void render(Graphics g) {
	}
	
}
