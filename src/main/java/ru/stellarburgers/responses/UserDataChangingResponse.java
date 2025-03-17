package ru.stellarburgers.responses;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.stellarburgers.models.UserData;

import static org.hamcrest.Matchers.equalTo;

public class UserDataChangingResponse {

    @Step("Checking the response after user email changing")
    public static void checkResponseWithUpdateEmail(Response response, UserData user) {
        response.then().assertThat().body("success", equalTo(true)).body("user.email", equalTo(user.getEmail()));
    }

    @Step("Checking the response after user name changing")
    public static void checkResponseWithUpdateName(Response response, UserData user) {
        response.then().assertThat().body("user.name", equalTo(user.getName()));
    }

    @Step("Checking the response after user data changing without authorization")
    public static void checkUserDataChangingWithoutAuthorization(Response response) {
        response.then().assertThat().body("success", equalTo(false)).body("message", equalTo("You should be authorised"));
    }
}
