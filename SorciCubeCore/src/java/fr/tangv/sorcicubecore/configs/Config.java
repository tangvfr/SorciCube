package fr.tangv.sorcicubecore.configs;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.ListConfig;
import fr.tangv.sorcicubecore.configs.npc.NPCConfig;

@SuppressWarnings("unused")
public class Config extends AbstractConfig {

	public ParameterConfig parameter;
	public LocationsConfig locations;
	public MessagesConfig messages;
	public GuiConfig gui;
	public EnumConfig enums;
	public NPCConfig npc;
	public LevelConfig level;
	public ListConfig<ArenaConfig> arenas;
	private ArenaConfig _arenas;
	public ListConfig<ItemConfig> items;
	private ItemConfig _items;
	
	public Config(Document doc) throws ConfigParseException {
		super(doc);
	}

}
