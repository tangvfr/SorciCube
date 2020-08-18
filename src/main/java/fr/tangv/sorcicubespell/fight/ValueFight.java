package fr.tangv.sorcicubespell.fight;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class ValueFight {

	public static ValueFight V = null;
	
	protected final int priceCard;
	protected final ItemStack itemNextRound;
	protected final ItemStack itemNone;
	protected final ItemStack itemNull;
	protected final ItemStack itemStickView;
	protected final ItemStack itemSwap;
	protected final ItemStack itemBuy;
	protected final int maxMana;
	protected final int startMana;
	protected final String titleBossBar;
	protected final BarColor titleBossBarColor;
	protected final String titleEnd; 
	protected final BarColor titleEndColor;
	protected final byte roundMaxAFK;
	
	public ValueFight(SorciCubeSpell sorci) {
		this.priceCard = sorci.getParameter().getInt("price_card");
		this.roundMaxAFK = (byte) sorci.getParameter().getInt("round_max_afk");
		//item
		this.itemNone = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, sorci.gertGuiConfig().getString("gui_player.none"), null, false);
		this.itemNull = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 8, sorci.gertGuiConfig().getString("gui_player.null"), null, false);
		this.itemNextRound = ItemBuild.buildItem(Material.PAPER, 1, (short) 0, (byte) 0, sorci.gertGuiConfig().getString("gui_player.next"), Arrays.asList(sorci.gertGuiConfig().getString("gui_player.next_desc")), false);
		this.itemStickView = ItemBuild.buildItem(Material.BLAZE_ROD, 1, (short) 0, (byte) 0, sorci.gertGuiConfig().getString("gui_player.stick_view"), Arrays.asList(sorci.gertGuiConfig().getString("gui_player.stick_view_desc")), false);
		this.itemBuy = ItemBuild.buildSkull(SkullUrl.QUESTION, 1,
				sorci.gertGuiConfig().getString("gui_player.buy").replace("{price}", Integer.toString(priceCard))
				, Arrays.asList(sorci.gertGuiConfig().getString("gui_player.buy_desc").replace("{price}", Integer.toString(priceCard))), false);
		this.itemSwap = ItemBuild.buildItem(Material.SHEARS, 1, (short) 0, (byte) 0, sorci.gertGuiConfig().getString("gui_player.swap"), Arrays.asList(sorci.gertGuiConfig().getString("gui_player.swap_desc")), false);
		//mana
		this.maxMana = sorci.getParameter().getInt("max_mana");
		this.startMana = sorci.getParameter().getInt("start_mana");
		//boss bar
		this.titleBossBar = sorci.gertGuiConfig().getString("boss_bar.name");
		this.titleBossBarColor = BarColor.valueOf(sorci.gertGuiConfig().getString("boss_bar.color"));
		this.titleEnd = sorci.gertGuiConfig().getString("boss_bar.name_end");
		this.titleEndColor = BarColor.valueOf(sorci.gertGuiConfig().getString("boss_bar.color_end"));
	}
	
}
