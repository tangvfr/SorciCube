package fr.tangv.sorcicubespell.gui;

import java.util.ArrayList;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.fight.FightData;
import fr.tangv.sorcicubespell.fight.FightStat;
import fr.tangv.sorcicubespell.fight.FightType;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class GuiListFight extends AbstractGui implements Runnable {

	private final Inventory inv;
	private Vector<FightData> listFight;
	
	public GuiListFight(ManagerGui manager) {
		super(manager, manager.getSorci().getGuiConfig().getConfigurationSection("gui_list_fight"));
		this.inv = Bukkit.createInventory(null, 54, this.name);
		ItemStack decoItem = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, " ", null, false);
		for (int i = 0; i < 6; i++)
			inv.setItem(8+(i*9), decoItem);
		inv.setItem(53, ItemBuild.buildSkull(SkullUrl.X_RED, 1, config.getString("close"), null, false));
		Bukkit.getScheduler().runTaskTimerAsynchronously(manager.getSorci(), this, 0, 20);
	}
	
	private ItemStack fightDataToItem(FightData fightData) {
		String stat = config.getString("stats."+fightData.getStat().name().toLowerCase());
		String namePlayer1 = Bukkit.getOfflinePlayer(fightData.getPlayerUUID1()).getName();
		String namePlayer2 = Bukkit.getOfflinePlayer(fightData.getPlayerUUID2()).getName();
		String levelPlayer1 = Byte.toString(fightData.getLevelPlayer1());
		String levelPlayer2 = Byte.toString(fightData.getLevelPlayer2());
		String factionPlayer1 = manager.getSorci().getEnumTool().factionToString(fightData.getFactionDeckPlayer1());
		String factionPlayer2 = manager.getSorci().getEnumTool().factionToString(fightData.getFactionDeckPlayer2());
		String fightUUID = fightData.getFightUUID().toString();
		String server = fightData.getServer();
		ArrayList<String> lore = new ArrayList<String>();
		for (String line : config.getStringList("lore_fight")) {
			lore.add(line	
				.replace("{stat}", stat)
				.replace("{name_player1}", namePlayer1)
				.replace("{name_player2}", namePlayer2)
				.replace("{level_player1}", levelPlayer1)
				.replace("{level_player2}", levelPlayer2)
				.replace("{faction_player1}", factionPlayer1)
				.replace("{faction_player2}", factionPlayer2)
				.replace("{fight_uuid}", fightUUID)
				.replace("{server}", server)
			);
		}
		if (fightData.getFightType() == FightType.DUEL) {
			return ItemBuild.buildItem(Material.IRON_SWORD, 1, (short) 0, (byte) 0, config.getString("duel"), lore, false);
		} else {
			return ItemBuild.buildSkull(SkullUrl.N_GRAY, 1, config.getString("unclassied"), lore, false);
		}
	}
	
	@Override
	public void run() {
		listFight = manager.getSorci().getManagerFightData().getAllFightData();
		int duel = 0;
		for (int i = 0; i < 48 && i < listFight.size(); i++) {
			FightData fightData = listFight.get(i);
			if (fightData.getFightType() == FightType.DUEL)
				duel++;
			inv.setItem(i+(i/8), fightDataToItem(fightData));
		}
		int all = listFight.size();
		int no_classed = all-duel;
		//duel
		inv.setItem(8, ItemBuild.buildItem(Material.IRON_SWORD, duel, (short) 0, (byte) 0, 
				config.getString("duel_number").replace("{number}", Integer.toString(duel))
		, null, false));
		//no classed
		inv.setItem(17, ItemBuild.buildSkull(SkullUrl.N_GRAY, no_classed,
				config.getString("no_classed_number").replace("{number}", Integer.toString(no_classed))
		, null, false));
		//all
		inv.setItem(35, ItemBuild.buildItem(Material.FIREBALL, all, (short) 0, (byte) 0,
				config.getString("all_number").replace("{number}", Integer.toString(all))
		, null, false));
	}

	@Override
	public Inventory getInventory(Player player) {
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		e.setCancelled(true);
		int raw = e.getRawSlot();
		if (raw == 53) {
			player.closeInventory();
		} else if (raw < 53 && raw >= 0) {
			if ((raw+1)%9 != 0) {
				int index = raw-(raw/9);
				if (index < listFight.size()) {
					FightData fight = listFight.get(index);
					if (fight.getStat() == FightStat.START) {
						manager.getSorci().getManagerFightData().whichSpectate(player.getUniqueId());
						manager.getSorci().getManagerFightData().addFightSpectate(player.getUniqueId(), fight.getFightUUID());
						manager.getSorci().sendPlayerToServer(player, fight.getServer());
					}
				}
			}
		}
	}

}
