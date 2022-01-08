package fr.tangv.sorcicubespell.manager;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.tangv.sorcicubecore.handler.HandlerServer;
import fr.tangv.sorcicubecore.requests.RequestException;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.util.HeaderFooter;

public class ManagerSecurity implements Listener {

	private final SorciCubeSpell sorci;
	private final HandlerServer handler;
	
	public ManagerSecurity(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.handler = sorci.getHandlerServer();
		Bukkit.getPluginManager().registerEvents(this, sorci);
	}
	
	private boolean isAuto(Player player) {
		return player.getGameMode() == GameMode.CREATIVE && player.hasPermission("sorcicubespell.build");
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent e) {
		try {
			handler.playerJoin(e.getPlayer().getUniqueId());
		} catch (IOException | RequestException e1) {
			e1.printStackTrace();
		}
		HeaderFooter.sendHeaderFooter(sorci.config().parameter, e.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onLeave(PlayerQuitEvent e) {
		try {
			handler.playerLeave(e.getPlayer().getUniqueId());
		} catch (IOException | RequestException e1) {
			e1.printStackTrace();
		}
	}
	
	@EventHandler
	public void onTable(HangingBreakEvent e) {
		if (e.getCause() != RemoveCause.ENTITY)
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onTable(HangingBreakByEntityEvent e) {
		if (e.getRemover() instanceof Player && isAuto((Player) e.getRemover()))
			e.setCancelled(false);
		else
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onExplosion(BlockExplodeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onExplosion(EntityExplodeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (!isAuto(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (!isAuto(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (!isAuto(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onNoDammage(EntityDamageEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onNoDammage(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player) && e.getDamager() instanceof Player) {
			if (isAuto((Player) e.getDamager()))
				e.setCancelled(false);
		}
	}
		
}

