package id.ac.unpas.manajemenlaundry;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelRiwayat extends JPanel {
    public JButton btnHapus, btnRefresh;
    public JTable tableRiwayat;
    public DefaultTableModel model;
    private int selectedId = -1;

    public PanelRiwayat() {
        setLayout(new BorderLayout());

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRefresh = new JButton("Refresh Data");
        btnHapus = new JButton("Hapus Data");
        
        panelTombol.add(btnRefresh);
        panelTombol.add(btnHapus);

        add(panelTombol, BorderLayout.NORTH);

        // --- Tabel Data ---
        model = new DefaultTableModel(new Object[]{"ID", "Pelanggan", "Layanan", "Berat", "Total Harga", "Status"}, 0);
        tableRiwayat = new JTable(model);
        add(new JScrollPane(tableRiwayat), BorderLayout.CENTER);

        // --- Event Handling ---
        
        tableRiwayat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableRiwayat.getSelectedRow();
                if (row != -1) {
                    selectedId = Integer.parseInt(model.getValueAt(row, 0).toString());
                }
            }
        });

        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusData();
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        selectedId = -1; 
        try {
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            
            String sql = "SELECT t.id, p.nama AS nama_pelanggan, l.jenis AS nama_layanan, " +
                         "t.berat, t.total_harga, t.status " +
                         "FROM transaksi t " +
                         "JOIN pelanggan p ON t.id_pelanggan = p.id " +
                         "JOIN layanan l ON t.id_layanan = l.id " +
                         "WHERE t.status = 'Selesai'";
            
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
            JOptionPane.showMessageDialog(this, "Gagal Load Riwayat: " + e.getMessage());
        }
    }

    private void hapusData() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data riwayat yang ingin dihapus!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin menghapus history ini secara permanen?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = KoneksiDB.configDB();
                String sql = "DELETE FROM transaksi WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, selectedId);
                pstmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Riwayat berhasil dihapus.");
                loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal hapus: " + e.getMessage());
            }
        }
    }
}
