package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiSellerPacketsGuiConfig extends AbstractConfig {

	public StringConfig packetBuy;
	public StringConfig packetNameRight;
	public StringConfig packetPriceRight;
	public StringConfig packetNameWrong;
	public StringConfig packetPriceWrong;
	public StringConfig packetError;
	public StringConfig cardBuy;
	public StringConfig cardNameRight;
	public StringConfig cardPriceRight;
	public StringConfig cardNameWrong;
	public StringConfig cardPriceWrong;
	public StringConfig cardError;
	public StringConfig close;
	public StringConfig error;

	public GuiSellerPacketsGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}