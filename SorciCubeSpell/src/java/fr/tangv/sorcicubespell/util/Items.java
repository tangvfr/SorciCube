package fr.tangv.sorcicubespell.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.config.ListConfig;
import fr.tangv.sorcicubecore.configs.ItemConfig;

public class Items {

	private final ConcurrentHashMap<String, ItemStack> maps;

	public Items(ListConfig<ItemConfig> items) throws IOException, InvalidConfigurationException {
		this.maps = new ConcurrentHashMap<String, ItemStack>();
		YamlConfiguration config = new YamlConfiguration();
		for (ItemConfig item : items) {
			config.load(new StringReader(item.itemData.value));
			maps.put(item.itemName.value, config.getItemStack("item"));
		}
	}
	
	public ItemStack getItem(String name) {
		return maps.get(name);
	}
	
}
