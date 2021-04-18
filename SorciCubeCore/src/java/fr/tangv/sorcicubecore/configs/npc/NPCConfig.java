package fr.tangv.sorcicubecore.configs.npc;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

@SuppressWarnings("unused")
public class NPCConfig extends AbstractConfig {

	public StringConfig editDeck;
	public StringConfig returnSpawn;
	public StringConfig defaultDeck;
	public StringConfig fight;
	public StringConfig leave;
	public StringConfig trash;
	public StringConfig listFight;
	public StringConfig increaseNumberDeck;
	public DeckPriceNPCConfig increaseNumberDeckPrice;
	public ListConfig<RewardNPCConfig> rewardNPCs;
	private RewardNPCConfig _rewardNPCs;
	public ListConfig<MessageNPCConfig> messageNPCs;
	private MessageNPCConfig _messageNPCs;
	public ListConfig<SellerCardsNPCConfig> sellerCardsNPCs;
	private SellerCardsNPCConfig _sellerCardsNPCs;
	public ListConfig<SellerItemsNPCConfig> sellerItemsNPCs;
	private SellerItemsNPCConfig _sellerItemsNPCs;
	
	public NPCConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}