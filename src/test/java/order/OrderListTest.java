package order;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.order.OrderClient;
import site.nomoreparties.stellarburgers.user.User;
import site.nomoreparties.stellarburgers.user.UserClient;
import site.nomoreparties.stellarburgers.user.UserCredentials;

public class OrderListTest {

    OrderClient orderClient = new OrderClient();
    UserClient userClient = new UserClient();
    UserCredentials credentials;
    User user;
    String token;

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
    public void getUserOrderListWithoutAuthorization() {
        userClient.logout(credentials);
        orderClient.getOrdersWithoutAuthorization();
    }

    @Test
    public void getUserOrderListWithAuthorization() {
        orderClient.getOrdersWithAuthorization(token);
    }
}
