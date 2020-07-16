package fr.tangv.sorcicubespell.util;

import org.bukkit.inventory.ItemStack;

public class ItemHead {

	private static ItemStack createHead(String url) {
		return ItemBuild.buildSkull(url, 1, "", null, false);
	}
	
	public final static ItemStack SELECTABLE_SPELL = createHead("http://textures.minecraft.net/texture/d143855134e3a477b0e83c1a8a93b157b68efd662e9bb6d490520c08d5392446");
	public final static ItemStack SELECTABLE_ATTACK = createHead("http://textures.minecraft.net/texture/ed75e9ed3763518c048ad3e035e284f5dbf04a12ea9fa0c241214d362ad7ed76");
	public final static ItemStack SELECTABLE_DAMAGE = createHead("http://textures.minecraft.net/texture/2b1d13a0b3f3687d05e2bd0ae7c0aebd8ae00a8e924de62b84dc15fe0096c6b3");
	public final static ItemStack SELECTABLE_DEAD = createHead("http://textures.minecraft.net/texture/5d1c700e085819930eaacd4785310d0347a705f3ebc3daa73c0edc5db3145940");
	public final static ItemStack SELECTABLE_SPAWN = createHead("http://textures.minecraft.net/texture/e271151a0f46ced9b811a6426ae868b033470281266a1b41e690f2c8bd2b3cb0");
	public final static ItemStack SELECTABLE_POSE = createHead("http://textures.minecraft.net/texture/8d4385db7adce92077c81943852a8e51e678d199eb899ed3a065c408d586d215");
	public final static ItemStack SELECTED_ENTITY = createHead("http://textures.minecraft.net/texture/8f9b1e4f03ae372b6c95a0e5d1e3d9b59f5a7db5d39f7b58d2303ad217059a0e");
	
}
