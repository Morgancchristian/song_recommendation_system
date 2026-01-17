import java.util.ArrayList;
import java.util.HashSet;

public class Cs214Project {

    public static void main(String[] args) {
        /**
         * If neither a name nor a path is provided, the output file will be
         * written to the path below with the name "output.csv".
         */

        String defaultOutputFilePath = "database/files/";

        if (args.length < 2) {
            System.err.println("Error: Incorrect number of arguments.");
            return;
        }

        if (args[0].isEmpty()) {
            System.err.println("Error: Input file path, args[0], can not be empty.");
            return;
        }

        if (!args[0].endsWith(".csv")) {
            System.err.println("Error: input paths must be '.csv'.");
            return;
        }

        if (args.length > 1 && !args[1].isEmpty() && !args[1].endsWith(".csv")) {
            System.err.println("Error: Output paths must be '.csv'.");
            return;
        }

        String outputArg = null;
        if (args.length > 1) {
            outputArg = args[1];
        }

        CSVFilePathChecks pathChecker = new CSVFilePathChecks(args[0], outputArg, defaultOutputFilePath);

        if (!pathChecker.checkIfInputFileExists()) {
            System.err.println("Error: Input file does not exist: " + args[0]);
            return;
        }

        if (pathChecker.checkIfInputFileEmpty()) {
            System.err.println("Error: Input file is empty.");
            return;
        }

        String outputFilePath = pathChecker.determineOutputFilePath();

        try {
            CSVtoListofLists csvConverter = CSVtoListofLists.getInstance(args[0]);
            csvConverter.convert();

            String thirdArg = null;
            if (args.length > 2) {
                thirdArg = args[2];
                if (!"-a".equals(thirdArg) && !"-u".equals(thirdArg) && !"-p".equals(thirdArg) && !"-r".equals(thirdArg) && !"-s".equals(thirdArg)) {
                    System.err.println("Error: unsupported argument " + "'" + thirdArg + "'");
                    return;
                }
            }

            ExecutionPath pathToExecute = new ExecutionPath(thirdArg);
            int path = pathToExecute.executionPath();

            if (path == 0) {
                GenerateStatistics statsGeneratorGeneral = new GenerateStatistics(csvConverter.getRawData());
                statsGeneratorGeneral.getAndSetSongNamesFromRawData();
                statsGeneratorGeneral.getAndSetNumberAndMeanOfRatingsForEachSongFromRawData();
                statsGeneratorGeneral.computeSTDForEachSong();

                WriteProcDataToCSV writerGeneral = new WriteProcDataToCSV(statsGeneratorGeneral.getProcessedData(), outputFilePath);
                writerGeneral.writeGeneralStatistics();

            } else if (path == 1) {
                GenerateStatistics statsGeneratorUserSpecific = new GenerateStatistics(csvConverter.getRawData());
                statsGeneratorUserSpecific.getAndSetSongNamesFromRawData();
                statsGeneratorUserSpecific.getUserNamesFromRawData();
                statsGeneratorUserSpecific.createUserFocusedArrayWithoutRatings();
                statsGeneratorUserSpecific.addUserRatingsToUserFocusedArray();
                statsGeneratorUserSpecific.filterOutUncooperativeUsersBasedOnSongs(statsGeneratorUserSpecific.filterOutUncooperativeUsersBasedOnRatings());

                WriteProcDataToCSV writerUserSpecific = new WriteProcDataToCSV(statsGeneratorUserSpecific.getProcessedData(), outputFilePath);
                writerUserSpecific.writeUserStatistics();

            } else if (path == 2) {
                GenerateStatistics statsGeneratorElucidianSimilarityBetweenSongs = new GenerateStatistics(csvConverter.getRawData());
                statsGeneratorElucidianSimilarityBetweenSongs.getAndSetSongNamesFromRawData();
                statsGeneratorElucidianSimilarityBetweenSongs.getUserNamesFromRawData();
                statsGeneratorElucidianSimilarityBetweenSongs.createUserFocusedArrayWithoutRatings();
                statsGeneratorElucidianSimilarityBetweenSongs.addUserRatingsToUserFocusedArray();
                statsGeneratorElucidianSimilarityBetweenSongs.filterOutUncooperativeUsersBasedOnSongs(statsGeneratorElucidianSimilarityBetweenSongs.filterOutUncooperativeUsersBasedOnRatings());
                statsGeneratorElucidianSimilarityBetweenSongs.normalizeRatings();
                statsGeneratorElucidianSimilarityBetweenSongs.calculateElucidianSimilarity(0);

                WriteProcDataToCSV writerElucidianSimilarityBetweenSongsPerUser = new WriteProcDataToCSV(statsGeneratorElucidianSimilarityBetweenSongs.getProcessedData(), outputFilePath, statsGeneratorElucidianSimilarityBetweenSongs.getElucidianSimilarities());
                writerElucidianSimilarityBetweenSongsPerUser.writeElucidianSimilarity();

            } else if (path == 3) {
                GenerateStatistics statsGeneratorElucidianSimilarityBetweenUsers= new GenerateStatistics(csvConverter.getRawData());
                statsGeneratorElucidianSimilarityBetweenUsers.getAndSetSongNamesFromRawData();
                statsGeneratorElucidianSimilarityBetweenUsers.getUserNamesFromRawData();
                statsGeneratorElucidianSimilarityBetweenUsers.createUserFocusedArrayWithoutRatings();
                statsGeneratorElucidianSimilarityBetweenUsers.addUserRatingsToUserFocusedArray();
                statsGeneratorElucidianSimilarityBetweenUsers.filterOutUncooperativeUsersBasedOnSongs(statsGeneratorElucidianSimilarityBetweenUsers.filterOutUncooperativeUsersBasedOnRatings());
                statsGeneratorElucidianSimilarityBetweenUsers.normalizeRatings();
                statsGeneratorElucidianSimilarityBetweenUsers.calculateElucidianSimilarity(1);
                statsGeneratorElucidianSimilarityBetweenUsers.predict();
                statsGeneratorElucidianSimilarityBetweenUsers.deNormalizeRatings();
                
                WriteProcDataToCSV writerElucidianSimilarityBetweenUsers = new WriteProcDataToCSV(outputFilePath, statsGeneratorElucidianSimilarityBetweenUsers.getPredictions());
                writerElucidianSimilarityBetweenUsers.writeUserPredictions();

            } else if (path == 4) {
                GenerateStatistics statsGeneratorClustering = new GenerateStatistics(csvConverter.getRawData());
                statsGeneratorClustering.getAndSetSongNamesFromRawData();
                statsGeneratorClustering.getUserNamesFromRawData();
                statsGeneratorClustering.createUserFocusedArrayWithoutRatings();
                statsGeneratorClustering.addUserRatingsToUserFocusedArray();
                statsGeneratorClustering.filterOutUncooperativeUsersBasedOnSongs(statsGeneratorClustering.filterOutUncooperativeUsersBasedOnRatings());
                statsGeneratorClustering.normalizeRatings();
                statsGeneratorClustering.calculateElucidianSimilarity(1);


               try {
                statsGeneratorClustering.predict();
                statsGeneratorClustering.deNormalizeRatings();
               } catch (Exception e) {

               }
               
                statsGeneratorClustering.fillInUnpredictedRatings();
                statsGeneratorClustering.buildCompleteRatingsMatrix();

                ArrayList<String> userSelections = new ArrayList<>();

                if (args.length < 4) {
                    System.err.println("Error: must select at lease one song for recommendations");
                    return;
                }
                
                for (int i = 3; i < args.length; i++) {
                    userSelections.add(args[i]);
                }
                
                statsGeneratorClustering.removeSongsWithOneDistinctRating(userSelections);
                statsGeneratorClustering.normalizeRatingsForClustering();
                statsGeneratorClustering.initializeClustersFromSelections(userSelections);
                statsGeneratorClustering.kmeansClustering();
                    
                ArrayList<ArrayList<String>> recommendations = statsGeneratorClustering.generateClusteringRecommendations(userSelections);
                    
                WriteProcDataToCSV writerClustering = new WriteProcDataToCSV(recommendations, outputFilePath);
                writerClustering.writeClusteringRecommendations();

            } else if (path == 5) {

                if (args.length < 4) {
                    System.err.println("Error: Fewer than 5 arguments passed");
                }

                String kStr = args[3];
                int k;
                try{
                    k = Integer.parseInt(kStr);
                } catch (NumberFormatException e) { 
                    System.err.println("Error: k must be a valid integer");
                    return;
                }

                if (k < 1) {
                    System.err.println("Error: k must be at least 1");
                }

                if (args.length < 4 + k) {
                    System.err.println("Error: fewer user provided songs than k");
                }

                ArrayList<String> allUserProvidedSongs = new ArrayList<>();
                for (int i = 4; i < args.length; i++) {
                    allUserProvidedSongs.add(args[i]);
                }

                if (allUserProvidedSongs.size() != new HashSet<>(allUserProvidedSongs).size()) {
                    System.err.println("Error: duplicate songs in cli inuput");
                    return;
                }

                GenerateStatistics statsGeneratorPlaylist = new GenerateStatistics(csvConverter.getRawData());
                statsGeneratorPlaylist.getAndSetSongNamesFromRawData();
                statsGeneratorPlaylist.getUserNamesFromRawData();
                statsGeneratorPlaylist.createUserFocusedArrayWithoutRatings();
                statsGeneratorPlaylist.addUserRatingsToUserFocusedArray();
                statsGeneratorPlaylist.filterOutUncooperativeUsersBasedOnSongs(statsGeneratorPlaylist.filterOutUncooperativeUsersBasedOnRatings());
                statsGeneratorPlaylist.normalizeRatings();
                statsGeneratorPlaylist.calculateElucidianSimilarity(1);

                try {
                    statsGeneratorPlaylist.predict();
                    statsGeneratorPlaylist.deNormalizeRatings();
                } catch (Exception e) {
                    
                }
            
                statsGeneratorPlaylist.fillInUnpredictedRatings();

                statsGeneratorPlaylist.buildCompleteRatingsMatrix();
                statsGeneratorPlaylist.validateUserProvidedSongs(allUserProvidedSongs);
                
                ArrayList<String> firstKSongs = new ArrayList<>(allUserProvidedSongs.subList(0, k));
            
                statsGeneratorPlaylist.removeSongsWithOneDistinctRating(firstKSongs);
                
                if (statsGeneratorPlaylist.getSongNames().size() < k + 1) {
                    System.err.println("Error: Pre-processing leaves fewer than K+1 songs");
                    return;
                }
                
                statsGeneratorPlaylist.normalizeRatingsForClustering();
                statsGeneratorPlaylist.initializeClustersFromSelections(firstKSongs);
                statsGeneratorPlaylist.kmeansClustering();

                ArrayList<String> playlist = statsGeneratorPlaylist.generatePlaylist(allUserProvidedSongs);
                
              
                WriteProcDataToCSV writerPlaylist = new WriteProcDataToCSV(playlist, outputFilePath, 1);
                writerPlaylist.writePlaylist();

            } else {
                System.err.println("Error: This program does not recognize the argument for the execution path that was provided: " + args[2]);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return;
        }
    }
}
