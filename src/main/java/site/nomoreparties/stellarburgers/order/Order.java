package site.nomoreparties.stellarburgers.order;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Order {

    private List<Ingredients> ingredients;

}