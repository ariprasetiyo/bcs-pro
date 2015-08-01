/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * JTextFieldResepBahan
 * app_search_data_resep
 * JTextFieldResepBahan.setModel(new javax.swing.DefaultComboBoxModel( app_search_data_resep.getData().toArray()));
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.ButtonJTableKeDua;
import SistemPro.Colom_table;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.ResepObject;
import SistemPro.ValidasiInputResep;
import SistemPro.app_search1;
import SistemPro.app_search_data_resep;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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
public class Resep extends javax.swing.JInternalFrame {
    
    private DefaultTableModel TabelModelOrder;
    
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah = new RenderingTengah();
    SistemPro.DetecSatuan SatuanSelect = new SistemPro.DetecSatuan();
    
    SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    Connection K = KD.createConnection();
    String NamaResep2, AA, BB, CC,DD,EE;
    SistemPro.KomaToString ConvertAngka = new SistemPro.KomaToString();

    /**
     * Creates new form Resep
     */
    public Resep() {      
        initComponents();
        SetTampilanComponent();
        JPanelInputBahan.setVisible(false);
        AksiResep();
        TabelResep();
        SetResetTampilanResep();
        //Tampilan();
    }
    
    //private void Tampilan(){
    //add(JComboBoxResepSatuan, 1, 0);
    //JComboBoxResepSatuan
    //JPanelAddResep.setOpaque(false);
    //LabelTanggal.repaint();
    //}
    
    HashMap DatahashMap;    
    String Satuan = "";
    private void AksiResep(){
        
        JTextFieldResepBahan.setSelectedIndex(-1);
        final JTextField JTextFieldItem = (JTextField)JTextFieldResepBahan.getEditor().getEditorComponent();
        JTextFieldItem.setText("");
        JTextFieldItem.addKeyListener(new app_search1(JTextFieldResepBahan));

        JTextFieldResepBahan.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent a){
                
                  /*
                   * Deteksi apakah item tersebut ada di master barang convert
                   * Jika Iya => Mka satuan bisa di convert
                   * Jika tdk => tdk bisa di konvert
                   * Saat pemeilihan barang, harus mendeteksi satuan tidak baku, satuan baku dan satuan pada harga, KG atau ML
                   */
                 AA = (String )JTextFieldResepBahan.getSelectedItem();
                 boolean Data = SatuanSelect.DetectConvert(AA, K); 
                 if ( Data == true){
                     
                     /*
                      * Buat deteksi sataun baku pada convert satuan
                      */
                     Satuan = (String)SatuanSelect.hashMap.get("satuan baku");
                     
                     DatahashMap = SatuanSelect.hashMap;
                     SatuanSelect.IsiSatuanPadaJComboBox(JComboBoxResepSatuan, SatuanSelect.hashMap ); 
                     
                 }
                 else {
                     AmbilHarga(AA);
                     SatuanSelect.IsiSatuanPadaJComboBox(JComboBoxResepSatuan, SatuanPublic);
                     Satuan = SatuanPublic;
                 }           
            }
        });
        //ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(HargaPerporsi ))
        /*
         * Aksi combobox satuan
         */
        //SatuanSelect.AksiAksi(JComboBoxResepSatuan);
        JComboBoxResepSatuan.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                try{
                    AA = (String ) JComboBoxResepSatuan.getSelectedItem(); 
                    int ab = JComboBoxResepSatuan.getSelectedIndex();

                    if (!Satuan.equalsIgnoreCase(AA)){
                        if (AA.equalsIgnoreCase("KG")){
                            
                            /*
                             *Kg ke G
                             */
                            JTextFieldResepQtyTidakBaku.setText(JTextFieldResepQty.getText() + "");
                            JComboBoxResepSatuanTidakBaku.setSelectedItem(AA);
                            
                            double data =  Double.valueOf(ConvertAngka.RupiahKeDoubel(JTextFieldResepQty.getText())).doubleValue() * 1000;
                            JTextFieldResepQty.setText(ConvertAngka.NilaiRupiah((double) Math.round(data * 100) / 100) + "");
                            JComboBoxResepSatuan.setSelectedItem(Satuan); 
                            JComboBoxResepSatuan1Porsi.setSelectedItem(Satuan);
                            //JOptionPane.showMessageDialog(null, "KG ke G");
                            
                        }
                        else if (AA.equalsIgnoreCase("G")){
                            /*
                             *G ke KG
                             */
                            JTextFieldResepQtyTidakBaku.setText(JTextFieldResepQty.getText() + "");
                            JComboBoxResepSatuanTidakBaku.setSelectedItem(AA); 
                            
                            double data =  Double.valueOf(ConvertAngka.RupiahKeDoubel(JTextFieldResepQty.getText())) / 1000 ;
                            JTextFieldResepQty.setText(ConvertAngka.NilaiRupiah((double) Math.round(data * 100) / 100) + "");
                            JComboBoxResepSatuan.setSelectedItem(Satuan);   
                            JComboBoxResepSatuan1Porsi.setSelectedItem(Satuan);
                            
                            //JOptionPane.showMessageDialog(null, "G ke KG");
                        
                        }
                        else if (AA.equalsIgnoreCase("L")){
                            
                            /*
                             *L ke ML
                             */
                            JTextFieldResepQtyTidakBaku.setText(JTextFieldResepQty.getText() + "");
                            JComboBoxResepSatuanTidakBaku.setSelectedItem(AA); 
                            
                            double data =  Double.valueOf(ConvertAngka.RupiahKeDoubel(JTextFieldResepQty.getText())).doubleValue() * 1000;
                            JTextFieldResepQty.setText(ConvertAngka.NilaiRupiah((double) Math.round(data * 100.0)/100.0 ) + "");
                            JComboBoxResepSatuan.setSelectedItem(Satuan); 
                            JComboBoxResepSatuan1Porsi.setSelectedItem(Satuan);
                            //JOptionPane.showMessageDialog(null, "L ke ML"); 
                        
                        }
                        else if (AA.equalsIgnoreCase("ML")){
                           
                            /*
                             *ML ke L
                             */
                            JTextFieldResepQtyTidakBaku.setText(JTextFieldResepQty.getText() + "");
                            JComboBoxResepSatuanTidakBaku.setSelectedItem(AA); 
                            
                            double data =  Double.valueOf(ConvertAngka.RupiahKeDoubel(JTextFieldResepQty.getText())).doubleValue() / 1000;
                            JTextFieldResepQty.setText(ConvertAngka.NilaiRupiah((double) Math.round(data * 100) / 100) + "");
                            JComboBoxResepSatuan.setSelectedItem(Satuan); 
                            JComboBoxResepSatuan1Porsi.setSelectedItem(Satuan);
                            //JOptionPane.showMessageDialog(null, "ML ke L");
                        }
                        else {
                            JTextFieldResepQtyTidakBaku.setText(JTextFieldResepQty.getText() + "");
                            JComboBoxResepSatuanTidakBaku.setSelectedItem(AA); 
                            
                            JTextFieldResepQty.setText(JTextFieldResepQty.getText());
                            
                            JComboBoxResepSatuan1Porsi.setSelectedItem(Satuan);
                            JComboBoxResepSatuan.setSelectedItem(Satuan);
                            //System.out.println(Satuan + " tidak ada perubahan " + AA);
                        } 
                      
                    }
                    else {
                        JTextFieldResepQtyTidakBaku.setText(JTextFieldResepQty.getText());
                        JComboBoxResepSatuanTidakBaku.setSelectedItem(AA); 
                        
                        JComboBoxResepSatuan1Porsi.setSelectedItem(Satuan);
                        JComboBoxResepSatuan.setSelectedItem(Satuan);
                        //System.out.println(Satuan + " tidak ada perubahan 2" + AA);
                    }
                    
                    /*
                    if (AA.equalsIgnoreCase("G")){
                         double data =  Double.valueOf(JTextFieldResepQty.getText()).doubleValue() * 0.0001;
                         JTextFieldResepQtyTidakBaku.setText(ConvertAngka.NilaiRupiah((double) Math.round(data * 100) / 10000) + "");
                       
                        JComboBoxResepSatuan.setSelectedItem("KG"); 
                        JComboBoxResepSatuanTidakBaku.setSelectedItem("KG"); 
                        JComboBoxResepSatuan1Porsi.setSelectedItem("KG");
                     }
                     else if (AA.equalsIgnoreCase("ML")){
                        double data =  Double.valueOf(JTextFieldResepQty.getText()).doubleValue() * 0.000001;
                        JTextFieldResepQtyTidakBaku.setText(ConvertAngka.NilaiRupiah((double) Math.round(data * 100) / 10000) + "");
                        
                        JComboBoxResepSatuan.setSelectedItem("L");
                        JComboBoxResepSatuanTidakBaku.setSelectedItem("L"); 
                        JComboBoxResepSatuan1Porsi.setSelectedItem("L");
                        
                     }
                     else {
                        
                    }
                    */

                    JTextFieldResepQty.setText(
                            ConvertAngka.NilaiRupiah(
                            SatuanSelect.ConvertSatuan(JComboBoxResepSatuan, JTextFieldResepQty,DatahashMap, ab)));              
                    ConvertKeSatuPorsi();                   
                }
                catch(Exception X){
                    System.out.println("Error 86621 "  + X);
                }            
            }
        });
        
        JTextFieldResepQty.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                /*
                 if (JComboBoxResepSatuan.getSelectedItem().equals("Sdm ( Sendok Makan )") ||
                     JComboBoxResepSatuan.getSelectedItem().equals("Sdt ( Sendok Teh)")    ){
                     JTextFieldResepQty.setText(ConvertAngka.NilaiRupiah(ConvertSdmKeGram() + ""));
                     JComboBoxResepSatuan.setSelectedIndex(1);
                }
                */
                AA = (String ) JComboBoxResepSatuan.getSelectedItem(); 
                
                try{
                     if ( !AA.equalsIgnoreCase("KG") || !AA.equalsIgnoreCase("G") ||
                              !AA.equalsIgnoreCase("L") || !AA.equalsIgnoreCase("ML") ){
                        JTextFieldResepQtyTidakBaku.setText(JTextFieldResepQty.getText());
                        JComboBoxResepSatuanTidakBaku.setSelectedItem(AA); 
                        JComboBoxResepSatuan1Porsi.setSelectedItem(AA); 
                    }
                     else {
                         JComboBoxResepSatuanTidakBaku.setSelectedItem(AA); 
                         JComboBoxResepSatuan1Porsi.setSelectedItem(AA); 
                     }                    
                }
                catch(Exception X){   
                }            
                 JTextFieldResepQtyTidakBaku.setText(JTextFieldResepQty.getText());
                 JComboBoxResepSatuan1Porsi.setSelectedItem(AA);
                 ConvertKeSatuPorsi();
            }
        });
        
        /*
         * Action jika di add
         */
        JButtonResepAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                AddTabelDiTabelResep();
                JTextFieldResepBahan.setSelectedItem("");
            }
        });
        
        /*
         * Action qty bahan resep agar tidak bisa di input String
         */ 
        SistemPro.ComponentHanyaAngka AntiAngka = new SistemPro.ComponentHanyaAngka();
        AntiAngka.SetAntiAngkaPakeKoma(JTextFieldResepQty);
        AntiAngka.SetAntiAngka(JTextFieldPorsi);
        AntiAngka.SetAntiAngkaPakeKoma(JTextFieldResepQty1Porsi);
        
        /*
         * Action jika resep di save
         */
        JButtonResepSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
               TabelResepSaveDatabase();
               JTabelResep.setEnabled(false); 
               JTextFieldResepBahan.setEnabled(false);
               JButtonResepAdd.setEnabled(false);
               JTextFieldResepQty.setEnabled(false);
               JComboBoxResepSatuan.setEnabled(false);
            }
        });
        
        JButtonResepEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
               SetTampilanButtonSaveDll(true, false, false , true, true);
               JTabelResep.setEnabled(true);  
               TampilanKedua(true);
               JTextFieldResepBahan.setEnabled(true);
               JButtonResepAdd.setEnabled(true);
            }
        });
        
        JButtonResepDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
               SetTampilanButtonSaveDll(false, false, false , true, true);
               int Pilih = JOptionPane.showConfirmDialog(null, " Anda yakin resep : " + NamaResep2 + " di hapus ?", "Hapus", JOptionPane.YES_NO_OPTION);
               if (Pilih == JOptionPane.YES_OPTION){
                        try {
                            DeleteDataResep();
                        }
                        catch (Exception X){
                            JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1227 : "  +X, " Error delete", JOptionPane.ERROR_MESSAGE);
                            X.printStackTrace();
                        } 
                 }
                 else if ( Pilih == JOptionPane.NO_OPTION){                     
                 } 

                SetResetTampilanResep();
                JTabelResep.setEnabled(false);  
            }
        });
        
        JButtonMasak.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                AmbilDataCaraMasak();
            }
        });

        JButtonPrintResep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                DefaultTableModel de = (DefaultTableModel)JTabelResep.getModel();
                             
                JRTableModelDataSource dataSource = new JRTableModelDataSource(de);
                
                JasperDesign jasperDesign = null;
                JasperPrint jasperPrint = null ;
                JasperReport jasperReport;
                HashMap hashMap = new HashMap();
                hashMap.put("NamaResep", NamaResep2);
                hashMap.put("JumlahPorsi", LabelJumlahPorsi.getText());
                hashMap.put("field", jTextArea1.getText());
                try {
                    /*
                     * Beberapa cara manggil report 
                     * Path currentRelativePath = Paths.get("");
                    String s = currentRelativePath.toAbsolutePath().toString();
                    System.out.println("Current relative path is: " + s);
                     */
                    //\\ReportJasper\\ReportResepBahan.jrxml"
                    //String x = System.getProperties().getProperty("java.class.path").split(";")[System.getProperties().getProperty("java.class.path").split(";").length - 1] + "\\ReportJasper\\ReportResepBahan.jrxml";
                    String x    = System.getProperty("user.dir")+"\\ReportJasper\\ReportResepBahan.jrxml";
                    //String cc   =  System.getProperty("user.dir");
                    //JOptionPane.showMessageDialog( null, x);    
                    // JOptionPane.showMessageDialog( null, this.getClass().getClassLoader().getResource("").getPath());
                    
                    jasperDesign = JRXmlLoader.load(x);
                    jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
                    
                    //java.awt.Container cont = jasperPrint.getContentPane();
                    //jPanel7.getcon(jasperPrint);
                    JasperViewer.viewReport(jasperPrint, false);
                           
                } catch (JRException ee) {
                    StatusSaveResep.setText(ee + "");
                    JOptionPane.showMessageDialog(null, ee);
                  ee.printStackTrace();
               }  
            }
        });
        
        JButtonResepNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
               SetTampilanButtonSaveDll(true, false, false , true, true);
               SetTampilanJPanelResep(false, true, false, false);
               SetResetTampilanResep();
               JTabelResep.setEnabled(true);  
               JTextFieldResepBahan.setEnabled(true);
               JButtonResepAdd.setEnabled(true);
               JTextFieldResepQty.setEnabled(true);
             JComboBoxResepSatuan.setEnabled(true);
            }
        });
        JButtonTambahResep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                ValidasiResep();
            }
        });
        JButtonResepBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                SetTampilanButtonSaveDll(false,true,true,true, true);
                SetResetTampilanResep();
                ViewResepYangSudahDiBuat();
                 JTabelResep.setEnabled(false);  
                 JTextFieldResepBahan.setEnabled(false);
               JButtonResepAdd.setEnabled(false);
               JTextFieldResepQty.setEnabled(false);
             JComboBoxResepSatuan.setEnabled(false);
            }
        });
    }  

    private void ValidasiResep(){
        SistemPro.ValidasiInputResep ValidasiResep = new  ValidasiInputResep() ;
            
        ValidasiResep.SetValidasiInputResep2(JTextFieldJudulResep.getText(), JTextFieldPorsi.getText());
        boolean BenarValidasiResep = ValidasiResep.GetValidasiInputResep2();
        if (BenarValidasiResep == false){
            SetTampilanButtonSaveDll(true, false, false , true, true);
            SetTampilanJPanelResep(true, false, true, true);
            
            TampilanKedua(true);
             this.setTitle("Membuat resep : " + JTextFieldJudulResep.getText());
             this.NamaResep2 = JTextFieldJudulResep.getText();
             LabelJumlahPorsi.setText(JTextFieldPorsi.getText());
             JLabelPerJumlahPorsi.setText(JTextFieldPorsi.getText());
  
        }    
    }
    
    private void TampilanKedua(boolean Bahan){
        JTextFieldResepBahan.setEnabled(Bahan);
        JTextFieldResepQty.setEnabled(Bahan);
        JTextFieldResepQty1Porsi.setEnabled(Bahan);
        JComboBoxResepSatuan.setEnabled(Bahan);
        //JComboBoxResepSatuan1Porsi.setEnabled(Bahan);
        JButtonMasak.setEnabled(Bahan);
    }
    
    private void SetResetTampilanResep(){
            
        /*
         * Logika hapus semua data di jtable
         */
        //TabelModelOrder.getDataVector().removeAllElements();
        //TabelModelOrder.fireTableDataChanged();
        DefaultTableModel dtm = (DefaultTableModel) JTabelResep.getModel();
        dtm.setRowCount(0); 
        SetResetText();
        jTextArea1.setText("");
    }
    
    private void SetResetText(){
        JTextFieldJudulResep.setText("");
        JTextFieldPorsi.setText("");
        StatusSaveResep.setText("");
        //JTextFieldResepBahan.setText("");
        JTextFieldResepQty.setText("");
        JTextFieldResepQty1Porsi.setText("");
    }
    
    /*
     * Convert data dari satuan sdm ke gram
     * Tidak di pake
     *
    private double ConvertSdmKeGram1(){
        if (JComboBoxResepSatuan.getSelectedItem().equals("Sdm ( Sendok Makan )")){
            Double HasilConvert = Double.valueOf(ConvertAngka.RupiahKeDoubel (JTextFieldResepQty.getText())).doubleValue() / 1.5 ;
            HasilConvert =  ConvertAngka.RoundingDesimal(HasilConvert,1);
            JOptionPane.showMessageDialog(null, ConvertAngka.RupiahKeDoubel (JTextFieldResepQty.getText()) 
                    +"Data diconvert dari " 
                    + JTextFieldResepQty.getText() + " Sdm = " + HasilConvert + " Gram" );
            return HasilConvert;
        }
        else if (JComboBoxResepSatuan.getSelectedItem().equals("Sdt ( Sendok Teh)")){
             Double HasilConvert = Double.valueOf(ConvertAngka.RupiahKeDoubel (JTextFieldResepQty.getText())).doubleValue() / 2;
             HasilConvert =  ConvertAngka.RoundingDesimal(HasilConvert,1);
             JOptionPane.showMessageDialog(null, "Data diconvert dari " 
                    + JTextFieldResepQty.getText() + " Sdt = " +  HasilConvert + " Gram" );
            return HasilConvert;
        }
        return 0;
    }*/
    
    
    /*
     * Convert dari banyak porsi ke 1 porsi
     */
    private void ConvertKeSatuPorsi(){
       try{
            Double HasilConvert = Double.valueOf(ConvertAngka.RupiahKeDoubel (JTextFieldResepQty.getText())).doubleValue();
            HasilConvert =  ConvertAngka.RoundingDesimal(HasilConvert,1) / Integer.valueOf(JLabelPerJumlahPorsi.getText()).intValue();
           
            //JTextFieldResepQty1Porsi.setText(ConvertAngka.NilaiRupiah(String.format("%.2f", HasilConvert)));
            JTextFieldResepQty1Porsi.setText(ConvertAngka.NilaiRupiah((double) Math.round(HasilConvert * 100) / 100) + "");
       }
       catch(Exception x){
           JOptionPane.showMessageDialog(null, "Klik new untuk terlebih dahulu");
       }
       
    }
    
    private void SetTampilanComponent() {
        
        /*
         * Static JPanel
         * Di Gunakan agar jika ada penambahan componet tidak berubah
         * biar tidak bergerak
         */
        Dimension dimension = new Dimension(10, 30);
        kazaoCalendarDateResep.setMaximumSize(dimension);
        kazaoCalendarDateResep.setMinimumSize(dimension);
        kazaoCalendarDateResep.setPreferredSize(dimension);
        kazaoCalendarDateResep.setFormat("dd-mm-yyy");
    }
    
    private void SetTampilanJPanelResep(boolean AddResep, boolean InputBahan, boolean PerhitunganResep, boolean ButtonSave){
        JPanelAddResepBesaran.setVisible(AddResep);
        JPanelInputBahan.setVisible(InputBahan);
        JPanelPerhitunganResep.setVisible(PerhitunganResep);
        JPanelButtonSave.setVisible(ButtonSave);
    }
    
    private void SetTampilanButtonSaveDll(boolean Save, boolean Edit, boolean Delete, boolean Browse, boolean New){
        JButtonResepEdit.setEnabled(Edit);
        JButtonResepSave.setEnabled(Save);
        JButtonResepDelete.setEnabled(Delete);
        JButtonResepBrowse.setEnabled(Browse);
        JButtonResepNew.setEnabled(New);
        JButtonMasak.setEnabled(Delete);
    }
    
    private void DeleteDataResep(){
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from header_resep where nama_resep = '"+ NamaResep2 + "'");
    }
    
    private void TabelResepSaveDatabase(){
        boolean SaveAtauTidak;
        
        /*
         * Save Header Resep
         */
        String ResepPorsi   = LabelJumlahPorsi.getText();
        String ResepTgl     = kazaoCalendarDateResep.getKazaoCalendar().getShortDate();
        
        SistemPro.TanggalSistem KazaoToIndo = new SistemPro.TanggalSistem();
        KazaoToIndo.SetKazaoToTglIndo(ResepTgl );
        String UserTgl  = KazaoToIndo.GetTglIndoStrKazao();
        KazaoToIndo.SetKazaoToBlnIndo(ResepTgl );
        String UserBln  = KazaoToIndo.GetBlnIndoStrKazao();
        KazaoToIndo.SetKazaoToThnIndo(ResepTgl );
        String UserThn = KazaoToIndo.GetThnIndoStKazao();
        String TanggalResep = UserTgl+"-"+UserBln+"-"+UserThn;
        
        try {
                /*
                 * Hapus jika tidak bisa save di detail_resep
                 */
                    DeleteDataResep();
                    
                /*
                 * Save Header
                 */
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    Stm.executeUpdate("INSERT INTO header_resep (nama_resep,porsi_resep,created_date,update_date) VALUES('"
                                + NamaResep2 + "','"+ ResepPorsi + "','" + TanggalResep + "'," + " now())"); 
                    
                 /*
                  * Save Cara Masak
                  */
                    Statement Stm2 = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    Stm2.executeUpdate("INSERT INTO header_memasak "
                            + "(nama_masakan, cara_memasak, created_date)"
                            + " VALUES ( '"+ NamaResep2 + "','"+ jTextArea1.getText() + "', now())");  
                        
                  /*
                   * Save DetailPo to database Mysql
                   */
                    
                 try {
                     int a = JTabelResep.getRowCount() ;
                     Statement stm = K.createStatement();

                     // dbStatement=con.createStatement();
                     for(int i=0;i< a;i++){
 
                         int no         =Integer.valueOf(JTabelResep.getValueAt(i, 0).toString()).intValue();
                         String bahan   =JTabelResep.getValueAt(i, 1).toString();
                         String qty1    =JTabelResep.getValueAt(i, 2).toString();
                         String satuan1 =JTabelResep.getValueAt(i, 3).toString();
                         String satuan  =JTabelResep.getValueAt(i, 7).toString();
                         String qty     =JTabelResep.getValueAt(i, 6).toString();
                         String QtyTdkBaku      =JTabelResep.getValueAt(i, 4).toString();
                         String SatuanTdkBaku   =JTabelResep.getValueAt(i, 5).toString();
                         
                         /*
                         JOptionPane.showMessageDialog(null, period+"','"+KeyNoDT+"','"+no+"','"+trans_no+"','"+itempart+"','"+itemname+"','"+qty+"','"
                                 +"','"+price+"','"+totprice+"','"+unit+"','"+bth+"','"+ket+"','"+TransDate+"','"+userDT+"','"+Updated);
                          Double.valueOf(ConvertAngka.RupiahKeDoubel (JTextFieldResepQty.getText())).doubleValue()
                          */
                         stm.executeUpdate("INSERT INTO detail_resep ( no,namresep, bahan,qty_1_porsi, satuan_1_porsi,qty_tdk_baku, satuan_tdk_baku, qty, satuan, created_date, update_date) VALUES ('"
                                 +no+"','"+ NamaResep2+ "','"+bahan+"','"
                                 +Double.valueOf(ConvertAngka.RupiahKeDoubel (qty1)).doubleValue()+"','"+satuan1+"','" 
                                 +Double.valueOf(ConvertAngka.RupiahKeDoubel (QtyTdkBaku)).doubleValue() + "','" + SatuanTdkBaku + "','"
                                 +Double.valueOf(ConvertAngka.RupiahKeDoubel (qty)).doubleValue()+"','"+satuan+"','"+TanggalResep+"', now())");
                         }
                     StatusSaveResep.setText("BERHASIL DI SAVE");
                     SetTampilanButtonSaveDll(false, false, false , true, true);
                     TampilanKedua(false);
                 }
                 catch (Exception X){
                     
                     /*
                      * Hapus jika tidak bisa save di detail_resep
                      */
                      DeleteDataResep();
                      JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 12269822 : "  +X, " Error", JOptionPane.ERROR_MESSAGE);
                    }           
                 }
                 catch (Exception Ex){
                JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1224 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
            }  
        
    }
    
    private void AmbilDataCaraMasak(){
        CaraMasak2   CaraMasakan = new CaraMasak2 (new javax.swing.JFrame(), true, jTextArea1.getText());
        CaraMasakan.setVisible(true);      
        CaraMasakObject Dt = CaraMasakan.GetTableData();
        jTextArea1.setText(Dt.GetCaraMasak());;   
    }
    private void ViewResepYangSudahDiBuat(){
        try {
            ResepView   ResepViewSekarang = new ResepView (new javax.swing.JFrame(), true);
            ResepViewSekarang.setVisible(true);

            ResepObject Dt = ResepViewSekarang.GetTableData();

            /*
            JTextOpenPo.setText(Dt.GetVPTransNo());
            JTextSupplierWr.setText(Dt.GetVPSuplier());
            JTextAdressWr1.setText(Dt.GetVPSuplierAddress());
            //JTextDeliveryToPo.setText(Dt.GetVPDepartementsAddress());
            */

            LabelJumlahPorsi.setText(Dt.GetVRPorsiResep());
            JLabelPerJumlahPorsi.setText(Dt.GetVRPorsiResep());
            this.setTitle("Membuat Resep : " + Dt.GetVRNamaResep());
            String TglBuatResep     = Dt.GetVRTanggalBuat();

            /*
             * Set Tanggal di kazao
             */
            SistemPro.TanggalSistem TglNow = new SistemPro.TanggalSistem(); 
            Calendar Tgl = Calendar.getInstance();

            Tgl.set(TglNow.ConvertTglBlnThnToTahun(TglBuatResep), TglNow.ConvertTglBlnThnToBulan(TglBuatResep) - 1, TglNow.ConvertTglBlnThnToTanggal(TglBuatResep));
           // Tgl.set(Calendar.MONTH, TglNow.ConvertTglBlnThnToBulan(TglBuatResep)); // di java range bulan 0..11
            //Tgl.set(Calendar.DATE, TglNow.ConvertTglBlnThnToTanggal(TglBuatResep));
            //Tgl.set(2014,0,11);
            //Tgl.clear();

            kazaoCalendarDateResep.setCalendar(Tgl);  
            this.NamaResep2 = Dt.GetVRNamaResep();
            AmbilDataDatabaseSetelahGetBrowse(Dt.GetVRNamaResep());
            
        }
        catch (Exception X){
            
        }
    }
    
   private void AmbilDataDatabaseSetelahGetBrowse(String PilihData){
       
        /*
         * Isi data ke Tabel dari database
         */      
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT namresep, no ,bahan,qty_1_porsi, satuan_1_porsi, qty_tdk_baku, satuan_tdk_baku, qty, satuan"
                       + " from detail_resep  where namresep = '"+ PilihData + "' order by no asc  ");              
               baris = HQ.getRow();

               while(HQ.next()  ){

                   String No     = String.valueOf(HQ.getInt("no")).toString();
                   String Bahan  = HQ.getString("bahan");
                   String Qty1   = HQ.getString("qty_1_porsi");
                   String Satuan1= HQ.getString("satuan_1_porsi");
                   String Qty    = String.valueOf(HQ.getDouble("qty")).toString();
                   String Satuan = HQ.getString("satuan");
                   String SatuanTdkBaku     = HQ.getString("satuan_tdk_baku");
                   String QtyTdkBaku        = HQ.getString("qty_tdk_baku");
                   
                   String[] add         = {No , Bahan,ConvertAngka.NilaiRupiah(Qty1), Satuan1,
                       ConvertAngka.NilaiRupiah(QtyTdkBaku) ,SatuanTdkBaku, ConvertAngka.NilaiRupiah(Qty),Satuan};
                   TabelModelOrder.addRow(add);      
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
      
           /*
            * Cara Masak
            */  
        ResultSet HQ2 = null;
           try {
               Statement stm3 = K.createStatement();
               HQ2 = stm3.executeQuery("SELECT nama_masakan, cara_memasak "
                       + "from  header_memasak where nama_masakan = '" + PilihData+ "'");              
               while(HQ2.next()  ){                  
                   String BB    = HQ2.getString("cara_memasak");
                   jTextArea1.setText(BB);
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }

   }
    
    private void TabelResep() {
        String header[] = {"No", "Bahan","Qty 1 Porsi", "Satuan 1 Porsi","Qty Tidak Baku","Satuan Tidak Baku" , "Qty","Satuan", "Action"};
        TabelModelOrder = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                                if(colIndex == 1) {return true ;} //  nilai false agar tidak bisa di edit
                                if(colIndex == 8) {return true ;}
                                //if(colIndex == 3) {return EditKolomQty ;}
                                return false;   //Disallow the editing of any cell
                        }
        };
        JTabelResep.setModel(TabelModelOrder);
       
        /*
         * Rata tengah atau kanan table
         */
        JTabelResep.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelResep.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        JTabelResep.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
        JTabelResep.getColumnModel().getColumn(4).setCellRenderer( tengah );
        JTabelResep.getColumnModel().getColumn(5).setCellRenderer( tengah );
        JTabelResep.getColumnModel().getColumn(6).setCellRenderer( tengah );
        JTabelResep.getColumnModel().getColumn(7).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         */      
        int jarak_colom[] = {40,250,60,100,60,100,60,100, 80};
        Colom_table ukuran_colom = new Colom_table();
        ukuran_colom.ukuran_colom(JTabelResep, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTabelResep.setName("Tabel Resep");
        JTabelResep.getColumnModel().getColumn(8).setCellRenderer(  new ButtonJTable("Delete"));
        JTabelResep.getColumnModel().getColumn(8).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelOrder, JTabelResep));
       
        /*
         * Disable drag colum tabel
         */       
        JTabelResep.getTableHeader().setReorderingAllowed(false);
    }
    
    private void AddTabelDiTabelResep(){
        
        String ResepBahan   = ( String ) JTextFieldResepBahan.getSelectedItem();
        /*
         * Filter bahan
         */
        ResepBahan          = ResepBahan.replaceAll(".*--", "");
        
        String ResepQty     = JTextFieldResepQty.getText();
        String ResepSatuan  = (String) JComboBoxResepSatuan.getSelectedItem();
        
        String Resep1Satuan = (String)JComboBoxResepSatuan1Porsi.getSelectedItem();
        String Resep1Qty    = JTextFieldResepQty1Porsi.getText();
        String ResepQtyTdkBaku = JTextFieldResepQtyTidakBaku.getText();
                
        JFormattedTextField JumlahPorsi  = new JFormattedTextField(NumberFormat.getIntegerInstance());
        
        SistemPro.NoUrut NoUrutan = new   SistemPro.NoUrut();
        
        /*
         * Set data no urut
         */
        int ax = JTabelResep.getRowCount() ;
        //JOptionPane.showMessageDialog(null, ax);
        if (ax == 0 ){
            NoUrutan.SetNoUrut(String.valueOf(ax).toString());
        }
        else if ( ax > 0) {
            ax = ax - 1;
            String ab = (String) JTabelResep.getValueAt(ax, 0);
            NoUrutan.SetNoUrut(ab);
        }
        
        /*
         * Validasi qty 100 porsi
         */       
        if ("".equals(ResepBahan)){
             JOptionPane.showMessageDialog(null, "Data bahan tidak ada !", "Perhatian",JOptionPane.ERROR_MESSAGE);           
        }
        else if("".equals(ResepQty)){
            JOptionPane.showMessageDialog(null, "Data qty bahan tidak ada !", "Perhatian",JOptionPane.ERROR_MESSAGE);           
        }
        else if("".equals(Resep1Qty)){
            JOptionPane.showMessageDialog(null, "Data qty bahan tidak ada !", "Perhatian",JOptionPane.ERROR_MESSAGE);            
        }
        else if ("".equals(ResepQtyTdkBaku)){
            JOptionPane.showMessageDialog(null, "Data qty bahan tidak ada !", "Perhatian",JOptionPane.ERROR_MESSAGE);   
        }
        else {
            Object obj[] = new Object[10];        
            obj[0] = NoUrutan.GetNoUrut();
            obj[1] = ResepBahan;
            obj[2] = Resep1Qty;
            obj[3] = Resep1Satuan;
            obj[4] = ResepQtyTdkBaku;
            obj[5] = JComboBoxResepSatuanTidakBaku.getSelectedItem();
            obj[6] = ResepQty;
            obj[7] = ResepSatuan;
            TabelModelOrder.addRow(obj);
            SetResetText();
        }       
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
               }
               if (baris == 0){
                   this.Harga   = 0; 
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (456789)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
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

        jComboBox1 = new javax.swing.JComboBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        JPanelAddResepBesaran = new javax.swing.JPanel();
        JPanelAddResep = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        JComboBoxResepSatuan = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        JTextFieldResepQty = new javax.swing.JTextField();
        JButtonResepAdd = new javax.swing.JButton();
        JLabelPerJumlahPorsi = new javax.swing.JLabel();
        JLabelPerJumlahPorsi1 = new javax.swing.JLabel();
        JLabelPerJumlahPorsi2 = new javax.swing.JLabel();
        JLabelPerJumlahPorsi3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        JLabelPerJumlahPorsi5 = new javax.swing.JLabel();
        JTextFieldResepQty1Porsi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        JComboBoxResepSatuan1Porsi = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        JTextFieldResepBahan = new javax.swing.JComboBox();
        JTextFieldResepQtyTidakBaku = new javax.swing.JTextField();
        JComboBoxResepSatuanTidakBaku = new javax.swing.JComboBox();
        JPanelButtonSave = new javax.swing.JPanel();
        JButtonResepSave = new javax.swing.JButton();
        JButtonResepEdit = new javax.swing.JButton();
        JButtonResepDelete = new javax.swing.JButton();
        JButtonResepBrowse = new javax.swing.JButton();
        JButtonResepNew = new javax.swing.JButton();
        JButtonPrintResep = new javax.swing.JButton();
        JButtonMasak = new javax.swing.JButton();
        JPanelPerhitunganResep = new javax.swing.JPanel();
        label2 = new java.awt.Label();
        kazaoCalendarDateResep = new org.kazao.calendar.KazaoCalendarDate();
        jLabel7 = new javax.swing.JLabel();
        StatusSaveResep = new javax.swing.JLabel();
        LabelJumlahPorsi = new javax.swing.JLabel();
        JPanelInputBahan = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        JTextFieldJudulResep = new javax.swing.JTextField();
        label1 = new java.awt.Label();
        JTextFieldPorsi = new javax.swing.JTextField();
        JButtonTambahResep = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelResep = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jCheckBox1.setText("jCheckBox1");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("RESEP");
        setAutoscrolls(true);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/logo.png"))); // NOI18N
        setVisible(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(0));

        JPanelAddResepBesaran.setBorder(javax.swing.BorderFactory.createBevelBorder(0));

        jLabel1.setText("Nama Bahan Resep");

        jLabel2.setText("   Satuan");

        JComboBoxResepSatuan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "KG", "G", "SDM", "SDT", "L", "ML", "BUNGKUS", "BIJI", "BUAH", "RUAS", "KEMASAN", "IKAT", "BATANG", "LEMBAR", "EKOR", "GELAS" }));
        JComboBoxResepSatuan.setEnabled(false);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Qty Per");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        JTextFieldResepQty.setEnabled(false);

        JButtonResepAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Apply.png"))); // NOI18N
        JButtonResepAdd.setText("Add");
        JButtonResepAdd.setEnabled(false);

        JLabelPerJumlahPorsi.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        JLabelPerJumlahPorsi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabelPerJumlahPorsi.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        JLabelPerJumlahPorsi1.setText("Porsi   ");

        JLabelPerJumlahPorsi2.setText(":");

        JLabelPerJumlahPorsi3.setText(":");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Qty Per  1 Porsi");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        JLabelPerJumlahPorsi5.setText(":");

        JTextFieldResepQty1Porsi.setEditable(false);
        JTextFieldResepQty1Porsi.setBackground(new java.awt.Color(204, 204, 204));
        JTextFieldResepQty1Porsi.setEnabled(false);

        jLabel5.setText("   Satuan");

        JComboBoxResepSatuan1Porsi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "KG", "G", "SDM", "SDT", "L", "ML", "BUNGKUS", "BIJI", "BUAH", "RUAS", "KEMASAN", "IKAT", "BATANG", "LEMBAR", "EKOR", "GELAS" }));
        JComboBoxResepSatuan1Porsi.setEnabled(false);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        JTextFieldResepBahan.setEditable(true);
        JTextFieldResepBahan.setToolTipText("");
        JTextFieldResepBahan.setEnabled(false);
        JTextFieldResepBahan.setModel(new javax.swing.DefaultComboBoxModel( app_search_data_resep.getData().toArray()));

        JTextFieldResepQtyTidakBaku.setEnabled(false);

        JComboBoxResepSatuanTidakBaku.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "KG", "G", "SDM", "SDT", "L", "ML", "BUNGKUS", "BIJI", "BUAH", "RUAS", "KEMASAN", "IKAT", "BATANG", "LEMBAR", "EKOR", "GELAS" }));
        JComboBoxResepSatuanTidakBaku.setEnabled(false);

        javax.swing.GroupLayout JPanelAddResepLayout = new javax.swing.GroupLayout(JPanelAddResep);
        JPanelAddResep.setLayout(JPanelAddResepLayout);
        JPanelAddResepLayout.setHorizontalGroup(
            JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelAddResepLayout.createSequentialGroup()
                .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPanelAddResepLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLabelPerJumlahPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLabelPerJumlahPorsi1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(JLabelPerJumlahPorsi2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JLabelPerJumlahPorsi5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(JLabelPerJumlahPorsi3, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelAddResepLayout.createSequentialGroup()
                        .addComponent(JTextFieldResepBahan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonResepAdd))
                    .addGroup(JPanelAddResepLayout.createSequentialGroup()
                        .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(JTextFieldResepQty, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                            .addComponent(JTextFieldResepQty1Porsi))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPanelAddResepLayout.createSequentialGroup()
                                .addComponent(JComboBoxResepSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JTextFieldResepQtyTidakBaku, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(JComboBoxResepSatuan1Porsi, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JComboBoxResepSatuanTidakBaku, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        JPanelAddResepLayout.setVerticalGroup(
            JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelAddResepLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(JLabelPerJumlahPorsi3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JTextFieldResepBahan))
                    .addComponent(JButtonResepAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(JPanelAddResepLayout.createSequentialGroup()
                        .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(JLabelPerJumlahPorsi1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JLabelPerJumlahPorsi2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JTextFieldResepQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)
                                .addComponent(JComboBoxResepSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JTextFieldResepQtyTidakBaku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JComboBoxResepSatuanTidakBaku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(JLabelPerJumlahPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(JComboBoxResepSatuan1Porsi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JLabelPerJumlahPorsi5)
                                .addComponent(JTextFieldResepQty1Porsi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(3, 3, 3))
        );

        javax.swing.GroupLayout JPanelAddResepBesaranLayout = new javax.swing.GroupLayout(JPanelAddResepBesaran);
        JPanelAddResepBesaran.setLayout(JPanelAddResepBesaranLayout);
        JPanelAddResepBesaranLayout.setHorizontalGroup(
            JPanelAddResepBesaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelAddResepBesaranLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPanelAddResep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        JPanelAddResepBesaranLayout.setVerticalGroup(
            JPanelAddResepBesaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelAddResepBesaranLayout.createSequentialGroup()
                .addComponent(JPanelAddResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        JPanelButtonSave.setBorder(javax.swing.BorderFactory.createBevelBorder(0));

        JButtonResepSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Save.png"))); // NOI18N
        JButtonResepSave.setText("Save");
        JButtonResepSave.setEnabled(false);

        JButtonResepEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Update.png"))); // NOI18N
        JButtonResepEdit.setText("Edit");
        JButtonResepEdit.setEnabled(false);

        JButtonResepDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Delete.png"))); // NOI18N
        JButtonResepDelete.setText("Delete");
        JButtonResepDelete.setEnabled(false);

        JButtonResepBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Search.png"))); // NOI18N
        JButtonResepBrowse.setText("Browse");

        JButtonResepNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonResepNew.setText("New");

        JButtonPrintResep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPrintResep.setText("Print");

        JButtonMasak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/cara_masak2.png"))); // NOI18N
        JButtonMasak.setText("Cara Masak");
        JButtonMasak.setEnabled(false);

        javax.swing.GroupLayout JPanelButtonSaveLayout = new javax.swing.GroupLayout(JPanelButtonSave);
        JPanelButtonSave.setLayout(JPanelButtonSaveLayout);
        JPanelButtonSaveLayout.setHorizontalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelButtonSaveLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JButtonMasak)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonResepSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonResepEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonResepDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonResepBrowse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonResepNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPrintResep)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPanelButtonSaveLayout.setVerticalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSaveLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelButtonSaveLayout.createSequentialGroup()
                        .addGroup(JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JButtonResepSave)
                            .addComponent(JButtonResepEdit)
                            .addComponent(JButtonResepDelete)
                            .addComponent(JButtonResepBrowse)
                            .addComponent(JButtonResepNew)
                            .addComponent(JButtonPrintResep))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(JButtonMasak, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        label2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        label2.setText("Perhitungan Porsi :");

        jLabel7.setText("Tanggal");

        javax.swing.GroupLayout JPanelPerhitunganResepLayout = new javax.swing.GroupLayout(JPanelPerhitunganResep);
        JPanelPerhitunganResep.setLayout(JPanelPerhitunganResepLayout);
        JPanelPerhitunganResepLayout.setHorizontalGroup(
            JPanelPerhitunganResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelPerhitunganResepLayout.createSequentialGroup()
                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelJumlahPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(kazaoCalendarDateResep, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StatusSaveResep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPanelPerhitunganResepLayout.setVerticalGroup(
            JPanelPerhitunganResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(StatusSaveResep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(JPanelPerhitunganResepLayout.createSequentialGroup()
                .addGroup(JPanelPerhitunganResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(kazaoCalendarDateResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(JPanelPerhitunganResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(LabelJumlahPorsi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(label2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        JPanelInputBahan.setBorder(new javax.swing.border.SoftBevelBorder(0));

        jLabel6.setText("Masukan nama resep :");

        label1.setText("Jumlah porsi :");

        JButtonTambahResep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Apply.png"))); // NOI18N
        JButtonTambahResep.setText("Tambah Resep");

        javax.swing.GroupLayout JPanelInputBahanLayout = new javax.swing.GroupLayout(JPanelInputBahan);
        JPanelInputBahan.setLayout(JPanelInputBahanLayout);
        JPanelInputBahanLayout.setHorizontalGroup(
            JPanelInputBahanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelInputBahanLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JTextFieldJudulResep, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JButtonTambahResep))
        );
        JPanelInputBahanLayout.setVerticalGroup(
            JPanelInputBahanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelInputBahanLayout.createSequentialGroup()
                .addGroup(JPanelInputBahanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JButtonTambahResep)
                    .addGroup(JPanelInputBahanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(JTextFieldJudulResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextFieldPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jScrollPane1.setBorder(new javax.swing.border.SoftBevelBorder(0));

        JTabelResep.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(JTabelResep);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("DATA ISI RESEP");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelAddResepBesaran, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelPerhitunganResep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelInputBahan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelAddResepBesaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelInputBahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelPerhitunganResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-758)/2, (screenSize.height-580)/2, 758, 580);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonMasak;
    private javax.swing.JButton JButtonPrintResep;
    private javax.swing.JButton JButtonResepAdd;
    private javax.swing.JButton JButtonResepBrowse;
    private javax.swing.JButton JButtonResepDelete;
    private javax.swing.JButton JButtonResepEdit;
    private javax.swing.JButton JButtonResepNew;
    private javax.swing.JButton JButtonResepSave;
    private javax.swing.JButton JButtonTambahResep;
    private javax.swing.JComboBox JComboBoxResepSatuan;
    private javax.swing.JComboBox JComboBoxResepSatuan1Porsi;
    private javax.swing.JComboBox JComboBoxResepSatuanTidakBaku;
    private javax.swing.JLabel JLabelPerJumlahPorsi;
    private javax.swing.JLabel JLabelPerJumlahPorsi1;
    private javax.swing.JLabel JLabelPerJumlahPorsi2;
    private javax.swing.JLabel JLabelPerJumlahPorsi3;
    private javax.swing.JLabel JLabelPerJumlahPorsi5;
    private javax.swing.JPanel JPanelAddResep;
    private javax.swing.JPanel JPanelAddResepBesaran;
    private javax.swing.JPanel JPanelButtonSave;
    private javax.swing.JPanel JPanelInputBahan;
    private javax.swing.JPanel JPanelPerhitunganResep;
    private javax.swing.JTable JTabelResep;
    private javax.swing.JTextField JTextFieldJudulResep;
    private javax.swing.JTextField JTextFieldPorsi;
    private javax.swing.JComboBox JTextFieldResepBahan;
    private javax.swing.JTextField JTextFieldResepQty;
    private javax.swing.JTextField JTextFieldResepQty1Porsi;
    private javax.swing.JTextField JTextFieldResepQtyTidakBaku;
    private javax.swing.JLabel LabelJumlahPorsi;
    private javax.swing.JLabel StatusSaveResep;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDateResep;
    private java.awt.Label label1;
    private java.awt.Label label2;
    // End of variables declaration//GEN-END:variables
}
