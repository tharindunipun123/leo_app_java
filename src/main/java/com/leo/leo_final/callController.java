package com.leo.leo_final;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class callController {
    @PostMapping("/addOrUpdateCall")
    public Map<String, Object> addOrUpdateCall(@RequestParam("senderID") int senderID,
                                               @RequestParam("callerId") String callerId,
                                               @RequestParam("receiverId") int receiverId) {
        Map<String, Object> response = new HashMap<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String selectQuery = "SELECT id FROM call_details WHERE senderID = ? AND receiverId = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, senderID);
            selectStatement.setInt(2, receiverId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Update existing record
                int id = resultSet.getInt("id");
                String updateQuery = "UPDATE call_details SET callerId = ? WHERE id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, callerId);
                updateStatement.setInt(2, id);
                updateStatement.executeUpdate();

                response.put("status", "updated");
                response.put("id", id);
                response.put("senderID", senderID);
                response.put("callerId", callerId);
                response.put("receiverId", receiverId);
            } else {
                // Insert new record
                String insertQuery = "INSERT INTO call_details (senderID, callerId, receiverId) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setInt(1, senderID);
                insertStatement.setString(2, callerId);
                insertStatement.setInt(3, receiverId);
                insertStatement.executeUpdate();

                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    response.put("status", "inserted");
                    response.put("id", id);
                    response.put("senderID", senderID);
                    response.put("callerId", callerId);
                    response.put("receiverId", receiverId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Database operation failed");
        }

        return response;
    }

    @GetMapping("/checkCallerId")
    public Map<String, Object> checkCallerId(@RequestParam("callerId") String callerId) {
        Map<String, Object> response = new HashMap<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "SELECT * FROM call_details WHERE callerId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, callerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                response.put("exists", true);
                response.put("details", mapResultSet(resultSet));
            } else {
                response.put("exists", false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Database operation failed");
        }

        return response;
    }

    @GetMapping("/getCallerId")
    public Map<String, Object> getCallerId(@RequestParam("senderID") int senderID,
                                           @RequestParam("receiverId") int receiverId) {
        Map<String, Object> response = new HashMap<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "SELECT * FROM call_details WHERE senderID = ? AND receiverId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, senderID);
            statement.setInt(2, receiverId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                response.put("exists", true);
                response.put("details", mapResultSet(resultSet));
            } else {
                response.put("exists", false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Database operation failed");
        }

        return response;
    }

    private Map<String, Object> mapResultSet(ResultSet resultSet) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        result.put("id", resultSet.getInt("id"));
        result.put("senderID", resultSet.getInt("senderID"));
        result.put("callerId", resultSet.getString("callerId"));
        result.put("receiverId", resultSet.getInt("receiverId"));
        return result;
    }
}
