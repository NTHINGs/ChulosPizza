CREATE DATABASE  IF NOT EXISTS `chulospizza` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `chulospizza`;
-- MySQL dump 10.13  Distrib 5.6.24, for debian-linux-gnu (x86_64)
--
-- Host: db4free.net    Database: chulospizza
-- ------------------------------------------------------
-- Server version	5.6.24

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
-- Table structure for table `Empleados`
--

DROP TABLE IF EXISTS `Empleados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Empleados` (
  `idEmpleados` int(11) NOT NULL AUTO_INCREMENT,
  `nombres` varchar(45) NOT NULL,
  `apellidos` varchar(45) NOT NULL,
  `calle` varchar(45) DEFAULT NULL,
  `colonia` varchar(45) DEFAULT NULL,
  `numero` varchar(45) DEFAULT NULL,
  `telefono` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `usuario` varchar(45) NOT NULL,
  `pass` varchar(45) NOT NULL,
  `puestos_idpuestos` int(11) NOT NULL,
  PRIMARY KEY (`idEmpleados`),
  KEY `fk_Empleados_puestos_idx` (`puestos_idpuestos`),
  CONSTRAINT `fk_Empleados_puestos` FOREIGN KEY (`puestos_idpuestos`) REFERENCES `puestos` (`idpuestos`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Empleados`
--

LOCK TABLES `Empleados` WRITE;
/*!40000 ALTER TABLE `Empleados` DISABLE KEYS */;
INSERT INTO `Empleados` VALUES (1,'Misael','Hernandez Villarreal','Mostros','Pastes','89','6181234563','misa@chulospizza.com','misa','misa',3),(2,'Juan Luis','Olguin Díaz','Abasolo','Rodeo','192','6181674829','pelos@chulospizza.com','olguin','olguin',5),(3,'Francisco Félix','Terrazas Zaffa','General','Centro','100','6181009823','paco@chulospizza.com','paco','paco',4),(4,'Edgar Ricardo','Esquivel Pantoja','Cuyo','Paloma','666','6181345498','triste@chulospizza','triste','triste',1),(5,'Manuel Alejandro','Ceniceros Mercado','Penca','Morga','90','6181049384','menis@chulospizza.com','meni','meni',2),(6,'Jaime','Alvidrez Arzola','Rev. Inst.','Cantores','3','6181435612','chawer@chulospizza.com','jaime','jaime',1);
/*!40000 ALTER TABLE `Empleados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clientes` (
  `idclientes` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `direccion` varchar(45) DEFAULT NULL,
  `ubicaciongps` varchar(45) DEFAULT NULL,
  `clientescol` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idclientes`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `combinaciones`
--

DROP TABLE IF EXISTS `combinaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `combinaciones` (
  `idcombinaciones` int(11) NOT NULL AUTO_INCREMENT,
  `ingredientes_idingredientes` int(11) NOT NULL,
  `pizzas_idpizzas` int(11) NOT NULL,
  PRIMARY KEY (`idcombinaciones`),
  KEY `fk_combinaciones_ingredientes1_idx` (`ingredientes_idingredientes`),
  KEY `fk_combinaciones_pizzas1_idx` (`pizzas_idpizzas`),
  CONSTRAINT `fk_combinaciones_ingredientes1` FOREIGN KEY (`ingredientes_idingredientes`) REFERENCES `ingredientes` (`idingredientes`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_combinaciones_pizzas1` FOREIGN KEY (`pizzas_idpizzas`) REFERENCES `pizzas` (`idpizzas`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `combinaciones`
--

LOCK TABLES `combinaciones` WRITE;
/*!40000 ALTER TABLE `combinaciones` DISABLE KEYS */;
INSERT INTO `combinaciones` VALUES (1,1,1),(2,2,1),(3,1,2),(4,3,2),(5,4,2),(6,5,3),(7,6,3),(8,1,4),(9,8,4),(10,1,5),(11,5,5);
/*!40000 ALTER TABLE `combinaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalleventas`
--

DROP TABLE IF EXISTS `detalleventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detalleventas` (
  `iddetalleventas` int(11) NOT NULL AUTO_INCREMENT,
  `pizzas_idpizzas` int(11) NOT NULL,
  PRIMARY KEY (`iddetalleventas`),
  KEY `fk_detalleventas_pizzas1_idx` (`pizzas_idpizzas`),
  CONSTRAINT `fk_detalleventas_pizzas1` FOREIGN KEY (`pizzas_idpizzas`) REFERENCES `pizzas` (`idpizzas`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalleventas`
--

LOCK TABLES `detalleventas` WRITE;
/*!40000 ALTER TABLE `detalleventas` DISABLE KEYS */;
/*!40000 ALTER TABLE `detalleventas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredientes`
--

DROP TABLE IF EXISTS `ingredientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ingredientes` (
  `idingredientes` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `stock` int(11) NOT NULL,
  PRIMARY KEY (`idingredientes`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredientes`
--

LOCK TABLES `ingredientes` WRITE;
/*!40000 ALTER TABLE `ingredientes` DISABLE KEYS */;
INSERT INTO `ingredientes` VALUES (1,'Jamón',50),(2,'Salami',50),(3,'Carne de Res',50),(4,'Carne de Cerdo',50),(5,'Champiñones',50),(6,'Peperonni de soya',50),(7,'Jamón',50),(8,'Peperonni',50);
/*!40000 ALTER TABLE `ingredientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pedidos` (
  `idpedidos` int(11) NOT NULL AUTO_INCREMENT,
  `Empleados_idEmpleados` int(11) NOT NULL,
  `clientes_idclientes` int(11) NOT NULL,
  `cantidad` int(11) DEFAULT NULL,
  `total` double DEFAULT NULL,
  `iva` double DEFAULT NULL,
  `totalneto` double DEFAULT NULL,
  `estado` int(11) DEFAULT '0',
  `detalleventas_iddetalleventas` int(11) NOT NULL,
  PRIMARY KEY (`idpedidos`),
  KEY `fk_pedidos_Empleados1_idx` (`Empleados_idEmpleados`),
  KEY `fk_pedidos_clientes1_idx` (`clientes_idclientes`),
  KEY `fk_pedidos_detalleventas1_idx` (`detalleventas_iddetalleventas`),
  CONSTRAINT `fk_pedidos_Empleados1` FOREIGN KEY (`Empleados_idEmpleados`) REFERENCES `Empleados` (`idEmpleados`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedidos_clientes1` FOREIGN KEY (`clientes_idclientes`) REFERENCES `clientes` (`idclientes`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedidos_detalleventas1` FOREIGN KEY (`detalleventas_iddetalleventas`) REFERENCES `detalleventas` (`iddetalleventas`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pizzas`
--

DROP TABLE IF EXISTS `pizzas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pizzas` (
  `idpizzas` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `precio` double DEFAULT NULL,
  `foto` blob,
  PRIMARY KEY (`idpizzas`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pizzas`
--

LOCK TABLES `pizzas` WRITE;
/*!40000 ALTER TABLE `pizzas` DISABLE KEYS */;
INSERT INTO `pizzas` VALUES (1,'Express',89,NULL),(2,'Tres Carnes',99.5,NULL),(3,'Vegetariana',99.99,NULL),(4,'Peperonni',95,NULL),(5,'Tradicional',90,NULL);
/*!40000 ALTER TABLE `pizzas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `puestos`
--

DROP TABLE IF EXISTS `puestos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `puestos` (
  `idpuestos` int(11) NOT NULL AUTO_INCREMENT,
  `puesto` varchar(45) NOT NULL,
  PRIMARY KEY (`idpuestos`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `puestos`
--

LOCK TABLES `puestos` WRITE;
/*!40000 ALTER TABLE `puestos` DISABLE KEYS */;
INSERT INTO `puestos` VALUES (1,'Cajero'),(2,'Gerente'),(3,'Inventarista'),(4,'Repartidor'),(5,'Cocinero');
/*!40000 ALTER TABLE `puestos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-05-07 18:38:26
