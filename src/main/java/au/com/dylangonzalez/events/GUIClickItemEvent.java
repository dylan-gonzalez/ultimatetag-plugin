package au.com.dylangonzalez.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import au.com.dylangonzalez.ultimatetag.UltimateTagPlugin;
import au.com.dylangonzalez.ultimatetag.util.Message;

public class GUIClickItemEvent implements Listener {

    @EventHandler
    public void clickEvent(InventoryClickEvent event) {
        Bukkit.getLogger().info("Clicked");
        Bukkit.getLogger().info(event.getView().getTitle());
        Bukkit.getLogger().info(UltimateTagPlugin.GUITitle);

        //Check to see if GUI menu is opened
        if (Message.format(event.getView().getTitle()).equalsIgnoreCase(Message.format(UltimateTagPlugin.GUITitle))) {
            Player player = (Player) event.getWhoClicked();

            switch (event.getCurrentItem().getType()) {
                case TNT:
                    player.closeInventory();
                    player.setHealth(0);
                    Message.sendMessage(player, "&cYou just killed yourself");
                    break;
                case BREAD:
                    player.closeInventory();
                    player.setFoodLevel(20);
                    Message.sendMessage(player, "&aYUM");
                    break;
                case IRON_SWORD:
                    player.closeInventory();
                    player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                    Message.sendMessage(player, "&6Start killin");
                    break;
                //$CASES-OMMITTED$
                default:
                    throw new IllegalArgumentException("Unknown item");
            }
            
            event.setCancelled(true); //so they can't take the items
        }

    }
    
}
