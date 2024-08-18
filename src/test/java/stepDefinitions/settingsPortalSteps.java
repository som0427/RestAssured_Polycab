package stepDefinitions;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.testUtils.CommonUtils;
import resources.testUtils.Endpoints;
import resources.testUtils.GetApiResponseObject;
import utilities.GetProperty;

import java.io.FileNotFoundException;

import static io.restassured.RestAssured.given;

public class settingsPortalSteps extends CommonUtils {
    RequestSpecification reqspec;
    ResponseSpecification respec;
    Response response;
    Endpoints ep;
    String bearerToken;
    private final GetApiResponseObject getApiResponseObject;
    private final CommonUtils commonUtils = CommonUtils.getInstance();
    public settingsPortalSteps() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        CommonUtils.getInstance();
    }

    @Then("add request for getAllAttributeTypes")
    public void addRequestForGetAllAttributeTypes() throws FileNotFoundException {
        response = getApiResponseObject.getResponse();
        bearerToken = getTokenFromResponse(response);
        reqspec = given().spec(commonUtils.requestSpec()).queryParam("moduleType", 2);
        respec = responseSpec();
    }

    @Then("user submit {string} with {string} request for settingsPortal")
    public void userSubmitWithRequestForSettingsPortal(String endpoint, String httpMethod) {
        ep = Endpoints.valueOf(endpoint);
        if (httpMethod.equalsIgnoreCase("GET")) {
            response = reqspec.when().header("Authorization", "Bearer " + bearerToken).get(ep.getValOfEndpoint())
                    .then().spec(respec).extract().response();
        }
        if (httpMethod.equalsIgnoreCase("POST")) {
            response = reqspec.when().header("Authorization", "Bearer " + bearerToken).post(ep.getValOfEndpoint())
                    .then().spec(respec).extract().response();
        }
    }

    @Then("add request for getFieldVisibilitySettings")
    public void addRequestForGetFieldVisibilitySettings() throws FileNotFoundException {
        reqspec = given().spec(commonUtils.requestSpec()).queryParam("projectId", GetProperty.value("projectId"))
                .queryParam("moduleType", 2).queryParam("filterByRole", false);
    }

    @Then("add request for getFieldsInSetting")
    public void addRequestForGetFieldsInSetting() throws FileNotFoundException {
        reqspec = given().spec(commonUtils.requestSpec()).queryParam("moduleType", 2);
    }

}
