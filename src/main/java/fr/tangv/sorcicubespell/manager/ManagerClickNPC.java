package fr.tangv.sorcicubespell.manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.npc.ClickNPC;
import fr.tangv.sorcicubespell.npc.EventClickNPC;
import fr.tangv.sorcicubespell.util.RenderException;

public class ManagerClickNPC {

	private SorciCubeSpell sorci;
	private HashMap<String, ClickNPC> clickNPCs;
	
	public ManagerClickNPC(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.clickNPCs = new HashMap<String, ClickNPC>();
		Location locationSpawn = (Location) sorci.getParameter().get("location_spawn");
		//init click npc
		clickNPCs.put(sorci.getParameter().getString("name_npc.edit_deck"), new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (sorci.getManagerPlayers().containtPlayer(player.getUniqueId()))
					sorci.getManagerGui().getGuiEditOrView().open(player);
			}
		});
		clickNPCs.put(sorci.getParameter().getString("name_npc.default_deck"), new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (sorci.getManagerPlayers().containtPlayer(player.getUniqueId()))
					player.sendMessage(sorci.getMessage().getString("message_already_select_default_deck"));
				else
					sorci.getManagerGui().getGuiSelectDefaultDeck().open(player);
			}
		});
		clickNPCs.put(sorci.getParameter().getString("name_npc.return_spawn"), new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (!sorci.getManagerPlayers().containtPlayer(player.getUniqueId()))
					player.sendMessage(sorci.getMessage().getString("message_need_for_return_spawn"));
				else {
					player.teleport(locationSpawn);
					player.sendMessage(sorci.getMessage().getString("message_teleport_spawn"));
					try {
						sorci.getManagerLobby().initPlayerLevel(player);
					} catch (Exception e) {
						Bukkit.getLogger().warning(RenderException.renderException(e));
					}
				}
			}
		});
		clickNPCs.put(sorci.getParameter().getString("name_npc.fight"), new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				if (sorci.getManagerPlayers().containtPlayer(player.getUniqueId()))
					sorci.getManagerGui().getGuiFight().open(player);
			}
		});
		clickNPCs.put(sorci.getParameter().getString("name_npc.leave"), new ClickNPC() {
			@Override
			public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
				sorci.getManagerCreatorFight().playerLeave(player, false);
				player.sendMessage(sorci.getMessage().getString("message_leave_fight"));
			}
		});
		//init bukkit
		Bukkit.getPluginManager().registerEvents(new EventClickNPC(this), sorci);
	}
	
	public void excuteClickNPC(Player player, String nameNPC) {
		if (clickNPCs.containsKey(nameNPC))
			clickNPCs.get(nameNPC).clickNPC(sorci, nameNPC, player);
	}
	
}
