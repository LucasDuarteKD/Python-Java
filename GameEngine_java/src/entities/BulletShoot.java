package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import Main.Game;
import World.Camera;
import World.world;

public class BulletShoot extends Entity {

    // Usamos double para manter precisão decimal no movimento
    private double dx;
    private double dy;
    private double spd = 4; // Velocidade da bala (você pode aumentar ou diminuir)
    
    public static boolean moved = false;

    public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
        super(x, y, width, height, sprite);
        this.dx = dx;
        this.dy = dy;
    }

    public void tick() {
        // Movimenta a bala multiplicando a direção pela velocidade
        x += dx * spd;
        y += dy * spd;

        // Destroi a bala caso ela saia dos limites do mapa ou colida com uma parede
        if(!world.isFree((int)x, (int)y) || x < 0 || y < 0 || x > world.WIDTH * 16 || y > world.HEIGHT * 16) {
            Game.bullets.remove(this);
            return;
        }
    }

    public void render(Graphics g) {
        g.setColor(java.awt.Color.yellow); // Cor amarela para destacar a bala (ou use sua imagem)
        g.fillOval((int)x - Camera.x, (int)y - Camera.y, width, height);
    }
}