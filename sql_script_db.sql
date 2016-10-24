-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema sd_2016_17
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sd_2016_17
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sd_2016_17` DEFAULT CHARACTER SET utf8 ;
USE `sd_2016_17` ;

-- -----------------------------------------------------
-- Table `sd_2016_17`.`USER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sd_2016_17`.`USER` (
  `idUSER` INT NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `online` TINYINT(1) NOT NULL,
  PRIMARY KEY (`idUSER`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sd_2016_17`.`AUCTION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sd_2016_17`.`AUCTION` (
  `idAUCTION` INT NOT NULL AUTO_INCREMENT,
  `idITEM` DOUBLE NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `description` VARCHAR(200) NOT NULL,
  `deadline` TIMESTAMP NOT NULL,
  `amount` INT NOT NULL,
  `USER_idUSER` INT NOT NULL,
  PRIMARY KEY (`idAUCTION`, `USER_idUSER`),
  INDEX `fk_AUCTION_USER_idx` (`USER_idUSER` ASC),
  CONSTRAINT `fk_AUCTION_USER`
    FOREIGN KEY (`USER_idUSER`)
    REFERENCES `sd_2016_17`.`USER` (`idUSER`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sd_2016_17`.`BID`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sd_2016_17`.`BID` (
  `idBID` INT NOT NULL AUTO_INCREMENT,
  `amount` INT NOT NULL,
  `USER_idUSER` INT NOT NULL,
  `AUCTION_idAUCTION` INT NOT NULL,
  `AUCTION_USER_idUSER` INT NOT NULL,
  PRIMARY KEY (`idBID`, `USER_idUSER`, `AUCTION_idAUCTION`, `AUCTION_USER_idUSER`),
  INDEX `fk_BID_USER1_idx` (`USER_idUSER` ASC),
  INDEX `fk_BID_AUCTION1_idx` (`AUCTION_idAUCTION` ASC, `AUCTION_USER_idUSER` ASC),
  CONSTRAINT `fk_BID_USER1`
    FOREIGN KEY (`USER_idUSER`)
    REFERENCES `sd_2016_17`.`USER` (`idUSER`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_BID_AUCTION1`
    FOREIGN KEY (`AUCTION_idAUCTION` , `AUCTION_USER_idUSER`)
    REFERENCES `sd_2016_17`.`AUCTION` (`idAUCTION` , `USER_idUSER`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sd_2016_17`.`MESSAGE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sd_2016_17`.`MESSAGE` (
  `idMESSAGE` INT NOT NULL AUTO_INCREMENT,
  `readed` TINYINT(1) NOT NULL,
  `USER_idUSER` INT NOT NULL,
  `AUCTION_idAUCTION` INT NOT NULL,
  `AUCTION_USER_idUSER` INT NOT NULL,
  PRIMARY KEY (`idMESSAGE`, `USER_idUSER`, `AUCTION_idAUCTION`, `AUCTION_USER_idUSER`),
  INDEX `fk_MESSAGE_USER1_idx` (`USER_idUSER` ASC),
  INDEX `fk_MESSAGE_AUCTION1_idx` (`AUCTION_idAUCTION` ASC, `AUCTION_USER_idUSER` ASC),
  CONSTRAINT `fk_MESSAGE_USER1`
    FOREIGN KEY (`USER_idUSER`)
    REFERENCES `sd_2016_17`.`USER` (`idUSER`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_MESSAGE_AUCTION1`
    FOREIGN KEY (`AUCTION_idAUCTION` , `AUCTION_USER_idUSER`)
    REFERENCES `sd_2016_17`.`AUCTION` (`idAUCTION` , `USER_idUSER`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
