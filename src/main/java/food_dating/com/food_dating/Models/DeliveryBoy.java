package food_dating.com.food_dating.Models;
import lombok.*;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "deliveryBoy")
public class DeliveryBoy {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotNull
    private String phoneNo;

    private String name;

    @NotNull
    private String password;

    private List<Double> location;

    private List<String> delivery = new ArrayList<>();

    private String role;
}
