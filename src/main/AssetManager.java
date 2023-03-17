package main;

import java.util.Arrays;

import entity.Entity;
import entity.NPC_OldMan;
import monster.MON_GreenSlime;
import object.OBJ_Axe;
import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Coin;
import object.OBJ_Door;
import object.OBJ_Heart;
import object.OBJ_Key;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Healing;
import object.OBJ_Shield_Iron;
import object.OBJ_Spider;
import object.OBJ_Sword_Normal;
import tile_interactive.IT_Log;
import tile_interactive.InteractiveTile;

public class AssetManager {
	GamePanel gp;
	int objCounter = 0;
	int npcCounter = 0;
	int monsterCounter = 0;
	
	public AssetManager(GamePanel gp) {
		this.gp = gp;
	}
	
	void set(String type, String id, int tileX, int tileY) {		
		Entity obj = null;
		
		if(type == "object") {
			
			if(id == "key") 			obj = new OBJ_Key(gp);
			if(id == "door") 			obj = new OBJ_Door(gp);
			if(id == "chest") 			obj = new OBJ_Chest(gp);
			if(id == "boots")			obj = new OBJ_Boots(gp);
			if(id == "spider")			obj = new OBJ_Spider(gp);
			if(id == "sword")			obj = new OBJ_Sword_Normal(gp);
			if(id == "axe")				obj = new OBJ_Axe(gp);
			if(id == "shieldIron")		obj = new OBJ_Shield_Iron(gp);
			if(id == "potionHealing")	obj = new OBJ_Potion_Healing(gp);
			if(id == "coin")			obj = new OBJ_Coin(gp);
			if(id == "heart")			obj = new OBJ_Heart(gp);
			if(id == "manaCrystal")		obj = new OBJ_ManaCrystal(gp);
			
			obj.worldX = tileX * gp.tileSize;
			obj.worldY = tileY * gp.tileSize;
			
			gp.obj[objCounter] = obj;
			objCounter++;
		}
		if(type == "npc") {		
			
			if(id == "oldman")	obj = new NPC_OldMan(gp);
			
			obj.worldX = tileX * gp.tileSize;
			obj.worldY = tileY * gp.tileSize;
			
			gp.npc[npcCounter] = obj;
			npcCounter++;
		}
		if(type == "monster") {	
			if(id == "greenSlime")	obj = new MON_GreenSlime(gp);
			
			obj.worldX = tileX * gp.tileSize;
			obj.worldY = tileY * gp.tileSize;
			
			gp.monster[monsterCounter] = obj;
			monsterCounter++;
		}
		if(type == "iTile") {
			InteractiveTile iTile = null;
			
			if(id == "log")	iTile = new IT_Log(gp);
			
			iTile.worldX = tileX * gp.tileSize;
			iTile.worldY = tileY * gp.tileSize;
					
			gp.iTiles.add(iTile);
		}
	}
	
	public void setObject() {
		set("object","key", 21, 19);
		set("object","coin", 25, 19);
		set("object","coin", 26, 21);
		set("object","axe", 33, 21);
		set("object","shieldIron", 35, 21);
		set("object","potionHealing", 22, 27);
		set("object","heart", 22, 29);
		set("object","manaCrystal", 22, 31);
	}
	
	public void setNPC() {
		set("npc","oldman", 21, 21);
	}
	
	public void setMonster() {
		set("monster","greenSlime", 23, 36);
		set("monster","greenSlime", 23, 37);
		set("monster","greenSlime", 24, 37);
		set("monster","greenSlime", 34, 42);
		set("monster","greenSlime", 38, 42);
	}
	
	public void setInteractiveTiles() {
		set("iTile","log", 23, 20);
		set("iTile","log", 23, 19);
		set("iTile","log", 23, 18);
		set("iTile","log", 23, 17);
	}
}
