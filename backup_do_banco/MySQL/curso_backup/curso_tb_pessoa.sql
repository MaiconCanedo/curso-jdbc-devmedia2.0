-- MySQL dump 10.13  Distrib 8.0.11, for Win64 (x86_64)
--
-- Host: localhost    Database: curso
-- ------------------------------------------------------
-- Server version	8.0.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_pessoa`
--

DROP TABLE IF EXISTS `tb_pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tb_pessoa` (
  `id_pessoa` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `cpf` varchar(14) DEFAULT NULL,
  `sexo` char(1) NOT NULL DEFAULT 'M',
  `data_nascimento` date NOT NULL,
  `ativo` tinyint(1) NOT NULL DEFAULT '1',
  `id_endereco` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_pessoa`),
  KEY `fk_endereco` (`id_endereco`),
  CONSTRAINT `fk_endereco` FOREIGN KEY (`id_endereco`) REFERENCES `endereco` (`id_endereco`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_pessoa`
--

LOCK TABLES `tb_pessoa` WRITE;
/*!40000 ALTER TABLE `tb_pessoa` DISABLE KEYS */;
INSERT INTO `tb_pessoa` VALUES (1,'Maicon','123.456.789-10','M','2018-06-12',1,16),(3,'João','123.456.789-11','M','2018-06-12',1,17),(4,'Maria','123.456.789-12','F','2018-06-12',1,18),(5,'Joana','12345678913','F','2018-06-14',1,NULL),(6,'Diana','12345678914','F','2018-06-14',1,NULL),(8,'Daniel','12345678915','F','2018-06-14',1,NULL),(9,'Rafael','12345678916','M','2018-06-14',1,NULL),(10,'Leonardo','12345678917','M','2018-06-14',0,NULL),(11,'Janaina','12345678918','F','2018-06-14',0,NULL),(12,'Jorge','12345678919','M','2018-06-14',1,NULL),(14,'Samuel','12345678920','M','2018-06-14',1,NULL),(15,'Ítalo','12345678921','M','2018-06-14',1,NULL),(16,'Renata','12345678922','F','2018-06-14',1,NULL),(17,'Vanessa','12345678923','F','2018-06-14',1,NULL),(18,'Ana','12345678924','F','2018-06-14',1,NULL),(19,'Rita','12345678925','F','2018-06-14',1,NULL),(20,'Luiza','12345678926','F','2018-06-14',0,NULL),(21,'Emanuel','12345678927','M','2018-06-14',1,NULL),(22,'Joaquim','12345678928','M','2018-06-14',1,NULL),(23,'Júlia','12345678929','F','1995-06-12',0,NULL);
/*!40000 ALTER TABLE `tb_pessoa` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-29 12:29:33
