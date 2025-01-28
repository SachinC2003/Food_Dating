package food_dating.com.food_dating.Repositary;

import food_dating.com.food_dating.Models.Delivery;
import food_dating.com.food_dating.Models.DeliveryBoy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryBoyRepositary extends MongoRepository<DeliveryBoy, String> {
    Optional<DeliveryBoy> findByPhoneNo(String phoneNo);
}
