package food_dating.com.food_dating.Controller;

import food_dating.com.food_dating.Models.DeliveryBoy;
import food_dating.com.food_dating.Models.User;
import food_dating.com.food_dating.Models.Vendor;
import food_dating.com.food_dating.Repositary.VendorRepositary;
import food_dating.com.food_dating.Services.DeliveryBoyServices;
import food_dating.com.food_dating.Services.DeliveryServices;
import food_dating.com.food_dating.Services.UserServices;
import food_dating.com.food_dating.Services.VendorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserServices userServices;
    @Autowired
    private DeliveryBoyServices deliveryBoyServices;
    @Autowired
    private VendorServices vendorServices;


    @PostMapping("/user/signup")
    public ResponseEntity<String> signupUser(@RequestBody User user)
    {
        try{
            userServices.saveUser(user);
            return new ResponseEntity<>("User created.", HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>("Failed to create user.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/deliveryBoy/signup")
    public ResponseEntity<String> signupDeliveryBoy(@RequestBody DeliveryBoy deliveryBoy)
    {
        try{
            deliveryBoyServices.saveDeliveryBoy(deliveryBoy);
            return new ResponseEntity<>("Delivery boy created.", HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>("Failed to create delivery boy.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/vendor/signup")
    public ResponseEntity<String> signupVendor(@RequestBody Vendor vendor)
    {
        try{
            vendorServices.saveVendor(vendor);
            return new ResponseEntity<>("Vendor created.", HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>("Failed to create vendor.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
