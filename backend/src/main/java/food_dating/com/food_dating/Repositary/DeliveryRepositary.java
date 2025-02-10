package food_dating.com.food_dating.Repositary;

import food_dating.com.food_dating.Models.Delivery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeliveryRepositary  extends MongoRepository<Delivery, String> {
    List<Delivery> findByDeliveryBoyId(String deliveryBoyId);
    List<Delivery> findByVendorId(String vendorId);
}
