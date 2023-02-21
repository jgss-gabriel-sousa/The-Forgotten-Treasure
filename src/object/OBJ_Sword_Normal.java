package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity {
	public OBJ_Sword_Normal(GamePanel gp) {
		super(gp);
		
		name = "Normal Sword";
		id = "sword_normal";
		down = new BufferedImage[1];
		down[0] = setup("/objects/"+id);

		attackValue = 1;
		description = "["+name+"]\nAn old sword";
	}
}
