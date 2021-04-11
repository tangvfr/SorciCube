package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class ItemNameGuiAdminViewCardsGuiConfig extends AbstractConfig {

	public StringConfig SORT;
	public StringConfig PREVIOUS;
	public StringConfig NEXT;
	public StringConfig CLOSE;
	public StringConfig PAGE;

	public ItemNameGuiAdminViewCardsGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}