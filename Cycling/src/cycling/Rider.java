import java.io.Serializable;
import java.util.ArrayList;

public class Rider implements Serializable{
    private static int ID =1;
    String name;
    int yearOfBirth;
    private int riderID;
    private int teamID;

    protected int getRiderId() {
        return riderID;
    }
    protected int getTeamId() {
        return teamID;
    }
    protected Rider(String name, int yearOfBirth, int teamID) throws IllegalArgumentException {
        
        // check if the name is null, empty, or if yearOfBirth is illegal
        if (name == null || yearOfBirth < 1900 || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        riderID = ID;
        this.teamID = teamID;
        ID++;
    }
  
    //checks if riderId exists in the Arraylist riders provided in the parameters
    protected static boolean checkID (int riderId, ArrayList<Rider>riders ) {
        boolean found = false;
        for (Rider rider : riders) {
            if (rider.riderID == riderId) { 
                found = true;
                break;
            }
        }
        return found;
    }
}