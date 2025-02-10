package food_dating.com.food_dating.Repositary;

import food_dating.com.food_dating.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositary extends MongoRepository<User, String> {
    Optional<User> findByPhoneNo(String phoneNo);
}
