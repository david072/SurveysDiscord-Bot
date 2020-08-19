package util;

import com.sun.security.auth.callback.TextCallbackHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.Random;

public class TextBuilder {

    public static void sendEmbedMessage(String title, String message, Color color, TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(message);
        builder.setColor(color);

        channel.sendMessage(builder.build()).queue();
    }

    public static void sendEmbedMessage(String title, String message, String footer, Color color, TextChannel channel, MessageEmbed.Field... fields) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(message);
        builder.setColor(color);
        builder.setFooter(footer);

        for (MessageEmbed.Field field : fields) {
            builder.addField(field);
        }

        channel.sendMessage(builder.build()).queue();
    }

    public static String sendEmbedMessage(String title, String message, Color color, TextChannel channel, String... reactions) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(message);
        builder.setColor(color);

        /*Message theMessage = channel.sendMessage(builder.build()).queue(message1 -> {

        });*/

        Message theMessage = channel.sendMessage(builder.build()).complete();
        for(String reaction : reactions) {
            theMessage.addReaction(reaction).queue();
        }

        return theMessage.getId();
    }

    public static Color randomColor() {
        Random rand = new Random();
        int nextInt = rand.nextInt(0xffffff + 1);
        String _color = String.format("#%06x", nextInt);

        return Color.decode(_color);
    }

}
