package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.InputHandler;
import main.Sound;
import main.UtilityTool;

public class Player extends Entity {
	GamePanel gp;
	InputHandler inputHandler;
	Sound sound = new Sound();
	
	public final int screenX;
	public final int screenY;
	
	public int keyCount = 0;
	int standCounter = 0;
	
	public Player(GamePanel gp, InputHandler ih) {
		this.gp = gp;
		this.inputHandler = ih;
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle();
		solidArea.x = 10;
		solidArea.y = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 28;
		solidArea.height = 16;
		
		setDefaultValues();
		getPlayerImage();
	}
	
	void setDefaultValues() {
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";
	}
	
	void getPlayerImage() {		
		up1 = setup("player_up_1");
		up2 = setup("player_up_2");
		up3 = setup("player_up_3");
		down1 = setup("player_down_1");
		down2 = setup("player_down_2");
		down3 = setup("player_down_3");
		left1 = setup("player_left_1");
		left2 = setup("player_left_2");
		left3 = setup("player_left_3");
		right1 = setup("player_right_1");
		right2 = setup("player_right_2");
		right3 = setup("player_right_3");
	}
	
	BufferedImage setup(String imageName) {
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
	
	void checkCollision() {
		collisionOn = false;
		
		gp.collisionChecker.checkTile(this);
		
		int objIndex = gp.collisionChecker.checkObject(this, true);
		pickUpObject(objIndex);
		
		if(!collisionOn) {
			if(direction == "up") 		worldY -= speed;
			if(direction == "down") 	worldY += speed;
			if(direction == "left") 	worldX -= speed;
			if(direction == "right") 	worldX += speed;
		}
	}
	
	public void update() {
		if(inputHandler.upPressed == true || inputHandler.downPressed == true ||
				inputHandler.leftPressed == true || inputHandler.rightPressed == true) {
			
			if(inputHandler.upPressed) 		direction = "up";
			if(inputHandler.downPressed) 	direction = "down";
			if(inputHandler.leftPressed) 	direction = "left";
			if(inputHandler.rightPressed)	direction = "right";
			
			checkCollision();
			
			spriteCounter++;
			if(spriteCounter > 8) { //Change sprite after every 12 frames
				if(spriteNum < 3) {
					spriteNum++;
				}
				else if(spriteNum == 3) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}
		}
		else {
			standCounter++;
			
			if(standCounter == 20) {
				standCounter = 0;
				spriteNum = 1; //Reset to standing sprite position
			}
		}
	}
	
	void pickUpObject(int index) {
		if(index != -1) {			
			String objectName = gp.obj[index].name;
			
			switch(objectName) {
			case "Key":
				gp.playSFX(sound.COIN);
				keyCount++;
				gp.obj[index] = null;
				gp.ui.showMessage("You got a key!");
				break;
				
			case "Door":
				if(keyCount > 0) {
					gp.playSFX(sound.UNLOCK);
					gp.obj[index] = null;
					keyCount--;
					gp.ui.showMessage("You opened the door!");
				}
				else {
					gp.ui.showMessage("You need a key!");
				}
				break;
				
			case "Boots":
				gp.playSFX(sound.POWERUP);
				speed += 2;
				gp.obj[index] = null;
				gp.ui.showMessage("Speed up!");
				break;
			
			case "Chest":
				gp.ui.gameFinished = true;
				gp.stopMusic();
				gp.playSFX(sound.FANFARE);
				break;
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		
		if(direction == "up")		image = spriteNum == 1 ? up1 : up2;
		if(direction == "down")		image = spriteNum == 1 ? down1 : down2;
		if(direction == "left")		image = spriteNum == 1 ? left1 : left2;
		if(direction == "right")	image = spriteNum == 1 ? right1 : right2;
		
		g2.drawImage(image, screenX, screenY, null);
		
		if(gp.debug) {
			//Player Collision
			g2.setColor(Color.red);
			g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
		}
	}
}
