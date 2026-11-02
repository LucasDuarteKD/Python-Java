package graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import Main.Game;

public class UI {

	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(20, 8,50,8);
		g.setColor(Color.green);
		g.fillRect(20, 8,(int)((Game.player.life/Game.player.maxlife)*50),8);
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,8));
		g.drawString(Game.player.life+"/"+ Game.player.maxlife,25,15);
		g.setColor(new Color(25,25,25));
		
		}
	
	
	
	
	
}
