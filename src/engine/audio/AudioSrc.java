package engine.audio;

import org.lwjgl.openal.AL10;

public class AudioSrc {
	
	public int srcID;
	
	public float volume = 1;
	
	private int boundSound = -1;
	
	public void bind(int buffer) {
		this.boundSound = buffer;
	}
	
	public void unBind() {
		this.boundSound = -1;
	}
	
	public AudioSrc() {
	}
	
	public void play(int buffer) {
	}
	
	public void play(int buffer, float vol) {
	}
	
	public void play() {
	}
	
	public void delete() {
	}
	
	public void pause() {
	}
	
	public void contPlaying() {
	}
	
	public void stop() {
	}
	
	//*********************************************
	
	public void setVelocity(float x, float y, float z) {
	}
	
	public void setLooping(boolean loop) {
	}
	
	public boolean isPlaying() {
		return false;
	}
	
	public void setVolume(float vol) {
	}
	
	public void setPitch(float pitch) {
	}
	
	public void setPosition(float x, float y, float z) {
	}

}
