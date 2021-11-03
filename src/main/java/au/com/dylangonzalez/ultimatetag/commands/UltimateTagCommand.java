package au.com.dylangonzalez.ultimatetag.commands;

import au.com.dylangonzalez.ultimatetag.UltimateTagPlugin;
import au.com.dylangonzalez.ultimatetag.util.Message;
import au.com.dylangonzalez.ultimatetag.util.MessageType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class UltimateTagCommand implements CommandExecutor {
    
    private final String INVALID_ARGS = "&c/ultimatetag [start | restart] [<player> <player> | null]";


    JavaPlugin plugin;

    public UltimateTagCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            Message.sendMessage(commandSender, MessageType.MUST_BE_PLAYER_ERROR);

            return false;
        }

        if (args.length == 0) {
            Message.sendMessage(commandSender, INVALID_ARGS);
            return false;
        }


        Player sender = (Player) commandSender;
        if (args[0].equalsIgnoreCase("start")) {
            UltimateTagPlugin.players.addAll(sender.getWorld().getPlayers());
            UltimateTagPlugin.startGame();
        } else if (args[0].equalsIgnoreCase("restart")) {


        } else {
            //invalid command
            Message.sendMessage(commandSender, INVALID_ARGS);
        }
        return true;
    }
    
    public void gameSetup(Player chaser, Player runner) {
        

        //timer
    }
}
