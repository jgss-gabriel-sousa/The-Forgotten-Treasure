package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
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
	InputHandler keyHandler = new InputHandler();
	Sound music = new Sound();
	Sound sfx = new Sound();
	public CollisionChecker collisionChecker = new CollisionChecker(this);
	public AssetManager assetManager = new AssetManager(this);
	public UI ui = new UI(this);
	Thread gameThread;
	
	final int FPS_CAP = 60;
	public boolean debug = false;
	public double drawTime;
	double[] drawTimes = new double[60];
	int drawIndex = 0;
	double drawStart;
	
	//Entity and Object
	public Player player = new Player(this, keyHandler);
	public SuperObject obj[] = new SuperObject[10];
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyHandler);
		this.setFocusable(true);
	}

	public void setupGame() {
		assetManager.setObject();
		playMusic(music.BGM);
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
		player.update();
		
		debug = keyHandler.debugMode;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		if(debug) {
			drawStart = 0;
			drawStart = System.nanoTime();
		}
		
		tileManager.draw(g2);
		
		//Objects
		for(int i = 0; i < obj.length; i++) {
			if(obj[i] != null) {
				obj[i].draw(g2, this);
			}
		}
		
		player.draw(g2);
		
		ui.draw(g2);

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
