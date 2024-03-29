package fr.tangv.sorcicubespell.fight;

public enum FightSlot {

	CARD_1(0, 36),
	CARD_2(1, 37),
	CARD_3(2, 38),
	CARD_4(3, 39),
	CARD_5(4, 40),
	CARD_6(5, 41),
	NONE_1(6, 42),
	STICK_VIEW(7, 43),
	FINISH_ROUND(8, 44),
	BUY_CARD(20, 20),
	SWAP_CARD(24, 24);

	public static FightSlot valueOfRaw(int raw) {
		for (FightSlot slot : values())
			if (slot.getSlotRaw() == raw)
				return slot;
		return null;
	}
	
	private final Integer slotInv;
	private final Integer slotRaw;
	
	private FightSlot(int slotInv, int slotRaw) {
		this.slotInv = slotInv;
		this.slotRaw = slotRaw;
	}
	
	public Integer getSlotInv() {
		return slotInv;
	}
	
	public Integer getSlotRaw() {
		return slotRaw;
	}
	
}
