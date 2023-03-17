package monster;

import java.awt.image.BufferedImage;
import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

public class MON_GreenSlime extends Entity {
	GamePanel gp;
	
	public MON_GreenSlime(GamePanel gp) {
		super(gp);
		
		this.gp = gp;

		name = "Green Slime";
		type = TYPE_MONSTER;
		speed = 1;
		maxHP = 4;
		hp = maxHP;
		attack = 5;
		defense = 0;
		exp = 2;
		projectile = new OBJ_Rock(gp);
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
	}
	
	public void getImage() {
		up = new BufferedImage[2];
		down = new BufferedImage[2];
		left = new BufferedImage[2];
		right = new BufferedImage[2];
		
		up[0] = setup("/monster/greenslime_0");
		up[1] = setup("/monster/greenslime_1");
		down[0] = setup("/monster/greenslime_0");
		down[1] = setup("/monster/greenslime_1");
		left[0] = setup("/monster/greenslime_0");
		left[1] = setup("/monster/greenslime_1");
		right[0] = setup("/monster/greenslime_0");
		right[1] = setup("/monster/greenslime_1");
	}
	
	public void setAction() {
		int i = new Random().nextInt(100)+1;
		
		actionLockCounter++;
		
		if(collisionOn) {
			actionLockCounter = 120;
		}
		
		if(actionLockCounter == 120) {
			if(i <= 20) {
				direction = "up";
			}
			else if(i <= 40) {
				direction = "down";
			}
			else if(i <= 60) {
				direction = "left";
			}
			else if(i <= 80) {
				direction = "right";
			}
			
			actionLockCounter = 0;
		}
		
		/*
		SHOOT PROJECTILE
		
		i = new Random().nextInt(100)+1;
		if(i > 99 && !projectile.alive && shotSpeedCounter == 80) {
			projectile.set(worldX, worldY, direction, true, this);
			gp.projectiles.add(projectile);
			shotSpeedCounter = 0;
		}*/
	}
	
	public void damageReaction() {
		actionLockCounter = 0;
		direction = gp.player.direction;
	}
	
	public void checkDrop() {
		int i = new Random().nextInt(100)+1;
		
		if(i <= 50) {
			dropItem(new OBJ_Coin(gp));
		}
		else if(i <= 75) {
			dropItem(new OBJ_Heart(gp));
		}
		else{
			dropItem(new OBJ_ManaCrystal(gp));
		}
	}
}
