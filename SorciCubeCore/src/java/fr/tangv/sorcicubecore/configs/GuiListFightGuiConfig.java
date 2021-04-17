package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiListFightGuiConfig extends BasicGuiConfig {

	public StringConfig unclassied;
	public StringConfig duel;
	public StringConfig close;
	public StatsGuiListFightGuiConfig stats;
	public StringConfig loreFight;
	public StringConfig duelNumber;
	public StringConfig noClassedNumber;
	public StringConfig allNumber;

	public GuiListFightGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}