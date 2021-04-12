package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiCreateDeckGuiConfig extends AbstractConfig {

	public StringConfig name;
	public StringConfig back;
	public StringConfig loreSelect;

	public GuiCreateDeckGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}