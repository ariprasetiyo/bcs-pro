-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Waktu pembuatan: 18. Juni 2014 jam 04:31
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
-- Struktur dari tabel `header_journal_transaksi`
--

CREATE TABLE IF NOT EXISTS `header_journal_transaksi` (
  `trans_no` varchar(20) NOT NULL,
  `chart_no` int(11) NOT NULL,
  `chart_name` varchar(200) NOT NULL,
  `amount` double NOT NULL,
  `note` varchar(300) NOT NULL,
  `tanggal` varchar(50) NOT NULL,
  `period` int(6) NOT NULL,
  `updated_date` datetime NOT NULL,
  PRIMARY KEY (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_journal_transaksi`
--

INSERT INTO `header_journal_transaksi` (`trans_no`, `chart_no`, `chart_name`, `amount`, `note`, `tanggal`, `period`, `updated_date`) VALUES
('JM2014061', 8101, 'Upah Langsung', 21122, '323', '17-06-2014', 201406, '2014-06-18 08:16:26'),
('JM2014062', 8101, 'Upah Langsung', 8, 'coba', '17-06-2014', 201406, '2014-06-18 08:16:26'),
('JM2014063', 8102, 'Upah Tidak Langsung', 212212, '11', '17-06-2014', 201406, '2014-06-18 08:16:26'),
('JM2014064', 8102, 'Upah Tidak Langsung', 21, '1212', '17-06-2014', 201406, '2014-06-18 08:16:26'),
('JM2014065', 8302, 'Biaya Penyusutan Alat', 1, '212', '17-06-2014', 201406, '2014-06-18 08:16:26'),
('JM2014066', 8302, 'Biaya Penyusutan Alat', 1, '121', '17-06-2014', 201406, '2014-06-18 08:16:26'),
('JM2014067', 8101, 'Upah Langsung', 121, '222', '18-06-2014', 201406, '2014-06-18 08:16:26');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
