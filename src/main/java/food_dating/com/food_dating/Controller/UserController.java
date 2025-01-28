package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.Order;
import food_dating.com.food_dating.Models.User;
import food_dating.com.food_dating.Models.Vendor;
import food_dating.com.food_dating.Repositary.OrderRepositary;
import food_dating.com.food_dating.Repositary.UserRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
