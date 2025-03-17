package ru.stellarburgers.requests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.stellarburgers.models.UserData;

public class UserAuthorizationRequests extends BaseApi {
    public static final String USER_AUTHORIZATION = "/api/auth/login";

    @Step("Send POST request to /api/auth/login")
    public static Response sendPostRequestUserAuthorization(UserData user) {
        return requestSpecification.body(user).when().post(USER_AUTHORIZATION);
    }
}
