package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_OldMan extends Entity {
	
	public NPC_OldMan(GamePanel gp) {
		super(gp);
		
		name = "Old Man";
		speed = 1;

		setBaseSprites(2);
		getBaseSprites("/NPC/","oldman");
		
		setDialogue();
	}
	
	void setDialogue() {		
		dialogues[0] = "Hello Adventurer";
		dialogues[1] = "I used to be a great wizard";
		dialogues[2] = "But now i'm a bit old for an adventure";
		dialogues[3] = "Good luck on your journey";
	}
	
	public void setAction() {
		Random random = new Random();
		int i = random.nextInt(100)+1;
		
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
	}
	
	void speak() {
		super.speak();
	}
}
