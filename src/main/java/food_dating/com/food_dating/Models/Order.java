package food_dating.com.food_dating.Models;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order")
public class Order {
    @Id
    private String Id;

    private Map<String, Integer> products = new HashMap<>();

    private String userId;

    private String deliveryBoyId;

    private String vendorId;

    private String paymentType;

    private Integer bill;

    private enum Status {
        PLACED, PICKUP, COMPLETED
    }

    private Status status;
}
