package fr.tangv.sorcicubespell.util;

import org.bukkit.inventory.ItemStack;

public class ItemHead {

	private static ItemStack createHead(String url) {
		return ItemBuild.buildSkull(url, 1, "", null, false);
	}
	
	public final static ItemStack SELECTED_SPELL = createHead("http://textures.minecraft.net/texture/7db9629cab72e021c8b561650c586b99bfdb3f6a9a962cc6e5611222d3558ef");
	public final static ItemStack SELECTED_ATTACK = createHead("http://textures.minecraft.net/texture/3e4f2f9698c3f186fe44cc63d2f3c4f9a241223acf0581775d9cecd7075");
	public final static ItemStack SELECTED_DAMAGE = createHead("http://textures.minecraft.net/texture/741941e7e7e914a115c342d6d38a22931e138b3da1eeb4e998571e90f871517");
	public final static ItemStack SELECTED_DEAD = createHead("http://textures.minecraft.net/texture/a3852bf616f31ed67c37de4b0baa2c5f8d8fca82e72dbcafcba66956a81c4");
	public final static ItemStack SELECTED_SPAWN = createHead("http://textures.minecraft.net/texture/d8aab6d9a0bdb07c135c97862e4edf3631943851efc545463d68e793ab45a3d3");
	public final static ItemStack SELECTED_POSE = createHead("http://textures.minecraft.net/texture/53c1a4cf49514b57a78c15d484fc0eaa87f2fc6487cd54ac3b5807f64c6a72d");
	public final static ItemStack SELECTED_ENTITY = createHead("http://textures.minecraft.net/texture/b221da4418bd3bfb42eb64d2ab429c61decb8f4bf7d4cfb77a162be3dcb0b927");
	
}
