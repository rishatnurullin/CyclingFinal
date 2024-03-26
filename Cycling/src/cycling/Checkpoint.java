import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class Checkpoint implements Serializable{
  private static int count = 1;
  private int checkpointId;
  private int stageID;
  private double location;
  double averageGradient = 0;
  private CheckpointType type;
  private double length;
  private int raceID;

  protected int getCheckpointId() {
    return checkpointId;
  }
  protected int getStageId() {
    return stageID;
  }
  protected double getLocation() {
    return location;
  }
  protected CheckpointType getType() {
    return type;
  }
  protected double getLength() {
    return length;
  }
  protected int getRaceId() {
    return raceID;
  }
  protected Checkpoint(
    int stageID,
    double location,
    double averageGradient,
    double length,
    CheckpointType type,
    ArrayList<Stage> stages
  )
    throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
    if (Stage.checkID(stageID, stages)) {
      double stageLength = 0;
      String state = null;
      StageType stageType = null;
      int raceID = 0;
      for (Stage stage : stages) {
        if (stage.getStageId() == stageID) {
          stageLength = stage.getLength();
          state = stage.getState();
          stageType = stage.getStageType();
          raceID = stage.getRaceId();
        }
      }
      if (location < stageLength) {
        if (!state.equals("waiting for results")) {
          if (stageType != StageType.TT) {
            //add the climb
            this.type = type;
            this.stageID = stageID;
            this.location = location;
            checkpointId = count;
            count++;
            this.raceID = raceID;
            if (type == CheckpointType.SPRINT) {
              this.averageGradient = 0;
              this.length = 0;
            } else {
              this.averageGradient = averageGradient;
              this.length = length;
            }
          } else {
            throw new InvalidStageTypeException();
          }
        } else {
          throw new InvalidStageStateException();
        }
      } else {
        throw new InvalidLocationException();
      }
    } else {
      throw new IDNotRecognisedException();
    }
  }
  //checks if checkpointId exists in the ArrayList provided
  protected static boolean checkID (int checkpointId, ArrayList<Checkpoint>checkpoints) {
    boolean found = false;
    for (Checkpoint checkpoint : checkpoints) {
        if (checkpoint.checkpointId == checkpointId) { 
            found = true;
            break;
        }
    }
    return found;
}

//retreives the checkpoints in order based on their location in a stage
protected static int[] getCheckpointsInOrder(ArrayList<Checkpoint> checkpoints, int stageId) {
  //remove checkpoints with other stageIds
  
  ArrayList<Checkpoint> checkpointsInStage = new ArrayList<>();
  for (Checkpoint checkpoint : checkpoints) {
    if (checkpoint.stageID == stageId) {
      checkpointsInStage.add(checkpoint);
    }
  }
  
  //the checkpoints is empty, just return an empty array
  if (checkpointsInStage.size() == 0) {
    return new int[0];
  }
  
  //sorting checkpoints using a comparator
  //we sort them by the location
  checkpointsInStage.sort(Comparator.comparingDouble(Checkpoint::getLocation));
  
  int[] checkpointIDs = new int[checkpointsInStage.size()];
    // Update checkpoint IDs array
  for (int i = 0; i < checkpointsInStage.size(); i++) {
      checkpointIDs[i] = checkpointsInStage.get(i).checkpointId;
    }
  return checkpointIDs;
}

//returns the type of the checkpoint
protected static CheckpointType getCheckPointType(ArrayList<Checkpoint> checkpoints, int checkpointID) {
  CheckpointType typeOfCheckpoint = null; 
  for (Checkpoint checkpoint : checkpoints) {
    //finds the checkpoint by id
    if (checkpoint.checkpointId == checkpointID) {
      typeOfCheckpoint = checkpoint.type;
      break;
    }
  }
  return typeOfCheckpoint;
}
}
