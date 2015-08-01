/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * JTextPbBahan
 */
package bcs_pro;

import SistemPro.ComponentHanyaAngka;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.ValidasiInputResep;
import SistemPro.app_search1;
import SistemPro.app_search_data_beban;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author LANTAI3
 */
public class BebanRencanaMainTab extends javax.swing.JInternalFrame {
    
    private ButtonGroup GroupsStatusAsset = new ButtonGroup();
    private SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    private Connection K = KD.createConnection();
    LaporanPerhitunganBiayaPenjualan BiayaPenjualan;
    DefaultTableModel   Modeltabel  = new DefaultTableModel();
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();
    private SistemPro.KomaToString ConvertAngka = new SistemPro.KomaToString();
    ComponentHanyaAngka AntiHuruf   = new ComponentHanyaAngka();
    String AA;
    boolean AAA = false;
    
    BebanMetode2Panel PanelMetode2 = new BebanMetode2Panel();
    BebanMetode3Panel PanelMetode3 = new BebanMetode3Panel();


    /**
     * Creates new form Beban
     */
    public BebanRencanaMainTab() {
        initComponents();
        Tampilan();
        AksiAksi();
        
        JTabelBiaya.setVisible(false);
       // PanelMetode2.setVisible(true);

        JScrollPane scrollPane2 = new JScrollPane(PanelMetode2);
        //PanelMetode2.setPreferredSize(new Dimension(1400, 1000));
        jTabbedPane1.addTab("Metode Prima", PanelMetode2);
        
        JScrollPane scrollPane3 = new JScrollPane(PanelMetode3);
        //PanelMetode3.setPreferredSize(new Dimension(1400, 1000));
        jTabbedPane1.addTab("Metode Profesional", PanelMetode3);
        

      //SistemPro.app_search_data_beban Combo = new SistemPro.app_search_data_beban();
       //JComboBoxFoodCost.setModel(new javax.swing.DefaultComboBoxModel( app_search_data_beban.getData().toArray()));    
     
    }
    
     /*
     * Transparan
     */
    private AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
    @Override
    public void paint(Graphics g) {
        comp = comp.derive(0.4f);
       Graphics2D g2 = (Graphics2D) g;
       g2.setComposite(comp);
       super.paint(g);
    }
    public void setAlpha(float alpha) {
       comp = comp.derive(alpha);
    }
    /*
    private String folder2;
    private String LokasiGambar2() {
        folder2 = System.getProperty("user.dir") + File.separator + "Gambar/background.jpg";
        return folder2 ;
    }
    
    */
    /*
 @Override
    protected void paintComponent(Graphics g) {
        /*
         * 1 = warna
         * 2 =
         * 3 =
         * 4 = Transparant
         
        g.setColor(new Color(100, 100, 4, 245));
        /*
         * 1 = warna = 0
         * 2 =  warna = 0
         * 3 = terlihat melingar 150
         * 4 = terlihat melingar 150
         *
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 150, 150);
    } 
     */
   
    int NoJTabbed = 1;
    private void AksiAksi(){
        JComboBoxFoodCost.setSelectedIndex(-1);
        final JTextField JTextFieldItem = (JTextField)JComboBoxFoodCost.getEditor().getEditorComponent();
        JTextFieldItem.setText("");
        JTextFieldItem.addKeyListener(new app_search1(JComboBoxFoodCost));
        
        JComboBoxFoodCost.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ResetTampilan();
                AmbilDataUntukPorsi();   
                new javax.swing.DefaultComboBoxModel( app_search_data_beban.getData().toArray());
                
                if (JComboBoxFoodCost.isPopupVisible()){
                    JComboBoxFoodCost.setEditable(false);
                }
            }
        });
        
        JComboBoxFoodCost.addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e) {
                JComboBoxFoodCost.setEditable(true);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {             
            }
            @Override
            public void mouseEntered(MouseEvent e) {             
            }
            @Override
            public void mouseExited(MouseEvent e) {            
            }
        });
        
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
                      String DataNoTransaksi      = (String) JComboBoxFoodCost.getSelectedItem();
                      DataNoTransaksi             = FilterNamaTransaksiSaja(DataNoTransaksi);
                      AmbilDataBahanResep( DataNoTransaksi);
                      PerhitunganAll ();
                      PrintView.setVisible(true);     
                      Kenaikan.setText(JTextFieldPersenKeuntungan.getText() + " % ");
                 }
                
            }
        });

        AntiHuruf.SetAntiAngka(JTextFieldBiayaKemasan);
        AntiHuruf.SetAntiAngka(JTextFieldBiayaLainnya);
        AntiHuruf.SetAntiAngkaLimit(JTextFieldPersenKeuntungan, 3);
        AntiHuruf.SetAntiAngkaLimit(JTextFieldPersenTenagaKerja, 3);
        AntiHuruf.SetAntiAngkaLimit(JTextFieldPersenBebanLain, 3);
        AntiHuruf.SetAntiAngkaLimit(JTextFieldPersenPenyusutan,3);

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
        hashMap.put("LabaBersih0", jLabel47.getText());
        hashMap.put("LabaBersih1", jLabel44.getText());
        hashMap.put("LabaBersih2", jLabel43.getText());
        hashMap.put("TotalFoodCost", ConvertAngka.NilaiRupiah(TotalProduksi));
        hashMap.put("BiayaKemasan", JTextFieldBiayaKemasan.getText());
        hashMap.put("LainLainnya", JTextFieldBiayaLainnya.getText());
        hashMap.put("TotalPro", ConvertAngka.NilaiRupiah(TotalAkhirProduksi() )+ "");
        hashMap.put("JumlahPorsi", JLabelBebanPorsi.getText());
        hashMap.put("Kenaikan", Kenaikan.getText());
        
        try {

            String x    = System.getProperty("user.dir")+"\\ReportJasper\\ReportLaporanPerhitunganBiaya.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
            JasperViewer.viewReport(jasperPrint, false);
            
            
            JRViewer vw = new JRViewer(jasperPrint);
            //getContentPane().add(vw);

            //vw.setBounds(5, 5, 800, 900);
            //setVisible(true);
            //setSize(900, 650);
            
            //ReportJasperInternal.RencanaHarga xx = new ReportJasperInternal.RencanaHarga(vw);
            //xx.setVisible(true);
            //add(xx);
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }  
    }
    
    private void Tampilan(){

        PrintView.setVisible(false);
    }
    
    private void ResetTampilan(){
        JTextField[] Data = {
            JTextFieldBiayaKemasan, JTextFieldBiayaLainnya,
            JTextFieldPersenKeuntungan, JTextFieldPersenTenagaKerja,
            JTextFieldPersenBebanLain, JTextFieldPersenPenyusutan};
        for (int a = 0; a < Data.length; a++){
            Data[a].setText("");
        }
        
        JLabel[] DataLabel = {
          JLabelBebanKet,
          jLabel16,
          jLabel17,
          jLabel19,
          jLabel18,
          jLabel26,
          jLabel27,
          jLabel29,
          jLabel28,
          jLabel34,
          jLabel33,
          jLabel41,
          jLabel42,
          jLabel44,
          jLabel43
        };
        for (int a = 0; a < DataLabel.length; a++){
            DataLabel[a].setText("");
        }
        
        DefaultTableModel dtm = (DefaultTableModel) JTabelBiaya.getModel();
        dtm.setRowCount(0); 
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

    
    private String FilterNamaTransaksiSaja(String Data){
        try {
            String PartRegex = "(.*)?(\\s)*(--)+(\\s)*";
            Data   = Data.replaceAll( PartRegex, "");
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
    private String FilterTransNoSaja(String Data){
        try {
            String PartRegex = ".*(\\s)*(--)+(\\s)";
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
        String JumlahPorsiText = null, JumlahSatuan = null;
        
        
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               int JumlahHarga = 0;
               JLabelBebanPorsi.setText("");  
               if (DataResep.contains("-1")){
                   DataResep           = FilterTransNoSaja(DataResep);
                   
                   /*
                    * SELECT sum(b.qty) * a.porsi as total_porsi_pesanan FROM header_pesanan a 
                    * inner join detail_pesanan b on a.trans_no = b.trans_no 
                    * where a.nama_pemesan = 'bp ari'
                    *
                    //JOptionPane.showMessageDialog(null, "xxx " + DataResep);
                    DataResep = DataResep.replaceAll("(-1)$", "");
                    HQ = stm.executeQuery("SELECT sum(b.qty) * a.porsi as porsi_resep "
                            + "FROM header_pesanan a inner join detail_pesanan b on a.trans_no = b.trans_no "
                            + "where a.nama_pemesan = '"+ DataResep + "'");*/
                    HQ = stm.executeQuery("select porsi as porsi_resep , status_pesanan from header_penerimaan where trans_no = '"+ DataResep + "'");
                    JLabelBebanKet.setText("Data Dari Pesanan");
                     while(HQ.next()  ){
                        JumlahPorsiText         = HQ.getString("porsi_resep");        
                        JumlahSatuan            = HQ.getString("status_pesanan");  
                    }
                     JLabelBebanPorsi.setText(JumlahPorsiText + " " + JumlahSatuan ); 
               }
               else {
                    DataResep           = FilterNamaResepSaja(DataResep);
                    HQ = stm.executeQuery("SELECT  porsi_resep, nama_resep from header_resep where nama_resep ='"+ DataResep + "'");
                    JLabelBebanKet.setText("Data Dari Resep");
                    while(HQ.next()  ){
                        JumlahPorsiText         = HQ.getString("porsi_resep");                           
                    }
                    JLabelBebanPorsi.setText(JumlahPorsiText + " Porsi"); 
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
        AntiHuruf.SetAntiAngkaLimit(JumlahPorsi,12);
        
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
                    JLabelBebanPorsi.setText(QtyPosrsi + " Porsi");
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
        
        String header[] = {"No", "Nama Bahan", "1 Porsi",Porsi , "Harga Satuan"};
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
        int jarak_colom[] = {40,250,100,150, 150};
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
        double TotalHrg1 ;
        double TotalHrg2 = 0.0 ;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               System.out.println( JLabelBebanKet.getText() + " sss ");
               if (JLabelBebanKet.getText().equalsIgnoreCase("Data Dari Resep")) {

                    AA = "SELECT detail_resep.qty_1_porsi, detail_resep.satuan_1_porsi, " +
                    "header_penerimaan.trans_no, "
                    + "header_penerimaan.judul_resep, "
                    + "header_penerimaan.total_biaya, "
                    + "header_penerimaan.total_rill, " 
                    + "detail_trans_penerimaan.trans_no, "
                    + "detail_trans_penerimaan.nama_bahan, "
                    + "detail_trans_penerimaan.qty, "
                    + "detail_trans_penerimaan.satuan, " 
                    + "detail_trans_penerimaan.harga_rill "
                    + "FROM detail_trans_penerimaan RIGHT JOIN header_penerimaan " +
                    "ON detail_trans_penerimaan.trans_no = header_penerimaan.trans_no " +
                    "LEFT JOIN detail_resep " +
                    "ON header_penerimaan.judul_resep = detail_resep.namresep " +
                    "WHERE detail_trans_penerimaan.trans_no = '"+ NoTransaksi+ "'" +
                    " group by detail_trans_penerimaan.nama_bahan  ";
                  
               }
               else {
                   /*
                    * Cara ambil resep dari pesanan
                    * header_penerimaan.judul_resep = header_pesanan.nama_pemesan
                    * header_pesanan.trans_no = detail_pesanan.trans_no
                    * detail_pesanan.nama_menu = detail_resep.namresep
                    * 
                    * SET @v1 := (SELECT COUNT(*) FROM user_rating);
                      SELECT @v1;
                      
                      SET @detailpesanan := (select nama_pemesan, trans_no from header_pesanan where nama_pemesan ='');
                      select @detailpesanan;
                      * 
                      select * from header_penerimaan hp inner join header_pesanan he on hp.judul_resep = he.nama_pemesan
                      inner join detail_pesanan dp on he.trans_no = dp.trans_no where hp.judul_resep = '' )
                    */ 
                   
                   AA = "select hp.total_biaya, dp.qty, dp.satuan, hp.total_rill, dp.harga_rill, dp.nama_bahan "
                           + " from header_penerimaan hp inner join detail_trans_penerimaan dp "
                           + "on hp.trans_no = dp.trans_no "
                           + "where hp.trans_no ='"+ NoTransaksi+ "'";
                   AAA = true;
               }
               HQ = stm.executeQuery(AA );                                
               baris = HQ.getRow();                                          
               while(HQ.next()  ){
                   if (AAA){
                        Qty             = HQ.getString("qty");
                        Qty1            = "";
                        Satuan1         = "";
                        Satuan          = HQ.getString("satuan");
                   }
                   else {
                        Qty             = HQ.getString("qty");
                        Qty1            = HQ.getString("qty_1_porsi");
                        Satuan1         = HQ.getString("satuan_1_porsi");
                        Satuan          = HQ.getString("satuan");                      
                   }           
                    TotalHrg1       = HQ.getDouble("harga_rill");
                    NamaBahan1      = HQ.getString("nama_bahan");               
                    Satuan          = FilterKeterangnSatuan(Satuan);
                    Satuan1         = FilterKeterangnSatuan(Satuan1);
                    TotalBiaya      = HQ.getString("total_rill");
                   
                    /*
                     * Hitung Total Produksi
                     * "No", "Nama Bahan", "Jumlah","Porsi", "Harga Satuan"
                     */
                    TotalHrg2   = TotalHrg2 + TotalHrg1 ;                    
                    String[] add         = {String.valueOf(HQ.getRow()).toString(), NamaBahan1, Qty1 + " " + Satuan1, ConvertAngka.NilaiRupiah(Qty) +" " + Satuan, ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(TotalHrg1)) + "" };
                    Modeltabel.addRow(add);
               }
               TotalProduksi    =   TotalHrg2+"";
           }
           catch (Exception ex){
                System.out.println("Error (4466) "+ ex);
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
         double HargaPerporsi   =  HargaJualPerPorsi(TotalAkhirProduksi(), JTextFieldPersenKeuntungan.getText(),Porsi);     
         String BTenagaKerja    = JTextFieldPersenTenagaKerja.getText();
         String BPenyusutan     = JTextFieldPersenPenyusutan.getText();
         String BLainnya        = JTextFieldPersenBebanLain.getText();
         
         /*
          * =100/( n Mark up ) x Food Cost
          */
         jLabel16.setText("= 100 / ("+ JTextFieldPersenKeuntungan.getText() + " )  x " +  ConvertAngka.NilaiRupiah(TotalAkhirProduksi() )+ " ");
         jLabel17.setText("= "+ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(KenaikanYangDiHarapkanInt)));
         
         /*
          * Harga Jual Perporsi
          */
         jLabel19.setText("= "+ ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(KenaikanYangDiHarapkanInt))   + " / " +  Porsi);
         jLabel18.setText("= "+ ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(HargaPerporsi )));
         
         /*
          * Laba kotor
          */
         double LabaKotor      = LabaKotorDanLabaBersih (KenaikanYangDiHarapkanInt, (int) TotalAkhirProduksi());
         jLabel26.setText("= " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(KenaikanYangDiHarapkanInt)) + " - " + ConvertAngka.NilaiRupiah(TotalAkhirProduksi()));
         jLabel27.setText("= " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(LabaKotor)));
         
         
         /*
          * Beban-beban
          * BebanAll();
          */
         double BTenagaKerjaHtng   =  BebanAll(BTenagaKerja ,(double) LabaKotor);
         double BPenyusutanHtng    =  BebanAll(BPenyusutan ,(double) LabaKotor);
         double BLainnyaHtng       =  BebanAll(BLainnya ,(double) LabaKotor);
         
         String Beban1, Beban2, Beban3;
         Beban1     = ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(BTenagaKerjaHtng ));
         Beban2     = ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(BPenyusutanHtng));
         Beban3     = ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(BLainnyaHtng));
         
         
         
         jLabel29.setText("= " + BTenagaKerja + "% x " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(LabaKotor)));
         jLabel28.setText("= " +Beban1);
         
         jLabel34.setText("= " +BPenyusutan + "% x " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(LabaKotor)));
         jLabel33.setText("= " +Beban2);
         
         jLabel41.setText("= " +BLainnya + "% x " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(LabaKotor)));
         jLabel42.setText("= " + Beban3);
         
         /*
          * Laba Bersih
          * LabaKotorDanLabaBersih
          */
         double[] data = {(double)BTenagaKerjaHtng,(double)BPenyusutanHtng, (double)BLainnyaHtng };
         double TotalBeban     = TotalBeban( data);
         jLabel47.setText("= " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(LabaKotor) )+ " - (" + Beban1 +" + " + Beban2 + " + " + Beban3 +" )");
         jLabel44.setText("= " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(LabaKotor) )+ " - " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(TotalBeban)));      
         jLabel43.setText("= " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(LabaKotorDanLabaBersih( (double)LabaKotor ,(double) TotalBeban))));
     
     }
     
     private double  TotalAkhirProduksi(){
         double TotalAkhirProduksi   =   HitunganTotalProduksi(TotalBiaya, JTextFieldBiayaLainnya.getText(), JTextFieldBiayaKemasan.getText());

         return TotalAkhirProduksi;
     }
     
     /*
      * Total food cost
      */
      private double HitunganTotalProduksi(String TotalProduksi, String BiayaLainnya, String BiayaKemasan){
        if (BiayaLainnya.equals("") ){
            BiayaLainnya = "0";
        }
        if (BiayaKemasan.equals("")){
            BiayaKemasan = "0";
        } 
        if (TotalProduksi.equals("")){
            TotalProduksi = "0";
        }  
        return  Double.valueOf(TotalProduksi).doubleValue() + Double.valueOf(BiayaLainnya).doubleValue() + Double.valueOf(BiayaKemasan).doubleValue();
    }
    
    double KenaikanYangDiHarapkanInt;
    double HasilSementaraHrgJual;
    
    private double HargaJualPerPorsi(double TotalAkhirProduksi, String KenaikanYangDiHarapakan1, String JumlahPorsi){

        if (KenaikanYangDiHarapakan1.equals("")){
            KenaikanYangDiHarapakan1 = "0";
        } 
        if (JumlahPorsi.equals("")){
            JumlahPorsi = "0";
        } 
        JumlahPorsi = JumlahPorsi.replaceAll("\\s.*", "");
                
        double KenaikanYangDiHarapakan  = Double.valueOf(KenaikanYangDiHarapakan1).doubleValue() ;
        double IntPorsi                 = Double.valueOf(JumlahPorsi).doubleValue() ;
              
        /*
        System.out.println(TotalAkhirProduksi + " + "+ KenaikanYangDiHarapakan/100 + " * "+  TotalAkhirProduksi + " = "+
                TotalAkhirProduksi + ( (KenaikanYangDiHarapakan/100 ) * TotalAkhirProduksi));
         */
        this.HasilSementaraHrgJual          =   (100/KenaikanYangDiHarapakan) * TotalAkhirProduksi;
        this.KenaikanYangDiHarapkanInt      =  HasilSementaraHrgJual;
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
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        JComboBoxFoodCost = new javax.swing.JComboBox();
        JLabelBebanPorsi = new javax.swing.JLabel();
        JLabelBebanKet = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelBiaya = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        JTextFieldBiayaKemasan = new javax.swing.JTextField();
        JTextFieldBiayaLainnya = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        JTextFieldPersenKeuntungan = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        JTextFieldPersenTenagaKerja = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        JTextFieldPersenBebanLain = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        JTextFieldPersenPenyusutan = new javax.swing.JTextField();
        PrintView = new javax.swing.JButton();
        JButtonProses1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
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
        jLabel47 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        Kenaikan = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Rencana Harga");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/logo.png"))); // NOI18N
        setVisible(true);

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Pilih Total Food Cost");

        JComboBoxFoodCost.setEditable(true);
        JComboBoxFoodCost.setModel(new javax.swing.DefaultComboBoxModel( app_search_data_beban.getData().toArray()));

        JLabelBebanPorsi.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        JLabelBebanPorsi.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        JLabelBebanKet.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        JLabelBebanKet.setForeground(new java.awt.Color(255, 51, 51));

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(51, 51, 255));
        jLabel54.setText("Rencana Kerja Metode Konvensional");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel54)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JLabelBebanKet, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JLabelBebanPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JComboBoxFoodCost, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(JComboBoxFoodCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JLabelBebanKet, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JLabelBebanPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 3, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
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

        jPanel7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel12.setText("Biaya Lainnya");

        jLabel11.setText("Biaya Kemasan");

        JTextFieldBiayaKemasan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        JTextFieldBiayaLainnya.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel2.setText("Kenaikan Di Harapkan");

        JTextFieldPersenKeuntungan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel5.setText("Upah Tenaga Kerja");

        JTextFieldPersenTenagaKerja.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel7.setText("%");

        jLabel4.setText("%");

        jLabel8.setText("%");

        JTextFieldPersenBebanLain.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel6.setText("Beban Umum");

        jLabel10.setText("Beban Penyusutan");

        JTextFieldPersenPenyusutan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        PrintView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Print.png"))); // NOI18N
        PrintView.setText("Print View");
        PrintView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrintViewActionPerformed(evt);
            }
        });

        JButtonProses1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Load.png"))); // NOI18N
        JButtonProses1.setText("Proses");

        jLabel9.setText("%");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(JButtonProses1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PrintView, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(JTextFieldBiayaLainnya, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(JTextFieldBiayaKemasan)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addComponent(JTextFieldPersenKeuntungan, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(JTextFieldPersenBebanLain, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JTextFieldPersenPenyusutan, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(JTextFieldPersenTenagaKerja, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(10, 10, 10))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(JTextFieldBiayaKemasan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(JTextFieldBiayaLainnya, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(JTextFieldPersenKeuntungan)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JTextFieldPersenTenagaKerja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JTextFieldPersenBebanLain)
                        .addComponent(jLabel8))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(JTextFieldPersenPenyusutan)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PrintView, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonProses1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
        jLabel32.setText("Beban ");

        jLabel33.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel33.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel34.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel34.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel35.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel35.setText("= Beban Penyusutan Alat ( % ) x Laba Kotor");
        jLabel35.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel36.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel36.setText("Biaya Penyusutan Alat");

        jLabel39.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel39.setText("Biaya Umum ( Listrik, Air dll )");

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
                            .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(15, 15, 15))
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jLabel47.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel47.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                            .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(15, 15, 15))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel46)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel17.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel15.setText("=100 / ( n Mark up ) x Food Cost");
        jLabel15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel14.setText("Kenaikan Yang Diharapkan");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel18.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel19.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel20.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel20.setText("= ( Variabel cost + Mark Up ) / Jumlah Porsi");
        jLabel20.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel22.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel22.setText("Harga Jual Per Porsi");

        Kenaikan.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        Kenaikan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jLabel37.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel37.setText("Harga Jual");

        jLabel27.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel27.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel26.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel26.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel25.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel25.setText("= Total Harga Jual - Food Cost");
        jLabel25.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel23.setText("Laba Kotor");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(15, 15, 15))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Kenaikan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(134, 134, 134))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(Kenaikan, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel37)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addGap(6, 6, 6)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Metode Konvensional", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PrintViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrintViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PrintViewActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonProses1;
    private javax.swing.JComboBox JComboBoxFoodCost;
    private javax.swing.JLabel JLabelBebanKet;
    private javax.swing.JLabel JLabelBebanPorsi;
    private javax.swing.JTable JTabelBiaya;
    private javax.swing.JTextField JTextFieldBiayaKemasan;
    private javax.swing.JTextField JTextFieldBiayaLainnya;
    private javax.swing.JTextField JTextFieldPersenBebanLain;
    private javax.swing.JTextField JTextFieldPersenKeuntungan;
    private javax.swing.JTextField JTextFieldPersenPenyusutan;
    private javax.swing.JTextField JTextFieldPersenTenagaKerja;
    private javax.swing.JLabel Kenaikan;
    private javax.swing.JButton PrintView;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
