import java.util.ArrayList;
import java.util.TreeMap;

public class DependencyManager {
    public TreeMap<String, Dependency> dependenciesTreeMap;

    public DependencyManager() {
        dependenciesTreeMap = new TreeMap<>();
    }

    public void removeDependency(String dependencyName) throws Exception {
        Dependency dependency = verifyDependency(dependencyName);
        if (dependency == null) {
            throw new Exception("DEPENDENCY DOES NOT EXIST!");
        }

        if (dependency.isInstalled) {
            // look for installed dependencies and remove if there's no conflict
            boolean isUsed = false;
            // find in all the elements who depends on me
            for (Dependency currentDependency : dependenciesTreeMap.values()) {
                if (currentDependency.dependsOnSet.contains(dependencyName)) {
                    isUsed = true;
                    break;
                }
            }

            if (!isUsed) {
                dependency.isInstalled = false;
                dependenciesTreeMap.put(dependencyName, dependency);
                System.out.println("Removing " + dependencyName);
            } else {
                System.out.println(dependencyName + " is still needed");
            }
        } else {
            // remove without pain
            dependency.isInstalled = false;
            dependenciesTreeMap.put(dependencyName, dependency);
            System.out.println("Removing " + dependencyName);
        }
    }

    public void associateDependency(String dependencyName, ArrayList<String> children) {
        Dependency dependency = verifyDependency(dependencyName);

        for (String childrenDependencyName : children) {
            Dependency associatedDependency = verifyDependency(childrenDependencyName);
            if (!dependency.dependsOnSet.contains(childrenDependencyName)) {
                dependency.dependsOnSet.add(childrenDependencyName);
            }
            dependenciesTreeMap.put(childrenDependencyName, associatedDependency);
        }

        dependenciesTreeMap.put(dependencyName, dependency);
    }

    private Dependency verifyDependency(String dependencyName) {
        Dependency dependency = dependenciesTreeMap.get(dependencyName);
        if (dependency == null) {
            dependency = new Dependency();
            dependency.isInstalled = false;
            dependency.dependencyName = dependencyName;
        }
        return dependency;
    }

    public void installDependency(String dependencyName) {
        Dependency dependency = dependenciesTreeMap.get(dependencyName);
        if (dependency != null) {
            // TODO looking only for first level dependencies, WIP
            for (String childrenDependencyName : dependency.dependsOnSet) {
                Dependency childrenDependency = dependenciesTreeMap.get(childrenDependencyName);
                if (!childrenDependency.isInstalled) {
                    childrenDependency.isInstalled = true;
                    dependenciesTreeMap.put(childrenDependencyName, childrenDependency);
                    System.out.println("Installing " + childrenDependencyName);
                }
            }
        } else {
            associateDependency(dependencyName, new ArrayList<>());
            dependency = verifyDependency(dependencyName);
        }
        if (dependency.isInstalled) {
            System.out.println(dependencyName + "is already installed.");
        } else {
            dependency.isInstalled = true;
            dependenciesTreeMap.put(dependencyName, dependency);
            System.out.println("Installing " + dependencyName);
        }


    }

    public String listDependencies() {
        ArrayList<String> keys = new ArrayList<>(dependenciesTreeMap.keySet());
        return String.join("\n", keys);
    }
}
