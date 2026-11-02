
package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import Main.Game;
import World.Camera;
import World.Node;
import World.Vector2i;

public class Entity {
	
	public static BufferedImage LAVA_EN = Game.spritesheet.GetSprite(0, 128, 32, 32);
	public static BufferedImage LAVA_WALL_EN = Game.spritesheet.GetSprite(32, 64, 16, 16);
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.GetSprite(7*16, 0, 16,16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.GetSprite(8*16, 0, 16,16);
	public static BufferedImage BULLET_EN = Game.spritesheet.GetSprite(7*16, 16, 16,16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.GetSprite(8*16, 16, 16,16);
	public static BufferedImage ENEMY_DAMAGED = Game.spritesheet.GetSprite(16, 16, 16, 16);
	public static BufferedImage LEFT_WEAPON = Game.spritesheet.GetSprite(144, 0, 16,16);
	public static BufferedImage RIGHT_WEAPON = Game.spritesheet.GetSprite(128, 0, 16,16);
	public static BufferedImage LUZ_EN = Game.spritesheet.GetSprite(112, 32, 16,16);
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	public int depth;
	
	protected List<Node> path;
	
	private BufferedImage sprite;
	
	private int maskx,masky,mwidth,mheight;
	public int maskx1 = 8,masky1 = 8,maskw = 7,maskh = 10;
	
	public Entity(int x,int y,int width,int height,BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
		
		@Override
		
		public int compare(Entity n0,Entity n1) {
		if(n1.depth < n0.depth)
			return +1;
		if(n1.depth > n0.depth)
			return -1;
		return 0;
		}
	};
	
	public void setMask(int maskx,int masky,int mwidth,int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}
	
	public void setX(int NewX) {
		this.x = NewX;
	}
	
	public void setY(int NewY) {
		this.y = NewY;
	}
	
	public int getX() {
		return (int)this.x;
	}
	public int getY() {
		return (int)this.y;
	} 
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}

public void tick() {
}

public double calculateDistance(int x1,int y1,int x2,int y2) {
	return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
}

public boolean isColidding(int xnext,int ynext) {
	Rectangle enemyCurrent = new Rectangle(xnext + maskx1,ynext + masky1,maskw,maskh);
	for(int i = 0; i < Game.enemies.size(); i++) {
		Enemy e = Game.enemies.get(i);
		if(e == this) 
			continue;
		Rectangle targetEnemy = new Rectangle(e.getX() + maskx1,e.getY() + masky1,maskw,maskh);
		if(enemyCurrent.intersects(targetEnemy)) {
			return true;
		}
	}
	
	return false;
}


public void followPath(List<Node> path) {
	if(path != null) {
		if(path.size() > 0) {
			Vector2i target = path.get(path.size() - 1).tile;
			//xprev = x;
			//yprev = y;
			if(x < target.x * 16 && !isColidding(this.getX() + 1,this.getY())) {
				x++;
			}else if(x > target.x * 16 && !isColidding(this.getX() - 1,this.getY())) {
				x--;
			}
			if(y < target.y * 16 && !isColidding(this.getX(),this.getY() + 1)) {
				y++;
			}else if(y > target.y * 16 && !isColidding(this.getX(),this.getY() - 1)) {
				y--;
			}
			if(x == target.x * 16 && y == target.y * 16) {
				path.remove(path.size() - 1);
			}
		}
	}
}

public static boolean isColidding(Entity e1,Entity e2) {
	Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky,e1.mwidth,e1.mheight);
    Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky,e2.mwidth,e2.mheight);

    return e1Mask.intersects(e2Mask);
}    



	public void render(Graphics g) {
    	g.drawImage(sprite,this.getX() - Camera.x,this.getY() - Camera.y,null);
    	
    	
    }






}

