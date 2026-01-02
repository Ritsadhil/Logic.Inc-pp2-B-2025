package id.ac.unpas.manajemenlaundry;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
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
        public JComboBox cbPelanggan, cbLayanan;
        public JTextField txtBerat, txtStatus;
        public JButton btnTambah, btnUbah, btnHapus, btnRefresh;
        public JTable tableTransaksi;
        public DefaultTableModel model;

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
             txtStatus = new JTextField();
            panelForm.add(txtStatus);

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

            btnRefresh.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
              loadData();
             loadComboData();
            }
            });
            
            loadData();
        }

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
