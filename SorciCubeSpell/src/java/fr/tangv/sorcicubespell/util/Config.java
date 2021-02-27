package fr.tangv.sorcicubespell.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.tangv.sorcicubecore.handler.HandlerConfigYAML;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;

public class Config extends YamlConfiguration {
	
	private final HandlerConfigYAML handler;
	private final String name;
	
	public Config(HandlerConfigYAML handler, String name) throws FileNotFoundException, IOException, InvalidConfigurationException, ReponseRequestException, RequestException {
		this.handler = handler;
		this.name = name;
		load();
	}
	
	public void load() throws FileNotFoundException, IOException, InvalidConfigurationException, ReponseRequestException, RequestException {
		this.load(new StringReader(handler.getConfig(name)));
	}
	
	public void save() throws IOException, ReponseRequestException, RequestException {
		handler.updateConfig(name, this.saveToString());
	}
	
}
