package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.DeliveryBoy;
import food_dating.com.food_dating.Models.User;
import food_dating.com.food_dating.Models.Vendor;
import food_dating.com.food_dating.Repositary.DeliveryBoyRepositary;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

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

    @PostMapping("/user/register")
    public ResponseEntity<String> signupUser(@RequestBody User user)
    {
        try{
            userServices.saveUser(user);
            return new ResponseEntity<>("User created.", HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>("Failed to create user.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getPhoneNo(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String jwtToken = jwtUtils.generateToken(user.getPhoneNo());
                return ResponseEntity.ok()
                        .body(new HashMap<String, String>() {{
                            put("token", jwtToken);
                            put("phoneNo", user.getPhoneNo());
                        }});
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication failed: " + e.getMessage());
        }
    }


    @PostMapping("/deliveryBoy/register")
    public ResponseEntity<String> signupDeliveryBoy(@RequestBody DeliveryBoy deliveryBoy)
    {
        try{
            deliveryBoyServices.saveDeliveryBoy(deliveryBoy);
            return new ResponseEntity<>("Delivery boy created.", HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>("Failed to create delivery boy.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/deliveryBoy/login")
    public ResponseEntity<?> deliveryBoyLogin(@RequestBody DeliveryBoy deliveryBoy) {
        try {
            // Fetch the delivery boy from the database using the phone number
            Optional<DeliveryBoy> existingDeliveryBoy = deliveryBoyRepositary.findByPhoneNo(deliveryBoy.getPhoneNo());

            // Check if the delivery boy exists
            if (existingDeliveryBoy.isPresent()) {
                DeliveryBoy storedDeliveryBoy = existingDeliveryBoy.get();

                // Compare the provided password with the stored hashed password using BCrypt
                if (BCrypt.checkpw(deliveryBoy.getPassword(), storedDeliveryBoy.getPassword())) {
                    log.info("Authentication successful");

                    // Generate JWT token
                    String jwtToken = jwtUtils.generateToken(deliveryBoy.getPhoneNo());

                    // Return the token in the response
                    return ResponseEntity.ok()
                            .body(new HashMap<String, String>() {{
                                put("token", jwtToken);
                                put("phoneNo", deliveryBoy.getPhoneNo());
                            }});
                } else {
                    log.info("Invalid credentials");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Invalid credentials");
                }
            } else {
                log.info("Delivery Boy not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Delivery boy not found");
            }
        } catch (Exception e) {
            log.error("Authentication failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/vendor/register")
    public ResponseEntity<String> signupVendor(@RequestBody Vendor vendor)
    {
        try{
            vendorServices.saveVendor(vendor);
            return new ResponseEntity<>("Vendor created.", HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>("Failed to create vendor.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/vendor/login")
    public ResponseEntity<?> vendorLogin(@RequestBody Vendor vendor) {
        try {
            Optional<Vendor> existingVendor = vendorRepositary.findByPhoneNo(vendor.getPhoneNo());

            // Check if the delivery boy exists
            if (existingVendor.isPresent()) {
                Vendor storedVendor= existingVendor.get();

                // Compare the provided password with the stored hashed password using BCrypt
                if (BCrypt.checkpw(vendor.getPassword(), storedVendor.getPassword())) {
                    log.info("Authentication successful");

                    // Generate JWT token
                    String jwtToken = jwtUtils.generateToken(vendor.getPhoneNo());

                    // Return the token in the response
                    return ResponseEntity.ok()
                            .body(new HashMap<String, String>() {{
                                put("token", jwtToken);
                                put("phoneNo", vendor.getPhoneNo());
                            }});
                } else {
                    log.info("Invalid credentials");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Invalid credentials");
                }
            } else {
                log.info("vendor not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("vendor not found");
            }
        } catch (Exception e) {
            log.error("Authentication failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication failed: " + e.getMessage());
        }
    }
}
