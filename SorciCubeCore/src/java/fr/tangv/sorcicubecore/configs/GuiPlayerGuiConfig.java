package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class GuiPlayerGuiConfig extends AbstractConfig {

	public StringConfig nul;
	public StringConfig none;
	public StringConfig next;
	public StringConfig nextDesc;
	public StringConfig stickView;
	public StringConfig stickViewDesc;
	public StringConfig buy;
	public StringConfig buyDesc;
	public StringConfig swap;
	public StringConfig swapDesc;
	public StringConfig backLobby;
	public StringConfig backLobbyDesc;

	public GuiPlayerGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}