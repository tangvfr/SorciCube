package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiCreateDeckGuiConfig extends AbstractConfig {

	public StringConfig NAME;
	public StringConfig BACK;
	public StringConfig LORE_SELECT;

	public GuiCreateDeckGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}