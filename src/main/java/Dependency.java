import java.util.HashSet;

public class Dependency {
    String dependencyName;
    HashSet<String> dependsOnSet = new HashSet<>();
    boolean isInstalled = false;

    public String toString() {
        return "> DependencyName: " + dependencyName +
                "> ChildrenDependencies: [" + dependsOnSet.stream().reduce("", (s, s2) -> s2 +" | "+ s) + "] " +
                "> IsInstalled: " + isInstalled;

    }
}
