# MCDS – Minecraft ↔ Discord Chat Bridge

MCDS is a lightweight Spigot plugin that bridges **Minecraft chat** with a **Discord channel**, using the Adapter pattern for clean extensibility.

## Features
- Relays messages from Minecraft to Discord and back
- Preserves Discord role colors when showing messages in Minecraft
- Bukkit event listeners for custom behavior

## Requirements
- Java 21
- Spigot/Paper 1.21+
- [JDA (Java Discord API)](https://github.com/discord-jda/JDA)

## Installation
1. Clone the repository
2. Build with Maven:
   ```bash
   mvn package
