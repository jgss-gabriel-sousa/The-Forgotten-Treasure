package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable {
	
	// Screen Settings
	final int originalTileSize = 16;	//16x16 Default Tile Sizes
	final int scale = 3;
	
	public final int tileSize = originalTileSize * scale;
	
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol;		//768px
	public final int screenHeight = tileSize * maxScreenRow;	//576px
	
	// World Settings
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	
	//System
	TileManager tileManager = new TileManager(this);
	public InputHandler inputHandler = new InputHandler(this);
	public CollisionChecker collisionChecker = new CollisionChecker(this);
	public AssetManager assetManager = new AssetManager(this);
	public UI ui = new UI(this);
	public EventHandler eventHandler = new EventHandler(this);
	Thread gameThread;

	Sound music = new Sound();
	public Sound sfx = new Sound();
	
	final int FPS_CAP = 60;
	public boolean debug = false;
	public double drawTime;
	double[] drawTimes = new double[60];
	int drawIndex = 0;
	double drawStart;
	double playTime;
	
	//Game States
	public int gameState;
	public final int LOADING_STATE = -1;
	public final int TITLE_STATE = 0;
	public final int PLAY_STATE = 1;
	public final int PAUSE_STATE = 2;
	public final int DIALOGUE_STATE = 3;
	public final int CHARACTER_STATE = 4;
	
	//Entity and Object
	public Player player = new Player(this, inputHandler);
	public Entity obj[] = new Entity[50];
	public Entity npc[] = new Entity[50];
	public Entity monster[] = new Entity[50];
	public ArrayList<Entity> entityList = new ArrayList<>();
	
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(inputHandler);
		this.setFocusable(true);
	}

	public void setupGame() {
		gameState = LOADING_STATE;
		
		assetManager.setObject();
		assetManager.setNPC();
		assetManager.setMonster();
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS_CAP;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		
		while(gameThread != null) {
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				delta--;
			}

			if(timer >= 1000000000) {
				timer = 0;
			}
		}
	}
	
	public void update() {
		if(gameState == LOADING_STATE) {
			if(playTime >= 0) {
				gameState = TITLE_STATE;
				playTime = 0;
			}
			playTime += (double)1/60;
		}
		
		
		if(gameState == PLAY_STATE) {
			player.update();
			
			for(int i = 0; i < npc.length; i++) {
				if(npc[i] != null) {
					npc[i].update();
				}
			}
			
			for(int i = 0; i < monster.length; i++) {
				if(monster[i] != null) {
					if(monster[i].alive && !monster[i].dying) {
						monster[i].update();
					}
					if(!monster[i].alive) {
						monster[i] = null;
					}
				}
			}
			
			playTime += (double)1/60;
		}
		if(gameState == PAUSE_STATE) {
			;
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		if(debug) {
			drawStart = 0;
			drawStart = System.nanoTime();
		}
		
		if(gameState == LOADING_STATE || gameState == TITLE_STATE) {
			ui.draw(g2);
		}
		else{
			tileManager.draw(g2);
			
			//Add Entities to the list
			entityList.add(player);
			
			for(int i = 0; i < npc.length; i++) {
				if(npc[i] != null) {
					entityList.add(npc[i]);
				}
			}
			
			for(int i = 0; i < obj.length; i++) {
				if(obj[i] != null) {
					entityList.add(obj[i]);
				}
			}

			for(int i = 0; i < monster.length; i++) {
				if(monster[i] != null) {
					entityList.add(monster[i]);
				}
			}
			
			//Sort Entities
			Collections.sort(entityList, new Comparator<Entity>() {

				@Override
				public int compare(Entity e1, Entity e2) {
					int result = Integer.compare(e1.worldY, e2.worldY);
					return result;
				}
			});
			
			//Draw Entities
			for(int i = 0; i < entityList.size(); i++) {
				entityList.get(i).draw(g2);
			}
			
			ui.draw(g2);
			
			//Reset Entities List
			entityList.clear();
		}
		

		if(debug) {
			long drawEnd = System.nanoTime();
			drawTimes[drawIndex] = drawEnd - drawStart;
			drawIndex++;
			
			if(drawIndex == drawTimes.length) {
				drawIndex = 0;
				drawTime = 0;

				for(int i = 0; i < drawTimes.length; i++) {
					drawTime += drawTimes[i];
				}
				drawTime /= drawTimes.length;
			}
		}
		
		g2.dispose();
	}
	
	public void playMusic(int id) {
		music.setFile(id);
		music.play();
		music.loop();
	}
	
	public void stopMusic() {
		music.stop();
	}
	
	public void playSFX(int id) {
		sfx.setFile(id);
		sfx.play();
	}
}
