/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
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
import java.util.HashMap;
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
public class MasterBarang extends javax.swing.JInternalFrame {
    DefaultTableModel   Modeltabel  = new DefaultTableModel();
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();
    SistemPro.KomaToString Rupiah = new  SistemPro.KomaToString();
    String AA,BB,CC,DD,EE,FF,GG,HH,II,JJ;
    SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    Connection K = KD.createConnection();
    boolean Edit = false;
    int DataNoTerakhirTabel;
    SistemPro.ComponentHanyaAngka HanyaAngka    = new SistemPro.ComponentHanyaAngka ();

    /**
     * Creates new form MasterBarang
     */
    public MasterBarang() {
        initComponents();
        AkasiAksi();
        Tabel(null , null);
        AmbilDariDatabase();

    }
    
    static int tableWidth = 0; // set the tableWidth
    static int tableHeight = 0; // set the tableHeight
    static JFileChooser jChooser;
    
    void AkasiAksi(){
        
        JButtonSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Edit= true;
                SaveDatabase();
            }
        });
        
        JButtonEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                 Edit= true;
                 JButtonSave.setEnabled(true);
                 JButtonEdit.setEnabled(false);
            }
        });
        
        JButtonPrint.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                PrintJasper();
            }
        });
        
        JButtonAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                AddItemHarga();
            }
        });
           
        /*
         * Action untuk import ke xls
         */
        jChooser = new JFileChooser("D:\\");
        JButtonImportXls.addActionListener(new ActionListener() {
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
                    JTabelBarang.removeAll();
                    fillData(file);
                    Tabel(headers,data );
                    JButtonSave.setEnabled(true);
                    JButtonEdit.setEnabled(false);
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
                 * Cek apakah Ada colum yang kosong
                 */
                if (cell.getContents().equalsIgnoreCase("")){
                    JOptionPane.showMessageDialog(null, "Data column xls ada yang kosong : " + AA);
                    data.clear();
                    headers.clear();
                    break;
                }
                
                
                /*
                 * Rubah ke rupiah
                 * 3 adalah column xls
                 */              
                if (i == 3){                
                    //Rupiah.NilaiRupiah(cell.getContents());
                    //System.out.println(Rupiah.NilaiRupiah(cell.getContents()));
                    d.add(Rupiah.NilaiRupiah(cell.getContents().toUpperCase()));
                }
                else {
                    d.add(cell.getContents().toUpperCase());
                }

            }
            d.add("\n");
            data.add(d);
            }
        } 
        catch (BiffException e) {
            e.printStackTrace();
        }
 }
    
    private void PrintJasper(){
        DefaultTableModel de = (DefaultTableModel)JTabelBarang.getModel();

        JRTableModelDataSource dataSource = new JRTableModelDataSource(de);

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();
        try {

            String x    = System.getProperty("user.dir")+"\\ReportJasper\\DaftarBarangDanHarga.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }  
    }
    
    /*
     * i think love is true, this code, this programming, is my life with sad.
     * waiting for you
     */
    private void AddItemHarga(){
        JTextField ItemGroups   = new JTextField();
        JTextField ItemName     = new JTextField();
        JComboBox Satuan        = new JComboBox();
        JTextField Harga        = new JTextField();
        
        HanyaAngka.SetAntiAngkaPakeKoma(Harga);
        
        /*
         *  Kg ( Kilogram )
            g ( Gram )
            Sdm ( Sendok Makan )
            Sdt ( Sendok Teh)
            L ( Litter )
            Bungkus
            Ml ( Mili Liter )
            Biji
            Buah	
            Ruas
            Kemasan
            Ikat
            Batang
            Lembar
         */
        Satuan.addItem("IKAT");
        Satuan.addItem("BUAH");
        Satuan.addItem("LEMBAR");
        Satuan.addItem("BIJI");
        Satuan.addItem("BUNGKUS");
        Satuan.addItem("KEMASAN");
        Satuan.addItem("BATANG");
        Satuan.addItem("RUAS");
        Satuan.addItem("SDT");
        Satuan.addItem("SDM");
        Satuan.addItem("POTONG");
        Satuan.addItem("EKOR");
        Satuan.addItem("KG");
        Satuan.addItem("G");
        Satuan.addItem("L");
        Satuan.addItem("ML");
        
        
        
        Object[] Object ={
          "Pilih Item Groups ", ItemGroups,
          "Pilih Item Name ", ItemName,
          "Input Satuan ", Satuan,
          "Input Harga ", Harga
        };
        
        int Pilih = JOptionPane.showConfirmDialog(null , Object , "Input data master barang ", JOptionPane.OK_CANCEL_OPTION);
        if (Pilih == JOptionPane.OK_OPTION){
            Object obj[] = new Object[10];        
            obj[0] = DataNoTerakhirTabel;
            DataNoTerakhirTabel = DataNoTerakhirTabel + 1;
            obj[1] = ItemGroups.getText();
            obj[2] = ItemName.getText();
            obj[3] = Satuan.getSelectedItem();
            obj[4] = Rupiah.NilaiRupiah(Harga.getText());
            Modeltabel.addRow(obj);
            JButtonSave.setEnabled(true);
        }
    }
    private void HapusDataJTabel(JTable Data){
        /*
         * Logika hapus semua data di jtable
         */
        DefaultTableModel dtm = (DefaultTableModel) Data.getModel();
        dtm.setRowCount(0); 
     }

     private void Tabel(Vector Header, Vector Data){ 
        HapusDataJTabel(JTabelBarang);
        String header[] = {"No", "Group Item", "Item Name","Satuan","Harga", "Action"}; 
        
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
                                    return false;   //Disallow the editing of any cell
                            }
            };
            JTabelBarang.setModel(Modeltabel);
            Tabel2 (JTabelBarang, Modeltabel,1);
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
                                if(colIndex == 5) {return true ;} 
                                return false;   //Disallow the editing of any cell
                         }
                };
                JTabelBarang.setModel(Modeltabel);
                Tabel2 (JTabelBarang,Modeltabel ,2);         
            }
            else {
                System.out.println("Header sama ok ok");
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
        JTabelBarang.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelBarang.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        JTabelBarang.getColumnModel().getColumn(1).setCellRenderer( tengah );
        JTabelBarang.getColumnModel().getColumn(4).setCellRenderer( kanan ); 
        JTabelBarang.getColumnModel().getColumn(5).setCellRenderer( tengah ); 

        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */

        int jarak_colom[] = {40,200,300,100, 100, 100 };
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
        JTabelBarang.getColumnModel().getColumn(5).setCellRenderer(  new ButtonJTable("Delete"));
        JTabelBarang.getColumnModel().getColumn(5).setCellEditor( new  ButtonJTableKeDua(new JCheckBox(),Modeltabel, JTabelBarang));

        /*
         * Disable drag colum tabel
         */       
        JTabelBarang.getTableHeader().setReorderingAllowed(false);
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
                String[] Valid  = {JTabelBarang.getValueAt(0, 1).toString()};
                if (Validasi(Valid)) {

                    int a = JTabelBarang.getRowCount() ;
                    if (Edit == true){
                            SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
                            MYSQL.MysqlDelete("delete from master_barang ");                      
                    }
                    
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    for(int i=0;i< a;i++){

                        int no      =Integer.valueOf(JTabelBarang.getValueAt(i, 0).toString()).intValue();
                        AA          =JTabelBarang.getValueAt(i, 1).toString();
                        BB          =JTabelBarang.getValueAt(i, 2).toString();
                        CC          =JTabelBarang.getValueAt(i, 3).toString();
                        DD          =JTabelBarang.getValueAt(i, 4).toString();

                        Stm.executeUpdate("INSERT INTO master_barang (no, item_groups, item_name, satuan, harga,created_date )"
                                + " values ('"+no +"','"+AA +"','"+BB +"','"+CC +"','"+Rupiah.RupiahKeDoubel (DD)+"', now())");
                        }             
                     AmbilDariDatabase();
                     JButtonSave.setEnabled(false);
                     JButtonEdit.setEnabled(true);
                     Edit = false;

                    }
                 }
                 catch (Exception Ex){
                     System.out.println(Ex);
                     //LabelPeringantan.setText("Data error " + Ex);                  
              }                   
     }
     
     private void AmbilDariDatabase(){
        HapusDataJTabel(JTabelBarang);
        /*
         * Isi data ke Tabel dari database
         */         
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT no, item_groups ,item_name, harga, satuan from master_barang order by no asc  ");              
               while(HQ.next()  ){
                   int No   = HQ.getRow();
                   AA       = HQ.getString("item_groups");
                   BB       = HQ.getString("item_name");
                   CC       = HQ.getString("harga");
                   DD       = HQ.getString("satuan");
                   String[] add         = {HQ.getRow() + "", AA,BB, DD,Rupiah.NilaiRupiah(CC)};
                   Modeltabel.addRow(add);      
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
            DataNoTerakhirTabel = 1+ Integer.valueOf( (String ) JTabelBarang.getValueAt(JTabelBarang.getRowCount() -1, 0)).intValue();
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
        JTabelBarang = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        JButtonImportXls = new javax.swing.JButton();
        JTextFieldSearch = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        JButtonSave = new javax.swing.JButton();
        JButtonEdit = new javax.swing.JButton();
        JButtonPrint = new javax.swing.JButton();
        JButtonAdd = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Master Barang");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/logo.png"))); // NOI18N

        JTabelBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(JTabelBarang);

        JButtonImportXls.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/xls kecil.png"))); // NOI18N
        JButtonImportXls.setText("Import Xls");

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/View.png"))); // NOI18N
        jLabel16.setText("Search  :");

        JButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Save.png"))); // NOI18N
        JButtonSave.setText("Save");
        JButtonSave.setEnabled(false);

        JButtonEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Update.png"))); // NOI18N
        JButtonEdit.setText("Edit");

        JButtonPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPrint.setText("Print");

        JButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Apply.png"))); // NOI18N
        JButtonAdd.setText("Add");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JButtonAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPrint)
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JButtonImportXls)
                    .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonImportXls))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JButtonPrint)
                            .addComponent(JButtonEdit)
                            .addComponent(JButtonSave)
                            .addComponent(JButtonAdd))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-815)/2, (screenSize.height-449)/2, 815, 449);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonAdd;
    private javax.swing.JButton JButtonEdit;
    private javax.swing.JButton JButtonImportXls;
    private javax.swing.JButton JButtonPrint;
    private javax.swing.JButton JButtonSave;
    private javax.swing.JTable JTabelBarang;
    private javax.swing.JTextField JTextFieldSearch;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
