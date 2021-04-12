package fr.tangv.sorcicubecore.configs.npc;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class DeckPriceNPCConfig extends AbstractConfig {

	public IntegerConfig DECK1;
	public IntegerConfig DECK2;
	public IntegerConfig DECK3;
	public IntegerConfig DECK4;
	public IntegerConfig DECK5;

	public DeckPriceNPCConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}