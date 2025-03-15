package responseMethods;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;

public class UserAuthorizationResponse {

    @Step("Check user authorization with incorrect data")
    public static void checkResponseWithIncorrectData(Response response) {
        response.then().assertThat().body("success", equalTo(false)).body("message", equalTo("email or password are incorrect")).and().statusCode(401);
    }
}
