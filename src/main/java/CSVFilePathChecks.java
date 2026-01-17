public class CSVFilePathChecks{
    private String argument0;
    private String argument1;
    private String defaultOutputFilePath;


    public CSVFilePathChecks(String argument0, String argument1, String defaultOutputFilePath) {
        this.argument0 = argument0;
        this.argument1 = argument1;
        this.defaultOutputFilePath = defaultOutputFilePath;
    }


    public boolean checkIfInputFileExists() {
       java.io.File file = new java.io.File(this.argument0);
       return file.exists();
    }

    public boolean checkIfInputFileEmpty() {
        java.io.File file = new java.io.File(this.argument0);
        if (file.length() == 0) {
            return true;
        }
        return false;
    }


    public String determineOutputFilePath() {
        if (this.argument1 == null) {
            return this.defaultOutputFilePath + "output.csv";
        } else {
            return this.argument1;
        }
    }
}
