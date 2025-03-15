package requestsMethods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.OrderData;

import java.util.ArrayList;
import java.util.List;

public class OrderCreatingRequests extends BaseApi {
    public static final String ORDER_CREATING = "/api/orders";
    public static final String GET_INGREDIENT = "/api/ingredients";

    @Step("Send POST request to /api/orders with authorization")
    public static Response sendOrderCreatingRequestWithAuthorization(String userAccessToken, OrderData orderData) {
        return requestSpecification.header("Authorization", userAccessToken).body(orderData).when().post(ORDER_CREATING);
    }

    @Step("Send POST request to /api/orders and get order number")
    public static Integer sendOrderCreatingRequestAndGetNumber(String userAccessToken, OrderData orderData) {
        Response response = requestSpecification.header("Authorization", userAccessToken).body(orderData).when().post(ORDER_CREATING);
        Integer orderNumber = response.then().extract().body().path("order.number");
        return orderNumber;
    }

    @Step("Send POST request to /api/orders without authorization")
    public static Response sendOrderCreatingRequestWithoutAuthorization(OrderData orderData) {
        return requestSpecification.body(orderData).when().post(ORDER_CREATING);
    }

    @Step("Send POST request to /api/orders without authorization and without ingredients")
    public static Response sendOrderCreatingRequestWithoutIngredients() {
        return requestSpecification.when().post(ORDER_CREATING);
    }

    @Step("Send GET request to /api/ingredients")
    public static List<String> getIngredientsIds() {
        Response response = requestSpecification.when().get(GET_INGREDIENT);
        List<String> ingredientIds = new ArrayList<>();
        ingredientIds.add(response.then().extract().body().path("data[0]._id"));
        ingredientIds.add(response.then().extract().body().path("data[3]._id"));
        return ingredientIds;
    }
}
