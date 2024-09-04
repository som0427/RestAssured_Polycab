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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.GetProperty;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommonUtils {
    RequestSpecification commonRequest;
    ResponseSpecification commonResponse;
    Response response;
    PrintStream logStream;
    private static CommonUtils instance = null;
    private final GetApiResponseObject getApiResponseObject;
    private String sessionToken;
    private static final Logger Log = LoggerFactory.getLogger(CommonUtils.class);


    // CommonUtils instance object created to concatenate all API logs in APITrace_logstream.txt when a step involves multiple API calls.
    public static synchronized CommonUtils getInstance() {
        if (instance == null) {
            instance = new CommonUtils();
        }
        return instance;
    }

    public CommonUtils() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        this.sessionToken = sessionToken;
    }



//    ** method for RequestSpecification
    public RequestSpecification requestSpec(String scenarioInfo) {
        if (commonRequest == null) {
            try {
                logStream = new PrintStream(new FileOutputStream("API_logstream.txt"));
                logStream.println("INFO: Starting API requests for: " + scenarioInfo);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Logging file could not be created",e);
            }
            commonRequest = new RequestSpecBuilder().setBaseUri(GetProperty.value("baseurl"))
                    .addFilter(RequestLoggingFilter.logRequestTo(logStream))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL, logStream)) //Log all response details to file
                    .setContentType(ContentType.JSON).build();
            return commonRequest;
        }
        else {
            logStream.println("INFO: Continuing API requests for: " + scenarioInfo);
        }
        return commonRequest;
    }


//    ** method for ResponseSpecification
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


//    ** method for validate success in ResponseBody with partial string match
    public void validatePartialDataInResponseBody(String keyCode, String expPartialText, String responseBody) {
        JsonPath js = new JsonPath(responseBody);
        String partialText = js.get(keyCode);
        assertTrue(partialText.contains(expPartialText));
    }


//    ** method for getJsonPath
    public <T> T getJsonPath(String response, String key) {
        JsonPath js = new JsonPath(response);
        return js.get(key);
    }


//    ** method for sleep
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

//    sessionTimeout() method actively checks the session every minute, useful if the session to expire before scheduled time.
//    currently not in use as only one scenario required to handle session.
    public void sessionTimeout() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable checkSession = () -> {
            boolean sessionValid = checkSessionStatus();    // Check if the session is still valid
            if (!sessionValid) {
                scheduler.shutdown();   //Stop further scheduling if the session has expired
            }
        };

        scheduler.scheduleAtFixedRate(checkSession, 0, 1, TimeUnit.MINUTES);

        try {
            if (!scheduler.awaitTermination(60, TimeUnit.MINUTES)) {
                scheduler.shutdownNow(); // Force shutdown if not terminated
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow(); // Handle interruption by shutting down immediately
            Thread.currentThread().interrupt(); // Restore interrupted status
            throw new RuntimeException(e);
        }
    }
    public boolean checkSessionStatus() {
        response = getApiResponseObject.getResponse();
        int statusCode = response.getStatusCode();

        // Check the response status code to determine if the session is still valid
        if (statusCode == 200) {
            System.out.println("Session is still valid.");
            return true;
        }
        else if (statusCode == 401) {
            System.out.println("Session has expired.");
            return false;
        }
        else {
            System.out.println("Unexpected status code: " +statusCode);
            return false;
        }
    }


}