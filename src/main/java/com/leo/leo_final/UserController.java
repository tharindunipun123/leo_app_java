package com.leo.leo_final;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.sql.Connection;
import java.sql.Date;
import java.util.*;
import java.util.Base64;
@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class UserController {
    private static final String API_URL = "https://app.notify.lk/api/v1/send";
    private static final String API_KEY = "9nkqa0JaEavbEvtZwuAb";
    private static final String SENDER_ID = "27438";


//    @GetMapping("/user/{userId}")
//    public Map<String, Object> getUserById(@PathVariable int userId) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            Connection connection = DBConnection.getInstance().getConnection();
//            String query = "SELECT id, phoneNumber, name, about, profilePicUrl, gender, country, birthday, bio, motto FROM users WHERE id = ?";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, userId);
//            ResultSet resultSet = statement.executeQuery();
//
//            if (resultSet.next()) {
//                response.put("id", resultSet.getInt("id"));
//                response.put("phoneNumber", resultSet.getString("phoneNumber"));
//                response.put("name", resultSet.getString("name"));
//                response.put("about", resultSet.getString("about"));
//                response.put("profilePicUrl", resultSet.getString("profilePicUrl"));
//                response.put("gender", resultSet.getString("gender"));
//                response.put("country", resultSet.getString("country"));
//                response.put("birthday", resultSet.getDate("birthday"));
//                response.put("bio", resultSet.getString("bio"));
//                response.put("motto", resultSet.getString("motto"));
//
//                // Encode the image as Base64
//                String profilePicUrl = resultSet.getString("profilePicUrl");
//                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
//                    String MAIN_FOLDER_PATH = File.separator+"profileImage"+File.separator+ profilePicUrl;
//                    Path path = Paths.get(MAIN_FOLDER_PATH);
//                   // Path imagePath = Paths.get(uploadDir + File.separator + profilePicUrl);
//                    if (Files.exists(path)) {
//                        byte[] imageBytes = Files.readAllBytes(path);
//                        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
//                        response.put("profilePic", encodedImage);
//                    } else {
//                        response.put("profilePic", "File not found");
//                    }
//                }
//            } else {
//                response.put("error", "User not found");
//            }
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//            response.put("error", "Failed to fetch user details");
//        }
//
//        return response;
//    }
//@GetMapping("/user/{userId}")
//public Map<String, Object> getUserById(@PathVariable int userId) {
//    Map<String, Object> response = new HashMap<>();
//
//    try {
//        Connection connection = DBConnection.getInstance().getConnection();
//        String query = "SELECT id, phoneNumber, name, about, profilePicUrl, gender, country, birthday, bio, motto FROM users WHERE id = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, userId);
//        ResultSet resultSet = statement.executeQuery();
//
//        if (resultSet.next()) {
//            response.put("id", resultSet.getInt("id"));
//            response.put("phoneNumber", resultSet.getString("phoneNumber"));
//            response.put("name", resultSet.getString("name"));
//            response.put("about", resultSet.getString("about"));
//            response.put("profilePicUrl", resultSet.getString("profilePicUrl"));
//            response.put("gender", resultSet.getString("gender"));
//            response.put("country", resultSet.getString("country"));
//            response.put("birthday", resultSet.getDate("birthday"));
//            response.put("bio", resultSet.getString("bio"));
//            response.put("motto", resultSet.getString("motto"));
//
//            String profilePicUrl = resultSet.getString("profilePicUrl");
//            if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
//                String MAIN_FOLDER_PATH = uploadDir + File.separator + "profileImage" + File.separator + profilePicUrl;
//                Path path = Paths.get(MAIN_FOLDER_PATH);
//                if (Files.exists(path)) {
//                    byte[] imageBytes = Files.readAllBytes(path);
//                    String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
//                    response.put("profilePic", encodedImage);
//                } else {
//                    response.put("profilePic", "File not found");
//                }
//            }
//        } else {
//            response.put("error", "User not found");
//        }
//    } catch (SQLException | IOException e) {
//        e.printStackTrace();
//        response.put("error", "Failed to fetch user details");
//    }
//
//    return response;
//}

    @PostMapping("/verifyOtp")
    public boolean verifyOtp(@RequestBody Map<String, String> details) {
        String mobileNumber = details.get("mobileNumber");
        int enteredOtp = Integer.parseInt(details.get("otp"));
        int storedOtp = 654321;

        return true;
    }
    @Value("${uploadDir}")
    private String uploadDir;

    // Endpoint to update user profile including profile photo
//    @PostMapping("/updateProfile")
//    public Map<String, String> updateProfile(
//            @RequestParam("userId") int userId,
//            @RequestParam("name") String name,
//            @RequestParam("image") String image,//base 64 image data as string
//            @RequestParam("image") String fileName,//string file name like name.jpg
//            @RequestParam("about") String about) {
//
//        Map<String, String> response = new HashMap<>();
//
//        try {
////            // Save the file to the upload directory
////            String fileName = file.getOriginalFilename();
////            Path filePath = Paths.get(uploadDir + File.separator + fileName);
////            Files.copy(file.getInputStream(), filePath);
////
////            // Save the file URL in the database
////            String fileUrl = "/uploads/" + fileName;
//
//            String MAIN_FOLDER_PATH = File.separator+"profileImage";
//            if (!new File(MAIN_FOLDER_PATH).exists()) {
//                new File(MAIN_FOLDER_PATH).mkdir();
//            }
//            File imageFile1 = new File(MAIN_FOLDER_PATH + File.separator + fileName);
//            if (!imageFile1.exists()) {
//                FileOutputStream fos1 = new FileOutputStream(MAIN_FOLDER_PATH + File.separator + fileName);
//                fos1.write(java.util.Base64.getDecoder().decode(image));
//                fos1.close();
//            }
//
//            saveProfileUrlToDatabase(userId, name, about, fileName);
//
//
//            response.put("status", "Profile updated successfully");
//            response.put("fileUrl", fileName);
//
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//            response.put("status", "Profile update failed");
//        }
//
//        return response;
//    }
//
//    private void saveProfileUrlToDatabase(int userId, String name, String about, String fileUrl) throws SQLException {
//        Connection connection = DBConnection.getInstance().getConnection();
//        String query = "UPDATE users SET name = ?, about = ?, profilePicUrl = ? WHERE id = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setString(1, name);
//        statement.setString(2, about);
//        statement.setString(3, fileUrl);
//        statement.setInt(4, userId);
//
//        statement.executeUpdate();
//    }


    @PostMapping("/getOtp")
    public Map<String, Object> getOtp(@RequestBody Map<String, String> details) {
        String mobileNumber = details.get("mobileNumber");
        System.out.println(mobileNumber);
        int otp = new Random().nextInt(900000) + 100000; // Generate a 6-digit OTP

        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();

            // Check if the phone number already exists in the database
            String checkQuery = "SELECT id FROM users WHERE phoneNumber = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, mobileNumber);
            ResultSet checkResult = checkStatement.executeQuery();

            int userId;
            if (checkResult.next()) {
                // If the phone number exists, retrieve the userId
                userId = checkResult.getInt("id");
            } else {
                // If the phone number does not exist, insert a new record
                String insertQuery = "INSERT INTO users (phoneNumber) VALUES (?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, mobileNumber);
                insertStatement.executeUpdate();

                ResultSet keys = insertStatement.getGeneratedKeys();
                if (keys.next()) {
                    userId = keys.getInt(1);
                } else {
                    throw new SQLException("User ID generation failed.");
                }
            }



            response.put("userId", userId);
            response.put("otp", otp);
            sendOtpToMobile(mobileNumber, otp);

        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "User registration failed");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Failed to send OTP via SMS");
        }
        return response;
    }


    private void sendOtpToMobile(String mobileNumber, int otp) throws IOException {
        String API_URL = "https://app.notify.lk/api/v1/send";
        String USER_ID = "27438";
        String API_KEY = "9nkqa0JaEavbEvtZwuAb";
        String SENDER_ID = "NotifyDEMO"; // Use NotifyDEMO for testing
        String message = "Your OTP code is: " + otp;

        OkHttpClient client = new OkHttpClient();

        // Ensure the phone number is in the correct format
        String formattedNumber = mobileNumber.startsWith("0") ? "94" + mobileNumber.substring(1) : mobileNumber;

        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder();
        urlBuilder.addQueryParameter("user_id", USER_ID);
        urlBuilder.addQueryParameter("api_key", API_KEY);
        urlBuilder.addQueryParameter("sender_id", SENDER_ID);
        urlBuilder.addQueryParameter("to", formattedNumber);
        urlBuilder.addQueryParameter("message", message);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Unexpected code " + response);
                System.err.println("Response body: " + response.body().string());
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string());
        }
    }



//    @PostMapping("/updateProfile")
//    public Map<String, String> updateProfile(@RequestBody Map<String, Object> payload) {
//
//        Map<String, String> response = new HashMap<>();
//
//        try {
//            int userId = Integer.parseInt((String) payload.get("userId"));
//            String name = (String) payload.get("name");
//            String image = (String) payload.get("image"); // base64 image data as string
//            String fileName = (String) payload.get("fileName"); // string file name like name.jpg
//            String about = (String) payload.get("about");
//
//            String MAIN_FOLDER_PATH = uploadDir + File.separator + "profileImage";
//            if (!new File(MAIN_FOLDER_PATH).exists()) {
//                new File(MAIN_FOLDER_PATH).mkdir();
//            }
//            File imageFile1 = new File(MAIN_FOLDER_PATH + File.separator + fileName);
//            if (!imageFile1.exists()) {
//                FileOutputStream fos1 = new FileOutputStream(imageFile1);
//                fos1.write(java.util.Base64.getDecoder().decode(image));
//                fos1.close();
//            }
//
//            saveProfileUrlToDatabase(userId, name, about, fileName);
//
//            response.put("status", "Profile updated successfully");
//            response.put("fileUrl", fileName);
//
//        } catch (IOException | SQLException | NumberFormatException e) {
//            e.printStackTrace();
//            response.put("status", "Profile update failed");
//        }
//
//        return response;
//    }
//
//    private void saveProfileUrlToDatabase(int userId, String name, String about, String fileUrl) throws SQLException {
//        Connection connection = DBConnection.getInstance().getConnection();
//        String query = "UPDATE users SET name = ?, about = ?, profilePicUrl = ? WHERE id = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setString(1, name);
//        statement.setString(2, about);
//        statement.setString(3, fileUrl);
//        statement.setInt(4, userId);
//
//        statement.executeUpdate();
//    }

    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserById(@PathVariable int userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT id, phoneNumber, name, about, profilePicUrl, gender, country, birthday, bio, motto FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                response.put("id", resultSet.getInt("id"));
                response.put("phoneNumber", resultSet.getString("phoneNumber"));
                response.put("name", resultSet.getString("name"));
                response.put("about", resultSet.getString("about"));
                response.put("profilePicUrl", resultSet.getString("profilePicUrl"));
                response.put("gender", resultSet.getString("gender"));
                response.put("country", resultSet.getString("country"));
                response.put("birthday", resultSet.getDate("birthday"));
                response.put("bio", resultSet.getString("bio"));
                response.put("motto", resultSet.getString("motto"));

                String profilePicUrl = resultSet.getString("profilePicUrl");
                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                    String fileUrl = "http://45.126.125.172:8080/uploads/profileImage/" + profilePicUrl;
                    response.put("profilePicUrl", fileUrl);
                } else {
                    response.put("profilePicUrl", "No image available");
                }
            } else {
                response.put("error", "User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Failed to fetch user details");
        }

        return response;
    }

    @GetMapping("/user/username/{userName}")
    public Map<String, Object> getUsersByName(@PathVariable String userName) {
        Map<String, Object> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT id, phoneNumber, name, about, profilePicUrl, gender, country, birthday, bio, motto FROM users WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                response.put("id", resultSet.getInt("id"));
                response.put("phoneNumber", resultSet.getString("phoneNumber"));
                response.put("name", resultSet.getString("name"));
                response.put("about", resultSet.getString("about"));
                response.put("profilePicUrl", resultSet.getString("profilePicUrl"));
                response.put("gender", resultSet.getString("gender"));
                response.put("country", resultSet.getString("country"));
                response.put("birthday", resultSet.getDate("birthday"));
                response.put("bio", resultSet.getString("bio"));
                response.put("motto", resultSet.getString("motto"));

                String profilePicUrl = resultSet.getString("profilePicUrl");
                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                    String fileUrl = "http://45.126.125:8080/uploads/profileImage/" + profilePicUrl;
                    response.put("profilePicUrl", fileUrl);
                } else {
                    response.put("profilePicUrl", "No image available");
                }
            } else {
                response.put("error", "User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Failed to fetch user details");
        }

        return response;
    }

    @PostMapping("/updateProfile")
    public Map<String, String> updateProfile(@RequestParam("userId") int userId,
                                             @RequestParam("name") String name,
                                             @RequestParam("about") String about,
                                             @RequestParam("file") MultipartFile file) {

        Map<String, String> response = new HashMap<>();

        try {
            // Save the file
            String fileName = file.getOriginalFilename();
            String fileUrl = saveFile(fileName, file);

            // Save the profile info in the database
            saveProfileUrlToDatabase(userId, name, about, fileUrl);

            response.put("status", "Profile updated successfully");
            response.put("fileUrl", fileUrl);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            response.put("status", "Profile update failed");
        }

        return response;
    }

    private String saveFile(String fileName, MultipartFile file) throws IOException {
        String uploadPath = uploadDir + File.separator + "profileImage";
        Path path = Paths.get(uploadPath);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        Path filePath = path.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }

    private void saveProfileUrlToDatabase(int userId, String name, String about, String fileUrl) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "UPDATE users SET name = ?, about = ?, profilePicUrl = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        statement.setString(2, about);
        statement.setString(3, fileUrl);
        statement.setInt(4, userId);

        statement.executeUpdate();
    }

    @PostMapping("/checkContacts")
    public List<Map<String, Object>> checkContacts(@RequestBody List<Map<String, String>> contacts) {
        List<Map<String, Object>> response = new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();

            for (Map<String, String> contact : contacts) {
                String mobileNumber = contact.get("mobileNumber");
                String contactName = contact.get("contactName");

                String query = "SELECT id, profilePicUrl FROM users WHERE phoneNumber = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, mobileNumber);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String profilePicUrl = resultSet.getString("profilePicUrl");

                    Map<String, Object> userResponse = new HashMap<>();
                    userResponse.put("userId", userId);
                    userResponse.put("contactName", contactName);
                    userResponse.put("profilePicUrl", profilePicUrl);

                    response.add(userResponse);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }

    @PostMapping("/partialUpdateProfile")
    public Map<String, String> partialUpdateProfile(@RequestBody Map<String, String> details) {
        int userId = Integer.parseInt(details.get("userId"));
        StringBuilder queryBuilder = new StringBuilder("UPDATE users SET ");
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        if (details.containsKey("name")) {
            columns.add("name = ?");
            values.add(details.get("name"));
        }
        if (details.containsKey("about")) {
            columns.add("about = ?");
            values.add(details.get("about"));
        }
        if (details.containsKey("profilePicUrl")) {
            columns.add("profilePicUrl = ?");
            values.add(details.get("profilePicUrl"));
        }
        if (details.containsKey("gender")) {
            columns.add("gender = ?");
            values.add(details.get("gender"));
        }
        if (details.containsKey("country")) {
            columns.add("country = ?");
            values.add(details.get("country"));
        }
        if (details.containsKey("birthday")) {
            columns.add("birthday = ?");
            values.add(Date.valueOf(details.get("birthday")));
        }
        if (details.containsKey("bio")) {
            columns.add("bio = ?");
            values.add(details.get("bio"));
        }
        if (details.containsKey("motto")) {
            columns.add("motto = ?");
            values.add(details.get("motto"));
        }
        queryBuilder.append(String.join(", ", columns));
        queryBuilder.append(" WHERE id = ?");
        values.add(userId);

        Map<String, String> response = new HashMap<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                response.put("status", "Profile updated successfully");
            } else {
                response.put("status", "Profile update failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("status", "Profile update failed");
        }
        return response;
    }
    @GetMapping("/users")
    public List<Map<String, Object>> getAllUsers() {
        List<Map<String, Object>> usersList = new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT id, phoneNumber, name, about, profilePicUrl, gender, country, birthday, bio, motto FROM users";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", resultSet.getInt("id"));
                user.put("phoneNumber", resultSet.getString("phoneNumber"));
                user.put("name", resultSet.getString("name"));
                user.put("about", resultSet.getString("about"));
                user.put("profilePicUrl", resultSet.getString("profilePicUrl"));
                user.put("gender", resultSet.getString("gender"));
                user.put("country", resultSet.getString("country"));
                user.put("birthday", resultSet.getDate("birthday"));
                user.put("bio", resultSet.getString("bio"));
                user.put("motto", resultSet.getString("motto"));

                usersList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception or return an error response
        }

        return usersList;
    }


}
