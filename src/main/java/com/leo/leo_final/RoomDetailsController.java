package com.leo.leo_final;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/roomDetails")
@CrossOrigin
public class RoomDetailsController {

    @Value("${server.address}")
    private String serverAddress;

    // 1. Add details when user joins a room
    @PostMapping("/joinRoom")
    public Map<String, String> joinRoom(@RequestBody Map<String, Object> joinDetails) {
        Map<String, String> response = new HashMap<>();
        int userId = (int) joinDetails.get("userId");
        int roomId = (int) joinDetails.get("roomId");

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "INSERT INTO roomDetails (userId, roomId, joined, isDeleted) VALUES (?, ?, TRUE, FALSE)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, roomId);
            statement.executeUpdate();

            response.put("status", "User joined room successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Failed to join room");
        }

        return response;
    }

    // 2. Update when user leaves a room
    @PutMapping("/leaveRoom")
    public Map<String, String> leaveRoom(@RequestParam int userId, @RequestParam int roomId) {
        Map<String, String> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "UPDATE roomDetails SET leaved = TRUE, updatedAt = CURRENT_TIMESTAMP WHERE userId = ? AND roomId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, roomId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                response.put("status", "User left room successfully");
            } else {
                response.put("status", "Failed to update user leaving room");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Failed to update user leaving room");
        }

        return response;
    }

    // 3. Fetch all user details and room details by entering roomId (not deleted)
    @GetMapping("/room/{roomId}")
    public List<Map<String, Object>> getUserDetailsByRoomId(@PathVariable int roomId) {
        List<Map<String, Object>> response = new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT r.*, u.* FROM roomDetails r " +
                    "JOIN users u ON r.userId = u.id " +
                    "WHERE r.roomId = ? AND r.isDeleted = FALSE";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> details = new HashMap<>();
                details.put("detailId", resultSet.getInt("detailId"));
                details.put("userId", resultSet.getInt("userId"));
                details.put("roomId", resultSet.getInt("roomId"));
                details.put("joinedDateTime", resultSet.getTimestamp("joinedDateTime"));
                details.put("isFollowing", resultSet.getBoolean("isFollowing"));
                details.put("joined", resultSet.getBoolean("joined"));
                details.put("leaved", resultSet.getBoolean("leaved"));
                details.put("isDeleted", resultSet.getBoolean("isDeleted"));
                details.put("userName", resultSet.getString("name"));
                details.put("phoneNumber", resultSet.getString("phoneNumber"));
                details.put("about", resultSet.getString("about"));
                details.put("profilePicUrl", "http://" + serverAddress + ":8080/uploads/profileImage/" + resultSet.getString("profilePicUrl"));
                details.put("gender", resultSet.getString("gender"));
                details.put("country", resultSet.getString("country"));
                details.put("birthday", resultSet.getDate("birthday"));
                details.put("bio", resultSet.getString("bio"));
                details.put("motto", resultSet.getString("motto"));
                response.add(details);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }

    // 4. View all entries in roomDetails table
    @GetMapping("/all")
    public List<Map<String, Object>> getAllRoomDetails() {
        List<Map<String, Object>> response = new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT * FROM roomDetails";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> details = new HashMap<>();
                details.put("detailId", resultSet.getInt("detailId"));
                details.put("userId", resultSet.getInt("userId"));
                details.put("roomId", resultSet.getInt("roomId"));
                details.put("joinedDateTime", resultSet.getTimestamp("joinedDateTime"));
                details.put("isFollowing", resultSet.getBoolean("isFollowing"));
                details.put("joined", resultSet.getBoolean("joined"));
                details.put("leaved", resultSet.getBoolean("leaved"));
                details.put("isDeleted", resultSet.getBoolean("isDeleted"));
                response.add(details);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }

    // 5. Get the number of participants by entering roomId
    @GetMapping("/countParticipants/{roomId}")
    public Map<String, Object> countParticipants(@PathVariable int roomId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT COUNT(*) AS participantCount FROM roomDetails WHERE roomId = ? AND isDeleted = FALSE";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                response.put("participantCount", resultSet.getInt("participantCount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Failed to retrieve participant count");
        }

        return response;
    }

    // 6. Check if a room is deleted or not by roomId
    @GetMapping("/isDeleted/{roomId}")
    public Map<String, Object> isRoomDeleted(@PathVariable int roomId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT isDeleted FROM voiceRoomTeam WHERE roomId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                response.put("isDeleted", resultSet.getBoolean("isDeleted"));
            } else {
                response.put("error", "Room not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Failed to check room deletion status");
        }

        return response;
    }

    // 7. Update if user is joined and leaving the same roomId that is not deleted
    @PutMapping("/updateJoinLeave")
    public Map<String, String> updateJoinLeave(@RequestParam int userId, @RequestParam int roomId, @RequestParam boolean joined, @RequestParam boolean leaved) {
        Map<String, String> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "UPDATE roomDetails SET joined = ?, leaved = ?, updatedAt = CURRENT_TIMESTAMP WHERE userId = ? AND roomId = ? AND isDeleted = FALSE";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBoolean(1, joined);
            statement.setBoolean(2, leaved);
            statement.setInt(3, userId);
            statement.setInt(4, roomId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                response.put("status", "User join/leave status updated successfully");
            } else {
                response.put("status", "Failed to update user join/leave status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Failed to update user join/leave status");
        }

        return response;
    }

    // 8. Check if roomOwner is joined or not to room that is not deleted
    @GetMapping("/isOwnerJoined/{roomId}")
    public Map<String, Object> isOwnerJoined(@PathVariable int roomId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT v.roomOwnerId, r.joined FROM voiceRoomTeam v " +
                    "JOIN roomDetails r ON v.roomId = r.roomId " +
                    "WHERE v.roomId = ? AND v.isDeleted = FALSE AND r.userId = v.roomOwnerId";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                response.put("roomOwnerId", resultSet.getInt("roomOwnerId"));
                response.put("joined", resultSet.getBoolean("joined"));
            } else {
                response.put("error", "Room owner not found or not joined");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Failed to check if room owner joined");
        }

        return response;
    }

    // 9. Update following status of a room by userId
    @PutMapping("/updateFollowing")
    public Map<String, String> updateFollowing(@RequestParam int userId, @RequestParam int roomId, @RequestParam boolean isFollowing) {
        Map<String, String> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "UPDATE roomDetails SET isFollowing = ?, updatedAt = CURRENT_TIMESTAMP WHERE userId = ? AND roomId = ? AND isDeleted = FALSE";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBoolean(1, isFollowing);
            statement.setInt(2, userId);
            statement.setInt(3, roomId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                response.put("status", "Following status updated successfully");
            } else {
                response.put("status", "Failed to update following status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Failed to update following status");
        }

        return response;
    }

    // 10. Get all following room details by entering userId
    @GetMapping("/followingRooms/{userId}")
    public List<Map<String, Object>> getFollowingRooms(@PathVariable int userId) {
        List<Map<String, Object>> response = new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT r.*, v.roomName, v.country, v.language, v.roomOwnerId, v.isDeleted, " +
                    "v.backgroundImageId, b.bgImageUrl FROM roomDetails r " +
                    "JOIN voiceRoomTeam v ON r.roomId = v.roomId " +
                    "JOIN backgroundImages b ON v.backgroundImageId = b.id " +
                    "WHERE r.userId = ? AND r.isFollowing = TRUE AND r.isDeleted = FALSE";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> details = new HashMap<>();
                details.put("detailId", resultSet.getInt("detailId"));
                details.put("userId", resultSet.getInt("userId"));
                details.put("roomId", resultSet.getInt("roomId"));
                details.put("joinedDateTime", resultSet.getTimestamp("joinedDateTime"));
                details.put("isFollowing", resultSet.getBoolean("isFollowing"));
                details.put("joined", resultSet.getBoolean("joined"));
                details.put("leaved", resultSet.getBoolean("leaved"));
                details.put("isDeleted", resultSet.getBoolean("isDeleted"));
                details.put("roomName", resultSet.getString("roomName"));
                details.put("country", resultSet.getString("country"));
                details.put("language", resultSet.getString("language"));
                details.put("roomOwnerId", resultSet.getInt("roomOwnerId"));
                details.put("backgroundImageUrl", "http://" + serverAddress + ":8080/uploads/backgroundImages/" + resultSet.getString("bgImageUrl"));
                response.add(details);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }
}

