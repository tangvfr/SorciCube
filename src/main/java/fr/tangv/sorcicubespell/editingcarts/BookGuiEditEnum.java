package fr.tangv.sorcicubespell.editingcarts;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftMetaBook;
import org.bukkit.inventory.meta.BookMeta;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.carts.CartCible;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;

public class BookGuiEditEnum extends BookGui {

	private Enum<?> typeEnum;
	private int numberByPage;
	private static int maxLine;
	
	static {
		BookGuiEditEnum.maxLine = 12;
	}
	
	public BookGuiEditEnum(EditCartsGui ec, Enum<?> typeEnum, String name, int numberByPage) {
		super(ec, name);
		this.typeEnum = typeEnum;
		this.numberByPage = numberByPage;
	}

	@Override
	protected BookMeta getBook(PlayerEditCart player, Cart cart, BookMeta meta) {
		List<IChatBaseComponent> book = new ArrayList<IChatBaseComponent>();
		Field[] enums = typeEnum.getClass().getFields();
		//page1
		//for (int i = 0; i < ) {
			TextComponent page = new TextComponent(this.config.getString("title"));
			
			book.add(this.toIChatBaseComposent(page));
		//}
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
			switch (action) {
	
				default:
					player.getPlayer().sendMessage("undefine !");
					break;
			}
		} else {
			return false;
		}
		return true;
	}
	
}
