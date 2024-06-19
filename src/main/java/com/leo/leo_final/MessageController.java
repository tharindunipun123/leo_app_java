package com.leo.leo_final;

import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {

    @PostMapping("/message")
    public String sendMessage(@RequestBody Message message) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "INSERT INTO messages (sender_id, receiver_id, content) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, message.getSenderId());
            statement.setInt(2, message.getReceiverId());
            statement.setString(3, message.getContent());
            int result = statement.executeUpdate();
            if (result > 0) {
                return "Message sent successfully";
            } else {
                return "Message sending failed";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Message sending failed";
        }
    }

    @GetMapping("/messages")
    public List<Message> getMessages(@RequestParam int senderId, @RequestParam int receiverId) {
        List<Message> messages = new ArrayList<>();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT * FROM messages WHERE sender_id = ? AND receiver_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, senderId);
            statement.setInt(2, receiverId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Message message = new Message();
                message.setId(resultSet.getInt("id"));
                message.setSenderId(resultSet.getInt("sender_id"));
                message.setReceiverId(resultSet.getInt("receiver_id"));
                message.setContent(resultSet.getString("content"));
                message.setTimestamp(resultSet.getTimestamp("timestamp"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}

