package com.insider.petstore.api.client;

import com.insider.petstore.api.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

public final class BaseApiClient {

    @Getter
    private static RequestSpecification baseSpec = buildSpec();

    private BaseApiClient() {
    }

    private static RequestSpecification buildSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ApiConfig.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }

    public static RequestSpecification given() {
        return RestAssured.given().spec(getBaseSpec());
    }

    public static void reset() {
        baseSpec = buildSpec();
    }
}
