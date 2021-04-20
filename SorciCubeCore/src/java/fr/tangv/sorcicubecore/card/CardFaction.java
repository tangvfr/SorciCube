package fr.tangv.sorcicubecore.card;

import fr.tangv.sorcicubecore.configs.FactionEnumConfig;

public enum CardFaction {

	BASIC((byte) 0),
	DARK((byte) 1),
	LIGHT((byte) 2),
	NATURE((byte) 3),
	TOXIC((byte) 4);
		
	private static String[] colors = new String[5];
	
	public static void initColors(FactionEnumConfig colors) {
		CardFaction.colors[0] = colors.basic.value;
		CardFaction.colors[1] = colors.dark.value;
		CardFaction.colors[2] = colors.light.value;
		CardFaction.colors[3] = colors.nature.value;
		CardFaction.colors[4] = colors.toxic.value;
	}
	
	private byte number;
	
	private CardFaction(byte number) {
		this.number = number;
	}
	
	public String getColor() {
		return colors[number];
	}
		
}
