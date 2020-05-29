package fr.tangv.sorcicubespell.editingcarts;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.util.AnvilGUI;
import fr.tangv.sorcicubespell.util.AnvilGUI.AnvilClickEvent;
import fr.tangv.sorcicubespell.util.AnvilGUI.AnvilSlot;
import fr.tangv.sorcicubespell.util.ItemBuild;

public abstract class AnvilEdit {

	public void open(Player player, String name, String value, SorciCubeSpell sorci) {
		ConfigurationSection config = sorci.getGui().getConfigurationSection("gui_edit_anvil");
		final AnvilGUI gui = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
            @Override
            public void onAnvilClick(AnvilClickEvent e) {
                if(e.getSlot() == AnvilSlot.OUTPUT && e.hasText()) {
                    e.setWillClose(true);
                    player.sendMessage("Your Text-Input: " + e.getText());
                    valid(e.getText());
                } else if(e.getSlot() == AnvilSlot.INPUT_RIGHT) {
                    e.setWillClose(true);
                    open(player, name, value, sorci);
                } else if(e.getSlot() == AnvilSlot.INPUT_LEFT) {
                    e.setWillClose(true);
                    back();
                }
            }
        }, sorci);
		List<String> lore = config.getStringList("lore_edit");
		for (int i = 0; i < lore.size(); i++)
			lore.set(i, 
					lore.get(i)
					.replace("{name}", name)
					.replace("{value}", value)
				);
        gui.setSlot(AnvilSlot.INPUT_LEFT, ItemBuild.buildItem(Material.PAPER, 1, (short) 0, (byte) 0, value.replace("§", "&"), lore, false));
        gui.setTitle(config.getString("name"));
        gui.open();
	}
	
	public abstract void valid(String text);
	public abstract void back();
	
}
