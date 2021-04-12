package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class LevelConfig extends AbstractConfig {

	public IntegerConfig experienceWin;
	public IntegerConfig moneyWin;
	public IntegerConfig experienceLoss;
	public IntegerConfig moneyLoss;
	public IntegerConfig experienceEquality;
	public IntegerConfig moneyEquality;
	public LevelsConfig levels;

	public LevelConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}