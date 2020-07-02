package fr.tangv.sorcicubespell.packet;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.util.Cooldown;

public class PlayerPacket {

	private Player player;
	private Inventory inv;
	private ItemStack[] itemCards;
	private boolean[] newCards;
	private int start;
	private int viewCard;
	private boolean actual;
	private Cooldown cooldown;
	
	public PlayerPacket(Player player, Inventory inv, ItemStack[] itemCards, boolean[] newCards, int start) {
		this.player = player;
		this.inv = inv;
		this.itemCards = itemCards;
		this.viewCard = 0;
		this.actual = true;
		this.newCards = newCards;
		this.start = start;
		this.cooldown = new Cooldown(1_500);
		cooldown.loop();
	}
	
	public void stopActual() {
		actual = false;
	}
	
	public boolean needActual() {
		return actual;
	}
	
	public int getStart() {
		return start;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	public int getSizeItemCards() {
		return itemCards.length;
	}
	
	public ItemStack getItemCard(int i) {
		return itemCards[i];
	}
	
	public boolean isNewCard(int i) {
		return newCards[i];
	}

	public int getViewCard() {
		return viewCard;
	}

	public void setViewCard(int viewCard) {
		this.viewCard = viewCard;
	}
	
	public Cooldown getCooldown() {
		return cooldown;
	}
	
}
