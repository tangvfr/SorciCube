package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class NpcConfig extends AbstractConfig {

	public StringConfig EDIT_DECK;
	public StringConfig RETURN_SPAWN;
	public StringConfig DEFAULT_DECK;
	public StringConfig FIGHT;
	public StringConfig LEAVE;
	public StringConfig TRASH;
	public StringConfig LIST_FIGHT;
	public StringConfig INCREASE_NUMBER_DECK;
	public DeckPriceNpcConfig INCREASE_NUMBER_DECK_PRICE;

	public NpcConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}