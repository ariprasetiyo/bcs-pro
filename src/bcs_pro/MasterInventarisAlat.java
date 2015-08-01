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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
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
 * @author admin
 */
public class MasterInventarisAlat extends javax.swing.JInternalFrame {
    
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();
    private DefaultTableModel   Modeltabel2  = new DefaultTableModel(); 
    private SistemPro.KoneksiDatabase KD    = new SistemPro.KoneksiDatabase();
    private Connection              K       = KD.createConnection();
    private String AA,BB;
    private int DataNoTerakhirTabel;
    private boolean Edit, BolehEdit = false;
    private static JFileChooser jChooser;

    /**
     * Creates new form MasterInventarisAlat
     */
    public MasterInventarisAlat() {
        initComponents();
        Tabel(null , null);
        Aksi();
        
        AmbilDariDatabase();
    }
    private void Aksi(){
        AddInventaris.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                AddMenu();
            }  
        });
        
        jChooser = new JFileChooser("D:\\DATA KU\\DOCUMENT\\PROJECT PRIBADI\\BCS\\Data Import");
        JButtonImportXls1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
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
                    JTableData.removeAll();
                    //HapusDataJTabel(JTableData);
                    fillData(file);
                    Tabel(headers,data );
                    JButtonPbSave.setEnabled(true);
                    JButtonPbEdit.setEnabled(false);
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
        
        JButtonPbEdit.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Reset2(true);
                BolehEdit = true;
                Edit = true;
            }  
        });
        JButtonPbNew.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextFieldAlatInventaris.setText("");
                Reset2(true);
            }  
        });
        
        JButtonPbSave.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextFieldAlatInventaris.setText("");
                Reset2(false);
                Edit= true;
                BolehEdit = false;
                JTextFieldSearch.setText("");
                 SearchTable(JTableData,Modeltabel2);
                 
                 try {
                 Thread.sleep(1000);
                 } 
                 catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                 }
                 
                SaveDatabase();
                JButtonPbEdit.setEnabled(true);
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
                    d.add(cell.getContents().toUpperCase());
                
            }
            d.add("\n");
            data.add(d);
            }
        } 
        catch (BiffException e) {
            e.printStackTrace();
        }
 }
    
    private void Reset2(boolean Benar){
        JButtonPbSave.setEnabled(Benar);
        JTextFieldAlatInventaris.setEnabled(Benar);
        AddInventaris.setEnabled(Benar);
        
    }
    
    private void AddMenu(){
        if ( FilterInputMenu() ){
            SistemPro.NoUrut NoUrutan = new   SistemPro.NoUrut();       
            Object obj[] = new Object[3];        
            obj[0] = DataNoTerakhirTabel;
            DataNoTerakhirTabel = DataNoTerakhirTabel + 1;
            
            obj[1] = JTextFieldAlatInventaris.getText().toUpperCase();
            Modeltabel2.addRow(obj);    
            JTextFieldAlatInventaris.setText("");
        }       
    }
    
    private boolean FilterInputMenu(){
        if( JTextFieldAlatInventaris.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Data input inventaris kosong");
            return false;
        }
        return true;
    }
    
    public void SearchTable(final JTable JTabelBarang, final DefaultTableModel Modeltabel){
         String text = JTextFieldSearch.getText();
               final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel);
                JTabelBarang.setRowSorter(sorter);
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
    
    private void Tabel(Vector Header, Vector Data){ 
        //HapusDataJTabel(JTableData);
        String header[] = {"No", "Data Alat Inventaris","Action"};
        
        /*
         * Jika import null
         */
        if (Header == null){
            
            Modeltabel2 = new DefaultTableModel(null,header) {
                @Override
                public boolean isCellEditable(int rowIndex, int colIndex) {
                            //nilai false agar tidak bisa di edit
                            if(colIndex == 2 ) {return true ;} 
                            if(colIndex == 1 ) {return BolehEdit ;} 
                            return false;   //Disallow the editing of any cell
                     }
            };
            JTableData.setModel(Modeltabel2);
            Tabel2 (JTableData, Modeltabel2,1);
        }
        else {
            if ((header.length -2 ) == Header.size()  ){           
                Header.clear();        
                for (int b = 0; b < header.length  ; b++){
                    Header.add(header[b]);
                }
                Modeltabel2 = new DefaultTableModel(Data,Header) {
                    @Override
                    public boolean isCellEditable(int rowIndex, int colIndex) {
                                //nilai false agar tidak bisa di edit
                                if(colIndex == 5) {return true ;} 
                                return false;   //Disallow the editing of any cell
                         }
                };
                JTableData.setModel(Modeltabel2);
                Tabel2 (JTableData,Modeltabel2 ,2);         
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
     private void Tabel2 (final JTable JTabelBarang, final DefaultTableModel Modeltabel, int a  ){
       /*
        * Membuat sort pada tabel
        * Search Data
        * kenaikab harga
        */ 
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel);
        JTabelBarang.setRowSorter(sorter);
        JTextFieldSearch.addKeyListener(new KeyListener(){
            @Override
            public void keyReleased(KeyEvent e) {
               SearchTable(JTabelBarang,  Modeltabel);
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
        JTableData.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTableData.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTableData.getColumnModel().getColumn(2).setCellRenderer( tengah );

        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,200,100 };
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTableData, jarak_colom);

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
        JTableData.getColumnModel().getColumn(2).setCellRenderer(  new ButtonJTable("Delete"));
        JTableData.getColumnModel().getColumn(2).setCellEditor( new  MasterInventarisAlat.ButtonJTableKeDuaLocal(new JCheckBox(),Modeltabel2, JTableData));
       
        /*
         * Disable drag colum tabel
         */       
        JTableData.getTableHeader().setReorderingAllowed(false); 
         
     }
     /*
        //private void Tabel2(){
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         *
        String header[] = {"No", "Data Alat Inventaris","Action"};
        Modeltabel2 = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 2 ) {return true ;} 
                        if(colIndex == 1 ) {return BolehEdit ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTableData.setModel(Modeltabel2);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        *    
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel2);
        JTableData.setRowSorter(sorter);
        JTextFieldSearch.addKeyListener(new KeyListener(){
            @Override
            public void keyReleased(KeyEvent e) {
               SearchTable(JTableData, Modeltabel2);
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
         *
        JTableData.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTableData.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTableData.getColumnModel().getColumn(2).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         *     
        int jarak_colom[] = {40,200,100 };
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTableData, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         
        JTableData.getColumnModel().getColumn(2).setCellRenderer(  new ButtonJTable("Delete"));
        JTableData.getColumnModel().getColumn(2).setCellEditor( new  MasterInventarisAlat.ButtonJTableKeDuaLocal(new JCheckBox(),Modeltabel2, JTableData));
       
        /*
         * Disable drag colum tabel
         *     
        JTableData.getTableHeader().setReorderingAllowed(false); 
        
    } */
        private void Reset(){
        JButtonPbSave.setEnabled(false);
        //JButtonPbEdit.setEnabled(false);
        JButtonPbNew.setEnabled(true);
        AddInventaris.setEnabled(false);
        JTextFieldAlatInventaris.setEnabled(false);
    }
    
    private void HapusDataJTabel(JTable Data){
        /*
         * Logika hapus semua data di jtable
         */
        //try{
            DefaultTableModel dtm = (DefaultTableModel) Data.getModel();
            dtm.setRowCount(0);
        //}
        //catch (Exception x){
            
        //}
         
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
    
     private void SaveDatabase(){
         
         try {
                /*
                 * validasi
                 */
                String[] Valid  = {JTableData.getValueAt(0, 1).toString()};
                if (Validasi(Valid)) {
                    //JOptionPane.showMessageDialog(null  , Edit);
                    int a = JTableData.getRowCount() ;
                    if (Edit == true){
                            SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
                            MYSQL.MysqlDelete("delete from master_inventaris_alat ");                      
                    }
                    
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    for(int i=0;i< a;i++){

                        int no      =Integer.valueOf(JTableData.getValueAt(i, 0).toString()).intValue();
                        AA          =JTableData.getValueAt(i, 1).toString();
                        
                        Stm.executeUpdate("INSERT INTO master_inventaris_alat"
                                + " (no, namaalat )"
                                + " values ('"+no +"','"+AA + "')");
                        }             
                     AmbilDariDatabase();
                     Reset2(false);
                     Edit = false;

                    }
                 }
                 catch (Exception Ex){
                     
                     System.out.println(Ex + " 1i78861987");
                     //LabelPeringantan.setText("Data error " + Ex);                  
              }                   
     }
    
    private void AmbilDariDatabase(){
        HapusDataJTabel(JTableData);
        /*
         * Isi data ke Tabel dari database
         */         
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("select * from master_inventaris_alat order by no asc  ");              
               while(HQ.next()  ){
                   //int No   = HQ.getRow();
                   AA       = HQ.getString("NamaAlat");
                   String[] add         = {HQ.getRow() + "", AA };
                   Modeltabel2.addRow(add);      
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
            //DataNoTerakhirTabel = 1+ Integer.valueOf( (String ) JTableData.getValueAt(JTableData.getRowCount() -1, 0)).intValue();
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
        MYSQL.MysqlDelete("delete from master_inventaris_alat where namaalat = '"+ Data + "'");
        HapusDataJTabel(JTableData);
        AmbilDariDatabase();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        JTableData = new javax.swing.JTable();
        JPanelButtonSave = new javax.swing.JPanel();
        JButtonPbSave = new javax.swing.JButton();
        JButtonPbEdit = new javax.swing.JButton();
        JButtonPbNew = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        JTextFieldSearch = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        JButtonImportXls1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        JTextFieldAlatInventaris = new javax.swing.JTextField();
        AddInventaris = new javax.swing.JButton();

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

        JPanelButtonSave.setBorder(javax.swing.BorderFactory.createBevelBorder(0));

        JButtonPbSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Save.png"))); // NOI18N
        JButtonPbSave.setText("Save");
        JButtonPbSave.setEnabled(false);

        JButtonPbEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Update.png"))); // NOI18N
        JButtonPbEdit.setText("Edit");

        JButtonPbNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonPbNew.setText("New");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(0));

        JTextFieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTextFieldSearchActionPerformed(evt);
            }
        });

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/View.png"))); // NOI18N
        jLabel17.setText("Search  :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(0, 19, Short.MAX_VALUE))
        );

        JButtonImportXls1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/xls kecil.png"))); // NOI18N
        JButtonImportXls1.setText("Import Xls");

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
                .addComponent(JButtonImportXls1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        JPanelButtonSaveLayout.setVerticalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSaveLayout.createSequentialGroup()
                .addGroup(JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JButtonPbSave)
                        .addComponent(JButtonPbEdit)
                        .addComponent(JButtonPbNew)
                        .addComponent(JButtonImportXls1))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(0));

        jLabel2.setText("Input Alat Inventaris");

        JTextFieldAlatInventaris.setEnabled(false);

        AddInventaris.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Apply.png"))); // NOI18N
        AddInventaris.setText("Add");
        AddInventaris.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldAlatInventaris)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AddInventaris)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(JTextFieldAlatInventaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddInventaris, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JPanelButtonSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
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
    private javax.swing.JButton JButtonImportXls1;
    private javax.swing.JButton JButtonPbEdit;
    private javax.swing.JButton JButtonPbNew;
    private javax.swing.JButton JButtonPbSave;
    private javax.swing.JPanel JPanelButtonSave;
    private javax.swing.JTable JTableData;
    private javax.swing.JTextField JTextFieldAlatInventaris;
    private javax.swing.JTextField JTextFieldSearch;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
