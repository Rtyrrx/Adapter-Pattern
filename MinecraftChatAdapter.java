package net.madias.mcds;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Consumer;

public class MinecraftChatAdapter implements ChatAdapter, Listener {
    private Consumer<String> handler;

    @Override
    public void sendMessage(String message) {
        Bukkit.broadcastMessage(message);
    }

    @Override
    public void onMessageReceived(Consumer<String> handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (handler != null) {
            event.setCancelled(true);
            handler.accept(event.getPlayer().getName() + "» " + event.getMessage());
        }
    }
}
