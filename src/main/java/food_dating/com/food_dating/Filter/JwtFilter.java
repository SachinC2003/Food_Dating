package food_dating.com.food_dating.Filter;

import food_dating.com.food_dating.Models.User;
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
            Optional<User> userOptional = userServices.loadByPhoneNo(phoneNo);

            if (userOptional.isPresent() && jwtUtils.validateToken(jwt, userOptional.get())) {
                User user = userOptional.get();

                // Create authentication token
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        Collections.emptyList() // You can use roles/authorities if needed
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in context
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
