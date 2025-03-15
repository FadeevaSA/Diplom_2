import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.UserData;
import requestsMethods.BaseApi;

import static requestsMethods.UserCreatingRequests.*;
import static responseMethods.UserCreatingResponse.*;

public class UserCreatingTest {
    private UserData user;
    private UserData existingUser;
    private UserData userWithoutName;
    private String userAccessToken;

    @Before
    public void before() {
        new BaseApi();
        user = new UserData("anna@gmail.com", "3333", "Ann");
    }

    @Test
    @DisplayName("Successful user creating")
    @Description("Code 200 for /api/auth/register with all parameters")
    public void checkSuccessfulUserCreating() {
        Response response = sendPostRequestUserCreating(user);
        userAccessToken = getAccessToken(response);
        checkCode200Response(response);
        checkResponseBody(response, user);
    }

    @Test
    @DisplayName("Unsuccessful existing user creating")
    @Description("Code 403 after creating an existing user using /api/auth/register")
    public void checkExistingUserCreating() {
        Response response = sendPostRequestUserCreating(user);
        userAccessToken = getAccessToken(response);
        existingUser = new UserData("anna@gmail.com", "3333", "Ann");
        Response responseOfExistingUser = sendPostRequestUserCreating(existingUser);
        checkResponseOfExistingUserCreating(responseOfExistingUser);
    }

    @Test
    @DisplayName("Unsuccessful user creating without name")
    @Description("Code 403 after user creating without name using /api/auth/register")
    public void checkUserCreatingWithoutName() {
        userWithoutName = new UserData("anna@gmail.com", "3333");
        Response response = sendPostRequestUserCreating(userWithoutName);
        userAccessToken = getAccessToken(response);
        checkResponseWithoutName(response);
    }

    @After
    public void after() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken);
        }
    }
}
