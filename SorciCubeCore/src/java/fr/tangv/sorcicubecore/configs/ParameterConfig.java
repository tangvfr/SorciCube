package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class ParameterConfig extends AbstractConfig {

	public StringConfig headerList;
	public StringConfig footerList;
	public StringConfig noneLvl;
	public StringConfig noneGroup;
	public StringConfig nonePrefix;
	public StringConfig chatFormat;
	public StringConfig actionBarMessage;
	public StringConfig formatTimeSec;
	public StringConfig formatTimeMin;
	public StringConfig formatSubName;
	public StringConfig chatFormatFight;
	public StringConfig spectatorFight;
	public StringConfig playerFight;
	public IntegerConfig cooldownBeforeFight;
	public IntegerConfig cooldownOneRound;
	public IntegerConfig cooldownEnd;
	public IntegerConfig maxMana;
	public IntegerConfig startMana;
	public IntegerConfig priceCard;
	public IntegerConfig roundMaxAfk;
	public IntegerConfig waitViewFight;
	public StringConfig serverLobby;
	public StringConfig serverFight;
	public StringConfig joinMessage;
	public StringConfig quitMessage;
	public StringConfig packetLore;
	public StringConfig packetFormat;
	public StringConfig packetSize;
	
	public ParameterConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}