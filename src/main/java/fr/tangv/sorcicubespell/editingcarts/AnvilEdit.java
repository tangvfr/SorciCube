package fr.tangv.sorcicubespell.editingcarts;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.util.AnvilGUI;
import fr.tangv.sorcicubespell.util.AnvilGUI.AnvilClickEvent;
import fr.tangv.sorcicubespell.util.AnvilGUI.AnvilSlot;
import fr.tangv.sorcicubespell.util.ItemBuild;

public abstract class AnvilEdit {

	protected PlayerEditCart player;
	private String name;
	private String value;
	protected SorciCubeSpell sorci;
	
	public AnvilEdit(PlayerEditCart player, String name, String value, SorciCubeSpell sorci) {
		this.player = player;
		this.name = name;
		this.value = value;
		this.sorci = sorci;
	}
	
	public void open() {
		this.open(this.name, this.value, this.sorci);
	}
	
	protected void open(String name, String value, SorciCubeSpell sorci) {
		ConfigurationSection config = sorci.getGui().getConfigurationSection("gui_edit_anvil");
		final AnvilGUI gui = new AnvilGUI(this.player.getPlayer(), new AnvilGUI.AnvilClickEventHandler() {
            @Override
            public void onAnvilClick(AnvilClickEvent e) {
                if(e.getSlot() == AnvilSlot.OUTPUT && e.hasText()) {
                    valid(e.getText());
                } else if(e.getSlot() == AnvilSlot.INPUT_RIGHT) {
                    open(name, value, sorci);
                } else if(e.getSlot() == AnvilSlot.INPUT_LEFT) {
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
	
	protected abstract void valid(String text);
	protected abstract void back();
	
}
