package stepDefinitions.Mobile_App;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.testUtils.CommonUtils;
import resources.testUtils.Endpoints;
import resources.testUtils.GetApiResponseObject;
import stepDefinitions.LoginApiSteps;
import utilities.GetProperty;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class AppLoginMobileVAPT extends CommonUtils {
    RequestSpecification reqspec;
    ResponseSpecification respec;
    Response response;
    LoginApiSteps loginApiSteps = new LoginApiSteps();
    private final GetApiResponseObject getApiResponseObject;
    private final CommonUtils commonUtils = CommonUtils.getInstance();
    public AppLoginMobileVAPT() {
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

    @Then("validate request otp with incorrect mobile number")
    public void validateRequestOtpWithIncorrectMobileNumber() {
        String mobileNumber = "00000000";
        String attributeId = "53788";
        String incorrect_loginOtp_Payload = String.format("{ \"mobileNumber\": \"%s\", \"attributeId\": \"%s\" }", mobileNumber, attributeId);
        reqspec = given().spec(commonUtils.requestSpec()).body(incorrect_loginOtp_Payload)
                .queryParam("projectId", GetProperty.value("projectId"));
        respec = responseSpec();

        Response incorrect_loginOtpResponse = reqspec.when().post((Endpoints.valueOf("request_login_otp")).getValOfEndpoint())
                .then().extract().response();

        assertEquals(404, (int) getJsonPath(incorrect_loginOtpResponse.asString(), "statusCode"));
        assertEquals("Customer not found or is Inactive", getJsonPath(incorrect_loginOtpResponse.asString(), "message"));
    }

    @Then("validate Resend Interval Control functionality is working properly with sending login request within time limit")
    public void validateResendIntervalControlFunctionalityIsWorkingProperlyWithSendingLoginRequestWithinTimeLimit() {
//        We will submit request_login_otp again and check whether response contains 'Failure'
        loginApiSteps.userSubmitWithRequestForApplogin("request_login_otp", "POST");
    }

    @Then("validate Resend Interval Control functionality is working properly with sending login request after time limit")
    public void validateResendIntervalControlFunctionalityIsWorkingProperlyWithSendingLoginRequestAfterTimeLimit() {
//        Resend OTP after 12 seconds
        sleepInSeconds(12);
        loginApiSteps.userSubmitWithRequestForApplogin("request_login_otp", "POST");
    }

    @Then("validate user_id request rate limiting by sending login_otp as maximum two success request can be called within two Minutes")
    public void validateUser_idRequestRateLimitingBySendingLogin_otpAsMaximumTwoSuccessRequestCanBeCalledWithinTwoMinutes() {
        sleepInSeconds(120);
        Response differentNumber_loginOtpResponse = null;   //This response uses during call login_otp api with other number

        //call API 1st time / success
        differentNumber_loginOtpResponse = sendLoginOtpRequest();
        assertEquals(200, differentNumber_loginOtpResponse.statusCode());
        assertEquals("SUCCESS", getJsonPath(differentNumber_loginOtpResponse.asString(), "message"));

        // Run the API 2 times within 12 seconds / should not return success
        for (int i = 0; i < 2; i++) {
            differentNumber_loginOtpResponse = sendLoginOtpRequest();
            assertEquals(400, differentNumber_loginOtpResponse.statusCode());
            assertEquals("Resend interval not reached. Please wait before requesting another OTP.", getJsonPath(differentNumber_loginOtpResponse.asString(), "message"));
        }

        // Wait for 20 seconds
        sleepInSeconds(20);

        // Run API 2 times after 20 seconds / should return success 1st time, failure 2nd time
        for (int i = 0; i < 2; i++) {
            differentNumber_loginOtpResponse = sendLoginOtpRequest();
            if (i == 0) {
                assertEquals(200, differentNumber_loginOtpResponse.statusCode());
                assertEquals("SUCCESS", getJsonPath(differentNumber_loginOtpResponse.asString(), "message"));
            }
            if (i == 1) {
                assertEquals(400, differentNumber_loginOtpResponse.statusCode());
            }
        }

        // Wait for 62 seconds
        sleepInSeconds(120);

        // Run the API again after 2 minutes
        differentNumber_loginOtpResponse = sendLoginOtpRequest();
        assertEquals(200, differentNumber_loginOtpResponse.statusCode());
    }
    private Response sendLoginOtpRequest() {
        String mobileNumber = "9123400000";
        String attributeId = "53788";
        String differentNumber_loginOtp_Payload = String.format("{ \"mobileNumber\": \"%s\", \"attributeId\": \"%s\" }", mobileNumber, attributeId);
        reqspec = given().spec(commonUtils.requestSpec()).body(differentNumber_loginOtp_Payload)
                .queryParam("projectId", GetProperty.value("projectId"));
        respec = responseSpec();

        return reqspec.when().post((Endpoints.valueOf("request_login_otp")).getValOfEndpoint())
                .then().extract().response();
        }

    @Then("validate IP request rate limiting by calling login_otp api more than five times within time limit")
    public void validateIPRequestRateLimitingByCallingLogin_otpApiMoreThanFiveTimesWithinTimeLimit() {
//        we have waited 120s for refresh otp cycle. then call login_otp api 6 times.
        sleepInSeconds(120);
        for (int i = 0; i <= 5; i++) {
            loginApiSteps.userSubmitWithRequestForApplogin("request_login_otp", "POST");
        }
//        api should return 429 (Too Many Requests) at the last call.
        assertEquals("HTTP/1.1 429 ", getApiResponseObject.getResponse().statusLine());
    }

    @Then("validate {string} is success after user_id request rate limit reached when call from different user")
    public void validateIsSuccessAfterUser_idRequestRateLimitReachedWhenCallFromDifferentUser(String arg0) {
        sleepInSeconds(12);
        loginApiSteps.userSubmitWithRequestForApplogin("request_login_otp", "POST");

        assertEquals(200, (int) getJsonPath(getApiResponseObject.getResponse().asString(), "statusCode"));
        assertEquals("SUCCESS", getJsonPath(getApiResponseObject.getResponse().asString(), "message"));
    }

}
