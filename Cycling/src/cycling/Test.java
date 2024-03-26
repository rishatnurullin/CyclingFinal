import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Arrays;


public class Test {

    public static void main(String[] args) {
        CyclingPortalImpl portal = new CyclingPortalImpl();
        
        try {
            assert portal.getRaceIds().length == 0: "getRaceIds should return an empty array";
            assert portal.getTeams().length == 0;
            

            int raceId = portal.createRace("Tour de Test", "A test race");
            assert portal.getRaceIds().length == 1: "Only 1 Race should exist";
            assert portal.getRaceStages(raceId).length == 0;
            assert portal.getRidersGeneralClassificationRank(raceId).length == 0;
            System.out.println(Arrays.toString(portal.getRidersGeneralClassificationRank(raceId)));
            
            
            int raceId2 = portal.createRace("Spain", "A test race");
            assert portal.getRaceIds().length == 2: "Only 2 Races should exist";
            assert portal.getRaceStages(raceId2).length == 0;
            assert portal.getRidersGeneralClassificationRank(raceId2).length == 0;
            System.out.println(Arrays.toString(portal.getRidersGeneralClassificationRank(raceId2)));

            int raceId3 = portal.createRace("Morocco", "A test race");
            assert portal.getRaceIds().length == 3: "Only 3 Races should exist";
            assert portal.getRaceStages(raceId3).length == 0;
            assert portal.getRidersGeneralClassificationRank(raceId).length == 0;
            System.out.println(Arrays.toString(portal.getRidersGeneralClassificationRank(raceId)));
            assert portal.getRidersMountainPointClassificationRank(raceId3).length == 0;
            System.out.println(Arrays.toString(portal.getRidersMountainPointClassificationRank(raceId3)));

            System.out.println(Arrays.toString(portal.getRaceIds()));


            int stageId = portal.addStageToRace(raceId, "Stage1", "First stage", 120.0, LocalDateTime.now(), StageType.FLAT);
            assert portal.getNumberOfStages(raceId) == 1: "Only 1 stage should exist";
            assert portal.getRaceStages(raceId).length == 1;

            assert portal.getNumberOfStages(raceId2) == 0: "Only 0 stage should exist";
            assert portal.getRaceStages(raceId2).length == 0;
            assert portal.getNumberOfStages(raceId3) == 0: "Only 1 stage should exist";
            assert portal.getRaceStages(raceId3).length == 0;

            //Adding one stage to race3
            int stageId3 = portal.addStageToRace(raceId3, "Stage3", "First stage", 50.0, LocalDateTime.now(), StageType.HIGH_MOUNTAIN);
            assert portal.getNumberOfStages(raceId3) == 1: "Only 1 stage should exist";
            assert portal.getRaceStages(raceId3).length == 1: "Only 1 stage should exist";

            System.out.println(portal.viewRaceDetails(raceId) + "\n");
            System.out.println(portal.viewRaceDetails(raceId2) + "\n");
            System.out.println(portal.viewRaceDetails(raceId3) + "\n");


            // REMOVE RACE BY ID
            portal.removeRaceById(raceId3);
            System.out.println("Now getRaceIds returns " + Arrays.toString(portal.getRaceIds()));
            assert portal.getRaceIds().length == 2: "Only 2 Races should exist";
            

            int stageId2 = portal.addStageToRace(raceId, "Stage2", "First stage", 110.0, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN);
            assert portal.getNumberOfStages(raceId) == 2: "Only 2 stages should exist";
            

            System.out.println("These are the race stages" + Arrays.toString(portal.getRaceStages(raceId)) + "\n");

            int teamId = portal.createTeam("Team1", null);
            int teamId2 = portal.createTeam("Team2", null);

            assert portal.getTeamRiders(teamId).length == 0: "No riders should be in the team";
            assert portal.getTeamRiders(teamId2).length == 0: "No riders should be in the team";

            int riderId = portal.createRider(teamId, "joe", 2005);
            int riderId2 = portal.createRider(teamId, "Bruh", 2000);
            int riderId3 = portal.createRider(teamId, "din", 2004);
            int riderId4 = portal.createRider(teamId2, "me", 2004);
            int riderId5 = portal.createRider(teamId2, "you", 2004);
            int riderId6 = portal.createRider(teamId2, "Atika", 2004);

            System.out.println(Arrays.toString(portal.getTeamRiders(teamId)));
            System.out.println(Arrays.toString(portal.getTeamRiders(teamId2)));

            portal.removeRider(riderId4);
            System.out.println(Arrays.toString(portal.getTeamRiders(teamId2)));

            
            assert portal.getStageLength(stageId2) == 110: "The returned length is incorrect";
            assert portal.getStageLength(stageId) == 120: "The returned length is inccorect";
            
            portal.addCategorizedClimbToStage(stageId, 60.9, CheckpointType.HC, 6.5, 15.1);
            portal.addIntermediateSprintToStage(stageId, 70);
            portal.addCategorizedClimbToStage(stageId, 80.0, CheckpointType.C3, 7.0, 50.0);
            
            
            portal.addIntermediateSprintToStage(stageId2, 100.0);
            portal.addCategorizedClimbToStage(stageId2, 99.0, CheckpointType.C1, 1.3, 12.0);
            
            
            portal.concludeStagePreparation(stageId);
            portal.concludeStagePreparation(stageId2);
    
            
            
            LocalTime[] localTimes = new LocalTime[5];

        // Initialize elements of the array
            localTimes[0] = LocalTime.of(9, 0, 0, 0);   // 09:30:00
            localTimes[1] = LocalTime.of(11, 0, 0, 0);
            localTimes[2] = LocalTime.of(12, 0, 0, 0);
            localTimes[3] = LocalTime.of(13, 0,0, 0); // 12
            localTimes[4] = LocalTime.of(14, 0, 0, 0);

            
            LocalTime[] localTimes2 = new LocalTime[5];

        // Initialize elements of the array
            localTimes2[0] = LocalTime.of(9, 0, 0,0);   // 09:30:00
            localTimes2[1] = LocalTime.of(10, 1, 0, 0);
            localTimes2[2] = LocalTime.of(11, 0,0,5000000);
            localTimes2[3] = LocalTime.of(12, 1, 0, 0);
            localTimes2[4] = LocalTime.of(13, 0,0,5000000); // 12:15:30

            LocalTime [] localTimes5 = new LocalTime[5];

            localTimes5[0] = LocalTime.of(9, 0, 0,0);   // 09:30:00
            localTimes5[1] = LocalTime.of(9, 30, 0, 0);
            localTimes5[2] = LocalTime.of(10, 0,1,0); // 12:15:30
            localTimes5[3] = LocalTime.of(11, 30, 0, 0);
            localTimes5[4] = LocalTime.of(12, 0,1,0); 

            
            LocalTime [] localTimes6 = new LocalTime[5];

            localTimes6[0] = LocalTime.of(9, 0, 0,0);   // 09:30:00
            localTimes6[1] = LocalTime.of(9, 35, 0, 0);
            localTimes6[2] = LocalTime.of(10, 23,1,0); // 12:15:30
            localTimes6[3] = LocalTime.of(11, 35, 0, 0);
            localTimes6[4] = LocalTime.of(12, 0,1,5000000); 

            
            LocalTime[] localTimesStage21 = new LocalTime[4];

        // Initialize elements of the array
            localTimesStage21[0] = LocalTime.of(9, 0, 0, 0);   // 09:30:00
            localTimesStage21[1] = LocalTime.of(9, 20,0, 0);
            localTimesStage21[2] = LocalTime.of(9, 30,0, 0); // 12
            localTimesStage21[3] = LocalTime.of(9, 40,0, 0);
            

            LocalTime[] localTimesStage22 = new LocalTime[4];

        // Initialize elements of the array
            localTimesStage22[0] = LocalTime.of(9, 0, 0,0);   // 09:30:00
            localTimesStage22[1] = LocalTime.of(10, 0, 0, 0);
            localTimesStage22[2] = LocalTime.of(12, 0, 0, 0);
            localTimesStage22[3] = LocalTime.of(12, 30, 0, 0);
            
            LocalTime [] localTimesStage25 = new LocalTime[4];

            localTimesStage25[0] = LocalTime.of(9, 0, 0,0);   // 09:30:00
            localTimesStage25[1] = LocalTime.of(9, 28, 0, 0);
            localTimesStage25[2] = LocalTime.of(9, 29, 0, 0);
            localTimesStage25[3] = LocalTime.of(9, 46, 0, 0);

            LocalTime [] localTimesStage26 = new LocalTime[4];

            localTimesStage26[0] = LocalTime.of(9, 0, 0,0);   // 09:30:00
            localTimesStage26[1] = LocalTime.of(9, 27, 0, 0);
            localTimesStage26[2] = LocalTime.of(9, 28, 0, 0);
            localTimesStage26[3] = LocalTime.of(9, 45, 0, 0);

         // 12:15:30

            // for the first stage
            //portal.registerRiderResultsInStage(stageId, riderId, localTimes);
            portal.registerRiderResultsInStage(stageId, riderId2, localTimes2);
            portal.registerRiderResultsInStage(stageId, riderId5, localTimes5);
            portal.registerRiderResultsInStage(stageId, riderId6, localTimes6);
            
            // 
            
            // for the second stage
            
            portal.registerRiderResultsInStage(stageId2, riderId, localTimesStage21);
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
            portal.registerRiderResultsInStage(stageId2, riderId2, localTimesStage22);
            portal.registerRiderResultsInStage(stageId2, riderId5, localTimesStage25);
            portal.registerRiderResultsInStage(stageId2, riderId6, localTimesStage26);
            
            
            System.out.println("Results for first rider "  + Arrays.toString(portal.getRiderResultsInStage(stageId, riderId)));
            System.out.println("Results for second rider "  + Arrays.toString(portal.getRiderResultsInStage(stageId, riderId2)));
            System.out.println("Results for 5th rider "  + Arrays.toString(portal.getRiderResultsInStage(stageId, riderId5)));
            System.out.println("Results for 6th rider "  + Arrays.toString(portal.getRiderResultsInStage(stageId, riderId6)));
            System.out.println("Adjusted elapsed time for the first rider " + portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId));
            System.out.println("Adjusted elapsed time for the second rider " + portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId2));
            System.out.println("Adjusted elapsed time for the 5th rider " + portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId5));
            System.out.println("Adjusted elapsed time for the 6th rider " + portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId6));
            System.out.println("The rank in stage by elapsed time is " + Arrays.toString(portal.getRidersRankInStage(stageId)));

            System.out.println("The adjusted elapsed times in stage are " + Arrays.toString(portal.getRankedAdjustedElapsedTimesInStage(stageId)));
            System.out.println("The points in stage. They are the same as the rankOfRidersInStage" + Arrays.toString(portal.getRidersPointsInStage(stageId)));
            System.out.println("The mountain points for the riders are " + Arrays.toString(portal.getRidersMountainPointsInStage(stageId)));

            
            //NOW FOR STAGE 2
            System.out.println("\n----------------------------------------------------------------------------------------------------\n");
            System.out.println("Results for first rider on the 2nd stage"  + Arrays.toString(portal.getRiderResultsInStage(stageId2, riderId)));
            System.out.println("Results for second rider on the 2nd stage"  + Arrays.toString(portal.getRiderResultsInStage(stageId2, riderId2)));
            System.out.println("Results for 5 rider on the 2nd stage"  + Arrays.toString(portal.getRiderResultsInStage(stageId2, riderId5)));
            System.out.println("Results for 6 rider on the 2nd stage"  + Arrays.toString(portal.getRiderResultsInStage(stageId2, riderId6)));
            
            
            /** 
            System.out.println("The rank in stage by elapsed time is " + Arrays.toString(portal.getRidersRankInStage(stageId2)));
            System.out.println("The adjusted elapsed times in stage are " + Arrays.toString(portal.getRankedAdjustedElapsedTimesInStage(stageId2)));
            */
            System.out.println("The points in stage. They are the same as the rankOfRidersInStage" + Arrays.toString(portal.getRidersPointsInStage(stageId2)));
            
            System.out.println("The mountain points for the riders are " + Arrays.toString(portal.getRidersMountainPointsInStage(stageId2)));
            System.out.println("\n----------------------------------------\n");
            System.out.println("bruh");
            System.out.println("The General Ranknig Classification is " + Arrays.toString(portal.getRidersGeneralClassificationRank(raceId)));
            System.out.println("\n----------------------------------------\n");
            System.out.println("This is the total elapsed times in race" + Arrays.toString(portal.getGeneralClassificationTimesInRace(raceId)));
            System.out.println("\n----------------------------------------\n");
            System.out.println("This is the points in race" + Arrays.toString(portal.getRidersPointClassificationRank(raceId)));

            System.out.println("Removing riderId");
            portal.removeRider(riderId);
            for (Rider rider : portal.riders) {
                System.out.println(rider.getRiderId());
            }
            
            
            System.out.println("The General Ranknig Classification is " + Arrays.toString(portal.getRidersGeneralClassificationRank(raceId)));
            System.out.println("nnnnnnn");
            portal.saveCyclingPortal("Serialized.txt");
            portal.eraseCyclingPortal();
            System.out.println("These are the races" + Arrays.toString(portal.getRaceIds()));
            System.out.println("These are the teams " + Arrays.toString(portal.getTeams()));
            System.out.println("These are the teams " + portal.riders);
            //portal.loadCyclingPortal("Serialized.txt");

        } catch (IDNotRecognisedException e) {
            // TODO: handle exception
            System.out.println("IDNotRecognisedException");
        } catch (IllegalArgumentException e){
            System.out.println("IllegalArgumentException");
        }  catch (InvalidStageStateException e) {
            System.out.println("InvalidStageStateException");
        } catch(InvalidCheckpointTimesException e) {
            System.out.println("InvalidCheckpointTimesException");
        } catch(DuplicatedResultException e) {
            System.out.println("DuplicatedResultException");
        } catch(InvalidNameException e) {
            System.out.println("InvalidNameException");
        } catch(IllegalNameException e) {
            System.out.println("IllegalNameException");
        }catch(InvalidLengthException e) {
            System.out.println("InvalidLengthException");
        } catch(InvalidLocationException e) {
            System.out.println("InvalidLocationException");
        }catch(InvalidStageTypeException e) {
            System.out.println("InvalidStageTypeException");
        }catch (Exception e) {
            System.out.println("Exception");
        }
        
        
            
            /** 
            // Test valid result registration
            try {
                portal.registerRiderResultsInStage(stageId, riderId, LocalTime.of(8, 0), LocalTime.of(9, 30), LocalTime.of(10, 45));
                System.out.println("Result registered successfully.");
            } catch (Exception e) {
                System.out.println("Failed to register result: " + e.getMessage());
            }

            // Test invalid stage ID
            try {
                portal.registerRiderResultsInStage(999, riderId, LocalTime.of(8, 0), LocalTime.of(9, 30), LocalTime.of(10, 45));
                System.out.println("Result registered successfully.");
            } catch (Exception e) {
                System.out.println("Failed to register result: " + e.getMessage());
            }

            // Test invalid rider ID
            try {
                portal.registerRiderResultsInStage(stageId, 999, LocalTime.of(8, 0), LocalTime.of(9, 30), LocalTime.of(10, 45));
                System.out.println("Result registered successfully.");
            } catch (Exception e) {
                System.out.println("Failed to register result: " + e.getMessage());
            }

            // Test duplicated result
            try {
                portal.registerRiderResultsInStage(stageId, riderId, LocalTime.of(8, 0), LocalTime.of(9, 30), LocalTime.of(10, 45));
                System.out.println("Result registered successfully.");
            } catch (Exception e) {
                System.out.println("Failed to register result: " + e.getMessage());
            }

            // Test invalid checkpoint times length
            try {
                portal.registerRiderResultsInStage(stageId, riderId, LocalTime.of(8, 0), LocalTime.of(9, 30));
                System.out.println("Result registered successfully.");
            } catch (Exception e) {
                System.out.println("Failed to register result: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("An error occurred during the test setup: " + e.getMessage());
        }
        */
    

}}
