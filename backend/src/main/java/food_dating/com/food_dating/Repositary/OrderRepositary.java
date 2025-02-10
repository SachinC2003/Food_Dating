package food_dating.com.food_dating.Repositary;

import food_dating.com.food_dating.Models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepositary extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);
    List<Order> findByVendorId(String vendorId);
    List<Order> findByDeliveryBoyId(String deliveryBoyId);
    Optional<Order> findById(String id);
}
