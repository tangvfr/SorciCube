package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class GuiEditConfirm extends GuiEdit {

	public GuiEditConfirm(EditCartsGui ec) {
		super(ec, ec.sorci.getGui().getConfigurationSection("gui_edit_confirm"));
		/*this.deco =  ItemBuild.buildItem(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 0, config.getString("item_name.deco"), null, false);
		this.createSort = ItemBuild.buildItem(Material.BLAZE_POWDER, 1, (short) 0, (byte) 0, config.getString("item_name.create_sort"), null, false);
	*/}

	@Override
	public Inventory getInventory(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(Player player, InventoryClickEvent e) {
		int raw = e.getRawSlot();
		switch (raw) {
			case 2:
				
				break;
	
			default:
				break;
		}
	}
	

	@Override
	public void onDrag(Player player, InventoryDragEvent e) {}

}
