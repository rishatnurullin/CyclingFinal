import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class Result implements Serializable {
    private int stageId;
    private int riderId;
    private LocalTime[] checkpointTimes;
    private int raceID;

    protected int getStageId() {
        return stageId;
    }
    protected int getRiderId() {
        return riderId;
    }
    protected LocalTime[] getCheckpointTimes() {
        return checkpointTimes;
    }
    protected int getRaceId() {
        return raceID;
    }
    /*this constructor has no checks for edge cases or wrong inputs 
     * because all the checks are already done before calling it
     */
    protected Result(int stageId, int riderId, LocalTime[]checkpointTimes, ArrayList<Stage> stages) {
        Stage stage = Stage.findStage(stageId, stages);
        this.raceID = stage.getRaceId();
        this.stageId = stageId;
        this.riderId =riderId;
        this.checkpointTimes = checkpointTimes;
    }
}

