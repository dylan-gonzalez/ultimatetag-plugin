package au.com.dylangonzalez.ultimatetag.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import au.com.dylangonzalez.ultimatetag.UltimateTagPlugin;
import au.com.dylangonzalez.ultimatetag.util.Message;

public class GUICommand implements CommandExecutor {
    JavaPlugin plugin;
    
    public GUICommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            Message.sendMessage(sender, "You must be a player to use this command!");
            return false;
        }
        

        Player player = (Player) sender;
        Inventory gui = Bukkit.createInventory(player, 9, Message.format(UltimateTagPlugin.GUITitle));

        //Menu options
        ItemStack suicide = new ItemStack(Material.TNT); //kills the player
        ItemStack feed = new ItemStack(Material.BREAD); //Fills hunger bar
        ItemStack sword = new ItemStack(Material.IRON_SWORD); //Gives the player a sword

        //Edit the item displays
        ItemMeta suicideMeta = suicide.getItemMeta();
        suicideMeta.setDisplayName(Message.format("&cSUICIDE"));
        ArrayList<String> suicideLore = new ArrayList<>();
        suicideLore.add(Message.format("&6kill yourself lol"));
        suicideMeta.setLore(suicideLore);
        suicide.setItemMeta(suicideMeta);

        ItemMeta feedMeta = feed.getItemMeta();
        feedMeta.setDisplayName(Message.format("&2Feed"));
        ArrayList<String> feedLore = new ArrayList<>();
        feedLore.add(Message.format("&6Fill your hunger bars"));
        feedMeta.setLore(feedLore);
        feed.setItemMeta(feedMeta);

        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.setDisplayName(Message.format("&dSword"));
        ArrayList<String> swordLore = new ArrayList<>();
        swordLore.add(Message.format("&6Get a sword boi"));
        swordMeta.setLore(swordLore);
        sword.setItemMeta(swordMeta);

        //Put the items in the inventory
        ItemStack[] items = { suicide, feed, sword };
        gui.setContents(items);
        player.openInventory(gui);

        return true;
    }
}
