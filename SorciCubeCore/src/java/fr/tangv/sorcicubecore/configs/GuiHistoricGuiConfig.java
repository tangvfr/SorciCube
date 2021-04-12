package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiHistoricGuiConfig extends AbstractConfig {

	public StringConfig name;

	public GuiHistoricGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}