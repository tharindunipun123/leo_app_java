package com.leo.leo_final;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    @Autowired
    private DBConnection dbConnection;

    // Endpoint to save wallet data
    @PostMapping("/save")
    public Map<String, Object> saveWalletData(@RequestBody Map<String, Object> walletDetails) {
        int userId = (int) walletDetails.get("userId");
        BigDecimal amount = new BigDecimal(walletDetails.get("amount").toString());
        String firstName = (String) walletDetails.get("firstName");
        String lastName = (String) walletDetails.get("lastName");
        String phoneNumber = (String) walletDetails.get("phoneNumber");
        String email = (String) walletDetails.get("email");
        BigDecimal diamondAmount = new BigDecimal(walletDetails.get("diamondAmount").toString());

        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = dbConnection.getConnection();

            // Check if userId already exists in wallet table
            String checkQuery = "SELECT * FROM wallet WHERE userId = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setInt(1, userId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // User already exists, update wallet details
                BigDecimal currentAmount = resultSet.getBigDecimal("amount");
                BigDecimal currentDiamondAmount = resultSet.getBigDecimal("diamondAmount");

                BigDecimal newAmount = currentAmount.add(amount);
                BigDecimal newDiamondAmount = currentDiamondAmount.add(diamondAmount);

                String updateQuery = "UPDATE wallet SET amount = ?, firstName = ?, lastName = ?, " +
                        "phoneNumber = ?, email = ?, diamondAmount = ? WHERE userId = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setBigDecimal(1, newAmount);
                updateStatement.setString(2, firstName);
                updateStatement.setString(3, lastName);
                updateStatement.setString(4, phoneNumber);
                updateStatement.setString(5, email);
                updateStatement.setBigDecimal(6, newDiamondAmount);
                updateStatement.setInt(7, userId);

                int rowsAffected = updateStatement.executeUpdate();
                if (rowsAffected > 0) {
                    response.put("status", "Wallet details updated successfully");
                } else {
                    response.put("status", "Failed to update wallet details");
                }
            } else {
                // User does not exist, insert new wallet details
                String insertQuery = "INSERT INTO wallet (userId, amount, firstName, lastName, " +
                        "phoneNumber, email, diamondAmount) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, userId);
                insertStatement.setBigDecimal(2, amount);
                insertStatement.setString(3, firstName);
                insertStatement.setString(4, lastName);
                insertStatement.setString(5, phoneNumber);
                insertStatement.setString(6, email);
                insertStatement.setBigDecimal(7, diamondAmount);

                int rowsAffected = insertStatement.executeUpdate();
                if (rowsAffected > 0) {
                    response.put("status", "Wallet details saved successfully");
                } else {
                    response.put("status", "Failed to save wallet details");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Error occurred while saving wallet details");
        }

        return response;
    }

    // Endpoint to fetch diamond amount by userId
    @GetMapping("/diamondAmount/{userId}")
    public Map<String, Object> getDiamondAmount(@PathVariable int userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = dbConnection.getConnection();

            String selectQuery = "SELECT diamondAmount FROM wallet WHERE userId = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, userId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                BigDecimal diamondAmount = resultSet.getBigDecimal("diamondAmount");
                response.put("diamondAmount", diamondAmount);
                response.put("status", "Diamond amount retrieved successfully");
            } else {
                response.put("status", "User not found for the given userId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Error occurred while retrieving diamond amount");
        }

        return response;
    }

    // Endpoint to fetch transaction history by userId
    @GetMapping("/transactionHistory/{userId}")
    public List<Map<String, Object>> getTransactionHistory(@PathVariable int userId) {
        List<Map<String, Object>> transactionHistory = new ArrayList<>();

        try {
            Connection connection = dbConnection.getConnection();

            String selectQuery = "SELECT * FROM walletHistory WHERE userId = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, userId);
            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> transaction = new HashMap<>();
                transaction.put("transactionId", resultSet.getInt("transactionId"));
                transaction.put("userId", resultSet.getInt("userId"));
                transaction.put("amountChanged", resultSet.getBigDecimal("amountChanged"));
                transaction.put("diamondAmountChanged", resultSet.getBigDecimal("diamondAmountChanged"));
                transaction.put("transactionDate", resultSet.getTimestamp("transactionDate"));
                transactionHistory.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Log or handle exception
        }

        return transactionHistory;
    }
}
