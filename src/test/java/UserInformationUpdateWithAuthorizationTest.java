import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.User;
import site.nomoreparties.stellarburgers.UserClient;
import site.nomoreparties.stellarburgers.UserCredentials;

public class UserInformationUpdateWithAuthorizationTest {

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
    @DisplayName("Update user's name while he's authorized")
    @Description("User's name should be equal to set value. Response should have status code 200. ")
    public void updateUserNameFieldWithAuthorizationReturnsOK() {
        user.setName(RandomStringUtils.randomAlphabetic(5));
        userClient.changeUserNameWithAuthorization(token, user);
    }

    @Test
    @DisplayName("Update user's email while he's authorized")
    @Description("User's email should be equal to set value. Response should have status code 200. ")
    public void updateUserEmailFieldWithAuthorizationReturnsOK() {
        user.setEmail(RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@mail.com");
        userClient.changeUserEmailWithAuthorization(token, user);
    }

    //TODO: переписать кейс - очень плохой
    @Test
    @DisplayName("Update user's email on existing user's email while he's authorized")
    @Description("There should be message: \"User with such email already exists\". Response should have status code 403. ")
    public void updateUserEmailOnExistingReturnsForbidden() {
        user.setEmail("test-data@yandex.ru");
        userClient.changeUserEmailWithAuthWithExistingEmail(token, user);
    }
}