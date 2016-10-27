SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `chulospizza` ;
CREATE SCHEMA IF NOT EXISTS `chulospizza` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `chulospizza` ;

-- -----------------------------------------------------
-- Table `chulospizza`.`ingredientes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chulospizza`.`ingredientes` (
  `idingredientes` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `precio` VARCHAR(45) NOT NULL,
  `stock` INT NOT NULL,
  PRIMARY KEY (`idingredientes`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chulospizza`.`puestos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chulospizza`.`puestos` (
  `idpuestos` INT NOT NULL AUTO_INCREMENT,
  `puesto` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idpuestos`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chulospizza`.`Empleados`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chulospizza`.`Empleados` (
  `idEmpleados` INT NOT NULL AUTO_INCREMENT,
  `nombres` VARCHAR(45) NOT NULL,
  `apellidos` VARCHAR(45) NOT NULL,
  `calle` VARCHAR(45) NULL,
  `colonia` VARCHAR(45) NULL,
  `numero` VARCHAR(45) NULL,
  `telefono` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `puestos_idpuestos` INT NOT NULL,
  PRIMARY KEY (`idEmpleados`),
  INDEX `fk_Empleados_puestos_idx` (`puestos_idpuestos` ASC),
  CONSTRAINT `fk_Empleados_puestos`
    FOREIGN KEY (`puestos_idpuestos`)
    REFERENCES `chulospizza`.`puestos` (`idpuestos`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chulospizza`.`clientes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chulospizza`.`clientes` (
  `idclientes` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  `direccion` VARCHAR(45) NULL,
  `ubicaciongps` VARCHAR(45) NULL,
  `clientescol` VARCHAR(45) NULL,
  PRIMARY KEY (`idclientes`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chulospizza`.`pizzas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chulospizza`.`pizzas` (
  `idpizzas` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  `precio` DOUBLE NULL,
  `foto` BLOB NULL,
  PRIMARY KEY (`idpizzas`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chulospizza`.`detalleventas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chulospizza`.`detalleventas` (
  `iddetalleventas` INT NOT NULL AUTO_INCREMENT,
  `pizzas_idpizzas` INT NOT NULL,
  PRIMARY KEY (`iddetalleventas`),
  INDEX `fk_detalleventas_pizzas1_idx` (`pizzas_idpizzas` ASC),
  CONSTRAINT `fk_detalleventas_pizzas1`
    FOREIGN KEY (`pizzas_idpizzas`)
    REFERENCES `chulospizza`.`pizzas` (`idpizzas`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chulospizza`.`pedidos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chulospizza`.`pedidos` (
  `idpedidos` INT NOT NULL AUTO_INCREMENT,
  `Empleados_idEmpleados` INT NOT NULL,
  `clientes_idclientes` INT NOT NULL,
  `cantidad` INT NULL,
  `total` DOUBLE NULL,
  `iva` DOUBLE NULL,
  `totalneto` DOUBLE NULL,
  `estado` INT NULL DEFAULT 0,
  `detalleventas_iddetalleventas` INT NOT NULL,
  PRIMARY KEY (`idpedidos`),
  INDEX `fk_pedidos_Empleados1_idx` (`Empleados_idEmpleados` ASC),
  INDEX `fk_pedidos_clientes1_idx` (`clientes_idclientes` ASC),
  INDEX `fk_pedidos_detalleventas1_idx` (`detalleventas_iddetalleventas` ASC),
  CONSTRAINT `fk_pedidos_Empleados1`
    FOREIGN KEY (`Empleados_idEmpleados`)
    REFERENCES `chulospizza`.`Empleados` (`idEmpleados`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedidos_clientes1`
    FOREIGN KEY (`clientes_idclientes`)
    REFERENCES `chulospizza`.`clientes` (`idclientes`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedidos_detalleventas1`
    FOREIGN KEY (`detalleventas_iddetalleventas`)
    REFERENCES `chulospizza`.`detalleventas` (`iddetalleventas`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chulospizza`.`combinaciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chulospizza`.`combinaciones` (
  `idcombinaciones` INT NOT NULL AUTO_INCREMENT,
  `ingredientes_idingredientes` INT NOT NULL,
  `pizzas_idpizzas` INT NOT NULL,
  PRIMARY KEY (`idcombinaciones`),
  INDEX `fk_combinaciones_ingredientes1_idx` (`ingredientes_idingredientes` ASC),
  INDEX `fk_combinaciones_pizzas1_idx` (`pizzas_idpizzas` ASC),
  CONSTRAINT `fk_combinaciones_ingredientes1`
    FOREIGN KEY (`ingredientes_idingredientes`)
    REFERENCES `chulospizza`.`ingredientes` (`idingredientes`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_combinaciones_pizzas1`
    FOREIGN KEY (`pizzas_idpizzas`)
    REFERENCES `chulospizza`.`pizzas` (`idpizzas`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
