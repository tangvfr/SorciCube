package fr.tangv.sorcicubespell;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tangv.sorcicubespell.manager.ManagerCards;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.manager.ManagerPlayers;
import fr.tangv.sorcicubespell.manager.MongoDBManager;
import fr.tangv.sorcicubespell.util.Config;
import fr.tangv.sorcicubespell.util.EnumTool;

public class SorciCubeSpell extends JavaPlugin {

	private MongoDBManager mongo;
	private ManagerCards carts;
	private Config message;
	private Config parameter;
	private Config enumConfig;
	private Config cartConfig;
	private Config guiConfig;
	private EnumTool enumTool;
	private ManagerGui managerGuiAdmin;
	private ManagerPlayers managerPlayers;
	
	@Override
	public void onEnable() {
		//try for bug
		try {
			//init Config
			this.message = new Config(this, "message.yml");
			this.parameter = new Config(this, "parameter.yml");
			this.enumConfig = new Config(this, "enum.yml");
			this.cartConfig = new Config(this, "cart.yml");
			this.guiConfig = new Config(this, "gui.yml");
			//init tool
			this.enumTool = new EnumTool(this.enumConfig);
			//init manager
			this.mongo = new MongoDBManager(parameter.getString("mongodb"), parameter.getString("database"));
			this.carts = new ManagerCards(this.mongo);
			this.managerGuiAdmin = new ManagerGui(this);
			this.managerPlayers = new ManagerPlayers(this.mongo, this.carts);
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
	
	public ManagerCards getCarts() {
		return carts;
	}

	public EnumTool getEnumTool() {
		return enumTool;
	}

	public Config getEnumConfig() {
		return enumConfig;
	}

	public Config getCartConfig() {
		return cartConfig;
	}
	
	public Config gertGuiConfig() {
		return guiConfig;
	}

	public MongoDBManager getMongo() {
		return mongo;
	}
	
	public ManagerGui getManagerGuiAdmin() {
		return managerGuiAdmin;
	}
	
	public ManagerPlayers getManagerPlayers() {
		return managerPlayers;
	}
	
}
