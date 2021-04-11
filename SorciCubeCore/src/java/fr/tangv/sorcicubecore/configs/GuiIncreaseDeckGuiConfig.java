package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiIncreaseDeckGuiConfig extends AbstractConfig {

	public StringConfig NAME;
	public StringConfig MAX;
	public StringConfig UNLOCK;
	public StringConfig PRENIUM;
	public StringConfig PRICE_WRONG;
	public StringConfig PRICE_RIGHT;

	public GuiIncreaseDeckGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}