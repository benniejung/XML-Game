import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music {
	private Clip startFrameMusic;
	private Clip gameBackgroundMusic1;
	private Clip gameBackgroundMusic2;
	private Clip shootMusic;
	private Clip dieMusic;
	private Clip itemMusic;
	
	
	public Music() {
		gameBackgroundMusic1 = getClip("music/background1.wav");
		gameBackgroundMusic2 = getClip("music/background2.wav");
		shootMusic = getClip("music/shoot.wav");
		dieMusic = getClip("music/die.wav");
		itemMusic = getClip("music/getItem.wav");
	}
	// 오디오 클립 가져오기
	private Clip getClip(String filePath) {
		
		Clip clip = null;
		
		try {
			clip = AudioSystem.getClip();
			File audioFile = new File(filePath);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			clip.open(audioStream);
		}
		catch (LineUnavailableException e) { e.printStackTrace(); }
		catch (UnsupportedAudioFileException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		
		return clip;
	}
	// 오디오 재생
	public void playAudio(String name) {
		
			switch(name) {
			case "startFrame":
				startFrameMusic.start();
				break;
			case "background1":
				gameBackgroundMusic1.start();
				break;
			case "background2":
				gameBackgroundMusic2.start();
				break;
			case "die":
				dieMusic.start();
				break;
			case "shoot":
				shootMusic.start();
				break;
			case "item":
				itemMusic.start();
			
		}
	}
	// 오디오 정지
	public void stopAudio(String name) {
		switch(name) {
		case "all":
			//startFrameMusic.stop();
			gameBackgroundMusic1.stop();
			gameBackgroundMusic2.stop();
			dieMusic.stop();
			shootMusic.stop();
			itemMusic.stop();
			break;
		case "startFrame":
			startFrameMusic.stop();
			break;
		case "background1":
			gameBackgroundMusic1.stop();
			break;
		case "background2":
			gameBackgroundMusic2.stop();
			break;
		case "die":
			dieMusic.stop();
			break;
		case "shoot":
			shootMusic.stop();
			break;
		case "item":
			itemMusic.stop();
			break;
		}
	}


}
