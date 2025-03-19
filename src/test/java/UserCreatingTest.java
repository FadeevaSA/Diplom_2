import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.stellarburgers.models.UserData;
import ru.stellarburgers.requests.BaseApi;

import static ru.stellarburgers.requests.UserCreatingRequests.*;
import static ru.stellarburgers.responses.UserCreatingResponse.*;
import static ru.stellarburgers.responses.UserCreatingResponse.checkCode403Response;

public class UserCreatingTest {
    private UserData user;
    private UserData userWithoutOneField;
    private String userAccessToken;
    private UserFactory userFactory;

    @Before
    public void beforeTest() {
        new BaseApi();
        userFactory = new UserFactory();
        user = userFactory.createRandomUser();
    }

    @Test
    @DisplayName("Successful user creating")
    @Description("Code 200 for /api/auth/register with all parameters")
    public void testCheckSuccessfulUserCreating() {
        Response response = sendPostRequestUserCreating(user);
        userAccessToken = getAccessToken(response);
        checkCode200Response(response);
        checkResponseBody(response, user);
    }

    @Test
    @DisplayName("Unsuccessful existing user creating")
    @Description("Code 403 after creating an existing user using /api/auth/register")
    public void testCheckExistingUserCreating() {
        Response response = sendPostRequestUserCreating(user);
        userAccessToken = getAccessToken(response);
        Response responseOfExistingUser = sendPostRequestUserCreating(user);
        checkCode403Response(responseOfExistingUser);
        checkResponseOfExistingUserCreating(responseOfExistingUser);
    }

    @Test
    @DisplayName("Unsuccessful user creating without name")
    @Description("Code 403 after user creating without name using /api/auth/register")
    public void testCheckUserCreatingWithoutName() {
        userWithoutOneField = new UserData(user.getEmail(), user.getPassword());
        Response response = sendPostRequestUserCreating(userWithoutOneField);
        userAccessToken = getAccessToken(response);
        checkCode403Response(response);
        checkResponseWithoutName(response);
    }

    @Test
    @DisplayName("Unsuccessful user creating without password")
    @Description("Code 403 after user creating without password using /api/auth/register")
    public void testCheckUserCreatingWithoutPassword() {
        userWithoutOneField = new UserData(user.getEmail(), null, user.getName());
        Response response = sendPostRequestUserCreating(userWithoutOneField);
        userAccessToken = getAccessToken(response);
        checkCode403Response(response);
        checkResponseWithoutName(response);
    }

    @Test
    @DisplayName("Unsuccessful user creating without email")
    @Description("Code 403 after user creating without email using /api/auth/register")
    public void testCheckUserCreatingWithoutEmail() {
        userWithoutOneField = new UserData(null, user.getPassword(), user.getName());
        Response response = sendPostRequestUserCreating(userWithoutOneField);
        userAccessToken = getAccessToken(response);
        checkCode403Response(response);
        checkResponseWithoutName(response);
    }

    @After
    public void afterTest() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken);
        }
    }
}
