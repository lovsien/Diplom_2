package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.user.User;
import site.nomoreparties.stellarburgers.user.UserClient;
import site.nomoreparties.stellarburgers.user.UserCredentials;

public class UserUpdatingEmailOnExistingTest {

    UserClient userClient = new UserClient();

    UserCredentials firstUserCredentials;
    User firstUser;
    String firstUserToken;
    String secondUserToken;

    @Before
    public void setUp() {
        firstUser = userClient.getRandomUserTestData();
        firstUser = User.builder()
                .email(firstUser.getEmail())
                .password(firstUser.getPassword())
                .name(firstUser.getName())
                .build();

        userClient.registerWithCorrectData(firstUser);

        firstUserCredentials = UserCredentials.builder()
                .email(firstUser.getEmail())
                .password(firstUser.getPassword())
                .build();

        firstUserToken = userClient.loginWithCorrectCredentials(firstUserCredentials);
        userClient.logout(firstUserCredentials);
    }

    @After
    public void tearDown() {
        userClient.delete(firstUserToken);
        userClient.delete(secondUserToken);
    }

    @Test
    @DisplayName("Update user's email on existing user's email while he's authorized")
    @Description("There should be message: \"User with such email already exists\". Response should have status code 403. ")
    public void updateUserEmailOnExistingReturnsForbidden() {
        User secondUser = userClient.getRandomUserTestData();
        secondUser = User.builder()
                .email(secondUser.getEmail())
                .password(secondUser.getPassword())
                .name(secondUser.getName())
                .build();
        userClient.registerWithCorrectData(secondUser);

        UserCredentials secondUserCredentials = UserCredentials.builder()
                .email(secondUser.getEmail())
                .password(secondUser.getPassword())
                .build();

        secondUserToken = userClient.loginWithCorrectCredentials(secondUserCredentials);

        secondUser.setEmail(firstUser.getEmail());
        userClient.changeUserEmailWithAuthWithExistingEmail(secondUserToken, secondUser);
    }
}