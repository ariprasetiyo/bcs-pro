/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.SatuUntukSemua;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
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
public class LaporanLabaRugi extends javax.swing.JInternalFrame {

    /**
     * Creates new form LaporanLabaRugi
     */
    DefaultTableModel   Modeltabel  = new DefaultTableModel();
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();
    private SistemPro.TanggalSistem N       = new SistemPro.TanggalSistem();
    private SistemPro.KoneksiDatabase KD    = new SistemPro.KoneksiDatabase();
    private Connection              K       = KD.createConnection();
    private SistemPro.KomaToString ConvertAngka = new SistemPro.KomaToString();
    private  SistemPro.ComponentHanyaAngka HanyaAngka = new SistemPro.ComponentHanyaAngka();
    String TransNo, ChartNo, ChartName, NoteString, Tanggal, Period, PeriodRefresh, Semua, Note, DPStatusKet, PeriodLunas, PeriodTrans;
    double Amount, Amount2, Amount3, Amount4, Amount5;
    int DPStatus , PeriodDBLunas, PeriodDBTrans;
    SatuUntukSemua Satu = new SatuUntukSemua();

    public LaporanLabaRugi() {
        initComponents();
        Tabel1();
        AmbilDatabaseJournalTransaksi(AmbilDataPeriodSistem());
        Aksi();
    }
    private void Aksi(){
        
        HanyaAngka.SetAntiAngka(PajakPenjualan);
        
        JButtonRefresh.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent X){
                Satu.HapusDataJTabel(JTabelLabaRugi);
                AmbilDatabaseJournalTransaksi(AmbilDataPeriod1());
            }
        });
        
        JButtonPbPrint.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent X){
                JasperPrint();
            }
        });
        
        PajakPenjualan.addKeyListener(new KeyListener(){
            public void keyReleased(KeyEvent e) {
                 LabaBersih();
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
    
    private void JasperPrint(){
        
        int jumlahRow = JTabelLabaRugi.getRowCount();
        for (int a = 0 ; a < jumlahRow ; a++){
             Semua = (String )JTabelLabaRugi.getValueAt(a, 3);
             if (Semua.contains("<html>")){
                 Semua = Semua.replaceAll("<html><h3>", "");
                 Semua = Semua.replaceAll("</html></h3>", "");
                 Semua = Semua.replaceAll("<font  color='red'>", "");
                 Semua = Semua.replaceAll("</font>", "");
                 
                         JTabelLabaRugi.setValueAt(Semua, a, 3);
             }
             /*
             Semua = (String )JTabelLabaRugi.getValueAt(a, 4);
             if (Semua.contains("0") || Semua.equals("")){
                 Semua = Semua.replaceAll("0", "");
                         JTabelLabaRugi.setValueAt(Semua, a, 4);
             }
             */
             Semua = (String )JTabelLabaRugi.getValueAt(a, 5);
             if (Semua == null ){
                         JTabelLabaRugi.setValueAt(" ", a, 5);
             }
        }
        
        DefaultTableModel de = (DefaultTableModel)JTabelLabaRugi.getModel();

        JRTableModelDataSource dataSource = new JRTableModelDataSource(de);

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();
        
        hashMap.put("penjualan", TotalPenjualan.getText());
        hashMap.put("TotalBiaya", TotalBiaya.getText());
        hashMap.put("LabaBersih",LabaBersihSebelumPajak.getText());
        hashMap.put("Pajak", PajakPenjualan.getText());
        hashMap.put("TotalLabaBersih", LabaBersih.getText());
             
        try {
            String x    = System.getProperty("user.dir")+"\\ReportJasper\\ReportLaporanLabaRugi.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }  
    }
    
    private String AmbilDataPeriod1(){
        PeriodRefresh   = kazaoCalendarDateInputAsset.getKazaoCalendar().getShortDate();
                 
        SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();
        N.SetKazaoToBlnIndo(PeriodRefresh);
        N.SetKazaoToThnIndo(PeriodRefresh);

        PeriodRefresh = N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
        return PeriodRefresh;
    }
        
    
    private String AmbilDataPeriodSistem(){
       N.SetTahunSis();
       N.SetBulanSis();
       
       int Bln = N.GetBulanSis();
       String bln ;

       if (Bln  < 10 ){
            bln = "0"+Bln;
       }
       else {
           bln = Bln+"";
       }
        
       return (N.GetTahunSis() + "" + bln );
    }
    
    private void LabaBersih(){
         /*
         * Laba bersih@
         */
        double LabaBersihPerBulan =  Satu.BersihDataKeDoubel(ConvertAngka.RupiahKeDoubel(LabaBersihSebelumPajak.getText())) - Satu.BersihDataKeDoubel(ConvertAngka.RupiahKeDoubel(PajakPenjualan.getText() ) );
        LabaBersih.setText(ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(LabaBersihPerBulan)));
    }
    
    private void TotalJournal(){
        TotalBiaya.setText( ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Amount3))) ;
        TotalPenjualan.setText( ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Amount2))) ;
        
        /*
         * Laba bersih sebelum Pajak
         */
        double LabaBersiSblmPajak = Satu.BersihDataKeDoubel(ConvertAngka.FormatDesimalRubahE9(Amount2 ))
                - Satu.BersihDataKeDoubel(ConvertAngka.FormatDesimalRubahE9(Amount3)) ;
        LabaBersihSebelumPajak.setText(ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(LabaBersiSblmPajak ))+"");
        
    }
    String AA;
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
               AA = ConvertAngka.RupiahKeDoubel(String.valueOf(Modeltabel.getValueAt(ab, AmbilKolomBerapa).toString()));
               Tot = Double.valueOf(AA).doubleValue() + Tot; 
            }
            catch (Exception x){
                JOptionPane.showMessageDialog(null, "Error Input Data");
                ModelTabel.setValueAt("0", ab, AmbilKolomBerapa);
            }
            
        }
        return Tot;
    }
    
    private void AmbilDatabaseJournalTransaksi(String PeriodAtauTransNo){  
        
            ResultSet HQ = null;
            ResultSet HQ3 = null;
            ResultSet HQ4 = null;
            ResultSet HQ5 = null;
            ResultSet HQ2 = null;
            ResultSet HQ6 = null;
            
            
               try {
                   /*
                   Statement stm2 = K.createStatement();
                    HQ2 = stm2.executeQuery("select "
                            + "sum( if (dp_status = 1, dp_amount, total_dibayar ) ) "
                            + " GrandTotal, period "
                      + " from header_penjualan "
                      + " where period = '" + PeriodAtauTransNo + "' and period_lunas = '"+ PeriodAtauTransNo+"'");
                    while(HQ2.next()  ){                        
                        Amount          = HQ2.getDouble("GrandTotal"); 
                   }  
                    FieldPenjualan.setText(ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Amount)));
                    
                     * Penpatan penjualan
                     */
                    Statement stm6 = K.createStatement();
                    HQ6 = stm6.executeQuery("select trans_no, ( "
                        + " case "
                        + "  when (dp_status = 1  ) then dp_amount "
                        + "  when (dp_status = 0 ) then  total_dibayar  "
                        + "  when (dp_status = 2 and period_lunas = period  ) then total_dibayar "
                        + "  when (dp_status = 2 and period =  '"+PeriodAtauTransNo+"'  ) then dp_amount"
                        + "  when (dp_status = 2 and period_lunas > period ) then bayar"
                        + "   else 0 "
                        + "  end  )"
                        + "  GrandTotal , nama , note, dp_status , period_lunas, period"
                        + "        from header_penjualan "
                        + " where "
                        + " ( if ( period_lunas  = '"+PeriodAtauTransNo+"',  period_lunas = '"+PeriodAtauTransNo+"', period =  '"+PeriodAtauTransNo+"' ) )" );
                    
                    String[] add6         = {"-","", "" ,"<html><h3>Penjualan</html></h3>",""};
                    Modeltabel.addRow(add6);
                    
                    Amount = 0;
                    while(HQ6.next()  ){ 
                        
                        Amount2         = HQ6.getDouble("GrandTotal");
                        Semua           = HQ6.getString("nama");
                        TransNo         = HQ6.getString("trans_no");
                        Note            = HQ6.getString("note");
                        DPStatus        = HQ6.getInt("dp_status");
                        PeriodLunas     = HQ6.getString("period_lunas");
                        PeriodTrans     = HQ6.getString("period");
                        
                        /*
                         * Periode Pelunasan
                         */
                        if (PeriodLunas == null ){
                            
                        }
                        else if (PeriodLunas.equals("")){
                            
                        }
                        else if (!PeriodLunas.equals("")){
                            
                        }
                        else if (!PeriodLunas.equalsIgnoreCase("-") ){
                             PeriodDBLunas    = Integer.valueOf(PeriodLunas).intValue();
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "error : 121993690a");
                        }
                        
                        /*
                         * Periode Transaksi
                         */        
                       
                        if (PeriodTrans == null ){
                            
                        }
                        else if (!PeriodLunas.equals("")){
                            
                        }
                        else if (PeriodTrans.equals("")){
                            
                        }
                        else if (!PeriodTrans.equalsIgnoreCase("-") ){
                             PeriodDBTrans    = Integer.valueOf(PeriodTrans).intValue();
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "error : 121993690b");
                        }
                        
                        if (DPStatus == 0){
                            DPStatusKet = "Lunas";
                        }
                        else if (DPStatus == 1 ){
                            DPStatusKet = "Pembayaran Dp";
                        }
                         else if (( DPStatus == 2 ) && ( PeriodDBLunas == PeriodDBTrans ) ){
                            DPStatusKet = "Pembayaran pelunasan DP";
                        }
                        else if ((DPStatus == 2  )&& ( PeriodDBLunas > PeriodDBTrans ) ){
                            DPStatusKet = "DP pembayaran yang sudah lunas";
                        }
                         System.out.println(PeriodTrans + " " + PeriodLunas);
                         
                        /*
                         * Jika note  tidak kosong
                         */
                         
                        if (!Note.equals("")){
                            String[] add         = {"-",TransNo, "" ,"          "+"Pendapatan dari "+Semua, ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Amount2)),DPStatusKet + ", "+ Note};
                            Modeltabel.addRow(add); 
                        }
                        else{
                            String[] add         = {"-",TransNo, "" ,"          "+"Pendapatan dari "+Semua, ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Amount2)),DPStatusKet};
                            Modeltabel.addRow(add);
                        }
                        
                        Amount = Amount2 + Amount;
                        
                   } 
                    Amount2 = Amount;
                    String[] add7         = {"-","", "" ,"<html><h3><font  color='red'>Total Penjualan</font></html></h3>",ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Amount))};
                    Modeltabel.addRow(add7);
                    
                    /*
                     * Biaya - biaya
                     */
                    Amount = 0;
                    Amount3 = 0;
                    Statement stm = K.createStatement();
                    HQ = stm.executeQuery("select sum( total_rill )  TotalFoodCost, periode "
                      + " from header_penerimaan "
                      + " where periode = '" + PeriodAtauTransNo + "'");
                    String[] add2         = {"-","", "" ,"<html><h3>Biaya Biaya</html></h3>",""};
                    Modeltabel.addRow(add2);
                    while(HQ.next()  ){ 
                        
                        Amount          = HQ.getDouble("TotalFoodCost");
                        String[] add         = {"-","", "" ,"     Biaya Bahan Baku / Food Cost ", ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Amount))};
                        Modeltabel.addRow(add); 
                        Amount3 = Amount3 + Amount;
                   }                  
                    
                    /*
                     * Biaya Tenaga Kerja
                     */
                    Statement stm3 = K.createStatement();
                    HQ3 = stm3.executeQuery("select trans_no, chart_no, chart_name, amount, tanggal, note, period "
                      + " from header_journal_transaksi "
                      + " where period = '" + PeriodAtauTransNo + "' and SUBSTRING(chart_no, 1,2) = '" + 81+ "'");
                    
                   String[] add3         = {HQ3.getRow()+ "","", "" ,"<html><h3>     Biaya Tenaga Kerja</html></h3>",""};
                   Modeltabel.addRow(add3);
                   while(HQ3.next()  ){
                        TransNo         = HQ3.getString("trans_no");
                        ChartNo         = HQ3.getString("chart_no");
                        ChartName       = HQ3.getString("chart_name");
                        Amount          = HQ3.getDouble("amount");
                        NoteString      = HQ3.getString("note");
                        Tanggal         = HQ3.getString("tanggal");                        
                        
                        String[] add         = {HQ3.getRow()+ "",TransNo, ChartNo ,"        " +ChartName, ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Amount)), NoteString, Tanggal};
                        Modeltabel.addRow(add);
                        Amount3 = Amount3 + Amount;
                   }
                   
                   Statement stm4 = K.createStatement();
                    HQ4 = stm4.executeQuery("select trans_no, chart_no, chart_name, amount, tanggal, note, period "
                      + " from header_journal_transaksi "
                      + " where period = '" + PeriodAtauTransNo + "' and SUBSTRING(chart_no, 1,2) = '" + 82+ "'");
                    
                   String[] add4         = {HQ4.getRow()+ "","", "" ,"<html><h3>     Biaya Operational</html></h3>",""};
                   Modeltabel.addRow(add4);
                   while(HQ4.next()  ){
                        TransNo         = HQ4.getString("trans_no");
                        ChartNo         = HQ4.getString("chart_no");
                        ChartName       = HQ4.getString("chart_name");
                        Amount          = HQ4.getDouble("amount");
                        NoteString      = HQ4.getString("note");
                        Tanggal         = HQ4.getString("tanggal");                        
                        
                        String[] add         = {HQ4.getRow()+ "",TransNo, ChartNo ,"          " +ChartName, ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Amount)), NoteString, Tanggal};
                        Modeltabel.addRow(add);
                        Amount3 = Amount3 + Amount;
                   }
                   
                   Statement stm5 = K.createStatement();
                    HQ5 = stm5.executeQuery("select trans_no, chart_no, chart_name, amount, tanggal, note, period "
                      + " from header_journal_transaksi "
                      + " where period = '" + PeriodAtauTransNo + "' and SUBSTRING(chart_no, 1,2) = '" + 83+ "'");
                    
                   String[] add5         = {HQ5.getRow()+ "","", "" ,"<html><h3>     Biaya OverHead</html></h3>","" };
                   Modeltabel.addRow(add5);
                   while(HQ5.next()  ){
                        TransNo         = HQ5.getString("trans_no");
                        ChartNo         = HQ5.getString("chart_no");
                        ChartName       = HQ5.getString("chart_name");
                        Amount          = HQ5.getDouble("amount");
                        NoteString      = HQ5.getString("note");
                        Tanggal         = HQ5.getString("tanggal");                        
                        
                        String[] add         = {HQ5.getRow()+ "",TransNo, ChartNo ,"          " +ChartName, ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Amount)), NoteString, Tanggal};
                        Modeltabel.addRow(add);
                        Amount3 = Amount3 + Amount;
                   }
                    String[] add8         = {"-","", "" ,"<html><h3><font  color='red'>Total Biaya</font></html></h3>",ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Amount3))};
                    Modeltabel.addRow(add8);
               }
               catch (Exception ex){
                    JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
               }  
               
               TotalJournal();
    }
    private void Tabel1(){
        
        String header[] = {"No","Trans No", "Chart No", "Chart Name", "Amount", "Keterangan", "Tanggal"};
        Modeltabel = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        //if(colIndex == 3) {return true ;} 
                        return false;   //Disallow the editing of any cellL
                 }
        };
        JTabelLabaRugi.setModel(Modeltabel);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel);
        JTabelLabaRugi.setRowSorter(sorter);
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
        JTabelLabaRugi.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelLabaRugi.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTabelLabaRugi.getColumnModel().getColumn(2).setCellRenderer( tengah );
        JTabelLabaRugi.getColumnModel().getColumn(4).setCellRenderer( kanan ); 
        JTabelLabaRugi.getColumnModel().getColumn(5).setCellRenderer( tengah ); 
        JTabelLabaRugi.getColumnModel().getColumn(6).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,100,100,200,100, 300,100 };
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTabelLabaRugi, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        //JTabelLabaRugi.getColumnModel().getColumn(3).setCellRenderer(  new ButtonJTable("Delete"));
        //JTabelLabaRugi.getColumnModel().getColumn(3).setCellEditor( new  ButtonJTableKeDua(new JCheckBox(),Modeltabel, JTabelLabaRugi));
       
        /*
         * Disable drag colum tabel
         */       
        JTabelLabaRugi.getTableHeader().setReorderingAllowed(false); 
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        JTextFieldSearch = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        JButtonRefresh = new javax.swing.JButton();
        kazaoCalendarDateInputAsset = new org.kazao.calendar.KazaoCalendarDate();
        jLabel19 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelLabaRugi = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        PajakPenjualan = new javax.swing.JTextField();
        LabaBersihSebelumPajak = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        LabaBersih = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        TotalBiaya = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        JButtonPbPrint = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        TotalPenjualan = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Laporan");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/logo.png"))); // NOI18N

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
                .addContainerGap(16, Short.MAX_VALUE)
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

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Laporan Laba Rugi");

        JTabelLabaRugi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(JTabelLabaRugi);

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setText("Laba Bersih Sebelum Pajak");

        jLabel3.setText("Pajak Penjualan");

        PajakPenjualan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        PajakPenjualan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        LabaBersihSebelumPajak.setEditable(false);
        LabaBersihSebelumPajak.setBackground(new java.awt.Color(153, 153, 153));
        LabaBersihSebelumPajak.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        LabaBersihSebelumPajak.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        LabaBersihSebelumPajak.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setText("Laba Bersih @Bulan");

        LabaBersih.setEditable(false);
        LabaBersih.setBackground(new java.awt.Color(153, 153, 153));
        LabaBersih.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        LabaBersih.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        LabaBersih.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel10.setText(":");

        jLabel9.setText(":");

        jLabel11.setText(":");

        jLabel12.setText(":");

        TotalBiaya.setEditable(false);
        TotalBiaya.setBackground(new java.awt.Color(153, 153, 153));
        TotalBiaya.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        TotalBiaya.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalBiaya.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setText("Total Biaya");

        JButtonPbPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPbPrint.setText("Print");

        jLabel7.setText("Total Penjualan");

        TotalPenjualan.setEditable(false);
        TotalPenjualan.setBackground(new java.awt.Color(153, 153, 153));
        TotalPenjualan.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        TotalPenjualan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalPenjualan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel13.setText(":");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JButtonPbPrint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(LabaBersihSebelumPajak, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                    .addComponent(PajakPenjualan)
                    .addComponent(LabaBersih)
                    .addComponent(TotalBiaya, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TotalPenjualan))
                .addGap(11, 11, 11))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TotalPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TotalBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(JButtonPbPrint)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(LabaBersihSebelumPajak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(PajakPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(LabaBersih, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)))
                .addGap(4, 4, 4))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Laporan Laba Rugi", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonPbPrint;
    private javax.swing.JButton JButtonRefresh;
    private javax.swing.JTable JTabelLabaRugi;
    private javax.swing.JTextField JTextFieldSearch;
    private javax.swing.JTextField LabaBersih;
    private javax.swing.JTextField LabaBersihSebelumPajak;
    private javax.swing.JTextField PajakPenjualan;
    private javax.swing.JTextField TotalBiaya;
    private javax.swing.JTextField TotalPenjualan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDateInputAsset;
    // End of variables declaration//GEN-END:variables
}
