package fr.tangv.sorcicubespell.editingcarts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftMetaBook;
import org.bukkit.inventory.meta.BookMeta;

import fr.tangv.sorcicubespell.carts.Cart;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;

public class BookGuiEditCart extends BookGui {

	public BookGuiEditCart(EditCartsGui ec) {
		super(ec, "editcart");
	}

	@SuppressWarnings("deprecation")
	@Override
	protected BookMeta getBook(PlayerEditCart player, BookMeta meta) {
		Cart cart = player.getCart();
		List<IChatBaseComponent> book = new ArrayList<IChatBaseComponent>();
		//page1
		TextComponent page = new TextComponent();
		page.addExtra(new TextComponent(this.config.getString("id").replace("{value}", cart.getId())));
		page.addExtra(new TextComponent(this.config.getString("material").replace("{value}", cart.getMaterial().getItemTypeId()+":"+cart.getMaterial().getData())));
		page.addExtra(new TextComponent(this.config.getString("name").replace("{value}", cart.getName())));
		page.addExtra(new TextComponent(this.config.getString("count_mana").replace("{value}", ""+cart.getCountMana())));
		page.addExtra(new TextComponent(this.config.getString("damage").replace("{value}", ""+cart.getDamage())));
		page.addExtra(new TextComponent(this.config.getString("rarity").replace("{value}", cart.getRarity().name())));
		page.addExtra(new TextComponent(this.config.getString("faction").replace("{value}", cart.getFaction().name())));
		//page.addExtra(new TextComponent(this.config.getString("").replace("{value}", "")));
		book.add(this.toIChatBaseComposent(new BaseComponent[] {page}));
		//page2
		String description = "";
		for (String string : cart.getDescription())
			description += string+"\n";
		TextComponent page2 = new TextComponent(this.config.getString("description").replace("{value}", description));
		book.add(this.toIChatBaseComposent(new BaseComponent[] {page2}));
		//set book
		((CraftMetaBook) meta).pages = book;
		return meta;
	}

	@Override
	protected boolean onCommand(PlayerEditCart player, String[] args) {
		this.open(player);
		return true;
	}

}
