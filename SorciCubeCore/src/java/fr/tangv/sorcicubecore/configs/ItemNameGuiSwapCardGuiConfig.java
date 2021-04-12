package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class ItemNameGuiSwapCardGuiConfig extends AbstractConfig {

	public StringConfig sort;
	public StringConfig previous;
	public StringConfig next;
	public StringConfig back;
	public StringConfig page;

	public ItemNameGuiSwapCardGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}