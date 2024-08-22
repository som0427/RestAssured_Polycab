package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import resources.testUtils.CommonUtils;
import resources.testUtils.GetApiResponseObject;

import static org.junit.Assert.assertEquals;

public class CommonStepDefinitions {

//    This is common step definitions which can be directly accessed with below mentioned gherkin.
    private final CommonUtils commonUtils;
    private final GetApiResponseObject getApiResponseObject;

    public CommonStepDefinitions() {
        this.commonUtils = new CommonUtils();
        this.getApiResponseObject = GetApiResponseObject.getInstance();
    }

//    gherkin: API call is success with status code 200
    @Then("API call is success with status code 200")
    public void apiCallIsSuccessWithStatusCode200() {
        commonUtils.validateStatusCode();
    }


//    gherkin: validate ApiResponse execution time
    @Then("validate ApiResponse execution time")
    public void validateApiResponseExecutionTime() {
        commonUtils.validateApiExecutionTime();
    }


//    gherkin: validate ApiResponseBody success parameter
    @And("validate {string} is {string} in responseBody")
    public void validateIsInResponseBody(String keyValue, String expValue) {
        Response response = getApiResponseObject.getResponse();
        commonUtils.validateSuccessInResponseBody(keyValue, expValue, response.asString());
    }

}
