-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: swp391
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `booking_id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(200) DEFAULT NULL,
  `appointment_date` date DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `last_updated_time` datetime(6) DEFAULT NULL,
  `note` varchar(500) DEFAULT NULL,
  `patient_name` varchar(200) DEFAULT NULL,
  `phone_number` varchar(32) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `patient_id` int DEFAULT NULL,
  `recipient_id` int DEFAULT NULL,
  `service_id` int DEFAULT NULL,
  `slot_id` int DEFAULT NULL,
  PRIMARY KEY (`booking_id`),
  KEY `FK5q5x2e4ror7mwq56tmd3w238v` (`patient_id`),
  KEY `FKiiou9s185gu624vsnn4mpalb6` (`recipient_id`),
  KEY `FKd6bok0hme4dnt62ldm25tgl7` (`service_id`),
  KEY `FKf91hxcvywio343rabt0v9gcd3` (`slot_id`),
  CONSTRAINT `FK5q5x2e4ror7mwq56tmd3w238v` FOREIGN KEY (`patient_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKd6bok0hme4dnt62ldm25tgl7` FOREIGN KEY (`service_id`) REFERENCES `services` (`service_id`),
  CONSTRAINT `FKf91hxcvywio343rabt0v9gcd3` FOREIGN KEY (`slot_id`) REFERENCES `slots` (`slot_id`),
  CONSTRAINT `FKiiou9s185gu624vsnn4mpalb6` FOREIGN KEY (`recipient_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `images`
--

DROP TABLE IF EXISTS `images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `images` (
  `image_id` int NOT NULL AUTO_INCREMENT,
  `image_url` text,
  PRIMARY KEY (`image_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `images`
--

LOCK TABLES `images` WRITE;
/*!40000 ALTER TABLE `images` DISABLE KEYS */;
INSERT INTO `images` VALUES (1,'/images/khamtongquat1.png'),(2,'/images/caovoirang1.png'),(3,'/images/taytrangrang1.png'),(4,'/images/chupxquangrang1.png');
/*!40000 ALTER TABLE `images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `invoice_id` int NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) DEFAULT NULL,
  `is_paid` bit(1) DEFAULT NULL,
  `medicine_fee` decimal(38,2) DEFAULT NULL,
  `paid_time` datetime(6) DEFAULT NULL,
  `service_fee` decimal(38,2) DEFAULT NULL,
  `total_amount` decimal(38,2) DEFAULT NULL,
  `booking_id` int DEFAULT NULL,
  PRIMARY KEY (`invoice_id`),
  UNIQUE KEY `UK32ywtxrkeu1wnmivu6mlcqdid` (`booking_id`),
  CONSTRAINT `FK4jd6uuk7w0d72riyre2w14fl7` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_medicine_detail`
--

DROP TABLE IF EXISTS `invoice_medicine_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_medicine_detail` (
  `invoice_id` int NOT NULL,
  `medicine_id` varchar(255) NOT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`invoice_id`,`medicine_id`),
  KEY `FKgndmskpqph3v1axim75n9to6j` (`medicine_id`),
  CONSTRAINT `FKgndmskpqph3v1axim75n9to6j` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`),
  CONSTRAINT `FKs8e49psnxcbf8stxoj2tan8dl` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`invoice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_medicine_detail`
--

LOCK TABLES `invoice_medicine_detail` WRITE;
/*!40000 ALTER TABLE `invoice_medicine_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_medicine_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicines`
--

DROP TABLE IF EXISTS `medicines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicines` (
  `medicine_id` varchar(255) NOT NULL,
  `ingredients` varchar(1000) DEFAULT NULL,
  `manufacturer` varchar(200) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `price` int DEFAULT NULL,
  `remaining_quantity` int DEFAULT NULL,
  `unit` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`medicine_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicines`
--

LOCK TABLES `medicines` WRITE;
/*!40000 ALTER TABLE `medicines` DISABLE KEYS */;
/*!40000 ALTER TABLE `medicines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_report`
--

DROP TABLE IF EXISTS `patient_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient_report` (
  `patient_report_id` int NOT NULL AUTO_INCREMENT,
  `diagnosis` varchar(500) DEFAULT NULL,
  `doctor_note` varchar(500) DEFAULT NULL,
  `last_updated_time` datetime(6) DEFAULT NULL,
  `treatment_method` varchar(500) DEFAULT NULL,
  `booking_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  PRIMARY KEY (`patient_report_id`),
  KEY `FKkimw0xfishcwbiap4qmdxdoti` (`booking_id`),
  KEY `FKsjfb0ht8x5oflvg95bi22olxf` (`doctor_id`),
  CONSTRAINT `FKkimw0xfishcwbiap4qmdxdoti` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`),
  CONSTRAINT `FKsjfb0ht8x5oflvg95bi22olxf` FOREIGN KEY (`doctor_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_report`
--

LOCK TABLES `patient_report` WRITE;
/*!40000 ALTER TABLE `patient_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_report_images`
--

DROP TABLE IF EXISTS `patient_report_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient_report_images` (
  `image_id` int NOT NULL,
  `report_id` int NOT NULL,
  PRIMARY KEY (`image_id`,`report_id`),
  KEY `FKqf4p7e8xxxwb5ewrq7b7wc1p9` (`report_id`),
  CONSTRAINT `FKiaipy6b7w0tktk4i90x4yg1kk` FOREIGN KEY (`image_id`) REFERENCES `images` (`image_id`),
  CONSTRAINT `FKqf4p7e8xxxwb5ewrq7b7wc1p9` FOREIGN KEY (`report_id`) REFERENCES `patient_report` (`patient_report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_report_images`
--

LOCK TABLES `patient_report_images` WRITE;
/*!40000 ALTER TABLE `patient_report_images` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient_report_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_medicine`
--

DROP TABLE IF EXISTS `report_medicine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report_medicine` (
  `medicine_id` varchar(255) NOT NULL,
  `patient_report_id` int NOT NULL,
  `note` varchar(200) DEFAULT NULL,
  `price` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`medicine_id`,`patient_report_id`),
  KEY `FKibe95b9rlfq22s1hqkp15nfha` (`patient_report_id`),
  CONSTRAINT `FK2twypbignvwgot9vk4tfrs2xp` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`),
  CONSTRAINT `FKibe95b9rlfq22s1hqkp15nfha` FOREIGN KEY (`patient_report_id`) REFERENCES `patient_report` (`patient_report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_medicine`
--

LOCK TABLES `report_medicine` WRITE;
/*!40000 ALTER TABLE `report_medicine` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_medicine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `is_active` bit(1) NOT NULL,
  `role_name` varchar(255) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,_binary '','ADMIN'),(2,_binary '','MANAGER'),(3,_binary '','DOCTOR'),(4,_binary '','RECEPTIONIST'),(5,_binary '','GUEST');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `room_id` int NOT NULL AUTO_INCREMENT,
  `is_active` bit(1) DEFAULT NULL,
  `room_name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_image`
--

DROP TABLE IF EXISTS `service_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_image` (
  `image_id` int NOT NULL,
  `service_id` int NOT NULL,
  PRIMARY KEY (`image_id`,`service_id`),
  KEY `FK9jqqqllv1qydjn4nelpprmlt4` (`service_id`),
  CONSTRAINT `FK9jqqqllv1qydjn4nelpprmlt4` FOREIGN KEY (`service_id`) REFERENCES `services` (`service_id`),
  CONSTRAINT `FKj44dgx53oav76ht9bsf7pjje4` FOREIGN KEY (`image_id`) REFERENCES `images` (`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_image`
--

LOCK TABLES `service_image` WRITE;
/*!40000 ALTER TABLE `service_image` DISABLE KEYS */;
INSERT INTO `service_image` VALUES (1,1),(2,2),(3,3),(4,4);
/*!40000 ALTER TABLE `service_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `services`
--

DROP TABLE IF EXISTS `services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `services` (
  `service_id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(1000) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `price` int DEFAULT NULL,
  `service_name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `services`
--

LOCK TABLES `services` WRITE;
/*!40000 ALTER TABLE `services` DISABLE KEYS */;
INSERT INTO `services` VALUES (1,'Khám tổng quát',_binary '',100000,'Khám tổng quát'),(2,'Cạo vôi răng',_binary '',200000,'Cạo vôi răng'),(3,'Tẩy trắng răng',_binary '',1500000,'Tẩy trắng răng'),(4,'Chụp X-quang răng',_binary '',300000,'Chụp X-quang răng');
/*!40000 ALTER TABLE `services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sessions` (
  `session_id` int NOT NULL AUTO_INCREMENT,
  `session_date` datetime(6) DEFAULT NULL,
  `booking_id` int DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `room_id` int DEFAULT NULL,
  `slot_id` int DEFAULT NULL,
  PRIMARY KEY (`session_id`),
  KEY `FKjdx4s91np9yr5nhstlc3olbs1` (`booking_id`),
  KEY `FK747sv335to4k7cr6wwxocdp4a` (`employee_id`),
  KEY `FKllu7qjrru66od7usrj7t7fm3v` (`room_id`),
  KEY `FKbhcw23mgoxq01enj7lcgf8rfl` (`slot_id`),
  CONSTRAINT `FK747sv335to4k7cr6wwxocdp4a` FOREIGN KEY (`employee_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKbhcw23mgoxq01enj7lcgf8rfl` FOREIGN KEY (`slot_id`) REFERENCES `slots` (`slot_id`),
  CONSTRAINT `FKjdx4s91np9yr5nhstlc3olbs1` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`),
  CONSTRAINT `FKllu7qjrru66od7usrj7t7fm3v` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `slots`
--

DROP TABLE IF EXISTS `slots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `slots` (
  `slot_id` int NOT NULL AUTO_INCREMENT,
  `end_time` time(6) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `start_time` time(6) DEFAULT NULL,
  PRIMARY KEY (`slot_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slots`
--

LOCK TABLES `slots` WRITE;
/*!40000 ALTER TABLE `slots` DISABLE KEYS */;
INSERT INTO `slots` VALUES (1,'09:00:00.000000',_binary '','08:00:00.000000'),(2,'10:00:00.000000',_binary '','09:00:00.000000'),(3,'11:00:00.000000',_binary '','10:00:00.000000'),(4,'12:00:00.000000',_binary '','11:00:00.000000'),(5,'13:00:00.000000',_binary '','12:00:00.000000'),(6,'14:00:00.000000',_binary '','13:00:00.000000'),(7,'15:00:00.000000',_binary '','14:00:00.000000'),(8,'16:00:00.000000',_binary '','15:00:00.000000'),(9,'17:00:00.000000',_binary '','16:00:00.000000'),(10,'18:00:00.000000',_binary '','17:00:00.000000'),(11,'19:00:00.000000',_binary '','18:00:00.000000'),(12,'20:00:00.000000',_binary '','19:00:00.000000'),(13,'21:00:00.000000',_binary '','20:00:00.000000'),(14,'22:00:00.000000',_binary '','21:00:00.000000');
/*!40000 ALTER TABLE `slots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKg7im3j7f0g31yhl6qco2iboy5` (`user_id`),
  CONSTRAINT `FKj8rfw4x0wjjyibfqq566j4qng` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(200) DEFAULT NULL,
  `avatar` longtext,
  `dob` date DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(50) DEFAULT NULL,
  `gender` varchar(30) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `roleid` int DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK9q63snka3mdh91as4io72espi` (`phone_number`),
  KEY `FKgrhs0suhl8cbodxn47xadxp94` (`roleid`),
  CONSTRAINT `FKgrhs0suhl8cbodxn47xadxp94` FOREIGN KEY (`roleid`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'123 Admin Street','data:image/png;base64,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','1990-01-01','admin@gmail.com','Adminnnnn','Female',_binary '','admin1234','0123456789',1),(2,'456 Manager Ave','','1991-02-02','manager@gmail.com','Manager User','Female',_binary '','manager123','0234567891',2),(3,'789 Doctor Blvd','','1992-03-03','doctor@gmail.com','Doctor User','Male',_binary '','doctor123','0345678912',3),(4,'321 Reception Rd','','1993-04-04','receptionist@gmail.com','Receptionist User','Female',_binary '','reception123','0456789123',4),(5,'654 Guest Ln','','1994-05-05','guest@gmail.com','Guest User','Other',_binary '','guest123','0567891234',5);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `working_schedules`
--

DROP TABLE IF EXISTS `working_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `working_schedules` (
  `schedule_id` int NOT NULL AUTO_INCREMENT,
  `checkin_time` time(6) DEFAULT NULL,
  `checkout_time` time(6) DEFAULT NULL,
  `date_working` datetime(6) DEFAULT NULL,
  `is_woring` bit(1) DEFAULT NULL,
  `last_updated_time` datetime(6) DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  PRIMARY KEY (`schedule_id`),
  KEY `FK7g5nxotdwlgkd8iog4ivq5s72` (`employee_id`),
  CONSTRAINT `FK7g5nxotdwlgkd8iog4ivq5s72` FOREIGN KEY (`employee_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `working_schedules`
--

LOCK TABLES `working_schedules` WRITE;
/*!40000 ALTER TABLE `working_schedules` DISABLE KEYS */;
/*!40000 ALTER TABLE `working_schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'swp391'
--

--
-- Dumping routines for database 'swp391'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-08 23:48:15
