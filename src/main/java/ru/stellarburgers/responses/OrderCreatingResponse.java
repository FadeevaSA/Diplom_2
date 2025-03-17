package ru.stellarburgers.responses;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

public class OrderCreatingResponse {
    @Step("Check order creating with ingredients and authorization")
    public static void checkSuccessfulOrderCreatingResponse(Response response) {
        response.then()
                .body("name", notNullValue())
                .body("order.number", notNullValue())
                .body("success", equalTo(true));
    }

    @Step("Check order creating without ingredients")
    public static void checkResponseWithoutIngredients(Response response) {
        response.then().assertThat().body("success", equalTo(false)).body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Compare response code 400")
    public static void checkCode400Response(Response response) {
        response.then().statusCode(400);
    }

    @Step("Compare response code 500")
    public static void checkCode500Response(Response response) {
        response.then().statusCode(500);
    }
}
