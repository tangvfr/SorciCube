package fr.tangv.sorcicubespell.card;

public enum CardRarity {

	COMMUN("§8"),
	RARE("§3"),
	EPIC("§d"),
	LEGENDARY("§6");
	
	private String color;
			
	private CardRarity(String color) {
		this.color = color;
	}
			
	public String getColor() {
		return color;
	}
			  
}
