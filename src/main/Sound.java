package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
	Clip clip;
	URL soundURL[] = new URL[30];

	public final int BGM = 0;
	public final int COIN = 1;
	public final int POWERUP = 2;
	public final int UNLOCK = 3;
	public final int FANFARE = 4;
	
	public Sound() {
		soundURL[BGM] = getClass().getResource("/sound/bgm.wav");
		soundURL[COIN] = getClass().getResource("/sound/coin.wav");
		soundURL[POWERUP] = getClass().getResource("/sound/powerup.wav");
		soundURL[UNLOCK] = getClass().getResource("/sound/unlock.wav");
		soundURL[FANFARE] = getClass().getResource("/sound/fanfare.wav");
	}
	
	public void setFile(int id) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[id]);
			clip = AudioSystem.getClip();
			clip.open(ais);

			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			double gain = 0.15;
			float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
			
		}catch(Exception e) {
			;
		}
	}
	
	public void play() {
		clip.start();
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() {
		clip.stop();
	}
}
