package fr.tangv.sorcicubecore.configs.npc;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.IntegerConfig;
import fr.tangv.sorcicubecore.config.StringConfig;

public class RewardNPCConfig extends AbstractConfig {

	public StringConfig id;
	public StringConfig nameNPC;
	public IntegerConfig reward;
	public StringConfig message;
	
	public RewardNPCConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
	@Override
	public String nameString() {
		return id.value;
	}
	
}
