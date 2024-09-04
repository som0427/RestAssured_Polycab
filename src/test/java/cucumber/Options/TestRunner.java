package cucumber.Options;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(TestRunner.class);
    @AfterClass
    public static void tearDown() {
        String className = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        LOGGER.info("All tags executed for class: {}", className);
        RestAssured.reset();    //clean up any resource utilized during test
        LOGGER.info("All Features and Scenarios under test are completed!...");
    }

}


//rerun:- The rerun plugin generates a file (target/rerun.txt) containing the paths to any failed scenarios.
// This allows you to easily rerun only the failed scenarios.

//timeline:- The timeline plugin generates a visual timeline report that shows when each test scenario started and finished.
