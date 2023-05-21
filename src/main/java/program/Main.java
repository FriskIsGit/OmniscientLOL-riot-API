package program;

public class Main{
    public static void main(String[] args){
        CLIProgram program = new CLIProgram();
        program.earlyInitialize();
        program.printCommands();
        program.execute();
    }
}
