import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVtoListofLists {

    private static CSVtoListofLists instance;
    private String inputFilePath;
    private ArrayList<ArrayList<String>> rawData;

    public CSVtoListofLists(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public static CSVtoListofLists getInstance(String inputFilePath) {
        if (instance == null) {
            instance = new CSVtoListofLists(inputFilePath);
        }
        return instance;
    }

    public void convert() throws Exception {
        Scanner scanner = new Scanner(new File(this.inputFilePath));
        rawData = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split(",");
            
            if (values.length != 3) {
                scanner.close();
                throw new Exception("CSV row ranges are unexpected.");
            }

            String ratingCheck1 = values[2].trim();
            if (ratingCheck1.contains(".")) {
                scanner.close();
                throw new Exception("The ratings bust be an integer.");
            }

            int ratingCheck2 = Integer.parseInt(ratingCheck1);
            if (ratingCheck2 < 1 || ratingCheck2 > 5) {
                scanner.close();
                throw new Exception("Rating must be between 1 and 5.");
            }

            ArrayList<String> row = new ArrayList<>();
            for (String value : values) {
                row.add(value.trim());
            }
            rawData.add(row);
        }
        scanner.close();
    }

    public ArrayList<ArrayList<String>> getRawData() {
        return rawData;
    }
}
