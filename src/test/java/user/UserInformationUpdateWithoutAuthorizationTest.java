package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import site.nomoreparties.stellarburgers.user.User;
import site.nomoreparties.stellarburgers.user.UserClient;

public class UserInformationUpdateWithoutAuthorizationTest {

    UserClient userClient = new UserClient();

    @Test
    @DisplayName("Updating user's name while he's authorized returns 401 Unauthorized")
    @Description("Response should have status code 401. Message: \"You should be authorised\"")
    public void updateUserNameFieldWithAuthorizationReturnsUnauthorized() {
        User user = User.builder().name(RandomStringUtils.randomAlphabetic(5)).build();
        userClient.changeUserNameWithoutAuthorization(user);
    }

    @Test
    @DisplayName("Updating user's email while he's authorized returns 401 Unauthorized")
    @Description("Response should have status code 401. Message: \"You should be authorised\"")
    public void updateUserEmailFieldWithAuthorizationReturnsUnauthorized() {
        User user = User.builder().email(RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@mail.com").build();
        userClient.changeUserEmailWithoutAuthorization(user);
    }

}