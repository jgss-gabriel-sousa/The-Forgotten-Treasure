package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity {
	public OBJ_Shield_Wood(GamePanel gp) {
		super(gp);
		
		name = "Wood Shield";
		down = new BufferedImage[1];
		down[0] = setup("/objects/shield_wood");
	
		defenseValue = 1;
		description = "["+name+"]\nAn simple shield.";
	}
}
