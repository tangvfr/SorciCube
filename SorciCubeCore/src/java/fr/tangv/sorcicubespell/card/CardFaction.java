package fr.tangv.sorcicubespell.card;

public enum CardFaction {

	BASIC("§7"),
	DARK("§8"),
	LIGHT("§6"),
	NATURE("§2"),
	TOXIC("§5");
		  
	private String color;
	
	private CardFaction(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}
	
}
