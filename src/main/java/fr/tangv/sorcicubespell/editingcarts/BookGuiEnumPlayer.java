package fr.tangv.sorcicubespell.editingcarts;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftMetaBook;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import fr.tangv.sorcicubespell.util.BookUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer;

public abstract class BookGuiEnumPlayer<T> {
	
	protected EditCartsGui ec;
	protected ConfigurationSection config;
	protected String name;
	private T typeEnum;
	private int numberByPage;
	
	public BookGuiEnumPlayer(EditCartsGui ec, T typeEnum, String name) {
		this.ec = ec;
		this.name = name;
		this.config = ec.sorci.getGui().getConfigurationSection("book_gui."+this.name);
		this.typeEnum = typeEnum;
		this.numberByPage = this.config.getInt("number_by_page");
		this.ec.guiBooksEnum.put(this.name, this);
	}

	protected abstract String valueEnum(PlayerEditCart player);
	protected abstract void setEnum(PlayerEditCart player, T enum1);
	protected abstract void back(PlayerEditCart player);
	
	protected void endPage (TextComponent page) {
		TextComponent comp = new TextComponent(this.config.getString("back"));
		comp.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/editcarts "+this.name+" back"));
		comp.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
				new TextComponent[] {new TextComponent(this.config.getString("hover_back"))}));
		page.addExtra(comp);
	}
	
	public void open(PlayerEditCart player) {
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
		book.setItemMeta(this.getBook(player, (BookMeta) book.getItemMeta()));
		BookUtil.openBook(book, player.getPlayer());
	}
	
	protected IChatBaseComponent toIChatBaseComposent(BaseComponent baseComponent) {
		return ChatSerializer.a(ComponentSerializer.toString(baseComponent));
	}
	
	protected BookMeta getBook(PlayerEditCart player, BookMeta meta) {
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
			this.addTextConfig(page, valueEnum(player).equals(value), value);
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
	
	protected boolean onCommand(PlayerEditCart player, String[] args) {
		if (args.length == 1) {
			this.open(player);
		} else if (args.length == 2) {
			String action = args[1];
			if (action.equals("back")) {
				this.back(player);
			} else {
				try {
					Field fe = typeEnum.getClass().getField(action);
					@SuppressWarnings("unchecked")
					T enum1 = (T) fe.get(typeEnum);
					this.setEnum(player, enum1);
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
