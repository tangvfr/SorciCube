package fr.tangv.sorcicubespell;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tangv.sorcicubecore.card.CardFaction;
import fr.tangv.sorcicubecore.clients.Client;
import fr.tangv.sorcicubecore.clients.ClientIdentification;
import fr.tangv.sorcicubecore.clients.ClientType;
import fr.tangv.sorcicubecore.config.ConfigParseException;
import fr.tangv.sorcicubecore.config.LocationConfig;
import fr.tangv.sorcicubecore.configs.Config;
import fr.tangv.sorcicubecore.configs.FactionColorEnumConfig;
import fr.tangv.sorcicubecore.handler.HandlerCards;
import fr.tangv.sorcicubecore.handler.HandlerConfig;
import fr.tangv.sorcicubecore.handler.HandlerFightData;
import fr.tangv.sorcicubecore.handler.HandlerGroups;
import fr.tangv.sorcicubecore.handler.HandlerPlayers;
import fr.tangv.sorcicubecore.handler.HandlerServer;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.requests.Request;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.requests.RequestType;
import fr.tangv.sorcicubecore.sorciclient.ResponseRequestException;
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
import fr.tangv.sorcicubespell.manager.ManagerPermissions;
import fr.tangv.sorcicubespell.manager.ManagerSecurity;
import fr.tangv.sorcicubespell.util.EnumTool;
import fr.tangv.sorcicubespell.util.WaitObject;
import net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;

public class SorciCubeSpell extends JavaPlugin {

	//stats
	private static final long TIMEOUT = 10_000;
	private boolean isLobby;
	private String nameServer;
	
	//server
	private String nameServerLobby;
	private String nameServerFight;
	
	//handler
	private ManagerPakcetCards managerPakcetCards;
	private HandlerPlayers handlerPlayers;
	private HandlerCards handlerCards;
	private HandlerFightData handlerFightData;
	private HandlerConfig handlerConfig;
	private HandlerServer handlerServer;
	private HandlerGroups handlerGroups;
	
	//manager
	private ManagerLobby managerLobby;
	private ManagerGui managerGui;
	private ManagerClickNPC managerClickNPC;
	private ManagerFight managerFight;
	private ManagerCreatorFight managerCreatorFight;
	private ManagerPermissions managerPermissions;
	
	//tools
	private EnumTool enumTool;
	
	@Override
	public void onEnable() {
		//try for bug
		try {
			this.isLobby = !Boolean.parseBoolean(System.getenv("SC_FIGHT"));
			Bukkit.getLogger().info("Fight: "+!isLobby);
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
				public synchronized void disconnected() {
					Bukkit.getLogger().warning("SorciClient is disconnected !");
					Bukkit.getPluginManager().disablePlugin(SorciCubeSpell.this);
				}
				
				@Override
				public synchronized void connected() {
					w.continueCode();
				}

				@Override
				public void handlingRequest(Client client, Request request) throws Exception {
					if (request.requestType == RequestType.PLAYER_UPDATING && isLobby) {
						Player player = Bukkit.getPlayer(UUID.fromString(request.name));
						if (player != null)
							managerGui.getPlayerGui(player).setPlayerFeatures(handlerPlayers.getPlayer(player.getUniqueId(), player.getName()), SorciCubeSpell.this);
					} else if (request.requestType == RequestType.PLAYER_SEND) {
						Player player = Bukkit.getPlayer(UUID.fromString(request.name));
						if (player != null)
							((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(request.data), (byte) 0));
					} else if (request.requestType == RequestType.START_SERVERS_REFRESH) {
						SorciCubeSpell.this.refresh();
					} 
				}
				
			};
			client.start();
			w.waitCode(TIMEOUT);
			if (!client.isAuthentified())
				throw new Exception("SorciClient is not connected !");
			//handler for config
			SorciCubeSpell.this.handlerConfig = new HandlerConfig(client);
			//init tool
			FactionColorEnumConfig color = config().enums.factionColor;
			CardFaction.initColors(color.basic.value, color.dark.value, color.light.value, color.nature.value, color.toxic.value);
			SorciCubeSpell.this.enumTool = new EnumTool(SorciCubeSpell.this);
			//init for change server
			SorciCubeSpell.this.handlerServer = new HandlerServer(client);
			getServer().getMessenger().registerOutgoingPluginChannel(SorciCubeSpell.this, "BungeeCord");
			//init handler
			SorciCubeSpell.this.handlerGroups = new HandlerGroups(client);
			SorciCubeSpell.this.handlerCards = new HandlerCards(client);
			SorciCubeSpell.this.handlerPlayers = new HandlerPlayers(client, handlerCards);
			SorciCubeSpell.this.handlerFightData = new HandlerFightData(client);
			if (SorciCubeSpell.this.isLobby) {
				SorciCubeSpell.this.handlerFightData.removeAllFightDataServer(nameServer);
				SorciCubeSpell.this.managerGui = new ManagerGui(SorciCubeSpell.this);
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
				SorciCubeSpell.this.managerFight = new ManagerFight(SorciCubeSpell.this);
			}
			//security
			new ManagerSecurity(SorciCubeSpell.this);
			//attachements
			SorciCubeSpell.this.managerPermissions = new ManagerPermissions(this, handlerGroups);
			Bukkit.getPluginManager().registerEvents(managerPermissions, this);
			//commands
			getCommand("refresh").setExecutor(new CommandRefresh(SorciCubeSpell.this));
			getCommand("givecard").setExecutor(new CommandGiveCard(SorciCubeSpell.this));
			getCommand("givearrowhead").setExecutor(new CommandGiveArrowHead(SorciCubeSpell.this));
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
			format += min+config().parameter.formatTimeMin.value;
		format += sec+config().parameter.formatTimeSec.value;
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
	
	//handler
	
	public ManagerPakcetCards getManagerPakcetCards() {
		return managerPakcetCards;
	}
	
	public HandlerPlayers getHandlerPlayers() {
		return handlerPlayers;
	}
	
	public HandlerFightData getHandlerFightData() {
		return handlerFightData;
	}
	
	public HandlerCards getHandlerCards() {
		return handlerCards;
	}
	
	public Config config() {
		return handlerConfig.getConfig();
	}
	
	public HandlerServer getHandlerServer() {
		return handlerServer;
	}
	
	//manager

	public ManagerCreatorFight getManagerCreatorFight() {
		return managerCreatorFight;
	}
	
	public ManagerLobby getManagerLobby() {
		return managerLobby;
	}
	
	public ManagerGui getManagerGui() {
		return managerGui;
	}
	
	public ManagerClickNPC getManagerClickNPC() {
		return managerClickNPC;
	}
	
	public ManagerFight getManagerFight() {
		return managerFight;
	}
	
	public ManagerPermissions getManagerPermissions() {
		return managerPermissions;
	}
	
	//tools
	
	public EnumTool getEnumTool() {
		return enumTool;
	}
	
	public Location convertLocation(LocationConfig loc) {
		return new Location(Bukkit.getWorld(loc.world), loc.x, loc.y, loc.z, loc.pitch, loc.yaw);
	}
	
	//refresh
	
	public void refresh() throws IOException, ResponseRequestException, RequestException, DeckException, ConfigParseException {
		handlerConfig.refreshConfig();
		FactionColorEnumConfig color = config().enums.factionColor;
		CardFaction.initColors(color.basic.value, color.dark.value, color.light.value, color.nature.value, color.toxic.value);
		handlerCards.refresh();
		handlerGroups.refresh();
		if (isLobby) {
			managerPakcetCards.refresh();
			managerGui.refreshFeaturePlayers();
		}
	}
	
}
