package fr.tangv.sorcicubespell.util;

import org.bukkit.inventory.ItemStack;

public class ItemHead {

	private static ItemStack createHead(String url) {
		return ItemBuild.buildSkull(url, 1, "", null, false);
	}
	
	//public final static ItemStack SELECTABLE_SPELL = /*purple*/createHead("http://textures.minecraft.net/texture/d143855134e3a477b0e83c1a8a93b157b68efd662e9bb6d490520c08d5392446");
	//public final static ItemStack SELECTABLE_ATTACK = /*yellow*/createHead("http://textures.minecraft.net/texture/ed75e9ed3763518c048ad3e035e284f5dbf04a12ea9fa0c241214d362ad7ed76");
	//public final static ItemStack SELECTABLE_DAMAGE = /*orange*/createHead("http://textures.minecraft.net/texture/2b1d13a0b3f3687d05e2bd0ae7c0aebd8ae00a8e924de62b84dc15fe0096c6b3");
	//public final static ItemStack SELECTABLE_DEAD = /*red*/createHead("http://textures.minecraft.net/texture/5d1c700e085819930eaacd4785310d0347a705f3ebc3daa73c0edc5db3145940");
	//public final static ItemStack SELECTABLE_SPAWN = /*cyan*/createHead("http://textures.minecraft.net/texture/e271151a0f46ced9b811a6426ae868b033470281266a1b41e690f2c8bd2b3cb0");
	//public final static ItemStack SELECTABLE_POSE = /*brown*/createHead("http://textures.minecraft.net/texture/8d4385db7adce92077c81943852a8e51e678d199eb899ed3a065c408d586d215");
	//public final static ItemStack SELECTED_ENTITY = /*lime*/createHead("http://textures.minecraft.net/texture/8f9b1e4f03ae372b6c95a0e5d1e3d9b59f5a7db5d39f7b58d2303ad217059a0e");
	
	public final static ItemStack SELECTABLE_SPELL = /*purple*/createHead("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDE0Mzg1NTEzNGUzYTQ3N2IwZTgzYzFhOGE5M2IxNTdiNjhlZmQ2NjJlOWJiNmQ0OTA1MjBjMDhkNTM5MjQ0NiJ9fX0=");
	public final static ItemStack SELECTABLE_ENTITY_ATTACK = /*yellow*/createHead("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ3NWU5ZWQzNzYzNTE4YzA0OGFkM2UwMzVlMjg0ZjVkYmYwNGExMmVhOWZhMGMyNDEyMTRkMzYyYWQ3ZWQ3NiJ9fX0=");
	public final static ItemStack SELECTABLE_ENTITY_DAMAGE = /*orange*/createHead("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmIxZDEzYTBiM2YzNjg3ZDA1ZTJiZDBhZTdjMGFlYmQ4YWUwMGE4ZTkyNGRlNjJiODRkYzE1ZmUwMDk2YzZiMyJ9fX0=");
	public final static ItemStack SELECTABLE_ALL_SPELL = /*red*/createHead("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQxYzcwMGUwODU4MTk5MzBlYWFjZDQ3ODUzMTBkMDM0N2E3MDVmM2ViYzNkYWE3M2MwZWRjNWRiMzE0NTk0MCJ9fX0=");
	//public final static ItemStack SELECTABLE_SPAWN = /*cyan*/createHead("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTI3MTE1MWEwZjQ2Y2VkOWI4MTFhNjQyNmFlODY4YjAzMzQ3MDI4MTI2NmExYjQxZTY5MGYyYzhiZDJiM2NiMCJ9fX0=");
	public final static ItemStack SELECTABLE_POSE = /*brown*/createHead("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQ0Mzg1ZGI3YWRjZTkyMDc3YzgxOTQzODUyYThlNTFlNjc4ZDE5OWViODk5ZWQzYTA2NWM0MDhkNTg2ZDIxNSJ9fX0=");
	public final static ItemStack SELECTED_ENTITY = /*lime*/createHead("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGY5YjFlNGYwM2FlMzcyYjZjOTVhMGU1ZDFlM2Q5YjU5ZjVhN2RiNWQzOWY3YjU4ZDIzMDNhZDIxNzA1OWEwZSJ9fX0=");
	
	//spell or action
	//SELECTABLE_SPELL = /*purple*/
	//SELECTABLE_DEAD = /*red*/
	//SELECTABLE_SPAWN = /*cyan*/
	
	//pose
	//SELECTABLE_POSE = /*brown*/
	
	//entity attack
	//SELECTABLE_ATTACK = /*yellow*/
	//SELECTED_ENTITY = /*lime*/
	//SELECTABLE_DAMAGE = /*orange*/
	
}
