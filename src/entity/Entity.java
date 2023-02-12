package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {
	public int worldX, worldY;
	public int speed;
	
	public BufferedImage up[], down[], left[], right[];
	public String direction;
	
	public int spriteCounter = 0;
	public int spriteNum = 0;
	
	public Rectangle solidArea;
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false;
	
	
	void setBaseSprites(int qty) {
		up = new BufferedImage[qty];
		down = new BufferedImage[qty];
		left = new BufferedImage[qty];
		right = new BufferedImage[qty];
	}
	
	void getBaseSprites(String name, GamePanel gp) {
		
		for(int i = 0; i < up.length; i++) {
			up[i] = setup(name+"_up_"+i, gp);
			down[i] = setup(name+"_down_"+i, gp);
			left[i] = setup(name+"_left_"+i, gp);
			right[i] = setup(name+"_right_"+i, gp);
		}
	}
	
	BufferedImage setup(String imageName, GamePanel gp) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/player/"+imageName+".png"));
			image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);			
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return image;
	}
}
