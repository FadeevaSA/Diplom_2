import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.stellarburgers.models.UserData;
import ru.stellarburgers.requests.BaseApi;

import static ru.stellarburgers.requests.UserAuthorizationRequests.sendPostRequestUserAuthorization;
import static ru.stellarburgers.requests.UserCreatingRequests.*;
import static ru.stellarburgers.responses.UserAuthorizationResponse.*;
import static ru.stellarburgers.responses.UserCreatingResponse.checkCode200Response;
import static ru.stellarburgers.responses.UserCreatingResponse.checkResponseBody;

public class UserAuthorizationTest {
    private UserData user;
    private UserData userAuthorization;
    private String userAccessToken;
    private UserFactory userFactory;

    @Before
    public void beforeTest() {
        new BaseApi();
        userFactory = new UserFactory();
        user = userFactory.createRandomUser();
        Response response = sendPostRequestUserCreating(user);
        userAccessToken = getAccessToken(response);
    }

    @Test
    @DisplayName("Successful user authorization")
    @Description("Code 200  for /api/auth/login with correct data")
    public void testCheckSuccessfulUserAuthorization() {
        userAuthorization = new UserData(user.getEmail(), user.getPassword());
        Response responseAuthorization = sendPostRequestUserAuthorization(userAuthorization);
        checkCode200Response(responseAuthorization);
        checkResponseBody(responseAuthorization, user);
    }

    @Test
    @DisplayName("Unsuccessful user authorization with incorrect email")
    @Description("Code 401 for /api/auth/login with incorrect email")
    public void testUnsuccessfulAuthorizationWithIncorrectEmail() {
        userAuthorization = new UserData("f" + user.getEmail(), user.getPassword());
        Response responseAuthorization = sendPostRequestUserAuthorization(userAuthorization);
        checkCode401Response(responseAuthorization);
        checkResponseWithIncorrectData(responseAuthorization);
    }

    @Test
    @DisplayName("Unsuccessful user authorization with incorrect password")
    @Description("Code 401 for /api/auth/login with incorrect password")
    public void testUnsuccessfulAuthorizationWithIncorrectPassword() {
        userAuthorization = new UserData(user.getEmail(), "1" + user.getPassword());
        Response responseAuthorization = sendPostRequestUserAuthorization(userAuthorization);
        checkCode401Response(responseAuthorization);
        checkResponseWithIncorrectData(responseAuthorization);
    }

    @After
    public void afterTest() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken);
        }
    }
}
