package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import object.OBJ_Key;

public class UI {
	GamePanel gp;
	Font arial_40, arial_80B;
	BufferedImage keyImage;
	public boolean messageOn = false;
	public String message = "";
	int messageCounter = 0;
	public boolean gameFinished = false;
	
	double playTime;
	DecimalFormat dFormat = new DecimalFormat("#0.00");
	DecimalFormat dFormat3 = new DecimalFormat("#0.000");
	
	public UI(GamePanel gp) {
		this.gp = gp;
		
		arial_40 = new Font("Arial", Font.PLAIN, 40);
		arial_80B = new Font("Arial", Font.BOLD, 80);
		OBJ_Key key = new OBJ_Key(gp);
		keyImage = key.image;
	}
	
	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}
	
	void debugUI(Graphics2D g2) {
		if(gp.debug) {
			g2.setColor(Color.white);
			g2.setFont(g2.getFont().deriveFont(10F));
			g2.drawString("Time: "+dFormat.format(playTime), 5, 10);
			g2.drawString("Player Speed: "+gp.player.speed, 5, 20);
			g2.drawString("Player Direction: "+gp.player.direction, 5, 30);
			g2.drawString("Player Key Count: "+gp.player.keyCount, 5, 40);
			g2.drawString("Draw Time: "+dFormat3.format(gp.drawTime/10000000)+"ms", 5, 50);
			int xPos = gp.player.worldX+(gp.tileSize/2);
			int yPos = gp.player.worldY+(gp.tileSize/2);
			int xTile = xPos / gp.tileSize;
			int yTile = yPos / gp.tileSize;
			g2.drawString("Position: "+xPos+", "+yPos, 5, 70);
			g2.drawString("Tile: "+xTile+", "+yTile, 5, 60);
		}
	}
	
	public void draw(Graphics2D g2) {
		String text;
		int textLength;
		int x;
		int y;
		
		if(gameFinished) {
			g2.setFont(arial_40);
			g2.setColor(Color.white);
			
			text = "You found the forgotten treasure!";
			textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = gp.screenWidth/2 - textLength/2;
			y = gp.screenHeight/2 + (gp.tileSize*2);
			g2.drawString(text, x, y);

			g2.setFont(arial_80B);
			g2.setColor(Color.yellow);
			
			text = "Congratulations!";
			textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = gp.screenWidth/2 - textLength/2;
			y = gp.screenHeight/2 - (gp.tileSize*3);
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
			y = gp.screenHeight - (gp.tileSize);
			g2.drawString(text, x, y);

			debugUI(g2);
			
			gp.gameThread = null;
		}
		else {
			if(gp.debug) {
				debugUI(g2);
			}
			else {
				g2.setFont(arial_40);
				g2.setColor(Color.white);
				g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
				g2.drawString("x "+gp.player.keyCount, 74, 65);
			}
			
			playTime += (double)1/60;

			
			if(messageOn) {
				g2.setFont(g2.getFont().deriveFont(30F));
				
				textLength = (int)g2.getFontMetrics().getStringBounds(message, g2).getWidth();
				x = gp.screenWidth/2 - textLength/2;
				y = gp.screenHeight - (gp.tileSize);
				
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
