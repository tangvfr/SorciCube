package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import fr.tangv.sorcicubespell.util.BookUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;

public abstract class BookGui {

	protected EditCartsGui ec;
	protected ConfigurationSection config;
	protected String name;

	public BookGui(EditCartsGui ec, String name) {
		this.ec = ec;
		this.config = ec.sorci.getGui().getConfigurationSection("book_gui."+name);
		this.name = name;
		this.ec.guiBooks.put(name, this);
	}
	
	public void open(PlayerEditCart player) {
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
		book.setItemMeta(this.getBook(player, (BookMeta) book.getItemMeta()));
		BookUtil.openBook(book, player.getPlayer());
	}
	
	protected IChatBaseComponent toIChatBaseComposent(BaseComponent[] baseComponents) {
		return IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(baseComponents));
	}
	
	protected abstract BookMeta getBook(PlayerEditCart player, BookMeta meta);
	
	protected abstract boolean onCommand(PlayerEditCart player, String[] args);
	
}
