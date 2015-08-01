/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.swing.AbstractCellEditor;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


/**
 *
 * @author LANTAI3
 */
public class PesananMakananStatus extends javax.swing.JInternalFrame {

    /**
     * Creates new form PesananMakananStatus
     */
    DefaultTableModel   Modeltabel2 = new DefaultTableModel();
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();
    private static final long serialVersionUID = 1L;
    private String AA,BB,CC,DD,EE,FF,GG, HH;
    private SistemPro.KoneksiDatabase KD    = new SistemPro.KoneksiDatabase();
    private Connection              K       = KD.createConnection();
    SistemPro.TanggalSistem N       = new SistemPro.TanggalSistem();
    JCheckBox checkBox = new javax.swing.JCheckBox();
    int A = -1;
    
    
    public PesananMakananStatus() {
        initComponents();
        Tabel2();
        Periode();
        AmbilDataDariDatabase(DatePo, DatePo, 0);  
        Aksi();
        Tanggal();
    }
    
    private void Aksi(){
        JButtonRefresh.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                SearhByPeriod();
            }
        });
        
        JTableData.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() >= 1){
                    A = JTableData.getSelectedRow();
                    AA = ( String ) JTableData.getValueAt(A, 8);
                    jComboBox1.setSelectedItem(AA);
                }
            }   
        });
        
        jComboBox1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent s){
                BB = ( String ) jComboBox1.getSelectedItem();
                if (A < 0){
                    JOptionPane.showMessageDialog(null, "Belum ada item yang dipilih");
                }
                else {
                    JTableData.setValueAt(BB, A, 8);
                }
            }
        });
        
    }
    
     private void Tanggal(){
        /*
         * Tampilan bagian Depreciation Procces
         * Perid yang diset ke bulan dan Tahun
         */
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
        dateChooserCombo1x.setDateFormat(dt1);
        dateChooserCombo2x.setDateFormat(dt1);
        
    }
        
    private Status theStatus;
    private void Tabel2(){
    /*
     * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
     */
        
    String header[] = {"No", "Trans No","Nama Pemesan", "Porsi","Jenis Pesanan","Tanggal Dikirim", "Tanggal Buat","periode ", "Status","Action"};
    Modeltabel2 = new DefaultTableModel(null,header) {

        @Override
        public boolean isCellEditable(int rowIndex, int colIndex) {
                    //nilai false agar tidak bisa di edit
                    if(colIndex == 9) {return true ;} 
                    return false;   //Disallow the editing of any cell
        }
        /*
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 8:
                    return Boolean.class;
            }
            return Object.class;
        }
        */
    };
    //Modeltabel2.addRow(new Object[]{true});
    JTableData.setModel(Modeltabel2);
    JTableData.setDefaultEditor(Status.class, new StatusEditor());
    
   /*
    * Membuat sort pada tabel
    * Search Data
    */     
    final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel2);
    JTableData.setRowSorter(sorter);
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
    JTableData.getColumnModel().getColumn(0).setCellRenderer( tengah );
    JTableData.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
    JTableData.getColumnModel().getColumn(2).setCellRenderer( tengah );
    JTableData.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
    JTableData.getColumnModel().getColumn(4).setCellRenderer( tengah ); 
    JTableData.getColumnModel().getColumn(5).setCellRenderer( tengah ); 
    JTableData.getColumnModel().getColumn(6).setCellRenderer( tengah );
    JTableData.getColumnModel().getColumn(7).setCellRenderer( tengah );
    JTableData.getColumnModel().getColumn(8).setCellRenderer( tengah );
    JTableData.getColumnModel().getColumn(9).setCellRenderer( tengah );

    /*
     * Ukuran table JTabelResep
     * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
     */      
    int jarak_colom[] = {40,100,200,40,100,100, 120,70, 100, 80};
    SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
    ukuran_colom.ukuran_colom(JTableData, jarak_colom);
    //JTableData.getColumn("Action").setCellEditor(new DefaultCellEditor(checkBox));

    /*
     * Memasukan tombol ke jtable
     */
    
     JTableData.getColumnModel().getColumn(9).setCellRenderer(   new ButtonJTable("Save"));
     JTableData.getColumnModel().getColumn(9).setCellEditor( new  ButtonJTableKeDuaLocal(new JCheckBox(),Modeltabel2, JTableData));

    /*
     * Disable drag colum tabel
     */       
    JTableData.getTableHeader().setReorderingAllowed(false);
    }
    
    private void SearhByPeriod(){
            AmbilDataDariDatabase(AmbilDataPeriod1(), AmbilDataPeriod2(), 1);
            //JOptionPane.showMessageDialog(null, AmbilDataPeriod1() + " dan " + AmbilDataPeriod2());
    }
    
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
    
    /*
     * Ambil data period dari DD_MM_YYYY menjadi YYYY-MM-DD
     * Ke String
     */
    private String AmbilDataPeriod1(){
        String Tgl1  = dateChooserCombo1x.getText();
        String Period1  = N.ConvertTgl_Bln_Thn_To_Tahun_String(Tgl1) + "-" + N.ConvertTgl_Bln_Thn_To_Bulan_String(Tgl1) 
                    + "-"+N.ConvertTgl_Bln_Thn_To_Tanggal_String(Tgl1);
        return Period1;
    }
    private String AmbilDataPeriod2(){
        String Tgl1  = dateChooserCombo2x.getText();
        String Period2  = N.ConvertTgl_Bln_Thn_To_Tahun_String(Tgl1) + "-" + N.ConvertTgl_Bln_Thn_To_Bulan_String(Tgl1) 
                    + "-"+N.ConvertTgl_Bln_Thn_To_Tanggal_String(Tgl1);
        return Period2;
    }
    
    private void HapusDataJTabel(JTable Data){
        /*
         * Logika hapus semua data di jtable
         */
        DefaultTableModel dtm = (DefaultTableModel) Data.getModel();
        dtm.setRowCount(0); 
     }
    /*
     * Jika a = 1, maka ambil data where berdasarkan YYYY-MM-DD pada tabel created_date
     * jika a = 0, maka ambil data where berdasarkan YYYY-MM pada tabel period
     */
    private void AmbilDataDariDatabase(String Periode1, String Periode2, int a){
         HapusDataJTabel(JTableData);
         int baris;       
            ResultSet HQ = null;
               try {
                   Statement stm = K.createStatement();
                   if (a == 1){
                       HQ = stm.executeQuery("select trans_no, nama_pemesan, porsi, jenis_pesanan, tgl_dipesan, "
                            + " created_date, periode, status_dikirim "
                            + " from header_pesanan "
                            + " where created_date between '" + Periode1 + "' and '" + Periode2 + "'"); 
                         
                   }
                   else if (a == 0){
                       HQ = stm.executeQuery("select trans_no, nama_pemesan, porsi, jenis_pesanan, tgl_dipesan, "
                            + " created_date, periode, status_dikirim "
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
                       HH       = HQ.getString("status_dikirim");
                       
                       if (HH.equalsIgnoreCase("1")){
                           HH = "Sudah dikirim";
                       }
                       else {
                           HH = "Belum dikirim";
                       }
                                             
                       /*
                        * Data dikumpulkan pada Data array ex String[] aaa = {}
                        * di add pada arraylist
                        */
                       //theStatus = Status.Divorced ;
                       String[] add         = {HQ.getRow()+ "",AA ,BB, CC,DD,GG, EE,FF,HH};
                       Modeltabel2.addRow(add);      
                   }
               }
               catch (Exception ex){
                    JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
               }
               
               
    }
    /*
 * Untuk status JTable
 */
private enum Status {
        Single, Married, Divorced;
  }
private class StatusPanel extends JPanel {

        private JRadioButton theSingleOption;
        private JRadioButton theMarriedOption;
        private JRadioButton theDivorcedOption;
        private ButtonGroup buttonGroup = new ButtonGroup();

        StatusPanel() {
            super(new GridLayout(0, 1));
            setOpaque(true);
            theSingleOption = createRadio(Status.Single);
            theMarriedOption = createRadio(Status.Married);
            theDivorcedOption = createRadio(Status.Divorced);
        }

        private JRadioButton createRadio(Status status) {
            JRadioButton jrb = new JRadioButton(status.toString());
            jrb.setOpaque(false);
            add(jrb);
            buttonGroup.add(jrb);
            return jrb;
        }

        public Status getStatus() {
            if (theMarriedOption.isSelected()) {
                return Status.Married;
            } else if (theDivorcedOption.isSelected()) {
                return Status.Divorced;
            } else {
                return Status.Single;
            }
        }

        public void setStatus(Status status) {
            if (status == Status.Married) {
                theMarriedOption.setSelected(true);
            } else if (status == Status.Divorced) {
                theDivorcedOption.setSelected(true);
            } else {
                theSingleOption.setSelected(true);
            }
        }
    }

 public class StatusEditor extends AbstractCellEditor implements TableCellEditor {

        private StatusPanel theStatusPanel;

        StatusEditor() {
            theStatusPanel = new StatusPanel();
        }

        @Override
        public Object getCellEditorValue() {
            return theStatusPanel.getStatus();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
            theStatusPanel.setStatus((Status) value);
            if (isSelected) {
                theStatusPanel.setBackground(table.getSelectionBackground());
            } else {
                theStatusPanel.setBackground(table.getBackground());
            }
            return theStatusPanel;
        }
    }

    private class StatusRenderer extends StatusPanel implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            setStatus((Status) value);
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
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
        jPanel2 = new javax.swing.JPanel();
        JTextFieldSearch = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        dateChooserCombo1x = new datechooser.beans.DateChooserCombo();
        dateChooserCombo2x = new datechooser.beans.DateChooserCombo();
        jLabel7 = new javax.swing.JLabel();
        JButtonRefresh = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Status Pesanan");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/logo.png"))); // NOI18N

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

        dateChooserCombo1x.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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

    dateChooserCombo2x.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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

jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sudah dikirim", "Belum dikirim" }));

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
        .addComponent(dateChooserCombo1x, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(dateChooserCombo2x, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(JButtonRefresh)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 249, Short.MAX_VALUE)
        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGap(4, 4, 4)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(dateChooserCombo1x, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dateChooserCombo2x, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1)
                .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonRefresh;
    private javax.swing.JTable JTableData;
    private javax.swing.JTextField JTextFieldSearch;
    private datechooser.beans.DateChooserCombo dateChooserCombo1x;
    private datechooser.beans.DateChooserCombo dateChooserCombo2x;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
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
        label = (value == null) ? "Save" + row: value.toString();
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                fireEditingStopped(); // agar tidak error saat di hapus jtable         
                try {                      
                    int Pilih = JOptionPane.showConfirmDialog(null, "Apakah anda yakin untuk save data ?" , "Save ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if (Pilih == JOptionPane.YES_OPTION){
                            
                            //System.out.println((String )ModelTabel2.getValueAt(row, 9) + "xxxxxxxxxxxxxxxxxxxxx");
                            String GetDataStatus = (String )ModelTabel2.getValueAt(row, 8);
                            if (GetDataStatus.equals("Belum dikirim")){
                                GetDataStatus = "0";
                            }
                            else if (GetDataStatus.equals("Sudah dikirim")){
                                GetDataStatus = "1";
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Data Error 978776721");
                            }
                            //DeleteDataAlokasiWaktu((String )ModelTabel2.getValueAt(row, 1));
                           // ModelTabel2.removeRow(row ); 
                            button.setSelected(true);
                            UpdatedDataBase(GetDataStatus,(String )ModelTabel2.getValueAt(row, 1));
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
    private void UpdatedDataBase(String DataUpdate,String  Where){
         try {
             SistemPro.KoneksiDatabase KD    = new SistemPro.KoneksiDatabase();
             Connection              K       = KD.createConnection();
             Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); 
             Stm.executeUpdate("update header_pesanan set status_dikirim ='"+DataUpdate+ "' where trans_no ='" + Where+ "'");
         } catch (SQLException ex) {
             Logger.getLogger(ButtonJTableKeDuaLocal.class.getName()).log(Level.SEVERE, null, ex);
         }

     }
}

class SelectAllHeader extends JToggleButton implements TableCellRenderer {

    private static final String ALL = "✓ Select all";
    private static final String NONE = "✓ Select none";
    private JTable table;
    private TableModel tableModel;
    private JTableHeader header;
    private TableColumnModel tcm;
    private int targetColumn;
    private int viewColumn;

    public SelectAllHeader(JTable table, int targetColumn) {
        super(ALL);
        this.table = table;
        this.tableModel = table.getModel();
        if (tableModel.getColumnClass(targetColumn) != Boolean.class) {
            throw new IllegalArgumentException("Boolean column required.");
        }
        this.targetColumn = targetColumn;
        this.header = table.getTableHeader();
        this.tcm = table.getColumnModel();
        this.applyUI();
        this.addItemListener(new ItemHandler());
        header.addMouseListener(new MouseHandler());
        tableModel.addTableModelListener(new ModelHandler());
    }

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
        return this;
    }

    private class ItemHandler implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            boolean state = e.getStateChange() == ItemEvent.SELECTED;
            setText((state) ? NONE : ALL);
            for (int r = 0; r < table.getRowCount(); r++) {
                table.setValueAt(state, r, viewColumn);
            }
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        applyUI();
    }

    private void applyUI() {
        this.setFont(UIManager.getFont("TableHeader.font"));
        this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        this.setBackground(UIManager.getColor("TableHeader.background"));
        this.setForeground(UIManager.getColor("TableHeader.foreground"));
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            viewColumn = header.columnAtPoint(e.getPoint());
            int modelColumn = tcm.getColumn(viewColumn).getModelIndex();
            if (modelColumn == targetColumn) {
                doClick();
            }
        }
    }

    private class ModelHandler implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent e) {
            if (needsToggle()) {
                doClick();
                header.repaint();
            }
        }
    }

    // Return true if this toggle needs to match the model.
    private boolean needsToggle() {
        boolean allTrue = true;
        boolean allFalse = true;
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            boolean b = (Boolean) tableModel.getValueAt(r, targetColumn);
            allTrue &= b;
            allFalse &= !b;
        }
        return allTrue && !isSelected() || allFalse && isSelected();
    }
}

