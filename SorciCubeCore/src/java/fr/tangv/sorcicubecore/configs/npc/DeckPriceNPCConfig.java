package fr.tangv.sorcicubecore.configs.npc;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class DeckPriceNPCConfig extends AbstractConfig {

	public IntegerConfig deck1;
	public IntegerConfig deck2;
	public IntegerConfig deck3;
	public IntegerConfig deck4;
	public IntegerConfig deck5;

	public DeckPriceNPCConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}