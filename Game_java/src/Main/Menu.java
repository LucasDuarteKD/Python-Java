package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import World.world;

public class Menu {

	public String[] options = {"Novo Jogo","Carregar","Sair","Som","Música"};
	
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	public boolean sla,sla2;
	
	public boolean down,up;
	
	public static boolean Som = true;
	public static boolean Musica = true;
	
	public static boolean pause = false;
	
	public static boolean saveExists = false;
	public static boolean saveGame = false;
	
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		}else {
			saveExists = false;
		}
		if(up) {
			up = false;
			currentOption--;
		}if(currentOption < 0) {
			currentOption = maxOption;
		}
		
		if(down) {
			down = false;
			currentOption++;
		}if(currentOption > maxOption) {
			currentOption = 0;
		}
		if(sla) {
			sla = false;
			if(options[currentOption] == "Novo Jogo") {
				if(Musica == true) {
					Sound.music.loop();
					}
			Game.CUR_LEVEL = 1;
			Game.gameState = "NORMAL";
			pause = false;
			File file1 = new File("save.txt");
			file1.delete();
			}else if(options[currentOption] == "Carregar") {
				if(Musica == true) {
					Sound.music.loop();
					}
				file = new File("save.txt");
				if(file.exists()) {
					String saver = LoadGame(13);
					applySave(saver);
				}
			}else if(options[currentOption] == "Sair") {
				System.exit(1);	
			}else if(options[currentOption] == "Som") {
				if(Som == true) {
				Sound.hurt.stop();
				Som = false;
				}else if(Som == false) {
					Som = true;
					}
			}else if(options[currentOption] == "Música") {
				if(Musica == true) {
					Sound.music.stop();
					Musica = false;
					}else if(Musica == false) {
						Musica = true;
						}
			}
		}
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for (int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch (spl2[0]) {
			case "level":
				world.restartGame("level" + spl2[1] + ".png");
				
				Game.CUR_LEVEL = Integer.parseInt(spl2[1]);
				
				Game.gameState = "NORMAL";
				pause = false;
				break;
			}
		}
	}
	
	public static String LoadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
			    BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
			try {
				while((singleLine = reader.readLine()) != null) {
					String[] trans = singleLine.split(":");
				char[] val = trans[1].toCharArray();
				trans[1] = "";
				for(int i = 0; i < val.length; i++) {
					val[i]-=encode;
					trans[1]+=val[i];
				}
				line+=trans[0];
				line+=":";
				line+=trans[1];
				line+="/";
				}	
			}catch(IOException e) {}
			}catch(FileNotFoundException e) {}
		}
	
	    return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
	for(int i = 0; i < val1.length; i++) {
		String current = val1[i];
		current+=":";
		char[] value = Integer.toString(val2[i]).toCharArray();
		for(int n = 0; n < value.length; n++) {
			value[n]+=encode;
			current+=value[n];
		}
	    try {
	    	write.write(current);
	    	if(i < val1.length - 1)
	    		write.newLine();
	    }catch(IOException e) {}
	}
	try {
		write.flush();
		write.close();
	}catch(IOException e) {}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(25,25,25,200));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setFont(new Font("arial",Font.BOLD,40));
		g.setColor(Color.white);
		g.drawString("Zelda Clone", (int) ((Game.WIDTH*Game.SCALE) / 2 - 125), (Game.HEIGHT*Game.SCALE) / 4);
		g.setFont(new Font("arial",Font.BOLD,30));
		if(pause == false)
		g.drawString("Novo Jogo", (int) ((Game.WIDTH*Game.SCALE) / 2.5), (Game.HEIGHT*Game.SCALE) / 2);
		else
			g.drawString("Continuar", (int) ((Game.WIDTH*Game.SCALE) / 2.5), (Game.HEIGHT*Game.SCALE) / 2);
		g.drawString("Carregar", (int) ((Game.WIDTH*Game.SCALE) / 2.5 + 5), (Game.HEIGHT*Game.SCALE) / 2 + 100);
		g.drawString("Sair", (int) ((Game.WIDTH*Game.SCALE) / 2.5 + 30), (Game.HEIGHT*Game.SCALE) / 2 + 200);
		g.drawString("Som ="   +Som, (int) ((Game.WIDTH*Game.SCALE) / 2.5 + 100), (Game.HEIGHT*Game.SCALE) / 2 + 300);
		g.drawString("Musica ="  +Musica, (int) ((Game.WIDTH*Game.SCALE) / 2.5 + 320), (Game.HEIGHT*Game.SCALE) / 2 + 300);
		
		if(options[currentOption] == "Novo Jogo") {
			g.drawString(">", (int) ((Game.WIDTH*Game.SCALE) / 2.5) - 30, (Game.HEIGHT*Game.SCALE) / 2);
		}else if(options[currentOption] == "Carregar") {
			g.drawString(">", (int) ((Game.WIDTH*Game.SCALE) / 2.5) - 30, (Game.HEIGHT*Game.SCALE) / 2 + 100);
		}else if(options[currentOption] == "Sair") {
			g.drawString(">", (int) ((Game.WIDTH*Game.SCALE) / 2.5) - 30, (Game.HEIGHT*Game.SCALE) / 2 + 200);
		}else if(options[currentOption] == "Som") {
			g.drawString(">", (int) ((Game.WIDTH*Game.SCALE) / 2.5) + 70 ,(Game.HEIGHT*Game.SCALE) / 2 + 300);
		}else if(options[currentOption] == "Música") {
			g.drawString(">", (int) ((Game.WIDTH*Game.SCALE) / 2.5) + 290, (Game.HEIGHT*Game.SCALE) / 2 + 300);
		}
	}
	
	
}
