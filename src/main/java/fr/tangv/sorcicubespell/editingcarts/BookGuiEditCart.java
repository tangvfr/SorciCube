package fr.tangv.sorcicubespell.editingcarts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftMetaBook;
import org.bukkit.inventory.meta.BookMeta;

import fr.tangv.sorcicubespell.carts.Cart;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;

public class BookGuiEditCart extends BookGui {

	public BookGuiEditCart(EditCartsGui ec) {
		super(ec, "editcart");
	}

	@SuppressWarnings("deprecation")
	@Override
	protected BookMeta getBook(PlayerEditCart player, Cart cart, BookMeta meta) {
		List<IChatBaseComponent> book = new ArrayList<IChatBaseComponent>();
		TextComponent comp;
		//page1
		TextComponent page = new TextComponent(this.config.getString("title"));
		//name
		comp = new TextComponent(this.config.getString("name").replace("{value}", cart.getName()));
		page.addExtra(comp);
		//material
		comp = new TextComponent(this.config.getString("material").replace("{value}", cart.getMaterial().getItemTypeId()+":"+cart.getMaterial().getData()));
		page.addExtra(comp);
		//count mana
		comp = new TextComponent(this.config.getString("count_mana").replace("{value}", ""+cart.getCountMana()));
		page.addExtra(comp);
		//damage
		comp = new TextComponent(this.config.getString("damage").replace("{value}", ""+cart.getDamage()));
		page.addExtra(comp);
		//rarity
		comp = new TextComponent(this.config.getString("rarity").replace("{value}", cart.getRarity().name()));
		page.addExtra(comp);
		//faction
		comp = new TextComponent(this.config.getString("faction").replace("{value}", cart.getFaction().name()));
		page.addExtra(comp);
		//others spec
		/*comp = new TextComponent(this.config.getString("").replace("{value}", ""));
		page.addExtra(comp);*/
		book.add(this.toIChatBaseComposent(page));
		//page2
		String description = "";
		for (String string : cart.getDescription())
			description += string+"\n";
		TextComponent page2 = new TextComponent(this.config.getString("title"));
		comp = new TextComponent(this.config.getString("description").replace("{value}", description));
		page2.addExtra(comp);
		book.add(this.toIChatBaseComposent(page2));
		//page3
		TextComponent page3 = new TextComponent(this.config.getString("title"));
		comp = new TextComponent(this.config.getString("id").replace("{value}", cart.getId()));
		page3.addExtra(comp);
		book.add(this.toIChatBaseComposent(page3));
		//set book
		((CraftMetaBook) meta).pages = book;
		return meta;
	}

	@Override
	protected boolean onCommand(PlayerEditCart player, Cart cart, String[] args) {
		this.open(player, cart);
		return true;
	}
	
}
