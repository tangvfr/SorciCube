package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiOpenPacketGuiConfig extends AbstractConfig {

	public StringConfig NO_VIEW;
	public StringConfig BACK;

	public GuiOpenPacketGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}