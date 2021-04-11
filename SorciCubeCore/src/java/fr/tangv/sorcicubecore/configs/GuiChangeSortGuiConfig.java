package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiChangeSortGuiConfig extends AbstractConfig {

	public StringConfig NAME;
	public StringConfig BACK;

	public GuiChangeSortGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}