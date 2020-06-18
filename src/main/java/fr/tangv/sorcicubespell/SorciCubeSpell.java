package fr.tangv.sorcicubespell;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tangv.sorcicubespell.cards.Cards;
import fr.tangv.sorcicubespell.manager.MongoDBManager;
import fr.tangv.sorcicubespell.util.Config;
import fr.tangv.sorcicubespell.util.EnumTool;

public class SorciCubeSpell extends JavaPlugin {

	private MongoDBManager mongo;
	private Cards carts;
	private Config message;
	private Config parameter;
	private Config enumConfig;
	private Config cartConfig;
	private EnumTool enumTool;
	
	@Override
	public void onEnable() {
		//try for bug
		try {
			//init Config
			this.message = new Config(this, "message.yml");
			this.parameter = new Config(this, "parameter.yml");
			this.enumConfig = new Config(this, "enum.yml");
			this.cartConfig = new Config(this, "cart.yml");
			//init tool
			this.enumTool = new EnumTool(this.enumConfig);
			//connect database
			this.mongo = new MongoDBManager(parameter.getString("mongodb"), parameter.getString("database"));
			this.carts = new Cards(this.mongo);
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
	
	public Cards getCarts() {
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

	public MongoDBManager getMongo() {
		return mongo;
	}
	
}
