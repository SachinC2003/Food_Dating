package food_dating.com.food_dating.Repositary;

import food_dating.com.food_dating.Models.Vendor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepositary extends MongoRepository<Vendor, String> {
    Optional<Vendor> findByPhoneNo(String phoneNo);
}
