package food_dating.com.food_dating.Repositary;

import food_dating.com.food_dating.Models.Vendor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepositary extends MongoRepository<Vendor, String> {
}
