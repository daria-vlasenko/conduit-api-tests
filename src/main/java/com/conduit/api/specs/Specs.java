package com.conduit.api.specs;

import com.conduit.config.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public final class Specs {

    private Specs() {
    }

    public static RequestSpecification request() {
        return new RequestSpecBuilder()
                .setBaseUri(Config.baseUrl())
                .setBasePath(Config.basePath())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(JsonMapper.config())
                .addFilter(allureFilter())
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification authorizedRequest(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(request())
                .addHeader("Authorization", "Token " + token)
                .build();
    }

    public static ResponseSpecification response(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .log(LogDetail.ALL)
                .build();
    }

    public static ResponseSpecification jsonResponse(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .addResponseSpecification(response(expectedStatusCode))
                .expectContentType(ContentType.JSON)
                .build();
    }

    private static AllureRestAssured allureFilter() {
        AllureRestAssured filter = new AllureRestAssured();
        filter.setRequestTemplate("http-request.ftl");
        filter.setResponseTemplate("http-response.ftl");
        return filter;
    }
}
