package fr.tangv.sorcicubespell.fight;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.configs.Config;
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
	protected final ItemStack itemExit;
	protected final int maxMana;
	protected final int startMana;
	protected final String titleBossBar;
	protected final BarColor titleBossBarColor;
	protected final String titleEnd; 
	protected final BarColor titleEndColor;
	protected final byte roundMaxAFK;
	
	public ValueFight(Config config) {
		this.priceCard = config.parameter.priceCard.value;
		this.roundMaxAFK = (byte) config.parameter.roundMaxAfk.value;
		//item
		this.itemNone = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, config.gui.guiPlayer.none.value, null, false);
		this.itemNull = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 8, config.gui.guiPlayer.nul.value, null, false);
		this.itemNextRound = ItemBuild.buildItem(Material.PAPER, 1, (short) 0, (byte) 0, config.gui.guiPlayer.next.value, Arrays.asList(config.gui.guiPlayer.nextDesc.value), false);
		this.itemStickView = ItemBuild.buildItem(Material.BLAZE_ROD, 1, (short) 0, (byte) 0, config.gui.guiPlayer.stickView.value, Arrays.asList(config.gui.guiPlayer.stickViewDesc.value), false);
		this.itemBuy = ItemBuild.buildSkull(SkullUrl.QUESTION, 1,
				config.gui.guiPlayer.buy.value.replace("{price}", Integer.toString(priceCard))
				, Arrays.asList(config.gui.guiPlayer.buyDesc.value.replace("{price}", Integer.toString(priceCard))), false);
		this.itemSwap = ItemBuild.buildItem(Material.SHEARS, 1, (short) 0, (byte) 0, config.gui.guiPlayer.swap.value, Arrays.asList(config.gui.guiPlayer.swapDesc.value), false);
		this.itemExit = ItemBuild.buildItem(Material.DARK_OAK_DOOR_ITEM, 1,  (short) 0, (byte) 0, config.gui.guiPlayer.backLobby.value, Arrays.asList(config.gui.guiPlayer.backLobbyDesc.value), false);
		//mana
		this.maxMana = config.parameter.maxMana.value;
		this.startMana = config.parameter.startMana.value;
		//boss bar
		this.titleBossBar = config.gui.bossBar.name.value;
		this.titleBossBarColor = BarColor.valueOf(config.gui.bossBar.color.value);
		this.titleEnd = config.gui.bossBar.nameEnd.value;
		this.titleEndColor = BarColor.valueOf(config.gui.bossBar.colorEnd.value);
	}
	
}
