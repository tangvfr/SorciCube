package fr.tangv.sorcicubespell.editingcarts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class EditCartsGui {
	
	protected SorciCubeSpell sorci;
	protected Map<Player, PlayerEditCart> editingCarts;
	protected GuiEditList guiEditList;
	protected GuiEditCart guiEditCart;
	
	public EditCartsGui(SorciCubeSpell sorci) {
		this.sorci = sorci;
		this.editingCarts = new ConcurrentHashMap<Player, PlayerEditCart>();
		//init gui
		this.guiEditList = new GuiEditList(this, sorci.getGui().getConfigurationSection("gui_edit_list"));
		this.guiEditCart = new GuiEditCart(this, sorci.getGui().getConfigurationSection("gui_edit_cart"));
		//spigot init
		sorci.getCommand("editcarts").setExecutor(new CommandEditCartsGui(this));
		Bukkit.getPluginManager().registerEvents(new EventEditCartsGui(this), sorci);
	}
	
}
