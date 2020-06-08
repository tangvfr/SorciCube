package fr.tangv.sorcicubespell.carts;

public class CartRender {

	/*public static ItemStack cartToItem(Cart cart, SorciCubeSpell sorci) {
		return CartRender.cartToItem(cart, sorci, 1, false);
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack cartToItem(Cart cart, SorciCubeSpell sorci, int amount, boolean ench) {
		//Config config = sorci.get
		ArrayList<String> lore = new ArrayList<String>();
		//type
		lore.add("");
		lore.add("§7"+sorci.getEnumTool().typeToString(cart.getType()));
		lore.add("§7"+sorci.getEnumTool().rarityToString(cart.getRarity()));
		lore.add("§7"+sorci.getEnumTool().factionToString(cart.getFaction()));
		//lore
		lore.add("");
		for (int i = 0; i < cart.getDescription().length; i++)
			lore.add(cart.getDescription()[i]);
		//id
		lore.add("");
		lore.add("§8Id: "+cart.getId());
		//return item
		if (cart.getMaterialURL() != null && ItemBuild.isSkull(cart.getMaterial()))
			return ItemBuild.buildSkull(cart.getMaterialURL(),
					amount,
					cart.getName(),
					lore,
					ench);
		else
			return ItemBuild.buildItem(cart.getMaterial().getItemType(),
					amount,
					(short) 0,
					cart.getMaterial().getData(),
					cart.getName(),
					lore,
					ench);
	}*/
	
}
