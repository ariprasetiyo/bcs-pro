/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.ButtonJTableKeDua;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.app_search1;
import SistemPro.app_search_data_pemesana_makanan;
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
import java.util.regex.PatternSyntaxException;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
/**
 *
 * @author LANTAI3
 */
public class PesananMakanan extends javax.swing.JInternalFrame {

    /**
     * Creates new form PesananMakanan
     */
    String JenisPesanan;
    String JmlhPorsi;
    
    SistemPro.TanggalSistem N       = new SistemPro.TanggalSistem();
    SistemPro.KoneksiDatabase KD    = new SistemPro.KoneksiDatabase();
    Connection              K       = KD.createConnection();
    DefaultTableModel   Modeltabel  = new DefaultTableModel(); // tabel details
    DefaultTableModel   Modeltabel2 = new DefaultTableModel(); // tabel header
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();
    private  SistemPro.ComponentHanyaAngka HanyaAngka = new SistemPro.ComponentHanyaAngka();
    private String AA,BB,CC,DD,EE, FF, GG;
    
    boolean Edit = false;
    public PesananMakanan() {
        initComponents();
        AksiAksi(); 
        Tabel1();
        Tabel2();
        Tanggal();
        Periode();
        AmbilDataDariDatabase(DatePo, DatePo, 0);
        JButtonPbPrint.setVisible(false);
    }
    
    private void AksiAksi(){
        
        HanyaAngka.SetAntiAngkaLimit(Qty, 4);
        
        InputMenu.setSelectedIndex(-1);
        final JTextField JTextFieldItem = (JTextField)InputMenu.getEditor().getEditorComponent();
        JTextFieldItem.setText("");
        JTextFieldItem.addKeyListener(new app_search1(InputMenu));
        
        JButtonPbNew.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {    
                InputAwal();
                Reset();
            }
        });
        
        JButtonPbSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {    
                SaveDb();
            }
        });
        
        JButtonPbEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {    
                Edit = true;
                InputAwal();
            }
        });
        
        Add.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {    
                AddMenu(); 
                Reset();
            }
        });
        
        DataPesanan.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()>=2){
                     SetTampilanReset(false,true);   
                     AA = ( String ) DataPesanan.getValueAt( DataPesanan.getSelectedRow(), 1);
                     AmbilDataView(AA);
                     
                }
            }   
        });
    
        JButtonRefresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) { 
                SearhByPeriod();
            }
        });
    }
    
    private void AmbilDataView(String DataTransNo){
        JTextFieldNamaPemesan.setText((String )DataPesanan.getValueAt( DataPesanan.getSelectedRow(), 2));
        JTextFieldJenisPesanan.setText((String )DataPesanan.getValueAt( DataPesanan.getSelectedRow(),4));
        JumlahPorsiPesanan.setText((String )DataPesanan.getValueAt( DataPesanan.getSelectedRow(),3));
        jLabel9.setText((String )DataPesanan.getValueAt( DataPesanan.getSelectedRow(),5));
        AmbilDataViewDetail(DataTransNo);
    }
    
    private void AmbilDataViewDetail(String TransNo){
        
         HapusDataJTabel(JTableData);     
            ResultSet HQ = null;
               try {
                    Statement stm = K.createStatement();
                    HQ = stm.executeQuery("select trans_no, nama_menu, qty "
                         + " from detail_pesanan "
                         + " where trans_no = '" + TransNo + "'");                              
                   while(HQ.next()  ){
                       AA       = HQ.getString("trans_no");
                       BB       = HQ.getString("nama_menu");
                       CC       = String.valueOf(HQ.getInt("qty")).toString();
                                             
                       /*
                        * Data dikumpulkan pada Data array ex String[] aaa = {}
                        * di add pada arraylist
                        */
                       String[] add         = {HQ.getRow()+ "",BB ,CC };
                       Modeltabel.addRow(add);      
                   }
                   JButtonPbEdit.setEnabled(true);
                   LabelTransNo.setText((String )DataPesanan.getValueAt( DataPesanan.getSelectedRow(),1));
               }
               catch (Exception ex){
                    JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
               }
    }
    
    private void SearhByPeriod(){
            AmbilDataDariDatabase(AmbilDataPeriod1(), AmbilDataPeriod2(), 1);
            JButtonPbEdit.setEnabled(false);
    }
    
    private boolean FilterInputMenu(){
        if (InputMenu.getSelectedItem().equals("") ){
            JOptionPane.showMessageDialog(null, "Data menu kosong");
            return false;
        }
        else if( Qty.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Data qty kosong");
            return false;
        }
        return true;
    }
    
    private boolean FilterString(String[] a){
        for(int b = 0; b < a.length; b++){
            if (a.equals("") || a.equals(null)){
                JOptionPane.showMessageDialog(null, "Data Kosong");
                return false;
            }
        }
        return true;
    }
    
    private void SetTampilanReset(boolean add,boolean edit){
        Add.setEnabled(add);
        InputMenu.setEnabled(add);
        Qty.setEnabled(add);
        JButtonPbSave.setEnabled(add);
        JButtonPbEdit.setEnabled(edit);
        Beritahu.setText("");
        jLabel9.setText("");
        if (Edit == false){
            HapusDataJTabel(JTableData);
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
        dateChooserCombo3.setDateFormat(dt1);
        
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
    
    private void Reset(){
        InputMenu.setSelectedItem("");
        Qty.setText("");
        JButtonPbEdit.setEnabled(false);
    }
    
    private void AddMenu(){
        if ( FilterInputMenu() ){
            SistemPro.NoUrut NoUrutan = new   SistemPro.NoUrut();       
            Object obj[] = new Object[3];        
            obj[0] = NoUrutan.GetNoUrut();
            obj[1] = InputMenu.getSelectedItem();        
            obj[2] = Qty.getText();

            Modeltabel.addRow(obj);                  
        }       
    }
    
    private void HapusDataJTabel(JTable Data){
        /*
         * Logika hapus semua data di jtable
         */
        DefaultTableModel dtm = (DefaultTableModel) Data.getModel();
        dtm.setRowCount(0); 
     }
    
    private void DeleteData(String Data, String Table){       
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from "+ Table+ " where trans_no = '"+ Data + "'");
        //DeleteGambar (Data);       
     } 
    
    int NoUrut;
    String DatePo;
    private void Periode(){
        /*
        * Ambil tanggal dari kzao kalender
        * buat tabel di database harus ada period ( 201301) dan key_no
        */
        SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();

        N.SetTahunSis();
        N.SetBulanSis();
        N.SetTanggalSis();

        this.DatePo = N.GetTahunSisString()+ N.GetBulanSisString();
    }
    private String TransNo(){
        Periode();
        
        /*
         * SELECT trans_no , periode FROM header_pembelian where periode  = WhereSyarat order by  key_no desc limit 0,1
         */        
        SistemPro.TransNo TN = new SistemPro.TransNo();
        //TN.SetTransNoPo("P", "trans_no", "periode", "header_pembelian", DatePo);
        TN.SetTransNoPo("PS", "trans_no", "periode", "header_pesanan", DatePo);
        this.NoUrut = TN.GetNoUrut();
        return TN.GetTransNoPo();
    }
    
    private void SaveDb(){
        
         try {
                AA  = JTextFieldJenisPesanan.getText();
                BB  = JumlahPorsiPesanan.getText();
                CC  = (String) JTableData.getValueAt(0, 0);
                DD  = JTextFieldNamaPemesan.getText() ;
                GG  = jLabel9.getText();
                String[] DataFilter = {AA,BB, CC, GG};
                
                /*
                 * Trans no
                 */                      
                if (FilterString(DataFilter)){  
                    String NoPS;
                    if (Edit == true ){
                        FF = LabelTransNo.getText();
                        DeleteData(FF, "header_pesanan"); 
                        NoPS = FF;
                    }
                    else{
                        NoPS = TransNo();
                        System.out.println("xxx " + NoUrut + " " + NoPS + " xxxx ");
                    }
                                       
                    /*
                     * Save Header 
                     * Buat data trans no, jenis pesanan, pemesan, jenis pesanan, porsi, period 
                     */
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    Stm.executeUpdate("INSERT INTO header_pesanan "
                            + "(key_no, trans_no, nama_pemesan, jenis_pesanan, porsi, tgl_dipesan, "
                            + " created_date, periode, status_dikirim )"
                            + " VALUES ('"+ NoUrut +"','"+NoPS + "','"+ DD
                            + "','" + AA + "','" 
                            + BB + "','"+ GG + "', now(), '"+DatePo + "','0') " );   
                    
                    /*
                     * Save detail
                     */
                    Statement Stm2 = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);                   
                    for (int a = 0; a < JTableData.getRowCount(); a++){
                        Stm2.executeUpdate("insert into detail_pesanan "
                            + "( trans_no, nama_menu, qty) values ('"
                            + NoPS + "','" + JTableData.getValueAt(a, 1) 
                            + "','" + JTableData.getValueAt(a, 2)+"')");       
                    }
                                                    
                     AmbilDataDariDatabase(DatePo, DatePo, 0);
                     JButtonPbEdit.setEnabled(false);
                     SetTampilanReset(false, false);
                     Beritahu.setText("Berhasil di saved"); 
                     Edit = false;
                }
            }
            catch (Exception Ex){
                System.out.println(Ex);
                JOptionPane.showMessageDialog(null, Ex);                
         }            
    }
    /*
     * Jika a = 1, maka ambil data where berdasarkan YYYY-MM-DD pada tabel created_date
     * jika a = 0, maka ambil data where berdasarkan YYYY-MM pada tabel period
     */
    private void AmbilDataDariDatabase(String Periode1, String Periode2, int a){
         HapusDataJTabel(DataPesanan);
         int baris;       
            ResultSet HQ = null;
               try {
                   Statement stm = K.createStatement();
                   if (a == 1){
                       HQ = stm.executeQuery("select trans_no, nama_pemesan, porsi, jenis_pesanan, tgl_dipesan, "
                            + " created_date, periode "
                            + " from header_pesanan "
                            + " where created_date between '" + Periode1 + "' and '" + Periode2 + "'"); 
                         
                   }
                   else if (a == 0){
                       HQ = stm.executeQuery("select trans_no, nama_pemesan, porsi, jenis_pesanan, tgl_dipesan, "
                            + " created_date, periode "
                            + " from header_pesanan "
                            + " where periode = '" + Periode1 + "'");                              
                   }
                   else {
                       JOptionPane.showMessageDialog(null, "Error 97862927 2");
                   }
                                             
                   while(HQ.next()  ){
                       AA       = HQ.getString("trans_no");
                       BB       = HQ.getString("nama_pemesan");
                       CC       = String.valueOf(HQ.getInt("porsi")).toString();
                       DD       = HQ.getString("jenis_pesanan");
                       EE       = HQ.getString("created_date");
                       FF       = HQ.getString("periode");
                       GG       = HQ.getString("tgl_dipesan");
                                             
                       /*
                        * Data dikumpulkan pada Data array ex String[] aaa = {}
                        * di add pada arraylist
                        */
                       String[] add         = {HQ.getRow()+ "",AA ,BB, CC,DD,GG, EE,FF };
                       Modeltabel2.addRow(add);      
                   }
               }
               catch (Exception ex){
                    JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
               }
    }
    
     private void Tabel1(){
        
        String header[] = {"No", "Nama Menu", "Qty Menu ", "Action"};
        Modeltabel = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 3) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTableData.setModel(Modeltabel);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel);
        JTableData.setRowSorter(sorter);
       
        /*
         * Rata tengah atau kanan table
         */
        JTableData.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTableData.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTableData.getColumnModel().getColumn(2).setCellRenderer( tengah );
        JTableData.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,300,100,100 };
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTableData, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTableData.getColumnModel().getColumn(3).setCellRenderer(  new ButtonJTable("Delete"));
        JTableData.getColumnModel().getColumn(3).setCellEditor( new  ButtonJTableKeDua(new JCheckBox(),Modeltabel, JTableData));
       
        /*
         * Disable drag colum tabel
         */       
        JTableData.getTableHeader().setReorderingAllowed(false); 
        
    }
     
      private void Tabel2(){
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         */
        String header[] = {"No", "Trans No","Nama Pemesan", "Porsi","Jenis Pesanan","Tanggal Dikirim", "Tanggal Buat","periode ", "Action"};
        Modeltabel2 = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 8) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        DataPesanan.setModel(Modeltabel2);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel2);
        DataPesanan.setRowSorter(sorter);
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
        DataPesanan.getColumnModel().getColumn(0).setCellRenderer( tengah );
        DataPesanan.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        DataPesanan.getColumnModel().getColumn(2).setCellRenderer( tengah );
        DataPesanan.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
        DataPesanan.getColumnModel().getColumn(4).setCellRenderer( tengah ); 
        DataPesanan.getColumnModel().getColumn(5).setCellRenderer( tengah ); 
        DataPesanan.getColumnModel().getColumn(6).setCellRenderer( tengah );
        DataPesanan.getColumnModel().getColumn(7).setCellRenderer( tengah );
        DataPesanan.getColumnModel().getColumn(8).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,100,200,40,100,100, 120,70, 100 };
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(DataPesanan, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        DataPesanan.getColumnModel().getColumn(8).setCellRenderer(  new ButtonJTable("Delete"));
        DataPesanan.getColumnModel().getColumn(8).setCellEditor( new  ButtonJTableKeDuaLocal(new JCheckBox(),Modeltabel2, DataPesanan));
       
        /*
         * Disable drag colum tabel
         */       
        DataPesanan.getTableHeader().setReorderingAllowed(false); 
        
    }
    
      /*
       * Set tampilan dcomposer saat klik new
       */
    datechooser.beans.DateChooserCombo dateChooserCombo3 = new datechooser.beans.DateChooserCombo();
    private void SettampilanDComposerDate(){
        
        dateChooserCombo3.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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
    }
    private void InputAwal(){
        SettampilanDComposerDate();
        JComboBox InputJenisPesanan = new JComboBox();
        JTextField JumlahPorsi      = new JTextField();
        JTextField Pemesan          = new JTextField();
        HanyaAngka.SetAntiAngkaLimit(JumlahPorsi, 4);
       
        InputJenisPesanan.addItem("LUNCH BOX");
        InputJenisPesanan.addItem("SNACK BOX");
        InputJenisPesanan.addItem("LAUK PAUK");
        InputJenisPesanan.addItem("TUMPENGAN");
        InputJenisPesanan.addItem("PRASMANAN");
        
        if (Edit == true){
            AA = "Edit ";
            BB = "Edit ";
            CC = "Edit";
            InputJenisPesanan.setSelectedItem(JTextFieldJenisPesanan.getText());
            Pemesan.setText(JTextFieldNamaPemesan.getText());
            JumlahPorsi.setText(JumlahPorsiPesanan.getText());
        }
        else {
            AA = "Isi ";
            BB = "Nama ";
            CC = "Masukan ";
        }
         Object[] Object ={
          AA + "Jenis Pesanan         ", InputJenisPesanan,
          AA + "Jumlah Porsi Pesanan  ", JumlahPorsi,
          BB + "Pemesan              ", Pemesan,
          CC + "Tanggal Dipesan       ", dateChooserCombo3
          
        };
        
        String[] a = {( String )InputJenisPesanan.getSelectedItem(),JumlahPorsi.getText(), Pemesan.getText() };
       
        if ( FilterString( a)){
            int Pilih = JOptionPane.showConfirmDialog(null , Object , "Input Data Awal ", JOptionPane.OK_CANCEL_OPTION);
            if (Pilih == JOptionPane.OK_OPTION){
                JTextFieldJenisPesanan.setText( ( String )InputJenisPesanan.getSelectedItem());
                JumlahPorsiPesanan .setText(JumlahPorsi.getText());
                JTextFieldNamaPemesan.setText(Pemesan.getText());
                SetTampilanReset(true,false);
                jLabel9.setText(dateChooserCombo3.getText());
            }
            else{
                Edit = false;
            }
        }        
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
                           // ModelTabel2.removeRow(row ); 
                            
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
        MYSQL.MysqlDelete("delete from header_pesanan where trans_no = '"+ Data + "'");
        Periode();
        AmbilDataDariDatabase(DatePo, DatePo, 0);
        HapusDataJTabel(JTableData);
        Reset();
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

        jPanel1 = new javax.swing.JPanel();
        JPanelButtonSave = new javax.swing.JPanel();
        JButtonPbSave = new javax.swing.JButton();
        JButtonPbEdit = new javax.swing.JButton();
        JButtonPbNew = new javax.swing.JButton();
        JButtonPbPrint = new javax.swing.JButton();
        InputMenu = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        Add = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        Qty = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        JTextFieldJenisPesanan = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        JumlahPorsiPesanan = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        JTextFieldNamaPemesan = new javax.swing.JTextField();
        Beritahu = new javax.swing.JLabel();
        LabelTransNo = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTableData = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        JTextFieldSearch = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        dateChooserCombo2 = new datechooser.beans.DateChooserCombo();
        jLabel7 = new javax.swing.JLabel();
        JButtonRefresh = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        DataPesanan = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Pesanan Makanan");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/logo.png"))); // NOI18N

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JButtonPbSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbPrint)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPanelButtonSaveLayout.setVerticalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSaveLayout.createSequentialGroup()
                .addGroup(JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JButtonPbSave)
                    .addComponent(JButtonPbEdit)
                    .addComponent(JButtonPbNew)
                    .addComponent(JButtonPbPrint))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        InputMenu.setEditable(true);
        InputMenu.setModel(new javax.swing.DefaultComboBoxModel(app_search_data_pemesana_makanan.getData().toArray()));
        InputMenu.setEnabled(false);

        jLabel2.setText("Input Menu");

        Add.setText("Add");
        Add.setEnabled(false);

        jLabel3.setText("Qty");

        Qty.setEnabled(false);

        jLabel4.setText("Jenis Pesanan");

        JTextFieldJenisPesanan.setEditable(false);
        JTextFieldJenisPesanan.setBackground(new java.awt.Color(204, 204, 204));
        JTextFieldJenisPesanan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setText("Jmlh Dipesanan");

        JumlahPorsiPesanan.setEditable(false);
        JumlahPorsiPesanan.setBackground(new java.awt.Color(204, 204, 204));
        JumlahPorsiPesanan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setText("Nama Pemesan");

        JTextFieldNamaPemesan.setEditable(false);
        JTextFieldNamaPemesan.setBackground(new java.awt.Color(204, 204, 204));
        JTextFieldNamaPemesan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        Beritahu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Beritahu.setForeground(new java.awt.Color(255, 0, 0));

        jLabel8.setText("Tgl Dipesan");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JPanelButtonSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(InputMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Qty, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(Add))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(JTextFieldNamaPemesan)
                            .addComponent(JTextFieldJenisPesanan, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JumlahPorsiPesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelTransNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Beritahu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(JTextFieldNamaPemesan)
                            .addComponent(Beritahu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(LabelTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(JumlahPorsiPesanan)
                        .addComponent(JTextFieldJenisPesanan)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InputMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(Add)
                    .addComponent(jLabel3)
                    .addComponent(Qty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

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

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel16.setText("Search data  :");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Data Pesanan Makanan");

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

jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
jLabel7.setText("-");

JButtonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Refresh.png"))); // NOI18N
JButtonRefresh.setText("Refesh");

javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
jPanel2.setLayout(jPanel2Layout);
jPanel2Layout.setHorizontalGroup(
    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel16)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(15, 15, 15)
        .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(JButtonRefresh)
        .addContainerGap(112, Short.MAX_VALUE))
    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addGap(0, 0, Short.MAX_VALUE))
                .addComponent(JButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .addContainerGap())
    );

    DataPesanan.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    jScrollPane2.setViewportView(DataPesanan);

    jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel10.setText("DATA PEMESANAN");

    jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel11.setText("DATA ISI PEMESANAN");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1)
                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE))))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(11, 11, 11)
            .addComponent(jLabel11)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel10)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add;
    private javax.swing.JLabel Beritahu;
    private javax.swing.JTable DataPesanan;
    private javax.swing.JComboBox InputMenu;
    private javax.swing.JButton JButtonPbEdit;
    private javax.swing.JButton JButtonPbNew;
    private javax.swing.JButton JButtonPbPrint;
    private javax.swing.JButton JButtonPbSave;
    private javax.swing.JButton JButtonRefresh;
    private javax.swing.JPanel JPanelButtonSave;
    private javax.swing.JTable JTableData;
    private javax.swing.JTextField JTextFieldJenisPesanan;
    private javax.swing.JTextField JTextFieldNamaPemesan;
    private javax.swing.JTextField JTextFieldSearch;
    private javax.swing.JTextField JumlahPorsiPesanan;
    private javax.swing.JLabel LabelTransNo;
    private javax.swing.JTextField Qty;
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private datechooser.beans.DateChooserCombo dateChooserCombo2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel16;
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
