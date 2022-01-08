package fr.tangv.sorcicubespell.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.tangv.sorcicubespell.player.DataPlayer;
import net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardTeam;

public class NameTag {
    
    public static void sendNameTag(DataPlayer player, Collection<? extends Player> players) {
        String rand = UUID.randomUUID().toString().substring(0, 16);
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        Class<?> clas = packet.getClass();
        Field team_name = NMSTool.getField(clas, "a");
        Field display_name = NMSTool.getField(clas, "b");
        Field prefix2 = NMSTool.getField(clas, "c");
        Field suffix2 = NMSTool.getField(clas, "d");
        Field members = NMSTool.getField(clas, "h");
        Field param_int = NMSTool.getField(clas, "i");
        Field pack_option = NMSTool.getField(clas, "j");
        NMSTool.setField(packet, team_name, rand);
        NMSTool.setField(packet, display_name, player.getName());
        NMSTool.setField(packet, prefix2, player.getPrefixGroup());
        NMSTool.setField(packet, suffix2, player.getSuffixGroup());
        NMSTool.setField(packet, members, Arrays.asList(player.getName()));
        NMSTool.setField(packet, param_int, 0);
        NMSTool.setField(packet, pack_option, 1);
        for (Player ps : players)
        	((CraftPlayer) ps).getHandle().playerConnection.sendPacket(packet);
    }

}
