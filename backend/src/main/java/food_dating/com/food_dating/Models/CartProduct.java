package food_dating.com.food_dating.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cart")
public class CartProduct {
    @Id
    private String id;

    private String userId; // Associate the cart with a user

    private List<Product> products = new ArrayList<>(); // List of selected products

    public CartProduct(String userId, List<Product> products) {
        this.userId = userId;
        this.products = products != null ? products : new ArrayList<>();
    }

}

