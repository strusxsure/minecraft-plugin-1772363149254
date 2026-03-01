package com.stormai.plugin;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MazeListener implements Listener {
    private Main plugin;
    private Map<UUID, Long> lastDamageTime = new HashMap<>();

    public MazeListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (block.getType() == Material.STONE_BUTTON) {
            // Trigger lava trap
            triggerLavaTrap(block.getLocation());
            player.sendMessage("Lava trap triggered!");
        } else if (block.getType() == Material.LEVER) {
            // Trigger flame wall
            triggerFlameWall(block.getLocation());
            player.sendMessage("Flame wall activated!");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();

        if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) {
            return;
        }

        // Check if player is in maze region
        if (isInMazeRegion(player)) {
            // Apply heat damage over time
            applyHeatDamage(player);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == DamageCause.LAVA) {
            event.setCancelled(true); // Cancel natural lava damage
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (isInMazeRegion(event.getLocation())) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            // Apply fire resistance to mobs in maze
            entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));
        }
    }

    private boolean isInMazeRegion(Player player) {
        // Check if player is in configured maze region
        return plugin.getMazeConfig().contains("mazes." + player.getUniqueId());
    }

    private boolean isInMazeRegion(Location location) {
        // Check if location is in any maze region
        for (String mazeId : plugin.getMazeConfig().getConfigurationSection("mazes").getKeys(false)) {
            String worldName = plugin.getMazeConfig().getString("mazes." + mazeId + ".world");
            int x = plugin.getMazeConfig().getInt("mazes." + mazeId + ".x");
            int y = plugin.getMazeConfig().getInt("mazes." + mazeId + ".y");
            int z = plugin.getMazeConfig().getInt("mazes." + mazeId + ".z");

            if (location.getWorld().getName().equals(worldName) &&
                location.getBlockX() == x &&
                location.getBlockY() == y &&
                location.getBlockZ() == z) {
                return true;
            }
        }
        return false;
    }

    private void applyHeatDamage(Player player) {
        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long lastTime = lastDamageTime.getOrDefault(uuid, 0L);
        long damageInterval = 2000; // 2 seconds

        if (currentTime - lastTime >= damageInterval) {
            // Apply heat damage
            player.damage(1.0);
            player.sendMessage("§cYou are taking heat damage!");
            lastDamageTime.put(uuid, currentTime);
        }
    }

    private void triggerLavaTrap(Location location) {
        // Create lava trap at location
        location.getWorld().getBlockAt(location).setType(Material.LAVA);
        // Schedule lava removal after 10 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                location.getWorld().getBlockAt(location).setType(Material.AIR);
            }
        }.runTaskLater(plugin, 200L);
    }

    private void triggerFlameWall(Location location) {
        // Create flame wall effect
        for (int i = -5; i <= 5; i++) {
            Location flameLoc = location.clone().add(i, 0, 0);
            flameLoc.getWorld().getBlockAt(flameLoc).setType(Material.FIRE);
            // Schedule flame removal after 5 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    flameLoc.getWorld().getBlockAt(flameLoc).setType(Material.AIR);
                }
            }.runTaskLater(plugin, 100L);
        }
    }
}