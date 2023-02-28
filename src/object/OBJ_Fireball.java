package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile{

	GamePanel gp;
	
	public OBJ_Fireball(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "Fireball";
		speed = 10;
		maxHP = 80;
		hp = maxHP;
		attack = 2;
		useCost = 1;
		alive = false;
		
		setBaseSprites(2);
		getBaseSprites("/projectile/","fireball");
	}
	
	public boolean haveResource(Entity user) {
		boolean haveResource = false;
		
		if(user.mana >= useCost) {
			haveResource = true;
		}
		
		return haveResource;
	}
	
	public void subtractResource(Entity user) {
		user.mana -= useCost;
	}
}
