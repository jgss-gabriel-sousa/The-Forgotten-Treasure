package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Rock extends Projectile{

	GamePanel gp;
	
	public OBJ_Rock(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "Rock";
		
		setBaseSprites(1);
		up[0] = setup("/projectile/rock");
		down[0] = setup("/projectile/rock");
		left[0] = setup("/projectile/rock");
		right[0] = setup("/projectile/rock");
		
		speed = 8;
		maxHP = 80;
		hp = maxHP;
		attack = 2;
		useCost = 1;
		alive = false;
	}
	
	public boolean haveResource(Entity user) {
		boolean haveResource = false;
		
		if(user.ammo >= useCost) {
			haveResource = true;
		}
		
		return haveResource;
	}
	
	public void subtractResource(Entity user) {
		user.ammo -= useCost;
	}
}
