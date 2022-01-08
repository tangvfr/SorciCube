package fr.tangv.sorcicubespell.player;

import fr.tangv.sorcicubecore.configs.ParameterConfig;
import fr.tangv.sorcicubecore.player.Group;
import fr.tangv.sorcicubecore.player.PlayerFeatures;

public class DataPlayer {

	private final String displayGroup;
	private final String prefixGroup;
	private final String suffixGroup;
	private final String name;
	private final String level;
	
	public DataPlayer(String name, ParameterConfig parameter) {
		this.displayGroup = parameter.noneGroup.value;
		this.name = name;
		this.level = parameter.noneLvl.value;
		this.prefixGroup = parameter.nonePrefix.value.replace("{level}", level);
		this.suffixGroup = parameter.noneSuffix.value.replace("{level}", level);
	}
	
	public DataPlayer(PlayerFeatures features, Group group, ParameterConfig parameter) {
		this.name = features.getPseudo();
		this.level = Byte.toString(features.getLevel());
		if (group == null) {
			this.displayGroup = parameter.noneGroup.value;
			this.prefixGroup = parameter.nonePrefix.value.replace("{level}", level);
			this.suffixGroup = parameter.noneSuffix.value.replace("{level}", level);
		} else {
			this.displayGroup = group.getDisplay();
			this.prefixGroup = group.getPrefix().replace("{level}", level);
			this.suffixGroup = group.getSuffix().replace("{level}", level);
		}
	}
	
	public String getDisplayGroup() {
		return displayGroup;
	}

	public String getPrefixGroup() {
		return prefixGroup;
	}
	
	public String getSuffixGroup() {
		return suffixGroup;
	}

	public String getName() {
		return name;
	}

	public String getLevel() {
		return level;
	}

	public String replace(String message) {
		return message
				.replace("{name}", name)
				.replace("{group}", displayGroup)
				.replace("{prefix}", prefixGroup)
				.replace("{level}", level)
			;
	}
	
}
