package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import site.nomoreparties.stellarburgers.user.User;
import site.nomoreparties.stellarburgers.user.UserClient;

public class UserRegisterNegativeTest {

    UserClient userClient = new UserClient();
    User credentials;
    User user;

    @Test
    @DisplayName("Creating new user with missing name field returns 403 Forbidden")
    @Description("Registration with missing name field returns status code 403 Forbidden and message:\"Email, password and name are required fields\"")
    public void createUserWithMissingNameReturnsForbidden() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .build();

        userClient.registerWithMissingDataField(user);
    }

    @Test
    @DisplayName("Creating new user with missing email field returns 403 Forbidden")
    @Description("Registration with missing email field returns status code 403 Forbidden and message:\"Email, password and name are required fields\"")
    public void createUserWithMissingEmailReturnsForbidden() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .password(credentials.getPassword())
                .name(credentials.getName())
                .build();

        userClient.registerWithMissingDataField(user);
    }

    @Test
    @DisplayName("Creating new user with missing password field returns 403 Forbidden")
    @Description("Registration with missing password field returns status code 403 Forbidden and message:\"Email, password and name are required fields\"")
    public void createUserWithMissingPasswordReturnsForbidden() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .name(credentials.getName())
                .build();

        userClient.registerWithMissingDataField(user);
    }

}