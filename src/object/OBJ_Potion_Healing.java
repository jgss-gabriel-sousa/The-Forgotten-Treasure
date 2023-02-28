package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Healing extends Entity {
	
	GamePanel gp;
	
	public OBJ_Potion_Healing(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		name = "Potion of Healing";
		id = "potion_red";
		value = 5;
		type = TYPE_ITEM_CONSUMABLE;
		down = new BufferedImage[1];
		down[0] = setup("/objects/"+id);
	
		description = "Heals "+value+" HP.\nDrink this magical elixir and feel your\nwounds disappear.";
	}
	
	public boolean use(Entity entity) {
		if(entity.hp == entity.maxHP) return false;
		
		if(entity.hp+value > entity.maxHP) {
			value = entity.maxHP-entity.hp;
			entity.hp = entity.maxHP;
		}
		else {
			entity.hp += value;
		}
		
		gp.ui.addMessage("You drink "+name);
		gp.ui.addMessage("Healed "+value+" HP");
		gp.playSFX(gp.sfx.POWERUP);
		
		return true;
	}
}
