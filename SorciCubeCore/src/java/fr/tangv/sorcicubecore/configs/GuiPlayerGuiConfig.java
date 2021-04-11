package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiPlayerGuiConfig extends AbstractConfig {

	public StringConfig NULL;
	public StringConfig NONE;
	public StringConfig NEXT;
	public StringConfig NEXT_DESC;
	public StringConfig STICK_VIEW;
	public StringConfig STICK_VIEW_DESC;
	public StringConfig BUY;
	public StringConfig BUY_DESC;
	public StringConfig SWAP;
	public StringConfig SWAP_DESC;
	public StringConfig BACK_LOBBY;
	public StringConfig BACK_LOBBY_DESC;

	public GuiPlayerGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}