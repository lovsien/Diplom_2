import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import site.nomoreparties.stellarburgers.User;
import site.nomoreparties.stellarburgers.UserClient;
import site.nomoreparties.stellarburgers.UserCredentials;

public class UserRegisterTest {

    UserClient userClient = new UserClient();
    User credentials;
    User user;
    String token;

    @After
    public void tearDown() {
        UserCredentials credentials = UserCredentials.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        userClient.loginWithCorrectCredentials(credentials);
        userClient.delete(token);
    }

    @Test
    @DisplayName("Creating a unique user with correct data")
    @Description("Registration of a new unique user returns status code 200")
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
    @DisplayName("Create an existing user returns ")
    @Description
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

    @Test
    public void createUserWithMissingNameReturnsForbidden() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .build();

        userClient.registerWithMissingDataField(user);
    }

    @Test
    public void createUserWithMissingEmailReturnsForbidden() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .password(credentials.getPassword())
                .name(credentials.getName())
                .build();

        userClient.registerWithMissingDataField(user);
    }

    @Test
    public void createUserWithMissingPasswordReturnsForbidden() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .name(credentials.getName())
                .build();

        userClient.registerWithMissingDataField(user);
    }

}