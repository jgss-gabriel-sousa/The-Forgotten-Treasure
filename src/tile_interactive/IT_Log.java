package tile_interactive;

import java.awt.Color;
import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class IT_Log extends InteractiveTile {

	GamePanel gp;
	
	public IT_Log(GamePanel gp) {
		super(gp);
		this.gp = gp;
	
		name = "Log";
		down = new BufferedImage[1];
		down[0] = setup("/tiles_interactive/log");
		
		maxHP = 3;
		hp = maxHP;

		destructible = true;
	}
	
	public boolean checkRequiredItem(Entity entity) {
		boolean isCorrectItem = false;
		
		if(entity.currentWeapon.type == entity.TYPE_ITEM_AXE) {
			isCorrectItem = true;
		}
		
		return isCorrectItem;
	}
	
	public void playSFX() {
		gp.playSFX(gp.sfx.CUT_TREE);
	}
	
	public InteractiveTile getDestroyedForm() {
		//InteractiveTile tile = new IT_Trunk(gp, worldX/gp.tileSize, worldY/gp.tileSize);
		InteractiveTile tile = null;
		return tile;
	}
	
	public Color getParticleColor() {
		Color color = new Color(65,50,30);
		return color;
	}
	public int getParticleSize() {
		int size = 6;
		return size;
	}
	public int getParticleSpeed() {
		int speed = 1;
		return speed;
	}
	public int getParticleDuration() {
		int duration = 20;
		return duration;
	}

}
