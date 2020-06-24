package fr.tangv.sorcicubespell.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.CommandGuiAdminViewCards;
import fr.tangv.sorcicubespell.gui.EventGuiPlayer;
import fr.tangv.sorcicubespell.gui.GuiAdminViewCards;
import fr.tangv.sorcicubespell.gui.GuiSelectDefaultDeck;
import fr.tangv.sorcicubespell.gui.PlayerGui;

public class ManagerGui {

	private SorciCubeSpell sorci;
	private Map<Player, PlayerGui> playerGuis;
	private GuiAdminViewCards guiViewCards;
	private GuiSelectDefaultDeck guiSelectDefaultDeck;
	
	public ManagerGui(SorciCubeSpell sorci) {
		this.playerGuis = new ConcurrentHashMap<Player, PlayerGui>();
		this.guiViewCards = new GuiAdminViewCards(this);
		this.guiSelectDefaultDeck = new GuiSelectDefaultDeck(this);
		//spigot init
		sorci.getCommand("viewcards").setExecutor(new CommandGuiAdminViewCards(this));
		Bukkit.getPluginManager().registerEvents(new EventGuiPlayer(this), sorci);
	}

	public SorciCubeSpell getSorci() {
		return sorci;
	}
	
	public Map<Player, PlayerGui> getPlayerGuis() {
		return playerGuis;
	}
	
	public GuiAdminViewCards getGuiAdminViewCards() {
		return guiViewCards;
	}

	public GuiSelectDefaultDeck getGuiSelectDefaultDeck() {
		return guiSelectDefaultDeck;
	}
	
}
