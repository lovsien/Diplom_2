package order;

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
    public void createOrderWithoutIngredientWhileUserIsAuthorized() {
        Order order = Order.builder().build();
        orderClient.createOrderWithoutIngredientsWithoutAuthorization(order);
    }

    @Test
    public void createOrderWithOneValidIngredientWhileUserIsAuthorized() {
        int index = new Random().nextInt(14);
        String ingredientHash = orderClient.getIngredient(index);

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(ingredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithIngredientsWithoutAuthorization(order);
    }

    @Test
    public void createOrderWithOneInvalidIngredientWhileUserIsAuthorized() {
        String invalidIngredientHash = "61c0c5a71d1f82001bdaaa6r";

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(invalidIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithInvalidIngredientHashWithoutAuthorization(order);
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
        orderClient.createOrderWithIngredientsWithoutAuthorization(order);
    }

    @Test
    public void createOrderWithInvalidIngredientsWhileUserIsAuthorized() {
        String validIngredientHash = "61c0c5a71d1f82001bdaaa6d";
        String invalidIngredientHash = "61c0c5a71d1f82001bdaaa7q";

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(invalidIngredientHash).build());
        ingredientsList.add(Ingredients.builder()._id(validIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithInvalidIngredientHashWithoutAuthorization(order);
    }
    
}