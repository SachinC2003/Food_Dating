package food_dating.com.food_dating.Utils;

import food_dating.com.food_dating.Models.User;
import food_dating.com.food_dating.Models.Vendor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private static final String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Extract phone number from token
    public String extractPhoneNo(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // Extract all claims from token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate token for a phone number
    public String generateToken(String phoneNo) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, phoneNo);
    }

    // Create a JWT token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 50)) // 50 minutes
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate token for User or Vendor
    public Boolean validateToken(String token, Object entity) {
        final String phoneNo = extractPhoneNo(token);

        // Handle User validation
        if (entity instanceof User) {
            User user = (User) entity;
            return phoneNo.equals(user.getPhoneNo()) && !isTokenExpired(token);
        }

        // Handle Vendor validation
        if (entity instanceof Vendor) {
            Vendor vendor = (Vendor) entity;
            return phoneNo.equals(vendor.getPhoneNo()) && !isTokenExpired(token);
        }

        // Default: Invalid token
        return false;
    }
}
