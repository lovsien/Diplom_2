package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import site.nomoreparties.stellarburgers.user.User;
import site.nomoreparties.stellarburgers.user.UserClient;
import site.nomoreparties.stellarburgers.user.UserCredentials;

public class UserRegisterTest {

    UserClient userClient = new UserClient();
    User credentials;
    User user;

    @After
    public void tearDown() {
        UserCredentials credentials = UserCredentials.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        String token = userClient.loginWithCorrectCredentials(credentials);
        userClient.delete(token);
    }

    @Test
    @DisplayName("Creating a unique user with correct data")
    @Description("Registration of a new unique user returns status code 200 OK")
    public void createUniqueUserReturnsOk() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .name(credentials.getName())
                .build();

        userClient.registerWithCorrectData(user);
    }

    @Test
    @DisplayName("Create an existing user returns 403")
    @Description("Registration with already existing data returns status code 403 Forbidden and message:\"User already exists\"")
    public void createExistingUserReturnsForbidden() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .name(credentials.getName())
                .build();

        userClient.registerWithCorrectData(user);
        userClient.registerWithExistingData(user);
    }

}