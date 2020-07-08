package fr.tangv.sorcicubespell.fight;

public enum FightSlot {

	CARD_1(0, 36),
	CARD_2(1, 37),
	CARD_3(2, 38),
	CARD_4(3, 39),
	CARD_5(4, 40),
	CARD_6(5, 41),
	NONE_1(6, 42),
	NONE_2(9, 43),
	FINISH_ROUND(8, 44);

	private Integer slotInv;
	private Integer slotClick;
	
	private FightSlot(int slotInv, int slotClick) {
		this.slotInv = slotInv;
		this.slotClick = slotClick;
	}
	
	public Integer getSlotInv() {
		return slotInv;
	}
	
	public Integer getSlotClick() {
		return slotClick;
	}
	
}
