package ru.stellarburgers.requests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GetUserOrdersRequests extends BaseApi {
    public static final String GET_USER_ORDER = "/api/orders";

    @Step("Send GET request to /api/orders with authorization")
    public static Response getUserOrderWithAuthorization(String userAccessToken) {
        RequestSpecification getOrderRequest = RestAssured.given().header("Content-Type", "application/json").header("Authorization", userAccessToken);
        return getOrderRequest.when().get(GET_USER_ORDER);
    }

    @Step("Send GET request to /api/orders without authorization")
    public static Response getUserOrderWithoutAuthorization() {
        return requestSpecification.when().get(GET_USER_ORDER);
    }
}
