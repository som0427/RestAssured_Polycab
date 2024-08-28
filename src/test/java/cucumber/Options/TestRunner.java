package cucumber.Options;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/java/features/Mobile_App", "src/test/java/features/Portal"},
        glue = {"stepDefinitions"},
        tags = "@login",
        dryRun = false,
        plugin = {"pretty", "rerun:target/rerun.txt", "timeline:target/timeline",
                "html:target/htmlReports/cucumber-reports.html", "json:target/jsonReports/cucumber-reports.json"},
        monochrome=true
)
public class TestRunner {
}


//rerun:- The rerun plugin generates a file (target/rerun.txt) containing the paths to any failed scenarios.
// This allows you to easily rerun only the failed scenarios.

//timeline:- The timeline plugin generates a visual timeline report that shows when each test scenario started and finished.
