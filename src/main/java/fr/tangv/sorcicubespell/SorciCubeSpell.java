package fr.tangv.sorcicubespell;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tangv.sorcicubespell.util.Config;

public class SorciCubeSpell extends JavaPlugin {

	private Config message;
	private Config parameter;
	
	@Override
	public void onEnable() {
		try {
			//init Config
			this.message = new Config(this, "message.yml");
			this.parameter = new Config(this, "parameter.yml");
			//next
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().warning(e.getMessage());
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	
}
