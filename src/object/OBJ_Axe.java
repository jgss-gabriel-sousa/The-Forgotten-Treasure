package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity {
	public OBJ_Axe(GamePanel gp) {
		super(gp);
		
		name = "Simple Axe";
		id = "axe";
		type = TYPE_ITEM_AXE;
		down = new BufferedImage[1];
		down[0] = setup("/objects/"+id);

		attackValue = 1;
		attackArea.width = 30;
		attackArea.height = 30;
		
		description = "Perfect for chopping wood, or zombies.\nJust remember to sharpen it first.";
	}
}
