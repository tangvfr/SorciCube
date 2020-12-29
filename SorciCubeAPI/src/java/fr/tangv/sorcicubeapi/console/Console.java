package fr.tangv.sorcicubeapi.console;

import java.util.Scanner;

import fr.tangv.sorcicubeapi.SorciCubeAPI;

public class Console extends Thread {

	private SorciCubeAPI sorci;
	
	public Console(SorciCubeAPI sorci) {
		this.sorci = sorci;
	}
	
	@Override
	public void run() {
		Scanner in = new Scanner(System.in);
		while (sorci.serverIsStart()) {
			String input = in.nextLine();
			int fs = input.indexOf((int) (' '));
			String cmd = input.substring(0, fs);
			String[] args = (input.length() >= fs+1) ? input.substring(fs+1).split(" ") : new String[0];
			
			
			
		}
		in.close();
	}
	
}
