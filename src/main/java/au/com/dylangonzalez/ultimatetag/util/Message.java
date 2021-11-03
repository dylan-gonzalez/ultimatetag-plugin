package au.com.dylangonzalez.ultimatetag.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message {

    public static String format(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String stripColor(String str) {
        return ChatColor.stripColor(str);
    }

    public static void sendMessage(CommandSender commandSender, String message) {
        commandSender.sendMessage(format(message));
    }

    public static void sendTitle(Player player, String title,String subtitle,  int fadeIn, int stay, int fadeOut) {
        player.sendTitle(format(title), format(subtitle), fadeIn, stay, fadeOut);
    }

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(format(message));
    }
    
}
