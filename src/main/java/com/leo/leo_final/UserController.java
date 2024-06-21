package com.leo.leo_final;

import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @PostMapping("/getOtp")
    public Map<String, Object> getOtp(@RequestBody Map<String, String> details) {
        String mobileNumber = details.get("mobileNumber");
        System.out.println(mobileNumber);
        int otp = new Random().nextInt(900000) + 100000; // Generate a 6-digit OTP

        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "INSERT INTO users (phoneNumber) VALUES (?) ON DUPLICATE KEY UPDATE phoneNumber = VALUES(phoneNumber)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, mobileNumber);
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                int userId = keys.getInt(1);
                // Store OTP in a secure way (like in-memory cache) associated with userId or mobile number.
                // For simplicity, we assume OTP is stored successfully and return it directly here.
                response.put("userId", userId);
                response.put("otp", otp);
            } else {
                throw new SQLException("User ID generation failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "User registration failed");
        }
        return response;
    }

    @PostMapping("/verifyOtp")
    public boolean verifyOtp(@RequestBody Map<String, String> details) {
        String mobileNumber = details.get("mobileNumber");
        int enteredOtp = Integer.parseInt(details.get("otp"));

        // Check OTP stored in cache or database (simplified here)
        int storedOtp = 654321; // Replace with actual retrieval logic

        return true;
    }
}
