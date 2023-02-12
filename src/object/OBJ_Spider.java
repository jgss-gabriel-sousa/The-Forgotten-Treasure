package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Spider extends SuperObject {
	public OBJ_Spider(GamePanel gp) {
		name = "Spider";
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objects/spider.png"));
			uTool.scaleImage(image, gp.tileSize, gp.tileSize);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		collision = true;
	}
}
