package com.leo.leo_final;

import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/gifting")
public class giftController {

    @PostMapping("/addGift")
    public Map<String, String> addGift(@RequestBody Map<String, Object> giftDetails) {
        String giftName = (String) giftDetails.get("giftName");
        BigDecimal giftAmount = new BigDecimal((String) giftDetails.get("giftAmount"));
        String giftUrl = (String) giftDetails.get("giftUrl");

        Map<String, String> response = new HashMap<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "INSERT INTO gifts (giftName, giftAmount, giftUrl) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, giftName);
            statement.setBigDecimal(2, giftAmount);
            statement.setString(3, giftUrl);
            statement.executeUpdate();

            response.put("status", "Gift added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Failed to add gift");
        }

        return response;
    }

    @GetMapping("/getAllGifts")
    public List<Map<String, Object>> getAllGifts() {
        List<Map<String, Object>> response = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "SELECT * FROM gifts";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> gift = new HashMap<>();
                gift.put("giftId", resultSet.getInt("giftId"));
                gift.put("giftName", resultSet.getString("giftName"));
                gift.put("giftAmount", resultSet.getBigDecimal("giftAmount"));
                gift.put("giftUrl", resultSet.getString("giftUrl"));
                response.add(gift);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }

    @PostMapping("/sendGift")
    public Map<String, Object> sendGift(@RequestBody Map<String, Object> giftDetails) {
        int senderId = (int) giftDetails.get("senderId");
        int receiverId = (int) giftDetails.get("receiverId");
        int giftId = (int) giftDetails.get("giftId");

        Map<String, Object> response = new HashMap<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {

            // Verify sender's balance
            String balanceQuery = "SELECT diamondAmount FROM wallet WHERE userId = ?";
            PreparedStatement balanceStatement = connection.prepareStatement(balanceQuery);
            balanceStatement.setInt(1, senderId);
            ResultSet balanceResult = balanceStatement.executeQuery();

            if (!balanceResult.next()) {
                response.put("status", "Sender not found");
                return response;
            }

            int diamondBalance = balanceResult.getInt("diamondAmount");

            // Get gift details
            String giftQuery = "SELECT giftAmount, giftName, giftUrl FROM gifts WHERE giftId = ?";
            PreparedStatement giftStatement = connection.prepareStatement(giftQuery);
            giftStatement.setInt(1, giftId);
            ResultSet giftResult = giftStatement.executeQuery();

            if (!giftResult.next()) {
                response.put("status", "Gift not found");
                return response;
            }

            BigDecimal giftAmount = giftResult.getBigDecimal("giftAmount");
            String giftName = giftResult.getString("giftName");
            String giftUrl = giftResult.getString("giftUrl");

            if (diamondBalance < giftAmount.intValue()) {
                response.put("status", "Insufficient balance");
                return response;
            }

            // Deduct diamonds from sender's balance
            String deductBalanceQuery = "UPDATE wallet SET diamondAmount = diamondAmount - ? WHERE userId = ?";
            PreparedStatement deductBalanceStatement = connection.prepareStatement(deductBalanceQuery);
            deductBalanceStatement.setBigDecimal(1, giftAmount);
            deductBalanceStatement.setInt(2, senderId);
            deductBalanceStatement.executeUpdate();

            // Insert gift transaction
            String transactionQuery = "INSERT INTO gift_transactions (giftId, senderId, receiverId, giftUrl, giftName) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement transactionStatement = connection.prepareStatement(transactionQuery);
            transactionStatement.setInt(1, giftId);
            transactionStatement.setInt(2, senderId);
            transactionStatement.setInt(3, receiverId);
            transactionStatement.setString(4, giftUrl);
            transactionStatement.setString(5, giftName);
            transactionStatement.executeUpdate();

            response.put("status", "Gift sent successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Transaction failed");
        }

        return response;
    }

    @GetMapping("/checkBalance/{userId}")
    public Map<String, Object> checkBalance(@PathVariable int userId) {
        Map<String, Object> response = new HashMap<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "SELECT diamondAmount FROM wallet WHERE userId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int diamondBalance = resultSet.getInt("diamondAmount");
                response.put("diamondAmount", diamondBalance);
                response.put("status", "Balance retrieved successfully");
            } else {
                response.put("status", "User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Failed to retrieve balance");
        }

        return response;
    }

    @GetMapping("/getTransactions/{userId}")
    public List<Map<String, Object>> getTransactions(@PathVariable int userId) {
        List<Map<String, Object>> response = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "SELECT * FROM gift_transactions WHERE senderId = ? OR receiverId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> transaction = new HashMap<>();
                transaction.put("transactionId", resultSet.getInt("transactionId"));
                transaction.put("giftId", resultSet.getInt("giftId"));
                transaction.put("senderId", resultSet.getInt("senderId"));
                transaction.put("receiverId", resultSet.getInt("receiverId"));
                transaction.put("giftUrl", resultSet.getString("giftUrl"));
                transaction.put("giftName", resultSet.getString("giftName"));
                transaction.put("createdAt", resultSet.getTimestamp("createdAt"));
                response.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping("/getReceivedGifts/{userId}")
    public List<Map<String, Object>> getReceivedGifts(@PathVariable int userId) {
        List<Map<String, Object>> response = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "SELECT * FROM gift_transactions WHERE receiverId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> transaction = new HashMap<>();
                transaction.put("transactionId", resultSet.getInt("transactionId"));
                transaction.put("giftId", resultSet.getInt("giftId"));
                transaction.put("senderId", resultSet.getInt("senderId"));
                transaction.put("receiverId", resultSet.getInt("receiverId"));
                transaction.put("giftUrl", resultSet.getString("giftUrl"));
                transaction.put("giftName", resultSet.getString("giftName"));
                transaction.put("createdAt", resultSet.getTimestamp("createdAt"));
                response.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }
}
