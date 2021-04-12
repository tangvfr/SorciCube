package fr.tangv.sorcicubecore.configs;

import org.bson.Document;
import fr.tangv.sorcicubecore.config.*;

public class BossBarGuiConfig extends AbstractConfig {

	public StringConfig name;
	public StringConfig color;
	public StringConfig nameArena;
	public StringConfig colorArena;
	public StringConfig nameEnd;
	public StringConfig colorEnd;

	public BossBarGuiConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
}