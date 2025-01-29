package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.Delivery;
import food_dating.com.food_dating.Models.DeliveryBoy;
import food_dating.com.food_dating.Repositary.DeliveryBoyRepositary;
import food_dating.com.food_dating.Repositary.DeliveryRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/deliveryBoy")
public class DeliveryBoyController {

    @Autowired
    private DeliveryRepositary deliveryRepositary;

    @Autowired
    private DeliveryBoyRepositary deliveryBoyRepositary;


    @GetMapping("/Alldeliveries")
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        List<Delivery> deliveries = deliveryRepositary.findAll();
        return ResponseEntity.ok(deliveries);
    }

    // Route to fetch deliveries of the currently authenticated delivery boy
    @GetMapping("/deliveries")
    public ResponseEntity<?> getDeliveriesForAuthenticatedDeliveryBoy() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String deliveryBoyPhoneNo;

        // Ensure the authenticated user is a DeliveryBoy
        if (principal instanceof DeliveryBoy) {
            DeliveryBoy deliveryBoy = (DeliveryBoy) principal;
            deliveryBoyPhoneNo = deliveryBoy.getPhoneNo();
        } else {
            return ResponseEntity.badRequest().body("Unauthorized: Only delivery boys can access this route.");
        }

        // Fetch the DeliveryBoy from the database using their phone number
        DeliveryBoy deliveryBoy = deliveryBoyRepositary.findByPhoneNo(deliveryBoyPhoneNo)
                .orElse(null);

        if (deliveryBoy == null) {
            return ResponseEntity.badRequest().body("Delivery boy not found.");
        }

        // Fetch deliveries assigned to the DeliveryBoy
        List<Delivery> deliveries = deliveryRepositary.findByDeliveryBoyId(deliveryBoy.getId());
        return ResponseEntity.ok(deliveries);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDeliveryBoy(@RequestBody Map<String, Object> updates) {
        // Get authenticated delivery boy's details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String deliveryBoyPhoneNo;

        // Ensure the authenticated user is a delivery boy
        if (principal instanceof DeliveryBoy) {
            DeliveryBoy authenticatedDeliveryBoy = (DeliveryBoy) principal;
            deliveryBoyPhoneNo = authenticatedDeliveryBoy.getPhoneNo();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Only delivery boys can access this route.");
        }

        // Fetch the current delivery boy from the database
        DeliveryBoy existingDeliveryBoy = deliveryBoyRepositary.findByPhoneNo(deliveryBoyPhoneNo).orElse(null);

        if (existingDeliveryBoy == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery boy not found.");
        }

        // Dynamically update fields based on the keys in the map
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    existingDeliveryBoy.setName((String) value);
                    break;
                case "location":
                    existingDeliveryBoy.setLocation((List<Double>) value);
                    break;
                case "delivery":
                    existingDeliveryBoy.setDelivery((List<String>) value);
                    break;
                case "role":
                    existingDeliveryBoy.setRole((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        // Save the updated delivery boy object
        deliveryBoyRepositary.save(existingDeliveryBoy);

        return ResponseEntity.ok("Delivery boy updated successfully.");
    }


}
