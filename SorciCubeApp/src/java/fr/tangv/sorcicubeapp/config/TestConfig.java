package fr.tangv.sorcicubeapp.config;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.BooleanConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.IntegerConfig;
import fr.tangv.sorcicubecore.config.ListConfig;
import fr.tangv.sorcicubecore.config.StringConfig;
import fr.tangv.sorcicubecore.configs.ParameterConfig;
import fr.tangv.sorcicubecore.configs.npc.RewardNPCConfig;

public class TestConfig extends AbstractConfig {

	public StringConfig test1;
	public BooleanConfig test2;
	public ParameterConfig test3;
	public ListConfig<RewardNPCConfig> test4;
	public IntegerConfig test5;
	
	public TestConfig(Document doc) throws ConfigParseException {
		super(doc);
	}

}
