package fr.tangv.sorcicubeapp;

import fr.tangv.sorcicubeapp.connection.FrameLogi;

public class SorciCubeApp {

	public static void main(String[] args) {
		new FrameLogi((args.length >= 1) ? args[0] : "").setVisible(true);
	}
	
}
