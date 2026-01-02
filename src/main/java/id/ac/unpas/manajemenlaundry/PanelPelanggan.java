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
    
}