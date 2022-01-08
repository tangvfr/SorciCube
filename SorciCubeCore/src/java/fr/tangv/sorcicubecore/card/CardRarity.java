package fr.tangv.sorcicubecore.card;

import fr.tangv.sorcicubecore.configs.RarityEnumConfig;

public enum CardRarity {

	COMMUN((byte) 0),
	RARE((byte) 1),
	EPIC((byte) 2),
	LEGENDARY((byte) 3);
	
	private static String[] colors = new String[4];
	
	public static void initColors(RarityEnumConfig colors) {
		CardRarity.colors[0] = colors.common.value;
		CardRarity.colors[1] = colors.rare.value;
		CardRarity.colors[2] = colors.epic.value;
		CardRarity.colors[3] = colors.legendary.value;
	}
	
	private byte number;
	
	private CardRarity(byte number) {
		this.number = number;
	}
	
	public String getColor() {
		return colors[number];
	}
			  
}
