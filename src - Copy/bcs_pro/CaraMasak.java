/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author LANTAI3
 */
public class CaraMasak extends javax.swing.JInternalFrame {
    
    SistemPro.TanggalSistem N       = new SistemPro.TanggalSistem();
    SistemPro.KoneksiDatabase KD    = new SistemPro.KoneksiDatabase();
    Connection              K       = KD.createConnection();
    DefaultTableModel   Modeltabel  = new DefaultTableModel();
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();
    boolean Edit = false;

    /**
     * Creates new form CaraMasak
     */
    public CaraMasak() {
        initComponents();
        TampilanReset ();
        Tabel();
        AmbilDataDariDatabase();
        AksiAksi();
        TampilanTextArea();
    }
    
    private void AksiAksi(){
        JButtonSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {    
                 SaveDB();
            }
        });
        
        JButtonEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {    
                Edit = true;
                jTextArea1.setEditable(true);
                JButtonSave.setEnabled(true);
                JButtonEdit.setEnabled(false);
            }
        });
        
        JButtonNew.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {    
                TampilanReset ();
                InpuJudulMasak();
            }
        });

        
        JButtonPrint.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) { 
                
                /*
                 * Logika untuk next text area jasper report
                 * Bisa seperti ini 
                String[] lines = jTextArea1.getText().split("\n");
                String workerName = lines[106];
                String workerDepartment = lines[1];
                 */
                
                int b = 0;
                StringBuilder sb = new StringBuilder();
                for (String line : jTextArea1.getText().split("\\n")) {
                    b = b + 1;
                    if (b > 51){
                           sb.append(line + "\n");
                    }
                }
                System.out.println(sb.toString());
                
                a = jTextArea1.getLineCount();
               CaraMasak multipages = new CaraMasak();  
                JasperPrint allpages = null;  
                try {
                    allpages = multipages.JasperReport(a, sb.toString(),  jTextArea1.getText());
                } catch (IOException ex) {
                    Logger.getLogger(CaraMasak.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JRException ex) {
                    Logger.getLogger(CaraMasak.class.getName()).log(Level.SEVERE, null, ex);
                }
        multipages.viewer(allpages); 
            }
        });
        
        JTabel.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    int row = JTabel.getSelectedRow();
                    ViewData(row);
                    JButtonEdit.setEnabled(true);
                    JButtonSave.setEnabled(false);
                }
            }   
        });      
    }
    
    private void TampilanReset (){
        jTextArea1.setText("");
        JButtonSave.setEnabled(true);
        JButtonEdit.setEnabled(false);
        NamaMasakan1.setText("");
        JTextFieldSearch.setText("");
        jTextArea1.setEditable(false);
    }
    
    
    private Font newFont = new Font("Courier", Font.PLAIN, 10);
    private void  TampilanTextArea(){
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setFont(newFont);
        
        Element paragraph = jTextArea1.getDocument().getDefaultRootElement();
        int contentCount = paragraph.getElementCount();
        for (int i = 0; i < contentCount; i++) {
            Element e = paragraph.getElement(i);
            int rangeStart = e.getStartOffset();
            int rangeEnd = e.getEndOffset();
            String line;
            try {
                line = jTextArea1.getText(rangeStart, rangeEnd - rangeStart);
                System.out.println(line);
            } catch (BadLocationException ex) {
                Logger.getLogger(CaraMasak.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    int a;
    private JasperPrint JasperReport(int a, String sb, String Text1)  throws IOException, JRException{
        JREmptyDataSource dataSource = new JREmptyDataSource();

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();
        hashMap.put("Text", Text1);
        try {
            
            String x;
            String x2;
            
            /*
             * Pindah report
             */
            if (a > 70){
                System.out.println("xxxxx" + a);
                Map reportparametermap1 = new HashMap();  
                Map reportparametermap2 = new HashMap();
               
                reportparametermap1.put("Text", Text1)  ;
                reportparametermap2.put("Txt2", sb)  ;
        
                x           = System.getProperty("user.dir")+"\\ReportJasper\\ReportCaraMemasak.jrxml";  
                x2          = System.getProperty("user.dir")+"\\ReportJasper\\ReportCaraMemasak2.jrxml";
                
                JasperPrint CetakKe1    = multipageLinking(fillJasperPrint(x  , reportparametermap1), fillJasperPrint(x2, reportparametermap2));  
                return CetakKe1;               
            }
            else {
                x               = System.getProperty("user.dir")+"\\ReportJasper\\ReportCaraMemasak.jrxml";  
                jasperDesign    = JRXmlLoader.load(x);
                jasperReport    = JasperCompileManager.compileReport(jasperDesign);
                jasperPrint     = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
                System.out.println("xxxxx" + a);
                return jasperPrint;
                
            }
                                     
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       } 
        return null;
    }
    
   /*
    * Yang ke tiga
    */
      public JasperPrint multipageLinking(JasperPrint page1, JasperPrint page2) {  
        List<JRPrintPage> pages = page2.getPages();  
        for (int count = 0; count <  
                pages.size(); count++) {  
            page1.addPage(pages.get(count));  
        }  
        return page1;  
    }
      
      /*
       * Yang ke Lima
       */
      private void viewer(JasperPrint page1) {  
        JasperViewer viewer = new JasperViewer(page1, false);  
        viewer.setVisible(true);  
    }
      
      /*
       * Yang ke dua
       */
       private JasperPrint fillJasperPrint(String jasperpath, Map parametersMap)   
            throws IOException, JRException {  
           /*
            JasperPrint jasperprint = new JasperPrint();  
            jasperprint = JasperFillManager.fillReport(getClass().  
            getResource(jasperpath).openStream(), parametersMap);  
            */
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(null);

            JasperDesign jasperDesign = null;
            JasperPrint jasperPrint = null ;
            JasperReport jasperReport;

            jasperDesign    = JRXmlLoader.load(jasperpath);
            jasperReport    = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint     = JasperFillManager.fillReport(jasperReport, parametersMap, new    JREmptyDataSource());

            return jasperPrint;  
    }  
    
    
    private void HapusDataJTabel(JTable Data){
        /*
         * Logika hapus semua data di jtable
         */
        DefaultTableModel dtm = (DefaultTableModel) Data.getModel();
        dtm.setRowCount(0); 
     }
    
      private void Tabel(){
        
        String header[] = {"No", "Nama Masakan", "Tanggal Buat", "Action"};
        Modeltabel = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 3) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTabel.setModel(Modeltabel);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel);
        JTabel.setRowSorter(sorter);
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
        JTabel.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabel.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTabel.getColumnModel().getColumn(2).setCellRenderer( tengah );
        JTabel.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,300,100,100 };
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTabel, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTabel.getColumnModel().getColumn(3).setCellRenderer(  new ButtonJTable("Delete"));
        JTabel.getColumnModel().getColumn(3).setCellEditor( new  CaraMasak.ButtonJTableKeDuaCuti(new JCheckBox(),Modeltabel, JTabel));
       
        /*
         * Disable drag colum tabel
         */       
        JTabel.getTableHeader().setReorderingAllowed(false); 
        
    }
      void ViewData(int Row){
          
          String DataAmbil  = (String) JTabel.getValueAt(Row, 1);
          int baris;       
          ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               //"No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
               HQ = stm.executeQuery("SELECT nama_masakan, cara_memasak from header_memasak where nama_masakan ='"+ DataAmbil + "'");              
               baris = HQ.getRow();

               while(HQ.next()  ){
                   AA    = HQ.getString("cara_memasak");
                   NamaMasakan1.setText(DataAmbil);
                   jTextArea1.setText(AA);
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
          
      }
      
      String AA, BB, CC;
     private void AmbilDataDariDatabase(){
         
        HapusDataJTabel(JTabel);
         
        /*
         * Isi data ke Tabel dari database
         */      
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               //"No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
               HQ = stm.executeQuery("SELECT * from header_memasak");              
               baris = HQ.getRow();

               while(HQ.next()  ){

                   BB    = HQ.getString("nama_masakan");
                   //CC    = HQ.getString("created_date");
                           
                   String[] add         = {String.valueOf(HQ.getRow()).toString(), BB ,  CC };
                   Modeltabel.addRow(add);    
                   //ResetDataPelamar();
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
           
        JTabel.setModel(Modeltabel);      
     }
     
     String NamaMasakan;
     private void InpuJudulMasak(){         
         JTextField InputMasak = new JTextField();
         
         Object[] Object ={
          "Input Nama Masakan    : ", InputMasak
        };
        
        int Pilih = JOptionPane.showConfirmDialog(null , Object , "Pilih dep&jab ", JOptionPane.OK_CANCEL_OPTION);
        if (Pilih == JOptionPane.OK_OPTION){
            this.NamaMasakan   = (String ) InputMasak.getText();
            NamaMasakan1.setText(NamaMasakan);
            jTextArea1.setEditable(true);
        }
     }
     
    boolean FilterData(String[] Data2){
        for (int a = 0; a < Data2.length ; a ++){
            if (Data2[a].equals("") || Data2[a].equals("")  ){                
                JOptionPane.showMessageDialog(null, "Ada Data Kosong");
                return false;
            }
        }
        return true;
    }
    
    private void DeleteData(String Data, String Tabel){       
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from " + Tabel  +" where nama_masakan = '"+ Data + "'");      
     }  
     
     private void SaveDB(){

        try {             
                AA  = jTextArea1.getText();
                BB  = NamaMasakan1.getText();
                
                String[] Data = {AA, BB};
                
                if ( FilterData(Data)){
                    /*
                     * validasi
                     */
                    if (Edit){
                         DeleteData(BB , "header_memasak"); 
                    }
                    
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    Stm.executeUpdate("INSERT INTO header_memasak "
                            + "(nama_masakan, cara_memasak, created_date)"
                            + " VALUES ( '"+ BB + "','"+ AA + "', now())");   

                     AmbilDataDariDatabase();
                     JButtonEdit.setEnabled(false);  
                }               
        }
        catch (Exception Ex){
            System.out.println(Ex);
            //LabelPeringantan.setText("Data error " + Ex);                  
     }            
    }
      
    /*
     * Hapus dari tabel
     */
    class ButtonJTableKeDuaCuti extends DefaultCellEditor {
    private String label;
    protected JButton button;
    DefaultTableModel ModelTabel2;
    JTable Tabel2;
    public ButtonJTableKeDuaCuti(JCheckBox checkBox,  final DefaultTableModel ModelTabel,final JTable Tabel ) {
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
                          if ( ( JumlahRow - 1 ) == ModelTabel2.getRowCount() ){
                              DeleteDataPelamar(GetData);                          
                              AmbilDataDariDatabase();                                                        
                          } 
                          else {
                              JOptionPane.showMessageDialog(null, "Tidak bisa di hapus");
                          }
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
    
    private void DeleteDataPelamar(String Data){       
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from header_memasak where nama_masakan = '"+ Data + "'");      
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabel = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        JPanelButtonSave = new javax.swing.JPanel();
        JButtonSave = new javax.swing.JButton();
        JButtonEdit = new javax.swing.JButton();
        JButtonNew = new javax.swing.JButton();
        JButtonPrint = new javax.swing.JButton();
        JTextFieldSearch = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        NamaMasakan1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cara Memasak");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/logo.png"))); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(JTabel);

        JPanelButtonSave.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Save.png"))); // NOI18N
        JButtonSave.setText("Save");

        JButtonEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Update.png"))); // NOI18N
        JButtonEdit.setText("Edit");
        JButtonEdit.setEnabled(false);

        JButtonNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonNew.setText("New");
        JButtonNew.setEnabled(false);

        JButtonPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPrint.setText("Print");

        jLabel16.setText("Search data  :");

        NamaMasakan1.setEnabled(false);

        jLabel17.setText("Nama Masakan :");

        javax.swing.GroupLayout JPanelButtonSaveLayout = new javax.swing.GroupLayout(JPanelButtonSave);
        JPanelButtonSave.setLayout(JPanelButtonSaveLayout);
        JPanelButtonSaveLayout.setHorizontalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSaveLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(JButtonSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonEdit)
                .addGap(5, 5, 5)
                .addComponent(JButtonNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPrint)
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NamaMasakan1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPanelButtonSaveLayout.setVerticalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSaveLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(NamaMasakan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel17))
                    .addGroup(JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JButtonSave)
                        .addComponent(JButtonEdit)
                        .addComponent(JButtonNew)
                        .addComponent(JButtonPrint)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JPanelButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(JPanelButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonEdit;
    private javax.swing.JButton JButtonNew;
    private javax.swing.JButton JButtonPrint;
    private javax.swing.JButton JButtonSave;
    private javax.swing.JPanel JPanelButtonSave;
    private javax.swing.JTable JTabel;
    private javax.swing.JTextField JTextFieldSearch;
    private javax.swing.JTextField NamaMasakan1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
