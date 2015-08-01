/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.Colom_table;
import SistemPro.KoneksiDatabase;
import SistemPro.ObjectPenerimaanDatatabse;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
public class PembelianBahanPenerimaanView extends javax.swing.JDialog {
    
    private DefaultTableModel TabelModelViewPenerimaan;
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah = new RenderingTengah();
     
    KoneksiDatabase KD = new KoneksiDatabase();
    Connection K = KD.createConnection();

    /**
     * Creates new form PembelianBahanPenerimaanView
     */
    public PembelianBahanPenerimaanView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        TabelDataViewPenerimaan();
        //AmbilDataDariDatabase();
        AksiAksi();
    }
    
      private void AksiAksi(){
         JButtonGetPenerimaan.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
               DiKirimKePembelian();
            }
        });
        
        JTabelViewPenerimaan.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                  DiKirimKePembelian();
                }
            }   
        });
        
        JButtonRefreshPenerimaan.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                AmbilDataDariDatabase();
                //FilterPeriod();
            }
        });
    }
    
     private void TabelDataViewPenerimaan(){
         
         String header[] = {"No", "Trans No Wr","Trans No Po","Nama Resep","Supplier","Total Biaya","Uang Muka","Uang Kembali","Tanggal Wr", "Tanggal Po","Note" };
         TabelModelViewPenerimaan = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                                //if(colIndex == 1) {return true ;} //  nilai false agar tidak bisa di edit
                                //if(colIndex == 4) {return true ;}
                                //if(colIndex == 3) {return EditKolomQty ;}
                                return false;   //Disallow the editing of any cell
                        }
        };
 
        AmbilDataDariDatabase();
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(TabelModelViewPenerimaan);
       JTabelViewPenerimaan.setRowSorter(sorter);
         
        JTextFieldSearchPenerimaan.addKeyListener(new KeyListener(){
            @Override
            public void keyReleased(KeyEvent e) {
               String text = JTextFieldSearchPenerimaan.getText();
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
        JTabelViewPenerimaan.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelViewPenerimaan.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTabelViewPenerimaan.getColumnModel().getColumn(5).setCellRenderer( kanan );
        JTabelViewPenerimaan.getColumnModel().getColumn(6).setCellRenderer( kanan );
        JTabelViewPenerimaan.getColumnModel().getColumn(7).setCellRenderer( kanan );
        JTabelViewPenerimaan.getColumnModel().getColumn(8).setCellRenderer( tengah );
        JTabelViewPenerimaan.getColumnModel().getColumn(9).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(3).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(7).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         */      
        int jarak_colom[] = {40,80,80,300, 300,100,100,100,100,100,100};
        Colom_table ukuran_colom = new Colom_table();
        ukuran_colom.ukuran_colom(JTabelViewPenerimaan, jarak_colom);

       
        /*
         * Disable drag colum tabel
         */
        JTabelViewPenerimaan.getTableHeader().setReorderingAllowed(false);
     }
     
     private void AmbilDataDariDatabase(){
         
        HapusDataJTabel();
         
        /*
         * Isi data ke Tabel dari database
         */      
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT trans_no, judul_resep,supplier, no_po, tanggal_po, note,total_biaya, uang_muka, uang_kembali, created_date from header_penerimaan order by created_date desc  ");              
               baris = HQ.getRow();

               while(HQ.next()  ){

                   String PembelianTransNo      = HQ.getString("trans_no");
                   String PembelainNamaResep    = HQ.getString("judul_resep");
                   String PembelianSupplier     = HQ.getString("supplier");
                   String PembelianTotBiaya     = String.valueOf(HQ.getInt("total_biaya")).toString();
                   String PembelianUangMuka     = String.valueOf(HQ.getInt("uang_muka")).toString();           
                   String Created_Date          = HQ.getString("created_date");
                   String PenerimaanNote        = HQ.getString("note");
                   String PenerimaanTglPo       = HQ.getString("tanggal_po");
                   String PenerimaanNoPo        = HQ.getString("no_po");
                   String PenerimaanUangKem     = String.valueOf(HQ.getInt("uang_Kembali")).toString();     
                   
                   
                   
                   String[] add         = {String.valueOf(HQ.getRow()).toString() , PembelianTransNo , PenerimaanNoPo,
                       PembelainNamaResep,PembelianSupplier,
                       PembelianTotBiaya,PembelianUangMuka ,PenerimaanUangKem,
                       Created_Date,PenerimaanTglPo,PenerimaanNote, };
                   TabelModelViewPenerimaan.addRow(add);      
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
           
        JTabelViewPenerimaan.setModel(TabelModelViewPenerimaan);
        
     }
     
     private void HapusDataJTabel(){
        /*
         * Logika hapus semua data di jtable
         */
        DefaultTableModel dtm = (DefaultTableModel) JTabelViewPenerimaan.getModel();
        dtm.setRowCount(0); 
     }
     
     /*
     * Membuat obejct dahulu memanggil PembelianObejct
     */   
    private ObjectPenerimaanDatatabse DataObject;
    public ObjectPenerimaanDatatabse GetTableData(){
        return DataObject;
    }
    private void DiKirimKePembelian(){
         DataObject = new ObjectPenerimaanDatatabse();
         
         int numRows     = JTabelViewPenerimaan.getSelectedRows().length;
         int AmbilRow    = JTabelViewPenerimaan.getSelectedRow() ;
         
         for(int i=0; i<numRows ; i++ ) {
                    
            /*
             * Ambil Data
             */
            List<String> DataYangAkanDIlist1 = new ArrayList<String>();
            String AmbilJudulResep      = (String) TabelModelViewPenerimaan.getValueAt(AmbilRow, 3);
            String AmbilTransNo         = (String) TabelModelViewPenerimaan.getValueAt(AmbilRow, 1);
            String AmbilSupplier        = (String) TabelModelViewPenerimaan.getValueAt(AmbilRow, 4);
            String AmbilUangMuka        = (String) TabelModelViewPenerimaan.getValueAt(AmbilRow, 6);
            String AmbilTotBiaya        = (String) TabelModelViewPenerimaan.getValueAt(AmbilRow, 5);
            String AmbilKembali         = (String) TabelModelViewPenerimaan.getValueAt(AmbilRow, 7);
            String AmbilTanggalWr       = (String) TabelModelViewPenerimaan.getValueAt(AmbilRow, 8);
            String AmbilTanggalPo       = (String) TabelModelViewPenerimaan.getValueAt(AmbilRow, 9);
            String AmbilTransNoPo       = (String) TabelModelViewPenerimaan.getValueAt(AmbilRow, 2);
            String AmbilNote            = (String) TabelModelViewPenerimaan.getValueAt(AmbilRow, 10);
            
            
            
            DataObject.SetPenerimaanJudulResep( AmbilJudulResep);
            DataObject.SetPenerimaanTransNo(AmbilTransNo);
            DataObject.SetPenerimaanCreateDateWr(AmbilTanggalWr);
            DataObject.SetPenerimaanSupplier(AmbilSupplier);
            DataObject.SetPenerimaanUangMuka(AmbilUangMuka);
            DataObject.SetPenerimaanTotalBiaya(AmbilTotBiaya);
            DataObject.SetPenerimaanUangKembali(AmbilKembali);
            DataObject.SetPenerimaanNoPo(AmbilTransNoPo);
            DataObject.SetPenerimaanTanggalPo(AmbilTanggalPo);
            DataObject.SetPenerimaanNote(AmbilNote);

            this.setVisible(false);
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
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        JButtonRefreshPenerimaan = new javax.swing.JButton();
        JButtonGetPenerimaan = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        JTextFieldSearchPenerimaan = new javax.swing.JTextField();
        label1 = new java.awt.Label();
        dateChooserComboPembelian1 = new datechooser.beans.DateChooserCombo();
        label3 = new java.awt.Label();
        dateChooserComboPembelian2 = new datechooser.beans.DateChooserCombo();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelViewPenerimaan = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        JButtonRefreshPenerimaan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/refresh_icon.png"))); // NOI18N

        JButtonGetPenerimaan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/get.png"))); // NOI18N

        jLabel1.setText("Search  :");

        label1.setText("Periode   :");

        dateChooserComboPembelian1.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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
    dateChooserComboPembelian1.setCurrentNavigateIndex(0);
    dateChooserComboPembelian1.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_PERIOD);

    label3.setText("  -");

    dateChooserComboPembelian2.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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
                .addComponent(JButtonRefreshPenerimaan, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonGetPenerimaan, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(JTextFieldSearchPenerimaan, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateChooserComboPembelian1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateChooserComboPembelian2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
    .addGroup(jPanel3Layout.createSequentialGroup()
        .addGap(76, 76, 76)
        .addComponent(jLabel2)
        .addGap(54, 54, 54)
        .addComponent(jLabel3))
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(JTextFieldSearchPenerimaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(dateChooserComboPembelian1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(dateChooserComboPembelian2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(JButtonRefreshPenerimaan)
                .addComponent(JButtonGetPenerimaan))
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
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    JTabelViewPenerimaan.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    jScrollPane1.setViewportView(JTabelViewPenerimaan);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
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
            java.util.logging.Logger.getLogger(PembelianBahanPenerimaanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PembelianBahanPenerimaanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PembelianBahanPenerimaanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PembelianBahanPenerimaanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PembelianBahanPenerimaanView dialog = new PembelianBahanPenerimaanView(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton JButtonGetPenerimaan;
    private javax.swing.JButton JButtonRefreshPenerimaan;
    private javax.swing.JTable JTabelViewPenerimaan;
    private javax.swing.JTextField JTextFieldSearchPenerimaan;
    private datechooser.beans.DateChooserCombo dateChooserComboPembelian1;
    private datechooser.beans.DateChooserCombo dateChooserComboPembelian2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Label label1;
    private java.awt.Label label3;
    // End of variables declaration//GEN-END:variables
}
