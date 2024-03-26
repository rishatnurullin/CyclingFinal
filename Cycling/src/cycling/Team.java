import java.io.Serializable;
import java.util.ArrayList;
public class Team implements Serializable{
    private static int ID = 1;
    private int teamID;
    private String name;
    private String description;

    protected int getTeamId() {
        return teamID;
    }
    protected String getName() {
        return name;
    }
    protected String getDescription() {
        return description;
    }
    protected Team(String name, String description, ArrayList<Team> teams) throws IllegalNameException,InvalidNameException {
        //check if team name is used
        if (Team.checkTeamName(name, teams)) {
            throw new IllegalNameException();
        } 
        //check if team name is null, longert hat 30 characters, contains white space, is empty
        else if (name == null || name.length() > 30 || name.contains(" ") || name.isEmpty()) {
            throw new InvalidNameException();
        }
        this.name = name;
        this.description = description;
        this.teamID = ID;
        ID++;
    }

    //checks if the name is already used
    protected static boolean checkTeamName(String name, ArrayList<Team> teams) {
        boolean nameTaken = false;
        for (Team team : teams) {
            if (team.name.equals(name)) {
                nameTaken = true;
                break;
            }
        }
        return nameTaken;
    }


    //checks if teamId exists
    protected static boolean checkID (int teamId, ArrayList<Team> teams) {
        boolean found = false;
        for (Team team : teams) {
            if (team.teamID == teamId) { 
                found = true;
                break;
            }
        }
        return found;
    }
}
