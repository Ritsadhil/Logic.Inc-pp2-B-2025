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

    private JTextField txtJenis, txtDeskripsi, txtHarga;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear;
    private JTable tableLayanan;
    private DefaultTableModel model;

    public PanelLayanan() {
        setLayout(new BorderLayout());

        // ===== FORM =====
        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 10));
        panelForm.add(new JLabel("Nama Layanan"));
        txtJenis = new JTextField();
        panelForm.add(txtJenis);

        panelForm.add(new JLabel("Deskripsi"));
        txtDeskripsi = new JTextField();
        panelForm.add(txtDeskripsi);

        panelForm.add(new JLabel("Harga (Per KG)"));
        txtHarga = new JTextField();
        panelForm.add(txtHarga);

        // ===== TOMBOL =====
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

        // ===== TABLE =====
        model = new DefaultTableModel(
                new Object[]{"ID", "Nama Layanan", "Deskripsi", "Harga"}, 0
        );
        tableLayanan = new JTable(model);
        add(new JScrollPane(tableLayanan), BorderLayout.CENTER);

        // ===== EVENT =====
        btnSimpan.addActionListener(e -> simpanData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnClear.addActionListener(e -> clearForm());

        tableLayanan.getSelectionModel().addListSelectionListener(e -> isiForm());

        loadData();
    }

    // ================= LOAD DATA =================
    private void loadData() {
        model.setRowCount(0);
        try {
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM layanan");

            while (res.next()) {
                model.addRow(new Object[]{
                        res.getString("id"),
                        res.getString("jenis"),
                        res.getString("deskripsi"),
                        res.getString("harga")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage());
        }
    }

    // ================= SIMPAN =================
    private void simpanData() {
        try {
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                "INSERT INTO layanan (jenis, deskripsi, harga) VALUES ('"
                + txtJenis.getText() + "','"
                + txtDeskripsi.getText() + "','"
                + txtHarga.getText() + "')"
            );
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            loadData();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= UBAH =================
    private void ubahData() {
        int row = tableLayanan.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu");
            return;
        }

        String id = model.getValueAt(row, 0).toString();

        try {
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                "UPDATE layanan SET jenis='"
                + txtJenis.getText() + "', deskripsi='"
                + txtDeskripsi.getText() + "', harga='"
                + txtHarga.getText() + "' WHERE id='" + id + "'"
            );
            JOptionPane.showMessageDialog(this, "Data berhasil diubah");
            loadData();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= HAPUS =================
    private void hapusData() {
        int row = tableLayanan.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu");
            return;
        }

        String id = model.getValueAt(row, 0).toString();

        try {
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM layanan WHERE id='" + id + "'");
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
            loadData();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= ISI FORM =================
    private void isiForm() {
        int row = tableLayanan.getSelectedRow();
        if (row >= 0) {
            txtJenis.setText(model.getValueAt(row, 1).toString());
            txtDeskripsi.setText(model.getValueAt(row, 2).toString());
            txtHarga.setText(model.getValueAt(row, 3).toString());
        }
    }

    // ================= CLEAR =================
    private void clearForm() {
        txtJenis.setText("");
        txtDeskripsi.setText("");
        txtHarga.setText("");
        tableLayanan.clearSelection();
    }
}
