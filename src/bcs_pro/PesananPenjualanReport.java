/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.PatternSyntaxException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author LANTAI3
 */
public class PesananPenjualanReport extends javax.swing.JPanel {

    /**
     * Creates new form PesananMakananPenjualanReport
     */
    DefaultTableModel   Modeltabel  = new DefaultTableModel();
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();
    SistemPro.KoneksiDatabase KD    = new SistemPro.KoneksiDatabase();
    Connection              K       = KD.createConnection();
    public PesananPenjualanReport() {
        initComponents();
        Aksi();
        Tabel2();
    }
    
    private void Aksi(){
        
    }
      private void Tabel2(){
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         */
        String header[] = {"No", "Trans No","Nama Pemesan", "Alamat","Tanggal Transaksi","Status Dibayar", "Tanggal Buat","periode ", "Action"};
        Modeltabel = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 8) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTabelDataPenjualan.setModel(Modeltabel);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel);
        JTabelDataPenjualan.setRowSorter(sorter);
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
        JTabelDataPenjualan.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelDataPenjualan.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTabelDataPenjualan.getColumnModel().getColumn(2).setCellRenderer( tengah );
        JTabelDataPenjualan.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
        JTabelDataPenjualan.getColumnModel().getColumn(4).setCellRenderer( tengah ); 
        JTabelDataPenjualan.getColumnModel().getColumn(5).setCellRenderer( tengah ); 
        JTabelDataPenjualan.getColumnModel().getColumn(6).setCellRenderer( tengah );
        JTabelDataPenjualan.getColumnModel().getColumn(7).setCellRenderer( tengah );
        JTabelDataPenjualan.getColumnModel().getColumn(8).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,100,200,40,100,100, 120,70, 100 };
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTabelDataPenjualan, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        //JTabelDataPenjualan.getColumnModel().getColumn(8).setCellRenderer(  new ButtonJTable("Delete"));
        //JTabelDataPenjualan.getColumnModel().getColumn(8).setCellEditor( new  PesananMakanan.ButtonJTableKeDuaLocal(new JCheckBox(),Modeltabel2, JTabelDataPenjualan));
       
        /*
         * Disable drag colum tabel
         */       
        JTabelDataPenjualan.getTableHeader().setReorderingAllowed(false); 
        
    }
    /*
     * Jika a = 1, maka ambil data where berdasarkan YYYY-MM-DD pada tabel created_date
     * jika a = 0, maka ambil data where berdasarkan YYYY-MM pada tabel period
     */
    String AA,BB,CC,DD,EE,FF,GG;
    private void AmbilDataDariDatabase(String Periode1, String Periode2, int a){
         HapusDataJTabel(JTabelDataPenjualan);
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
                       Modeltabel.addRow(add);      
                   }
               }
               catch (Exception ex){
                    JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
               }
    }
    private void HapusDataJTabel(JTable Data){
        /*
         * Logika hapus semua data di jtable
         */
        DefaultTableModel dtm = (DefaultTableModel) Data.getModel();
        dtm.setRowCount(0); 
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
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTabelDataPenjualan = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        JTextFieldSearch = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        dateChooserCombo1x1 = new datechooser.beans.DateChooserCombo();
        dateChooserCombo2x1 = new datechooser.beans.DateChooserCombo();
        jLabel8 = new javax.swing.JLabel();
        JButtonRefresh1 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        JTabelDataPenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(JTabelDataPenjualan);

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel17.setText("Search data  :");

        dateChooserCombo2x1.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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

    jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel8.setText("-");

    JButtonRefresh1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Refresh.png"))); // NOI18N
    JButtonRefresh1.setText("Refesh");

    jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Belum Lunas", "Sudah Lunas" }));

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel17)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(15, 15, 15)
            .addComponent(dateChooserCombo1x1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(dateChooserCombo2x1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(JButtonRefresh1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addGap(4, 4, 4)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(dateChooserCombo1x1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dateChooserCombo2x1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8))
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JButtonRefresh1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane2)
        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonRefresh1;
    private javax.swing.JTable JTabelDataPenjualan;
    private javax.swing.JTextField JTextFieldSearch;
    private datechooser.beans.DateChooserCombo dateChooserCombo1x1;
    private datechooser.beans.DateChooserCombo dateChooserCombo2x1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
