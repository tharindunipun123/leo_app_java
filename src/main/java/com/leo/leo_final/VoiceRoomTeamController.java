package com.leo.leo_final;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/voiceRoomTeam")
@CrossOrigin
public class VoiceRoomTeamController {

    @Value("${server.address}")
    private String serverAddress;

    // 1. Save room details
    @PostMapping("/saveRoom")
    public Map<String, String> saveRoom(@RequestBody Map<String, Object> roomDetails) {
        Map<String, String> response = new HashMap<>();
        int roomId = (int) roomDetails.get("roomId");
        String roomName = (String) roomDetails.get("roomName");
        String country = (String) roomDetails.get("country");
        String language = (String) roomDetails.get("language");
        int roomOwnerId = (int) roomDetails.get("roomOwnerId");
        int backgroundImageId = (int) roomDetails.get("backgroundImageId");

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "INSERT INTO voiceRoomTeam (roomId, roomName, country, language, roomOwnerId, backgroundImageId) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomId);
            statement.setString(2, roomName);
            statement.setString(3, country);
            statement.setString(4, language);
            statement.setInt(5, roomOwnerId);
            statement.setInt(6, backgroundImageId);
            statement.executeUpdate();

            response.put("status", "Room saved successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Failed to save room");
        }

        return response;
    }

    // 2. Fetch all rooms that are not deleted
    @GetMapping("/rooms")
    public List<Map<String, Object>> getAllRooms() {
        List<Map<String, Object>> response = new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT v.roomId, v.roomName, v.country, v.language, v.roomOwnerId, v.isDeleted, b.bgImageUrl " +
                    "FROM voiceRoomTeam v " +
                    "JOIN backgroundImages b ON v.backgroundImageId = b.id " +
                    "WHERE v.isDeleted = FALSE";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> room = new HashMap<>();
                room.put("roomId", resultSet.getInt("roomId"));
                room.put("roomName", resultSet.getString("roomName"));
                room.put("country", resultSet.getString("country"));
                room.put("language", resultSet.getString("language"));
                room.put("roomOwnerId", resultSet.getInt("roomOwnerId"));
                room.put("isDeleted", resultSet.getBoolean("isDeleted"));
                room.put("backgroundImageUrl", resultSet.getString("bgImageUrl"));
                response.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }
    

    // 3. Update room deletion status by roomId
    @PutMapping("/updateDeletionStatus/{roomId}")
    public Map<String, String> updateDeletionStatus(@PathVariable int roomId, @RequestParam boolean isDeleted) {
        Map<String, String> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "UPDATE voiceRoomTeam SET isDeleted = ? WHERE roomId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBoolean(1, isDeleted);
            statement.setInt(2, roomId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                response.put("status", "Room deletion status updated successfully");
            } else {
                response.put("status", "Failed to update room deletion status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Failed to update room deletion status");
        }

        return response;
    }

    // 4. Fetch all room details by roomOwnerId which are not deleted
    @GetMapping("/roomsByOwner/{roomOwnerId}")
    public List<Map<String, Object>> getRoomsByOwner(@PathVariable int roomOwnerId) {
        List<Map<String, Object>> response = new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT v.roomId, v.roomName, v.country, v.language, v.roomOwnerId, v.isDeleted, b.bgImageUrl " +
                    "FROM voiceRoomTeam v " +
                    "JOIN backgroundImages b ON v.backgroundImageId = b.id " +
                    "WHERE v.roomOwnerId = ? AND v.isDeleted = FALSE";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomOwnerId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> room = new HashMap<>();
                room.put("roomId", resultSet.getInt("roomId"));
                room.put("roomName", resultSet.getString("roomName"));
                room.put("country", resultSet.getString("country"));
                room.put("language", resultSet.getString("language"));
                room.put("roomOwnerId", resultSet.getInt("roomOwnerId"));
                room.put("isDeleted", resultSet.getBoolean("isDeleted"));
                room.put("backgroundImageUrl", resultSet.getString("bgImageUrl"));
                response.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }

    // 5. Update background image ID via roomId
    @PutMapping("/updateBackgroundImage/{roomId}")
    public Map<String, String> updateBackgroundImage(@PathVariable int roomId, @RequestParam int backgroundImageId) {
        Map<String, String> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "UPDATE voiceRoomTeam SET backgroundImageId = ? WHERE roomId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, backgroundImageId);
            statement.setInt(2, roomId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                response.put("status", "Background image updated successfully");
            } else {
                response.put("status", "Failed to update background image");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Failed to update background image");
        }

        return response;
    }

    // 6. Fetch room details by roomId
    @GetMapping("/room/{roomId}")
    public Map<String, Object> getRoomById(@PathVariable int roomId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT v.roomId, v.roomName, v.country, v.language, v.roomOwnerId, v.isDeleted, b.bgImageUrl " +
                    "FROM voiceRoomTeam v " +
                    "JOIN backgroundImages b ON v.backgroundImageId = b.id " +
                    "WHERE v.roomId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                response.put("roomId", resultSet.getInt("roomId"));
                response.put("roomName", resultSet.getString("roomName"));
                response.put("country", resultSet.getString("country"));
                response.put("language", resultSet.getString("language"));
                response.put("roomOwnerId", resultSet.getInt("roomOwnerId"));
                response.put("isDeleted", resultSet.getBoolean("isDeleted"));
                response.put("backgroundImageUrl", resultSet.getString("bgImageUrl"));
                response.put("status", "Room details retrieved successfully");
            } else {
                response.put("status", "Room not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Failed to retrieve room details");
        }

        return response;
    }

    // 7. Fetch all background images
    @GetMapping("/backgroundImages")
    public List<Map<String, Object>> getAllBackgroundImages() {
        List<Map<String, Object>> response = new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT id, bgImageUrl FROM backgroundImages";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> image = new HashMap<>();
                image.put("id", resultSet.getInt("id"));
                image.put("bgImageUrl", resultSet.getString("bgImageUrl"));
                response.add(image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }


}
