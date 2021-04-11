package fr.tangv.sorcicubeapp;

import java.io.IOException;

import fr.tangv.sorcicubeapp.connection.FrameLogi;
import fr.tangv.sorcicubeapp.dialog.Items;
import fr.tangv.sorcicubecore.card.CardFaction;

public class SorciCubeApp {

	public static Items items;
	
	public static void main(String[] args) throws IOException {
		CardFaction.initColors("§7", "§8", "§6", "§2", "§5");
		new FrameLogi((args.length >= 1) ? args[0] : "");
		items = new Items();
	}
	
}
