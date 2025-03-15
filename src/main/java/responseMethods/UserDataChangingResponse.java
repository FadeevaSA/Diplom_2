package responseMethods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.UserData;

import static org.hamcrest.Matchers.equalTo;

public class UserDataChangingResponse {

    @Step("Checking the response after user email changing")
    public static void checkResponseWithUpdateEmail(Response response, UserData user) {
        response.then().assertThat().body("success", equalTo(true)).body("user.email", equalTo(user.getEmail())).and().statusCode(200);
    }

    @Step("Checking the response after user name changing")
    public static void checkResponseWithUpdateName(Response response, UserData user) {
        response.then().assertThat().body("user.name", equalTo(user.getName()));
    }

    @Step("Checking successful authorization after user password changing")
    public static void checkAuthorizationWithNewPassword(Response response) {
        response.then().statusCode(200);
    }

    @Step("Checking the response after user data changing without authorization")
    public static void checkUserDataChangingWithoutAuthorization(Response response) {
        response.then().assertThat().body("success", equalTo(false)).body("message", equalTo("You should be authorised")).and().statusCode(401);
    }
}
