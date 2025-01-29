package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.Product;
import food_dating.com.food_dating.Models.Vendor;
import food_dating.com.food_dating.Repositary.ProductRepositary;
import food_dating.com.food_dating.Repositary.VendorRepositary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepositary productRepositary;
    @Autowired
    private VendorRepositary vendorRepository;  // Add this

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String vendorPhoneNo;
        if (principal instanceof Vendor) {
            Vendor vendor = (Vendor) principal;
            vendorPhoneNo = vendor.getPhoneNo();
        } else {
            return ResponseEntity.badRequest().body("Unauthorized: Only vendors can add products");
        }

        log.info("Vendor phone no: {}", vendorPhoneNo);
        try {
            // Find the vendor by phone number
            Optional<Vendor> vendorOptional = vendorRepository.findByPhoneNo(vendorPhoneNo);
            log.info("vendor ", vendorOptional);
            if (!vendorOptional.isPresent()) {
                return ResponseEntity.badRequest().body("Vendor not found");
            }

            Vendor vendor = vendorOptional.get();

            // Associate the vendor ID with the product
            product.setVendorId(vendor.getId());

            // Save the product to the database
            Product savedProduct = productRepositary.save(product);

            vendor.getProducts().add(savedProduct.getId());
            vendorRepository.save(vendor);

            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred while adding product: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody Product updatedProduct) {
        // Retrieve the authenticated vendor's phone number
        String vendorPhoneNo;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof Vendor) {
            Vendor vendor = (Vendor) principal;
            vendorPhoneNo = vendor.getPhoneNo();
        } else {
            return ResponseEntity.badRequest().body("Unauthorized: Only vendors can update products");
        }

        log.info("Vendor phone no: {}", vendorPhoneNo);

        try {
            // Find the vendor by phone number
            Optional<Vendor> vendorOptional = vendorRepository.findByPhoneNo(vendorPhoneNo);
            if (!vendorOptional.isPresent()) {
                return ResponseEntity.badRequest().body("Vendor not found");
            }

            Vendor vendor = vendorOptional.get();

            // Check if the productId exists in the vendor's list of products
            if (!vendor.getProducts().contains(updatedProduct.getId())) {
                return ResponseEntity.badRequest().body("Product not found in vendor's product list");
            }

            // Retrieve the product to update
            Optional<Product> existingProductOpt = productRepositary.findById(updatedProduct.getId());
            if (!existingProductOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Product not found");
            }

            Product existingProduct = existingProductOpt.get();

            // Update the product fields if provided
            if (updatedProduct.getProductName() != null) {
                existingProduct.setProductName(updatedProduct.getProductName());
            }
            if (updatedProduct.getDescription() != null) {
                existingProduct.setDescription(updatedProduct.getDescription());
            }
            if (updatedProduct.getCategories() != null) {
                existingProduct.setCategories(updatedProduct.getCategories());
            }
            if (updatedProduct.getPrice() != null) {
                existingProduct.setPrice(updatedProduct.getPrice());
            }
            if (updatedProduct.getStock() != null) {
                existingProduct.setStock(updatedProduct.getStock());
            }
            if (updatedProduct.getImages() != null) {
                existingProduct.setImages(updatedProduct.getImages());
            }
            if (updatedProduct.getStatus() != null) {
                existingProduct.setStatus(updatedProduct.getStatus());
            }

            // Save the updated product
            productRepositary.save(existingProduct);

            return ResponseEntity.ok("Product updated successfully");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating product: " + e.getMessage());
        }
    }
}
