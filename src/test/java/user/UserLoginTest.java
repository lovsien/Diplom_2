package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.user.User;
import site.nomoreparties.stellarburgers.user.UserClient;
import site.nomoreparties.stellarburgers.user.UserCredentials;

public class UserLoginTest {

    UserClient userClient = new UserClient();
    UserCredentials credentials;

    @Before
    public void setUp() {
        User user = userClient.getRandomUserTestData();
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
    }

    @After
    public void tearDown() {
        String token = userClient.loginWithCorrectCredentials(credentials);
        userClient.delete(token);
    }

    @Test
    @DisplayName("Login with correct data of existing user returns 200 OK")
    @Description("Register a new user. Login with correct data. " +
            "Expected results: " +
            "Status code - 200. Access and refresh tokens are not null values.")
    public void loginWithCorrectDataExistingUserReturnsOK() {
        UserCredentials credentialsForLogin = UserCredentials.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .build();

        userClient.loginWithCorrectCredentials(credentialsForLogin);
    }

    @Test
    @DisplayName("Login with incorrect login returns 401 Unauthorized")
    @Description("Register a new user. Login with incorrect email. " +
            "Expected results: " +
            "Status code - 401. Message - email or password are incorrect.")
    public void loginWithIncorrectLoginReturnsUnauthorized() {
        UserCredentials credentialsForLogin = UserCredentials.builder()
                .email(credentials.getEmail() + "1")
                .password(credentials.getPassword())
                .build();

        userClient.loginWithIncorrectCredentials(credentialsForLogin);
    }

    @Test
    @DisplayName("Login with incorrect password returns 401 Unauthorized")
    @Description("Register a new user. Login with incorrect password. " +
            "Expected results: " +
            "Status code - 401. Message - email or password are incorrect.")
    public void loginWithIncorrectPasswordReturnsUnauthorized() {
        UserCredentials credentialsForLogin = UserCredentials.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword() + "1")
                .build();

        userClient.loginWithIncorrectCredentials(credentialsForLogin);
    }

}