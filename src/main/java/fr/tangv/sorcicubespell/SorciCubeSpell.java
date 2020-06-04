package fr.tangv.sorcicubespell;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import fr.tangv.sorcicubespell.carts.Carts;
import fr.tangv.sorcicubespell.editingcarts.EditCartsGui;
import fr.tangv.sorcicubespell.util.Config;
import fr.tangv.sorcicubespell.util.EnumTool;

public class SorciCubeSpell extends JavaPlugin {

	private MongoDatabase database;
	private EditCartsGui editCartsGui;
	private Carts carts;
	private Config message;
	private Config parameter;
	private Config gui;
	private Config enumConfig;
	private EnumTool enumTool;
	
	@Override
	public void onEnable() {
		//close all inventory of player
		for (Player player : Bukkit.getOnlinePlayers())
			player.closeInventory();
		//try for bug
		try {
			//init Config
			this.message = new Config(this, "message.yml");
			this.parameter = new Config(this, "parameter.yml");
			this.gui = new Config(this, "gui.yml");
			this.enumConfig = new Config(this, "enum.yml");
			//init tool
			this.enumTool = new EnumTool(this.enumConfig);
			//connect database
			MongoClient client = MongoClients.create(parameter.getString("mongodb"));
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
			this.carts = new Carts(database.getCollection(colCartsName));
			//gui edit menu
			this.editCartsGui = new EditCartsGui(this);
		} catch (Exception e) {
			Bukkit.getLogger().warning(e.getMessage());
			e.printStackTrace();
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

	public EnumTool getEnumTool() {
		return enumTool;
	}

	public Config getEnumConfig() {
		return enumConfig;
	}
	
}
