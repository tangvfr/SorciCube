package fr.tangv.sorcicubespell;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;

public class test {

	public static void main(String[] args) {
		TextComponent page = new TextComponent("");
		page.addExtra(new TextComponent("§cOk dac"));
		page.addExtra(new TextComponent("§6fuck you"));
		page.addExtra(new TextComponent("§ehello"));
		String json = ComponentSerializer.toString(page);
		System.out.println(json);
		System.out.println(IChatBaseComponent.ChatSerializer.a(json));
	}
	
}
