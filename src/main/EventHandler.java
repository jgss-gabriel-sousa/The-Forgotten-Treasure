package main;

public class EventHandler {
	GamePanel gp;
	EventRect eventRect[][];

	int previousEventX, previousEventY;
	boolean canTouchEvent = true;
	
	EventHandler(GamePanel gp){
		this.gp = gp;
		
		eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];
		
		int col = 0;
		int row = 0;
		
		while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
			eventRect[col][row] = new EventRect();
			eventRect[col][row].x = 18;
			eventRect[col][row].y = 18;
			eventRect[col][row].width = 10;
			eventRect[col][row].height = 10;
			eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
			eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
			
			col++;
			if(col == gp.maxWorldCol) {
				col = 0;
				row++;
			}
		}
	}
	
	public void checkEvent() {
		//Check if the player is 1 tile away from last event
		int xDistance = Math.abs(gp.player.worldX - previousEventX);
		int yDistance = Math.abs(gp.player.worldY - previousEventY);
		int distance = Math.max(xDistance, yDistance);
		if(distance > gp.tileSize) {
			canTouchEvent = true;
		}
		
		if(canTouchEvent) {
			if(hit(36,30,"down")) 	damagePit(36,30,gp.DIALOGUE_STATE);
			if(hit(23,12,"up")) 	healingPool(23,12,gp.DIALOGUE_STATE);
		}
	}

	public boolean hit(int col, int row, String reqDirection) {
		boolean hit = false;
		
		gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
		gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
		eventRect[col][row].x = col*gp.tileSize + eventRect[col][row].x;
		eventRect[col][row].y = row*gp.tileSize + eventRect[col][row].y;
		
		if(gp.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
			if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")){
				hit = true;
				
				previousEventX = gp.player.worldX;
				previousEventY = gp.player.worldY;
			}
		}
		
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
		eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;
		
		return hit;
	}
	
	void damagePit(int col, int row, int gameState) {
		gp.gameState = gameState;
		gp.playSFX(gp.sfx.RECEIVE_DMG);
		gp.ui.currentDialogue = "You fall into a pit!";
		gp.player.hp--;

		canTouchEvent = false;
	}
	
	void healingPool(int col, int row, int gameState) {
		if(gp.inputHandler.enterPressed){
			gp.gameState = gameState;
			gp.player.attackCanceled = true;
			gp.playSFX(gp.sfx.POWERUP);
			gp.ui.currentDialogue = "You drink the water\nYour life has been recovered!";
			gp.player.hp = gp.player.maxHP;
			gp.assetManager.setMonster();
		}
	}
}
