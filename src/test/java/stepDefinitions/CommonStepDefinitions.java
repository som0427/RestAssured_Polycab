package stepDefinitions;

import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import resources.testUtils.CommonUtils;

public class CommonStepDefinitions {

//    This is common step definitions which can be directly accessed with below mentioned gherkin.
    private final CommonUtils commonUtils;

    public CommonStepDefinitions() {
        this.commonUtils = new CommonUtils();
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

}
