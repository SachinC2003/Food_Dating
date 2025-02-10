package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.Delivery;
import food_dating.com.food_dating.Models.Order;
import food_dating.com.food_dating.Models.Product;
import food_dating.com.food_dating.Models.Vendor;
import food_dating.com.food_dating.Repositary.DeliveryRepositary;
import food_dating.com.food_dating.Repositary.OrderRepositary;
import food_dating.com.food_dating.Repositary.ProductRepositary;
import food_dating.com.food_dating.Repositary.VendorRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/vendor")
public class VendorController {

    @Autowired
    private DeliveryRepositary deliveryRepositary;

    @Autowired
    private ProductRepositary productRepositary;

    @Autowired
    private OrderRepositary orderRepositary;

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
                case "image":
                    existingVendor.setImage((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        // Save the updated vendor object
        vendorRepositary.save(existingVendor);

        return ResponseEntity.ok("Vendor updated successfully.");
    }


    @GetMapping("/product")
    public ResponseEntity<?> getProductOfVendor() {
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

        // Fetch products matching the vendor ID
        List<Product> products = productRepositary.findByVendorId(vendor.getId());
        if (products.isEmpty()) {
            return ResponseEntity.ok("No products found for this vendor.");
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrdersOfVendor() {
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

        // Fetch products matching the vendor ID
        List<Order> orders = orderRepositary.findByVendorId(vendor.getId());
        if (orders.isEmpty()) {
            if (orders == null || orders.isEmpty()) {
                orders = new ArrayList<>();  // Ensure it's an empty array instead of null
            }
        }

        return ResponseEntity.ok(orders);
    }
}
