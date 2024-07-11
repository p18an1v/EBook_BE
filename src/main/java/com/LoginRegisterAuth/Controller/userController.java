package com.LoginRegisterAuth.Controller;

import com.LoginRegisterAuth.Model.LoginRequest;
import com.LoginRegisterAuth.Model.User;
import com.LoginRegisterAuth.Model.UserActivity;
import com.LoginRegisterAuth.Model.otpData;
import com.LoginRegisterAuth.Service.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import com.LoginRegisterAuth.Service.userService;

@RestController
@RequestMapping("/api")
public class userController {

    @Autowired
    private userService userService;

    @Autowired
    private otpController otpController;

    @Autowired
    private UserActivityService userActivityService;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.\\w+$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^(\\+91|91)?[7-9]\\d{9}$");

    private static final Logger logger = LoggerFactory.getLogger(userController.class);

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        logger.info("Register request received for user: " + user.getEmail());


        if (user.getFullName().isEmpty() || !user.getFullName().matches("[a-zA-Z ]+")) {
            return ResponseEntity.badRequest().body("Invalid full name.");
        }
        if (!MOBILE_PATTERN.matcher(user.getMobile()).matches()) {
            return ResponseEntity.badRequest().body("Invalid mobile number.");
        }
        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            return ResponseEntity.badRequest().body("Invalid email address.");
        }
        if (user.getUsername().length() <= 3) {
            return ResponseEntity.badRequest().body("Username must be longer than 3 characters.");
        }
        if (user.getPassword().length() < 8) {
            return ResponseEntity.badRequest().body("Password must be at least 8 characters long.");
        }
        if (userService.userExists(user.getUsername(), user.getEmail())) {
            return ResponseEntity.badRequest().body("User with this username or email already exists.");
        }

        otpController.storeTemporaryUser(user);

        otpData otp = new otpData(user.getEmail(), 0);
        otpController.sendOtp(otp);

        return ResponseEntity.ok("User OTP sent for verification successfully.");
    }

    @PostMapping("/verify-registration-otp")
    public ResponseEntity<?> verifyRegistrationOtp(@RequestBody otpData otp) {
        Map<String, String> verificationResult = otpController.verifyOtp(otp);
        String message = verificationResult.get("message");
        if ("OTP verified successfully".equals(message)) {
            User user = otpController.getTemporaryUser(otp.getEmail());
            if (user != null) {
                user.setOtpVerified(true);
                userService.saveUser(user);
                otpController.removeTemporaryUser(otp.getEmail());
                return ResponseEntity.ok("User registration completed successfully.");
            } else {
                return ResponseEntity.badRequest().body("No registration data found for this OTP.");
            }
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        boolean isValidUser = userService.validateUserCredentials(loginRequest.getEmail(), loginRequest.getPassword());
        if (!isValidUser) {
            return ResponseEntity.badRequest().body("Invalid email or password.");
        }

        // Retrieve user role
        User user = userService.findByEmail(loginRequest.getEmail());
        String role = user.getRole();

        // Determine dashboard URL based on role
        String dashboardUrl = "/user-dashboard"; // Default URL for regular users
        if ("ROLE_ADMIN".equals(role) && loginRequest.getEmail().endsWith("@numetry.in")) {
            dashboardUrl = "/admin-dashboard"; // Update URL for admin users with @numetry.in email
        }

        // Log login activity
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        Instant instant = Instant.parse(loginRequest.getLoginTimestamp());
        LocalDateTime loginTimestamp = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        userActivityService.logLoginActivity(loginRequest.getEmail(), loginTimestamp);

        // Prepare response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("role", role);
        response.put("dashboardUrl", dashboardUrl);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> payload) {
        System.out.println("Logout request received with payload: " + payload);
        String email = payload.get("email");
        String logoutTimestampStr = payload.get("logoutTimestamp");

        if (email == null || logoutTimestampStr == null) {
            return ResponseEntity.badRequest().body("Email and logoutTimestamp must be provided");
        }

        LocalDateTime logoutTimestamp;
        try {
            System.out.println("Parsing logout timestamp: " + logoutTimestampStr);
            Instant instant = Instant.parse(logoutTimestampStr);
            logoutTimestamp = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            System.out.println("Parsed logout timestamp: " + logoutTimestamp);
        } catch ( DateTimeException e) {
            System.out.println("Error parsing logout timestamp: " + logoutTimestampStr);
            return ResponseEntity.badRequest().body("Invalid logoutTimestamp format");
        }

        userActivityService.logLogoutActivity(email, logoutTimestamp);
        System.out.println("Logged logout activity for email: " + email);

        return ResponseEntity.ok("Logout successful");
    }


    @GetMapping("/activities")
    public ResponseEntity<?> getUserActivities() {
        List<UserActivity> activities = userActivityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/user-data")
    public ResponseEntity<?> getUserDataByEmail(@RequestParam(value = "email") String email) {
        User user = userService.findByEmail(email);
        if (user != null) {
            // Prepare response with user data
            Map<String, String> userData = new HashMap<>();
            userData.put("email", user.getEmail());
            userData.put("username", user.getUsername());
            userData.put("password", user.getPassword());
            // You can add more user attributes as needed

            return ResponseEntity.ok(userData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
