package fr.tangv.sorcicubespell.editingcarts;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.tangv.sorcicubespell.carts.Cart;
import fr.tangv.sorcicubespell.carts.CartRender;
import fr.tangv.sorcicubespell.util.AnvilGUI;
import fr.tangv.sorcicubespell.util.AnvilGUI.AnvilClickEvent;
import fr.tangv.sorcicubespell.util.AnvilGUI.AnvilSlot;

public abstract class AnvilEdit {

	protected BookGuiEditCart bgec;
	protected PlayerEditCart player;
	protected Cart cart;
	private String name;
	private String value;
	
	public AnvilEdit(PlayerEditCart player, String name, String value, Cart cart, BookGuiEditCart bgec) {
		this.player = player;
		this.name = name;
		this.value = value;
		this.cart = cart;
		this.bgec = bgec;
	}
	
	protected void open() {
		ConfigurationSection config = this.bgec.ec.sorci.getGui().getConfigurationSection("gui_edit_anvil");
		final AnvilGUI gui = new AnvilGUI(this.player.getPlayer(), new AnvilGUI.AnvilClickEventHandler() {
            @Override
            public void onAnvilClick(AnvilClickEvent e) {
                if(e.getSlot() == AnvilSlot.OUTPUT && e.hasText()) {
                	AnvilEdit.this.valid(e.getText());
                } else if(e.getSlot() == AnvilSlot.INPUT_RIGHT) {
                    AnvilEdit.this.open();
                } else if(e.getSlot() == AnvilSlot.INPUT_LEFT) {
                	AnvilEdit.this.back();
                }
            }
        }, this.bgec.ec.sorci);
		ItemStack item = CartRender.cartToItem(cart, this.bgec.ec.sorci);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = config.getStringList("lore_edit");
		for (int i = 0; i < lore.size(); i++)
			lore.set(i, 
					lore.get(i)
					.replace("{name}", name)
					.replace("{value}", value)
				);
		meta.setDisplayName(value.replace("§", "&"));
		meta.setLore(lore);
		item.setItemMeta(meta);
        gui.setSlot(AnvilSlot.INPUT_LEFT, item);
        gui.setTitle(config.getString("name"));
        gui.open();
	}
	
	protected abstract void valid(String text);
	
	protected void back() {
		this.bgec.open(this.player, this.cart);
	}
	
}
