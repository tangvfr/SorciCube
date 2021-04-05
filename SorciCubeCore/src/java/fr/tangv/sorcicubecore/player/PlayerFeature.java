package fr.tangv.sorcicubecore.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;

import fr.tangv.sorcicubecore.card.Card;

public class PlayerFeature {

	private final UUID uuid;
	private final String pseudo;
	private final DeckCards deck1;
	private final DeckCards deck2;
	private final DeckCards deck3;
	private final DeckCards deck4;
	private final DeckCards deck5;
	private volatile int unlockDecks;
	private final List<String> cardsUnlocks;
	private final List<String> rewardNPC;
	private volatile int money;
	private volatile int experience;
	private volatile byte level;
	private volatile String group;
	private volatile boolean admin;
	
	public PlayerFeature(UUID uuid,
			String pseudo,
			DeckCards deck1,
			DeckCards deck2,
			DeckCards deck3,
			DeckCards deck4,
			DeckCards deck5,
			int unlockDecks,
			List<String> cardsUnlocks,
			List<String> rewardNPC,
			int money,
			int experience,
			byte level,
			String group,
			boolean admin
		) {
		this.uuid = uuid;
		this.pseudo = pseudo;
		this.unlockDecks = unlockDecks;
		this.deck1 = deck1;
		this.deck2 = deck2;
		this.deck3 = deck3;
		this.deck4 = deck4;
		this.deck5 = deck5;
		this.cardsUnlocks = cardsUnlocks;
		this.rewardNPC = rewardNPC;
		this.money = money;
		this.experience = experience;
		this.level = level;
		this.group = group;
		this.admin = admin;
	}
	
	public String getGroup() {
		return group;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getPseudo() {
		return pseudo;
	}

	public byte getLevel() {
		return level;
	}
	
	public boolean isLevel(byte level) {
		return this.level >= level;
	}
	
	public void addLevel(byte level) {
		this.level += level;
	}

	@Deprecated
	public void setLevel(byte level) {
		this.level = level;
	}
	
	public int getExperience() {
		return experience;
	}
	
	public boolean hasExperience(int experience) {
		return this.experience >= experience;
	}
	
	public void removeExperience(int experience) {
		this.experience -= experience;
	}
	
	public void addExperience(int experience) {
		this.experience += experience;
	}
	
	@Deprecated
	public void setExperience(int experience) {
		this.experience = experience;
	}
	
	public int getMoney() {
		return money;
	}
	
	public boolean hasMoney(int money) {
		return this.money >= money;
	}
	
	public void removeMoney(int money) {
		this.money -= money;
	}
	
	public void addMoney(int money) {
		this.money += money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public int getUnlockDecks() {
		return unlockDecks;
	}

	public void setUnlockDecks(int unlockDecks) {
		this.unlockDecks = unlockDecks;
	}

	public DeckCards getDeck1() {
		return deck1;
	}
	
	public DeckCards getDeck2() {
		return deck2;
	}
	
	public DeckCards getDeck3() {
		return deck3;
	}
	
	public DeckCards getDeck4() {
		return deck4;
	}
	
	public DeckCards getDeck5() {
		return deck5;
	}
	
	public DeckCards getDeck(int number) {
		switch (number) {
			case 1:
				return deck1;
	
			case 2:
				return deck2;
				
			case 3:
				return deck3;
				
			case 4:
				return deck4;
				
			case 5:
				return deck5;
				
			default:
				return null;
		}
	}
	
	public List<String> getCardsUnlocks() {
		return cardsUnlocks;
	}
	
	public List<String> getRewardNPC() {
		return rewardNPC;
	}
	
	public Document toDocument() {
		Document doc = new Document()
				.append("uuid", uuid.toString())
				.append("pseudo", pseudo)
				.append("deck1", deck1.toDocument())
				.append("deck2", deck2.toDocument())
				.append("deck3", deck3.toDocument())
				.append("deck4", deck4.toDocument())
				.append("deck5", deck5.toDocument())
				.append("deck_unlock", unlockDecks)
				.append("cards_unlocks", cardsUnlocks)
				.append("reward_npc", rewardNPC)
				.append("money", money)
				.append("experience", experience)
				.append("level", level)
				.append("group", group)
				.append("admin", admin)
			;
		return doc;
	}
	
	public static PlayerFeature toPlayerFeature(UUID uuid, String pseudo, Map<UUID, Card> manager, Document doc) throws DeckException {
		DeckCards deck1 = DeckCards.toDeckCards(manager, doc.get("deck1", Document.class));
		DeckCards deck2 = DeckCards.toDeckCards(manager, doc.get("deck2", Document.class));
		DeckCards deck3 = DeckCards.toDeckCards(manager, doc.get("deck3", Document.class));
		DeckCards deck4 = DeckCards.toDeckCards(manager, doc.get("deck4", Document.class));
		DeckCards deck5 = DeckCards.toDeckCards(manager, doc.get("deck5", Document.class));
		int unlockDecks = doc.getInteger("deck_unlock");
		List<String> cardsUnlocks = doc.getList("cards_unlocks", String.class);
		List<String> rewardNPC = doc.getList("reward_npc", String.class);
		int money = doc.getInteger("money", 0);
		int experience = doc.getInteger("experience", 0);
		byte level = (byte) doc.getInteger("level", 1);
		String group = doc.getString("group");
		boolean admin = doc.getBoolean("admin", false);
		return new PlayerFeature(uuid, pseudo, deck1, deck2, deck3, deck4, deck5, unlockDecks, cardsUnlocks,
				((rewardNPC == null) ? new ArrayList<String>() : rewardNPC),
				money, experience, level, group, admin);
	}
	
}
