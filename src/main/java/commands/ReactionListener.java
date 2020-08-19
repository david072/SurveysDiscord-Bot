package commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import surveys.SurveyManager;
import util.TextBuilder;

import javax.annotation.Nonnull;

public class ReactionListener extends ListenerAdapter {

    private static String messageID;
    private static String name;
    private static Member creator;

    static void setup(String _messageID, String _name, Member _author) {
        messageID = _messageID;
        name = _name;
        creator = _author;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(messageID == null || name == null || creator == null || event.getUser().isBot()) return;

        if(event.getMessageId().equals(messageID)) {
            MessageReaction mReaction = event.getReaction();
            if(mReaction.getReactionEmote().getEmoji().equalsIgnoreCase("\uD83D\uDC4D")) {
                boolean success = SurveyManager.removeSurvey(name, creator);
                if(success) {
                    TextBuilder.sendEmbedMessage("Umfrage l\u006fschen", "Die Umfrage `" + name + "` wurde erfolgreich gel\u006fscht! :partying_face:",
                            TextBuilder.randomColor(), event.getTextChannel());
                }
                else {
                    TextBuilder.sendEmbedMessage("Umfrage l\u00f6schen", "Oh, etwas ist schiefgelaufen. :sob: Warscheinlich hast du dich im Namen geirrt.",
                            TextBuilder.randomColor(), event.getTextChannel());
                }
            }
            else if(mReaction.getReactionEmote().getEmoji().equalsIgnoreCase("\uD83D\uDC4E")) {
                TextBuilder.sendEmbedMessage("Umfrage l\u006fschen", "Alles klar. Deine Umfrage soll noch weiter leben! :smiley:", TextBuilder.randomColor(),
                        event.getTextChannel());
            }

            messageID = null;
            name = null;
            creator = null;
        }
    }
}
