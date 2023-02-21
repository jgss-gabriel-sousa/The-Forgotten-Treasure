package main;

import java.util.Arrays;

import entity.Entity;
import entity.NPC_OldMan;
import monster.MON_GreenSlime;
import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;
import object.OBJ_Spider;
import object.OBJ_Sword_Normal;

public class AssetManager {
	GamePanel gp;
	int objCounter = 0;
	int npcCounter = 0;
	int monsterCounter = 0;
	
	public AssetManager(GamePanel gp) {
		this.gp = gp;
	}
	
	void set(String type, int tileX, int tileY) {
		String objs[] = {"key","door","chest","boots","spider","sword"};
		String npcs[] = {"oldman"};
		String monsters[] = {"greenSlime"};
		
		if(Arrays.stream(objs).anyMatch(type::equals)) {
			Entity obj = null;
			
			if(type == "key") 		obj = new OBJ_Key(gp);
			if(type == "door") 		obj = new OBJ_Door(gp);
			if(type == "chest") 	obj = new OBJ_Chest(gp);
			if(type == "boots")		obj = new OBJ_Boots(gp);
			if(type == "spider")	obj = new OBJ_Spider(gp);
			if(type == "sword")		obj = new OBJ_Sword_Normal(gp);
			
			obj.worldX = tileX * gp.tileSize;
			obj.worldY = tileY * gp.tileSize;
			
			gp.obj[objCounter] = obj;
			objCounter++;
		}
		if(Arrays.stream(npcs).anyMatch(type::equals)) {
			Entity npc = null;
			
			if(type == "oldman")	npc = new NPC_OldMan(gp);
			
			npc.worldX = tileX * gp.tileSize;
			npc.worldY = tileY * gp.tileSize;
			
			gp.npc[npcCounter] = npc;
			npcCounter++;
		}
		if(Arrays.stream(monsters).anyMatch(type::equals)) {
			Entity monster = null;
			
			if(type == "greenSlime")	monster = new MON_GreenSlime(gp);
			
			monster.worldX = tileX * gp.tileSize;
			monster.worldY = tileY * gp.tileSize;
					
			gp.monster[monsterCounter] = monster;
			monsterCounter++;
		}
	}
	
	public void setObject() {
	}
	
	public void setNPC() {
		set("oldman", 21, 21);
	}
	
	public void setMonster() {
		set("greenSlime", 23, 36);
		set("greenSlime", 23, 37);
		set("greenSlime", 24, 37);
		set("greenSlime", 34, 42);
		set("greenSlime", 38, 42);
	}
}
