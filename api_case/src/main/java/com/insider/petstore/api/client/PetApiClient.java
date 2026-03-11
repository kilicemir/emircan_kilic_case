package com.insider.petstore.api.client;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static com.insider.petstore.api.client.BaseApiClient.given;

public final class PetApiClient {

    public static final String PET_PATH = "/pet";
    public static final String PET_BY_ID_PATH = PET_PATH + "/{petId}";
    public static final String PET_FIND_BY_STATUS_PATH = PET_PATH + "/findByStatus";

    private PetApiClient() {
    }

    private static Response sendRequest(String method, String path, Object body,
                                       Map<String, Object> queryParams, Map<String, Object> pathParams,
                                       RequestSpecification requestSpec) {
        if (queryParams == null) {
            queryParams = Map.of();
        }
        if (pathParams == null) {
            pathParams = Map.of();
        }
        if (body != null) {
            requestSpec = requestSpec.body(body);
        }

        return requestSpec
                .queryParams(queryParams)
                .pathParams(pathParams)
                .when()
                .request(method, path)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public static Response sendRequest(String method, String path, Object body,
                                      Map<String, Object> queryParams, Map<String, Object> pathParams) {
        return sendRequest(method, path, body, queryParams, pathParams, given());
    }

    public static Response sendGetRequest(String path, Map<String, Object> queryParams, Map<String, Object> pathParams) {
        return sendRequest("GET", path, null, queryParams, pathParams);
    }

    public static Response sendPostRequest(String path, Object body, Map<String, Object> queryParams, Map<String, Object> pathParams) {
        return sendRequest("POST", path, body, queryParams, pathParams);
    }

    public static Response sendPutRequest(String path, Object body, Map<String, Object> queryParams, Map<String, Object> pathParams) {
        return sendRequest("PUT", path, body, queryParams, pathParams);
    }

    public static Response sendDeleteRequest(String path, Map<String, Object> queryParams, Map<String, Object> pathParams) {
        return sendRequest("DELETE", path, null, queryParams, pathParams);
    }
}
