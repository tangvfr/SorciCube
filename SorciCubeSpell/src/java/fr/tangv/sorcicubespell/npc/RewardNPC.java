package fr.tangv.sorcicubespell.npc;

import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.configs.npc.RewardNPCConfig;
import fr.tangv.sorcicubecore.player.PlayerFeatures;
import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.gui.PlayerGui;

public class RewardNPC implements ClickNPC {

	private final String id;
    private final int reward;
	private final String message;
	
	public RewardNPC(SorciCubeSpell sorci, RewardNPCConfig npc) {
		this.id = npc.id.value;
		this.reward = npc.reward.value;
		this.message = npc.message.value.replace("{reward}", Integer.toString(this.reward));
	}
	
	@Override
	public void clickNPC(SorciCubeSpell sorci, String nameNPC, Player player) {
		PlayerGui playerG = sorci.getManagerGui().getPlayerGui(player);
		if (playerG.getPlayerFeatures() != null) {
			PlayerFeatures feature = playerG.getPlayerFeatures();
			if (!feature.getRewardNPC().contains(id)) {
				feature.getRewardNPC().add(id);
				feature.addMoney(reward);
				playerG.uploadPlayerFeatures(sorci.getHandlerPlayers());
				player.sendMessage(this.message.replace("{player}", player.getName()));
			}
		}
	}
	
}
