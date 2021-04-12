package fr.tangv.sorcicubecore.configs.npc;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.ListConfig;
import fr.tangv.sorcicubecore.config.StringConfig;

public class MessageNPCConfig extends AbstractConfig {

	public StringConfig nameNPC;
	public ListConfig<StringConfig> messages;
	
	public MessageNPCConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}
