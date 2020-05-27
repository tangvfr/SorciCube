package fr.tangv.sorcicubespell.editingcarts;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import fr.tangv.sorcicubespell.util.BookUtil;

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
	
	protected abstract BookMeta getBook(PlayerEditCart player, BookMeta meta);
	
	protected abstract boolean onCommand(PlayerEditCart player, String[] args);
	
}
