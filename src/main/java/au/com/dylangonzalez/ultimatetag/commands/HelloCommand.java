package au.com.dylangonzalez.ultimatetag.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelloCommand implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (player.hasPermission("hello.use")) {
                player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Hey welcome to the server!");
            } else {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            }

            return true;
        } else {
            commandSender.sendMessage("Hey console!");
        }
        
        return false;
    }
}
