package fr.tangv.sorcicubespell;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tangv.sorcicubespell.manager.ManagerSecurity;
import fr.tangv.sorcicubespell.command.CommandGiveArrowHead;
import fr.tangv.sorcicubespell.command.CommandGiveCard;
import fr.tangv.sorcicubespell.command.CommandMoney;
import fr.tangv.sorcicubespell.command.CommandRefresh;
import fr.tangv.sorcicubespell.manager.ManagerCards;
import fr.tangv.sorcicubespell.manager.ManagerClickNPC;
import fr.tangv.sorcicubespell.manager.ManagerCreatorFight;
import fr.tangv.sorcicubespell.manager.ManagerDefaultDeck;
import fr.tangv.sorcicubespell.manager.ManagerFight;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.manager.ManagerLobby;
import fr.tangv.sorcicubespell.manager.ManagerPacketCards;
import fr.tangv.sorcicubespell.manager.ManagerPlayers;
import fr.tangv.sorcicubespell.manager.ManagerFightData;
import fr.tangv.sorcicubespell.manager.MongoDBManager;
import fr.tangv.sorcicubespell.util.Config;
import fr.tangv.sorcicubespell.util.EnumTool;
import fr.tangv.sorcicubespell.util.LibLoader;
import fr.tangv.sorcicubespell.util.RenderException;

public class SorciCubeSpell extends JavaPlugin {

	private boolean isLobby;
	private String nameServerLobby;
	private String nameServerFight;
	private String nameServerJump;
	private Config message;
	private Config parameter;
	private Config enumConfig;
	private Config guiConfig;
	private Config arenaConfig;
	private Config levelConfig;
	private Config configNPC;
	private EnumTool enumTool;
	private MongoDBManager mongo;
	private ManagerLobby managerLobby;
	private ManagerCards managerCards;
	private ManagerGui managerGuiAdmin;
	private ManagerPlayers managerPlayers;
	private ManagerDefaultDeck managerDefaultDeck;
	private ManagerClickNPC managerClickNPC;
	private ManagerPacketCards managerPacketCards;
	private ManagerFightData managerFightData;
	private ManagerFight managerFight;
	private ManagerCreatorFight managerCreatorFight;
	
	private Config newConfig(String name) throws Exception {
		try {
			return new Config(this, name);
		} catch (Exception e) {
			throw new Exception("Error in config named \""+name+"\"");
		}
	}
	
	@Override
	public void onEnable() {
		//try for bug
		try {
			//init lib
			LibLoader.loadLibs(new File(this.getDataFolder().getAbsolutePath()+File.separatorChar+"libs"), this);
			//init Config
			this.message = newConfig("message.yml");
			this.parameter = newConfig("parameter.yml");
			this.enumConfig = newConfig("enum.yml");
			this.guiConfig = newConfig("gui.yml");
			this.levelConfig = newConfig("level.yml");
			this.configNPC = newConfig("npc.yml");
			//is lobby
			this.isLobby = getParameter().getBoolean("is_lobby");
			//init tool
			this.enumTool = new EnumTool(this.enumConfig);
			//init for change server
			getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
			this.nameServerLobby = this.parameter.getString("server_lobby");
			this.nameServerFight = this.parameter.getString("server_fight");
			this.nameServerJump = this.parameter.getString("server_jump");
			//init manager
			this.mongo = new MongoDBManager(parameter.getString("mongodb"), parameter.getString("collection"));
			this.managerCards = new ManagerCards(this.mongo);
			this.managerPlayers = new ManagerPlayers(this);
			this.managerFightData = new ManagerFightData(this.mongo);
			if (this.isLobby) {
				this.managerFightData.removeAllFightData();
				this.managerDefaultDeck = new ManagerDefaultDeck(this.mongo, this.managerCards);
				this.managerGuiAdmin = new ManagerGui(this);
				this.managerPacketCards = new ManagerPacketCards(this);
				this.managerClickNPC = new ManagerClickNPC(this);
				this.managerCreatorFight = new ManagerCreatorFight(this);
				this.managerLobby = new ManagerLobby(this);
				CommandMoney commandMoney = new CommandMoney(this);
				getCommand("money").setExecutor(commandMoney);
				getCommand("money").setTabCompleter(commandMoney);
			} else {
				this.arenaConfig = newConfig("arena.yml");
				this.managerFight = new ManagerFight(this);
			}
			new ManagerSecurity(this);
			getCommand("refresh").setExecutor(new CommandRefresh(this));
			getCommand("givecard").setExecutor(new CommandGiveCard(this));
			getCommand("givearrowhead").setExecutor(new CommandGiveArrowHead(this));
		} catch (Exception e) {
			Bukkit.getLogger().warning(RenderException.renderException(e));
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	
	@Override
	public void onDisable() {
		if (!this.isLobby && managerFight != null) {
			managerFight.stop();
		}
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
	
	public boolean isLobby() {
		return isLobby;
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
	
	public Config getGuiConfig() {
		return guiConfig;
	}
	
	public Config getArenaConfig() {
		return arenaConfig;
	}
	
	public Config getLevelConfig() {
		return levelConfig;
	}
	
	public Config getConfigNPC() {
		return configNPC;
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
	
	public ManagerFightData getManagerFightData() {
		return managerFightData;
	}
	
	@Nullable
	public ManagerFight getManagerFight() {
		return managerFight;
	}
	
	@Nullable
	public ManagerCreatorFight getManagerCreatorFight() {
		return managerCreatorFight;
	}
	
	@Nullable
	public ManagerLobby getManagerLobby() {
		return managerLobby;
	}
	
}
