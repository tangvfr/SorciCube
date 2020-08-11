package fr.tangv.sorcicubespell;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tangv.sorcicubespell.manager.ManagerSecurity;
import fr.tangv.sorcicubespell.manager.ManagerCards;
import fr.tangv.sorcicubespell.manager.ManagerClickNPC;
import fr.tangv.sorcicubespell.manager.ManagerCreatorFight;
import fr.tangv.sorcicubespell.manager.ManagerDefaultDeck;
import fr.tangv.sorcicubespell.manager.ManagerFight;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.manager.ManagerLobby;
import fr.tangv.sorcicubespell.manager.ManagerPacketCards;
import fr.tangv.sorcicubespell.manager.ManagerPlayers;
import fr.tangv.sorcicubespell.manager.ManagerPreFightData;
import fr.tangv.sorcicubespell.manager.MongoDBManager;
import fr.tangv.sorcicubespell.refresh.CommandRefresh;
import fr.tangv.sorcicubespell.util.Config;
import fr.tangv.sorcicubespell.util.EnumTool;
import fr.tangv.sorcicubespell.util.LibLoader;
import fr.tangv.sorcicubespell.util.RenderException;

public class SorciCubeSpell extends JavaPlugin {

	private String nameServerLobby;
	private String nameServerFight;
	private String nameServerJump;
	private Config message;
	private Config parameter;
	private Config enumConfig;
	private Config guiConfig;
	private Config arenaConfig;
	private EnumTool enumTool;
	private MongoDBManager mongo;
	private ManagerCards managerCards;
	private ManagerGui managerGuiAdmin;
	private ManagerPlayers managerPlayers;
	private ManagerDefaultDeck managerDefaultDeck;
	private ManagerClickNPC managerClickNPC;
	private ManagerPacketCards managerPacketCards;
	private ManagerPreFightData managerPreFightData;
	private ManagerFight managerFight;
	private ManagerCreatorFight managerCreatorFight;
	
	@Override
	public void onEnable() {
		//try for bug
		try {
			//init lib
			LibLoader.loadLibs(new File(this.getDataFolder().getAbsolutePath()+File.separatorChar+"libs"), this);
			//init Config
			this.message = new Config(this, "message.yml");
			this.parameter = new Config(this, "parameter.yml");
			this.enumConfig = new Config(this, "enum.yml");
			this.guiConfig = new Config(this, "gui.yml");
			//init tool
			this.enumTool = new EnumTool(this.enumConfig);
			//init for change server
			getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
			this.nameServerLobby = this.parameter.getString("server_lobby");
			this.nameServerFight = this.parameter.getString("server_fight");
			this.nameServerJump = this.parameter.getString("server_jump");
			//init manager
			this.mongo = new MongoDBManager(parameter.getString("mongodb"), parameter.getString("database"));
			this.managerCards = new ManagerCards(this.mongo);
			this.managerPlayers = new ManagerPlayers(this);
			this.managerPreFightData = new ManagerPreFightData(this);
			if (getParameter().getBoolean("is_lobby")) {
				this.managerDefaultDeck = new ManagerDefaultDeck(this.mongo, this.managerCards);
				this.managerClickNPC = new ManagerClickNPC(this);
				this.managerGuiAdmin = new ManagerGui(this);
				this.managerPacketCards = new ManagerPacketCards(this);
				this.managerCreatorFight = new ManagerCreatorFight(this);
				new ManagerLobby(this);
			} else {
				this.arenaConfig = new Config(this, "arena.yml");
				this.managerFight = new ManagerFight(this);
			}
			new ManagerSecurity(this);
			getCommand("refresh").setExecutor(new CommandRefresh(this));
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
		Bukkit.shutdown();
	}
	
	public void sendPlayerToServer(Player player, String server) {
		if (player != null && player.isOnline()) {
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
	}
	
	public String formatTime(long time) {
		long timeInSec = (long) Math.ceil(time/1000D);
		long min = timeInSec/60;
		long sec = timeInSec%60;
		String format = "";
		if (min > 0)
			format += min+parameter.getString("format_time_min");
		format += sec+parameter.getString("format_time_sec");
		return format;
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
	
	public Config gertArenaConfig() {
		return arenaConfig;
	}

	public MongoDBManager getMongo() {
		return mongo;
	}
	
	public ManagerCards getManagerCards() {
		return managerCards;
	}
	
	@Nullable
	public ManagerGui getManagerGui() {
		return managerGuiAdmin;
	}
	
	public ManagerPlayers getManagerPlayers() {
		return managerPlayers;
	}
	
	@Nullable
	public ManagerDefaultDeck getManagerDefaultDeck() {
		return managerDefaultDeck;
	}
	
	@Nullable
	public ManagerClickNPC getManagerClickNPC() {
		return managerClickNPC;
	}
	
	@Nullable
	public ManagerPacketCards getManagerPacketCards() {
		return managerPacketCards;
	}
	
	public ManagerPreFightData getManagerPreFightData() {
		return managerPreFightData;
	}
	
	@Nullable
	public ManagerFight getManagerFight() {
		return managerFight;
	}
	
	@Nullable
	public ManagerCreatorFight getManagerCreatorFight() {
		return managerCreatorFight;
	}
	
}
