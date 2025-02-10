package food_dating.com.food_dating.Models;
import lombok.*;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    private String id;
    public void setId(ObjectId id) {
        this.id = id.toString();
    }
    private List<OrderProduct> products = new ArrayList<>();

    private String userId;

    private String deliveryBoyId;

    private String vendorId;

    private String paymentType;

    private Integer bill;

    public enum Status {
        PLACED, PICKUP, COMPLETED
    }

    private Status status;

    private List<Double> location;
}
