package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiListFightGuiConfig extends AbstractConfig {

	public StringConfig NAME;
	public StringConfig UNCLASSIED;
	public StringConfig DUEL;
	public StringConfig CLOSE;
	public StatsGuiListFightGuiConfig STATS;
	public StringConfig LORE_FIGHT;
	public StringConfig DUEL_NUMBER;
	public StringConfig NO_CLASSED_NUMBER;
	public StringConfig ALL_NUMBER;

	public GuiListFightGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}