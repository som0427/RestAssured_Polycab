package resources.testUtils;

import io.restassured.response.Response;

public class GetApiResponseObject {
    private Response response;
    private static GetApiResponseObject instance = null;
    private GetApiResponseObject() {
    }

    public static synchronized GetApiResponseObject getInstance() {
        if (instance == null) {
            instance = new GetApiResponseObject();
        }
        return instance;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
