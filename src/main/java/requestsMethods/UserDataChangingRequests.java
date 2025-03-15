package requestsMethods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.UserData;

public class UserDataChangingRequests extends BaseApi {
    public static final String USER_DATA_CHANGING = "/api/auth/user";

    @Step("Send PATCH request to /api/auth/user")
    public static Response changeUserDataWithAuthorization(String userAccessToken, UserData user) {
        return requestSpecification.header("Authorization", userAccessToken).and().body(user).when().patch(USER_DATA_CHANGING);
    }

    @Step("Send PATCH request to /api/auth/user without authorization")
    public static Response changeUserDataWithoutAuthorization(UserData user) {
        return requestSpecification.body(user).when().patch(USER_DATA_CHANGING);
    }
}

