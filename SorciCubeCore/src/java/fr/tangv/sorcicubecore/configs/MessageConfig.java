package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class MessageConfig extends AbstractConfig {

	public StringConfig MESSAGE_NO_PLAYER;
	public StringConfig MESSAGE_SELECT_DEFAULT_DECK;
	public StringConfig MESSAGE_ALREADY_SELECT_DEFAULT_DECK;
	public StringConfig MESSAGE_INITIALIZED_PLAYER;
	public StringConfig MESSAGE_NEED_FOR_RETURN_SPAWN;
	public StringConfig MESSAGE_TELEPORT_SPAWN;
	public StringConfig MESSAGE_WELCOM;
	public StringConfig MESSAGE_WELCOM_BACK;
	public StringConfig MESSAGE_ALREADY_EXIST_PACKET_CARDS;
	public StringConfig MESSAGE_CREATE_PACKET_CARDS;
	public StringConfig MESSAGE_PLAYER_NO_FOUND;
	public StringConfig MESSAGE_PACKET_NO_FOUND;
	public StringConfig MESSAGE_GIVE_PACKET;
	public StringConfig MESSAGE_PACKET_ERROR_TAKE;
	public StringConfig MESSAGE_BELOW_START_GAME;
	public StringConfig MESSAGE_INVALID_DECK;
	public StringConfig MESSAGE_WAIT_FIGHT;
	public StringConfig MESSAGE_WAIT_DUEL;
	public StringConfig MESSAGE_DUEL_SEND_INVITE;
	public StringConfig MESSAGE_DUEL_RECEIVE_INVITE;
	public StringConfig MESSAGE_DUEL_ALREADY_INVITE;
	public StringConfig MESSAGE_DUEL_CANCEL_INVITE;
	public StringConfig MESSAGE_LEAVE_FIGHT;
	public StringConfig MESSAGE_ROUND;
	public StringConfig MESSAGE_MANA_INSUFFICIENT;
	public StringConfig MESSAGE_SPECTATOR;
	public StringConfig MESSAGE_SPECTATOR_EQUALITY;
	public StringConfig MESSAGE_WINNER;
	public StringConfig MESSAGE_LOSSER;
	public StringConfig MESSAGE_EQUALITY;
	public StringConfig MESSAGE_REFRESH;
	public StringConfig MESSAGE_INVALID_CARD;
	public StringConfig MESSAGE_GIVE_CARD;
	public StringConfig MESSAGE_GIVE_HEAD;
	public StringConfig MESSAGE_AFK_FIGHT;
	public StringConfig MESSAGE_PLAYER_USE_CARD;
	public StringConfig MESSAGE_PLAYER_INVOKE_CARD;
	public StringConfig MESSAGE_ATTACK_ENTITY;
	public StringConfig MESSAGE_SPAWN;
	public StringConfig MESSAGE_SPAWN_ACTION;
	public StringConfig MESSAGE_DEAD;
	public StringConfig MESSAGE_DEAD_ACTION;
	public StringConfig MESSAGE_IS_ATTACK;
	public StringConfig MESSAGE_IS_ATTACK_ACTION;
	public StringConfig MESSAGE_IS_ATTACK_ONE;
	public StringConfig MESSAGE_IS_ATTACK_ONE_ACTION;
	public StringConfig MESSAGE_IS_ATTACK_GIVE_ACTION;
	public StringConfig MESSAGE_IS_ATTACK_GIVE_ONE_ACTION;
	public StringConfig MESSAGE_REWARD_END_GAME;
	public StringConfig MESSAGE_CHANGE_LEVEL;
	public StringConfig MESSAGE_INCREASE_DECK_MAX;
	public StringConfig MESSAGE_INCREASE_DECK_PRENIUM;
	public StringConfig MESSAGE_INCREASE_DECK_NO_MONEY;
	public StringConfig MESSAGE_INCREASE_DECK_UNLOCK;
	public StringConfig MESSAGE_PLAYER_NO_FEATURE;
	public StringConfig MESSAGE_NUMBER_INVALID;
	public StringConfig MESSAGE_ACTION_INVALID;
	public StringConfig MESSAGE_MONEY_GET;
	public StringConfig MESSAGE_MONEY_SET;
	public StringConfig MESSAGE_MONEY_ADD;
	public StringConfig MESSAGE_MONEY_REMOVE;
	public StringConfig MESSAGE_ITEMLIST_NO_ITEM;
	public StringConfig MESSAGE_ITEMLIST_INVALID_NAME;
	public StringConfig MESSAGE_ITEMLIST_ALREADY_NAME;
	public StringConfig MESSAGE_ITEMLIST_SAVED;
	public StringConfig MESSAGE_SELLER_ITEMS_NO_MONEY;
	public StringConfig MESSAGE_SELLER_ITEMS_BUY;
	public StringConfig MESSAGE_PACKET_NO_MONEY;
	public StringConfig MESSAGE_PACKET_BUY_PACKET;
	public StringConfig MESSAGE_PACKET_BUY_CARD;
	public StringConfig MESSAGE_PACKET_ALREADY_CARD;

	public MessageConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}