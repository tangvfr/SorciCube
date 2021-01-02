package fr.tangv.sorcicubespell;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tangv.sorcicubecore.handler.HandlerCards;
import fr.tangv.sorcicubecore.handler.HandlerDefaultDeck;
import fr.tangv.sorcicubecore.handler.ManagerDefaultDeck;
import fr.tangv.sorcicubecore.handler.HandlerFightData;
import fr.tangv.sorcicubecore.handler.HandlerPlayers;
import fr.tangv.sorcicubecore.handler.MongoDBManager;
import fr.tangv.sorcicubecore.util.RenderException;
import fr.tangv.sorcicubespell.command.CommandAddItemInList;
import fr.tangv.sorcicubespell.command.CommandGiveArrowHead;
import fr.tangv.sorcicubespell.command.CommandGiveCard;
import fr.tangv.sorcicubespell.command.CommandMoney;
import fr.tangv.sorcicubespell.command.CommandRefresh;
import fr.tangv.sorcicubespell.manager.ManagerClickNPC;
import fr.tangv.sorcicubespell.manager.ManagerCreatorFight;
import fr.tangv.sorcicubespell.manager.ManagerFight;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.manager.ManagerLobby;
import fr.tangv.sorcicubespell.manager.ManagerPakcetCards;
import fr.tangv.sorcicubespell.manager.ManagerSecurity;
import fr.tangv.sorcicubespell.util.Config;
import fr.tangv.sorcicubespell.util.EnumTool;
import fr.tangv.sorcicubespell.util.LibLoader;

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
	private Config configItemList;
	private EnumTool enumTool;
	private ManagerLobby managerLobby;
	private HandlerCards managerCards;
	private ManagerGui managerGuiAdmin;
	private HandlerPlayers managerPlayers;
	private HandlerDefaultDeck managerDefaultDeck;
	private ManagerClickNPC managerClickNPC;
	private ManagerPakcetCards managerPakcetCards;
	private HandlerFightData managerFightData;
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
			this.managerCards = new HandlerCards(mongo);
			this.managerPlayers = new HandlerPlayers();
			this.managerFightData = new HandlerFightData();
			if (this.isLobby) {
				this.configItemList = newConfig("itemlist.yml");
				this.configNPC = newConfig("npc.yml");
				this.managerFightData.removeAllFightData();
				this.managerDefaultDeck = new ManagerDefaultDeck(this.mongo, this.managerCards);
				this.managerGuiAdmin = new ManagerGui(this);
				this.managerPakcetCards = new ManagerPakcetCards(this);
				this.managerCreatorFight = new ManagerCreatorFight(this);
				//init for npc
				getCommand("additeminlist").setExecutor(new CommandAddItemInList(this));
				this.managerClickNPC = new ManagerClickNPC(this);
				//init for lobby
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
	
	public Config getConfigItemList() {
		return configItemList;
	}

	public MongoDBManager getMongo() {
		return mongo;
	}
	
	public HandlerCards getManagerCards() {
		return managerCards;
	}
	
	public ManagerGui getManagerGui() {
		return managerGuiAdmin;
	}
	
	public HandlerPlayers getManagerPlayers() {
		return managerPlayers;
	}
	
	public ManagerDefaultDeck getManagerDefaultDeck() {
		return managerDefaultDeck;
	}
	
	public ManagerClickNPC getManagerClickNPC() {
		return managerClickNPC;
	}
	
	public ManagerPakcetCards getManagerPakcetCards() {
		return managerPakcetCards;
	}
	
	public HandlerFightData getManagerFightData() {
		return managerFightData;
	}
	
	public ManagerFight getManagerFight() {
		return managerFight;
	}
	
	public ManagerCreatorFight getManagerCreatorFight() {
		return managerCreatorFight;
	}
	
	public ManagerLobby getManagerLobby() {
		return managerLobby;
	}
	
}
