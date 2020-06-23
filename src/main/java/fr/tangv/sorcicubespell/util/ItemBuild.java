package fr.tangv.sorcicubespell.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class ItemBuild {

	public static ItemStack buildItem(Material type, int amount, short damage, byte data
			, String name, List<String> lore, boolean ench) {
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(type, amount, damage, data);
		ItemMeta meta = item.getItemMeta();
		if (name != null)
			meta.setDisplayName(name);
		if (lore != null)
			meta.setLore(lore);
		if (ench)
			meta.addEnchant(Enchantment.DURABILITY, 0, true);
		for (ItemFlag flag : ItemFlag.values())
			meta.addItemFlags(flag);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack buildSkull(String url, int amount, String name, List<String> lore, boolean ench) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
		//verif differente null
		if (url == null || url.isEmpty())
			return skull;
		//init texture meta of skull
		ItemMeta skullMeta = skull.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
	    profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
	    //change profil of metaskull
	    try {
	    	Field profileField = skullMeta.getClass().getDeclaredField("profile");
	    	profileField.setAccessible(true);
	    	profileField.set(skullMeta, profile);
	    } catch (Exception e) {
       		Bukkit.getLogger().warning(e.getMessage());
       	}
	    //init others meta of skull
	    if (name != null)
	    	skullMeta.setDisplayName(name);
		if (lore != null)
			skullMeta.setLore(lore);
		if (ench)
			skullMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		for (ItemFlag flag : ItemFlag.values())
			skullMeta.addItemFlags(flag);
		//set meta
		skull.setItemMeta(skullMeta);
	    return skull;
	}
	
}
