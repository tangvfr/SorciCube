package fr.tangv.sorcicubespell.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NameTag {

    private static void setField(Object packet, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(packet, value);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        field.setAccessible(!field.isAccessible());
    }

    private static Field getField(Class<?> classs, String fieldname) {
        try {
            return classs.getDeclaredField(fieldname);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static void sendNameTag(String prefix, String displayName, String suffix, List<Player> players, String playerName) {
        String name = UUID.randomUUID().toString().substring(0, 16);
        net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardTeam packet = new net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardTeam();
        Class<? extends net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardTeam> clas = packet.getClass();
        Field team_name = getField(clas, "a");
        Field display_name = getField(clas, "b");
        Field prefix2 = getField(clas, "c");
        Field suffix2 = getField(clas, "d");
        Field members = getField(clas, "h");
        Field param_int = getField(clas, "i");
        Field pack_option = getField(clas, "j");
        setField(packet, team_name, name);
        setField(packet, display_name, displayName);
        setField(packet, prefix2, prefix);
        setField(packet, suffix2, suffix);
        setField(packet, members, Arrays.asList(playerName));
        setField(packet, param_int, 0);
        setField(packet, pack_option, 1);
        for (Player ps : players)
        	((CraftPlayer) ps).getHandle().playerConnection.sendPacket(packet);
    }
 
    public static void send(Player player, String display, List<Player> players) {
    	String prefix = "";
    	String name = "";
    	String suffix = "";
    	if (display.length() <= 16) {
    		name = display;
    	} else if (display.length() <= 32) {
    		prefix = display.substring(0, 16);
    		name = display.substring(16, display.length());
    	} else if (display.length() <= 48) {
    		prefix = display.substring(0, 16);
    		name = display.substring(16, 32);
    		suffix = display.substring(32, display.length());
    	} else
    		throw new IndexOutOfBoundsException("nametag "+display+" size > 48");
        sendNameTag(prefix, name, suffix, players, player.getName());
    }

}
