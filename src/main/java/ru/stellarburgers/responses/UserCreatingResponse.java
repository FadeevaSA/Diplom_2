package ru.stellarburgers.responses;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.stellarburgers.models.UserData;

import static org.hamcrest.Matchers.*;

public class UserCreatingResponse {

    @Step("Compare response code 200")
    public static void checkCode200Response(Response response) {
        response.then().statusCode(200);
    }

    @Step("Compare response code 403")
    public static void checkCode403Response(Response response) {
        response.then().statusCode(403);
    }

    @Step("Checking the response when creating an existing user")
    public static void checkResponseOfExistingUserCreating(Response response) {
        response.then().assertThat().body("success", equalTo(false)).body("message", equalTo("User already exists"));
    }

    @Step("Check user creating without name")
    public static void checkResponseWithoutName(Response response) {
        response.then().assertThat().body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Check response body of user creating")
    public static void checkResponseBody(Response response, UserData user) {
        response.then()
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", notNullValue());
    }
}
