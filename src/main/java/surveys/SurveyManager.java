package surveys;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import util.TextBuilder;

import java.util.ArrayList;

public class SurveyManager {

    private static ArrayList<Survey> surveys = new ArrayList<>();

    public static Survey getSurvey(String name) {
        for(Survey survey : surveys) {
            if(survey.getName().equals(name)) {
                return survey;
            }
        }

        return null;
    }

    public static void addSurvey(Survey newSurvey, TextChannel channel) {
        int creatorsSurveysCount = 1;
        for(Survey s : surveys) {
            if(s.getName().equals(newSurvey.getName())) {
                TextBuilder.sendEmbedMessage("Umfrage erstellen", "Es existiert schon eine Umfrage mit dem Namen `" + newSurvey.getName() + "`. " +
                        "Schau nochmal ob du dich nicht verschrieben hast. :wink:", TextBuilder.randomColor(), channel);
                return;
            }
            if(s.getCreator().getId().equals(newSurvey.getCreator().getId())) {
                creatorsSurveysCount++;
            }
        }

        if(creatorsSurveysCount > 5) {
            TextBuilder.sendEmbedMessage("Umfrage erstellen", newSurvey.getCreator().getAsMention() + " Du kannst nur maximal 5 Umfragen haben!",
                    TextBuilder.randomColor(), channel);

            return;
        }

        surveys.add(newSurvey);

        TextBuilder.sendEmbedMessage("Umfrage erstellt!", "Deine Umfrage `" + newSurvey.getName() + "` wurde erstellt! :partying_face:", TextBuilder.randomColor(), channel);
    }

    public static boolean removeSurvey(String name, Member creator) {
        for(Survey s : surveys) {
            if(s.isThisSurvey(name, creator)) {
                surveys.remove(s);
                return true;
            }
        }

        return false;
    }

    public static void removeSurvey(Survey survey) {
        surveys.remove(survey);
    }

    public static ArrayList<AnswerOption> getAnswersForSurvey(String name, Member creator) {
        for(Survey s : surveys) {
            if(s.isThisSurvey(name, creator)) {
                return s.getAnswerOptions();
            }
        }

        return null;
    }

    public static int vote(String name, String answerOption, Member voter, TextChannel channel) {
        for(Survey s : surveys) {
            if(s.getName().equals(name)) {
                ArrayList<AnswerOption> answers = s.getAnswerOptions();
                for(int i = 0; i < answers.size(); i++) {
                    if(answers.get(i).getName().equalsIgnoreCase(answerOption)) {
                        if(answers.get(i).getVotedMembers().contains(voter)) {
                            TextBuilder.sendEmbedMessage("F\u00fcr Umfrage abstimmen", voter.getAsMention() + " Du kannst nicht zweimal f\u00fcr eine Umfrage abstimmen!",
                                    TextBuilder.randomColor(), channel);

                            return -1;
                        }
                        else {
                            s.addVote(voter, i);
                            return 1;
                        }
                    }
                }
            }
        }

        return 0;
    }

}
