package food_dating.com.food_dating.Services;

import food_dating.com.food_dating.Models.DeliveryBoy;
import food_dating.com.food_dating.Repositary.DeliveryBoyRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeliveryBoyServices {

    private final DeliveryBoyRepositary deliveryBoyRepositary;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DeliveryBoyServices(DeliveryBoyRepositary deliveryBoyRepositary) {
        this.deliveryBoyRepositary = deliveryBoyRepositary;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Save a new delivery boy
    public DeliveryBoy saveDeliveryBoy(DeliveryBoy deliveryBoy) {
        deliveryBoy.setPassword(passwordEncoder.encode(deliveryBoy.getPassword())); // Encrypt the password
        return deliveryBoyRepositary.save(deliveryBoy); // Save to the repository
    }

    // Load delivery boy by phone number
    public Optional<DeliveryBoy> loadByPhoneNo(String phoneNo) {
        return deliveryBoyRepositary.findByPhoneNo(phoneNo); // Find by phone number
    }

    // Validate delivery boy credentials
    public boolean validateDeliveryBoy(String phoneNo, String rawPassword) {
        Optional<DeliveryBoy> deliveryBoyOptional = deliveryBoyRepositary.findByPhoneNo(phoneNo);
        return deliveryBoyOptional
                .map(deliveryBoy -> passwordEncoder.matches(rawPassword, deliveryBoy.getPassword()))
                .orElse(false);
    }

    // Get role of a delivery boy
    public String getDeliveryBoyRole(String phoneNo) {
        Optional<DeliveryBoy> deliveryBoyOptional = deliveryBoyRepositary.findByPhoneNo(phoneNo);
        return deliveryBoyOptional.map(DeliveryBoy::getRole).orElse(null);
    }
}
