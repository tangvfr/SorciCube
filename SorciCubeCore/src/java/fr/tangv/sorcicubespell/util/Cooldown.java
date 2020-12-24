package fr.tangv.sorcicubespell.util;

public class Cooldown {
	
	private final long time;
	private volatile long timeStart;
	private volatile long timeRemaining;
	private volatile CooldownState state;
	
	public Cooldown(long time) {
		this.time = time;
		this.state = CooldownState.STOP;
		this.timeRemaining = 0;
	}
	
	public void stop() {
		this.state = CooldownState.STOP;
	}
	
	public void start() {
		this.timeStart = System.currentTimeMillis()+time;
		this.state = CooldownState.START;
	}

	public void loop() {
		this.timeStart = System.currentTimeMillis()+time;
		this.state = CooldownState.LOOP;
	}
	
	public double getProgess() {
		return this.timeRemaining <= 0 ? 0 : this.timeRemaining/(double) this.time; 
	}
	
	public long getTime() {
		return this.time;
	}
	
	public long getTimeRemaining() {
		return this.timeRemaining;
	}
	
	public CooldownState getState() {
		return this.state;
	}
	
	public boolean update() {
		if (state != CooldownState.STOP) {
			timeRemaining = timeStart-System.currentTimeMillis();
			if (timeRemaining <= 0) {
				timeRemaining = 0;
				if (state != CooldownState.LOOP)
					this.state = CooldownState.STOP;
				else
					this.timeStart = System.currentTimeMillis()+time;
				return true;
			}
		}
		return false;
	}
	
	public enum CooldownState {
		LOOP(),
		START(),
		STOP();
	}
	
}
