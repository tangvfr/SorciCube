package fr.tangv.sorcicubespell.util;

public class WaitObject {

	public void continueCode() {
		this.notify();
	}
	
	public void waitCode(long timeout) throws InterruptedException {
		this.wait(timeout);
	}
	
}
