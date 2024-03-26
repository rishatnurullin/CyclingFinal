//package cycling;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.Duration;
import java.util.Collections;
//import javax.naming.InvalidNameException;
import java.util.HashMap;
import java.util.Map;
import java.io.*;


public class CyclingPortalImpl implements Serializable, CyclingPortal{
  //these ArrayLists store riders, teams, races, stages, checkpoints and results
  //of a CyclingPortalIml instance
  private ArrayList<Rider> riders;
  private ArrayList<Team> teams;
  private ArrayList<Race> races;
  private ArrayList<Stage> stages;
  private ArrayList<Checkpoint> checkpoints;
  private ArrayList<Result> results;

  // Constructor of CyclingPortalImpl
  public CyclingPortalImpl() {
    // initializing empty ArrayLists
    this.races = new ArrayList<Race>();
    this.teams = new ArrayList<Team>();
    this.stages = new ArrayList<Stage>();
    this.riders = new ArrayList<Rider>();
	  this.checkpoints = new ArrayList<Checkpoint>();
    this.results = new ArrayList<Result>();
  }

  public int[] getRaceIds() {
    // checks if the races ArrayList in CyclingPortalIml is empty
    if (races.size() == 0) {
      return new int[0];
    }
    //LOOP OVER RACES ARRAYLIST in CyclingPortaIml
    int[] raceIDArray = new int[races.size()];
    for (int i = 0; i < races.size(); i++) {
      raceIDArray[i] = races.get(i).getObjectID();
    }
    return raceIDArray;
  }

  public int createRace(String name, String description) throws InvalidNameException, IllegalNameException {
      //call Race constuctor with the name, description and the races instance field as arguments
      Race race = new Race(name, description, races);
      //add new race to races ArrayList instance field in CyclingPortalIml
      races.add(race);
      //return its raceId
      return race.getObjectID();
  }

  public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
    //check if raceId exists using static method checkID() of the Race class
    if (!Race.checkID(raceId, races)) {
      throw new IDNotRecognisedException("Race ID inputted does not exist");
    }
    //getRaceDetails is a static method of Race and it constructs the string
    //containing info about the race
    //its takes, raceId, races ArrayList and stages ArrayList (both instance fields)
    //as arguments
    String answer = Race.getRaceDetails(raceId, races, stages);
    return answer;
  }

  public void removeRaceById(int raceId) throws IDNotRecognisedException {
    // check of raceId exists
    if (Race.checkID(raceId, races)) {

      //remove stages with the raceId from stages instance field
      stages.removeIf(stage -> stage.getRaceId() == raceId);

      //Remove checkpoints with the raceId from checkpoints instance field
      checkpoints.removeIf(checkpoint -> checkpoint.getRaceId() == raceId);

      //Remove results with the raceId from results instance field
      results.removeIf(result -> result.getRaceId() == raceId);

      // remove the race with the raceId from races instance field
      races.removeIf(race -> race.getObjectID() == raceId);
    } else {
      throw new IDNotRecognisedException("Race ID inputted does not exist");
    }
  }

  public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
    //check the validity of the raceId
    if (!Race.checkID(raceId, races)) {
      throw new IDNotRecognisedException("Race ID inputted does not exist");
    }
    //loop over stages ArrayList and count stages with the given raceId
    int count = 0;
    for (Stage stage : stages) {
        if (stage.getRaceId() == raceId) {
            count++;
        }
    }
    return count;
  }

  public int addStageToRace(
    int raceId,
    String stageName,
    String description,
    double length,
    LocalDateTime startTime,
    StageType type
  )
    throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {

      // check the validity of the raceId using a static method checkID
      if (!Race.checkID(raceId, races)) {
        throw new IDNotRecognisedException();
      } 
      //create a new stage using the constructor
      //amongst other, it takes races and stages instance fields of CyclingPortalIml
      //object as inputs
      Stage stage = new Stage(
        raceId,
        stageName,
        description,
        length,
        startTime,
        type,
        races,
        stages
      );
      // add the new stage object to stages instance field
      stages.add(stage);
      //return the stageId
      return stage.getStageId();
  }


  public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
    // check if raceId exists
    if (!Race.checkID(raceId, races)) {
      throw new IDNotRecognisedException();
    }
    /* getStagesInOrder takes stages ArrayList, int raceId as parameters 
     * It returns a sorted array of stageIds
    */
    return Stage.getStagesInOrder(stages, raceId);
  }

  public double getStageLength(int stageId) throws IDNotRecognisedException {
    for (Stage stage : stages) {
      if (stage.getStageId() == stageId) {
          return stage.getLength();
      }
    }
    throw new IDNotRecognisedException();
  }

  public void removeStageById(int stageId) throws IDNotRecognisedException {
    // Check if stageId exists
    if (!Stage.checkID(stageId, stages)) {
      throw new IDNotRecognisedException();
    }

    // Remove checkpoints of the stage from checkpoints instance field
    checkpoints.removeIf(checkpoint -> checkpoint.getStageId() == stageId);

    // Remove results of the stage from results instance field
    results.removeIf(result -> result.getStageId() == stageId);

    // Remove the stage from stages instance field
    stages.removeIf(stage -> stage.getStageId() == stageId);
  }

  public int addCategorizedClimbToStage(
    int stageId,
    Double location,
    CheckpointType type,
    Double averageGradient,
    Double length
  )
    throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
      //create the checkpoint using Checkpoint class Constructor
      Checkpoint climbCheckpoint = new Checkpoint(stageId, location, averageGradient, length, type, stages);
      //add it to checkpoints instance field
      checkpoints.add(climbCheckpoint);
      //return checkpointId
      return climbCheckpoint.getCheckpointId();

	}

  public int addIntermediateSprintToStage(int stageId, double location)
    throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
      //create the intermediate sprint
      //averageGradient and length are set to 0 by default
      Checkpoint checkpoint = new Checkpoint(stageId, location, 0, 0, CheckpointType.SPRINT, stages);
      //add it to checkpoints
      checkpoints.add(checkpoint);
      //returns its checkpointId
      return checkpoint.getCheckpointId();
	}

  public void removeCheckpoint(int checkpointId)
    throws IDNotRecognisedException, InvalidStageStateException {
      //check if checkpointId exists
      if (!Checkpoint.checkID(checkpointId, checkpoints)) {
        throw new IDNotRecognisedException();
      }
      //retriving the state of the stage
      String state = null;
      for (Checkpoint checkpoint : checkpoints) {
        if (checkpoint.getCheckpointId() == checkpointId) {
          state = Stage.getState(stages, checkpoint.getStageId());
          if (state.equals("waiting for results")) {
            throw new InvalidStageStateException();
          } else {
            // remove the checkpoint from checkpoints ArrayList
            checkpoints.remove(checkpoint);
          }
          break;
        }
      }
	}

  public void concludeStagePreparation(int stageId)
    throws IDNotRecognisedException, InvalidStageStateException {

      // Check if stageId exists
      if (!Stage.checkID(stageId, stages)) {
          throw new IDNotRecognisedException();
      }

      // Check if the state of the stage is eligible
      String stageState = Stage.getState(stages, stageId);
      if ("waiting for results".equals(stageState)) {
          throw new InvalidStageStateException();
      }

      // Change the state of the stage
      for (Stage stage : stages) {
          if (stage.getStageId() == stageId) {
              stage.setState("waiting for results");
              break;
          }
        }
      }
  
  public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {

    //check if stageId exists
    if (!Stage.checkID(stageId, stages)) {
      throw new IDNotRecognisedException();
    }
    // use static method getCheckpointsInOrder to filter and return an array of checkpointIds
    return Checkpoint.getCheckpointsInOrder(checkpoints, stageId);

    }

  public int createTeam(String name, String description)
    throws IllegalNameException, InvalidNameException {
      //call Team constructor. arrayList teams is one of its parameters
      Team team = new Team(name, description, teams);
      // add team to teams
      teams.add(team);
      //returns its teamId
      return team.getTeamId();
  }

  public void removeTeam(int teamId) throws IDNotRecognisedException {
    // Check if teamId exists
    if (!Team.checkID(teamId, teams)) {
      throw new IDNotRecognisedException();
    }
    //remove team with the teamId from the teams instance field
    teams.removeIf(team -> team.getTeamId() == teamId);
  }

  public int[] getTeams() {
    int[] teamIDArray = new int[teams.size()];
    // loop over teams and store each teamID in teamIDArray
    for (int i = 0; i < teams.size(); i++) {
      teamIDArray[i] = teams.get(i).getTeamId();
    }
    return teamIDArray;
  }

  public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
    //check if the teamId provided is valid
    if (!Team.checkID(teamId, teams)) {
      throw new IDNotRecognisedException();
    }

    //listOfRiderID contains the ids of riders in this team
    ArrayList<Integer> listOfRiderID = new ArrayList<Integer>();
    for (Rider rider : riders) {
      if (rider.getTeamId() == teamId) {
        listOfRiderID.add(rider.getRiderId());
      }
    }
    //covert the array list into a int[] 
    return listOfRiderID.stream().mapToInt(Integer::intValue).toArray();
  }

  public int createRider(int teamID, String name, int yearOfBirth)
    throws IDNotRecognisedException, IllegalArgumentException {
      //check if the teamID is valid using a static checkID method
      if (!Team.checkID(teamID, teams)) {
        throw new IDNotRecognisedException(); 
    } 
      // call the Rider constructor
      Rider rider = new Rider(name, yearOfBirth, teamID);
      // add the new rider to riders
      riders.add(rider);
      //return its riderId
      return rider.getRiderId();
  }

  public void removeRider(int riderId) throws IDNotRecognisedException {
    // Check if riderId exists
    if (!Rider.checkID(riderId, riders)) {
        throw new IDNotRecognisedException();
    }

    // Remove the rider from the riders instance field
    riders.removeIf(rider -> rider.getRiderId() == riderId);

    // Remove the results associated with the rider from the results instance fields
    results.removeIf(result -> result.getRiderId() == riderId);

    //updating the ArrayList of riderIds of the race by looping over the races
    //and removing the riderId from the riderIdsInRace instance field
    for (Race race : races) {
      race.riderIdsInRace.removeIf(riderID -> riderID == riderId);
    }
    //updating the ArrayList of riderIds of the race by looping over the stages
    //and removing the riderId from the riderIdsInStage instance field
    for (Stage stage : stages) {
      stage.riderIdsInStage.removeIf(riderID -> riderID == riderId);
    }
}

  public void registerRiderResultsInStage(
    int stageId,
    int riderId,
    LocalTime... checkpointTimes
  )
    throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException, InvalidStageStateException {

      // Check if stageId and riderId exist
      if (!Stage.checkID(stageId, stages) || !Rider.checkID(riderId, riders)) {
        throw new IDNotRecognisedException();
      }

      // Check if the result already exists by looping over the results
      //and seeing if riderId and stageId match.
      for (Result result : results) {
          if (result.getRiderId() == riderId && result.getStageId() == stageId) {
              throw new DuplicatedResultException();
          }
      }
      /*getState retrives the stage of a stage with stageID */
      if (!Stage.getState(stages, stageId).equals("waiting for results")) {
        throw new InvalidStageStateException();
      }
      int countOfCheckpoints = this.getStageCheckpoints(stageId).length;
      
      // Check if checkpointTimes has the correct length
      if (checkpointTimes.length != countOfCheckpoints + 2) {
        throw new InvalidCheckpointTimesException();
      }

      // create a new result object
      Result result = new Result(stageId, riderId, checkpointTimes, stages);
      //add it to the results instance field of CyclingPortalIml object
      results.add(result);
      

      //ADD THE riderId TO riderIdsinStage of the STAGE with the stageId
      // FND THE RACEID OF THE RACE, IN WHICH THE STAGE IS LOCATED
      int raceId = 0;
      for(Stage stage : stages) {
        if (stage.getStageId() == stageId) {
          stage.riderIdsInStage.add(riderId);
          raceId = stage.getRaceId();
        }
      }
      /////////////////////////////////////////////////////////////////////////
      

      /* ADD THE riderId IF IT IS NOT THERE ALREADY 
       * we do so by checking if riderIdsInRace instacne field already contains
       * the riderId or not
       * if not we add it
       */
      for (Race race : races) {
        if (race.getObjectID() == raceId) {
          if (!race.riderIdsInRace.contains(riderId)) {
            race.riderIdsInRace.add(riderId);
          }  
        }
      }
    
      ////////////////////////////////////////////////////////////////////
    }

  public LocalTime[] getRiderResultsInStage(int stageId, int riderId)
    throws IDNotRecognisedException {
      // check if stageId and riderId exist
      if (!Stage.checkID(stageId, stages) || !Rider.checkID(riderId, riders)) {
        throw new IDNotRecognisedException();
      } else {
        /*Loop over results
         * find the elapsed time of the rider 
         * find the checkpoint times of the rider
         * put it together into an array 
         * return the array
         */
        for (Result result : results) {
          //find the needed result
          if (result.getRiderId() == riderId & result.getStageId() == stageId) {
            int length = result.getCheckpointTimes().length;
            LocalTime[] arrayOfTimes = new LocalTime[length - 1];
            // calculate the duration between start and finish times
            Duration totalDuration = Duration.between(result.getCheckpointTimes()[0],result.getCheckpointTimes()[length-1]);
            // convert it into a LocalTime variable
            LocalTime totalFinalTime = LocalTime.of((int) totalDuration.toHours(), totalDuration.toMinutesPart(), totalDuration.toSecondsPart(), totalDuration.toNanosPart());
            //store it as the alst element
            arrayOfTimes[arrayOfTimes.length-1] = totalFinalTime;
            //copy all the checkpoint times to arrayOfTimes
            for (int i = 1; i < result.getCheckpointTimes().length-1; i++) {
              arrayOfTimes[i-1] = result.getCheckpointTimes()[i];
            }
            return arrayOfTimes;
          }
        }
        // output an empty array if no result is found
        return new LocalTime[0];
      }
    }

  public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId)
    throws IDNotRecognisedException {
      //check if the stageId and riderId are valid using static methods of Stage and Rider
      if (!Stage.checkID(stageId, stages) || !Rider.checkID(riderId, riders)) {
        throw new IDNotRecognisedException();
      }
      
      ArrayList<LocalTime> sortedResultsForStage = new ArrayList<LocalTime>();
      //store riderIds who participated in  the stage
      int [] sortedRiderIdsInStage = this.getRidersRankInStage(stageId);

      //CHECK IF THE RIDER HAD A RESULT FOR THIS STAGE
      //////////////////////////////////////////////////////////
      boolean registered = false;
      for (int id : sortedRiderIdsInStage) {
        if (id == riderId) {
          registered = true;
        }
      }
      //returning null if there is no result for the rider in the stage
      if (registered == false) {
        return null;
      }
      ///////////////////////////////////////////////////////////////////

      int indexOfRiderId = 0;

      //////////////////////////////////////////////////////////////////

      // RETRIVE THE ELPASED TIME OF THE RIDERS IN STAGE
      /*
       * loop over sortedRiderIdsInStage
       * store the elapsed time of each rider
       */
      for (int i = 0; i < sortedRiderIdsInStage.length; i++) {
        LocalTime[] times = this.getRiderResultsInStage(stageId,sortedRiderIdsInStage[i]);
        int length = times.length;
        LocalTime elapsedTime = times[length -1];
        sortedResultsForStage.add(elapsedTime);
        if (sortedRiderIdsInStage[i] == riderId) {
          indexOfRiderId = i;
        }
      }
      ///////////////////////////////////////////////////////////

      /*CALCULATE THE ADJUSTED ELAPSED TIME OF THE RIDER
       * done by comparing the elapsed time of the rider with the elapsed times
       * of the riders in front of him 
       * if they are less that 1s apart -> their elapsed time becomes the adjustted elapsed time
       * of the rider
       */
      LocalTime adjustedElapsedTime = sortedResultsForStage.get(indexOfRiderId);
      for (int i = -1; i > (indexOfRiderId*-1)-1;i--) {
        Duration duration = Duration.between(sortedResultsForStage.get(indexOfRiderId+i), adjustedElapsedTime);
        if (duration.getSeconds() < 1) {
          adjustedElapsedTime = sortedResultsForStage.get(indexOfRiderId+i);
        } else {
          break;
        }
      }
      //////////////////////////////////////////////////////////////
      return adjustedElapsedTime;
    }

  public void deleteRiderResultsInStage(int stageId, int riderId)
    throws IDNotRecognisedException {

      //check if stageId and riderId exist
      if (!Stage.checkID(stageId, stages) || !Rider.checkID(riderId, riders)) {
        throw new IDNotRecognisedException();
    }
      // Remove results for the specified rider and stage from the results instance fields
      results.removeIf(result -> result.getRiderId() == riderId && result.getStageId() == stageId);


      // GET THE RACEID of the stage
      int raceId = 0;
      for (Stage stage : stages) {
        if (stage.getStageId() == stageId) {
          raceId = stage.getRaceId();
        }
      }
      ////////////////////////////////////////////////

      // DETERMINE IF WE NEED TO DELETE THE RIDER ID FROM riderIdsInRace
      /*this is needed to account for the fact that a rider might be present in one stage
       * of a race. And not in the other
       * although using a system as such would be incorrect
       * we still account for this case
       */
      boolean remove = true; 
      for (Stage stage : stages) {
        if (stage.getRaceId() == raceId & stage.getStageId() != stageId) {
          if (stage.riderIdsInStage.contains(riderId)) {
            remove  = false;
          }
        }
      }
      ////////////////////////////////////////////////////////

      //REMOVE IF NEEDED
      if (remove) {
        for(Race race : races) {
          if (race.getObjectID() == raceId) {
            race.riderIdsInRace.removeIf(riderID -> riderID == riderId);
          }
        }
      }
      ////////////////////////////////////////////
    }

  public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
    //check if stageId exists in the system
    if (!Stage.checkID(stageId, stages)) {
      throw new IDNotRecognisedException();
    }

    //ridersInStage stores the riderIds of the riders in the stage
    ArrayList<Integer> ridersInStage = new ArrayList<Integer>();
    // their elapsed time in the same order as ridersInStage
    ArrayList<LocalTime> timeElapsedOfRiders = new ArrayList<LocalTime>();

    //LOOP OVER results AND ADD RIDERIDS AND ELAPSED TIMES TO ridersInStage AND timeElapsedOfRiders
    for (Result result :results) {
      if (result.getStageId() == stageId) {
        ridersInStage.add(result.getRiderId());
        int length = this.getRiderResultsInStage(stageId, result.getRiderId()).length;
        LocalTime timeElapsed = this.getRiderResultsInStage(stageId, result.getRiderId())[length-1];
        timeElapsedOfRiders.add(timeElapsed);
      }
    }
    //////////////////////////////////////////////////
    if (timeElapsedOfRiders.isEmpty()) {
      return new int[0];
    }
    //////////////////////////////////////////////////////
    //SORT timeElapsedOfRiders AND riderIdsInStage USING BUBBLE SORT
    boolean sorted = false;
    while (sorted == false) {
      sorted = true;
      for (int i = 0; i < ridersInStage.size()-1; i++){
        if (timeElapsedOfRiders.get(i).isAfter(timeElapsedOfRiders.get(i+1))) {
          sorted = false;
          LocalTime temp = timeElapsedOfRiders.get(i);
          timeElapsedOfRiders.set(i,timeElapsedOfRiders.get(i+1));
          timeElapsedOfRiders.set(i+1,temp);

          int tempIndex = ridersInStage.get(i);
          ridersInStage.set(i, ridersInStage.get(i+1));
          ridersInStage.set(i+1, tempIndex);
        }
      }
    } 
    ////////////////////////////////////////////////////////////////

    //CONVERT ridersInStage into an array
    int[] finalArray = ridersInStage.stream().mapToInt(Integer::intValue).toArray();
    return finalArray;
  }

  public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId)
    throws IDNotRecognisedException{
      //check if the stageId exists
      if (!Stage.checkID(stageId, stages)) {
        throw new IDNotRecognisedException();
      } else {
        //check how many results are registered for the stage
        int count = 0;
        for (Result result : results) {
          if (result.getStageId() == stageId) {
            count++;
          } 
        }
        ////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////
        if (count != 0) {
          //ridersInStageRanked stores the riders ranked by elapsed time
          int[] ridersInStageRanked = this.getRidersRankInStage(stageId);

          //here we retrieve adjusted elapsed times of the riders in order
          LocalTime[] rankedAdjustedTimes = new LocalTime[ridersInStageRanked.length];
          for (int i = 0; i < ridersInStageRanked.length; i++) {
            int riderId = ridersInStageRanked[i];
            rankedAdjustedTimes[i] = this.getRiderAdjustedElapsedTimeInStage(stageId, riderId);
          }
          //returning the array
          return rankedAdjustedTimes;
        }
        return new LocalTime[0];
      }
    }

  public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException{
    //check if the stageId exists
    if (!Stage.checkID(stageId, stages)) {
        throw new IDNotRecognisedException("Stage ID not recognized.");
    }
    
    
    // point distributions for differnt stages
    Map<StageType, int[]> pointsDistributionMap = new HashMap<>();
    pointsDistributionMap.put(StageType.FLAT, new int[]{50,30,20,18,16,14,12,10,8,7,6,5,4,3,2});
    pointsDistributionMap.put(StageType.HIGH_MOUNTAIN, new int[]{20,17,15,13,11,10,9,8,7,6,5,4,3,2,1});
    pointsDistributionMap.put(StageType.MEDIUM_MOUNTAIN, new int[]{30,25,22,19,17,15,13,11,9,7,6,5,4,3,2});
    pointsDistributionMap.put(StageType.TT, new int[]{20,17,15,13,11,10,9,8,7,6,5,4,3,2,1});
    int[] sprintPoints = {20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};
    //////////////////////////////////////////////////////////


    //rankedRiderIds stores the riderIds ranked by their elapsed time
    int[] rankedRiderIds = this.getRidersRankInStage(stageId);

  
    //this array will store the final points for the stage 
    int[] finalPointsArray = new int[rankedRiderIds.length];
    Arrays.fill(finalPointsArray, 0); // Initialize points to 0
    
    // Assign points based on stage type
    StageType stageType = Stage.getStageType(stageId, stages);
    int[] pointDistribution = pointsDistributionMap.getOrDefault(stageType, new int[0]);
    
  

    // Assign points to riders based on their rank and the points distribution for the stage
    for (int i = 0; i < rankedRiderIds.length && i < pointDistribution.length; i++) {
        finalPointsArray[i] = pointDistribution[i];
    }
    ////////////////////////////////////////

    //create a cope of rankedRiderIds because we will use it later with the indexOf method
    ArrayList<Integer> rankedRiderIdsInStage = new ArrayList<>();
    for (int i = 0; i < rankedRiderIds.length; i++) {
      rankedRiderIdsInStage.add(rankedRiderIds[i]);
    }
    /////////////////////////////////////////////////

    // Process sprints if applicable
    if (stageType != StageType.TT) { 

      // getStageCheckpoints returns an ordered list of checkpoints IDs for the stage
      int[] checkpointIdsInStage = this.getStageCheckpoints(stageId);
      
      for (int k = 0; k < checkpointIdsInStage.length; k++) {
          

          //check that the checkpoint is a sprint
        if (Checkpoint.getCheckPointType(checkpoints, checkpointIdsInStage[k]) == CheckpointType.SPRINT) {


          //another sorted list of riderIds in the stage
          //STORES THE RANKINGS OF IDS FOR THIS EXACT CHECKPOINT, so far it is the same as rankedRiderIdsInStage
          ArrayList<Integer> sortedIDsforCheckpoint = new ArrayList<>();
          for (int i : rankedRiderIdsInStage) {
            sortedIDsforCheckpoint.add(i);
          }

          //get the times for that checkpoint
          ArrayList<LocalTime> timesForCheckPoint = new ArrayList<>();
          for (Integer riderId : rankedRiderIdsInStage) {
            LocalTime time = this.getRiderResultsInStage(stageId, riderId)[k];
            timesForCheckPoint.add(time);
          }
          //SORTING timesForCheckPoint AND sortedIDsforCheckpointN WITH IT
          boolean sorted = false;
          while (sorted == false) {
            sorted = true;
            for (int i = 0; i < sortedIDsforCheckpoint.size()-1; i++){
              if (timesForCheckPoint.get(i).isAfter(timesForCheckPoint.get(i+1))) {
                sorted = false;
                Collections.swap(timesForCheckPoint, i, i + 1);
                Collections.swap(sortedIDsforCheckpoint, i, i + 1);
              }
            }
            }
          ////////////////////////////

          //ASSIGING THE POINTS FOR THE SPRINT TO THE finalPointsArray
          for (int b = 0; b < sprintPoints.length && b < sortedIDsforCheckpoint.size(); b++) {
            int pointToAdd= sprintPoints[b];
            int indexOfRider = rankedRiderIdsInStage.indexOf(sortedIDsforCheckpoint.get(b));
            finalPointsArray[indexOfRider] += pointToAdd;
          }
          ////////////////////
          }
        }
    }
    return finalPointsArray;
  }

  public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException{
    
    //CHECK IF THE ID IS VALID
    if (!Stage.checkID(stageId, stages)) {
      throw new IDNotRecognisedException();
    }
    ///////////////////////////////////////////

    // SET UP THE POINTS FOR THE DIFFERNT CLIMBS
    Map<CheckpointType, int[]> pointsDistributionMap = new HashMap<>();
    pointsDistributionMap.put(CheckpointType.C4, new int[]{1});
    pointsDistributionMap.put(CheckpointType.C3, new int[]{2,1});
    pointsDistributionMap.put(CheckpointType.C2, new int[]{5,3,2,1});
    pointsDistributionMap.put(CheckpointType.C1, new int[]{10,8,6,4,2,1});
    pointsDistributionMap.put(CheckpointType.HC, new int[]{20,15,12,10,8,6,4,2});
    ///////////////////////////////////////////////////////////////////////////////////
    

    //CREATE AN ARRAYLIST OF RANKED RIDER IDS IN THE STAGE
    ArrayList<Integer> rankedRiderIdsInStage = new ArrayList<>();
    for (int i = 0; i < this.getRidersRankInStage(stageId).length; i++) {
      rankedRiderIdsInStage.add(this.getRidersRankInStage(stageId)[i]);
    }
    /////////////////////////////////////////////////////////////////////

    //CREATE THE ARRAY THAT WILL STORE THE MOUNTAIN POINTS
    int[] finalPointsArray = new int[rankedRiderIdsInStage.size()];
    Arrays.fill(finalPointsArray, 0); // Initialize points to 0
    //////////////////////////////////////////////////////////////////


    //LOOPING OVER THE CHECKPOINTS IN THE STAGE
    int[] checkpointIdsInStage = this.getStageCheckpoints(stageId);
    for (int k = 0; k < checkpointIdsInStage.length; k++) {
      
      //CHECK IF THE CHECKPOINT IS NOT A SPRINT
      if (Checkpoint.getCheckPointType(checkpoints,checkpointIdsInStage[k]) != CheckpointType.SPRINT) {
        //STORES THE RANKINGS OF IDS FOR THIS EXACT CHECKPOINT
        ArrayList<Integer> sortedIDsforCheckpoint = new ArrayList<>();
        for (int i : rankedRiderIdsInStage) {
          sortedIDsforCheckpoint.add(i);
        }

        //GET WHAT TYPE OF CLIMB IT IS
        CheckpointType checkpointType = Checkpoint.getCheckPointType(checkpoints, checkpointIdsInStage[k]);
        int[] pointDistribution = pointsDistributionMap.getOrDefault(checkpointType, new int[0]);
        ///////////////////////////////////////////////////


        //GET THE RAW TIMES FOR THAT CHECKPOINT FOR EACH RIDER. THE TIMES ARE ORDERED BY 
        ArrayList<LocalTime> timesForCheckPoint = new ArrayList<>();
        for (Integer riderId : rankedRiderIdsInStage) {
          LocalTime time = this.getRiderResultsInStage(stageId, riderId)[k];
          timesForCheckPoint.add(time);
        }
        /////////////////////////////////////////////////////////////////////

        //SORT THE timesForCheckPoint ARRAY AND sortedIDsforCheckpoint AT THE SAME TIME
        boolean sorted = false;
        while (sorted == false) {
          sorted = true;
          for (int i = 0; i < sortedIDsforCheckpoint.size()-1; i++){
            if (timesForCheckPoint.get(i).isAfter(timesForCheckPoint.get(i+1))) {
              sorted = false;
              LocalTime temp = timesForCheckPoint.get(i);
              timesForCheckPoint.set(i,timesForCheckPoint.get(i+1));
              timesForCheckPoint.set(i+1,temp);
    
              int tempIndex = sortedIDsforCheckpoint.get(i);
              sortedIDsforCheckpoint.set(i, sortedIDsforCheckpoint.get(i+1));
              sortedIDsforCheckpoint.set(i+1, tempIndex);
            }
          }
          }
          /////////////////////////////////////////////////////////////////////////

          
          for (int b = 0; b < pointDistribution.length && b < sortedIDsforCheckpoint.size(); b++) {
            int pointToAdd = pointDistribution[b];
            int indexOfRider = rankedRiderIdsInStage.indexOf(sortedIDsforCheckpoint.get(b));
            finalPointsArray[indexOfRider] += pointToAdd;
          }
      }
    }
    return finalPointsArray;
    }

	public void removeRaceByName(String name) throws NameNotRecognisedException {
    //check if the name of the race exists in the system
    if (!Race.checkRaceName(name, races)) {
      throw new NameNotRecognisedException();
    }
    //remove the race with this name from the arrayList of races, instacnce field of CyclingPortalIml
    races.removeIf(race -> race.getName().equals(name));
  }

  public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
    // CHECK IF THE raceId exists in the system
    if (!Race.checkID(raceId, races)) {
      throw new IDNotRecognisedException(); 
    }
    
    //GET THE RIDERS in the race
    ArrayList<Integer> riderIDsInRace = new ArrayList<>();
    for (Race race : races) {
      if (race.getObjectID() == raceId) {
        for (int i : race.riderIdsInRace) {
          riderIDsInRace.add(i);
        } 
      }
    }
    ///////////////////////////////////////////////////////////
    
    ArrayList<LocalTime> totalAdjElapsedTimeRiders = new ArrayList<>();
    //LOOP OVER THE RIDER IDS IN THE RACE
    for (int i = 0; i < riderIDsInRace.size(); i++) {
      int riderId = riderIDsInRace.get(i);
      //THIS WILL HOLD THE OVERALL ADJUSTED ELAPSED TIME FOR A RIDER
      LocalTime sum = LocalTime.of(0, 0, 0, 0);
      int [] stageIds = this.getRaceStages(raceId);
      
      // LOOP OVER THE STAGES IN RACE
      for (int j = 0; j < stageIds.length; j++) {
        int stageIdCurrent  = stageIds[j];
        
        //get the adjusted elapsed time in stage FOR THE RIDER
        LocalTime AdjustedTimeForStage = this.getRiderAdjustedElapsedTimeInStage(stageIdCurrent, riderId);
        //in there is no adjusted elapsed time of the rider in the stage
        if (AdjustedTimeForStage == null) {
          AdjustedTimeForStage = LocalTime.of(0, 0, 0, 0);
        }
        //add it to the sum
        LocalDateTime dateTime1 = LocalDateTime.of(1, 1, 1, AdjustedTimeForStage.getHour(), AdjustedTimeForStage.getMinute(), AdjustedTimeForStage.getSecond(), AdjustedTimeForStage.getNano());
        LocalDateTime dateTime2 = LocalDateTime.of(1, 1, 1, sum.getHour(), sum.getMinute(), sum.getSecond(), sum.getNano());

        // Add LocalTime variables by converting them to LocalDateTime and adding them
        LocalDateTime sumDateTime = dateTime1.plusHours(dateTime2.getHour())
                                          .plusMinutes(dateTime2.getMinute())
                                          .plusSeconds(dateTime2.getSecond())
                                          .plusNanos(dateTime2.getNano());

      // Extract the time component from the sum
        LocalTime sumTime = LocalTime.of(sumDateTime.getHour(), sumDateTime.getMinute(), sumDateTime.getSecond(), sumDateTime.getNano());
        sum = sumTime;
      }

      totalAdjElapsedTimeRiders.add(sum);
      //NOW WE HAVE THE TOTAL ADJUSTED ELAPSED TIME FOR ALL RIDERS
    }
    //SORT BOTH ARRAY LISTS
    boolean sorted = false;
    while (sorted == false) {
      sorted = true;
      for (int i = 0; i < totalAdjElapsedTimeRiders.size()-1; i++){
        if (totalAdjElapsedTimeRiders.get(i).isAfter(totalAdjElapsedTimeRiders.get(i+1))) {
          sorted = false;
          LocalTime temp = totalAdjElapsedTimeRiders.get(i);
          totalAdjElapsedTimeRiders.set(i,totalAdjElapsedTimeRiders.get(i+1));
          totalAdjElapsedTimeRiders.set(i+1,temp);

          int tempIndex = riderIDsInRace.get(i);
          riderIDsInRace.set(i, riderIDsInRace.get(i+1));
          riderIDsInRace.set(i+1, tempIndex);
        }
      }
      }
    //CONVERT THE ARRAY LIST INTO A ARRAY
    int []generalClassificationRank = new int[riderIDsInRace.size()];
    for (int i = 0; i < generalClassificationRank.length; i++) {

      generalClassificationRank[i] = riderIDsInRace.get(i);
    }
    return generalClassificationRank;
    
  }  
  public void eraseCyclingPortal() {
    this.races = new ArrayList<Race>();
    this.riders = new ArrayList<Rider>();
    this.teams = new ArrayList<Team>();
    this.stages = new ArrayList<Stage>();
    this.checkpoints = new ArrayList<Checkpoint>();
    this.results = new ArrayList<Result>();
  }

  public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
    if (!Race.checkID(raceId, races)) {
      throw new IDNotRecognisedException();
    }

    //get the general classification rank for the stage
    int [] generalClassificationRank = this.getRidersGeneralClassificationRank(raceId);

    //this array stores the Adjusted Elapsed Time of eac hrider for the race
    LocalTime[] totalAdjElapsedTimeRiders = new LocalTime[generalClassificationRank.length];
    //LOOP OVER THE RIDER IDS IN THE RACE
    for (int i = 0; i < generalClassificationRank.length; i++) {
      int riderId = generalClassificationRank[i];
      //THIS WILL HOLD THE OVERALL ADJUSTED ELAPSED TIME FOR A RIDER
      LocalTime sum = LocalTime.of(0, 0, 0, 0);
      int [] stageIds = this.getRaceStages(raceId);
      
      // LOOP OVER THE STAGES IN RACE
      for (int j = 0; j < stageIds.length; j++) {
        int stageIdCurrent  = stageIds[j];
        
        //get the adjusted elapsed time in stage FOR THE RIDER
        LocalTime AdjustedTimeForStage = this.getRiderAdjustedElapsedTimeInStage(stageIdCurrent, riderId);
        //in there is no adjusted elapsed time of the rider in the stage
        if (AdjustedTimeForStage == null) {
          AdjustedTimeForStage = LocalTime.of(0, 0, 0, 0);
        }
        //add it to the sum
        LocalDateTime dateTime1 = LocalDateTime.of(1, 1, 1, AdjustedTimeForStage.getHour(), AdjustedTimeForStage.getMinute(), AdjustedTimeForStage.getSecond(), AdjustedTimeForStage.getNano());
        LocalDateTime dateTime2 = LocalDateTime.of(1, 1, 1, sum.getHour(), sum.getMinute(), sum.getSecond(), sum.getNano());

        // Add LocalTime variables by converting them to LocalDateTime and adding them
        LocalDateTime sumDateTime = dateTime1.plusHours(dateTime2.getHour())
                                          .plusMinutes(dateTime2.getMinute())
                                          .plusSeconds(dateTime2.getSecond())
                                          .plusNanos(dateTime2.getNano());

      // Extract the time component from the sum
        LocalTime sumTime = LocalTime.of(sumDateTime.getHour(), sumDateTime.getMinute(), sumDateTime.getSecond(), sumDateTime.getNano());
        sum = sumTime;
      }

      totalAdjElapsedTimeRiders[i] = sum;
      //NOW WE HAVE THE TOTAL ADJUSTED ELAPSED TIME FOR ALL RIDERS
    }

    return totalAdjElapsedTimeRiders;
  }

	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
    if (Race.checkID(raceId, races) == false) {
      throw new IDNotRecognisedException();
    }
    //orderedRiderIds stores the GeneralClassificationRank 
    int [] orderedRiderIds = this.getRidersGeneralClassificationRank(raceId);
    //pointsForRiders will store their total points for the race
    int [] pointsForRiders = new int[orderedRiderIds.length];
    int [] stagesInRace = this.getRaceStages(raceId);

    if (orderedRiderIds.length == 0) {
      return new int[0];
    }

    // loop over riderId and stages 
    for (int k = 0; k < orderedRiderIds.length; k++) {
      //pointsForAllStages stores the total point of the rider for the stage
      int pointsForAllStages = 0;
      int riderId = orderedRiderIds[k];
      /*
       * we loop over each stage of the race
       * and addthe points the rider got for it to pointsForAllStages
       */
      for (int h = 0; h < stagesInRace.length; h++) {
        int stageId = stagesInRace[h];
        int [] ridersRankedInStage = this.getRidersRankInStage(stageId);
        int [] ridersPointsInStage = this.getRidersPointsInStage(stageId);

        //we find the position of the rider
        int indexOfRider = 0;
        for (int i = 0; i < ridersRankedInStage.length; i++) {
          if (ridersRankedInStage[i] == riderId) {
            indexOfRider = i;
          }
        }
        //we add the points that he got to pointsForAllStages
        int pointsOfRiderInStage = ridersPointsInStage[indexOfRider];
        pointsForAllStages += pointsOfRiderInStage;
      }
      pointsForRiders[k] = pointsForAllStages;
    }
    /////////////////////////////////////////////////////////////
    return pointsForRiders;
  }


	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
    //check if the raceId exists
    if (!Race.checkID(raceId, races)) {
      throw new IDNotRecognisedException();
    }
    //the same process as for getRidersPointsInRace bbut this time we are considering only the mountain points
    int [] orderedRiderIds = this.getRidersGeneralClassificationRank(raceId);
    int [] pointsForRiders = new int[orderedRiderIds.length];
    int [] stagesInRace = this.getRaceStages(raceId);

    for (int k = 0; k < orderedRiderIds.length; k++) {
      int pointsForAllStages = 0;
      int riderId = orderedRiderIds[k];
      for (int h = 0; h < stagesInRace.length; h++) {
        int stageId = stagesInRace[h];
        int [] ridersRankedInStage = this.getRidersRankInStage(stageId);
        int [] ridersPointsInStage = this.getRidersMountainPointsInStage(stageId);

        int indexOfRider = 0;
        for (int i = 0; i < ridersRankedInStage.length; i++) {
          if (ridersRankedInStage[i] == riderId) {
            indexOfRider = i;
          }
        }
        
        int pointsOfRiderInStage = ridersPointsInStage[indexOfRider];
        pointsForAllStages += pointsOfRiderInStage;
      }
      pointsForRiders[k] = pointsForAllStages;
    }
    if (orderedRiderIds.length == 0) {
      return new int[0];
    }
    return pointsForRiders;
  }

	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
    //check if the raceId exists 
    if (!Race.checkID(raceId, races)) {
      throw new IDNotRecognisedException();
    }
    /*
     * get the riders in the race
     * by using a static method of Race findRaceId
     */
    Race race = Race.findRaceById(races, raceId);
    int [] rankedRidersInRace = new int[race.riderIdsInRace.size()];
    for (int i =0; i<rankedRidersInRace.length; i++) {
      rankedRidersInRace[i] = race.riderIdsInRace.get(i);
    }
    int [] pointsInRace = this.getRidersPointsInRace(raceId);

    //sort the rankedRidersInRace using the pointsInRace because this is the point classification
    
    boolean sorted = false;
    while (sorted == false) {
      sorted = true;
      for (int i = 0; i < pointsInRace.length-1; i++){
        if (pointsInRace[i] < pointsInRace[i+1]) {
          sorted = false;
          int temp = pointsInRace[i];
          pointsInRace[i] = pointsInRace[i+1];
          pointsInRace[i+1] = temp;

          int temp2 = rankedRidersInRace[i];
          rankedRidersInRace[i] = rankedRidersInRace[i+1];
          rankedRidersInRace[i+1] = temp2;
        }
      }
    }
    return rankedRidersInRace;

  }

	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
    //check if the raceId exists
    if (!Race.checkID(raceId, races)) {
      throw new IDNotRecognisedException();
    }
    int [] rankedRidersInRace = this.getRidersGeneralClassificationRank(raceId);
    int [] pointsInRace = this.getRidersMountainPointsInRace(raceId);

    //sort rankedRidersInRace using pointsInRace because we want the Mountain point classification
    //this is bubble sort
    boolean sorted = false;
    while (sorted == false) {
      sorted = true;
      for (int i = 0; i < pointsInRace.length-1; i++){
        if (pointsInRace[i] < pointsInRace[i+1]) {
          sorted = false;
          int temp = pointsInRace[i];
          pointsInRace[i] = pointsInRace[i+1];
          pointsInRace[i+1] = temp;

          int temp2 = rankedRidersInRace[i];
          rankedRidersInRace[i] = rankedRidersInRace[i+1];
          rankedRidersInRace[i+1] = temp2;
        }
      }
    }
    return rankedRidersInRace;

  }

  public void saveCyclingPortal(String filename) throws IOException{
    try {
      FileOutputStream fileOut = new FileOutputStream(filename);
      ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
      objectOut.writeObject(this);
      objectOut.close();
      fileOut.close();
    } catch (Exception e) {
      throw new IOException();
    }
  }

  public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
    try (FileInputStream fileInputStream = new FileInputStream(filename);
         ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

        CyclingPortalImpl portalCopy = (CyclingPortalImpl) objectInputStream.readObject();
        this.teams = portalCopy.teams;
        this.checkpoints = portalCopy.checkpoints;
        this.races = portalCopy.races;
        this.riders = portalCopy.riders;
        this.stages = portalCopy.stages;
        this.results = portalCopy.results;
    }
}
}
