package Main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import World.world;
import entities.BulletShoot;
import entities.Enemy;
import entities.Entity;
import entities.Lava;
import entities.Player;
import graficos.Spritesheet;
import graficos.UI;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 320;
	public static int HEIGHT = 240;
	public static final int SCALE = 3;
	
	public static int CUR_LEVEL = 1; 
	public static int MAX_LEVEL = 3;
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	
	public static Spritesheet spritesheet;
	
	public static world world;
	
	public static Player player;
	public static Enemy enemy;
	public static Lava lava;
	
	public int layer = 0;
	
	
	public static Random rand;
	
	public UI ui;
	
	public int xx,yy;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("PixelFont.ttf");
	public Font newfont;
	
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	public Menu menu;
	public static int[] pixels;
	public static int[] minimapaPixels;
	public int[] lightMapPixels;
	public int[] lightMapPixels_01;
	public int[] lightMapPixels_02;
	public int[] lightMapPixels_03;
	public int[] lightMapPixels_04;
	
	public BufferedImage lightMap;
	public BufferedImage lightMap_01;
	public BufferedImage lightMap_02;
	public BufferedImage lightMap_03;
	public BufferedImage lightMap_04;
	public static BufferedImage minimapa;
	
	public boolean saveGame = false;
	
	private int Luz = 0;
	public int mx,my;
	public Game() {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		//setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
	    setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
	    initFrame();
	    //Inicializando objetos
	    ui = new UI();
	    image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	    pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	    entities = new ArrayList<Entity>();
	    enemies = new ArrayList<Enemy>();
	    bullets = new ArrayList<BulletShoot>();
	    
	    spritesheet = new Spritesheet("/spritsheet.png");
	    player = new Player(0,0,16,16,spritesheet.GetSprite(32, 0, 16, 16));
	    Game.entities.add(player);
	    world = new world("/level1.png");
	    try {
	    	lightMap = ImageIO.read(getClass().getResource("/lightmap.png"));
	    	lightMap_01 = ImageIO.read(getClass().getResource("/lightmap_01.png"));
	    	lightMap_02 = ImageIO.read(getClass().getResource("/lightmap_02.png"));
	    	lightMap_03 = ImageIO.read(getClass().getResource("/lightmap_03.png"));
	    	lightMap_04 = ImageIO.read(getClass().getResource("/lightmap_04.png"));
	    }catch (IOException e) {
	    	e.printStackTrace();
	    }
	    lightMapPixels_01 = new int[lightMap_01.getWidth() * lightMap_01.getHeight()];
	    lightMapPixels_02 = new int[lightMap_02.getWidth() * lightMap_02.getHeight()];
	    lightMapPixels_03 = new int[lightMap_03.getWidth() * lightMap_03.getHeight()];
	    lightMapPixels_04 = new int[lightMap_04.getWidth() * lightMap_04.getHeight()];
	    lightMapPixels = new int[lightMap.getWidth() * lightMap.getHeight()];
	    lightMap_04.getRGB(0, 0, lightMap_04.getWidth(), lightMap_04.getHeight(),lightMapPixels_04,0, lightMap_04.getWidth());
	    lightMap_03.getRGB(0, 0, lightMap_03.getWidth(), lightMap_03.getHeight(),lightMapPixels_03,0, lightMap_03.getWidth());
	    lightMap_02.getRGB(0, 0, lightMap_02.getWidth(), lightMap_02.getHeight(),lightMapPixels_02,0, lightMap_02.getWidth());
        lightMap_01.getRGB(0, 0, lightMap_01.getWidth(), lightMap_01.getHeight(),lightMapPixels_01,0, lightMap_01.getWidth());
        lightMap.getRGB(0, 0, lightMap.getWidth(), lightMap.getHeight(),lightMapPixels,0, lightMap.getWidth());
	    minimapa = new BufferedImage(World.world.WIDTH,World.world.HEIGHT,BufferedImage.TYPE_INT_RGB);
	    minimapaPixels = ((DataBufferInt)minimapa.getRaster().getDataBuffer()).getData();
	    
	    try {
			newfont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(30f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    menu = new Menu();
	}
	
	public void initFrame() {
		frame = new JFrame("Zelda Clone");
	    frame.add(this);
	    //frame.setUndecorated(true);
	    frame.setResizable(true);
	    frame.pack();
	   /* Image imagem = null;
	    try {
	    	imagem = ImageIO.read(getClass().getResource("/"));
	    }catch (IOException e) {
	    	e.printStackTrace();
	    }
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Image image = toolkit.getImage(getClass().getResource("/"));
	    Cursor c = toolkit.createCustomCursor(image , new Point(0,
	    		     0), "img");
	    frame.setCursor(c);
	    frame.setIconImage(imagem);
	    frame.setAlwaysOnTop(true);*/
	    frame.setLocationRelativeTo(null);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);	
	}
	
	public synchronized void start() {
			thread = new Thread(this);
			isRunning = true;
			thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
	Game game = new Game();
	game.start();
	}
	
	public void tick() {
		if(CUR_LEVEL == 2) {
			World.world.renderMiniMap();
		}
		if(gameState == "NORMAL") {

			if(this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level"};
				int[] opt2 = {Game.CUR_LEVEL};
				Menu.saveGame(opt1,opt2,13);
				System.out.println("jogo salvo");
			}
			this.restartGame = false;
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
	for(int i = 0; i < bullets.size(); i++) {
		bullets.get(i).tick();
		}
	if(enemies.size() == 0) {
		int ammoTemp = player.ammo;
		
		CUR_LEVEL++;
		if(CUR_LEVEL > MAX_LEVEL) {
			CUR_LEVEL = 1;
		}
	String newWorld = "level"+CUR_LEVEL+".png";
	World.world.restartGame(newWorld);
	
	// Restaura a munição para o player na nova fase
    player.ammo = ammoTemp;
	}
	}else if(gameState == "GAME_OVER") {
		this.framesGameOver++;
		if(this.framesGameOver == 30) {
			this.framesGameOver = 0;
			if(this.showMessageGameOver)
				this.showMessageGameOver = false;
			else
				this.showMessageGameOver = true;
		}
		if(restartGame) {
			this.restartGame = false;
			gameState = "NORMAL";
			CUR_LEVEL = 1;
			String newWorld = "level"+CUR_LEVEL+".png";
			World.world.restartGame(newWorld);
		}
	}else if(gameState == "MENU") {
		menu.tick();
	}
	}

	/*
	public void drawRectangleExample(int xoff,int yoff) {
		for(int xx = 0; xx < 32; xx++) {
			for(int yy = 0; yy < 32; yy++) {
				int xOff = xx + xoff;
				int yOff = yy + yoff;
				if(xOff < 0 || yOff < 0 || xOff >= WIDTH || yOff >= HEIGHT)
					continue;
				pixels[xOff + (yOff*WIDTH)] = 0xff0000;
			}
		}
	}
	*/
	
	public void applyLight() {
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				if(lightMapPixels[xx+yy*WIDTH] == 0xFFFFFFFF) {
					int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0x808080, 0);
					pixels[xx+yy*WIDTH] = pixel;
				}
			}
		}
	}
	public void applyLight_01() {
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				if(lightMapPixels_01[xx+yy*WIDTH] == 0xFFFFFFFF) {
					int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0x3D3D3D, 0);
					pixels[xx+yy*WIDTH] = pixel;
				}
			}
		}
	}
	public void applyLight_02() {
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				if(lightMapPixels_02[xx+yy*WIDTH] == 0xFFFFFFFF) {
					int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0x3D3D3D, 0);
					pixels[xx+yy*WIDTH] = pixel;
				}
			}
		}
	}
	public void applyLight_03() {
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				if(lightMapPixels_03[xx+yy*WIDTH] == 0xFFFFFFFF) {
					int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0x3D3D3D, 0);
					pixels[xx+yy*WIDTH] = pixel;
				}
			}
		}
	}
	public void applyLight_04() {
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				if(lightMapPixels_04[xx+yy*WIDTH] == 0xFFFFFFFF) {
					int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0x3D3D3D, 0);
					pixels[xx+yy*WIDTH] = pixel;
				}
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(25,25,25));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		
		/*Renderiza��o do Jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		Collections.sort(entities,Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
			}
		if(player.hasLuz == false && CUR_LEVEL == 2) {
		applyLight();
		}else if(player.hasLuz && Luz == 0 && CUR_LEVEL == 2) {
		applyLight_01();
		}else if(player.hasLuz && Luz == 1 && CUR_LEVEL == 2) {
			applyLight_02();
		}else if(player.hasLuz && Luz == 2 && CUR_LEVEL == 2) {
		    applyLight_03();
		}else if(player.hasLuz && Luz == 3 && CUR_LEVEL == 2) {
			applyLight_04();
			}
		
		ui.render(g);
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		//g.drawImage(image,0,0,Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height,null);
		g.setFont(new Font("arial",Font.BOLD,17));
		g.setColor(Color.white);
		g.drawString("Muni��o: "+player.ammo , 1, 710);
		if(gameState == "GAME_OVER") {
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(25,25,25,150));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g2.setFont(new Font("arial",Font.BOLD,40));
			g2.setColor(Color.white);
			g2.drawString("Game Over", 360, 280);
			g2.setFont(new Font("arial",Font.BOLD,36));
			if(showMessageGameOver)
			g2.drawString(">Pressione Enter Para Reniciar<", 220, 340);
		}else if(gameState == "MENU") {
            menu.render(g);
		}
		World.world.renderMiniMap();
		if(CUR_LEVEL == 1) {
		g.drawImage(minimapa,850,10,World.world.WIDTH*5,World.world.HEIGHT*5,null);
		}
		if(CUR_LEVEL == 2) {
			g.drawImage(minimapa,850,10,World.world.WIDTH*2,World.world.HEIGHT*2,null);
		}
		if(CUR_LEVEL == 3) {
			g.drawImage(minimapa,850,10,World.world.WIDTH*2,World.world.HEIGHT*2,null);
		}
		bs.show();
	}
	
	
	 
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
        while(isRunning) {
        	long now = System.nanoTime();
        			delta+= (now - lastTime) / ns;
                    lastTime = now;
        			if (delta >= 1) {
        				tick();
        				render();
        				frames++;
        				delta--;
        			}
        			
        			if(System.currentTimeMillis() - timer >= 1000) {
        				System.out.println("Fps: "+ frames);
        				frames = 0;
        				timer+=1000;
        			}
        }
        
        stop();
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
			if(CUR_LEVEL == 2)
			Luz = 2;
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
			if(CUR_LEVEL == 2)
				Luz = 3;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP ||
			e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if(CUR_LEVEL == 2)
				Luz = 1;
			if(gameState == "MENU") {
				menu.up = true;
			}
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			if(CUR_LEVEL == 2)
				Luz = 0;
			
			if(gameState == "MENU") {
				menu.down = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_X) {
			player.shoot = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			
			if(gameState == "MENU") {
				menu.sla = true;
				menu.sla2 = true;
			}
		}if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			Menu.pause = true;
		}if(e.getKeyCode() == KeyEvent.VK_Z) {
			if(gameState == "NORMAL") {
				this.saveGame = true;
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			Player.index = 0;
			player.right = false;
			
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			Player.index = 0;
			player.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP ||
			e.getKeyCode() == KeyEvent.VK_W) {
			Player.index = 0;
			player.up = false;
			
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			Player.index = 0;
			player.down = false;
			}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
	player.mouseShoot = true;
	player.mx = (e.getX()/3);
	player.my = (e.getY()/3);
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();		
	}


}
