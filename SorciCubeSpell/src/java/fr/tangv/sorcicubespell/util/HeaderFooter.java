package fr.tangv.sorcicubespell.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubecore.configs.ParameterConfig;
import net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerListHeaderFooter;

public class HeaderFooter {

	public static void sendHeaderFooter(ParameterConfig config, Player player) {
		
		sendHeaderFooter(config, Arrays.asList(player));
	}
	
	public static void sendHeaderFooter(ParameterConfig config, Collection<Player> list) {
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		Class<?> clazz = packet.getClass();
		Field header = NMSTool.getField(clazz, "a");
		Field footer = NMSTool.getField(clazz, "b");
		NMSTool.setField(packet, header, config.headerList.value.isEmpty() ? ChatSerializer.a("{\"translate\":\"\"}") : CraftChatMessage.fromString(config.headerList.value)[0]);
		NMSTool.setField(packet, footer, config.footerList.value.isEmpty() ? ChatSerializer.a("{\"translate\":\"\"}") : CraftChatMessage.fromString(config.footerList.value)[0]);
		for (Player p : list)
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
	
}
