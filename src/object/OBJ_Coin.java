package object;

import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Coin  extends Entity {
	
	GamePanel gp;
	
	public OBJ_Coin(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		type = TYPE_ITEM_PICKUP;
		name = "Coin";
		value = 1;
		down = new BufferedImage[1];
		down[0] = setup("/objects/coin");
	}
	
	public boolean use(Entity entity) {
		gp.playSFX(gp.sfx.COIN);
		entity.coin += value;
		gp.ui.addMessage("You got a "+name);
		
		return true;
	}
}
