import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.OrderData;
import pojo.UserData;
import requestsMethods.BaseApi;


import java.util.Arrays;
import java.util.List;

import static requestsMethods.OrderCreatingRequests.*;
import static requestsMethods.UserCreatingRequests.*;
import static responseMethods.OrderCreatingResponse.*;
import static responseMethods.UserCreatingResponse.checkCode200Response;

public class OrderCreatingTest {
    private UserData user;
    private String userAccessToken;
    private OrderData orderData;
    private OrderData incorrectOrderData;

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
    @DisplayName("Successful order creating")
    @Description("Code 200 for /api/orders with ingredients and authorization")
    public void checkSuccessfulOrderCreating() {
        Response orderResponse = sendOrderCreatingRequestWithAuthorization(userAccessToken, orderData);
        checkCode200Response(orderResponse);
        checkSuccessfulOrderCreatingResponse(orderResponse);
    }

    @Test
    @DisplayName("Successful order creating without authorization")
    @Description("Code 200 for /api/orders with ingredients and without authorization")
    public void checkSuccessfulOrderCreatingWithoutAuthorization() {
        Response orderResponse = sendOrderCreatingRequestWithoutAuthorization(orderData);
        checkCode200Response(orderResponse);
        checkSuccessfulOrderCreatingResponse(orderResponse);
    }

    @Test
    @DisplayName("Unsuccessful order creating without ingredients")
    @Description("Code 400 for /api/orders without ingredients")
    public void checkUnsuccessfulOrderCreatingWithoutIngredients() {
        Response orderResponse = sendOrderCreatingRequestWithoutIngredients();
        checkResponseWithoutIngredients(orderResponse);
    }

    @Test
    @DisplayName("Unsuccessful order creating with incorrect ingredients hash")
    @Description("Code 500 for /api/orders with incorrect ingredients hash")
    public void checkOrderCreatingWithIncorrectIngredientsHash() {
        List<String> incorrectIngredient = Arrays.asList("id1", "id5");
        incorrectOrderData = new OrderData(incorrectIngredient);
        Response orderResponse = sendOrderCreatingRequestWithoutAuthorization(incorrectOrderData);
        checkCode500Response(orderResponse);
    }

    @After
    public void after() {
        if (userAccessToken != null) {
            deleteUser(userAccessToken);
        }
    }
}

