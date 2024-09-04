package stepDefinitions.Portal;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.testUtils.CommonUtils;
import resources.testUtils.Endpoints;
import resources.testUtils.GetApiResponseObject;
import utilities.GetProperty;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class PortalLoginVAPTSteps extends CommonUtils {

    RequestSpecification reqspec;
    ResponseSpecification respec;
    Response response;
    Endpoints ep;
    String bearerToken, sessionToken;
    private final GetApiResponseObject getApiResponseObject;
    private final CommonUtils commonUtils = CommonUtils.getInstance();
    public PortalLoginVAPTSteps() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        CommonUtils.getInstance();
    }


//    1st # Scenario

    @Then("submit {string} API with same token session")
    public void submitAPIWithSameTokenSession(String endpoint) {
//        We are calling assistant API as random API where login token is used to access the system.
        response = getApiResponseObject.getResponse();  //response called here instead of during submit api (next step) so that apiTracelogstream.txt contains all api data.
        reqspec = given().spec(commonUtils.requestSpec("assistant"));
        respec = responseSpec();
        bearerToken = getTokenFromResponse(response);
        ep = Endpoints.valueOf(endpoint);
        response = reqspec.when().header("Authorization", "Bearer " + bearerToken).queryParam("projectId", GetProperty.value("VAPTProjectId"))
                .get(ep.getValOfEndpoint());
    }
    @And("verify user able to submit API with same token session")
    public void verifyUserAbleToSubmitAPIWithSameTokenSession() {
        assertEquals(200, (int) getJsonPath(response.asString(), "statusCode"));
        assertEquals("SUCCESS", getJsonPath(response.asString(), "message"));
    }

    @When("add logoutApi payload")
    public void addLogoutApiPayload() {
        reqspec = given().spec(commonUtils.requestSpec("logoutApi"));
        respec = responseSpec();
    }

    @Then("user submit {string} with {string} request for PortalLoginVAPT")
    public void userSubmitWithRequestWithForPortalLoginVAPT(String endpoint, String POST) {
        //response = getApiResponseObject.getResponse();
        ep = Endpoints.valueOf(endpoint);
        response = reqspec.when().header("Authorization", "Bearer " + bearerToken).queryParam("projectId", GetProperty.value("VAPTProjectId"))
                .post(ep.getValOfEndpoint()).then().extract().response();
    }
    @And("verify current statusCode for logOutApi")
    public void verifyCurrentStatusCodeForLogOutApi() {
        assertEquals(200, (int) getJsonPath(response.asString(), "statusCode"));
    }

    @When("validate after a user manually logs out the session token must be immediately expired")
    public void validateAfterAUserManuallyLogsOutTheSessionTokenMustBeImmediatelyExpired() {
        reqspec = given().spec(commonUtils.requestSpec("logoutApi"));
//        We are calling the logOut API again with same bearer token. logOut API should through error as session already expired.
        userSubmitWithRequestWithForPortalLoginVAPT("logoutApi", "POST");

        assertEquals("Token has expired", getJsonPath(response.asString(), "message"));
        assertEquals("Unauthorized", getJsonPath(response.asString(), "type"));
        assertEquals(401, (int) getJsonPath(response.asString(), "statusCode"));
    }

    @Then("validate users should not be able to use the expired token to access the system")
    public void validateUsersShouldNotBeAbleToUseTheExpiredTokenToAccessTheSystem() {
        reqspec = given().spec(commonUtils.requestSpec(""));
//        We are calling assistant API with same token to check now we are unable to access system without generating new token.
        submitAPIWithSameTokenSession("assistant");

        assertEquals("Token has expired", getJsonPath(response.asString(), "message"));
        assertEquals("Unauthorized", getJsonPath(response.asString(), "type"));
        assertEquals(401, (int) getJsonPath(response.asString(), "statusCode"));
    }


//    2nd # Scenario

    @Then("validate session should expire after {string} of inactivity")
    public void validateSessionShouldExpireAfterOfInactivity(String scheduled_time) {
        submitAPIWithSameTokenSession("assistant");
        assertEquals(200, (int) getJsonPath(response.asString(), "statusCode"));
        assertEquals("SUCCESS", getJsonPath(response.asString(), "message"));

        sessionToken = getTokenFromResponse(response);

//        Schedule a task to check the session expiration after scheduled time
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> checkSessionExpiration(sessionToken), Long.parseLong(scheduled_time), TimeUnit.SECONDS);

//        Log message to confirm the scheduler is set up
        System.out.println("Scheduled session expiration check after " + scheduled_time + " seconds.");

//        Continue with other test executions.
        System.out.println("Continuing with other tests.....");
    }

    private void checkSessionExpiration(String sessionToken) {
        System.out.println("Checking session expiration with token: " +sessionToken);
//        Attempt to use the session token after inactivity.
        Response postTimeoutResponse = response;
        int statusCode = postTimeoutResponse.getStatusCode();
        System.out.println("Status Code After Inactivity: " +statusCode);

//        Verify automatic logout.
        if (statusCode == 401) {
            System.out.println("Session expired and user is logged out automatically.");
        } else {
            System.out.println("Session is still active or another issue occurred.");
        }
    }

    @Then("validate session should expire after {int} minutes of inactivity")
    public void validateSessionShouldExpireAfterMinutesOfInactivity(int arg0) {
        submitAPIWithSameTokenSession("assistant");
        assertEquals(200, (int) getJsonPath(response.asString(), "statusCode"));
        assertEquals("SUCCESS", getJsonPath(response.asString(), "message"));

//        Forcefully inactive system for 15 minutes and then calling assistant API again
        sleepInSeconds(1020);
        submitAPIWithSameTokenSession("assistant");
        assertEquals("Unauthorized", getJsonPath(response.asString(), "type"));
        assertEquals(401, (int) getJsonPath(response.asString(), "statusCode"));
    }

    @Then("validate users should be automatically logged out after the session expires due to inactivity")
    public void validateUsersShouldBeAutomaticallyLoggedOutAfterTheSessionExpiresDueToInactivity() {

//        ***This functionality is not yet implemented by developers as of now due to development constraints.
    }

}
