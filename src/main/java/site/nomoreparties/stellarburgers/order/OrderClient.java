package site.nomoreparties.stellarburgers.order;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import site.nomoreparties.stellarburgers.StellarburgersRestClient;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderClient extends StellarburgersRestClient {

    private final String ORDERS = "/orders";

    @Step("Create an order")
    public void create(List<String> ingredients) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(ingredients)
                .when()
                .post(ORDERS)
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.ingredients", notNullValue());

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
