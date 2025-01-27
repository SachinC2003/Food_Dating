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

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepositary.save(user);
    }

    public Optional<User> loadByPhoneNo(String phoneNo) {
        return userRepositary.findByPhoneNo(phoneNo);
    }

    public boolean validateUser(String phoneNo, String rawPassword) {
        Optional<User> userOptional = userRepositary.findByPhoneNo(phoneNo);
        return userOptional
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

    public String getUserRole(String phoneNo) {
        Optional<User> userOptional = userRepositary.findByPhoneNo(phoneNo);
        return userOptional.map(User::getRole).orElse(null);
    }
}