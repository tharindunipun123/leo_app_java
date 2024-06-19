package com.leo.leo_final;

import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            int result = statement.executeUpdate();
            if (result > 0) {
                return "User registered successfully";
            } else {
                return "User registration failed";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "User registration failed";
        }
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User loggedInUser = new User();
                loggedInUser.setId(resultSet.getInt("id"));
                loggedInUser.setUsername(resultSet.getString("username"));
                loggedInUser.setPassword(resultSet.getString("password"));
                return loggedInUser;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

