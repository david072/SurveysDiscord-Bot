package surveys;

import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;

public class AnswerOption {

    private String name;
    private ArrayList<Member> votedMembers = new ArrayList<>();

    public AnswerOption(String name) {
        this.name = name;
    }

    public void addVote(Member voter) {
        votedMembers.add(voter);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Member> getVotedMembers() {
        return votedMembers;
    }

}
