package com.LoginRegisterAuth.Service;

import com.LoginRegisterAuth.Controller.userController;
import com.LoginRegisterAuth.Model.User;
import com.LoginRegisterAuth.Repository.userRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class userService {

    private static final Logger logger = LoggerFactory.getLogger(userController.class);

    @Autowired
    private userRepository userRepository;

    public void saveUser(User user) {
        if (user.getEmail().endsWith("@numetry.in")) {
            user.setRole("ROLE_ADMIN");
        } else if (user.getEmail().endsWith("@gmail.com")) {
            user.setRole("ROLE_USER");
        }
        user.setOtpVerified(true); // Ensure OTP is marked as verified
        userRepository.save(user);
    }

    public boolean userExists(String username, String email) {
        return userRepository.existsByUsername(username) || userRepository.existsById(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public boolean validateUserCredentials(String email, String rawPassword) {
        User user = findByEmail(email);
        if (user == null) {
            return false;
        }
        return rawPassword.equals(user.getPassword());
    }
}
