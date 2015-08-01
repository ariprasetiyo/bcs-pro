-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Waktu pembuatan: 22. Juni 2014 jam 04:31
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
('coba', 'beli bahan', '10-1'),
('coba', 'mengelolah', '2-2'),
('Desain Animasi', 'Nyari konsep ', '1-2'),
('Desain Animasi', 'Belajar', '2-2'),
('Desain Animasi', 'Developing', '3-2');

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_penjualan`
--

CREATE TABLE IF NOT EXISTS `detail_penjualan` (
  `trans_no` varchar(20) NOT NULL,
  `nama_menu` varchar(50) NOT NULL,
  `qty` int(11) NOT NULL,
  KEY `trans_no` (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `detail_penjualan`
--

INSERT INTO `detail_penjualan` (`trans_no`, `nama_menu`, `qty`) VALUES
('PS2014062', 'ds', 11),
('PS2014061', 'da', 1),
('PS2014061', 'ds', 1);

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
('PS2014061', 'da', 1),
('PS2014061', 'ds', 1),
('PS2014062', 'ds', 11);

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_rencana_inventaris`
--

CREATE TABLE IF NOT EXISTS `detail_rencana_inventaris` (
  `no` int(11) NOT NULL,
  `trans_no` varchar(20) NOT NULL,
  `nama_inventaris` varchar(200) NOT NULL,
  `qty` int(11) NOT NULL,
  KEY `trans_no` (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `detail_rencana_inventaris`
--

INSERT INTO `detail_rencana_inventaris` (`no`, `trans_no`, `nama_inventaris`, `qty`) VALUES
(1, 'PS2014061 ', 'COBA', 1),
(11, 'PS2014061 ', 'COBA', 1);

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
  `qty_tdk_baku` double NOT NULL,
  `satuan_tdk_baku` varchar(100) NOT NULL,
  `qty` double NOT NULL,
  `satuan` varchar(100) NOT NULL,
  `created_date` varchar(100) NOT NULL,
  `update_date` datetime NOT NULL,
  KEY `namresep` (`namresep`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `detail_resep`
--

INSERT INTO `detail_resep` (`namresep`, `no`, `bahan`, `qty_1_porsi`, `satuan_1_porsi`, `qty_tdk_baku`, `satuan_tdk_baku`, `qty`, `satuan`, `created_date`, `update_date`) VALUES
('ds', 1, ' BAYAM', 1, 'IKAT', 11, 'IKAT', 11, 'IKAT', '19-05-2014', '2014-05-19 20:54:21'),
('da', 1, ' BERAS ', 1, 'KG', 11, 'KG', 11, 'KG', '19-05-2014', '2014-05-19 21:00:27');

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
(1, 'P2014051', 5, ' BUNGA KECOMBRANG', 111, 'BIJI', 1000, 111000, '16-05-2014', '2014-05-16 00:05:48'),
(1, 'P2014052', 5, ' BERAS ', 111, 'KG', 8500, 943500, '16-05-2014', '2014-05-16 00:09:52'),
(1, 'P2014061', 6, ' BERAS ', 11, 'KG', 8500, 93500, '08-06-2014', '2014-06-08 21:01:06'),
(2, 'P2014061', 6, ' KETAN', 11, 'KG', 12000, 132000, '08-06-2014', '2014-06-08 21:01:06'),
(3, 'P2014061', 6, ' KETELA SINGKONG', 11, 'KG', 2500, 27500, '08-06-2014', '2014-06-08 21:01:06'),
(4, 'P2014061', 6, ' JAGUNG', 11, 'KG', 6000, 66000, '08-06-2014', '2014-06-08 21:01:06'),
(5, 'P2014061', 6, ' BAYAM', 11, 'IKAT', 2000, 22000, '08-06-2014', '2014-06-08 21:01:06'),
(6, 'P2014061', 6, ' BIT', 11, 'KG', 25000, 275000, '08-06-2014', '2014-06-08 21:01:06'),
(7, 'P2014061', 6, ' CABAI HIJAU RAWIT', 11, 'KG', 20000, 220000, '08-06-2014', '2014-06-08 21:01:06'),
(8, 'P2014061', 6, ' BAWAL', 11, 'KG', 15000, 165000, '08-06-2014', '2014-06-08 21:01:06'),
(9, 'P2014061', 6, ' GALANTIN', 11, 'KEMASAN', 7000, 77000, '08-06-2014', '2014-06-08 21:01:06'),
(10, 'P2014061', 6, ' TAHU KUNING', 11, 'KEMASAN', 4000, 44000, '08-06-2014', '2014-06-08 21:01:06'),
(11, 'P2014061', 6, ' TEH', 11, 'KEMASAN', 3000, 33000, '08-06-2014', '2014-06-08 21:01:06'),
(12, 'P2014061', 6, ' TEH', 11, 'KEMASAN', 3000, 33000, '08-06-2014', '2014-06-08 21:01:06'),
(13, 'P2014061', 6, ' TEH', 11, 'KEMASAN', 3000, 33000, '08-06-2014', '2014-06-08 21:01:06'),
(14, 'P2014061', 6, ' COKLAT BUBUK', 11, 'KEMASAN', 10000, 110000, '08-06-2014', '2014-06-08 21:01:06'),
(15, 'P2014061', 6, ' KOPI', 11, 'KEMASAN', 1000, 11000, '08-06-2014', '2014-06-08 21:01:06');

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
(1, 'WR2014051', 201405, ' BERAS ', '111.00', 'KG', '8500.00', '943500.00', '1000.00', '18-05-2014', '2014-05-18 13:34:05'),
(1, 'WR2014061', 201406, ' BUNGA KECOMBRANG', '111.00', 'BIJI', '1000.00', '111000.00', '100.00', '05-06-2014', '2014-06-05 07:49:45'),
(1, 'WR2014062', 201406, ' BERAS ', '11.00', 'KG', '8500.00', '93500.00', '11111110.00', '08-06-2014', '2014-06-08 21:01:37'),
(2, 'WR2014062', 201406, ' KETAN', '11.00', 'KG', '12000.00', '132000.00', '1111.00', '08-06-2014', '2014-06-08 21:01:37'),
(3, 'WR2014062', 201406, ' KETELA SINGKONG', '11.00', 'KG', '2500.00', '27500.00', '111111.00', '08-06-2014', '2014-06-08 21:01:37'),
(4, 'WR2014062', 201406, ' JAGUNG', '11.00', 'KG', '6000.00', '66000.00', '111111.00', '08-06-2014', '2014-06-08 21:01:37'),
(5, 'WR2014062', 201406, ' BAYAM', '11.00', 'IKAT', '2000.00', '22000.00', '111111.00', '08-06-2014', '2014-06-08 21:01:37'),
(6, 'WR2014062', 201406, ' BIT', '11.00', 'KG', '25000.00', '275000.00', '111111.00', '08-06-2014', '2014-06-08 21:01:37'),
(7, 'WR2014062', 201406, ' CABAI HIJAU RAWIT', '11.00', 'KG', '20000.00', '220000.00', '11111.00', '08-06-2014', '2014-06-08 21:01:37'),
(8, 'WR2014062', 201406, ' BAWAL', '11.00', 'KG', '15000.00', '165000.00', '1111.00', '08-06-2014', '2014-06-08 21:01:37'),
(9, 'WR2014062', 201406, ' GALANTIN', '11.00', 'KEMASAN', '7000.00', '77000.00', '111.00', '08-06-2014', '2014-06-08 21:01:37'),
(10, 'WR2014062', 201406, ' TAHU KUNING', '11.00', 'KEMASAN', '4000.00', '44000.00', '111.00', '08-06-2014', '2014-06-08 21:01:37'),
(11, 'WR2014062', 201406, ' TEH', '11.00', 'KEMASAN', '3000.00', '33000.00', '111.00', '08-06-2014', '2014-06-08 21:01:37'),
(12, 'WR2014062', 201406, ' TEH', '11.00', 'KEMASAN', '3000.00', '33000.00', '111.00', '08-06-2014', '2014-06-08 21:01:37'),
(13, 'WR2014062', 201406, ' TEH', '11.00', 'KEMASAN', '3000.00', '33000.00', '111.00', '08-06-2014', '2014-06-08 21:01:37'),
(14, 'WR2014062', 201406, ' COKLAT BUBUK', '11.00', 'KEMASAN', '10000.00', '110000.00', '111.00', '08-06-2014', '2014-06-08 21:01:37'),
(15, 'WR2014062', 201406, ' KOPI', '11.00', 'KEMASAN', '1000.00', '11000.00', '111.00', '08-06-2014', '2014-06-08 21:01:37');

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
('coba', 3480, '  2 Hari  10 Jam  0 Menit', '2014-06-09 16:56:29'),
('Desain Animasi', 8640, '  6 Hari  0 Jam  0 Menit', '2014-06-09 16:57:19');

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
('JM2014062', 8304, 'Biaya Asuransi', 10000, 'sss', '21-06-2014', 201406, '2014-06-21 21:20:47'),
('JM2014063', 8304, 'Biaya Asuransi', 10011210, 'COBASSS', '19-06-2014', 201406, '2014-06-21 21:20:47'),
('JM2014064', 8203, 'Biaya Bahan Bakar', 111, 'sass', '21-06-2014', 201406, '2014-06-21 21:20:47');

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
('ds', '', '2014-05-19 20:54:21'),
('da', '', '2014-05-19 21:00:27');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_pembelian`
--

CREATE TABLE IF NOT EXISTS `header_pembelian` (
  `key_no` int(11) NOT NULL,
  `trans_no` varchar(100) NOT NULL,
  `nama_resep` varchar(100) NOT NULL,
  `porsi` int(11) NOT NULL,
  `status_pesanan` varchar(100) NOT NULL,
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

INSERT INTO `header_pembelian` (`key_no`, `trans_no`, `nama_resep`, `porsi`, `status_pesanan`, `periode`, `supplier`, `total_biaya`, `uang_muka`, `created_date`, `update_date`) VALUES
(1, 'P2014051', 'null', 0, '-', 201405, 'sas', '111000.00', '10000.00', '16-05-2014', '2014-05-16 00:05:48'),
(2, 'P2014052', 'null', 0, '-', 201405, 'asas', '943500.00', '10000.00', '16-05-2014', '2014-05-16 00:09:52'),
(1, 'P2014061', 'null', 0, '-', 201406, 'xxxxxxxxx', '1342000.00', '10000000.00', '08-06-2014', '2014-06-08 21:01:06');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_penerimaan`
--

CREATE TABLE IF NOT EXISTS `header_penerimaan` (
  `key_no` int(11) NOT NULL,
  `trans_no` varchar(100) NOT NULL,
  `periode` int(11) NOT NULL,
  `judul_resep` varchar(300) NOT NULL,
  `porsi` int(11) NOT NULL,
  `status_pesanan` varchar(100) NOT NULL,
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

INSERT INTO `header_penerimaan` (`key_no`, `trans_no`, `periode`, `judul_resep`, `porsi`, `status_pesanan`, `supplier`, `note`, `no_po`, `tanggal_po`, `total_biaya`, `total_rill`, `uang_muka`, `uang_kembali`, `created_date`, `update_date`) VALUES
(1, 'WR2014051', 201405, 'null', 0, '-', 'asas', '0', 'P2014052', '16-05-2014', 943500, 1000, 10000, 9000, '18-05-2014', '2014-05-18 13:34:05'),
(1, 'WR2014061', 201406, 'null', 0, '-', 'sas', '0', 'P2014051', '16-05-2014', 111000, 100, 10000, 9900, '05-06-2014', '2014-06-05 07:49:45'),
(2, 'WR2014062', 201406, 'null', 0, '-', 'xxxxxxxxx', 'xxxxx', 'P2014061', '08-06-2014', 1342000, 11569664, 10000000, -1569664, '08-06-2014', '2014-06-08 21:01:37');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_penjualan`
--

CREATE TABLE IF NOT EXISTS `header_penjualan` (
  `trans_no` varchar(20) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `alamat` varchar(200) NOT NULL,
  `no_hp` varchar(15) NOT NULL,
  `note` varchar(200) NOT NULL,
  `tanggal_dikirim` varchar(15) NOT NULL,
  `jenis_pesanan` varchar(20) NOT NULL,
  `jumlah_dipesan` int(3) NOT NULL,
  `dp_status` int(1) NOT NULL,
  `dp_amount` double NOT NULL,
  `bayar` double NOT NULL,
  `total_dibayar` double NOT NULL,
  `updated` date NOT NULL,
  `tanggal_transaksi` varchar(20) NOT NULL,
  `period` varchar(10) NOT NULL,
  `tanggal_lunas_dp` varchar(10) NOT NULL,
  `period_lunas` varchar(10) NOT NULL,
  PRIMARY KEY (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_penjualan`
--

INSERT INTO `header_penjualan` (`trans_no`, `nama`, `alamat`, `no_hp`, `note`, `tanggal_dikirim`, `jenis_pesanan`, `jumlah_dipesan`, `dp_status`, `dp_amount`, `bayar`, `total_dibayar`, `updated`, `tanggal_transaksi`, `period`, `tanggal_lunas_dp`, `period_lunas`) VALUES
('PS2014061', 'ari', 'sas', 'asa', 'asas', '17-06-2014', 'LUNCH BOX', 111, 2, 10000, 100000, 110000, '2014-06-22', '19-06-2014', '201406', '23-06-2014', '201406'),
('PS2014062', '11', '', '', '', '17-06-2014', 'LUNCH BOX', 212, 2, 10000, 90000, 100000, '2014-06-21', '19-06-2014', '201406', '17-07-2014', '201407');

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
(0, 'PS2014061', 'LUNCH BOX', 201406, 'ari', 111, '17-06-2014', 0, '2014-06-17 18:23:54'),
(2, 'PS2014062', 'LUNCH BOX', 201406, '11', 212, '17-06-2014', 0, '2014-06-17 23:28:24');

-- --------------------------------------------------------

--
-- Struktur dari tabel `header_rencana_inventaris`
--

CREATE TABLE IF NOT EXISTS `header_rencana_inventaris` (
  `key_no` int(11) NOT NULL,
  `trans_no` varchar(20) NOT NULL,
  `nama_produksi` varchar(200) NOT NULL,
  `tanggal` datetime NOT NULL,
  PRIMARY KEY (`trans_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `header_rencana_inventaris`
--

INSERT INTO `header_rencana_inventaris` (`key_no`, `trans_no`, `nama_produksi`, `tanggal`) VALUES
(1, 'PS2014061 ', 'ari', '2014-06-02 21:48:47');

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
('da', 11, '19-05-2014', '2014-05-19 21:00:26'),
('ds', 11, '19-05-2014', '2014-05-19 20:54:21');

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
(1, 'BAHAN POKOK', 'BERAS ', 'KG', '8500.00', '2014-05-12 11:36:41'),
(2, 'BAHAN POKOK', 'BERAS MERAH', 'KG', '12000.00', '2014-05-12 11:36:41'),
(3, 'BAHAN POKOK', 'KETAN', 'KG', '12000.00', '2014-05-12 11:36:41'),
(4, 'BAHAN POKOK', 'KETELA SINGKONG', 'KG', '2500.00', '2014-05-12 11:36:41'),
(5, 'BAHAN POKOK', 'UBI', 'KG', '4000.00', '2014-05-12 11:36:41'),
(6, 'BAHAN POKOK', 'JAGUNG', 'KG', '6000.00', '2014-05-12 11:36:41'),
(7, 'SAYURAN ', 'BAYAM', 'IKAT', '2000.00', '2014-05-12 11:36:41'),
(8, 'SAYURAN ', 'BROKOLI', 'KG', '14000.00', '2014-05-12 11:36:41'),
(9, 'SAYURAN ', 'BUNCIS', 'KG', '8000.00', '2014-05-12 11:36:41'),
(10, 'SAYURAN ', 'BUNGA KOL', 'KG', '10000.00', '2014-05-12 11:36:41'),
(11, 'SAYURAN ', 'BUNGA TURI', 'KG', '8000.00', '2014-05-12 11:36:42'),
(12, 'SAYURAN ', 'DAUN PEPAYA', 'IKAT', '2000.00', '2014-05-12 11:36:42'),
(13, 'SAYURAN ', 'DAUN SINGKONG', 'IKAT', '1000.00', '2014-05-12 11:36:42'),
(14, 'SAYURAN ', 'DAUN SO', 'IKAT', '500.00', '2014-05-12 11:36:42'),
(15, 'SAYURAN ', 'GAMBAS/OYONG', 'KG', '5000.00', '2014-05-12 11:36:42'),
(16, 'SAYURAN ', 'JAGUNG MANIS', 'KG', '7500.00', '2014-05-12 11:36:42'),
(17, 'SAYURAN ', 'JAGUNG KUNING', 'KG', '7000.00', '2014-05-12 11:36:42'),
(18, 'SAYURAN ', 'JAMUR KUPING ', 'KG', '25000.00', '2014-05-12 11:36:42'),
(19, 'SAYURAN ', 'JAMUR SITAKE', 'KG', '30000.00', '2014-05-12 11:36:42'),
(20, 'SAYURAN ', 'JAMUR TIRAM', 'KG', '20000.00', '2014-05-12 11:36:42'),
(21, 'SAYURAN ', 'KACANG KARA', 'KG', '10000.00', '2014-05-12 11:36:42'),
(22, 'SAYURAN ', 'KACANG PANJANG', 'IKAT', '5000.00', '2014-05-12 11:36:42'),
(23, 'SAYURAN ', 'KANGKUNG', 'IKAT', '2000.00', '2014-05-12 11:36:42'),
(24, 'SAYURAN ', 'KECIPIR', 'IKAT', '2000.00', '2014-05-12 11:36:42'),
(25, 'SAYURAN ', 'KENINGKIR', 'IKAT', '1000.00', '2014-05-12 11:36:42'),
(26, 'SAYURAN ', 'KENTANG', 'KG', '8000.00', '2014-05-12 11:36:42'),
(27, 'SAYURAN ', 'KOL', 'KG', '2500.00', '2014-05-12 11:36:42'),
(28, 'SAYURAN ', 'LABU KUNING', 'KG', '3000.00', '2014-05-12 11:36:42'),
(29, 'SAYURAN ', 'LABU SIAM', 'KG', '3000.00', '2014-05-12 11:36:42'),
(30, 'SAYURAN ', 'LOBAK', 'KG', '4000.00', '2014-05-12 11:36:42'),
(31, 'SAYURAN ', 'PAPRIKA', 'KG', '25000.00', '2014-05-12 11:36:42'),
(32, 'SAYURAN ', 'PARE', 'KG', '5000.00', '2014-05-12 11:36:42'),
(33, 'SAYURAN ', 'SAWI HIJAU', 'IKAT', '2000.00', '2014-05-12 11:36:42'),
(34, 'SAYURAN ', 'SAWI PUTIH', 'IKAT', '3000.00', '2014-05-12 11:36:42'),
(35, 'SAYURAN ', 'SELADA', 'IKAT', '3000.00', '2014-05-12 11:36:42'),
(36, 'SAYURAN ', 'SELEDRI', 'IKAT', '2000.00', '2014-05-12 11:36:42'),
(37, 'SAYURAN ', 'DAUN SINGKONG', 'KG', '2500.00', '2014-05-12 11:36:42'),
(38, 'SAYURAN ', 'TAUGE', 'KG', '10000.00', '2014-05-12 11:36:42'),
(39, 'SAYURAN ', 'TERONG', 'KG', '14000.00', '2014-05-12 11:36:42'),
(40, 'SAYURAN ', 'TOMAT', 'KG', '12000.00', '2014-05-12 11:36:42'),
(41, 'SAYURAN ', 'WORTEL', 'KG', '12000.00', '2014-05-12 11:36:42'),
(42, 'BUAH-BUAHAN', 'ALPUKAT', 'KG', '14000.00', '2014-05-12 11:36:42'),
(43, 'BUAH-BUAHAN', 'ANGGUR', 'KG', '30000.00', '2014-05-12 11:36:42'),
(44, 'BUAH-BUAHAN', 'APEL FUJI', 'KG', '25000.00', '2014-05-12 11:36:43'),
(45, 'BUAH-BUAHAN', 'APEL MALANG', 'KG', '13000.00', '2014-05-12 11:36:43'),
(46, 'BUAH-BUAHAN', 'BELIMBING', 'KG', '12000.00', '2014-05-12 11:36:43'),
(47, 'BUAH-BUAHAN', 'BENGKUANG', 'KG', '7000.00', '2014-05-12 11:36:43'),
(48, 'BUAH-BUAHAN', 'BIT', 'KG', '25000.00', '2014-05-12 11:36:43'),
(49, 'BUAH-BUAHAN', 'CERRY', 'KG', '30000.00', '2014-05-12 11:36:43'),
(50, 'BUAH-BUAHAN', 'DELIMA', 'KG', '12000.00', '2014-05-12 11:36:43'),
(51, 'BUAH-BUAHAN', 'DONDONG', 'KG', '6000.00', '2014-05-12 11:36:43'),
(52, 'BUAH-BUAHAN', 'DUKU', 'KG', '7000.00', '2014-05-12 11:36:43'),
(53, 'BUAH-BUAHAN', 'DURIAN', 'BUAH', '40000.00', '2014-05-12 11:36:43'),
(54, 'BUAH-BUAHAN', 'JAMBU AIR', 'KG', '7000.00', '2014-05-12 11:36:43'),
(55, 'BUAH-BUAHAN', 'JAMBU BIJI', 'KG', '5000.00', '2014-05-12 11:36:43'),
(56, 'BUAH-BUAHAN', 'JERUK SUNKIS', 'KG', '15000.00', '2014-05-12 11:36:43'),
(57, 'BUAH-BUAHAN', 'JERUK LEMON', 'KG', '20000.00', '2014-05-12 11:36:43'),
(58, 'BUAH-BUAHAN', 'JERUK LOKAL', 'KG', '17000.00', '2014-05-12 11:36:43'),
(59, 'BUAH-BUAHAN', 'KELAPA', 'BUAH', '4000.00', '2014-05-12 11:36:43'),
(60, 'BUAH-BUAHAN', 'KELENGKENG', 'KG', '14000.00', '2014-05-12 11:36:43'),
(61, 'BUAH-BUAHAN', 'KIWI', 'KG', '25000.00', '2014-05-12 11:36:43'),
(62, 'BUAH-BUAHAN', 'LECI', 'KG', '30000.00', '2014-05-12 11:36:43'),
(63, 'BUAH-BUAHAN', 'MANGGA', 'KG', '15000.00', '2014-05-12 11:36:43'),
(64, 'BUAH-BUAHAN', 'MANGGIS', 'KG', '14000.00', '2014-05-12 11:36:43'),
(65, 'BUAH-BUAHAN', 'MARKISA', 'KG', '6000.00', '2014-05-12 11:36:43'),
(66, 'BUAH-BUAHAN', 'MELON', 'KG', '5000.00', '2014-05-12 11:36:43'),
(67, 'BUAH-BUAHAN', 'MENTIMUN', 'KG', '10000.00', '2014-05-12 11:36:43'),
(68, 'BUAH-BUAHAN', 'NANAS', 'BUAH', '4000.00', '2014-05-12 11:36:43'),
(69, 'BUAH-BUAHAN', 'NANGKA KUPAS', 'KG', '20000.00', '2014-05-12 11:36:43'),
(70, 'BUAH-BUAHAN', 'PEPAYA', 'KG', '3000.00', '2014-05-12 11:36:43'),
(71, 'BUAH-BUAHAN', 'PERSIK', 'KG', '20000.00', '2014-05-12 11:36:43'),
(72, 'BUAH-BUAHAN', 'RAMBUTAN', 'KG', '3500.00', '2014-05-12 11:36:43'),
(73, 'BUAH-BUAHAN', 'SAWO', 'KG', '7000.00', '2014-05-12 11:36:43'),
(74, 'BUAH-BUAHAN', 'SEMANGKA', 'KG', '5000.00', '2014-05-12 11:36:43'),
(75, 'BUAH-BUAHAN', 'SIRSAK', 'KG', '8000.00', '2014-05-12 11:36:43'),
(76, 'BUAH-BUAHAN', 'STRAWBERRY', 'KG', '22000.00', '2014-05-12 11:36:43'),
(77, 'KACANG-KACANGAN', 'KACANG TANAH', 'KG', '20000.00', '2014-05-12 11:36:43'),
(78, 'KACANG-KACANGAN', 'KACANG KEDELAI', 'KG', '13000.00', '2014-05-12 11:36:43'),
(79, 'KACANG-KACANGAN', 'KACANG METE', 'KG', '35000.00', '2014-05-12 11:36:43'),
(80, 'KACANG-KACANGAN', 'KACANG HIJAU', 'KG', '12000.00', '2014-05-12 11:36:43'),
(81, 'KACANG-KACANGAN', 'KACANG MERAH', 'KG', '14000.00', '2014-05-12 11:36:43'),
(82, 'KACANG-KACANGAN', 'KACANG TOLO', 'KG', '12000.00', '2014-05-12 11:36:44'),
(83, 'KACANG-KACANGAN', 'KACANG KORO', 'KG', '14000.00', '2014-05-12 11:36:44'),
(84, 'KACANG-KACANGAN', 'KACANG KAPRI', 'KG', '20000.00', '2014-05-12 11:36:44'),
(85, 'BUMBU', 'BAWANG BOMBAY', 'KG', '22000.00', '2014-05-12 11:36:44'),
(86, 'BUMBU', 'BAWANG MERAH', 'KG', '18000.00', '2014-05-12 11:36:44'),
(87, 'BUMBU', 'BAWANG PUTIH', 'KG', '14000.00', '2014-05-12 11:36:44'),
(88, 'BUMBU', 'BAY LEAF', 'LEMBAR', '200.00', '2014-05-12 11:36:44'),
(89, 'BUMBU', 'BIJI PALA', 'BIJI', '1000.00', '2014-05-12 11:36:44'),
(90, 'BUMBU', 'BUNGA KECOMBRANG', 'BIJI', '1000.00', '2014-05-12 11:36:44'),
(91, 'BUMBU', 'BUNGA PEKAK', 'BIJI', '200.00', '2014-05-12 11:36:44'),
(92, 'BUMBU', 'CABAI HIJAU RAWIT', 'KG', '20000.00', '2014-05-12 11:36:44'),
(93, 'BUMBU', 'CABAI HIJAU TEOPONG', 'KG', '18000.00', '2014-05-12 11:36:44'),
(94, 'BUMBU', 'CABAI MERAH KERITING ', 'KG', '12000.00', '2014-05-12 11:36:44'),
(95, 'BUMBU', 'CABAI MERAH TEROPONG', 'KG', '19000.00', '2014-05-12 11:36:44'),
(96, 'BUMBU', 'CABAI RAWIT SETAN', 'KG', '35000.00', '2014-05-12 11:36:44'),
(97, 'BUMBU', 'CENGKEH', 'KG', '120000.00', '2014-05-12 11:36:44'),
(98, 'BUMBU', 'DAUN JERUK PURUT', 'LEMBAR', '50.00', '2014-05-12 11:36:44'),
(99, 'BUMBU', 'DAUN KARI', 'LEMBAR', '50.00', '2014-05-12 11:36:44'),
(100, 'BUMBU', 'DAUN SALAM', 'LEMBAR', '50.00', '2014-05-12 11:36:44'),
(101, 'BUMBU', 'JAHE', 'KG', '15000.00', '2014-05-12 11:36:44'),
(102, 'BUMBU', 'JERUK NIPIS', 'KG', '15000.00', '2014-05-12 11:36:44'),
(103, 'BUMBU', 'JERUK PURUT', 'KG', '23000.00', '2014-05-12 11:36:44'),
(104, 'BUMBU', 'JINTEN', 'KG', '30000.00', '2014-05-12 11:36:44'),
(105, 'BUMBU', 'KAPULAGA', 'KG', '70000.00', '2014-05-12 11:36:44'),
(106, 'BUMBU', 'KAYU MANIS', 'KG', '28000.00', '2014-05-12 11:36:44'),
(107, 'BUMBU', 'KENCUR', 'KG', '25000.00', '2014-05-12 11:36:44'),
(108, 'BUMBU', 'KETUMBAR', 'KG', '50000.00', '2014-05-12 11:36:44'),
(109, 'BUMBU', 'KUNCI', 'KG', '24000.00', '2014-05-12 11:36:44'),
(110, 'BUMBU', 'LENGKUAS', 'KG', '5000.00', '2014-05-12 11:36:44'),
(111, 'BUMBU', 'MERICA BUTIR', 'KG', '75000.00', '2014-05-12 11:36:44'),
(112, 'BUMBU', 'MERICA BUBUK', 'KG', '100000.00', '2014-05-12 11:36:44'),
(113, 'BUMBU', 'ROSEMARY', 'KG', '90000.00', '2014-05-12 11:36:44'),
(114, 'BUMBU', 'SEREH', 'KG', '4000.00', '2014-05-12 11:36:44'),
(115, 'BUMBU', 'THYME', 'KG', '80000.00', '2014-05-12 11:36:44'),
(116, 'DAGING', 'DAGING SAPI', 'KG', '90000.00', '2014-05-12 11:36:44'),
(117, 'DAGING', 'DAGING HAS DALAM', 'KG', '100000.00', '2014-05-12 11:36:44'),
(118, 'DAGING', 'DAGING HAS LUAR', 'KG', '90000.00', '2014-05-12 11:36:45'),
(119, 'DAGING', 'DAGING SAMPIL', 'KG', '80000.00', '2014-05-12 11:36:45'),
(120, 'DAGING', 'JEROHAN', 'KG', '30000.00', '2014-05-12 11:36:45'),
(121, 'DAGING', 'HATI', 'KG', '50000.00', '2014-05-12 11:36:45'),
(122, 'DAGING', 'KIKIL', 'KG', '20000.00', '2014-05-12 11:36:45'),
(123, 'DAGING', 'DAGING KAMBING', 'KG', '120000.00', '2014-05-12 11:36:45'),
(124, 'DAGING', 'BALUNGAN', 'KG', '18000.00', '2014-05-12 11:36:45'),
(125, 'UNGGAS', 'AYAM KAMPUNG', 'KG', '40000.00', '2014-05-12 11:36:45'),
(126, 'UNGGAS', 'PUYUH', 'KG', '50000.00', '2014-05-12 11:36:45'),
(127, 'UNGGAS', 'DAGING AYAM', 'KG', '25000.00', '2014-05-12 11:36:45'),
(128, 'UNGGAS', 'DAGING AYAM FILLET', 'KG', '30000.00', '2014-05-12 11:36:45'),
(129, 'UNGGAS', 'SAYAP', 'KG', '20000.00', '2014-05-12 11:36:45'),
(130, 'UNGGAS', 'CEKER', 'KG', '10000.00', '2014-05-12 11:36:45'),
(131, 'UNGGAS', 'KEPALA', 'KG', '12000.00', '2014-05-12 11:36:45'),
(132, 'UNGGAS', 'JEROHAN', 'KG', '16000.00', '2014-05-12 11:36:45'),
(133, 'IKAN', 'BANDENG', 'KG', '20000.00', '2014-05-12 11:36:45'),
(134, 'IKAN', 'BANDENG PRESTO', 'EKOR', '5000.00', '2014-05-12 11:36:45'),
(135, 'IKAN', 'BAWAL', 'KG', '15000.00', '2014-05-12 11:36:45'),
(136, 'IKAN', 'BELUT', 'KG', '20000.00', '2014-05-12 11:36:45'),
(137, 'IKAN', 'CAKALANG', 'KG', '15000.00', '2014-05-12 11:36:45'),
(138, 'IKAN', 'CUMI', 'KG', '20000.00', '2014-05-12 11:36:45'),
(139, 'IKAN', 'GURAME', 'KG', '14000.00', '2014-05-12 11:36:45'),
(140, 'IKAN', 'IKAN ASIN', 'KG', '20000.00', '2014-05-12 11:36:45'),
(141, 'IKAN', 'KEPITING', 'KG', '75000.00', '2014-05-12 11:36:45'),
(142, 'IKAN', 'KERANG KUPAS', 'KG', '25000.00', '2014-05-12 11:36:45'),
(143, 'IKAN', 'KERAPU', 'KG', '35000.00', '2014-05-12 11:36:45'),
(144, 'IKAN', 'LELE', 'KG', '14000.00', '2014-05-12 11:36:45'),
(145, 'IKAN', 'NILA', 'KG', '15000.00', '2014-05-12 11:36:45'),
(146, 'IKAN', 'TERI PUTIH', 'KG', '45000.00', '2014-05-12 11:36:45'),
(147, 'IKAN', 'TERI ', 'KG', '45000.00', '2014-05-12 11:36:45'),
(148, 'IKAN', 'UDANG', 'KG', '60000.00', '2014-05-12 11:36:45'),
(149, 'TELUR', 'TELUR AYAM RAS', 'KG', '14000.00', '2014-05-12 11:36:45'),
(150, 'TELUR', 'TELUR AYAM KAMPUNG', 'KG', '25000.00', '2014-05-12 11:36:45'),
(151, 'TELUR', 'TELUR PUYUH', 'KG', '18000.00', '2014-05-12 11:36:45'),
(152, 'SUSU', 'SUSU SEGAR', 'L', '6000.00', '2014-05-12 11:36:45'),
(153, 'SUSU', 'SUSU SEGAR KEMASAN', 'L', '14000.00', '2014-05-12 11:36:45'),
(154, 'SUSU', 'SUSU KENTAL MANIS', 'L', '30000.00', '2014-05-12 11:36:45'),
(155, 'SUSU', 'SUSU BUBUK FULL CREAM', 'KG', '75000.00', '2014-05-12 11:36:45'),
(156, 'SUSU', 'SUSU BUBUK SKIM', 'KG', '60000.00', '2014-05-12 11:36:45'),
(157, 'TEPUNG-TEPUNG AN', 'TEPUNG TERIGU', 'KG', '7000.00', '2014-05-12 11:36:45'),
(158, 'TEPUNG-TEPUNG AN', 'TEPUNG MAIZENA', 'KG', '10000.00', '2014-05-12 11:36:45'),
(159, 'TEPUNG-TEPUNG AN', 'TEPUNG SAGU', 'KG', '12000.00', '2014-05-12 11:36:45'),
(160, 'TEPUNG-TEPUNG AN', 'TEPUNG HUNKWE', 'KG', '10000.00', '2014-05-12 11:36:45'),
(161, 'TEPUNG-TEPUNG AN', 'TEPUNG BERAS', 'KG', '8000.00', '2014-05-12 11:36:46'),
(162, 'TEPUNG-TEPUNG AN', 'TEPUNGTAPIOKA', 'KG', '10000.00', '2014-05-12 11:36:46'),
(163, 'TEPUNG-TEPUNG AN', 'TEPUNG KETAN', 'KG', '10000.00', '2014-05-12 11:36:46'),
(164, 'TEPUNG-TEPUNG AN', 'TEPUNG BUMBU', 'KG', '17000.00', '2014-05-12 11:36:46'),
(165, 'TEPUNG-TEPUNG AN', 'TEPUNG ROTI', 'KG', '25000.00', '2014-05-12 11:36:46'),
(166, 'TEPUNG-TEPUNG AN', 'TEPUNG CUSTARD', 'KG', '20000.00', '2014-05-12 11:36:46'),
(167, 'BAHAN OLAHAN', 'ABON', 'KEMASAN', '12000.00', '2014-05-12 11:36:46'),
(168, 'BAHAN OLAHAN', 'BAKSO IKAN', 'KEMASAN', '4000.00', '2014-05-12 11:36:46'),
(169, 'BAHAN OLAHAN', 'BAKSO SAPI', 'KEMASAN', '4000.00', '2014-05-12 11:36:46'),
(170, 'BAHAN OLAHAN', 'BIHUN', 'KEMASAN', '3000.00', '2014-05-12 11:36:46'),
(171, 'BAHAN OLAHAN', 'BISKUIT', 'KEMASAN', '5000.00', '2014-05-12 11:36:46'),
(172, 'BAHAN OLAHAN', 'COKLAT BUBUK COKLAT BATANG', 'KG', '12000.00', '2014-05-12 11:36:46'),
(173, 'BAHAN OLAHAN', 'CREAMER', 'KG', '50000.00', '2014-05-12 11:36:46'),
(174, 'BAHAN OLAHAN', 'DAGING ASAP', 'LEMBAR', '2000.00', '2014-05-12 11:36:46'),
(175, 'BAHAN OLAHAN', 'GALANTIN', 'KEMASAN', '7000.00', '2014-05-12 11:36:46'),
(176, 'BAHAN OLAHAN', 'IKAN ASAP', 'BUAH', '2000.00', '2014-05-12 11:36:46'),
(177, 'BAHAN OLAHAN', 'KEJU', 'KG', '70000.00', '2014-05-12 11:36:46'),
(178, 'BAHAN OLAHAN', 'KELAPA PARUT', 'KEMASAN', '2000.00', '2014-05-12 11:36:46'),
(179, 'BAHAN OLAHAN', 'KEMBANG TAHU', 'KEMASAN', '5000.00', '2014-05-12 11:36:46'),
(180, 'BAHAN OLAHAN', 'KERUPUK BIASA', 'G', '7000.00', '2014-05-12 11:36:46'),
(181, 'BAHAN OLAHAN', 'KERUPUK UDANG', 'KG', '10000.00', '2014-05-12 11:36:46'),
(182, 'BAHAN OLAHAN', 'KORNET', 'KEMASAN', '12000.00', '2014-05-12 11:36:46'),
(183, 'BAHAN OLAHAN', 'MACRONI', 'KG', '10000.00', '2014-05-12 11:36:46'),
(184, 'BAHAN OLAHAN', 'MIE BASAH', 'KG', '5000.00', '2014-05-12 11:36:46'),
(185, 'BAHAN OLAHAN', 'MIE KERING', 'KEMASAN', '10000.00', '2014-05-12 11:36:46'),
(186, 'BAHAN OLAHAN', 'MINYAK GORENG CURAH', 'L', '10000.00', '2014-05-12 11:36:46'),
(187, 'BAHAN OLAHAN', 'MINYAK GORENG KEMASAN', 'L', '12500.00', '2014-05-12 11:36:46'),
(188, 'BAHAN OLAHAN', 'MINYAK SALAD', 'L', '20000.00', '2014-05-12 11:36:46'),
(189, 'BAHAN OLAHAN', 'MINYAK WIJEN', 'L', '35000.00', '2014-05-12 11:36:46'),
(190, 'BAHAN OLAHAN', 'NUGGET', 'KEMASAN', '15000.00', '2014-05-12 11:36:46'),
(191, 'BAHAN OLAHAN', 'ONCOM', 'KEMASAN', '3000.00', '2014-05-12 11:36:46'),
(192, 'BAHAN OLAHAN', 'OTAK-OTAK BANDENG', 'BUAH', '7500.00', '2014-05-12 11:36:46'),
(193, 'BAHAN OLAHAN', 'PASTA', 'KEMASAN', '4000.00', '2014-05-12 11:36:46'),
(194, 'BAHAN OLAHAN', 'ROLADE DAGING', 'BUAH', '10000.00', '2014-05-12 11:36:46'),
(195, 'BAHAN OLAHAN', 'ROTI TAWAR ', 'KEMASAN', '6000.00', '2014-05-12 11:36:46'),
(196, 'BAHAN OLAHAN', 'SANTAN KEMASAN', 'KEMASAN', '2000.00', '2014-05-12 11:36:46'),
(197, 'BAHAN OLAHAN', 'SARDEN', 'KEMASAN', '8000.00', '2014-05-12 11:36:46'),
(198, 'BAHAN OLAHAN', 'SOSIS', 'KEMASAN', '12000.00', '2014-05-12 11:36:46'),
(199, 'BAHAN OLAHAN', 'SOUN', 'KEMASAN', '3000.00', '2014-05-12 11:36:46'),
(200, 'BAHAN OLAHAN', 'TAHU PUTIH', 'KEMASAN', '5000.00', '2014-05-12 11:36:46'),
(201, 'BAHAN OLAHAN', 'TAHU KUNING', 'KEMASAN', '4000.00', '2014-05-12 11:36:46'),
(202, 'BAHAN OLAHAN', 'TAHU BAKSO', 'KEMASAN', '10000.00', '2014-05-12 11:36:46'),
(203, 'BAHAN OLAHAN', 'TEMPE', 'KEMASAN', '2500.00', '2014-05-12 11:36:46'),
(204, 'BAHAN OLAHAN', 'TEMPE GEMBUS', 'KEMASAN', '1000.00', '2014-05-12 11:36:46'),
(205, 'BAHAN OLAHAN', 'YOGHURT', 'KEMASAN', '5000.00', '2014-05-12 11:36:46'),
(206, 'BAHAN TAMBAHAN PANGAN', 'AGAR-AGAR ', 'KEMASAN', '2000.00', '2014-05-12 11:36:46'),
(207, 'BAHAN TAMBAHAN PANGAN', 'BAKING POWDER', 'KG', '25000.00', '2014-05-12 11:36:46'),
(208, 'BAHAN TAMBAHAN PANGAN', 'CUKA', 'L', '26000.00', '2014-05-12 11:36:46'),
(209, 'BAHAN TAMBAHAN PANGAN', 'GARAM', 'KG', '8000.00', '2014-05-12 11:36:47'),
(210, 'BAHAN TAMBAHAN PANGAN', 'GULA PASIR', 'KG', '11000.00', '2014-05-12 11:36:47'),
(211, 'BAHAN TAMBAHAN PANGAN', 'GULA JAWA', 'KG', '12500.00', '2014-05-12 11:36:47'),
(212, 'BAHAN TAMBAHAN PANGAN', 'KALDU BUBUK', 'KEMASAN', '500.00', '2014-05-12 11:36:47'),
(213, 'BAHAN TAMBAHAN PANGAN', 'KECAP ASIN', 'KEMASAN', '7000.00', '2014-05-12 11:36:47'),
(214, 'BAHAN TAMBAHAN PANGAN', 'KECAP MANIS', 'KEMASAN', '4000.00', '2014-05-12 11:36:47'),
(215, 'BAHAN TAMBAHAN PANGAN', 'KECAP PEDAS', 'KEMASAN', '5000.00', '2014-05-12 11:36:47'),
(216, 'BAHAN TAMBAHAN PANGAN', 'MAYYONAISSE', 'KEMASAN', '8000.00', '2014-05-12 11:36:47'),
(217, 'BAHAN TAMBAHAN PANGAN', 'MSG', 'KEMASAN', '2000.00', '2014-05-12 11:36:47'),
(218, 'BAHAN TAMBAHAN PANGAN', 'OVALET', 'KEMASAN', '2000.00', '2014-05-12 11:36:47'),
(219, 'BAHAN TAMBAHAN PANGAN', 'PASTA/ESSENCE', 'KEMASAN', '4000.00', '2014-05-12 11:36:47'),
(220, 'BAHAN TAMBAHAN PANGAN', 'PEMANIS BUATAN', 'KEMASAN', '2000.00', '2014-05-12 11:36:47'),
(221, 'BAHAN TAMBAHAN PANGAN', 'PETIS', 'KEMASAN', '4000.00', '2014-05-12 11:36:47'),
(222, 'BAHAN TAMBAHAN PANGAN', 'PEWARNA', 'KEMASAN', '1500.00', '2014-05-12 11:36:47'),
(223, 'BAHAN TAMBAHAN PANGAN', 'SAMBAL PECEL', 'KEMASAN', '2000.00', '2014-05-12 11:36:47'),
(224, 'BAHAN TAMBAHAN PANGAN', 'SAUS SAMBAL', 'KEMASAN', '4000.00', '2014-05-12 11:36:47'),
(225, 'BAHAN TAMBAHAN PANGAN', 'SAUS TIRAM', 'KEMASAN', '5000.00', '2014-05-12 11:36:47'),
(226, 'BAHAN TAMBAHAN PANGAN', 'SAUS TOMAT', 'KEMASAN', '4000.00', '2014-05-12 11:36:47'),
(227, 'BAHAN TAMBAHAN PANGAN', 'SODA KUE', 'KEMASAN', '40000.00', '2014-05-12 11:36:47'),
(228, 'BAHAN TAMBAHAN PANGAN', 'TERASI', 'KEMASAN', '300.00', '2014-05-12 11:36:47'),
(229, 'BAHAN TAMBAHAN PANGAN', 'VANILI', 'KEMASAN', '300.00', '2014-05-12 11:36:47'),
(230, 'BAHAN TAMBAHAN PANGAN', 'JELLY', 'KEMASAN', '2000.00', '2014-05-12 11:36:47'),
(231, 'BAHAN TAMBAHAN PANGAN', 'KOPI', 'KEMASAN', '1000.00', '2014-05-12 11:36:47'),
(232, 'BAHAN TAMBAHAN PANGAN', 'COKLAT BUBUK', 'KEMASAN', '10000.00', '2014-05-12 11:36:47'),
(233, 'BAHAN TAMBAHAN PANGAN', 'TEH', 'KEMASAN', '3000.00', '2014-05-12 11:36:47'),
(234, 'BAHAN TAMBAHAN PANGAN', 'AIR', 'LITER', '0.00', '2014-05-12 11:36:47');

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
(1, 'ALPUKAT', 4, 'BUAH', 1, 'KG'),
(2, 'APEL FUJI', 6, 'BUAH', 1, 'KG'),
(3, 'APEL MALANG', 12, 'BUAH', 1, 'KG'),
(4, 'BANDENG', 4, 'EKOR', 1, 'KG'),
(5, 'BAWAL', 3, 'EKOR', 1, 'KG'),
(6, 'BAWANG BOMBAY', 10, 'BUAH', 1, 'KG'),
(7, 'BAWANG MERAH', 100, 'BUAH', 1, 'KG'),
(8, 'BAWANG PUTIH', 100, 'BUAH', 1, 'KG'),
(9, 'BELIMBING', 5, 'BUAH', 1, 'KG'),
(10, 'BELUT', 10, 'EKOR', 1, 'KG'),
(11, 'BENGKUANG', 7, 'BUAH', 1, 'KG'),
(12, 'BIT', 8, 'BUAH', 1, 'KG'),
(13, 'CABAI HIJAU RAWIT', 200, 'BUAH', 1, 'KG'),
(14, 'CABAI HIJAU TEOPONG', 90, 'BUAH', 1, 'KG'),
(15, 'CABAI MERAH KERITING ', 120, 'BUAH', 1, 'KG'),
(16, 'CABAI MERAH TEROPONG', 90, 'BUAH', 1, 'KG'),
(17, 'CABAI RAWIT SETAN', 175, 'BUAH', 1, 'KG'),
(18, 'CAKALANG', 4, 'EKOR', 1, 'KG'),
(19, 'CEKER', 15, 'POTONG', 1, 'KG'),
(20, 'CENGKEH', 200, 'SDM', 1, 'KG'),
(21, 'COKLAT BUBUK COKLAT BATANG', 100, 'SDM', 1, 'KG'),
(22, 'CREAMER', 100, 'SDM', 1, 'KG'),
(23, 'CUKA', 200, 'SDM', 1, 'L'),
(24, 'DAGING AYAM', 8, 'POTONG', 1, 'KG'),
(25, 'DELIMA', 14, 'BUAH', 1, 'KG'),
(26, 'DONDONG', 12, 'BUAH', 1, 'KG'),
(27, 'GARAM', 65, 'SDM', 1, 'KG'),
(28, 'GULA JAWA', 16, 'BUAH', 1, 'KG'),
(29, 'GULA PASIR', 65, 'SDM', 1, 'KG'),
(30, 'GURAME', 3, 'EKOR', 1, 'KG'),
(31, 'JAGUNG KUNING', 3, 'BUAH', 1, 'KG'),
(32, 'JAGUNG MANIS', 4, 'BUAH', 1, 'KG'),
(33, 'JAHE', 66, 'RUAS', 1, 'KG'),
(34, 'JAMBU AIR', 15, 'BUAH', 1, 'KG'),
(35, 'JAMBU BIJI', 4, 'BUAH', 1, 'KG'),
(36, 'JERUK LEMON', 10, 'BUAH', 1, 'KG'),
(37, 'JERUK LOKAL', 8, 'BUAH', 1, 'KG'),
(38, 'JERUK NIPIS', 30, 'BUAH', 1, 'KG'),
(39, 'JERUK PURUT', 50, 'BUAH', 1, 'KG'),
(40, 'JERUK SUNKIS', 8, 'BUAH', 1, 'KG'),
(41, 'JINTEN', 200, 'SDM', 1, 'KG'),
(42, 'KAPULAGA', 140, 'BUAH', 1, 'KG'),
(43, 'KAYU MANIS', 70, 'BATANG', 1, 'KG'),
(44, 'KENCUR', 80, 'RUAS', 1, 'KG'),
(45, 'KENTANG', 12, 'BUAH', 1, 'KG'),
(46, 'KEPALA', 10, 'POTONG', 1, 'KG'),
(47, 'KEPITING', 5, 'EKOR', 1, 'KG'),
(48, 'KERAPU', 5, 'EKOR', 1, 'KG'),
(49, 'KETUMBAR', 200, 'SDM', 1, 'KG'),
(50, 'KIWI', 8, 'BUAH', 1, 'KG'),
(51, 'KOL', 2, 'BUAH', 1, 'KG'),
(52, 'KUNCI', 80, 'RUAS', 1, 'KG'),
(53, 'LABU KUNING', 0.5, 'BUAH', 1, 'KG'),
(54, 'LABU SIAM', 5, 'BUAH', 1, 'KG'),
(55, 'LELE', 6, 'EKOR', 1, 'KG'),
(56, 'LENGKUAS', 50, 'SDM', 1, 'KG'),
(57, 'LOBAK', 4, 'BUAH', 1, 'KG'),
(58, 'MANGGA', 3, 'BUAH', 1, 'KG'),
(59, 'MANGGIS', 10, 'BUAH', 1, 'KG'),
(60, 'MARKISA', 11, 'BUAH', 1, 'KG'),
(61, 'MELON', 0.5, 'BUAH', 1, 'KG'),
(62, 'MENTIMUN', 4, 'BUAH', 1, 'KG'),
(63, 'MERICA BUBUK', 200, 'SDM', 1, 'KG'),
(64, 'MERICA BUTIR', 200, 'SDM', 1, 'KG'),
(65, 'NANGKA KUPAS', 25, 'BUAH', 1, 'KG'),
(66, 'NILA', 4, 'EKOR', 1, 'KG'),
(67, 'PAPRIKA', 6, 'BUAH', 1, 'KG'),
(68, 'PARE', 5, 'BUAH', 1, 'KG'),
(69, 'PEPAYA', 0.5, 'BUAH', 1, 'KG'),
(70, 'PERSIK', 8, 'BUAH', 1, 'KG'),
(71, 'PUYUH', 4, 'EKOR', 1, 'KG'),
(72, 'RAMBUTAN', 24, 'BUAH', 1, 'KG'),
(73, 'ROSEMARY', 200, 'SDM', 1, 'KG'),
(74, 'SAUS TOMAT', 1111, 'IKAT', 11111, 'KG'),
(75, 'SAWO', 14, 'BUAH', 1, 'KG'),
(76, 'SAYAP', 10, 'POTONG', 1, 'KG'),
(77, 'SEMANGKA', 0.3, 'BUAH', 1, 'KG'),
(78, 'SEREH', 40, 'BATANG', 1, 'KG'),
(79, 'SIRSAK', 1, 'BUAH', 1, 'KG'),
(80, 'STRAWBERRY', 80, 'BUAH', 1, 'KG'),
(81, 'SUSU SEGAR', 5, 'GELAS', 1, 'L'),
(82, 'SUSU SEGAR KEMASAN', 5, 'GELAS', 1, 'L'),
(83, 'TELUR AYAM KAMPUNG', 10, 'BUTIR', 1, 'KG'),
(84, 'TELUR AYAM RAS', 8, 'BUTIR', 1, 'KG'),
(85, 'TEPUNG BERAS', 100, 'SDM', 1, 'KG'),
(86, 'TEPUNG BUMBU', 100, 'SDM', 1, 'KG'),
(87, 'TEPUNG CUSTARD', 100, 'SDM', 1, 'KG'),
(88, 'TEPUNG HUNKWE', 100, 'SDM', 1, 'KG'),
(89, 'TEPUNG KETAN', 100, 'SDM', 1, 'KG'),
(90, 'TEPUNG MAIZENA', 100, 'SDM', 1, 'KG'),
(91, 'TEPUNG ROTI', 100, 'SDM', 1, 'KG'),
(92, 'TEPUNG SAGU', 100, 'SDM', 1, 'KG'),
(93, 'TEPUNG TERIGU', 100, 'SDM', 1, 'KG'),
(94, 'TEPUNGTAPIOKA', 100, 'SDM', 1, 'KG'),
(95, 'TERONG', 10, 'BUAH', 1, 'KG'),
(96, 'TOMAT', 10, 'BUAH', 1, 'KG'),
(97, 'WORTEL', 10, 'IKAT', 1, 'G');

-- --------------------------------------------------------

--
-- Struktur dari tabel `master_inventaris_alat`
--

CREATE TABLE IF NOT EXISTS `master_inventaris_alat` (
  `no` int(11) NOT NULL,
  `namaalat` varchar(200) NOT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `master_inventaris_alat`
--

INSERT INTO `master_inventaris_alat` (`no`, `namaalat`) VALUES
(0, 'COBA');

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
('7208', 'BIAYA LAINNYA', '72'),
('8', 'Biaya Biaya', '0'),
('81', 'Biaya Tenaga Kerja', '8'),
('8101', 'Upah Langsung', '81'),
('8102', 'Upah Tidak Langsung', '81'),
('82', 'Biaya Operational', '8'),
('8201', 'Biaya Penjualan', '82'),
('8202', 'Biaya Umum', '82'),
('8203', 'Biaya Bahan Bakar', '82'),
('8204', 'Biaya Bahan Sewa', '82'),
('83', 'Biaya Overhead', '8'),
('8301', 'Bahan Tidak Langsung', '83'),
('8302', 'Biaya Penyusutan Alat', '83'),
('8303', 'Biaya Pemeliharaan dan Reparasi', '83'),
('8304', 'Biaya Asuransi', '83'),
('8305', 'Biaya Rupa-rupa', '83');

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
-- Ketidakleluasaan untuk tabel `detail_penjualan`
--
ALTER TABLE `detail_penjualan`
  ADD CONSTRAINT `detail_penjualan_ibfk_1` FOREIGN KEY (`trans_no`) REFERENCES `header_penjualan` (`trans_no`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `detail_pesanan`
--
ALTER TABLE `detail_pesanan`
  ADD CONSTRAINT `detail_pesanan_ibfk_1` FOREIGN KEY (`trans_no`) REFERENCES `header_pesanan` (`trans_no`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `detail_rencana_inventaris`
--
ALTER TABLE `detail_rencana_inventaris`
  ADD CONSTRAINT `detail_rencana_inventaris_ibfk_1` FOREIGN KEY (`trans_no`) REFERENCES `header_rencana_inventaris` (`trans_no`) ON DELETE CASCADE ON UPDATE CASCADE;

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
