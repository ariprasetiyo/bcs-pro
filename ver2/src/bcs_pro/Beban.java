/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ComponentHanyaAngka;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.ValidasiInputResep;
import SistemPro.app_search1;
import SistemPro.app_search_data_beban;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author LANTAI3
 */
public class Beban extends javax.swing.JInternalFrame {
    
    private ButtonGroup GroupsStatusAsset = new ButtonGroup();
    private SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    private Connection K = KD.createConnection();
    LaporanPerhitunganBiayaPenjualan BiayaPenjualan;
    DefaultTableModel   Modeltabel  = new DefaultTableModel();
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();

    /**
     * Creates new form Beban
     */
    public Beban() {
        initComponents();
        Tampilan();
        AksiAksi();
        JTabelBiaya.setVisible(false);

      //SistemPro.app_search_data_beban Combo = new SistemPro.app_search_data_beban();
       //JComboBoxFoodCost.setModel(new javax.swing.DefaultComboBoxModel( app_search_data_beban.getData().toArray()));
    }
    int NoJTabbed = 1;
    private void AksiAksi(){
        JComboBoxFoodCost.setSelectedIndex(-1);
        final JTextField JTextFieldItem = (JTextField)JComboBoxFoodCost.getEditor().getEditorComponent();
        JTextFieldItem.setText("");
        JTextFieldItem.addKeyListener(new app_search1(JComboBoxFoodCost));
        
        JButtonProses1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent E){
                JTabelBiaya.setVisible(true);
                
                
                JTextField[]   TextCek  = {JTextFieldBiayaKemasan, JTextFieldBiayaLainnya, JTextFieldPersenKeuntungan,
                JTextFieldPersenTenagaKerja, JTextFieldPersenBebanLain, JTextFieldPersenPenyusutan};
                String[] DataPesanErro  = {"Biaya Kemasan", "Biaya Lainnya", "Kenaikan Yang DiHarapkan",
                "Beban Tenaga Kerja", "Beban Lain-lain", "Beban Penyusutan"};
                
                 if (ValidasiData(TextCek,DataPesanErro )){
                     
                     Tabel(JLabelBebanPorsi.getText());
                     /*
                      * Ambil data transaksi saja
                      */
                      String DataNoTransaksi      =    (String) JComboBoxFoodCost.getSelectedItem();
                      DataNoTransaksi             = FilterNamaTransaksiSaja(DataNoTransaksi);
                      AmbilDataBahanResep( DataNoTransaksi);
                      PerhitunganAll ();
                      PrintView.setVisible(true);
                      
                 }
                
            }
        });
        
        JButtonProses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent E){
                
                JTextField[]   TextCek  = {JTextFieldBiayaKemasan, JTextFieldBiayaLainnya, JTextFieldPersenKeuntungan,
                JTextFieldPersenTenagaKerja, JTextFieldPersenBebanLain, JTextFieldPersenPenyusutan};
                String[] DataPesanErro  = {"Biaya Kemasan", "Biaya Lainnya", "Kenaikan Yang DiHarapkan",
                "Beban Tenaga Kerja", "Beban Lain-lain", "Beban Penyusutan"};
                
                if (ValidasiData(TextCek,DataPesanErro )){
                    try {
                        BiayaPenjualan = new LaporanPerhitunganBiayaPenjualan();
                
                        String DataNoTransaksi      =    (String) JComboBoxFoodCost.getSelectedItem();
                        BiayaPenjualan.JudulResep   = FilterNamaResepSaja(DataNoTransaksi);

                        DataNoTransaksi             = FilterNamaTransaksiSaja(DataNoTransaksi);

                        BiayaPenjualan.JumlahPorsi  = JLabelBebanPorsi.getText();
                        BiayaPenjualan.NoTransaksi  = DataNoTransaksi;
                        BiayaPenjualan.VorH         = VerticalOrHorizontal();
                        BiayaPenjualan.JumlahPorsi  = JLabelBebanPorsi.getText();   
                        BiayaPenjualan.BiayaKemasan = JTextFieldBiayaKemasan.getText();
                        BiayaPenjualan.BiayaLain    = JTextFieldBiayaLainnya.getText();
                        BiayaPenjualan.BebanLainnya = JTextFieldPersenBebanLain.getText();
                        BiayaPenjualan.BebanPenyusutan          = JTextFieldPersenPenyusutan.getText();
                        BiayaPenjualan.BebanTenagaKerja         = JTextFieldPersenTenagaKerja.getText();
                        BiayaPenjualan.KenaikanYangDiHarapkan   = JTextFieldPersenKeuntungan.getText();

                        SistemPro.TambahTab TabNewViewData = new SistemPro.TambahTab();
                        TabNewViewData.createTabButtonActionPerformed(E, jTabbedPaneLihatProses,
                                "View Report " + NoJTabbed, BiayaPenjualan,
                                // ( Panjang, Tinggi )
                                new Dimension(10, 20), new Dimension(1400, 1000));
                        NoJTabbed++;    
                        //jTabbedPaneLihatProses.add(BiayaPenjualan);
                    }
                    catch (Exception x){
                        JOptionPane.showMessageDialog(null, x + "x x");
                    }                   
                    }
                }
                
        });
        
        ComponentHanyaAngka AntiHuruf   = new ComponentHanyaAngka();
        AntiHuruf.SetAntiAngka(JTextFieldBiayaKemasan);
        AntiHuruf.SetAntiAngka(JTextFieldBiayaLainnya);
        AntiHuruf.SetAntiAngkaLimit(JTextFieldPersenKeuntungan, 3);
        AntiHuruf.SetAntiAngkaLimit(JTextFieldPersenTenagaKerja, 3);
        AntiHuruf.SetAntiAngkaLimit(JTextFieldPersenBebanLain, 3);
        AntiHuruf.SetAntiAngkaLimit(JTextFieldPersenPenyusutan,3);
        

        JComboBoxFoodCost.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                AmbilDataUntukPorsi();   
                new javax.swing.DefaultComboBoxModel( app_search_data_beban.getData().toArray());
                
                if (JComboBoxFoodCost.isPopupVisible()){
                    JComboBoxFoodCost.setEditable(false);
                }
                //JComboBoxFoodCost.
                /*
                if (JComboBoxFoodCost.isShowing()){
                    JComboBoxFoodCost.setEditable(true);
                } */
                //JComboBoxFoodCost.setEnabled(false);
            }
        });
        
        JComboBoxFoodCost.addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
                JComboBoxFoodCost.setEditable(true);
                //System.out.println("cccc");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                //JComboBoxFoodCost.setEditable(true);
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        jTabbedPaneLihatProses.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent ARI) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) ARI.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                String coba = sourceTabbedPane.getTitleAt(index);
                
                if (sourceTabbedPane.getTitleAt(index).equals("View Tabel") ){
                        PrintView.setVisible(true);                     
                        setSize(1000, 660);
                }
                else {
                    PrintView.setVisible(false);      
                }                    
              }
        });
        
        PrintView.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                    PrintJasper();
            }
        });
    }
    
    private void PrintJasper(){
        
        String DataNoTransaksi      =    (String) JComboBoxFoodCost.getSelectedItem();
        DefaultTableModel de = (DefaultTableModel)JTabelBiaya.getModel();

        JRTableModelDataSource dataSource = new JRTableModelDataSource(de);

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();
        
        hashMap.put("JudulPorsi", FilterNamaResepSaja(DataNoTransaksi));
        hashMap.put("KenaikanYangDiHarapkan1", jLabel16.getText());
        hashMap.put("KenaikanYangDiHarapkan2", jLabel17.getText());
        hashMap.put("HargaJualPerporsi1", jLabel19.getText());
        hashMap.put("HargaJualPerporsi2", jLabel18.getText());
        hashMap.put("LabaKotor1", jLabel26.getText());
        hashMap.put("LabaKotor2", jLabel27.getText());
        hashMap.put("BebanTenagaKerja1", jLabel29.getText());
        hashMap.put("BebanTenagaKerja2", jLabel28.getText());
        hashMap.put("BebanPenyusutanAlat1", jLabel34.getText());
        hashMap.put("BebanPenyusutanAlat2", jLabel33.getText());
        hashMap.put("BebanLainya1", jLabel41.getText());
        hashMap.put("BebanLainya2", jLabel42.getText());
        hashMap.put("LabaBersih1", jLabel44.getText());
        hashMap.put("LabaBersih2", jLabel43.getText());
        hashMap.put("TotalFoodCost", TotalProduksi);
        hashMap.put("BiayaKemasan", JTextFieldBiayaKemasan.getText());
        hashMap.put("LainLainnya", JTextFieldBiayaLainnya.getText());
        hashMap.put("TotalPro", (int) TotalAkhirProduksi() + "");
        
        try {

            String x    = System.getProperty("user.dir")+"\\ReportJasper\\ReportLaporanPerhitunganBiaya.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }  
    }
    
    private void Tampilan(){
        
        JRadioButtonVertical.setSelected(true);
        GroupsStatusAsset.add(JRadioButtonVertical);
        GroupsStatusAsset.add(JRadioButtonHorizontal);     
        PrintView.setVisible(false);
    }
    
    private boolean ValidasiData(JTextField[] Data, String DataPesanError[]){
        if (Data.length == DataPesanError.length){
            for (int a = 1; a < Data.length; a++){
                if (Data[a].getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Data tidak boleh kosong : " + DataPesanError[a]);
                    return false;
                }
            }           
        }
        else {
            System.out.println("tidak sama data length");
            System.exit(1);
        }
        return true;
    }
    
    private boolean VerticalOrHorizontal(){
        if (JRadioButtonVertical.isSelected()){
            return true;
        }
        else {
            return false;
        }
    }
    
    private String FilterNamaTransaksiSaja(String Data){
        try {
            String PartRegex = "(.*)?(\\s)*(--)+(\\s)*";
            Data   = Data.replaceAll( PartRegex, "");
            JLabelBebanKet.setText("");
            return Data;
        }
        catch (Exception  x){
            JLabelBebanPorsi.setText("");
        }
        return "xxx";   
    }
    
    
    private String FilterNamaResepSaja(String Data){
        try {
            String PartRegex = "(\\s)*(--)+(\\s)*(.*)?";
            Data   = Data.replaceAll( PartRegex, "");
            JLabelBebanKet.setText("");
            return Data;
        }
        catch (Exception  x){
            JLabelBebanPorsi.setText("");
        }
        return "xxx";   
    }
    
    private void AmbilDataUntukPorsi(){
        String DataResep    =   (String) JComboBoxFoodCost.getSelectedItem();       
        int baris;
        String JumlahPorsiText;
        
        DataResep           = FilterNamaResepSaja(DataResep);
        
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT  porsi_resep, nama_resep from header_resep where nama_resep ='"+ DataResep + "'");                     
               //System.out.println("SELECT  trans_no ,qty,nama_bahan, satuan, harga_satuan, harga_tot from detail_trans_penerimaan where trans_no = '"+ TransNo + "' order by no asc  ");
               baris = HQ.getRow();
               
               int JumlahHarga = 0;
               while(HQ.next()  ){

                    //String No             = String.valueOf(HQ.getInt("no")).toString();
                    JumlahPorsiText         = HQ.getString("porsi_resep");                   
                    JLabelBebanPorsi.setText(HQ.getString("porsi_resep"));  
                    JLabelBebanKet.setText("Data Dari Resep");
               }
               
               if (DataResep.equals("null") ){
                   InputJikaTotalFoodCostNull();
               }             
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }    
    }
    
    private void InputJikaTotalFoodCostNull(){
        JTextField NamaResep    = new JTextField();
        JTextField JumlahPorsi = new JTextField();
        
        Object[] Object ={
        "Masukan Nama Resep Baru   : ", NamaResep,
        "Masukan Jumlah Porsi      : ", JumlahPorsi
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

                if (BenarValidasiResep == false){
                    String a = "[/*\\-\\(\\)<>_\\=+\\.,:\";\'\\\\#\\$&\\^\\}\\{%~`\\|\\[\\]\\!\\?\\@a-zA-Z]";
                    QtyPosrsi = QtyPosrsi.replaceAll(a, "");
                    JLabelBebanPorsi.setText(QtyPosrsi);
                    JLabelBebanKet.setText("Data Manual");
                }
          }
          catch (Exception X){
              JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1227 : "  +X, " Error delete", JOptionPane.ERROR_MESSAGE);
              X.printStackTrace();
          } 
        }      
    }
    
    /*
     * Bagian Tabel
     */
     private void Tabel(String Porsi){
        
        String header[] = {"No", "Nama Bahan", "1 Porsi",Porsi +" Porsi", "Harga Satuan"};
        Modeltabel = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        //if(colIndex == 9) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTabelBiaya.setModel(Modeltabel);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel);
        JTabelBiaya.setRowSorter(sorter);
        
        /*
         * Rata tengah atau kanan table
         */
        JTabelBiaya.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelBiaya.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        JTabelBiaya.getColumnModel().getColumn(1).setCellRenderer( tengah );
        JTabelBiaya.getColumnModel().getColumn(3).setCellRenderer( tengah );
        JTabelBiaya.getColumnModel().getColumn(4).setCellRenderer( kanan ); 
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,400,150,150, 200};
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTabelBiaya, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         
        JTabelBiaya.getColumnModel().getColumn(9).setCellRenderer(  new ButtonJTable("Delete"));
        JTabelBiaya.getColumnModel().getColumn(9).setCellEditor( new  menu_cuti.ButtonJTableKeDuaCuti(new JCheckBox(),Modeltabel, JTabelBiaya));
       
        /*
         * Disable drag colum tabel
         */       
        JTabelBiaya.getTableHeader().setReorderingAllowed(false);       
    }
     
     private String FilterKeterangnSatuan(String Data){
        Data  = Data.replaceAll("\\(.*\\)", "");
        return Data;
    }
     String TotalBiaya, TotalProduksi      = "0";
     
     private void AmbilDataBahanResep(String NoTransaksi ){
          
      /*
       * Query 3 join untuk ambil data qty_1_porsi, satuan_1_porsi, nama bahan, dan trans_no
         SELECT detail_resep.qty_1_porsi, detail_resep.satuan_1_porsi, 
                header_penerimaan.trans_no, header_penerimaan.judul_resep, 
                detail_trans_penerimaan.trans_no, detail_trans_penerimaan.nama_bahan, detail_trans_penerimaan.qty, detail_trans_penerimaan.satuan
         FROM detail_trans_penerimaan RIGHT JOIN header_penerimaan 
                ON detail_trans_penerimaan.trans_no = header_penerimaan.trans_no
            LEFT JOIN detail_resep 
                ON header_penerimaan.judul_resep = detail_resep.namresep
         WHERE detail_trans_penerimaan.trans_no = 'WR2014011'
         AND detail_trans_penerimaan.nama_bahan = detail_resep.bahan
       */
        int baris;
        String JumlahPorsi2 ;
        String Qty1;
        String Qty;
        String Satuan1;
        String Satuan;
        String NamaBahan1;
        int TotalHrg1       = 0;
        int TotalHrg2       = 0;       
        System.out.println(NoTransaksi);
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery(
                    "SELECT detail_resep.qty_1_porsi, detail_resep.satuan_1_porsi, " +
                    "header_penerimaan.trans_no, "
                  + "header_penerimaan.judul_resep, "
                  + "header_penerimaan.total_biaya, " +
                    "detail_trans_penerimaan.trans_no, detail_trans_penerimaan.nama_bahan, "+
                    "detail_trans_penerimaan.qty, detail_trans_penerimaan.satuan, " +
                    "detail_trans_penerimaan.harga_tot "+
                    "FROM detail_trans_penerimaan RIGHT JOIN header_penerimaan " +
                    "ON detail_trans_penerimaan.trans_no = header_penerimaan.trans_no " +
                    "LEFT JOIN detail_resep " +
                    "ON header_penerimaan.judul_resep = detail_resep.namresep " +
                    "WHERE detail_trans_penerimaan.trans_no = '"+ NoTransaksi+ "'" +
                    "AND detail_trans_penerimaan.nama_bahan = detail_resep.bahan ");                   
               
               baris = HQ.getRow();                                          
               while(HQ.next()  ){
                   
                    Qty             = HQ.getString("qty");
                    Qty1            = HQ.getString("qty_1_porsi");
                    Satuan1         = HQ.getString("satuan_1_porsi");
                    Satuan          = HQ.getString("satuan");
                    TotalHrg1       = HQ.getInt("harga_tot");
                    NamaBahan1      = HQ.getString("nama_bahan");               
                    Satuan          = FilterKeterangnSatuan(Satuan);
                    Satuan1         = FilterKeterangnSatuan(Satuan1);
                    TotalBiaya      = HQ.getString("total_biaya");
                    /*
                     * Hitung Total Produksi
                     * "No", "Nama Bahan", "Jumlah","Porsi", "Harga Satuan"
                     */
                    TotalHrg2   = TotalHrg2 + TotalHrg1 ;
                    
                    String[] add         = {String.valueOf(HQ.getRow()).toString(), NamaBahan1, Qty1 + " " + Satuan1, Qty + " " + Satuan, TotalHrg1 + "" };
                   Modeltabel.addRow(add);
               }
               TotalProduksi    =   TotalHrg2+"";

           }
           catch (Exception ex){
                System.out.println("Error (4) "+ ex);
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
           }    
           JTabelBiaya.setModel(Modeltabel);
  }
     
     /*
      * Perhitungan Rumus
      */
     
     private void PerhitunganAll (){
         /*
          * Perhitungan Kenaikan yang diharapkan
          */
         String Porsi           = JLabelBebanPorsi.getText();
         int HargaPerporsi      =  (int) HargaJualPerPorsi(TotalAkhirProduksi(), JTextFieldPersenKeuntungan.getText(),Porsi);     
         String BTenagaKerja    = JTextFieldPersenTenagaKerja.getText();
         String BPenyusutan     = JTextFieldPersenPenyusutan.getText();
         String BLainnya        = JTextFieldPersenBebanLain.getText();
         jLabel16.setText("= "+ (int) TotalAkhirProduksi() + " x ( "+ JTextFieldPersenKeuntungan.getText() + "% x " +TotalAkhirProduksi() + " )");
         jLabel17.setText("= "+KenaikanYangDiHarapkanInt);
         
         /*
          * Harga Jual Perporsi
          */
         jLabel19.setText("= "+ (int) KenaikanYangDiHarapkanInt + " : " +  Porsi);
         jLabel18.setText("= "+ HargaPerporsi );
         
         /*
          * Laba kotor
          */
         int LabaKotor      =(int) LabaKotorDanLabaBersih (KenaikanYangDiHarapkanInt, (int) TotalAkhirProduksi());
         jLabel26.setText("= " + KenaikanYangDiHarapkanInt + " - " + (int) TotalAkhirProduksi());
         jLabel27.setText("= " + LabaKotor);
         
         
         /*
          * Beban-beban
          * BebanAll();
          */
         int BTenagaKerjaHtng   = (int ) BebanAll(BTenagaKerja ,(double) LabaKotor);
         int BPenyusutanHtng    = (int ) BebanAll(BPenyusutan ,(double) LabaKotor);
         int BLainnyaHtng       = (int ) BebanAll(BLainnya ,(double) LabaKotor);
         
         
         jLabel29.setText("= " +BTenagaKerja + "% x " + LabaKotor);
         jLabel28.setText("= " + BTenagaKerjaHtng );
         
         jLabel34.setText("= " +BPenyusutan + "% x " + LabaKotor);
         jLabel33.setText("= " +BPenyusutanHtng);
         
         jLabel41.setText("= " +BLainnya + "% x " + LabaKotor);
         jLabel42.setText("= " + BLainnyaHtng);
         
         /*
          * Laba Bersih
          * LabaKotorDanLabaBersih
          */
         double[] data = {(double)BTenagaKerjaHtng,(double)BPenyusutanHtng, (double)BLainnyaHtng };
         int TotalBeban     = (int) TotalBeban( data);
         jLabel44.setText("= " + LabaKotor + " - " + TotalBeban);
         jLabel43.setText("= " + LabaKotorDanLabaBersih( (double)LabaKotor ,(double) TotalBeban));
     }
     
     private int  TotalAkhirProduksi(){
         int TotalAkhirProduksi   =   HitunganTotalProduksi(TotalBiaya, JTextFieldBiayaLainnya.getText(), JTextFieldBiayaKemasan.getText());
         return TotalAkhirProduksi;
     }
     
      private int HitunganTotalProduksi(String TotalProduksi, String BiayaLainnya, String BiayaKemasan){
        if (BiayaLainnya.equals("") ){
            BiayaLainnya = "0";
        }
        if (BiayaKemasan.equals("")){
            BiayaKemasan = "0";
        } 
        if (TotalProduksi.equals("")){
            TotalProduksi = "0";
        }  
        return  Integer.valueOf(TotalProduksi).intValue() - Integer.valueOf(BiayaLainnya).intValue() -Integer.valueOf(BiayaKemasan).intValue();
    }
    
    double KenaikanYangDiHarapkanInt;
    double HasilSementaraHrgJual;
    
    private double HargaJualPerPorsi(int TotalAkhirProduksi, String KenaikanYangDiHarapakan1, String JumlahPorsi){

        if (KenaikanYangDiHarapakan1.equals("")){
            KenaikanYangDiHarapakan1 = "0";
        } 
        if (JumlahPorsi.equals("")){
            JumlahPorsi = "0";
        } 
        
        double KenaikanYangDiHarapakan  = Integer.valueOf(KenaikanYangDiHarapakan1).intValue() ;
        double IntPorsi                 = Integer.valueOf(JumlahPorsi).intValue() ;
              
        /*
        System.out.println(TotalAkhirProduksi + " + "+ KenaikanYangDiHarapakan/100 + " * "+  TotalAkhirProduksi + " = "+
                TotalAkhirProduksi + ( (KenaikanYangDiHarapakan/100 ) * TotalAkhirProduksi));
         */
        this.HasilSementaraHrgJual          =   (KenaikanYangDiHarapakan/100) * TotalAkhirProduksi;
        this.KenaikanYangDiHarapkanInt      =  HasilSementaraHrgJual + TotalAkhirProduksi;
        return   (KenaikanYangDiHarapkanInt  / IntPorsi);
    }
    
    private double LabaKotorDanLabaBersih(double TotalPendapatan , double TotalAkhirProduksi){
        return  (TotalPendapatan - TotalAkhirProduksi );
    }
    
    /*
     * Beban Beban all
     */
    private double BebanAll (String BebanPesen, double LabaKotor){
        return  (Double.valueOf(BebanPesen).doubleValue()/100 )* LabaKotor;
    }
    
    private double TotalBeban (double BebanSemua[]){
        double TotalBeban = 0;
        for (int a = 0; a < BebanSemua.length; a++){
            TotalBeban += BebanSemua[a];
        }
        return TotalBeban;
    }
    
    private double FormatDesimal (double Data){
        DecimalFormat numberFormat = new DecimalFormat("##.00"); //#.##
        String Data2;
        Data2             = numberFormat.format(Data);
        if (Data2.contains(",")){
             return Double.valueOf(Data2.replaceAll(",", ".")).doubleValue();
        }       
       return Double.valueOf(Data2).doubleValue();
    } 
    
    private String FormatDesimalString(double Data){
        DecimalFormat numberFormat  = new DecimalFormat("#.00"); //#.##
        String Data2                = numberFormat.format(Data);
         //int decimalPlace = 2;
        //BigDecimal bd = new BigDecimal(Data);
        //bd = bd.setScale(decimalPlace,BigDecimal.ROUND_UP);
        if (Data2.contains(",")){
            
            Data2   = Data2.replaceAll(",", ".");
            return Data2;
        }       
       return Data2;    
    }
    
    /*
     *  /*
        * Hitung Harga Jual Porsi
        
       
       
       double HJualp    =   HargaJualPerPorsi(TotalAkhirProduksi, KenaikanYangDiHarapkan, JumlahPorsi);
       double LabaKotor1=  LabaKotorDanLabaBersih(KenaikanYangDiHarapkanInt, TotalAkhirProduksi);
       
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Harga Jual Perporsi      :", AwalRowPosisi , JarakDariAtas = JarakDrAtasMurni );
       g2d.drawString("Kenaikan Yang Diharapkan :", AwalRowPosisi, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Variabel cost + ( Kenaikan(%) x Variabel Cost )", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ TotalAkhirProduksi +" + ( "+ KenaikanYangDiHarapkan+" % ) x "+ TotalAkhirProduksi +" )", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ FormatDesimalString (KenaikanYangDiHarapkanInt), AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Harga Jual Perporsi ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= ( Variabel Cost + Mark Up ) \\ Jumlah Porsi ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= ( "+ FormatDesimalString (KenaikanYangDiHarapkanInt)+" ) \\ "+ JumlahPorsi, AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+FormatDesimalString (HJualp), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Laba Kotor ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Total Pendapatan - Variabel Cost ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+  FormatDesimalString (KenaikanYangDiHarapkanInt)+" - "+ FormatDesimalString (TotalAkhirProduksi ), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ FormatDesimalString (LabaKotor1), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       int GarisBwah   = JarakDariAtas += JarakAntarRow;

       AwalRowPosisi  = 350 +AwalRowPosisi; // Akhir Posisis Horizontal
       JarakDariAtas  = JarakDrAtasMurni;

       /*
        * Pindah Samping
        
       double BebanTenaga     =   BebanAll(BebanTenagaKerja,LabaKotor1 );
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Beban - Beban ", AwalRowPosisi, JarakDariAtas);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("Upah Tenaga Kerja ", AwalRowPosisi, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= Beban Tenaga Kerja( % ) x Nominal", AwalRowPosisi +20 , JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+ BebanTenagaKerja + " % x " +FormatDesimalString (LabaKotor1),  AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= " + FormatDesimalString (BebanTenaga), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
     
       double B_Penyusutan    =   BebanAll(BebanPenyusutan,LabaKotor1 );
       g2d.drawString("Biaya Penyusutan Alat ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.drawString("= Beban Penyusutan % x Laba Kotor ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+ BebanPenyusutan+" % x " + FormatDesimalString (LabaKotor1), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ FormatDesimalString (B_Penyusutan), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       
       double BebanLain       =   BebanAll(BebanLainnya,LabaKotor1 );
       g2d.drawString("Biaya Lainnya ( (listrik, air, pajak, dsb)  ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf );
       g2d.drawString("= % x Laba Kotor ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+ BebanLainnya+ " % x "+FormatDesimalString (LabaKotor1), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ FormatDesimalString (BebanLain), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       AwalRowPosisi  = 250 +AwalRowPosisi; //600+ AkhirRowPosisi Horizontal
       JarakDariAtas  = JarakDrAtasMurni;
       
       double[] TotBeban    = {BebanTenaga,B_Penyusutan, BebanLain };
       double TotBeban2     = TotalBeban(TotBeban);
       double LabaBersih      =   LabaKotorDanLabaBersih(LabaKotor1, TotBeban2 );
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Laba Bersih",AwalRowPosisi, JarakDariAtas );
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Laba Kotor - Total Beban ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+ FormatDesimalString (LabaKotor1)+" - " + FormatDesimalString (TotBeban2), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= " + FormatDesimalString (LabaBersih), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       g2d.drawLine(AwalPosisiGaris  , JarakDrAtasMurni2 - 10, AwalRowPosisi +20 + 150 ,JarakDrAtasMurni2 -10);
       g2d.drawLine(AwalPosisiGaris  , GarisBwah, AwalRowPosisi +20 + 150 ,GarisBwah );

     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        JTextFieldPersenKeuntungan = new javax.swing.JTextField();
        JComboBoxFoodCost = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        JLabelBebanPorsi = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        JTextFieldPersenTenagaKerja = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        JTextFieldPersenBebanLain = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        JTextFieldPersenPenyusutan = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        JTextFieldBiayaKemasan = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        JTextFieldBiayaLainnya = new javax.swing.JTextField();
        JLabelBebanKet = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        JRadioButtonVertical = new javax.swing.JRadioButton();
        JRadioButtonHorizontal = new javax.swing.JRadioButton();
        JButtonProses = new javax.swing.JButton();
        JButtonProses1 = new javax.swing.JButton();
        PrintView = new javax.swing.JButton();
        jTabbedPaneLihatProses = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelBiaya = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Rencana Kerja");

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Total Food Cost");

        jLabel2.setText("Kenaikan Yang Di Harapkan");

        JTextFieldPersenKeuntungan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        JComboBoxFoodCost.setEditable(true);
        JComboBoxFoodCost.setModel(new javax.swing.DefaultComboBoxModel( app_search_data_beban.getData().toArray()));

        jLabel4.setText("%");

        jLabel3.setText("Porsi");

        JLabelBebanPorsi.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        JLabelBebanPorsi.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel5.setText("Upah Tenaga Kerja");

        jLabel6.setText("Beban Lain");

        JTextFieldPersenTenagaKerja.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel7.setText("%");

        JTextFieldPersenBebanLain.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel8.setText("%");

        JTextFieldPersenPenyusutan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel9.setText("%");

        jLabel10.setText("Beban Penyusutan");

        jLabel11.setText("Biaya Kemasan");

        JTextFieldBiayaKemasan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel12.setText("Biaya Lainnya");

        JTextFieldBiayaLainnya.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        JLabelBebanKet.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        JLabelBebanKet.setForeground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(JTextFieldPersenKeuntungan, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPersenTenagaKerja, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7))
                    .addComponent(JComboBoxFoodCost, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPersenBebanLain, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLabelBebanPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(22, 22, 22)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(JTextFieldBiayaKemasan, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldBiayaLainnya, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(JTextFieldPersenPenyusutan, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLabelBebanKet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(JTextFieldBiayaKemasan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12)
                        .addComponent(JTextFieldBiayaLainnya, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JComboBoxFoodCost)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JLabelBebanPorsi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(JTextFieldPersenKeuntungan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel10)
                            .addComponent(JTextFieldPersenPenyusutan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel5)
                            .addComponent(JLabelBebanKet, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JTextFieldPersenTenagaKerja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(JTextFieldPersenBebanLain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)))))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        JRadioButtonVertical.setText("View Report Vertical");

        JRadioButtonHorizontal.setText("View Report Horizontal");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(JRadioButtonVertical)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JRadioButtonHorizontal))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(JRadioButtonVertical)
                .addComponent(JRadioButtonHorizontal))
        );

        JButtonProses.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Load.png"))); // NOI18N
        JButtonProses.setText("Proses View Text");

        JButtonProses1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Load.png"))); // NOI18N
        JButtonProses1.setText("Proses View Tabel");

        PrintView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Print.png"))); // NOI18N
        PrintView.setText("Print View");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonProses, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JButtonProses1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PrintView)
                        .addGap(152, 152, 152))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PrintView, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JButtonProses, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JButtonProses1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        JTabelBiaya.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTabelBiaya.setToolTipText("");
        jScrollPane1.setViewportView(JTabelBiaya);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel17.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel15.setText("= Variabel cost x ( Kenaikan (%) x Variabel Cost )");
        jLabel15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel14.setText("Kenaikan Yang Diharapkan");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel13.setText("Harga Jual Perorsi");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel18.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel19.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel20.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel20.setText("= ( Variabel cost + Mark Up ) : Jumlah Porsi");
        jLabel20.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel22.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel22.setText("Harga Jual Perorsi");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel23.setText("Laba Kotor");

        jLabel25.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel25.setText("= Total Pendapatan - Variabel Cost");
        jLabel25.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel26.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel26.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel27.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel27.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel22)
                .addGap(6, 6, 6)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel23)
                .addGap(6, 6, 6)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel28.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel28.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel29.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel29.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel30.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel30.setText("= Beban Tenaga Kerja ( % ) x Laba Kotor");
        jLabel30.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel31.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel31.setText("Upah Tenaga Kerja");

        jLabel32.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel32.setText("Beban - Beban");

        jLabel33.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel33.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel34.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel34.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel35.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel35.setText("= Beban Penyusutan Alat ( % ) x Kotor");
        jLabel35.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel36.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel36.setText("Biaya Penyusutan Alat");

        jLabel39.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel39.setText("Biaya Lainnya ( Listrik, Air dll )");

        jLabel40.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel40.setText("= Beban Lainnya ( % ) x Laba Kotor");
        jLabel40.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel41.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel41.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel42.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel42.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel43.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel43.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel44.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel44.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel45.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel45.setText("= Laba Kotor - Total Beban");
        jLabel45.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel46.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel46.setText("Laba Bersih");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel46)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPaneLihatProses.addTab("View Tabel", jPanel6);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneLihatProses)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jTabbedPaneLihatProses))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonProses;
    private javax.swing.JButton JButtonProses1;
    private javax.swing.JComboBox JComboBoxFoodCost;
    private javax.swing.JLabel JLabelBebanKet;
    private javax.swing.JLabel JLabelBebanPorsi;
    private javax.swing.JRadioButton JRadioButtonHorizontal;
    private javax.swing.JRadioButton JRadioButtonVertical;
    private javax.swing.JTable JTabelBiaya;
    private javax.swing.JTextField JTextFieldBiayaKemasan;
    private javax.swing.JTextField JTextFieldBiayaLainnya;
    private javax.swing.JTextField JTextFieldPersenBebanLain;
    private javax.swing.JTextField JTextFieldPersenKeuntungan;
    private javax.swing.JTextField JTextFieldPersenPenyusutan;
    private javax.swing.JTextField JTextFieldPersenTenagaKerja;
    private javax.swing.JButton PrintView;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPaneLihatProses;
    // End of variables declaration//GEN-END:variables
}
