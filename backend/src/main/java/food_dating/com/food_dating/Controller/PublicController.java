package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.DeliveryBoy;
import food_dating.com.food_dating.Models.User;
import food_dating.com.food_dating.Models.Vendor;
import food_dating.com.food_dating.Repositary.DeliveryBoyRepositary;
import food_dating.com.food_dating.Repositary.UserRepositary;
import food_dating.com.food_dating.Repositary.VendorRepositary;
import food_dating.com.food_dating.Services.DeliveryBoyServices;
import food_dating.com.food_dating.Services.UserServices;
import food_dating.com.food_dating.Services.VendorServices;
import food_dating.com.food_dating.Utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserServices userServices;
    @Autowired
    private DeliveryBoyServices deliveryBoyServices;
    @Autowired
    private VendorServices vendorServices;
    @Autowired
    private DeliveryBoyRepositary deliveryBoyRepositary;
    @Autowired
    private VendorRepositary vendorRepositary;
    @Autowired
    private UserRepositary userRepositary;

    @PostMapping("/user/register")
    public ResponseEntity<?> signupUser(@RequestBody User user) {
        try {
            // Save the user directly
            User savedUser = userServices.saveUser(user);

            // Generate JWT token
            String jwtToken = jwtUtils.generateToken(user.getPhoneNo());

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("phoneNo", savedUser.getPhoneNo());
            response.put("user", savedUser);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("User registration failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create user: " + e.getMessage());
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> userLogin(@RequestBody User user) {
        try {
            Optional<User> existingUser = userRepositary.findByPhoneNo(user.getPhoneNo());

            if (existingUser.isPresent()) {
                User storedUser = existingUser.get();

                if (BCrypt.checkpw(user.getPassword(), storedUser.getPassword())) {
                    String jwtToken = jwtUtils.generateToken(user.getPhoneNo());

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", jwtToken);
                    response.put("phoneNo", user.getPhoneNo());
                    response.put("user", storedUser);

                    return ResponseEntity.ok(response);
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        } catch (Exception e) {
            log.error("User login failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/deliveryBoy/register")
    public ResponseEntity<?> signupDeliveryBoy(@RequestBody DeliveryBoy deliveryBoy) {
        try {
            // Save the delivery boy directly
            DeliveryBoy savedDeliveryBoy = deliveryBoyServices.saveDeliveryBoy(deliveryBoy);

            // Generate JWT token
            String jwtToken = jwtUtils.generateToken(deliveryBoy.getPhoneNo());

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("phoneNo", savedDeliveryBoy.getPhoneNo());
            response.put("deliveryBoy", savedDeliveryBoy);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Delivery boy registration failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create delivery boy: " + e.getMessage());
        }
    }

    @PostMapping("/deliveryBoy/login")
    public ResponseEntity<?> deliveryBoyLogin(@RequestBody DeliveryBoy deliveryBoy) {
        try {
            Optional<DeliveryBoy> existingDeliveryBoy = deliveryBoyRepositary.findByPhoneNo(deliveryBoy.getPhoneNo());

            if (existingDeliveryBoy.isPresent()) {
                DeliveryBoy storedDeliveryBoy = existingDeliveryBoy.get();

                if (BCrypt.checkpw(deliveryBoy.getPassword(), storedDeliveryBoy.getPassword())) {
                    String jwtToken = jwtUtils.generateToken(deliveryBoy.getPhoneNo());

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", jwtToken);
                    response.put("phoneNo", deliveryBoy.getPhoneNo());
                    response.put("deliveryBoy", storedDeliveryBoy);

                    return ResponseEntity.ok(response);
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        } catch (Exception e) {
            log.error("Delivery boy login failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/vendor/register")
    public ResponseEntity<?> signupVendor(@RequestBody Vendor vendor) {
        try {
            // Save the vendor directly
            Vendor savedVendor = vendorServices.saveVendor(vendor);

            // Generate JWT token
            String jwtToken = jwtUtils.generateToken(vendor.getPhoneNo());

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("phoneNo", savedVendor.getPhoneNo());
            response.put("vendor", savedVendor);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Vendor registration failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create vendor: " + e.getMessage());
        }
    }

    @PostMapping("/vendor/login")
    public ResponseEntity<?> loginVendor(@RequestBody Vendor vendor) {
        try {
            Optional<Vendor> existingVendor = vendorRepositary.findByPhoneNo(vendor.getPhoneNo());

            if (existingVendor.isPresent()) {
                Vendor storedVendor = existingVendor.get();

                if (BCrypt.checkpw(vendor.getPassword(), storedVendor.getPassword())) {
                    String jwtToken = jwtUtils.generateToken(vendor.getPhoneNo());

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", jwtToken);
                    response.put("phoneNo", vendor.getPhoneNo());
                    response.put("vendor", storedVendor);

                    return ResponseEntity.ok(response);
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        } catch (Exception e) {
            log.error("Vendor login failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication failed: " + e.getMessage());
        }
    }
}