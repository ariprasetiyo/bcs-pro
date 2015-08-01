/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;
//import bcs_pro.BebanPilihanObject;

/**
 *
 * @author LANTAI3
 */
public class Bsc_pro extends javax.swing.JFrame {
    
    private JDesktopPane desktop = new JDesktopPane();
    JButton ButtonResep, BDaftarHarga, BMasterBarang, BPemesanan, BRancanganHarga, BAlokasiWaktu, BResep, 
            BBelanja, BInventarisAlat, BPenjualan, BBiaya, BDaftarBarang, BConvertSatuan, DaftarInventarisAlat, Laporaan, BLaporanLabaRugi ;
   
        String IpServer = "localhost";
        String Database = "bsc_pro";
        String User     = "root";
        String Password = "C0b4dib4c4";
        //String Password = "";
        public Connection koneksi = null;
       
   public void KonekDatabase () {
           SistemPro.KoneksiDatabase Connect = new SistemPro.KoneksiDatabase();
           Connect.KonekDatabase();
       
    }
    
    /**
     * Creates new form Bsc_pro
     */
    public Bsc_pro() {
        initComponents();
        KonekDatabase ();
        /*
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Bsc_pro.class.getName()).log(Level.SEVERE, null, ex);
        }
        * */
        
        //desktop.setOpaque(true);
        setContentPane( desktop );
        ButtonTransaksi();
        ButtonLaporan();
        ButtonMasterBarang ();
        ButtonTransparant();
        AksiRancangan(); 
        Aksi ();
        
        /*
         * Set tranparasi jframe
         */
        /*
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setUndecorated(true);
    //this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        this.setUndecorated(true);
        this.setBackground(new Color(0, 0, 0, 0)); // alpha <1 = transparent
        this.setOpacity(0.9f);
        //this.setResizable(true);
        //Dimension xx = new Dimension(1000,700);
        //this.setPreferredSize(xx);
        //this.setSize(xx);
        
        * */
        
        //this.setMaximumSize(xx);
        
        //PembelianBahanPrint drawingPanel = new PembelianBahanPrint();
        //content.add(drawingPanel, BorderLayout.SOUTH);
        //content.setVisible(true);
        //pack();
        //drawingPanel.setVisible(true);
        //add(drawingPanel);
        //paintComponent(  desktop.getGraphics());
        
        this.setIconImage( new ImageIcon(LokasiGambar()).getImage() );
        //desktop.getGraphics();
        
       desktop.setBorder(new BackGroundImage());
       
       /*
        * Membuat menu tambahan
        */
       desktop.setLayout(new BorderLayout(100,100));
       
       
       setJStatusBar2(Center);      
       setJStatusBar(JPanelMenu); 
       
    }
    
    /*
     * Setting tata letak loakasi button 
     * YY = Jarang Atas Bwah menu kecil
     */
    int AtasMenuBesar       = 115, XMenuBesar = 80, JarakAnatarButtonBesar = 110;
    int AtasMenuKecilKecil  = 115, XMenuKecil =250, YY  = 47;
    int UkuranPanjangBesar  = 134, UkuranLebarBesar = 114;
    
    private void ButtonLaporan(){
        /*
         * Laporaan, BLaporanLabaRugi 
         */
       Laporaan = new SistemPro.ClButtonTransparant("");
       Laporaan.setHorizontalTextPosition(SwingConstants.CENTER);
       Laporaan.setVerticalTextPosition(SwingConstants.BOTTOM);
       Laporaan.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       Laporaan.setBounds(XMenuBesar, AtasMenuBesar + (JarakAnatarButtonBesar * 3 ), UkuranPanjangBesar, UkuranLebarBesar);
       Laporaan.setVisible(true);
       Laporaan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/UtamaLaporan.png")));
       add(Laporaan);
       
       /*
        * Button  BLaporanLabaRugi
        */
       BLaporanLabaRugi = new SistemPro.ClButtonTransparant("");
       BLaporanLabaRugi.setHorizontalTextPosition(SwingConstants.CENTER);
       BLaporanLabaRugi.setVerticalTextPosition(SwingConstants.BOTTOM);
       BLaporanLabaRugi.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       //BPemesanan.setBounds(790, 212, 220, 50);
       
       BLaporanLabaRugi.setBounds(XMenuKecil, AtasMenuKecilKecil + ( YY * 0), 210, 50);
       BLaporanLabaRugi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenuUtama/pngLabaRugi.png")));
       BLaporanLabaRugi.setVisible(false);
       add(BLaporanLabaRugi);
       
      
    }
    
    private void ButtonMasterBarang (){
        
       /*
        * Button Master Barang
        * BDaftarBarang, BConvertSatuan, DaftarInventarisAlats
        */
       BMasterBarang = new SistemPro.ClButtonTransparant("");
       BMasterBarang.setHorizontalTextPosition(SwingConstants.CENTER);
       BMasterBarang.setVerticalTextPosition(SwingConstants.BOTTOM);
       BMasterBarang.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       BMasterBarang.setBounds(XMenuBesar, AtasMenuBesar + (JarakAnatarButtonBesar * 2 ),  UkuranPanjangBesar, UkuranLebarBesar);
       BMasterBarang.setVisible(true);
       BMasterBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/UtamaDatabase.png")));
       add(BMasterBarang);
 
       
        /*
        * Button Daftar Pemesanan
        */
       BDaftarBarang = new SistemPro.ClButtonTransparant("");
       BDaftarBarang.setHorizontalTextPosition(SwingConstants.CENTER);
       BDaftarBarang.setVerticalTextPosition(SwingConstants.BOTTOM);
       BDaftarBarang.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       //BPemesanan.setBounds(790, 212, 220, 50);
       
       BDaftarBarang.setBounds(XMenuKecil, AtasMenuKecilKecil + ( YY * 0), 430, 50);
       BDaftarBarang.setVisible(true);
       BDaftarBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenuUtama/pngDaftarBarangDanHarga.png")));
       BDaftarBarang.setVisible(false);
       add(BDaftarBarang);
       
        /*
        * Button Daftar Belanja
        */
       BConvertSatuan = new SistemPro.ClButtonTransparant("");
       BConvertSatuan.setHorizontalTextPosition(SwingConstants.CENTER);
       BConvertSatuan.setVerticalTextPosition(SwingConstants.BOTTOM);
       BConvertSatuan.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       //JadipngpanahKuningDaftarPembelajaan
       BConvertSatuan.setBounds(XMenuKecil, AtasMenuKecilKecil + ( YY * 1), 430, 50);
       BConvertSatuan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenuUtama/pngPengaturanConvertSatuan.png")));
       BConvertSatuan.setVisible(false);
       add(BConvertSatuan);
       
       /*
        * Button Rancangan Harga
        */
       DaftarInventarisAlat = new SistemPro.ClButtonTransparant("");
       DaftarInventarisAlat.setHorizontalTextPosition(SwingConstants.CENTER);
       DaftarInventarisAlat.setVerticalTextPosition(SwingConstants.BOTTOM);
       DaftarInventarisAlat.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       //BRancanganHarga.setBounds(790, 325, 320, 50);
       DaftarInventarisAlat.setBounds(XMenuKecil, AtasMenuKecilKecil + ( YY * 2), 430, 50);
       DaftarInventarisAlat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenuUtama/pngDaftarInventarisAlat.png")));
       DaftarInventarisAlat.setVisible(false);
       add(DaftarInventarisAlat);

       Center = new SistemPro.ClPanelTransparant();
       
        
    }


    private void ButtonTransaksi(){
        /*
        * Membuat Transparant
        */
        int Awal_X_groups2 = 250;
        int Awal_X_groups1 = 80;
        
       /*
        * Button Daftar Harga
        * pngDaftarHarga
        */
       BDaftarHarga = new SistemPro.ClButtonTransparant("");
       BDaftarHarga.setHorizontalTextPosition(SwingConstants.CENTER);
       BDaftarHarga.setVerticalTextPosition(SwingConstants.BOTTOM);
       BDaftarHarga.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       BDaftarHarga.setBounds(XMenuBesar, AtasMenuBesar + (JarakAnatarButtonBesar * 1 ),  UkuranPanjangBesar, UkuranLebarBesar);
       BDaftarHarga.setVisible(true);
       BDaftarHarga.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/UtamaTransaksi.png")));
       add(BDaftarHarga);
       
      

        /*
        * Button Daftar Pemesanan
        * pngPesanan
        */
       BPenjualan = new SistemPro.ClButtonTransparant("");
       BPenjualan.setHorizontalTextPosition(SwingConstants.CENTER);
       BPenjualan.setVerticalTextPosition(SwingConstants.BOTTOM);
       BPenjualan.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       //BPemesanan.setBounds(790, 212, 220, 50);
       
       BPenjualan.setBounds(XMenuKecil, AtasMenuKecilKecil + ( YY * 0), 345, 50);
       BPenjualan.setVisible(true);
       BPenjualan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenuUtama/pngPenjualanPesanan.png")));
       BPenjualan.setVisible(false);
       add(BPenjualan);
       
        /*
        * Button Daftar Belanja
        */
       BBiaya = new SistemPro.ClButtonTransparant("");
       BBiaya.setHorizontalTextPosition(SwingConstants.CENTER);
       BBiaya.setVerticalTextPosition(SwingConstants.BOTTOM);
       BBiaya.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       //JadipngpanahKuningDaftarPembelajaan
       BBiaya.setBounds(XMenuKecil, AtasMenuKecilKecil + ( YY * 1), 345, 50);
       BBiaya.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenuUtama/pngBiayaBiaya.png")));
       BBiaya.setVisible(false);
       add(BBiaya);

       Center = new SistemPro.ClPanelTransparant();
       
       
        
    }
    private void ButtonTransparant(){
        /*
        * Membuat Transparant
        */
        int Awal_X_groups2 = 250;
        int Awal_X_groups1 = 80;
        
       /*
        * Button File Resep
        */
       ButtonResep = new SistemPro.ClButtonTransparant("");
       ButtonResep.setHorizontalTextPosition(SwingConstants.CENTER);
       ButtonResep.setVerticalTextPosition(SwingConstants.BOTTOM);
       ButtonResep.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly c
       ButtonResep.setBounds(XMenuBesar, AtasMenuBesar + (JarakAnatarButtonBesar * 0 ),  UkuranPanjangBesar, UkuranLebarBesar);
       ButtonResep.setVisible(true);
            ButtonResep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenuUtama/Rancangan2.png")));
       add(ButtonResep);
       
       /*
        * Button Daftar Pemesanan
        * pngPesanan
        */
       BResep = new SistemPro.ClButtonTransparant("");
       BResep.setHorizontalTextPosition(SwingConstants.CENTER);
       BResep.setVerticalTextPosition(SwingConstants.BOTTOM);
       BResep.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       //int AtasMenuKecilKecil  = 158, XMenuKecil =250, YY  = 57;
       //BPemesanan.setBounds(790, 212, 220, 50);
       
       BResep.setBounds(XMenuKecil, AtasMenuKecilKecil + ( YY * 0), 345, 50);
       BResep.setVisible(true);
       BResep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenuUtama/pngResep.png")));
       BResep.setVisible(false);
       add(BResep);
       
      
        /*
        * Button Daftar Pemesanan
        * pngPesanan
        */
       BPemesanan = new SistemPro.ClButtonTransparant("");
       BPemesanan.setHorizontalTextPosition(SwingConstants.CENTER);
       BPemesanan.setVerticalTextPosition(SwingConstants.BOTTOM);
       BPemesanan.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       //int AtasMenuKecilKecil  = 158, XMenuKecil =250, YY  = 57;
       //BPemesanan.setBounds(790, 212, 220, 50);
       
       BPemesanan.setBounds(XMenuKecil, AtasMenuKecilKecil + ( YY * 1), 345, 50);
       BPemesanan.setVisible(true);
       BPemesanan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenuUtama/pngPemesanan.png")));
       BPemesanan.setVisible(false);
       add(BPemesanan);
       
        /*
        * Button Daftar Belanja
        */
       BBelanja = new SistemPro.ClButtonTransparant("");
       BBelanja.setHorizontalTextPosition(SwingConstants.CENTER);
       BBelanja.setVerticalTextPosition(SwingConstants.BOTTOM);
       BBelanja.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       //JadipngpanahKuningDaftarPembelajaan
       BBelanja.setBounds(XMenuKecil, AtasMenuKecilKecil + ( YY * 2), 345, 50);
       
       //BBelanja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/JadipngpanahKuningDaftarPembelajaan.png")));
       BBelanja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenuUtama/pngDaftarPembelanjaan.png")));
       BBelanja.setVisible(false);
       add(BBelanja);
       
       /*
        * Button Rancangan Harga
        */
       BRancanganHarga = new SistemPro.ClButtonTransparant("");
       BRancanganHarga.setHorizontalTextPosition(SwingConstants.CENTER);
       BRancanganHarga.setVerticalTextPosition(SwingConstants.BOTTOM);
       BRancanganHarga.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       //BRancanganHarga.setBounds(790, 325, 320, 50);
       BRancanganHarga.setBounds(XMenuKecil, AtasMenuKecilKecil + ( YY * 3), 345, 50);
       BRancanganHarga.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenuUtama/pngRancanganHarga.png")));
       BRancanganHarga.setVisible(false);
       add(BRancanganHarga);
       
       /*
        * Button Rancangan Harga
        */
       BAlokasiWaktu = new SistemPro.ClButtonTransparant("");
       BAlokasiWaktu.setHorizontalTextPosition(SwingConstants.CENTER);
       BAlokasiWaktu.setVerticalTextPosition(SwingConstants.BOTTOM);
       BAlokasiWaktu.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       BAlokasiWaktu.setBounds(XMenuKecil, AtasMenuKecilKecil + ( YY * 4) , 345, 50);
       BAlokasiWaktu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenuUtama/pngAlokasiWaktu.png")));
       BAlokasiWaktu.setVisible(false);
       add(BAlokasiWaktu);
       
       /*
        * Button Inventaris ALat
        */
       BInventarisAlat = new SistemPro.ClButtonTransparant("");
       BInventarisAlat.setHorizontalTextPosition(SwingConstants.CENTER);
       BInventarisAlat.setVerticalTextPosition(SwingConstants.BOTTOM);
       BInventarisAlat.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       BInventarisAlat.setBounds(XMenuKecil, AtasMenuKecilKecil + ( YY * 5), 345, 50);
       BInventarisAlat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenuUtama/pngInvetarisALat.png")));
       BInventarisAlat.setVisible(false);
       add(BInventarisAlat);
       
       Center = new SistemPro.ClPanelTransparant();
            
       
        
    }

    private void setJStatusBar(JPanel statusbar) {
        add(statusbar,BorderLayout.NORTH);
    }
    private void setJStatusBar2(JPanel statusbar) {
        add(statusbar,BorderLayout.CENTER);
    }
    
    private String folder;
    private String LokasiGambar() {
        folder = System.getProperty("user.dir") + File.separator + "Gambar/logo.png";
        return folder ;
    }
    private String folder2;
    private String LokasiGambarStartDb() {
        folder2 = System.getProperty("user.dir") + File.separator + "GambarMenu/bgStartDatabase.jpg";
        return folder2 ;
    }
    
    private void AksiRancangan(){
        
        JMenuAcounting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                 JournalTransaksi();            
            }
        });
        
        BBelanja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                  PanggilMenuPembelianBahan();        
            }
        });
        
        BPemesanan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                 PanggilMenuPesananMakan();        
            }
        });

        BRancanganHarga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                 PanggilMenuBeban();        
            }
        });         
        
        BAlokasiWaktu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                PanggilMenuAlokasiWaktu();        
            }
        });
        
        
        BInventarisAlat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                PangilRencanaInventarisAlat();        
            }
        });
    }
    
    private void DisableButtonMain(boolean[] x ){
        /*
         * Rancangan
         */
        BResep.setVisible(x[0]);
        BRancanganHarga.setVisible(x[0]);
        BAlokasiWaktu.setVisible(x[0]);
        BInventarisAlat.setVisible(x[0]);
        BBelanja.setVisible(x[0]);
        BPemesanan.setVisible(x[0]);
           
        /*
         * Transaksi
         */
        BPenjualan.setVisible(x[1]);
        BBiaya.setVisible(x[1]);
        
        /*
         * Master Barang
         * BDaftarBarang, BConvertSatuan, DaftarInventarisAlat
         */
        BDaftarBarang.setVisible(x[2]);
        BConvertSatuan.setVisible(x[2]);
        DaftarInventarisAlat.setVisible(x[2]);
        
        /*
         * Laporan
         * Laporaan, BLaporanLabaRugi 
         */
        BLaporanLabaRugi.setVisible(x[3]);
        
    }
    
    private void Aksi () {
        BMasterBarang.addActionListener(new ActionListener () {
            @Override
           public void actionPerformed(ActionEvent e) {
                boolean[] data = {false, false, true, false};
                DisableButtonMain(data);
            }
        });
        Laporaan.addActionListener(new ActionListener () {
            @Override
           public void actionPerformed(ActionEvent e) {
                boolean[] data = {false, false, false, true};
                DisableButtonMain(data);
            }
        });
        ButtonResep.addActionListener(new ActionListener () {
            @Override
           public void actionPerformed(ActionEvent e) {
                boolean[] data = {true, false, false, false};
                DisableButtonMain(data);
            }
        });
        
        BDaftarHarga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                boolean[] data = {false, true, false, false};
                DisableButtonMain(data);
            }
        });
        
        /*
         *  BPenjualan, BBiaya, BDaftarBarang, BConvertSatuan, DaftarInventarisAlat, Laporaan, BLaporanLabaRugi 
         */
        BPenjualan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                PangilPenjualan();        
            }
        });
         BBiaya.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                JournalTransaksi();        
            }
        });
         BDaftarBarang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                MasterBarang();        
            }
        });
         BConvertSatuan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                PengaturanConvert();        
            }
        });
         DaftarInventarisAlat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                PanggilDaftarInventaris();
            }
        });
         BLaporanLabaRugi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                PanggilLaporanLabaRugi();       
            }
        });

        jMenuPenjualan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                PangilPenjualan();        
            }
        });
        JMenuDaftarInventaris.addActionListener(new ActionListener () {
            @Override
           public void actionPerformed(ActionEvent e) {
               PanggilDaftarInventaris();                  
            }
        });

        StartDatabase.addActionListener(new ActionListener () {
            @Override
           public void actionPerformed(ActionEvent e) {
               SistemPro.KoneksiDatabase K = new SistemPro.KoneksiDatabase();
               K. StartDatabase();                  
            }
        });
        
        JMenuResep.addActionListener(new ActionListener () {
            @Override
           public void actionPerformed(ActionEvent e) {
               PanggiMenuResep();                  
            }
        });
        JMenuBahanPembelian.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                PanggilMenuPembelianBahan();             
            }
        });
        
        /*
        JMenuSetupParameter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                PanggilMenuSetupProgram();             
            }
        });
        */
        JMenuAlokasiWaktu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                PanggilMenuAlokasiWaktu();        
            }
        });
        
        /*
        JMenuFixedAsset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                 PanggilMenuFixedAsset();        
            }
        });
        */
        JMenuItemPerancangan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                 PanggilMenuBeban();        
            }
        }); 
        
        PengaturanConvertSatuan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                 PengaturanConvert();        
            }
        }); 
        
        CaraMemasak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                 PanggilCaraMasak();        
            }
        });
        
        JMenuPesanInventaris.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                 PangilRencanaInventarisAlat();        
            }
        });
        
        BarangDanHarga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                 MasterBarang();        
            }
        });
        
        JMenuPesanaMakanan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                 PanggilMenuPesananMakan();        
            }
        });
        
        jMenuItemLabaRugi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                 PanggilLaporanLabaRugi();        
            }
        });
        
        BResep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                 PanggiMenuResep();        
            }
        });
    }
    
   private void PangilPenjualan() {
        POS B = new POS();
        setLayout(null);
        B.setVisible(true);

        try {                         
            add(B);
            
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            B.setSelected(true);
            B.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void PanggilHelp (){
        Help B = new Help();
        setLayout(null);
        B.setVisible(true);

        try {                         
            add(B);
            
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            B.setSelected(true);
            B.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void PanggilLaporanLabaRugi (){
        LaporanLabaRugi B = new LaporanLabaRugi();
        setLayout(null);
        B.setVisible(true);

        try {                         
            add(B);
            
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            B.setSelected(true);
            B.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void PanggilDaftarInventaris(){
        MasterInventarisAlat B = new MasterInventarisAlat();
        setLayout(null);
        B.setVisible(true);

        try {                         
            add(B);
            
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            B.setSelected(true);
            B.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void PanggilMenuPesananMakan(){
        PesananMakanan B = new PesananMakanan();
        setLayout(null);
        B.setVisible(true);

        try {                         
            add(B);
            
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            B.setSelected(true);
            B.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void PanggilMenuPesananMakanStatus(){
        PesananMakananStatus B = new PesananMakananStatus();
        setLayout(null);
        B.setVisible(true);

        try {                         
            add(B);
            
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            B.setSelected(true);
            B.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void PangilRencanaInventarisAlat(){
        RencanaInventarisAlat B = new RencanaInventarisAlat();
        setLayout(null);
        B.setVisible(true);

        try {                         
            add(B);
            
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            B.setSelected(true);
            B.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void  JournalTransaksi (){
        System.out.println("xxxx");
        JournalTransaksi B = new JournalTransaksi();
        setLayout(null);
        B.setVisible(true);
        System.out.println("YY");
        try {                         
            add(B);
            
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            B.setSelected(true);
            B.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void MasterBarang(){
        MasterBarang B = new MasterBarang();
        setLayout(null);
        B.setVisible(true);

        try {                         
            add(B);
            
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            B.setSelected(true);
            B.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void PengaturanConvert(){
        MasterBarangSatuanConvert B = new MasterBarangSatuanConvert();
        setLayout(null);
        B.setVisible(true);

        try {                         
            add(B);
            
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            B.setSelected(true);
            B.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void PanggilMenuBeban(){
         BebanRencanaMainTab B = new BebanRencanaMainTab();
                setLayout(null);
                B.setVisible(true);

                try {                         
                    add(B);

                    /*
                     * biar form berada di depan 
                     * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
                     */
                    B.setSelected(true);
                    B.moveToFront();
                } catch (PropertyVetoException ex) {
                    Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
    
    
   
    private void PanggilMenuSetupProgram(){
        SetupParameter SP = new SetupParameter();
        setLayout(null);
        SP.setVisible(true);

        try {                         
            add(SP);
            
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            SP.setSelected(true);
            SP.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void PanggilMenuFixedAsset(){
        InputFixedAsset FA = new InputFixedAsset();
        setLayout(null);
        FA.setVisible(true);

        try {                         
            add(FA);
            
            FA.setSelected(true);
            FA.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void PanggilMenuAlokasiWaktu(){
        AlokasiWaktu3 AW = new AlokasiWaktu3();

        setLayout(null);     
        AW.setVisible(true);
        
        /*
        for (int a = 1; a < 100; a++){
            
            try
                {//AW.pack();
                //AW.setSize(a, a);  
                
                
                
                Thread.sleep(10);  
                    //System.out.println( UkuranA += 10);
                }catch (InterruptedException ie)
                {
                System.out.println(ie.getMessage());
                }
        }
      */
        try {
            add(AW);
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            AW.setSelected(true);
            AW.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void PanggilMenuPembelianBahan(){
        
        PembelianBahan PB = new PembelianBahan();
        setLayout(null);
        PB.setVisible(true);

        try {                         
            add(PB);
            
            PB.setSelected(true);
            PB.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void PanggilCaraMasak(){
        
        CaraMasak PB = new CaraMasak();
        setLayout(null);
        PB.setVisible(true);

        try {                         
            add(PB);
            
            PB.setSelected(true);
            PB.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void PanggiMenuResep(){

            Resep Resep = new Resep();
            setLayout(null);
            Resep.setVisible(true);  
            //MoveToFront(PO);
            //moveToFront(PO);
            try { 
                add(Resep);
                Resep.setSelected(true);
                Resep.moveToFront();
            } catch (PropertyVetoException ex) {
                Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
     private static void TemaGelap() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
        // select Look and Feel
        try{
            Properties props = new Properties();
            props.put("logoString", "my company");
            props.put("textShadow", "off");
            props.put("systemTextFont", "Arial PLAIN 13");
            props.put("controlTextFont", "Arial PLAIN 13");
            props.put("menuTextFont", "Arial PLAIN 13");
            props.put("userTextFont", "Arial PLAIN 13");
            props.put("subTextFont", "Arial PLAIN 12");
            props.put("windowTitleFont", "Arial BOLD 13");
            com.jtattoo.plaf.hifi.HiFiLookAndFeel.setCurrentTheme(props);
            javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
             } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Bsc_pro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
      public static void TemaWarnaPutih () throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        try {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             * 
             * for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
             */
            //javax.swing.UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
             //javax.swing.UIManager.installLookAndFeel("SeaGlass", "com.seaglasslookandfeel.SeaGlassLookAndFeel");
            //("com.jtattoo.plaf.smart.SmartLookAndFeel");
            
               //javax.swing.UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
             // setup the look and feel properties
            
            
            
            /*
             * Look and feel warna putih
             */
            Properties props = new Properties();
            props.put("logoString", "my comsdsdpany");
//            props.put("logoString", "\u00A0");
            props.put("backgroundPattern", "on");

            /*
            props.put("windowTitleForegroundColor", "228 228 255");
            props.put("windowTitleBackgroundColor", "0 0 96");
            props.put("windowTitleColorLight", "0 0 96");
            props.put("windowTitleColorDark", "0 0 64");
            props.put("windowBorderColor", "96 96 64");

            props.put("windowInactiveTitleForegroundColor", "228 228 255");
            props.put("windowInactiveTitleBackgroundColor", "0 0 96");
            props.put("windowInactiveTitleColorLight", "0 0 96");
            props.put("windowInactiveTitleColorDark", "0 0 64");
            props.put("windowInactiveBorderColor", "32 32 128");
            */ 

            //props.put("menuForegroundColor", "228 228 255");
            //props.put("menuBackgroundColor", "0 0 64");
            //props.put("menuSelectionForegroundColor", "0 0 0");
            props.put("menuSelectionBackgroundColor", "255 192 48");
            //props.put("menuColorLight", "32 32 128");
            //props.put("menuColorDark", "16 16 96");
            
            props.put("toolbarColorLight", "32 32 128");
            props.put("toolbarColorDark", "16 16 96");

            //props.put("controlForegroundColor", "228 228 255");
            //props.put("controlBackgroundColor", "16 16 96");
            //props.put("controlColorLight", "16 16 96");
            //props.put("controlColorDark", "8 8 64");
            //props.put("controlHighlightColor", "32 32 128");
            props.put("controlShadowColor", "16 16 64");
            props.put("controlDarkShadowColor", "8 8 32");

            //props.put("buttonForegroundColor", "0 0 32");
            //props.put("buttonBackgroundColor", "196 196 196");
            props.put("buttonColorLight", "196 196 240");
            props.put("buttonColorDark", "164 164 228");
            

            

            //props.put("foregroundColor", "228 228 255");
            //props.put("backgroundColor", "0 0 64");
            //props.put("backgroundColorLight", "16 16 96");
            //props.put("backgroundColorDark", "8 8 64");
            //props.put("alterBackgroundColor", "255 0 0");
           // props.put("frameColor", "96 96 64");
            //props.put("gridColor", "96 96 64");
            
            props.put("focusCellColor", "240 0 0");

            //props.put("disabledForegroundColor", "128 128 164");
            //"223 243 247" biru
            props.put("disabledForegroundColor", "223 243 247");
            props.put("disabledBackgroundColor", "237 241 +242");

            //props.put("selectionForegroundColor", "0 0 0");
            //props.put("selectionBackgroundColor", "196 148 16");

            /*
            props.put("inputForegroundColor", "228 228 255");
            props.put("inputBackgroundColor", "0 0 96");
            */
            
            props.put("rolloverColor", "240 168 0");
            props.put("rolloverColorLight", "240 168 0");
            props.put("rolloverColorDark", "196 137 0");
           
            props.put("systemTextFont", "Arial PLAIN 13");
            props.put("controlTextFont", "Arial PLAIN 13");
            props.put("menuTextFont", "Arial PLAIN 13");
            props.put("userTextFont", "Arial PLAIN 13");
            props.put("subTextFont", "Arial PLAIN 12");
            props.put("windowTitleFont", "Arial BOLD 13");

            // set your theme
           com.jtattoo.plaf.mcwin.McWinLookAndFeel.setCurrentTheme(props);
            com.jtattoo.plaf.mcwin.McWinLookAndFeel.setCurrentTheme(props);
            javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Bsc_pro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
      private static void TemaAwal(){
           try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        /*
                         * Pada bagian tengajh adalah transparasi 
                         * untk header pingiran form
                         */
                        javax.swing.UIManager.put("nimbusBase", new Color(100, 30, 255));
                        
                        /*
                         * Tombol
                         * 40, 200, 255
                         * 200, 200, 255 => abu2
                         */
                        javax.swing.UIManager.put("nimbusBlueGrey", new Color(200, 230, 255));
                        
                        /*
                         * Background
                         */
                        javax.swing.UIManager.put("control", new Color(160,200, 255));
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(Bsc_pro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(Bsc_pro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(Bsc_pro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(Bsc_pro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
      }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JPanelMenu = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        StartDatabase = new javax.swing.JButton();
        Center = new javax.swing.JPanel();
        South = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        JMenuPembelianBahan = new javax.swing.JMenu();
        JMenuResep = new javax.swing.JMenuItem();
        CaraMemasak = new javax.swing.JMenuItem();
        JMenuBahanPembelian = new javax.swing.JMenuItem();
        JMenuPesanaMakanan = new javax.swing.JMenuItem();
        JMenuAlokasiWaktu = new javax.swing.JMenuItem();
        JMenuItemPerancangan = new javax.swing.JMenuItem();
        JMenuPesanInventaris = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuPenjualan = new javax.swing.JMenuItem();
        JMenuAcounting = new javax.swing.JMenuItem();
        JMenuMasterBarang = new javax.swing.JMenu();
        BarangDanHarga = new javax.swing.JMenuItem();
        PengaturanConvertSatuan = new javax.swing.JMenuItem();
        JMenuDaftarInventaris = new javax.swing.JMenuItem();
        JMenuMasterBarang2 = new javax.swing.JMenu();
        jMenuItemLabaRugi = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bcs_Pro");

        JPanelMenu = new javax.swing.JPanel()
        {
            protected void paintComponent(Graphics g)
            {
                g.drawImage(new ImageIcon(LokasiGambarStartDb()).getImage(), 15, 5, null);
                super.paintComponent(g);
            }
        }
        ;
        /*
        * Menghilangkan gairs
        */
        //JPanelMenu.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JPanelMenu.setPreferredSize(new java.awt.Dimension(0, 100));
        JPanelMenu.setOpaque(false);
        JPanelMenu.setLayout(null);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/LOGGOO.png"))); // NOI18N

        StartDatabase.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        StartDatabase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/networkserverdatabasekecill.png"))); // NOI18N
        StartDatabase.setText("Start Database");

        javax.swing.GroupLayout JPanelMenuLayout = new javax.swing.GroupLayout(JPanelMenu);
        JPanelMenu.setLayout(JPanelMenuLayout);
        JPanelMenuLayout.setHorizontalGroup(
            JPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelMenuLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(StartDatabase)
                .addGap(30, 30, 30))
        );
        JPanelMenuLayout.setVerticalGroup(
            JPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StartDatabase)
                .addContainerGap(48, Short.MAX_VALUE))
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout CenterLayout = new javax.swing.GroupLayout(Center);
        Center.setLayout(CenterLayout);
        CenterLayout.setHorizontalGroup(
            CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        CenterLayout.setVerticalGroup(
            CenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout SouthLayout = new javax.swing.GroupLayout(South);
        South.setLayout(SouthLayout);
        SouthLayout.setHorizontalGroup(
            SouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        SouthLayout.setVerticalGroup(
            SouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        jMenuBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JMenuPembelianBahan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/keciltransaksi.png"))); // NOI18N
        JMenuPembelianBahan.setText("Rencana Kerja");
        JMenuPembelianBahan.setToolTipText("");
        JMenuPembelianBahan.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N

        JMenuResep.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuResep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/kecilResep.png"))); // NOI18N
        JMenuResep.setText("Buat Resep");
        JMenuPembelianBahan.add(JMenuResep);

        CaraMemasak.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        CaraMemasak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/kecilinventarisalat.png"))); // NOI18N
        CaraMemasak.setText("Cara Memasak");
        JMenuPembelianBahan.add(CaraMemasak);

        JMenuBahanPembelian.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuBahanPembelian.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/kecilpembelian.png"))); // NOI18N
        JMenuBahanPembelian.setText("Pembelian Bahan");
        JMenuPembelianBahan.add(JMenuBahanPembelian);

        JMenuPesanaMakanan.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuPesanaMakanan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/kecilPesanan Makanpng.png"))); // NOI18N
        JMenuPesanaMakanan.setText("Pesanan Makanan");
        JMenuPembelianBahan.add(JMenuPesanaMakanan);

        JMenuAlokasiWaktu.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuAlokasiWaktu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/KecilAlokasiWaktu.png"))); // NOI18N
        JMenuAlokasiWaktu.setText("Alokasi Waktu");
        JMenuPembelianBahan.add(JMenuAlokasiWaktu);

        JMenuItemPerancangan.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuItemPerancangan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/kecilrancanganharga.png"))); // NOI18N
        JMenuItemPerancangan.setText("Rancangan Harga");
        JMenuPembelianBahan.add(JMenuItemPerancangan);

        JMenuPesanInventaris.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuPesanInventaris.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/kecilinventarisalat2.png"))); // NOI18N
        JMenuPesanInventaris.setText("Rencana Inventaris Alat");
        JMenuPembelianBahan.add(JMenuPesanInventaris);

        jMenuBar1.add(JMenuPembelianBahan);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/marketing_icon_trustKecil.png"))); // NOI18N
        jMenu2.setText("Transaksi");
        jMenu2.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N

        jMenuPenjualan.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        jMenuPenjualan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/cash-register-iconKecil.png"))); // NOI18N
        jMenuPenjualan.setText("Penjualan Pesanan");
        jMenu2.add(jMenuPenjualan);

        JMenuAcounting.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuAcounting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/kecilmasterharga.png"))); // NOI18N
        JMenuAcounting.setText("Biaya");
        jMenu2.add(JMenuAcounting);

        jMenuBar1.add(jMenu2);

        JMenuMasterBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/kecilMasterBarang.png"))); // NOI18N
        JMenuMasterBarang.setText("Database");
        JMenuMasterBarang.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N

        BarangDanHarga.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        BarangDanHarga.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/kecilmasterharga.png"))); // NOI18N
        BarangDanHarga.setText("Daftar Barang dan Harga");
        JMenuMasterBarang.add(BarangDanHarga);

        PengaturanConvertSatuan.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        PengaturanConvertSatuan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/kecilMasterSatuan.png"))); // NOI18N
        PengaturanConvertSatuan.setText("Pengaturan Convert Satuan");
        JMenuMasterBarang.add(PengaturanConvertSatuan);

        JMenuDaftarInventaris.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuDaftarInventaris.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/kecilMasterAlat.png"))); // NOI18N
        JMenuDaftarInventaris.setText("Daftar Inventaris Alat");
        JMenuMasterBarang.add(JMenuDaftarInventaris);

        jMenuBar1.add(JMenuMasterBarang);

        JMenuMasterBarang2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GambarMenu/KecilLaporan.png"))); // NOI18N
        JMenuMasterBarang2.setText("Laporan");
        JMenuMasterBarang2.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N

        jMenuItemLabaRugi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/KecilLabaRugi.png"))); // NOI18N
        jMenuItemLabaRugi.setText("Laba Rugi");
        JMenuMasterBarang2.add(jMenuItemLabaRugi);

        jMenuBar1.add(JMenuMasterBarang2);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Help2.png"))); // NOI18N
        jMenu1.setText("Bantuan");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Center, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(266, 266, 266)
                .addComponent(South, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(JPanelMenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(JPanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(224, 224, 224)
                .addComponent(Center, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(South, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-727)/2, (screenSize.height-533)/2, 727, 533);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        PanggilHelp ();
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             *
           
            //</editor-fold>

            /* Create and display the form */
            //TemaGelap();
            TemaWarnaPutih ();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Bsc_pro().setVisible(true);
                }
            });
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Bsc_pro.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Bsc_pro.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Bsc_pro.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem BarangDanHarga;
    private javax.swing.JMenuItem CaraMemasak;
    private javax.swing.JPanel Center;
    private javax.swing.JMenuItem JMenuAcounting;
    private javax.swing.JMenuItem JMenuAlokasiWaktu;
    private javax.swing.JMenuItem JMenuBahanPembelian;
    private javax.swing.JMenuItem JMenuDaftarInventaris;
    private javax.swing.JMenuItem JMenuItemPerancangan;
    private javax.swing.JMenu JMenuMasterBarang;
    private javax.swing.JMenu JMenuMasterBarang2;
    private javax.swing.JMenu JMenuPembelianBahan;
    private javax.swing.JMenuItem JMenuPesanInventaris;
    private javax.swing.JMenuItem JMenuPesanaMakanan;
    private javax.swing.JMenuItem JMenuResep;
    private javax.swing.JPanel JPanelMenu;
    private javax.swing.JMenuItem PengaturanConvertSatuan;
    private javax.swing.JPanel South;
    private javax.swing.JButton StartDatabase;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemLabaRugi;
    private javax.swing.JMenuItem jMenuPenjualan;
    // End of variables declaration//GEN-END:variables
}
