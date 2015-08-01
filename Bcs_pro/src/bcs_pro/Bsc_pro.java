/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author LANTAI3
 */
public class Bsc_pro extends javax.swing.JFrame {
    
    private JDesktopPane desktop = new JDesktopPane();
    JButton ButtonResep, BDaftarHarga, BPesanan, BPemesanan, BRancanganHarga, BAlokasiWaktu, BBelanja;
        
    /**
     * Creates new form Bsc_pro
     */
    public Bsc_pro() {
        initComponents();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Bsc_pro.class.getName()).log(Level.SEVERE, null, ex);
        }
        setContentPane( desktop );
        Aksi();
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
       ButtonTransparant();
       
       setJStatusBar2(Center);      
       setJStatusBar(JPanelMenu); 
    }
    
    private void ButtonTransparant(){
        /*
        * Membuat Transparant
        */
       
       /*
        * Button Resep
        */
       
       ButtonResep = new SistemPro.ClButtonTransparant("");
       ButtonResep.setHorizontalTextPosition(SwingConstants.CENTER);
       ButtonResep.setVerticalTextPosition(SwingConstants.BOTTOM);
       ButtonResep.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       ButtonResep.setBounds(90, 275, 112, 110);
       ButtonResep.setVisible(true);
       add(ButtonResep);
       
       /*
        * Button Daftar Harga
        */
       BDaftarHarga = new SistemPro.ClButtonTransparant("");
       BDaftarHarga.setHorizontalTextPosition(SwingConstants.CENTER);
       BDaftarHarga.setVerticalTextPosition(SwingConstants.BOTTOM);
       BDaftarHarga.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       BDaftarHarga.setBounds(90, 391, 112, 110);
       BDaftarHarga.setVisible(true);
       add(BDaftarHarga);
       
       /*
        * Button Daftar Pemesanan
        */
       BPesanan = new SistemPro.ClButtonTransparant("");
       BPesanan.setHorizontalTextPosition(SwingConstants.CENTER);
       BPesanan.setVerticalTextPosition(SwingConstants.BOTTOM);
       BPesanan.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       BPesanan.setBounds(90, 513, 112, 110);
       BPesanan.setVisible(true);
       add(BPesanan);
       
        /*
        * Button Daftar Pemesanan
        */
       BPemesanan = new SistemPro.ClButtonTransparant("");
       BPemesanan.setHorizontalTextPosition(SwingConstants.CENTER);
       BPemesanan.setVerticalTextPosition(SwingConstants.BOTTOM);
       BPemesanan.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       BPemesanan.setBounds(800, 212, 220, 50);
       BPemesanan.setVisible(true);
       add(BPemesanan);
       
        /*
        * Button Daftar Belanja
        */
       BBelanja = new SistemPro.ClButtonTransparant("");
       BBelanja.setHorizontalTextPosition(SwingConstants.CENTER);
       BBelanja.setVerticalTextPosition(SwingConstants.BOTTOM);
       BBelanja.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       BBelanja.setBounds(800, 268, 365, 50);
       BBelanja.setVisible(true);
       add(BBelanja);
       
       /*
        * Button Rancangan Harga
        */
       BRancanganHarga = new SistemPro.ClButtonTransparant("");
       BRancanganHarga.setHorizontalTextPosition(SwingConstants.CENTER);
       BRancanganHarga.setVerticalTextPosition(SwingConstants.BOTTOM);
       BRancanganHarga.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       BRancanganHarga.setBounds(800, 325, 320, 50);
       BRancanganHarga.setVisible(true);
       add(BRancanganHarga);
       
       /*
        * Button Rancangan Harga
        */
       BAlokasiWaktu = new SistemPro.ClButtonTransparant("");
       BAlokasiWaktu.setHorizontalTextPosition(SwingConstants.CENTER);
       BAlokasiWaktu.setVerticalTextPosition(SwingConstants.BOTTOM);
       BAlokasiWaktu.setVerticalAlignment(SwingConstants.BOTTOM);
       //x,y , px, ly
       BAlokasiWaktu.setBounds(800, 380, 280, 50);
       BAlokasiWaktu.setVisible(true);
       add(BAlokasiWaktu);
       
       Center = new SistemPro.ClPanelTransparant();
             
        ButtonResep.addActionListener(new ActionListener () {
            @Override
           public void actionPerformed(ActionEvent e) {
               PanggiMenuResep();                  
            }
        });
        
        BBelanja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                  PanggilMenuPembelianBahan();        
            }
        });
                
        BDaftarHarga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                 MasterBarang();        
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
        
        BPesanan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                PanggilMenuPesananMakanStatus();        
            }
        });
    }
    private void StartDatabase(){
        try {
            Runtime.getRuntime().exec("cmd /K start C:\\xampp\\sql.bat");
        } catch (IOException ex) {
            Logger.getLogger(Bsc_pro.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,"1927182 "+ ex);
        }
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
    
    private void Aksi () {
        
        JMenuDaftarInventaris.addActionListener(new ActionListener () {
            @Override
           public void actionPerformed(ActionEvent e) {
               PanggilDaftarInventaris();                  
            }
        });

        StartDatabase.addActionListener(new ActionListener () {
            @Override
           public void actionPerformed(ActionEvent e) {
               StartDatabase();                  
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
        Beban B = new Beban();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JPanelMenu = new javax.swing.JPanel();
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
        JMenuMasterBarang = new javax.swing.JMenu();
        BarangDanHarga = new javax.swing.JMenuItem();
        PengaturanConvertSatuan = new javax.swing.JMenuItem();
        JMenuDaftarInventaris = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bcs_Pro");
        setResizable(false);

        StartDatabase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/networkserverdatabasekecill.png"))); // NOI18N
        StartDatabase.setText("Start Database");

        javax.swing.GroupLayout JPanelMenuLayout = new javax.swing.GroupLayout(JPanelMenu);
        JPanelMenu.setLayout(JPanelMenuLayout);
        JPanelMenuLayout.setHorizontalGroup(
            JPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelMenuLayout.createSequentialGroup()
                .addContainerGap(1065, Short.MAX_VALUE)
                .addComponent(StartDatabase)
                .addGap(25, 25, 25))
        );
        JPanelMenuLayout.setVerticalGroup(
            JPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StartDatabase)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addGap(0, 238, Short.MAX_VALUE)
        );

        JMenuPembelianBahan.setText("Transaksi & Resep");
        JMenuPembelianBahan.setToolTipText("");
        JMenuPembelianBahan.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N

        JMenuResep.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuResep.setText("Buat Resep");
        JMenuPembelianBahan.add(JMenuResep);

        CaraMemasak.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        CaraMemasak.setText("Cara Memasak");
        JMenuPembelianBahan.add(CaraMemasak);

        JMenuBahanPembelian.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuBahanPembelian.setText("Pembelian Bahan");
        JMenuPembelianBahan.add(JMenuBahanPembelian);

        JMenuPesanaMakanan.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuPesanaMakanan.setText("Pesanan Makanan");
        JMenuPembelianBahan.add(JMenuPesanaMakanan);

        JMenuAlokasiWaktu.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuAlokasiWaktu.setText("Alokasi Waktu");
        JMenuPembelianBahan.add(JMenuAlokasiWaktu);

        JMenuItemPerancangan.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuItemPerancangan.setText("Rancangan Harga");
        JMenuPembelianBahan.add(JMenuItemPerancangan);

        JMenuPesanInventaris.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuPesanInventaris.setText("Rencana Inventaris Alat");
        JMenuPembelianBahan.add(JMenuPesanInventaris);

        jMenuBar1.add(JMenuPembelianBahan);

        JMenuMasterBarang.setText("Master Barang");
        JMenuMasterBarang.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N

        BarangDanHarga.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        BarangDanHarga.setText("Daftar Barang dan Harga");
        JMenuMasterBarang.add(BarangDanHarga);

        PengaturanConvertSatuan.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        PengaturanConvertSatuan.setText("Pengaturan Convert Satuan");
        JMenuMasterBarang.add(PengaturanConvertSatuan);

        JMenuDaftarInventaris.setFont(new java.awt.Font("Garamond", 0, 18)); // NOI18N
        JMenuDaftarInventaris.setText("Daftar Inventaris Alat");
        JMenuMasterBarang.add(JMenuDaftarInventaris);

        jMenuBar1.add(JMenuMasterBarang);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JPanelMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(266, 266, 266)
                .addComponent(South, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 855, Short.MAX_VALUE))
            .addComponent(Center, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(JPanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(202, 202, 202)
                .addComponent(Center, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(South, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-1237)/2, (screenSize.height-670)/2, 1237, 670);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Bsc_pro().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem BarangDanHarga;
    private javax.swing.JMenuItem CaraMemasak;
    private javax.swing.JPanel Center;
    private javax.swing.JMenuItem JMenuAlokasiWaktu;
    private javax.swing.JMenuItem JMenuBahanPembelian;
    private javax.swing.JMenuItem JMenuDaftarInventaris;
    private javax.swing.JMenuItem JMenuItemPerancangan;
    private javax.swing.JMenu JMenuMasterBarang;
    private javax.swing.JMenu JMenuPembelianBahan;
    private javax.swing.JMenuItem JMenuPesanInventaris;
    private javax.swing.JMenuItem JMenuPesanaMakanan;
    private javax.swing.JMenuItem JMenuResep;
    private javax.swing.JPanel JPanelMenu;
    private javax.swing.JMenuItem PengaturanConvertSatuan;
    private javax.swing.JPanel South;
    private javax.swing.JButton StartDatabase;
    private javax.swing.JMenuBar jMenuBar1;
    // End of variables declaration//GEN-END:variables
}
