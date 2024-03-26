import java.time.LocalTime;
import java.util.ArrayList;
import java.io.Serializable;
import java.time.Duration;

public class Race implements Serializable {
    private static int ID = 1;
    private int objectID;
    private String name;
    private String description;
    protected ArrayList<Integer> riderIdsInRace;
    
    
    protected int getObjectID() {
        return objectID;
    }
    protected String getName() {
        return name;
    }
    //Constructor to create Races
    //Checks if a race with the same name already exists or is invalid
    //If all good creates a race with a unique name, description and ID 
    protected Race(String name, String description, ArrayList<Race> races) throws IllegalNameException,InvalidNameException {
        //if no races are present just create the race
        if (races.size() == 0) {
            this.name = name;
            this.description = description;
            objectID = ID;
            ID++;
            riderIdsInRace = new ArrayList<>();
        }
        //check if race name is already used
        else if (Race.checkRaceName(name, races)) {
            throw new IllegalNameException();
        } 
        //check if the name is legal
        else if (name == null || name.length() > 30 || name.contains(" ")) {
            throw new InvalidNameException();
        } else {
            this.name = name;
            this.description = description;
            objectID = ID;
            ID++;
            riderIdsInRace = new ArrayList<>();
        }
    }


    // static; checks if the raceId of the race exists in an Array List of races
    //provided in the second parameter
    protected static boolean checkID (int raceId, ArrayList<Race> races) {
        boolean found = false;
        for (Race race : races) {
            if (race.objectID == raceId) { 
                found = true;
                break;
            }
        }
        return found;
    }

    // static; returns a formated string about the race
    protected static String getRaceDetails(int raceId, ArrayList<Race> races, ArrayList<Stage> stages) {
        String name = null;
        String description = null;
        int numStages = 0;
        double totalLength = 0;

        //loop over races and find the needed race
        for (Race race : races) {
            if (race.objectID == raceId) {
                //store its details
                name = race.name;
                description = race.description;
				for (Stage stage : stages) {
					if (stage.getRaceId() == raceId) {
						numStages++;
						totalLength = totalLength + stage.getLength();
					}
				}
                break;
                
            }
        }
        return "RaceID: " + raceId + "; Name: " + name + "; Description: " + description + "; Number of stages: " + numStages +"; Total Length: " + totalLength; 
    }

    // static; checks if the provided name is free
    protected static boolean checkRaceName(String name, ArrayList<Race> races) {
        boolean found = false;
        // loops  over Array List of races
        for (Race race : races) {
            // checks if the name matches
            if (race.name.equals(name)) {
                found = true;
                break;
            }
        }
        return found;
    }

    //finds the race by a given raceId
    protected static Race findRaceById(ArrayList<Race> races, int raceId) {
        for (Race race : races) {
            if (race.objectID == raceId) {
                return race;
            }
        }
        return null;
    }
}
