package Commands;

public interface Command {

    public void initialize(String command, String subcommand, String... arguments);

    public String execute();


}
