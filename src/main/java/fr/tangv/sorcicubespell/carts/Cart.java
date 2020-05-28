package fr.tangv.sorcicubespell.carts;

import org.bukkit.material.MaterialData;

public abstract class Cart {
	
	private String id;
	private MaterialData material;
	private String name;
	private String[] description;
	private int countMana;
	private int damage;
	private CartType type;
	private CartRarity rarity;
	private CartFaction faction;
	
	protected Cart(String id,
			MaterialData material,
			String name,
			String[] description,
			int countMana,
			int damage,
			CartType type,
			CartRarity rarity,
			CartFaction faction) {
		this.id = id;
		this.setMaterial(material);
		this.setName(name);
		this.setDescription(description);
		this.setCountMana(countMana);
		this.setDamage(damage);
		this.setType(type);
		this.setRarity(rarity);
		this.setFaction(faction);
	}

	public String getId() {
		return id;
	}

	public MaterialData getMaterial() {
		return material;
	}

	public void setMaterial(MaterialData material) {
		this.material = material;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getDescription() {
		return description;
	}

	public void setDescription(String[] description) {
		this.description = description;
	}

	public int getCountMana() {
		return countMana;
	}

	public void setCountMana(int countMana) {
		this.countMana = countMana;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public CartType getType() {
		return type;
	}

	public void setType(CartType type) {
		this.type = type;
	}

	public CartRarity getRarity() {
		return rarity;
	}

	public void setRarity(CartRarity rarity) {
		this.rarity = rarity;
	}

	public CartFaction getFaction() {
		return faction;
	}

	public void setFaction(CartFaction faction) {
		this.faction = faction;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Cart)
			return ((Cart) obj).getId().equals(this.getId());
		return false;
	}
	
}
