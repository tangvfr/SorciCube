package fr.tangv.sorcicubecore.configs.npc;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.BooleanConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.IntegerConfig;
import fr.tangv.sorcicubecore.config.StringConfig;

public class ItemSellerItemsNPCConfig extends AbstractConfig {

	public BooleanConfig isEnable;
	public StringConfig nameItem;
	public IntegerConfig price;
	
	public ItemSellerItemsNPCConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}