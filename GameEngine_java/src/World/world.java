package World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Main.Game;
import entities.*;
import graficos.Spritesheet;


public class world {
	
    public static Tile[] tiles;
    public static int WIDTH,HEIGHT;
    public static final int TILE_SIZE = 16;
    public static final int GRID_SIZE = 14;
	
	public world(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(),pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy =0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					if(Game.CUR_LEVEL == 1 || Game.CUR_LEVEL == 2) {
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					}if(Game.CUR_LEVEL == 3) {
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR_01);
					}
					if(pixelAtual == 0xFF000000) {
						//Floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					}else if(pixelAtual == 0xFF636363) {
						//Floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR_01);
					}else if(pixelAtual == 0xFF565656) {
						//Floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR_02);
					}else if(pixelAtual == 0xFFFFFFFF) {
						//parede
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_WALL);
					}else if (pixelAtual == 0xFFFFF607) {
						Lava z = new Lava(xx*16,yy*16,16,16,Entity.LAVA_EN);
						Game.entities.add(z);
						//Lava
					}else if(pixelAtual == 0xFFFF6A00) {
						//player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}else if(pixelAtual == 0xFF7F006E) {
						//Enemy
						Enemy en = (new Enemy(xx*16,yy*16,16,16,Entity.ENEMY_EN));
						Game.enemies.add(en);
						Game.entities.add(en);
					}else if(pixelAtual == 0xFF0026FF) {
						//Bullet
						Game.entities.add(new Weapon(xx*16,yy*16,16,16,Entity.WEAPON_EN));
					}else if(pixelAtual == 0xFF00FFFF) {
						//Weapon
						Game.entities.add(new Bullet(xx*16,yy*16,16,16,Entity.BULLET_EN));
					}else if(pixelAtual == 0xFFFF0000) {
						Lifepack pack = new Lifepack(xx*16,yy*16,16,16,Entity.LIFEPACK_EN);
						Game.entities.add(pack);
						//Life_Pack
					}else if(pixelAtual == 0xFFFFF642) {
						Luz luz = new Luz(xx*16,yy*16,16,16,Entity.LUZ_EN);
						Game.entities.add(luz);
					}
				
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void tick() {
		
	}
	
	
	public void render(Graphics g) {
		int xstart = Camera.x>>4;
		int ystart = Camera.y>>4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
	public static void renderMiniMap() {
		for(int i = 0; i < Game.minimapaPixels.length; i++) {
			Game.minimapaPixels[i] = 0;
		}
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				if(tiles[xx + (yy*WIDTH)] instanceof WallTile) {
					Game.minimapaPixels[xx + (yy*WIDTH)] = 0xFFFFFFFF;
					
				}
			}
		}
		
		int xPlayer = Game.player.getX()/16;
		int yPlayer = Game.player.getY()/16;

		Game.minimapaPixels[xPlayer + (yPlayer*WIDTH)] = 0xFFFF6A00;
			}
	
	public static boolean isFree(int xnext,int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE) / TILE_SIZE;

		return !((tiles[(x1 + (y1*(world.WIDTH)))] instanceof WallTile) ||
				(tiles[x2 + (y2*world.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*world.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*world.WIDTH)] instanceof WallTile));
	
	}
	
public static void restartGame(String level) {
	Game.entities.clear();
	Game.enemies.clear();
	Game.entities = new ArrayList<Entity>();
    Game.enemies = new ArrayList<Enemy>();
    Game.spritesheet = new Spritesheet("/spritsheet.png");
    Game.player = new Player(0,0,16,16,Game.spritesheet.GetSprite(32, 0, 16, 16));
    Game.entities.add(Game.player);
    Game.world = new world("/"+level);
    Game.minimapa = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    Game.minimapaPixels = ((DataBufferInt)Game.minimapa.getRaster().getDataBuffer()).getData();
    return;
}




}
