package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import Main.Game;
import World.Camera;
import World.world;

public class Player extends Entity {

	public boolean right,up,left,down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public int up_dir = 2;
	public int down_dir = 3;
	private double speed = 1.5;
	
	private int frames = 0, maxFrames = 7, maxIndex = 3;
	public static int index = 0;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[]	leftPlayer;	
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage playerDamage;
	
	public static boolean hasGun = false;
	public boolean hasLuz = false;
	
	public int ammo = 0;
	public int vspd = 0;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	public boolean shoot = false,mouseShoot = false;
	
	public double life = 100 ,maxlife = 100;
	public int mx,my;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	
	rightPlayer = new BufferedImage[4];
    leftPlayer = new BufferedImage[4];
    upPlayer = new BufferedImage[4];
    downPlayer = new BufferedImage[4];
    playerDamage = Game.spritesheet.GetSprite(0, 16, 16, 16);
    for(int i = 0; i < 4; i++) {
    	rightPlayer[i] = Game.spritesheet.GetSprite(32 + (i*16), 0, 16, 16);
    } for(int i = 0; i < 4; i++) {
    	leftPlayer[i] = Game.spritesheet.GetSprite(80 + (i*-16), 16, 16, 16);
    } for(int i = 0; i < 4; i++) {
    	upPlayer[i] = Game.spritesheet.GetSprite(32 + (i*16), 48, 16, 16);
    } for(int i = 0; i < 4; i++) {
    	downPlayer[i] = Game.spritesheet.GetSprite(32 + (i*16), 32, 16, 16);
    }
	}

	public void tick() {
		depth = 2;
		moved = false;

		if(right && world.isFree((int)(x+speed),this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;
			}
		else if(left && world.isFree((int)(x-speed),(this.getY()))) {
			moved = true;
			dir = left_dir;
			x-=speed; 
			}
		else if(up && world.isFree(this.getX(),(int)(y-speed))) {
			speed = 1.5;
			moved = true;
			dir = up_dir;
			y-=speed;
			}
		else if(down && world.isFree(this.getX(),(int)(y+speed))) {
			moved = true;
			dir = down_dir;
			y+=speed;
			}
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) 
					index = 0;
				} 
			}
		checkCollisionLifePack();
		checkCollisionGun();
		checkCollisionAmmo();
		checkCollisionLava();
		updateCamera();
		checkCollisionLuz();
		
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 10) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot) {
			shoot = false;
			if(hasGun && ammo > 0) {
			BulletShoot.moved = true;
			ammo--;
			int dx = 0;
			int px = 0;
			int py = 5;
			if(dir == right_dir) {
				px = 22;
				dx = 1;
			}else {
				px = -9;
				dx = -1;
			}
			BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,0);
			Game.bullets.add(bullet);
		}
		}
		
		if(mouseShoot) {
			mouseShoot = false;
			if(hasGun && ammo > 0) {
				BulletShoot.moved = true;
				ammo--;

				// Posição central do player
				double px = this.getX() + 8;
				double py = this.getY() + 8;
				
				// Posição do mouse ajustada com a câmera do mundo
				double targetX = mx + Camera.x;
				double targetY = my + Camera.y;

				// Cálculo do ângulo exato em radianos até o cursor
				double angle = Math.atan2(targetY - py, targetX - px);
				
				// Vetores de direção proporcionais (suportam 360 graus)
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				
				// Ponto de origem da bala (centro do player)
				int spawnX = this.getX() + 8;
				int spawnY = this.getY() + 8;

				BulletShoot bullet = new BulletShoot(spawnX, spawnY, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}
		}
		if(Game.player.life <= 0) {
			Game.gameState = "GAME_OVER";
			life = 0;

		}
		
		
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp( this.getX() - (Game.WIDTH/2),0,world.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp( this.getY() - (Game.HEIGHT/2),0,world.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkCollisionLuz() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Luz) {
				if(Entity.isColidding(this, atual)) {
					hasLuz = true;
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkCollisionLava() {
		for(int i = 0; i < Game.entities.size(); i++) {
    		Entity atual = Game.entities.get(i);
    		if(atual instanceof Lava) {
    			if(Entity.isColidding(this, atual)) {
    				if(Game.rand.nextInt(100) < 30) {
    					Game.player.life-=Game.rand.nextInt(3);
    				Game.player.isDamaged = true;
    				}
    			}
    		}
		}
	}
	
	public void checkCollisionGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
    		Entity atual = Game.entities.get(i);
    		if(atual instanceof Weapon) {
    			if(Entity.isColidding(this, atual)) {
    				hasGun = true;
    				Game.entities.remove(atual);
    			}
    		}
		}
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
    		Entity atual = Game.entities.get(i);
    		if(atual instanceof Bullet) {
    			if(Entity.isColidding(this, atual)) {
    				ammo+=10;
    				Game.entities.remove(atual);
    			}
    		}
		}
	}
    
    public void checkCollisionLifePack() {
    	for(int i = 0; i < Game.entities.size(); i++) {
    		Entity atual = Game.entities.get(i);
    		if(atual instanceof Lifepack) {
    			if(Entity.isColidding(this, atual)) {
    				life+=10;
    				if(life > 100)
    					life = 100;
    				Game.entities.remove(atual);
    			}
    		}
    	}
    }

	
	public void render(Graphics g) {
		if(!isDamaged) {
    	if(dir == right_dir) {
    		if(hasGun) {
    			g.drawImage(RIGHT_WEAPON, this.getX()+11 - Camera.x, this.getY() - Camera.y, null);
    		}
    	g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
    	}else if(dir == left_dir){
    		if(hasGun) {
    			g.drawImage(LEFT_WEAPON, this.getX()-10 - Camera.x, this.getY() - Camera.y, null);
    		}
    		g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
    	}if(dir == up_dir) {
        	g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
    	}else if(dir == down_dir) {
        	g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
    	}
		}else {
			g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
    }
    


}
