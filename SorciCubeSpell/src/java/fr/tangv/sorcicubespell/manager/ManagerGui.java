package fr.tangv.sorcicubespell.manager;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.handler.HandlerPlayers;
import fr.tangv.sorcicubecore.player.DeckException;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.command.CommandGuiAdminViewCards;
import fr.tangv.sorcicubespell.gui.EventGuiPlayer;
import fr.tangv.sorcicubespell.gui.GuiAdminViewCards;
import fr.tangv.sorcicubespell.gui.GuiChangeSort;
import fr.tangv.sorcicubespell.gui.GuiCreateDeck;
import fr.tangv.sorcicubespell.gui.GuiDecks;
import fr.tangv.sorcicubespell.gui.GuiEditDeck;
import fr.tangv.sorcicubespell.gui.GuiEditOrView;
import fr.tangv.sorcicubespell.gui.GuiFight;
import fr.tangv.sorcicubespell.gui.GuiFightDeck;
import fr.tangv.sorcicubespell.gui.GuiIncreaseDeck;
import fr.tangv.sorcicubespell.gui.GuiListFight;
import fr.tangv.sorcicubespell.gui.GuiSelectDefaultDeck;
import fr.tangv.sorcicubespell.gui.GuiSwapCard;
import fr.tangv.sorcicubespell.gui.GuiViewCards;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.player.RunnableActionBar;

public class ManagerGui {

	private SorciCubeSpell sorci;
	private ConcurrentHashMap<Player, PlayerGui> playerGuis;
	private RunnableActionBar runnableActionBar;
	private GuiAdminViewCards guiAdminViewCards;
	private GuiSelectDefaultDeck guiSelectDefaultDeck;
	private GuiEditOrView guiEditOrView;
	private GuiViewCards guiViewCards;
	private GuiDecks guiDecks;
	private GuiEditDeck guiEditDeck;
	private GuiSwapCard guiSwapCard;
	private GuiChangeSort guiChangeSort;
	private GuiCreateDeck guiCreateDeck;
	private GuiFightDeck guiFightDeck;
	private GuiFight guiFight;
	private GuiListFight guiListFight;
	private GuiIncreaseDeck guiIncreaseDeck;
	
	public ManagerGui(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.playerGuis = new ConcurrentHashMap<Player, PlayerGui>();
		this.guiAdminViewCards = new GuiAdminViewCards(this);
		this.guiSelectDefaultDeck = new GuiSelectDefaultDeck(this);
		this.guiEditOrView = new GuiEditOrView(this);
		this.guiViewCards = new GuiViewCards(this);
		this.guiDecks = new GuiDecks(this);
		this.guiEditDeck = new GuiEditDeck(this);
		this.guiSwapCard = new GuiSwapCard(this);
		this.guiChangeSort = new GuiChangeSort(this);
		this.guiCreateDeck = new GuiCreateDeck(this);
		this.guiFightDeck = new GuiFightDeck(this);
		this.guiFight = new GuiFight(this);
		this.guiListFight = new GuiListFight(this);
		this.guiIncreaseDeck = new GuiIncreaseDeck(this);
		//spigot init
		sorci.getCommand("viewcards").setExecutor(new CommandGuiAdminViewCards(this));
		Bukkit.getPluginManager().registerEvents(new EventGuiPlayer(this), sorci);
		this.runnableActionBar = new RunnableActionBar(sorci, playerGuis);
		Bukkit.getScheduler().runTaskTimerAsynchronously(sorci, runnableActionBar, 0, 20);
	}

	public SorciCubeSpell getSorci() {
		return sorci;
	}
	
	public void updateDisplayPlayer(PlayerGui player) {
		runnableActionBar.updateOnePlayer(player);
	}
	
	public PlayerGui getPlayerGui(Player player) {
		return playerGuis.get(player);
	}
	
	public void removePlayerGui(Player player) {
		playerGuis.remove(player);
	}
	
	public void putPlayerGui(Player player, PlayerGui playerGui) {
		playerGuis.put(player, playerGui);
	}
	
	public GuiAdminViewCards getGuiAdminViewCards() {
		return guiAdminViewCards;
	}

	public GuiSelectDefaultDeck getGuiSelectDefaultDeck() {
		return guiSelectDefaultDeck;
	}

	public GuiEditOrView getGuiEditOrView() {
		return guiEditOrView;
	}
	
	public GuiViewCards getGuiViewCards() {
		return guiViewCards;
	}
	
	public GuiDecks getGuiDecks() {
		return guiDecks;
	}
	
	public GuiEditDeck getGuiEditDeck() {
		return guiEditDeck;
	}
	
	public GuiSwapCard getGuiSwapCard() {
		return guiSwapCard;
	}
	
	public GuiChangeSort getGuiChangeSort() {
		return guiChangeSort;
	}
	
	public GuiCreateDeck getGuiCreateDeck() {
		return guiCreateDeck;
	}
	
	public GuiFightDeck getGuiFightDeck() {
		return guiFightDeck;
	}
	
	public GuiFight getGuiFight() {
		return guiFight;
	}
	
	public GuiListFight getGuiListFight() {
		return guiListFight;
	}
	
	public GuiIncreaseDeck getGuiIncreaseDeck() {
		return guiIncreaseDeck;
	}
	
	public void refreshFeaturePlayers() throws IOException, ReponseRequestException, RequestException, DeckException {
		HandlerPlayers handler = sorci.getHandlerPlayers();
		for (PlayerGui pg : playerGuis.values())
			pg.setPlayerFeatures(handler.getPlayer(pg.getUUID(), pg.getName()), sorci);
	}
	
}
