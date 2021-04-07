package fr.tangv.sorcicubespell.manager;

import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

import fr.tangv.sorcicubecore.handler.HandlerGroups;
import fr.tangv.sorcicubecore.player.Group;
import fr.tangv.sorcicubespell.SorciCubeSpell;

public class ManagerPermissions implements Listener {

	private final SorciCubeSpell plugin;
	private final HandlerGroups groups;
	private final ConcurrentHashMap<UUID, PermissionAttachment> attachements;
	
	public ManagerPermissions(SorciCubeSpell plugin, HandlerGroups groups) {
		this.plugin = plugin;
		this.groups = groups;
		this.attachements = new ConcurrentHashMap<UUID, PermissionAttachment>();
	}
	
	//return display group
	public String applyPermission(Player player, boolean isAdmin, String groupName) {
		PermissionAttachment attach = attachements.remove(player.getUniqueId());
		if (attach != null)
			player.removeAttachment(attach);
		attach = player.addAttachment(plugin);
		attachements.put(player.getUniqueId(), attach);
		//admin
		for (Permission perm : Bukkit.getPluginManager().getPermissions())
			attach.setPermission(perm, isAdmin);
		//group
		Group group = groups.get(groupName);
		if (group == null)
			return "";
		for (Entry<String, Boolean> perm : group.getAllPerms().entrySet())
			attach.setPermission(perm.getKey(), perm.getValue());
		//end
		player.recalculatePermissions();
		return group.getDisplay();
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent e) {
		attachements.remove(e.getPlayer().getUniqueId());
	}
	
}
