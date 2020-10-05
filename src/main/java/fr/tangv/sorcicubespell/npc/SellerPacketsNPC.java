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
import fr.tangv.sorcicubespell.player.PlayerFeature;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class SellerPacketsNPC extends AbstractGui implements ClickNPC {

	private String nameNPC;
	private Vector<PacketCardsSell> packetsSell;
	private ItemStack itemDeco;
	private ItemStack itemClose;
	private ItemStack itemError;
	
	public SellerPacketsNPC(SorciCubeSpell sorci, String nameNPC) {
		super(sorci.getManagerGui(), sorci.getGuiConfig().getConfigurationSection("gui_seller_packets"));
		this.packetsSell = new Vector<PacketCardsSell>();
		this.itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, " ", null, false);
		this.itemClose = ItemBuild.buildSkull(SkullUrl.X_RED, 1, config.getString("close"), null, false);
		this.itemError = ItemBuild.buildItem(Material.BARRIER, 1, (short) 0, (byte) 15, config.getString("error"), null, false);
		this.nameNPC = nameNPC;
		ConfigurationSection packets = sorci.getConfigNPC().getConfigurationSection("list_seller_packet_cards."+nameNPC);
		for (String packet : packets.getKeys(false))
			packetsSell.add(new PacketCardsSell(sorci, packet, packets.getInt(packet), this.config));
	}

	@Override
	public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
		if (getPlayerGui(player).getPlayerFeature() != null)
			this.open(player);
	}
	
	@Override
	public Inventory getInventory(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, this.nameNPC);
		inv.setItem(0, itemDeco);
		inv.setItem(8, itemDeco);
		inv.setItem(9, itemDeco);
		inv.setItem(17, itemDeco);
		for (int i = 18; i < 27; i++)
			inv.setItem(i, itemDeco);
		for (int i = 0; i < 7 && i < packetsSell.size(); i ++) {
			PacketCardsSell packetSell = packetsSell.get(i);
			if (packetSell.isValid()) {
				inv.setItem(i+1, packetSell.getItemSell());
				inv.setItem(i+10, packetSell.getItemPacket());
			} else {
				inv.setItem(i+1, this.itemError);
				inv.setItem(i+10, packetSell.getItemError());
			}
		}
		inv.setItem(22, itemClose);
		return inv;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		e.setCancelled(true);
		int raw = e.getRawSlot();
		if (raw == 22) {
			player.closeInventory();
		} else if (raw > 0 && raw < 9) {
			int number = raw-1;
			if (number < packetsSell.size()) {
				PacketCardsSell packetSell = packetsSell.get(number);
				if (packetSell.isValid()) {
					int price = packetSell.getPrice();
					PlayerGui playerG = getPlayerGui(player);
					PlayerFeature feature = playerG.getPlayerFeature();
					if (feature.getMoney() >= price) {
						feature.removeMoney(price);
						playerG.uploadPlayerFeature(manager.getSorci().getManagerPlayers());
						player.getInventory().addItem(packetSell.getItemPacket());
						player.sendMessage(getMessage("message_packet_buy").replace("{name}", packetSell.getPacketCards().getName()));
					} else { 
						player.sendMessage(getMessage("message_packet_no_money"));
						player.closeInventory();
					}
				}
			}
		}
	}

}
