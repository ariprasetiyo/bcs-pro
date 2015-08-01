/*
 * To change this template, choose Tools | Templates
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.ButtonJTableKeDua;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.app_search1;
import SistemPro.app_search_data_rencana_inventaris_alat;
import SistemPro.app_search_data_rencana_inventaris_alat_master;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
 * @author admin
 */
public class RencanaInventarisAlat extends javax.swing.JInternalFrame {
    private SistemPro.TanggalSistem N       = new SistemPro.TanggalSistem();
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();
    private DefaultTableModel   Modeltabel2 = new DefaultTableModel(); 
    private DefaultTableModel   Modeltabel  = new DefaultTableModel();
    private SistemPro.KoneksiDatabase KD    = new SistemPro.KoneksiDatabase();
    private Connection              K       = KD.createConnection();
    private String AA,BB,CC;
    private boolean JTabelEditPenerimaan = false,EditBoleh = false;

    /**
     * Creates new form DaftarInventarisAlat
     */
    public RencanaInventarisAlat() {
        initComponents();
        Tanggal();
        Aksi();
        Tabel1();
        Tabel2();
        Periode();
        AmbilDataDariDatabase(DatePo, DatePo, 0);
    }
    private void Aksi(){
        
        InpuDataProduksi.setSelectedIndex(-1);
        final JTextField JTextFieldItem = (JTextField)InpuDataProduksi.getEditor().getEditorComponent();
        JTextFieldItem.setText("");
        JTextFieldItem.addKeyListener(new app_search1(InpuDataProduksi));
        
        InpuDataProduksi.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                AmbilDataProduksi(InpuDataProduksi.getSelectedItem().toString());
            }
        });
        
        InputAlat.setSelectedIndex(-1);
        final JTextField JTextFieldItem2 = (JTextField)InputAlat.getEditor().getEditorComponent();
        JTextFieldItem2.setText("");
        JTextFieldItem2.addKeyListener(new app_search1(InputAlat));
        
        AddInventaris.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                AddMenu();
                InputAlat.setSelectedItem("");
                InputDataQty.setText("");
            }  
        });
        
        JButtonPbNew.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JLabelKet.setText("");
                TransNoIA.setText("");
                EditBoleh = true;
                Reset(true);
            }  
        });
        
        JButtonPbSave.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                TabelResepSaveDatabase();
                Reset(false);
                EditBoleh = false;
            }  
        });
        
        JButtonPbEdit.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JTabelEditPenerimaan = true;
                InputAlat.setEnabled(true);
                InputDataQty.setEnabled(true);
                AddInventaris.setEnabled(true);
                JButtonPbEdit.setEnabled(false);
                JButtonPbSave.setEnabled(true);
                EditBoleh = true;
            }  
        });
        
        JTableDataDataProduksi.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()>=2){
                     Reset(false);
                     AA = ( String ) JTableDataDataProduksi.getValueAt( JTableDataDataProduksi.getSelectedRow(), 1);
                     AmbilDataView(AA);
                     EditBoleh = false;                    
                }
            }   
        });
        
        JButtonPbPrint.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                PrintJasperReportPesanan();
            }  
        });
        JButtonRefresh.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                SearhByPeriod();
            }  
        });
    }
    
    private void PrintJasperReportPesanan(){
        DefaultTableModel de = (DefaultTableModel)JTableData.getModel();

        JRTableModelDataSource dataSource = new JRTableModelDataSource(de);

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();
        
        String TransNo, NamaProduksi, Tanggal2;
        Periode();
        
        TransNo     = TransNoIA.getText();
        NamaProduksi=NamaPro.getText();
        Tanggal2    = Tanggal.getText();
        Tanggal2    = Tanggal2.replaceAll("\\s.*", "");
        
        hashMap.put("NoTransaksi", TransNo);
        hashMap.put("Nama", NamaProduksi);
        hashMap.put("TanggalBuat",Tanggal2);
        //hashMap.put("TanggalBuat", DateComplit);
             
        try {
            String x    = System.getProperty("user.dir")+"\\ReportJasper\\ReportInventarisAlat.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }  
    }
    
    private void AmbilDataView(String DataTransNo){
        TransNoIA.setText(DataTransNo);
        NamaPro.setText((String )JTableDataDataProduksi.getValueAt( JTableDataDataProduksi.getSelectedRow(), 2));
        Tanggal.setText((String )JTableDataDataProduksi.getValueAt( JTableDataDataProduksi.getSelectedRow(),3));
               
        AmbilDataViewDetail(DataTransNo);
    }
    
    private void AmbilDataViewDetail(String TransNo){
        
         HapusDataJTabel(JTableData);     
            ResultSet HQ = null;
               try {
                    Statement stm = K.createStatement();
                    HQ = stm.executeQuery("select trans_no, nama_inventaris, qty "
                         + " from detail_rencana_inventaris "
                         + " where trans_no = '" + TransNo + "'");                              
                   while(HQ.next()  ){
                       AA       = HQ.getString("trans_no");
                       BB       = HQ.getString("nama_inventaris");
                       CC       = String.valueOf(HQ.getInt("qty")).toString();
                                             
                       /*
                        * Data dikumpulkan pada Data array ex String[] aaa = {}
                        * di add pada arraylist
                        */
                       String[] add         = {HQ.getRow()+ "",BB ,CC };
                       Modeltabel2.addRow(add);      
                   }
                   JButtonPbEdit.setEnabled(true);
                   
               }
               catch (Exception ex){
                    JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
               }
    }
    
    private void Tanggal(){
        /*
         * Tampilan bagian Depreciation Procces
         * Perid yang diset ke bulan dan Tahun
         */
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
        dateChooserCombo2.setDateFormat(dt1);
        dateChooserCombo1.setDateFormat(dt1);        
    }
    /*
     * Ambil data period dari DD_MM_YYYY menjadi YYYY-MM-DD
     * Ke String
     */
    private String AmbilDataPeriod1(){
        String Tgl1  = dateChooserCombo1.getText();
        String Period1  = N.ConvertTgl_Bln_Thn_To_Tahun_String(Tgl1) + "-" + N.ConvertTgl_Bln_Thn_To_Bulan_String(Tgl1) 
                    + "-"+N.ConvertTgl_Bln_Thn_To_Tanggal_String(Tgl1);
        return Period1;
    }
    private String AmbilDataPeriod2(){
        String Tgl1  = dateChooserCombo2.getText();
        String Period2  = N.ConvertTgl_Bln_Thn_To_Tahun_String(Tgl1) + "-" + N.ConvertTgl_Bln_Thn_To_Bulan_String(Tgl1) 
                    + "-"+N.ConvertTgl_Bln_Thn_To_Tanggal_String(Tgl1);
        return Period2;
    }
    private void SearhByPeriod(){
            AmbilDataDariDatabase(AmbilDataPeriod1(), AmbilDataPeriod2(), 1);
            JButtonPbEdit.setEnabled(false);
    }
    String DatePo;
    String DateComplit;
    private void Periode(){
        /*
        * Ambil tanggal dari kzao kalender
        * buat tabel di database harus ada period ( 201301) dan key_no
        */
        SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();

        N.SetTahunSis();
        N.SetBulanSis();
        N.SetTanggalSis();
        this.DateComplit    = N.GetTanggalSisString() + "-"+N.GetBulanSisString() + "-"+N.GetTahunSisString();
        this.DatePo         = N.GetTahunSisString()+"-" +N.GetBulanSisString();
    }
    
    /*
     * Jika a = 1, maka ambil data where berdasarkan YYYY-MM-DD pada tabel created_date
     * jika a = 0, maka ambil data where berdasarkan YYYY-MM pada tabel period
     */
    private void AmbilDataDariDatabase(String Periode1, String Periode2, int a){
         HapusDataJTabel(JTableDataDataProduksi);
         int baris;       
            ResultSet HQ = null;
               try {
                   Statement stm = K.createStatement();
                   if (a == 1){
                       HQ = stm.executeQuery("select trans_no, nama_produksi, "
                            + " tanggal  "
                            + " from header_rencana_inventaris "
                            + " where tanggal between '" + Periode1 + "' and '" + Periode2 + "'"); 
                         
                   }
                   else if (a == 0){
                       System.out.println(Periode1);
                       HQ = stm.executeQuery("select trans_no, nama_produksi, "
                            + " tanggal "
                            + " from header_rencana_inventaris "
                            + " where tanggal like '%" + Periode1 + "%'");                              
                   }
                   else {
                       JOptionPane.showMessageDialog(null, "Error 97862927 2");
                   }
                                             
                   while(HQ.next()  ){
                       AA       = HQ.getString("trans_no");
                       BB       = HQ.getString("nama_produksi");
                       CC       = HQ.getString("tanggal");
                       String[] add         = {HQ.getRow()+ "",AA ,BB, CC };
                       Modeltabel.addRow(add);      
                   }
               }
               catch (Exception ex){
                    JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
               }
    }
    
    private void AmbilDataProduksi(String TransNoProduksi){
        try {
            AA  = TransNoProduksi.replaceAll(".*--", "");  
            AA  = AA.replaceAll("^[\\s]", "");
            
            BB  = TransNoProduksi.replaceAll("--.*", ""); 
            BB  = BB.replaceAll("^[\\s]", "");
            NamaPro.setText(AA);
            TransNoIA.setText(BB);
            }
        catch (Exception X){}
          
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();              
               HQ = stm.executeQuery("SELECT created_date from header_pesanan where trans_no = '"+ BB + "' ");  
               while(HQ.next()  ){
                   CC           = HQ.getString("created_date"); 
               }
               Tanggal.setText(CC);
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (456dd789)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
    }
    
    private boolean FilterInputMenu(){
        if (InputAlat.getSelectedItem().equals("") ){
            JOptionPane.showMessageDialog(null, "Data Alat kosong");
            return false;
        }
        else if( InputDataQty.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Data qty kosong");
            return false;
        }
        return true;
    }
    
    private void AddMenu(){
        if ( FilterInputMenu() ){
            SistemPro.NoUrut NoUrutan = new   SistemPro.NoUrut();       
            Object obj[] = new Object[3];        
            obj[0] = "-";
            obj[1] = InputAlat.getSelectedItem();        
            obj[2] = InputDataQty.getText();

            Modeltabel2.addRow(obj);                  
        }       
    }
    
    private void Reset(boolean Data){
        InpuDataProduksi.setEnabled(Data);
        InputAlat.setEnabled(Data);
        AddInventaris.setEnabled(Data);
        InputDataQty.setEnabled(Data);
        JButtonPbSave.setEnabled(Data);
        HapusDataJTabel(JTableData);
    }
    
    private void HapusDataJTabel(JTable Data){
        /*
         * Logika hapus semua data di jtable
         */
        DefaultTableModel dtm = (DefaultTableModel) Data.getModel();
        dtm.setRowCount(0); 
     }
    
    
    private void DeleteDataResep(){
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from header_Rencana_Inventaris where trans_no = '"+ TransNoIA.getText() + "'");
    }
    
    /*
     * harus disisi 
     */
    private boolean ValidasiSebelumDiSave() {
        if (TransNoIA.equals("")){
            JOptionPane.showMessageDialog(null, "Nama Produksi Tidak ada");
            return false;
        }
        else if (JTableData.getRowCount() < 0){
            JOptionPane.showMessageDialog(null, "Tidak ada alat inventaris yang diinput");
            return false;
        }
        if (JTabelEditPenerimaan == true){
            return true;
        }
        if (InpuDataProduksi.getSelectedItem().equals("")){
            JOptionPane.showMessageDialog(null, "Data Produksi Tidak ada");
            return false;
        }
        
        return true;
    }
    
     private void TabelResepSaveDatabase(){
         if (ValidasiSebelumDiSave() == true){
            boolean SaveAtauTidak;
            SistemPro.TransNo TN    = new SistemPro.TransNo();
            String TransNoP, NamaProduksi, TanggalProduksi;
            TransNoP        = TransNoIA.getText();
            NamaProduksi    = NamaPro.getText();
            TanggalProduksi = Tanggal.getText();

            /*
             * Save Header
             */           
            try {
                    /*
                     * Logika jika diedit
                     */
                    if (JTabelEditPenerimaan == true){
                        DeleteDataResep();
                    }
                    /*
                     * Save Header
                     */                  
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    Stm.executeUpdate("INSERT INTO header_rencana_inventaris "
                            + "(key_no,trans_no,nama_produksi,tanggal) "
                            + "VALUES('"+  1 + "','"+TransNoP + "','"+NamaProduksi 
                            + "','" + TanggalProduksi + "')");     

                    /*
                     * Save DetailPo to database Mysql
                     */
                    try {
                        int a = JTableData.getRowCount() ;
                        Statement stm = K.createStatement();

                        // dbStatement=con.createStatement();
                        for(int i=0;i< a;i++){
                            String bahan   =JTableData.getValueAt(i, 1).toString();
                            String qty     =JTableData.getValueAt(i, 2).toString();

                            stm.executeUpdate("INSERT INTO detail_rencana_inventaris "
                                    + "( no,trans_no, nama_inventaris, qty ) VALUES ('"
                                    + i+1 +"','"+ TransNoP+ "','"+ bahan+ "','"+qty + "')");
                            }
                        JLabelKet.setText("Berhasil Di Saved");
                        Reset(false);
                        TransNoIA.setText(TransNoP );
                        JButtonPbSave.setEnabled(false);
                        Periode();
                        AmbilDataDariDatabase(DatePo, DatePo, 0);  
                 }
                 catch (Exception X){

                     /*
                      * Hapus jika tidak bisa save di detail_resep
                      */
                      DeleteDataResep();
                      JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 122668744 : "  +X, " Error", JOptionPane.ERROR_MESSAGE);
                    }           
                 }
                 catch (Exception Ex){
                     if (Ex.toString().contains("PRIMARY")){
                         JOptionPane.showMessageDialog(null, "Data inventaris alat untuk data produksi " + TransNoP + " bernama "+ NamaProduksi + " sudah pernah dibuat");
                     }
                     else {
                         JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1224215 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
                         Logger.getLogger(PembelianBahan.class.getName()).log(Level.SEVERE, null, Ex);
                     }
                 }              
             } 
         else{
             JLabelKet.setText("Tidak berhasil di save");;
         }
    }
     
      private void Tabel1(){
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         */
        String header[] = {"No","Trans No", "Data Nama Produksi","Tgl Produksi", "Action"};
        Modeltabel = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 4 ) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTableDataDataProduksi.setModel(Modeltabel);

       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel);
        JTableDataDataProduksi.setRowSorter(sorter);
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
        JTableDataDataProduksi.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTableDataDataProduksi.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTableDataDataProduksi.getColumnModel().getColumn(2).setCellRenderer( tengah );
        JTableDataDataProduksi.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
        JTableDataDataProduksi.getColumnModel().getColumn(4).setCellRenderer( tengah ); 
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,80, 200,100,100 };
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTableDataDataProduksi, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTableDataDataProduksi.getColumnModel().getColumn(4).setCellRenderer(  new ButtonJTable("Delete"));
        JTableDataDataProduksi.getColumnModel().getColumn(4).setCellEditor( new  RencanaInventarisAlat.ButtonJTableKeDuaLocal(new JCheckBox(),Modeltabel, JTableDataDataProduksi));
       
        /*
         * Disable drag colum tabel
         */       
        JTableDataDataProduksi.getTableHeader().setReorderingAllowed(false); 
        
    }
    
    private void Tabel2(){
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         */
        String header[] = {"No", "Data Alat Invetaris","Qty", "Action"};
        Modeltabel2 = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 3 ) {return EditBoleh ;} 
                        if(colIndex == 2 ) {return EditBoleh ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTableData.setModel(Modeltabel2);
       
        /*
         * Rata tengah atau kanan table
         */
        JTableData.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTableData.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTableData.getColumnModel().getColumn(2).setCellRenderer( tengah );
        JTableData.getColumnModel().getColumn(3).setCellRenderer( tengah ); ;
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,300,40,100 };
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTableData, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTableData.getColumnModel().getColumn(3).setCellRenderer(  new ButtonJTable("Delete"));
        JTableData.getColumnModel().getColumn(3).setCellEditor( new ButtonJTableKeDua(new JCheckBox(),Modeltabel2, JTableData));
       
        /*
         * Disable drag colum tabel
         */       
        JTableData.getTableHeader().setReorderingAllowed(false); 
        
    }
    
    class ButtonJTableKeDuaLocal extends DefaultCellEditor {
        private String label;
        protected JButton button;
        DefaultTableModel ModelTabel2;
        JTable Tabel2;
    public ButtonJTableKeDuaLocal(JCheckBox checkBox,  final DefaultTableModel ModelTabel,final JTable Tabel ) {
        super(checkBox);
        this.ModelTabel2 = ModelTabel;
        this.Tabel2 = Tabel; 

    }
    
    
    public Component getTableCellEditorComponent(final JTable table, final Object value,
        boolean isSelected, final int row, int column) {
        label = (value == null) ? "Delete" + row: value.toString();
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                fireEditingStopped(); // agar tidak error saat di hapus jtable         
                try {                      
                    int Pilih = JOptionPane.showConfirmDialog(null, "Apakah anda yakin untuk menghapusnya ?" , " Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if (Pilih == JOptionPane.YES_OPTION){
                            
                            //System.out.println((String )ModelTabel2.getValueAt(row, 1) + "xxxxxxxxxxxxxxxxxxxxx");
                            DeleteDataAlokasiWaktu((String )ModelTabel2.getValueAt(row, 1));
                            ModelTabel2.removeRow(row ); 
                     }
                     else if ( Pilih == JOptionPane.NO_OPTION){
                    }
                }
                catch (Exception X){
                    //JOptionPane.showMessageDialog(null," Tidak boleh di hapus semua" + row);          
                }
              }
        });
        Tabel2.repaint();
        button.setText(label);
        return button;
    }
    public Object getCellEditorValue() {
        return new String(label);
        }
    private void DeleteDataAlokasiWaktu(String Data){
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from header_rencana_inventaris where trans_no = '"+ Data + "'");
        
        HapusDataJTabel(JTableData);
        Reset(false);
        Periode();
        AmbilDataDariDatabase(DatePo, DatePo, 0);      
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

        app_search_data_pemesana_makanan1 = new SistemPro.app_search_data_pemesana_makanan();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTableData = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        InpuDataProduksi = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        InputAlat = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        InputDataQty = new javax.swing.JTextField();
        AddInventaris = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        TransNoIA = new javax.swing.JTextField();
        JLabelKet = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        NamaPro = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        Tanggal = new javax.swing.JTextField();
        JPanelButtonSave = new javax.swing.JPanel();
        JButtonPbSave = new javax.swing.JButton();
        JButtonPbEdit = new javax.swing.JButton();
        JButtonPbNew = new javax.swing.JButton();
        JButtonPbPrint = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTableDataDataProduksi = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        JTextFieldSearch = new javax.swing.JTextField();
        JButtonRefresh = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        dateChooserCombo2 = new datechooser.beans.DateChooserCombo();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Daftar Inventaris Alat");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/logo.png"))); // NOI18N

        JTableData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(JTableData);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        InpuDataProduksi.setEditable(true);
        InpuDataProduksi.setModel(new javax.swing.DefaultComboBoxModel(app_search_data_rencana_inventaris_alat.getData().toArray()));
        InpuDataProduksi.setEnabled(false);

        jLabel1.setText("Input Data Produksi");

        jLabel2.setText("Input Alat Inventaris");

        InputAlat.setEditable(true);
        InputAlat.setModel(new javax.swing.DefaultComboBoxModel(app_search_data_rencana_inventaris_alat_master.getData().toArray()));
        InputAlat.setEnabled(false);

        jLabel3.setText("Qty");

        InputDataQty.setEnabled(false);

        AddInventaris.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Apply.png"))); // NOI18N
        AddInventaris.setText("Add");

        jLabel5.setText("Trans No : ");

        TransNoIA.setEnabled(false);

        JLabelKet.setForeground(new java.awt.Color(255, 0, 102));

        jLabel8.setText("Nama Produksi :");

        NamaPro.setEnabled(false);

        jLabel9.setText("Tgl :");

        Tanggal.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JLabelKet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(InputAlat, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(InpuDataProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TransNoIA, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NamaPro, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(InputDataQty, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(AddInventaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(InpuDataProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(TransNoIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(NamaPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(InputAlat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(InputDataQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddInventaris, javax.swing.GroupLayout.PREFERRED_SIZE, 22, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JLabelKet, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        JPanelButtonSave.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JButtonPbSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Save.png"))); // NOI18N
        JButtonPbSave.setText("Save");
        JButtonPbSave.setEnabled(false);

        JButtonPbEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Update.png"))); // NOI18N
        JButtonPbEdit.setText("Edit");
        JButtonPbEdit.setEnabled(false);

        JButtonPbNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonPbNew.setText("New");

        JButtonPbPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPbPrint.setText("Print");

        javax.swing.GroupLayout JPanelButtonSaveLayout = new javax.swing.GroupLayout(JPanelButtonSave);
        JPanelButtonSave.setLayout(JPanelButtonSaveLayout);
        JPanelButtonSaveLayout.setHorizontalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSaveLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JButtonPbSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbPrint)
                .addContainerGap())
        );
        JPanelButtonSaveLayout.setVerticalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(JButtonPbSave)
                .addComponent(JButtonPbEdit)
                .addComponent(JButtonPbNew)
                .addComponent(JButtonPbPrint))
        );

        JTableDataDataProduksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(JTableDataDataProduksi);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTextFieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTextFieldSearchActionPerformed(evt);
            }
        });

        JButtonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Refresh.png"))); // NOI18N
        JButtonRefresh.setText("Refesh");

        jLabel4.setText("Search");

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

    dateChooserCombo2.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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

jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
jLabel10.setText("-");

javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
jPanel2.setLayout(jPanel2Layout);
jPanel2Layout.setHorizontalGroup(
    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(2, 2, 2)
        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(JButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(JButtonRefresh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(JTextFieldSearch, javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(dateChooserCombo2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dateChooserCombo1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(0, 17, Short.MAX_VALUE))
    );

    jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel6.setText("DATA PRODUKSI");

    jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel7.setText("DATA INVENTARIS ALAT");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(JPanelButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel7)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(JPanelButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JTextFieldSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTextFieldSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTextFieldSearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddInventaris;
    private javax.swing.JComboBox InpuDataProduksi;
    private javax.swing.JComboBox InputAlat;
    private javax.swing.JTextField InputDataQty;
    private javax.swing.JButton JButtonPbEdit;
    private javax.swing.JButton JButtonPbNew;
    private javax.swing.JButton JButtonPbPrint;
    private javax.swing.JButton JButtonPbSave;
    private javax.swing.JButton JButtonRefresh;
    private javax.swing.JLabel JLabelKet;
    private javax.swing.JPanel JPanelButtonSave;
    private javax.swing.JTable JTableData;
    private javax.swing.JTable JTableDataDataProduksi;
    private javax.swing.JTextField JTextFieldSearch;
    private javax.swing.JTextField NamaPro;
    private javax.swing.JTextField Tanggal;
    private javax.swing.JTextField TransNoIA;
    private SistemPro.app_search_data_pemesana_makanan app_search_data_pemesana_makanan1;
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private datechooser.beans.DateChooserCombo dateChooserCombo2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
