package id.ac.unpas.manajemenlaundry;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class FrameLogin extends JFrame{
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnCancel;

    public FrameLogin() {
        setTitle("Login");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel panelLogin = new JPanel(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panelForm.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        panelForm.add(txtUsername);

        panelForm.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panelForm.add(txtPassword);

        JPanel panelWrapper = new JPanel(new BorderLayout());
        panelWrapper.add(panelForm, BorderLayout.NORTH);

        JPanel panelTombol = new JPanel(new FlowLayout());
        btnLogin = new JButton("Login");
        btnCancel = new JButton("Batal");

        panelTombol.add(btnLogin);
        panelTombol.add(btnCancel);

        JLabel lblJudul = new JLabel("Kamu Siapa?", SwingConstants.CENTER);

        panelLogin.add(lblJudul, BorderLayout.NORTH);
        panelLogin.add(panelForm, BorderLayout.CENTER);
        panelLogin.add(panelTombol, BorderLayout.SOUTH);

        tabbedPane.addTab("Login", panelLogin);
        tabbedPane.addTab("Register", new PanelRegister());  
        add(tabbedPane);

        
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login();
            }
        });

        btnCancel.addActionListener(e -> System.exit(0));
        
        getRootPane().setDefaultButton(btnLogin);
    }

    //ini buat login mase
    private void Login() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi username sama passwordnya mase");
            return;
        }

        try {
            Connection conn = KoneksiDB.configDB();
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat Bekerja mase");
                this.dispose(); 
                new ManajemenLaundryApp(); 
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Passwordnya salah mase", "Login Gagal mase", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error Database: " + e.getMessage());
        }

    }

    

      public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // buat tampilan nyesuaiin sama OS biar enak diliat -Harits
        } catch (Exception e) {
        }
        new FrameLogin().setVisible(true);;
    }
}