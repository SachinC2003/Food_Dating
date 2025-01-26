package food_dating.com.food_dating.Models;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order")
public class Order {
    @Id
    private String productId;

    private String productName;

    private Integer productPrice;

    private Integer availableQuantity;

    private String additionalDetails;
}
