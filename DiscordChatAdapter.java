package net.madias.mcds;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import javax.security.auth.login.LoginException;
import java.util.function.Consumer;

public class DiscordChatAdapter extends ListenerAdapter implements ChatAdapter {
    private final String channelId;
    private final String messageFormat;
    private final JDA jda;
    private final TextChannel channel;
    private Consumer<String> handler;

    public DiscordChatAdapter(String token, String channelId, String messageFormat) throws LoginException, InterruptedException {
        this.channelId = channelId;
        this.messageFormat = messageFormat;

        this.jda = JDABuilder.createDefault(token,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(this)
                .build();

        jda.awaitReady();
        this.channel = jda.getTextChannelById(channelId);

        if (this.channel == null) {
            throw new IllegalArgumentException("Invalid Discord channel ID: " + channelId);
        }
    }

    @Override
    public void sendMessage(String message) {
        if (channel != null) {
            channel.sendMessage(MessageCreateData.fromContent(message)).queue();
        }
    }

    @Override
    public void onMessageReceived(Consumer<String> handler) {
        this.handler = handler;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getId().equals(this.channelId) && !event.getAuthor().isBot() && this.handler != null) {
            String content = event.getMessage().getContentDisplay().trim();
            if (!content.isEmpty()) {
                String roleName = "";
                String mcColor = "";
                if (event.getMember() != null && !event.getMember().getRoles().isEmpty()) {
                    var role = event.getMember().getRoles().get(0);
                    roleName = role.getName();
                    mcColor = getMinecraftColorCode(role.getColorRaw());
                }

                String formatted = messageFormat
                        .replace("%user%", event.getAuthor().getName())
                        .replace("%role%", (roleName.isEmpty() ? "" : mcColor + roleName + "§r "))
                        .replace("%msg%", content);

                handler.accept(formatted);
            }
        }
    }

    private String getMinecraftColorCode(int colorRaw) {
        if (colorRaw == 0) return "";
        String hex = String.format("%06X", colorRaw);
        StringBuilder mcColor = new StringBuilder("§x");
        for (char c : hex.toCharArray()) {
            mcColor.append('§').append(c);
        }
        return mcColor.toString();
    }

    public void shutdown() {
        if (jda != null) jda.shutdownNow();
    }
}
