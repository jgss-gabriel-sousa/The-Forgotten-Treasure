package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import entity.Entity;
import object.OBJ_Heart;

public class UI {
	GamePanel gp;
	Graphics2D g2;
	
	Font maruMonica, purisaBold, arial;
	
	BufferedImage heart_full, heart_half, heart_blank;
	
	public boolean messageOn = false;
	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	public String currentDialogue = "";
	public int commandNum = 0;
	public int slotRow = 0;
	public int slotCol = 0;
	
	public boolean gameFinished = false;
	
	double playTime;
	DecimalFormat dFormat = new DecimalFormat("#0.00");
	DecimalFormat dFormat3 = new DecimalFormat("#0.000");
	
	public UI(GamePanel gp) {
		this.gp = gp;

		arial = new Font("Arial", Font.PLAIN, 20);

		try {
			InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
			maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
			
			is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
			purisaBold = Font.createFont(Font.TRUETYPE_FONT, is);
			
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//HUD Objects
		Entity heart = new OBJ_Heart(gp);
		heart_full = heart.image;
		heart_half = heart.image2;
		heart_blank = heart.image3;
	}
	
	public void addMessage(String text) {
		message.add(text);
		messageCounter.add(0);
	}
	
	void drawDebug() {
		int i = 1;
		int xPos = gp.player.worldX + (gp.tileSize/2);
		int yPos = gp.player.worldY + (gp.tileSize/2);
		int xTile = xPos / gp.tileSize;
		int yTile = yPos / gp.tileSize;
		
		g2.setFont(arial);
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(10F));
		
		g2.drawString("Time: "+dFormat.format(gp.playTime), 5, i++*10);
		g2.drawString("Position: "+xPos+", "+yPos, 5, i++*10);
		g2.drawString("Tile: "+xTile+", "+yTile, 5, i++*10);
		g2.drawString("Player Speed: "+gp.player.speed, 5, i++*10);
		g2.drawString("Player Direction: "+gp.player.direction, 5, i++*10);
		g2.drawString("Player Sprite: "+gp.player.spriteNum, 5, i++*10);
		g2.drawString("Player Collision: "+gp.player.collisionOn, 5, i++*10);
		g2.drawString("Player Collision with NPC: "+gp.player.collisionWithNPC, 5, i++*10);
		g2.drawString("Draw Time: "+dFormat3.format(gp.drawTime/10000000)+"ms", 5, i++*10);
		g2.drawString("Entity Count: "+gp.entityList.size(), 5, i++*10);
		
		if(gp.gameState == gp.TITLE_STATE) {
			for(int j = 0; j < (int)gp.screenWidth/gp.tileSize; j++) {
				g2.drawRect(j*gp.tileSize, 0, gp.tileSize, gp.tileSize);
				
				for(int k = 0; k < (int)gp.screenWidth/gp.tileSize; k++) {
					g2.drawRect(j*gp.tileSize, k*gp.tileSize, gp.tileSize, gp.tileSize);
				}
			}
		}
	}

	int textLength(String text) {
		return (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
	}
	int textHeight(String text) {
		return (int)g2.getFontMetrics().getStringBounds(text, g2).getHeight();
	}
	
	int getXforCenteredText(String text) {
		int textLength = textLength(text);
		int x = gp.screenWidth/2 - textLength/2;
		return x;
	}
	
	int getXforAlignToRightText(String text, int tailX) {
		int textLength = textLength(text);
		int x = tailX - textLength;
		return x;
	}
	
	void drawLoadingScreen() {
		g2.setColor(new Color(70,120,80));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		g2.setFont(maruMonica);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 72F));
		
		String text = "Loading...";
		int x = getXforCenteredText(text);
		int y = gp.screenHeight/2;
		
		//Shadow
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		//Main Color
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
	}
	
	void drawTitleScreen() {
		g2.setColor(new Color(70,120,80));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		g2.setFont(maruMonica);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 72F));
		
		String text = "The Forgotten Treasure";
		int x = getXforCenteredText(text);
		int y = gp.tileSize*2;
		
		//Shadow
		g2.setColor(Color.black);
		g2.drawString(text,x+5,y+5);
		
		//Main Color
		g2.setColor(Color.white);
		g2.drawString(text,x,y);
		
		//Player Image
		x = gp.screenWidth/2 - gp.tileSize;
		y += gp.tileSize*2;
		g2.drawImage(gp.player.down[0], x, y, gp.tileSize*2, gp.tileSize*2, null);
		
		//Menu
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

		g2.setColor(Color.white);
		text = "NEW GAME";
		x = getXforCenteredText(text);
		y += gp.tileSize*4;	
		if(commandNum == 0) {
			g2.setColor(Color.yellow);
			g2.drawString(">", x-gp.tileSize, y);
			g2.drawString("<", x+textLength(text)+gp.tileSize/2, y);
		}
		g2.drawString(text,x,y);

		g2.setColor(Color.white);
		text = "LOAD GAME";
		x = getXforCenteredText(text);
		y += gp.tileSize;	
		if(commandNum == 1) {
			g2.setColor(Color.yellow);
			g2.drawString(">", x-gp.tileSize, y);
			g2.drawString("<", x+textLength(text)+gp.tileSize/2, y);
		}
		g2.drawString(text,x,y);

		g2.setColor(Color.white);
		text = "CREDITS";
		x = getXforCenteredText(text);
		y += gp.tileSize;	
		if(commandNum == 2) {
			g2.setColor(Color.yellow);
			g2.drawString(">", x-gp.tileSize, y);
			g2.drawString("<", x+textLength(text)+gp.tileSize/2, y);
		}
		g2.drawString(text,x,y);

		g2.setColor(Color.white);
		text = "QUIT";
		x = getXforCenteredText(text);
		y += gp.tileSize;	
		if(commandNum == 3) {
			g2.setColor(Color.yellow);
			g2.drawString(">", x-gp.tileSize, y);
			g2.drawString("<", x+textLength(text)+gp.tileSize/2, y);
		}
		g2.drawString(text,x,y);
	}
	
	void drawPauseScreen() {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));

		String text = "PAUSED";
		int x = getXforCenteredText(text);
		int y = gp.tileSize*2;

		g2.setColor(Color.white);
		g2.drawString(text, x, y);
	}
	
	void drawDialogueScreen() {
		g2.setFont(purisaBold);
		g2.setColor(Color.white);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));
		
		int x = gp.tileSize*2;
		int y = gp.screenWidth - gp.tileSize*8 - (gp.tileSize/2);
		int width = gp.screenWidth - (gp.tileSize*4);
		int height = gp.tileSize*4;
		
		drawWindow(x, y, width, height);
		
		x += gp.tileSize;
		y += gp.tileSize;
		
		for(String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}
		
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
	}
	
	void drawCharacterScreen() {
		final int frameX = gp.tileSize/2;
		final int frameY = gp.tileSize/2;
		final int frameWidth = gp.tileSize*5;
		final int frameHeight = gp.tileSize*10;
		drawWindow(frameX, frameY, frameWidth, frameHeight);
		
		g2.setColor(Color.white);
		g2.setFont(maruMonica);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int textX = frameX + 20;
		int textY = frameY + gp.tileSize;
		final int lineHeight = 35;
		
		g2.drawString("Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Exp", textX, textY);
		textY += lineHeight;
		g2.drawString("HP", textX, textY);
		textY += lineHeight;
		g2.drawString("Strength", textX, textY);
		textY += lineHeight;
		g2.drawString("Dexterity", textX, textY);
		textY += lineHeight;
		g2.drawString("Attack", textX, textY);
		textY += lineHeight;
		g2.drawString("Defense", textX, textY);
		textY += lineHeight;
		g2.drawString("Coin", textX, textY);
		textY += lineHeight + 20;
		g2.drawString("Weapon", textX, textY);
		textY += lineHeight + 16;
		g2.drawString("Shield", textX, textY);
		textY += lineHeight + 16;
		
		int tailX = (frameX + frameWidth) - 30;
		textY = frameY + gp.tileSize;
		String value;
		
		value = String.valueOf(gp.player.level);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.exp + "/" + gp.player.nextLevelExp);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gp.player.hp + "/" + gp.player.maxHP);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gp.player.strength);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.dexterity);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.attack);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.defense);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.coin);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		g2.drawImage(gp.player.currentWeapon.down[0], tailX - gp.tileSize, textY-14, null);
		textY += gp.tileSize;
		
		g2.drawImage(gp.player.currentShield.down[0], tailX - gp.tileSize, textY-14, null);
		textY += gp.tileSize;
	}
	
	void drawInventory() {
		final int frameWidth = gp.tileSize*6;
		final int frameHeight = gp.tileSize*5;
		final int frameX = gp.screenWidth-frameWidth-gp.tileSize/2;
		final int frameY = gp.tileSize/2;
		drawWindow(frameX, frameY, frameWidth, frameHeight);
		
		final int slotXstart = frameX + 20;
		final int slotYstart = frameY + 20;
		int slotX = slotXstart;
		int slotY = slotYstart;
		int slotSize = gp.tileSize+3;
		
		for(int i = 0; i < gp.player.inventory.size(); i++) {
			g2.drawImage(gp.player.inventory.get(i).down[0], slotX, slotY, null);
			
			slotX += slotSize;
			
			if(i == 4 || i == 9 || i == 14) {
				slotX = slotXstart;
				slotY += slotSize;
			}
		}
		
		int cursorX = slotXstart + (slotSize * slotCol);
		int cursorY = slotYstart + (slotSize * slotRow);
		int cursorWidth = gp.tileSize;
		int cursorHeight = gp.tileSize;
		
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
		
		//Description
		int dFrameWidth = frameWidth;
		int dFrameHeight = gp.tileSize*3;
		int dFrameX = frameX;
		int dFrameY = frameY + frameHeight;
		drawWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
		
		int textX = dFrameX + 20;
		int textY = dFrameY + gp.tileSize;

		g2.setFont(maruMonica);
		g2.setFont(g2.getFont().deriveFont(20F));
		
		int itemIndex = getItemIndexOnSlot();
		
		if(itemIndex < gp.player.inventory.size()) {
			
			for(String line: gp.player.inventory.get(itemIndex).description.split("\n")) {
				g2.drawString(line, textX, textY);
				textY += 32;
			}
		}
	}
	
	int getItemIndexOnSlot() {
		return slotCol + (slotRow*5);
	}
	
	void drawWindow(int x, int y, int width, int heigth) {
		Color c = new Color(0,0,0,210);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, heigth, 35, 35);
		
		c = new Color(255,255,255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, heigth-10, 25, 25);
	}
	
	void drawPlayerHP(){
		int x = gp.tileSize/2;
		int y = gp.tileSize/2;
		int i = 0;
		
		while(i < gp.player.maxHP/2) {
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x += gp.tileSize;
		}
		
		x = gp.tileSize/2;
		y = gp.tileSize/2;
		i = 0;
		
		while(i < gp.player.hp) {
			g2.drawImage(heart_half, x, y, null);
			i++;
			if(i < gp.player.hp) {
				g2.drawImage(heart_full, x, y, null);
			}
			i++;
			x += gp.tileSize;
		}
	}
	
	void drawMessage(){
		int messageX = gp.tileSize/4;
		int messageY = gp.tileSize*3;

		g2.setFont(maruMonica);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24F));
		
		for(int i = 0; i < message.size(); i++) {
			if(message.get(i) != null) {
				g2.setColor(Color.black);
				g2.drawString(message.get(i), messageX+2, messageY+2);
				g2.setColor(Color.white);
				g2.drawString(message.get(i), messageX, messageY);
				
				int counter = messageCounter.get(i) + 1;
				messageCounter.set(i, counter);
				messageY += 35;
				
				if(messageCounter.get(i) > 180) {
					message.remove(i);
					messageCounter.remove(i);
				}
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		this.g2 = g2;
		
		if(gp.gameState == gp.LOADING_STATE) {
			drawLoadingScreen();
		}
		if(gp.gameState == gp.TITLE_STATE) {
			drawTitleScreen();
		}
		if(gp.gameState == gp.PLAY_STATE) {
			drawPlayerHP();
			drawMessage();
		}
		if(gp.gameState == gp.PAUSE_STATE) {
			drawPauseScreen();
			drawPlayerHP();
		}
		if(gp.gameState == gp.DIALOGUE_STATE) {
			drawDialogueScreen();
			drawPlayerHP();
			
			gp.inputHandler.dialogueDelay++;
		}
		if(gp.gameState == gp.CHARACTER_STATE) {
			drawCharacterScreen();
			drawInventory();
		}
		
		if(gp.debug) {
			drawDebug();
		}
	}
}
