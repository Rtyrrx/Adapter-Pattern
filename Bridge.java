package net.madias.mcds;

public class Bridge {
    public Bridge(ChatAdapter a, ChatAdapter b) {
        a.onMessageReceived(b::sendMessage);
        b.onMessageReceived(a::sendMessage);
    }
}
