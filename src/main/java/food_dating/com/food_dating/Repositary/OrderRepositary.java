package food_dating.com.food_dating.Repositary;

import food_dating.com.food_dating.Models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepositary extends MongoRepository<Order, String> {
}
