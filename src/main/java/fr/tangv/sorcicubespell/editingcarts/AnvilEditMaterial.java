package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import fr.tangv.sorcicubespell.carts.Cart;

public class AnvilEditMaterial extends AnvilEdit {
	
	@SuppressWarnings("deprecation")
	public AnvilEditMaterial(PlayerEditCart player, Cart cart, BookGuiEditCart bgec) {
		super(player, "material", cart.getMaterial().getItemTypeId()+":"+cart.getMaterial().getData(), cart, bgec);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void valid(String string) {
		String[] mat = string.split(":");
		if (mat.length == 1 || mat.length == 2) {
			try {
				int id = Integer.parseInt(mat[0]);
				byte data = (mat.length) == 1 ? 0 : Byte.parseByte(mat[1]);
				MaterialData material = new MaterialData(id, data);
				if (!this.itemIsValid(material.toItemStack()))
					throw new Exception("Item invalid");
				this.cart.setMaterial(material);
				this.bgec.ec.sorci.getCarts().update(this.cart);
				this.back();
				return;
			} catch (Exception e) {}
		}
		this.open();
	}
	
	private boolean itemIsValid(ItemStack item) {
		Inventory inv = Bukkit.createInventory(null, 9);
		inv.setItem(0, item);
		return inv.getItem(0) != null;
	}
	
}
