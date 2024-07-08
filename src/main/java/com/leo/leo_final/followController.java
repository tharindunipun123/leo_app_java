package com.leo.leo_final;

import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class followController {
    @PostMapping("/follow")
    public Map<String, Object> followUser(@RequestBody Map<String, Integer> details) {
        int followerId = details.get("followerId");
        int followingId = details.get("followingId");

        Map<String, Object> response = new HashMap<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            // Insert follow relationship
            String insertQuery = "INSERT INTO followers (followerId, followingId) VALUES (?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, followerId);
            insertStatement.setInt(2, followingId);
            insertStatement.executeUpdate();

            // Get follower count
            String countQuery = "SELECT COUNT(*) AS followerCount FROM followers WHERE followingId = ?";
            PreparedStatement countStatement = connection.prepareStatement(countQuery);
            countStatement.setInt(1, followingId);
            ResultSet countResult = countStatement.executeQuery();

            if (countResult.next()) {
                int followerCount = countResult.getInt("followerCount");
                response.put("followerCount", followerCount);
            }

            response.put("status", "Followed successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Failed to follow user");
        }

        return response;
    }

    @PostMapping("/getFollowers")
    public List<Map<String, Object>> getFollowers(@RequestBody Map<String, Integer> details) {
        int userId = details.get("userId");
        List<Map<String, Object>> response = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "SELECT u.id, u.name, u.phoneNumber FROM followers f JOIN users u ON f.followerId = u.id WHERE f.followingId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> follower = new HashMap<>();
                follower.put("id", resultSet.getInt("id"));
                follower.put("name", resultSet.getString("name"));
                follower.put("phoneNumber", resultSet.getString("phoneNumber"));
                response.add(follower);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }

    @PostMapping("/getFollowing")
    public List<Map<String, Object>> getFollowing(@RequestBody Map<String, Integer> details) {
        int userId = details.get("userId");
        List<Map<String, Object>> response = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "SELECT u.id, u.name, u.phoneNumber FROM followers f JOIN users u ON f.followingId = u.id WHERE f.followerId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> following = new HashMap<>();
                following.put("id", resultSet.getInt("id"));
                following.put("name", resultSet.getString("name"));
                following.put("phoneNumber", resultSet.getString("phoneNumber"));
                response.add(following);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }

    @PostMapping("/isFollowing")
    public Map<String, Object> isFollowing(@RequestBody Map<String, Integer> details) {
        int followerId = details.get("followerId");
        int followingId = details.get("followingId");

        Map<String, Object> response = new HashMap<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "SELECT COUNT(*) AS count FROM followers WHERE followerId = ? AND followingId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, followerId);
            statement.setInt(2, followingId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                boolean isFollowing = resultSet.getInt("count") > 0;
                response.put("isFollowing", isFollowing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Failed to check following status");
        }

        return response;
    }

    @GetMapping("/getFollowersFollowingCount")
    public Map<String, Object> getFollowersFollowingCount(@RequestParam("userId") int userId) {
        Map<String, Object> response = new HashMap<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            // Get follower count
            String followerCountQuery = "SELECT COUNT(*) AS followerCount FROM followers WHERE followingId = ?";
            PreparedStatement followerCountStatement = connection.prepareStatement(followerCountQuery);
            followerCountStatement.setInt(1, userId);
            ResultSet followerCountResult = followerCountStatement.executeQuery();

            if (followerCountResult.next()) {
                response.put("followerCount", followerCountResult.getInt("followerCount"));
            }

            // Get following count
            String followingCountQuery = "SELECT COUNT(*) AS followingCount FROM followers WHERE followerId = ?";
            PreparedStatement followingCountStatement = connection.prepareStatement(followingCountQuery);
            followingCountStatement.setInt(1, userId);
            ResultSet followingCountResult = followingCountStatement.executeQuery();

            if (followingCountResult.next()) {
                response.put("followingCount", followingCountResult.getInt("followingCount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Failed to retrieve follower/following count");
        }

        return response;
    }
}
