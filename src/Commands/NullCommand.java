package Commands;

public class NullCommand implements Command {

    @Override
    public String execute(String arguments) {
        return "Command not recognized: " + arguments;
    }

    @Override
    public String getManualStatement() {
        return "Null Command - returned when a command is unrecognized";
    }
}
