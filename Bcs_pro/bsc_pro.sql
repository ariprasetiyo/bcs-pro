-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Waktu pembuatan: 02. Mei 2014 jam 07:49
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

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `doiterate`(p1 INT)
BEGIN
label1: LOOP
SET p1 = p1 - 1;
IF p1 > 0 THEN
SELECT 1;
ITERATE label1;
END IF;
LEAVE label1;
END LOOP label1;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_alokasi_waktu`
--

CREATE TABLE IF NOT EXISTS `detail_alokasi_waktu` (
  `nama_produksi` varchar(100) NOT NULL,
  `kegiatan` varchar(100) NOT NULL,
  `waktu` varchar(10) NOT NULL,
  KEY `nama_produksi` (`nama_produksi`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `detail_alokasi_waktu`
--

INSERT INTO `detail_alokasi_waktu` (`nama_produksi`, `kegiatan`, `waktu`) VALUES
('xxxxx', '2', '3:20-1'),
('xxxxx', '2', '3:20-1'),
('xxxxx', '2', '3:20-1'),
('xxxxxxxxx', 'a', '2-0'),
('xxxxxxxxx', 'b', '1-0'),
('xxxxxxxxx', 's', '1-0'),
('xxxxxxxxx', 's', '1-0'),
('xxxxxxxxx', 'f', '1-0'),
('xxxxxxxxx', 's', '1-0'),
('zzzzz', '3', '1-0'),
('zzzzz', '3', '24-1'),
('zzzzz', '3', '3-1'),
('zzzzz', 'sasa', '1-0');

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_pesanan`
--

CREATE TABLE IF NOT EXISTS `detail_pesanan` (
  `trans_no` varchar(25) NOT NULL,
  `nama_menu` varchar(100) NOT NULL,
  `qty` int(3) NOT NULL,
  KEY `trans no` (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `detail_pesanan`
--

INSERT INTO `detail_pesanan` (`trans_no`, `nama_menu`, `qty`) VALUES
('PS2014041', 'resep 1', 1),
('PS2014041', 'resep 2', 1),
('PS2014042', 'resep ari', 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_resep`
--

CREATE TABLE IF NOT EXISTS `detail_resep` (
  `namresep` varchar(100) NOT NULL,
  `no` int(11) NOT NULL,
  `bahan` varchar(100) NOT NULL,
  `qty_1_porsi` double NOT NULL,
  `satuan_1_porsi` varchar(50) NOT NULL,
  `qty` double NOT NULL,
  `satuan` varchar(100) NOT NULL,
  `created_date` varchar(100) NOT NULL,
  `update_date` datetime NOT NULL,
  KEY `namresep` (`namresep`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `detail_resep`
--

INSERT INTO `detail_resep` (`namresep`, `no`, `bahan`, `qty_1_porsi`, `satuan_1_porsi`, `qty`, `satuan`, `created_date`, `update_date`) VALUES
('bandeng presto acar kuning', 1, 'minyak goreng', 0.33, 'sdm', 2, 'sdm', '26-10-2014', '0000-00-00 00:00:00'),
('bandeng presto acar kuning', 2, 'bawang merah', 2.5, 'siung', 15, 'siung', '26-10-2014', '0000-00-00 00:00:00'),
('bandeng presto acar kuning', 3, 'bawang putih', 0.5, 'siung', 3, 'siung', '26-10-2014', '0000-00-00 00:00:00'),
('bandeng presto acar kuning', 4, 'daun salam', 0.33, 'lembar', 2, 'lembar', '26-10-2014', '0000-00-00 00:00:00'),
('bandeng presto acar kuning', 5, 'serai', 0.33, 'batang', 2, 'batang', '26-10-2014', '0000-00-00 00:00:00'),
('bandeng presto acar kuning', 6, 'jeruk purut', 1.67, 'lembar', 10, 'lembar', '26-10-2014', '0000-00-00 00:00:00'),
('bandeng presto acar kuning', 7, 'cabai rawit', 12.5, 'gram', 75, 'gram', '26-10-2014', '0000-00-00 00:00:00'),
('bandeng presto acar kuning', 8, 'bandeng ', 100, 'gram', 600, 'gram', '26-10-2014', '0000-00-00 00:00:00'),
('bandeng presto acar kuning', 9, 'garam', 0.17, 'sdt', 1, 'sdt', '26-10-2014', '0000-00-00 00:00:00'),
('bandeng presto acar kuning', 10, 'gula pasir', 0.33, 'sdm', 2, 'sdm', '26-10-2014', '0000-00-00 00:00:00'),
('bandeng presto acar kuning', 11, 'air asam jawa', 0.33, 'sdt', 2, 'sdt', '26-10-2014', '0000-00-00 00:00:00'),
('bandeng presto acar kuning', 12, 'air asam jawa', 33.33, 'ml', 200, 'ml', '26-10-2014', '0000-00-00 00:00:00'),
('bandeng presto acar kuning', 13, 'daun kemangi', 1.67, 'lembar', 10, 'lembar', '26-10-2014', '0000-00-00 00:00:00'),
('perkedel tahu', 1, 'tahu putih', 21.43, 'gram', 300, 'gram', '26-10-2014', '0000-00-00 00:00:00'),
('perkedel tahu', 2, 'telur ayam', 0.07, 'butir', 1, 'butir', '26-10-2014', '0000-00-00 00:00:00'),
('perkedel tahu', 3, 'daun bawang ', 0.07, 'batang', 1, 'batang', '26-10-2014', '0000-00-00 00:00:00'),
('perkedel tahu', 4, 'bawang putih', 0.14, 'siung', 2, 'siung', '26-10-2014', '0000-00-00 00:00:00'),
('perkedel tahu', 5, 'tepung terigu', 0.07, 'sdm', 1, 'sdm', '26-10-2014', '0000-00-00 00:00:00'),
('perkedel tahu', 6, 'merica bubuk', 0.04, 'sdt', 0.5, 'sdt', '26-10-2014', '0000-00-00 00:00:00'),
('perkedel tahu', 7, 'garam', 0.07, 'sdt', 1, 'sdt', '26-10-2014', '0000-00-00 00:00:00'),
('tahu tumis tausi', 1, 'tahu putih', 50, 'gram', 200, 'gram', '26-10-2014', '0000-00-00 00:00:00'),
('tahu tumis tausi', 2, 'minyak goreng', 50, 'ml', 200, 'ml', '26-10-2014', '0000-00-00 00:00:00'),
('tahu tumis tausi', 3, 'bawang putih', 0.5, 'siung', 2, 'siung', '26-10-2014', '0000-00-00 00:00:00'),
('tahu tumis tausi', 4, 'bawang bombay', 6.25, 'gram', 25, 'gram', '26-10-2014', '0000-00-00 00:00:00'),
('tahu tumis tausi', 5, 'udang ukuran sedang', 25, 'gram', 100, 'gram', '26-10-2014', '0000-00-00 00:00:00'),
('tahu tumis tausi', 6, 'saus tiram', 0.25, 'sdm', 1, 'sdm', '26-10-2014', '0000-00-00 00:00:00'),
('tahu tumis tausi', 7, 'kecap asin', 0.25, 'sdm', 1, 'sdm', '26-10-2014', '0000-00-00 00:00:00'),
('tahu tumis tausi', 8, 'tausi', 0.25, 'sdm', 1, 'sdm', '26-10-2014', '0000-00-00 00:00:00'),
('tahu tumis tausi', 9, 'merica bubuk', 0.13, 'sdt', 0.5, 'sdt', '26-10-2014', '0000-00-00 00:00:00'),
('tahu tumis tausi', 10, 'garam', 0.13, 'sdt', 0.5, 'sdt', '26-10-2014', '0000-00-00 00:00:00'),
('tahu tumis tausi', 11, 'air', 18.75, 'ml', 75, 'ml', '26-10-2014', '0000-00-00 00:00:00'),
('ayam goreng kalasan', 1, 'ayam', 166.67, 'gram', 500, 'gram', '26-10-2014', '0000-00-00 00:00:00'),
('ayam goreng kalasan', 2, 'daun salam', 0.67, 'lembar', 2, 'lembar', '26-10-2014', '0000-00-00 00:00:00'),
('ayam goreng kalasan', 3, 'serai', 0.33, 'batang', 1, 'batang', '26-10-2014', '0000-00-00 00:00:00'),
('ayam goreng kalasan', 4, 'tepung sagu', 0.67, 'sdm', 2, 'sdm', '26-10-2014', '0000-00-00 00:00:00'),
('ayam goreng kalasan', 5, 'baking powder', 0.17, 'sdt', 0.5, 'sdt', '26-10-2014', '0000-00-00 00:00:00'),
('ayam goreng kalasan', 6, 'garam', 0.17, 'sdm', 0.5, 'sdm', '26-10-2014', '0000-00-00 00:00:00'),
('ayam goreng kalasan', 7, 'air putih', 166.67, 'ml', 500, 'ml', '26-10-2014', '0000-00-00 00:00:00'),
('ayam goreng kalasan', 8, 'minyak goreng', 100, 'ml', 300, 'ml', '26-10-2014', '0000-00-00 00:00:00'),
('ayam goreng kalasan', 9, 'bawang putih', 2.33, 'siung', 7, 'siung', '26-10-2014', '0000-00-00 00:00:00'),
('ayam goreng kalasan', 10, 'kemiri', 2.33, 'siung', 7, 'siung', '26-10-2014', '0000-00-00 00:00:00'),
('ayam goreng kalasan', 11, 'kunyit', 0.33, 'ruas', 1, 'ruas', '26-10-2014', '0000-00-00 00:00:00'),
('ayam goreng kalasan', 12, 'ketumbar', 0.33, 'sdm', 1, 'sdm', '26-10-2014', '0000-00-00 00:00:00'),
('tempe bacem', 1, 'tempe kedelai', 0.13, 'papan/kemasan', 2, 'papan/kemasan', '26-10-2014', '2014-04-30 21:55:09'),
('tempe bacem', 2, 'air', 31.25, 'ml', 500, 'ml', '26-10-2014', '2014-04-30 21:55:09'),
('tempe bacem', 3, 'gula merah', 18.75, 'gram', 300, 'gram', '26-10-2014', '2014-04-30 21:55:09'),
('tempe bacem', 4, 'kecap manis', 0.25, 'sdm', 4, 'sdm', '26-10-2014', '2014-04-30 21:55:09'),
('tempe bacem', 5, 'lengkuas', 0.06, 'ruas', 1, 'ruas', '26-10-2014', '2014-04-30 21:55:09'),
('tempe bacem', 6, 'daun salam', 0.13, 'lembar', 2, 'lembar', '26-10-2014', '2014-04-30 21:55:09'),
('tempe bacem', 7, 'minyak goreng', 6.25, 'ml', 100, 'ml', '26-10-2014', '2014-04-30 21:55:09'),
('tempe bacem', 8, 'bawang merah', 0.38, 'siung', 6, 'siung', '26-10-2014', '2014-04-30 21:55:09'),
('tempe bacem', 9, 'bawang putih', 0.19, 'siung', 3, 'siung', '26-10-2014', '2014-04-30 21:55:09'),
('tempe bacem', 10, 'ketumbar', 0.06, 'sdt', 1, 'sdt', '26-10-2014', '2014-04-30 21:55:09'),
('tempe bacem', 11, 'kemiri', 0.25, 'butir', 4, 'butir', '26-10-2014', '2014-04-30 21:55:09'),
('tempe bacem', 12, 'jintan', 0.03, 'sdt', 0, 'sdt', '26-10-2014', '2014-04-30 21:55:09'),
('tempe bacem', 13, 'garam', 0.06, 'sdt', 1, 'sdt', '26-10-2014', '2014-04-30 21:55:09');

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_trans`
--

CREATE TABLE IF NOT EXISTS `detail_trans` (
  `no` int(11) NOT NULL,
  `trans_no` varchar(100) NOT NULL,
  `periode` int(11) NOT NULL,
  `nama_bahan` varchar(100) NOT NULL,
  `qty` double NOT NULL,
  `satuan` varchar(100) NOT NULL,
  `harga_satuan` double NOT NULL,
  `harga_tot` double NOT NULL,
  `created_date` varchar(20) NOT NULL,
  `update_date` datetime NOT NULL,
  KEY `trans_no` (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `detail_trans`
--

INSERT INTO `detail_trans` (`no`, `trans_no`, `periode`, `nama_bahan`, `qty`, `satuan`, `harga_satuan`, `harga_tot`, `created_date`, `update_date`) VALUES
(1, 'P2014048', 4, ' BROKOLI', 1.66, 'g ( Gram )', 14, 23.24, '18-04-2014', '2014-04-18 15:55:18'),
(1, 'P20140413', 4, ' BAYAM', 100, 'g ( Gram )', 2000, 2, '18-04-2014', '2014-04-18 16:46:27'),
(2, 'P20140413', 4, ' COKLAT BUBUK', 100, 'Kemasan', 10000, 10000, '18-04-2014', '2014-04-18 16:46:27'),
(3, 'P20140413', 4, ' KACANG PANJANG', 100, 'Ikat', 5000, 5000, '18-04-2014', '2014-04-18 16:46:27'),
(4, 'P20140413', 4, ' MINYAK SALAD', 1100, 'Ml ( Mili Liter )', 20000, 0, '18-04-2014', '2014-04-18 16:46:27'),
(5, 'P20140413', 4, ' MINYAK WIJEN', 100, 'L ( Litter )', 35000, 35000, '18-04-2014', '2014-04-18 16:46:27'),
(6, 'P20140413', 4, ' PEMANIS BUATAN', 100, 'Kemasan', 2000, 2000, '18-04-2014', '2014-04-18 16:46:27'),
(1, 'P20140414', 4, ' BAYAM', 100, 'g ( Gram )', 2000, 2, '18-04-2014', '2014-04-18 16:52:37'),
(2, 'P20140414', 4, ' COKLAT BUBUK', 100, 'Kemasan', 10000, 10000, '18-04-2014', '2014-04-18 16:52:37'),
(3, 'P20140414', 4, ' KACANG PANJANG', 100, 'Ikat', 5000, 5000, '18-04-2014', '2014-04-18 16:52:37'),
(4, 'P20140414', 4, ' MINYAK SALAD', 1100, 'Ml ( Mili Liter )', 20000, 0, '18-04-2014', '2014-04-18 16:52:38'),
(5, 'P20140414', 4, ' MINYAK WIJEN', 100, 'L ( Litter )', 35000, 35000, '18-04-2014', '2014-04-18 16:52:38'),
(6, 'P20140414', 4, ' PEMANIS BUATAN', 100, 'Kemasan', 2000, 2000, '18-04-2014', '2014-04-18 16:52:38'),
(1, 'P20140421', 4, ' BAYAM', 100, 'g ( Gram )', 2000, 2, '18-04-2014', '2014-04-18 21:22:28'),
(2, 'P20140421', 4, ' COKLAT BUBUK', 100, 'Kemasan', 10000, 10000, '18-04-2014', '2014-04-18 21:22:28'),
(3, 'P20140421', 4, ' KACANG PANJANG', 100, 'Ikat', 5000, 5000, '18-04-2014', '2014-04-18 21:22:28'),
(4, 'P20140421', 4, ' MINYAK SALAD', 1100, 'Ml ( Mili Liter )', 20000, 0, '18-04-2014', '2014-04-18 21:22:28'),
(5, 'P20140421', 4, ' MINYAK WIJEN', 100, 'L ( Litter )', 35000, 35000, '18-04-2014', '2014-04-18 21:22:28'),
(6, 'P20140421', 4, ' PEMANIS BUATAN', 100, 'Kemasan', 2000, 2000, '18-04-2014', '2014-04-18 21:22:28'),
(1, 'P20140423', 4, ' BAYAM', 100, 'g ( Gram )', 2000, 2, '18-04-2014', '2014-04-18 21:23:15'),
(2, 'P20140423', 4, ' COKLAT BUBUK', 100, 'Kemasan', 10000, 10000, '18-04-2014', '2014-04-18 21:23:15'),
(3, 'P20140423', 4, ' KACANG PANJANG', 100, 'Ikat', 5000, 5000, '18-04-2014', '2014-04-18 21:23:15'),
(4, 'P20140423', 4, ' MINYAK SALAD', 1100, 'Ml ( Mili Liter )', 20000, 0, '18-04-2014', '2014-04-18 21:23:16'),
(5, 'P20140423', 4, ' MINYAK WIJEN', 100, 'L ( Litter )', 35000, 35000, '18-04-2014', '2014-04-18 21:23:16'),
(6, 'P20140423', 4, ' PEMANIS BUATAN', 100, 'Kemasan', 2000, 2000, '18-04-2014', '2014-04-18 21:23:16'),
(1, 'P20140424', 4, ' BAYAM', 100, 'g ( Gram )', 2000, 2, '19-04-2014', '2014-04-19 07:22:49'),
(1, 'P20140425', 4, ' BAYAM', 100, 'g ( Gram )', 2000, 2, '19-04-2014', '2014-04-19 08:02:27'),
(1, 'P20140426', 4, ' BAYAM', 100, 'g ( Gram )', 2000, 2, '19-04-2014', '2014-04-19 08:02:37'),
(2, 'P20140426', 4, ' COKLAT BUBUK', 100, 'Kemasan', 10000, 10000, '19-04-2014', '2014-04-19 08:02:37'),
(3, 'P20140426', 4, ' KACANG PANJANG', 100, 'Ikat', 5000, 5000, '19-04-2014', '2014-04-19 08:02:37'),
(4, 'P20140426', 4, ' MINYAK SALAD', 1100, 'Ml ( Mili Liter )', 20000, 0, '19-04-2014', '2014-04-19 08:02:37'),
(5, 'P20140426', 4, ' MINYAK WIJEN', 100, 'L ( Litter )', 35000, 35000, '19-04-2014', '2014-04-19 08:02:37'),
(6, 'P20140426', 4, ' PEMANIS BUATAN', 100, 'Kemasan', 2000, 2000, '19-04-2014', '2014-04-19 08:02:37'),
(1, 'P20140427', 4, ' BROKOLI', 111, 'Kg ( Kilogram )', 14000, 1554000, '19-04-2014', '2014-04-19 08:02:54'),
(2, 'P20140427', 4, ' BUNGA TURI', 111, 'Kg ( Kilogram )', 8000, 888000, '19-04-2014', '2014-04-19 08:02:54'),
(1, 'P20140428', 4, ' BROKOLI', 1.2, 'Kg ( Kilogram )', 14000, 16800, '19-04-2014', '2014-04-19 08:03:40'),
(1, 'P20140429', 4, ' GULA PASIR', 1200, 'Kg ( Kilogram )', 11000, 132000, '24-04-2014', '2014-04-24 16:15:32');

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_trans_penerimaan`
--

CREATE TABLE IF NOT EXISTS `detail_trans_penerimaan` (
  `no` int(11) NOT NULL,
  `trans_no` varchar(100) NOT NULL,
  `periode` int(11) NOT NULL,
  `nama_bahan` varchar(100) NOT NULL,
  `qty` varchar(20) NOT NULL,
  `satuan` varchar(50) NOT NULL,
  `harga_satuan` varchar(20) NOT NULL,
  `harga_tot` varchar(20) NOT NULL,
  `harga_rill` varchar(20) NOT NULL,
  `created_date` varchar(100) NOT NULL,
  `updated_date` datetime NOT NULL,
  KEY `trans_no` (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `detail_trans_penerimaan`
--

INSERT INTO `detail_trans_penerimaan` (`no`, `trans_no`, `periode`, `nama_bahan`, `qty`, `satuan`, `harga_satuan`, `harga_tot`, `harga_rill`, `created_date`, `updated_date`) VALUES
(1, 'WR2014041', 201404, ' BAYAM', '100.00', 'g ( Gram )', '2000.00', '200000.00', '1.00', '18-04-2014', '2014-04-19 00:32:15'),
(2, 'WR2014041', 201404, ' COKLAT BUBUK', '100.00', 'Kemasan', '10000.00', '1000000.00', '1.00', '18-04-2014', '2014-04-19 00:32:15'),
(3, 'WR2014041', 201404, ' KACANG PANJANG', '100.00', 'Ikat', '5000.00', '500000.00', '1.20', '18-04-2014', '2014-04-19 00:32:15'),
(4, 'WR2014041', 201404, ' MINYAK SALAD', '1100.00', 'Ml ( Mili Liter )', '20000.00', '22000000.00', '0.00', '18-04-2014', '2014-04-19 00:32:15'),
(5, 'WR2014041', 201404, ' MINYAK WIJEN', '100.00', 'L ( Litter )', '35000.00', '3500000.00', '100010.20', '18-04-2014', '2014-04-19 00:32:15'),
(6, 'WR2014041', 201404, ' PEMANIS BUATAN', '100.00', 'Kemasan', '2000.00', '200000.00', '0.00', '18-04-2014', '2014-04-19 00:32:15'),
(1, 'WR2014042', 201404, ' BAYAM', '100.00', 'g ( Gram )', '2000.00', '200000.00', '0.00', '19-04-2014', '2014-04-19 07:27:19'),
(1, 'WR2014043', 201404, ' BROKOLI', '111.00', 'Kg ( Kilogram )', '14000.00', '1554000.00', '1110.00', '19-04-2014', '2014-04-19 08:03:17'),
(2, 'WR2014043', 201404, ' BUNGA TURI', '111.00', 'Kg ( Kilogram )', '8000.00', '888000.00', '1111.00', '19-04-2014', '2014-04-19 08:03:17'),
(1, 'WR2014044', 201404, ' BROKOLI', '1.66', 'g ( Gram )', '14.00', '23.24', '1111.00', '19-04-2014', '2014-04-19 08:04:23'),
(1, 'WR2014045', 201404, ' GULA PASIR', '1200.00', 'Kg ( Kilogram )', '11000.00', '13200000.00', '0.00', '24-04-2014', '2014-04-24 16:57:57'),
(1, 'WR2014046', 201404, ' GULA PASIR', '1200.00', 'Kg ( Kilogram )', '11000.00', '13200000.00', '0.00', '24-04-2014', '2014-04-24 17:05:29'),
(1, 'WR2014047', 201404, ' GULA PASIR', '1200.00', 'Kg ( Kilogram )', '11000.00', '13200000.00', '0.00', '24-04-2014', '2014-04-24 17:07:52'),
(1, 'WR2014048', 201404, ' GULA PASIR', '1200.00', 'Kg ( Kilogram )', '11000.00', '13200000.00', '13300000.00', '24-04-2014', '2014-04-24 17:14:07');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_alokasi_waktu`
--

CREATE TABLE IF NOT EXISTS `header_alokasi_waktu` (
  `nama_produksi` varchar(100) NOT NULL,
  `total_menit` int(11) NOT NULL,
  `total_waktu` varchar(100) NOT NULL,
  `updated_date` datetime NOT NULL,
  PRIMARY KEY (`nama_produksi`),
  KEY `updated_date` (`updated_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_alokasi_waktu`
--

INSERT INTO `header_alokasi_waktu` (`nama_produksi`, `total_menit`, `total_waktu`, `updated_date`) VALUES
('xxxxx', 0, '  0 Hari  0 Jam  0 Menit', '2014-03-17 22:49:32'),
('xxxxxxxxx', 0, '  0 Hari  0 Jam  0 Menit', '2014-03-17 22:55:00'),
('zzzzz', 1622, '  1 Hari  3 Jam  2 Menit', '2014-03-17 23:47:23');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_memasak`
--

CREATE TABLE IF NOT EXISTS `header_memasak` (
  `nama_masakan` varchar(100) NOT NULL,
  `cara_memasak` text NOT NULL,
  `created_date` datetime NOT NULL,
  KEY `nama_masakan` (`nama_masakan`),
  KEY `nama_masakan_2` (`nama_masakan`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_memasak`
--

INSERT INTO `header_memasak` (`nama_masakan`, `cara_memasak`, `created_date`) VALUES
('bandeng presto acar kuning', 'Panaskan minyak, tumis bumbu halus, bawang merah, bawang putih, daun salam, serai, dan daun jeruk hingga harum.? Masukkan cabai rawit.? Tumis hingga matang. Masukkan lokio dan bandeng. Bubuhi garam dan gula. Perciki air asam jawa. Tambahkan air, aduk rata. Biarkan hinggabumbu meresap. Masukkan kemangi, aduk rata.Angkat, sajikan.', '0000-00-00 00:00:00'),
('perkedel tahu', 'Tiriskan tahu lalu haluskan.Campur tahu dengan telur dan bumbu lain kecuali minyak hingga rata.Panaskan minyak banyak di atas api sedang. Bentuk adonan tahu menjadi bola-bola dengan bantuan 2 sendok teh. Goreng dalam minyak panas dan banyak hingga kuning kecokelatan. Angkat dan tiriskan.', '0000-00-00 00:00:00'),
('tahu tumis tausi', 'Potong-potong tahu ukuran 2x4x3/4 cm.Goreng dalam minyak banyak hingga agak kering. Tiriskan.Panaskan minyak, tumis bawang putih dan bawang Bombay hingga wangi.Masukkan udang, aduk hingga kemerahan.Tambahkan tahu dan bumbu, aduk hingga rata.Tuangi air, masak hingga mendidih dan kuah menyusut.Angkat. Sajikan ', '0000-00-00 00:00:00'),
('ayam goreng kalasan', 'Campur semua bahan dan bumbu yang sudah dihaluskan kemudian ungkep hingga air menjadi 200 ml dengan api kecil.Angkat ayam diamkan hingga dingin, lalu rebus sisa kaldu hingga mendidih. Kemudian masukkan tepung sagu yang sudah dilarutkan dengan 3 sdm air aduk hingga mengental, angkat dan dinginkan, tambahkan baking powder, aduk rata.Goreng ayam dengan minyak goreng hingga kecoklatan lalu angkat.Goreng kaldu yang sudah dikentalkan hingga menjadi keringan kemudian angkat, letakkan di atas ayam goreng. Hidangkan.', '0000-00-00 00:00:00'),
('sasa', '', '2014-04-29 21:31:43'),
('tempe bacem', 'potong-potong tempe sesuyai ukuran. haluskan bumbu-bumbu kecuali dauns salam dan lengkuas. \nmasak bumbu, \nair dan tempe hingga \nbumbu meresap dan air habis.goreng tempe dalam minyak goreng hingga kecoklatan. angkat tiriskan', '2014-04-30 21:55:09');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_pembelian`
--

CREATE TABLE IF NOT EXISTS `header_pembelian` (
  `key_no` int(11) NOT NULL,
  `trans_no` varchar(100) NOT NULL,
  `nama_resep` varchar(100) NOT NULL,
  `periode` int(11) NOT NULL,
  `supplier` varchar(100) NOT NULL,
  `total_biaya` varchar(15) NOT NULL,
  `uang_muka` varchar(20) NOT NULL,
  `created_date` varchar(20) NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_pembelian`
--

INSERT INTO `header_pembelian` (`key_no`, `trans_no`, `nama_resep`, `periode`, `supplier`, `total_biaya`, `uang_muka`, `created_date`, `update_date`) VALUES
(13, 'P20140413', 'null', 201404, 'coba dari pesanan', '52002.00', '52002.00', '18-04-2014', '2014-04-18 16:46:27'),
(14, 'P20140414', 'Dari Pesanan', 201404, 'coba resep2', '52002.00', '1000000.00', '18-04-2014', '2014-04-18 16:52:37'),
(21, 'P20140421', 'PS2014041', 201404, 'coba dari pesanan udah benar', '52002.00', '52002.00', '18-04-2014', '2014-04-18 21:22:28'),
(23, 'P20140423', 'bp ari-1', 201404, 'cobaaaaaaa', '52002.00', '1000000.00', '18-04-2014', '2014-04-18 21:23:15'),
(24, 'P20140424', 'resep 1', 201404, 'xxxx', '2.00', '3.00', '19-04-2014', '2014-04-19 07:22:49'),
(25, 'P20140425', 'resep 1', 201404, 'xxxx', '2.00', '3.00', '19-04-2014', '2014-04-19 08:02:27'),
(26, 'P20140426', 'bp ari-1', 201404, 'sss', '52002.00', '111111111.00', '19-04-2014', '2014-04-19 08:02:37'),
(27, 'P20140427', 'bp ari-1', 201404, 'yyyyyyyyyyyyyyyyyyyyyyy', '2442000.00', '1111111111.00', '19-04-2014', '2014-04-19 08:02:54'),
(28, 'P20140428', '', 201404, 'sasas', '16800.00', '11111111.00', '19-04-2014', '2014-04-19 08:03:40'),
(29, 'P20140429', 'Ari Prasetiyo-1', 201404, 'ariiiii', '132000.00', '10000000.00', '24-04-2014', '2014-04-24 16:15:32'),
(8, 'P2014048', 'null', 201404, 'coba', '2324', '1000', '18-04-2014', '2014-04-18 15:55:18');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_penerimaan`
--

CREATE TABLE IF NOT EXISTS `header_penerimaan` (
  `key_no` int(11) NOT NULL,
  `trans_no` varchar(100) NOT NULL,
  `periode` int(11) NOT NULL,
  `judul_resep` varchar(300) NOT NULL,
  `supplier` varchar(100) NOT NULL,
  `note` varchar(100) NOT NULL,
  `no_po` varchar(100) NOT NULL,
  `tanggal_po` varchar(20) NOT NULL,
  `total_biaya` double NOT NULL,
  `total_rill` double NOT NULL,
  `uang_muka` double NOT NULL,
  `uang_kembali` double NOT NULL,
  `created_date` varchar(50) NOT NULL,
  `update_date` datetime NOT NULL,
  KEY `trans_no` (`trans_no`),
  KEY `judul_resep` (`judul_resep`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_penerimaan`
--

INSERT INTO `header_penerimaan` (`key_no`, `trans_no`, `periode`, `judul_resep`, `supplier`, `note`, `no_po`, `tanggal_po`, `total_biaya`, `total_rill`, `uang_muka`, `uang_kembali`, `created_date`, `update_date`) VALUES
(1, 'WR2014041', 201404, 'bp ari-1', 'cobaaaaaaa', 'xxxx', 'P20140423', '18-04-2014', 27400000, 100013.4, 1000000, 899986.6, '18-04-2014', '2014-04-19 00:32:15'),
(2, 'WR2014042', 201404, 'resep 1', 'xxxx', '0sasas', 'P20140424', '19-04-2014', 200000, 0, 3, 3, '19-04-2014', '2014-04-19 07:27:19'),
(3, 'WR2014043', 201404, 'bp ari-1', 'yyyyyyyyyyyyyyyyyyyyyyy', 'ssss', 'P20140427', '19-04-2014', 2442000, 2221, 1111111111, 1111108890, '19-04-2014', '2014-04-19 08:03:17'),
(4, 'WR2014044', 201404, 'null', 'coba', 'sasasas', 'P2014048', '18-04-2014', 23.24, 1111, 1000, -111, '19-04-2014', '2014-04-19 08:04:23'),
(5, 'WR2014045', 201404, 'Ari Prasetiyo-1', 'ariiiii', 'coba', 'P20140429', '24-04-2014', 13200000, 0, 10000000, 10000000, '24-04-2014', '2014-04-24 16:57:57'),
(6, 'WR2014046', 201404, 'Ari Prasetiyo-1', 'ariiiii', 'lagi', 'P20140429', '24-04-2014', 13200000, 0, 10000000, 10000000, '24-04-2014', '2014-04-24 17:05:29'),
(7, 'WR2014047', 201404, 'Ari Prasetiyo-1', 'ariiiii', 'sasas', 'P20140429', '24-04-2014', 13200000, 0, 10000000, 10000000, '24-04-2014', '2014-04-24 17:07:52'),
(8, 'WR2014048', 201404, 'Ari Prasetiyo-1', 'ariiiii', 'ariiiiiiiiiiiiii', 'P20140429', '24-04-2014', 13200000, 13300000, 10000000, -3300000, '24-04-2014', '2014-04-24 17:14:07');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_pesanan`
--

CREATE TABLE IF NOT EXISTS `header_pesanan` (
  `key_no` int(11) NOT NULL,
  `trans_no` varchar(25) NOT NULL,
  `jenis_pesanan` varchar(100) NOT NULL,
  `periode` int(11) NOT NULL,
  `nama_pemesan` varchar(200) NOT NULL,
  `porsi` int(11) NOT NULL,
  `tgl_dipesan` varchar(20) NOT NULL,
  `status_dikirim` int(1) NOT NULL,
  `created_date` datetime NOT NULL,
  PRIMARY KEY (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_pesanan`
--

INSERT INTO `header_pesanan` (`key_no`, `trans_no`, `jenis_pesanan`, `periode`, `nama_pemesan`, `porsi`, `tgl_dipesan`, `status_dikirim`, `created_date`) VALUES
(1, 'PS2014041', 'LUNCH BOX', 201404, 'bp ari', 10, '10-04-2014', 0, '2014-04-10 22:31:19'),
(2, 'PS2014042', 'LUNCH BOX', 201404, 'Ari Prasetiyo', 10, '24-04-2014', 0, '2014-04-24 16:10:16');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_resep`
--

CREATE TABLE IF NOT EXISTS `header_resep` (
  `nama_resep` varchar(100) NOT NULL,
  `porsi_resep` int(11) NOT NULL,
  `created_date` varchar(50) NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`nama_resep`),
  KEY `created_date` (`created_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_resep`
--

INSERT INTO `header_resep` (`nama_resep`, `porsi_resep`, `created_date`, `update_date`) VALUES
('ayam goreng kalasan', 3, '26-10-2014', '0000-00-00 00:00:00'),
('bandeng presto acar kuning', 6, '26-10-2014', '0000-00-00 00:00:00'),
('perkedel tahu', 14, '26-10-2014', '0000-00-00 00:00:00'),
('sasa', 10, '29-04-2014', '2014-04-29 21:31:43'),
('tahu tumis tausi', 4, '26-10-2014', '0000-00-00 00:00:00'),
('tempe bacem', 16, '26-10-2014', '2014-04-30 21:55:09');

-- --------------------------------------------------------

--
-- Struktur dari tabel `master_barang`
--

CREATE TABLE IF NOT EXISTS `master_barang` (
  `no` int(11) NOT NULL,
  `item_groups` varchar(100) NOT NULL,
  `item_name` varchar(200) NOT NULL,
  `satuan` varchar(50) NOT NULL,
  `harga` varchar(20) NOT NULL,
  `created_date` datetime NOT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `master_barang`
--

INSERT INTO `master_barang` (`no`, `item_groups`, `item_name`, `satuan`, `harga`, `created_date`) VALUES
(1, 'BAHAN POKOK', 'BERAS ', 'KG', '8500.00', '2014-04-30 22:56:05'),
(2, 'BAHAN POKOK', 'BERAS MERAH', 'KG', '12000.00', '2014-04-30 22:56:05'),
(3, 'BAHAN POKOK', 'KETAN', 'KG', '12000.00', '2014-04-30 22:56:05'),
(4, 'BAHAN POKOK', 'KETELA SINGKONG', 'KG', '2500.00', '2014-04-30 22:56:05'),
(5, 'BAHAN POKOK', 'UBI', 'KG', '4000.00', '2014-04-30 22:56:05'),
(6, 'BAHAN POKOK', 'JAGUNG', 'KG', '6000.00', '2014-04-30 22:56:05'),
(7, 'SAYURAN ', 'BAYAM', 'IKAT', '2000.00', '2014-04-30 22:56:05'),
(8, 'SAYURAN ', 'BROKOLI', 'KG', '14000.00', '2014-04-30 22:56:05'),
(9, 'SAYURAN ', 'BUNCIS', 'KG', '8000.00', '2014-04-30 22:56:05'),
(10, 'SAYURAN ', 'BUNGA KOL', 'KG', '10000.00', '2014-04-30 22:56:05'),
(11, 'SAYURAN ', 'BUNGA TURI', 'KG', '8000.00', '2014-04-30 22:56:05'),
(12, 'SAYURAN ', 'DAUN PEPAYA', 'IKAT', '2000.00', '2014-04-30 22:56:05'),
(13, 'SAYURAN ', 'DAUN SINGKONG', 'IKAT', '1000.00', '2014-04-30 22:56:05'),
(14, 'SAYURAN ', 'DAUN SO', 'IKAT', '500.00', '2014-04-30 22:56:05'),
(15, 'SAYURAN ', 'GAMBAS/OYONG', 'KG', '5000.00', '2014-04-30 22:56:05'),
(16, 'SAYURAN ', 'JAGUNG MANIS', 'KG', '7500.00', '2014-04-30 22:56:05'),
(17, 'SAYURAN ', 'JAGUNG KUNING', 'KG', '7000.00', '2014-04-30 22:56:05'),
(18, 'SAYURAN ', 'JAMUR KUPING ', 'KG', '25000.00', '2014-04-30 22:56:05'),
(19, 'SAYURAN ', 'JAMUR SITAKE', 'KG', '30000.00', '2014-04-30 22:56:05'),
(20, 'SAYURAN ', 'JAMUR TIRAM', 'KG', '20000.00', '2014-04-30 22:56:05'),
(21, 'SAYURAN ', 'KACANG KARA', 'KG', '10000.00', '2014-04-30 22:56:05'),
(22, 'SAYURAN ', 'KACANG PANJANG', 'IKAT', '5000.00', '2014-04-30 22:56:06'),
(23, 'SAYURAN ', 'KANGKUNG', 'IKAT', '2000.00', '2014-04-30 22:56:06'),
(24, 'SAYURAN ', 'KECIPIR', 'IKAT', '2000.00', '2014-04-30 22:56:06'),
(25, 'SAYURAN ', 'KENINGKIR', 'IKAT', '1000.00', '2014-04-30 22:56:06'),
(26, 'SAYURAN ', 'KENTANG', 'KG', '8000.00', '2014-04-30 22:56:06'),
(27, 'SAYURAN ', 'KOL', 'KG', '2500.00', '2014-04-30 22:56:06'),
(28, 'SAYURAN ', 'LABU KUNING', 'KG', '3000.00', '2014-04-30 22:56:06'),
(29, 'SAYURAN ', 'LABU SIAM', 'KG', '3000.00', '2014-04-30 22:56:06'),
(30, 'SAYURAN ', 'LOBAK', 'KG', '4000.00', '2014-04-30 22:56:06'),
(31, 'SAYURAN ', 'PAPRIKA', 'KG', '25000.00', '2014-04-30 22:56:06'),
(32, 'SAYURAN ', 'PARE', 'KG', '5000.00', '2014-04-30 22:56:06'),
(33, 'SAYURAN ', 'SAWI HIJAU', 'IKAT', '2000.00', '2014-04-30 22:56:06'),
(34, 'SAYURAN ', 'SAWI PUTIH', 'IKAT', '3000.00', '2014-04-30 22:56:06'),
(35, 'SAYURAN ', 'SELADA', 'IKAT', '3000.00', '2014-04-30 22:56:06'),
(36, 'SAYURAN ', 'SELEDRI', 'IKAT', '2000.00', '2014-04-30 22:56:06'),
(37, 'SAYURAN ', 'DAUN SINGKONG', 'KG', '2500.00', '2014-04-30 22:56:06'),
(38, 'SAYURAN ', 'TAUGE', 'KG', '10000.00', '2014-04-30 22:56:06'),
(39, 'SAYURAN ', 'TERONG', 'KG', '14000.00', '2014-04-30 22:56:06'),
(40, 'SAYURAN ', 'TOMAT', 'KG', '12000.00', '2014-04-30 22:56:06'),
(41, 'SAYURAN ', 'WORTEL', 'KG', '12000.00', '2014-04-30 22:56:06'),
(42, 'BUAH-BUAHAN', 'ALPUKAT', 'KG', '14000.00', '2014-04-30 22:56:06'),
(43, 'BUAH-BUAHAN', 'ANGGUR', 'KG', '30000.00', '2014-04-30 22:56:06'),
(44, 'BUAH-BUAHAN', 'APEL FUJI', 'KG', '25000.00', '2014-04-30 22:56:06'),
(45, 'BUAH-BUAHAN', 'APEL MALANG', 'KG', '13000.00', '2014-04-30 22:56:06'),
(46, 'BUAH-BUAHAN', 'BELIMBING', 'KG', '12000.00', '2014-04-30 22:56:06'),
(47, 'BUAH-BUAHAN', 'BENGKUANG', 'KG', '7000.00', '2014-04-30 22:56:06'),
(48, 'BUAH-BUAHAN', 'BIT', 'KG', '25000.00', '2014-04-30 22:56:06'),
(49, 'BUAH-BUAHAN', 'CERRY', 'KG', '30000.00', '2014-04-30 22:56:06'),
(50, 'BUAH-BUAHAN', 'DELIMA', 'KG', '12000.00', '2014-04-30 22:56:06'),
(51, 'BUAH-BUAHAN', 'DONDONG', 'KG', '6000.00', '2014-04-30 22:56:06'),
(52, 'BUAH-BUAHAN', 'DUKU', 'KG', '7000.00', '2014-04-30 22:56:06'),
(53, 'BUAH-BUAHAN', 'DURIAN', 'BUAH', '40000.00', '2014-04-30 22:56:06'),
(54, 'BUAH-BUAHAN', 'JAMBU AIR', 'KG', '7000.00', '2014-04-30 22:56:06'),
(55, 'BUAH-BUAHAN', 'JAMBU BIJI', 'KG', '5000.00', '2014-04-30 22:56:06'),
(56, 'BUAH-BUAHAN', 'JERUK SUNKIS', 'KG', '15000.00', '2014-04-30 22:56:06'),
(57, 'BUAH-BUAHAN', 'JERUK LEMON', 'KG', '20000.00', '2014-04-30 22:56:06'),
(58, 'BUAH-BUAHAN', 'JERUK LOKAL', 'KG', '17000.00', '2014-04-30 22:56:06'),
(59, 'BUAH-BUAHAN', 'KELAPA', 'BUAH', '4000.00', '2014-04-30 22:56:06'),
(60, 'BUAH-BUAHAN', 'KELENGKENG', 'KG', '14000.00', '2014-04-30 22:56:06'),
(61, 'BUAH-BUAHAN', 'KIWI', 'KG', '25000.00', '2014-04-30 22:56:06'),
(62, 'BUAH-BUAHAN', 'LECI', 'KG', '30000.00', '2014-04-30 22:56:06'),
(63, 'BUAH-BUAHAN', 'MANGGA', 'KG', '15000.00', '2014-04-30 22:56:06'),
(64, 'BUAH-BUAHAN', 'MANGGIS', 'KG', '14000.00', '2014-04-30 22:56:06'),
(65, 'BUAH-BUAHAN', 'MARKISA', 'KG', '6000.00', '2014-04-30 22:56:06'),
(66, 'BUAH-BUAHAN', 'MELON', 'KG', '5000.00', '2014-04-30 22:56:06'),
(67, 'BUAH-BUAHAN', 'MENTIMUN', 'KG', '10000.00', '2014-04-30 22:56:06'),
(68, 'BUAH-BUAHAN', 'NANAS', 'BUAH', '4000.00', '2014-04-30 22:56:06'),
(69, 'BUAH-BUAHAN', 'NANGKA KUPAS', 'KG', '20000.00', '2014-04-30 22:56:06'),
(70, 'BUAH-BUAHAN', 'PEPAYA', 'KG', '3000.00', '2014-04-30 22:56:06'),
(71, 'BUAH-BUAHAN', 'PERSIK', 'KG', '20000.00', '2014-04-30 22:56:07'),
(72, 'BUAH-BUAHAN', 'RAMBUTAN', 'KG', '3500.00', '2014-04-30 22:56:07'),
(73, 'BUAH-BUAHAN', 'SAWO', 'KG', '7000.00', '2014-04-30 22:56:07'),
(74, 'BUAH-BUAHAN', 'SEMANGKA', 'KG', '5000.00', '2014-04-30 22:56:07'),
(75, 'BUAH-BUAHAN', 'SIRSAK', 'KG', '8000.00', '2014-04-30 22:56:07'),
(76, 'BUAH-BUAHAN', 'STRAWBERRY', 'KG', '22000.00', '2014-04-30 22:56:07'),
(77, 'KACANG-KACANGAN', 'KACANG TANAH', 'KG', '20000.00', '2014-04-30 22:56:07'),
(78, 'KACANG-KACANGAN', 'KACANG KEDELAI', 'KG', '13000.00', '2014-04-30 22:56:07'),
(79, 'KACANG-KACANGAN', 'KACANG METE', 'KG', '35000.00', '2014-04-30 22:56:07'),
(80, 'KACANG-KACANGAN', 'KACANG HIJAU', 'KG', '12000.00', '2014-04-30 22:56:07'),
(81, 'KACANG-KACANGAN', 'KACANG MERAH', 'KG', '14000.00', '2014-04-30 22:56:07'),
(82, 'KACANG-KACANGAN', 'KACANG TOLO', 'KG', '12000.00', '2014-04-30 22:56:07'),
(83, 'KACANG-KACANGAN', 'KACANG KORO', 'KG', '14000.00', '2014-04-30 22:56:07'),
(84, 'KACANG-KACANGAN', 'KACANG KAPRI', 'KG', '20000.00', '2014-04-30 22:56:07'),
(85, 'BUMBU', 'BAWANG BOMBAY', 'KG', '22000.00', '2014-04-30 22:56:07'),
(86, 'BUMBU', 'BAWANG MERAH', 'KG', '18000.00', '2014-04-30 22:56:07'),
(87, 'BUMBU', 'BAWANG PUTIH', 'KG', '14000.00', '2014-04-30 22:56:07'),
(88, 'BUMBU', 'BAY LEAF', 'LEMBAR', '200.00', '2014-04-30 22:56:07'),
(89, 'BUMBU', 'BIJI PALA', 'BIJI', '1000.00', '2014-04-30 22:56:07'),
(90, 'BUMBU', 'BUNGA KECOMBRANG', 'BIJI', '1000.00', '2014-04-30 22:56:07'),
(91, 'BUMBU', 'BUNGA PEKAK', 'BIJI', '200.00', '2014-04-30 22:56:07'),
(92, 'BUMBU', 'CABAI HIJAU RAWIT', 'KG', '20000.00', '2014-04-30 22:56:07'),
(93, 'BUMBU', 'CABAI HIJAU TEOPONG', 'KG', '18000.00', '2014-04-30 22:56:07'),
(94, 'BUMBU', 'CABAI MERAH KERITING ', 'KG', '12000.00', '2014-04-30 22:56:07'),
(95, 'BUMBU', 'CABAI MERAH TEROPONG', 'KG', '19000.00', '2014-04-30 22:56:07'),
(96, 'BUMBU', 'CABAI RAWIT SETAN', 'KG', '35000.00', '2014-04-30 22:56:07'),
(97, 'BUMBU', 'CENGKEH', 'KG', '120000.00', '2014-04-30 22:56:07'),
(98, 'BUMBU', 'DAUN JERUK PURUT', 'LEMBAR', '50.00', '2014-04-30 22:56:07'),
(99, 'BUMBU', 'DAUN KARI', 'LEMBAR', '50.00', '2014-04-30 22:56:07'),
(100, 'BUMBU', 'DAUN SALAM', 'LEMBAR', '50.00', '2014-04-30 22:56:07'),
(101, 'BUMBU', 'JAHE', 'KG', '15000.00', '2014-04-30 22:56:07'),
(102, 'BUMBU', 'JERUK NIPIS', 'KG', '15000.00', '2014-04-30 22:56:07'),
(103, 'BUMBU', 'JERUK PURUT', 'KG', '23000.00', '2014-04-30 22:56:07'),
(104, 'BUMBU', 'JINTEN', 'KG', '30000.00', '2014-04-30 22:56:07'),
(105, 'BUMBU', 'KAPULAGA', 'KG', '70000.00', '2014-04-30 22:56:07'),
(106, 'BUMBU', 'KAYU MANIS', 'KG', '28000.00', '2014-04-30 22:56:07'),
(107, 'BUMBU', 'KENCUR', 'KG', '25000.00', '2014-04-30 22:56:07'),
(108, 'BUMBU', 'KETUMBAR', 'KG', '50000.00', '2014-04-30 22:56:07'),
(109, 'BUMBU', 'KUNCI', 'KG', '24000.00', '2014-04-30 22:56:07'),
(110, 'BUMBU', 'LENGKUAS', 'KG', '5000.00', '2014-04-30 22:56:07'),
(111, 'BUMBU', 'MERICA BUTIR', 'KG', '75000.00', '2014-04-30 22:56:07'),
(112, 'BUMBU', 'MERICA BUBUK', 'KG', '100000.00', '2014-04-30 22:56:07'),
(113, 'BUMBU', 'ROSEMARY', 'KG', '90000.00', '2014-04-30 22:56:07'),
(114, 'BUMBU', 'SEREH', 'KG', '4000.00', '2014-04-30 22:56:07'),
(115, 'BUMBU', 'THYME', 'KG', '80000.00', '2014-04-30 22:56:07'),
(116, 'DAGING', 'DAGING SAPI', 'KG', '90000.00', '2014-04-30 22:56:07'),
(117, 'DAGING', 'DAGING HAS DALAM', 'KG', '100000.00', '2014-04-30 22:56:07'),
(118, 'DAGING', 'DAGING HAS LUAR', 'KG', '90000.00', '2014-04-30 22:56:07'),
(119, 'DAGING', 'DAGING SAMPIL', 'KG', '80000.00', '2014-04-30 22:56:07'),
(120, 'DAGING', 'JEROHAN', 'KG', '30000.00', '2014-04-30 22:56:07'),
(121, 'DAGING', 'HATI', 'KG', '50000.00', '2014-04-30 22:56:07'),
(122, 'DAGING', 'KIKIL', 'KG', '20000.00', '2014-04-30 22:56:07'),
(123, 'DAGING', 'DAGING KAMBING', 'KG', '120000.00', '2014-04-30 22:56:07'),
(124, 'DAGING', 'BALUNGAN', 'KG', '18000.00', '2014-04-30 22:56:07'),
(125, 'UNGGAS', 'AYAM KAMPUNG', 'KG', '40000.00', '2014-04-30 22:56:08'),
(126, 'UNGGAS', 'PUYUH', 'KG', '50000.00', '2014-04-30 22:56:08'),
(127, 'UNGGAS', 'DAGING AYAM', 'KG', '25000.00', '2014-04-30 22:56:08'),
(128, 'UNGGAS', 'DAGING AYAM FILLET', 'KG', '30000.00', '2014-04-30 22:56:08'),
(129, 'UNGGAS', 'SAYAP', 'KG', '20000.00', '2014-04-30 22:56:08'),
(130, 'UNGGAS', 'CEKER', 'KG', '10000.00', '2014-04-30 22:56:08'),
(131, 'UNGGAS', 'KEPALA', 'KG', '12000.00', '2014-04-30 22:56:08'),
(132, 'UNGGAS', 'JEROHAN', 'KG', '16000.00', '2014-04-30 22:56:08'),
(133, 'IKAN', 'BANDENG', 'KG', '20000.00', '2014-04-30 22:56:08'),
(134, 'IKAN', 'BANDENG PRESTO', 'EKOR', '5000.00', '2014-04-30 22:56:08'),
(135, 'IKAN', 'BAWAL', 'KG', '15000.00', '2014-04-30 22:56:08'),
(136, 'IKAN', 'BELUT', 'KG', '20000.00', '2014-04-30 22:56:08'),
(137, 'IKAN', 'CAKALANG', 'KG', '15000.00', '2014-04-30 22:56:08'),
(138, 'IKAN', 'CUMI', 'KG', '20000.00', '2014-04-30 22:56:08'),
(139, 'IKAN', 'GURAME', 'KG', '14000.00', '2014-04-30 22:56:08'),
(140, 'IKAN', 'IKAN ASIN', 'KG', '20000.00', '2014-04-30 22:56:08'),
(141, 'IKAN', 'KEPITING', 'KG', '75000.00', '2014-04-30 22:56:08'),
(142, 'IKAN', 'KERANG KUPAS', 'KG', '25000.00', '2014-04-30 22:56:08'),
(143, 'IKAN', 'KERAPU', 'KG', '35000.00', '2014-04-30 22:56:08'),
(144, 'IKAN', 'LELE', 'KG', '14000.00', '2014-04-30 22:56:08'),
(145, 'IKAN', 'NILA', 'KG', '15000.00', '2014-04-30 22:56:08'),
(146, 'IKAN', 'TERI PUTIH', 'KG', '45000.00', '2014-04-30 22:56:08'),
(147, 'IKAN', 'TERI ', 'KG', '45000.00', '2014-04-30 22:56:08'),
(148, 'IKAN', 'UDANG', 'KG', '60000.00', '2014-04-30 22:56:08'),
(149, 'TELUR', 'TELUR AYAM RAS', 'KG', '14000.00', '2014-04-30 22:56:08'),
(150, 'TELUR', 'TELUR AYAM KAMPUNG', 'KG', '25000.00', '2014-04-30 22:56:08'),
(151, 'TELUR', 'TELUR PUYUH', 'KG', '18000.00', '2014-04-30 22:56:08'),
(152, 'SUSU', 'SUSU SEGAR ', 'L', '6000.00', '2014-04-30 22:56:08'),
(153, 'SUSU', 'SUSU SEGAR KEMASAN', 'L', '14000.00', '2014-04-30 22:56:08'),
(154, 'SUSU', 'SUSU KENTAL MANIS', 'L', '30000.00', '2014-04-30 22:56:08'),
(155, 'SUSU', 'SUSU BUBUK FULL CREAM', 'KG', '75000.00', '2014-04-30 22:56:08'),
(156, 'SUSU', 'SUSU BUBUK SKIM', 'KG', '60000.00', '2014-04-30 22:56:08'),
(157, 'TEPUNG-TEPUNG AN', 'TEPUNG TERIGU', 'KG', '7000.00', '2014-04-30 22:56:08'),
(158, 'TEPUNG-TEPUNG AN', 'TEPUNG MAIZENA', 'KG', '10000.00', '2014-04-30 22:56:08'),
(159, 'TEPUNG-TEPUNG AN', 'TEPUNG SAGU', 'KG', '12000.00', '2014-04-30 22:56:08'),
(160, 'TEPUNG-TEPUNG AN', 'TEPUNG HUNKWE', 'KG', '10000.00', '2014-04-30 22:56:08'),
(161, 'TEPUNG-TEPUNG AN', 'TEPUNG BERAS', 'KG', '8000.00', '2014-04-30 22:56:08'),
(162, 'TEPUNG-TEPUNG AN', 'TEPUNGTAPIOKA', 'KG', '10000.00', '2014-04-30 22:56:08'),
(163, 'TEPUNG-TEPUNG AN', 'TEPUNG KETAN', 'KG', '10000.00', '2014-04-30 22:56:08'),
(164, 'TEPUNG-TEPUNG AN', 'TEPUNG BUMBU', 'KG', '17000.00', '2014-04-30 22:56:08'),
(165, 'TEPUNG-TEPUNG AN', 'TEPUNG ROTI', 'KG', '25000.00', '2014-04-30 22:56:08'),
(166, 'TEPUNG-TEPUNG AN', 'TEPUNG CUSTARD', 'KG', '20000.00', '2014-04-30 22:56:08'),
(167, 'BAHAN OLAHAN', 'ABON', 'KEMASAN', '12000.00', '2014-04-30 22:56:08'),
(168, 'BAHAN OLAHAN', 'BAKSO IKAN', 'KEMASAN', '4000.00', '2014-04-30 22:56:08'),
(169, 'BAHAN OLAHAN', 'BAKSO SAPI', 'KEMASAN', '4000.00', '2014-04-30 22:56:08'),
(170, 'BAHAN OLAHAN', 'BIHUN', 'KEMASAN', '3000.00', '2014-04-30 22:56:08'),
(171, 'BAHAN OLAHAN', 'BISKUIT', 'KEMASAN', '5000.00', '2014-04-30 22:56:08'),
(172, 'BAHAN OLAHAN', 'COKLAT BUBUK COKLAT BATANG', 'KG', '12000.00', '2014-04-30 22:56:08'),
(173, 'BAHAN OLAHAN', 'CREAMER', 'KG', '50000.00', '2014-04-30 22:56:08'),
(174, 'BAHAN OLAHAN', 'DAGING ASAP', 'LEMBAR', '2000.00', '2014-04-30 22:56:08'),
(175, 'BAHAN OLAHAN', 'GALANTIN', 'KEMASAN', '7000.00', '2014-04-30 22:56:08'),
(176, 'BAHAN OLAHAN', 'IKAN ASAP', 'BUAH', '2000.00', '2014-04-30 22:56:08'),
(177, 'BAHAN OLAHAN', 'KEJU', 'KG', '70000.00', '2014-04-30 22:56:09'),
(178, 'BAHAN OLAHAN', 'KELAPA PARUT', 'KEMASAN', '2000.00', '2014-04-30 22:56:09'),
(179, 'BAHAN OLAHAN', 'KEMBANG TAHU', 'KEMASAN', '5000.00', '2014-04-30 22:56:09'),
(180, 'BAHAN OLAHAN', 'KERUPUK BIASA', 'KG', '7000.00', '2014-04-30 22:56:09'),
(181, 'BAHAN OLAHAN', 'KERUPUK UDANG', 'KG', '10000.00', '2014-04-30 22:56:09'),
(182, 'BAHAN OLAHAN', 'KORNET', 'KEMASAN', '12000.00', '2014-04-30 22:56:09'),
(183, 'BAHAN OLAHAN', 'MACRONI', 'KG', '10000.00', '2014-04-30 22:56:09'),
(184, 'BAHAN OLAHAN', 'MIE BASAH', 'KG', '5000.00', '2014-04-30 22:56:09'),
(185, 'BAHAN OLAHAN', 'MIE KERING', 'KEMASAN', '10000.00', '2014-04-30 22:56:09'),
(186, 'BAHAN OLAHAN', 'MINYAK GORENG CURAH', 'L', '10000.00', '2014-04-30 22:56:09'),
(187, 'BAHAN OLAHAN', 'MINYAK GORENG KEMASAN', 'L', '12500.00', '2014-04-30 22:56:09'),
(188, 'BAHAN OLAHAN', 'MINYAK SALAD', 'L', '20000.00', '2014-04-30 22:56:09'),
(189, 'BAHAN OLAHAN', 'MINYAK WIJEN', 'L', '35000.00', '2014-04-30 22:56:09'),
(190, 'BAHAN OLAHAN', 'NUGGET', 'KEMASAN', '15000.00', '2014-04-30 22:56:09'),
(191, 'BAHAN OLAHAN', 'ONCOM', 'KEMASAN', '3000.00', '2014-04-30 22:56:09'),
(192, 'BAHAN OLAHAN', 'OTAK-OTAK BANDENG', 'BUAH', '7500.00', '2014-04-30 22:56:09'),
(193, 'BAHAN OLAHAN', 'PASTA', 'KEMASAN', '4000.00', '2014-04-30 22:56:09'),
(194, 'BAHAN OLAHAN', 'ROLADE DAGING', 'BUAH', '10000.00', '2014-04-30 22:56:09'),
(195, 'BAHAN OLAHAN', 'ROTI TAWAR ', 'KEMASAN', '6000.00', '2014-04-30 22:56:09'),
(196, 'BAHAN OLAHAN', 'SANTAN KEMASAN', 'KEMASAN', '2000.00', '2014-04-30 22:56:09'),
(197, 'BAHAN OLAHAN', 'SARDEN', 'KEMASAN', '8000.00', '2014-04-30 22:56:09'),
(198, 'BAHAN OLAHAN', 'SOSIS', 'KEMASAN', '12000.00', '2014-04-30 22:56:09'),
(199, 'BAHAN OLAHAN', 'SOUN', 'KEMASAN', '3000.00', '2014-04-30 22:56:09'),
(200, 'BAHAN OLAHAN', 'TAHU PUTIH', 'KEMASAN', '5000.00', '2014-04-30 22:56:09'),
(201, 'BAHAN OLAHAN', 'TAHU KUNING', 'KEMASAN', '4000.00', '2014-04-30 22:56:09'),
(202, 'BAHAN OLAHAN', 'TAHU BAKSO', 'KEMASAN', '10000.00', '2014-04-30 22:56:09'),
(203, 'BAHAN OLAHAN', 'TEMPE', 'KEMASAN', '2500.00', '2014-04-30 22:56:09'),
(204, 'BAHAN OLAHAN', 'TEMPE GEMBUS', 'KEMASAN', '1000.00', '2014-04-30 22:56:09'),
(205, 'BAHAN OLAHAN', 'YOGHURT', 'KEMASAN', '5000.00', '2014-04-30 22:56:09'),
(206, 'BAHAN TAMBAHAN PANGAN', 'AGAR-AGAR ', 'KEMASAN', '2000.00', '2014-04-30 22:56:09'),
(207, 'BAHAN TAMBAHAN PANGAN', 'BAKING POWDER', 'KG', '25000.00', '2014-04-30 22:56:09'),
(208, 'BAHAN TAMBAHAN PANGAN', 'CUKA', 'L', '26000.00', '2014-04-30 22:56:09'),
(209, 'BAHAN TAMBAHAN PANGAN', 'GARAM', 'KG', '8000.00', '2014-04-30 22:56:09'),
(210, 'BAHAN TAMBAHAN PANGAN', 'GULA PASIR', 'KG', '11000.00', '2014-04-30 22:56:09'),
(211, 'BAHAN TAMBAHAN PANGAN', 'GULA JAWA', 'KG', '12500.00', '2014-04-30 22:56:09'),
(212, 'BAHAN TAMBAHAN PANGAN', 'KALDU BUBUK', 'KEMASAN', '500.00', '2014-04-30 22:56:09'),
(213, 'BAHAN TAMBAHAN PANGAN', 'KECAP ASIN', 'KEMASAN', '7000.00', '2014-04-30 22:56:09'),
(214, 'BAHAN TAMBAHAN PANGAN', 'KECAP MANIS', 'KEMASAN', '4000.00', '2014-04-30 22:56:09'),
(215, 'BAHAN TAMBAHAN PANGAN', 'KECAP PEDAS', 'KEMASAN', '5000.00', '2014-04-30 22:56:09'),
(216, 'BAHAN TAMBAHAN PANGAN', 'MAYYONAISSE', 'KEMASAN', '8000.00', '2014-04-30 22:56:09'),
(217, 'BAHAN TAMBAHAN PANGAN', 'MSG', 'KEMASAN', '2000.00', '2014-04-30 22:56:09'),
(218, 'BAHAN TAMBAHAN PANGAN', 'OVALET', 'KEMASAN', '2000.00', '2014-04-30 22:56:09'),
(219, 'BAHAN TAMBAHAN PANGAN', 'PASTA/ESSENCE', 'KEMASAN', '4000.00', '2014-04-30 22:56:09'),
(220, 'BAHAN TAMBAHAN PANGAN', 'PEMANIS BUATAN', 'KEMASAN', '2000.00', '2014-04-30 22:56:09'),
(221, 'BAHAN TAMBAHAN PANGAN', 'PETIS', 'KEMASAN', '4000.00', '2014-04-30 22:56:09'),
(222, 'BAHAN TAMBAHAN PANGAN', 'PEWARNA', 'KEMASAN', '1500.00', '2014-04-30 22:56:09'),
(223, 'BAHAN TAMBAHAN PANGAN', 'SAMBAL PECEL', 'KEMASAN', '2000.00', '2014-04-30 22:56:09'),
(224, 'BAHAN TAMBAHAN PANGAN', 'SAUS SAMBAL', 'KEMASAN', '4000.00', '2014-04-30 22:56:09'),
(225, 'BAHAN TAMBAHAN PANGAN', 'SAUS TIRAM', 'KEMASAN', '5000.00', '2014-04-30 22:56:09'),
(226, 'BAHAN TAMBAHAN PANGAN', 'SAUS TOMAT', 'KEMASAN', '4000.00', '2014-04-30 22:56:09'),
(227, 'BAHAN TAMBAHAN PANGAN', 'SODA KUE', 'KEMASAN', '40000.00', '2014-04-30 22:56:09'),
(228, 'BAHAN TAMBAHAN PANGAN', 'TERASI', 'KEMASAN', '300.00', '2014-04-30 22:56:09'),
(229, 'BAHAN TAMBAHAN PANGAN', 'VANILI', 'KEMASAN', '300.00', '2014-04-30 22:56:09'),
(230, 'BAHAN TAMBAHAN PANGAN', 'JELLY', 'KEMASAN', '2000.00', '2014-04-30 22:56:09'),
(231, 'BAHAN TAMBAHAN PANGAN', 'KOPI', 'KEMASAN', '1000.00', '2014-04-30 22:56:09'),
(232, 'BAHAN TAMBAHAN PANGAN', 'COKLAT BUBUK', 'KEMASAN', '10000.00', '2014-04-30 22:56:09'),
(233, 'BAHAN TAMBAHAN PANGAN', 'TEH', 'KEMASAN', '3000.00', '2014-04-30 22:56:10'),
(234, 'BAHAN TAMBAHAN PANGAN', 'AIR', 'L', '0.00', '2014-04-30 22:56:10');

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
(13, 'ALPUKAT', 4, 'BUAH', 1, 'KG'),
(14, 'APEL FUJI', 6, 'BUAH', 1, 'KG'),
(15, 'APEL MALANG', 12, 'BUAH', 1, 'KG'),
(69, 'BANDENG', 4, 'EKOR', 1, 'KG'),
(70, 'BAWAL', 3, 'EKOR', 1, 'KG'),
(41, 'BAWANG BOMBAY', 10, 'BUAH', 1, 'KG'),
(42, 'BAWANG MERAH', 100, 'BUAH', 1, 'KG'),
(43, 'BAWANG PUTIH', 100, 'BUAH', 1, 'KG'),
(16, 'BELIMBING', 5, 'BUAH', 1, 'KG'),
(71, 'BELUT', 10, 'EKOR', 1, 'KG'),
(17, 'BENGKUANG', 7, 'BUAH', 1, 'KG'),
(18, 'BIT', 8, 'BUAH', 1, 'KG'),
(44, 'CABAI HIJAU RAWIT', 200, 'BUAH', 1, 'KG'),
(45, 'CABAI HIJAU TEOPONG', 90, 'BUAH', 1, 'KG'),
(46, 'CABAI MERAH KERITING ', 120, 'BUAH', 1, 'KG'),
(47, 'CABAI MERAH TEROPONG', 90, 'BUAH', 1, 'KG'),
(48, 'CABAI RAWIT SETAN', 175, 'BUAH', 1, 'KG'),
(72, 'CAKALANG', 4, 'EKOR', 1, 'KG'),
(67, 'CEKER', 15, 'POTONG', 1, 'KG'),
(49, 'CENGKEH', 200, 'SDM', 1, 'KG'),
(92, 'COKLAT BUBUK COKLAT BATANG', 100, 'SDM', 1, 'KG'),
(93, 'CREAMER', 100, 'SDM', 1, 'KG'),
(94, 'CUKA', 200, 'SDM', 1, 'L'),
(65, 'DAGING AYAM', 8, 'POTONG', 1, 'KG'),
(19, 'DELIMA', 14, 'BUAH', 1, 'KG'),
(20, 'DONDONG', 12, 'BUAH', 1, 'KG'),
(95, 'GARAM', 65, 'SDM', 1, 'KG'),
(97, 'GULA JAWA', 16, 'BUAH', 1, 'KG'),
(96, 'GULA PASIR', 65, 'SDM', 1, 'KG'),
(73, 'GURAME', 3, 'EKOR', 1, 'KG'),
(2, 'JAGUNG KUNING', 3, 'BUAH', 1, 'KG'),
(1, 'JAGUNG MANIS', 4, 'BUAH', 1, 'KG'),
(50, 'JAHE', 66, 'RUAS', 1, 'KG'),
(21, 'JAMBU AIR', 15, 'BUAH', 1, 'KG'),
(22, 'JAMBU BIJI', 4, 'BUAH', 1, 'KG'),
(24, 'JERUK LEMON', 10, 'BUAH', 1, 'KG'),
(25, 'JERUK LOKAL', 8, 'BUAH', 1, 'KG'),
(51, 'JERUK NIPIS', 30, 'BUAH', 1, 'KG'),
(52, 'JERUK PURUT', 50, 'BUAH', 1, 'KG'),
(23, 'JERUK SUNKIS', 8, 'BUAH', 1, 'KG'),
(53, 'JINTEN', 200, 'SDM', 1, 'KG'),
(54, 'KAPULAGA', 140, 'BUAH', 1, 'KG'),
(55, 'KAYU MANIS', 70, 'BATANG', 1, 'KG'),
(56, 'KENCUR', 80, 'RUAS', 1, 'KG'),
(3, 'KENTANG', 12, 'BUAH', 1, 'KG'),
(68, 'KEPALA', 10, 'POTONG', 1, 'KG'),
(74, 'KEPITING', 5, 'EKOR', 1, 'KG'),
(75, 'KERAPU', 5, 'EKOR', 1, 'KG'),
(57, 'KETUMBAR', 200, 'SDM', 1, 'KG'),
(26, 'KIWI', 8, 'BUAH', 1, 'KG'),
(4, 'KOL', 2, 'BUAH', 1, 'KG'),
(58, 'KUNCI', 80, 'RUAS', 1, 'KG'),
(5, 'LABU KUNING', 0.5, 'BUAH', 1, 'KG'),
(6, 'LABU SIAM', 5, 'BUAH', 1, 'KG'),
(76, 'LELE', 6, 'EKOR', 1, 'KG'),
(59, 'LENGKUAS', 50, 'SDM', 1, 'KG'),
(7, 'LOBAK', 4, 'BUAH', 1, 'KG'),
(27, 'MANGGA', 3, 'BUAH', 1, 'KG'),
(28, 'MANGGIS', 10, 'BUAH', 1, 'KG'),
(29, 'MARKISA', 11, 'BUAH', 1, 'KG'),
(30, 'MELON', 0.5, 'BUAH', 1, 'KG'),
(31, 'MENTIMUN', 4, 'BUAH', 1, 'KG'),
(61, 'MERICA BUBUK', 200, 'SDM', 1, 'KG'),
(60, 'MERICA BUTIR', 200, 'SDM', 1, 'KG'),
(32, 'NANAS', 3, 'BUAH', 1, 'BUAH'),
(33, 'NANGKA KUPAS', 25, 'BUAH', 1, 'KG'),
(77, 'NILA', 4, 'EKOR', 1, 'KG'),
(8, 'PAPRIKA', 6, 'BUAH', 1, 'KG'),
(9, 'PARE', 5, 'BUAH', 1, 'KG'),
(34, 'PEPAYA', 0.5, 'BUAH', 1, 'KG'),
(35, 'PERSIK', 8, 'BUAH', 1, 'KG'),
(64, 'PUYUH', 4, 'EKOR', 1, 'KG'),
(36, 'RAMBUTAN', 24, 'BUAH', 1, 'KG'),
(62, 'ROSEMARY', 200, 'SDM', 1, 'KG'),
(37, 'SAWO', 14, 'BUAH', 1, 'KG'),
(66, 'SAYAP', 10, 'POTONG', 1, 'KG'),
(38, 'SEMANGKA', 0.3, 'BUAH', 1, 'KG'),
(63, 'SEREH', 40, 'BATANG', 1, 'KG'),
(39, 'SIRSAK', 1, 'BUAH', 1, 'KG'),
(40, 'STRAWBERRY', 80, 'BUAH', 1, 'KG'),
(80, 'SUSU SEGAR ', 5, 'GELAS', 1, 'L'),
(81, 'SUSU SEGAR KEMASAN', 5, 'GELAS', 1, 'L'),
(79, 'TELUR AYAM KAMPUNG', 10, 'BUTIR', 1, 'KG'),
(78, 'TELUR AYAM RAS', 8, 'BUTIR', 1, 'KG'),
(86, 'TEPUNG BERAS', 100, 'SDM', 1, 'KG'),
(89, 'TEPUNG BUMBU', 100, 'SDM', 1, 'KG'),
(91, 'TEPUNG CUSTARD', 100, 'SDM', 1, 'KG'),
(85, 'TEPUNG HUNKWE', 100, 'SDM', 1, 'KG'),
(88, 'TEPUNG KETAN', 100, 'SDM', 1, 'KG'),
(83, 'TEPUNG MAIZENA', 100, 'SDM', 1, 'KG'),
(90, 'TEPUNG ROTI', 100, 'SDM', 1, 'KG'),
(84, 'TEPUNG SAGU', 100, 'SDM', 1, 'KG'),
(82, 'TEPUNG TERIGU', 100, 'SDM', 1, 'KG'),
(87, 'TEPUNGTAPIOKA', 100, 'SDM', 1, 'KG'),
(10, 'TERONG', 10, 'BUAH', 1, 'KG'),
(11, 'TOMAT', 10, 'BUAH', 1, 'KG'),
(12, 'WORTEL', 12, 'BUAH', 1, 'KG');

-- --------------------------------------------------------

--
-- Struktur dari tabel `sys_asset`
--

CREATE TABLE IF NOT EXISTS `sys_asset` (
  `key_no` int(11) NOT NULL,
  `asset_no` varchar(50) NOT NULL,
  `invoice_no` varchar(20) NOT NULL,
  `period` int(11) NOT NULL,
  `name_fa` varchar(100) NOT NULL,
  `usia` int(11) NOT NULL,
  `purchase_amount` int(11) NOT NULL,
  `gol` varchar(100) NOT NULL,
  `accum_depreciation` int(11) NOT NULL,
  `depreciation_month` int(11) NOT NULL,
  `depreciation_amount` int(11) NOT NULL,
  `adjustment` int(11) NOT NULL,
  `status` varchar(20) NOT NULL,
  `ket` varchar(200) NOT NULL,
  `purchase_date` varchar(20) NOT NULL,
  `create_date` varchar(20) NOT NULL,
  `updated_date` datetime NOT NULL,
  PRIMARY KEY (`asset_no`),
  KEY `key_no` (`key_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `sys_asset`
--

INSERT INTO `sys_asset` (`key_no`, `asset_no`, `invoice_no`, `period`, `name_fa`, `usia`, `purchase_amount`, `gol`, `accum_depreciation`, `depreciation_month`, `depreciation_amount`, `adjustment`, `status`, `ket`, `purchase_date`, `create_date`, `updated_date`) VALUES
(0, 'FA2014022', '1111MMM', 201312, 'xxxyyy', 1, 111, 'Coba2', -889, 0, 0, 1000, 'Closed', 'xxxyyy', '18-12-2013', '13-02-2014', '2014-02-13 13:51:39'),
(0, 'FA2014023', 'MF111', 201310, 'Mouse', 1, 1000000, 'Peralatan', 999500, 1, 200, 300, 'Lost', 'Beli Mouse', '22-10-2013', '13-02-2014', '2014-02-13 13:52:07');

-- --------------------------------------------------------

--
-- Struktur dari tabel `sys_chart`
--

CREATE TABLE IF NOT EXISTS `sys_chart` (
  `chart_no` varchar(20) NOT NULL,
  `chart_parent` varchar(150) NOT NULL,
  `parent` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `sys_chart`
--

INSERT INTO `sys_chart` (`chart_no`, `chart_parent`, `parent`) VALUES
('1', 'AKTIVA', '0'),
('11', 'AKTIVA LANCAR', '1'),
('1101', 'KAS', '11'),
('110100', 'KAS PENJUALAN', '1101'),
('110101', 'KAS KECIL   ', '1101'),
('1102', 'BANK', '11'),
('110201', 'BCA BANK A/C IN', '1102'),
('110202', 'BCA BANK A/C OUT', '1102'),
('110298', 'PIUTANG CC BANK', '1102'),
('110299', 'BG KLIRING', '1102'),
('1103', 'INVESTASI', '11'),
('110301', 'DEPOSITO', '1103'),
('1104', 'KAS TRANSFER', '11'),
('110401', 'KAS KECIL - BANK A/C IN', '1104'),
('110402', 'KAS KECIL - BANK A/C OUT', '1104'),
('110403', 'KAS PENJUALAN - BANK A/C IN', '1104'),
('110404', 'BANK A/C IN - BANK A/C OUT', '1104'),
('110405', 'KAS - KAS', '1104'),
('1105', 'PIUTANG DAGANG', '11'),
('110501', 'PIUTANG DAGANG BSP', '1105'),
('110502', 'PIUTANG DAGANG MF', '1105'),
('110503', 'PIUTANG DAGANG FR', '1105'),
('110598', 'PIUTANG ACCRUED', '1105'),
('110599', 'PIUTANG DAGANG LAINNYA', '1105'),
('1106', 'PIUTANG KARYAWAN', '11'),
('1107', 'PIUTANG PEMEGANG SAHAM', '11'),
('1108', 'PIUTANG LAINNYA', '11'),
('110801', 'PIUTANG PIHAK KE-3', '1108'),
('110802', 'PIUTANG TRADE IN (FR)', '1108'),
('110803', 'PRICE VARIANCE', '1108'),
('1109', 'INVENTORY   ', '11'),
('1110', 'UANG MUKA PEMBELIAN', '11'),
('111001', 'ACCESSORIES SPARE PARTS', '1110'),
('1111', 'PAJAK DIBAYAR DIMUKA', '11'),
('111101', 'UANG MUKA PAJAK PPH 22', '1111'),
('111102', 'UANG MUKA PAJAK PPH 23', '1111'),
('111103', 'UANG MUKA PAJAK PPH 25', '1111'),
('111104', 'PPN MASUKAN', '1111'),
('111105', 'VALUE ADDIT TAX IMPORT - IN', '1111'),
('111106', 'LUXURY TAX - IN', '1111'),
('1112', 'BIAYA DIBAYAR DIMUKA', '11'),
('111201', 'BIAYA DIBAYAR DIMUKA', '1112'),
('111202', 'SEWA DIBAYAR DIMUKA', '1112'),
('111203', 'ASURANSI DIBAYAR DIMUKA', '1112'),
('1113', 'PEMBAYARAN DIBAYAR DIMUKA', '11'),
('111301', 'ADV PAYMENT', '1113'),
('1115', 'UANG MUKA PEMBELIAN', '11'),
('1116', 'PEMBAYARAN DIBAYAR DIMUKA', '11'),
('111601', 'ADV PAYMENT TO XXX', '1116'),
('12', 'AKTIVA TETAP', '1'),
('1201', 'FIXED ASSET', '12'),
('120101', 'BANGUNAN', '1201'),
('120102', 'KENDARAAN', '1201'),
('120103', 'PERALATAN KANTOR', '1201'),
('120104', 'PERLENGKAPAN BENGKEL', '1201'),
('120105', 'PERALATAN BENGKEL', '1201'),
('120106', 'ACCOUNTING SOFTWARE', '1201'),
('120107', 'LISENSI', '1201'),
('120108', 'PRA OPERATIONAL', '1201'),
('1202', 'AKUM DEPRESIASI', '12'),
('120201', 'AK. DEP. BANGUNAN', '1202'),
('120202', 'AK. DEP. KENDARAAN', '1202'),
('120203', 'AK. DEP. PERALATAN KANTOR', '1202'),
('120204', 'AK DEP. PERLENGKAPAN BENGKEL', '1202'),
('120205', 'AK. DEP. PERALATAN BENGKEL', '1202'),
('120206', 'AK. AMRT. ACCOUNTING SOFTWARE', '1202'),
('120207', 'AK. AMRT. LISENSI', '1202'),
('120208', 'AK. AMRT. PRA OPERATIONAL', '1202'),
('13', 'AKTIVA LAINNYA', '1'),
('1301', 'JAMINAN (DEPOSIT)', '13'),
('1302', 'STOCK INTRANSIT', '13'),
('2', 'HUTANG', '0'),
('21', 'HUTANG JK PENDEK', '2'),
('2101', 'HUTANG DAGANG', '21'),
('210101', 'HUTANG DAGANG SUPPLIER', '2101'),
('210102', 'HUTANG DAGANG KE UPLINE', '2101'),
('2102', 'ACCRUED HUTANG DAGANG', '21'),
('2103', 'HUTANG PAJAK', '21'),
('210301', 'HUTANG PAJAK PPH 21', '2103'),
('210302', 'HUTANG PAJAK PPH 23', '2103'),
('210303', 'HUTANG PAJAK PPH 25/29 BADAN', '2103'),
('210304', 'HUTANG PAJAK PPH FINAL (PPH SEWAA)', '2103'),
('210305', 'PPN KELUARAN', '2103'),
('210306', 'LUXURY TAX-OUT', '2103'),
('2104', 'UM PENJUALAN', '21'),
('22', 'PENGAKUAN BIAYA', '2'),
('2201', 'HUTANG BIAYA', '22'),
('220101', 'HUTANG TELEPHONE & FAX', '2201'),
('220102', 'HUTANG BIAYA LISTRIK DAN AIR', '2201'),
('220103', 'HUTANG BIAYA GAJI', '2201'),
('220104', 'HUTANG BIAYA KOMISI PENJUALAN / INSENTIF', '2201'),
('220105', 'HUTANG BIAYA PROMOSI', '2201'),
('220106', 'BONUS DEALER/AGENT', '2201'),
('220107', 'BLIND BONUS', '2201'),
('220108', 'INCENTIVE', '2201'),
('220109', 'ENTERTAINMENT', '2201'),
('220110', 'DELIVERY GOODS', '2201'),
('220111', 'AFTER SALES SERVICE', '2201'),
('220112', 'OFFICE / WAREHOUSE RENTAL', '2201'),
('220113', 'VEHICLE RENTAL', '2201'),
('220114', 'OFFICE/WAREHOUSE INSURANCE', '2201'),
('220115', 'VEHICLE INSURANCE', '2201'),
('2202', 'PROVISION PAYMENT DISCOUNT', '22'),
('2299', 'HUTANG LAIN-LAIN', '22'),
('229901', 'CEK / GIRO DLM PERJALANAN', '2299'),
('229902', 'REKENING ANTAR CABANG', '2299'),
('229904', 'TITIPAN PIHAK KE-3', '2299'),
('3', 'MODAL', '0'),
('31', 'MODAL SAHAM', '3'),
('3101', 'MODAL SAHAM DISETOR', '31'),
('3102', 'TAMBAHAN MODAL DISETOR', '31'),
('32', 'LABA DITAHAN', '3'),
('3201', 'LABA DITAHAN S.D TAHUN LALU', '32'),
('3202', 'LABA DITAHAN TAHUN BERJALAN', '32'),
('3203', 'LABA DITAHAN BULAN BERJALAN', '32'),
('4', 'PENDAPATAN', '0'),
('4101', 'PENJUALAN', '4'),
('4102', 'DISKON PENJUALAN', '4'),
('4103', 'RETUR PENJUALAN', '4'),
('5', 'HPP', '0'),
('5101', 'HPP', '5'),
('6', 'BIAYA OPERASI', '0'),
('61', 'BIAYA PENJUALAN', '6'),
('6101', 'BIAYA KOMISI TARGET PENJ', '61'),
('6102', 'BIAYA CUCI MOTOR', '61'),
('6103', 'BIAYA GAJI STAFF BENGKEL', '61'),
('6104', 'BIAYA PROMOSI', '61'),
('6105', 'BIAYA MANAGEMENT FEE', '61'),
('62', 'BIAYA ADMINISTRASI', '6'),
('6201', 'BIAYA KANTOR', '62'),
('620101', 'GAJI, BONUS, THR STAFF KANTOR', '6201'),
('620102', 'ALAT TULIS DAN BRG CETAKAN', '6201'),
('620103', 'PERANGKO DAN MATERAI', '6201'),
('620104', 'FOTOCOPY', '6201'),
('620105', 'BARANG KEPERLUAN KOMPUTER', '6201'),
('620106', 'BIAYA RUMAH TANGGA KANTOR', '6201'),
('620107', 'BIAYA JAMSOSTEK', '6201'),
('620108', 'BIAYA TRAINING & REKRUTMEN', '6201'),
('620109', 'BIAYA', '6201'),
('620110', 'BIAYA MEDICAL KARYAWAN', '6201'),
('620111', 'BIAYA KESEJAHTERAAN KARYAWAN', '6201'),
('6202', 'BIAYA SARANA', '62'),
('620201', 'TELPON, INTERNET, FAX', '6202'),
('620202', 'AIR', '6202'),
('620203', 'LISTRIK', '6202'),
('6203', 'BIAYA PERJALANAN DAN TRSNP', '62'),
('620301', 'BIAYA BENSIN, TOL DSB', '6203'),
('620302', 'BIAYA PERJALANAN DINAS', '6203'),
('6204', 'BIAYA PEMELIHARAAN', '62'),
('620401', 'B.PEMELIHARAAN MOBIL', '6204'),
('620402', 'B.PEMELIHARAAN MOTOR', '6204'),
('620403', 'B.PEMELIHARAAN ALAT KANTOR', '6204'),
('620404', 'B.PEMELIHARAAN GEDUNG', '6204'),
('6205', 'B.UMUM', '62'),
('620501', 'BIAYA SEWA', '6205'),
('620502', 'BIAYA REPRESENTASI/REKOMENDASI', '6205'),
('620503', 'BIAYA NOTARIS/KONSULTAN', '6205'),
('620504', 'BIAYA KEAMANAN/KEBERSIHAN', '6205'),
('620505', 'BIAYA ENTERTAINMENT', '6205'),
('620506', 'BIAYA SUMBANGAN/IURAN/RETR', '6205'),
('620507', 'BIAYA PEMBULATAN', '6205'),
('620508', 'BIAYA ADM BANK', '6205'),
('620509', 'B.UMUM LAINNYA', '6205'),
('6206', 'BIAYA ASURANSI', '62'),
('620601', 'B.ASURANSI GEDUNG', '6206'),
('620602', 'B.ASURANSI KENDARAAN', '6206'),
('620603', 'B.ASURANSI LAINNYA', '6206'),
('6207', 'BIAYA PENYUSUTAN', '62'),
('620701', 'B.PENYUSUTAN GEDUNG', '6207'),
('620702', 'B.PENYUSUTAN KENDARAAN', '6207'),
('620703', 'B.PENYUSUTAN PERALTAN KTR', '6207'),
('620704', 'B.PENYUSUTAN PERLGKPN BENGKEL', '6207'),
('620705', 'B.PENYUSUTAN PERARATAN BENGKEL', '6207'),
('6208', 'BIAYA AMORTISASI', '62'),
('620801', 'B.AMORTISASI S/W', '6208'),
('620802', 'B.AMORTISASI PRA OPS', '6208'),
('620803', 'B.AMORTISASI LISENSI/PATEN', '6208'),
('7', 'PENDAPATAN & BIAYA DILUAR USAHA', '0'),
('71', 'PENDAPATAN LAINNYA', '7'),
('7101', 'PENDAPTAN BUNGA/JASA GIRO', '71'),
('7102', 'LABA PENJUALAN AKTIVA', '71'),
('7103', 'PENDAPATAN SEWA', '71'),
('7104', 'PENDAPATAN SELISIH KURS', '71'),
('7199', 'PENDAPATAN LAINNYA', '71'),
('72', 'BIAYA LAINNYA', '7'),
('7201', 'BIAYA PAJAK BANK', '72'),
('7202', 'BIAYA PROVISI BANK', '72'),
('7203', 'BIAYA BUNGA LEASING', '72'),
('7204', 'RUGI ATAS PENJ.AKTIVA', '72'),
('7205', 'SELISIH KURS', '72'),
('7206', 'SELISIH PIUTANG/PENGHAPUSAN', '72'),
('7207', 'BIAYA BUNGA BANK', '72'),
('7208', 'BIAYA LAINNYA', '72');

-- --------------------------------------------------------

--
-- Struktur dari tabel `sys_gol_asset`
--

CREATE TABLE IF NOT EXISTS `sys_gol_asset` (
  `gol` int(11) NOT NULL,
  `nama_golongan` varchar(150) NOT NULL,
  `chart_no_debit` int(11) NOT NULL,
  `chart_no_kredit` int(11) NOT NULL,
  `usia` int(11) NOT NULL,
  `debit` varchar(100) NOT NULL,
  `kredit` varchar(100) NOT NULL,
  `updated_date` datetime NOT NULL,
  KEY `gol` (`gol`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `sys_gol_asset`
--

INSERT INTO `sys_gol_asset` (`gol`, `nama_golongan`, `chart_no_debit`, `chart_no_kredit`, `usia`, `debit`, `kredit`, `updated_date`) VALUES
(4, 'xxx', 7101, 6208, 1, 'PENDAPTAN BUNGA/JASA GIRO', 'BIAYA AMORTISASI', '2014-02-08 21:25:14'),
(3, 'asas', 7104, 7203, 21232, 'PENDAPATAN SELISIH KURS', 'BIAYA BUNGA LEASING', '2014-02-08 21:25:28'),
(5, '1', 7207, 7207, 1, 'BIAYA BUNGA BANK', 'BIAYA BUNGA BANK', '2014-02-09 18:07:11'),
(6, 'Peralatan', 7207, 7207, 1, 'BIAYA BUNGA BANK', 'BIAYA BUNGA BANK', '2014-02-09 18:10:13'),
(7, 'Peralatan', 7207, 7207, 1, 'BIAYA BUNGA BANK', 'BIAYA BUNGA BANK', '2014-02-09 18:10:13'),
(34, 'Coba2', 7208, 7208, 11, 'BIAYA LAINNYA', 'BIAYA LAINNYA', '2014-02-09 18:13:17'),
(36, 'Coba2', 7208, 7208, 11, 'BIAYA LAINNYA', 'BIAYA LAINNYA', '2014-02-09 18:13:17'),
(48, 'Coba2', 7208, 7208, 11, 'BIAYA LAINNYA', 'BIAYA LAINNYA', '2014-02-09 18:13:17'),
(50, 'Coba2', 7208, 7208, 11, 'BIAYA LAINNYA', 'BIAYA LAINNYA', '2014-02-09 18:13:17'),
(51, 'Coba2', 6208, 6103, 11, 'BIAYA AMORTISASI', 'BIAYA GAJI STAFF BENGKEL', '2014-02-13 14:10:55');

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `detail_alokasi_waktu`
--
ALTER TABLE `detail_alokasi_waktu`
  ADD CONSTRAINT `detail_alokasi_waktu_ibfk_1` FOREIGN KEY (`nama_produksi`) REFERENCES `header_alokasi_waktu` (`nama_produksi`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `detail_pesanan`
--
ALTER TABLE `detail_pesanan`
  ADD CONSTRAINT `detail_pesanan_ibfk_1` FOREIGN KEY (`trans_no`) REFERENCES `header_pesanan` (`trans_no`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `detail_resep`
--
ALTER TABLE `detail_resep`
  ADD CONSTRAINT `detail_resep_ibfk_1` FOREIGN KEY (`namresep`) REFERENCES `header_resep` (`nama_resep`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `detail_trans`
--
ALTER TABLE `detail_trans`
  ADD CONSTRAINT `detail_trans_ibfk_1` FOREIGN KEY (`trans_no`) REFERENCES `header_pembelian` (`trans_no`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `detail_trans_penerimaan`
--
ALTER TABLE `detail_trans_penerimaan`
  ADD CONSTRAINT `detail_trans_penerimaan_ibfk_1` FOREIGN KEY (`trans_no`) REFERENCES `header_penerimaan` (`trans_no`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `header_memasak`
--
ALTER TABLE `header_memasak`
  ADD CONSTRAINT `header_memasak_ibfk_1` FOREIGN KEY (`nama_masakan`) REFERENCES `header_resep` (`nama_resep`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
