package stepDefinitions;

import io.cucumber.java.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources.testUtils.CommonUtils;

import java.io.File;
import java.util.concurrent.*;

public class Hooks extends CommonUtils {
    private static int scenarioCount = 0;
    private static int completedScenarios = 0;
    private static final Logger LOGGER = LoggerFactory.getLogger(Hooks.class);


/*    private final ScheduledExecutorService scheduler;

    public Hooks() {
//        Used to create an instance of ScheduledExecutorService, which is a part of the Java concurrency framework.
//        This code creates a thread pool with a single thread that can schedule tasks to run after a certain delay or execute periodically.
        this.scheduler = Executors.newScheduledThreadPool(1);
    }   */

    @Before(order = 0)
    public void beforeScenarios() {
        scenarioCount++;
    }

    @Before(order = 1)
    public void envInfo(Scenario scenario) {
        LOGGER.info("Running scenario: {}. Running environment: {}", scenario.getName(), System.getProperty("environment"));
    }



    @After(order = 0)
    public void getFailedScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            LOGGER.error("Failed scenario: {}", scenario.getName());
        }
    }

    @After(order = 1)
    public void afterScenarios() {
        completedScenarios++;
        if (completedScenarios == scenarioCount) {
            LOGGER.info("Scenario completed!...");
        }
    }



    /*    @After(value = "@PortalLoginVAPTsc2", order = 1)
    public void checkAndShutdownScheduler() {
            long scheduledTime = 60;
//        Shut down the scheduler after your tests are done.
            try {
                if (!scheduler.awaitTermination(scheduledTime + 10, TimeUnit.SECONDS)) {
//                Force shutdown if not terminated within the scheduled time + buffer
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            System.out.println("Waited for" + scheduledTime + "seconds<-------------------->");
            System.out.println("Scheduler shut down after session expiration check.<-------------------->");
    }   */

}
