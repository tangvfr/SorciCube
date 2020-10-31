package fr.tangv.sorcicubespell.npc;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;
import fr.tangv.sorcicubespell.player.PlayerFeature;

public class RewardNPC implements ClickNPC {

	private SorciCubeSpell sorci;
	private String key;
	private String nameNPC;
    private int reward;
	private String message;
	
	public RewardNPC(SorciCubeSpell sorci, String key) {
		this.sorci = sorci;
		ConfigurationSection config = sorci.getConfigNPC().getConfigurationSection("");
		this.key = key;
		this.nameNPC = config.getString("name_npc");
		this.reward = config.getInt("reward");
		this.message = config.getString("message").replace("{reward}", Integer.toString(this.reward));
	}
	
	@Override
	public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
		PlayerGui playerG = sorci.getManagerGui().getPlayerGui(player);
		if (playerG.getPlayerFeature() != null) {
			PlayerFeature feature = playerG.getPlayerFeature();
			
			
			this.message.replace("{player}", player.getName());
			
			
		}
	}
	
}
