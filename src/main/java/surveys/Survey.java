package surveys;

import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.Date;

public class Survey {

    private Member creator;
    private Date dateCreated;
    private String name;
    private ArrayList<AnswerOption> answerOptions = new ArrayList<>();

    public Survey(Member creator, Date dateCreated, String name) {
        this.creator = creator;
        this.dateCreated = dateCreated;
        this.name = name;
    }

    public boolean isThisSurvey(String name, Member creator) {
        if(this.name.equals(name) && this.getCreator().getId().equals(creator.getId())) {
            return true;
        }
        else {
            return false;
        }
    }

    public void addVote(Member voter, int answerIndex) {
        answerOptions.get(answerIndex).addVote(voter);
    }

    public Member getCreator() {
        return creator;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getName() {
        return name;
    }

    public ArrayList<AnswerOption> getAnswerOptions() {
        return answerOptions;
    }
    public void setAnswers(ArrayList<AnswerOption> answers) { this.answerOptions = answers; }

}
