package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {
	
	GamePanel gp;
	
	public OBJ_Heart(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "Heart";
		type = TYPE_ITEM_PICKUP;
		value = 2;
		image = setup("/objects/heart_full");
		image2 = setup("/objects/heart_half");
		image3 = setup("/objects/heart_blank");
		down = new BufferedImage[1];
		down[0] = image;
	}
	
	public boolean use(Entity entity) {
		if(entity.hp == entity.maxHP) return false;

		if(entity.hp+value > entity.maxHP) {
			value = entity.maxHP-entity.hp;
			entity.hp = entity.maxHP;
		}
		else {
			entity.hp += value;
		}
		
		gp.ui.addMessage("Healed "+value+" HP");
		gp.playSFX(gp.sfx.POWERUP);
		
		return true;
	}
}