import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GenerateStatistics {

    private ArrayList<ArrayList<String>> rawData;
    private ArrayList<String> songNames;
    private ArrayList<ArrayList<String>> processedData;
    private ArrayList<String> userNames;
    private ArrayList<ArrayList<String>> userStatisticsMeanStd;
    private ArrayList<ArrayList<String>> eulcidianSimilarities;
    private ArrayList<ArrayList<String>> userPredictions;
    private ArrayList<ArrayList<String>> completeRatingsForClustering;
    private ArrayList<Cluster> clusters;

    public GenerateStatistics(ArrayList<ArrayList<String>> rawData) {
        this.rawData = rawData;
    }

    public void getAndSetSongNamesFromRawData() {
        /*
         * Extracts the unique song names and sorts them alphabetically.
         * Creates the processedData structure
         * Plan for updatde: break this into two methods, one to get song names
         * and one to create processed data structure
         */
        songNames = new ArrayList<>();
        for (ArrayList<String> record : rawData) {
            String songName = record.get(0);
            if (!this.songNames.contains(songName)) {
                this.songNames.add(songName);
            }
        }

        Collections.sort(this.songNames);

        processedData = new ArrayList<>();
        for (int i = 0; i < songNames.size(); i++) {
            ArrayList<String> secondArrayToPutInProcessedData =
                new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                secondArrayToPutInProcessedData.add("");
                // [songName, count, mean, stdDev]
            }
            this.processedData.add(secondArrayToPutInProcessedData);
            this.processedData.get(i).set(0, songNames.get(i));
        }
    }

    public ArrayList<String> getSongNames() {
        return this.songNames;
    }

    public void getAndSetNumberAndMeanOfRatingsForEachSongFromRawData() {
        for (int i = 0; i < songNames.size(); i++) {
            int count = 0;
            double sum = 0;
            for (ArrayList<String> record : rawData) {
                if (record.get(0).equals(songNames.get(i))) {
                    count++;
                    sum += Integer.parseInt(record.get(2));
                }
            }
            this.processedData.get(i).set(1, String.valueOf(count));
            this.processedData.get(i).set(2, String.valueOf(sum / count));
        }
    }

    public void computeSTDForEachSong() {
        /**
         * This method utalizes the "Two Pass Algorithm" to compute STD found:
         * https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance
         */
        for (int i = 0; i < songNames.size(); i++) {
            Double count = Double.parseDouble(processedData.get(i).get(1));
            double mean = Double.parseDouble(processedData.get(i).get(2));
            double sumOfSquares = 0.0;
            for (ArrayList<String> record : rawData) {
                if (record.get(0).equals(songNames.get(i))) {
                    double rating = Double.parseDouble(record.get(2));
                    sumOfSquares += Math.pow(rating - mean, 2);
                }
            }
            double stdDev = Math.sqrt(sumOfSquares / count);
            this.processedData.get(i).set(3, String.valueOf(stdDev));
        }
    }

    public ArrayList<ArrayList<String>> getProcessedData() {
        return this.processedData;
    }

    public void getUserNamesFromRawData() {
        userNames = new ArrayList<>();
        for (ArrayList<String> record : rawData) {
            String userName = record.get(1);
            if (!this.userNames.contains(userName)) {
                this.userNames.add(userName);
            }
        }
        Collections.sort(this.userNames);
    }

    public void createUserFocusedArrayWithoutRatings() {
        processedData = new ArrayList<>();
        for (int k = 0; k < userNames.size(); k++) {
            for (int i = 0; i < songNames.size(); i++) {
                ArrayList<String> secondArrayToPutInProcessedData = new ArrayList<>();

                for (int j = 0; j < 3; j++) {
                    secondArrayToPutInProcessedData.add("");
                }

                this.processedData.add(secondArrayToPutInProcessedData);

                int lastIndex = processedData.size() - 1;
                this.processedData.get(lastIndex).set(0, userNames.get(k));
                this.processedData.get(lastIndex).set(1, songNames.get(i));
            }
        }
    }

    public void addUserRatingsToUserFocusedArray() {
        for (int i = 0; i < processedData.size(); i++) {
            String user = processedData.get(i).get(0);
            String song = processedData.get(i).get(1);
            boolean found = false;

            for (int j = 0; j < rawData.size(); j++) {
                if (
                    ((rawData.get(j).get(1).equals(user))) && (rawData.get(j).get(0).equals(song))) {
                    this.processedData.get(i).set(2, rawData.get(j).get(2));
                    found = true;
                    break;
                }
            }

            if (!found) {
                this.processedData.get(i).set(2, "NaN");
            }
        }
    }
    // This is what not sure about yet and might want to simplify:

    public Set<String> filterOutUncooperativeUsersBasedOnRatings() {
        Set<String> songsToCheck = new HashSet<>();
        for (int i = 0; i < this.userNames.size(); i++) {
            String userName = this.userNames.get(i);
            ArrayList<Integer> allUserRatings = new ArrayList<>();
            for (int j = 0; j < this.processedData.size(); j++) {
                if (userName.equals(this.processedData.get(j).get(0)) && !this.processedData.get(j).get(2).equals("NaN")) {
                    allUserRatings.add(Integer.parseInt(this.processedData.get(j).get(2)));
                }
            }
            Set<Integer> uniqueRatings = new HashSet<>(allUserRatings);
            if (uniqueRatings.size() <= 1) {
                for (int k = this.processedData.size() - 1; k >= 0; k--) {
                    if (userName.equals(this.processedData.get(k).get(0))) {
                        songsToCheck.add(this.processedData.get(k).get(1));
                        this.processedData.remove(k);
                    }
                }
                this.userNames.remove(i);
                i--;
            }
        }
        return songsToCheck;
    }

    public void filterOutUncooperativeUsersBasedOnSongs(Set<String> songsToCheck) {
        Set<String> songsWithCooperativeRatings = new HashSet<>();
        for (ArrayList<String> row : this.processedData) {
            if (!row.get(2).equals("NaN")) {
                songsWithCooperativeRatings.add(row.get(1));
            }
        }
        for (int i = this.processedData.size() - 1; i >= 0; i--) {
            String song = this.processedData.get(i).get(1);
            if (!songsWithCooperativeRatings.contains(song)) {
                this.processedData.remove(i);
            }
        }
    }

    public ArrayList<String> getUserNames() {
        return this.userNames;
    }
    // Stop here

    public void normalizeRatings() {
        userStatisticsMeanStd = new ArrayList<>();

        for (int i = 0; i < this.userNames.size(); i++) {
            String userNameInUserNames = userNames.get(i);
            double count = 0;
            double runRating = 0;

            for (int j = 0; j < processedData.size(); j++) {
                String userNameInProcessedData = processedData.get(j).get(0);
                String ratingStr = processedData.get(j).get(2);
                if (userNameInUserNames.equals(userNameInProcessedData) && !ratingStr.equals("NaN")) {
                    count++;
                    runRating += Double.parseDouble(processedData.get(j).get(2));
                }
            }

            double userMean = runRating / count;
            double sumOfSquares = 0.0;

            for (int k = 0; k < processedData.size(); k++) {
                String userNameInProcessedData = processedData.get(k).get(0);
                String ratingStr = processedData.get(k).get(2);
                if (userNameInUserNames.equals(userNameInProcessedData) && !ratingStr.equals("NaN")) {
                    double rating = Double.parseDouble(processedData.get(k).get(2));
                    sumOfSquares += Math.pow(rating - userMean, 2);
                }
            }

            double stdDevUser = Math.sqrt(sumOfSquares / count);

            ArrayList<String> stats = new ArrayList<>();
            stats.add(userNameInUserNames);        
            stats.add("");                         
            stats.add(String.valueOf(userMean));   
            stats.add(String.valueOf(stdDevUser)); 
            userStatisticsMeanStd.add(stats);

            for (int t = 0; t < processedData.size(); t++) {
                String userNameInProcessedData = processedData.get(t).get(0);
                String ratingStr = processedData.get(t).get(2);
                if (userNameInUserNames.equals(userNameInProcessedData) && !ratingStr.equals("NaN")) {
                    double userRating = Double.parseDouble(processedData.get(t).get(2));
                    String normalizedRating = String.valueOf((userRating - userMean) / stdDevUser);
                    processedData.get(t).set(2, normalizedRating);
                }
            }
        }
    }

   public ArrayList<String> uniqueSongsForSimilarityComp() {
        ArrayList<String> uniqueSongs = new ArrayList<>();
        for (ArrayList<String> row : this.processedData) {
            String song = row.get(1);
            if (!uniqueSongs.contains(song)) uniqueSongs.add(song);
        }
        Collections.sort(uniqueSongs);
        return uniqueSongs;
    }

    public ArrayList<String> uniqueUsersForSimilarityComp() {
        ArrayList<String> uniqueUsers = new ArrayList<>();
        for (ArrayList<String> row : this.processedData) {
            String user = row.get(0);
            if (!uniqueUsers.contains(user)) uniqueUsers.add(user);
        }
        Collections.sort(uniqueUsers);
        return uniqueUsers;
    }

   public void calculateElucidianSimilarity(int compareFlag) throws RuntimeException{
        ArrayList<String> uniqueComparison;
        ArrayList<String> dimensions;
        int comparisonIndex;
        int dimensionsIndex;

        if (compareFlag == 0) {
            uniqueComparison = uniqueSongsForSimilarityComp();
            dimensions = this.userNames;
            comparisonIndex = 1; // song index
            dimensionsIndex = 0; // user index
            if (uniqueComparison.size() < 2) {
                throw new RuntimeException("at least two cooperative users are required for song similarity");
            }
        } else {
            uniqueComparison = uniqueUsersForSimilarityComp();
            dimensions = this.songNames;
            comparisonIndex = 0; // user index
            dimensionsIndex = 1; // song index
            if (uniqueComparison.size() < 2) {
                throw new RuntimeException("at least two cooperative users are required for user similarity");
            }
        }

        

        eulcidianSimilarities = new ArrayList<>();

        for (int i = 0; i < uniqueComparison.size(); i++) {
            for (int j = i + 1; j < uniqueComparison.size(); j++) {
                String object1 = uniqueComparison.get(i);
                String object2 = uniqueComparison.get(j);

                ArrayList<Double> object1Ratings = new ArrayList<>();
                ArrayList<Double> object2Ratings = new ArrayList<>();

                for (String dimension : dimensions) {
                    Double rating1 = null;
                    Double rating2 = null;

                    for (ArrayList<String> row : this.processedData) {
                        if (row.get(comparisonIndex).equals(object1) && row.get(dimensionsIndex).equals(dimension) && !row.get(2).equals("NaN")) {
                            rating1 = Double.parseDouble(row.get(2));
                            break;
                        }
                    }

                    for (ArrayList<String> row : this.processedData) {
                        if (row.get(comparisonIndex).equals(object2) && row.get(dimensionsIndex).equals(dimension) && !row.get(2).equals("NaN")) {
                            rating2 = Double.parseDouble(row.get(2));
                            break;
                        }
                    }

                    if (rating1 != null && rating2 != null) {
                        object1Ratings.add(rating1);
                        object2Ratings.add(rating2);
                    }
                }

                String similarity;
                if (object1Ratings.size() == 0) {
                    similarity = "NaN";
                } else {
                    double distance = 0.0;
                    for (int r = 0; r < object1Ratings.size(); r++) {
                        double diff = object1Ratings.get(r) - object2Ratings.get(r);
                        distance += diff * diff;
                    }
                    distance = Math.sqrt(distance);
                    similarity = String.valueOf(distance);
                }

                ArrayList<String> comp = new ArrayList<>();
                comp.add(object1);
                comp.add(object2);
                comp.add(similarity);
                this.eulcidianSimilarities.add(comp);
            }
        }
    }

    public ArrayList<ArrayList<String>> getElucidianSimilarities() {
        return this.eulcidianSimilarities;
    }

    // processedData = [[user1, song, normalized rating], [...], ...]
    // uniqueUsers = [user1, user2, user3, ...]
    // uniqueSongs = [song1, song2, song3, ...]
    // elucidianSimilarities = [[user1, user2, similarities], [...], ...]
    
    public void predict() throws RuntimeException {

        userPredictions = new ArrayList<>();
        
        for (ArrayList<String> row : this.processedData) {
            String rating = row.get(2);

            if (rating.equals("NaN")) {
                ArrayList<String> format = new ArrayList<>();
                String userName = row.get(0);
                String songName = row.get(1);
                
                format.add(songName);   
                format.add(userName);   
                format.add(rating);     
                
                this.userPredictions.add(format);
            }
        }

    
        for (int i = 0; i < this.userPredictions.size() - 1; i++) {
            for (int j = 0; j < this.userPredictions.size() - i - 1; j++) {
                ArrayList<String> current = this.userPredictions.get(j);
                ArrayList<String> next = this.userPredictions.get(j + 1);

                int songComparison = current.get(0).compareTo(next.get(0));

                if (songComparison > 0) {
                    this.userPredictions.set(j, next);
                    this.userPredictions.set(j + 1, current);
                } else if (songComparison == 0) {
                    int userComparison = current.get(1).compareTo(next.get(1));

                    if (userComparison > 0) {
                        this.userPredictions.set(j, next);
                        this.userPredictions.set(j + 1, current);
                    }
                }
            }
        }

        
        for (int g = 0; g < this.userPredictions.size(); g++) {
            String songName = this.userPredictions.get(g).get(0);
            String userName = this.userPredictions.get(g).get(1);

            ArrayList<ArrayList<String>> similarUsers = new ArrayList<>();
            
            for (ArrayList<String> row : this.eulcidianSimilarities) {
                
                if (row.get(2).equals("NaN")) {
                    continue;
                }
                
                String similarity = row.get(2);
                
                
                if (row.get(0).equals(userName)) {
                    ArrayList<String> simUser = new ArrayList<>();
                    simUser.add(row.get(1)); 
                    simUser.add(similarity);   
                    similarUsers.add(simUser);
                    
                } else if (row.get(1).equals(userName)) {
                    ArrayList<String> simUser = new ArrayList<>();
                    simUser.add(row.get(0));  
                    simUser.add(similarity);   
                    similarUsers.add(simUser);
                }
            }
            
            for (int i = 0; i < similarUsers.size() - 1; i++) {
                for (int j = 0; j < similarUsers.size() - i - 1; j++) {
                    double current = Double.parseDouble(similarUsers.get(j).get(1));
                    double next = Double.parseDouble(similarUsers.get(j + 1).get(1));
                    
                    if (current > next) {
                        ArrayList<String> temp = similarUsers.get(j);
                        similarUsers.set(j, similarUsers.get(j + 1));
                        similarUsers.set(j + 1, temp);
                    }
                }
            }
            
            
            boolean foundPrediction = false;
            for (ArrayList<String> simUser : similarUsers) {
                String simUserName = simUser.get(0);
                
                for (ArrayList<String> row : this.processedData) {
                    if (row.get(0).equals(simUserName) && 
                        row.get(1).equals(songName)) {
                        
                        if (!row.get(2).equals("NaN")) {
                            this.userPredictions.get(g).set(2, row.get(2));
                            foundPrediction = true;
                            break;
                        }
                        break;
                    }
                }
                
                if (foundPrediction) {
                    break;
                }
            }
        }

        if (this.userPredictions.size() <= 0) {
            throw new RuntimeException("no predictions to be made");
        }
    }

    // userStatisticsMeanStd = [[userName, "", mean, stdDev], [...], ...]

    public void deNormalizeRatings() {
        
        for (ArrayList<String> predictedUserRating : this.userPredictions) {
            String userName = predictedUserRating.get(1);
            String normalizedRatingStr = predictedUserRating.get(2);

            if (normalizedRatingStr.equals("NaN")) {
                continue;
            }

            double userMean = 0;
            double userStdDev = 0;
            boolean found = false;

            for (ArrayList<String> stats : this.userStatisticsMeanStd) {
                if (stats.get(0).equals(userName)) {
                    userMean = Double.parseDouble(stats.get(2));
                    userStdDev = Double.parseDouble(stats.get(3));
                    found = true;
                    break;
                }
            }

             if (!found) {
                continue;
            }

            double normalizedRating = Double.parseDouble(normalizedRatingStr);
            double denormalizedRating = (normalizedRating * userStdDev) + userMean;
            int finalPredictedRating = (int) Math.round(denormalizedRating);

            if (finalPredictedRating < 1) {
                finalPredictedRating = 1;
            } else if (finalPredictedRating > 5) {
                finalPredictedRating = 5;
            }
            predictedUserRating.set(2, String.valueOf(finalPredictedRating));
        }
    }

    public ArrayList<ArrayList<String>> getPredictions() {
        return this.userPredictions;
    }

    // PA 5

    public void fillInUnpredictedRatings() {
        for (ArrayList<String> row : this.userPredictions) {
            String songName = row.get(0);
            String userName = row.get(1);
            String ratingStr = row.get(2);

            if (ratingStr.equals("NaN")) {
                double songMean = getSongMean(songName);
                int numSongRatings = getNumOfSongRatings(songName);
                double userMean = getUserMean(userName);
                int numUserRatings = getNumOfUserRatings(userName);
                
                double unpredictedRating = (songMean * numSongRatings + userMean * numUserRatings) / (numSongRatings + numUserRatings);
                int finalRating = (int) Math.round(unpredictedRating);
                
                row.set(2, String.valueOf(finalRating));
            }
        }
    }

    public double getSongMean(String songName) {
        double sum = 0;
        int count = 0;
        
        for (ArrayList<String> record : this.rawData) {
            if (record.get(0).equals(songName)) {
                sum += Integer.parseInt(record.get(2));
                count++;
            }
        }
        
        if (count == 0) {
            return 0;
        }
        return sum / count;
    }

    public int getNumOfSongRatings(String songName) {
        int count = 0;
        
        for (ArrayList<String> record : this.rawData) {
            if (record.get(0).equals(songName)) {
                count++;
            }
        }
        
        return count;
    }

    public double getUserMean(String userName) {
        double sum = 0;
        int count = 0;
        
        for (ArrayList<String> record : this.rawData) {
            if (record.get(1).equals(userName)) {
                sum += Integer.parseInt(record.get(2));
                count++;
            }
        }
        
        if (count == 0) {
            return 0;
        }
        return sum / count;
    }

    public int getNumOfUserRatings(String userName) {
        int count = 0;
        
        for (ArrayList<String> record : this.rawData) {
            if (record.get(1).equals(userName)) {
                count++;
            }
        }
        
        return count;
    }

    private void calculateSongStatisticsForClustering(ArrayList<Double> songMeans, ArrayList<Double> songStdDevs) {
        for (String songName : this.songNames) {
            double sum = 0;
            int count = 0;
            
            for (ArrayList<String> row : this.completeRatingsForClustering) {
                if (row.get(0).equals(songName)) {
                    for (int i = 2; i < row.size(); i++) {
                        String ratingStr = row.get(i);
                        if (!ratingStr.equals("NaN")) {
                            sum += Double.parseDouble(ratingStr);
                            count++;
                        }
                    }
                    break;
                }
            }
            
            double songMean = sum / count;
            songMeans.add(songMean);
            
            double sumOfSquares = 0.0;
            for (ArrayList<String> row : this.completeRatingsForClustering) {
                if (row.get(0).equals(songName)) {
                    for (int i = 2; i < row.size(); i++) {
                        String ratingStr = row.get(i);
                        if (!ratingStr.equals("NaN")) {
                            double rating = Double.parseDouble(ratingStr);
                            sumOfSquares += Math.pow(rating - songMean, 2);
                        }
                    }
                    break;
                }
            }
            
            double stdDev = Math.sqrt(sumOfSquares / count);
            songStdDevs.add(stdDev);
        }
    }

    public void normalizeRatingsForClustering() {
        ArrayList<Double> songMeans = new ArrayList<>();
        ArrayList<Double> songStdDevs = new ArrayList<>();
        
        calculateSongStatisticsForClustering(songMeans, songStdDevs);
        
        for (ArrayList<String> row : this.completeRatingsForClustering) {
            String songName = row.get(0);
            int songIndex = this.songNames.indexOf(songName);
            
            for (int i = 2; i < row.size(); i++) {
                String ratingStr = row.get(i);
                if (!ratingStr.equals("NaN")) {
                    double normalizedRating = (Double.parseDouble(ratingStr) - songMeans.get(songIndex)) / songStdDevs.get(songIndex);
                    row.set(i, String.valueOf(normalizedRating));
                }
            }
        }
    }

    public void removeSongsWithOneDistinctRating(ArrayList<String> userSelections) throws RuntimeException {
        ArrayList<String> songsToRemove = new ArrayList<>();
        
        for (String songName : this.songNames) {
            ArrayList<Double> distinctRatings = new ArrayList<>();
            
            for (ArrayList<String> row : this.completeRatingsForClustering) {
                if (row.get(0).equals(songName)) {
                    for (int i = 2; i < row.size(); i++) {
                        String ratingStr = row.get(i);
                        if (!ratingStr.equals("NaN")) {
                            double rating = Double.parseDouble(ratingStr);
                            if (!distinctRatings.contains(rating)) {
                                distinctRatings.add(rating);
                            }
                        }
                    }
                    break;
                }
            }
            
            if (distinctRatings.size() <= 1) {
                if (userSelections.contains(songName)) {
                    throw new RuntimeException("selected songs must have more than one distinct rating");
                }
                songsToRemove.add(songName);
            }
        }
        
        for (String songName : songsToRemove) {
            for (int i = this.completeRatingsForClustering.size() - 1; i >= 0; i--) {
                if (this.completeRatingsForClustering.get(i).get(0).equals(songName)) {
                    this.completeRatingsForClustering.remove(i);
                    break;
                }
            }
            this.songNames.remove(songName);
        }
        
        if (this.songNames.size() < 2) {
            throw new RuntimeException("no songs to recomend. Songs may have been removed. Try with a larger file or fewer selctions.");
        }
    }

    // completeRatingsForClustering = [[songName, "", rating_user1, rating_user2, ...], [...], ...]

    public void buildCompleteRatingsMatrix() {
        this.completeRatingsForClustering = new ArrayList<>();
        
        
        for (String songName : this.songNames) {
            ArrayList<String> songRow = new ArrayList<>();
            songRow.add(songName);
            songRow.add("");
            
           
            for (int k = 0; k < this.userNames.size(); k++) {
                songRow.add("NaN");
            }
            
            this.completeRatingsForClustering.add(songRow);
        }
        
        
        for (ArrayList<String> rawRow : this.rawData) {
            String songName = rawRow.get(0);
            String userName = rawRow.get(1);
            String rating = rawRow.get(2);
            
            int songIndex = this.songNames.indexOf(songName);
            int userIndex = this.userNames.indexOf(userName);
            
            if (songIndex != -1 && userIndex != -1) {
                this.completeRatingsForClustering.get(songIndex).set(userIndex + 2, rating);
            }
        }
        
       
        for (ArrayList<String> predRow : this.userPredictions) {
            String songName = predRow.get(0);
            String userName = predRow.get(1);
            String ratingStr = predRow.get(2);
            
            if (!ratingStr.equals("NaN")) {
                int songIndex = this.songNames.indexOf(songName);
                int userIndex = this.userNames.indexOf(userName);
                
                if (songIndex != -1 && userIndex != -1) {
                    this.completeRatingsForClustering.get(songIndex).set(userIndex + 2, ratingStr);
                }
            }
        }
    }

    public ArrayList<ArrayList<String>> getCompleteRatingsForClustering() {
        return this.completeRatingsForClustering;
    }

    // clusters = [Cluster1, Cluster2, ...]

    public void initializeClustersFromSelections(ArrayList<String> userSelections) throws RuntimeException {
        this.clusters = new ArrayList<>();
        
        for (String selectedSongName : userSelections) {
            Cluster cluster = new Cluster();
            
            for (ArrayList<String> row : this.completeRatingsForClustering) {
                if (row.get(0).equals(selectedSongName)) {
                    ArrayList<Double> centroid = new ArrayList<>();
                    
                    for (int i = 2; i < row.size(); i++) {
                        centroid.add(Double.parseDouble(row.get(i)));
                    }
                    
                    cluster.setCentroid(centroid);
                    break;
                }
            }
            
            this.clusters.add(cluster);
        }
    }

    public void kmeansClustering() {
        for (int iteration = 0; iteration < 10; iteration++) {
            for (Cluster cluster : this.clusters) {
                cluster.clearSongs();
            }
            
            for (ArrayList<String> row : this.completeRatingsForClustering) {
                String songName = row.get(0);
                ArrayList<Double> songRatings = new ArrayList<>();
                
                for (int i = 2; i < row.size(); i++) {
                    songRatings.add(Double.parseDouble(row.get(i)));
                }
                
                int closestClusterIndex = 0;
                double closestDistance = calculateEuclideanDistance(songRatings, this.clusters.get(0).getCentroid());
                
                for (int i = 1; i < this.clusters.size(); i++) {
                    double distance = calculateEuclideanDistance(songRatings, this.clusters.get(i).getCentroid());
                    
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestClusterIndex = i;
                    }
                }
                
                this.clusters.get(closestClusterIndex).addSong(songName);
            }
            
            for (Cluster cluster : this.clusters) {
                ArrayList<Double> newCentroid = calculateNewCentroid(cluster);
                cluster.setCentroid(newCentroid);
            }
        }
    }


    private double calculateEuclideanDistance(ArrayList<Double> vector1, ArrayList<Double> vector2) {
        double sumOfSquares = 0.0;
        
        for (int i = 0; i < vector1.size(); i++) {
            double diff = vector1.get(i) - vector2.get(i);
            sumOfSquares += diff * diff;
        }
        
        return Math.sqrt(sumOfSquares);
    }

    private ArrayList<Double> calculateNewCentroid(Cluster cluster) {
        ArrayList<String> songsInCluster = cluster.getSongs();
        ArrayList<Double> newCentroid = new ArrayList<>();
        
        if (songsInCluster.size() == 0) {
            return cluster.getCentroid();
        }
        
        int numDimensions = this.userNames.size();
        
        for (int i = 0; i < numDimensions; i++) {
            double sum = 0.0;
            
            for (String songName : songsInCluster) {
                for (ArrayList<String> row : this.completeRatingsForClustering) {
                    if (row.get(0).equals(songName)) {
                        sum += Double.parseDouble(row.get(i + 2));
                        break;
                    }
                }
            }
            
            newCentroid.add(sum / songsInCluster.size());
        }
        
        return newCentroid;
    }

    public ArrayList<Cluster> getClusters() {
        return this.clusters;
    }

    public ArrayList<ArrayList<String>> generateClusteringRecommendations(ArrayList<String> userSelections) {
        ArrayList<ArrayList<String>> recommendations = new ArrayList<>();
        
        for (int i = 0; i < userSelections.size(); i++) {
            String userChoice = userSelections.get(i);
            ArrayList<String> songsInCluster = this.clusters.get(i).getSongs();
            
            for (String songName : songsInCluster) {
                if (!userSelections.contains(songName)) {
                    ArrayList<String> recommendation = new ArrayList<>();
                    recommendation.add(userChoice);
                    recommendation.add(songName);
                    recommendations.add(recommendation);
                }
            }
        }
        
        for (int i = 0; i < recommendations.size() - 1; i++) {
            for (int j = 0; j < recommendations.size() - i - 1; j++) {
                ArrayList<String> current = recommendations.get(j);
                ArrayList<String> next = recommendations.get(j + 1);
                
                int userChoiceComparison = current.get(0).compareTo(next.get(0));
                
                if (userChoiceComparison > 0) {
                    recommendations.set(j, next);
                    recommendations.set(j + 1, current);
                } else if (userChoiceComparison == 0) {
                    int songComparison = current.get(1).compareTo(next.get(1));
                    
                    if (songComparison > 0) {
                        recommendations.set(j, next);
                        recommendations.set(j + 1, current);
                    }
                }
            }
        }
        return recommendations;
    }

    public void validateUserProvidedSongs(ArrayList<String> userProvidedSongs) throws RuntimeException {
        for (String songName : userProvidedSongs) {
            if (!this.songNames.contains(songName)) {
                throw new RuntimeException("song not found in available songs");
            }
        }
    }

    public ArrayList<String> generatePlaylist(ArrayList<String> allUserProvidedSongs) {
        ArrayList<ArrayList<String>> candidateSongs = new ArrayList<>();
        
        for (String likedSongName : allUserProvidedSongs) {
            int clusterIndex = -1;
            for (int i = 0; i < this.clusters.size(); i++) {
                if (this.clusters.get(i).getSongs().contains(likedSongName)) {
                    clusterIndex = i;
                    break;
                }
            }
            
            if (clusterIndex == -1) {
                continue;
            }
            
            ArrayList<Double> likedSongRatings = getSongRatingsVector(likedSongName);
            ArrayList<String> songsInCluster = this.clusters.get(clusterIndex).getSongs();
            
            for (String candidateSongName : songsInCluster) {
                if (!candidateSongName.equals(likedSongName)) {
                    ArrayList<Double> candidateRatings = getSongRatingsVector(candidateSongName);
                    double distance = calculateEuclideanDistance(candidateRatings, likedSongRatings);
                    
                    double similarityScore;
                    if (distance == 0.0) {
                        similarityScore = 90000000.0;
                    } else {
                        similarityScore = 1.0 / distance;
                    }
                    
                    boolean found = false;
                    for (ArrayList<String> candidate : candidateSongs) {
                        if (candidate.get(0).equals(candidateSongName)) {
                            double existingInverseDistance = Double.parseDouble(candidate.get(1));
                            if (similarityScore > existingInverseDistance) {
                                candidate.set(1, String.valueOf(similarityScore));
                            }
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        ArrayList<String> newCandidate = new ArrayList<>();
                        newCandidate.add(candidateSongName);
                        newCandidate.add(String.valueOf(similarityScore));
                        candidateSongs.add(newCandidate);
                    }
                }
            }
        }
        
        
        for (int i = candidateSongs.size() - 1; i >= 0; i--) {
            if (allUserProvidedSongs.contains(candidateSongs.get(i).get(0))) {
                candidateSongs.remove(i);
            }
        }
        
      
        for (int i = 0; i < candidateSongs.size() - 1; i++) {
            for (int j = 0; j < candidateSongs.size() - i - 1; j++) {
                double dist1 = Double.parseDouble(candidateSongs.get(j).get(1));
                double dist2 = Double.parseDouble(candidateSongs.get(j + 1).get(1));
                
                if (dist1 < dist2) {
                    ArrayList<String> temp = candidateSongs.get(j);
                    candidateSongs.set(j, candidateSongs.get(j + 1));
                    candidateSongs.set(j + 1, temp);
                }
            }
        }
        
       
        ArrayList<String> playlist = new ArrayList<>();
        int maxPlaylistSize = Math.min(20, candidateSongs.size());
        for (int i = 0; i < maxPlaylistSize; i++) {
            playlist.add(candidateSongs.get(i).get(0));
        }
        
        return playlist;
    }

    private ArrayList<Double> getSongRatingsVector(String songName) {
        ArrayList<Double> ratings = new ArrayList<>();
        
        for (ArrayList<String> row : this.completeRatingsForClustering) {
            if (row.get(0).equals(songName)) {
                for (int i = 2; i < row.size(); i++) {
                    ratings.add(Double.parseDouble(row.get(i)));
                }
                break;
            }
        }
        
        return ratings;
    }
}
