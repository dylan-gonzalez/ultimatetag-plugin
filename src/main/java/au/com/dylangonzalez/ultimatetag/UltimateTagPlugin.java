package au.com.dylangonzalez.ultimatetag;

import java.util.*;

import au.com.dylangonzalez.events.GUIClickItemEvent;
import au.com.dylangonzalez.ultimatetag.commands.GUICommand;
import au.com.dylangonzalez.ultimatetag.commands.UltimateTagCommand;
import au.com.dylangonzalez.ultimatetag.util.Message;
import au.com.dylangonzalez.ultimatetag.util.Role;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class UltimateTagPlugin extends JavaPlugin {
    public static boolean isRunning = false;
    public static int timer = 120; // seconds
    public static int defaultTimer = 120; //seconds
    public static ArrayList<Player> players = new ArrayList<Player>();
    public static HashMap<Player, Role> roles = new HashMap<Player, Role>();
    private static ArrayList<ItemStack> defaultTaggerItems = new ArrayList<ItemStack>();
    private static ArrayList<ItemStack> defaultRunnerItems = new ArrayList<ItemStack>();

    private static final int ARENA_SIZE = 140;
    private static final int TITLE_FADE = 20; //ticks; 20 ticks = 1s
    private static final int TITLE_STAY = 60; //ticks
    private static final Biome[] biomeBlacklist = new Biome[] { Biome.OCEAN, Biome.COLD_OCEAN, Biome.DEEP_OCEAN,
            Biome.WARM_OCEAN, Biome.FROZEN_OCEAN, Biome.LUKEWARM_OCEAN };

    static Random random = new Random();

    public static final String GUITitle = "&bCustom GUI";

    @Override
    public void onEnable() {
        // this.getCommand("hello").setExecutor(new HelloCommand());
        this.getCommand("ultimatetag").setExecutor(new UltimateTagCommand(this));
        this.getCommand("gui").setExecutor(new GUICommand(this));

        getServer().getPluginManager().registerEvents(new GUIClickItemEvent(), this);

        BukkitScheduler scheduler = getServer().getScheduler();

        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                try {
                    if (isRunning) {
                        String timerMsg = "&c" + timer + (timer > 1 ? " seconds" : " second") + " left!";

                        if (timer < 0) {
                            // tagger has lost
                            Message.broadcastMessage("&6Timer went out - Runners win the round!");
                            endGame();
                        } else if (timer == 60 || timer == 30 || timer == 15 || timer == 10 || timer <= 5) {
                            Message.broadcastMessage(timerMsg);
                        }

                        if (!roles.containsValue(Role.RUNNER)) {
                            //Taggers win.
                            Message.broadcastMessage("&6The taggers have won the round!");
                            endGame();
                        }
                        
                        timer--;
                    }
                } catch (Exception e) {
                    Bukkit.getLogger().info("ERROR - unexpected exception: " + e);
                }
            }
        }, 0, 20);

        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
        ItemStack shovel = new ItemStack(Material.DIAMOND_SHOVEL);
        ItemStack cobblestone = new ItemStack(Material.COBBLESTONE, 16);
        ItemStack compass = new ItemStack(Material.COMPASS);

        defaultRunnerItems.addAll(Arrays.asList(pickaxe, axe, shovel, cobblestone));
        defaultTaggerItems.addAll(Arrays.asList(pickaxe, axe, shovel, cobblestone, compass));
    }

    @Override
    public void onDisable() {
        endGame();
        super.onDisable();
    }

    public static void startGame() {
        Bukkit.getLogger().info("Starting game...");

        int taggerIdx = random.nextInt(players.size());
        Player tagger = players.get(taggerIdx);
        World world = tagger.getWorld();

        Location gameCenter = findGameLocation(world);

        // assign roles to all the contestants
        // teleport each player to the game arena
        for (Player player : players) {
            int multiplier = 1;
            if (player == tagger) {
                multiplier = -1;
                setRole(player, Role.TAGGER);
                Message.sendTitle(player, "You are a tagger!", "Tag all the runners.",
                        TITLE_FADE, TITLE_STAY, TITLE_FADE);
                
                Player closestRunner = getClosestRunner(tagger);
                if (closestRunner != null) {
                    player.setCompassTarget(getClosestRunner(tagger).getLocation());
                }
                        
            } else {
                setRole(player, Role.RUNNER);
                Message.sendTitle(player, tagger.getDisplayName() + " is the tagger!", "Avoid being tagged.",
                        TITLE_FADE, TITLE_STAY, TITLE_FADE);
            }
            

            // spawn player in the half of the map separate from the tagger  
            int playerX = gameCenter.getBlockX() + random.nextInt(ARENA_SIZE / 2) * multiplier; 
            int playerZ = gameCenter.getBlockZ() + random.nextInt(ARENA_SIZE) - ARENA_SIZE / 2;
            int playerY = world.getHighestBlockYAt(playerX, playerZ);

            player.teleport(new Location(world, playerX, playerY, playerZ));

            // resetStats(player);
            // assignItems(player);
        }

        world.getWorldBorder().setCenter(gameCenter);
        world.getWorldBorder().setSize(ARENA_SIZE);

        isRunning = true;

        Message.broadcastMessage("Begin!");
    }

    public static void endGame() {
        isRunning = false;
        timer = defaultTimer;
        players.clear();
        roles.clear();

        for (World world : Bukkit.getWorlds()) {
            world.getWorldBorder().reset();
        }
    }

    public static void setRole(Player player, Role role) {
        roles.put(player, role);

        if (role == Role.TAGGER) {
            String taggerDisplayName = "&c" + player.getName();

            player.setDisplayName(taggerDisplayName);
            player.setPlayerListName(taggerDisplayName); 
        }

        assignItems(player);
    }

    public static void assignItems(Player player) {
        resetStats(player);

        Role role = roles.get(player);

        if (role == Role.TAGGER) {
            for (ItemStack item : defaultTaggerItems) {
                player.getInventory().addItem(item);
            }
        } else {
            for (ItemStack item : defaultRunnerItems) {
                player.getInventory().addItem(item);
            }
        }
    }

    public static Player getClosestRunner(Player tagger) {
        double closestDistance = Math.sqrt(2*(Math.pow(ARENA_SIZE,2))); //max possible distance
        Player closestPlayer = null;

        for (Player player : players) {
            if (roles.get(player) != Role.TAGGER) {
                Location location = player.getLocation();
                double distance = location.subtract(location).lengthSquared(); 
                
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestPlayer = player;
                }
            }
        }

        return closestPlayer;
    }

    public static Location findGameLocation(World world) {
        int x = random.nextInt(10000);
        int z = random.nextInt(10000);
        int y = world.getHighestBlockYAt(x, z);
        Biome biome = world.getBiome(x, y, z);

        while (Arrays.asList(biomeBlacklist).contains(biome)) {
            x = random.nextInt(10000);
            z = random.nextInt(10000);
            y = world.getHighestBlockYAt(x, z);

            biome = world.getBiome(x, y, z);
        }

        Location location = new Location(world, x, y, z);
        return location;
    }

    public static void resetStats(Player player) {
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setHealth(20);
        player.setExhaustion(0);
        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getName());
        player.setGameMode(GameMode.SURVIVAL);
    }
}
