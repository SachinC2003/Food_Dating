package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.Delivery;
import food_dating.com.food_dating.Models.Vendor;
import food_dating.com.food_dating.Repositary.DeliveryRepositary;
import food_dating.com.food_dating.Repositary.VendorRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    @Autowired
    private DeliveryRepositary deliveryRepositary;

    @Autowired
    private VendorRepositary vendorRepositary;

    // Route to fetch deliveries for the authenticated vendor
    @GetMapping("/deliveries")
    public ResponseEntity<?> getDeliveriesForVendor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String vendorPhoneNo;

        // Ensure the authenticated user is a vendor
        if (principal instanceof Vendor) {
            Vendor vendor = (Vendor) principal;
            vendorPhoneNo = vendor.getPhoneNo();
        } else {
            return ResponseEntity.badRequest().body("Unauthorized: Only vendors can access this route.");
        }

        // Fetch vendor from the database using phone number
        Vendor vendor = vendorRepositary.findByPhoneNo(vendorPhoneNo).orElse(null);
        if (vendor == null) {
            return ResponseEntity.badRequest().body("Vendor not found.");
        }

        // Fetch deliveries matching the vendor ID
        List<Delivery> deliveries = deliveryRepositary.findByVendorId(vendor.getId());
        if (deliveries.isEmpty()) {
            return ResponseEntity.ok("No deliveries found for this vendor.");
        }

        return ResponseEntity.ok(deliveries);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateVendor(@RequestBody Map<String, Object> updates) {
        // Get authenticated vendor's details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String vendorPhoneNo;

        // Ensure the authenticated user is a vendor
        if (principal instanceof Vendor) {
            Vendor authenticatedVendor = (Vendor) principal;
            vendorPhoneNo = authenticatedVendor.getPhoneNo();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Only vendors can access this route.");
        }

        // Fetch the current vendor from the database
        Vendor existingVendor = vendorRepositary.findByPhoneNo(vendorPhoneNo).orElse(null);

        if (existingVendor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vendor not found.");
        }

        // Dynamically update fields based on the keys in the map
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    existingVendor.setName((String) value);
                    break;
                case "location":
                    existingVendor.setLocation((List<Double>) value);
                    break;
                case "delivery":
                    existingVendor.setDelivery((List<String>) value);
                    break;
                case "products":
                    existingVendor.setProducts((List<String>) value);
                    break;
                case "role":
                    existingVendor.setRole((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        // Save the updated vendor object
        vendorRepositary.save(existingVendor);

        return ResponseEntity.ok("Vendor updated successfully.");
    }


}
