package net.madias.mcds;

import java.util.function.Consumer;

public interface ChatAdapter {
    void sendMessage(String message);
    void onMessageReceived(Consumer<String> handler);
}
