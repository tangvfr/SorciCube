package fr.tangv.sorcicubecore.configs;

import org.bson.Document;

import fr.tangv.sorcicubecore.config.AbstractConfig;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.StringConfig;

public class ItemConfig extends AbstractConfig {

	public StringConfig itemName;
	public StringConfig itemData;
	
	public ItemConfig(String name, String data) throws ConfigParseException {
		super(null);
		this.itemName.value = name;
		this.itemData.value = data;
	}
	
	public ItemConfig(Document doc) throws ConfigParseException {
		super(doc);
	}
	
	@Override
	public String nameString() {
		return itemName.value;
	}
	
}
