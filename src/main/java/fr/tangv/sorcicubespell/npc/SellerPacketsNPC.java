package fr.tangv.sorcicubespell.npc;

import java.util.Vector;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.AbstractGui;
import fr.tangv.sorcicubespell.packet.PacketCards;
import fr.tangv.sorcicubespell.util.ItemBuild;
import fr.tangv.sorcicubespell.util.SkullUrl;

public class SellerPacketsNPC extends AbstractGui implements ClickNPC {

	private String nameNPC;
	private Vector<PacketCardsSell> packetsSell;//remake this
	private ItemStack itemDeco;
	private ItemStack itemClose;
	
	public SellerPacketsNPC(SorciCubeSpell sorci, String nameNPC) {
		super(sorci.getManagerGui(), sorci.getGuiConfig().getConfigurationSection("gui_seller_packets"));
		this.packetsSell = new Vector<PacketCardsSell>();
		this.itemDeco = ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15, " ", null, false);
		this.itemClose = ItemBuild.buildSkull(SkullUrl.X_RED, 1, config.getString("close"), null, false);
		this.nameNPC = nameNPC;
		ConfigurationSection packets = sorci.getConfigNPC().getConfigurationSection("list_seller_packet_cards."+nameNPC);
		for (String packet : packets.getKeys(false)) {
			
			//continue here
			
		}
	}

	@Override
	public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
		if (getPlayerGui(player).getPlayerFeature() != null)
			this.open(player);
	}
	
	@Override
	public Inventory getInventory(Player player) {
		Inventory inv = 
		return null;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		// TODO Auto-generated method stub
		
	}

}
