CREATE TABLE pelanggan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100),
    no_hp VARCHAR(20)
);

CREATE TABLE layanan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    jenis VARCHAR(50),
    deskripsi VARCHAR(255),
    harga INT
);

CREATE TABLE transaksi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pelanggan INT,
    id_layanan INT,
    berat FLOAT,
    total_harga INT,
    status VARCHAR(20),
    FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id),
    FOREIGN KEY (id_layanan) REFERENCES layanan(id)
);