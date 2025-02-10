package food_dating.com.food_dating.Services;

import food_dating.com.food_dating.Models.Vendor;
import food_dating.com.food_dating.Repositary.VendorRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class VendorServices {

    @Autowired
    private VendorRepositary vendorRepositary;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder as a bean

    public Vendor saveVendor(Vendor vendor) {
        vendor.setName(vendor.getName());
        vendor.setPassword(passwordEncoder.encode(vendor.getPassword()));
        vendor.setPhoneNo(vendor.getPhoneNo());
        vendor.setRole(vendor.getRole());

        return vendorRepositary.save(vendor);
    }
}
