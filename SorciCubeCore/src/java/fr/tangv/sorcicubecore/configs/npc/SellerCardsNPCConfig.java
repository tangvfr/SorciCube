package fr.tangv.sorcicubecore.configs.npc;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.StringConfig;

public class SellerCardsNPCConfig extends AbstractConfig {

	public StringConfig nameNPC;
	public ItemSellerCardsNPCConfig item1;
	public ItemSellerCardsNPCConfig item2;
	public ItemSellerCardsNPCConfig item3;
	public ItemSellerCardsNPCConfig item4;
	public ItemSellerCardsNPCConfig item5;
	public ItemSellerCardsNPCConfig item6;
	public ItemSellerCardsNPCConfig item7;
	
	public SellerCardsNPCConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}
