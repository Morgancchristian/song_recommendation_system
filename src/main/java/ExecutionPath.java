public class ExecutionPath {
    private String argument2;

    public ExecutionPath(String argument2) {
        this.argument2 = argument2;
    }

    public void setArg2(String argument2) {
        this.argument2 = argument2;
    }

    public String getArg2() {
        return this.argument2;
    }

    public int executionPath() {
        if ("-a".equals(this.argument2)) {
            return 1;
        }

        if ("-u".equals(this.argument2)) {
            return 2;
        }

        if ("-p".equals(this.argument2)) {
            return 3;
        }
        
        if ("-r".equals(this.argument2)) {
            return 4;
        }
        
        if ("-s".equals(argument2)) {
            return 5;
        }
        
        return 0;
    }
}
