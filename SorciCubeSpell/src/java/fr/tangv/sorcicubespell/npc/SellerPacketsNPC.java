package fr.tangv.sorcicubespell.npc;

import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.AbstractGui;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class SellerPacketsNPC extends AbstractGui implements ClickNPC {

	private String nameNPC;
	private Vector<PCSell> pcSells;
	private ItemStack itemDeco;
	private ItemStack itemClose;
	private ItemStack itemError;
	
	public SellerPacketsNPC(SorciCubeSpell sorci, String keyNPC) {
		super(sorci.getManagerGui(), sorci.getGuiConfig().getConfigurationSection("gui_seller_packets"));
		this.pcSells = new Vector<PCSell>();
		this.itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, " ", null, false);
		this.itemClose = ItemBuild.buildSkull(SkullUrl.X_RED, 1, config.getString("close"), null, false);
		this.itemError = ItemBuild.buildItem(Material.BARRIER, 1, (short) 0, (byte) 15, config.getString("error"), null, false);
		this.nameNPC = sorci.getConfigNPC().getString("list_seller_packet_cards."+keyNPC+".name_npc");
		ConfigurationSection list = sorci.getConfigNPC().getConfigurationSection("list_seller_packet_cards."+keyNPC+".list");
		for (String key : list.getKeys(false)) {
			boolean card = list.getBoolean(key+".card");
			int price = list.getInt(key+".price");
			String id = list.getString(key+".id");
			pcSells.add(card ? new CardSell(sorci, this.config, price, id) : new PacketCardsSell(sorci, this.config, price, id));
		}
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
		for (int i = 0; i < 7 && i < pcSells.size(); i ++) {
			PCSell pcSell =  pcSells.get(i);
			if (pcSell.isValid()) {
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
			if (number < pcSells.size()) {
				PCSell pcSell =  pcSells.get(number);
				if (pcSell.isValid()) {
					PlayerGui playerG = getPlayerGui(player);
					if (pcSell.hasMoney(playerG)) {
						if(pcSell.buy(playerG))
							updateInv(e.getInventory(), player);
					} else { 
						player.sendMessage(getMessage("message_packet_no_money"));
						player.closeInventory();
					}
				}
			}
		}
	}

}
