package fr.tangv.sorcicubespell.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Config extends YamlConfiguration {

	private Plugin plugin;
	private String path;
	private File file;
	
	public Config(JavaPlugin plugin, String path) throws IOException, InvalidConfigurationException {
		this(plugin, path, true);
	}
	
	public Config(JavaPlugin plugin, String path, boolean saveDefault) throws IOException, InvalidConfigurationException {
		this.path = path;
		this.file = new File(plugin.getDataFolder().getPath()+"/"+path);
		this.plugin = plugin;
		if (saveDefault)
			saveDefault(false);
		this.load();
	}
	
	public void saveDefault(boolean replace) throws IOException {
		if (!file.exists() || replace) {
			if (!file.exists())
				file.createNewFile();
			InputStream in = plugin.getResource(path);
			FileOutputStream out = new FileOutputStream(file);
			int len;
			byte[] buf = new byte[1024];
			while ((len = in.read(buf)) != -1)
				out.write(buf, 0, len);
			out.close();
			in.close();
		}
	}
	
	public void load() throws IOException, InvalidConfigurationException {
		this.load(file);
	}
	
	public void save() throws IOException {
		this.save(file);
	}
	
}
