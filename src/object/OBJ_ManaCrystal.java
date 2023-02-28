package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_ManaCrystal extends Entity {
	
	GamePanel gp;
	
	public OBJ_ManaCrystal(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "Mana Crystal";
		type = TYPE_ITEM_PICKUP;
		value = 2;
		image = setup("/objects/manacrystal_full");
		image2 = setup("/objects/manacrystal_blank");
		down = new BufferedImage[1];
		down[0] = image;
	}
	
	public boolean use(Entity entity) {
		if(entity.mana == entity.maxMana) return false;

		if(entity.mana+value > entity.maxMana) {
			value = entity.maxMana-entity.mana;
			entity.mana = entity.maxMana;
		}
		else {
			entity.mana += value;
		}
		
		gp.ui.addMessage("Recovered "+value+" Mana");
		gp.playSFX(gp.sfx.POWERUP);
		
		return true;
	}
}
