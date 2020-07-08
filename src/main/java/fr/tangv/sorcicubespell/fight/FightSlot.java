package fr.tangv.sorcicubespell.fight;

public enum FightSlot {

	CARD_1(0, -1),
	CARD_2(1, -1),
	CARD_3(2, -1),
	CARD_4(3, -1),
	CARD_5(4, -1),
	CARD_6(5, -1),
	NONE_1(6, -1),
	NONE_2(9, -1),
	FINISH_ROUND(8, -1);

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
