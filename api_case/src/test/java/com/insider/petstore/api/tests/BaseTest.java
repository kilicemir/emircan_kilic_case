package com.insider.petstore.api.tests;

import com.insider.petstore.api.client.BaseApiClient;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        RestAssured.filters(new AllureRestAssured()
                .setRequestAttachmentName("Request")
                .setResponseAttachmentName("Response"));
        BaseApiClient.reset();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        BaseApiClient.reset();
    }
}
