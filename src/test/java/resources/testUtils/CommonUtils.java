package resources.testUtils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import utilities.GetProperty;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;

public class CommonUtils {
    RequestSpecification commonRequest;      //RequestSpecification is return type
    ResponseSpecification commonResponse;
    Response response;
    PrintStream logStream;
    private static CommonUtils instance = null;
    private final GetApiResponseObject getApiResponseObject;


    // CommonUtils instance object created to concatenate all API logs in logging.txt when a step involves multiple API calls.
    public static synchronized CommonUtils getInstance() {
        if (instance == null) {
            instance = new CommonUtils();
        }
        return instance;
    }

    public CommonUtils() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
    }


    public RequestSpecification requestSpec() {
        if (commonRequest == null) {
            try {
                logStream = new PrintStream(new FileOutputStream("logging.txt"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Logging file could not be created",e);
            }
            commonRequest = new RequestSpecBuilder().setBaseUri(GetProperty.value("baseurl"))
                    .addFilter(RequestLoggingFilter.logRequestTo(logStream))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL, logStream)) //Log all response details to file
                    .setContentType(ContentType.JSON).build();
            return commonRequest;
        }
        return commonRequest;
    }


    public ResponseSpecification responseSpec() {
        commonResponse = new ResponseSpecBuilder().expectStatusCode(200)
                .expectResponseTime(lessThan(5000L)).expectContentType(ContentType.JSON).build();
        return commonResponse;
    }


//    ** method for StatusCode
    public void validateStatusCode() {
        response = getApiResponseObject.getResponse();
        assertEquals(200, response.statusCode());
    }


//    ** method for ApiExecutionTime
    public void validateApiExecutionTime() {
        response = getApiResponseObject.getResponse();
        long response_time = response.time();
        //Assert.assertTrue(response_time < 5000);
        System.out.println("API Response Time: " + response_time + " ms" + "-----------------responseTime------------------");
    }


//    ** method for get Token from login APi Response for portal
    public String getTokenFromResponse(Response response) {
        if (response != null) {
            return response.jsonPath().getString("token");
        }
        else {
            throw new NullPointerException("This Response is null");
        }
    }

//    ** method for get Token from loginOTP APi Response for mobile App
    public String getOTPtokenFromResponse(Response response) {
        if (response != null) {
            return response.jsonPath().getString("responseData.token");
        }
        else {
            throw new NullPointerException("This Response is null");
        }
    }


//    ** method for validate success in ResponseBody
    public void validateDataInResponseBody(String keyCode, String expCode, String responseBody) {
        JsonPath js = new JsonPath(responseBody);
        assertEquals(js.getString(keyCode), expCode);
    }


    public <T> T getJsonPath(String response, String key) {
        JsonPath js = new JsonPath(response);
        return js.get(key);
    }

    public void sleepInSeconds(long seconds) {
        if (seconds < 0 || seconds > Long.MAX_VALUE / 1000) {
            throw new IllegalArgumentException("Seconds value is too large or negative.");
        }
        long milliseconds = seconds * 1000;
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}