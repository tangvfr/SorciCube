package fr.tangv.sorcicubespell;

import java.io.IOException;
import java.util.Iterator;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import fr.tangv.sorcicubespell.carts.Carts;
import fr.tangv.sorcicubespell.editingcarts.EditCartsGui;
import fr.tangv.sorcicubespell.util.Config;

public class SorciCubeSpell extends JavaPlugin {

	private MongoDatabase database;
	private EditCartsGui editCartsGui;
	private Carts carts;
	private Config message;
	private Config parameter;
	private Config gui;
	
	@Override
	public void onEnable() {
		try {
			//init Config
			this.message = new Config(this, "message.yml");
			this.parameter = new Config(this, "parameter.yml");
			this.gui = new Config(this, "gui.yml");
			//connect database
			String user = parameter.getString("user");
			String password = parameter.getString("password");
			String host = parameter.getString("host");
			MongoClient client = MongoClients.create("mongodb://"+user+":"+password+"@"+host+"/?authSource="+parameter.getString("databaseAuth"));
			MongoDatabase database = client.getDatabase(parameter.getString("database"));
			Iterator<String> listCol = database.listCollectionNames().iterator();
			//init carts
			String colCartsName = "carts";
			boolean hasCarts = false;
			while (listCol.hasNext()) {
				String name = listCol.next();
				if (name.equals(colCartsName)) {
					hasCarts = true;
					break;
				}	
			}
			if (!hasCarts)
				database.createCollection(colCartsName);
			MongoCollection<Document> carts = database.getCollection(colCartsName);
			this.carts = new Carts(carts);
			//gui edit menu
			this.editCartsGui = new EditCartsGui(this);
		} catch (Exception e) {
			Bukkit.getLogger().warning(e.getMessage());
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	
	public Config getMessage() {
		return message;
	}
	
	public Config getParameter() {
		return parameter;
	}
	
	public Config getGui() {
		return gui;
	}
	
	public Carts getCarts() {
		return carts;
	}

	public MongoDatabase getMongoDatabase() {
		return database;
	}
	
	public EditCartsGui getEditCartsGui() {
		return editCartsGui;
	}
	
}
