package com.leo.leo_final;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class statusController {
    @Value("${uploadDir}")
    private String uploadDir;

    @Value("${server.address}")
    private String serverAddress;

    @PostMapping("/addStatus")
    public Map<String, String> addStatus(@RequestParam("userId") int userId,
                                         @RequestParam(value = "statusText", required = false) String statusText,
                                         @RequestParam(value = "file", required = false) MultipartFile file) {

        Map<String, String> response = new HashMap<>();
        String fileName = null;

        try {
            if (file != null) {
                fileName = saveFile(file.getOriginalFilename(), file);
            }

            saveStatusToDatabase(userId, statusText, fileName);

            response.put("status", "Status added successfully");
            if (fileName != null) {
                response.put("fileUrl", "http://" + serverAddress + ":8080/uploads/statusImages/" + fileName);
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            response.put("status", "Status addition failed");
        }

        return response;
    }

    private String saveFile(String fileName, MultipartFile file) throws IOException {
        String uploadPath = uploadDir + File.separator + "statusImages";
        Path path = Paths.get(uploadPath);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        Path filePath = path.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }

    private void saveStatusToDatabase(int userId, String statusText, String fileUrl) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "INSERT INTO statuses (userId, statusText, statusImageUrl) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setString(2, statusText);
        statement.setString(3, fileUrl);
        statement.executeUpdate();
    }

    @PostMapping("/getStatuses")
    public List<Map<String, Object>> getStatuses(@RequestParam("userId") int userId) {
        List<Map<String, Object>> response = new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT s.statusId, s.statusText, s.statusImageUrl, s.createdAt, u.name, u.profilePicUrl " +
                    "FROM statuses s " +
                    "JOIN users u ON s.userId = u.id " +
                    "WHERE s.userId = ? " +
                    "OR s.userId IN (SELECT contactUserId FROM contacts WHERE userId = ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> status = new HashMap<>();
                status.put("statusId", resultSet.getInt("statusId"));
                status.put("statusText", resultSet.getString("statusText"));
                status.put("statusImageUrl", "http://" + serverAddress + ":8080/uploads/statusImages/" + Paths.get(resultSet.getString("statusImageUrl")).getFileName().toString());
                status.put("createdAt", resultSet.getTimestamp("createdAt"));
                status.put("userName", resultSet.getString("name"));
                String profilePicUrl = resultSet.getString("profilePicUrl");
                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                    profilePicUrl = "http://" + serverAddress + ":8080/uploads/profileImage/" + Paths.get(profilePicUrl).getFileName().toString();
                }
                status.put("profilePicUrl", profilePicUrl);
                response.add(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping("/user/{userId}/statuses")
    public List<Map<String, Object>> getUserStatuses(@PathVariable int userId) {
        List<Map<String, Object>> response = new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT s.statusId, s.statusText, s.statusImageUrl, s.createdAt, u.name, u.profilePicUrl " +
                    "FROM statuses s " +
                    "JOIN users u ON s.userId = u.id " +
                    "WHERE s.userId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> status = new HashMap<>();
                status.put("statusId", resultSet.getInt("statusId"));
                status.put("statusText", resultSet.getString("statusText"));
                status.put("statusImageUrl", "http://" + serverAddress + ":8080/uploads/statusImages/" + Paths.get(resultSet.getString("statusImageUrl")).getFileName().toString());
                status.put("createdAt", resultSet.getTimestamp("createdAt"));
                status.put("userName", resultSet.getString("name"));
                String profilePicUrl = resultSet.getString("profilePicUrl");
                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                    profilePicUrl = "http://" + serverAddress + ":8080/uploads/profileImage/" + Paths.get(profilePicUrl).getFileName().toString();
                }
                status.put("profilePicUrl", profilePicUrl);
                response.add(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }





//    // Fetch statuses available for user by userID
//    @GetMapping("/user/{userId}/statuses")
//    public List<Map<String, Object>> getUserStatuses(@PathVariable int userId) {
//        List<Map<String, Object>> response = new ArrayList<>();
//
//        try {
//            Connection connection = DBConnection.getInstance().getConnection();
//            String query = "SELECT s.statusId, s.statusText, s.statusImageUrl, s.createdAt, u.name, u.profilePicUrl " +
//                    "FROM statuses s " +
//                    "JOIN users u ON s.userId = u.id " +
//                    "WHERE s.userId = ?";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, userId);
//
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                Map<String, Object> status = new HashMap<>();
//                status.put("statusId", resultSet.getInt("statusId"));
//                status.put("statusText", resultSet.getString("statusText"));
//                status.put("statusImageUrl", "http://" + serverAddress + ":8080/uploads/statusImages/" + Paths.get(resultSet.getString("statusImageUrl")).getFileName().toString());
//                status.put("createdAt", resultSet.getTimestamp("createdAt"));
//                status.put("userName", resultSet.getString("name"));
//                String profilePicUrl = resultSet.getString("profilePicUrl");
//                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
//                    profilePicUrl = "http://" + serverAddress + ":8080/uploads/profileImage/" + Paths.get(profilePicUrl).getFileName().toString();
//                }
//                status.put("profilePicUrl", profilePicUrl);
//                response.add(status);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return response;
//    }

//    @PostMapping("/getUserStatusesForFriends")
//    public List<Map<String, Object>> getUserStatusesForFriends(@RequestParam("friendId") int friendId) {
//        List<Map<String, Object>> response = new ArrayList<>();
//
//        try {
//            Connection connection = DBConnection.getInstance().getConnection();
//            String query = "SELECT s.statusId, s.statusText, s.statusImageUrl, s.createdAt, u.name, u.profilePicUrl " +
//                    "FROM statuses s " +
//                    "JOIN users u ON s.userId = u.id " +
//                    "WHERE s.userId IN (SELECT userId FROM contacts WHERE contactUserId = ?)";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, friendId);
//
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                Map<String, Object> status = new HashMap<>();
//                status.put("statusId", resultSet.getInt("statusId"));
//                status.put("statusText", resultSet.getString("statusText"));
//                status.put("statusImageUrl", "http://" + serverAddress + ":8080/uploads/statusImages/" + Paths.get(resultSet.getString("statusImageUrl")).getFileName().toString());
//                status.put("createdAt", resultSet.getTimestamp("createdAt"));
//                status.put("userName", resultSet.getString("name"));
//                String profilePicUrl = resultSet.getString("profilePicUrl");
//                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
//                    profilePicUrl = "http://" + serverAddress + ":8080/uploads/profileImage/" + Paths.get(profilePicUrl).getFileName().toString();
//                }
//                status.put("profilePicUrl", profilePicUrl);
//                response.add(status);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return response;
//    }
@PostMapping("/getUserStatusesForFriends")
public List<Map<String, Object>> getUserStatusesForFriends(@RequestParam("userId") int userId) {
    List<Map<String, Object>> response = new ArrayList<>();

    try {
        Connection connection = DBConnection.getInstance().getConnection();

        // Fetch contact IDs for the given user
        String contactQuery = "SELECT contactUserId FROM contacts WHERE userId = ?";
        PreparedStatement contactStatement = connection.prepareStatement(contactQuery);
        contactStatement.setInt(1, userId);
        ResultSet contactResultSet = contactStatement.executeQuery();

        List<Integer> contactIds = new ArrayList<>();
        while (contactResultSet.next()) {
            contactIds.add(contactResultSet.getInt("contactUserId"));
        }

        if (contactIds.isEmpty()) {
            return response; // Return empty response if no contacts found
        }

        // Fetch statuses for the contact IDs
        StringBuilder statusQueryBuilder = new StringBuilder(
                "SELECT s.statusId, s.statusText, s.statusImageUrl, s.createdAt, u.name, u.profilePicUrl " +
                        "FROM statuses s " +
                        "JOIN users u ON s.userId = u.id " +
                        "WHERE s.userId IN (");

        for (int i = 0; i < contactIds.size(); i++) {
            statusQueryBuilder.append("?");
            if (i < contactIds.size() - 1) {
                statusQueryBuilder.append(",");
            }
        }
        statusQueryBuilder.append(")");

        PreparedStatement statusStatement = connection.prepareStatement(statusQueryBuilder.toString());
        for (int i = 0; i < contactIds.size(); i++) {
            statusStatement.setInt(i + 1, contactIds.get(i));
        }

        ResultSet statusResultSet = statusStatement.executeQuery();
        while (statusResultSet.next()) {
            Map<String, Object> status = new HashMap<>();
            status.put("statusId", statusResultSet.getInt("statusId"));
            status.put("statusText", statusResultSet.getString("statusText"));
            status.put("statusImageUrl", "http://" + serverAddress + ":8080/uploads/statusImages/" + Paths.get(statusResultSet.getString("statusImageUrl")).getFileName().toString());
            status.put("createdAt", statusResultSet.getTimestamp("createdAt"));
            status.put("userName", statusResultSet.getString("name"));
            String profilePicUrl = statusResultSet.getString("profilePicUrl");
            if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                profilePicUrl = "http://" + serverAddress + ":8080/uploads/profileImage/" + Paths.get(profilePicUrl).getFileName().toString();
            }
            status.put("profilePicUrl", profilePicUrl);
            response.add(status);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return response;
}



    @GetMapping("/getStatusCount")
    public Map<String, Object> getStatusCount(@RequestParam("userId") int userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT COUNT(*) AS statusCount FROM statuses WHERE userId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                response.put("statusCount", resultSet.getInt("statusCount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Failed to retrieve status count");
        }

        return response;
    }

    @PostMapping("/addContacts")
    public Map<String, String> addContacts(@RequestBody Map<String, Object> payload) {
        Map<String, String> response = new HashMap<>();
        try {
            int userId = (int) payload.get("userId");
            List<Integer> contactUserIds = (List<Integer>) payload.get("contactUserIds");

            Connection connection = DBConnection.getInstance().getConnection();
            String query = "INSERT INTO contacts (userId, contactUserId) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            for (Integer contactUserId : contactUserIds) {
                statement.setInt(1, userId);
                statement.setInt(2, contactUserId);
                statement.addBatch();
            }

            statement.executeBatch();

            response.put("status", "Contacts added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Failed to add contacts");
        }

        return response;
    }

}




//package com.leo.leo_final;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1")
//@CrossOrigin
//public class statusController {
//
//    @Value("${uploadDir}")
//    private String uploadDir;
//
//    @PostMapping("/addStatus")
//    public Map<String, String> addStatus(@RequestParam("userId") int userId,
//                                         @RequestParam(value = "statusText", required = false) String statusText,
//                                         @RequestParam(value = "file", required = false) MultipartFile file) {
//
//        Map<String, String> response = new HashMap<>();
//        String fileName = null;
//
//        try {
//            if (file != null) {
//                fileName = saveFile(file.getOriginalFilename(), file);
//            }
//
//            saveStatusToDatabase(userId, statusText, fileName);
//
//            response.put("status", "Status added successfully");
//            if (fileName != null) {
//                response.put("fileUrl", fileName);
//            }
//
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//            response.put("status", "Status addition failed");
//        }
//
//        return response;
//    }
//
//    private String saveFile(String fileName, MultipartFile file) throws IOException {
//        String uploadPath = uploadDir + File.separator + "statusImages";
//        Path path = Paths.get(uploadPath);
//
//        if (!Files.exists(path)) {
//            Files.createDirectories(path);
//        }
//
//        Path filePath = path.resolve(fileName);
//        Files.copy(file.getInputStream(), filePath);
//
//        return filePath.toString();
//    }
//
//    private void saveStatusToDatabase(int userId, String statusText, String fileUrl) throws SQLException {
//        Connection connection = DBConnection.getInstance().getConnection();
//        String query = "INSERT INTO statuses (userId, statusText, statusImageUrl) VALUES (?, ?, ?)";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, userId);
//        statement.setString(2, statusText);
//        statement.setString(3, fileUrl);
//        statement.executeUpdate();
//    }
//
//    @PostMapping("/getStatuses")
//    public List<Map<String, Object>> getStatuses(@RequestParam("userId") int userId) {
//        List<Map<String, Object>> response = new ArrayList<>();
//
//        try {
//            Connection connection = DBConnection.getInstance().getConnection();
//            String query = "SELECT s.statusId, s.statusText, s.statusImageUrl, s.createdAt, u.name, u.profilePicUrl " +
//                    "FROM statuses s " +
//                    "JOIN users u ON s.userId = u.id " +
//                    "WHERE s.userId = ? " +
//                    "OR s.userId IN (SELECT contactUserId FROM contacts WHERE userId = ?)";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, userId);
//            statement.setInt(2, userId);
//
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                Map<String, Object> status = new HashMap<>();
//                status.put("statusId", resultSet.getInt("statusId"));
//                status.put("statusText", resultSet.getString("statusText"));
//                status.put("statusImageUrl", resultSet.getString("statusImageUrl"));
//                status.put("createdAt", resultSet.getTimestamp("createdAt"));
//                status.put("userName", resultSet.getString("name"));
//                status.put("profilePicUrl", resultSet.getString("profilePicUrl"));
//                response.add(status);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Response: " + response); // Debugging output
//
//        return response;
//    }
//    // fetch status availble for user by giving userID
//    @GetMapping("/user/{userId}/statuses")
//    public List<Map<String, Object>> getUserStatuses(@PathVariable int userId) {
//        List<Map<String, Object>> response = new ArrayList<>();
//
//        try {
//            Connection connection = DBConnection.getInstance().getConnection();
//            String query = "SELECT s.statusId, s.statusText, s.statusImageUrl, s.createdAt, u.name, u.profilePicUrl " +
//                    "FROM statuses s " +
//                    "JOIN users u ON s.userId = u.id " +
//                    "WHERE s.userId = ?";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, userId);
//
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                Map<String, Object> status = new HashMap<>();
//                status.put("statusId", resultSet.getInt("statusId"));
//                status.put("statusText", resultSet.getString("statusText"));
//                status.put("statusImageUrl", resultSet.getString("statusImageUrl"));
//                status.put("createdAt", resultSet.getTimestamp("createdAt"));
//                status.put("userName", resultSet.getString("name"));
//                status.put("profilePicUrl", resultSet.getString("profilePicUrl"));
//                response.add(status);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return response;
//    }
//
//    @PostMapping("/getUserStatusesForFriends")
//    public List<Map<String, Object>> getUserStatusesForFriends(@RequestParam("friendId") int friendId) {
//        List<Map<String, Object>> response = new ArrayList<>();
//
//        try {
//            Connection connection = DBConnection.getInstance().getConnection();
//            String query = "SELECT s.statusId, s.statusText, s.statusImageUrl, s.createdAt, u.name, u.profilePicUrl " +
//                    "FROM statuses s " +
//                    "JOIN users u ON s.userId = u.id " +
//                    "WHERE s.userId IN (SELECT userId FROM contacts WHERE contactUserId = ?)";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, friendId);
//
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                Map<String, Object> status = new HashMap<>();
//                status.put("statusId", resultSet.getInt("statusId"));
//                status.put("statusText", resultSet.getString("statusText"));
//                status.put("statusImageUrl", resultSet.getString("statusImageUrl"));
//                status.put("createdAt", resultSet.getTimestamp("createdAt"));
//                status.put("userName", resultSet.getString("name"));
//                status.put("profilePicUrl", resultSet.getString("profilePicUrl"));
//                response.add(status);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Response: " + response); // Debugging output
//
//        return response;
//    }
//
//
//    @GetMapping("/getStatusCount")
//    public Map<String, Object> getStatusCount(@RequestParam("userId") int userId) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            Connection connection = DBConnection.getInstance().getConnection();
//            String query = "SELECT COUNT(*) AS statusCount FROM statuses WHERE userId = ?";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, userId);
//
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                response.put("statusCount", resultSet.getInt("statusCount"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.put("error", "Failed to retrieve status count");
//        }
//
//        return response;
//    }
//
//    @PostMapping("/addContacts")
//    public Map<String, String> addContacts(@RequestBody Map<String, Object> payload) {
//        Map<String, String> response = new HashMap<>();
//        try {
//            int userId = (int) payload.get("userId");
//            List<Integer> contactUserIds = (List<Integer>) payload.get("contactUserIds");
//
//            Connection connection = DBConnection.getInstance().getConnection();
//            String query = "INSERT INTO contacts (userId, contactUserId) VALUES (?, ?)";
//            PreparedStatement statement = connection.prepareStatement(query);
//
//            for (Integer contactUserId : contactUserIds) {
//                statement.setInt(1, userId);
//                statement.setInt(2, contactUserId);
//                statement.addBatch();
//            }
//
//            statement.executeBatch();
//
//            response.put("status", "Contacts added successfully");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.put("status", "Failed to add contacts");
//        }
//
//        return response;
//    }
//}
