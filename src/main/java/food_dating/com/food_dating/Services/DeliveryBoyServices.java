package food_dating.com.food_dating.Services;

import food_dating.com.food_dating.Models.DeliveryBoy;
import food_dating.com.food_dating.Repositary.DeliveryBoyRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DeliveryBoyServices {

    @Autowired
    private DeliveryBoyRepositary deliveryBoyRepositary;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder as a bean

    public void saveDeliveryBoy(DeliveryBoy deliveryBoy) {
        deliveryBoy.setName(deliveryBoy.getName());
        deliveryBoy.setPassword(passwordEncoder.encode(deliveryBoy.getPassword()));
        deliveryBoy.setPhoneNo(deliveryBoy.getPhoneNo());
        deliveryBoy.setRole(deliveryBoy.getRole());

        deliveryBoyRepositary.save(deliveryBoy);
    }
}
