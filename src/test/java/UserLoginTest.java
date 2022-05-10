import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.User;
import site.nomoreparties.stellarburgers.UserClient;
import site.nomoreparties.stellarburgers.UserCredentials;

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
    @DisplayName("Login with correct data of existing user")
    @Description("1. Register a new user. " +
            "2. Login with correct data. " +
            "Expected results: " +
            "Status code - 200. Success - true. Access and refresh tokens are not null values." +
            "3. Delete the user.")
    public void loginWithCorrectDataExistingUserReturnsOK() {
        UserCredentials credentialsForLogin = UserCredentials.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .build();

        userClient.loginWithCorrectCredentials(credentialsForLogin);
    }

    @Test
    @DisplayName("Login with incorrect login")
    @Description("1. Register a new user. " +
            "2. Login with incorrect email. " +
            "Expected results: " +
            "Status code - 401. Success - false. Message - email or password are incorrect." +
            "3. Delete the user.")
    public void loginWithIncorrectLoginReturnsUnauthorized() {
        UserCredentials credentialsForLogin = UserCredentials.builder()
                .email(credentials.getEmail() + "1")
                .password(credentials.getPassword())
                .build();

        userClient.loginWithIncorrectCredentials(credentialsForLogin);
    }

    @Test
    @DisplayName("Login with incorrect password")
    @Description("1. Register a new user. " +
            "2. Login with incorrect password. " +
            "Expected results: " +
            "Status code - 401. Success - false. Message - email or password are incorrect." +
            "3. Delete the user.")
    public void loginWithIncorrectPasswordReturnsUnauthorized() {
        UserCredentials credentialsForLogin = UserCredentials.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword() + "1")
                .build();

        userClient.loginWithIncorrectCredentials(credentialsForLogin);
    }

}