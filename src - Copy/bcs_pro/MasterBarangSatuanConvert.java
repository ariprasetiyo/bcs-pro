/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.app_search1;
import SistemPro.app_search_data_resep;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author LANTAI3
 */
public class MasterBarangSatuanConvert extends javax.swing.JInternalFrame {

    /**
     * Creates new form MasterBarangSatuanConvert
     */
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();
    private DefaultTableModel   Modeltabel  = new DefaultTableModel();
    private SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    private Connection K = KD.createConnection();
    private int DataNoTerakhirTabel;
    static int tableWidth = 0; // set the tableWidth
    static int tableHeight = 0; // set the tableHeight
    static JFileChooser jChooser;
    SistemPro.KomaToString Convert = new SistemPro.KomaToString();
    SistemPro.ComponentHanyaAngka HanyaAngka    = new SistemPro.ComponentHanyaAngka ();
    boolean Berhenti = true;

    
    private String AA,BB,CC,DD, EE;
    boolean Edit = false;
    
    public MasterBarangSatuanConvert() {
        initComponents();
        AksiAksi();
        Tabel(null , null);
        AmbilDariDatabase();
    }
    
    private void AksiAksi(){
        JButtonSave1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Edit= true;
                SaveDatabase();
            }
        });
        JButtonAdd1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                AddItemHarga();
            }
        });
        
        JButtonEdit1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JButtonSave1.setEnabled(true);
                JButtonEdit1.setEnabled(false);
            }
        });
                     
        /*
         * Action untuk import ke xls
         */
        jChooser = new JFileChooser("D:\\DATA KU\\DOCUMENT\\PROJECT PRIBADI\\BCS\\Data Import");
        JButtonImportXls1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Edit= true;
                jChooser.showOpenDialog(null);
    
                File file = jChooser.getSelectedFile();
                if(!file.getName().endsWith("xls")){
                    JOptionPane.showMessageDialog(null, 
                      "Please select only Excel file.",
                      "Error",JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    JTabelConvertSatuan.removeAll();
                    HapusDataJTabel(JTabelConvertSatuan);
                    CekXlsDenganMasterBarang();
                    fillData(file);
                    Tabel(headers,data );
                    JButtonSave1.setEnabled(true);
                    JButtonEdit1.setEnabled(false);
                    /*
                    Modeltabel = new DefaultTableModel(data, 
                      headers);
                    tableWidth = Modeltabel.getColumnCount() 
                      * 150;
                    tableHeight = Modeltabel.getRowCount() 
                      * 25;
                    JTabelBarang.setPreferredSize(new Dimension(
                      tableWidth, tableHeight));

                    JTabelBarang.setModel(Modeltabel);
                    */
                }      
            }
        });        
    }
    
    private void AddItemHarga(){
        JComboBox ItemName          = new JComboBox();
        JTextField QtyTdkBaku       = new JTextField();
        JComboBox SatuanTdkBaku     = new JComboBox();
        JTextField QtyBaku          = new JTextField();
        JComboBox SatuanBaku        = new JComboBox();
        
        /*
         * Hanya angka
         */
        HanyaAngka.SetAntiAngkaPakeKoma(QtyTdkBaku);
        HanyaAngka.SetAntiAngkaPakeKoma(QtyBaku);
        
        /*
         * Aksi untuk mencari nama item
         */
        ItemName.setEditable(true);
        ItemName.setModel(new javax.swing.DefaultComboBoxModel( app_search_data_resep.getData().toArray()));
        ItemName.setSelectedIndex(-1);
        final JTextField JTextFieldItem = (JTextField)ItemName.getEditor().getEditorComponent();
        JTextFieldItem.setText("");
        JTextFieldItem.addKeyListener(new app_search1(ItemName));

        SatuanTdkBaku.addItem("IKAT");
        SatuanTdkBaku.addItem("BUAH");
        SatuanTdkBaku.addItem("LEMBAR");
        SatuanTdkBaku.addItem("BIJI");
        SatuanTdkBaku.addItem("BUNGKUS");
        SatuanTdkBaku.addItem("KEMASAN");
        SatuanTdkBaku.addItem("BATANG");
        SatuanTdkBaku.addItem("RUAS");
        SatuanTdkBaku.addItem("SDT");
        SatuanTdkBaku.addItem("SDM");
        SatuanTdkBaku.addItem("POTONG");
        SatuanTdkBaku.addItem("EKOR");
        SatuanTdkBaku.addItem("BUAH");
        
        SatuanBaku.addItem("KG");
        SatuanBaku.addItem("G");
        SatuanBaku.addItem("L");
        SatuanBaku.addItem("ML");
        
        Object[] Object ={
          "Pilih nama item ", ItemName,
          "input qty Satuan tidak baku ", QtyTdkBaku,
          "Pilih satuan tidak baku ", SatuanTdkBaku,
          "Input qty Baku ", QtyBaku,
          "Pilih satuan baku ", SatuanBaku
        };
        
        int Pilih = JOptionPane.showConfirmDialog(null , Object , "Input Data Convert", JOptionPane.OK_CANCEL_OPTION);
        if (Pilih == JOptionPane.OK_OPTION){
            Object obj[] = new Object[10];        
            obj[0] = DataNoTerakhirTabel;
            DataNoTerakhirTabel = DataNoTerakhirTabel + 1;
            obj[1] = ItemName.getSelectedItem();
            obj[2] = QtyTdkBaku.getText();
            obj[3] = SatuanTdkBaku.getSelectedItem();
            obj[4] = QtyBaku.getText();
            obj[5] = SatuanBaku.getSelectedItem();
            
            obj[1] = obj[1].toString().replaceAll(".*--\\s", "");
            
            Object[] data = {obj[1],obj[2],obj[3],obj[4],obj[5]};
            if (ValidasiInputDataConvert(data)){
                 Modeltabel.addRow(obj);
                 JButtonSave1.setEnabled(true);
            } 
        }
    }
    
    boolean ValidasiInputDataConvert(Object[] Data){
        for (int a = 0; a < Data.length; a++){
            if (Data[a] == null){
                Data[a] = "";
            }
            if (Data[a].toString().equalsIgnoreCase("")){
                JOptionPane.showMessageDialog(null, "Data ada yang kosong");
                return false;
            }          
        }
        return true;
    }
    
    /*
     * Mengisi data xls ke JTabel
     */
    static Vector headers = new Vector();
    static Vector data = new Vector();
    void fillData(File file) {
        
        Workbook workbook = null;
        try {
            try {
             workbook = Workbook.getWorkbook(file);
            } 
            catch (IOException ex) {
                Logger.getLogger(MasterBarang.class.getName()).log(Level.SEVERE, null, ex);
         }
         Sheet sheet = workbook.getSheet(0);

         headers.clear();
         for (int i = 0; i < sheet.getColumns(); i++) {
            Cell cell1 = sheet.getCell(i, 0);
            headers.add(cell1.getContents());
         }
         data.clear();
         for (int j = 1; j < sheet.getRows(); j++) {
            Vector d = new Vector();
            for (int i = 0; i < sheet.getColumns(); i++) {

                /*
                 * Membuat no urut
                 */
                Cell cell = sheet.getCell(i, j);
                if (i == 0 ){
                    d.add(j);
                }
                
                /*
                 * Cek Apakah ada item name pada master barang atau tidak
                 */
                boolean Cek = true;
                if (i == 0 ){
                    Object[] ol = list.toArray();
                    for (int a = 0; a <= ol.length - 1; a++){
                       if (ol[a].toString().contains(cell.getContents())){
                           AA = cell.getContents();
                           Cek = true;
                           Berhenti = false;
                           break;
                       }
                       Cek = false;                       
                    }
                    
                    if (Cek == false){
                        JOptionPane.showMessageDialog(null, "Nama Item xls tidak ada di Master Barang : " + cell.getContents());
                        data.clear();
                        headers.clear();
                        Berhenti = false;
                        break;
                     }                   
                }
                
                /*
                 * Cek apakah satuan sudah seuai atau tidak
                 */               
                if(i == 2 || i == 4){
                    AA = cell.getContents().toString().toUpperCase();
                    
                    String[] DataPembanding = { "IKAT",
                                                "BUAH",
                                                "LEMBAR",
                                                "BIJI",
                                                "BUNGKUS",
                                                "KEMASAN",
                                                "BATANG",
                                                "RUAS",
                                                "SDT",
                                                "SDM",
                                                "POTONG",
                                                "EKOR",
                                                "GELAS",
                                                "BUTIR",
                                                "BUAH",
                                                "KG",
                                                "G",
                                                "L",
                                                "ML"};
                    
                    for (int a = 0; a < DataPembanding.length; a++){
                        if (AA.equalsIgnoreCase(DataPembanding[a]))
                        {
                            Berhenti = true;   
                            break;
                        }
                        Berhenti = false;
                    }
                    if (Berhenti != true){
                        JOptionPane.showMessageDialog(null, "Satuan tidak terdaftar di program : " + AA);
                        Berhenti = false;
                    }
                }
                
                /*
                 * Cek apakah Ada colum yang kosong
                 */              
                if (cell.getContents().equalsIgnoreCase("")){
                    JOptionPane.showMessageDialog(null, "Data column xls ada yang kosong : " + AA);
                    data.clear();
                    headers.clear();
                    Berhenti = false;
                    break;
                }
                
                /*
                 * Cek satuan Benar atau tidak
                 */
                d.add(cell.getContents().toUpperCase()); 
            }
            
            /*
             * Berhenti
             */
            if (Berhenti == false){
                data.clear();
                headers.clear();
                break;
            }
            d.add("\n");
            data.add(d);
            }
        } 
        catch (BiffException e) {
            e.printStackTrace();
        }
 }
    
    List list = new ArrayList();
    private void CekXlsDenganMasterBarang(){
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT item_name from master_barang ");              
               while(HQ.next()  ){
                   AA       = HQ.getString("item_name");
                   list.add(AA);
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
    }
    
     private void Tabel(Vector Header, Vector Data){ 
        
        String header[] = {"No", "item name", "qty tidak baku","Satuan tidak baku","qty baku", "satuan baku", "Action"}; 
        /*
         * Jika import null
         */     
        if (Header == null){
            Modeltabel = new DefaultTableModel(null,header) {
                @Override
                public boolean isCellEditable(int rowIndex, int colIndex) {
                                    if(colIndex == 1) {return Edit ;} //  nilai false agar tidak bisa di edit
                                    if(colIndex == 2) {return Edit ;}
                                    if(colIndex == 3) {return Edit ;}
                                    if(colIndex == 4) {return Edit ;}
                                    if(colIndex == 5) {return Edit ;}
                                    if(colIndex == 6) {return true ;}
                                    return false;   //Disallow the editing of any cell
                            }
            };
            JTabelConvertSatuan.setModel(Modeltabel);
            Tabel2 (JTabelConvertSatuan, Modeltabel,1);
        }
        else {
            if ((header.length -2 ) == Header.size()  ){           
                Header.clear();        
                for (int b = 0; b < header.length  ; b++){
                    Header.add(header[b]);
                }
                Modeltabel = new DefaultTableModel(Data,Header) {
                    @Override
                    public boolean isCellEditable(int rowIndex, int colIndex) {
                                //nilai false agar tidak bisa di edit
                                if(colIndex == 6) {return true ;} 
                                return false;   //Disallow the editing of any cell
                         }
                };
                JTabelConvertSatuan.setModel(Modeltabel);
                Tabel2 (JTabelConvertSatuan,Modeltabel ,2);         
            }
            else {
                JOptionPane.showMessageDialog(null, "xls tidak sesuai format");
                System.out.println("xls tidak sesuai format");
            }                                 
        }     
    }
       
     /*
      * Jika a = 1 dari database
      * jika a = 2 dari xls
      */
     private void Tabel2 (JTable JTabelBarang, DefaultTableModel Modeltabel, int a  ){

       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel);
        JTabelBarang.setRowSorter(sorter);
        JTextFieldSearch1.addKeyListener(new KeyListener(){
            @Override
            public void keyReleased(KeyEvent e) {
               String text = JTextFieldSearch1.getText();
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
        JTabelBarang.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelBarang.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(1).setCellRenderer( tengah );
        JTabelBarang.getColumnModel().getColumn(4).setCellRenderer( kanan ); 
        JTabelBarang.getColumnModel().getColumn(5).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(6).setCellRenderer( tengah ); 

        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */

        int jarak_colom[] = {40,200,100,100, 100, 100, 100 };
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTabelBarang, jarak_colom); 

       /*
        * Biar ukuran column JTable sesuai column
        *
       tableWidth  = Modeltabel.getColumnCount() * 150;
       tableHeight = Modeltabel.getRowCount() * 25;
       JTabelBarang.setPreferredSize(new Dimension(
       tableWidth, tableHeight));               


        /*
         * Memasukan tombol ke jtable
         */
        JTabelBarang.getColumnModel().getColumn(6).setCellRenderer(  new ButtonJTable("delete"));
        JTabelBarang.getColumnModel().getColumn(6).setCellEditor( new  MasterBarangSatuanConvert.ButtonJTableKeDua(new JCheckBox(),Modeltabel, JTabelBarang));

        /*
         * Disable drag colum tabel
         */       
        JTabelBarang.getTableHeader().setReorderingAllowed(false);
     }
     
     private void HapusDataJTabel(JTable Data){
        /*
         * Logika hapus semua data di jtable
         */
        try {
            DefaultTableModel dtm = (DefaultTableModel) Data.getModel();
            dtm.setRowCount(0); 
        }
        catch (Exception X){
            
        }
        
     }
     
     private void AmbilDariDatabase(){
        HapusDataJTabel(JTabelConvertSatuan);
        
        /*
         * Isi data ke Tabel dari database
         */         
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT item_name, qty_tdk_baku, satuan_tdk_baku, qty_baku, satuan_baku from master_barang_convert_satuan order by item_name asc  ");              
               while(HQ.next()  ){
                   int No   = HQ.getRow();
                   AA       = HQ.getString("item_name");
                   BB       = HQ.getString("qty_tdk_baku");
                   EE       = HQ.getString("satuan_tdk_baku");
                   CC       = HQ.getString("qty_baku");
                   DD       = HQ.getString("satuan_baku");
                   
                   BB = Convert.NilaiRupiah(BB);
                   CC = Convert.NilaiRupiah(CC);
                   String[] add         = {HQ.getRow() + "", AA,BB,EE, CC,DD};
                   Modeltabel.addRow(add);      
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
           try {
               DataNoTerakhirTabel = 1+ Integer.valueOf( (String ) JTabelConvertSatuan.getValueAt(JTabelConvertSatuan.getRowCount() -1, 0)).intValue();
           }
           catch (Exception X){
               
           }
           
     }
     
      private boolean Validasi(String[] c){
        for (int a = 0; a < c.length; a ++){
            if (c.equals("") ){
                JOptionPane.showMessageDialog(null, "Data kosong");
                return false;
            }
        }
        return true;
    }
     private void DeleteData(String Data, String Table){       
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from "+ Table+ " where no = '"+ Data + "'");
        //DeleteGambar (Data);       
     } 
     
     private void SaveDatabase(){
         
         try {
                /*
                 * validasi
                 */
                String[] Valid  = {JTabelConvertSatuan.getValueAt(0, 1).toString()};
                if (Validasi(Valid)) {

                    int a = JTabelConvertSatuan.getRowCount() ;
                    if (Edit == true){
                            SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
                            MYSQL.MysqlDelete("delete from master_barang_convert_satuan ");                      
                    }
                    
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    for(int i=0;i< a;i++){

                        int no      =Integer.valueOf(JTabelConvertSatuan.getValueAt(i, 0).toString()).intValue();
                        AA          =JTabelConvertSatuan.getValueAt(i, 1).toString();
                        BB          =JTabelConvertSatuan.getValueAt(i, 2).toString();
                        CC          =JTabelConvertSatuan.getValueAt(i, 3).toString();
                        DD          =JTabelConvertSatuan.getValueAt(i, 4).toString();
                        EE          =JTabelConvertSatuan.getValueAt(i, 5).toString();
                                               
                        BB = Convert.RupiahKeDoubel(BB);
                        DD = Convert.RupiahKeDoubel(DD);
                       
                        Stm.executeUpdate("INSERT INTO master_barang_convert_satuan"
                                + " (no, item_name, qty_tdk_baku, satuan_tdk_baku, qty_baku, satuan_baku)"
                                + " values ('"+no +"','"+AA +"','"+BB +"','"+CC +"','"+DD+"','"+ EE + "')");
                        }             
                     AmbilDariDatabase();
                     JButtonSave1.setEnabled(false);
                     JButtonEdit1.setEnabled(true);
                     Edit = false;

                    }
                 }
                 catch (Exception Ex){
                     if (Ex.toString().contains("PRIMARY")){
                         JOptionPane.showMessageDialog(null, "Ada nama item yang sama: Tidak berhasil di save");
                         SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
                         MYSQL.MysqlDelete("delete from master_barang_convert_satuan ");    
                     }
                     System.out.println(Ex + " 1i78861987");
                     //LabelPeringantan.setText("Data error " + Ex);                  
              }                   
     }
     
    /*
     * Hapus dari tabel
     */
    class ButtonJTableKeDua extends DefaultCellEditor {
    private String label;
    protected JButton button;
    DefaultTableModel ModelTabel2;
    JTable Tabel2;
    public ButtonJTableKeDua(JCheckBox checkBox,  final DefaultTableModel ModelTabel,final JTable Tabel ) {
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
                         
                         /*
                          * Perhatikan Ambil data dulu baru di hapus via row ( bukan database ) 
                          */
                          int JumlahRow     = ModelTabel2.getRowCount(); 
                          String GetData    = (String )ModelTabel2.getValueAt(row, 1);
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
}
     

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelConvertSatuan = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        JButtonImportXls1 = new javax.swing.JButton();
        JTextFieldSearch1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        JButtonSave1 = new javax.swing.JButton();
        JButtonEdit1 = new javax.swing.JButton();
        JButtonAdd1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Pengaturan Convert Satuan");
        setToolTipText("");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/logo.png"))); // NOI18N

        JTabelConvertSatuan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(JTabelConvertSatuan);

        JButtonImportXls1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/xls kecil.png"))); // NOI18N
        JButtonImportXls1.setText("Import Xls");

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/View.png"))); // NOI18N
        jLabel17.setText("Search  :");

        JButtonSave1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Save.png"))); // NOI18N
        JButtonSave1.setText("Save");
        JButtonSave1.setEnabled(false);

        JButtonEdit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Update.png"))); // NOI18N
        JButtonEdit1.setText("Edit");

        JButtonAdd1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Apply.png"))); // NOI18N
        JButtonAdd1.setText("Add");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(324, Short.MAX_VALUE)
                .addComponent(JButtonAdd1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonSave1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonEdit1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JButtonImportXls1)
                    .addComponent(JTextFieldSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(JTextFieldSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonImportXls1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JButtonEdit1)
                            .addComponent(JButtonSave1)
                            .addComponent(JButtonAdd1))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonAdd1;
    private javax.swing.JButton JButtonEdit1;
    private javax.swing.JButton JButtonImportXls1;
    private javax.swing.JButton JButtonSave1;
    private javax.swing.JTable JTabelConvertSatuan;
    private javax.swing.JTextField JTextFieldSearch1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
