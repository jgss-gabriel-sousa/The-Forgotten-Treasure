package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;

public class Particle extends Entity {

	Entity generator;
	Color color;
	int size;
	int xd;
	int yd;
	
	public Particle(GamePanel gp, Entity generator, Color color, int size, int speed, int duration, int xd, int yd) {
		super(gp);
		
		this.generator = generator;
		this.color = color;
		this.size = size;
		this.speed = speed;
		maxHP = duration;
		this.xd = xd;
		this.yd = yd;
		
		hp = maxHP;
		int offset = (gp.tileSize/2) - (size/2);
		worldX = generator.worldX + offset;
		worldY = generator.worldY + offset;
	}
	
	public void update() {
		hp--;
		
		if(hp < maxHP/3) {
			yd += 0.5;
		}
			
		worldX += xd*speed;
		worldY += yd*speed;
		
		if(hp <= 0) {
			alive = false;
		}
	}
	
	public void draw(Graphics2D g2) {
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
	
		g2.setColor(color);
		g2.fillRect(screenX, screenY, size, size);
	}
	
}
