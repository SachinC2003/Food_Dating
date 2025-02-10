package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.CartProduct;
import food_dating.com.food_dating.Models.Product;
import food_dating.com.food_dating.Services.CartProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartProductController {
    @Autowired
    private CartProductServices cartProductServices;

    @GetMapping("/{userId}")
    public CartProduct getCart(@PathVariable String userId) {
        return cartProductServices.getCartByUserId(userId);
    }

    @PostMapping("/{userId}/add")
    public CartProduct addToCart(@PathVariable String userId, @RequestBody Product product) {
        return cartProductServices.addToCart(userId, product);
    }

    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable String userId) {
        cartProductServices.clearCart(userId);
    }

    @DeleteMapping("/{userId}/product/{productId}")
    public void deleteProduct(@PathVariable String userId, @PathVariable String productId) {
        cartProductServices.clearCart(userId);
    }
}
