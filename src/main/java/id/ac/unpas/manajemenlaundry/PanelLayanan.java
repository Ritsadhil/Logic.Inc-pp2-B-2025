package id.ac.unpas.manajemenlaundry;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class PanelLayanan extends JPanel {
        public JTextField txtJenis, txtHarga, txtSatuan;
        public JButton btnSimpan, btnUbah, btnHapus, btnClear;
        public JTable tableLayanan;
        public DefaultTableModel model;

        public PanelLayanan() {
            setLayout(new BorderLayout());

            JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 10));
            panelForm.add(new JLabel("Nama Layanan:"));
            txtJenis = new JTextField();
            panelForm.add(txtJenis);

            panelForm.add(new JLabel("Deskripsi:"));
            txtHarga = new JTextField();
            panelForm.add(txtHarga);

            panelForm.add(new JLabel("Harga (Per KG):"));
            txtSatuan = new JTextField();
            panelForm.add(txtSatuan);

            JPanel panelTombol = new JPanel(new FlowLayout());
            btnSimpan = new JButton("Simpan");
            btnUbah = new JButton("Ubah");
            btnHapus = new JButton("Hapus");
            btnClear = new JButton("Clear");
            panelTombol.add(btnSimpan);
            panelTombol.add(btnUbah);
            panelTombol.add(btnHapus);
            panelTombol.add(btnClear);

            JPanel panelAtas = new JPanel(new BorderLayout());
            panelAtas.add(panelForm, BorderLayout.NORTH);
            panelAtas.add(panelTombol, BorderLayout.CENTER);
            panelAtas.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            add(panelAtas, BorderLayout.NORTH);

            model = new DefaultTableModel(new Object[]{"ID", "Nama Layanan", "Deskripsi", "Harga (Per KG)"}, 0);
            tableLayanan = new JTable(model);
            add(new JScrollPane(tableLayanan), BorderLayout.CENTER);

            loadData();
        }

        public void loadData() {
        model.setRowCount(0);
        try {
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM layanan");

            while (res.next()) {
                model.addRow(new Object[] {
                    res.getString("id"),
                    res.getString("jenis"),
                    res.getString("deskripsi"),
                    res.getString("harga")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Load Data Pelanggan: " + e.getMessage());
        }
    }
    }