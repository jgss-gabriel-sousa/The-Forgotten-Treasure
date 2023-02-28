package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Iron extends Entity {
	public OBJ_Shield_Iron(GamePanel gp) {
		super(gp);
		
		name = "Iron Shield";
		id = "shield_blue";
		type = TYPE_ITEM_SHIELD;
		down = new BufferedImage[1];
		down[0] = setup("/objects/"+id);
	
		defenseValue = 2;
		description = "A shield that's nearly identical to the Wood\nShield, except it's made of iron and won't\ncatch on fire. Progress!";
	}
}
