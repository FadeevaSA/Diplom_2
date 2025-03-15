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
import static requestsMethods.UserDataChangingRequests.*;
import static responseMethods.UserDataChangingResponse.*;

public class UserDataChangingTest {
    private UserData user;
    private UserData userDataUpdate;
    private UserData userAuthorization;
    private String userAccessToken;

    @Before
    public void before() {
        new BaseApi();
        user = new UserData("roman@gmail.com", "666", "Roman");
        Response response = sendPostRequestUserCreating(user);
        userAccessToken = getAccessToken(response);
    }

    @Test
    @DisplayName("Successful user email and name changing")
    @Description("Code 200 after user data changing for /api/auth/user with authorization")
    public void checkSuccessfulUserEmailAndNameChanging() {
        userDataUpdate = new UserData("maks@gmail.com", "666", "Maks");
        Response responseWithChanges = changeUserDataWithAuthorization(userAccessToken, userDataUpdate);
        checkResponseWithUpdateEmail(responseWithChanges, userDataUpdate);
        checkResponseWithUpdateName(responseWithChanges, userDataUpdate);
    }

    @Test
    @DisplayName("Successful user password changing")
    @Description("Code 200 after authorization with new password")
    public void checkSuccessfulUserPasswordChanging() {
        userDataUpdate = new UserData("roman@gmail.com", "999", "Roman");
        changeUserDataWithAuthorization(userAccessToken, userDataUpdate);
        userAuthorization = new UserData("roman@gmail.com", "999");
        Response responseAuthorization = sendPostRequestUserAuthorization(userAuthorization);
        checkAuthorizationWithNewPassword(responseAuthorization);
    }

    @Test
    @DisplayName("Unsuccessful user email changing")
    @Description("Code 401 after user email changing without authorization")
    public void checkUnsuccessfulUserEmailChanging() {
        userDataUpdate = new UserData("maks@gmail.com");
        Response responseAfterChanges = changeUserDataWithoutAuthorization(userDataUpdate);
        checkUserDataChangingWithoutAuthorization(responseAfterChanges);
    }

    @Test
    @DisplayName("Unsuccessful user name changing")
    @Description("Code 401 after user name changing without authorization")
    public void checkUnsuccessfulUserNameChanging() {
        userDataUpdate = new UserData("roman@gmail.com", "666", "Maks");
        Response responseAfterChanges = changeUserDataWithoutAuthorization(userDataUpdate);
        checkUserDataChangingWithoutAuthorization(responseAfterChanges);
    }

    @Test
    @DisplayName("Unsuccessful user password changing")
    @Description("Code 401 after user password changing without authorization")
    public void checkUnsuccessfulUserPasswordChanging() {
        userDataUpdate = new UserData("roman@gmail.com", "11111");
        Response responseAfterChanges = changeUserDataWithoutAuthorization(userDataUpdate);
        checkUserDataChangingWithoutAuthorization(responseAfterChanges);
    }

    @After
    public void after() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken);
        }
    }
}
