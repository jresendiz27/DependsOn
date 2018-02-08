import java.util.ArrayList;
import java.util.Arrays;

public class DependencyExample {
    DependencyManager dependencyManager;

    public DependencyExample() {
        dependencyManager = new DependencyManager();
    }

    public void executeCommand(String... args) throws Exception {
        String command = args[0];
        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
        switch (command) {
            case "DEPEND":
                dependencyManager.associateDependency(argsList.get(0), new ArrayList<>(argsList.subList(1, argsList.size())));
                break;
            case "INSTALL":
                dependencyManager.installDependency(argsList.get(0));
                break;
            case "REMOVE":
                dependencyManager.removeDependency(argsList.get(0));
                break;
            case "LIST":
                dependencyManager.listDependencies();
                break;
            default:
                throw new Exception("UNKNOWN COMMAND");
        }
    }

    public void executeScript(String script) throws Exception {
        String[] lines = script.split("\n");
        for (String line : lines) {
            System.out.println(line);
            String[] args = line.trim().split(" ");
            executeCommand(args);
        }
    }
}
