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
@Document(collection = "order")
public class Order {
    @Id
    private String Id;

    private List<String> products = new ArrayList<>();

    private String deliveryById;

    private String vendorId;

    private String paymentType;

    private Integer bill;

    private enum Status {
        PLACED, PICKUP, COMPLETED
    }

    private Status status;
}
