package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import object.OBJ_Key;
import object.OBJ_Sword;

public class UI {
	GamePanel gp;
	Font arial_28, arial_80B;
	BufferedImage keyImage, swordImage;
	public boolean messageOn = false;
	public String message = "";
	int messageCounter = 0;
	public boolean gameFinished = false;
	
	double playTime;
	DecimalFormat dFormat = new DecimalFormat("#0.00");
	DecimalFormat dFormat3 = new DecimalFormat("#0.000");
	
	public UI(GamePanel gp) {
		this.gp = gp;
		
		arial_28 = new Font("Arial", Font.PLAIN, 28);
		arial_80B = new Font("Arial", Font.BOLD, 80);
		OBJ_Key key = new OBJ_Key(gp);
		keyImage = key.image;
		OBJ_Sword sword = new OBJ_Sword(gp);
		swordImage = sword.image;
	}
	
	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}
	
	void drawDebug(Graphics2D g2) {
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(10F));
		int i = 1;
		g2.drawString("Time: "+dFormat.format(playTime), 5, i++*10);
		g2.drawString("Player Speed: "+gp.player.speed, 5, i++*10);
		g2.drawString("Player Direction: "+gp.player.direction, 5, i++*10);
		g2.drawString("Player Sprite: "+gp.player.spriteNum, 5, i++*10);
		g2.drawString("Player Key Count: "+gp.player.keyCount, 5, i++*10);
		g2.drawString("Player Sword Count: "+gp.player.swordCount, 5, i++*10);
		g2.drawString("Draw Time: "+dFormat3.format(gp.drawTime/10000000)+"ms", 5, i++*10);
		int xPos = gp.player.worldX + (gp.tileSize/2);
		int yPos = gp.player.worldY + (gp.tileSize/2);
		int xTile = xPos / gp.tileSize;
		int yTile = yPos / gp.tileSize;
		g2.drawString("Position: "+xPos+", "+yPos, 5, i++*10);
		g2.drawString("Tile: "+xTile+", "+yTile, 5, i++*10);
	}
	
	void drawPause(Graphics2D g2) {
		g2.setFont(g2.getFont().deriveFont(30F));

		String text = "Paused";
		int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int textHeigth = (int)g2.getFontMetrics().getStringBounds(text, g2).getHeight();
		int x = gp.screenWidth/2 - textLength/2;
		int y = gp.tileSize*2;

		g2.setColor(Color.darkGray);
		g2.fillRect(x-5,y-textHeigth,textLength+10,textHeigth+10);
		
		g2.setColor(Color.black);
		g2.drawRect(x-5,y-textHeigth,textLength+10,textHeigth+10);

		g2.setColor(Color.white);
		g2.drawString(text, x, y);
	}
	
	public void draw(Graphics2D g2) {
		String text;
		int textLength;
		int x;
		int y;
		
		if(gameFinished) {
			g2.setFont(g2.getFont().deriveFont(30F));

			text = "You found the forgotten treasure!";
			textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			int textHeigth = (int)g2.getFontMetrics().getStringBounds(text, g2).getHeight();
			x = gp.screenWidth/2 - textLength/2;
			y = gp.screenHeight - (gp.tileSize/2);

			g2.setColor(Color.darkGray);
			g2.fillRect(x-5,y-textHeigth,textLength+10,textHeigth+10);
			
			g2.setColor(Color.black);
			g2.drawRect(x-5,y-textHeigth,textLength+10,textHeigth+10);

			g2.setColor(Color.white);
			g2.drawString(text, x, y);

			g2.setFont(arial_80B);
			g2.setColor(Color.yellow);
			
			text = "Congratulations!";
			textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = gp.screenWidth/2 - textLength/2;
			y = gp.tileSize*2;
			g2.drawString(text, x, y);
			
			g2.setFont(g2.getFont().deriveFont(30F));

			String timeText;
			int minutes = (int) playTime / 60;
			int seconds = (int) playTime % 60;
			int ms = (int)((playTime - (minutes * 60 + seconds)) * 1000);
			
			timeText = String.format("%d:%02d:%03d", minutes, seconds, ms);
			text = "Your Time is: "+timeText;
			textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = gp.screenWidth/2 - textLength/2;
			y = (gp.tileSize*3)+10;
			g2.drawString(text, x, y);
			
			gp.gameThread = null;
		}
		else {
			if(gp.gameState == gp.PAUSE_STATE) {
				drawPause(g2);
			}
			else {
				playTime += (double)1/60;
			}
			
			if(gp.debug) {
				drawDebug(g2);
			}
			else {
				g2.setColor(Color.darkGray);
				g2.fillRect((gp.tileSize/2)-10, (gp.tileSize/2)-10, gp.tileSize*2-5, gp.tileSize*2-10);
				
				g2.setColor(Color.black);
				g2.drawRect((gp.tileSize/2)-10, (gp.tileSize/2)-10, gp.tileSize*2-5, gp.tileSize*2-10);

				g2.setFont(arial_28);
				g2.setColor(Color.white);
				
				int iconSize = (int)(gp.tileSize/1.5);
				
				g2.drawImage(keyImage, (gp.tileSize/2)-5, (gp.tileSize/2)-5, iconSize, iconSize, null);
				g2.drawString("x "+gp.player.keyCount, 55, 50);
				
				g2.drawImage(swordImage, (gp.tileSize/2)-5, (gp.tileSize/2)+5+iconSize, iconSize, iconSize, null);
				g2.drawString("x "+gp.player.swordCount, 55, 60+iconSize);
			}

			if(messageOn) {
				g2.setFont(g2.getFont().deriveFont(30F));
				
				textLength = (int)g2.getFontMetrics().getStringBounds(message, g2).getWidth();
				int textHeigth = (int)g2.getFontMetrics().getStringBounds(message, g2).getHeight();
				x = gp.screenWidth/2 - textLength/2;
				y = gp.screenHeight - (gp.tileSize/2);

				g2.setColor(Color.darkGray);
				g2.fillRect(x-5,y-textHeigth,textLength+10,textHeigth+10);
				
				g2.setColor(Color.black);
				g2.drawRect(x-5,y-textHeigth,textLength+10,textHeigth+10);

				g2.setColor(Color.white);
				g2.drawString(message, x, y);
				
				messageCounter++;
				
				if(messageCounter > 120) {
					messageCounter = 0;
					messageOn = false;
				}
			}
		}
	}
}
