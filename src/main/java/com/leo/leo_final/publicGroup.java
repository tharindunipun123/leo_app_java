package com.leo.leo_final;

import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/publicgroups")
public class publicGroup {

    // Endpoint to create a new group
    @PostMapping("create")
    public Map<String, Object> createGroup(@RequestBody Map<String, Object> groupDetails) {
        String groupName = (String) groupDetails.get("groupName");
        int groupAdminId = (int) groupDetails.get("groupAdminId");

        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String insertQuery = "INSERT INTO `publicgroups` (groupName, groupAdminId) VALUES (?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, groupName);
            insertStatement.setInt(2, groupAdminId);

            int rowsAffected = insertStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet keys = insertStatement.getGeneratedKeys();
                if (keys.next()) {
                    int groupId = keys.getInt(1);
                    response.put("groupId", groupId);
                    response.put("groupName", groupName);
                    response.put("groupAdminId", groupAdminId);
                    response.put("status", "Group created successfully");
                } else {
                    throw new SQLException("Group ID generation failed.");
                }
            } else {
                response.put("status", "Group creation failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Group creation failed");
        }

        return response;
    }

    // Endpoint to update users in a group
    @PostMapping("/{groupId}/users")
    public Map<String, String> updateGroupUsers(@PathVariable int groupId, @RequestBody Map<String, Object> request) {
        Map<String, String> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();

            // Check if request contains "delete" key with userId or userIds
            if (request.containsKey("delete")) {
                Object deleteRequest = request.get("delete");
                if (deleteRequest instanceof Integer) {
                    int userIdToDelete = (int) deleteRequest;
                    String deleteQuery = "DELETE FROM public_group_members WHERE groupId = ? AND userId = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, groupId);
                    deleteStatement.setInt(2, userIdToDelete);
                    int deletedCount = deleteStatement.executeUpdate();

                    if (deletedCount > 0) {
                        response.put("status", "User deleted from group successfully");
                    } else {
                        response.put("status", "User delete operation failed");
                    }
                } else if (deleteRequest instanceof List<?>) {
                    List<Integer> userIdsToDelete = (List<Integer>) deleteRequest;
                    String deleteQuery = "DELETE FROM public_group_members WHERE groupId = ? AND userId IN (?)";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, groupId);
                    deleteStatement.setArray(2, connection.createArrayOf("INTEGER", userIdsToDelete.toArray()));
                    int[] deletedCounts = deleteStatement.executeBatch();

                    if (Arrays.stream(deletedCounts).sum() == userIdsToDelete.size()) {
                        response.put("status", "Users deleted from group successfully");
                    } else {
                        response.put("status", "Users delete operation failed");
                    }
                }
            }

            // Check if request contains "insert" key with userId or userIds
            if (request.containsKey("insert")) {
                Object insertRequest = request.get("insert");
                if (insertRequest instanceof Integer) {
                    int userIdToInsert = (int) insertRequest;
                    String insertQuery = "INSERT INTO public_group_members (groupId, userId) VALUES (?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setInt(1, groupId);
                    insertStatement.setInt(2, userIdToInsert);
                    int insertedCount = insertStatement.executeUpdate();

                    if (insertedCount > 0) {
                        response.put("status", "User added to group successfully");
                    } else {
                        response.put("status", "User add operation failed");
                    }
                } else if (insertRequest instanceof List<?>) {
                    List<Integer> userIdsToInsert = (List<Integer>) insertRequest;
                    String insertQuery = "INSERT INTO public_group_members (groupId, userId) VALUES (?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

                    for (int userId : userIdsToInsert) {
                        insertStatement.setInt(1, groupId);
                        insertStatement.setInt(2, userId);
                        insertStatement.addBatch();
                    }

                    int[] insertedCounts = insertStatement.executeBatch();
                    if (Arrays.stream(insertedCounts).sum() == userIdsToInsert.size()) {
                        response.put("status", "Users added to group successfully");
                    } else {
                        response.put("status", "Users add operation failed");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Operation failed");
        }

        return response;
    }

    // Endpoint to fetch group admin by groupId
    @GetMapping("/{groupId}/admin")
    public Map<String, Object> getGroupAdmin(@PathVariable int groupId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT u.id, u.name, u.phoneNumber FROM publicgroups g " +
                    "INNER JOIN users u ON g.groupAdminId = u.id " +
                    "WHERE g.groupId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int adminId = resultSet.getInt("id");
                String adminName = resultSet.getString("name");
                String adminPhoneNumber = resultSet.getString("phoneNumber");

                response.put("adminId", adminId);
                response.put("adminName", adminName);
                response.put("adminPhoneNumber", adminPhoneNumber);
                response.put("status", "Group admin retrieved successfully");
            } else {
                response.put("status", "Group admin not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Failed to fetch group admin");
        }

        return response;
    }
    @GetMapping("/getPublicGroupDetails")
    public Map<String, Object> getPublicGroupDetails(@RequestParam("groupName") String groupName) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> members = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            // Query to fetch group details
            String groupQuery = "SELECT * FROM publicgroups WHERE groupName = ?";
            PreparedStatement groupStatement = connection.prepareStatement(groupQuery);
            groupStatement.setString(1, groupName);
            ResultSet groupResultSet = groupStatement.executeQuery();

            if (groupResultSet.next()) {
                int groupId = groupResultSet.getInt("groupId");
                response.put("groupId", groupId);
                response.put("groupName", groupResultSet.getString("groupName"));
                response.put("groupAdminId", groupResultSet.getInt("groupAdminId"));
                response.put("createdAt", groupResultSet.getTimestamp("createdAt"));
                response.put("updatedAt", groupResultSet.getTimestamp("updatedAt"));

                // Query to fetch group members
                String membersQuery = "SELECT userId FROM public_group_members WHERE groupId = ?";
                PreparedStatement membersStatement = connection.prepareStatement(membersQuery);
                membersStatement.setInt(1, groupId);
                ResultSet membersResultSet = membersStatement.executeQuery();

                while (membersResultSet.next()) {
                    Map<String, Object> member = new HashMap<>();
                    member.put("userId", membersResultSet.getInt("userId"));
                    members.add(member);
                }

                response.put("members", members);
            } else {
                response.put("error", "Group not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Database operation failed");
        }

        return response;
    }

    // Endpoint to fetch all group information including group users by groupId
    @GetMapping("/{groupId}")
    public Map<String, Object> getGroupInfo(@PathVariable int groupId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT g.groupName, g.groupAdminId, u.name AS adminName, " +
                    "gm.userId, u2.name AS userName, u2.phoneNumber " +
                    "FROM publicgroups g " +
                    "LEFT JOIN users u ON g.groupAdminId = u.id " +
                    "LEFT JOIN public_group_members gm ON g.groupId = gm.groupId " +
                    "LEFT JOIN users u2 ON gm.userId = u2.id " +
                    "WHERE g.groupId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();

            List<Map<String, Object>> usersList = new ArrayList<>();

            while (resultSet.next()) {
                if (response.isEmpty()) {
                    response.put("groupName", resultSet.getString("groupName"));
                    response.put("groupAdminId", resultSet.getInt("groupAdminId"));
                    response.put("adminName", resultSet.getString("adminName"));
                }

                int userId = resultSet.getInt("userId");
                if (userId > 0) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("userId", userId);
                    user.put("userName", resultSet.getString("userName"));
                    user.put("phoneNumber", resultSet.getString("phoneNumber"));
                    usersList.add(user);
                }
            }

            response.put("users", usersList);
            response.put("status", "Group information retrieved successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Failed to fetch group information");
        }

        return response;
    }
    // get all public groups
    @GetMapping("/groups")
    public List<Map<String, Object>> getAllGroups() {
        List<Map<String, Object>> responseList = new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT groupId, groupName, groupAdminId, createdAt, updatedAt FROM publicgroups";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> group = new HashMap<>();
                group.put("groupId", resultSet.getInt("groupId"));
                group.put("groupName", resultSet.getString("groupName"));
                group.put("groupAdminId", resultSet.getInt("groupAdminId"));
                group.put("createdAt", resultSet.getTimestamp("createdAt"));
                group.put("updatedAt", resultSet.getTimestamp("updatedAt"));

                responseList.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("status", "Failed to fetch group information");
            responseList.add(error);
        }

        return responseList;
    }
}
