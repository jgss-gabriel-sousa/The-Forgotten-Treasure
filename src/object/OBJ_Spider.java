package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Spider extends Entity {
	public OBJ_Spider(GamePanel gp) {
		super(gp);
		
		name = "Spider";
		down = new BufferedImage[1];
		down[0] = setup("/objects/spider.png");
		collision = true;
	}
}
