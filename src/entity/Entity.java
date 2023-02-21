package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {
	GamePanel gp;
	
	
	public BufferedImage up[], down[], left[], right[];
	public BufferedImage attack_up[], attack_down[], attack_left[], attack_right[];
	public BufferedImage image, image2, image3;
	public BufferedImage weapon_up, weapon_down, weapon_left, weapon_right;
	public Rectangle solidArea = new Rectangle(0,0,47,47);
	public Rectangle attackArea = new Rectangle(0,0,0,0);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false;
	String dialogues[] = new String[20];
	
	//State
	public int worldX, worldY;
	public String direction = "down";
	public boolean invincible = false;
	public boolean collision = false;
	int dialogueIndex = 0;
	boolean attacking = false;
	public boolean alive = true;
	public boolean dying = false;
	boolean hpBarOn = false;
	
	//Counters
	public int spriteCounter = 0;
	public int spriteNum = 0;
	public int invincibleCounter = 0;
	public int actionLockCounter = 0;
	int dyingCounter = 0;
	int hpBarCounter = 0;
	
	//Character Attributes
	public int type;
	public String name;
	public String id;
	public int speed = 1;
	public int maxHP;
	public int hp;
	public int level;
	public int exp;
	public int nextLevelExp;
	public int strength;
	public int dexterity;
	public int attack;
	public int defense;
	public int coin;
	public Entity currentWeapon;
	public Entity currentShield;
	
	//Item Attributes
	public int attackValue;
	public int defenseValue;
	public String description = "";
	
	//Types
	public final int TYPE_PLAYER = 0;
	public final int TYPE_NPC = 1;
	public final int TYPE_MONSTER = 2;
	
	public Entity(GamePanel gp) {
		this.gp = gp;
	}
	
	void setBaseSprites(int qty) {
		up = new BufferedImage[qty];
		down = new BufferedImage[qty];
		left = new BufferedImage[qty];
		right = new BufferedImage[qty];
	}
	
	void getBaseSprites(String folder, String name) {		
		for(int i = 0; i < up.length; i++) {
			up[i] = setup(folder+name+"_up_"+i);
			down[i] = setup(folder+name+"_down_"+i);
			left[i] = setup(folder+name+"_left_"+i);
			right[i] = setup(folder+name+"_right_"+i);
		}
	}
	
	public BufferedImage setup(String imagePath) {
		return setupResize(imagePath, gp.tileSize, gp.tileSize);
	}
	
	public BufferedImage setupResize(String imagePath, int width, int heigth) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath+".png"));
			image = uTool.scaleImage(image, width, heigth);			
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return image;
	}
	
	public void setAction() {
		;
	}
	public void damageReaction() {
		;
	}

	void speak() {
		if(dialogues[dialogueIndex] == null) {
			dialogueIndex = 0;
		}
		
		gp.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;
		
		if(gp.player.direction == "up")		direction = "down";
		if(gp.player.direction == "down")	direction = "up";
		if(gp.player.direction == "left")	direction = "right";
		if(gp.player.direction == "right")	direction = "left";
	}
	
	public void update() {
		setAction();
		
		collisionOn = false;
		gp.collisionChecker.checkTile(this);
		gp.collisionChecker.checkObject(this, false);
		gp.collisionChecker.checkEntity(this, gp.npc);
		gp.collisionChecker.checkEntity(this, gp.monster);
		boolean hitPlayer = gp.collisionChecker.checkPlayer(this);
		
		if(hitPlayer && this.type == TYPE_MONSTER) {
			if(!gp.player.invincible) {

				int damage = attack - gp.player.defense;
				if(damage < 0) {
					damage = 0;
				}
				else {
					gp.playSFX(gp.sfx.RECEIVE_DMG);
					gp.player.invincible = true;
				}
				
				gp.player.hp -= damage;
			}
		}

		if(!collisionOn) {
			if(direction == "up") 		worldY -= speed;
			if(direction == "down") 	worldY += speed;
			if(direction == "left") 	worldX -= speed;
			if(direction == "right") 	worldX += speed;
		}
		
		spriteCounter++;
		if(spriteCounter > 12) { //Change sprite after every 12 frames
			spriteNum++;

			if(spriteNum == up.length) {
				spriteNum = 0;
			}
			spriteCounter = 0;
		}
		
		if(invincible) {
			invincibleCounter++;
			if(invincibleCounter > 40) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		//Check if NPC is off the Screen
		if(!(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
		   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)) 
			return;

		if(direction == "up") 	 image = up[spriteNum];
		if(direction == "down")  image = down[spriteNum];
		if(direction == "left")	 image = left[spriteNum];
		if(direction == "right") image = right[spriteNum];

		if(type == TYPE_MONSTER && hpBarOn) {
			double oneScale = (double)gp.tileSize/maxHP;
			double hpBarValue = oneScale*hp;
			
			g2.setColor(new Color(35,35,35));
			g2.fillRect(screenX-1, screenY-11, gp.tileSize+2, 7);
			
			g2.setColor(new Color(255,0,30));
			g2.fillRect(screenX, screenY-10, (int)hpBarValue, 5);
			
			hpBarCounter++;
			
			if(hpBarCounter > 600) { //10 Seconds
				hpBarCounter = 0;
				hpBarOn = false;
			}
		}
		
		if(invincible) {
			hpBarCounter = 0;
			hpBarOn = true;
			changeAlpha(g2,0.4f);
		}
		
		if(dying) dyingAnimation(g2);
		
		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

		changeAlpha(g2,1f);
		
		if(gp.debug) {
			//Player Collision
			g2.setColor(Color.red);
			g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
		}
	}

	void dyingAnimation(Graphics2D g2) {
		dyingCounter++;
		
		int i = 5;
		
		if(dyingCounter <= i) 		 changeAlpha(g2,0f);
		else if(dyingCounter <= i*2) changeAlpha(g2,1f);
		else if(dyingCounter <= i*3) changeAlpha(g2,0f);
		else if(dyingCounter <= i*4) changeAlpha(g2,1f);
		else if(dyingCounter <= i*5) changeAlpha(g2,0f);
		else if(dyingCounter <= i*6) changeAlpha(g2,1f);
		else if(dyingCounter <= i*7) changeAlpha(g2,0f);
		else if(dyingCounter <= i*8) changeAlpha(g2,1f);
		
		if(dyingCounter > i*8) {
			alive = false;
		}
	}
	
	void changeAlpha(Graphics2D g2, float alphaValue) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}
}
