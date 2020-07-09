package fr.tangv.sorcicubespell.fight;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.tangv.sorcicubespell.manager.ManagerFight;

public class EventFight implements Listener {

	private ManagerFight manager;

	public EventFight(ManagerFight manager) {
		this.manager = manager;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onChangeGameMode(PlayerGameModeChangeEvent e) {
		if (manager.getPlayerFights().containsKey(e.getPlayer())) {
			e.setCancelled(true);
			return;
		}
		for (PreFight preFight : manager.getPreFights().values())
			if (preFight.getPlayerUUID1().equals(e.getPlayer().getUniqueId())) {
				e.setCancelled(true);
				return;
			}
	}
	
	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (manager.getPlayerFights().containsKey(e.getPlayer())) {
			PlayerFight player = manager.getPlayerFights().get(e.getPlayer());
			if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
				player.setCardSelect(-1);
				player.openInvHistoric();
			}
			e.setCancelled(true);
		} else if (!e.getPlayer().hasPermission(manager.getSorci().getParameter().getString("perm_admin"))) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onClickInv(InventoryClickEvent e) {
		if (manager.getPlayerFights().containsKey(e.getWhoClicked())) {
			PlayerFight player = manager.getPlayerFights().get(e.getWhoClicked());
			if (e.getInventory().hashCode() == player.getInvHistoric().hashCode()) {
				if (player.canPlay()) {
					FightSlot slot = FightSlot.valueOfRaw(e.getRawSlot());
					if (slot != null)
						switch (slot) {
							case CARD_1:
								player.setCardSelect(0);
								player.showSelectCard();
								break;
		
							case CARD_2:
								player.setCardSelect(1);
								player.showSelectCard();
								break;
								
							case CARD_3:
								player.setCardSelect(2);
								player.showSelectCard();
								break;
								
							case CARD_4:
								player.setCardSelect(3);
								player.showSelectCard();
								break;
								
							case CARD_5:
								player.setCardSelect(4);
								player.showSelectCard();
								break;
								
							case CARD_6:
								player.setCardSelect(5);
								player.showSelectCard();
								break;
								
							case FINISH_ROUND:
								player.getFight().nextRound();
								break;
								
							default:
								break;
						}
					player.getPlayer().sendMessage("Click raw: "+e.getRawSlot());
				}
			} else {
				player.openInvHistoric();
			}
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onOpenInv(InventoryOpenEvent e) {
		if (manager.getPlayerFights().containsKey(e.getPlayer()) ? 
			manager.getPlayerFights().get(e.getPlayer()).getInvHistoric().hashCode() !=  e.getInventory().hashCode()
			: !e.getPlayer().hasPermission(manager.getSorci().getParameter().getString("perm_admin"))) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		e.setJoinMessage("");
		player.setGameMode(GameMode.ADVENTURE);
		player.setFoodLevel(20);
		player.setMaxHealth(20);
		player.setHealth(20);
		player.setExp(1F);
		player.setLevel(0);
		player.setCollidable(false);
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[4]);
		for (Player other : Bukkit.getOnlinePlayers()) {
			other.hidePlayer(player);
			player.hidePlayer(other);
		}
		manager.playerJoin(player);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage("");
		manager.playerQuit(e.getPlayer());
	}
	
}
