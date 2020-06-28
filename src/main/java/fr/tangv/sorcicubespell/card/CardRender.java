package fr.tangv.sorcicubespell.card;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.tangv.sorcicubespell.SorciCubeSpell;
import fr.tangv.sorcicubespell.util.ItemBuild;

public class CardRender {

	public static ItemStack cardToItem(Card card, SorciCubeSpell sorci) {
		return CardRender.cardToItem(card, sorci, 1, false);
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack cardToItem(Card card, SorciCubeSpell sorci, int amount, boolean ench) {
		if (card == null) return null;
		//Config config = sorci.get
		ArrayList<String> lore = new ArrayList<String>();
		//type
		lore.add("");
		lore.add("§7"+sorci.getEnumTool().typeToString(card.getType()));
		lore.add("§7"+sorci.getEnumTool().rarityToString(card.getRarity()));
		lore.add("§7"+sorci.getEnumTool().factionToString(card.getFaction()));
		//lore
		lore.add("");
		for (int i = 0; i < card.getDescription().size(); i++)
			lore.add(card.getDescription().get(i));
		//id
		lore.add("");
		lore.add("§8Id: "+card.getUUID());
		//return item
		CardMaterial material = card.getMaterial();
		if (material.hasUrl())
			return ItemBuild.buildSkull(material.getUrl(),
					amount,
					card.getName(),
					lore,
					ench);
		else
			return ItemBuild.buildItem(Material.getMaterial(material.getId()),
					amount,
					(short) 0,
					(byte) material.getData(),
					card.getName(),
					lore,
					ench);
	}
	
}
