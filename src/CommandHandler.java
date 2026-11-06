import Commands.Command;

public class CommandHandler {

    private CommandHandler() {}

    private static Command parseCommand(String str) {

        int idx = str.indexOf(" ");

        if (idx == -1) {
            return
        }

        switch (str) {
            case "help" -> return new HelpCommand()
        }
    }

}