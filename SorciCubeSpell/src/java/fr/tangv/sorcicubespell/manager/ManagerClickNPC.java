package fr.tangv.sorcicubespell.manager;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubecore.sorciclient.ReponseRequestException;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.npc.ClickNPC;
import fr.tangv.sorcicubespell.npc.EventClickNPC;
import fr.tangv.sorcicubespell.npc.MessageNPC;
import fr.tangv.sorcicubespell.npc.RewardNPC;
import fr.tangv.sorcicubespell.npc.SellerItemsNPC;
import fr.tangv.sorcicubespell.npc.SellerPacketsNPC;
import fr.tangv.sorcicubespell.npc.TrashNPC;
import fr.tangv.sorcicubespell.util.Config;

public class ManagerClickNPC {

	private SorciCubeSpell sorci;
	private HashMap<String, ClickNPC> clickNPCs;
	
	private String getNameNPC(String keyEnd) {
		return sorci.getConfigNPC().getString(keyEnd);
	}
	
	private boolean playerIsInit(SorciCubeSpell sorci, Player player, boolean message) {
		try {
			if (sorci.getHandlerPlayers().containtPlayer(player.getUniqueId())) {
				return true;
			} else {
				if (message)
					player.sendMessage(sorci.getMessage().getString("message_initialized_player"));
				return false;
			}
		} catch (IOException | ReponseRequestException | RequestException e) {
			e.printStackTrace();
			player.sendMessage(e.getMessage());
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
		//trash
		clickNPCs.put(getNameNPC("trash"), new TrashNPC(sorci));
		//init config
		Config configNPC = sorci.getConfigNPC();
		//seller packet
		for (String key : configNPC.getConfigurationSection("list_seller_packet_cards").getKeys(false))
			clickNPCs.put(configNPC.getString("list_seller_packet_cards."+key+".name_npc"), new SellerPacketsNPC(sorci, key));
		//rewarder
		for (String key : configNPC.getConfigurationSection("npc_rewards").getKeys(false))
			clickNPCs.put(configNPC.getString("npc_rewards."+key+".name_npc"), new RewardNPC(sorci, key));
		//messager
		for (String key : configNPC.getConfigurationSection("npc_messages").getKeys(false))
			clickNPCs.put(configNPC.getString("npc_messages."+key+".name_npc"), new MessageNPC(configNPC.getStringList("npc_messages."+key+".messages")));
		//seller items
		for (String key : configNPC.getConfigurationSection("npc_seller_item").getKeys(false)) {
			try {
				SellerItemsNPC sellerItemsNPC = new SellerItemsNPC(sorci, key);
				clickNPCs.put(sellerItemsNPC.getNameNPC(), sellerItemsNPC);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//init bukkit
		Bukkit.getPluginManager().registerEvents(new EventClickNPC(this), sorci);
	}
	
	public void excuteClickNPC(Player player, String nameNPC) {
		if (clickNPCs.containsKey(nameNPC))
			clickNPCs.get(nameNPC).clickNPC(sorci, nameNPC, player);
	}
	
}
