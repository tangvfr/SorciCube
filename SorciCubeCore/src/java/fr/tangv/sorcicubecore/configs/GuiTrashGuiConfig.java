package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiTrashGuiConfig extends AbstractConfig {

	public StringConfig NAME;
	public StringConfig DUMPED;

	public GuiTrashGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}