package fr.tangv.sorcicubespell.manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.configs.npc.MessageNPCConfig;
import fr.tangv.sorcicubecore.configs.npc.NPCConfig;
import fr.tangv.sorcicubecore.configs.npc.RewardNPCConfig;
import fr.tangv.sorcicubecore.configs.npc.SellerCardsNPCConfig;
import fr.tangv.sorcicubecore.configs.npc.SellerItemsNPCConfig;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.npc.ClickNPC;
import fr.tangv.sorcicubespell.npc.EventClickNPC;
import fr.tangv.sorcicubespell.npc.MessageNPC;
import fr.tangv.sorcicubespell.npc.RewardNPC;
import fr.tangv.sorcicubespell.npc.SellerItemsNPC;
import fr.tangv.sorcicubespell.npc.SellerPacketsNPC;
import fr.tangv.sorcicubespell.npc.TrashNPC;

public class ManagerClickNPC {

	private SorciCubeSpell sorci;
	private HashMap<String, ClickNPC> clickNPCs;
	
	private boolean playerIsInit(SorciCubeSpell sorci, Player player, boolean message) {
		if (sorci.getManagerGui().getPlayerGui(player).getPlayerFeatures() != null) {
			return true;
		} else {
			if (message)
				player.sendMessage(sorci.config().messages.initializedPlayer.value);
			return false;
		}
	}
	
	public ManagerClickNPC(SorciCubeSpell sorci) throws Exception {
		this.sorci = sorci;
		this.clickNPCs = new HashMap<String, ClickNPC>();
		final Location locationSpawn = SorciCubeSpell.convertLocation(sorci.config().locations.locationSpawn);
		NPCConfig npc = sorci.config().npc;
		//init click npc
		clickNPCs.put(npc.editDeck.value, new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (playerIsInit(sorci, player, true))
					sorci.getManagerGui().getGuiEditOrView().open(player);
			}
		});
		clickNPCs.put(npc.increaseNumberDeck.value, new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (playerIsInit(sorci, player, true))
					sorci.getManagerGui().getGuiIncreaseDeck().open(player);
			}
		});
		clickNPCs.put(npc.defaultDeck.value, new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (playerIsInit(sorci, player, false))
					player.sendMessage(sorci.config().messages.alreadySelectDefaultDeck.value);
				else
					sorci.getManagerGui().getGuiSelectDefaultDeck().open(player);
			}
		});
		clickNPCs.put(npc.returnSpawn.value, new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (!playerIsInit(sorci, player, false))
					player.sendMessage(sorci.config().messages.needForReturnSpawn.value);
				else {
					player.teleport(locationSpawn);
					player.sendMessage(sorci.config().messages.teleportSpawn.value);
				}
			}
		});
		clickNPCs.put(npc.fight.value, new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (playerIsInit(sorci, player, true))
					sorci.getManagerGui().getGuiFight().open(player);
			}
		});
		clickNPCs.put(npc.leave.value, new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				sorci.getManagerCreatorFight().playerLeave(player, false);
				player.sendMessage(sorci.config().messages.leaveFight.value);
			}
		});
		clickNPCs.put(npc.listFight.value, new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (playerIsInit(sorci, player, true))
					sorci.getManagerGui().getGuiListFight().open(player);
			}
		});
		//trash
		clickNPCs.put(npc.trash.value, new TrashNPC(sorci));
		//seller packet
		for (SellerCardsNPCConfig item : npc.sellerCardsNPCs)
			clickNPCs.put(item.nameNPC.value, new SellerPacketsNPC(sorci, item));
		//rewarder
		for (RewardNPCConfig item : npc.rewardNPCs)
			clickNPCs.put(item.nameNPC.value, new RewardNPC(sorci, item));
		//messager
		for (MessageNPCConfig item : npc.messageNPCs)
			clickNPCs.put(item.nameNPC.value, new MessageNPC(item.messages));
		//seller items
		for (SellerItemsNPCConfig item : npc.sellerItemsNPCs)
			clickNPCs.put(item.nameNPC.value, new SellerItemsNPC(sorci, item));
		//init bukkit
		Bukkit.getPluginManager().registerEvents(new EventClickNPC(this), sorci);
	}
	
	public void excuteClickNPC(Player player, String nameNPC) {
		if (clickNPCs.containsKey(nameNPC))
			clickNPCs.get(nameNPC).clickNPC(sorci, nameNPC, player);
	}
	
}
