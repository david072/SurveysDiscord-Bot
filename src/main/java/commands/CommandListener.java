package commands;

import main.Main;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();

        if(event.isFromType(ChannelType.TEXT)) {
            if(message.startsWith(Main.PREFIX)) {
                String[] args = message.substring(1).split(" ");
                if(args.length == 0) return;

                for(Commands.CommandOptions commandOption : Commands.CommandOptions.values()) {
                    if(commandOption.name().equalsIgnoreCase(args[0])) {
                        commandOption.function.func(event.getTextChannel(), args, event.getMember());
                    }
                }
            }
        }
    }
}
