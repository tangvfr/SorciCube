package fr.tangv.sorcicubecore.card;

public enum CardFaction {

	BASIC((byte) 0),
	DARK((byte) 1),
	LIGHT((byte) 2),
	NATURE((byte) 3),
	TOXIC((byte) 4);
		
	private static String[] colors = new String[5];
	
	public static void initColors(char basic, char dark, char light, char nature, char toxic) {
		final String c = "§";
		colors[0] = c+basic;
		colors[1] = c+dark;
		colors[2] = c+light;
		colors[3] = c+nature;
		colors[4] = c+toxic;
	}
	
	private byte number;
	
	private CardFaction(byte number) {
		this.number = number;
	}
	
	public String getColor() {
		return colors[number];
	}
		
}
