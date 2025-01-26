package food_dating.com.food_dating.Services;

import food_dating.com.food_dating.Models.User;
import food_dating.com.food_dating.Repositary.UserRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServices {

    private final UserRepositary userRepositary;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServices(UserRepositary userRepositary) {
        this.userRepositary = userRepositary;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Save a new user
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt the password
        userRepositary.save(user); // Save the user to the repository
    }

    // Load user by phone number
    public Optional<User> loadByPhoneNo(String phoneNo) {
        return userRepositary.findByPhoneNo(phoneNo); // Find user by phone number
    }

    // Validate user credentials
    public boolean validateUser(String phoneNo, String rawPassword) {
        Optional<User> userOptional = userRepositary.findByPhoneNo(phoneNo);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(rawPassword, user.getPassword()); // Compare passwords
        }

        return false; // User not found or password mismatch
    }

    // Get role of a user
    public String getUserRole(String phoneNo) {
        Optional<User> userOptional = userRepositary.findByPhoneNo(phoneNo);
        return userOptional.map(User::getRole).orElse(null);
    }
}
