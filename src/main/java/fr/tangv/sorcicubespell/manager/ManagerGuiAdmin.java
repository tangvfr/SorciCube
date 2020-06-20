package fr.tangv.sorcicubespell.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.admin.CommandGuiAdminViewCards;
import fr.tangv.sorcicubespell.gui.admin.EventGuiAdminViewCards;
import fr.tangv.sorcicubespell.gui.admin.GuiAdminViewCards;
import fr.tangv.sorcicubespell.gui.admin.PlayerGuiAdmin;

public class ManagerGuiAdmin {

	private SorciCubeSpell sorci;
	private Map<Player, PlayerGuiAdmin> playerGuiAdmins;
	private GuiAdminViewCards guiAdminViewCards;
	
	public ManagerGuiAdmin(SorciCubeSpell sorci) {
		this.playerGuiAdmins = new ConcurrentHashMap<Player, PlayerGuiAdmin>();
		this.guiAdminViewCards = new GuiAdminViewCards(this);
		//spigot init
		sorci.getCommand("viewcards").setExecutor(new CommandGuiAdminViewCards(this));
		Bukkit.getPluginManager().registerEvents(new EventGuiAdminViewCards(this), sorci);
	}

	public SorciCubeSpell getSorci() {
		return sorci;
	}
	
	public Map<Player, PlayerGuiAdmin> getPlayerGuiAdmins() {
		return playerGuiAdmins;
	}
	
	public GuiAdminViewCards getGuiAdminViewCards() {
		return guiAdminViewCards;
	}
	
}
