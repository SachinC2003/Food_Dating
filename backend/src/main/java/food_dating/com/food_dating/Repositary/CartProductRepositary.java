package food_dating.com.food_dating.Repositary;

import food_dating.com.food_dating.Models.CartProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartProductRepositary extends MongoRepository<CartProduct, String> {
    Optional<CartProduct> findByUserId(String userId);
}
