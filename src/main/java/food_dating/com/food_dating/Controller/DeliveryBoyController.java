package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.Delivery;
import food_dating.com.food_dating.Models.DeliveryBoy;
import food_dating.com.food_dating.Repositary.DeliveryBoyRepositary;
import food_dating.com.food_dating.Repositary.DeliveryRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
