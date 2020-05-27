package fr.tangv.sorcicubespell.editingcarts;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.carts.CartEntity;
import fr.tangv.sorcicubespell.carts.CartSort;
import fr.tangv.sorcicubespell.carts.CartType;
import fr.tangv.sorcicubespell.carts.EditCartEntity;
import fr.tangv.sorcicubespell.carts.EditCartSort;

public class EditCartsGui {
	
	protected SorciCubeSpell sorci;
	protected Map<Player, PlayerEditCart> editingCarts;
	protected Map<String, BookGui> guiBooks;
	protected GuiEditList guiEditList;
	
	public EditCartsGui(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.editingCarts = new ConcurrentHashMap<Player, PlayerEditCart>();
		this.guiBooks = new ConcurrentHashMap<String, BookGui>();
		//init gui
		this.guiEditList = new GuiEditList(this, sorci.getGui().getConfigurationSection("gui_edit_list"));
		//init book gui
		new BookGuiEditCart(this);
		//spigot init
		sorci.getCommand("editcarts").setExecutor(new CommandEditCartsGui(this));
		Bukkit.getPluginManager().registerEvents(new EventEditCartsGui(this), sorci);
	}
	
	protected String[] getEditBook(ItemStack item) {
		if (item.getType() == Material.BOOK_AND_QUILL && item.hasItemMeta()) {
			BookMeta meta = (BookMeta) item.getItemMeta();
			if (meta.hasDisplayName() && meta.hasLore() 
					&& meta.getDisplayName().equals(this.sorci.getGui().getString("name_book_desc")))
				return new String[] {meta.getLore().get(1).replaceFirst("§8Id: ", ""), meta.getPage(0).replace("&", "§")};
		}
		return null;
	}
	
	protected void setBookDesc(Player player, Cart cart) {
		ItemStack book = new ItemStack(Material.BOOK_AND_QUILL, 1);
		BookMeta meta = (BookMeta) book.getItemMeta();
		String[] desc = cart.getDescription();
		String text = desc.length > 0 ? desc[0].replace("§", "&") : "";
		for (int i = 1; i < desc.length; i++)
			text += "\n"+desc[i].replace("§", "&");
		meta.addPage(text);
		meta.setDisplayName(this.sorci.getGui().getString("name_book_desc"));
		meta.setLore(Arrays.asList(("§7Name: "+cart.getName()), ("§8Id: "+cart.getId())));
		book.setItemMeta(meta);
		player.getInventory().setItemInMainHand(book);
	}
	
	protected Cart setDescCartByBook(String id, String text) {
		Cart cart = this.sorci.getCarts().getCart(id);
		if (cart.getType() == CartType.ENTITY) {
			EditCartEntity edit = new EditCartEntity((CartEntity) cart);
			edit.setDescription(text.split("\n"));
		} else {
			EditCartSort edit = new EditCartSort((CartSort) cart);
			edit.setDescription(text.split("\n"));
		}
		this.sorci.getCarts().update(cart);
		return cart;
	}
	
}
