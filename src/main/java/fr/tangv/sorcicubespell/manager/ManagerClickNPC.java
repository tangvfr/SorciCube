package fr.tangv.sorcicubespell.manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.npc.ClickNPC;
import fr.tangv.sorcicubespell.npc.EventClickNPC;
import fr.tangv.sorcicubespell.npc.SellerPacketsNPC;

public class ManagerClickNPC {

	private SorciCubeSpell sorci;
	private HashMap<String, ClickNPC> clickNPCs;
	
	private String getNameNPC(String keyEnd) {
		return sorci.getConfigNPC().getString(keyEnd);
	}
	
	private boolean playerIsInit(SorciCubeSpell sorci, Player player, boolean message) {
		if (sorci.getManagerPlayers().containtPlayer(player.getUniqueId())) {
			return true;
		} else {
			if (message)
				player.sendMessage(sorci.getMessage().getString("message_initialized_player"));
			return false;
		}
	}
	
	public ManagerClickNPC(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.clickNPCs = new HashMap<String, ClickNPC>();
		Location locationSpawn = (Location) sorci.getParameter().get("location_spawn");
		//init click npc
		clickNPCs.put(getNameNPC("edit_deck"), new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (playerIsInit(sorci, player, true))
					sorci.getManagerGui().getGuiEditOrView().open(player);
			}
		});
		clickNPCs.put(getNameNPC("increase_number_deck"), new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (playerIsInit(sorci, player, true))
					sorci.getManagerGui().getGuiIncreaseDeck().open(player);
			}
		});
		clickNPCs.put(getNameNPC("default_deck"), new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (playerIsInit(sorci, player, false))
					player.sendMessage(sorci.getMessage().getString("message_already_select_default_deck"));
				else
					sorci.getManagerGui().getGuiSelectDefaultDeck().open(player);
			}
		});
		clickNPCs.put(getNameNPC("return_spawn"), new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (!playerIsInit(sorci, player, false))
					player.sendMessage(sorci.getMessage().getString("message_need_for_return_spawn"));
				else {
					player.teleport(locationSpawn);
					player.sendMessage(sorci.getMessage().getString("message_teleport_spawn"));
				}
			}
		});
		clickNPCs.put(getNameNPC("fight"), new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (playerIsInit(sorci, player, true))
					sorci.getManagerGui().getGuiFight().open(player);
			}
		});
		clickNPCs.put(getNameNPC("leave"), new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				sorci.getManagerCreatorFight().playerLeave(player, false);
				player.sendMessage(sorci.getMessage().getString("message_leave_fight"));
			}
		});
		clickNPCs.put(getNameNPC("list_fight"), new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (playerIsInit(sorci, player, true))
					sorci.getManagerGui().getGuiListFight().open(player);
			}
		});
		for (String nameNPC : sorci.getConfigNPC().getConfigurationSection("list_seller_packet_cards").getKeys(false))
			clickNPCs.put(nameNPC.replace("§p", "."), new SellerPacketsNPC(sorci, nameNPC));
		//init bukkit
		Bukkit.getPluginManager().registerEvents(new EventClickNPC(this), sorci);
	}
	
	public void excuteClickNPC(Player player, String nameNPC) {
		if (clickNPCs.containsKey(nameNPC))
			clickNPCs.get(nameNPC).clickNPC(sorci, nameNPC, player);
	}
	
}
