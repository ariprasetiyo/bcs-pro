
package bcs_pro;

import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.SatuUntukSemua;
import SistemPro.app_search1;
import SistemPro.app_search_data_pemesanan_penjualan;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author pein
 */
public class PesananPenjualan2 extends javax.swing.JPanel {

    /**
     * Creates new form PesananPenjualan2
     */
    SistemPro.KoneksiDatabase KD    = new SistemPro.KoneksiDatabase();
    SistemPro.TanggalSistem N       = new SistemPro.TanggalSistem();
    Connection              K       = KD.createConnection();
    DefaultTableModel   Modeltabel2 = new DefaultTableModel();
    DefaultTableModel   Modeltabel3 = new DefaultTableModel();
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();
    private SistemPro.KomaToString ConvertAngka = new SistemPro.KomaToString();
    private  SistemPro.ComponentHanyaAngka HanyaAngka = new SistemPro.ComponentHanyaAngka();
    SatuUntukSemua Satu = new SatuUntukSemua();
    String TransNoPesanan, NamaPemesan, AA,BB,CC,DD,EE,FF, GG, HH, II, JJ,KK;
    boolean Edit = false, BayarDP = true;
    
    
    public PesananPenjualan2() {
        initComponents();
        SetTanggalPeriod();
        Tabel2();
        Tabel3();
        Aksi();
        AmbilHeaderPenjualan(AmbilDataPeriod1(), 0);
        SetTanggalDateChooser();
    }
    
    private void Aksi(){
        
        InputMenu.setSelectedIndex(-1);
        final JTextField JTextFieldItem = (JTextField)InputMenu.getEditor().getEditorComponent();
        JTextFieldItem.setText("");
        JTextFieldItem.addKeyListener(new app_search1(InputMenu));
        
        JButtonPbSave.addActionListener(new ActionListener(){
            @Override
             public void actionPerformed (ActionEvent A){
                 SaveDatabase();
                 Component[] Data    = {AlamaPemesan,NoTelpon, FieldNote,FieldTotalDibayar, DPtidak, kazaoCalendarDatePesanan, Add, DPtidak };
                 boolean[] TutupTidak= {false,false,false,false,false,false, false, false};
                 Satu.LogikaComponent(Data, TutupTidak);
                 
                 /*
                  * Save, edit, delete, new, print, add, iput menu
                  */
                 boolean[] EnableDisable = {false, false, true, true, false, false, true};
                 Buttonn(EnableDisable);
                 AmbilHeaderPenjualan(AmbilDataPeriod1(), 0);
                 dateChooserCombo1.setEnabled(false);
                 DisabelDoubelKlikTabel(false);
                 BayarDP = false;
             }
        });
        
        JButtonPbNew.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent A){
                 ResetTampilan();
                 /*
                  * Save, edit, delete, new, print, add, iput menu
                  */
                 boolean[] EnableDisable = {false, false, true, true, true, true, false};
                 Buttonn(EnableDisable);
                 
                 Component[] Data    = {AlamaPemesan,NoTelpon, FieldNote,FieldTotalDibayar, DPtidak, kazaoCalendarDatePesanan, Add ,DPtidak};
                 boolean[] TutupTidak= {true,true,true,true,false,true, true, true};
                 Satu.LogikaComponent(Data, TutupTidak);
                 dateChooserCombo1.setEnabled(false);
                 DisabelDoubelKlikTabel(true);
                  FieldBayar.setEnabled(false);
                  BayarDP = true;
                  dateChooserCombo1.setText("");
             }
        });
        
        JButtonPbDelete.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent A){
                 
                JPasswordField pass = new JPasswordField();
                Object[] Object ={
                  "Masukan password untuk menghapus data ", pass
                };
                int Pilih = JOptionPane.showConfirmDialog(null , Object , "Input data master barang ", JOptionPane.OK_CANCEL_OPTION);
                if (Pilih == JOptionPane.OK_OPTION){
                    if (pass.getText().equalsIgnoreCase("AriHanif")){
                         Satu.DeleteData(FieldNoPemesan.getText(), "header_penjualan");
                         AmbilHeaderPenjualan(AmbilDataPeriod1(), 0);
                     }
                 }          
             }
        });
        
        Add.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent A){
                 ResetTampilan();
                 AddMenu();
                 
                 /*
                  * Save, edit, delete, new, print, add, input menu
                  */
                 boolean[] EnableDisable = {true, false, true, true, true, true, false};
                 Buttonn(EnableDisable);
             }
        });
        
        DPtidak.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent A){
                 if (DPtidak.getSelectedIndex() == 0){
                     //FieldBayar.setEnabled(false);
                    // FieldBayar.setText("0");
                     
                     PerhitunganTotal();
                     dateChooserCombo1.setEnabled(false);
                     FieldBayar.setEnabled(false);
                 }
                 else if (DPtidak.getSelectedIndex() == 2){
                     dateChooserCombo1.setText("");
                     dateChooserCombo1.setEnabled(true);
                     PerhitunganTotal();
                      FieldBayar.setEnabled(false);
                      FieldTotalDibayar.setEnabled(false);
                 }
                 else {
                    // FieldBayar.setEnabled(true);
                     PerhitunganTotal();
                     dateChooserCombo1.setEnabled(false);
                     FieldBayar.setEnabled(BayarDP);
                 }
             }
        });
        
        
        JButtonPbEdit.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent A){
                 JButtonPbSave.setEnabled(true);
                 JButtonPbEdit.setEnabled(false);
                 Edit = true;
             }
        });
        
        JButtonRefresh.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent A){
                 AmbilHeaderPenjualan(AmbilDataPeriod1(), 0);
                 dateChooserCombo1.setText("");
             }
        });
        
        JTabelListPenjualan.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()>=2){
                    dateChooserCombo1.setText("");
                    BayarDP = false;
                     ResetTampilan();   
                     AA = ( String ) JTabelListPenjualan.getValueAt( JTabelListPenjualan.getSelectedRow(), 1);
                     //System.out.println(AA);
                     JButtonPbPrint.setEnabled(true);
                     
                     Component[] Data    = {AlamaPemesan,NoTelpon, FieldNote,FieldTotalDibayar, DPtidak, kazaoCalendarDatePesanan, Add, DPtidak };
                    boolean[] TutupTidak= {false,false,false,false,false,false, false, false};
                    Satu.LogikaComponent(Data, TutupTidak);
                    
                    
                    /*
                     * Save, edit, delete, new, print, add, iput menu
                     */
                    boolean[] EnableDisable = {false, false, true, true, false, false, true};
                    Buttonn(EnableDisable);
                    dateChooserCombo1.setEnabled(false);
                    DisabelDoubelKlikTabel(false);
                     AmbilDataView(AA);
                }
            }   
        });
        
        JButtonPbPrint.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent A){
                 PrintJasperReportPesanan();
             }
        });
        
        HanyaAngka.SetAntiAngkaPakeKoma(FieldBayar);
        HanyaAngka.SetAntiAngkaPakeKoma(FieldTotalDibayar);
        
        FieldBayar.addKeyListener(new KeyListener(){
            public void keyReleased(KeyEvent e) {
                if (DPtidak.getSelectedIndex() == 2 ){
                     FieldBayar.setText("0");
                 }
                 PerhitunganTotal();
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });        
        
        FieldTotalDibayar.addKeyListener(new KeyListener(){
            public void keyReleased(KeyEvent e) {
                 PerhitunganTotal();
            }

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });        
    }
    
    private void PrintJasperReportPesanan(){
        DefaultTableModel de = (DefaultTableModel)JTablePenjualan.getModel();

        JRTableModelDataSource dataSource = new JRTableModelDataSource(de);

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();
        
        String TransNo, NamaPemesan, TanggalTransaksi, JumlahDanJenisPemesan, Alamat, NoHp, Status, DP, SubTotal, GrandTotal, Note;
        
        TransNo = FieldNoPemesan.getText();
        BB  = FieldJenisPesananQty.getText();
        CC  = FieldJenisPesanan.getText();
        
        JumlahDanJenisPemesan = BB + " " + CC;
        
        TanggalTransaksi  = kazaoCalendarDatePesanan.getKazaoCalendar().getShortDate();
        
        NamaPemesan  = FieldNamaPemesan.getText();
        Alamat  = AlamaPemesan.getText();
        NoHp    = NoTelpon.getText();
        Note    = FieldNote.getText();
        
        Status = (String) DPtidak.getSelectedItem();

        DP              =  ConvertAngka.RupiahKeDoubel(FieldDP.getText());
        SubTotal        =  ConvertAngka.RupiahKeDoubel(FieldSubTotal.getText());
        GrandTotal      =  ConvertAngka.RupiahKeDoubel(FieldGrandTotal.getText());

        hashMap.put("TransNo", TransNo);
        hashMap.put("NamaPemesan", NamaPemesan);
        hashMap.put("TanggalTransaksi",TanggalTransaksi);
        hashMap.put("JumlahJenisPorsi",JumlahDanJenisPemesan);
        hashMap.put("Alamat",Alamat);
        hashMap.put("NoHP",NoHp);
        hashMap.put("Status",Status);
        hashMap.put("Dp",DP);
        hashMap.put("SubTotal",SubTotal);
        hashMap.put("GrandTotal",GrandTotal);
        hashMap.put("Note",Note);
             
        try {
            String x    = System.getProperty("user.dir")+"\\ReportJasper\\PrintPOS.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
            //JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }  
        
        //choose default print service  
 PrintService printService = PrintServiceLookup.lookupDefaultPrintService();  
 if (printService == null){  
            try {
                //error message                
                   throw new PrinterException("No Printer Attached / Shared to the server");
            } catch (PrinterException ex) {
                Logger.getLogger(PesananPenjualan2.class.getName()).log(Level.SEVERE, null, ex);
            }
 }else{  
            try {
                JRExporter exporter = new JRPrintServiceExporter();  
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);           
                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printService.getAttributes());  
                exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);  
                exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.TRUE);        
                exporter.exportReport();
            } catch (JRException ex) {
                Logger.getLogger(PesananPenjualan2.class.getName()).log(Level.SEVERE, null, ex);
            }
 }  
    }
    
    private void AmbilDataView(String TransNo){
        FieldNoPemesan.setText(TransNo);
        FieldNamaPemesan.setText((String )JTabelListPenjualan.getValueAt( JTabelListPenjualan.getSelectedRow(),2));
        DD = (String )JTabelListPenjualan.getValueAt( JTabelListPenjualan.getSelectedRow(),3);
        
       /*
        * Set Tanggal di kazao
        * 11-12-2014
        */
       SistemPro.TanggalSistem TglNow = new SistemPro.TanggalSistem(); 
       Calendar Tgl = Calendar.getInstance();
       Tgl.set(TglNow.ConvertTglBlnThnToTahun(DD), TglNow.ConvertTglBlnThnToBulan(DD) - 1, TglNow.ConvertTglBlnThnToTanggal(DD));
       kazaoCalendarDateInputAsset.setCalendar(Tgl);
       AmbilHeaderPenjualan(TransNo, 1);
       AmbilDetailPenjualan(TransNo);
    }
    
    private void SetTanggalDateChooser(){
        /*
         * Tampilan bagian Depreciation Procces
         * Perid yang diset ke bulan dan Tahun
         */
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
        dateChooserCombo1.setDateFormat(dt1); 
    }
    private String AmbilDataPeriodDateChooser(){
        String Tgl1  = dateChooserCombo1.getText();
        String Period2  = N.ConvertTgl_Bln_Thn_To_Tahun_String(Tgl1) + N.ConvertTgl_Bln_Thn_To_Bulan_String(Tgl1) ;
        return Period2;
    }
    private String AmbilDataPeriodKazao(){
        String PeriodRefresh   = kazaoCalendarDatePesanan.getKazaoCalendar().getShortDate();
                 
        SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();
        N.SetKazaoToBlnIndo(PeriodRefresh);
        N.SetKazaoToThnIndo(PeriodRefresh);

        PeriodRefresh = N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
        return PeriodRefresh;
    }

    private void SetTanggalPeriod(){
         
        kazaoCalendarDateInputAsset.setFormat("mm-yyy");

         /*
         * Static JPanel
         * Di Gunakan agar jika ada penambahan componet tidak berubah
         * biar tidak bergerak
         */
        Dimension dimension = new Dimension(10, 30);
        kazaoCalendarDateInputAsset.setMaximumSize(dimension);
        kazaoCalendarDateInputAsset.setMinimumSize(dimension);
        kazaoCalendarDateInputAsset.setPreferredSize(dimension);
     }
    private String AmbilDataPeriod1(){
        String PeriodRefresh   = kazaoCalendarDateInputAsset.getKazaoCalendar().getShortDate();
                 
        SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();
        N.SetKazaoToBlnIndo(PeriodRefresh);
        N.SetKazaoToThnIndo(PeriodRefresh);

        PeriodRefresh = N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
        return PeriodRefresh;
    }
    
    private void PerhitunganTotal(){
        double SubTotal;
        double Dibayar = Satu.BersihDataKeDoubel(ConvertAngka.RupiahKeDoubel(FieldTotalDibayar.getText()));
        double Dp      = Satu.BersihDataKeDoubel(ConvertAngka.RupiahKeDoubel(FieldBayar.getText()));
                
        FieldGrandTotal.setText(ConvertAngka.NilaiRupiah(Dibayar)+"");
        SubTotal    =  Dibayar - Dp;
        FieldSubTotal.setText(ConvertAngka.NilaiRupiah(SubTotal) +"");
        FieldDP.setText(ConvertAngka.NilaiRupiah(Dp )+"");
    }
    
    private void AddMenu(){
        if ( FilterInputMenu() ){
            AmbilDatabasePesanan();
            JButtonPbSave.setEnabled(true);
        }       
    }
    private boolean FilterInputMenu(){
        if (InputMenu.getSelectedItem().equals("") ){
            JOptionPane.showMessageDialog(null, "Data menu kosong");
            return false;
        }
        return true;
    }
    
    private void Buttonn (boolean[] Button){
        JButtonPbSave.setEnabled(Button[0]);
        JButtonPbEdit.setEnabled(Button[1]);
        //JButtonPbDelete.setEnabled(Button[2]);
        JButtonPbNew.setEnabled(Button[2]);
        JButtonPbPrint.setEnabled(Button[3]);
        Add.setEnabled(Button[4]);
        InputMenu.setEnabled(Button[5]);
        JButtonPbPrint.setEnabled(Button[6]);
    }
    private void ResetTampilan( ){
        JTextField[] Field = {FieldJenisPesanan,FieldJenisPesananQty,FieldNoPemesan, FieldNamaPemesan, AlamaPemesan, NoTelpon, FieldNote, FieldDP, FieldSubTotal, FieldGrandTotal, FieldBayar, FieldTotalDibayar};
        JLabel[] Label = {LabelTanggalKirim};       
        Satu.ResetTampilan(Field, Label);
        Satu.HapusDataJTabel(JTablePenjualan);
    }
    private boolean Validasi(){
        JTextField[] DataValidasi   = {FieldNoPemesan, FieldNamaPemesan, FieldJenisPesananQty, FieldJenisPesanan, FieldSubTotal,FieldGrandTotal, FieldTotalDibayar};
        String[] DataError = {"No Transaksi Tidak ada", "Nama Pemesanan Tidak ada", "Qty Pesanan Kosong", "Jenis Pesanan Kosong", "Sub Total Kosong", "Grand Total Kosong", "Total Dibayar Kosong"}; 
        return Satu.ValidasiData(DataValidasi, DataError);
    }
    
    private void AmbilDatabasePesanan(){
        TransNoPesanan  = Satu.AmbilDepan(InputMenu.getSelectedItem().toString(), null);
        NamaPemesan     = Satu.AmbilBelakang(InputMenu.getSelectedItem().toString(), null);
        ResetTampilan();
        FieldNoPemesan.setText(TransNoPesanan);
        FieldNamaPemesan.setText(NamaPemesan);
        
        /*
         * Ambil Lagi Database Header Pesanan
         */
         int baris;       
            ResultSet HQ = null;
            ResultSet HQ2 = null;
               try {
                   Statement stm = K.createStatement();
                       HQ = stm.executeQuery("select  porsi, jenis_pesanan, tgl_dipesan, "
                            + " periode "
                            + " from header_pesanan "
                            + " where trans_no ='" + TransNoPesanan + "'");                                                                          
                   while(HQ.next()  ){
                       CC       = String.valueOf(HQ.getInt("porsi")).toString();
                       DD       = HQ.getString("jenis_pesanan");
                       FF       = HQ.getString("periode");
                       GG       = HQ.getString("tgl_dipesan");
                                             
                       /*
                        * Data dikumpulkan pada Data array ex String[] aaa = {}
                        * di add pada arraylist
                        */
                       LabelTanggalKirim.setText(GG);
                       FieldJenisPesanan.setText(DD);
                       FieldJenisPesananQty.setText(CC);  
                       LabelTanggalKirim1.setText(FF);
                   }
                   
                   Statement stm2 = K.createStatement();
                   HQ2 = stm2.executeQuery("select trans_no, nama_menu, qty "
                         + " from detail_pesanan "
                         + " where trans_no = '" + TransNoPesanan + "'");                              
                   while(HQ2.next()  ){
                       AA       = HQ2.getString("trans_no");
                       BB       = HQ2.getString("nama_menu");
                       CC       = String.valueOf(HQ2.getInt("qty")).toString();
                                             
                       /*
                        * Data dikumpulkan pada Data array ex String[] aaa = {}
                        * di add pada arraylist
                        */
                       String[] add         = {HQ2.getRow()+ "",BB ,CC };
                       Modeltabel2.addRow(add);      
                   }
                   
               }
               catch (Exception ex){
                    JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
               }
    }
    
    /*
     * Pilihan 
     * 0 = Ambil List penjulan
     * Period
     * 1 = dopubel klik penjualan / data Lengkap
     * TransNo
     */
    private void DisabelDoubelKlikTabel(boolean TutupBuka){
                    
        AlamaPemesan.setEditable(TutupBuka);
        NoTelpon.setEditable(TutupBuka);
        FieldNote.setEditable(TutupBuka);
        FieldBayar.setEnabled(TutupBuka);
        FieldTotalDibayar.setEnabled(TutupBuka);
        JButtonPbEdit.setEnabled(TutupBuka);
    }
    
    private void AmbilHeaderPenjualan(String PeriodAtauTransNo, int Pilihan){   
        String LunasTanggalDP = "-";
        int StatusDp, StatusLunas;
        double DpAmount, SubTotal, GrandTotal;
            ResultSet HQ2 = null;
               try {
                   Statement stm2 = K.createStatement();
                   if (Pilihan == 0 ){
                       Satu.HapusDataJTabel(JTabelListPenjualan);
                       HQ2 = stm2.executeQuery("select trans_no, nama, tanggal_transaksi "
                         + " from header_penjualan "
                         + " where period = '" + PeriodAtauTransNo + "'");
                   }
                   else if (Pilihan == 1){
                        HQ2 = stm2.executeQuery("select * "
                         + " from header_penjualan "
                         + " where trans_no = '" + PeriodAtauTransNo + "'");
                   }
                                                 
                   while(HQ2.next()  ){
                       if (Pilihan == 0){
                            AA       = HQ2.getString("trans_no");
                            BB       = HQ2.getString("nama");
                            CC       = HQ2.getString("tanggal_transaksi");
                            
                            String[] add         = {HQ2.getRow()+ "",AA, BB ,CC };
                            Modeltabel3.addRow(add); 
                       }
                       else if (Pilihan == 1){
                           AA       = HQ2.getString("alamat");
                           BB       = HQ2.getString("no_hp");
                           CC       = HQ2.getString("note");
                           EE       = HQ2.getString("tanggal_dikirim");
                           FF       = HQ2.getString("jenis_pesanan");
                           GG       = HQ2.getString("jumlah_dipesan");
                           StatusDp       = HQ2.getInt("dp_status");
                           DpAmount       = HQ2.getDouble("dp_amount");
                           SubTotal       = HQ2.getDouble("bayar");
                           GrandTotal     = HQ2.getDouble("total_dibayar");
                           LunasTanggalDP = HQ2.getString("tanggal_lunas_dp");
                           HH       = HQ2.getString("period");
                           String TglTransaksi = HQ2.getString("tanggal_transaksi");

                           SistemPro.TanggalSistem TglNow = new SistemPro.TanggalSistem(); 
                           Calendar Tgl = Calendar.getInstance();
                           Tgl.set(TglNow.ConvertTglBlnThnToTahun(TglTransaksi), TglNow.ConvertTglBlnThnToBulan(TglTransaksi) - 1, TglNow.ConvertTglBlnThnToTanggal(TglTransaksi));
                           kazaoCalendarDatePesanan.setCalendar(Tgl);  
                           
                           try {
                               Tgl.set(TglNow.ConvertTglBlnThnToTahun(LunasTanggalDP), TglNow.ConvertTglBlnThnToBulan(LunasTanggalDP) - 1, TglNow.ConvertTglBlnThnToTanggal(LunasTanggalDP));
                               dateChooserCombo1.setSelectedDate(Tgl);
                           }
                           catch (Exception X){
                               dateChooserCombo1.setSelectedDate(null);
                           }
                               
                           AlamaPemesan.setText(AA);
                           NoTelpon.setText(BB);
                           FieldNote.setText(CC);
                           LabelTanggalKirim.setText(EE);
                           LabelTanggalKirim1.setText(HH);
                           FieldJenisPesanan.setText(FF);
                           FieldJenisPesananQty.setText(GG);
                           
                           DPtidak.setSelectedIndex(StatusDp);
                           FieldDP.setText(ConvertAngka.NilaiRupiah(DpAmount));
                           FieldSubTotal.setText(ConvertAngka.NilaiRupiah(SubTotal));
                           FieldGrandTotal.setText(ConvertAngka.NilaiRupiah(GrandTotal));
                           
                           FieldTotalDibayar.setText(ConvertAngka.NilaiRupiah(GrandTotal));
                           FieldBayar.setText(ConvertAngka.NilaiRupiah(DpAmount));
                           
                           if (StatusDp == 0){    
                               DisabelDoubelKlikTabel(false);
                               JButtonPbSave.setEnabled(false);  
                               DPtidak.setEnabled(false); 
                               //System.out.println("xxxxxx 0");
                           }
                           else if (StatusDp == 1){    
                               JButtonPbSave.setEnabled(true);
                               DisabelDoubelKlikTabel(false);
                               DPtidak.setEnabled(true); 
                               JButtonPbSave.setEnabled(true); 
                               FieldBayar.setEnabled(false); 
                               //System.out.println("xxxxxx 1");
                           }
                           else{                            
                               DisabelDoubelKlikTabel(false);
                               JButtonPbSave.setEnabled(false);
                               DPtidak.setEnabled(false); 
                               dateChooserCombo1.setEnabled(false);
                                FieldBayar.setEnabled(false); 
                               //System.out.println("xxxxxx 2");
                           }                         
                       }
                   }                   
               }
               catch (Exception ex){
                    JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
               }      
    }
    
    private void AmbilDetailPenjualan(String TransNO){
         int baris;       
            ResultSet HQ2 = null;
               try {
                   Statement stm2 = K.createStatement();
                   HQ2 = stm2.executeQuery("select  nama_menu, qty "
                         + " from detail_penjualan "
                         + " where trans_no = '" + TransNO + "'");   
                   while(HQ2.next()  ){
                       //AA       = HQ2.getString("trans_no");
                       BB       = HQ2.getString("nama_menu");
                       CC       = String.valueOf(HQ2.getInt("qty")).toString();
                                             
                       /*
                        * Data dikumpulkan pada Data array ex String[] aaa = {}
                        * di add pada arraylist
                        */
                       String[] add         = {HQ2.getRow()+ "",BB ,CC };
                       Modeltabel2.addRow(add);      
                   }
               }
               catch (Exception ex){
                    JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
         }
              
    }
    boolean Edit2 = true;
    private void SaveDatabase(){
        AA  = FieldNoPemesan.getText();
         try {
                
                String Alamat, NoHp, Note, QtyPesanan;
                int StatusDp;
                double DpAmount, SubTotal, GrandTotal;
                
                BB  = FieldJenisPesananQty.getText();
                CC  = FieldJenisPesanan.getText();
                DD  = kazaoCalendarDatePesanan.getKazaoCalendar().getShortDate();
                GG  = LabelTanggalKirim.getText();
                EE  = AmbilDataPeriodKazao();
                FF  = FieldNamaPemesan.getText();
                Alamat  = AlamaPemesan.getText();
                NoHp    = NoTelpon.getText();
                Note    = FieldNote.getText();
                QtyPesanan  = FieldJenisPesananQty.getText();
                StatusDp    = DPtidak.getSelectedIndex();
               
                
                DpAmount        =  Satu.BersihDataKeDoubel(ConvertAngka.RupiahKeDoubel(FieldDP.getText()));
                SubTotal        =  Satu.BersihDataKeDoubel(ConvertAngka.RupiahKeDoubel(FieldSubTotal.getText()));
                GrandTotal      =  Satu.BersihDataKeDoubel(ConvertAngka.RupiahKeDoubel(FieldGrandTotal.getText()));
                
                String LunasTanggalDp = "-", LunasPeriod ="-";
                if(DPtidak.getSelectedIndex() == 2){
                    
                    LunasTanggalDp  = dateChooserCombo1.getText();
                    System.out.println("xx" + LunasTanggalDp + "ss");
                    if (LunasTanggalDp.equals("")){
                        JOptionPane.showMessageDialog(null, "Tanggal DP lunas harus diisi");
                        Edit2 = false;
                        
                    }
                    else {
                        Edit2 = true;
                        Satu.DeleteData(AA, "header_penjualan");
                    }
                    LunasPeriod     = AmbilDataPeriodDateChooser();
                }
                
                /*
                 * Convert Tanggal Kazao
                 */
                SistemPro.TanggalSistem KazaoToIndo = new SistemPro.TanggalSistem();
                KazaoToIndo.SetKazaoToTglIndo(DD );
                String UserTgl  = KazaoToIndo.GetTglIndoStrKazao();
                KazaoToIndo.SetKazaoToBlnIndo(DD );
                String UserBln  = KazaoToIndo.GetBlnIndoStrKazao();
                KazaoToIndo.SetKazaoToThnIndo(DD );
                String UserThn = KazaoToIndo.GetThnIndoStKazao();
                String TanggalTransaksi = UserTgl+"-"+UserBln+"-"+UserThn;

                /*
                 * Trans no
                 */                      
                if (Validasi() && Edit2 == true){       
                    
                    /*
                     * Save Header 
                     * Buat data trans no, jenis pesanan, pemesan, jenis pesanan, porsi, period 
                     * AmbilDataPeriodDateChooser
                     */
                    
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    Stm.executeUpdate("INSERT INTO header_penjualan "
                            + "(trans_no, nama, alamat, no_hp, note, "
                            + "tanggal_dikirim, jenis_pesanan, jumlah_dipesan, "
                            + "dp_status, dp_amount, "
                            + "bayar, total_dibayar, "
                            + "updated, tanggal_transaksi, period, tanggal_lunas_dp, period_lunas)"
                            + " VALUES ('"+AA + "','"+ FF + "','"+ Alamat+"','" + NoHp + "','" + Note
                            + "','" + GG + "','" + CC+ "','" + QtyPesanan+ "','" 
                            + StatusDp + "','"+ DpAmount 
                            + "','" + SubTotal + "','" + GrandTotal+ "'"
                            + ",now(),'"+TanggalTransaksi + "','"+ EE +"','" +LunasTanggalDp+ "','"+LunasPeriod+ "') " );   
                    
                    /*
                     * Save detail
                     */
                    Statement Stm2 = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);                   
                    for (int a = 0; a < JTablePenjualan.getRowCount(); a++){
                        Stm2.executeUpdate("insert into detail_penjualan "
                            + "( trans_no, nama_menu, qty) values ('"
                            + AA + "','" + JTablePenjualan.getValueAt(a, 1) 
                            + "','" + JTablePenjualan.getValueAt(a, 2)+"')");       
                    }                              
                }
            }
            catch (Exception Ex){
                if (Ex.toString().contains("PRIMARY")){
                         JOptionPane.showMessageDialog(null, "Datasudah pernah dibuat " + AA );
                 }
                else{
                    System.out.println(Ex);
                    JOptionPane.showMessageDialog(null, Ex); 
                }               
         }
         
         /*
          * Logika jika status lunas atau tidak
          */
         if (DPtidak.getSelectedIndex() == 1){
             JButtonPbEdit.setEnabled(true);
         }
         else {
             JButtonPbEdit.setEnabled(false);
         }
        
    }
    
     private void Tabel2(){
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         */
        String header[] = {"No", "Nama Menu", "Qty"};
        Modeltabel2 = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        //if(colIndex == 8) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTablePenjualan.setModel(Modeltabel2);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel2);
        JTablePenjualan.setRowSorter(sorter);
        
        /*
         * Rata tengah atau kanan table
         */
        JTablePenjualan.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTablePenjualan.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTablePenjualan.getColumnModel().getColumn(2).setCellRenderer( tengah );

        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,250,40};
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTablePenjualan, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        //JTablePenjualan.getColumnModel().getColumn(8).setCellRenderer(  new ButtonJTable("Delete"));
        //JTablePenjualan.getColumnModel().getColumn(8).setCellEditor( new  PesananMakanan.ButtonJTableKeDuaLocal(new JCheckBox(),Modeltabel2, JTablePenjualan));
       
        /*
         * Disable drag colum tabel
         */       
        JTablePenjualan.getTableHeader().setReorderingAllowed(false); 
        
    }
     private void Tabel3(){
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         */
        String header[] = {"No", "Trans No","Nama Pemesan", "Tanggal"};
        Modeltabel3 = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        //if(colIndex == 8) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTabelListPenjualan.setModel(Modeltabel3);

       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel3);
        JTabelListPenjualan.setRowSorter(sorter);
        JTextFieldSearch.addKeyListener(new KeyListener(){
            @Override
            public void keyReleased(KeyEvent e) {
               String text = JTextFieldSearch.getText();
               if (text.length() == 0) {
                 sorter.setRowFilter(null);
               } else {
                 try {
                   sorter.setRowFilter(
                       RowFilter.regexFilter("(?i)"+text));
                       //System.out.println(sorter.getViewRowCount());
                 } catch (PatternSyntaxException pse) {
                   System.err.println("Bad regex pattern");
                 }
               }
            }     
            @Override
            public void keyTyped(KeyEvent e) {
                }
           @Override
           public void keyPressed(KeyEvent e) {
            }
        });
        
        /*
         * Rata tengah atau kanan table
         */
        JTabelListPenjualan.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelListPenjualan.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTabelListPenjualan.getColumnModel().getColumn(2).setCellRenderer( tengah );
        JTabelListPenjualan.getColumnModel().getColumn(3).setCellRenderer( tengah ); 

        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {30,80,150,80};
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTabelListPenjualan, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        //JTablePenjualan.getColumnModel().getColumn(8).setCellRenderer(  new ButtonJTable("Delete"));
        //JTablePenjualan.getColumnModel().getColumn(8).setCellEditor( new  PesananMakanan.ButtonJTableKeDuaLocal(new JCheckBox(),Modeltabel2, JTablePenjualan));
       
        /*
         * Disable drag colum tabel
         */       
        JTablePenjualan.getTableHeader().setReorderingAllowed(false); 
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox2 = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        JTablePenjualan = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        InputMenu = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        Add = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        FieldNoPemesan = new javax.swing.JTextField();
        FieldNamaPemesan = new javax.swing.JTextField();
        AlamaPemesan = new javax.swing.JTextField();
        NoTelpon = new javax.swing.JTextField();
        FieldNote = new javax.swing.JTextField();
        kazaoCalendarDatePesanan = new org.kazao.calendar.KazaoCalendarDate();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        FieldJenisPesanan = new javax.swing.JTextField();
        LabelTanggalKirim = new javax.swing.JLabel();
        FieldJenisPesananQty = new javax.swing.JTextField();
        LabelTanggalKirim1 = new javax.swing.JLabel();
        JPanelButtonSave = new javax.swing.JPanel();
        JButtonPbSave = new javax.swing.JButton();
        JButtonPbEdit = new javax.swing.JButton();
        JButtonPbDelete = new javax.swing.JButton();
        JButtonPbNew = new javax.swing.JButton();
        JButtonPbPrint = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        JTextFieldSearch = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        JButtonRefresh = new javax.swing.JButton();
        kazaoCalendarDateInputAsset = new org.kazao.calendar.KazaoCalendarDate();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        JTabelListPenjualan = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        DPtidak = new javax.swing.JComboBox();
        FieldBayar = new javax.swing.JTextField();
        FieldTotalDibayar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        FieldDP = new javax.swing.JTextField();
        FieldSubTotal = new javax.swing.JTextField();
        FieldGrandTotal = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        JTablePenjualan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTablePenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(JTablePenjualan);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        InputMenu.setEditable(true);
        InputMenu.setModel(new javax.swing.DefaultComboBoxModel(app_search_data_pemesanan_penjualan.getData().toArray()));
        InputMenu.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("Pesanan Makanan");

        Add.setText("Add");
        Add.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(InputMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Add)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InputMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(Add)))
        );

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("No Pesanan");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setText("Nama Pemesan");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText("Alamat Pemesan");

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel7.setText("No Hp / Tlpn");

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel8.setText("Note");

        FieldNoPemesan.setEditable(false);

        FieldNamaPemesan.setEditable(false);

        AlamaPemesan.setEnabled(false);

        NoTelpon.setEnabled(false);

        FieldNote.setEnabled(false);

        jLabel14.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel14.setText("Tanggal Transaksi");

        jLabel15.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel15.setText("Tanggal Kirim");

        jLabel17.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel17.setText("Jenis Pesanan");

        FieldJenisPesanan.setEditable(false);
        FieldJenisPesanan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        LabelTanggalKirim.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        FieldJenisPesananQty.setEditable(false);
        FieldJenisPesananQty.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        FieldJenisPesananQty.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        LabelTanggalKirim1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FieldNamaPemesan)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(AlamaPemesan))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(FieldNoPemesan, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(kazaoCalendarDatePesanan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 152, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(FieldJenisPesananQty, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(FieldJenisPesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(LabelTanggalKirim, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(LabelTanggalKirim1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 149, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FieldNote)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(NoTelpon, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(FieldNoPemesan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(FieldJenisPesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FieldJenisPesananQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(kazaoCalendarDatePesanan, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(LabelTanggalKirim, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(LabelTanggalKirim1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(FieldNamaPemesan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(AlamaPemesan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(NoTelpon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(FieldNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        JButtonPbNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonPbNew.setText("New");

        JButtonPbPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPbPrint.setText("Print");
        JButtonPbPrint.setEnabled(false);

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
                .addGap(5, 5, 5)
                .addComponent(JButtonPbNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbPrint)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPanelButtonSaveLayout.setVerticalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(JButtonPbSave)
                .addComponent(JButtonPbEdit)
                .addComponent(JButtonPbDelete)
                .addComponent(JButtonPbNew)
                .addComponent(JButtonPbPrint))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(JPanelButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        jLabel18.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel18.setText("Search data  :");

        JButtonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Refresh.png"))); // NOI18N
        JButtonRefresh.setText("Refresh");

        kazaoCalendarDateInputAsset.setFormat("mm/yyyy");

        jLabel19.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel19.setText("Periode  :");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kazaoCalendarDateInputAsset, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonRefresh)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(kazaoCalendarDateInputAsset, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19))
                    .addComponent(JButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        JTabelListPenjualan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTabelListPenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(JTabelListPenjualan);

        DPtidak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tunai", "Pembayaran DP", "DP Lunas" }));
        DPtidak.setEnabled(false);

        FieldBayar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        FieldBayar.setText("0");
        FieldBayar.setEnabled(false);

        FieldTotalDibayar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        FieldTotalDibayar.setEnabled(false);

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel9.setText("Status");

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel10.setText("Pembayaran DP ");

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel11.setText("Total Di Bayar");

        jLabel16.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel16.setText("Tgl Dp Lunas");

        dateChooserCombo1.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    dateChooserCombo1.setEnabled(false);

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(FieldTotalDibayar, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(FieldBayar, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(DPtidak, javax.swing.GroupLayout.Alignment.TRAILING, 0, 193, Short.MAX_VALUE)
                .addComponent(dateChooserCombo1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
            .addGap(5, 5, 5)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(DPtidak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel9))
            .addGap(8, 8, 8)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel16)
                .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(FieldBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel10))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(FieldTotalDibayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel11)))
    );

    jLabel12.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabel12.setText("DP");

    jLabel13.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabel13.setText("Kekurangan DP");

    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabel4.setText("Grand Total");

    FieldDP.setEditable(false);
    FieldDP.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    FieldDP.setText("0");
    FieldDP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

    FieldSubTotal.setEditable(false);
    FieldSubTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    FieldSubTotal.setText("0");
    FieldSubTotal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

    FieldGrandTotal.setEditable(false);
    FieldGrandTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    FieldGrandTotal.setText("0");
    FieldGrandTotal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(FieldDP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                .addComponent(FieldGrandTotal)
                .addComponent(FieldSubTotal, javax.swing.GroupLayout.Alignment.TRAILING)))
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
            .addGap(0, 8, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel12)
                .addComponent(FieldDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel13)
                .addComponent(FieldSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(FieldGrandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
    );

    jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

    jLabel1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel1.setForeground(new java.awt.Color(0, 51, 255));
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Data Transaksi Penjualan");

    javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    jPanel7Layout.setVerticalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
        .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add;
    private javax.swing.JTextField AlamaPemesan;
    private javax.swing.JComboBox DPtidak;
    private javax.swing.JTextField FieldBayar;
    private javax.swing.JTextField FieldDP;
    private javax.swing.JTextField FieldGrandTotal;
    private javax.swing.JTextField FieldJenisPesanan;
    private javax.swing.JTextField FieldJenisPesananQty;
    private javax.swing.JTextField FieldNamaPemesan;
    private javax.swing.JTextField FieldNoPemesan;
    private javax.swing.JTextField FieldNote;
    private javax.swing.JTextField FieldSubTotal;
    private javax.swing.JTextField FieldTotalDibayar;
    private javax.swing.JComboBox InputMenu;
    private javax.swing.JButton JButtonPbDelete;
    private javax.swing.JButton JButtonPbEdit;
    private javax.swing.JButton JButtonPbNew;
    private javax.swing.JButton JButtonPbPrint;
    private javax.swing.JButton JButtonPbSave;
    private javax.swing.JButton JButtonRefresh;
    private javax.swing.JPanel JPanelButtonSave;
    private javax.swing.JTable JTabelListPenjualan;
    private javax.swing.JTable JTablePenjualan;
    private javax.swing.JTextField JTextFieldSearch;
    private javax.swing.JLabel LabelTanggalKirim;
    private javax.swing.JLabel LabelTanggalKirim1;
    private javax.swing.JTextField NoTelpon;
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private javax.swing.JComboBox jComboBox2;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDateInputAsset;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDatePesanan;
    // End of variables declaration//GEN-END:variables
}
