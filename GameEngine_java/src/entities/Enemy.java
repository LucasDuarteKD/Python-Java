package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import Main.Game;
import Main.Menu;
import Main.Sound;
import World.AStar;
import World.Camera;
import World.Node;
import World.Vector2i;

public class Enemy extends Entity {
	
	private int life = 1;
	
	private boolean damaged = false;
	private int damageFrames = 10, damageCurrent = 0;
	
	
	private int frames = 0, maxFrames = 30, maxIndex = 1, index = 0;
	private boolean moved = false;
	
	protected  List<Node> path;
	
	private BufferedImage[] sprites;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.GetSprite(128, 16, 16, 16);
		sprites[1] = Game.spritesheet.GetSprite(144, 16, 16, 16);
	}

public void tick() {
	depth = 1;
	maskx1 = 5;
	masky1 = 5;
	maskw = 8;
	maskh = 8;
	
	
	if(this.calculateDistance(this.getX(), this.getY(), Game.player.getX(),Game.player.getY()) < 200) {
	if(!isColiddingWithPlay()) {
	if(path == null || path.size() > 0){
		moved = true;
		Vector2i start = new Vector2i((int)(x/16),(int)(y/16));
		Vector2i end = new Vector2i((int)(Game.player.x/16),(int)(Game.player.y/16));
		path = AStar.findPath(Game.world, start, end);
	     
	}
	}else {
		moved = false;
		if(new Random().nextInt(100) < 10) {
			if(Menu.Som == true) {
			Sound.hurt.play();
			}
			Game.player.life-=Game.rand.nextInt(3);
			Game.player.isDamaged = true;
		}
	}
	if(new Random().nextInt(100) < 65)
	followPath(path);
	if(new Random().nextInt(100) < 5) {
		Vector2i start = new Vector2i((int)(x/16),(int)(y/16));
		Vector2i end = new Vector2i((int)(Game.player.x/16),(int)(Game.player.y/16));
		path = AStar.findPath(Game.world, start, end);
	}
	}
	if (moved == false) {
		index = 0;
	}
	
	else if(moved) {
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) 
				index = 0;
			}}
    collidingBullet();
    
    if(damaged) {
		this.damageCurrent++;
		if(this.damageCurrent == this.damageFrames) {
			this.damageCurrent = 0;
			damaged = false;
		}
	}
    
    if(life <= 0) {
    destroySelf();
    return;
    }

}

public void destroySelf() {
	Game.enemies.remove(this);
    Game.entities.remove(this);
}

public void collidingBullet() {
	for(int i = 0; i < Game.bullets.size(); i++) {
		Entity e = Game.bullets.get(i);
		if(e instanceof BulletShoot) {
			if(Entity.isColidding(this, e)) {
				damaged = true;
				life--;
				Game.bullets.remove(i);
				return;
			}
			}
		}
	
}

	
public boolean isColiddingWithPlay() {
	Rectangle enemyCurrent = new Rectangle(this.getX() + maskx1,this.getY() + masky1,maskw,maskh);
	Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),16,16);
	
	
	
	return enemyCurrent.intersects(player);
}




public void render(Graphics g) {
	if(!damaged) {
	g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}else {
	g.drawImage(Entity.ENEMY_DAMAGED, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
}




}
