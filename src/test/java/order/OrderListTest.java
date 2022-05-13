package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.order.OrderClient;
import site.nomoreparties.stellarburgers.user.User;
import site.nomoreparties.stellarburgers.user.UserClient;
import site.nomoreparties.stellarburgers.user.UserCredentials;

public class OrderListTest {

    UserClient userClient = new UserClient();
    OrderClient orderClient = new OrderClient();
    UserCredentials credentials;
    User user;
    String token;
    int totalOrders;

    @Before
    public void setUp() {
        user = userClient.getRandomUserTestData();
        user = User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .build();

        userClient.registerWithCorrectData(user);

        credentials = UserCredentials.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        token = userClient.loginWithCorrectCredentials(credentials);
    }

    @After
    public void tearDown() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Getting user's order list when user isn't authorized returns 401 Unauthorised")
    public void getUserOrderListWithoutAuthorizationReturns401() {
        userClient.logout(credentials);
        orderClient.getOrdersWithoutAuthorization();
    }

    @Test
    @DisplayName("Getting user's order list when user is authorized returns 200 OK")
    @Description("Register new user. Authorize and get order list. As user don't have any orders, " +
            "amounts of total and totalToday should be zero.")
    public void getUserOrderListWithAuthorizationReturns200() {
        totalOrders = 0;
        orderClient.getOrdersWithAuthorization(token, totalOrders);
    }
}
