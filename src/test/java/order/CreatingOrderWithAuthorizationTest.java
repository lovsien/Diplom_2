package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.order.Ingredients;
import site.nomoreparties.stellarburgers.order.Order;
import site.nomoreparties.stellarburgers.order.OrderClient;
import site.nomoreparties.stellarburgers.user.User;
import site.nomoreparties.stellarburgers.user.UserClient;
import site.nomoreparties.stellarburgers.user.UserCredentials;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreatingOrderWithAuthorizationTest {

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
    @DisplayName("Creating an order without ingredients, while user is authorized, returns 400 Bad Request")
    @Description("Register new user and log in. Create an order without ingredients. " +
            "There are should be in response: status code is 400 and message: \"Ingredient ids must be provided\".")
    public void createOrderWithoutIngredientWhileUserIsAuthorizedReturns400() {
        Order order = Order.builder().build();
        orderClient.createOrderWithoutIngredientsWithAuthorization(order, token);
    }

    @Test
    @DisplayName("Creating an order with one valid ingredient, while user is authorized, returns 200 OK")
    @Description("Register new user and log in. Create an order with one valid hash of ingredient. " +
            "There are should be in response: status code is 200, order's number isn't null.")
    public void createOrderWithOneValidIngredientWhileUserIsAuthorizedReturns200() {
        int index = new Random().nextInt(14);
        String ingredientHash = orderClient.getIngredient(index);

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(ingredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithIngredientsWithAuthorization(order, token);
    }

    @Test
    @DisplayName("Creating an order with one invalid ingredient, while user is authorized, returns 500 Internal Server Error")
    @Description("Register new user and log in. Create an order with one invalid hash of ingredient. " +
            "There are should be in response: status code is 500.")
    public void createOrderWithOneInvalidIngredientWhileUserIsAuthorizedReturns500() {
        String invalidIngredientHash = "61c0c5a71d1f82001bdaaa6r";

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(invalidIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithInvalidIngredientHashWithAuthorization(order, token);
    }

    @Test
    @DisplayName("Creating an order with more than one valid ingredients, while user is authorized, returns 200 OK")
    @Description("Register new user and log in. Create an order with invalid hashes of ingredients. " +
            "There are should be in response: status code is 200 and order's number isn't null.")
    public void createOrderWithValidIngredientsWhileUserIsAuthorizedReturns200() {
        int firstIndex = new Random().nextInt(14);
        String firstIngredientHash = orderClient.getIngredient(firstIndex);

        int secondIndex = new Random().nextInt(14);
        String secondIngredientHash = orderClient.getIngredient(secondIndex);

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(firstIngredientHash).build());
        ingredientsList.add(Ingredients.builder()._id(secondIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithIngredientsWithAuthorization(order, token);
    }

    @Test
    @DisplayName("Creating an order with two ingredients, one of them with invalid hash, while user is authorized, " +
            "returns 500 Internal Server Error")
    @Description("Register new user and log in. Create an order with two ingredients - valid and invalid hashes. " +
            "There are should be in response: status code is 500.")
    public void createOrderWithInvalidIngredientsWhileUserIsAuthorizedReturns500() {
        int index = new Random().nextInt(14);
        String validIngredientHash = orderClient.getIngredient(index);
        String invalidIngredientHash = orderClient.getIngredient(index) + "q";

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(invalidIngredientHash).build());
        ingredientsList.add(Ingredients.builder()._id(validIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithInvalidIngredientHashWithAuthorization(order, token);
    }

}