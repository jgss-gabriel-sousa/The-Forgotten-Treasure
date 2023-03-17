package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.plaf.synth.SynthOptionPaneUI;

public class InputHandler implements KeyListener {
	GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
	
	int dialogueDelay = 0;
	
	public InputHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	@Override
	public void keyTyped(KeyEvent e){}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		
		if(gp.gameState == gp.TITLE_STATE) 			titleState(code);
		else if(gp.gameState == gp.PLAY_STATE) 		playState(code);
		else if(gp.gameState == gp.DIALOGUE_STATE) 	dialogueState(code);
		else if(gp.gameState == gp.PAUSE_STATE)		pauseState(code);
		else if(gp.gameState == gp.CHARACTER_STATE)	characterState(code);
		
		//All States
		if(code == KeyEvent.VK_QUOTE) {			
			if(gp.debug)
				gp.debug = false;
			else
				gp.debug = true;
		}
	}

	void titleState(int code) {
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			gp.ui.commandNum--;
			if(gp.ui.commandNum < 0) {
				gp.ui.commandNum = 3;
			}
		}	
		
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum++;
			if(gp.ui.commandNum > 3) {
				gp.ui.commandNum = 0;
			}
		}

		if(code == KeyEvent.VK_ENTER) {	
			if(gp.ui.commandNum == 0) {
				gp.gameState = gp.PLAY_STATE;
				gp.playMusic(gp.music.BGM);
			}
			if(gp.ui.commandNum == 1) {
				;
			}
			if(gp.ui.commandNum == 2) {
				;
			}
			if(gp.ui.commandNum == 3) {
				System.exit(0);
			}
		}
	}
	
	void playState(int code) {
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			upPressed = true;
		}		
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
		
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			downPressed = true;
		}
		
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}
		
		if(code == KeyEvent.VK_P) {
			gp.gameState = gp.PAUSE_STATE;
		}

		if(code == KeyEvent.VK_ENTER) {	
			enterPressed = true;
		}

		if(code == KeyEvent.VK_I || code == KeyEvent.VK_TAB) {
			gp.gameState = gp.CHARACTER_STATE;
		}
		
		if(code == KeyEvent.VK_F) {
			shotKeyPressed = true;
		}
	}
	
	void pauseState(int code) {
		if(code == KeyEvent.VK_P) {
			gp.gameState = gp.PLAY_STATE;
		}
	}
	
	void dialogueState(int code) {
		if(code == KeyEvent.VK_ENTER && dialogueDelay > 30) {
			gp.gameState = gp.PLAY_STATE;
		}
	}
	
	void characterState(int code) {
		if(code == KeyEvent.VK_I ||  code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_TAB) {
			gp.gameState = gp.PLAY_STATE;
			gp.playSFX(gp.sfx.CURSOR);
		}
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			if(gp.ui.slotRow != 0)
				gp.ui.slotRow--;
			
			gp.playSFX(gp.sfx.CURSOR);
		}		
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			if(gp.ui.slotCol != 0)
				gp.ui.slotCol--;
			
			gp.playSFX(gp.sfx.CURSOR);
		}
		
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			if(gp.ui.slotRow != 3)
				gp.ui.slotRow++;
			
			gp.playSFX(gp.sfx.CURSOR);
		}
		
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			if(gp.ui.slotCol != 4)
				gp.ui.slotCol++;
			
			gp.playSFX(gp.sfx.CURSOR);
		}
		if(code == KeyEvent.VK_ENTER) {
			gp.player.selectItem();
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();

		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			upPressed = false;
		}		
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			leftPressed = false;
		}
		
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			downPressed = false;
		}
		
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			rightPressed = false;
		}
		
		if(code == KeyEvent.VK_F) {
			shotKeyPressed = false;
		}
	}
}
