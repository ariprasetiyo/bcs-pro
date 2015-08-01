/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.Colom_table;
import SistemPro.KoneksiDatabase;
import SistemPro.ObjectView;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.ResepObject;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author LANTAI3
 */
public class PembelianBahanViewPesanan extends javax.swing.JDialog {

    /**
     * Creates new form ResepView
     */
    private SistemPro.TanggalSistem N       = new SistemPro.TanggalSistem();
    public PembelianBahanViewPesanan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        TabelDataViewResep();
        AksiViewResep();
        Tanggal();
    }
    
    private void AksiViewResep(){
        JButtonGetResepView.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                DiKirimKeResep();
            }
        });
        
        JTabelViewResep.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    DiKirimKeResep();
                }
            }   
        });
        
        JButtonRefreshResep.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                AmbilDataDariDatabase(AmbilDataPeriodDhooser());
                FilterPeriod();
            }
        });
  
    }
    
    /*
     * action diaman mau ambil data dari open view po 2
     */
    private ObjectView DataObject;
    public ObjectView GetTableData(){
        return DataObject;
    }
     private void DiKirimKeResep(){
         DataObject = new ObjectView();
         
         int numRows     = JTabelViewResep.getSelectedRows().length;
         int AmbilRow    = JTabelViewResep.getSelectedRow() ;
         
         for(int i=0; i<numRows ; i++ ) {
                    
            /*
             * Ambil Data
             */

            List<String> DataYangAkanDIlist1 = new ArrayList<String>();
            String TransNo              = (String) TabelModelViewResep.getValueAt(AmbilRow, 1);
            String NamaPesanan          = (String) TabelModelViewResep.getValueAt(AmbilRow, 2);
            String JumlahDipesan        = (String) TabelModelViewResep.getValueAt(AmbilRow, 3);
            String JenisBungkus         = (String) TabelModelViewResep.getValueAt(AmbilRow, 4);
            
            DataObject.SetA(TransNo);
            DataObject.SetB(NamaPesanan);
            DataObject.SetC(JumlahDipesan);
            DataObject.SetD(JenisBungkus);

            this.setVisible(false);
        }
     }
     
     /*
      * Bagian Tabel vie
      */
     
     public DefaultTableModel TabelModelViewResep;
     private TableCellRenderer kanan = new RenderingKanan();
     private TableCellRenderer tengah = new RenderingTengah();
     
     KoneksiDatabase KD = new KoneksiDatabase();
     Connection K       = KD.createConnection();
     String AA, BB, CC,DD,EE,FF;
     
     private void HapusDataJTabel(){
        /*
         * Logika hapus semua data di jtable
         */
        DefaultTableModel dtm = (DefaultTableModel) JTabelViewResep.getModel();
        dtm.setRowCount(0); 
     }
     
     private void Tanggal(){
        /*
         * Tampilan bagian Depreciation Procces
         * Perid yang diset ke bulan dan Tahun
         */
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
        dateChooserComboResep1.setDateFormat(dt1);        
    }
     
     /*
     * Ambil data period dari DD_MM_YYYY menjadi YYYY-MM-DD
     * Ke String
     */
     
    private String AmbilDataPeriodDhooser(){
        String Tgl1  = dateChooserComboResep1.getText();
        String Period1  = N.ConvertTgl_Bln_Thn_To_Tahun_String(Tgl1) + "" + N.ConvertTgl_Bln_Thn_To_Bulan_String(Tgl1) ;
        return Period1;
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
     
     private void AmbilDataDariDatabase(String Period){
         
        HapusDataJTabel();
         
        /*
         * Isi data ke Tabel dari database
         */
        
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT trans_no, nama_pemesan, porsi, jenis_pesanan, tgl_dipesan from header_pesanan"
                       + " where periode = '" + Period + "' order by  created_date desc  ");              
               baris = HQ.getRow();

               while(HQ.next()  ){

                   AA       = HQ.getString("trans_no");
                   BB       = HQ.getString("nama_pemesan");
                   CC       = HQ.getString("porsi");
                   DD       = HQ.getString("jenis_pesanan");
                   FF       = HQ.getString("tgl_dipesan");
                   
                   
                   String[] add         = {HQ.getRow() +"" , AA,BB,CC, DD,FF };
                   TabelModelViewResep.addRow(add);      
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
           
        JTabelViewResep.setModel(TabelModelViewResep);
        
     }
     
     private void TabelDataViewResep(){
         
         String header[] = {"No", "Trans No","Nama Pemesan","Jumlah Pesanan", "Jenis Pesanan", "Tangga Dikirim"};
         TabelModelViewResep = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                                //if(colIndex == 1) {return true ;} //  nilai false agar tidak bisa di edit
                                //if(colIndex == 4) {return true ;}
                                //if(colIndex == 3) {return EditKolomQty ;}
                                return false;   //Disallow the editing of any cell
                        }
        };
 
        AmbilDataDariDatabase(AmbilDataPeriodSistem());
        /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(TabelModelViewResep);
        JTabelViewResep.setRowSorter(sorter);
         
        JTextFieldSearchResep.addKeyListener(new KeyListener(){
            @Override
            public void keyReleased(KeyEvent e) {
               String text = JTextFieldSearchResep.getText();
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
        JTabelViewResep.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelViewResep.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        JTabelViewResep.getColumnModel().getColumn(3).setCellRenderer( tengah );
        JTabelViewResep.getColumnModel().getColumn(1).setCellRenderer( tengah );
        JTabelViewResep.getColumnModel().getColumn(4).setCellRenderer( tengah );
        JTabelViewResep.getColumnModel().getColumn(5).setCellRenderer( tengah );
        
        //JTabelResep.getColumnModel().getColumn(3).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(7).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         */      
        int jarak_colom[] = {40,100,100,100, 100,100,100};
        Colom_table ukuran_colom = new Colom_table();
        ukuran_colom.ukuran_colom(JTabelViewResep, jarak_colom);

       
        /*
         * Disable drag colum tabel
         */
        JTabelViewResep.getTableHeader().setReorderingAllowed(false);
     }
     
     private void FilterPeriod(){
         
        String FilterDate1 = dateChooserComboResep1.getText();
        
        SistemPro.TransDate FilterTgl = new SistemPro.TransDate();
        FilterTgl.SetTransDate(FilterDate1);
        int FilterDateIntSatu = FilterTgl.GetTransDate();
        int FilterDateIntDua  = FilterTgl.GetTransDate();
        
        //System.out.print(FilterDateIntSatu);
        
     }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label2 = new java.awt.Label();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelViewResep = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        JButtonRefreshResep = new javax.swing.JButton();
        JButtonGetResepView = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        JTextFieldSearchResep = new javax.swing.JTextField();
        label1 = new java.awt.Label();
        dateChooserComboResep1 = new datechooser.beans.DateChooserCombo();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        label2.setText("Periode   :");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JTabelViewResep.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(JTabelViewResep);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 222, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        JButtonRefreshResep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/refresh_icon.png"))); // NOI18N

        JButtonGetResepView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/get.png"))); // NOI18N

        jLabel1.setText("Search  :");

        label1.setText("Periode   :");

        dateChooserComboResep1.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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
    dateChooserComboResep1.setCurrentNavigateIndex(0);
    dateChooserComboResep1.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_PERIOD);

    jLabel2.setText("Refresh");

    jLabel3.setText("Get");

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(JButtonRefreshResep, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(JButtonGetResepView, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(JTextFieldSearchResep, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dateChooserComboResep1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addGap(76, 76, 76)
            .addComponent(jLabel2)
            .addGap(54, 54, 54)
            .addComponent(jLabel3)
            .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(JTextFieldSearchResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(dateChooserComboResep1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(JButtonRefreshResep)
                .addComponent(JButtonGetResepView))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(jLabel3))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((screenSize.width-771)/2, (screenSize.height-351)/2, 771, 351);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PembelianBahanViewPesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PembelianBahanViewPesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PembelianBahanViewPesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PembelianBahanViewPesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PembelianBahanViewPesanan dialog = new PembelianBahanViewPesanan(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonGetResepView;
    private javax.swing.JButton JButtonRefreshResep;
    private javax.swing.JTable JTabelViewResep;
    private javax.swing.JTextField JTextFieldSearchResep;
    private datechooser.beans.DateChooserCombo dateChooserComboResep1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Label label1;
    private java.awt.Label label2;
    // End of variables declaration//GEN-END:variables
}
