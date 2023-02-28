package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity {
	public OBJ_Chest(GamePanel gp) {
		super(gp);
		
		name = "Chest";
		down = new BufferedImage[1];
		down[0] = setup("/objects/chest");
	}
}
