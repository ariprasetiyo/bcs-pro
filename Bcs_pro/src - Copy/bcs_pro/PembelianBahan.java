/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * PR konsep pembelian bahan
 * 1. Bahan pada pembelian bahan dicocok kan pada master barang
 * 2. Jika sama maka harga barang pada pembelian akan ikut master barang
 * 3. Jika tidak sama maka akan disuruh input harga baru dan akan create automatis di master barang
 *    - Nama barang
 *    - Satuan
 *    - Harga
 *    - Groups item
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.ButtonJTableKeDua;
import SistemPro.Colom_table;
import SistemPro.ObjectPenerimaanDatatabse;
import SistemPro.ObjectView;
import SistemPro.PembelianObject;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.ResepObject;
import SistemPro.app_search1;
import SistemPro.app_search_data_resep;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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
public class PembelianBahan extends javax.swing.JInternalFrame {
    
    private DefaultTableModel TabelModelPb;
    private DefaultTableModel TabelModelPenerimaan;
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah = new RenderingTengah();
    SistemPro.DetecSatuan SatuanSelect = new SistemPro.DetecSatuan();
    SistemPro.KomaToString ConvertAngka = new SistemPro.KomaToString();
    private  SistemPro.ComponentHanyaAngka HanyaAngka = new SistemPro.ComponentHanyaAngka();
    private String TglBuatResep, A;
    private String TglBuatWr, SatuanSementara;
    private double AAA;
    
    private SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    private Connection K = KD.createConnection();
    private boolean JTabelEdit;
    private boolean JTabelEditPenerimaan =false;
    /**
     * Creates new form PembelianBahan
     */
    public PembelianBahan() { 

        initComponents();
        
        /*
         * Membuat awal satuan 
         * Untuk mendeteksi satuan sebelumnnya
         * Difungsikan pada saat select item kedua maka convert KG ke Gram berjalan
         */
        SatuanSementara = (String )JComboBoxSatuan.getSelectedItem();
        AksiAksi();
        SetTampilanReset(false, true, true);
        SetTampilanUmum();
        TabelPesananBahan();
        if (JTabelPesananBahan.isEditing()) {             

            JTabelPesananBahan.getCellEditor().stopCellEditing();
          }
        /*
         * Penerimaan Pesanan Bahan
         */
        AksiPenerimaanBahan();
        TabelPenerimaanBahan();
        KembalianPenerimaan(); 
        
    }
    
    private void Printerr(){  
    
        PembelianBahanPrintView PrintPembelian =    new PembelianBahanPrintView(JTextFieldTransNo.getText(), TglBuatResep);

    }
    HashMap DatahashMap;
    boolean AksiBahan = false;
    private void AksiAksi(){
        
        HanyaAngka.SetAntiAngkaLimit(JFormattedTextFieldPbUangMuka, 15);
        
        JTextPbBahan.setSelectedIndex(-1);
        final JTextField JTextFieldItem = (JTextField)JTextPbBahan.getEditor().getEditorComponent();
        JTextFieldItem.setText("");
        JTextFieldItem.addKeyListener(new app_search1(JTextPbBahan));
        
        JTextPbBahan.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent a){
                  
                 //AmbilHarga((String )JTextPbBahan.getSelectedItem());
                          /*
                   * Deteksi apakah item tersebut ada di master barang convert
                   * Jika Iya => Mka satuan bisa di convert
                   * Jika tdk => tdk bisa di konvert
                   * Saat pemeilihan barang, harus mendeteksi satuan tidak baku, satuan baku dan satuan pada harga, KG atau ML
                   */
                 A = (String )JTextPbBahan.getSelectedItem();
                 boolean Data = SatuanSelect.DetectConvert(A, K); 
                 if ( Data == true){
                     DatahashMap = SatuanSelect.hashMap;
                     //System.out.println(DatahashMap + "xxx");
                     SatuanSelect.IsiSatuanPadaJComboBox(JComboBoxSatuan, SatuanSelect.hashMap );
                     AmbilHarga(A);
                     
                 }
                 else {
                     AmbilHarga(A);
                     //System.out.println(SatuanPublic);
                     SatuanSelect.IsiSatuanPadaJComboBox(JComboBoxSatuan, SatuanPublic);
                 }
                // TidakBisaConvert = (String ) JComboBoxSatuan.getSelectedItem();
                 
                 /*
                 try {
                      SatuanSelect.DetectSatuan(SatuanPublic, JComboBoxSatuan);   
                 }
                 catch (Exception X){
                      JOptionPane.showMessageDialog(null, "Error 13763: Silahkan coba lagi");
                 } */
            }
        });
        
        /*
         * Aksi combobox satuan
         */
        JComboBoxSatuan.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                 try{
                    JTextFieldPbQty.setText(
                            ConvertAngka.NilaiRupiah(
                            SatuanSelect.ConvertSatuan(JComboBoxSatuan, JTextFieldPbQty,DatahashMap)));
                    
                    /*
                    if (JComboBoxResepSatuan.getSelectedItem().equals("Sdm") ||
                        JComboBoxResepSatuan.getSelectedItem().equals("Sdt")    ){
                        JTextFieldResepQty.setText(ConvertAngka.NilaiRupiah(ConvertSdmKeGram() + ""));
                        JComboBoxResepSatuan.setSelectedIndex(1);
                    }
                     JComboBoxResepSatuan1Porsi.setSelectedIndex(JComboBoxResepSatuan.getSelectedIndex()); 
                     */
                    
                    
                 }
                catch(Exception X){   
                } 
                 if (JComboBoxSatuan.getSelectedItem() != null){
                     ConvertSdmKeGram();
                 }
                
                 
                /*
                SatuanSementara = (String ) JComboBoxSatuan.getSelectedItem();
                AddHargaManual();
                A = (String )JComboBoxSatuan.getSelectedItem();
                if (A.equals("Sdm ( Sendok Makan )") ||
                    A.equals("Sdt ( Sendok Teh)")
                    ){
                    JTextFieldPbQty.setText(ConvertAngka.NilaiRupiah(ConvertSdmKeGram() + ""));
                    JComboBoxSatuan.setSelectedIndex(1);
                }
                else if (A.equals("g ( Gram )") ||
                         A.equals("Kg ( Kilogram )")  ||
                         A.equals("L ( Litter )") ||
                         A.equals("Ml ( Mili Liter )")){
                    JTextFieldPbQty.setText(ConvertAngka.NilaiRupiah(ConvertSdmKeGram() + ""));
                }
               // if (SatuanSelect.ValidasiSatuan2(SatuanPublic)){
                //    SatuanSelect.ValidasiSatuan ((String)JComboBoxSatuan.getSelectedItem(), JComboBoxSatuan);
               // } */     
            }
        });
               
        JButtonPesananPrint2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                PrintJasperReportPesanan();
            }
        });
        
        JButtonPbBrowse.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {    
                SetTampilanReset(false, false, false);
                ViewPembelianYangSudahDiBuat();    
                SetTampilanJButton(false, true, true, true,true, true);
                CaraInput = true;               
            }
        });
        JButtonPbNew.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SetTampilanAksi();
                SetTampilanReset(false, true, true);
                SetTampilanJButton(false, false, false, true,true, false);
                JPanelPesananHeader.setEnabled(true);
                CaraInput = false;
                CallPembelianView  = true;
            }
        });
        
        JButtonAddPb.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SetTampilanJButton(true, false, false, true,true, true);
                JPanelPesananHeader.setEnabled(true);
                JTabelPesananBahan.setEnabled(true);
                double Abc = Double.valueOf(ConvertAngka.RupiahKeDoubel(JTextFieldPbHarga.getText())).doubleValue();
                double Abd = Double.valueOf(ConvertAngka.RupiahKeDoubel(JTextFieldPbQty.getText())).doubleValue();
                
                if (Abc <= 0.0 || Abd <= 0.0 ){
                    JOptionPane.showMessageDialog(null, "Harga kosong atau qty kosong");
                }
                else {
                    AddDiTabelPesananBahan();
                    PerhitunganTotalPesanan2( TabelModelPb, "TabelModelPb");
                }
            }
        });
        
         JComboBoxPbCaraPesan.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if ( JComboBoxPbCaraPesan.getSelectedItem().equals("Dari Resep") && CallPembelianView  == true){
                    ViewResepYangSudahDiBuat();
                    JComboBoxPbCaraPesan.setEnabled(false); 
                    JTabelPesananBahan.setEnabled(true);
                    PerhitunganTotalPesanan2( TabelModelPb, "TabelModelPb");
                }
                else if (JComboBoxPbCaraPesan.getSelectedItem().equals("Manual" ) && CallPembelianView  == true){
                    SetTampilanAksi();
                    JComboBoxPbCaraPesan.setEnabled(false);
                }
                else if(JComboBoxPbCaraPesan.getSelectedItem().equals("Dari Pesanan") && CallPembelianView  == true){
                    ViewPesanan();
                    JComboBoxPbCaraPesan.setEnabled(false);
                    PerhitunganTotalPesanan2( TabelModelPb, "TabelModelPb");
                }
                else if(JComboBoxPbCaraPesan.getSelectedItem().equals("Dari Resep") ||JComboBoxPbCaraPesan.getSelectedItem().equals("Manual" ) ){
                    
                }
                
                else if (JComboBoxPbCaraPesan.getSelectedItem().equals("..."))  {
                    JOptionPane.showMessageDialog(null, "Pilih cara input \"Manual\" atau \"Dari Resep\" ?");          
                }
            }
        });
         
         JButtonPbSave.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){                
                 TabelResepSaveDatabase(); 
                 JPanelInputPbBahan.setVisible(false);
             }
         });
         
         JButtonPbPrint.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){                
                 SetTampilanJButton(false, false, false, true,true, true);
                 Printerr() ;     
             }
         });
         JButtonPbDelete.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){  
                 DeleteDataResep();
                 SetTampilanReset(false, false, false);
                 SetTampilanJButton(false, false, false, true,true, false);
                 JTabelPesananBahan.setEnabled(true);
                 JPanelPesananHeader.setEnabled(true);
                 SetTampilanJButton(true, false, false, true,true, false);
                 SetTampilanHeader(true);
       
             }
         });
         
         JButtonPbEdit.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){  
                SetTampilanJButton(true, false, false, true,true, false);
                JTabelEditPenerimaan = true;
                JTabelPesananBahan.setEnabled(true);
                SetTampilanHeader(true);
                JComboBoxPbCaraPesan.setEnabled(false);
                
                CC = (String )JComboBoxPbCaraPesan.getSelectedItem();
                if (CC.equalsIgnoreCase("Dari Resep") || CC.equalsIgnoreCase("Dari Pesanan") ) {
                   JTabelPesananBahan.setEnabled(false);
                }
             }
         });
          
         /*
          * Kejadian JTabel   JTabelPesananBahan untuk menghitung biaya
          */
        JTabelPesananBahan.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel =  JTabelPesananBahan.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //TableModelEvent tme = JTabelPesananBahan;
                 PerhitunganTotalPesanan2( TabelModelPb, "TabelModelPb");
            }
             public void tableChanged(TableModelEvent tme) {
                PerhitunganJumlahTotalPesanan( tme,  TabelModelPb, "TabelModelPb");
            }
         });   
        
        /*
         * Kejadian JTabel   JTabelPenerimaan untuk menghitung total biaya
         */
        JTabelPenerimaanBahan.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel2 =  JTabelPenerimaanBahan.getSelectionModel();
        cellSelectionModel2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //TableModelEvent tme = JTabelPesananBahan;
                 PerhitunganTotalPesanan2( TabelModelPenerimaan, "TabelModelPenerimaan");
                 KembalianPenerimaan();
            }
             public void tableChanged(TableModelEvent tme) {
                PerhitunganJumlahTotalPesanan( tme,  TabelModelPenerimaan, "TabelModelPenerimaan");
                KembalianPenerimaan();
            }             
         });
        
        JButtonPenerimaanPrint1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                 PrintJasperReport();
            }
        });
    }
    
    /*
     * Convert data dari satuan sdm ke gram
     */
    String TidakBisaConvert ;
    private double ConvertSdmKeGram(){
        System.out.println(SatuanPublic + " = " + JComboBoxSatuan.getSelectedItem());
        if (!SatuanPublic.equalsIgnoreCase((String) JComboBoxSatuan.getSelectedItem())){
            if (JComboBoxSatuan.getSelectedItem().equals("G")){
                PerhitunganConvertHarga(1000 , 0); 
                SatuanPublic = (String ) JComboBoxSatuan.getSelectedItem();
            }
            else if (JComboBoxSatuan.getSelectedItem().equals("ML")){
                 PerhitunganConvertHarga(1000000 , 0); 
                  SatuanPublic = (String ) JComboBoxSatuan.getSelectedItem();
            }
            else if (JComboBoxSatuan.getSelectedItem().equals("KG")){
                PerhitunganConvertHarga(1000 , 1);    
                 SatuanPublic = (String ) JComboBoxSatuan.getSelectedItem();
            }
            else if (JComboBoxSatuan.getSelectedItem().equals("L")){
                PerhitunganConvertHarga(1000000 , 1);
                 SatuanPublic = (String ) JComboBoxSatuan.getSelectedItem();
            }
        }        
        return 0;
    }
    
    /*
     * Digunakan untuk convert harga KG/G L/ML
     * int a = 1 berarti kalo
     * int a = 0 berarti bagi
     */
    private void PerhitunganConvertHarga(int NilaiConvert, int a){
        JOptionPane.showMessageDialog(null, "Data diconvert dari " + JComboBoxSatuan.getSelectedItem());  
        A = JTextFieldPbHarga.getText();
       /*
        * Convert harga satuan
        */
        Double HasilConvert = null;
        if (a == 0){
            HasilConvert = Double.valueOf(A).doubleValue() / NilaiConvert;
        }
        else if (a == 1){
            HasilConvert = Double.valueOf(A).doubleValue() * NilaiConvert;
        }

        HasilConvert =  ConvertAngka.RoundingDesimal(HasilConvert,10);
        JOptionPane.showMessageDialog(null, "Data diconvert dari " 
               + A + "  = " +  HasilConvert );  
        JTextFieldPbHarga.setText(HasilConvert + "");
        TidakBisaConvert = (String)JComboBoxSatuan.getSelectedItem();
        
    }

    private void AddHargaManual(){
     
            String ResepBahan           = (String )JTextPbBahan.getSelectedItem();
            if (ResepBahan == null){
                ResepBahan = "";
            }
            else {
                ResepBahan              = ResepBahan.replaceAll(".*--", "");
            }
            
            String Qty                  = JTextFieldPbQty.getText();
            Qty                         = ConvertAngka.RupiahKeDoubel(Qty);
            if (Qty.equalsIgnoreCase("")){
                Qty = "0";
            }
             ConvertHargaUntukBerat(ResepBahan ,( String ) JComboBoxSatuan.getSelectedItem(), Double.valueOf(Qty).doubleValue() ) ;            
    }
    
     private void PrintJasperReportPesanan(){
        DefaultTableModel de = (DefaultTableModel)JTabelPesananBahan.getModel();

        JRTableModelDataSource dataSource = new JRTableModelDataSource(de);

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();
        
        String date_po = kazaoCalendarDatePesanan.getKazaoCalendar().getShortDate();
        SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();

        N.SetKazaoToTglIndo(date_po);
        N.SetKazaoToBlnIndo(date_po);
        N.SetKazaoToThnIndo(date_po);

        String DatePo =N.GetTglIndoStrKazao() + " - " +N.GetBlnIndoStrKazao() + " - " +N.GetThnIndoStKazao();
        
        hashMap.put("Judul", JLabelPemberitahuanPb.getText());
        hashMap.put("NoTransaksi", JTextFieldTransNo.getText());
        hashMap.put("Supplier",JTextFieldPbPasar.getText());
        hashMap.put("TanggalBuat", DatePo);
        hashMap.put("Total", JTextFieldPbTotal.getText());
        hashMap.put("UangMuka", JFormattedTextFieldPbUangMuka.getText()); 
             
        try {
            String x    = System.getProperty("user.dir")+"\\ReportJasper\\ReportPenerimaanBarangPesanan.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }  
    }
    private void PrintJasperReport(){
        DefaultTableModel de = (DefaultTableModel)JTabelPenerimaanBahan.getModel();

        JRTableModelDataSource dataSource = new JRTableModelDataSource(de);

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();
        
        String date_po = kazaoCalendarDatePenerimaanWR.getKazaoCalendar().getShortDate();
        SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();

        N.SetKazaoToTglIndo(date_po);
        N.SetKazaoToBlnIndo(date_po);
        N.SetKazaoToThnIndo(date_po);

        String DatePo =N.GetTglIndoStrKazao() + " - " +N.GetBlnIndoStrKazao() + " - " +N.GetThnIndoStKazao();
        
        hashMap.put("Judul", JLabelPemberitahuanPenerimaan.getText());
        hashMap.put("NoTransaksi", JTextFieldPenermaanTransNo.getText());
        hashMap.put("Supplier",JTextFieldPenerimaanPasar.getText());
        hashMap.put("TanggalBuat", DatePo);
        hashMap.put("Note", JTextFieldPenerimaanNote.getText());
        hashMap.put("Total", JTextFieldPenerimaanTotal1.getText());
        hashMap.put("UangMuka", JTextFieldPenerimaanUangMuka.getText()); 
        hashMap.put("Kembali", JTextFieldPenerimaanKembalian.getText());
        hashMap.put("TotalRill", JTextFieldPenerimaanTotalRill.getText());
             
        try {
            String x    = System.getProperty("user.dir")+"\\ReportJasper\\ReportPenerimaanBarang.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }  
    }

    private void JTabelUntukPerhituanganAntarKolom(){
         int numRows     = JTabelPesananBahan.getSelectedRows().length;
         int AmbilRow    = JTabelPesananBahan.getSelectedRow() ;
         
         for(int i=0; i<numRows ; i++ ) {
                    
            /*
             * Ambil Data
             */

            String ColumHarga   = (String) TabelModelPb.getValueAt(AmbilRow, 4);
            String ColumQty     = (String) TabelModelPb.getValueAt(AmbilRow, 2);
            
            int ColumTotHrg  = Integer.valueOf(ColumHarga).intValue() * Integer.valueOf(ColumQty).intValue();
            TabelModelPb.setValueAt(ColumTotHrg, AmbilRow, 5);
        }
    }
    private void SetTampilanReset(boolean InputBahan, boolean JTabelPesanaaBahan, boolean JPanelHeader){
        JPanelInputPbBahan.setVisible(InputBahan);
        /*
         * Logika hapus semua data di jtable
         */
        //TabelModelOrder.getDataVector().removeAllElements();
        //TabelModelOrder.fireTableDataChanged();
        DefaultTableModel dtm = (DefaultTableModel) JTabelPesananBahan.getModel();
        dtm.setRowCount(0); 
        //JTextPbBahan.setText("");
        JTextFieldPbHarga.setText("");
        JTextFieldPbQty.setText("");
        JTextFieldPbTotal.setText("0");
        JLabelPemberitahuanPb.setText("");
        JTextFieldPbPasar.setText("");
        JFormattedTextFieldPbUangMuka.setText("");
        JTextFieldTransNo.setText("");
        JComboBoxPbCaraPesan.setEnabled(true);
        this.JTabelEdit = false;
        JTabelPesananBahan.setEnabled(InputBahan);    
        SetTampilanHeader( JPanelHeader);
    }
    private void SetTampilanHeader(boolean JPanelHeader){
        JTextFieldPbPasar.setEnabled(JPanelHeader);
        JComboBoxPbCaraPesan.setEnabled(JPanelHeader);
        JFormattedTextFieldPbUangMuka.setEnabled(JPanelHeader);
        kazaoCalendarDatePesanan.setEnabled(JPanelHeader);
    }
    private void SetTampilanAksi(){
        JPanelInputPbBahan.setVisible(true);
        JComboBoxPbCaraPesan.setEnabled(true);  
    }
    private void SetTampilanUmum(){
        SistemPro.ComponentHanyaAngka AntiHuruf = new SistemPro.ComponentHanyaAngka();
        AntiHuruf.SetAntiAngka(JTextFieldPbHarga);
        AntiHuruf.SetAntiAngkaPakeKoma(JTextFieldPbQty);

    }
    private void SetTampilanSetelahSave(){
        JTabelPesananBahan.setEnabled(false);
        JTextFieldPbPasar.setEnabled(false);
        JComboBoxPbCaraPesan.setEnabled(false);
        kazaoCalendarDatePesanan.setEnabled(false);
        JFormattedTextFieldPbUangMuka.setEnabled(false);
        JPanelInputPbBahan.setEnabled(false);
    }
    private void SetTampilanJButton(boolean Button, boolean Edit, boolean Delete, boolean Browse, boolean New, boolean Print){
        JButtonPbSave.setEnabled(Button);
        JButtonPbEdit.setEnabled(Edit);
        JButtonPbDelete.setEnabled(Delete);
        JButtonPbBrowse.setEnabled(Browse);
        JButtonPbNew.setEnabled(New);
        JButtonPbPrint.setEnabled(Print);
    }
    
    private void AddDiTabelPesananBahan(){
        String PbBahan      = (String )JTextPbBahan.getSelectedItem();
        PbBahan             = PbBahan .replaceAll(".*--", "");
        String PbHarga      = JTextFieldPbHarga.getText();
        String PbQty        = JTextFieldPbQty.getText();
        String PbSatuan     = (String) JComboBoxSatuan.getSelectedItem();
        SistemPro.KomaToString Koma = new SistemPro.KomaToString();
                   
        /*
         * Tidak ada koma
         */
        java.text.DecimalFormat decimalFormat2 = new java.text.DecimalFormat("#,##0");
        
        /*
         * Pehitungan grand total
         * Hapus Koma
         */
        PerhitunganTotalPesanan2( TabelModelPb, "TabelModelPb");
        
        JFormattedTextField JumlahPorsi  = new JFormattedTextField(NumberFormat.getIntegerInstance());
        
        SistemPro.NoUrut NoUrutan = new   SistemPro.NoUrut();
        
        /*
         * Set data no urut
         */
        int ax = JTabelPesananBahan.getRowCount() ;
        //JOptionPane.showMessageDialog(null, ax);
        if (ax == 0 ){
            NoUrutan.SetNoUrut(String.valueOf(ax).toString());
        }
        else if ( ax > 0) {
            ax = ax - 1;
            String ab = (String) JTabelPesananBahan.getValueAt(ax, 0);
            NoUrutan.SetNoUrut(ab);
        }
        
        /*
         * Validasi
         */       
        if ("".equals(PbBahan)){
             JOptionPane.showMessageDialog(null, "Data bahan tidak ada !", "Perhatian",JOptionPane.ERROR_MESSAGE);           
        }
        else if("".equals(PbBahan)){
            JOptionPane.showMessageDialog(null, "Data qty bahan tidak ada !", "Perhatian",JOptionPane.ERROR_MESSAGE);           
        }
        else if("".equalsIgnoreCase(PbHarga)){
            JOptionPane.showMessageDialog(null, "Data harga bahan tidak ada !", "Perhatian",JOptionPane.ERROR_MESSAGE);      
        }
            
        else {
            System.out.println(PbHarga + " 12");
            if (PbHarga.contains(".")){
            }
            else {
                PbHarga  = ConvertAngka.RupiahKeDoubel(PbHarga);
            }

            System.out.println(PbQty + " dan " + PbHarga);
            
            System.out.println(PbQty + " dan " + PbHarga);
            Object obj[] = new Object[10];        
            obj[0] = NoUrutan.GetNoUrut();
            obj[1] = PbBahan;         
            obj[2] = PbQty;
            obj[3] = PbSatuan;
            obj[4] =  ConvertAngka.NilaiRupiah(Double.valueOf(PbHarga).doubleValue());
            
            PbQty  = ConvertAngka.RupiahKeDoubel(PbQty);
            obj[5] = ConvertAngka.NilaiRupiah(ConvertAngka.RupiahKeDoubel(ConvertAngka.FormatDesimalRubahE9(Double.valueOf(PbHarga).doubleValue() * Double.valueOf(PbQty).doubleValue())));         
            TabelModelPb.addRow(obj);
        }       
    }
    private void TabelPesananBahan(){
        String header[] = {"No", "Bahan","Qty","Satuan", "Harga", "@Harga" ,"Action"};
        TabelModelPb = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) { 
                                //  nilai false agar tidak bisa di edit
                                if(colIndex == 6) {return true ;}
                                if(colIndex == 2) {return JTabelEdit ;}
                                //if(colIndex == 3) {return EditKolomQty ;}
                                return false;   //Disallow the editing of any cell
                        }
        };
        JTabelPesananBahan.setModel(TabelModelPb);
       
        /*
         * Rata tengah atau kanan table
         */
        JTabelPesananBahan.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelPesananBahan.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        JTabelPesananBahan.getColumnModel().getColumn(4).setCellRenderer( tengah );
        JTabelPesananBahan.getColumnModel().getColumn(5).setCellRenderer( tengah );
        JTabelPesananBahan.getColumnModel().getColumn(6).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(3).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(7).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         */      
        int jarak_colom[] = {40,300,60,100,100, 100, 80};
        Colom_table ukuran_colom = new Colom_table();
        ukuran_colom.ukuran_colom(JTabelPesananBahan, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTabelPesananBahan.setName("Pesanan Barang");
        JTabelPesananBahan.getColumnModel().getColumn(6).setCellRenderer(  new ButtonJTable("Delete"));
        JTabelPesananBahan.getColumnModel().getColumn(6).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelPb, JTabelPesananBahan));
       
        /*
         * Disable drag colum tabel
         */       
        JTabelPesananBahan.getTableHeader().setReorderingAllowed(false);
        
        /*
         * Jika ada perubahan data pada cell jtable
         */
        TabelModelPb.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent tme) {
                PerhitunganJumlahTotalPesanan( tme, TabelModelPb, "TabelModelPb");
            }
        });
    }
    
    private void  PerhitunganJumlahTotalPesanan(TableModelEvent tme, DefaultTableModel ModelTabel, String NamaModel){
        if (tme.getType() == TableModelEvent.UPDATE) {
                    
            /*
             *  tme.getFirstRow() = row
             *  tme.getColumn() = colum
             */                 
            if ( tme.getColumn() == 4 || tme.getColumn() == 2 || tme.getColumn() == 6  ){            
                /*
                 * Filter karakter aneh
                 */
                String a = "[/*\\-\\(\\)<>_\\=+:\";\'\\\\#\\$&\\^\\}\\{%~`\\|\\[\\]\\!\\?\\@a-zA-Z]";

                String HargaJTabel = String.valueOf( ModelTabel.getValueAt(tme.getFirstRow(),4)).toString();
                String HargaQty    = (String) ModelTabel.getValueAt(tme.getFirstRow(),2);
                
                /*
                 * Convert Rupiah ke doubel
                 */
                HargaJTabel = ConvertAngka.RupiahKeDoubel(HargaJTabel);
                HargaQty    = ConvertAngka.RupiahKeDoubel(HargaQty);
                
                HargaJTabel = HargaJTabel.replaceAll(a, "x");
                HargaQty    = HargaQty.replaceAll(a, "x");

                if (HargaQty.contains("x") ||  HargaQty.equals("")  ) {
                    ModelTabel.setValueAt("0", tme.getFirstRow(), 2);
                    getToolkit().beep();
                }

                if (HargaJTabel.contains("x") ||  HargaJTabel.equals("")  ) {
                    ModelTabel.setValueAt("0", tme.getFirstRow(), tme.getColumn());
                    getToolkit().beep();
                }                       
                else {                            
                    double PesananHarga = Double.valueOf(HargaJTabel).doubleValue();
                    double PesananQty   = Double.valueOf(HargaQty ).doubleValue();
                    
                    /*
                     * Pesananan * qty
                     */
                    ModelTabel.setValueAt(ConvertAngka.NilaiRupiah(PesananHarga * PesananQty), tme.getFirstRow(),5);   
                    PerhitunganTotalPesanan2(ModelTabel, NamaModel);
                }               
            }
        }
    }
    
    private void PerhitunganTotalPesanan2(DefaultTableModel ModelTabel, String NamaModel){
        int JumlahRowPesan= ModelTabel.getRowCount();

        double Tot;
        if (NamaModel.equalsIgnoreCase("TabelModelPenerimaan")){
            Tot = TotalPerhitungan (ModelTabel, JumlahRowPesan, 6);
            JTextFieldPenerimaanTotalRill.setText( ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Tot)) );
            
            Tot = TotalPerhitungan (ModelTabel, JumlahRowPesan, 5);
            JTextFieldPenerimaanTotal1.setText( ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Tot)) );
        }
        else if (NamaModel.equalsIgnoreCase("TabelModelPb")){
            Tot = TotalPerhitungan (ModelTabel, JumlahRowPesan, 5);
            JTextFieldPbTotal.setText( ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Tot))) ;
        }
        KembalianPenerimaan();
    }
    
    double TotalPerhitungan (DefaultTableModel ModelTabel, int JumlahRowPesan, int AmbilKolomBerapa){
        double Tot = 0;
        for (int ab = 0 ; ab <= JumlahRowPesan - 1 ; ab++ ){

            if (ModelTabel.getValueAt(ab, AmbilKolomBerapa) == null){
                ModelTabel.setValueAt("0", ab, AmbilKolomBerapa);
            }        
            if (ModelTabel.getValueAt(ab, AmbilKolomBerapa).equals("")){
               ModelTabel.setValueAt("0", ab, AmbilKolomBerapa);
           }

            /*
             * Menghitung jumlah yang harus di beli
             * Dan Hapus Koma
             */
            try {
               AA = ConvertAngka.RupiahKeDoubel(String.valueOf(ModelTabel.getValueAt(ab, AmbilKolomBerapa).toString()));
               Tot = Double.valueOf(AA).doubleValue() + Tot; 
            }
            catch (Exception x){
                JOptionPane.showMessageDialog(null, "Error Input Data");
                ModelTabel.setValueAt("0", ab, AmbilKolomBerapa);
            }
            
        }
        return Tot;
    }
    
    /*
     * Ambil JDialog Pembelian
     */
     boolean CallPembelianView = true;
     private void ViewPembelianYangSudahDiBuat(){
        PembelianBahanView CallPembelianBahan = new PembelianBahanView (new javax.swing.JFrame(), true);
        CallPembelianBahan.setVisible(true);
        
        PembelianObject Dt = CallPembelianBahan.GetTableData();  
        try{
        /*
         * Difungsikan untuk saat edit agar pesanaan nama tidak hilang
         */
        BB = Dt.GetPembelianNamaResep();
        AA = Dt.GetPembelianNamaResep().replaceAll("(-1)$", "");
        
        JLabelPemberitahuanPb.setText( AA);
        
        if (Dt.GetPembelianNamaResep().equals("") || Dt.GetPembelianNamaResep().equals("null")){
            CallPembelianView = false;
            JComboBoxPbCaraPesan.setSelectedItem("Manual");
           
        }
        /*
         * -1 menandakan bahawa dri pesanan makanan
         */
        else if (Dt.GetPembelianNamaResep().contains("-1")){
            CallPembelianView = false;
            JComboBoxPbCaraPesan.setSelectedItem("Dari Pesanan");
        }
        else {
            CallPembelianView = false;
            JComboBoxPbCaraPesan.setSelectedItem("Dari Resep");
            
        }
            JTextFieldTransNo.setText(Dt.GetPembelianTransNo());
            JTextFieldPbPasar.setText(Dt.GetPembelianSupplier());
            JTextFieldPbTotal.setText(Dt.GetPembelianTotalBiaya());
            JFormattedTextFieldPbUangMuka.setText(Dt.GetPembelianUangMuka());
        
           /*
            * Logika hapus semua data di jtable
            */
        
            DefaultTableModel dtm = (DefaultTableModel) JTabelPesananBahan.getModel();
            dtm.setRowCount(0); 
            AmbilDataDatabaseSetelahGetBrowsePembelian(Dt.GetPembelianTransNo(), TabelModelPb, "detail_trans", false);   

            /*
             * Set Tanggal di kazao
             */
            this. TglBuatResep     = Dt.GetPembelianTanggalBuat();
            SistemPro.TanggalSistem TglNow = new SistemPro.TanggalSistem(); 
            Calendar Tgl = Calendar.getInstance();

            Tgl.set(TglNow.ConvertTglBlnThnToTahun(TglBuatResep), TglNow.ConvertTglBlnThnToBulan(TglBuatResep) - 1, TglNow.ConvertTglBlnThnToTanggal(TglBuatResep));
            kazaoCalendarDatePesanan.setCalendar(Tgl);  
               
        }  
        catch(Exception X){           
        }
    }
     
      private void AmbilDataDatabaseSetelahGetBrowsePembelian(String PilihData, DefaultTableModel DataModel, String TabelDatabaseName, boolean DetectHarga){
       
        /*
         * Isi data ke Tabel dari database
         */      
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();

               if (DetectHarga == true){
                   HQ = stm.executeQuery("SELECT  trans_no, no ,qty,nama_bahan, satuan, harga_satuan, harga_rill, harga_tot from "+ TabelDatabaseName+ " where trans_no = '"+ PilihData + "' order by no asc  "); 
               }
               else {
                   HQ = stm.executeQuery("SELECT  trans_no, no ,qty,nama_bahan, satuan, harga_satuan, harga_tot from "+ TabelDatabaseName+ " where trans_no = '"+ PilihData + "' order by no asc  "); 
               }
              
               baris = HQ.getRow();
               
               while(HQ.next()  ){

                   String No            = String.valueOf(HQ.getInt("no")).toString();
                   String NamaBahan     = HQ.getString("nama_bahan");
                   String Qty           = String.valueOf(HQ.getDouble("qty")).toString();
                   String Satuan        = HQ.getString("satuan");
                   String Harga_Satuan  = String.valueOf(HQ.getDouble("harga_satuan")).toString();
                   String Harga_Tot     = String.valueOf(HQ.getDouble("harga_tot")).toString();
                   if (DetectHarga == true){
                       String Harga_Rill    = String.valueOf(HQ.getDouble("harga_rill")).toString();
                       Harga_Rill           = ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Harga_Rill));
                       Harga_Tot            = ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Harga_Tot));
                       String[] add         = {No,NamaBahan,Qty,Satuan,Harga_Satuan,Harga_Tot, Harga_Rill};
                       DataModel.addRow(add); 
                   }
                   else {
                       Qty              = ConvertAngka.NilaiRupiah(Qty);
                       Harga_Satuan     = ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Harga_Satuan));
                       Harga_Tot        = ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Harga_Tot));
                       String[] add         = {No,NamaBahan,Qty,Satuan,Harga_Satuan, Harga_Tot};
                       DataModel.addRow(add);
                   }
                   
               }
                this.JTabelEdit = true;
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
       // JTabelResep.getColumnModel().getColumn(4).setCellRenderer(  new ButtonJTable("Delete"));
        //JTabelResep.getColumnModel().getColumn(4).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelOrder, JTabelResep));   

   }
    
    /*
     * Ambil JDialog resep
     */
    String GetVRNamaResep;
    String GetVRNPorsi;
    private void ViewResepYangSudahDiBuat(){
        ResepView   ResepViewSekarang = new ResepView (new javax.swing.JFrame(), true);
        ResepViewSekarang.setVisible(true);
        
        ResepObject Dt = ResepViewSekarang.GetTableData();  
        try{
            this.GetVRNamaResep     = Dt.GetVRNamaResep();
            this.GetVRNPorsi        = Dt.GetVRPorsiResep();

            AmbilDataDatabaseSetelahGetBrowseResep(Dt.GetVRNamaResep(), 0);
            SetTampilanJButton(true, false, false, true,true, false);   
            
        }  
        catch(Exception X){           
        }
    }
    
    /*
     * Pesanan makanan view
     */
    String PesananJenisBungkus = "-";
    String PesananJumlahBungkus = "0";
    private void ViewPesanan(){
        PembelianBahanViewPesanan   ViewSekarang = new PembelianBahanViewPesanan (new javax.swing.JFrame(), true);
        ViewSekarang.setVisible(true);
        
        ObjectView Dt = ViewSekarang.GetTableData();  
        try{
            /*
             * Get B = Nama Pesanan
             * Get A = TranNo Pesanan
             */
            this.GetVRNamaResep         = Dt.GetB() + "-1";
            this.PesananJenisBungkus    = Dt.GetD();
            this.PesananJumlahBungkus   = Dt.GetC();
            AmbilDataDatabasePS(Dt.GetA());
            SetTampilanJButton(true, false, false, true,true, false);          
        }  
        catch(Exception X){           
        }
    }
    
    /*
     * Pesanan makanan AmbilDatabase
     */
    String AA,BB,CC,DD, FF;
    private void AmbilDataDatabasePS(String PilihData){
       
        this.FF = PilihData;
        /*
         * Isi data ke Tabel dari database
         */      
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT trans_no, nama_menu, qty from detail_pesanan where trans_no = '"+ PilihData + "' ");              
               baris = HQ.getRow();
               
               ArrayList<String> AB = new ArrayList<String>();

               while(HQ.next() ){
                   AA   = HQ.getString("nama_menu");
                   BB   = HQ.getString("qty");
                   AB.add(AA);                
               }

               // Converting ArrayList to String in Java using advanced for-each loop
               StringBuilder BA = new StringBuilder();
               for(String str : AB){
                   BA.append(str).append("','"); //separating contents using semi colon
               }
               String ABC = "'" + BA.toString().replaceAll("(,')$","");
               AmbilDataDatabaseSetelahGetBrowseResep(ABC, 1);  

               this.JTabelEdit = true;
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
        //JTabelResep.getColumnModel().getColumn(4).setCellRenderer(  new ButtonJTable("Delete"));
        //JTabelResep.getColumnModel().getColumn(4).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelOrder, JTabelResep));   
   }
    
     private void AmbilDataDatabaseSetelahGetBrowseResep(String PilihData, int Data){
       
        /*
         * Isi data ke Tabel dari database
         * Data == 2 Input Manual
         * Data == 1 Input pesanan
         * Data == 0 Input Resep
         */      
        String HasilConvert;       
        ResultSet HQ        = null;
        ResultSet HQ2       = null;
           try {
               Statement stm    = K.createStatement();
               Statement stm2   = K.createStatement();
               if (Data == 1){
                   HQ = stm.executeQuery("SELECT DISTINCT  bahan, namresep ,sum( qty ) total_qty, satuan"
                           + " from detail_resep "
                           + " where namresep in ( "+ PilihData + " )"
                           + " GROUP BY bahan");
                   HQ2= stm2.executeQuery("Select porsi from header_pesanan where trans_no ='" + FF + "'");  
               }
               else {
                   HQ = stm.executeQuery("SELECT namresep ,bahan,qty, satuan"
                           + " from detail_resep where namresep = '"+ PilihData + "' order by no asc  ");
                   HQ2= stm2.executeQuery("Select porsi_resep from header_resep where nama_resep ='" + PilihData + "'");  
               }
                           
               String Qty   = "0";
               String Porsi = "0";
               while(HQ.next()  ){
                   String Bahan  = HQ.getString("bahan");
                   String Satuan = HQ.getString("satuan");

                   if (Data == 1){                      
                       while(HQ2.next()  ){
                           Porsi    = HQ2.getString("porsi");
                       }
                       Qty      = HQ.getString("total_qty");
                       Qty      = ( Integer.valueOf(Porsi).intValue() *  Double.valueOf(Qty).doubleValue() ) + "";
                   }
                   else if (Data == 0) {
                       while(HQ2.next()  ){
                           Porsi    = HQ2.getString("porsi_resep");
                       }
                       Qty      = HQ.getString("qty");
                   }
                   
                   /*
                    * satuan adalah satuan baku
                    * SatuanPublic adalah satuan turuan
                    */
                   
                   Qty          = ConvertAngka.NilaiRupiah(Qty);
                    System.out.println(Qty + " xxxx yyyyyyyyyyy");
                   Bahan = Bahan.replaceAll("^[\\s]", "");
                   AmbilHarga (Bahan);
                   
                   HasilConvert =  (Double.valueOf(ConvertAngka.RupiahKeDoubel(Qty)).doubleValue() * Harga  ) + "";
               
                   if (Harga != 0){
                       String[] add         = {HQ.getRow()+"",Bahan,Qty +"",Satuan,Harga + "" , ConvertAngka.NilaiRupiah(HasilConvert) };
                       TabelModelPb.addRow(add);
                   }
                   else {
                       JOptionPane.showMessageDialog(null, "Tidak ada di master barang : " + SemBahan);
                   }    
               }
                this.JTabelEdit = true;
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
       // JTabelResep.getColumnModel().getColumn(4).setCellRenderer(  new ButtonJTable("Delete"));
        //JTabelResep.getColumnModel().getColumn(4).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelOrder, JTabelResep));   
   }
     
     
     /*
      * Untuk memotong karakter dan mengambil satuan saja
      */
     private String FilterSatuan(String Satuan){
         Satuan = Satuan.replaceAll("\\s", "");
         Satuan = Satuan.replaceAll("\\(.*", "");
         Satuan = Satuan.toLowerCase();
         return Satuan;
     }
     String SemBahan;
     String ConvertHargaUntukBerat (String Bahan, String Satuan, double qty){
         /*
          * Satuan Baku adalah KG
          */
         Bahan = Bahan.replaceAll("^[\\s]", "");
        
         AmbilHarga (Bahan);
         SistemPro.DetecSatuan SatuanSelect = new SistemPro.DetecSatuan();
         Satuan = FilterSatuan (Satuan);
         JTextFieldPbHarga.setText(Harga + "");
         double a = 0; 
         String b = null;
         switch ( Satuan ) {
             case "kg" :              
                 a =  ( Double.valueOf(Harga).doubleValue() * Double.valueOf(qty).doubleValue() );
                 b = ConvertAngka.FormatDesimalRubahE9(a);
              break;
              case "hg" :              
                 a =  ( Double.valueOf(Harga).doubleValue() * Double.valueOf(qty).doubleValue() ) / 10;
                 b = ConvertAngka.FormatDesimalRubahE9(a);
              break;
              case "dag" :
                 a =  ( Double.valueOf(Harga).doubleValue() * Double.valueOf(qty).doubleValue() ) / 100;
                 b = ConvertAngka.FormatDesimalRubahE9(a);
              break;
              case "g" : 
                 a =  ( Double.valueOf(Harga).doubleValue() * Double.valueOf(qty).doubleValue() )/ 1000;
                 b = ConvertAngka.FormatDesimalRubahE9(a);
              break;
              case "dg" :
                 a =  ( Double.valueOf(Harga).doubleValue() * Double.valueOf(qty).doubleValue() ) / 10000;
                 b = ConvertAngka.FormatDesimalRubahE9(a);
              break;               
              case "kl" : 
                   a =  ( Double.valueOf(Harga).doubleValue() * Double.valueOf(qty).doubleValue() )* 1000000000;
                   b = ConvertAngka.FormatDesimalRubahE9(a);
              break;
              case "hl" : 
                  a =  ( Double.valueOf(Harga).doubleValue() * Double.valueOf(qty).doubleValue() ) * 1000000;
                  b = ConvertAngka.FormatDesimalRubahE9(a);
              break;
              case "dal" :
                  a =  ( Double.valueOf(Harga).doubleValue() * Double.valueOf(qty).doubleValue() ) * 1000;
                  b = ConvertAngka.FormatDesimalRubahE9(a);
              break;
              case "l" :
                  a =  ( Double.valueOf(Harga).doubleValue() * Double.valueOf(qty).doubleValue() );
                  b = ConvertAngka.FormatDesimalRubahE9(a);
              break;
              case "dl" :
                  a =  ( Double.valueOf(Harga).doubleValue() * Double.valueOf(qty).doubleValue() ) / 1000;
                  b = ConvertAngka.FormatDesimalRubahE9(a);
              break;
              case "cl" :
                  a =  ( Double.valueOf(Harga).doubleValue() * Double.valueOf(qty).doubleValue() ) / 1000000;
                  b = ConvertAngka.FormatDesimalRubahE9(a);
              break;
              case "ml" :

                  a =  ( Double.valueOf(Harga).doubleValue() * Double.valueOf(qty).doubleValue() ) / 1000000000; 
                  b = ConvertAngka.FormatDesimalRubahE9(a);
              break; 
              default :
                  a = ( Double.valueOf(Harga).doubleValue() * Double.valueOf(qty).doubleValue() );
                  b = ConvertAngka.FormatDesimalRubahE9(a);
              break;
                  
              /*
               * Disini bisa ditambahkan convert satuan
               */
         }
         this.SemBahan = Bahan;
        return b;           
     }

    int Harga;
    String SatuanPublic;
      private void AmbilHarga(String PilBahan){ 
          
        try {
            PilBahan = PilBahan.replaceAll(".*--", "");  
            PilBahan = PilBahan.replaceAll("^[\\s]", "");  
            }
        catch (Exception X){}
          
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();              
               HQ = stm.executeQuery("SELECT item_name , satuan, harga from master_barang where item_name = '"+ PilBahan + "' ");  
               baris = HQ.getRow();
               while(HQ.next()  ){
                   baris = 1 + baris;
                   this.Harga           = HQ.getInt("harga"); 
                   this.SatuanPublic    = HQ.getString("satuan");
                   //System.out.println( "xxxx " + SatuanPublic);
               }
               if (baris == 0){
                   this.Harga   = 0; 
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (456789)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
            JTextFieldPbHarga.setText(Harga + "" );
   }
     
     private int NoUrut;
     private int NoUrut(){
         return NoUrut;
     }
     private String TransNoPesanan(){
         
       /*
        * Ambil tanggal dari kzao kalender
        */
        String date_po = kazaoCalendarDatePesanan.getKazaoCalendar().getShortDate();
        SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();

        N.SetKazaoToTglIndo(date_po);
        N.SetKazaoToBlnIndo(date_po);
        N.SetKazaoToThnIndo(date_po);

        String DatePo = N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
         /*
          * SELECT trans_no , periode FROM header_pembelian where periode  = WhereSyarat order by  key_no desc limit 0,1
          */        
         SistemPro.TransNo TN = new SistemPro.TransNo();
         TN.SetTransNoPo("P", "trans_no", "periode", "header_pembelian", DatePo);
         this.NoUrut = TN.GetNoUrut();
         return TN.GetTransNoPo();
     }
     private void Peringatan(String Peringatan){
         JOptionPane.showMessageDialog(null, Peringatan, "Kesalahan input data", JOptionPane.INFORMATION_MESSAGE);     
     }
     
     boolean CaraInput = false;
     private boolean ValidasiSebelumDiSave(){
         
        int Tot = 0;
        int JumlahRowPesan= TabelModelPb.getRowCount();
        for (int ab = 0 ; ab <= JumlahRowPesan - 1 ; ab++ ){

            if (TabelModelPb.getValueAt(ab, 4) == null){
                Peringatan("Nilai tabel baris ke :" + ab +" kolom ke : 4 Data Null");
                return false;
            }        
            if (TabelModelPb.getValueAt(ab, 4).equals("")){
                Peringatan("Nilai tabel baris ke :" + ab +" kolom ke : 4 Data Null");
               return false;
           }
             if (TabelModelPb.getValueAt(ab, 4).equals("0")){
                 Peringatan("Nilai tabel baris ke :" + ab +" kolom ke : 4 Data Null");
               return false;
           }
        }                 
            
         if (JTextFieldPbPasar.getText().equals("")){
             Peringatan("Data pasar tidak ada");
             return false;
         }
         else if(JComboBoxPbCaraPesan.getSelectedItem().equals("...")){
             Peringatan("Salah membuat Pesanan Barang");
             return CaraInput;
         }
         else if (JTextFieldPbTotal.getText().equals("0")){
             Peringatan("Data total kosong");
             return false;
         }
         else if (JFormattedTextFieldPbUangMuka.getText().equals("")){
             Peringatan("Data uang muka kosong");
             return false;       
         }
         else{
             return true;
         }
     }
     private void DeleteDataResep(){
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from header_pembelian where trans_no = '"+ JTextFieldTransNo.getText() + "'");
    }
     
     private void TabelResepSaveDatabase(){
         if (ValidasiSebelumDiSave() == true){
            boolean SaveAtauTidak;
            SistemPro.TransNo TN    = new SistemPro.TransNo();
            String TransNoP         = TransNoPesanan();
            //System.out.println(TransNoP);
            //System.out.println(NoUrut());
            /*
             * Save Header Resep
             */
            String NoTransPesanan   = JTextFieldTransNo.getText();
            String SupplierPesanan  = JTextFieldPbPasar.getText();
            String PesananTgl       = kazaoCalendarDatePesanan.getKazaoCalendar().getShortDate();

            SistemPro.TanggalSistem KazaoToIndo = new SistemPro.TanggalSistem();
            KazaoToIndo.SetKazaoToTglIndo(PesananTgl  );
            String UserTgl          = KazaoToIndo.GetTglIndoStrKazao();
            KazaoToIndo.SetKazaoToBlnIndo(PesananTgl  );
            String UserBln          = KazaoToIndo.GetBlnIndoStrKazao();
            KazaoToIndo.SetKazaoToThnIndo(PesananTgl );
            String UserThn          = KazaoToIndo.GetThnIndoStKazao();
            String TanggalPesanan   = UserTgl+"-"+UserBln+"-"+UserThn;
            String Periode          = UserThn+UserBln;
            
            try {
                    /*
                     * Logika jika diedit
                     */
                    if (JTabelEditPenerimaan == true){
                        GetVRNamaResep = JLabelPemberitahuanPb.getText();
                        if (BB.contains("-1")){
                            GetVRNamaResep = GetVRNamaResep + "-1";
                        }
                    }
                    /*
                     * Hapus jika tidak bisa save di detail_resep
                     */
                    DeleteDataResep();

                    /*
                     * Hapus Koma
                     */
                    String  UangMuka    =   ConvertAngka.RupiahKeDoubel(JFormattedTextFieldPbUangMuka.getText());
                    String TotBiaya     =   ConvertAngka.RupiahKeDoubel(JTextFieldPbTotal.getText());
                    
                    /*
                     * Save Header
                     */                  
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    Stm.executeUpdate("INSERT INTO header_pembelian "
                            + "(key_no,trans_no,nama_resep,porsi, status_pesanan, periode,supplier, total_biaya, uang_muka, created_date, update_date) "
                            + "VALUES('"
                            +  NoUrut() + "','"+TransNoP + "','"+GetVRNamaResep 
                            + "','" + PesananJumlahBungkus + "','" + PesananJenisBungkus
                            + "','"+Periode  + "','" + JTextFieldPbPasar.getText() + "','" 
                            + TotBiaya + "','"
                            + UangMuka + "','"+TanggalPesanan + "',"
                            + " now())");     

                    /*
                     * Save DetailPo to database Mysql
                     */
                 try {
                     int a = JTabelPesananBahan.getRowCount() ;
                     Statement stm = K.createStatement();

                     // dbStatement=con.createStatement();
                     for(int i=0;i< a;i++){
                         
                         /*
                          * Ada bug ketika nominal pake pemisah  (,) 
                          * Solusi , dihilangkan dulu ketika akan di save
                          */
                         
                         int no         =Integer.valueOf(JTabelPesananBahan.getValueAt(i, 0).toString()).intValue();
                         String bahan   =JTabelPesananBahan.getValueAt(i, 1).toString();
                         String qty     =JTabelPesananBahan.getValueAt(i, 2).toString();
                         qty            = ConvertAngka.RupiahKeDoubel(qty);
                         String satuan  =JTabelPesananBahan.getValueAt(i, 3).toString();
                         
                         //Koma.SetHapusKoma(JTabelPesananBahan.getValueAt(i, 4).toString());                        
                         String harga   =JTabelPesananBahan.getValueAt(i, 4).toString();
                         harga          =ConvertAngka.RupiahKeDoubel(harga, 10);
                         String tothrg  = JTabelPesananBahan.getValueAt(i, 5).toString();

                         /*
                          * Hapus koma
                          */
                         tothrg =ConvertAngka.RupiahKeDoubel(tothrg);
                         //Koma.SetHapusKoma(tothrg);
                         harga = ConvertAngka.RupiahKeDoubel(harga);
                         System.out.println(qty + " - " + harga + " - " + tothrg);
                         stm.executeUpdate("INSERT INTO detail_trans "
                                 + "( no,trans_no, periode, nama_bahan, qty, satuan, harga_satuan, harga_tot, created_date, update_date) VALUES ('"
                                 +no+"','"+ TransNoP+ "','"+ UserBln+ "','"
                                 +bahan+"','"+qty+"','"+satuan+"','"+harga+ "','"+tothrg+"','" + TanggalPesanan+"', now())");
                         }
                     JLabelPemberitahuanPb.setText("Berhasil Di Saved");
                     SetTampilanSetelahSave();
                     JTextFieldTransNo.setText(TransNoP );
                     JButtonPbSave.setEnabled(false);
                     JPanelPesananHeader.setEnabled(false);
                     SetTampilanJButton(false, false, false, true,true, true);
                 }
                 catch (Exception X){

                     /*
                      * Hapus jika tidak bisa save di detail_resep
                      */
                      DeleteDataResep();
                      JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1226687 : "  +X, " Error", JOptionPane.ERROR_MESSAGE);
                    }           
                 }
                 catch (Exception Ex){
                    JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1224215 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(PembelianBahan.class.getName()).log(Level.SEVERE, null, Ex);
                 }              
             } 
         else{
             JLabelPemberitahuanPb.setText("Tidak berhasil di save");;
         }
    }
     
     /*
      * 
      * 
      * 
      * 
      * 
      * 
      * Bagian Penerimaan Bahan
      * All Code ada dibawah ini
      * 
      * 
      * 
      * 
      * 
      */
     private void AksiPenerimaanBahan(){
          jTabbedPane1.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent ARI) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) ARI.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                String coba = sourceTabbedPane.getTitleAt(index);
                
                if (sourceTabbedPane.getTitleAt(index).equals("Penerimaan Bahan") ){
                        
                }

                else{
                                        
                }
              }
        });
         JButtonOpenPesanan.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 ViewPembelianPesanan();
                 SetTampilanPenerimaanButtonSave(true, false, false, true, false, true, true, true, false);
                  PerhitunganTotalPesanan2( TabelModelPenerimaan, "TabelModelPenerimaan");
                  KembalianPenerimaan();
             }
         });
         
         JButtonPenerimaanSave.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 //SetTampilanPenerimaanButtonSave(false, false, false, true, true, true);
                 PenerimaanDatabaseSave();
             }
         });
         
         JButtonPenerimaanNew.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 // Sava, edit, delete, browse,  print, new
                 //SetTampilanPenerimaanButtonSave(false, false, false, true, false, true);
                 SetResetPenerimaan();
                 JButtonPenerimaanDelete.setEnabled(false);
             }
         });
         
         JButtonPenerimaanEdit.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 // Sava, edit, delete, browse,  print, new
                 SetTampilanPenerimaanButtonSave(true, false, false, true, false, true, true, true, false);
             }
         });
         
         JButtonPenerimaanDelete.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 // Sava, edit, delete, browse,  print, new
                 JOptionPane.showInputDialog("Masukan Password : ");
                 SetTampilanPenerimaanButtonSave(false, false, false, true, false, true, false, false, false);
                 DeleteDataPenerimaan();
             }
         });
         
         JButtonPenerimaanBrowse.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 ViewDataPembelianPenerimaan();
                 // Save, edit, delete, browse,  print, new
                 SetTampilanPenerimaanButtonSave(false, false, true, true, true, true, false, false, false);
             }
         });
         
         JButtonPenerimaanPrint.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 PrinterPenerimaan();
                 //Save, edit, delete, browse,  print, new
                 SetTampilanPenerimaanButtonSave(false, false, false, true, false, true, false, false, false);
             }
         });
         
    
         
     } 
     
    /*
     * Ambil JDialog Pembelian untuk penerimaan bahan
     */
     private String PenerimaanPorsi = "0", PenerimaanStatusSatuan = "-";
     private String NamaResepPenerimaan;
     private void ViewPembelianPesanan(){
        PembelianBahanView CallPembelianBahan = new PembelianBahanView (new javax.swing.JFrame(), true);
        CallPembelianBahan.setVisible(true);
        
        PembelianObject Dt = CallPembelianBahan.GetTableData();  

        try{
            
            JTextTransNoPenerimaanUtkPesanan.setText(Dt.GetPembelianTransNo());
            JTextFieldPenerimaanPasar.setText(Dt.GetPembelianSupplier());
            JTextFieldPenerimaanUangMuka.setText(Dt.GetPembelianUangMuka());
            this.PenerimaanPorsi        = Dt.GetPembelianPorsi();
            this.PenerimaanStatusSatuan = Dt.GetPembelianStatusSatuan();
            
            if (Dt.GetPembelianNamaResep().contains("-1")){
                JLabelPemberitahuanPenerimaan.setText("Jenis Po Dari Pesanan");
            }
            else if ( !Dt.GetPembelianNamaResep().equalsIgnoreCase("null")){

               JLabelPemberitahuanPenerimaan.setText("Jenis Po Dari Resep");

            }
            else {

                JLabelPemberitahuanPenerimaan.setText("Jenis Po Manual");

            }
            
            NamaResepPenerimaan = Dt.GetPembelianNamaResep();
            
           /*
            * Logika hapus semua data di jtable
            */
        
            DefaultTableModel dtm = (DefaultTableModel) JTabelPenerimaanBahan.getModel();
            dtm.setRowCount(0); 
            AmbilDataDatabaseSetelahGetBrowsePembelian(Dt.GetPembelianTransNo(), TabelModelPenerimaan, "detail_trans", false);   

            /*
             * Set Tanggal di kazao
             */
            String TglBuatResep     = Dt.GetPembelianTanggalBuat();
            SistemPro.TanggalSistem TglNow = new SistemPro.TanggalSistem(); 
            Calendar Tgl = Calendar.getInstance();

            Tgl.set(TglNow.ConvertTglBlnThnToTahun(TglBuatResep), TglNow.ConvertTglBlnThnToBulan(TglBuatResep) - 1, TglNow.ConvertTglBlnThnToTanggal(TglBuatResep));
            kazaoCalendarDatePenerimaanPO.setCalendar(Tgl);  
            
            /*
             * agar colum qty dan harga bisa di edit
             */
            this.JTabelEditPenerimaan = true;
            
        }  
        catch(Exception X){           
        }
    }
     
     /*
     * Ambil JDialog Datababase Pemebelian
     */
     private void ViewDataPembelianPenerimaan(){
        PembelianBahanPenerimaanView CallPembelianBahan = new PembelianBahanPenerimaanView (new javax.swing.JFrame(), true);
        CallPembelianBahan.setVisible(true);
        
        ObjectPenerimaanDatatabse Dt = CallPembelianBahan.GetTableData();  
            
        try{
            JTextFieldPenerimaanTotal1.setText(ConvertAngka.NilaiRupiah(Dt.GetPerimaanTotalBiaya()));
            JLabelPemberitahuanPenerimaan.setText(Dt.GetPerimaanJudulResep().replaceAll("(-1)$", ""));
            JTextTransNoPenerimaanUtkPesanan.setText(Dt.GetPerimaanNoPo());
            JTextFieldPenerimaanPasar.setText(Dt.GetPerimaanSupplier());
            JTextFieldPenerimaanUangMuka.setText(ConvertAngka.NilaiRupiah(Dt.GetPerimaanUangMuka()));
            JTextFieldPenerimaanKembalian.setText(ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Dt.GetPerimaanUangKembali())));
            JTextFieldPenerimaanNote.setText(Dt.GetPerimaanNote());
            JTextFieldPenermaanTransNo.setText(Dt.GetPerimaanTransNo());
            JTextFieldPenerimaanTotalRill.setText(Dt.GetPerimaanTotalRill());
                           
            
           /*
            * Logika hapus semua data di jtable
            */       
            DefaultTableModel dtm = (DefaultTableModel) JTabelPenerimaanBahan.getModel();
            dtm.setRowCount(0); 
            
            AmbilDataDatabaseSetelahGetBrowsePembelian(Dt.GetPerimaanTransNo(), TabelModelPenerimaan, "detail_trans_penerimaan", true);   
            
            /*
             * Set Tanggal di kazao Penerimaan WR
             */
            this.TglBuatWr     = Dt.GetPerimaanCreateDateWr();
            SistemPro.TanggalSistem TglNow = new SistemPro.TanggalSistem(); 
            Calendar Tgl = Calendar.getInstance();

            Tgl.set(TglNow.ConvertTglBlnThnToTahun(TglBuatWr  ), TglNow.ConvertTglBlnThnToBulan(TglBuatWr  ) - 1, TglNow.ConvertTglBlnThnToTanggal(TglBuatWr  ));
            kazaoCalendarDatePenerimaanWR.setCalendar(Tgl);  
            
            /*
             * Set Tanggal di kazao Penerimaan PO
             */
            String TglBuatPerimaanPo     = Dt.GetPerimaanNoPo();
            Tgl.set(TglNow.ConvertTglBlnThnToTahun(TglBuatPerimaanPo), TglNow.ConvertTglBlnThnToBulan(TglBuatPerimaanPo ) - 1, TglNow.ConvertTglBlnThnToTanggal(TglBuatPerimaanPo ));
            kazaoCalendarDatePenerimaanPO.setCalendar(Tgl); 
            
            /*
             * agar colum qty dan harga bisa di edit
             */
            //this.JTabelEditPenerimaan = true;
            
            
        }  
        catch(Exception X){           
        }
    }
      private void TabelPenerimaanBahan(){
        String header[] = {"No", "Bahan","Qty","Satuan", "Harga", "@Harga Sistem", "@Harga Pasar" ,"Action"};
        TabelModelPenerimaan = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                                //if(colIndex == 1) {return true ;} //  nilai false agar tidak bisa di edit
                                //if(colIndex == 6) {return JTabelEditPenerimaan ;}
                                if(colIndex == 6) {return JTabelEditPenerimaan ;}
                                //if(colIndex == 2) {return JTabelEditPenerimaan ;}
                                //if(colIndex == 3) {return EditKolomQty ;}
                                return false;   //Disallow the editing of any cell
                        }
        };
        JTabelPenerimaanBahan.setModel(TabelModelPenerimaan);
       
        /*
         * Rata tengah atau kanan table
         */
        JTabelPenerimaanBahan.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelPenerimaanBahan.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        JTabelPenerimaanBahan.getColumnModel().getColumn(4).setCellRenderer( tengah );
        JTabelPenerimaanBahan.getColumnModel().getColumn(5).setCellRenderer( tengah );
        JTabelPenerimaanBahan.getColumnModel().getColumn(6).setCellRenderer( tengah );
        JTabelPenerimaanBahan.getColumnModel().getColumn(7).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(3).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(7).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         */      
        int jarak_colom[] = {40,300,60,100,100, 100,100, 80};
        Colom_table ukuran_colom = new Colom_table();
        ukuran_colom.ukuran_colom(JTabelPenerimaanBahan, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTabelPenerimaanBahan.setName("Pesanan Barang");
        JTabelPenerimaanBahan.getColumnModel().getColumn(7).setCellRenderer(  new ButtonJTable("Delete"));
        JTabelPenerimaanBahan.getColumnModel().getColumn(7).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelPenerimaan, JTabelPenerimaanBahan));
       
        /*
         * Disable drag colum tabel
         */       
        JTabelPenerimaanBahan.getTableHeader().setReorderingAllowed(false);
        
        /*
         * Jika ada perubahan data pada cell jtable
         */
        TabelModelPenerimaan.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent tme) {
                PerhitunganJumlahTotalPesanan( tme,  TabelModelPenerimaan, "TabelModelPenerimaan");
            }
        });
    }
      private void  KembalianPenerimaan(){
          
           /*
            * Menghitung jumlah yang harus di beli
            * Dan Hapus Koma
            */
          double PenerimaanTotalJumlah;
          if (jTabbedPane1.getTitleAt(1).equalsIgnoreCase("Penerimaan Bahan")){
              PenerimaanTotalJumlah    = Double.valueOf(ConvertAngka.RupiahKeDoubel(JTextFieldPenerimaanTotalRill.getText())).doubleValue();
          }
          else {
              PenerimaanTotalJumlah    = Double.valueOf(ConvertAngka.RupiahKeDoubel(JTextFieldPenerimaanTotal1.getText())).doubleValue();
          }
          double PenerimaanKembalian      = Double.valueOf(ConvertAngka.RupiahKeDoubel(JTextFieldPenerimaanUangMuka.getText())).doubleValue();
   
          JTextFieldPenerimaanKembalian.setText(ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(PenerimaanKembalian - PenerimaanTotalJumlah)));
      }
      
      private void SetSetelahDiSavePenerimaan(boolean JTabel, boolean OpenPesanan, boolean Note) {
          SetTampilanPenerimaanButtonSave(false, false, false, true, false, true, false, false, true);
          JTabelPenerimaanBahan.setEnabled(JTabel);
          JButtonOpenPesanan.setEnabled(OpenPesanan);
          JTextFieldPenerimaanNote.setEnabled(Note);
      }
      
      private void SetResetPenerimaan(){
          SetSetelahDiSavePenerimaan(true, true, true);
          JTextFieldPenermaanTransNo.setText("");
          JTextFieldPenerimaanPasar.setText("");
          JTextTransNoPenerimaanUtkPesanan.setText("");
          JTextFieldPenerimaanTotal1.setText("0");
          JTextFieldPenerimaanUangMuka.setText("0");
          JTextFieldPenerimaanKembalian.setText("0");
          JTextFieldPenerimaanNote.setText("0");
          
          /*
           * Logika hapus semua data di jtable
           */
           DefaultTableModel dtm = (DefaultTableModel) JTabelPenerimaanBahan.getModel();
           dtm.setRowCount(0);
      }
      
      private void SetTampilanPenerimaanButtonSave(boolean Save, boolean Edit, boolean Delete, boolean Browse, boolean Print, boolean New, boolean Note, boolean Tabel, boolean OpenPesan){
          JButtonPenerimaanSave.setEnabled(Save);
          JButtonPenerimaanEdit.setEnabled(Edit);
          JButtonPenerimaanDelete.setEnabled(Delete);
          JButtonPenerimaanBrowse.setEnabled(Browse);
          JButtonPenerimaanPrint.setEnabled(Print);
          JButtonPenerimaanPrint1.setEnabled(Print);
          JButtonPenerimaanNew.setEnabled(New);
          JTextFieldPenerimaanNote.setEnabled(Note);
          JTabelPenerimaanBahan.setEnabled(Tabel);
          JButtonOpenPesanan.setEnabled(OpenPesan);
      }
      
      private String TransNoPenerimaan(){
         
         /*
        * Ambil tanggal dari kzao kalender
        */
        String date_wr = kazaoCalendarDatePenerimaanWR.getKazaoCalendar().getShortDate();
        SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();

        N.SetKazaoToTglIndo(date_wr);
        N.SetKazaoToBlnIndo(date_wr);
        N.SetKazaoToThnIndo(date_wr);

        String DatePo = N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
         /*
          * SELECT trans_no , periode FROM header_pembelian where periode  = WhereSyarat order by  key_no desc limit 0,1
          */        
         SistemPro.TransNo TN = new SistemPro.TransNo();
         TN.SetTransNoPo("WR", "trans_no", "periode", "header_penerimaan", DatePo);
         this.NoUrut = TN.GetNoUrut();
         return TN.GetTransNoPo();
     }
      
     boolean CaraInputPenerimaan = false;
     private boolean ValidasiSebelumDiSavePenerimaan(){
         
        int Tot = 0;
        int JumlahRowPesan= TabelModelPenerimaan.getRowCount();
        for (int ab = 0 ; ab <= JumlahRowPesan - 1 ; ab++ ){

            if (TabelModelPenerimaan.getValueAt(ab, 4) == null){
                Peringatan("Nilai tabel baris ke :" + ab +" kolom ke : 4 Data Null");
                return false;
            }        
            if (TabelModelPenerimaan.getValueAt(ab, 4).equals("")){
                Peringatan("Nilai tabel baris ke :" + ab +" kolom ke : 4 Data Null");
               return false;
           }
             if (TabelModelPenerimaan.getValueAt(ab, 4).equals("0")){
                 Peringatan("Nilai tabel baris ke :" + ab +" kolom ke : 4 Data Null");
               return false;
           }             
        }                 
            
         if (JTextTransNoPenerimaanUtkPesanan.getText().equals("")){
             Peringatan("Data Trans No tidak ada");
             return false;
         }
         
         if (JTextFieldPenerimaanTotal1.getText().equalsIgnoreCase("0")){
             Peringatan("Data total biaya kosong");
             return false;
         }   
         if (JTextFieldPenerimaanTotalRill.getText().equalsIgnoreCase("0")){
             Peringatan("Data total rill  kosong");
             return false;
         }   
 
         else{
             return true;
         }
     }
     
     private void DeleteDataPenerimaan(){
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from header_penerimaan where trans_no = '"+ JTextFieldPenermaanTransNo.getText() + "'");
    }
      private void PenerimaanDatabaseSave(){
            if (ValidasiSebelumDiSavePenerimaan() == true){
                boolean SaveAtauTidak;
                   
                SistemPro.TransNo TN    = new SistemPro.TransNo();
                String TransNoWr         =  TransNoPenerimaan();
                
                /*
                 * Save Header Penerimaan
                 */
                String NoTransPenerimaanPO  = JTextTransNoPenerimaanUtkPesanan.getText();
                String SupplierPenerimaan   = JTextFieldPenerimaanPasar.getText();
                String PerimaanTglPo        = kazaoCalendarDatePenerimaanPO.getKazaoCalendar().getShortDate();
                String PerimaanTglWr        = kazaoCalendarDatePenerimaanWR.getKazaoCalendar().getShortDate();
                String PenerimaanNote       = JTextFieldPenerimaanNote.getText();
                
                String PenerimaanTotBiaya   = ConvertAngka.RupiahKeDoubel(JTextFieldPenerimaanTotal1.getText());              
                String PenerimaanUangMuka   = ConvertAngka.RupiahKeDoubel(JTextFieldPenerimaanUangMuka.getText());       
                String PenerimaanKembali    = ConvertAngka.RupiahKeDoubel(JTextFieldPenerimaanKembalian.getText());    
                String PenerimaanTotRill    = ConvertAngka.RupiahKeDoubel(JTextFieldPenerimaanTotalRill.getText());
                /*
                 * Penerumaan Tanggal PO
                 */               
                SistemPro.TanggalSistem KazaoToIndo = new SistemPro.TanggalSistem();
                KazaoToIndo.SetKazaoToTglIndo(PerimaanTglPo  );
                String UserTgl  = KazaoToIndo.GetTglIndoStrKazao();
                KazaoToIndo.SetKazaoToBlnIndo(PerimaanTglPo  );
                String UserBln  = KazaoToIndo.GetBlnIndoStrKazao();
                KazaoToIndo.SetKazaoToThnIndo(PerimaanTglPo );
                String UserThn = KazaoToIndo.GetThnIndoStKazao();
                String TanggalPesanan = UserTgl+"-"+UserBln+"-"+UserThn;
                //String Periode          = UserThn+UserBln;
                
                /*
                 * Penerimaan Tanggal PO WR
                 */
                KazaoToIndo.SetKazaoToTglIndo(PerimaanTglWr  );
                String UserTglWR  = KazaoToIndo.GetTglIndoStrKazao();
                KazaoToIndo.SetKazaoToBlnIndo(PerimaanTglWr  );
                String UserBlnWR  = KazaoToIndo.GetBlnIndoStrKazao();
                KazaoToIndo.SetKazaoToThnIndo(PerimaanTglWr );
                String UserThnWR = KazaoToIndo.GetThnIndoStKazao();
                String TanggalPesananWR     = UserTglWR+"-"+UserBlnWR+"-"+UserThnWR;
                String PeriodeWR            = UserThnWR+UserBlnWR;
                
                try {
                        /*
                         * Hapus jika tidak bisa save di detail_resep
                         */
                       DeleteDataPenerimaan();
                       
                        /*
                         * Save Header
                         */
                        Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        Stm.executeUpdate("INSERT INTO header_penerimaan "
                                + "(key_no, trans_no,  judul_resep,porsi, status_pesanan, periode, supplier, note, no_po, tanggal_po, total_biaya, uang_muka, uang_kembali, created_date, update_date, total_rill )VALUES ('"
                                + NoUrut() 
                                + "','"+TransNoWr +"','"+ NamaResepPenerimaan
                                + "','" + PenerimaanPorsi + "','" + PenerimaanStatusSatuan 
                                + "','"+PeriodeWR    + "','" + SupplierPenerimaan + "','" 
                                + PenerimaanNote + "','"+NoTransPenerimaanPO + "','"+ TanggalPesanan  +"','"                               
                                + PenerimaanTotBiaya + "','" 
                                + PenerimaanUangMuka + "','"+ PenerimaanKembali+ "','"+TanggalPesananWR + "',"
                                + " now(), '" +PenerimaanTotRill + "')");     

                        /*
                                + PenerimaanUangMuka + "','"+ P
                         * Save DetailPo to database Mysql
                         */
                     try {
                         int a = JTabelPenerimaanBahan.getRowCount() ;
                         Statement stm = K.createStatement();

                         // dbStatement=con.createStatement();
                         for(int i=0;i< a;i++){

                             int no         =Integer.valueOf(JTabelPenerimaanBahan.getValueAt(i, 0).toString()).intValue();
                             String bahan   =JTabelPenerimaanBahan.getValueAt(i, 1).toString();
                             String qty     =JTabelPenerimaanBahan.getValueAt(i, 2).toString();
                             String satuan  =JTabelPenerimaanBahan.getValueAt(i, 3).toString();
                             String harga   =JTabelPenerimaanBahan.getValueAt(i, 4).toString();
                             String tothrg  =JTabelPenerimaanBahan.getValueAt(i, 5).toString();
                             String totrill =JTabelPenerimaanBahan.getValueAt(i, 6).toString();

                             /*
                              * Hapus koma
                              */
                             qty     = ConvertAngka.RupiahKeDoubel(qty);
                             harga   = ConvertAngka.RupiahKeDoubel(harga);
                             tothrg  = ConvertAngka.RupiahKeDoubel(tothrg);
                             totrill = ConvertAngka.RupiahKeDoubel(totrill);
                             
                             stm.executeUpdate("INSERT INTO detail_trans_penerimaan "
                                     + "( no, trans_no, periode, nama_bahan, qty, satuan, harga_satuan, harga_tot, created_date, updated_date, harga_rill) VALUES ('"
                                     +no+"','"+ TransNoWr+ "','"+ PeriodeWR+ "','"
                                     +bahan+"','"+qty+"','"+satuan+"','"+harga+ "','"
                                     +tothrg+"','" + TanggalPesananWR+"', now(), '" +totrill + "')");    
                             }
                         JLabelPemberitahuanPenerimaan.setText("Berhasil Di Saved");
                         SetSetelahDiSavePenerimaan(false  , false, false);
                         JTextFieldPenermaanTransNo.setText(TransNoWr );
                         SetTampilanPenerimaanButtonSave(false, false, false, true, true, true, false, false, false);
                         //JButtonPbSave.setEnabled(false);
                         //JPanelPesananHeader.setEnabled(false);
                         //SetTampilanJButton(false, false, false, true,true, true);
                     }
                     catch (Exception X){

                         /*
                          * Hapus jika tidak bisa save di detail_resep
                          */
                          DeleteDataPenerimaan();
                          JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1226 : "  +X, " Error", JOptionPane.ERROR_MESSAGE);
                        }           
                     }
                     catch (Exception Ex){
                        JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1224 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
                    }              
                 } 
             else{
                 JLabelPemberitahuanPb.setText("Tidak berhasil di save");;
             }
      }
      
      private void PrinterPenerimaan(){
         PembelianBahanPenerimanPrintView penerimaanPrint = new  PembelianBahanPenerimanPrintView( JTextFieldPenermaanTransNo.getText(), TglBuatWr);
      }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jInternalFrame2 = new javax.swing.JInternalFrame();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelPesananBahan = new javax.swing.JTable();
        JPanelInputPbBahan = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        JTextFieldPbQty = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        JComboBoxSatuan = new javax.swing.JComboBox();
        JButtonAddPb = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        JTextFieldPbHarga = new javax.swing.JTextField();
        JTextPbBahan = new javax.swing.JComboBox();
        JPanelButtonSave = new javax.swing.JPanel();
        JButtonPbSave = new javax.swing.JButton();
        JButtonPbEdit = new javax.swing.JButton();
        JButtonPbDelete = new javax.swing.JButton();
        JButtonPbBrowse = new javax.swing.JButton();
        JButtonPbNew = new javax.swing.JButton();
        JButtonPbPrint = new javax.swing.JButton();
        JButtonPesananPrint2 = new javax.swing.JButton();
        JPanelPesananHeader = new javax.swing.JPanel();
        JPanelHeader2 = new javax.swing.JPanel();
        JTextFieldTransNo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        JTextFieldPbPasar = new javax.swing.JTextField();
        kazaoCalendarDatePesanan = new org.kazao.calendar.KazaoCalendarDate();
        jLabel15 = new javax.swing.JLabel();
        JComboBoxPbCaraPesan = new javax.swing.JComboBox();
        JLabelPemberitahuanPb = new javax.swing.JLabel();
        JPanelHeaderTotal = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        JTextFieldPbTotal = new javax.swing.JTextField();
        JFormattedTextFieldPbUangMuka = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTabelPenerimaanBahan = new javax.swing.JTable();
        JPanelPesananHeader1 = new javax.swing.JPanel();
        JPanelHeader3 = new javax.swing.JPanel();
        JTextFieldPenermaanTransNo = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        JTextFieldPenerimaanPasar = new javax.swing.JTextField();
        kazaoCalendarDatePenerimaanPO = new org.kazao.calendar.KazaoCalendarDate();
        jLabel22 = new javax.swing.JLabel();
        JLabelPemberitahuanPenerimaan = new javax.swing.JLabel();
        JTextTransNoPenerimaanUtkPesanan = new javax.swing.JTextField();
        JButtonOpenPesanan = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        kazaoCalendarDatePenerimaanWR = new org.kazao.calendar.KazaoCalendarDate();
        label2 = new java.awt.Label();
        jLabel29 = new javax.swing.JLabel();
        JTextFieldPenerimaanNote = new javax.swing.JTextField();
        JPanelHeaderTotal1 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        JTextFieldPenerimaanTotal1 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        JTextFieldPenerimaanUangMuka = new javax.swing.JTextField();
        JTextFieldPenerimaanKembalian = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        JTextFieldPenerimaanTotalRill = new javax.swing.JTextField();
        JPanelButtonSave1 = new javax.swing.JPanel();
        JButtonPenerimaanSave = new javax.swing.JButton();
        JButtonPenerimaanEdit = new javax.swing.JButton();
        JButtonPenerimaanDelete = new javax.swing.JButton();
        JButtonPenerimaanBrowse = new javax.swing.JButton();
        JButtonPenerimaanNew = new javax.swing.JButton();
        JButtonPenerimaanPrint = new javax.swing.JButton();
        JButtonPenerimaanPrint1 = new javax.swing.JButton();

        jButton2.setText("jButton2");

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jInternalFrame2.setVisible(true);

        javax.swing.GroupLayout jInternalFrame2Layout = new javax.swing.GroupLayout(jInternalFrame2.getContentPane());
        jInternalFrame2.getContentPane().setLayout(jInternalFrame2Layout);
        jInternalFrame2Layout.setHorizontalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame2Layout.setVerticalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Pembelian Bahan");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/logo.png"))); // NOI18N

        jScrollPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTabelPesananBahan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(JTabelPesananBahan);

        JPanelInputPbBahan.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText(" Bahan : ");

        jLabel2.setText("Qty : ");

        jLabel3.setText("Satuan :");

        JComboBoxSatuan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kg ( Kilogram )", "g ( Gram )", "Sdm ( Sendok Makan )", "Sdt ( Sendok Teh)", "L ( Litter )", "Ml ( Mili Liter )", "Bungkus", "Biji", "Buah", "Ruas", "Kemasan", "Ikat", "Batang", "Lembar" }));

        JButtonAddPb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Apply.png"))); // NOI18N
        JButtonAddPb.setText("Add");

        jLabel14.setText("Harga : ");

        JTextFieldPbHarga.setEditable(false);

        JTextPbBahan.setEditable(true);
        JTextPbBahan.setModel(new javax.swing.DefaultComboBoxModel( app_search_data_resep.getData().toArray()));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextPbBahan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldPbHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldPbQty, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JComboBoxSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonAddPb, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(JTextFieldPbQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(JComboBoxSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonAddPb)
                    .addComponent(jLabel14)
                    .addComponent(JTextFieldPbHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextPbBahan))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout JPanelInputPbBahanLayout = new javax.swing.GroupLayout(JPanelInputPbBahan);
        JPanelInputPbBahan.setLayout(JPanelInputPbBahanLayout);
        JPanelInputPbBahanLayout.setHorizontalGroup(
            JPanelInputPbBahanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelInputPbBahanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        JPanelInputPbBahanLayout.setVerticalGroup(
            JPanelInputPbBahanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        JPanelButtonSave.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JButtonPbSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Save.png"))); // NOI18N
        JButtonPbSave.setText("Save");
        JButtonPbSave.setEnabled(false);

        JButtonPbEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Update.png"))); // NOI18N
        JButtonPbEdit.setText("Edit");
        JButtonPbEdit.setEnabled(false);

        JButtonPbDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Delete.png"))); // NOI18N
        JButtonPbDelete.setText("Delete");
        JButtonPbDelete.setEnabled(false);

        JButtonPbBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Search.png"))); // NOI18N
        JButtonPbBrowse.setText("Browse");

        JButtonPbNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonPbNew.setText("New");

        JButtonPbPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPbPrint.setText("Print");
        JButtonPbPrint.setEnabled(false);

        JButtonPesananPrint2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPesananPrint2.setText("Print View");

        javax.swing.GroupLayout JPanelButtonSaveLayout = new javax.swing.GroupLayout(JPanelButtonSave);
        JPanelButtonSave.setLayout(JPanelButtonSaveLayout);
        JPanelButtonSaveLayout.setHorizontalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelButtonSaveLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JButtonPbSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbBrowse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbPrint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPesananPrint2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPanelButtonSaveLayout.setVerticalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSaveLayout.createSequentialGroup()
                .addGroup(JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JButtonPbSave)
                    .addComponent(JButtonPbEdit)
                    .addComponent(JButtonPbDelete)
                    .addComponent(JButtonPbBrowse)
                    .addComponent(JButtonPbNew)
                    .addComponent(JButtonPbPrint)
                    .addComponent(JButtonPesananPrint2))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        JPanelPesananHeader.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTextFieldTransNo.setEditable(false);
        JTextFieldTransNo.setBackground(new java.awt.Color(204, 204, 204));

        jLabel4.setText("No Transaksi");

        jLabel5.setText("Supplier / Pasar ");

        jLabel6.setText("Tanggal");

        jLabel7.setText(":");

        jLabel8.setText(":");

        jLabel9.setText(":");

        jLabel15.setText("Pilih cara input pemesan :");

        JComboBoxPbCaraPesan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "...", "Manual", "Dari Resep", "Dari Pesanan" }));

        JLabelPemberitahuanPb.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout JPanelHeader2Layout = new javax.swing.GroupLayout(JPanelHeader2);
        JPanelHeader2.setLayout(JPanelHeader2Layout);
        JPanelHeader2Layout.setHorizontalGroup(
            JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeader2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader2Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kazaoCalendarDatePesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JComboBoxPbCaraPesan, 0, 134, Short.MAX_VALUE))
                    .addGroup(JPanelHeader2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLabelPemberitahuanPb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPanelHeader2Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPbPasar)))
                .addContainerGap())
        );
        JPanelHeader2Layout.setVerticalGroup(
            JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeader2Layout.createSequentialGroup()
                .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JLabelPemberitahuanPb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(JTextFieldTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addComponent(JTextFieldPbPasar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jLabel9))
                    .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(kazaoCalendarDatePesanan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(JComboBoxPbCaraPesan, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        JPanelHeaderTotal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel10.setText("Total Biaya");

        jLabel11.setText("Uang Muka");

        jLabel12.setText(":");

        jLabel13.setText(":");

        JTextFieldPbTotal.setEditable(false);
        JTextFieldPbTotal.setBackground(new java.awt.Color(204, 204, 204));
        JTextFieldPbTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextFieldPbTotal.setText("0");

        JFormattedTextFieldPbUangMuka.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        JFormattedTextFieldPbUangMuka.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout JPanelHeaderTotalLayout = new javax.swing.GroupLayout(JPanelHeaderTotal);
        JPanelHeaderTotal.setLayout(JPanelHeaderTotalLayout);
        JPanelHeaderTotalLayout.setHorizontalGroup(
            JPanelHeaderTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeaderTotalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelHeaderTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeaderTotalLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JFormattedTextFieldPbUangMuka))
                    .addGroup(JPanelHeaderTotalLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPbTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)))
                .addContainerGap())
        );
        JPanelHeaderTotalLayout.setVerticalGroup(
            JPanelHeaderTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeaderTotalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelHeaderTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(JTextFieldPbTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeaderTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13)
                    .addComponent(JFormattedTextFieldPbUangMuka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout JPanelPesananHeaderLayout = new javax.swing.GroupLayout(JPanelPesananHeader);
        JPanelPesananHeader.setLayout(JPanelPesananHeaderLayout);
        JPanelPesananHeaderLayout.setHorizontalGroup(
            JPanelPesananHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelPesananHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPanelHeader2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelHeaderTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        JPanelPesananHeaderLayout.setVerticalGroup(
            JPanelPesananHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelPesananHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelPesananHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JPanelHeader2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelHeaderTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(JPanelButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JPanelPesananHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelInputPbBahan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelInputPbBahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelPesananHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JPanelButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Pemesanan Bahan", jPanel1);

        jScrollPane2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTabelPenerimaanBahan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTabelPenerimaanBahan.setName("Penerimaan"); // NOI18N
        jScrollPane2.setViewportView(JTabelPenerimaanBahan);

        JPanelPesananHeader1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTextFieldPenermaanTransNo.setEditable(false);
        JTextFieldPenermaanTransNo.setBackground(new java.awt.Color(204, 204, 204));

        jLabel16.setText("No Transaksi");

        jLabel17.setText("Supplier / Pasar ");

        jLabel18.setText("Tanggal PO");

        jLabel19.setText(":");

        jLabel20.setText(":");

        jLabel21.setText(":");

        JTextFieldPenerimaanPasar.setEditable(false);
        JTextFieldPenerimaanPasar.setBackground(new java.awt.Color(204, 204, 204));

        kazaoCalendarDatePenerimaanPO.setEditable(false);

        jLabel22.setText("No PO :");

        JLabelPemberitahuanPenerimaan.setForeground(new java.awt.Color(255, 0, 0));

        JTextTransNoPenerimaanUtkPesanan.setEditable(false);
        JTextTransNoPenerimaanUtkPesanan.setBackground(new java.awt.Color(204, 204, 204));

        JButtonOpenPesanan.setText("Open PO");

        jLabel27.setText("Tanggal WR");

        jLabel28.setText(":");

        kazaoCalendarDatePenerimaanWR.setEditable(false);

        label2.setText("Note");

        jLabel29.setText(":");

        javax.swing.GroupLayout JPanelHeader3Layout = new javax.swing.GroupLayout(JPanelHeader3);
        JPanelHeader3.setLayout(JPanelHeader3Layout);
        JPanelHeader3Layout.setHorizontalGroup(
            JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeader3Layout.createSequentialGroup()
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kazaoCalendarDatePenerimaanPO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextTransNoPenerimaanUtkPesanan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonOpenPesanan))
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(kazaoCalendarDatePenerimaanWR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(JTextFieldPenerimaanNote))
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPanelHeader3Layout.createSequentialGroup()
                                .addComponent(JTextFieldPenermaanTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JLabelPemberitahuanPenerimaan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(JTextFieldPenerimaanPasar))))
                .addContainerGap())
        );
        JPanelHeader3Layout.setVerticalGroup(
            JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeader3Layout.createSequentialGroup()
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JLabelPemberitahuanPenerimaan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(JTextFieldPenermaanTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19)))
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel20)))
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPenerimaanPasar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(jLabel21))
                    .addComponent(kazaoCalendarDatePenerimaanPO, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel22)
                        .addComponent(JTextTransNoPenerimaanUtkPesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JButtonOpenPesanan)))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelHeader3Layout.createSequentialGroup()
                        .addComponent(kazaoCalendarDatePenerimaanWR, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel29)
                        .addComponent(JTextFieldPenerimaanNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        JPanelHeaderTotal1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel23.setText("Total Sist");

        jLabel24.setText("Uang Muka");

        jLabel25.setText(":");

        jLabel26.setText(":");

        JTextFieldPenerimaanTotal1.setEditable(false);
        JTextFieldPenerimaanTotal1.setBackground(new java.awt.Color(204, 204, 204));
        JTextFieldPenerimaanTotal1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextFieldPenerimaanTotal1.setText("0");

        jLabel30.setText("Kembalian");

        jLabel31.setText(":");

        JTextFieldPenerimaanUangMuka.setEditable(false);
        JTextFieldPenerimaanUangMuka.setBackground(new java.awt.Color(204, 204, 204));
        JTextFieldPenerimaanUangMuka.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextFieldPenerimaanUangMuka.setText("0");

        JTextFieldPenerimaanKembalian.setEditable(false);
        JTextFieldPenerimaanKembalian.setBackground(new java.awt.Color(204, 204, 204));
        JTextFieldPenerimaanKembalian.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextFieldPenerimaanKembalian.setText("0");
        JTextFieldPenerimaanKembalian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTextFieldPenerimaanKembalianActionPerformed(evt);
            }
        });

        jLabel32.setText("Total Rill");

        jLabel33.setText(":");

        JTextFieldPenerimaanTotalRill.setEditable(false);
        JTextFieldPenerimaanTotalRill.setBackground(new java.awt.Color(204, 204, 204));
        JTextFieldPenerimaanTotalRill.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextFieldPenerimaanTotalRill.setText("0");

        javax.swing.GroupLayout JPanelHeaderTotal1Layout = new javax.swing.GroupLayout(JPanelHeaderTotal1);
        JPanelHeaderTotal1.setLayout(JPanelHeaderTotal1Layout);
        JPanelHeaderTotal1Layout.setHorizontalGroup(
            JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPenerimaanUangMuka, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                    .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPenerimaanKembalian, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                    .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JTextFieldPenerimaanTotalRill)
                            .addComponent(JTextFieldPenerimaanTotal1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))))
                .addContainerGap())
        );
        JPanelHeaderTotal1Layout.setVerticalGroup(
            JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel25)
                    .addComponent(JTextFieldPenerimaanTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(JTextFieldPenerimaanTotalRill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel26)
                    .addComponent(JTextFieldPenerimaanUangMuka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31)
                    .addComponent(JTextFieldPenerimaanKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout JPanelPesananHeader1Layout = new javax.swing.GroupLayout(JPanelPesananHeader1);
        JPanelPesananHeader1.setLayout(JPanelPesananHeader1Layout);
        JPanelPesananHeader1Layout.setHorizontalGroup(
            JPanelPesananHeader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelPesananHeader1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPanelHeader3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelHeaderTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        JPanelPesananHeader1Layout.setVerticalGroup(
            JPanelPesananHeader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelPesananHeader1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JPanelPesananHeader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JPanelHeaderTotal1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelHeader3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        JPanelButtonSave1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JButtonPenerimaanSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Save.png"))); // NOI18N
        JButtonPenerimaanSave.setText("Save");
        JButtonPenerimaanSave.setEnabled(false);

        JButtonPenerimaanEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Update.png"))); // NOI18N
        JButtonPenerimaanEdit.setText("Edit");
        JButtonPenerimaanEdit.setEnabled(false);

        JButtonPenerimaanDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Delete.png"))); // NOI18N
        JButtonPenerimaanDelete.setText("Delete");
        JButtonPenerimaanDelete.setEnabled(false);

        JButtonPenerimaanBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Search.png"))); // NOI18N
        JButtonPenerimaanBrowse.setText("Browse");

        JButtonPenerimaanNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonPenerimaanNew.setText("New");

        JButtonPenerimaanPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPenerimaanPrint.setText("Print");
        JButtonPenerimaanPrint.setEnabled(false);

        JButtonPenerimaanPrint1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPenerimaanPrint1.setText("Print View");
        JButtonPenerimaanPrint1.setEnabled(false);

        javax.swing.GroupLayout JPanelButtonSave1Layout = new javax.swing.GroupLayout(JPanelButtonSave1);
        JPanelButtonSave1.setLayout(JPanelButtonSave1Layout);
        JPanelButtonSave1Layout.setHorizontalGroup(
            JPanelButtonSave1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelButtonSave1Layout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addComponent(JButtonPenerimaanSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPenerimaanEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPenerimaanDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPenerimaanBrowse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPenerimaanNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPenerimaanPrint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPenerimaanPrint1)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        JPanelButtonSave1Layout.setVerticalGroup(
            JPanelButtonSave1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSave1Layout.createSequentialGroup()
                .addGroup(JPanelButtonSave1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JButtonPenerimaanSave)
                    .addComponent(JButtonPenerimaanEdit)
                    .addComponent(JButtonPenerimaanDelete)
                    .addComponent(JButtonPenerimaanBrowse)
                    .addComponent(JButtonPenerimaanNew)
                    .addComponent(JButtonPenerimaanPrint)
                    .addComponent(JButtonPenerimaanPrint1))
                .addGap(0, 22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
            .addComponent(JPanelButtonSave1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPanelPesananHeader1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelPesananHeader1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelButtonSave1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Penerimaan Bahan", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-800)/2, (screenSize.height-554)/2, 800, 554);
    }// </editor-fold>//GEN-END:initComponents

    private void JTextFieldPenerimaanKembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTextFieldPenerimaanKembalianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTextFieldPenerimaanKembalianActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonAddPb;
    private javax.swing.JButton JButtonOpenPesanan;
    private javax.swing.JButton JButtonPbBrowse;
    private javax.swing.JButton JButtonPbDelete;
    private javax.swing.JButton JButtonPbEdit;
    private javax.swing.JButton JButtonPbNew;
    private javax.swing.JButton JButtonPbPrint;
    private javax.swing.JButton JButtonPbSave;
    private javax.swing.JButton JButtonPenerimaanBrowse;
    private javax.swing.JButton JButtonPenerimaanDelete;
    private javax.swing.JButton JButtonPenerimaanEdit;
    private javax.swing.JButton JButtonPenerimaanNew;
    private javax.swing.JButton JButtonPenerimaanPrint;
    private javax.swing.JButton JButtonPenerimaanPrint1;
    private javax.swing.JButton JButtonPenerimaanSave;
    private javax.swing.JButton JButtonPesananPrint2;
    private javax.swing.JComboBox JComboBoxPbCaraPesan;
    private javax.swing.JComboBox JComboBoxSatuan;
    private javax.swing.JFormattedTextField JFormattedTextFieldPbUangMuka;
    private javax.swing.JLabel JLabelPemberitahuanPb;
    private javax.swing.JLabel JLabelPemberitahuanPenerimaan;
    private javax.swing.JPanel JPanelButtonSave;
    private javax.swing.JPanel JPanelButtonSave1;
    private javax.swing.JPanel JPanelHeader2;
    private javax.swing.JPanel JPanelHeader3;
    private javax.swing.JPanel JPanelHeaderTotal;
    private javax.swing.JPanel JPanelHeaderTotal1;
    private javax.swing.JPanel JPanelInputPbBahan;
    private javax.swing.JPanel JPanelPesananHeader;
    private javax.swing.JPanel JPanelPesananHeader1;
    private javax.swing.JTable JTabelPenerimaanBahan;
    private javax.swing.JTable JTabelPesananBahan;
    private javax.swing.JTextField JTextFieldPbHarga;
    private javax.swing.JTextField JTextFieldPbPasar;
    private javax.swing.JTextField JTextFieldPbQty;
    private javax.swing.JTextField JTextFieldPbTotal;
    private javax.swing.JTextField JTextFieldPenerimaanKembalian;
    private javax.swing.JTextField JTextFieldPenerimaanNote;
    private javax.swing.JTextField JTextFieldPenerimaanPasar;
    private javax.swing.JTextField JTextFieldPenerimaanTotal1;
    private javax.swing.JTextField JTextFieldPenerimaanTotalRill;
    private javax.swing.JTextField JTextFieldPenerimaanUangMuka;
    private javax.swing.JTextField JTextFieldPenermaanTransNo;
    private javax.swing.JTextField JTextFieldTransNo;
    private javax.swing.JComboBox JTextPbBahan;
    private javax.swing.JTextField JTextTransNoPenerimaanUtkPesanan;
    private javax.swing.JButton jButton2;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JInternalFrame jInternalFrame2;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDatePenerimaanPO;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDatePenerimaanWR;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDatePesanan;
    private java.awt.Label label2;
    // End of variables declaration//GEN-END:variables
}
