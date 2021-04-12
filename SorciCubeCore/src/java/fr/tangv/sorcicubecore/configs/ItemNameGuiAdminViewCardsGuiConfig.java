package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class ItemNameGuiAdminViewCardsGuiConfig extends AbstractConfig {

	public StringConfig sort;
	public StringConfig previous;
	public StringConfig next;
	public StringConfig close;
	public StringConfig page;

	public ItemNameGuiAdminViewCardsGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}