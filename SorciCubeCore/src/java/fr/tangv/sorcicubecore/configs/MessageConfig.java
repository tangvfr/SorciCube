package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class MessageConfig extends AbstractConfig {

	public StringConfig messageNoPlayer;
	public StringConfig messageSelectDefaultDeck;
	public StringConfig messageAlreadySelectDefaultDeck;
	public StringConfig messageInitializedPlayer;
	public StringConfig messageNeedForReturnSpawn;
	public StringConfig messageTeleportSpawn;
	public StringConfig messageWelcom;
	public StringConfig messageWelcomBack;
	public StringConfig messageAlreadyExistPacketCards;
	public StringConfig messageCreatePacketCards;
	public StringConfig messagePlayerNoFound;
	public StringConfig messagePacketNoFound;
	public StringConfig messageGivePacket;
	public StringConfig messagePacketErrorTake;
	public StringConfig messageBelowStartGame;
	public StringConfig messageInvalidDeck;
	public StringConfig messageWaitFight;
	public StringConfig messageWaitDuel;
	public StringConfig messageDuelSendInvite;
	public StringConfig messageDuelReceiveInvite;
	public StringConfig messageDuelAlreadyInvite;
	public StringConfig messageDuelCancelInvite;
	public StringConfig messageLeaveFight;
	public StringConfig messageRound;
	public StringConfig messageManaInsufficient;
	public StringConfig messageSpectator;
	public StringConfig messageSpectatorEquality;
	public StringConfig messageWinner;
	public StringConfig messageLosser;
	public StringConfig messageEquality;
	public StringConfig messageRefresh;
	public StringConfig messageInvalidCard;
	public StringConfig messageGiveCard;
	public StringConfig messageGiveHead;
	public StringConfig messageAfkFight;
	public StringConfig messagePlayerUseCard;
	public StringConfig messagePlayerInvokeCard;
	public StringConfig messageAttackEntity;
	public StringConfig messageSpawn;
	public StringConfig messageSpawnAction;
	public StringConfig messageDead;
	public StringConfig messageDeadAction;
	public StringConfig messageIsAttack;
	public StringConfig messageIsAttackAction;
	public StringConfig messageIsAttackOne;
	public StringConfig messageIsAttackOneAction;
	public StringConfig messageIsAttackGiveAction;
	public StringConfig messageIsAttackGiveOneAction;
	public StringConfig messageRewardEndGame;
	public StringConfig messageChangeLevel;
	public StringConfig messageIncreaseDeckMax;
	public StringConfig messageIncreaseDeckPrenium;
	public StringConfig messageIncreaseDeckNoMoney;
	public StringConfig messageIncreaseDeckUnlock;
	public StringConfig messagePlayerNoFeature;
	public StringConfig messageNumberInvalid;
	public StringConfig messageActionInvalid;
	public StringConfig messageMoneyGet;
	public StringConfig messageMoneySet;
	public StringConfig messageMoneyAdd;
	public StringConfig messageMoneyRemove;
	public StringConfig messageItemlistNoItem;
	public StringConfig messageItemlistInvalidName;
	public StringConfig messageItemlistAlreadyName;
	public StringConfig messageItemlistSaved;
	public StringConfig messageSellerItemsNoMoney;
	public StringConfig messageSellerItemsBuy;
	public StringConfig messagePacketNoMoney;
	public StringConfig messagePacketBuyPacket;
	public StringConfig messagePacketBuyCard;
	public StringConfig messagePacketAlreadyCard;

	public MessageConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}