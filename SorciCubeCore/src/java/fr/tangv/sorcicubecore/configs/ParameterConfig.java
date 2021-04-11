package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class ParameterConfig extends AbstractConfig {

	public StringConfig NONE_LVL;
	public StringConfig NONE_GROUP;
	public StringConfig CHAT_FORMAT;
	public StringConfig ACTION_BAR_MESSAGE;
	public StringConfig FORMAT_TIME_SEC;
	public StringConfig FORMAT_TIME_MIN;
	public StringConfig FORMAT_LEVEL;
	public StringConfig CHAT_FORMAT_FIGHT;
	public StringConfig SPECTATOR_FIGHT;
	public StringConfig PLAYER_FIGHT;
	public IntegerConfig COOLDOWN_BEFORE_FIGHT;
	public IntegerConfig COOLDOWN_ONE_ROUND;
	public IntegerConfig COOLDOWN_END;
	public IntegerConfig MAX_MANA;
	public IntegerConfig START_MANA;
	public IntegerConfig PRICE_CARD;
	public IntegerConfig ROUND_MAX_AFK;
	public IntegerConfig WAIT_VIEW_FIGHT;
	public StringConfig SERVER_LOBBY;
	public StringConfig SERVER_FIGHT;
	public LocationConfig LOCATION_SPAWN;
	public LocationConfig LOCATION_TUTO;
	public LocationConfig LOCATION_NOCLASSIFIED;
	public LocationConfig LOCATION_DUEL;
	public LocationConfig LOCATION_NPC;
	public StringConfig JOIN_MESSAGE;
	public StringConfig QUIT_MESSAGE;
	public StringConfig PACKET_LORE;
	public StringConfig PACKET_FORMAT;
	public StringConfig PACKET_SIZE;

	public ParameterConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}