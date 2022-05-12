package site.nomoreparties.stellarburgers.order;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import site.nomoreparties.stellarburgers.StellarburgersRestClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderClient extends StellarburgersRestClient {

    private final String ORDERS = "/orders";
    private final String INGREDIENTS = "/ingredients";

    @Step("Get ingredient on the random index")
    public String getIngredient(int index) {
        String path = "data[" + index + "]._id";
        return RestAssured.given()
                .spec(requestSpecification())
                .when()
                .get(INGREDIENTS)
                .then().log().ifError()
                .extract()
                .body()
                .path(path).toString();
    }

    @Step("Create an order with ingredients while user is authorized")
    public void createOrderWithIngredientsWithAuthorization(Order order, String token) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.ingredients", notNullValue());
    }

    @Step("Create an order with ingredients without authorization")
    public void createOrderWithIngredientsWithoutAuthorization(Order order) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.ingredients", notNullValue());
    }

    @Step("Create an order without ingredients while user is authorized")
    public void createOrderWithoutIngredientsWithAuthorization(Order order, String token) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifStatusCodeIsEqualTo(SC_OK)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Create an order without ingredient without authorization")
    public void createOrderWithoutIngredientsWithoutAuthorization(Order order) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifStatusCodeIsEqualTo(SC_OK)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Create an order with invalid ingredient's hash while user is authorized")
    public void createOrderWithInvalidIngredientHashWithAuthorization(Order order, String token) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifValidationFails()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Create an order with invalid ingredient's hash without authorization")
    public void createOrderWithInvalidIngredientHashWithoutAuthorization(Order order) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifValidationFails()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    //TODO: проверить total
    @Step("Get user's orders with authorization")
    public void getOrdersWithAuthorization(String token) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .when()
                .get(ORDERS)
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    //TODO: проверить total
    @Step("Get user's orders without authorization")
    public void getOrdersWithoutAuthorization() {
        RestAssured.given()
                .spec(requestSpecification())
                .when()
                .get(ORDERS)
                .then().log().all()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

}