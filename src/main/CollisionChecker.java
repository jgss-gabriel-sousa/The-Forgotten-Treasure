package main;

import java.util.ArrayList;

import entity.Entity;
import entity.Player;
import tile_interactive.InteractiveTile;

public class CollisionChecker {
	GamePanel gp;
	
	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}
	
	public void checkTile(Entity entity) {
		int entityLeftWorldX = entity.worldX + entity.solidArea.x;
		int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
		int entityTopWorldY = entity.worldY + entity.solidArea.y;
		int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
		
		int entityLeftCol = entityLeftWorldX / gp.tileSize;
		int entityRightCol = entityRightWorldX / gp.tileSize;
		int entityTopRow = entityTopWorldY / gp.tileSize;
		int entityBottomRow = entityBottomWorldY / gp.tileSize;
		
		int tileNum1 = 0, tileNum2 = 0;
		
		switch(entity.direction) {
		case "up":
			entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
			tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
			break;
		case "down":
			entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
			tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
			tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
			break;
		case "left":
			entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
			tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
			break;
		case "right":
			entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
			tileNum1 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
			tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
			break;
		}
		
		if(gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision) {
			entity.collisionOn = true;
		}
	}
	
	public int checkObject(Entity entity, boolean player) {
	    int index = -1;
	    
	    for(int i = 0; i < gp.obj.length; i++) {
	        if(gp.obj[i] != null) {

	    	    // Get entity solid area position
	    	    entity.solidArea.x = entity.worldX + entity.solidArea.x;
	    	    entity.solidArea.y = entity.worldY + entity.solidArea.y;
	    	    // Get object solid area position
	    	    gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
	    	    gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

	    	    if(entity.direction == "up")	entity.solidArea.y -= entity.speed;
	    	    if(entity.direction == "down")	entity.solidArea.y += entity.speed;
	    	    if(entity.direction == "left")	entity.solidArea.x -= entity.speed;
	    	    if(entity.direction == "right")	entity.solidArea.x += entity.speed;

                if(checkIntersectionAndSetProperties(entity, gp.obj[i], player)) {
                    index = i;
                }
                
	            // Reset entity and object coordinates
	    	    entity.solidArea.x = entity.solidAreaDefaultX;
	    	    entity.solidArea.y = entity.solidAreaDefaultY;
	    	    gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
	    	    gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
	        }
	    }
	    
	    return index;
	}

	private boolean checkIntersectionAndSetProperties(Entity entity, Entity obj, boolean player) {
	    if(entity.solidArea.intersects(obj.solidArea)) {
	        if(obj.collision) {
	            entity.collisionOn = true;
	        }
	        if(player) {
		        return true;
	        }
	    }
	    return false; //Non player characters cannot pickup objects
	}
	
	public int checkEntity(Entity entity, Entity[] target) {
		int index = -1;
	    
	    for(int i = 0; i < target.length; i++) {
	        if(target[i] != null) {

	    	    // Get entity solid area position
	    	    entity.solidArea.x = entity.worldX + entity.solidArea.x;
	    	    entity.solidArea.y = entity.worldY + entity.solidArea.y;
	    	    // Get object solid area position
	    	    target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
	    	    target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

	    	    if(entity.direction == "up")	entity.solidArea.y -= entity.speed;
	    	    if(entity.direction == "down")	entity.solidArea.y += entity.speed;
	    	    if(entity.direction == "left")	entity.solidArea.x -= entity.speed;
	    	    if(entity.direction == "right")	entity.solidArea.x += entity.speed;
	            
	            if(entity.solidArea.intersects(target[i].solidArea)) {
                	if(target[i] != entity) {
                    	entity.collisionOn = true;
                    	index = i;
                	}
                }
	            // Reset entity and object coordinates
	    	    entity.solidArea.x = entity.solidAreaDefaultX;
	    	    entity.solidArea.y = entity.solidAreaDefaultY;
	    	    target[i].solidArea.x = target[i].solidAreaDefaultX;
	    	    target[i].solidArea.y = target[i].solidAreaDefaultY;
	        }
	    }
	    
	    return index;
	}

	public int checkITile(Entity entity, ArrayList<InteractiveTile> target) {
		int index = -1;
	    
	    for(int i = 0; i < target.size(); i++) {
	        if(target.get(i) != null) {

	    	    // Get entity solid area position
	    	    entity.solidArea.x = entity.worldX + entity.solidArea.x;
	    	    entity.solidArea.y = entity.worldY + entity.solidArea.y;
	    	    // Get object solid area position
	    	    target.get(i).solidArea.x = target.get(i).worldX + target.get(i).solidArea.x;
	    	    target.get(i).solidArea.y = target.get(i).worldY + target.get(i).solidArea.y;

	    	    if(entity.direction == "up")	entity.solidArea.y -= entity.speed;
	    	    if(entity.direction == "down")	entity.solidArea.y += entity.speed;
	    	    if(entity.direction == "left")	entity.solidArea.x -= entity.speed;
	    	    if(entity.direction == "right")	entity.solidArea.x += entity.speed;
	            
	            if(entity.solidArea.intersects(target.get(i).solidArea)) {
                	if(target.get(i) != entity) {
                    	entity.collisionOn = true;
                    	index = i;
                	}
                }
	            // Reset entity and object coordinates
	    	    entity.solidArea.x = entity.solidAreaDefaultX;
	    	    entity.solidArea.y = entity.solidAreaDefaultY;
	    	    target.get(i).solidArea.x = target.get(i).solidAreaDefaultX;
	    	    target.get(i).solidArea.y = target.get(i).solidAreaDefaultY;
	        }
	    }
	    
	    return index;
	}
	
	
	public boolean checkPlayer(Entity entity) {
		boolean hitPlayer = false;
		
	    // Get entity solid area position
	    entity.solidArea.x = entity.worldX + entity.solidArea.x;
	    entity.solidArea.y = entity.worldY + entity.solidArea.y;
	    // Get object solid area position
	    gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
	    gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

	    if(entity.direction == "up")	entity.solidArea.y -= entity.speed;
	    if(entity.direction == "down")	entity.solidArea.y += entity.speed;
	    if(entity.direction == "left")	entity.solidArea.x -= entity.speed;
	    if(entity.direction == "right")	entity.solidArea.x += entity.speed;
        
        if(entity.solidArea.intersects(gp.player.solidArea)) {
        	entity.collisionOn = true;
        	hitPlayer = true;
        }
    
        // Reset entity and object coordinates
	    entity.solidArea.x = entity.solidAreaDefaultX;
	    entity.solidArea.y = entity.solidAreaDefaultY;
	    gp.player.solidArea.x = gp.player.solidAreaDefaultX;
	    gp.player.solidArea.y = gp.player.solidAreaDefaultY;
	    
	    return hitPlayer;
	}
}
