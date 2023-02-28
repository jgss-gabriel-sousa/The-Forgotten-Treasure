package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GamePanel;
import main.InputHandler;
import main.Sound;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_Rock;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

public class Player extends Entity {
	GamePanel gp;
	InputHandler inputHandler;
	Sound sound = new Sound();
	
	public final int screenX;
	public final int screenY;
	
	int standCounter = 0;
	public boolean collisionWithNPC = false;
	public boolean attackCanceled = false;
	public ArrayList<Entity> inventory = new ArrayList<>();
	public final int maxInventorySize = 20;
	
	public Player(GamePanel gp, InputHandler input) {
		super(gp);
		
		this.gp = gp;
		this.inputHandler = input;
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;
		
		setDefaultValues();
		setItems();

		setBaseSprites(3);
		getBaseSprites("/player/","player");
		//getPlayerAttackImage();

		getWeaponSprites();
	}
	
	void getWeaponSprites(){
		weapon_up = setup("/player/weapons/"+currentWeapon.id+"_up");
		weapon_down = setup("/player/weapons/"+currentWeapon.id+"_down");
		weapon_left = setup("/player/weapons/"+currentWeapon.id+"_left");
		weapon_right = setup("/player/weapons/"+currentWeapon.id+"_right");
	}
	
	void getPlayerAttackImage(){
		attack_up = new BufferedImage[2];
		attack_down = new BufferedImage[2];
		attack_left = new BufferedImage[2];
		attack_right = new BufferedImage[2];
		
		for(int i = 0; i < 2; i++) {
			attack_up[i] = setupResize("/player/attack_up_"+i,gp.tileSize,gp.tileSize*2);
			attack_down[i] = setupResize("/player/attack_down_"+i,gp.tileSize,gp.tileSize*2);
			attack_left[i] = setupResize("/player/attack_left_"+i,gp.tileSize*2,gp.tileSize);
			attack_right[i] = setupResize("/player/attack_right_"+i,gp.tileSize*2,gp.tileSize);
		}
	}
	
	void setDefaultValues() {
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";
		
		maxHP = 6;
		hp = maxHP;
		maxMana = 4;
		mana = maxMana;
		ammo = 10;
		level = 1;
		strength = 1;
		dexterity = 1;
		exp = 0;
		nextLevelExp = 5;
		coin = 0;
		currentWeapon = new OBJ_Sword_Normal(gp);
		currentShield = new OBJ_Shield_Wood(gp);
		projectile = new OBJ_Fireball(gp);
		
		attack = getAttackValue();
		defense = getDefenseValue();
	}
	
	void setItems() {
		inventory.add(currentWeapon);
		inventory.add(currentShield);
		inventory.add(new OBJ_Key(gp));
		inventory.add(new OBJ_Key(gp));
	}
	
	int getAttackValue() {
		attackArea = currentWeapon.attackArea;
		return strength * currentWeapon.attackValue;
	}
	
	int getDefenseValue() {
		return dexterity * currentShield.defenseValue;
	}
		
	void checkCollision() {
		collisionOn = false;
		
		gp.collisionChecker.checkTile(this);
		
		//Check OBJ Collision
		int objIndex = gp.collisionChecker.checkObject(this, true);
		pickUpObject(objIndex);
		
		//Check NPC Collision
		int npcIndex = gp.collisionChecker.checkEntity(this, gp.npc);
		interactNPC(npcIndex);

		//Check Monster Collision
		int monsterIndex = gp.collisionChecker.checkEntity(this, gp.monster);
		contactMonster(monsterIndex);

		//Check Events
		gp.eventHandler.checkEvent();
		
		if(!collisionOn && !inputHandler.enterPressed) {
			if(direction == "up") 		worldY -= speed;
			if(direction == "down") 	worldY += speed;
			if(direction == "left") 	worldX -= speed;
			if(direction == "right") 	worldX += speed;
		}
	}

	public void update() {
		if(invincible) {
			invincibleCounter++;
			if(invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		
		if(attacking) {
			attack();
		}
		else if(inputHandler.upPressed || 
			inputHandler.downPressed ||
			inputHandler.leftPressed || 
			inputHandler.rightPressed || 
			inputHandler.enterPressed) {
			
			if(inputHandler.upPressed) 		direction = "up";
			if(inputHandler.downPressed) 	direction = "down";
			if(inputHandler.leftPressed) 	direction = "left";
			if(inputHandler.rightPressed)	direction = "right";
			
			checkCollision();
			
			if(inputHandler.enterPressed && !attackCanceled) {
				gp.playSFX(gp.sfx.SWING_WEAPON);
				attacking = true;
				spriteCounter = 0;
			}
			
			attackCanceled = false;
			gp.inputHandler.enterPressed = false;
			
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
				spriteNum = 0; //Reset to standing sprite position
			}
		}
		
		if(gp.inputHandler.shotKeyPressed && !projectile.alive && shotSpeedCounter == 80 && projectile.haveResource(this)) {
			projectile.set(worldX, worldY, direction, true, this);
			
			gp.projectiles.add(projectile);
			
			projectile.subtractResource(this);
			
			shotSpeedCounter = 0;
			
			gp.playSFX(gp.sfx.EXPLOSION);
		}
		
		if(shotSpeedCounter < 80) {
			shotSpeedCounter++;
		}
	}
	
	void attack() {
		spriteCounter++;
		
		if(spriteCounter <= 5) {
			spriteNum = 0;
		}
		else if(spriteCounter <= 25) {
			spriteNum = 1;
			
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			if(direction == "up")		worldY -= attackArea.height;
			if(direction == "down")		worldY += attackArea.height;
			if(direction == "left")		worldX -= attackArea.width;
			if(direction == "right")	worldX += attackArea.width;
			
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			
			int monsterIndex = gp.collisionChecker.checkEntity(this, gp.monster);
			damageMonster(monsterIndex, attack);
			
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;
		}
		else{
			spriteNum = 0;
			spriteCounter = 0;
			attacking = false;
		}
	}

	void pickUpObject(int i) {
		if(i == -1) return;

		String text;
		
		//Pickup Only Items
		if(gp.obj[i].type == TYPE_ITEM_PICKUP) {
			
			if(gp.obj[i].use(this)) {
				gp.playSFX(gp.sfx.COIN);			
				gp.obj[i] = null;
			}
		}
		
		//Inventory Items
		else {			
			if(inventory.size() != maxInventorySize) {
				
				inventory.add(gp.obj[i]);
				gp.playSFX(gp.sfx.COIN);
				text = "You got a "+gp.obj[i].name;
			}
			else {
				text = "You cannot carry anymore";
			}
			
			gp.ui.addMessage(text);
			gp.obj[i] = null;
		}
	}
	
	void interactNPC(int index) {
		if(index == -1) {
			collisionWithNPC = false;
			return;
		}
		
		collisionWithNPC = true;
		
		if(gp.inputHandler.enterPressed) {
			attackCanceled = true;
			gp.gameState = gp.DIALOGUE_STATE;
			gp.npc[index].speak();
		}
	}
	
	void contactMonster(int i) {
		if(i == -1) return;
		
		if(!invincible && !gp.monster[i].dying) {
			gp.playSFX(gp.sfx.RECEIVE_DMG);
			
			int damage = gp.monster[i].attack - defense;
			if(damage < 0) {
				damage = 0;
			}
			hp -= damage;
			
			invincible = true;
		}
	}

	void damageMonster(int i, int attack) {
		if(i == -1) return;
		
		if(!gp.monster[i].invincible) {
			gp.playSFX(gp.sfx.HIT_MONSTER);
			
			int damage = attack - gp.monster[i].defense;
			if(damage < 0) {
				damage = 0;
			}
			gp.monster[i].hp -= damage;
		
			if(gp.monster[i].hp < 0) {
				gp.monster[i].hp = 0;
			}
			
			gp.ui.addMessage(damage+" damage!");
			
			gp.monster[i].invincible = true;
			gp.monster[i].damageReaction();
			
			if(gp.monster[i].hp <= 0) {
				gp.monster[i].dying = true;
				gp.ui.addMessage(gp.monster[i].name+" has been killed! ("+gp.monster[i].exp+" xp)");
				exp += gp.monster[i].exp;
				checkLevelUp();
			}
		}
	}
	
	void checkLevelUp() {
		if(exp >= nextLevelExp) {
			level++;
			nextLevelExp = nextLevelExp*2;
			maxHP += 2;
			hp += 2;
			strength++;
			dexterity++;
			attack = getAttackValue();
			defense = getDefenseValue();
			
			gp.playSFX(gp.sfx.LEVEL_UP);
			gp.ui.addMessage("Level Up!");
		}
	}
	
	public void selectItem() {
		int itemIndex = gp.ui.getItemIndexOnSlot();
		
		if(itemIndex < inventory.size()) {
			Entity selectedItem = inventory.get(itemIndex);
			
			if(selectedItem.type == TYPE_ITEM_SWORD || selectedItem.type == TYPE_ITEM_AXE) {
				currentWeapon = selectedItem;
				attack = getAttackValue();
				getWeaponSprites();
				
				gp.playSFX(gp.sfx.POWERUP);
			}
			if(selectedItem.type == TYPE_ITEM_SHIELD) {
				currentShield = selectedItem;
				defense = getDefenseValue();
				
				gp.playSFX(gp.sfx.POWERUP);
			}
			if(selectedItem.type == TYPE_ITEM_CONSUMABLE) {
				if(selectedItem.use(this))
					inventory.remove(itemIndex);
			}
		}
	}

	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		BufferedImage weapon = null;
		Boolean weaponDrawed = false;
		int weaponX = solidArea.x + screenX;
		int weaponY = solidArea.y + screenY;

		if(attacking) {
			if(direction == "up"){
				weaponX -= attackArea.width/4;
				weaponY -= attackArea.height;
			}		
			if(direction == "down") {
				weaponX -= attackArea.width/4;
				weaponY += attackArea.height-solidArea.y;
			}
			if(direction == "left") {
				weaponX -= attackArea.width;
				weaponY -= attackArea.height/4;
			}
			if(direction == "right"){
				weaponX += attackArea.width-solidArea.y;
				weaponY -= attackArea.height/4;
			}	

			if(direction == "up") 	 weapon = weapon_up;
			if(direction == "down")  weapon = weapon_down;
			if(direction == "left")	 weapon = weapon_left;
			if(direction == "right") weapon = weapon_right;
			
			if(direction == "up" || direction == "left") {
				g2.drawImage(weapon, weaponX, weaponY, null);
				weaponDrawed = true;
			}	
		}
		
		if(direction == "up") 	 image = up[spriteNum];
		if(direction == "down")  image = down[spriteNum];
		if(direction == "left")	 image = left[spriteNum];
		if(direction == "right") image = right[spriteNum];
		
		if(invincible) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		}
		
		g2.drawImage(image, screenX, screenY, null);
		
		if(attacking && !weaponDrawed) {
			g2.drawImage(weapon, weaponX, weaponY, null);
		}
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
		if(gp.debug) {
			//Player Collision
			g2.setColor(Color.red);
			g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

			
			g2.setColor(Color.yellow);
			
			int x = solidArea.x + screenX;
			int y = solidArea.y + screenY;
			
			if(direction == "up")		y -= attackArea.height;
			if(direction == "down")		y += attackArea.height;
			if(direction == "left")		x -= attackArea.width;
			if(direction == "right")	x += attackArea.width;
				
			g2.drawRect(x, y, attackArea.width, attackArea.height);
		}
	}
}
