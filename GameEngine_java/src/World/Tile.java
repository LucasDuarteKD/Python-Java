package World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Main.Game;

public class Tile {

	
	public static BufferedImage TILE_FLOOR = Game.spritesheet.GetSprite(0, 0, 16, 16);
	public static BufferedImage TILE_FLOOR_01 = Game.spritesheet.GetSprite(0, 32, 16, 16);
	public static BufferedImage TILE_FLOOR_02 = Game.spritesheet.GetSprite(16, 32, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.GetSprite(16, 0, 16, 16);
	public static BufferedImage TILE_WALL_01 = Game.spritesheet.GetSprite(32, 64, 16, 16);
	
	
	
	
	private BufferedImage sprite;
	private int x,y;

	
	public Tile(int x,int y, BufferedImage sprite) {

		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	public void tick() {
	}
	
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
	

	
}
