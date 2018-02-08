import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            case "END":
                System.exit(0);
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

    public static void main(String args[]) {
        DependencyExample dependencyExample = new DependencyExample();
        String str = "";
        try (InputStream inputStream = dependencyExample.getClass().getClassLoader().getResourceAsStream("Progra.dat");
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            while ((str = bufferedReader.readLine()) != null) {
                dependencyExample.executeCommand(str.trim().split(" "));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
