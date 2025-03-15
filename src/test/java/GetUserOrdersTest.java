import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.OrderData;
import pojo.UserData;
import requestsMethods.BaseApi;

import java.util.List;

import static requestsMethods.GetUserOrdersRequests.getUserOrderWithAuthorization;
import static requestsMethods.GetUserOrdersRequests.getUserOrderWithoutAuthorization;
import static requestsMethods.OrderCreatingRequests.*;
import static requestsMethods.UserCreatingRequests.*;
import static responseMethods.GetUserOrdersResponse.checkSuccessfulUserOrderResponse;
import static responseMethods.GetUserOrdersResponse.checkUnsuccessfulUserOrderResponse;
import static responseMethods.UserCreatingResponse.checkCode200Response;

public class GetUserOrdersTest {
    private UserData user;
    private String userAccessToken;
    private OrderData orderData;
    private Integer orderNumber;

    @Before
    public void before() {
        new BaseApi();
        user = new UserData("anna@gmail.com", "3333", "Ann");
        Response response = sendPostRequestUserCreating(user);
        userAccessToken = getAccessToken(response);
        List<String> ingredientIds = getIngredientsIds();
        orderData = new OrderData(ingredientIds);
    }

    @Test
    @DisplayName("Get user order with authorization")
    @Description("Code 200 for /api/orders with authorization")
    public void getOrderWithAuthorization() {
        orderNumber = sendOrderCreatingRequestAndGetNumber(userAccessToken, orderData);
        Response userOrder = getUserOrderWithAuthorization(userAccessToken);
        checkCode200Response(userOrder);
        checkSuccessfulUserOrderResponse(userOrder, orderNumber, orderData);
    }

    @Test
    @DisplayName("Error when receiving an order without authorization")
    @Description("Code 401 when receiving an order without authorization")
    public void getOrderWithoutAuthorization() {
        Response userOrder = getUserOrderWithoutAuthorization();
        checkUnsuccessfulUserOrderResponse(userOrder);
    }

    @After
    public void after() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken);
        }
    }
}
