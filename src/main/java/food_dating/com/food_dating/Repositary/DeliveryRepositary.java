package food_dating.com.food_dating.Repositary;

import food_dating.com.food_dating.Models.Deliveries;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryRepositary  extends MongoRepository<Deliveries, String> {
}
