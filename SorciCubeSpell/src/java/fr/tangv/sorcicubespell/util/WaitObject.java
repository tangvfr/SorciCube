package fr.tangv.sorcicubespell.util;

public class WaitObject {

	public synchronized void continueCode() {
		this.notify();
	}
	
	public synchronized void waitCode(long timeout) throws InterruptedException {
		this.wait(timeout);
	}
	
}
