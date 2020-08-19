package fr.tangv.sorcicubespell.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mongodb.client.MongoCollection;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.card.CardFaction;
import fr.tangv.sorcicubespell.player.DeckCards;
import fr.tangv.sorcicubespell.player.PlayerFeature;
import fr.tangv.sorcicubespell.util.RenderException;

public class ManagerPlayers {

	private MongoCollection<Document> players;
	private SorciCubeSpell sorci;
	
	public ManagerPlayers(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.players = sorci.getMongo().getPlayers();
	}
	
	public boolean containtPlayer(Player player) {
		Iterator<Document> rep = players.find(Card.toUUIDDocument(player.getUniqueId())).iterator();
		return rep.hasNext();
	}
	
	public PlayerFeature getPlayerFeature(Player player) throws Exception {
		Iterator<Document> rep = players.find(Card.toUUIDDocument(player.getUniqueId())).iterator();
		if (rep.hasNext())
			return PlayerFeature.toPlayerFeature(player, sorci.getManagerCards(), rep.next());
		else
			return null;
	}
	
	private void insert(PlayerFeature playerFeature) {
		players.insertOne(playerFeature.toDocument());
	}
	
	public void update(PlayerFeature playerFeature) {
		players.findOneAndReplace(playerFeature.toUUIDDocument(), playerFeature.toDocument());
	}
	
	public boolean initPlayer(Player player, CardFaction faction) {
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
			PlayerFeature playerFeature = new PlayerFeature(player,
					defaultDeck,
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					DeckCards.createDeckCardsEmpty(),
					sorci.getParameter().getInt("number_deck_start"),
					cardsUnlocks);
			this.insert(playerFeature);
			return true;
		} catch (Exception e) {
			Bukkit.getLogger().warning(RenderException.renderException(e));
			return false;
		}
	}
	
}
