package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity {
	public OBJ_Shield_Wood(GamePanel gp) {
		super(gp);
		
		name = "Wood Shield";
		id = "shield_wood";
		type = TYPE_ITEM_SHIELD;
		down = new BufferedImage[1];
		down[0] = setup("/objects/"+id);
	
		defenseValue = 1;
		description = "It won't protect you from much, but at least\nyou can use it to start a campfire..";
	}
}
