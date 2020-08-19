package commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNameEvent;
import org.w3c.dom.Text;
import surveys.AnswerOption;
import surveys.Survey;
import surveys.SurveyManager;
import util.TextBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class Commands {

    public static Commands INSTANCE;

    public CommandFunctions add;
    public CommandFunctions remove;
    public CommandFunctions vote;
    public CommandFunctions survey;
    public CommandFunctions help;
    public CommandFunctions finish;

    public Commands() {
        INSTANCE = this;

        add = this::add;
        remove = this::remove;
        vote = this::vote;
        survey = this::survey;
        help = this::help;
        finish = this::finish;
    }


    private void add(TextChannel channel, String[] args, Member author) {
        if(args.length >= 4) {
            Date dateCreated = Date.from(Instant.now());
            String name = args[1];
            name = name.replace("-", " ");
            ArrayList<AnswerOption> answers = new ArrayList<>();
            for(int i = 2; i < args.length; i++) {
                String answer = args[i];
                answer = answer.replace("-", " ");
                AnswerOption answerOption = new AnswerOption(answer);
                answers.add(answerOption);
            }

            if(answers.size() > 8) {
                TextBuilder.sendEmbedMessage("Umfrage erstellen", "Du kannst nur maximal 8 Antwort-M\u00f6glichkeiten angeben.", TextBuilder.randomColor(), channel);
                return;
            }

            ArrayList<String> alreadyCameAnswers = new ArrayList<>();
            for(AnswerOption s : answers) {
                for(String string : alreadyCameAnswers) {
                    if(string.equals(s.getName())) {
                        TextBuilder.sendEmbedMessage("Umfrage erstellen", "Na na na. Du kannst nicht zweimal die gleiche Antwort angeben!",
                                TextBuilder.randomColor(), channel);
                        return;
                    }
                }

                alreadyCameAnswers.add(s.getName());
            }

            Survey newSurvey = new Survey(author, dateCreated, name);
            newSurvey.setAnswers(answers);
            SurveyManager.addSurvey(newSurvey, channel);
        }
        else {
            TextBuilder.sendEmbedMessage("Umfrage erstellen", "Um eine Umfrage zu erstellen, gib `!add [Umfragename] [Antwort1] [Antwort2]` ein.\n" +
                    "**Tipp:** um Leerzeichen einzugeben gib einen Strich (`-`) ein.", TextBuilder.randomColor(), channel);
        }
    }

    private void remove(TextChannel channel, String[] args, Member author) {
        if(args.length >= 2) {
            String name = args[1];
            name = name.replace("-", " ");
            String messageID = TextBuilder.sendEmbedMessage("Umfrage l\u00f6schen", "M\u00f6chtest du die Umfrage `" + name + "` wirklich l\u00f6schen?", TextBuilder.randomColor(), channel,
                    "\uD83D\uDC4D", "\uD83D\uDC4E");

            ReactionListener.setup(messageID, name, author);
        }
        else {
            TextBuilder.sendEmbedMessage("Umfrage l\u00f6schen", "Um eine Umfrage zu l\u00f6schen, gib `!remove [Umfragename]` ein. Reagiere danach mit **thumbsup** wenn du zustimmst, " +
                    "oder reagiere mit **thumbsdown** wenn du es dir nochmal anders \u00fcberlegen m\u00f6chtest.", TextBuilder.randomColor(), channel);
        }
    }

    private void vote(TextChannel channel, String[] args, Member author) {
        if(args.length == 2) {
            String name = args[1];
            name = name.replace("-", " ");
            ArrayList<AnswerOption> answers = SurveyManager.getAnswersForSurvey(name, author);
            if(answers == null) {
                TextBuilder.sendEmbedMessage("F\u00fcr Umfrage abstimmen", "Die Umfrage `" + name + "` existiert nicht. Checke mal deine Schreibung.",
                        TextBuilder.randomColor(), channel);
                return;
            }

            StringBuilder sb = new StringBuilder();
            for(AnswerOption answerOption : answers) {
                sb.append("- ").append(answerOption.getName()).append("\n");
            }

            TextBuilder.sendEmbedMessage("F\u00fcr Umfrage abstimmen", "F\u00fcr die Umfrage **" + name + "** gibt es folgende Antwort-M\u00f6glichkeiten: \n"
                    + sb.toString(), TextBuilder.randomColor(), channel);
        }
        else if(args.length >= 3) {
            String name = args[1];
            name = name.replace("-", " ");
            String answerOption = args[2];
            answerOption = answerOption.replace("-", " ");

            int success = SurveyManager.vote(name, answerOption, author, channel);
            if(success == 1) {
                TextBuilder.sendEmbedMessage("F\u00fcr Umfrage abstimmen", "Wohoo! Du hast erfolgreich f\u00fcr `" + name + "` abgestimmt! :partying_face:",
                        TextBuilder.randomColor(), channel);
            }
            else if(success == 0) {
                TextBuilder.sendEmbedMessage("F\u00fcr Umfrage abstimmen", "Oh, etwas ist schiefgelaufen. :sob: Warscheinlich hast du dich im Namen geirrt.",
                        TextBuilder.randomColor(), channel);
            }
        }
        else {
            TextBuilder.sendEmbedMessage("F\u00fcr Umfrage abstimmen", "Um f\u00fcr eine Umfrage abzustimmen gib `!vote [Umfragename] [Antwort]` ein. " +
                    "Falls du die Antwort-M\u00f6glichkeiten nicht kennst, gib `!vote [Umfragename]` ein, und ich sage dir welche Antwort-M\u00f6glichkeiten es gibt. :wink:",
                    TextBuilder.randomColor(), channel);
        }
    }

    private void survey(TextChannel channel, String[] args, Member author) {
        if(args.length >= 2) {
            String name = args[1];
            name = name.replace("-", " ");
            Survey survey = SurveyManager.getSurvey(name);
            ArrayList<AnswerOption> answerOptions = survey.getAnswerOptions();

            StringBuilder sb = new StringBuilder();
            for(AnswerOption answerOption : answerOptions) {
                sb.append("- ").append(answerOption.getName()).append("\n");
            }

            TextBuilder.sendEmbedMessage("Umfrage", "Informationen \u00fcber eine Umfrage:", "Umfrage am " + survey.getDateCreated().toString() + " von " +
                    survey.getCreator().getUser().getName() + " erstellt.", TextBuilder.randomColor(), channel,
                    new MessageEmbed.Field("Name", survey.getName(), false), new MessageEmbed.Field("Antwort-M\u00f6glichkeiten", sb.toString(), false));
        }
        else {
            TextBuilder.sendEmbedMessage("Umfrage", "Um mehr \u00fcber eine Umfrage zu erfahren gib `!survey [Umfragename]` ein.",
                    TextBuilder.randomColor(), channel);
        }
    }

    private void help(TextChannel channel, String[] args, Member author) {
        TextBuilder.sendEmbedMessage("Hilfe", "Brauchst du hilfe?", "", TextBuilder.randomColor(), channel,
                new MessageEmbed.Field("Commands", "- !help\n - !add\n - !remove\n - !vote\n - !survey\n - !finish", false));
    }

    private void finish(TextChannel channel, String[] args, Member author) {
        if(args.length >= 2) {
            String name = args[1];
            name = name.replace("-", " ");

            Survey survey = SurveyManager.getSurvey(name);

            if(!survey.getCreator().getId().equals(author.getId())) {
                TextBuilder.sendEmbedMessage("Umfrage beenden", "Du kannst keine Umfragen beenden die dir nicht geh\u00f6ren!", TextBuilder.randomColor(), channel);
            }

            StringBuilder sbAnswers = new StringBuilder();
            StringBuilder sbVoter = new StringBuilder();
            for(AnswerOption answerOption : survey.getAnswerOptions()) {
                sbAnswers.append("- ").append(answerOption.getName()).append("\n");

                sbVoter.append("- ").append(answerOption.getName()).append(" => ");
                for(Member member : answerOption.getVotedMembers()) {
                    sbVoter.append(member.getUser().getName()).append(", ");
                }
                sbVoter.append("\n");
            }

            TextBuilder.sendEmbedMessage("Umfrage beenden", "Und eine weitere Umfrage geht zu Ende...",
                    "Umfrage am " + survey.getDateCreated().toString() + " von " + survey.getCreator().getUser().getName() + " erstellt.", TextBuilder.randomColor(), channel,
                    new MessageEmbed.Field("Name", survey.getName(), false),
                    new MessageEmbed.Field("Antwort-M\u00f6glichkeiten", sbAnswers.toString(), false),
                    new MessageEmbed.Field("Benutzer die Abgestimmt haben", sbVoter.toString(), false));

            SurveyManager.removeSurvey(survey);
        }
        else {
            TextBuilder.sendEmbedMessage("Umfrage beenden", "Um eine Umfrage zu beenden, gib `!finish [Umfragename]` ein.", TextBuilder.randomColor(), channel);
        }
    }

    public enum CommandOptions {

        ADD(Commands.INSTANCE.add),
        REMOVE(Commands.INSTANCE.remove),
        VOTE(Commands.INSTANCE.vote),
        SURVEY(Commands.INSTANCE.survey),
        HELP(Commands.INSTANCE.help),
        FINISH(Commands.INSTANCE.finish);

        CommandFunctions function;

        CommandOptions(CommandFunctions test) {
            this.function = test;
        }
    }

    interface CommandFunctions {
        void func(TextChannel channel, String[] args, Member author);
    }
}
