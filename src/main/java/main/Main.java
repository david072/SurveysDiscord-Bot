package main;

import commands.CommandListener;
import commands.Commands;
import commands.ReactionListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class Main {

    public static Main INSTANCE;

    public static ShardManager shardManager;

    public static final String PREFIX = "!";

    public static void main(String[] args) {
        try {
            new Main();
        }
        catch (LoginException e) { e.printStackTrace(); }
    }

    public Main() throws LoginException {
        INSTANCE = this;

        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
        builder.setToken("NzQzMDQ5ODg4MTQ0Njg3MTA1.XzPBIg.nwqb8YRnJrkqovmEHZLXjdIvlD8");

        builder.setActivity(Activity.watching("wie man eine Umfrage macht."));
        builder.setStatus(OnlineStatus.ONLINE);

        builder.addEventListeners(new CommandListener());
        builder.addEventListeners(new ReactionListener());

        shardManager = builder.build();

        new Commands();
    }

}
