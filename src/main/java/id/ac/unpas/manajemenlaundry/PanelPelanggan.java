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

public class PanelPelanggan extends JPanel {
    public JTextField txtNama, txtHP, txtCari;
    public JButton btnSimpan, btnUbah, btnHapus, btnClear, btnCari;
    public JTable tablePelanggan;
    public DefaultTableModel model;

    public PanelPelanggan() {
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 10));
        panelForm.add(new JLabel("Nama Pelanggan:"));
        txtNama = new JTextField();
        panelForm.add(txtNama);

        panelForm.add(new JLabel("No. Handphone:"));
        txtHP = new JTextField();
        panelForm.add(txtHP);

        JPanel panelTombol = new JPanel(new FlowLayout());
        btnSimpan = new JButton("Simpan");
        btnUbah = new JButton("Ubah");
        btnHapus = new JButton("Hapus");
        btnClear = new JButton("Clear Form");
        panelTombol.add(btnSimpan);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        panelTombol.add(btnClear);

        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtCari = new JTextField(20);
        btnCari = new JButton("Cari Data");
        panelCari.add(new JLabel("Cari Nama:"));
        panelCari.add(txtCari);
        panelCari.add(btnCari);

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelForm, BorderLayout.NORTH);
        panelAtas.add(panelTombol, BorderLayout.CENTER);
        panelAtas.add(panelCari, BorderLayout.SOUTH);
        panelAtas.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 

        add(panelAtas, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID", "Nama", "No HP"}, 0);
        tablePelanggan = new JTable(model);
        add(new JScrollPane(tablePelanggan), BorderLayout.CENTER);
        
        loadData();
        
        // ===== EVENT LISTENER =====
        btnSimpan.addActionListener(e -> simpanData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnClear.addActionListener(e -> clearForm());
        btnCari.addActionListener(e -> cariData());

        tablePelanggan.getSelectionModel().addListSelectionListener(e -> {
            int row = tablePelanggan.getSelectedRow();
            if (row != -1) {
                txtNama.setText(model.getValueAt(row, 1).toString());
                txtHP.setText(model.getValueAt(row, 2).toString());
            }
        });
        
    }

    public void loadData() {
        model.setRowCount(0);
        try {
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM pelanggan");

            while (res.next()) {
                model.addRow(new Object[] {
                    res.getString("id"),
                    res.getString("nama"),
                    res.getString("no_hp")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Load Data Pelanggan: " + e.getMessage());
        }
    }
    
     // ================= CREATE =================
    public void simpanData() {
        if (txtNama.getText().isEmpty() || txtHP.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan No HP tidak boleh kosong!");
            return;
        }

        try {
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                "INSERT INTO pelanggan (nama, no_hp) VALUES ('"
                + txtNama.getText() + "', '"
                + txtHP.getText() + "')"
            );

            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            clearForm();
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= UPDATE =================
    public void ubahData() {
        int row = tablePelanggan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah!");
            return;
        }

        String id = model.getValueAt(row, 0).toString();

        try {
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                "UPDATE pelanggan SET nama='"
                + txtNama.getText() + "', no_hp='"
                + txtHP.getText() + "' WHERE id=" + id
            );

            JOptionPane.showMessageDialog(this, "Data berhasil diubah");
            clearForm();
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= DELETE =================
    public void hapusData() {
        int row = tablePelanggan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin menghapus data?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String id = model.getValueAt(row, 0).toString();

            try {
                Connection conn = KoneksiDB.configDB();
                Statement stm = conn.createStatement();
                stm.executeUpdate("DELETE FROM pelanggan WHERE id=" + id);

                JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
                clearForm();
                loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    // ================= SEARCH =================
    public void cariData() {
        model.setRowCount(0);
        try {
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(
                "SELECT * FROM pelanggan WHERE nama LIKE '%" + txtCari.getText() + "%'"
            );

            while (res.next()) {
                model.addRow(new Object[]{
                    res.getString("id"),
                    res.getString("nama"),
                    res.getString("no_hp")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= CLEAR =================
    public void clearForm() {
        txtNama.setText("");
        txtHP.setText("");
        txtCari.setText("");
        tablePelanggan.clearSelection();
    }
}