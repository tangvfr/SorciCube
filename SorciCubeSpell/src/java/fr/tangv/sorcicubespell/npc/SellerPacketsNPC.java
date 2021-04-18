package fr.tangv.sorcicubespell.npc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubecore.configs.GuiSellerPacketsGuiConfig;
import fr.tangv.sorcicubecore.configs.npc.ItemSellerCardsNPCConfig;
import fr.tangv.sorcicubecore.configs.npc.SellerCardsNPCConfig;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.AbstractGui;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class SellerPacketsNPC extends AbstractGui<GuiSellerPacketsGuiConfig> implements ClickNPC {

	private final String nameNPC;
	private final ItemStack itemDeco;
	private final ItemStack itemClose;
	private final ItemStack itemError;
	private final PCSell[] pcSells;
	
	public SellerPacketsNPC(SorciCubeSpell sorci, SellerCardsNPCConfig conf) {
		super(sorci.getManagerGui(), sorci.config().gui.guiSellerPackets);
		this.nameNPC = conf.nameNPC.value;
		this.itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, " ", null, false);
		this.itemClose = ItemBuild.buildSkull(SkullUrl.X_RED, 1, config.close.value, null, false);
		this.itemError = ItemBuild.buildItem(Material.BARRIER, 1, (short) 0, (byte) 15, config.error.value, null, false);
		this.pcSells = new PCSell[7];
		this.pcSells[0] = createPCSell(sorci, conf.item1);
		this.pcSells[1] = createPCSell(sorci, conf.item2);
		this.pcSells[2] = createPCSell(sorci, conf.item3);
		this.pcSells[3] = createPCSell(sorci, conf.item4);
		this.pcSells[4] = createPCSell(sorci, conf.item5);
		this.pcSells[5] = createPCSell(sorci, conf.item6);
		this.pcSells[6] = createPCSell(sorci, conf.item7);
	}
	
	private PCSell createPCSell(SorciCubeSpell sorci, ItemSellerCardsNPCConfig c) {
		if (c.isEnable.value)
			if (c.isCard.value)
				return new CardSell(sorci, c.price.value, c.idItem.value);
			else
				return new PacketCardsSell(sorci, c.price.value, c.idItem.value);
		else
			return null;
	}

	@Override
	public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
		if (getPlayerGui(player).getPlayerFeatures() != null)
			this.open(player);
	}
	
	@Override
	public Inventory createInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, this.nameNPC);
		inv.setItem(0, itemDeco);
		inv.setItem(8, itemDeco);
		inv.setItem(9, itemDeco);
		inv.setItem(17, itemDeco);
		for (int i = 18; i < 27; i++)
			inv.setItem(i, itemDeco);
		inv.setItem(22, itemClose);
		updateInv(inv, player);
		return inv;
	}
	
	private void updateInv(Inventory inv, Player player) {
		for (int i = 0; i < 7; i ++) {
			PCSell pcSell = pcSells[i];
			if (pcSell != null && pcSell.isValid()) {
				inv.setItem(i+1, pcSell.getItemSell(this.getPlayerGui(player)));
				inv.setItem(i+10, pcSell.getItemView());
			} else {
				inv.setItem(i+1, this.itemError);
				inv.setItem(i+10, pcSell.getItemView());
			}
		}
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		e.setCancelled(true);
		int raw = e.getRawSlot();
		if (raw == 22) {
			player.closeInventory();
		} else if (raw > 0 && raw < 9) {
			int number = raw-1;
			PCSell pcSell =  pcSells[number];
			if (pcSell != null && pcSell.isValid()) {
				PlayerGui playerG = getPlayerGui(player);
				if (pcSell.hasMoney(playerG)) {
					if(pcSell.buy(playerG))
						updateInv(e.getInventory(), player);
				} else { 
					player.sendMessage(sorci.config().messages.packetNoMoney.value);
					player.closeInventory();
				}
			}
		}
	}

}
