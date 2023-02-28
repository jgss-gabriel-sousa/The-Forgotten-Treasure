package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity {
	public OBJ_Sword_Normal(GamePanel gp) {
		super(gp);
		
		name = "Normal Sword";
		id = "sword_normal";
		type = TYPE_ITEM_SWORD;
		down = new BufferedImage[1];
		down[0] = setup("/objects/"+id);

		attackValue = 1;
		attackArea.width = 36;
		attackArea.height = 36;
		
		description = "A mediocre sword, perfect for those who\nwant to experience an underwhelming\nadventure.";
	}
}
