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
	public int swordCount = 0;
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

		setBaseSprites(3);
		getBaseSprites("player", gp);
	}
	
	void setDefaultValues() {
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";
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
			if(spriteCounter > (int)(8/(speed/4))) { //Change sprite after every 12 frames
				spriteNum++;

				if(spriteNum == up.length) {
					spriteNum = 0;
				}
				spriteCounter = 0;
			}
		}
		else {
			standCounter++;
			
			if(standCounter == 20) {
				standCounter = 0;
				//spriteNum = 0; //Reset to standing sprite position
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
				
			case "Sword":
				gp.playSFX(sound.COIN);
				swordCount++;
				gp.obj[index] = null;
				gp.ui.showMessage("You got a sword!");
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
				
			case "Spider":
				if(swordCount > 0) {
					gp.playSFX(sound.CUT);
					gp.obj[index] = null;
					swordCount--;
					gp.ui.showMessage("You sliced the webs!");
				}
				else {
					gp.ui.showMessage("You need a sword!");
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
		
		if(direction == "up") 	 image = up[spriteNum];
		if(direction == "down")  image = down[spriteNum];
		if(direction == "left")	 image = left[spriteNum];
		if(direction == "right") image = right[spriteNum];
		
		g2.drawImage(image, screenX, screenY, null);
		
		if(gp.debug) {
			//Player Collision
			g2.setColor(Color.red);
			g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
		}
	}
}
