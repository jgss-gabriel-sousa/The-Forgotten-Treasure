package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {
	public OBJ_Key(GamePanel gp) {
		super(gp);
		
		name = "Key";
		down = new BufferedImage[1];
		down[0] = setup("/objects/key");
		description = "It opens doors. Don't expect it to solve your\nemotional problems.";
	}
}
