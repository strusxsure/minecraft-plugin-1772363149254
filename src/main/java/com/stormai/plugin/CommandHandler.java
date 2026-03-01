package com.stormai.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class CommandHandler implements CommandExecutor {
    private Main plugin;

    public CommandHandler(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player!");
            return true;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();

        if (args.length == 0) {
            player.sendMessage("FireMaze Commands:");
            player.sendMessage("/firemaze create - Create a new maze region");
            player.sendMessage("/firemaze setspawn - Set the maze spawn point");
            player.sendMessage("/firemaze info - Show maze info");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                plugin.getMazeConfig().set("mazes." + player.getUniqueId() + ".world", location.getWorld().getName());
                plugin.getMazeConfig().set("mazes." + player.getUniqueId() + ".x", location.getBlockX());
                plugin.getMazeConfig().set("mazes." + player.getUniqueId() + ".y", location.getBlockY());
                plugin.getMazeConfig().set("mazes." + player.getUniqueId() + ".z", location.getBlockZ());
                plugin.saveMazeConfig();
                player.sendMessage("Maze region created at your location!");
                break;

            case "setspawn":
                plugin.getMazeConfig().set("mazes." + player.getUniqueId() + ".spawn.world", location.getWorld().getName());
                plugin.getMazeConfig().set("mazes." + player.getUniqueId() + ".spawn.x", location.getBlockX());
                plugin.getMazeConfig().set("mazes." + player.getUniqueId() + ".spawn.y", location.getBlockY());
                plugin.getMazeConfig().set("mazes." + player.getUniqueId() + ".spawn.z", location.getBlockZ());
                plugin.saveMazeConfig();
                player.sendMessage("Maze spawn point set!");
                break;

            case "info":
                if (plugin.getMazeConfig().contains("mazes." + player.getUniqueId())) {
                    player.sendMessage("Maze Info:");
                    player.sendMessage("World: " + plugin.getMazeConfig().getString("mazes." + player.getUniqueId() + ".world"));
                    player.sendMessage("Location: X:" + plugin.getMazeConfig().getInt("mazes." + player.getUniqueId() + ".x") +
                            " Y:" + plugin.getMazeConfig().getInt("mazes." + player.getUniqueId() + ".y") +
                            " Z:" + plugin.getMazeConfig().getInt("mazes." + player.getUniqueId() + ".z"));
                } else {
                    player.sendMessage("No maze found for you!");
                }
                break;

            default:
                player.sendMessage("Unknown subcommand. Use /firemaze for help.");
                break;
        }

        return true;
    }
}