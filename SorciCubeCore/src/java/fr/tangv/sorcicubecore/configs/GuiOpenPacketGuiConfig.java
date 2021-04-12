package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiOpenPacketGuiConfig extends AbstractConfig {

	public StringConfig noView;
	public StringConfig back;

	public GuiOpenPacketGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}