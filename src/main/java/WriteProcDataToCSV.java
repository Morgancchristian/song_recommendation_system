import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteProcDataToCSV {

    private ArrayList<ArrayList<String>> processedData;
    private String outputFilePath;
    private ArrayList<ArrayList<String>> elucidianSimilarities;
    private ArrayList<ArrayList<String>> userPredictions;
    private ArrayList<String> playlist;
    private boolean isPlaylist;

    public WriteProcDataToCSV(ArrayList<ArrayList<String>> processedData, String outputFilePath) {
        this.processedData = processedData;
        this.outputFilePath = outputFilePath;
        this.isPlaylist = false;
    }

    public WriteProcDataToCSV(ArrayList<ArrayList<String>> processedData, String outputFilePath, ArrayList<ArrayList<String>> elucidianSimilarities) {
        this.processedData = processedData;
        this.outputFilePath = outputFilePath;
        this.elucidianSimilarities = elucidianSimilarities;
        this.isPlaylist = false;
    }

    public WriteProcDataToCSV(String outputFilePath, ArrayList<ArrayList<String>> userPredictions) {
        this.outputFilePath = outputFilePath;
        this.userPredictions = userPredictions;
        this.isPlaylist = false;
    }

    public WriteProcDataToCSV(ArrayList<String> playlist, String outputFilePath, int try_to_fix_erasure_error_flag) {
        this.playlist = playlist;
        this.outputFilePath = outputFilePath;
        this.isPlaylist = true;
    }

    public void writeGeneralStatistics() throws IOException {
        try (FileWriter writer = new FileWriter(this.outputFilePath)) {
            writer.append("song,number of ratings,mean,standard deviation\n");
            for (ArrayList<String> row : this.processedData) {
                writer.append(String.join(",", row));
                writer.append("\n");
            }
        }
    }

    public void writeUserStatistics() throws IOException {
        try (FileWriter writer = new FileWriter(this.outputFilePath)) {
            writer.append("username,song,rating\n");
            for (ArrayList<String> row : this.processedData) {
                writer.append(String.join(",", row));
                writer.append("\n");
            }
        }
    }

    public void writeElucidianSimilarity() throws IOException {
        try (FileWriter writer = new FileWriter(this.outputFilePath)) {
            writer.append("name1,name2,similarity\n");
            for (ArrayList<String> row : this.elucidianSimilarities) {
                writer.append(row.get(0)).append(",").append(row.get(1)).append(",").append(row.get(2));
                writer.append("\n");
            }
        }
    }

    public void writeUserPredictions() throws IOException {
        try (FileWriter writer = new FileWriter(this.outputFilePath)) {
            writer.append("song,user,predicted rating\n");
            for (ArrayList<String> row : this.userPredictions) {
                writer.append(row.get(0)).append(",").append(row.get(1)).append(",").append(row.get(2));
                writer.append("\n");
            }
        }
    }

    public void writeClusteringRecommendations() throws IOException {
        try (FileWriter writer = new FileWriter(this.outputFilePath)) {
            writer.append("user choice,recommendation\n");
            for (ArrayList<String> row : this.processedData) {
                writer.append(row.get(0)).append(",").append(row.get(1));
                writer.append("\n");
            }
        }
    }

    public void writePlaylist() throws IOException {
        try (FileWriter writer = new FileWriter(this.outputFilePath)) {
            for (String song : this.playlist) {
                writer.append(song);
                writer.append("\n");
            }
        }
    }
}
