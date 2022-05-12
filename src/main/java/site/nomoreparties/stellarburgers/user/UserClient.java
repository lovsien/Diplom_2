package site.nomoreparties.stellarburgers.user;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import site.nomoreparties.stellarburgers.StellarburgersRestClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class UserClient extends StellarburgersRestClient {

    private final String USER_AUTH_REGISTER_PATH = "/auth/register";
    private final String USER_AUTH_LOGIN_PATH = "/auth/login";
    private final String USER_AUTH_LOGOUT_PATH = "/auth/logout";
    private final String USER_AUTH_USER = "/auth/user";

    @Step("Create random credentials")
    public User getRandomUserTestData() {
        final String email = RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@mail.com";
        final String password = RandomStringUtils.randomAlphabetic(5);
        final String name = RandomStringUtils.randomAlphabetic(5);

        Allure.addAttachment("Email: ", email);
        Allure.addAttachment("Password: ", password);
        Allure.addAttachment("Username: ", name);

        return new User(email, password, name);
    }

    @Step("Register a new user with correct data successfully")
    public void registerWithCorrectData(User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .post(USER_AUTH_REGISTER_PATH)
                .then().log().ifError()
                .assertThat()
                .body("success", equalTo(true))
                .body("user", notNullValue())
                .statusCode(SC_OK);
    }

    @Step("Register a new user with existing unsuccessfully")
    public void registerWithExistingData(User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .post(USER_AUTH_REGISTER_PATH)
                .then().log().ifError()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"))
                .statusCode(SC_FORBIDDEN);
    }


    @Step("Register a new user with missing field unsuccessfully")
    public void registerWithMissingDataField(User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .post(USER_AUTH_REGISTER_PATH)
                .then().log().ifError()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(SC_FORBIDDEN);
    }

    @Step("Login with correct credentials, status code = 200")
    public String loginWithCorrectCredentials(UserCredentials credentials) {
        return RestAssured.given()
                .spec(requestSpecification())
                .body(credentials)
                .when()
                .post(USER_AUTH_LOGIN_PATH)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .extract()
                .path("accessToken")
                .toString()
                .substring(7);
    }

    @Step("Login with correct credentials, status code = 401")
    public void loginWithIncorrectCredentials(UserCredentials credentials) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(credentials)
                .when()
                .post(USER_AUTH_LOGIN_PATH)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Log out from the system")
    public void logout(UserCredentials credentials) {
        Token refreshToken = new Token(getRefreshToken(credentials));

        RestAssured.given()
                .spec(requestSpecification())
                .body(refreshToken)
                .when()
                .post(USER_AUTH_LOGOUT_PATH)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("message", equalTo("Successful logout"));
    }

    public String getRefreshToken(UserCredentials credentials) {
        return RestAssured.given()
                .spec(requestSpecification())
                .body(credentials)
                .when()
                .post(USER_AUTH_LOGIN_PATH)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .extract()
                .path("refreshToken");
    }

    @Step("Change user's name, using authorization")
    public void changeUserNameWithAuthorization(String token, User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .and()
                .body(user)
                .when()
                .patch(USER_AUTH_USER)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user", notNullValue())
                .body("user.name", equalTo(user.getName()));
    }

    @Step("Change user's email, using authorization")
    public void changeUserEmailWithAuthorization(String token, User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .and()
                .body(user)
                .when()
                .patch(USER_AUTH_USER)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user", notNullValue())
                .body("user.email", equalTo(user.getEmail()))
                ;
    }

    @Step("Change user's email to existing email")
    public void changeUserEmailWithAuthWithExistingEmail(String token, User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .and()
                .body(user)
                .when()
                .patch(USER_AUTH_USER)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }

    @Step("Change user's name, without authorization")
    public void changeUserNameWithoutAuthorization(User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .patch(USER_AUTH_USER)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Change user's email, without authorization")
    public void changeUserEmailWithoutAuthorization(User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .patch(USER_AUTH_USER)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Delete the user")
    public void delete(String token) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .when()
                .delete(USER_AUTH_USER)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_ACCEPTED)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

}