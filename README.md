# FireMaze Plugin

A Minecraft plugin for Paper 1.21 that creates fire-based maze challenges with traps and hazards.

## Features

- Create maze regions at specific locations
- Set spawn points within mazes
- Interactive traps (lava, flame walls)
- Heat damage over time in maze regions
- Fire resistance for mobs in mazes
- Configurable via YAML file

## Commands

- `/firemaze create` - Create a maze region at your location
- `/firemaze setspawn` - Set the maze spawn point at your location
- `/firemaze info` - Show information about your maze
- Alias: `/fm`

## Installation

1. Build the plugin using Maven: `mvn clean package`
2. Copy the generated JAR file from `target/FireMazePlugin-1.0.jar` to your server's `plugins` folder
3. Restart your Paper server

## Configuration

The plugin automatically creates a `firemaze.yml` file in the plugin data folder with maze configurations.

## Requirements

- Paper server 1.21-R0.1-SNAPSHOT
- Java 21