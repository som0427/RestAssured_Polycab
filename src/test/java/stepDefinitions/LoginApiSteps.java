package stepDefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.payload.Login_Payload;
import resources.testUtils.CommonUtils;
import resources.testUtils.Endpoints;
import resources.testUtils.GetApiResponseObject;
import utilities.GetProperty;

import java.io.FileNotFoundException;
import static io.restassured.RestAssured.given;
public class LoginApiSteps extends CommonUtils {

    RequestSpecification reqspec;
    ResponseSpecification respec;
    static Response response;
    Endpoints ep;
    private final GetApiResponseObject getApiResponseObject;
    Login_Payload payload = new Login_Payload();

    //This will ensure single CommonUtils instance will be created throughout the execution.
    //Calling getInstance() ensures you are interacting with that single shared instance.
    //This is particularly useful if CommonUtils maintains state that should be consistent across different parts of your application.i.e. Logging.txt
    private final CommonUtils commonUtils = CommonUtils.getInstance();

    public LoginApiSteps() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
    }

    @When("user submit {string} with {string} request for login")
    public void userSubmitWithPOSTRequestForLogin(String endpoint, String POST) {

        reqspec = given().spec(commonUtils.requestSpec()).body(payload.loginPayload());
        respec = responseSpec();
        ep = Endpoints.valueOf(endpoint);

        response = reqspec.when().post(ep.getValOfEndpoint()).then().spec(respec).extract().response();

        getApiResponseObject.setResponse(response);
    }

    @When("user submit {string} with {string} request for App-login")
    public void userSubmitWithRequestForApplogin(String endpoint, String httpMethod) {
        reqspec = given().spec(commonUtils.requestSpec()).body(payload.loginMobilegetOTPPayload());
        respec = responseSpec();
        ep = Endpoints.valueOf(endpoint);

        response = reqspec.when().queryParam("projectId", GetProperty.value("projectId")).post(ep.getValOfEndpoint())
                .then().extract().response();

        getApiResponseObject.setResponse(response);
    }

}