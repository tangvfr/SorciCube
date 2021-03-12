package fr.tangv.sorcicubeapp;

import java.io.IOException;

import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubeapp.dialog.Items;

public class SorciCubeApp {

	public static Items items;
	
	public static void main(String[] args) throws IOException {
		new FrameLogi((args.length >= 1) ? args[0] : "").setVisible(true);
		items = new Items();
	}
	
}
