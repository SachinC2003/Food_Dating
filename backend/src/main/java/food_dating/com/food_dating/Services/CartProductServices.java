package food_dating.com.food_dating.Services;

import food_dating.com.food_dating.Models.CartProduct;
import food_dating.com.food_dating.Models.Product;
import food_dating.com.food_dating.Repositary.CartProductRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartProductServices {
    @Autowired
    private CartProductRepositary cartProductRepositary;

    public CartProduct getCartByUserId(String userId) {
        return cartProductRepositary.findByUserId(userId).orElse(new CartProduct(userId, null));
    }

    public CartProduct addToCart(String userId, Product product) {
        CartProduct cart = cartProductRepositary.findByUserId(userId)
                .orElseGet(() -> {
                    CartProduct newCart = new CartProduct(userId, new ArrayList<>());
                    newCart.setUserId(userId); // Explicitly set userId
                    return newCart;
                });


        if (cart.getProducts() == null) {
            cart.setProducts(new ArrayList<>());
        }
        cart.getProducts().add(product);

        return cartProductRepositary.save(cart);
    }

    public CartProduct deleteProduct(String userId, String productId) {
        CartProduct cart = cartProductRepositary.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        if (cart.getProducts() != null) {
            cart.getProducts().removeIf(product -> product.getId().equals(productId));
        }

        return cartProductRepositary.save(cart);
    }

    public void clearCart(String userId) {
        cartProductRepositary.findByUserId(userId).ifPresent(cartProductRepositary::delete);
    }
}

