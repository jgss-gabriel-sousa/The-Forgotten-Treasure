package entity;

import main.GamePanel;

public class Projectile extends Entity{

	Entity user;
	
	public Projectile(GamePanel gp) {
		super(gp);
	}
	
	public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
		
		this.worldX = worldX;
		this.worldY = worldY;
		this.direction = direction;
		this.alive = alive;
		this.user = user;
		this.hp = this.maxHP;
	}

	public void update() {
		
		if(user == gp.player) {
			int monsterIndex = gp.collisionChecker.checkEntity(this, gp.monster);
			
			if(monsterIndex != -1) {
				gp.player.damageMonster(monsterIndex, attack);
				generateParticles(user.projectile, gp.monster[monsterIndex]);
				alive = false;
			}
		}
		else {
			boolean contactPlayer = gp.collisionChecker.checkPlayer(this);
			
			if(!gp.player.invincible && contactPlayer) {
				damagePlayer(attack);
				generateParticles(user.projectile, gp.player);
				alive = false;
			}
		}
		
		if(direction == "up") 		worldY -= speed;
		if(direction == "down") 	worldY += speed;
		if(direction == "left") 	worldX -= speed;
		if(direction == "right") 	worldX += speed;
		
		hp--;	
		if(hp <= 0) {
			alive = false;
		}
		
		spriteCounter++;
		if(spriteCounter > 12) { //Change sprite after every 12 frames
			spriteNum++;

			if(spriteNum == up.length) {
				spriteNum = 0;
			}
			spriteCounter = 0;
		}
	}
}
