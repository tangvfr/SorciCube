package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiSellerPacketsGuiConfig extends AbstractConfig {

	public StringConfig PACKET_BUY;
	public StringConfig PACKET_NAME_RIGHT;
	public StringConfig PACKET_PRICE_RIGHT;
	public StringConfig PACKET_NAME_WRONG;
	public StringConfig PACKET_PRICE_WRONG;
	public StringConfig PACKET_ERROR;
	public StringConfig CARD_BUY;
	public StringConfig CARD_NAME_RIGHT;
	public StringConfig CARD_PRICE_RIGHT;
	public StringConfig CARD_NAME_WRONG;
	public StringConfig CARD_PRICE_WRONG;
	public StringConfig CARD_ERROR;
	public StringConfig CLOSE;
	public StringConfig ERROR;

	public GuiSellerPacketsGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}