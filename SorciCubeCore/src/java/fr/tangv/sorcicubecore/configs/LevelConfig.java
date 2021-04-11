package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class LevelConfig extends AbstractConfig {

	public IntegerConfig EXPERIENCE_WIN;
	public IntegerConfig MONEY_WIN;
	public IntegerConfig EXPERIENCE_LOSS;
	public IntegerConfig MONEY_LOSS;
	public IntegerConfig EXPERIENCE_EQUALITY;
	public IntegerConfig MONEY_EQUALITY;

	public LevelConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}