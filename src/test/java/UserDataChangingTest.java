import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.stellarburgers.models.UserData;
import ru.stellarburgers.requests.BaseApi;

import static ru.stellarburgers.requests.UserCreatingRequests.*;
import static ru.stellarburgers.requests.UserDataChangingRequests.changeUserDataWithAuthorization;
import static ru.stellarburgers.requests.UserDataChangingRequests.changeUserDataWithoutAuthorization;
import static ru.stellarburgers.responses.UserAuthorizationResponse.checkCode401Response;
import static ru.stellarburgers.responses.UserCreatingResponse.checkCode200Response;
import static ru.stellarburgers.responses.UserDataChangingResponse.*;

public class UserDataChangingTest {
    private UserData user;
    private UserData userDataUpdate;
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
    @DisplayName("Successful user email changing")
    @Description("Code 200 after user email changing for /api/auth/user with authorization")
    public void testCheckSuccessfulUserEmailChanging() {
        userDataUpdate = new UserData("f" + user.getEmail(), user.getPassword(), user.getName());
        Response responseWithChanges = changeUserDataWithAuthorization(userAccessToken, userDataUpdate);
        checkCode200Response(responseWithChanges);
        checkResponseWithUpdateEmail(responseWithChanges, userDataUpdate);
    }

    @Test
    @DisplayName("Successful user name changing")
    @Description("Code 200 after user name changing for /api/auth/user with authorization")
    public void testCheckSuccessfulUserNameChanging() {
        userDataUpdate = new UserData(user.getEmail(), user.getPassword(), "R" + user.getName());
        Response responseWithChanges = changeUserDataWithAuthorization(userAccessToken, userDataUpdate);
        checkCode200Response(responseWithChanges);
        checkResponseWithUpdateName(responseWithChanges, userDataUpdate);
    }

    @Test
    @DisplayName("Unsuccessful user email changing")
    @Description("Code 401 after user email changing without authorization")
    public void testCheckUnsuccessfulUserEmailChanging() {
        userDataUpdate = new UserData("m" + user.getEmail());
        Response responseAfterChanges = changeUserDataWithoutAuthorization(userDataUpdate);
        checkCode401Response(responseAfterChanges);
        checkUserDataChangingWithoutAuthorization(responseAfterChanges);
    }

    @Test
    @DisplayName("Unsuccessful user name changing")
    @Description("Code 401 after user name changing without authorization")
    public void testCheckUnsuccessfulUserNameChanging() {
        userDataUpdate = new UserData(user.getEmail(), user.getPassword(), "K" + user.getName());
        Response responseAfterChanges = changeUserDataWithoutAuthorization(userDataUpdate);
        checkCode401Response(responseAfterChanges);
        checkUserDataChangingWithoutAuthorization(responseAfterChanges);
    }

    @After
    public void afterTest() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken);
        }
    }
}
