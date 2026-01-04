package id.ac.unpas.manajemenlaundry;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class PanelTransaksi extends JPanel {
    public JComboBox cbPelanggan, cbLayanan, cbStatus;
    public JTextField txtBerat;
    public JButton btnTambah, btnUbah, btnHapus, btnRefresh;
    public JTable tableTransaksi;
    public DefaultTableModel model;
    private int selectedId = -1;

    public PanelTransaksi() {
        setLayout(new BorderLayout());

        // input
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.add(new JLabel("Nama Pelanggan:"));
        cbPelanggan = new JComboBox();
        panelForm.add(cbPelanggan);

        panelForm.add(new JLabel("Jenis Layanan:"));
        cbLayanan = new JComboBox();
        panelForm.add(cbLayanan);

        panelForm.add(new JLabel("Berat Cucian (Kg):"));
        txtBerat = new JTextField();
        panelForm.add(txtBerat);

        panelForm.add(new JLabel("Status (Proses/Selesai):"));
        String[] statusOptions = {"Proses", "Selesai"};
        cbStatus = new JComboBox(statusOptions);
        panelForm.add(cbStatus);

        // tombol
        JPanel panelTombol = new JPanel(new FlowLayout());
        btnTambah = new JButton("Tambah transaksi"); 
        btnUbah = new JButton("Ubah Transaksi");
        btnHapus = new JButton("Hapus Riwayat");
        btnRefresh = new JButton("Refresh Data");
        panelTombol.add(btnTambah);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        panelTombol.add(btnRefresh);

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelForm, BorderLayout.NORTH);
        panelAtas.add(panelTombol, BorderLayout.CENTER);
        panelAtas.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(panelAtas, BorderLayout.NORTH);

        // Tabel
        model = new DefaultTableModel(new Object[]{"ID Transaksi", "Pelanggan", "Layanan", "Berat", "Total Harga", "Status"}, 0);
        tableTransaksi = new JTable(model);
        add(new JScrollPane(tableTransaksi), BorderLayout.CENTER);

        // ===== EVENT HANDLERS =====
        
        // Tombol Tambah
        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tambahTransaksi();
            }
        });

        // Tombol Ubah
        btnUbah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ubahTransaksi();
            }
        });

        // Tombol Hapus
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusTransaksi();
            }
        });

        // Tombol Refresh
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
                loadComboData();
                clearForm();
            }
        });

        // Klik pada tabel
        tableTransaksi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableTransaksi.getSelectedRow();
                if (row != -1) {
                    selectedId = Integer.parseInt(model.getValueAt(row, 0).toString());
                    
                    // Set combo pelanggan
                    String pelanggan = model.getValueAt(row, 1).toString();
                    for (int i = 0; i < cbPelanggan.getItemCount(); i++) {
                        if (cbPelanggan.getItemAt(i).toString().contains(pelanggan)) {
                            cbPelanggan.setSelectedIndex(i);
                            break;
                        }
                    }
                    
                    // Set combo layanan
                    String layanan = model.getValueAt(row, 2).toString();
                    for (int i = 0; i < cbLayanan.getItemCount(); i++) {
                        if (cbLayanan.getItemAt(i).toString().contains(layanan)) {
                            cbLayanan.setSelectedIndex(i);
                            break;
                        }
                    }

                    String status = model.getValueAt(row, 5).toString();
                    cbStatus.setSelectedItem(status);
                    
                    txtBerat.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        // Load data awal
        loadData();
        loadComboData();
    }

    // ===== CRUD METHODS =====
    
    private void tambahTransaksi() {
        if (cbPelanggan.getSelectedItem() == null || cbLayanan.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pelanggan dan Layanan harus dipilih!");
            return;
        }

        try {
            // Ambil ID dari combo box
            String selectedPelanggan = cbPelanggan.getSelectedItem().toString();
            int idPelanggan = Integer.parseInt(selectedPelanggan.split(" - ")[0]);
            
            String selectedLayanan = cbLayanan.getSelectedItem().toString();
            int idLayanan = Integer.parseInt(selectedLayanan.split(" - ")[0]);
            
            double berat = Double.parseDouble(txtBerat.getText());
            String status = cbStatus.getSelectedItem().toString();
            // Ambil harga layanan
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT harga FROM layanan WHERE id = " + idLayanan);
            
            double hargaPerKg = 0;
            if (rs.next()) {
                hargaPerKg = rs.getDouble("harga");
            }
            
            double totalHarga = berat * hargaPerKg;

            // Insert transaksi
            String sql = "INSERT INTO transaksi (id_pelanggan, id_layanan, berat, total_harga, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idPelanggan);
            pstmt.setInt(2, idLayanan);
            pstmt.setDouble(3, berat);
            pstmt.setDouble(4, totalHarga);
            pstmt.setString(5, status);
            
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Transaksi berhasil ditambahkan!\nTotal Harga: Rp " + totalHarga);
            loadData();
            clearForm();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Berat harus berupa angka!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menambah transaksi: " + e.getMessage());
        }
    }

    private void ubahTransaksi() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang akan diubah dari tabel!");
            return;
        }

        try {
            String selectedPelanggan = cbPelanggan.getSelectedItem().toString();
            int idPelanggan = Integer.parseInt(selectedPelanggan.split(" - ")[0]);
            
            String selectedLayanan = cbLayanan.getSelectedItem().toString();
            int idLayanan = Integer.parseInt(selectedLayanan.split(" - ")[0]);
            
            double berat = Double.parseDouble(txtBerat.getText());
            String status = cbStatus.getSelectedItem().toString();
            
            // Ambil harga layanan
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT harga FROM layanan WHERE id = " + idLayanan);
            
            double hargaPerKg = 0;
            if (rs.next()) {
                hargaPerKg = rs.getDouble("harga");
            }
            
            double totalHarga = berat * hargaPerKg;

            // Update transaksi
            String sql = "UPDATE transaksi SET id_pelanggan = ?, id_layanan = ?, berat = ?, total_harga = ?, status = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idPelanggan);
            pstmt.setInt(2, idLayanan);
            pstmt.setDouble(3, berat);
            pstmt.setDouble(4, totalHarga);
            pstmt.setString(5, status);
            pstmt.setInt(6, selectedId);
            
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Transaksi berhasil diubah!\nTotal Harga Baru: Rp " + totalHarga);
            loadData();
            clearForm();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Berat harus berupa angka!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah transaksi: " + e.getMessage());
        }
    }

    private void hapusTransaksi() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang akan dihapus dari tabel!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin menghapus transaksi ini?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = KoneksiDB.configDB();
                String sql = "DELETE FROM transaksi WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, selectedId);
                
                pstmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!");
                loadData();
                clearForm();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus transaksi: " + e.getMessage());
            }
        }
    }

    private void clearForm() {
        selectedId = -1;
        if (cbPelanggan.getItemCount() > 0) {
            cbPelanggan.setSelectedIndex(0);
        }
        if (cbLayanan.getItemCount() > 0) {
            cbLayanan.setSelectedIndex(0);
        }
        txtBerat.setText("");
        cbStatus.setSelectedIndex(0);   
        tableTransaksi.clearSelection();
    }

    // ===== LOAD DATA METHODS =====
    
    public void loadData() {
        model.setRowCount(0);
        try {
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            
            String sql = "SELECT t.id, p.nama AS nama_pelanggan, l.jenis AS nama_layanan, " +
                         "t.berat, t.total_harga, t.status " +
                         "FROM transaksi t " +
                         "JOIN pelanggan p ON t.id_pelanggan = p.id " +
                         "JOIN layanan l ON t.id_layanan = l.id";
            
            ResultSet res = stm.executeQuery(sql);

            while (res.next()) {
                model.addRow(new Object[] {
                    res.getString("id"),
                    res.getString("nama_pelanggan"),
                    res.getString("nama_layanan"),
                    res.getString("berat"),
                    res.getString("total_harga"),
                    res.getString("status")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Load Data Transaksi: " + e.getMessage());
        }
    }

    public void loadComboData() {
        try {
            cbPelanggan.removeAllItems();
            cbLayanan.removeAllItems();

            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();

            ResultSet resPel = stm.executeQuery("SELECT id, nama FROM pelanggan");
            while(resPel.next()) {
                String item = resPel.getString("id") + " - " + resPel.getString("nama");
                cbPelanggan.addItem(item);
            }

            ResultSet resLay = stm.executeQuery("SELECT id, jenis, harga FROM layanan");
            while(resLay.next()) {
                String item = resLay.getString("id") + " - " + resLay.getString("jenis") + 
                              " (Rp" + resLay.getString("harga") + ")";
                cbLayanan.addItem(item);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Load Data Combo: " + e.getMessage());
        }
    }
}