package ru.stellarburgers.responses;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.stellarburgers.models.OrderData;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetUserOrdersResponse {
    @Step("Check response of user order with authorization")
    public static void checkSuccessfulUserOrderResponse(Response response, Integer orderNumber, OrderData orderData) {
        response.then()
                .body("success", equalTo(true))
                .body("orders._id", notNullValue())
                .body("orders.ingredients[0]", equalTo(orderData.getIngredients()))
                .body("orders.status[0]", equalTo("done"))
                .body("orders.name", notNullValue())
                .body("orders.number[0]", equalTo(orderNumber));
    }

    @Step("Check response of user order without authorization")
    public static void checkUnsuccessfulUserOrderResponse(Response response) {
        response.then().assertThat().body("success", equalTo(false)).body("message", equalTo("You should be authorised"));
    }
}
