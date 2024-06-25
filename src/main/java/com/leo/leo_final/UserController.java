package com.leo.leo_final;

import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;
@CrossOrigin
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

            // Check if the phone number already exists in the database
            String checkQuery = "SELECT id FROM users WHERE phoneNumber = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, mobileNumber);
            ResultSet checkResult = checkStatement.executeQuery();

            if (checkResult.next()) {
                // If the phone number exists, retrieve the userId
                int userId = checkResult.getInt("id");
                System.out.println(otp);
                System.out.println(userId);
                response.put("userId", userId);
                response.put("otp", otp);
            } else {
                // If the phone number does not exist, insert a new record
                String insertQuery = "INSERT INTO users (phoneNumber) VALUES (?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, mobileNumber);
                insertStatement.executeUpdate();

                ResultSet keys = insertStatement.getGeneratedKeys();
                if (keys.next()) {
                    int userId = keys.getInt(1);
                    System.out.println(otp);
                    System.out.println(userId);
                    response.put("userId", userId);
                    response.put("otp", otp);
                } else {
                    throw new SQLException("User ID generation failed.");
                }
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
        int storedOtp = 654321;

        return true;
    }
    @PostMapping("/updateProfile")
    public Map<String, String> updateProfile(@RequestBody Map<String, String> details) {
        int userId = Integer.parseInt(details.get("userId"));
        String name = details.get("name");
        String about = details.get("about");

        Map<String, String> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "UPDATE users SET name = ?, about = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, about);
            statement.setInt(3, userId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                response.put("status", "Profile updated successfully");
            } else {
                response.put("status", "Profile update failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Profile update failed");
        }
        return response;
    }
    @PostMapping("/checkContacts")
    public List<Map<String, Object>> checkContacts(@RequestBody List<Map<String, String>> contacts) {
        List<Map<String, Object>> response = new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();

            for (Map<String, String> contact : contacts) {
                String mobileNumber = contact.get("mobileNumber");
                String contactName = contact.get("contactName");

                String query = "SELECT id, profilePicUrl FROM users WHERE phoneNumber = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, mobileNumber);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String profilePicUrl = resultSet.getString("profilePicUrl");

                    Map<String, Object> userResponse = new HashMap<>();
                    userResponse.put("userId", userId);
                    userResponse.put("contactName", contactName);
                    userResponse.put("profilePicUrl", profilePicUrl);

                    response.add(userResponse);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }


}
