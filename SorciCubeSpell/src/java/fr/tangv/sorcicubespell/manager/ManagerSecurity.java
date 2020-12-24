package fr.tangv.sorcicubespell.manager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class ManagerSecurity implements Listener {

	private SorciCubeSpell sorci;
	
	public ManagerSecurity(SorciCubeSpell sorci) {
		this.sorci = sorci;
		Bukkit.getPluginManager().registerEvents(this, sorci);
	}
	
	private boolean isAuto(Player player) {
		return player.getGameMode() == GameMode.CREATIVE && player.hasPermission(sorci.getParameter().getString("perm_build"));
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
