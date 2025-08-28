package vehicles;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class Car extends Entity {
    GamePanel gp;

    public final int screenX;
    public final int screenY;
    public boolean playerInCar = false;

    private int drag = 20; // Redução de velocidade gradual
    private int dragCounter = 0;
    private final int maxSpeed = 10; // Velocidade máxima
    private int speedX = 0;
    private int speedY = 0;

    public Car(GamePanel gp) {
        super(gp);

        this.gp = gp;

        name = "Car";
        type = TYPE_VEHICLE;
        speed = 0;
        maxHP = 50;
        hp = maxHP;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        loadImages();
    }

    private void loadImages() {
        up = new BufferedImage[1];
        down = new BufferedImage[1];
        left = new BufferedImage[1];
        right = new BufferedImage[1];

        up[0] = setup("/vehicles/cart_up_0");
        down[0] = setup("/vehicles/cart_down_0");
        left[0] = setup("/vehicles/cart_left_0");
        right[0] = setup("/vehicles/cart_right_0");
    }

    @Override
    public void update() {
        handleMovement();

        // Checagem de colisões
        collisionOn = false;
        gp.collisionChecker.checkTile(this);

        // Atualiza a posição se não houver colisão
        if (!collisionOn && playerInCar) {
            switch (direction) {
                case "up" -> worldY -= speedY;
                case "down" -> worldY += speedY;
                case "left" -> worldX -= speedX;
                case "right" -> worldX += speedX;
            }
        }

        applyDrag();
        animateSprite();
        handleInvincibility();
        
        if (playerInCar) {
            gp.player.worldX = worldX;
            gp.player.worldY = worldY;
        }
    }

    private void handleMovement() {
    	if (!playerInCar && gp.inputHandler.enterCarPressed) {
    		playerInCar = true;
    		gp.player.isInCar = true;
    		return;
    	}
        if (playerInCar && gp.inputHandler.enterCarPressed) {
            playerInCar = false;
            gp.player.isInCar = false;
            return;
        }
        

        if (!playerInCar) return;

        // Ajusta a direção e velocidade
        if (gp.inputHandler.upPressed) {
            direction = "up";
            speedY = Math.min(speedY + 1, maxSpeed);
        } else if (gp.inputHandler.downPressed) {
            direction = "down";
            speedY = Math.min(speedY + 1, maxSpeed);
        } else {
            speedY = Math.max(speedY - 1, 0); // Reduz a velocidade gradualmente
        }

        if (gp.inputHandler.leftPressed) {
            direction = "left";
            speedX = Math.min(speedX + 1, maxSpeed);
        } else if (gp.inputHandler.rightPressed) {
            direction = "right";
            speedX = Math.min(speedX + 1, maxSpeed);
        } else {
            speedX = Math.max(speedX - 1, 0); // Reduz a velocidade gradualmente
        }
    }

    private void applyDrag() {
        dragCounter++;
        if (dragCounter >= drag) {
            if (speedX > 0) speedX--;
            if (speedY > 0) speedY--;
            dragCounter = 0;
        }
    }

    private void animateSprite() {
        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum + 1) % up.length;
            spriteCounter = 0;
        }
    }

    private void handleInvincibility() {
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
}
