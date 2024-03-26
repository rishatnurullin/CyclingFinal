import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Stage implements Serializable{
    private static int ID = 1;
    private int stageID;
    private int raceID;
    private String name; 
    private String description;
    private double length;
    private LocalDateTime startTime;
    private StageType type;
    private String stageState = "initialized";
    ArrayList<Integer> riderIdsInStage; 

    protected int getStageId() {
        return stageID;
    }
    protected int getRaceId() {
        return raceID;
    }
    protected double getLength() {
        return length;
    }
    protected String getState() {
        return stageState; 
    }
    protected StageType getStageType() {
        return type;
    }
    protected Stage(int raceID, String name, String description, double length, LocalDateTime startTime, StageType type, ArrayList<Race> races, ArrayList<Stage> stages) throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException{
        //check if the stage name is used
        if (Stage.checkStageName(name, stages)) {
            throw new IllegalNameException();
        } 
        //check if name is null, longer that 30,empty or with white spaces
        else if (name == null || name.length() > 30 || name.contains(" ") || name.isEmpty()) {
            throw new InvalidNameException();
        } else if (length < 5) {
            throw new InvalidLengthException();
        } else {
            this.name = name;
            this.description = description;
            this.length = length;
            this.startTime = startTime;
            this.type = type;
            this.raceID = raceID;
            stageID = ID;
            ID++;
            riderIdsInStage = new ArrayList<>();
        } 
    }
    /*checks if the stageId exists in the ArrayList stages provided as
     * an argument
     */
    protected static boolean checkID (int stageId, ArrayList<Stage>stages ) {
        boolean found = false;
        for (Stage stage : stages) {
            if (stage.stageID == stageId) { 
                found = true;
                break;
            }
        }
        return found;
    }
    /*Checks if the name provided is already being used in an ArrayList stages
     * provided in the parameters
     */
    protected static boolean checkStageName(String name, ArrayList<Stage> stages) {
        for (Stage stage : stages) {
            if (stage.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    protected static int[] getStagesInOrder(ArrayList<Stage> stages, int raceId) {
        //get the stages of the race by only selecting stages with the raceId 
        ArrayList<Stage> stagesInRace = new ArrayList<Stage>();
        for (int j = 0; j < stages.size(); j++) {
            if (stages.get(j).raceID == raceId) {
                stagesInRace.add(stages.get(j));
            }
        }
        // sort the stages based on startTime using a comparator
        Collections.sort(stagesInRace, Comparator.comparing(Stage::getStartTime));

        //return the ids in order 
        int[] stageIDs = new int[stagesInRace.size()];
        for (int i = 0; i < stagesInRace.size(); i++) {
            stageIDs[i] = stagesInRace.get(i).stageID;
    }
        return stageIDs;
    }
    
    protected void setState(String state) {
        this.stageState = state;

    }
    /*Gets the state of the of the stage based on its stageId
     * and the ArrayList of stages provided
     */
    protected static String getState(ArrayList<Stage> stages, int stageId) {
        String state = null; 
        for (Stage stage : stages) {
            if (stage.stageID == stageId) {
                state = stage.stageState;
                break;
            }
        }
        return state;
    }
    //returns the StageType of a using its stageId and the ArrayList stages provided
    protected static StageType getStageType(int stageId, ArrayList<Stage> stages) {
        StageType typeOfStage = null; 
        for (Stage stage : stages) {
            if (stage.stageID == stageId) {
                typeOfStage = stage.type;
                break;
            }
        }
        return typeOfStage;
    }
    protected LocalDateTime getStartTime() {
        return startTime;
    }
    /*finds the stage by its stageId amongst stages in the ArrayList
     * stages provided in the parameters
     */
    protected static Stage findStage(int stageId, ArrayList<Stage>stages) {
        for (Stage stage : stages) {
            if (stage.stageID == stageId) {
                return stage;
            }
        }
        return null;
    }
 }
