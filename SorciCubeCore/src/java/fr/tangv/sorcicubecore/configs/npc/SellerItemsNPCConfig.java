package fr.tangv.sorcicubecore.configs.npc;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.StringConfig;

public class SellerItemsNPCConfig extends AbstractConfig {

	public StringConfig nameNPC;
	public ItemSellerItemsNPCConfig item1;
	public ItemSellerItemsNPCConfig item2;
	public ItemSellerItemsNPCConfig item3;
	public ItemSellerItemsNPCConfig item4;
	public ItemSellerItemsNPCConfig item5;
	public ItemSellerItemsNPCConfig item6;
	public ItemSellerItemsNPCConfig item7;
	public ItemSellerItemsNPCConfig item8;
	public ItemSellerItemsNPCConfig item9;
	
	public SellerItemsNPCConfig(Document doc) throws ConfigParseException {
		super(doc);
	}

}
