import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.stellarburgers.models.OrderData;
import ru.stellarburgers.models.UserData;
import ru.stellarburgers.requests.BaseApi;


import java.util.Arrays;
import java.util.List;

import static ru.stellarburgers.requests.OrderCreatingRequests.*;
import static ru.stellarburgers.requests.UserCreatingRequests.*;
import static ru.stellarburgers.responses.OrderCreatingResponse.*;
import static ru.stellarburgers.responses.UserCreatingResponse.checkCode200Response;

public class OrderCreatingTest {
    private UserData user;
    private String userAccessToken;
    private OrderData orderData;
    private OrderData incorrectOrderData;
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
    @DisplayName("Successful order creating")
    @Description("Code 200 for /api/orders with ingredients and authorization")
    public void testCheckSuccessfulOrderCreating() {
        Response orderResponse = sendOrderCreatingRequestWithAuthorization(userAccessToken, orderData);
        checkCode200Response(orderResponse);
        checkSuccessfulOrderCreatingResponse(orderResponse);
    }

    @Test
    @DisplayName("Successful order creating without authorization")
    @Description("Code 200 for /api/orders with ingredients and without authorization")
    public void testCheckSuccessfulOrderCreatingWithoutAuthorization() {
        Response orderResponse = sendOrderCreatingRequestWithoutAuthorization(orderData);
        checkCode200Response(orderResponse);
        checkSuccessfulOrderCreatingResponse(orderResponse);
    }

    @Test
    @DisplayName("Unsuccessful order creating without ingredients")
    @Description("Code 400 for /api/orders without ingredients")
    public void testCheckUnsuccessfulOrderCreatingWithoutIngredients() {
        Response orderResponse = sendOrderCreatingRequestWithoutIngredients();
        checkCode400Response(orderResponse);
        checkResponseWithoutIngredients(orderResponse);
    }

    @Test
    @DisplayName("Unsuccessful order creating with incorrect ingredients hash")
    @Description("Code 500 for /api/orders with incorrect ingredients hash")
    public void testCheckOrderCreatingWithIncorrectIngredientsHash() {
        List<String> incorrectIngredient = Arrays.asList("id1", "id5");
        incorrectOrderData = new OrderData(incorrectIngredient);
        Response orderResponse = sendOrderCreatingRequestWithoutAuthorization(incorrectOrderData);
        checkCode500Response(orderResponse);
    }

    @After
    public void afterTest() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken);
        }
    }
}

