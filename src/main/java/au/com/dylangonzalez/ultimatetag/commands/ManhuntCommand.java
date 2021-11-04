package au.com.dylangonzalez.ultimatetag.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.dylangonzalez.ultimatetag.UltimateTagPlugin;

public class ManhuntCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String args[]) {
        if (!(sender instanceof Player)) {
            return false;
        }


        if (args.length == 1) {
            //set runner to args[0]

            if (args[0] != "reset") {
                UltimateTagPlugin.MANHUNT = true;
                UltimateTagPlugin.manhuntRunner = args[0];
                UltimateTagPlugin.startManhunt(Bukkit.getPlayer(args[0]));
            } 
        }
        return true;
    }
}
