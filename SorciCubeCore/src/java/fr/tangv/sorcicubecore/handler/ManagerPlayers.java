package fr.tangv.sorcicubecore.handler;

import java.util.UUID;

import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.player.PlayerFeature;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;

public class ManagerPlayers {

	private SorciClient sorci;
	
	public ManagerPlayers(SorciClient sorci) {
		this.sorci = sorci;
	}
	
	public boolean containtPlayer(UUID uuid) {
		return false;
	};
	
	public PlayerFeature getPlayer(UUID uuid) throws Exception {return null;};
	
	public void update(PlayerFeature playerFeature) {};
	
	//return null if failing
	public PlayerFeature initPlayer(UUID player, CardFaction faction) {return null;};
	/* server this {
		
	}*/
	
}
