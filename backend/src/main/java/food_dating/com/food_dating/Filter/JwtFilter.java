package food_dating.com.food_dating.Filter;

import food_dating.com.food_dating.Models.DeliveryBoy;
import food_dating.com.food_dating.Models.User;
import food_dating.com.food_dating.Models.Vendor;
import food_dating.com.food_dating.Repositary.DeliveryBoyRepositary;
import food_dating.com.food_dating.Repositary.VendorRepositary;
import food_dating.com.food_dating.Services.UserServices;
import food_dating.com.food_dating.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserServices userServices;

    @Autowired
    private VendorRepositary vendorRepository;

    @Autowired
    private DeliveryBoyRepositary deliveryBoyRepositary;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String phoneNo = null;
        String jwt = null;

        // Extract JWT token and phone number
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            phoneNo = jwtUtils.extractPhoneNo(jwt);
        }

        // Validate JWT and set authentication context
        if (phoneNo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            boolean isAuthenticated = false;

            // Check if the phone number belongs to a User
            Optional<User> userOptional = userServices.loadByPhoneNo(phoneNo);
            if (userOptional.isPresent() && jwtUtils.validateToken(jwt, userOptional.get())) {
                User user = userOptional.get();
                setAuthentication(user, request);
                isAuthenticated = true;
            }

            // Check if the phone number belongs to a Vendor
            if (!isAuthenticated) {
                Optional<Vendor> vendorOptional = vendorRepository.findByPhoneNo(phoneNo);
                if (vendorOptional.isPresent() && jwtUtils.validateToken(jwt, vendorOptional.get())) {
                    Vendor vendor = vendorOptional.get();
                    setAuthentication(vendor, request);
                }
            }

            // Check if the phone number belongs to a delivary boy
            if (!isAuthenticated) {
                Optional<DeliveryBoy> deliveryBoyOptional = deliveryBoyRepositary.findByPhoneNo(phoneNo);
                if (deliveryBoyOptional.isPresent() && jwtUtils.validateToken(jwt, deliveryBoyOptional.get())) {
                    DeliveryBoy deliveryBoy = deliveryBoyOptional.get();
                    setAuthentication(deliveryBoy, request);
                }
            }
        }

        chain.doFilter(request, response);
    }

    private void setAuthentication(Object principal, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                Collections.emptyList() // Use roles/authorities if needed
        );
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}

