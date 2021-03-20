package fr.tangv.sorcicubespell;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.clients.ClientIdentification;
import fr.tangv.sorcicubecore.clients.ClientType;
import fr.tangv.sorcicubecore.handler.HandlerCards;
import fr.tangv.sorcicubecore.handler.HandlerConfigYAML;
import fr.tangv.sorcicubecore.handler.HandlerDefaultDeck;
import fr.tangv.sorcicubecore.handler.HandlerFightData;
import fr.tangv.sorcicubecore.handler.HandlerPlayers;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.player.PlayerFeature;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestAnnotation;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubecore.sorciclient.SorciClient;
import fr.tangv.sorcicubecore.sorciclient.SorciClientURI;
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
import fr.tangv.sorcicubespell.util.WaitObject;

public class SorciCubeSpell extends JavaPlugin {

	//stats
	private static final long TIMEOUT = 10_000;
	private boolean isLobby;
	private String nameServer;
	
	//server
	private String nameServerLobby;
	private String nameServerFight;
	
	//config
	private Config message;
	private Config parameter;
	private Config enumConfig;
	private Config guiConfig;
	private Config arenaConfig;
	private Config levelConfig;
	private Config configNPC;
	private Config configItemList;
	
	//handler
	private ManagerPakcetCards managerPakcetCards;
	private HandlerPlayers handlerPlayers;
	private HandlerDefaultDeck handlerDefaultDeck;
	private HandlerCards handlerCards;
	private HandlerFightData handlerFightData;
	private HandlerConfigYAML handlerConfigYAML;
	
	//manager
	private ManagerLobby managerLobby;
	private ManagerGui managerGuiAdmin;
	private ManagerClickNPC managerClickNPC;
	private ManagerFight managerFight;
	private ManagerCreatorFight managerCreatorFight;
	
	//tools
	private EnumTool enumTool;
	
	private Config newConfig(String name) throws Exception {
		try {
			return new Config(handlerConfigYAML, name);
		} catch (InvalidConfigurationException e) {
			throw new Exception("Error in config named \""+name+"\"");
		} catch (IOException | ReponseRequestException | RequestException e) {
			throw new Exception("Error config \""+name+"\" caused "+e.getMessage());
		}
	}
	
	@Override
	public void onEnable() {
		//try for bug
		try {
			//init env
			this.isLobby = !Boolean.parseBoolean(System.getenv("SC_FIGHT"));
			System.out.println("Fight: "+!isLobby);
			this.nameServer = System.getenv("SC_NAME_SERVER");
			String tokenServer = System.getenv("SC_TOKEN_SERVER");
			String hostAPI = System.getenv("SC_HOST_API");
			int portAPI = Integer.parseInt(System.getenv("SC_PORT_API"));
			//init client
			WaitObject w = new WaitObject();
			SorciClient client = new SorciClient(
					new SorciClientURI(InetAddress.getByName(hostAPI), portAPI,
							new ClientIdentification(Client.VERSION_PROTOCOL, ClientType.SPIGOT.mask, nameServer, tokenServer)),
					TIMEOUT
			) {
				@Override
				public void disconnected() {
					Bukkit.getLogger().warning("SorciClient is disconnected !");
					Bukkit.getPluginManager().disablePlugin(SorciCubeSpell.this);
				}
				
				@Override
				public void connected() {
					w.continueCode();
					try {
						SorciClient client = this;
						//handler for config
						SorciCubeSpell.this.handlerConfigYAML = new HandlerConfigYAML(client);
						//init Config
						SorciCubeSpell.this.message = newConfig("message.yml");
						SorciCubeSpell.this.parameter = newConfig("parameter.yml");
						SorciCubeSpell.this.enumConfig = newConfig("enum.yml");
						SorciCubeSpell.this.guiConfig = newConfig("gui.yml");
						SorciCubeSpell.this.levelConfig = newConfig("level.yml");
						//init tool
						SorciCubeSpell.this.enumTool = new EnumTool(SorciCubeSpell.this.enumConfig);
						//init for change server
						getServer().getMessenger().registerOutgoingPluginChannel(SorciCubeSpell.this, "BungeeCord");
						SorciCubeSpell.this.nameServerLobby = SorciCubeSpell.this.parameter.getString("server_lobby");
						SorciCubeSpell.this.nameServerFight = SorciCubeSpell.this.parameter.getString("server_fight");
						//init handler
						SorciCubeSpell.this.handlerCards = new HandlerCards(client);
						SorciCubeSpell.this.handlerPlayers = new HandlerPlayers(client, handlerCards);
						SorciCubeSpell.this.handlerFightData = new HandlerFightData(client);
						if (SorciCubeSpell.this.isLobby) {
							SorciCubeSpell.this.configItemList = newConfig("itemlist.yml");
							SorciCubeSpell.this.configNPC = newConfig("npc.yml");
							SorciCubeSpell.this.handlerFightData.removeAllFightDataServer(nameServer);
							SorciCubeSpell.this.handlerDefaultDeck = new HandlerDefaultDeck(client, SorciCubeSpell.this.handlerCards);
							SorciCubeSpell.this.managerGuiAdmin = new ManagerGui(SorciCubeSpell.this);
							SorciCubeSpell.this.managerPakcetCards = new ManagerPakcetCards(client, SorciCubeSpell.this);
							SorciCubeSpell.this.managerCreatorFight = new ManagerCreatorFight(SorciCubeSpell.this);
							//init for npc
							getCommand("additeminlist").setExecutor(new CommandAddItemInList(SorciCubeSpell.this));
							SorciCubeSpell.this.managerClickNPC = new ManagerClickNPC(SorciCubeSpell.this);
							//init for lobby
							SorciCubeSpell.this.managerLobby = new ManagerLobby(SorciCubeSpell.this);
							CommandMoney commandMoney = new CommandMoney(SorciCubeSpell.this);
							getCommand("money").setExecutor(commandMoney);
							getCommand("money").setTabCompleter(commandMoney);
						} else {
							SorciCubeSpell.this.arenaConfig = newConfig("arena.yml");
							SorciCubeSpell.this.managerFight = new ManagerFight(SorciCubeSpell.this);
						}
						new ManagerSecurity(SorciCubeSpell.this);
						getCommand("refresh").setExecutor(new CommandRefresh(SorciCubeSpell.this));
						getCommand("givecard").setExecutor(new CommandGiveCard(SorciCubeSpell.this));
						getCommand("givearrowhead").setExecutor(new CommandGiveArrowHead(SorciCubeSpell.this));
					} catch (Exception e) {
						e.printStackTrace();
						Bukkit.getPluginManager().disablePlugin(SorciCubeSpell.this);
					}
				}

				@Override
				public void handlingRequest(Client client, Request request) throws Exception {}
				
				@RequestAnnotation(type=RequestType.PLAYER_UPDATING)
				public void updatingPlayer(Client client, Request request) throws IOException, ReponseRequestException, RequestException, DeckException {
					Player player = Bukkit.getPlayer(UUID.fromString(request.name));
					if (player != null) {
						PlayerFeature feature = handlerPlayers.getPlayer(player.getUniqueId(), player.getName());
						player.closeInventory();
						managerGuiAdmin.getPlayerGui(player).setPlayerFeature(feature);
					}
				}
				
			};
			client.start();
			w.waitCode(TIMEOUT);
			if (!client.isAuthentified())
				throw new Exception("SorciClient is not connected !");
		} catch (Exception e) {
			e.printStackTrace();
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
				e.printStackTrace();
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
	
	//stats
	
	public boolean isLobby() {
		return isLobby;
	}
	
	public String getNameServer() {
		return nameServer;
	}
	
	//server

	public String getNameServerLobby() {
		return nameServerLobby;
	}
	
	public String getNameServerFight() {
		return nameServerFight;
	}
	
	//config
	
	public Config getMessage() {
		return message;
	}
	
	public Config getParameter() {
		return parameter;
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
	
	//handler
	
	public ManagerPakcetCards getManagerPakcetCards() {
		return managerPakcetCards;
	}
	
	public HandlerPlayers getHandlerPlayers() {
		return handlerPlayers;
	}
	
	public HandlerDefaultDeck getHandlerDefaultDeck() {
		return handlerDefaultDeck;
	}
	
	public HandlerFightData getHandlerFightData() {
		return handlerFightData;
	}
	
	public HandlerCards getHandlerCards() {
		return handlerCards;
	}
	
	public HandlerConfigYAML getHandlerConfigYAML() {
		return handlerConfigYAML;
	}
	
	//manager

	public ManagerCreatorFight getManagerCreatorFight() {
		return managerCreatorFight;
	}
	
	public ManagerLobby getManagerLobby() {
		return managerLobby;
	}
	
	public ManagerGui getManagerGui() {
		return managerGuiAdmin;
	}
	
	public ManagerClickNPC getManagerClickNPC() {
		return managerClickNPC;
	}
	
	public ManagerFight getManagerFight() {
		return managerFight;
	}
	
	//tools
	
	public EnumTool getEnumTool() {
		return enumTool;
	}
	
}
