package com.leo.leo_final;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;


@CrossOrigin
@RestController
@RequestMapping("/api/v1/voiceroom")
public class voiceroomController {

//    @Autowired
//    private DBConnection dbConnection;
//
//    // 1. Save the data and return all details for the primary key
//    @PostMapping("/save")
//    public Map<String, Object> saveVoiceroom(@RequestBody Map<String, Object> voiceroomDetails) {
//        int groupId = (int) voiceroomDetails.get("groupId");
//        int voiceRoomId = (int) voiceroomDetails.get("voiceRoomId");
//        boolean isCreated = (boolean) voiceroomDetails.get("isCreated");
//
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            Connection connection = dbConnection.getConnection();
//
//            // Check if the groupId already exists
//            String checkQuery = "SELECT * FROM voiceRoom WHERE groupId = ?";
//            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
//            checkStatement.setInt(1, groupId);
//            ResultSet resultSet = checkStatement.executeQuery();
//
//            if (resultSet.next()) {
//                response.put("status", "Group ID already exists");
//                return response;
//            }
//
//            // Insert data into voiceRoom table
//            String insertQuery = "INSERT INTO voiceRoom (groupId, voiceRoomId, isCreated) VALUES (?, ?, ?)";
//            PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
//            insertStatement.setInt(1, groupId);
//            insertStatement.setInt(2, voiceRoomId);
//            insertStatement.setBoolean(3, isCreated);
//
//            int rowsAffected = insertStatement.executeUpdate();
//            if (rowsAffected > 0) {
//                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
//                if (generatedKeys.next()) {
//                    int id = generatedKeys.getInt(1);
//
//                    String selectQuery = "SELECT * FROM voiceRoom WHERE id = ?";
//                    PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
//                    selectStatement.setInt(1, id);
//                    resultSet = selectStatement.executeQuery();
//
//                    if (resultSet.next()) {
//                        response.put("id", resultSet.getInt("id"));
//                        response.put("groupId", resultSet.getInt("groupId"));
//                        response.put("voiceRoomId", resultSet.getInt("voiceRoomId"));
//                        response.put("isCreated", resultSet.getBoolean("isCreated"));
//                        response.put("createdAt", resultSet.getTimestamp("createdAt"));
//                        response.put("updatedAt", resultSet.getTimestamp("updatedAt"));
//                        response.put("status", "Voice room saved successfully");
//                    } else {
//                        response.put("status", "Failed to retrieve saved voice room details");
//                    }
//                } else {
//                    response.put("status", "Failed to get generated primary key");
//                }
//            } else {
//                response.put("status", "Failed to save voice room");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.put("status", "Error occurred while saving voice room");
//        }
//
//        return response;
//    }

    @Autowired
    private DBConnection dbConnection;

    // 1. Save the data and return all details for the primary key
    @PostMapping("/save")
    public Map<String, Object> saveVoiceroom(@RequestBody Map<String, Object> voiceroomDetails) {
        int groupId = (int) voiceroomDetails.get("groupId");
        String voiceRoomId = (String) voiceroomDetails.get("voiceRoomId");
        boolean isCreated = (boolean) voiceroomDetails.get("isCreated");

        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = dbConnection.getConnection();

            // Check if the groupId already exists
            String checkQuery = "SELECT * FROM voiceRoom WHERE groupId = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setInt(1, groupId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                response.put("status", "Group ID already exists");
                return response;
            }

            // Insert data into voiceRoom table
            String insertQuery = "INSERT INTO voiceRoom (groupId, voiceRoomId, isCreated) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setInt(1, groupId);
            insertStatement.setString(2, voiceRoomId);
            insertStatement.setBoolean(3, isCreated);

            int rowsAffected = insertStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);

                    String selectQuery = "SELECT * FROM voiceRoom WHERE id = ?";
                    PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                    selectStatement.setInt(1, id);
                    resultSet = selectStatement.executeQuery();

                    if (resultSet.next()) {
                        response.put("id", resultSet.getInt("id"));
                        response.put("groupId", resultSet.getInt("groupId"));
                        response.put("voiceRoomId", resultSet.getString("voiceRoomId"));
                        response.put("isCreated", resultSet.getBoolean("isCreated"));
                        response.put("createdAt", resultSet.getTimestamp("createdAt"));
                        response.put("updatedAt", resultSet.getTimestamp("updatedAt"));
                        response.put("status", "Voice room saved successfully");
                    } else {
                        response.put("status", "Failed to retrieve saved voice room details");
                    }
                } else {
                    response.put("status", "Failed to get generated primary key");
                }
            } else {
                response.put("status", "Failed to save voice room");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Error occurred while saving voice room");
        }

        return response;
    }

//    // 2. Get all details using groupId
//    @GetMapping("/roomdetails/{groupId}")
//    public Map<String, Object> getVoiceroomDetails(@PathVariable int groupId) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            Connection connection = dbConnection.getConnection();
//
//            String selectQuery = "SELECT * FROM voiceRoom WHERE groupId = ?";
//            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
//            selectStatement.setInt(1, groupId);
//            ResultSet resultSet = selectStatement.executeQuery();
//
//            if (resultSet.next()) {
//                response.put("id", resultSet.getInt("id"));
//                response.put("groupId", resultSet.getInt("groupId"));
//                response.put("voiceRoomId", resultSet.getInt("voiceRoomId"));
//                response.put("isCreated", resultSet.getBoolean("isCreated"));
//                response.put("createdAt", resultSet.getTimestamp("createdAt"));
//                response.put("updatedAt", resultSet.getTimestamp("updatedAt"));
//                response.put("status", "Voice room details retrieved successfully");
//            } else {
//                response.put("status", "Voice room not found for the given groupId");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.put("status", "Error occurred while retrieving voice room details");
//        }
//
//        return response;
//    }


    // 2. Get all details using groupId
    @GetMapping("/roomdetails/{groupId}")
    public Map<String, Object> getVoiceroomDetails(@PathVariable int groupId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = dbConnection.getConnection();

            String selectQuery = "SELECT * FROM voiceRoom WHERE groupId = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, groupId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                response.put("id", resultSet.getInt("id"));
                response.put("groupId", resultSet.getInt("groupId"));
                response.put("voiceRoomId", resultSet.getString("voiceRoomId"));
                response.put("isCreated", resultSet.getBoolean("isCreated"));
                response.put("createdAt", resultSet.getTimestamp("createdAt"));
                response.put("updatedAt", resultSet.getTimestamp("updatedAt"));
                response.put("status", "Voice room details retrieved successfully");
            } else {
                response.put("status", "Voice room not found for the given groupId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Error occurred while retrieving voice room details");
        }

        return response;
    }

//    // 3. Update voice room and isCreated state using groupId
//    @PutMapping("/update/{groupId}")
//    public Map<String, Object> updateVoiceroom(@PathVariable int groupId, @RequestBody Map<String, Object> updateDetails) {
//        int voiceRoomId = (int) updateDetails.get("voiceRoomId");
//        boolean isCreated = (boolean) updateDetails.get("isCreated");
//
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            Connection connection = dbConnection.getConnection();
//
//            String updateQuery = "UPDATE voiceRoom SET voiceRoomId = ?, isCreated = ?, updatedAt = CURRENT_TIMESTAMP WHERE groupId = ?";
//            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
//            updateStatement.setInt(1, voiceRoomId);
//            updateStatement.setBoolean(2, isCreated);
//            updateStatement.setInt(3, groupId);
//
//            int rowsAffected = updateStatement.executeUpdate();
//            if (rowsAffected > 0) {
//                response.put("status", "Voice room updated successfully");
//            } else {
//                response.put("status", "Failed to update voice room");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.put("status", "Error occurred while updating voice room");
//        }
//
//        return response;
//    }

    // 3. Update voice room and isCreated state using groupId
    @PutMapping("/update/{groupId}")
    public Map<String, Object> updateVoiceroom(@PathVariable int groupId, @RequestBody Map<String, Object> updateDetails) {
        String voiceRoomId = (String) updateDetails.get("voiceRoomId");
        boolean isCreated = (boolean) updateDetails.get("isCreated");

        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = dbConnection.getConnection();

            String updateQuery = "UPDATE voiceRoom SET voiceRoomId = ?, isCreated = ?, updatedAt = CURRENT_TIMESTAMP WHERE groupId = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, voiceRoomId);
            updateStatement.setBoolean(2, isCreated);
            updateStatement.setInt(3, groupId);

            int rowsAffected = updateStatement.executeUpdate();
            if (rowsAffected > 0) {
                response.put("status", "Voice room updated successfully");
            } else {
                response.put("status", "Failed to update voice room");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Error occurred while updating voice room");
        }

        return response;
    }

//    // 4. Get all group details using groupId
//    @GetMapping("/group-details/{groupId}")
//    public Map<String, Object> getGroupDetails(@PathVariable int groupId) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            Connection connection = dbConnection.getConnection();
//
//            // Retrieve group information
//            String groupQuery = "SELECT * FROM `groups` WHERE groupId = ?";
//            PreparedStatement groupStatement = connection.prepareStatement(groupQuery);
//            groupStatement.setInt(1, groupId);
//            ResultSet groupResultSet = groupStatement.executeQuery();
//
//            if (groupResultSet.next()) {
//                Map<String, Object> groupInfo = new HashMap<>();
//                groupInfo.put("groupId", groupResultSet.getInt("groupId"));
//                groupInfo.put("groupName", groupResultSet.getString("groupName"));
//                groupInfo.put("groupAdminId", groupResultSet.getInt("groupAdminId"));
//
//                response.put("groupInfo", groupInfo);
//
//                // Retrieve group members
//                String membersQuery = "SELECT userId FROM voiceRoom WHERE groupId = ?";
//                PreparedStatement membersStatement = connection.prepareStatement(membersQuery);
//                membersStatement.setInt(1, groupId);
//                ResultSet membersResultSet = membersStatement.executeQuery();
//
//                List<Integer> groupMembers = new ArrayList<>();
//                while (membersResultSet.next()) {
//                    groupMembers.add(membersResultSet.getInt("userId"));
//                }
//
//                response.put("groupMembers", groupMembers);
//                response.put("status", "Group details retrieved successfully");
//            } else {
//                response.put("status", "Group not found for the given groupId");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.put("status", "Error occurred while retrieving group details");
//        }
//
//        return response;
//    }
// 4. Get all group details using groupId
@GetMapping("/group-details/{groupId}")
public Map<String, Object> getGroupDetails(@PathVariable int groupId) {
    Map<String, Object> response = new HashMap<>();

    try {
        Connection connection = dbConnection.getConnection();

        // Retrieve group information
        String groupQuery = "SELECT * FROM publicgroups WHERE groupId = 1";
        PreparedStatement groupStatement = connection.prepareStatement(groupQuery);
        groupStatement.setInt(1, groupId);
        ResultSet groupResultSet = groupStatement.executeQuery();

        if (groupResultSet.next()) {
            Map<String, Object> groupInfo = new HashMap<>();
            groupInfo.put("groupId", groupResultSet.getInt("groupId"));
            groupInfo.put("groupName", groupResultSet.getString("groupName"));
            groupInfo.put("groupAdminId", groupResultSet.getInt("groupAdminId"));

            response.put("groupInfo", groupInfo);

            // Retrieve group members
            String membersQuery = "SELECT userId FROM voiceRoom WHERE groupId = ?";
            PreparedStatement membersStatement = connection.prepareStatement(membersQuery);
            membersStatement.setInt(1, groupId);
            ResultSet membersResultSet = membersStatement.executeQuery();

            List<Integer> groupMembers = new ArrayList<>();
            while (membersResultSet.next()) {
                groupMembers.add(membersResultSet.getInt("userId"));
            }

            response.put("groupMembers", groupMembers);
            response.put("status", "Group details retrieved successfully");
        } else {
            response.put("status", "Group not found for the given groupId");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        response.put("status", "Error occurred while retrieving group details");
    }

    return response;
}

//    // 5. Check if voiceRoomId is already in use
//    @GetMapping("/check-voiceRoomId/{voiceRoomId}")
//    public Map<String, Object> checkVoiceRoomId(@PathVariable int voiceRoomId) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            Connection connection = dbConnection.getConnection();
//
//            String checkQuery = "SELECT * FROM voiceRoom WHERE voiceRoomId = ?";
//            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
//            checkStatement.setInt(1, voiceRoomId);
//            ResultSet resultSet = checkStatement.executeQuery();
//
//            if (resultSet.next()) {
//                response.put("isInUse", true);
//                response.put("reason", "Voice room ID already in use");
//            } else {
//                response.put("isInUse", false);
//                response.put("reason", "You Can Use this Voice Room ID");
//            }
//            response.put("status", "Voice room ID check performed successfully");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.put("status", "Error occurred while checking voice room ID");
//        }
//
//        return response;
//    }
//
//    @GetMapping("/count-members/{groupId}")
//    public Map<String, Object> countGroupMembers(@PathVariable int groupId) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            Connection connection = dbConnection.getConnection();
//
//            // Use the group_members table to count members in the group
//            String countQuery = "SELECT COUNT(userId) AS memberCount FROM group_members WHERE groupId = ?";
//            PreparedStatement countStatement = connection.prepareStatement(countQuery);
//            countStatement.setInt(1, groupId);
//            ResultSet resultSet = countStatement.executeQuery();
//
//            if (resultSet.next()) {
//                int memberCount = resultSet.getInt("memberCount");
//                response.put("groupId", groupId);
//                response.put("memberCount", memberCount);
//                response.put("status", "Group member count retrieved successfully");
//            } else {
//                response.put("status", "Group not found for the given groupId");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.put("status", "Error occurred while retrieving group member count");
//        }
//
//        return response;
//    }
//
//
//    @GetMapping("/group-profile-pictures/{groupId}")
//    public Map<String, Object> getGroupProfilePictures(@PathVariable int groupId) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            Connection connection = dbConnection.getConnection();
//
//            String query = "SELECT u.id AS userId, u.profilePicUrl FROM group_members gm " +
//                    "JOIN users u ON gm.userId = u.id WHERE gm.groupId = ?";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, groupId);
//            ResultSet resultSet = statement.executeQuery();
//
//            List<Map<String, Object>> memberDetails = new ArrayList<>();
//            while (resultSet.next()) {
//                int userId = resultSet.getInt("userId");
//                String profilePicUrl = resultSet.getString("profilePicUrl");
//
//                Map<String, Object> memberDetail = new HashMap<>();
//                memberDetail.put("userId", userId);
//                memberDetail.put("profilePicUrl", profilePicUrl);
//
//                // Encode the image as Base64 if it exists
//                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
//                    String MAIN_FOLDER_PATH = File.separator + "profileImage" + File.separator + profilePicUrl;
//                    Path path = Paths.get(MAIN_FOLDER_PATH);
//                    if (Files.exists(path)) {
//                        byte[] imageBytes = Files.readAllBytes(path);
//                        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
//                        memberDetail.put("profilePicBase64", encodedImage);
//                    } else {
//                        memberDetail.put("profilePicBase64", "File not found");
//                    }
//                } else {
//                    memberDetail.put("profilePicBase64", "No profile picture");
//                }
//
//                memberDetails.add(memberDetail);
//            }
//
//            response.put("members", memberDetails);
//            response.put("status", "Group profile pictures retrieved successfully");
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//            response.put("status", "Error occurred while retrieving group profile pictures");
//        }
//
//        return response;
//    }

    // 5. Check if voiceRoomId is already in use
    @GetMapping("/check-voiceRoomId/{voiceRoomId}")
    public Map<String, Object> checkVoiceRoomId(@PathVariable String voiceRoomId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = dbConnection.getConnection();

            String checkQuery = "SELECT * FROM voiceRoom WHERE voiceRoomId = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, voiceRoomId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                response.put("isInUse", true);
                response.put("reason", "Voice room ID already in use");
            } else {
                response.put("isInUse", false);
                response.put("reason", "You Can Use this Voice Room ID");
            }
            response.put("status", "Voice room ID check performed successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Error occurred while checking voice room ID");
        }

        return response;
    }

    @GetMapping("/count-members/{groupId}")
    public Map<String, Object> countGroupMembers(@PathVariable int groupId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = dbConnection.getConnection();

            // Use the group_members table to count members in the group
            String countQuery = "SELECT COUNT(userId) AS memberCount FROM public_group_members WHERE groupId = ?";
            PreparedStatement countStatement = connection.prepareStatement(countQuery);
            countStatement.setInt(1, groupId);
            ResultSet resultSet = countStatement.executeQuery();

            if (resultSet.next()) {
                int memberCount = resultSet.getInt("memberCount");
                response.put("groupId", groupId);
                response.put("memberCount", memberCount);
                response.put("status", "Group member count retrieved successfully");
            } else {
                response.put("status", "Group not found for the given groupId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Error occurred while retrieving group member count");
        }

        return response;
    }


    @GetMapping("/group-profile-pictures/{groupId}")
    public Map<String, Object> getGroupProfilePictures(@PathVariable int groupId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = dbConnection.getConnection();

            String query = "SELECT u.id AS userId, u.profilePicUrl FROM public_group_members gm " +
                    "JOIN users u ON gm.userId = u.id WHERE gm.groupId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();

            List<Map<String, Object>> memberDetails = new ArrayList<>();
            while (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                String profilePicUrl = resultSet.getString("profilePicUrl");

                Map<String, Object> memberDetail = new HashMap<>();
                memberDetail.put("userId", userId);
                memberDetail.put("profilePicUrl", profilePicUrl);

                // Encode the image as Base64 if it exists
                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                    String MAIN_FOLDER_PATH = File.separator + "profileImage" + File.separator + profilePicUrl;
                    Path path = Paths.get(MAIN_FOLDER_PATH);
                    if (Files.exists(path)) {
                        byte[] imageBytes = Files.readAllBytes(path);
                        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
                        memberDetail.put("profilePicBase64", encodedImage);
                    } else {
                        memberDetail.put("profilePicBase64", "File not found");
                    }
                } else {
                    memberDetail.put("profilePicBase64", "No profile picture");
                }

                memberDetails.add(memberDetail);
            }

            response.put("members", memberDetails);
            response.put("status", "Group profile pictures retrieved successfully");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            response.put("status", "Error occurred while retrieving group profile pictures");
        }

        return response;
    }

}
