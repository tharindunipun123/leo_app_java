-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 08, 2024 at 03:36 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `leo_chat`
--

-- --------------------------------------------------------

--
-- Table structure for table `call_details`
--

CREATE TABLE `call_details` (
  `id` int(11) NOT NULL,
  `senderID` int(11) NOT NULL,
  `callerId` varchar(255) NOT NULL,
  `receiverId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `call_details`
--

INSERT INTO `call_details` (`id`, `senderID`, `callerId`, `receiverId`) VALUES
(1, 1, 'caller123', 2);

-- --------------------------------------------------------

--
-- Table structure for table `contacts`
--

CREATE TABLE `contacts` (
  `id` int(11) NOT NULL,
  `userId` int(11) DEFAULT NULL,
  `contactUserId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `contacts`
--

INSERT INTO `contacts` (`id`, `userId`, `contactUserId`) VALUES
(1, 1, 2),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4),
(5, 2, 1),
(6, 3, 1),
(7, 3, 4);

-- --------------------------------------------------------

--
-- Table structure for table `followers`
--

CREATE TABLE `followers` (
  `id` int(11) NOT NULL,
  `followerId` int(11) NOT NULL,
  `followingId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `followers`
--

INSERT INTO `followers` (`id`, `followerId`, `followingId`) VALUES
(1, 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `groups`
--

CREATE TABLE `groups` (
  `groupId` int(11) NOT NULL,
  `groupName` varchar(255) NOT NULL,
  `groupAdminId` int(11) NOT NULL,
  `createdAt` timestamp NULL DEFAULT current_timestamp(),
  `updatedAt` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `groups`
--

INSERT INTO `groups` (`groupId`, `groupName`, `groupAdminId`, `createdAt`, `updatedAt`) VALUES
(1, 'Sample Group', 1, '2024-06-27 17:41:14', '2024-06-27 17:41:14'),
(2, 'My Group', 2, '2024-06-27 17:46:00', '2024-06-27 17:46:00');

-- --------------------------------------------------------

--
-- Table structure for table `group_members`
--

CREATE TABLE `group_members` (
  `id` int(11) NOT NULL,
  `groupId` int(11) NOT NULL,
  `userId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `group_members`
--

INSERT INTO `group_members` (`id`, `groupId`, `userId`) VALUES
(2, 1, 3),
(4, 2, 1),
(5, 2, 2),
(3, 2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `publicgroups`
--

CREATE TABLE `publicgroups` (
  `groupId` int(11) NOT NULL,
  `groupName` varchar(255) NOT NULL,
  `groupAdminId` int(11) NOT NULL,
  `createdAt` timestamp NULL DEFAULT current_timestamp(),
  `updatedAt` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `publicgroups`
--

INSERT INTO `publicgroups` (`groupId`, `groupName`, `groupAdminId`, `createdAt`, `updatedAt`) VALUES
(1, 'New Group', 1, '2024-07-04 21:39:56', '2024-07-04 21:39:56');

-- --------------------------------------------------------

--
-- Table structure for table `public_group_members`
--

CREATE TABLE `public_group_members` (
  `id` int(11) NOT NULL,
  `groupId` int(11) NOT NULL,
  `userId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `statuses`
--

CREATE TABLE `statuses` (
  `statusId` int(11) NOT NULL,
  `userId` int(11) DEFAULT NULL,
  `statusText` text DEFAULT NULL,
  `statusImageUrl` varchar(255) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `statuses`
--

INSERT INTO `statuses` (`statusId`, `userId`, `statusText`, `statusImageUrl`, `createdAt`) VALUES
(1, 1, 'hello', 'upload\\uploads\\statusImages\\OIP.jpg', '2024-07-05 21:19:07'),
(2, 2, 'awserdt', 'upload\\uploads\\statusImages\\_F1A0126.jpg', '2024-07-05 23:27:27'),
(3, 1, 'afsf', '2841c78f-88fd-4548-bb7e-087829e53234.jpg', '2024-07-07 13:04:32'),
(4, 1, 'fdgdfgsdg', '1665991589_polonnaruwa.jpg', '2024-07-07 13:04:49'),
(5, 2, 'test status 1', 'uploaduploadsstatusImages	est1.jpg', '2024-07-08 06:30:00'),
(6, 3, 'test status 2', 'uploaduploadsstatusImages	est2.jpg', '2024-07-08 06:35:00'),
(7, 3, 'awserdfgty', 'Untitled.jpg', '2024-07-07 14:38:09'),
(8, 5, 'awserdt', 'OIP (5).jpg', '2024-07-07 15:10:28');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `phoneNumber` varchar(15) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `about` text DEFAULT NULL,
  `profilePicUrl` varchar(255) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `bio` text DEFAULT NULL,
  `motto` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `phoneNumber`, `name`, `about`, `profilePicUrl`, `gender`, `country`, `birthday`, `bio`, `motto`) VALUES
(1, '1234567890', 'Tharindu Nipun', 'fdsf', '1665991575_sigiriya.jpg', 'male', 'USA', '1990-05-15', 'Passionate about technology.', 'Live and learn.'),
(2, '9876543210', 'Jane Smith', 'Data Scientist', 'https://example.com/janesmith.jpg', 'Female', 'Canada', '1988-10-20', 'Loves analyzing data.', 'Always curious.'),
(3, '5556667777', 'Alice Johnson', 'UX Designer', 'https://example.com/alicejohnson.jpg', 'Female', 'UK', '1995-02-28', 'Creates delightful user experiences.', 'Design is life.'),
(4, '+94769146421', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(5, '+94759146425', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(6, '94769146421', 'Tharindu Nipun', 'fsdf', 'OIP (2).jpg', NULL, NULL, NULL, NULL, NULL),
(7, '0769146421', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `voiceroom`
--

CREATE TABLE `voiceroom` (
  `id` int(11) NOT NULL,
  `groupId` int(11) NOT NULL,
  `voiceRoomId` varchar(255) DEFAULT NULL,
  `isCreated` tinyint(1) DEFAULT 0,
  `createdAt` timestamp NULL DEFAULT current_timestamp(),
  `updatedAt` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `voiceroom`
--

INSERT INTO `voiceroom` (`id`, `groupId`, `voiceRoomId`, `isCreated`, `createdAt`, `updatedAt`) VALUES
(1, 1, '102', 0, '2024-06-29 14:01:55', '2024-06-29 14:06:01');

-- --------------------------------------------------------

--
-- Table structure for table `wallet`
--

CREATE TABLE `wallet` (
  `userId` int(11) NOT NULL,
  `amount` decimal(10,2) DEFAULT 0.00,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `diamondAmount` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wallet`
--

INSERT INTO `wallet` (`userId`, `amount`, `firstName`, `lastName`, `phoneNumber`, `email`, `diamondAmount`) VALUES
(1, 1000.00, 'John', 'Doe', '1234567890', 'john.doe@example.com', 350),
(3, 100.00, 'John', 'Doe', '1234567890', 'john.doe@example.com', 50),
(5, 100.00, 'John', 'Doe', '1234567890', 'john.doe@example.com', 50),
(10, 100.00, 'John', 'Doe', '1234567890', 'john.doe@example.com', 50);

-- --------------------------------------------------------

--
-- Table structure for table `wallethistory`
--

CREATE TABLE `wallethistory` (
  `id` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `amount` decimal(10,2) DEFAULT 0.00,
  `diamondAmount` int(11) DEFAULT 0,
  `action` varchar(50) NOT NULL,
  `createdAt` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `call_details`
--
ALTER TABLE `call_details`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `contacts`
--
ALTER TABLE `contacts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `userId` (`userId`),
  ADD KEY `contactUserId` (`contactUserId`);

--
-- Indexes for table `followers`
--
ALTER TABLE `followers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `followerId` (`followerId`),
  ADD KEY `followingId` (`followingId`);

--
-- Indexes for table `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`groupId`);

--
-- Indexes for table `group_members`
--
ALTER TABLE `group_members`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_group_user` (`groupId`,`userId`),
  ADD KEY `userId` (`userId`);

--
-- Indexes for table `publicgroups`
--
ALTER TABLE `publicgroups`
  ADD PRIMARY KEY (`groupId`);

--
-- Indexes for table `public_group_members`
--
ALTER TABLE `public_group_members`
  ADD PRIMARY KEY (`id`),
  ADD KEY `groupId` (`groupId`),
  ADD KEY `userId` (`userId`);

--
-- Indexes for table `statuses`
--
ALTER TABLE `statuses`
  ADD PRIMARY KEY (`statusId`),
  ADD KEY `userId` (`userId`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `phoneNumber` (`phoneNumber`);

--
-- Indexes for table `voiceroom`
--
ALTER TABLE `voiceroom`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_group` (`groupId`);

--
-- Indexes for table `wallet`
--
ALTER TABLE `wallet`
  ADD PRIMARY KEY (`userId`);

--
-- Indexes for table `wallethistory`
--
ALTER TABLE `wallethistory`
  ADD PRIMARY KEY (`id`),
  ADD KEY `userId` (`userId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `call_details`
--
ALTER TABLE `call_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `contacts`
--
ALTER TABLE `contacts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `followers`
--
ALTER TABLE `followers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `groups`
--
ALTER TABLE `groups`
  MODIFY `groupId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `group_members`
--
ALTER TABLE `group_members`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `publicgroups`
--
ALTER TABLE `publicgroups`
  MODIFY `groupId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `public_group_members`
--
ALTER TABLE `public_group_members`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `statuses`
--
ALTER TABLE `statuses`
  MODIFY `statusId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `voiceroom`
--
ALTER TABLE `voiceroom`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `wallethistory`
--
ALTER TABLE `wallethistory`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `contacts`
--
ALTER TABLE `contacts`
  ADD CONSTRAINT `contacts_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `contacts_ibfk_2` FOREIGN KEY (`contactUserId`) REFERENCES `users` (`id`);

--
-- Constraints for table `followers`
--
ALTER TABLE `followers`
  ADD CONSTRAINT `followers_ibfk_1` FOREIGN KEY (`followerId`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `followers_ibfk_2` FOREIGN KEY (`followingId`) REFERENCES `users` (`id`);

--
-- Constraints for table `group_members`
--
ALTER TABLE `group_members`
  ADD CONSTRAINT `group_members_ibfk_1` FOREIGN KEY (`groupId`) REFERENCES `groups` (`groupId`),
  ADD CONSTRAINT `group_members_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `users` (`id`);

--
-- Constraints for table `public_group_members`
--
ALTER TABLE `public_group_members`
  ADD CONSTRAINT `public_group_members_ibfk_1` FOREIGN KEY (`groupId`) REFERENCES `publicgroups` (`groupId`),
  ADD CONSTRAINT `public_group_members_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `users` (`id`);

--
-- Constraints for table `statuses`
--
ALTER TABLE `statuses`
  ADD CONSTRAINT `statuses_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`id`);

--
-- Constraints for table `voiceroom`
--
ALTER TABLE `voiceroom`
  ADD CONSTRAINT `fk_group` FOREIGN KEY (`groupId`) REFERENCES `groups` (`groupId`);

--
-- Constraints for table `wallethistory`
--
ALTER TABLE `wallethistory`
  ADD CONSTRAINT `wallethistory_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `wallet` (`userId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
