package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class MessagesConfig extends AbstractConfig {

	public StringConfig noPlayer;
	public StringConfig selectDefaultDeck;
	public StringConfig alreadySelectDefaultDeck;
	public StringConfig initializedPlayer;
	public StringConfig needForReturnSpawn;
	public StringConfig teleportSpawn;
	public StringConfig welcom;
	public StringConfig welcomBack;
	public StringConfig alreadyExistPacketCards;
	public StringConfig createPacketCards;
	public StringConfig playerNoFound;
	public StringConfig packetNoFound;
	public StringConfig givePacket;
	public StringConfig packetErrorTake;
	public StringConfig belowStartGame;
	public StringConfig invalidDeck;
	public StringConfig waitFight;
	public StringConfig waitDuel;
	public StringConfig duelSendInvite;
	public StringConfig duelReceiveInvite;
	public StringConfig duelAlreadyInvite;
	public StringConfig duelCancelInvite;
	public StringConfig leaveFight;
	public StringConfig round;
	public StringConfig manaInsufficient;
	public StringConfig spectator;
	public StringConfig spectatorEquality;
	public StringConfig winner;
	public StringConfig losser;
	public StringConfig equality;
	public StringConfig refresh;
	public StringConfig invalidCard;
	public StringConfig giveCard;
	public StringConfig giveHead;
	public StringConfig afkFight;
	public StringConfig playerUseCard;
	public StringConfig playerInvokeCard;
	public StringConfig attackEntity;
	public StringConfig spawn;
	public StringConfig spawnAction;
	public StringConfig dead;
	public StringConfig deadAction;
	public StringConfig isAttack;
	public StringConfig isAttackAction;
	public StringConfig isAttackOne;
	public StringConfig isAttackOneAction;
	public StringConfig isAttackGiveAction;
	public StringConfig isAttackGiveOneAction;
	public StringConfig rewardEndGame;
	public StringConfig changeLevel;
	public StringConfig increaseDeckMax;
	public StringConfig increaseDeckPrenium;
	public StringConfig increaseDeckNoMoney;
	public StringConfig increaseDeckUnlock;
	public StringConfig playerNoFeature;
	public StringConfig numberInvalid;
	public StringConfig actionInvalid;
	public StringConfig moneyGet;
	public StringConfig moneySet;
	public StringConfig moneyAdd;
	public StringConfig moneyRemove;
	public StringConfig itemlistNoItem;
	public StringConfig itemlistInvalidName;
	public StringConfig itemlistAlreadyName;
	public StringConfig itemlistSaved;
	public StringConfig sellerItemsNoMoney;
	public StringConfig sellerItemsBuy;
	public StringConfig packetNoMoney;
	public StringConfig packetBuyPacket;
	public StringConfig packetBuyCard;
	public StringConfig packetAlreadyCard;

	public MessagesConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}