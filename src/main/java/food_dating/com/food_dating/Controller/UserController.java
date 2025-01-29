package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.Order;
import food_dating.com.food_dating.Models.User;
import food_dating.com.food_dating.Models.Vendor;
import food_dating.com.food_dating.Repositary.OrderRepositary;
import food_dating.com.food_dating.Repositary.UserRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private OrderRepositary orderRepositary;

    @Autowired
    private UserRepositary userRepositary;

    // Route to fetch orders for a specific user by userId
    @GetMapping("/orders")
    public ResponseEntity<?> getOrdersByUserId() {
        // Fetch orders by userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String userPhoneNo;

        // Ensure the authenticated user is a vendor
        if (principal instanceof User) {
            User user = (User) principal;
            userPhoneNo = user.getPhoneNo();
        } else {
            return ResponseEntity.badRequest().body("Unauthorized: Only user can access this route.");
        }

        // Fetch user from the database using phone number
        User user = userRepositary.findByPhoneNo(userPhoneNo).orElse(null);
        List<Order> orders = orderRepositary.findByUserId(user.getId());

        if (orders.isEmpty()) {
            return ResponseEntity.ok("No orders found for this user.");
        }

        return ResponseEntity.ok(orders);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, Object> updates) {
        // Get authenticated user's details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String userPhoneNo;

        // Ensure the authenticated user is a user
        if (principal instanceof User) {
            User authenticatedUser = (User) principal;
            userPhoneNo = authenticatedUser.getPhoneNo();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Only users can access this route.");
        }

        // Fetch the current user from the database
        User existingUser = userRepositary.findByPhoneNo(userPhoneNo).orElse(null);

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // Update fields dynamically based on keys in the map
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    existingUser.setName((String) value);
                    break;
                case "location":
                    existingUser.setLocation((List<Double>) value);
                    break;
                case "role":
                    existingUser.setRole((String) value);
                    break;
                case "typeOfUser":
                    existingUser.setTypeOfUser((String) value);
                    break;
                case "orders":
                    existingUser.setOrders((List<String>) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        // Save the updated user object
        userRepositary.save(existingUser);

        return ResponseEntity.ok("User updated successfully.");
    }



}
