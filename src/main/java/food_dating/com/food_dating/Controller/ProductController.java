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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
