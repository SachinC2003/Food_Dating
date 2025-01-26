package food_dating.com.food_dating.Models;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "delivery")
public class Deliveries {
    @Id
    private String deliveryId;

    private List<String> orderId;

    private Integer totalPrice;

    private String deliveryBoyId;

    private String vendorId;
}
