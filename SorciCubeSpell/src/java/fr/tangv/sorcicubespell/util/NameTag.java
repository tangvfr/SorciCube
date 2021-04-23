package fr.tangv.sorcicubespell.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NameTag {
    
    private static void sendNameTag(String prefix, String displayName, String suffix, Collection<? extends Player> players, Player player) {
        String name = UUID.randomUUID().toString().substring(0, 16);
        net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardTeam packet = new net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardTeam();
        Class<? extends net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardTeam> clas = packet.getClass();
        Field team_name = NMSTool.getField(clas, "a");
        Field display_name = NMSTool.getField(clas, "b");
        Field prefix2 = NMSTool.getField(clas, "c");
        Field suffix2 = NMSTool.getField(clas, "d");
        Field members = NMSTool.getField(clas, "h");
        Field param_int = NMSTool.getField(clas, "i");
        Field pack_option = NMSTool.getField(clas, "j");
        NMSTool.setField(packet, team_name, name);
        NMSTool.setField(packet, display_name, displayName);
        NMSTool.setField(packet, prefix2, prefix);
        NMSTool.setField(packet, suffix2, suffix);
        NMSTool.setField(packet, members, Arrays.asList(player.getName()));
        NMSTool.setField(packet, param_int, 0);
        NMSTool.setField(packet, pack_option, 1);
        player.setPlayerListName(displayName);
        for (Player ps : players)
        	((CraftPlayer) ps).getHandle().playerConnection.sendPacket(packet);
    }
 
    public static void send(Player player, Collection<? extends Player> collection) {
    	String display = player.getDisplayName();
    	if (display.length() <= 16) {
    		 sendNameTag("", display, "", collection, player);
    	} else if (display.length() <= 32) {
    		sendNameTag(display.substring(0, 16), display.substring(16, display.length()), "", collection, player);
    	} else if (display.length() <= 48) {
    		 sendNameTag(display.substring(0, 16), display.substring(16, 32), display.substring(32, display.length()), collection, player);
    	} else
    		throw new IndexOutOfBoundsException("nametag "+display+" size > 48");
    }

}
