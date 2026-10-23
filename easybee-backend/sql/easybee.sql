-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 23, 2026 at 04:53 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `easybee_projet2`
--
CREATE DATABASE IF NOT EXISTS `easybee_projet2` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `easybee_projet2`;

-- --------------------------------------------------------

--
-- Table structure for table `bonlivraison`
--

DROP TABLE IF EXISTS `bonlivraison`;
CREATE TABLE `bonlivraison` (
  `id` int(11) NOT NULL,
  `dateLivraison` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `categoriesalarie`
--

DROP TABLE IF EXISTS `categoriesalarie`;
CREATE TABLE `categoriesalarie` (
  `id` int(11) NOT NULL,
  `nomCat` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `categoriesalarie`
--

INSERT INTO `categoriesalarie` (`id`, `nomCat`) VALUES
(1, 'vendeur'),
(2, 'preparateur');

-- --------------------------------------------------------

--
-- Table structure for table `cmdeapprodepot`
--

DROP TABLE IF EXISTS `cmdeapprodepot`;
CREATE TABLE `cmdeapprodepot` (
  `id` int(11) NOT NULL,
  `dateCommande` datetime NOT NULL DEFAULT current_timestamp(),
  `statutCommande` varchar(50) NOT NULL DEFAULT 'en attente',
  `idCatSalarie` int(11) NOT NULL,
  `nomCommande` varchar(255) NOT NULL,
  `quantite` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `detailcmd`
--

DROP TABLE IF EXISTS `detailcmd`;
CREATE TABLE `detailcmd` (
  `idProduit` int(11) NOT NULL,
  `idBonLivraison` int(11) NOT NULL,
  `qtePrepa` int(11) NOT NULL,
  `qteRecu` int(11) NOT NULL DEFAULT 0,
  `idCmdeApproDepot` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `detailproduit`
--

DROP TABLE IF EXISTS `detailproduit`;
CREATE TABLE `detailproduit` (
  `idProduit` int(11) NOT NULL,
  `idCmdeApproDepot` int(11) NOT NULL,
  `qteCmde` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `produit`
--

DROP TABLE IF EXISTS `produit`;
CREATE TABLE `produit` (
  `id` int(11) NOT NULL,
  `codeProduit` int(11) NOT NULL,
  `stockMag` int(11) NOT NULL,
  `stockMiniMag` int(11) NOT NULL,
  `designationProduit` varchar(255) NOT NULL DEFAULT '',
  `prixPdt` float NOT NULL,
  `stockEntrepot` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `produit`
--

INSERT INTO `produit` (`id`, `codeProduit`, `stockMag`, `stockMiniMag`, `designationProduit`, `prixPdt`, `stockEntrepot`) VALUES
(24, 1001, 15, 20, 'Pot en verre 250g', 0.5, 100),
(25, 1002, 40, 20, 'Pot en verre 500g', 0.6, 80),
(26, 1003, 10, 30, 'Couvercle doré pour pot', 0.1, 200),
(27, 1004, 200, 50, 'Étiquettes personnalisables', 0.15, 500),
(28, 1005, 10, 2, 'Ruche Dadant 10 cadres', 85, 15),
(29, 1006, 5, 1, 'Ruche Dadant 20 cadres', 110, 10),
(30, 1007, 15, 20, 'Cadres montés avec fil', 1.2, 100),
(31, 1008, 70, 30, 'Feuilles de cire gaufrée', 0.9, 120),
(32, 1009, 15, 5, 'Enfumoir inox', 25, 30),
(33, 1010, 20, 5, 'Lève-cadres', 8, 40),
(34, 1011, 25, 10, 'Brosse à abeilles', 4.5, 50),
(35, 1012, 4, 2, 'Extracteur manuel 4 cadres', 190, 6),
(36, 1013, 10, 3, 'Couteau à désoperculer', 15, 15),
(37, 1014, 20, 5, 'Seau alimentaire 10L', 6, 30),
(38, 1015, 25, 10, 'Nourrissement sirop', 12, 40),
(39, 1016, 30, 10, 'Pâte candi', 8, 50),
(40, 1017, 10, 5, 'Traitement Varroa Apivar', 18, 20),
(41, 1018, 12, 3, 'Blouson apiculteur', 35, 20),
(42, 1019, 18, 5, 'Gants cuir + toile', 12, 30),
(43, 1020, 15, 5, 'Chapeau avec voile', 18, 25);

-- --------------------------------------------------------

--
-- Table structure for table `salarie`
--

DROP TABLE IF EXISTS `salarie`;
CREATE TABLE `salarie` (
  `id` int(11) NOT NULL,
  `matriculeSalarie` int(11) NOT NULL,
  `nomSalarie` varchar(255) NOT NULL DEFAULT '',
  `prenomSalarie` varchar(255) NOT NULL DEFAULT '',
  `idCat` int(11) NOT NULL,
  `identifiant` varchar(255) NOT NULL DEFAULT '',
  `motDePasse` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bonlivraison`
--
ALTER TABLE `bonlivraison`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `categoriesalarie`
--
ALTER TABLE `categoriesalarie`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `cmdeapprodepot`
--
ALTER TABLE `cmdeapprodepot`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idCat` (`idCatSalarie`);

--
-- Indexes for table `detailcmd`
--
ALTER TABLE `detailcmd`
  ADD PRIMARY KEY (`idProduit`,`idBonLivraison`),
  ADD KEY `idBonLivraison` (`idBonLivraison`),
  ADD KEY `idCmdeApproDepot` (`idCmdeApproDepot`);

--
-- Indexes for table `detailproduit`
--
ALTER TABLE `detailproduit`
  ADD PRIMARY KEY (`idProduit`,`idCmdeApproDepot`),
  ADD KEY `idCmdeApproDepot` (`idCmdeApproDepot`);

--
-- Indexes for table `produit`
--
ALTER TABLE `produit`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `salarie`
--
ALTER TABLE `salarie`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idCat` (`idCat`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bonlivraison`
--
ALTER TABLE `bonlivraison`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `categoriesalarie`
--
ALTER TABLE `categoriesalarie`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `cmdeapprodepot`
--
ALTER TABLE `cmdeapprodepot`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `produit`
--
ALTER TABLE `produit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `salarie`
--
ALTER TABLE `salarie`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cmdeapprodepot`
--
ALTER TABLE `cmdeapprodepot`
  ADD CONSTRAINT `cmdeapprodepot_ibfk_1` FOREIGN KEY (`idCatSalarie`) REFERENCES `categoriesalarie` (`id`);

--
-- Constraints for table `detailcmd`
--
ALTER TABLE `detailcmd`
  ADD CONSTRAINT `detailcmd_ibfk_1` FOREIGN KEY (`idProduit`) REFERENCES `produit` (`id`),
  ADD CONSTRAINT `detailcmd_ibfk_2` FOREIGN KEY (`idBonLivraison`) REFERENCES `bonlivraison` (`id`),
  ADD CONSTRAINT `detailcmd_ibfk_3` FOREIGN KEY (`idCmdeApproDepot`) REFERENCES `cmdeapprodepot` (`id`);

--
-- Constraints for table `detailproduit`
--
ALTER TABLE `detailproduit`
  ADD CONSTRAINT `detailproduit_ibfk_1` FOREIGN KEY (`idProduit`) REFERENCES `produit` (`id`),
  ADD CONSTRAINT `detailproduit_ibfk_2` FOREIGN KEY (`idCmdeApproDepot`) REFERENCES `cmdeapprodepot` (`id`);

--
-- Constraints for table `salarie`
--
ALTER TABLE `salarie`
  ADD CONSTRAINT `salarie_ibfk_1` FOREIGN KEY (`idCat`) REFERENCES `categoriesalarie` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
