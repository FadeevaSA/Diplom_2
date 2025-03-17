import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.stellarburgers.models.OrderData;
import ru.stellarburgers.models.UserData;
import ru.stellarburgers.requests.BaseApi;

import java.util.List;

import static ru.stellarburgers.requests.GetUserOrdersRequests.getUserOrderWithAuthorization;
import static ru.stellarburgers.requests.GetUserOrdersRequests.getUserOrderWithoutAuthorization;
import static ru.stellarburgers.requests.OrderCreatingRequests.getIngredientsIds;
import static ru.stellarburgers.requests.OrderCreatingRequests.sendOrderCreatingRequestAndGetNumber;
import static ru.stellarburgers.requests.UserCreatingRequests.*;
import static ru.stellarburgers.responses.GetUserOrdersResponse.*;
import static ru.stellarburgers.responses.UserAuthorizationResponse.checkCode401Response;
import static ru.stellarburgers.responses.UserCreatingResponse.checkCode200Response;

public class GetUserOrdersTest {
    private UserData user;
    private String userAccessToken;
    private OrderData orderData;
    private Integer orderNumber;
    private UserFactory userFactory;

    @Before
    public void beforeTest() {
        new BaseApi();
        userFactory = new UserFactory();
        user = userFactory.createRandomUser();
        Response response = sendPostRequestUserCreating(user);
        userAccessToken = getAccessToken(response);
        List<String> ingredientIds = getIngredientsIds();
        orderData = new OrderData(ingredientIds);
    }

    @Test
    @DisplayName("Get user order with authorization")
    @Description("Code 200 for /api/orders with authorization")
    public void testGetOrderWithAuthorization() {
        orderNumber = sendOrderCreatingRequestAndGetNumber(userAccessToken, orderData);
        Response userOrder = getUserOrderWithAuthorization(userAccessToken);
        checkCode200Response(userOrder);
        checkSuccessfulUserOrderResponse(userOrder, orderNumber, orderData);
    }

    @Test
    @DisplayName("Error when receiving an order without authorization")
    @Description("Code 401 when receiving an order without authorization")
    public void testGetOrderWithoutAuthorization() {
        Response userOrder = getUserOrderWithoutAuthorization();
        checkCode401Response(userOrder);
        checkUnsuccessfulUserOrderResponse(userOrder);
    }

    @After
    public void afterTest() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken);
        }
    }
}
