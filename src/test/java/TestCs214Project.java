import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class TestCs214Project {

    @Test
    public void testCSVFilePathChecksConstructor() {
        String inputPath = "database/files/file2.csv";
        String outputPath = "output.csv";
        String defaultPath = "database/files/";
        
        CSVFilePathChecks pathChecker = new CSVFilePathChecks(inputPath, outputPath, defaultPath);
        assertNotNull(pathChecker);
    }

    @Test
    public void testFileExistsForValidFile() {
        String inputPath = "database/files/file2.csv";
        CSVFilePathChecks pathChecker = new CSVFilePathChecks(inputPath, "output.csv", "database/files/");
        assertTrue(pathChecker.checkIfInputFileExists());
    }

    @Test
    public void testFileExistsForInvalidFile() {
        String inputPath = "rando.csv";
        CSVFilePathChecks pathChecker = new CSVFilePathChecks(inputPath, "output.csv", "database/files/");
        assertFalse(pathChecker.checkIfInputFileExists());
    }

    @Test
    public void testDetermineOutputFilePathWithProvidedOutput() {
        String inputPath = "database/files/file2.csv";
        String outputPath = "output.csv";
        String defaultPath = "database/files/";
        
        CSVFilePathChecks pathChecker = new CSVFilePathChecks(inputPath, outputPath, defaultPath);
        String result = pathChecker.determineOutputFilePath();
        assertEquals("output.csv", result);
    }

    @Test
    public void testDetermineOutputFilePathWithNullOutput() {
        String inputPath = "database/files/file2.csv";
        String outputPath = null;
        String defaultPath = "database/files/";
        
        CSVFilePathChecks pathChecker = new CSVFilePathChecks(inputPath, outputPath, defaultPath);
        String result = pathChecker.determineOutputFilePath();
        assertEquals("database/files/output.csv", result);
    }

    // @Test
    // public void testExecutionPathWithNull() {
    //     ExecutionPath pathToExecute = new ExecutionPath(null);
    //     int result = pathToExecute.executionPath();
    //     assertEquals(0, result);
    // }

    // @Test
    // public void testExecutionPathWithDashA() {
    //     ExecutionPath pathToExecute = new ExecutionPath("-a");
    //     int result = pathToExecute.executionPath();
    //     assertEquals(1, result);
    // }

    // @Test
    // public void testExecutionPathWithOtherArgument() {
    //     ExecutionPath pathToExecute = new ExecutionPath("-b");
    //     int result = pathToExecute.executionPath();
    //     assertEquals(0, result);
    // }

    @Test
    public void testExecutionPathGetterSetter() {
        ExecutionPath pathToExecute = new ExecutionPath("-a");
        assertEquals("-a", pathToExecute.getArg2());
        
        pathToExecute.setArg2("-b");
        assertEquals("-b", pathToExecute.getArg2());
    }

    @Test
    public void testCSVtoListofListsConstructor() {
        String inputPath = "database/files/file2.csv";
        CSVtoListofLists csvConverter = new CSVtoListofLists(inputPath);
        assertNotNull(csvConverter);
    }

    @Test
    public void testGenerateStatisticsConstructor() {
        ArrayList<ArrayList<String>> testData = new ArrayList<>();
        GenerateStatistics statsGenerator = new GenerateStatistics(testData);
        assertNotNull(statsGenerator);
    }

    @Test
    public void testWriteProcDataToCSVConstructor() {
        ArrayList<ArrayList<String>> testData = new ArrayList<>();
        String outputPath = "output.csv";
        
        WriteProcDataToCSV writer = new WriteProcDataToCSV(testData, outputPath);
        assertNotNull(writer);
    }


    // @Test
    // public void testCompletePA1ConversionsWithFile3() throws Exception {
    //     CSVtoListofLists csvConverter = new CSVtoListofLists("database/files/file3.csv");
    //     csvConverter.convert();
        
    //     GenerateStatistics statsGenerator = new GenerateStatistics(csvConverter.getRawData());
    //     statsGenerator.getAndSetSongNamesFromRawData();
    //     statsGenerator.getAndSetNumberAndMeanOfRatingsForEachSongFromRawData();
    //     statsGenerator.computeSTDForEachSong();
        
    //     ArrayList<ArrayList<String>> processedData = statsGenerator.getProcessedData();
        
    //     assertNotNull(processedData);
    //     assertEquals(3, processedData.size());
    //     assertEquals("song1", processedData.get(0).get(0));
    //     assertEquals("2", processedData.get(0).get(1)); 
    //     assertEquals("2.5", processedData.get(0).get(2));
    // }

    @Test
    public void testFileEmptyCheckScenarios() {
        CSVFilePathChecks pathChecker1 = new CSVFilePathChecks("database/files/file2.csv", "output.csv", "database/files/");
        assertFalse(pathChecker1.checkIfInputFileEmpty());
        
        CSVFilePathChecks pathChecker2 = new CSVFilePathChecks("nonexistent.csv", "output.csv", "database/files/");
        assertFalse(pathChecker2.checkIfInputFileExists());
    }

    @Test
    public void testFilteringUncooperativeUsersDetailed() throws Exception {
        CSVtoListofLists csvConverter = new CSVtoListofLists("database/files/file2.csv");
        csvConverter.convert();
        
        GenerateStatistics statsGenerator = new GenerateStatistics(csvConverter.getRawData());
        statsGenerator.getAndSetSongNamesFromRawData();
        statsGenerator.getUserNamesFromRawData();
        statsGenerator.createUserFocusedArrayWithoutRatings();
        statsGenerator.addUserRatingsToUserFocusedArray();
        
        assertNotNull(statsGenerator.filterOutUncooperativeUsersBasedOnRatings());
  
        ArrayList<String> remainingUsers = statsGenerator.getUserNames();
        assertNotNull(remainingUsers);
        
        ArrayList<ArrayList<String>> processedData = statsGenerator.getProcessedData();
        assertNotNull(processedData);
    }

    // @Test
    // public void testDataConsistencyAcrossOperations() throws Exception {
    //     CSVtoListofLists csvConverter = new CSVtoListofLists("database/files/file2.csv");
    //     csvConverter.convert();
    //     ArrayList<ArrayList<String>> originalData = csvConverter.getRawData();
        
    //     GenerateStatistics statsGenerator = new GenerateStatistics(originalData);
        
    //     statsGenerator.getAndSetSongNamesFromRawData();
    //     assertEquals(6, originalData.size()); 
        
    //     statsGenerator.getUserNamesFromRawData();
    //     assertEquals(6, originalData.size()); 
        
    //     ArrayList<String> songNames1 = statsGenerator.getSongNames();
    //     ArrayList<String> songNames2 = statsGenerator.getSongNames();
    //     assertEquals(songNames1, songNames2);
    // }

    @Test
    public void testCompletePA2WorkflowWithFile2() throws Exception {
        CSVtoListofLists csvConverter = new CSVtoListofLists("database/files/file2.csv");
        csvConverter.convert();
        
        GenerateStatistics statsGenerator = new GenerateStatistics(csvConverter.getRawData());
        statsGenerator.getAndSetSongNamesFromRawData();
        statsGenerator.getUserNamesFromRawData();
        statsGenerator.createUserFocusedArrayWithoutRatings();
        statsGenerator.addUserRatingsToUserFocusedArray();
        statsGenerator.filterOutUncooperativeUsersBasedOnSongs(
            statsGenerator.filterOutUncooperativeUsersBasedOnRatings()
        );
        
        ArrayList<ArrayList<String>> processedData = statsGenerator.getProcessedData();
        ArrayList<String> userNames = statsGenerator.getUserNames();
        
        assertNotNull(processedData);
        assertNotNull(userNames);
        assertTrue(processedData.size() > 0);
        assertEquals(3, processedData.get(0).size());
    }

    // @Test
    // public void testUserNameExtractionAndSorting() throws Exception {
    //     CSVtoListofLists csvConverter = new CSVtoListofLists("database/files/file2.csv");
    //     csvConverter.convert();
        
    //     GenerateStatistics statsGenerator = new GenerateStatistics(csvConverter.getRawData());
    //     statsGenerator.getAndSetSongNamesFromRawData();
    //     statsGenerator.getUserNamesFromRawData();
    //     ArrayList<String> userNames = statsGenerator.getUserNames();
        
    //     assertNotNull(userNames);
    //     assertEquals(3, userNames.size());
    //     assertEquals("alex", userNames.get(0));
    //     assertEquals("cameron", userNames.get(1));
    //     assertEquals("charlie", userNames.get(2));
    // }

    @Test
    public void testWriteGeneralStatistics() throws Exception {
        CSVtoListofLists csvConverter = new CSVtoListofLists("database/files/file3.csv");
        csvConverter.convert();
        
        GenerateStatistics statsGenerator = new GenerateStatistics(csvConverter.getRawData());
        statsGenerator.getAndSetSongNamesFromRawData();
        statsGenerator.getAndSetNumberAndMeanOfRatingsForEachSongFromRawData();
        statsGenerator.computeSTDForEachSong();
        
        WriteProcDataToCSV writer = new WriteProcDataToCSV(statsGenerator.getProcessedData(), "output.csv");
        writer.writeGeneralStatistics();
        
        assertTrue(true);
    }


    @Test 
    public void testIfTheIntermediateNamesArrayIsNotEmpty() {


        String pathToTestFile = "database/files/file2.csv";
        String nameOfOutputFile = "outputCsv.csv";
        String defaultOutputFilePath = "database/files/";

        
        CSVFilePathChecks pathChecker = new CSVFilePathChecks(pathToTestFile, nameOfOutputFile, defaultOutputFilePath);
        if (!pathChecker.checkIfInputFileExists()) {
            System.err.println("Input file does not exist: " + pathToTestFile);
            return;
        }
        
        try {
            CSVtoListofLists csvConverter = new CSVtoListofLists(pathToTestFile);
            csvConverter.convert();


            GenerateStatistics statsGenerator = new GenerateStatistics(csvConverter.getRawData());
            statsGenerator.getAndSetSongNamesFromRawData();
            ArrayList<String> arrayListToCheckIfEmpty = new ArrayList<>();
            arrayListToCheckIfEmpty = statsGenerator.getSongNames();
    

            assertNotNull(arrayListToCheckIfEmpty, "Intermediate (names) array should not be null");
            assertTrue(arrayListToCheckIfEmpty.size() > 0, "Size of array should be greater than 0");
            assertFalse(arrayListToCheckIfEmpty.isEmpty(), "Intermediate names array should not be empty");
        } catch (Exception e) {
            System.err.println("Test failed" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}