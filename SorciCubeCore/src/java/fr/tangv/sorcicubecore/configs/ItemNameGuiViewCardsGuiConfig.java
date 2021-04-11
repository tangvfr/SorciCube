package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class ItemNameGuiViewCardsGuiConfig extends AbstractConfig {

	public StringConfig SORT;
	public StringConfig PREVIOUS;
	public StringConfig NEXT;
	public StringConfig BACK;
	public StringConfig PAGE;

	public ItemNameGuiViewCardsGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}