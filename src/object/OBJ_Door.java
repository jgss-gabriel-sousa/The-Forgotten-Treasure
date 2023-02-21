package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Door extends Entity {
	public OBJ_Door(GamePanel gp) {
		super(gp);
		
		name = "Door";
		down = new BufferedImage[1];
		down[0] = setup("/objects/door");
		collision = true;

		solidArea.x = 0;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 48;
		solidArea.height = 32;
		
		description = "["+name+"]\nIt opens a door.";
	}
}
