package order;

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
    public void createOrderWithoutIngredientWhileUserIsAuthorized() {
        Order order = Order.builder().build();
        orderClient.createOrderWithoutIngredientsWithAuthorization(order, token);
    }

    @Test
    public void createOrderWithOneValidIngredientWhileUserIsAuthorized() {
        int index = new Random().nextInt(14);
        String ingredientHash = orderClient.getIngredient(index);

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(ingredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithIngredientsWithAuthorization(order, token);
    }

    @Test
    public void createOrderWithOneInvalidIngredientWhileUserIsAuthorized() {
        String invalidIngredientHash = "61c0c5a71d1f82001bdaaa6r";

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(invalidIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithInvalidIngredientHashWithAuthorization(order, token);
    }

    @Test
    public void createOrderWithValidIngredientsWhileUserIsAuthorized() {
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
    public void createOrderWithInvalidIngredientsWhileUserIsAuthorized() {
        String validIngredientHash = "61c0c5a71d1f82001bdaaa6d";
        String invalidIngredientHash = "61c0c5a71d1f82001bdaaa7q";

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(invalidIngredientHash).build());
        ingredientsList.add(Ingredients.builder()._id(validIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithInvalidIngredientHashWithAuthorization(order, token);
    }

}