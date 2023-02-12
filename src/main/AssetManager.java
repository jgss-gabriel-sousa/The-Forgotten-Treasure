package main;

import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;
import object.SuperObject;

public class AssetManager {
	GamePanel gp;
	int indexCounter = 0;
	
	public AssetManager(GamePanel gp) {
		this.gp = gp;
	}
	
	void set(String type, int tileX, int tileY) {
		SuperObject obj = null;
		
		if(type == "key") 		obj = new OBJ_Key(gp);
		if(type == "door") 		obj = new OBJ_Door(gp);
		if(type == "chest") 	obj = new OBJ_Chest(gp);
		if(type == "boots")		obj = new OBJ_Boots(gp);

		
		obj.worldX = tileX * gp.tileSize;
		obj.worldY = tileY * gp.tileSize;
		
		gp.obj[indexCounter] = obj;
		
		indexCounter++;
	}
	
	public void setObject() {
		set("key", 30, 37);
		set("key", 23, 40);
		set("key", 8, 41);
		
		set("door", 10, 12);
		set("door", 12, 23);
		set("door", 30, 40);
		
		
		
		set("boots", 37, 42);
		
		set("chest", 10, 8);
	}
}
