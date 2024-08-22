package stepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import org.junit.Assert;
import resources.payload.Login_Payload;
import resources.testUtils.CommonUtils;
import resources.testUtils.Endpoints;
import resources.testUtils.GetApiResponseObject;
import utilities.GetProperty;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LoginPortalSteps extends CommonUtils {

    RequestSpecification reqspec;
    ResponseSpecification respec;
    Response response, assignedWebMenus;
    String loginResponse;
    JsonPath json;
    Login_Payload payload = new Login_Payload();
    int noOfAssignedProjects;
    private final GetApiResponseObject getApiResponseObject;

    public LoginPortalSteps() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
    }

    //1st Scenario
    @Given("add loginAPI payload")
    public void addLoginAPIPayload() throws FileNotFoundException {
        reqspec = given().spec(requestSpec()).body(payload.loginPayload());
        respec = responseSpec();
    }

    @When("user submit {string} with {string} request for loginPortal")
    public void userSubmitWithPOSTRequestForLoginPortal(String endpoint, String httpMethod) {
        Endpoints ep = Endpoints.valueOf(endpoint);
        if (httpMethod.equalsIgnoreCase("POST")) {
            response = reqspec.when().post(ep.getValOfEndpoint()).then().spec(respec).extract().response();
        }
        else if (httpMethod.equalsIgnoreCase("GET")) {
            response = reqspec.when().get(ep.getValOfEndpoint()).then().spec(respec).extract().response();
        }
        getApiResponseObject.setResponse(response);
    }

    @Then("validate {string} is generated")
    public void validateIsGenerated(String token) {
        json = new JsonPath(response.asString());
        String fetchToken = json.get(token);
        System.out.println("token is: " + fetchToken + "-----------------token-------------------");
    }

    @Then("{string} is validated")
    public void isValidated(String project_id) {
        loginResponse = response.asString();
        System.out.println(loginResponse + "-------------------------response-----------");
        json = new JsonPath(loginResponse);
        int projectId = json.getInt("user.userProject[0].project.projectId");
        System.out.println("projectId is: " + projectId + "---------------projectId----------------3");
        Assert.assertEquals(GetProperty.value("respProjectId"), String.valueOf(projectId));
    }

    @Then("validate count of assigned projects and display project name")
    public void validateCountOfAssignedProjectsAndDisplayProjectName() {
        loginResponse = response.asString();
        json = new JsonPath(loginResponse);
        //projectCount
        noOfAssignedProjects = json.getInt("user.userProject.size()");
        Assert.assertEquals(GetProperty.value("projectCount"), String.valueOf(noOfAssignedProjects));
        System.out.println("projectCount is: " + noOfAssignedProjects + "---------------projectCount----------------");
        //projectName
        for (int i = 0; i < noOfAssignedProjects; i++) {
            String projectName = json.getString("user.userProject[" + i + "].project.projectName");
            System.out.println("projectName " + i + ": " + projectName + "---------------projectName----------------");

            if (noOfAssignedProjects > 1) {
                Assert.assertEquals(GetProperty.value("projectName"), json.getString("user.userProject[0].project.projectName"));
                System.out.println("project logo img " + i + ":" + (json.get("user.userProject[" + i + "].project.projectLogoImage")) + " ---------------logo----------------");
            }
        }
    }


    //2nd Scenario
    @Then("submit {string} api with {string} and validate menu items for selected {string}")
    public void submitApiWithAndValidateMenuItemsForSelected(String arg0, String token, String project_id) {
        json = new JsonPath(response.asString());
        String accessToken = json.get(token);
        noOfAssignedProjects = json.getInt("user.userProject.size()");

        if (noOfAssignedProjects > 1) {
            assignedWebMenus = reqspec.when().header("Authorization", "Bearer " + accessToken).queryParam("projectId", project_id)
                    .get("/retailitynew/login/api/menu/assignedWebMenus").then().spec(respec).extract().response();
        }
        Assert.assertEquals(assignedWebMenus.statusCode(), 200);
    }

    @SuppressWarnings("unchecked")
    @Then("validate count and value of submenus for {string} and {string} as per user-role rules")
    public void validateCountAndValueOfSubmenusForAsPerUserRoleRules(String selected_menu, String selectedSubMenu) {

            json = new JsonPath(assignedWebMenus.asString());
            int menuItemCount = json.getInt("responseData.size()");
            System.out.println("menuCount is: " + menuItemCount + "--------------menuCount--------------");

            //submenu count of selected user-role
            for (int i = 0; i < menuItemCount; i++) {
                List<String> submenus = json.getList("responseData[" + i + "].submenu");
                //        System.out.println("submenus is: " + submenus+ "--------------submenus-------------");

                if (submenus != null && !submenus.isEmpty()) {

                    int subMenuItemCount = submenus.size();
                    //System.out.println("subMenuItemCount is: " + subMenuItemCount + "--------------subMenuItemCount--------------");

                    for (Object submenuObj : submenus) {
                        //System.out.println("submenuObj: " + submenuObj +"--------------submenuObj--------------");

                        if (submenuObj instanceof Map) {
                            Map<String, Object> submenuMap = (Map<String, Object>) submenuObj;
                            String fetchSubmenuTitle = (String) submenuMap.get("title");
                            //System.out.println("fetchSubmenuTitle is: " +fetchSubmenuTitle+ "--------------fetchSubmenuTitle--------------");

                            List <Object> fetchSub_Submenu = (List<Object>) submenuMap.get("submenu");
                            if (fetchSub_Submenu != null) {
                                //System.out.println("fetchSubmenu is: " + fetchSub_Submenu + "--------------fetchSub_Submenu-1---------");

                                for (Object fetchSub_SubmenuObj : fetchSub_Submenu) {

                                    if (fetchSub_SubmenuObj instanceof Map) {
                                        Map<String, Object> fetchSub_SubmenuMap = (Map<String, Object>) fetchSub_SubmenuObj;
                                        String fetchSub_SubmenuTitle = (String) fetchSub_SubmenuMap.get("title");

                                            if (fetchSub_SubmenuTitle.equals(selectedSubMenu)) {
                                            Assert.assertEquals(fetchSub_SubmenuTitle, "Daily Activity Report");
                                            System.out.println("fetchSub_SubmenuTitle is: " + fetchSub_SubmenuTitle + "---------fetchSub_SubmenuTitle-----2------");
                                        }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
