CREATE DATABASE  IF NOT EXISTS `taxianytime` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `taxianytime`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: taxianytime
-- ------------------------------------------------------
-- Server version	5.5.24-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
  `commentID` int(11) NOT NULL AUTO_INCREMENT,
  `comment` varchar(200) DEFAULT NULL,
  `rating` float DEFAULT NULL,
  `orderid` int(11) DEFAULT NULL,
  PRIMARY KEY (`commentID`),
  KEY `orderfk_idx` (`orderid`),
  CONSTRAINT `orderfk` FOREIGN KEY (`orderid`) REFERENCES `orders` (`orderID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (1,'ΗΤΑΝ ΠΟΛΥ ΚΑΛΟΣ',3,14),(2,'ΓΡΗΓΟΡΗ ΕΞΥΠΗΡΕΤΗΣΗ',3.5,15);
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `customerID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `sirname` varchar(50) DEFAULT NULL,
  `cellphone` varchar(15) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(25) NOT NULL,
  `customerDeviceID` varchar(20) DEFAULT NULL,
  `town` varchar(50) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`customerID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (2,'ΠΕΛΑΤΗΣ1','ΕΠΙΘΕΤΟ1','6900000000','1945-12-24','customer1','123456','/356718771788243','serres','mail1@mail.gr'),(3,'ΠΕΛΑΤΗΣ2','ΕΠΙΘΕΤΟ2','6940000000','2013-02-13','customer2','56789','/356778781788245','ΣΕΡΡΕΣ','usr2@gmail.com');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `orderID` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `customerLocation` varchar(100) DEFAULT NULL,
  `driversLocation` varchar(100) DEFAULT NULL,
  `taxidriverFK` int(11) DEFAULT NULL,
  `customerFK` int(11) DEFAULT NULL,
  `orderState` tinyint(4) DEFAULT NULL,
  `distance` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`orderID`),
  KEY `taxidriverFK` (`taxidriverFK`),
  KEY `customerFK` (`customerFK`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`taxidriverFK`) REFERENCES `taxidriver` (`driverID`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`customerFK`) REFERENCES `customer` (`customerID`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (14,'2013-03-24 15:00:44','41.14064700+24.15103600','41.14773333+24.15000833',3,2,3,'791.6996'),(15,'2013-03-24 15:08:13','41.14156100+24.15164500','41.14773333+24.15000833',3,3,3,'699.11383'),(24,'2013-04-07 22:19:05','41.14339030+24.15114070','41.14773333+24.15000833',3,2,3,'491.60178');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report` (
  `reportID` int(11) NOT NULL AUTO_INCREMENT,
  `reportReason` varchar(200) DEFAULT NULL,
  `orderFK` int(11) DEFAULT NULL,
  `reportFrom` int(11) DEFAULT NULL,
  `reportTo` int(11) DEFAULT NULL,
  `byUser` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`reportID`),
  KEY `orderFK` (`orderFK`),
  CONSTRAINT `report_ibfk_1` FOREIGN KEY (`orderFK`) REFERENCES `orders` (`orderID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
INSERT INTO `report` VALUES (1,'Δεν εμφανίστηκε στο ραντεβού den ton vlepw',24,3,2,'driver'),(2,'Ήταν πολύ αγενής  ka8olou kalh sumperifora',24,3,2,'driver');
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taxidriver`
--

DROP TABLE IF EXISTS `taxidriver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taxidriver` (
  `driverID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `sirname` varchar(50) DEFAULT NULL,
  `cellphone` varchar(15) DEFAULT NULL,
  `town` varchar(50) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `totalRate` float DEFAULT NULL,
  `isAvailable` tinyint(1) DEFAULT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `driverDeviceID` varchar(20) DEFAULT NULL,
  `driverImageUrl` varchar(200) DEFAULT NULL,
  `taxiPlateNumber` varchar(25) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`driverID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taxidriver`
--

LOCK TABLES `taxidriver` WRITE;
/*!40000 ALTER TABLE `taxidriver` DISABLE KEYS */;
INSERT INTO `taxidriver` VALUES (1,'ΚΥΡΙΑΚΟΣ','ΚΥΡΙΑΚΟΥ','6945000000','ΣΕΡΡΕΣ','1967-05-24',3,0,'driver1','123456','/357777961700933','http://192.168.2.100/taxiAnytime_server/androidBot.jpg','ΕΡΕ-4534','driver1@mail.gr'),(2,'ΝΙΚΟΣ','ΝΙΚΟΛΑΟΥ','6970000000','ΣΕΡΡΕΣ','1978-12-10',0,0,'driver3','123456','/567586867956','http://192.168.2.100/taxiAnytime_server/androidBot.jpg','ΕΡΝ-3452','driver2@mail.gr'),(3,'ΘΥΜΙΟΣ','ΠΑΠΑΔΟΠΟΥΛΟΣ','6990000000','serrassss','1974-03-03',4,0,'driver2','123456','/357717577700933','http://192.168.2.100/taxiAnytime_server/androidBot.jpg','ERN-6879','driver3@mail.gr');
/*!40000 ALTER TABLE `taxidriver` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-04-08  0:05:09
