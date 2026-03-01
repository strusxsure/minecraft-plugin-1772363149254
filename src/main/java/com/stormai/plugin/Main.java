package com.stormai.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin implements Listener {
    private File mazeConfig;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        getLogger().info("FireMaze Plugin has been enabled!");

        // Create configuration file if it doesn't exist
        saveDefaultConfig();
        mazeConfig = new File(getDataFolder(), "firemaze.yml");
        if (!mazeConfig.exists()) {
            saveResource("firemaze.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(mazeConfig);

        // Register events
        getServer().getPluginManager().registerEvents(new MazeListener(this), this);

        // Register commands
        getCommand("firemaze").setExecutor(new CommandHandler(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("FireMaze Plugin has been disabled!");
    }

    public FileConfiguration getMazeConfig() {
        return config;
    }

    public File getMazeConfigFile() {
        return mazeConfig;
    }

    public void saveMazeConfig() {
        try {
            config.save(mazeConfig);
        } catch (IOException e) {
            getLogger().severe("Could not save maze config: " + e.getMessage());
        }
    }
}