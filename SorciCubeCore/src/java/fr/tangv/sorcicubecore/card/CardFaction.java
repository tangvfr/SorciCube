package fr.tangv.sorcicubecore.card;

public enum CardFaction {

	BASIC((byte) 0),
	DARK((byte) 1),
	LIGHT((byte) 2),
	NATURE((byte) 3),
	TOXIC((byte) 4);
		
	private static String[] colors = new String[5];
	
	public static void initColors(String basic, String dark, String light, String nature, String toxic) {
		colors[0] = basic;
		colors[1] = dark;
		colors[2] = light;
		colors[3] = nature;
		colors[4] = toxic;
	}
	
	private byte number;
	
	private CardFaction(byte number) {
		this.number = number;
	}
	
	public String getColor() {
		return colors[number];
	}
		
}
