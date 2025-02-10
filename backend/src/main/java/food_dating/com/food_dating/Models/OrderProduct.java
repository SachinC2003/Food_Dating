package food_dating.com.food_dating.Models;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {
    private Product product;  // Reference to the product
    private Integer quantity;  // Quantity of the product
}
