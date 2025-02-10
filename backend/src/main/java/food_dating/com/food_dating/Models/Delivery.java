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
@Document(collection = "delivery")
public class Delivery {
    @Id
    private String deliveryId;

    private List<Order> orderId = new ArrayList<>();

    private Integer totalPrice;

    private String deliveryBoyId;

    private String vendorId;
}
