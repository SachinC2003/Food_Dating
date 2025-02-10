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

import java.util.*;

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
            // Ensure product details are stored properly
            if (order.getProducts() != null) {
                List<OrderProduct> orderProducts = new ArrayList<>();
                for (OrderProduct orderProduct : order.getProducts()) {
                    if (orderProduct.getProduct() == null || orderProduct.getQuantity() == null || orderProduct.getQuantity() <= 0) {
                        return ResponseEntity.badRequest().body("Each order product must have a valid product and quantity.");
                    }
                    orderProducts.add(new OrderProduct(
                            orderProduct.getProduct(),
                            orderProduct.getQuantity()
                    ));
                }
                order.setProducts(orderProducts);
            }

            // Save the order
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

        log.info("Received updateOrder request with body: {}", requestBody);

        try {
            // Extract orderId from request
            String orderId = (String) requestBody.get("orderId");
            if (orderId == null || orderId.trim().isEmpty()) {
                log.error("Order ID is missing in the request.");
                return ResponseEntity.badRequest().body("Order ID is required.");
            }

            log.info("Looking up order with ID: {}", orderId);

            // Fetch order from database
            Optional<Order> orderOptional = orderRepositary.findById(orderId);
            if (orderOptional.isEmpty()) {
                log.error("Order not found with ID: {}", orderId);
                return ResponseEntity.badRequest().body("Order not found.");
            }

            Order order = orderOptional.get();
            log.info("Fetched order: {}", order);

            // Ensure order belongs to the authenticated user
            if (!order.getUserId().equals(user.getId())) {
                log.error("Unauthorized attempt to update order. User ID: {}, Order User ID: {}", user.getId(), order.getUserId());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: You can only update your own orders.");
            }

            // Check if the order status is PLACED
            if (order.getStatus() != Order.Status.PLACED) {
                log.warn("Order status is not PLACED. Current status: {}", order.getStatus());
                return ResponseEntity.badRequest().body("Order status is not PLACED. Only PLACED orders can be updated.");
            }

            // Process products from request and update only the quantity
            if (requestBody.containsKey("products")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> productsList = (List<Map<String, Object>>) requestBody.get("products");

                if (productsList != null) {
                    Map<String, Integer> updatedQuantities = new HashMap<>();

                    // Extract quantity updates from request
                    for (Map<String, Object> productData : productsList) {
                        String productId = (String) productData.get("productId");
                        Integer quantity = (Integer) productData.get("quantity");

                        if (productId == null || quantity == null || quantity <= 0) {
                            return ResponseEntity.badRequest().body("Each product must have a valid productId and quantity greater than 0.");
                        }

                        updatedQuantities.put(productId, quantity);
                    }

                    // Update only the quantity in the existing order
                    for (OrderProduct orderProduct : order.getProducts()) {
                        if (updatedQuantities.containsKey(orderProduct.getProduct().getId())) {
                            orderProduct.setQuantity(updatedQuantities.get(orderProduct.getProduct().getId()));
                        }
                    }

                    log.info("Updated product quantities: {}", updatedQuantities);
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
