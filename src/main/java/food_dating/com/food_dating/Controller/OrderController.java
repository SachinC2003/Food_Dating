package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.Order;
import food_dating.com.food_dating.Models.OrderProduct;
import food_dating.com.food_dating.Models.User;
import food_dating.com.food_dating.Repositary.OrderRepositary;
import food_dating.com.food_dating.Repositary.UserRepositary;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
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
                order.setUserId(user.getId());
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


    @PutMapping("/updateOrder")
    public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> requestBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userPhoneNo = user.getPhoneNo();

        log.info("Received updateOrder request with body: {}", requestBody);

        try {
            // Extract orderId from request body
            String orderId = (String) requestBody.get("orderId");
            if (orderId == null || orderId.trim().isEmpty()) {
                log.error("Order ID is missing in the request.");
                return ResponseEntity.badRequest().body("Order ID is required.");
            }

            log.info("Looking up order with ID: {}", orderId);

            // Fetch order from the database using String ID
            Optional<Order> orderOptional = orderRepositary.findById(orderId);
            if (orderOptional.isEmpty()) {
                log.error("Order not found with ID: {}", orderId);
                return ResponseEntity.badRequest().body("Order not found.");
            }

            Order order = orderOptional.get();
            log.info("Fetched order: {}", order);

            // Ensure the order belongs to the authenticated user
            if (!order.getUserId().equals(user.getId())) {
                log.error("Unauthorized attempt to update order. User ID: {}, Order User ID: {}", user.getId(), order.getUserId());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: You can only update your own orders.");
            }

            // Check if the order status is PLACED
            if (order.getStatus() != Order.Status.PLACED) {
                log.warn("Order status is not PLACED. Current status: {}", order.getStatus());
                return ResponseEntity.badRequest().body("Order status is not PLACED. Only PLACED orders can be updated.");
            }

            // Process updates from request body
            if (requestBody.containsKey("products")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> productsList = (List<Map<String, Object>>) requestBody.get("products");

                if (productsList != null) {
                    List<OrderProduct> orderProducts = new ArrayList<>();
                    for (Map<String, Object> productData : productsList) {
                        String productId = (String) productData.get("orderId");
                        Integer amount = (Integer) productData.get("amount");

                        if (productId == null || amount == null || amount <= 0) {
                            return ResponseEntity.badRequest().body("Product ID and amount must be provided, and amount must be greater than 0.");
                        }

                        orderProducts.add(new OrderProduct(productId, amount));
                    }

                    order.setProducts(orderProducts);
                    log.info("Updated products: {}", orderProducts);
                }
            }

            // Save updated order
            orderRepositary.save(order);
            log.info("Order updated successfully: {}", order);

            return ResponseEntity.ok("Order updated successfully.");

        } catch (Exception e) {
            log.error("Error updating order: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }




}
