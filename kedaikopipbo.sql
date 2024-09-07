-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 06, 2024 at 04:49 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kedaikopipbo`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `total_harga_transaksi` ()   BEGIN
    SELECT SUM(keranjang.jumlah * keranjang.harga) AS total_harga
    FROM keranjang;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

CREATE TABLE `kategori` (
  `id_kategori` int(11) NOT NULL,
  `nama_kategori` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kategori`
--

INSERT INTO `kategori` (`id_kategori`, `nama_kategori`) VALUES
(1, 'makanan'),
(2, 'minuman'),
(3, 'snack');

-- --------------------------------------------------------

--
-- Table structure for table `keranjang`
--

CREATE TABLE `keranjang` (
  `id_pesanan` int(11) NOT NULL,
  `id_produk` int(11) DEFAULT NULL,
  `tgl_pesanan` date DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `catatan` varchar(255) DEFAULT NULL,
  `harga` varchar(225) NOT NULL,
  `total_harga` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `nota2`
--

CREATE TABLE `nota2` (
  `id_nota` int(11) NOT NULL,
  `kode_pesanan` varchar(50) NOT NULL,
  `tgl_pesanan` datetime NOT NULL,
  `id_user` varchar(50) NOT NULL,
  `nama_produk` varchar(50) NOT NULL,
  `harga` int(11) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `catatan` varchar(255) DEFAULT NULL,
  `harga_total` int(11) NOT NULL,
  `uang_user` int(11) NOT NULL,
  `uang_kembalian` int(11) NOT NULL,
  `jenis_pesanan` varchar(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nota2`
--

INSERT INTO `nota2` (`id_nota`, `kode_pesanan`, `tgl_pesanan`, `id_user`, `nama_produk`, `harga`, `jumlah`, `catatan`, `harga_total`, `uang_user`, `uang_kembalian`, `jenis_pesanan`, `created_at`, `created_by`) VALUES
(115, '20240816001', '2024-08-16 09:52:39', '31', 'teh obeng', 5000, 3, '', 15000, 50000, 5000, 'Take Away', '2024-08-16 09:52:39', 31),
(116, '20240816001', '2024-08-16 09:52:39', '31', 'nasi goreng', 10000, 3, 'yang pedas', 30000, 50000, 5000, 'Take Away', '2024-08-16 09:52:39', 31),
(117, '20240816002', '2024-08-16 10:16:34', '31', 'kwetiau', 15000, 3, '', 45000, 100000, 45000, 'Dine In', '2024-08-16 10:16:34', 31),
(118, '20240816002', '2024-08-16 10:16:34', '31', 'teh obeng', 5000, 2, 'kurang manis', 10000, 100000, 45000, 'Dine In', '2024-08-16 10:16:34', 31);

-- --------------------------------------------------------

--
-- Table structure for table `pesanan`
--

CREATE TABLE `pesanan` (
  `id_pesanan` int(11) NOT NULL,
  `tgl_pesanan` datetime DEFAULT NULL,
  `id_produk` int(11) DEFAULT NULL,
  `catatan` varchar(255) DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `harga` varchar(255) DEFAULT NULL,
  `total_harga` int(11) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `jenis_pesanan` varchar(20) DEFAULT NULL,
  `kode_pesanan` varchar(20) DEFAULT NULL,
  `status_bayar` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pesanan`
--

INSERT INTO `pesanan` (`id_pesanan`, `tgl_pesanan`, `id_produk`, `catatan`, `jumlah`, `harga`, `total_harga`, `status`, `jenis_pesanan`, `kode_pesanan`, `status_bayar`) VALUES
(114, '2024-08-29 00:00:00', 25, 'wedfw', 2, '15000', 30000, 'Done', 'Take Away', '20240903133708-002', 'belum Terbayar'),
(119, '2024-09-03 00:00:00', 25, '', 1, '15000', 15000, 'Done', 'Take Away', '20240903133708-001', 'Terbayar'),
(120, '2024-09-03 00:00:00', 23, '', 1, '10000', 10000, 'Done', 'Take Away', '20240903133708-001', 'Terbayar'),
(121, '2024-09-04 00:00:00', 23, 'yang pedas ya', 2, '10000', 20000, 'Done', 'Take Away', '20240904134908-001', 'Terbayar'),
(122, '2024-09-06 00:00:00', 23, 'yang pedas', 1, '10000', 10000, 'Not Yet', 'Take Away', '20240906212158-001', 'Terbayar'),
(123, '2024-09-06 00:00:00', 24, 'kurang es', 1, '5000', 5000, 'Not Yet', 'Take Away', '20240906212158-001', 'Terbayar');

--
-- Triggers `pesanan`
--
DELIMITER $$
CREATE TRIGGER `after_insert_pesanan` AFTER INSERT ON `pesanan` FOR EACH ROW BEGIN
    DELETE FROM keranjang WHERE id_produk = NEW.id_produk;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `keluar` AFTER INSERT ON `pesanan` FOR EACH ROW update produk set stok=stok-new.jumlah
where id_produk=new.id_produk
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `produk`
--

CREATE TABLE `produk` (
  `id_produk` int(11) NOT NULL,
  `nama_produk` varchar(50) DEFAULT NULL,
  `harga` int(11) DEFAULT NULL,
  `deskripsi` varchar(50) DEFAULT NULL,
  `stok` int(11) DEFAULT NULL,
  `nama_kategori` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produk`
--

INSERT INTO `produk` (`id_produk`, `nama_produk`, `harga`, `deskripsi`, `stok`, `nama_kategori`) VALUES
(23, 'nasi gorenggg', 10000, 'telur, sayur, sosis', 2, 'makanan'),
(24, 'teh obeng', 5000, 'dingin, manis', 24, 'minuman'),
(25, 'kwetiau goreng', 15000, 'telur, sayur, ayam', 9, 'makanan'),
(27, 'kukubima', 5000, 'manis', 9, 'minuman');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id_user` int(11) NOT NULL,
  `nama_user` varchar(50) DEFAULT NULL,
  `alamat` varchar(50) DEFAULT NULL,
  `nomor_hp` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `level` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `nama_user`, `alamat`, `nomor_hp`, `password`, `level`) VALUES
(18, 'admin', 'winner', 'admin', '827ccb0eea8a706c4c34a16891f84e7b', 1),
(31, 'waitress', 'waitress', 'waitress', '827ccb0eea8a706c4c34a16891f84e7b', 2),
(32, 'dapur', 'dapur', 'dapur', '827ccb0eea8a706c4c34a16891f84e7b', 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`id_kategori`);

--
-- Indexes for table `keranjang`
--
ALTER TABLE `keranjang`
  ADD PRIMARY KEY (`id_pesanan`) USING BTREE;

--
-- Indexes for table `nota2`
--
ALTER TABLE `nota2`
  ADD PRIMARY KEY (`id_nota`);

--
-- Indexes for table `pesanan`
--
ALTER TABLE `pesanan`
  ADD PRIMARY KEY (`id_pesanan`);

--
-- Indexes for table `produk`
--
ALTER TABLE `produk`
  ADD PRIMARY KEY (`id_produk`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kategori`
--
ALTER TABLE `kategori`
  MODIFY `id_kategori` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `keranjang`
--
ALTER TABLE `keranjang`
  MODIFY `id_pesanan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=219;

--
-- AUTO_INCREMENT for table `nota2`
--
ALTER TABLE `nota2`
  MODIFY `id_nota` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=119;

--
-- AUTO_INCREMENT for table `pesanan`
--
ALTER TABLE `pesanan`
  MODIFY `id_pesanan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=124;

--
-- AUTO_INCREMENT for table `produk`
--
ALTER TABLE `produk`
  MODIFY `id_produk` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
