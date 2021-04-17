package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiIncreaseDeckGuiConfig extends BasicGuiConfig {

	public StringConfig max;
	public StringConfig unlock;
	public StringConfig prenium;
	public StringConfig priceWrong;
	public StringConfig priceRight;

	public GuiIncreaseDeckGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}