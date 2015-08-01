/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ValidasiInputResep;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author LANTAI3
 */
public class Bsc_pro extends javax.swing.JFrame {
    
    private JDesktopPane desktop = new JDesktopPane();
        
    /**
     * Creates new form Bsc_pro
     */
    public Bsc_pro() {
        initComponents();
        setContentPane( desktop );
        Aksi();
        //PembelianBahanPrint drawingPanel = new PembelianBahanPrint();
    //content.add(drawingPanel, BorderLayout.SOUTH);
   //content.setVisible(true);
    //pack();
    //drawingPanel.setVisible(true);
    //add(drawingPanel);
    }
    
    private void Aksi () {             
        JMenuResep.addActionListener(new ActionListener () {
           public void actionPerformed(ActionEvent e) {
               PanggiMenuResep();    
               
            }
        });
        JMenuBahanPembelian.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                PanggilMenuPembelianBahan();             
            }
        });
        
        JMenuSetupParameter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                PanggilMenuSetupProgram();             
            }
        });
        
        JMenuAlokasiWaktu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                PanggilMenuAlokasiWaktu();        
            }
        });
        
        JMenuFixedAsset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                 PanggilMenuFixedAsset();        
            }
        });
        
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
            
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            FA.setSelected(true);
            FA.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void PanggilMenuAlokasiWaktu(){
        AlokasiWaktu AW = new AlokasiWaktu();

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
            
            /*
             * biar form berada di depan 
             * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
             */
            PB.setSelected(true);
            PB.moveToFront();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void PanggiMenuResep(){
        
        JTextField NamaResep    = new JTextField();
        JTextField JumlahPorsi = new JTextField();
        
        /*
         * Anti Karakter
         */
        SistemPro.ComponentHanyaAngka AntiAngka = new SistemPro.ComponentHanyaAngka();
        AntiAngka.SetAntiAngka(JumlahPorsi);
      
        Object[] Object ={
          "Masukan Nama Resep   : ", NamaResep,
          "Masukan Jumlah Porsi : ", JumlahPorsi
        };
        
        int Pilih = JOptionPane.showConfirmDialog(null , Object , "Masukan Data ", JOptionPane.OK_CANCEL_OPTION);
        
        if (Pilih == JOptionPane.OK_OPTION){
            try {
                 //String NamaResep = JOptionPane.showInputDialog("Input Nama Resep :");
                String TitleResep = NamaResep.getText();
                String QtyPosrsi  = JumlahPorsi.getText();

                SistemPro.ValidasiInputResep ValidasiResep = new  ValidasiInputResep() ;
                ValidasiResep.SetValidasiInputResep2(TitleResep, QtyPosrsi);
                boolean BenarValidasiResep = ValidasiResep.GetValidasiInputResep2();
                //System.out.println(BenarValidasiResep);
                if (BenarValidasiResep == false){
                    Resep Resep = new Resep(TitleResep,QtyPosrsi );
                    setLayout(null);
                    Resep.setVisible(true);  
                    //MoveToFront(PO);
                    //moveToFront(PO);
                    try {                         
                        add(Resep);
                        /*
                         * biar form berada di depan 
                         * JInternalFrame.moveToFront + JInternalFrame.setSelected(true)
                         */
                        Resep.setSelected(true);
                        Resep.moveToFront();
                    } catch (PropertyVetoException ex) {
                        Logger.getLogger(Resep.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            catch (Exception X){
                JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1227 : "  +X, " Error delete", JOptionPane.ERROR_MESSAGE);
                X.printStackTrace();
            } 
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

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        JMenuPembelianBahan = new javax.swing.JMenu();
        JMenuResep = new javax.swing.JMenuItem();
        JMenuBahanPembelian = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        JMenuAlokasiWaktu = new javax.swing.JMenuItem();
        Accounting = new javax.swing.JMenu();
        JMenuFixedAsset = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        JMenuSetupParameter = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bcs_Pro");

        jMenu1.setText("File");
        jMenu1.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N
        jMenuBar1.add(jMenu1);

        JMenuPembelianBahan.setText("Transaksi & Resep");
        JMenuPembelianBahan.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N

        JMenuResep.setText("Buat Resep");
        JMenuPembelianBahan.add(JMenuResep);

        JMenuBahanPembelian.setText("Pembelian Bahan");
        JMenuPembelianBahan.add(JMenuBahanPembelian);

        jMenuItem3.setText("Print Label Produksi");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        JMenuPembelianBahan.add(jMenuItem3);

        JMenuAlokasiWaktu.setText("Alokasi Waktu");
        JMenuPembelianBahan.add(JMenuAlokasiWaktu);

        jMenuBar1.add(JMenuPembelianBahan);

        Accounting.setText("Accounting");
        Accounting.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N

        JMenuFixedAsset.setText("Input Fixed Asset");
        Accounting.add(JMenuFixedAsset);

        jMenuItem6.setText("Laporan Laba/Rugi");
        Accounting.add(jMenuItem6);

        jMenuBar1.add(Accounting);

        jMenu5.setText("Setup");
        jMenu5.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N

        jMenuItem7.setText("Setup Program");
        jMenu5.add(jMenuItem7);

        JMenuSetupParameter.setText("Setup Parameter");
        jMenu5.add(JMenuSetupParameter);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1199, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 591, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem3ActionPerformed

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
    private javax.swing.JMenu Accounting;
    private javax.swing.JMenuItem JMenuAlokasiWaktu;
    private javax.swing.JMenuItem JMenuBahanPembelian;
    private javax.swing.JMenuItem JMenuFixedAsset;
    private javax.swing.JMenu JMenuPembelianBahan;
    private javax.swing.JMenuItem JMenuResep;
    private javax.swing.JMenuItem JMenuSetupParameter;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    // End of variables declaration//GEN-END:variables
}
