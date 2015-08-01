-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Waktu pembuatan: 30. April 2014 jam 18:09
-- Versi Server: 5.5.16
-- Versi PHP: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `bsc_pro`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `master_barang_convert_satuan`
--

CREATE TABLE IF NOT EXISTS `master_barang_convert_satuan` (
  `no` int(11) NOT NULL,
  `item_name` varchar(200) NOT NULL,
  `qty_tdk_baku` double NOT NULL,
  `satuan_tdk_baku` varchar(100) NOT NULL,
  `qty_baku` double NOT NULL,
  `satuan_baku` varchar(100) NOT NULL,
  PRIMARY KEY (`item_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `master_barang_convert_satuan`
--

INSERT INTO `master_barang_convert_satuan` (`no`, `item_name`, `qty_tdk_baku`, `satuan_tdk_baku`, `qty_baku`, `satuan_baku`) VALUES
(2, 'JAGUNG KUNING', 3, 'Buah', 7000, 'KG'),
(1, 'JAGUNG MANIS', 4, 'Buah', 7500, 'KG'),
(3, 'KENTANG', 12, 'Buah', 8000, 'KG'),
(4, 'KOL', 2, 'Buah', 2500, 'KG');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
