package fr.tangv.sorcicubespell.npc;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;

public class RewardNPC implements ClickNPC {

	private final String key;
    private final int reward;
	private final String message;
	
	public RewardNPC(SorciCubeSpell sorci, String key) {
		ConfigurationSection config = sorci.getConfigNPC().getConfigurationSection("npc_rewards."+key);
		this.key = key;
		this.reward = config.getInt("reward");
		this.message = config.getString("message").replace("{reward}", Integer.toString(this.reward));
	}
	
	@Override
	public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
		PlayerGui playerG = sorci.getManagerGui().getPlayerGui(player);
		if (playerG.getPlayerFeatures() != null) {
			PlayerFeatures feature = playerG.getPlayerFeatures();
			if (!feature.getRewardNPC().contains(key)) {
				feature.getRewardNPC().add(key);
				feature.addMoney(reward);
				playerG.uploadPlayerFeatures(sorci.getHandlerPlayers());
				player.sendMessage(this.message.replace("{player}", player.getName()));
			}
		}
	}
	
}
