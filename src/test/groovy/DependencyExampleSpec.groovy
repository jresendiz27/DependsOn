import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class DependencyExampleSpec extends Specification {
    @Shared
    DependencyExample dependsOn = new DependencyExample();

    void "Should add a dependency and create the structure for it children"() {
        given:
        String dependenciesCreationScript = """DEPEND TELNET TCPIP NETCARD
DEPEND TCPIP NETCARD
DEPEND DNS TCPIP NETCARD
DEPEND BROWSER TCPIP HTML"""

        dependsOn.executeScript(dependenciesCreationScript);

        expect:
        dependsOn.dependencyManager.dependenciesTreeMap["TELNET"].dependsOnSet == ["TCPIP", "NETCARD"] as HashSet
        dependsOn.dependencyManager.dependenciesTreeMap["DNS"].dependsOnSet == ["TCPIP", "NETCARD"] as HashSet
        dependsOn.dependencyManager.dependenciesTreeMap["BROWSER"].dependsOnSet == ["TCPIP", "HTML"] as HashSet
    }

    void "Should install a dependency"() {
        given:
        String dependenciesInstallScript = """INSTALL NETCARD
INSTALL TELNET
INSTALL foo"""

        dependsOn.executeScript(dependenciesInstallScript);

        expect:
        dependsOn.dependencyManager.dependenciesTreeMap["NETCARD"].isInstalled
        dependsOn.dependencyManager.dependenciesTreeMap["TELNET"].isInstalled
        dependsOn.dependencyManager.dependenciesTreeMap["TCPIP"].isInstalled
        dependsOn.dependencyManager.dependenciesTreeMap["foo"].isInstalled
    }

    void "Should list all dependencies"() {
        expect:
        "BROWSER\n" +
                "DNS\n" +
                "HTML\n" +
                "NETCARD\n" +
                "TCPIP\n" +
                "TELNET\n" +
                "foo" == dependsOn.dependencyManager.listDependencies()
    }

    void "Should remove a dependency with conflicts"() {
        given:
        String dependenciesInstallScript = """REMOVE TELNET
REMOVE NETCARD
REMOVE BROWSER
"""
        dependsOn.executeScript(dependenciesInstallScript);

        expect:
        !dependsOn.dependencyManager.dependenciesTreeMap["TELNET"].isInstalled
        dependsOn.dependencyManager.dependenciesTreeMap["NETCARD"].isInstalled
    }
}
