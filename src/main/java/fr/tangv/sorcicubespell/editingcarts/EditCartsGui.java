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
import fr.tangv.sorcicubespell.carts.CartCible;
import fr.tangv.sorcicubespell.carts.CartEntity;
import fr.tangv.sorcicubespell.carts.CartFaction;
import fr.tangv.sorcicubespell.carts.CartRarity;
import fr.tangv.sorcicubespell.carts.CartSort;
import fr.tangv.sorcicubespell.carts.CartType;

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
		new BookGuiEditEnum<CartCible>(this, CartCible.ALL, BookGuis.CIBLE) {
			@Override
			protected String valueEnum(Cart cart) {
				if (cart instanceof CartSort)
					return ((CartSort) cart).getCible().name();
				else
					return "";
			}
			@Override
			protected void setEnum(Cart cart, CartCible enum1, PlayerEditCart player) {
				if (cart instanceof CartSort) {
					((CartSort) cart).setCible(enum1);
					this.ec.sorci.getCarts().update(cart);
					this.ec.guiBooks.get(BookGuis.MAIN).open(player, cart);
				}
			}
		};
		new BookGuiEditEnum<CartFaction>(this, CartFaction.DARK, BookGuis.FACTION) {
			@Override
			protected String valueEnum(Cart cart) {
				return cart.getFaction().name();
			}
			@Override
			protected void setEnum(Cart cart, CartFaction enum1, PlayerEditCart player) {
				cart.setFaction(enum1);
				this.ec.sorci.getCarts().update(cart);
				this.ec.guiBooks.get(BookGuis.MAIN).open(player, cart);
			}
		};
		new BookGuiEditEnum<CartRarity>(this, CartRarity.COMMUN, BookGuis.RARITY) {
			@Override
			protected String valueEnum(Cart cart) {
				return cart.getRarity().name();
			}
			@Override
			protected void setEnum(Cart cart, CartRarity enum1, PlayerEditCart player) {
				cart.setRarity(enum1);
				this.ec.sorci.getCarts().update(cart);
				this.ec.guiBooks.get(BookGuis.MAIN).open(player, cart);
			}
		};
		new BookGuiEditEnum<CartType>(this, CartType.ENTITY, BookGuis.TYPE) {
			@Override
			protected String valueEnum(Cart cart) {
				return cart.getType().name();
			}
			@Override
			protected void setEnum(Cart cart, CartType enum1, PlayerEditCart player) {
				Cart newCart;
				if (enum1 == CartType.ENTITY) {
					newCart = new CartEntity(cart.getId(),
							cart.getMaterial(),
							cart.getName(),
							cart.getDescription(),
							cart.getCountMana(),
							cart.getDamage(),
							cart.getRarity(),
							cart.getFaction(),
							1);
				} else {
					newCart = new CartSort(cart.getId(),
							cart.getMaterial(),
							cart.getName(),
							cart.getDescription(),
							cart.getCountMana(),
							cart.getDamage(),
							cart.getRarity(),
							cart.getFaction(),
							-1,
							-1,
							CartCible.ONE_ENEMIE);
				}
				this.ec.sorci.getCarts().update(newCart);
				this.ec.guiBooks.get(BookGuis.MAIN).open(player, newCart);
			}
		};
		//spigot init
		sorci.getCommand("editcarts").setExecutor(new CommandEditCartsGui(this));
		Bukkit.getPluginManager().registerEvents(new EventEditCartsGui(this), sorci);
	}
	
	protected String[] getEditBook(ItemStack item) {
		if ((item.getType() == Material.BOOK_AND_QUILL || item.getType() == Material.WRITTEN_BOOK) && item.hasItemMeta()) {
			BookMeta meta = (BookMeta) item.getItemMeta();
			if (meta.hasDisplayName() && meta.hasLore() 
					&& meta.getDisplayName().equals(this.sorci.getGui().getString("name_book_desc"))) {
				String id = meta.getLore().get(meta.getLore().size()-1).replaceFirst("§8Id: ", "");
				String lore = "";
				if (meta.getPageCount() > 0)
					lore = meta.getPages().get(0);
				lore = lore.replace("&", "§");
				return new String[] {id, lore};
			}
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
		meta.setLore(Arrays.asList(("§7Name: "+cart.getName()), 
				this.sorci.getGui().getString("edit_book_desc"),
				this.sorci.getGui().getString("valid_book_desc"),
				this.sorci.getGui().getString("cancel_book_desc"),
				("§8Id: "+cart.getId())));
		book.setItemMeta(meta);
		player.getInventory().setItemInMainHand(book);
	}
	
	protected Cart setDescCartByBook(String id, String text) {
		Cart cart = this.sorci.getCarts().getCart(id);
		if (cart == null) return null;
		cart.setDescription(text.split("\n"));
		this.sorci.getCarts().update(cart);
		return cart;
	}
	
}
