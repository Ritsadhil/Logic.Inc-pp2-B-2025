package id.ac.unpas.manajemenlaundry;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PanelRegister extends JPanel {
    private JTextField txtUsername;
    private JPasswordField txtPassword, txtKode;
    private JButton btnSimpan;
    private final String KODE = "monokotil";

    public PanelRegister() {
        setLayout(new BorderLayout());
        JLabel lblJudul = new JLabel("Buat Akun Baru", SwingConstants.CENTER);
        lblJudul.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panelForm.add(new JLabel("Username :"));
        txtUsername = new JTextField();
        panelForm.add(txtUsername);

        panelForm.add(new JLabel("Password :"));
        txtPassword = new JPasswordField();
        panelForm.add(txtPassword);

        panelForm.add(new JLabel("Kode Keamanan :"));
        txtKode = new JPasswordField();
        panelForm.add(txtKode);



        JPanel panelTombol = new JPanel(new FlowLayout());
        btnSimpan = new JButton("Daftar Sekarang");

        panelTombol.add(btnSimpan);

        btnSimpan.addActionListener(e -> Daftar());

        add(lblJudul, BorderLayout.NORTH);
        add(panelTombol, BorderLayout.SOUTH);
        add(panelForm, BorderLayout.CENTER);
    }


    //ini buat daftar
    private void Daftar() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi semua kolom mase");
            return;
        }

        if (!new String(txtKode.getPassword()).equals(KODE)) {
            JOptionPane.showMessageDialog(this, "Kode Keamanan SALAH!", "Akses Ditolak", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = KoneksiDB.configDB();
            String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registrasi Sukses! Silakan pindah ke Tab Login.");
            
            txtUsername.setText("");
            txtPassword.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Daftar: " + e.getMessage());
        }
    }
}