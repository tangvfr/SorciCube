package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.Bukkit;
import org.bukkit.material.MaterialData;

import fr.tangv.sorcicubespell.carts.Cart;

public class AnvilEditMaterial extends AnvilEdit {

	private BookGuiEditCart bgec;
	private Cart cart;
	
	@SuppressWarnings("deprecation")
	public AnvilEditMaterial(PlayerEditCart player, Cart cart, BookGuiEditCart bgec) {
		super(player, "material", cart.getMaterial().getItemTypeId()+":"+cart.getMaterial().getData(), bgec.ec.sorci);
		this.bgec = bgec;
		this.cart = cart;
	}

	@Override
	public void valid(String string) {
		String[] mat = string.split(":");
		if (mat.length == 1 || mat.length == 2) {
			try {
				int id = Integer.parseInt(mat[0]);
				byte data = (mat.length) == 1 ? 0 : Byte.parseByte(mat[1]);
				@SuppressWarnings("deprecation")
				MaterialData material = new MaterialData(id, data);
				Bukkit.broadcastMessage(material.toItemStack().toString());
				Bukkit.broadcastMessage(material.toItemStack().getType().name());
				this.cart.setMaterial(material);
				this.sorci.getCarts().update(this.cart);
				this.back();
				return;
			} catch (Exception e) {}
		}
		this.open();
	}
	
	@Override
	public void back() {
		this.bgec.open(this.player, this.cart);
	}
	
}
