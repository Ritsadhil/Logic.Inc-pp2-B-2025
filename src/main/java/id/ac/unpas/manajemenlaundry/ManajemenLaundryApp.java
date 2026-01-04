/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package id.ac.unpas.manajemenlaundry;


import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author Harits
 */
public class ManajemenLaundryApp extends JFrame {

  public ManajemenLaundryApp() {
       setTitle("Aplikasi Manajemen Laundry");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Kelola Pelanggan", new PanelPelanggan());
        tabbedPane.addTab("Kelola Layanan", new PanelLayanan());
        tabbedPane.addTab("Transaksi Laundry", new PanelTransaksi());
        tabbedPane.addTab("Riwayat Transaksi", new PanelRiwayat());

        add(tabbedPane);
        setVisible(true);
    }

}
