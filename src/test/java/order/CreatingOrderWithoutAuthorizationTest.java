package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import site.nomoreparties.stellarburgers.order.Ingredients;
import site.nomoreparties.stellarburgers.order.Order;
import site.nomoreparties.stellarburgers.order.OrderClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreatingOrderWithoutAuthorizationTest {

    OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Creating an order without ingredients, user isn't authorized, returns 400 Bad Request")
    @Description("Create an order without ingredients. " +
            "There are should be in response: status code is 400 and message: \"Ingredient ids must be provided\".")
    public void createOrderWithoutIngredientUserIsNotAuthorizedReturns400() {
        Order order = Order.builder().build();
        orderClient.createOrderWithoutIngredientsWithoutAuthorization(order);
    }

    @Test
    @DisplayName("Creating an order with one valid ingredient, user isn't authorized, returns 200 OK")
    @Description("Create an order with one valid hash of ingredient. " +
            "There are should be in response: status code is 200, order's number isn't null.")
    public void createOrderWithOneValidIngredientUserIsNotAuthorizedReturns200() {
        int index = new Random().nextInt(14);
        String ingredientHash = orderClient.getIngredient(index);

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(ingredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithIngredientsWithoutAuthorization(order);
    }

    @Test
    @DisplayName("Creating an order with one invalid ingredient, user isn't authorized, returns 500 Internal Server Error")
    @Description("Create an order with one invalid hash of ingredient. " +
            "There are should be in response: status code is 500.")
    public void createOrderWithOneInvalidIngredientUserIsNotAuthorizedReturns500() {
        String invalidIngredientHash = "61c0c5a71d1f82001bdaaa6r";

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(invalidIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithInvalidIngredientHashWithoutAuthorization(order);
    }

    @Test
    @DisplayName("Creating an order with more than one valid ingredients, user isn't authorized, returns 200 OK")
    @Description("Create an order with invalid hashes of ingredients. " +
            "There are should be in response: status code is 200 and order's number isn't null.")
    public void createOrderWithValidIngredientsUserIsNotAuthorizedReturns200() {
        int firstIndex = new Random().nextInt(14);
        String firstIngredientHash = orderClient.getIngredient(firstIndex);

        int secondIndex = new Random().nextInt(14);
        String secondIngredientHash = orderClient.getIngredient(secondIndex);

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(firstIngredientHash).build());
        ingredientsList.add(Ingredients.builder()._id(secondIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithIngredientsWithoutAuthorization(order);
    }

    @Test
    @DisplayName("Creating an order with two ingredients, one of them with invalid hash, user isn't authorized, " +
            "returns 500 Internal Server Error")
    @Description("Create an order with two ingredients - valid and invalid hashes. " +
            "There are should be in response: status code is 500.")
    public void createOrderWithInvalidIngredientsUserIsNotAuthorizedReturns500() {
        int index = new Random().nextInt(14);
        String validIngredientHash = orderClient.getIngredient(index);
        String invalidIngredientHash = orderClient.getIngredient(index) + "q";

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(invalidIngredientHash).build());
        ingredientsList.add(Ingredients.builder()._id(validIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithInvalidIngredientHashWithoutAuthorization(order);
    }

}