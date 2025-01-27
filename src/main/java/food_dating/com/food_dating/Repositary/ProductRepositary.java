package food_dating.com.food_dating.Repositary;

import food_dating.com.food_dating.Models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepositary extends MongoRepository<Product, String> {
}
