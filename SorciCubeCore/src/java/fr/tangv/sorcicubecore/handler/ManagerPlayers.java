package fr.tangv.sorcicubecore.handler;

import java.util.UUID;

import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.player.PlayerFeature;

public interface ManagerPlayers {

	/*private MongoCollection<Document> players;
	private SorciCubeSpell sorci;
	
	public ManagerPlayers(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.players = sorci.getMongo().getPlayers();
	}
	*/
	
	public boolean containtPlayer(UUID uuid);
	
	public PlayerFeature getPlayerFeature(UUID uuid) throws Exception;
	
	public void update(PlayerFeature playerFeature);
	
	//return null if failing
	public PlayerFeature initPlayer(UUID player, CardFaction faction);
	/* server this {
		try {
			if (this.containtPlayer(player))
				return false;
			//define type default deck
			DeckCards defaultDeck;
			switch (faction) {
				case DARK:
					defaultDeck = sorci.getManagerDefaultDeck().getDeckDark();
					break;
					
				case LIGHT:
					defaultDeck = sorci.getManagerDefaultDeck().getDeckLight();
					break;
					
				case NATURE:
					defaultDeck = sorci.getManagerDefaultDeck().getDeckNature();
					break;
					
				case TOXIC:
					defaultDeck = sorci.getManagerDefaultDeck().getDeckToxic();
					break;
	
				default:
					throw new Exception("Faction for default deck is invalid !");
			}
			//list cards unlocked
			List<String> cardsUnlocks = new ArrayList<String>();
			for (int i = 0; i < defaultDeck.size(); i++) {
				Card card = defaultDeck.getCard(i);
				if (card != null)
					cardsUnlocks.add(card.getUUID().toString());
			}
			//create and insert playerfeature
			PlayerFeature playerFeature = new PlayerFeature(player.getUniqueId(),
					defaultDeck,
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					manyStartDecks,
					cardsUnlocks,
					new ArrayList<String>(),
					0, 0, (byte) 1);
			this.insert(playerFeature);
			sorci.getManagerGui().getPlayerGui(player).setPlayerFeature(playerFeature);
			return true;
		} catch (Exception e) {
			Bukkit.getLogger().warning(RenderException.renderException(e));
			return false;
		}
	}*/
	
}
