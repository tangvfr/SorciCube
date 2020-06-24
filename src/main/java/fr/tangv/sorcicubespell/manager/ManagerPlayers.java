package fr.tangv.sorcicubespell.manager;

import java.util.Iterator;

import org.bson.Document;
import org.bukkit.entity.Player;

import com.mongodb.client.MongoCollection;

import fr.tangv.sorcicubespell.card.Card;
import fr.tangv.sorcicubespell.player.PlayerFeature;

public class ManagerPlayers {

	private MongoCollection<Document> players;
	private ManagerCards managerCards;
	
	public ManagerPlayers(MongoDBManager managerDB, ManagerCards managerCards) {
		this.players = managerDB.getPlayers();
		this.managerCards = managerCards;
	}
	
	public PlayerFeature getPlayerFeature(Player player) throws Exception {
		Iterator<Document> rep = players.find(Card.toUUIDDocument(player.getUniqueId())).iterator();
		if (rep.hasNext())
			return PlayerFeature.toPlayerFeature(managerCards, player, rep.next());
		else
			return null;
	}
	
	public void insert(PlayerFeature playerFeature) {
		players.insertOne(playerFeature.toDocument());
	}
	
	public void update(PlayerFeature playerFeature) {
		players.findOneAndReplace(playerFeature.toUUIDDocument(), playerFeature.toDocument());
	}
	
	public void delete(PlayerFeature playerFeature) {
		players.findOneAndDelete(playerFeature.toUUIDDocument());
	}
	
}
