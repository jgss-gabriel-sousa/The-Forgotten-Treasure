package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Boots extends Entity {
	public OBJ_Boots(GamePanel gp) {
		super(gp);
		
		name = "Boots";
		down = new BufferedImage[1];
		down[0] = setup("/objects/boots.png");
	}
}
