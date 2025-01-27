package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.Order;
import food_dating.com.food_dating.Models.User;
import food_dating.com.food_dating.Repositary.OrderRepositary;
import food_dating.com.food_dating.Repositary.UserRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderRepositary orderRepositary;

    @Autowired
    private UserRepositary userRepositary;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userPhoneNo = user.getPhoneNo();

        try {
            // Save the order to the database
            Order savedOrder = orderRepositary.save(order);

            // Update the user's order list
            Optional<User> userOptional = userRepositary.findByPhoneNo(userPhoneNo);
            if (userOptional.isPresent()) {
                user = userOptional.get();
                if (user.getOrders() == null) {
                    user.setOrders(new ArrayList<>());
                }
                user.getOrders().add(savedOrder.getId());
                userRepositary.save(user);

                return ResponseEntity.ok(savedOrder);
            } else {
                return ResponseEntity.badRequest().body("User not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating order: " + e.getMessage());
        }
    }

}
