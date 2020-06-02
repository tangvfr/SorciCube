package fr.tangv.sorcicubespell.editingcarts;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftMetaBook;
import org.bukkit.inventory.meta.BookMeta;

import fr.tangv.sorcicubespell.carts.Cart;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;

public abstract class BookGuiEditEnum<T> extends BookGui {

	private T typeEnum;
	private int numberByPage;
	
	public BookGuiEditEnum(EditCartsGui ec, T typeEnum, String name) {
		super(ec, name);
		this.typeEnum = typeEnum;
		this.numberByPage = this.config.getInt("number_by_page");
	}

	protected abstract String valueEnum(Cart cart);
	protected abstract void setEnum(Cart cart, T enum1, PlayerEditCart player);
	
	protected void endPage (TextComponent page) {
		TextComponent comp = new TextComponent(this.config.getString("back"));
		comp.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/editcarts "+this.name+" back"));
		comp.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
				new TextComponent[] {new TextComponent(this.config.getString("hover_back"))}));
		page.addExtra(comp);
	}
	
	@Override
	protected BookMeta getBook(PlayerEditCart player, Cart cart, BookMeta meta) {
		List<IChatBaseComponent> book = new ArrayList<IChatBaseComponent>();
		Field[] enums = this.typeEnum.getClass().getFields();
		//page
		TextComponent page = null;
		for (int i = 0; i < enums.length; i++) {
			if ((i % this.numberByPage) == 0) {
				if (page != null) {
					this.endPage(page);
					book.add(this.toIChatBaseComposent(page));
				}
				page = new TextComponent(this.config.getString("title"));
			}
			String value = enums[i].getName();
			this.addTextConfig(page, valueEnum(cart).equals(value), value);
		}
		this.endPage(page);
		book.add(this.toIChatBaseComposent(page));
		//set book
		((CraftMetaBook) meta).pages = book;
		return meta;
	}

	protected void addTextConfig(TextComponent page, boolean selected, String value) {
		String text = this.ec.sorci.getEnumConfig().getString(this.name+'.'+value.toLowerCase());
		TextComponent comp = new TextComponent(this.config.getString(selected ? "select" : "no_select")
				.replace("{value}", text));
		comp.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/editcarts "+this.name+" "+value));
		comp.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
				new TextComponent[] {new TextComponent(this.config.getString("hover").replace("{value}", text))}));
		page.addExtra(comp);
	}
	
	@Override
	protected boolean onCommand(PlayerEditCart player, Cart cart, String[] args) {
		if (args.length == 1) {
			this.open(player, cart);
		} else if (args.length == 2) {
			String action = args[1];
			player.getPlayer().sendMessage("action: "+action);
			if (action.equals("back")) {
				this.ec.guiBooks.get(BookGuis.MAIN).open(player, cart);
			} else {
				try {
					Field fe = typeEnum.getClass().getField(action);
					@SuppressWarnings("unchecked")
					T enum1 = (T) fe.get(typeEnum);
					this.setEnum(cart, enum1, player);
				} catch (Exception e) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
	
}