package fr.tangv.sorcicubespell;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tangv.sorcicubespell.manager.ManagerSecurity;
import fr.tangv.sorcicubespell.manager.ManagerCards;
import fr.tangv.sorcicubespell.manager.ManagerClickNPC;
import fr.tangv.sorcicubespell.manager.ManagerDefaultDeck;
import fr.tangv.sorcicubespell.manager.ManagerFight;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.manager.ManagerLobby;
import fr.tangv.sorcicubespell.manager.ManagerPacketCards;
import fr.tangv.sorcicubespell.manager.ManagerPlayers;
import fr.tangv.sorcicubespell.manager.ManagerPreFightData;
import fr.tangv.sorcicubespell.manager.MongoDBManager;
import fr.tangv.sorcicubespell.util.Config;
import fr.tangv.sorcicubespell.util.EnumTool;
import fr.tangv.sorcicubespell.util.RenderException;

public class SorciCubeSpell extends JavaPlugin {

	private Config message;
	private Config parameter;
	private Config enumConfig;
	private Config guiConfig;
	private EnumTool enumTool;
	private MongoDBManager mongo;
	private ManagerCards managerCards;
	private ManagerGui managerGuiAdmin;
	private ManagerPlayers managerPlayers;
	private ManagerDefaultDeck managerDefaultDeck;
	private ManagerClickNPC managerClickNPC;
	private ManagerPacketCards managerPacketCards;
	private ManagerPreFightData managerPreFightData;
	private String nameServerLobby;
	private String nameServerFight;
	private String nameServerJump;
	
	@Override
	public void onEnable() {
		//try for bug
		try {
			//init Config
			this.message = new Config(this, "message.yml");
			this.parameter = new Config(this, "parameter.yml");
			this.enumConfig = new Config(this, "enum.yml");
			this.guiConfig = new Config(this, "gui.yml");
			//init tool
			this.enumTool = new EnumTool(this.enumConfig);
			//init manager
			this.mongo = new MongoDBManager(parameter.getString("mongodb"), parameter.getString("database"));
			this.managerCards = new ManagerCards(this.mongo);
			this.managerGuiAdmin = new ManagerGui(this);
			this.managerPlayers = new ManagerPlayers(this);
			this.managerDefaultDeck = new ManagerDefaultDeck(this.mongo, this.managerCards);
			this.managerClickNPC = new ManagerClickNPC(this);
			this.managerPacketCards = new ManagerPacketCards(this);
			this.managerPreFightData = new ManagerPreFightData(this);
			if (getParameter().getBoolean("is_lobby")) {
				new ManagerLobby(this);
			} else {
				new ManagerFight(this);
			}
			new ManagerSecurity(this);
			//init for change server
			getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
			this.nameServerLobby = this.parameter.getString("server_lobby");
			this.nameServerFight = this.parameter.getString("server_fight");
			this.nameServerJump = this.parameter.getString("server_jump");
		} catch (Exception e) {
			Bukkit.getLogger().warning(RenderException.renderException(e));
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	
	@Override
	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers())
			player.closeInventory();
		Bukkit.getScheduler().cancelTasks(this);
	}
	
	public void sendPlayerToServer(Player player, String server) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(data);
		try {
			out.writeUTF("Connect");
			out.writeUTF(server);
		} catch (Exception e) {
			Bukkit.getLogger().warning(RenderException.renderException(e));
		}
		player.sendPluginMessage(this, "BungeeCord", data.toByteArray());
	}
	
	public String getNameServerLobby() {
		return nameServerLobby;
	}
	
	public String getNameServerFight() {
		return nameServerFight;
	}
	
	public String getNameServerJump() {
		return nameServerJump;
	}
	
	public Config getMessage() {
		return message;
	}
	
	public Config getParameter() {
		return parameter;
	}

	public EnumTool getEnumTool() {
		return enumTool;
	}

	public Config getEnumConfig() {
		return enumConfig;
	}
	
	public Config gertGuiConfig() {
		return guiConfig;
	}

	public MongoDBManager getMongo() {
		return mongo;
	}
	
	public ManagerCards getManagerCards() {
		return managerCards;
	}
	
	public ManagerGui getManagerGui() {
		return managerGuiAdmin;
	}
	
	public ManagerPlayers getManagerPlayers() {
		return managerPlayers;
	}
	
	public ManagerDefaultDeck getManagerDefaultDeck() {
		return managerDefaultDeck;
	}
	
	public ManagerClickNPC getManagerClickNPC() {
		return managerClickNPC;
	}
	
	public ManagerPacketCards getManagerPacketCards() {
		return managerPacketCards;
	}
	
	public ManagerPreFightData getManagerPreFightData() {
		return managerPreFightData;
	}
	
}
