package net.madias.mcds;

import org.bukkit.plugin.java.JavaPlugin;

public final class Mcds extends JavaPlugin {
    private Bridge bridge;
    private DiscordChatAdapter discordAdapter;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        String token = getConfig().getString("discord-token");
        String channelId = getConfig().getString("discord-channel-id");
        String messageFormat = getConfig().getString("message-format", "%role%%user% » %msg%");

        MinecraftChatAdapter mcAdapter = new MinecraftChatAdapter();
        getServer().getPluginManager().registerEvents(mcAdapter, this);

        try {
            discordAdapter = new DiscordChatAdapter(token, channelId, messageFormat);
            bridge = new Bridge(mcAdapter, discordAdapter);
            getLogger().info("[MCDS] Bridge enabled!");
        } catch (Exception e) {
            getLogger().severe("[MCDS] Failed to start Discord adapter: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        if (discordAdapter != null) {
            discordAdapter.shutdown();
        }
        getLogger().info("[MCDS] Plugin has been disabled!");
    }
}
