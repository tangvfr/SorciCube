package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiEditDeckGuiConfig extends AbstractConfig {

	public StringConfig NAME;
	public StringConfig DECK;
	public StringConfig BACK;
	public StringConfig LORE_FACTION;

	public GuiEditDeckGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}