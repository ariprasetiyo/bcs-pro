-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Waktu pembuatan: 11. Juni 2014 jam 10:02
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
-- Struktur dari tabel `header_penjualan`
--

CREATE TABLE IF NOT EXISTS `header_penjualan` (
  `trans_no` varchar(20) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `alamat` varchar(200) NOT NULL,
  `no_hp` int(13) NOT NULL,
  `note` varchar(200) NOT NULL,
  `tanggal_dikirim` varchar(15) NOT NULL,
  `jenis_pesanan` varchar(20) NOT NULL,
  `jumlah_dipesan` int(3) NOT NULL,
  `dp_status` int(11) NOT NULL,
  `dp_amount` double NOT NULL,
  `bayar` double NOT NULL,
  `total_dibayar` double NOT NULL,
  `lock` int(1) NOT NULL,
  `update` date NOT NULL,
  `tanggal_transaksi` date NOT NULL,
  `period` varchar(10) NOT NULL,
  PRIMARY KEY (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
