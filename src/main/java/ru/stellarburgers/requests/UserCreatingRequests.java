package ru.stellarburgers.requests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.stellarburgers.models.UserData;

public class UserCreatingRequests extends BaseApi {
    public static final String USER_CREATING = "/api/auth/register";
    public static final String DELETE_USER = "/api/auth/user";

    @Step("Send POST request to /api/auth/register")
    public static Response sendPostRequestUserCreating(UserData user) {
        return requestSpecification.body(user).when().post(USER_CREATING);
    }

    @Step("Send POST request to /api/auth/register and get accessToken")
    public static String getAccessToken(Response response) {
        String userAccessToken = response.then().extract().body().path("accessToken");
        return userAccessToken;
    }

    @Step("Send DELETE request to /api/auth/user")
    public static void deleteUser(String userAccessToken) {
        RequestSpecification deleteRequest = RestAssured.given().header("Content-Type", "application/json").header("Authorization", userAccessToken);
        deleteRequest.when().delete(DELETE_USER);
    }
}
