package fr.tangv.sorcicubespell.util;

import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config extends YamlConfiguration {

	private String path;
	private JavaPlugin plugin;
	
	public Config(JavaPlugin plugin, String path) throws IOException, InvalidConfigurationException {
		this(plugin, path, true);
	}
	
	public Config(JavaPlugin plugin, String path, boolean saveDefault) throws IOException, InvalidConfigurationException {
		this.path = path;
		this.plugin = plugin;
		if (saveDefault)
			saveDefault(false);
		this.load();
	}
	
	public void saveDefault(boolean replace) {
		plugin.saveResource(path, replace);
	}
	
	public void load() throws IOException, InvalidConfigurationException {
		this.load(plugin.getDataFolder().getPath()+"/"+path);
	}
	
	public void save() throws IOException {
		this.save(plugin.getDataFolder().getPath()+"/"+path);
	}
	
}
