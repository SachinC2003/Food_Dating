package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.Delivery;
import food_dating.com.food_dating.Models.DeliveryBoy;
import food_dating.com.food_dating.Repositary.DeliveryBoyRepositary;
import food_dating.com.food_dating.Repositary.DeliveryRepositary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryRepositary deliveryRepositary;

    @Autowired
    private DeliveryBoyRepositary deliveryBoyRepositary;

    @PostMapping
    public ResponseEntity<?> createDelivery(@RequestBody Delivery delivery) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        log.info("delivery boy" , principal);
        if (!(principal instanceof DeliveryBoy)) {
            return ResponseEntity.badRequest().body("Unauthorized: Only delivery boys can add a delivery");
        }

        DeliveryBoy deliveryBoy = (DeliveryBoy) principal;
        String deliveryBoyPhoneNo = deliveryBoy.getPhoneNo();
        log.info("Delivery boy phone number: {}", deliveryBoyPhoneNo);

        try {
            // Fetch DeliveryBoy from the database
            DeliveryBoy existingDeliveryBoy = deliveryBoyRepositary.findByPhoneNo(deliveryBoyPhoneNo)
                    .orElseThrow(() -> new IllegalArgumentException("Delivery boy not found with phone number: " + deliveryBoyPhoneNo));

            // Set DeliveryBoy ID in the Delivery object
            delivery.setDeliveryBoyId(existingDeliveryBoy.getId());

            // Save Delivery to the database
            Delivery savedDelivery = deliveryRepositary.save(delivery);

            // Update DeliveryBoy's list of deliveries
            existingDeliveryBoy.getDelivery().add(savedDelivery.getDeliveryId());
            deliveryBoyRepositary.save(existingDeliveryBoy);

            log.info("Delivery successfully created with ID: {}", savedDelivery.getDeliveryId());
            return ResponseEntity.ok(savedDelivery);
        } catch (Exception e) {
            log.error("Error occurred while adding delivery: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("An error occurred while adding the delivery: " + e.getMessage());
        }
    }
}
