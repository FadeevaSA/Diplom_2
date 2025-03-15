import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.UserData;
import requestsMethods.BaseApi;

import static requestsMethods.UserAuthorizationRequests.sendPostRequestUserAuthorization;
import static requestsMethods.UserCreatingRequests.*;
import static responseMethods.UserAuthorizationResponse.checkResponseWithIncorrectData;
import static responseMethods.UserCreatingResponse.checkCode200Response;
import static responseMethods.UserCreatingResponse.checkResponseBody;

public class UserAuthorizationTest {
    private UserData user;
    private UserData userAuthorization;
    private String userAccessToken;

    @Before
    public void before() {
        new BaseApi();
        user = new UserData("anna@gmail.com", "3333", "Ann");
        Response response = sendPostRequestUserCreating(user);
        userAccessToken = getAccessToken(response);
    }

    @Test
    @DisplayName("Successful user authorization")
    @Description("Code 200  for /api/auth/login with correct data")
    public void checkSuccessfulUserAuthorization() {
        userAuthorization = new UserData("anna@gmail.com", "3333");
        Response responseAuthorization = sendPostRequestUserAuthorization(userAuthorization);
        checkCode200Response(responseAuthorization);
        checkResponseBody(responseAuthorization, user);
    }

    @Test
    @DisplayName("Unsuccessful user authorization")
    @Description("Code 401  for /api/auth/login with incorrect data")
    public void checkUnsuccessfulUserAuthorization() {
        userAuthorization = new UserData("na@gmail.com", "33");
        Response responseAuthorization = sendPostRequestUserAuthorization(userAuthorization);
        checkResponseWithIncorrectData(responseAuthorization);
    }

    @After
    public void after() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken);
        }
    }
}
