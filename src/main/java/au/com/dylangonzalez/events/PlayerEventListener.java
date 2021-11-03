package au.com.dylangonzalez.events;

import au.com.dylangonzalez.ultimatetag.UltimateTagPlugin;
import au.com.dylangonzalez.ultimatetag.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerEventListener implements Listener {
    @EventHandler
    public void onPlayerTookDamage(EntityDamageByBlockEvent event) {
        if (event.getEntity() instanceof Player) {
            Player target = (Player) event.getEntity();

            if (UltimateTagPlugin.isRunning) {
                if (event.getDamager() instanceof Player) {
                    Player damager = (Player) event.getDamager();

                    if (UltimateTagPlugin.roles.get(damager) == Role.TAGGER && UltimateTagPlugin.roles.get(target) == Role.RUNNER) {
                        UltimateTagPlugin.setRole(target, Role.TAGGER);

                        Bukkit.broadcastMessage(target.getDisplayName() + " has been tagged!");
                        target.sendMessage(ChatColor.RED + "You've been tagged!");
                        damager.sendMessage("You tagged " + target.getDisplayName() + "!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player target = (Player) event.getEntity();

            if (UltimateTagPlugin.isRunning) {
                if (UltimateTagPlugin.roles.get(target) == Role.RUNNER) {
                    if (target.getHealth() - event.getDamage() < 1) {
                        UltimateTagPlugin.setRole(target, Role.TAGGER);

                        Bukkit.broadcastMessage(target.getDisplayName() + " died and is now also a tagger!");
                        target.sendMessage(ChatColor.RED + "Because you died you are a now a tagger!");

                        target.setHealth(20);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
