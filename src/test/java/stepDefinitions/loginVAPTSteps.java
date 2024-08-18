package stepDefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.testUtils.CommonUtils;
import resources.testUtils.Endpoints;
import resources.testUtils.GetApiResponseObject;
import utilities.GetProperty;

import java.io.FileNotFoundException;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class loginVAPTSteps extends CommonUtils {

    RequestSpecification reqspec;
    ResponseSpecification respec;
    Response response;
    Endpoints ep;
    String bearerToken;
    private final GetApiResponseObject getApiResponseObject;
    private final CommonUtils commonUtils = CommonUtils.getInstance();
    public loginVAPTSteps() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        CommonUtils.getInstance();
    }


//    1st # Scenario

    @Then("verify if user able to submit {string} API with same token session")
    public void verifyIfUserAbleToSubmitAPIWithSameTokenSession(String endpoint) throws FileNotFoundException {
//        We are calling assistant API as random API where login token is used to access the system.
        response = getApiResponseObject.getResponse();  //response called here instead of during submit api (next step) so that logging.txt contains all api data.
        reqspec = given().spec(commonUtils.requestSpec());
        respec = responseSpec();
        bearerToken = getTokenFromResponse(response);
        ep = Endpoints.valueOf(endpoint);
        response = reqspec.when().header("Authorization", "Bearer " + bearerToken).queryParam("projectId", GetProperty.value("VAPTProjectId"))
                .get(ep.getValOfEndpoint());

        assertEquals(200, (int) getJsonPath(response.asString(), "statusCode"));
        assertEquals("SUCCESS", getJsonPath(response.asString(), "message"));

    }
    @When("add logoutApi payload")
    public void addLogoutApiPayload() throws FileNotFoundException {
        reqspec = given().spec(commonUtils.requestSpec());
        respec = responseSpec();
    }
    @Then("user submit {string} with {string} request with {string} for logout")
    public void userSubmitWithRequestWithForLogout(String endpoint, String POST, String token) {
        //response = getApiResponseObject.getResponse();
        ep = Endpoints.valueOf(endpoint);
        response = reqspec.when().header("Authorization", "Bearer " + bearerToken).queryParam("projectId", GetProperty.value("VAPTProjectId"))
                .post(ep.getValOfEndpoint()).then().extract().response();
    }
    @When("validate after a user manually logs out the session token must be immediately expired")
    public void validateAfterAUserManuallyLogsOutTheSessionTokenMustBeImmediatelyExpired() throws FileNotFoundException {
        reqspec = given().spec(commonUtils.requestSpec());
//        We are calling the logOut API again with same bearer token. logOut API should through error as session already expired.
        userSubmitWithRequestWithForLogout("logoutApi", "POST", "token");

        assertEquals("Token has expired", getJsonPath(response.asString(), "message"));
        assertEquals("Unauthorized", getJsonPath(response.asString(), "type"));
        assertEquals(401, (int) getJsonPath(response.asString(), "statusCode"));
    }

    @Then("validate users should not be able to use the expired token to access the system")
    public void validateUsersShouldNotBeAbleToUseTheExpiredTokenToAccessTheSystem() throws FileNotFoundException {
        reqspec = given().spec(commonUtils.requestSpec());
//        We are calling assistant API with same token to check now we are unable to access system without generating new token.
        verifyIfUserAbleToSubmitAPIWithSameTokenSession("assistant");

        assertEquals("Token has expired", getJsonPath(response.asString(), "message"));
        assertEquals("Unauthorized", getJsonPath(response.asString(), "type"));
        assertEquals(401, (int) getJsonPath(response.asString(), "statusCode"));
    }


//    2nd # Scenario

    @Then("validate session should expire after {int} minutes of inactivity")
    public void validateSessionShouldExpireAfterMinutesOfInactivity(int arg0) throws FileNotFoundException {
        verifyIfUserAbleToSubmitAPIWithSameTokenSession("assistant");
        assertEquals(200, (int) getJsonPath(response.asString(), "statusCode"));
        assertEquals("SUCCESS", getJsonPath(response.asString(), "message"));

//        Forcefully inactive system for 15 minutes and then calling assistant API again
        sleepInMinutes(17);
        verifyIfUserAbleToSubmitAPIWithSameTokenSession("assistant");
        assertEquals("Unauthorized", getJsonPath(response.asString(), "type"));
        assertEquals(401, (int) getJsonPath(response.asString(), "statusCode"));
    }

    @Then("validate users should be automatically logged out after the session expires due to inactivity")
    public void validateUsersShouldBeAutomaticallyLoggedOutAfterTheSessionExpiresDueToInactivity() {

//        ***This functionality is not yet implemented by developers as of now due to development constraints.
    }


}
