package stepDefinitions;

import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.testUtils.CommonUtils;
import resources.testUtils.Endpoints;
import resources.testUtils.GetApiResponseObject;

import static org.junit.Assert.assertEquals;

public class LoginMobileAppVAPT extends CommonUtils{
    RequestSpecification reqspec;
    ResponseSpecification respec;
    Response response;
    Endpoints ep;
    String bearerToken;
    private final GetApiResponseObject getApiResponseObject;
    private final CommonUtils commonUtils = CommonUtils.getInstance();
    public LoginMobileAppVAPT() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        CommonUtils.getInstance();
    }

    @Then("validate response token validity time limit and generated token")
    public void validateResponseTokenValidityTimeLimitAndGeneratedToken() {
        response = getApiResponseObject.getResponse();
        String OTPtoken = getOTPtokenFromResponse(response);
        String validForTimeLimit = response.jsonPath().getString("responseData.validForInMilliSec");
        assertEquals("60000", validForTimeLimit);
    }

}
