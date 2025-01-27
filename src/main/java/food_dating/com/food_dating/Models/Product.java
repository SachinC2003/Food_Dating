package food_dating.com.food_dating.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product")
public class Product {
    @Id
    private String id;

    private String productName;

    private String description;

    private List<String> categories;

    private Double price;

    private Integer stock;

    private String vendorId;

    private List<String> images;

    private enum AvailabilityStatus {
        AVAILABLE, OUT_OF_STOCK, DISCONTINUED
    }

    private AvailabilityStatus status;
}

