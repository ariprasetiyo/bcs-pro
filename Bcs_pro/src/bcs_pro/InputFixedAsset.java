/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.ButtonJTableKeDua;
import SistemPro.Colom_table;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.app_search_data_fixed_asset;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.PatternSyntaxException;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author LANTAI3
 * Rumus FA Depreciation
 * Book Value   = ( dep bulan * dep )
 * Accum Dep    = FA Depreciation - Book Value 
 */
public class InputFixedAsset extends javax.swing.JInternalFrame {
    
    private DefaultTableModel ModelTabelGolongan ;
    private DefaultTableModel ModelTabelInput ;
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah = new RenderingTengah();
    private boolean JTabelEditgpGol;
    private SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    private Connection K = KD.createConnection();
    private boolean Golongan = false;
    private ButtonGroup GroupsStatusAsset = new ButtonGroup();
    private  SistemPro.TransNo NoAsset = new SistemPro.TransNo();

    /**
     * Creates new form InputFixedAsset
     */
    public InputFixedAsset() {
        initComponents();
        AksiAksi();
        BagianTabelGol();
        
        /*
         * Bagian input fixed Asset
         */
        AksiInputAsset();
        BagianInputFixedAsset();
        
        /*
         * Tampilan bagian Depreciation Procces
         * Perid yang diset ke bulan dan Tahun
         */
        SimpleDateFormat dt1 = new SimpleDateFormat("MM/yyyy");
        dateChooserCombo2.setDateFormat(dt1);
        dateChooserCombo3.setDateFormat(dt1);
    }
    
    private void BagianTabelGol(){        
        TampilanJTable();
        AmbilDataDatabaseBuatTabel();
    }
    
    private void BagianInputFixedAsset(){
        SetTampilanInput();
        TampilanJTableInputFixedAsset();
        AmbilBuatJComboBoxInputGol();
        AmbilDatabaseBuatTabelFA(AmbilDatePembuatanData(2));
    }
    
    private void AksiAksi(){
        JButtonFANew.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent E){
                TampilanNew(true, true,true,true,true,true, false);
            }
        });
        JButtonFASave.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent E){
                 SaveDatabaseGol();
                TampilanNew(true, false, false, false, false, false, false );
            }
        });
        
        JButtonFAEdit.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent E){
                 TampilanNew(true, true,true,true,true,true, false);
                 Golongan = true;
            }
        });
        
        JComboBoxCOA1.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent E){
                 int a =  AmbilCoaDatabase(String.valueOf(JComboBoxCOA1.getSelectedItem()).toString());
                 JLabelDebit.setText(a+"");
            }
        });
        JComboBoxCOA2.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent E){
                 int a =  AmbilCoaDatabase(String.valueOf(JComboBoxCOA2.getSelectedItem()).toString());
                 JLabelKredit.setText(a+"");
            }
        });
        SistemPro.ComponentHanyaAngka AntiHuruf = new SistemPro.ComponentHanyaAngka();
        AntiHuruf.SetAntiAngka(JTextFieldFAUsia);
        AntiHuruf.LimitCharInput(JTextFieldFAUsia, 2);
        
        
        JTabbedPaneGol.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent ARI) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) ARI.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                String coba = sourceTabbedPane.getTitleAt(index);
                
                if (sourceTabbedPane.getTitleAt(index).equals("Tabel Golongan") ){
                        BagianTabelGol();                       
                        setSize(1150, 500);
                }
                else if (sourceTabbedPane.getTitleAt(index).equalsIgnoreCase("Depreciation Process") ){
                        
                        setSize(220, 300);
                      
                }
                else if (sourceTabbedPane.getTitleAt(index).equalsIgnoreCase("Input Fixed Asset") ){
                        JComboBoxInputGol.removeAllItems();
                        BagianInputFixedAsset();
                        setSize(910, 500);                    
                }
                
                    
              }
        });
        
        JTabelTabelGolongan.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    IsiDanAmbilData();
                    TampilanNew(false, false, false, false, false, false, true );
                }
            }   
        });
        
    }
    
    private void TampilanNew(boolean Save, boolean Gol, boolean NamaGol, boolean Usia, boolean Coa1, boolean Coa2, boolean Edit){
        JButtonFASave.setEnabled(Save);
        JTextFieldFANamaGol.setEnabled(NamaGol);
        JTextFieldFAUsia.setEnabled(Usia);
        JComboBoxCOA1.setEnabled(Coa1);
        JComboBoxCOA2.setEnabled(Coa2);
        JButtonFAEdit.setEnabled(Edit);
    }
    
    private void TampilanJTable(){
        String header[] = {"No", "Gol","Nama Golongan","Usia", "Debit", "Kredit" ,"Create Date", "Action"};
        ModelTabelGolongan = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                                //if(colIndex == 1) {return true ;} //  nilai false agar tidak bisa di edit
                                //if(colIndex == 6) {return JTabelEditPenerimaan ;}
                                
                                if(colIndex == 7) {return true ;}
                                //if(colIndex == 3) {return EditKolomQty ;}
                                return false;   //Disallow the editing of any cell
                        }
        };
        JTabelTabelGolongan.setModel(ModelTabelGolongan);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(ModelTabelGolongan);
        JTabelTabelGolongan.setRowSorter(sorter);
       
        /*
         * Rata tengah atau kanan table
         */
        JTabelTabelGolongan.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelTabelGolongan.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTabelTabelGolongan.getColumnModel().getColumn(3).setCellRenderer( tengah );
        JTabelTabelGolongan.getColumnModel().getColumn(7).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(3).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(7).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         */      
        int jarak_colom[] = {30,30,200,30,150, 150, 100, 80};
        Colom_table ukuran_colom = new Colom_table();
        ukuran_colom.ukuran_colom(JTabelTabelGolongan, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTabelTabelGolongan.getColumnModel().getColumn(7).setCellRenderer(  new ButtonJTable("Delete"));
        JTabelTabelGolongan.getColumnModel().getColumn(7).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),ModelTabelGolongan, JTabelTabelGolongan));
       
        /*
         * Disable drag colum tabel
         */       
        JTabelTabelGolongan.getTableHeader().setReorderingAllowed(false);
        
        /*
         * Jika ada perubahan data pada cell jtable
         */
        ModelTabelGolongan.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent tme) {
                
            }
        });
    }
    
    private void IsiDanAmbilData(){
        int AmbilRow            = JTabelTabelGolongan.getSelectedRow() ;
        String DataGol          = (String) ModelTabelGolongan.getValueAt(AmbilRow , 1);
        String DataNamaGol      = (String) ModelTabelGolongan.getValueAt(AmbilRow , 2);
        String DataUsia         = (String) ModelTabelGolongan.getValueAt(AmbilRow , 3);
        String DataDebit        = (String) ModelTabelGolongan.getValueAt(AmbilRow , 4);
        String DataKredit       = (String) ModelTabelGolongan.getValueAt(AmbilRow , 5);
        
        JTextFieldFAGol.setText(DataGol);
        JTextFieldFAUsia.setText(DataUsia);
        JTextFieldFANamaGol.setText(DataNamaGol);
        
        JComboBoxCOA1.setSelectedItem(DataDebit);
        JComboBoxCOA2.setSelectedItem(DataKredit);
        
        AmbilDataChartNo(DataGol);
    }
    private void AmbilDataChartNo(String Data){
        int No = 0;       
        ResultSet HQ = null;
        
        try {
            Statement stm = K.createStatement();
            HQ = stm.executeQuery("SELECT  gol, chart_no_debit, chart_no_kredit from sys_gol_asset  where gol ='"+ Data+"'");                     

            while(HQ.next()  ){
                 int COADebit = HQ.getInt("chart_no_debit");
                 int COAKredit= HQ.getInt("chart_no_kredit");
                 JLabelDebit.setText(COADebit+"");
                 JLabelKredit.setText(COAKredit+"");
            }

        }
        catch (Exception ex){
             JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
        }

        
    }
    
    private int AmbilCoaDatabase(String Data){
        int No = 0;       
        ResultSet HQ = null;
        try {
            Statement stm = K.createStatement();
            HQ = stm.executeQuery("SELECT  chart_no, chart_parent from sys_chart  where chart_parent ='"+ Data+"'");                     

            while(HQ.next()  ){
                 No = HQ.getInt("chart_no");
            }
            return No;
        }
        catch (Exception ex){
             JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
        }
        return 0;
    }
    
    private int CreateGol(){    
        int No = 0;       
        ResultSet HQ = null;
        
        if (Golongan == true ){
            return Integer.valueOf(JTextFieldFAGol.getText()).intValue();
        }
        try {
            Statement stm = K.createStatement();
            HQ = stm.executeQuery("SELECT  gol from sys_gol_asset  order by gol desc limit 1  ");                     

            while(HQ.next()  ){
                 No = HQ.getInt("gol");
            }
            return No + 1;
        }
        catch (Exception ex){
             JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
        }
        return 1;
    }
    
    private boolean  ValidasiSblmSaveGol(){
        if (JTextFieldFAUsia.getText().equalsIgnoreCase("")||
            JTextFieldFAUsia.getText().equalsIgnoreCase("null") ){
            JLabelKetGol.setText("Usia Kosong");
            return false;
        }
        else if (JLabelDebit.getText().equalsIgnoreCase("")||
            JLabelDebit.getText().equalsIgnoreCase("0") ){
            JLabelKetGol.setText("Chart No Debit Kosong");
            return false;
        } 
         else if (JLabelKredit.getText().equalsIgnoreCase("")||
            JLabelKredit.getText().equalsIgnoreCase("0") ){
            JLabelKetGol.setText("Chart No Kredit Kosong");
            return false;
        }
         else if (JComboBoxCOA1.getSelectedItem().equals("")){
            JLabelKetGol.setText("Debit Kosong");
            return false;
         }
         else if (JComboBoxCOA2.getSelectedItem().equals("")){
            JLabelKetGol.setText("Kredit Kosong");
            return false;
         }
       return true;
    }
        
    
       private void DeleteDataPenerimaan(String Query){
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete(Query);
    }
       
    private void SaveDatabaseGol(){
        if ( ValidasiSblmSaveGol() == true){ 
            try {
                /*
                 * Hapus jika tidak bisa save di detail_resep
                 */
               DeleteDataPenerimaan("delete from sys_gol_asset where gol = '"+ JTextFieldFAGol.getText() + "'");
                Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                Stm.executeUpdate("INSERT INTO sys_gol_asset "
                        + "(gol, nama_golongan, chart_no_debit,chart_no_kredit, usia, debit, kredit, updated_date )VALUES ('"
                        +  CreateGol() 
                        + "','"+JTextFieldFANamaGol.getText() +"','"+ JLabelDebit.getText()
                        + "','"+ JLabelKredit.getText()+ "','"+JTextFieldFAUsia.getText()                      
                        + "','"+JComboBoxCOA1.getSelectedItem() + "','"+JComboBoxCOA2.getSelectedItem()                        
                        + "', now())"); 
                AmbilDataDatabaseBuatTabel();
             }
             catch (Exception Ex){
                 System.out.println(Ex);
                JOptionPane.showMessageDialog(null,  "InputFixAsset.java : error : 1224 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
                }              
             } 
    }
    
     private void HapusDataJTabel(JTable Tabel ){
        /*
         * Logika hapus semua data di jtable
         */
        DefaultTableModel dtm = (DefaultTableModel) Tabel.getModel();
        dtm.setRowCount(0); 
     }
     
     private void AmbilDataDatabaseBuatTabel(){
        HapusDataJTabel(JTabelTabelGolongan);
        /*
         * Isi data ke Tabel dari database
         */      
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               
               //"No", "Gol","Nama Golongan","Usia", "Debit", "Kredit" ,"Create Date", "Action"
               HQ = stm.executeQuery("SELECT gol, nama_golongan,  usia, debit, kredit, updated_date  from sys_gol_asset order by updated_date desc  ");              
               baris = HQ.getRow();
               
               while(HQ.next()  ){

                   String Gol        = String.valueOf(HQ.getInt("gol")).toString();
                   String NamaGol   = HQ.getString("nama_golongan");
                   String Usia      = String.valueOf(HQ.getInt("usia")).toString();
                   String Debit1    = HQ.getString("Debit");
                   String Kredit    = HQ.getString("kredit");
                   String Tgl       = HQ.getString("updated_date");
                   
                   String[] add         = {String.valueOf(HQ.getRow()).toString(), 
                       Gol,NamaGol,Usia,Debit1,Kredit, Tgl};
                   ModelTabelGolongan.addRow(add);      
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
       // JTabelResep.getColumnModel().getColumn(4).setCellRenderer(  new ButtonJTable("Delete"));
        //JTabelResep.getColumnModel().getColumn(4).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelOrder, JTabelResep));   

   }
     
     
     
     /*
      * 
      * Bagian Input Fixed Asset
      * 
      * 
      */
     
     private String PurchaseAmount;
     private String AccumDep;
     private String DepreciationAmount = "0";
     private String DepreciationMonth = "1";
     private String AdjustmentAsset;
     private String AssetNo;
     private String GolAsset;
     private String InvoiceNoAsset;
     private String KetAsset;
     private String NameFa;
     private String Usia;
     private String StatusAsset = "null";
     private String PurchaseDate;
     private String PurchaseDate2;
       
     private void SetTampilanInput(){
         
        kazaoCalendarDateCreatePurchase.setFormat("dd-mm-yyy");
        kazaoCalendarDateInputAsset.setFormat("mm-yyy");

         /*
         * Static JPanel
         * Di Gunakan agar jika ada penambahan componet tidak berubah
         * biar tidak bergerak
         */
        Dimension dimension = new Dimension(10, 30);
        kazaoCalendarDateCreatePurchase.setMaximumSize(dimension);
        kazaoCalendarDateCreatePurchase.setMinimumSize(dimension);
        kazaoCalendarDateCreatePurchase.setPreferredSize(dimension);
        
        kazaoCalendarDateInputAsset.setMaximumSize(dimension);
        kazaoCalendarDateInputAsset.setMinimumSize(dimension);
        kazaoCalendarDateInputAsset.setPreferredSize(dimension);
     }
     
     private void AksiInputAsset(){
         JTextAssetPurchaseData.setText(JTextPurchaseAmnt.getText());
         SistemPro.ComponentHanyaAngka HanyaAngka = new  SistemPro.ComponentHanyaAngka();
         HanyaAngka.SetAntiAngkaLimit(JTextInputAssetUsia, 2);
         HanyaAngka.SetAntiAngkaLimit(JTextAssetPurchaseData, 15);
         HanyaAngka.SetAntiAngkaLimit(JTextAssetDataAccu, 15);    
         HanyaAngka.SetAntiAngkaLimit(JTextAssetAdjustment, 15);
         HanyaAngka.SetAntiAngkaLimit(JTextPurchaseAmnt, 15);
         
         JTextPurchaseAmnt.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e)
                {
                    JTextAssetPurchaseData.setText(JTextPurchaseAmnt.getText());
                    RumusDepreciation();
                    //System.out.println(RumusBulanDepreciatioan() + "xxx");
                }
        });
         
         JTextAssetDataDepreciation.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e)
                {
                    JTextAssetPurchaseData.setText(JTextPurchaseAmnt.getText());
                    RumusDepreciation();
                     
                }
        });
         
         JTextAssetAdjustment.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e)
                {
                    JTextAssetPurchaseData.setText(JTextPurchaseAmnt.getText());
                    RumusDepreciation();
                    
                }
        });
         
         JButtonAssetSave.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                 SavedDatabaseAsset();
                 
             }
         });
         
         JButtonAssetDelete.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                 SetAwalInputAsset(false, false, false, true);
                 DeleteDataPenerimaan("delete from sys_asset where asset_no = '"+ AssetNo+"'");
                 AmbilDatabaseBuatTabelFA(AmbilDatePembuatanData(2));
             }
         });
         
         JButtonAssetEdit.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                 SetAwalInputAsset(true, false, false, true);
             }
         });
         
         JButtonAssetPrint.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                 //SetAwalInputAsset(false, false, false, true);
             }
         });
         
         JButtonAssesNew.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                 SetAwalInputAsset(true, false, false, true);
                 SetAwalInptuAsset2();
                 //System.out.println("xxx   sss" + PilihAssetJRadioButtton);
             }
         });
         
         JButtonFARefresh.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                 String PeriodRefresh   = kazaoCalendarDateInputAsset.getKazaoCalendar().getShortDate();
                 
                 SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();
                 N.SetKazaoToBlnIndo(PeriodRefresh);
                 N.SetKazaoToThnIndo(PeriodRefresh);

                 PeriodRefresh = N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
                 AmbilDatabaseBuatTabelFA(PeriodRefresh);
             }
         });
         
          JTableInputFixedAsset.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    IsiDanAmbilDataFA();
                    SetAwalInputAsset(true, true, true, true); 
                    RumusDepreciation();   
                }
            }   
        });
          
        JButtonRadioPilihan(JRadioAssetNormal);
        JButtonRadioPilihan(JRadioAssetLost);
        JButtonRadioPilihan(JRadioAssetClosed);
        JButtonRadioPilihan(JRadioAssetSold); 
        
        /*
         * Depreciation Procces
         */
        JButtonDepreciationProcess.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                 CreateJournalAsset();
            }
        });
     }
   
     /*
      * Untuk action JRadioButton
      */   
     private void JButtonRadioPilihan(JRadioButton x){

         x.addActionListener(new ActionListener(){   
             @Override
             public void actionPerformed(ActionEvent e) {
                  AbstractButton aButton = (AbstractButton) e.getSource();
                  StatusAsset = aButton.getText();  
             }                        
         });        
     }
     
     private void SetAwalInptuAsset2(){
         JTextAssetSearch.setText("");
         JTextAssetPurchaseData.setText("");
         JTextAssetDataAccu.setText("");   
         JTextAssetDataDepreciation.setText("");        
         JTextAssetDataDepreciationBulan.setText("");        
         JTextAssetAdjustment.setText("");        
         JTextAssetDataBookValue.setText("");
         GroupsStatusAsset.clearSelection();
         JTextAssetNo.setText("");
         JTextPurchaseAmnt.setText("");
         JTextAssetKet.setText("");        
         JTextAssetName.setText("");        
         JTextInputAssetUsia.setText("");
         JTextAssetInvoiceNo .setText("");      
         JLabelAssetError.setText("");      
         StatusAsset = "null";                
     }
     
     private void SetAwalInputAsset(boolean Save, boolean Delete, boolean Edit, boolean Print){
         JButtonAssetSave.setEnabled(Save);
         JButtonAssetEdit.setEnabled(Edit);
         JButtonAssetDelete.setEnabled(Delete);
         JButtonAssetPrint.setEnabled(Print);
     }
     
     private void IsiDanAmbilDataFA(){
        int AmbilRow            = JTableInputFixedAsset.getSelectedRow() ;
        this.AssetNo            = (String) ModelTabelInput.getValueAt(AmbilRow , 1);
        InvoiceNoAsset          = (String) ModelTabelInput.getValueAt(AmbilRow , 2);
        NameFa                  = (String) ModelTabelInput.getValueAt(AmbilRow , 4);
        GolAsset                = (String) ModelTabelInput.getValueAt(AmbilRow , 3);
        PurchaseDate2           = (String) ModelTabelInput.getValueAt(AmbilRow , 5);
        
        JComboBoxInputGol.setSelectedItem(GolAsset);        
        JTextAssetNo.setText(AssetNo);
        JTextAssetInvoiceNo.setText(InvoiceNoAsset);
        JTextAssetName.setText(NameFa);
        
       /*
        * Set Tanggal di kazao
        */

        SistemPro.TanggalSistem TglNow = new SistemPro.TanggalSistem(); 
        Calendar Tgl = Calendar.getInstance();

        Tgl.set(TglNow.ConvertTglBlnThnToTahun(PurchaseDate2), TglNow.ConvertTglBlnThnToBulan(PurchaseDate2) - 1, TglNow.ConvertTglBlnThnToTanggal(PurchaseDate2));
        kazaoCalendarDateCreatePurchase.setCalendar(Tgl); 
     
        AmbilDataFA(AssetNo);
    }
    private void AmbilDataFA(String Data){
        int No = 0;       
        ResultSet HQ = null;
        
        try {
            Statement stm = K.createStatement();
            HQ = stm.executeQuery("select asset_no,period, usia, purchase_amount,"
                        + "accum_depreciation, depreciation_month,depreciation_amount, adjustment,status, ket"
                        + " from sys_asset where asset_no = '" + Data + "'");                     

            while(HQ.next()  ){      
                 JTextPurchaseAmnt.setText(String.valueOf(HQ.getInt("purchase_amount")).toString()); 
                 JTextInputAssetUsia.setText(String.valueOf(HQ.getInt("usia")).toString());
                 JTextAssetDataAccu.setText(String.valueOf(HQ.getInt("accum_depreciation")).toString()); 
                 JTextAssetDataDepreciationBulan.setText(String.valueOf(HQ.getInt("depreciation_month")).toString()); 
                 JTextAssetDataDepreciation.setText(String.valueOf(HQ.getInt("depreciation_amount")).toString()); 
                 JTextAssetAdjustment.setText(String.valueOf(HQ.getInt("adjustment")).toString());
                 SetStatusFA(HQ.getString("status"));
                 JTextAssetKet.setText(HQ.getString("ket"));
            }
            JTextAssetPurchaseData.setText( JTextPurchaseAmnt.getText());
        }
        catch (Exception ex){
             JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
        }        
    }
    
    private void SetStatusFA(String Data){       
        switch (Data){
            case "Normal" :
                JRadioAssetNormal.setSelected(true);
            break;
            case "Lost":
                JRadioAssetLost.setSelected(true);
                JTextAssetDataDepreciation.setText("0");
            break;
            case "Closed":
                JRadioAssetClosed.setSelected(true);
                JTextAssetDataDepreciation.setText("0");
            break;
            case "Sold":
                JRadioAssetSold.setSelected(true);
                JTextAssetDataDepreciation.setText("0");
            break;                
        }
    }
    
    private String GetStatusFA(){
        if (JRadioAssetNormal.isSelected()){
            return "Normal";
        }
        else if (JRadioAssetLost.isSelected()){
            return "Lost";
        }
        else if (JRadioAssetClosed.isSelected()){
            return "Closed";
        }
        else if(JRadioAssetSold.isSelected()){
             return "Sold";
        }
        else {
            return "null";
        }
    }
    
    private void RumusDepreciation(){

        DepreciationAmount      = JTextAssetDataDepreciation.getText();
        DepreciationMonth       = String.valueOf(RumusBulanDepreciatioan());
        AdjustmentAsset         = JTextAssetAdjustment.getText();
        String BookValueAsset   = JTextAssetDataBookValue.getText();       
        Usia                    = JTextInputAssetUsia.getText();  
        /*
         * Biar aman data
         */       
        PurchaseAmount  = JTextAssetPurchaseData.getText();
        
        if (DepreciationMonth.equals("")){
            DepreciationMonth   = "1";
            JTextAssetDataDepreciationBulan.setText("1");
        }
        if (DepreciationAmount.equals("")){
            DepreciationAmount  = "0";
        }
        if (AdjustmentAsset.equals("")){
            AdjustmentAsset     = "0";
        }
        if (BookValueAsset.equals("")){
            BookValueAsset      = "0";
        }
        if (PurchaseAmount.equals("")){
            PurchaseAmount      = "0";
        }
        if (Usia.equals("")){
            Usia      = "0";
        }
        
        /*
         * Usia barang dan prediksi barang sama
         */
        if (Integer.valueOf(Usia).intValue() < RumusBulanDepreciatioan() ){
            JRadioAssetLost.setSelected(true);
            JLabelAssetError.setText("Asset sudah melebihi batas umur");
        }

        
       /* 
        * Rumus FA Depreciation
        * Book Value   = ( dep bulan * dep )
        * Accum Dep    = FA Depreciation - Adjustment - Book Value 
        */
        int BookValue   = Integer.valueOf(DepreciationMonth).intValue() *
                          Integer.valueOf(DepreciationAmount).intValue();
        int AccumDep2   = Integer.valueOf(PurchaseAmount).intValue() -
                          Integer.valueOf(AdjustmentAsset).intValue() -
                          BookValue  ;
        JTextAssetDataAccu.setText(AccumDep2+"");
        JTextAssetDataBookValue.setText(BookValue+"");
        JTextAssetDataDepreciationBulan.setText(DepreciationMonth);
        
    }
    
    private int RumusBulanDepreciatioan(){
        /*
          Periode dari create date purchase Ex 201401
          AmbilDatePembuatanData(2);       
          period now dari computer
          AmbilCreatedDate(2);
        * */
        int Bln1    = Integer.valueOf(AmbilCreatedDate(2).substring(4)).intValue();
        int Bln2    = Integer.valueOf(AmbilDatePembuatanData(2).substring(4)).intValue();  
        
        int Tahun1  = Integer.valueOf(AmbilCreatedDate(2).substring(0,4)).intValue();
        int Tahun2  = Integer.valueOf(AmbilDatePembuatanData(2).substring(0,4)).intValue(); 
        
        /*
         * Untuk Depreciation bulan
         */
        
        if (Tahun2 == Tahun1){
            if (Bln1 < Bln2){
                return 0;
            }
            else {
                return Bln1 - Bln2;
            }
        }
        else if (Tahun2 < Tahun1){
            int JarakBln2Ke0 = 12- Bln2;
            int JaraKeTahun  = Tahun1 - Tahun2;
            if ((JaraKeTahun ) > 1){
                return (12 * ( JaraKeTahun -1 ) ) + JarakBln2Ke0 + Bln1;
            }
            else if((JaraKeTahun ) == 1 ) {
                return JarakBln2Ke0 + Bln1;
            }
            else{
                return 0;
            }
        }
        else{
            return 0;
        }
        
    }
     
      private void TampilanJTableInputFixedAsset(){
        String header[] = {"No", "Asset No","Invoice No","Gol","Name FA","Purchase Date", "Last Updated"};
        ModelTabelInput = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                    //if(colIndex == 1) {return true ;} //  nilai false agar tidak bisa di edit
                    //if(colIndex == 6) {return JTabelEditPenerimaan ;}
                    if(colIndex == 4) {return JTabelEditgpGol;}
                    if(colIndex == 2) {return JTabelEditgpGol ;}
                    //if(colIndex == 3) {return EditKolomQty ;}
                    return false;   //Disallow the editing of any cell
            }
        };
        JTableInputFixedAsset.setModel(ModelTabelInput);
        
        /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(ModelTabelInput);
       JTableInputFixedAsset.setRowSorter(sorter);
         
        JTextAssetSearch.addKeyListener(new KeyListener(){
            @Override
            public void keyReleased(KeyEvent e) {
               String text = JTextAssetSearch.getText();
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
        JTableInputFixedAsset.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTableInputFixedAsset.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTableInputFixedAsset.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        JTableInputFixedAsset.getColumnModel().getColumn(3).setCellRenderer( tengah );
        JTableInputFixedAsset.getColumnModel().getColumn(4).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(3).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(7).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         */      
        int jarak_colom[] = {30,100,100,200,150, 100, 100};
        Colom_table ukuran_colom = new Colom_table();
        ukuran_colom.ukuran_colom(JTableInputFixedAsset, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        //JTableInputFixedAsset.getColumnModel().getColumn(7).setCellRenderer(  new ButtonJTable("Delete"));
       // JTableInputFixedAsset.getColumnModel().getColumn(7).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),ModelTabelInput, JTableInputFixedAsset));
       
        /*
         * Disable drag colum tabel
         */       
        JTableInputFixedAsset.getTableHeader().setReorderingAllowed(false);
        
        /*
         * Jika ada perubahan data pada cell jtable
         */
        ModelTabelInput.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent tme) {
                
            }
        });
    }
      
      private void AmbilBuatJComboBoxInputGol(){
          
        /*
         * Isi data ke Tabel dari database
         */      
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT updated_date, nama_golongan from sys_gol_asset order by updated_date desc  ");              
               
               while(HQ.next()  ){
                   String NamaGol   = HQ.getString("nama_golongan");
                   JComboBoxInputGol.addItem(NamaGol);
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
   }
      private void AmbilDataDulu (){
          this.PurchaseAmount    = JTextPurchaseAmnt.getText();
          this.AccumDep          = JTextAssetDataAccu.getText();
          this.DepreciationAmount= JTextAssetDataDepreciation.getText();
          this.DepreciationMonth = JTextAssetDataDepreciationBulan.getText();
          this.AdjustmentAsset   = JTextAssetAdjustment.getText();
          this.AssetNo           = JTextAssetInvoiceNo.getText();
          this.KetAsset          = JTextAssetKet.getText();
          this.Usia              = JTextInputAssetUsia.getText();
          //this.StatusAsset       = GroupsStatusAsset.getSelection().toString();
          this.NameFa            = JTextAssetName.getText();
          this.InvoiceNoAsset    = JTextAssetInvoiceNo.getText();
          this.GolAsset          = (String) JComboBoxInputGol.getSelectedItem();
          
          /*
           * Ambil tanggal pembelian asset
           */
          this.PurchaseDate      = kazaoCalendarDateCreatePurchase.getKazaoCalendar().getShortDate();
          SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();

          N.SetKazaoToTglIndo(PurchaseDate);
          N.SetKazaoToBlnIndo(PurchaseDate);
          N.SetKazaoToThnIndo(PurchaseDate);

          this.PurchaseDate2     = N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
      }
      
      private boolean ValidasiForm1(String[] data){

          for (int a = 0 ; a < data.length; a ++ ){
              //System.out.println(data[a]);
                if (data[a].equals("")||
                    data[a].equalsIgnoreCase("0")||
                    data[a].equalsIgnoreCase("Null")){
                    JLabelAssetError.setText("Tidak berhasil di save  " + data[a]);
                return false;
                }               
          }
        return true;
      }
      
      private void ValidasiForm2(){
            if (DepreciationAmount.equals("")){
                this.DepreciationAmount = "0";
            }
            if (DepreciationMonth.equals("")){
                this.DepreciationMonth   = "0";
            }
            if (StatusAsset.equals("null")){
                this.StatusAsset        = GetStatusFA();
            }
      }
      
      /*
       * Tidak di pake
       */      
      private String Periode1(){
       /*
        * Ambil tanggal dari kzao kalender
        */
        String date_po = kazaoCalendarDateCreatePurchase.getKazaoCalendar().getShortDate();
        SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();

        N.SetKazaoToTglIndo(date_po);
        N.SetKazaoToBlnIndo(date_po);
        N.SetKazaoToThnIndo(date_po);

        return N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
      }
      
      private String AmbilDatePembuatanData(int a){
       /*
        * Ambil tanggal dari kzao kalender
        * Jika int a = 1 maka 31-01-2014
        * jika int a = 2 maka 201401
        */
        String date_po = kazaoCalendarDateCreatePurchase.getKazaoCalendar().getShortDate();
        SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();
        
        JOptionPane.showMessageDialog(null, date_po);

        N.SetKazaoToTglIndo(date_po);
        N.SetKazaoToBlnIndo(date_po);
        N.SetKazaoToThnIndo(date_po);
        
        if (a == 1){
            return N.GetTglIndoStrKazao() + "-"+N.GetBlnIndoStrKazao()+ "-"+N.GetThnIndoStKazao();
        }
        else if (a == 2){
            return N.GetThnIndoStKazao()+ N.GetBlnIndoStrKazao();
        }
        else {
            return null;
        }       
      }
      
      private String AmbilCreatedDate(int a){
          /*
           * Ambil tanggal dari System Comp
            * Jika int a = 1 maka 31-01-2014
            * jika int a = 2 maka 201401
           */

          SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();
          N.SetBulanSis();
          N.SetTahunSis();
          N.SetTanggalSis();
         
          if (a == 1){
                return N.GetTanggalSisString() + "-"+ N.GetBulanSisString() + "-" + N.GetTahunSisString();
           }
          else if (a == 2){
                return N.GetTahunSisString()+ N.GetBulanSisString();
           }
          else {
                return null;
           } 
      }
      
      private String TransNoAsset(){

          //SetTransNoPo ( String JnsData, String SelectKolomTransNo, String SelectTabelPeriod, String FromTabel, String WhereSyarat)
          NoAsset.SetTransNoPo("FA", "asset_no", "period", "sys_asset", AmbilDatePembuatanData(2));
          return NoAsset.GetTransNoPo();
      }
      
      private void SavedDatabaseAsset(){
          AmbilDataDulu();
          
          ValidasiForm2();
          
          String TransNo;
          
          if (JTextAssetNo.equals("")){
              TransNo    = TransNoAsset();
          }
          else {
              TransNo   = JTextAssetNo.getText();
          }
                 
          String[] Data     = {StatusAsset, PurchaseAmount,AccumDep, AssetNo, KetAsset,Usia,NameFa, InvoiceNoAsset  };
          
          if ( ValidasiForm1(Data) == true){ 
            try {
                
                /*
                 * Hapus jika tidak bisa save di detail_resep
                 * 
                 */
                DeleteDataPenerimaan("delete from sys_asset where asset_no = '"+ JTextAssetNo.getText()+"'");
                
                Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                Stm.executeUpdate("INSERT INTO  sys_asset  "
                        + "(key_no, asset_no, invoice_no,period, name_fa, usia, purchase_amount, gol,"
                        + "accum_depreciation, depreciation_month,depreciation_amount, adjustment,status, ket,"
                        + "purchase_date,create_date,updated_date )VALUES ('"
                        +  NoAsset.GetNoUrut()+ "','"+TransNo + "','"+InvoiceNoAsset
                        + "','"+PurchaseDate2 + "','" + NameFa + "','"+Usia+ "','" + PurchaseAmount 
                        + "','"+ GolAsset + "','"+ AccumDep+ "','"+ DepreciationMonth
                        + "','"+ DepreciationAmount + "','"+ AdjustmentAsset + "','"+ StatusAsset
                        + "','"+ KetAsset+ "','"+ AmbilDatePembuatanData(1) + "','"+ AmbilCreatedDate(1)                               
                        + "', now())"); 
                AmbilDatabaseBuatTabelFA(AmbilDatePembuatanData(2));
                SetAwalInputAsset(false, false, false, true);
             }
             catch (Exception Ex){
                System.out.println(Ex);
                JOptionPane.showMessageDialog(null,  "InputFixAsset.java : error : 1224 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
                }              
             }
          else{
              SetAwalInputAsset(true, false, false, true);
              
          }
      }
      private void AmbilDatabaseBuatTabelFA(String Period){
          HapusDataJTabel(JTableInputFixedAsset);
        /*
         * Isi data ke Tabel dari database
         */      
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               
               //"No", "Gol","Nama Golongan","Usia", "Debit", "Kredit" ,"Create Date", "Action"
               HQ = stm.executeQuery("select asset_no, invoice_no,name_fa, usia,"
                        + " gol,"
                        + "purchase_date,updated_date"
                        + " from sys_asset where period = '"+Period + "' order by updated_date desc  ");              
     
               while(HQ.next()  ){

                   AssetNo      = HQ.getString("asset_no");
                   GolAsset     = HQ.getString("gol");
                   Usia         = String.valueOf(HQ.getInt("usia")).toString();
                   NameFa       = HQ.getString("name_fa");
                   PurchaseDate = HQ.getString("purchase_date");
                   InvoiceNoAsset       = HQ.getString("invoice_no");
                   String LastUpdate    = HQ.getString("updated_date");
                   
                   String[] add         = {HQ.getRow()+ "",AssetNo ,InvoiceNoAsset, 
                       GolAsset, NameFa, PurchaseDate, LastUpdate };
                   ModelTabelInput.addRow(add);      
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
       // JTabelResep.getColumnModel().getColumn(4).setCellRenderer(  new ButtonJTable("Delete"));
        //JTabelResep.getColumnModel().getColumn(4).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelOrder, JTabelResep));   
      }
      
      /*
       * Bagian create database sendiri tiap bulan
       * Asset tiap bulan
       */
      
      private void CreateJournalAsset(){
          
       /*
        * Create dahulu period
        * Create depreciatiion = bulannya
        * Ambil accum dep bulan sebelumnya
        * crate dan Save Accumulation depreciation = bulan kemarin - depreciation bulan ini 
        * Di atas ( akan di jadikan accum dep bulan ini )
        * Purchase date must be disabled
        * mm/yyyy
        */
         String Period1  = dateChooserCombo2.getText().substring(3, 7) + dateChooserCombo2.getText().substring(0, 2);
         
      }
      
       private void AmbilDatabaseFACreateAutomatis(String Period){
         int baris;       
            ResultSet HQ = null;
               try {
                   Statement stm = K.createStatement();
                   HQ = stm.executeQuery("select asset_no, invoice_no,name_fa, usia,"
                            + " gol,"
                            + "purchase_date,updated_date"
                            + " from sys_asset where period = '"+Period + "' order by updated_date desc  ");                                
                   while(HQ.next()  ){
                       AssetNo      = HQ.getString("asset_no");
                       GolAsset     = HQ.getString("gol");
                       Usia         = String.valueOf(HQ.getInt("usia")).toString();
                       NameFa       = HQ.getString("name_fa");
                       PurchaseDate = HQ.getString("purchase_date");
                       InvoiceNoAsset       = HQ.getString("invoice_no");
                       String LastUpdate    = HQ.getString("updated_date");
                       
                       
                       /*
                        * Data dikumpulkan pada Data array ex String[] aaa = {}
                        * di add pada arraylist
                        */
                       String[] add         = {HQ.getRow()+ "",AssetNo ,InvoiceNoAsset, 
                           GolAsset, NameFa, PurchaseDate, LastUpdate };
                       ModelTabelInput.addRow(add);      
                   }
               }
               catch (Exception ex){
                    JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
               }
      }
       
       private void SavedDatabaseFACreateAutomatis(){
          AmbilDataDulu();
          
          ValidasiForm2();
          
          String TransNo;
          
          if (JTextAssetNo.equals("")){
              TransNo    = TransNoAsset();
          }
          else {
              TransNo   = JTextAssetNo.getText();
          }
                 
          String[] Data     = {StatusAsset, PurchaseAmount,AccumDep, AssetNo, KetAsset,Usia,NameFa, InvoiceNoAsset  };
          
          if ( ValidasiForm1(Data) == true){ 
            try {
                
                /*
                 * Hapus jika tidak bisa save di detail_resep
                 * 
                 */
                DeleteDataPenerimaan("delete from sys_asset where asset_no = '"+ JTextAssetNo.getText()+"'");
                
                Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                Stm.executeUpdate("INSERT INTO  sys_asset  "
                        + "(key_no, asset_no, invoice_no,period, name_fa, usia, purchase_amount, gol,"
                        + "accum_depreciation, depreciation_month,depreciation_amount, adjustment,status, ket,"
                        + "purchase_date,create_date,updated_date )VALUES ('"
                        +  NoAsset.GetNoUrut()+ "','"+TransNo + "','"+InvoiceNoAsset
                        + "','"+PurchaseDate2 + "','" + NameFa + "','"+Usia+ "','" + PurchaseAmount 
                        + "','"+ GolAsset + "','"+ AccumDep+ "','"+ DepreciationMonth
                        + "','"+ DepreciationAmount + "','"+ AdjustmentAsset + "','"+ StatusAsset
                        + "','"+ KetAsset+ "','"+ AmbilDatePembuatanData(1) + "','"+ AmbilCreatedDate(1)                               
                        + "', now())"); 
                AmbilDatabaseBuatTabelFA(AmbilDatePembuatanData(2));
                SetAwalInputAsset(false, false, false, true);
             }
             catch (Exception Ex){
                System.out.println(Ex);
                JOptionPane.showMessageDialog(null,  "InputFixAsset.java : error : 1224 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
                }              
             }
          else{
              SetAwalInputAsset(true, false, false, true);
              
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

        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        button1 = new java.awt.Button();
        JTabbedPaneGol = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        kazaoCalendarDateInputAsset = new org.kazao.calendar.KazaoCalendarDate();
        jLabel1 = new javax.swing.JLabel();
        JButtonFARefresh = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        JTextAssetNo = new javax.swing.JTextField();
        JTextAssetKet = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        kazaoCalendarDateCreatePurchase = new org.kazao.calendar.KazaoCalendarDate();
        JComboBoxInputGol = new javax.swing.JComboBox();
        JTextPurchaseAmnt = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        JTextAssetInvoiceNo = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        JTextAssetName = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        JTextInputAssetUsia = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        JTextAssetPurchaseData = new javax.swing.JTextField();
        JTextAssetDataDepreciationBulan = new javax.swing.JTextField();
        JTextAssetAdjustment = new javax.swing.JTextField();
        JTextAssetDataBookValue = new javax.swing.JTextField();
        JTextAssetDataAccu = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        JTextAssetDataDepreciation = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        JRadioAssetNormal = new javax.swing.JRadioButton();
        JRadioAssetSold = new javax.swing.JRadioButton();
        JRadioAssetClosed = new javax.swing.JRadioButton();
        JRadioAssetLost = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        JTableInputFixedAsset = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        JTextAssetSearch = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        JButtonAssetSave = new javax.swing.JButton();
        JButtonAssetEdit = new javax.swing.JButton();
        JButtonAssetDelete = new javax.swing.JButton();
        JButtonAssetPrint = new javax.swing.JButton();
        JButtonAssesNew = new javax.swing.JButton();
        JLabelAssetError = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        Gol = new java.awt.Label();
        JTextFieldFAGol = new java.awt.TextField();
        Gol1 = new java.awt.Label();
        JTextFieldFANamaGol = new java.awt.TextField();
        Gol2 = new java.awt.Label();
        JTextFieldFAUsia = new java.awt.TextField();
        Gol3 = new java.awt.Label();
        Debit = new java.awt.Label();
        Gol5 = new java.awt.Label();
        JComboBoxCOA1 = new javax.swing.JComboBox();
        JComboBoxCOA2 = new javax.swing.JComboBox();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelPesananBahan = new javax.swing.JTable();
        JPanelInputPbBahan = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        JTextPbBahan = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        JTextFieldPbQty = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        JComboBoxSatuan = new javax.swing.JComboBox();
        JButtonAddPb = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        JTextFieldPbHarga = new javax.swing.JTextField();
        JPanelButtonSave = new javax.swing.JPanel();
        JButtonPbSave = new javax.swing.JButton();
        JButtonPbEdit = new javax.swing.JButton();
        JButtonPbDelete = new javax.swing.JButton();
        JButtonPbBrowse = new javax.swing.JButton();
        JButtonPbNew = new javax.swing.JButton();
        JButtonPbPrint = new javax.swing.JButton();
        JPanelPesananHeader = new javax.swing.JPanel();
        JPanelHeader2 = new javax.swing.JPanel();
        JTextFieldTransNo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        JTextFieldPbPasar = new javax.swing.JTextField();
        kazaoCalendarDatePesanan = new org.kazao.calendar.KazaoCalendarDate();
        jLabel15 = new javax.swing.JLabel();
        JComboBoxPbCaraPesan = new javax.swing.JComboBox();
        JLabelPemberitahuanPb = new javax.swing.JLabel();
        JPanelHeaderTotal = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        JTextFieldPbTotal = new javax.swing.JTextField();
        JFormattedTextFieldPbUangMuka = new javax.swing.JFormattedTextField();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTabelPenerimaanBahan = new javax.swing.JTable();
        JPanelPesananHeader1 = new javax.swing.JPanel();
        JPanelHeader3 = new javax.swing.JPanel();
        JTextFieldPenermaanTransNo = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        JTextFieldPenerimaanPasar = new javax.swing.JTextField();
        kazaoCalendarDatePenerimaanPO = new org.kazao.calendar.KazaoCalendarDate();
        jLabel23 = new javax.swing.JLabel();
        JLabelPemberitahuanPenerimaan = new javax.swing.JLabel();
        JTextTransNoPenerimaanUtkPesanan = new javax.swing.JTextField();
        JButtonOpenPesanan = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        kazaoCalendarDatePenerimaanWR = new org.kazao.calendar.KazaoCalendarDate();
        label2 = new java.awt.Label();
        jLabel29 = new javax.swing.JLabel();
        JTextFieldPenerimaanNote = new javax.swing.JTextField();
        JPanelHeaderTotal1 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        JTextFieldPenerimaanTotal1 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        JTextFieldPenerimaanUangMuka = new javax.swing.JTextField();
        JTextFieldPenerimaanKembalian = new javax.swing.JTextField();
        JPanelButtonSave1 = new javax.swing.JPanel();
        JButtonPenerimaanSave = new javax.swing.JButton();
        JButtonPenerimaanEdit = new javax.swing.JButton();
        JButtonPenerimaanDelete = new javax.swing.JButton();
        JButtonPenerimaanBrowse = new javax.swing.JButton();
        JButtonPenerimaanNew = new javax.swing.JButton();
        JButtonPenerimaanPrint = new javax.swing.JButton();
        JButtonFASave = new javax.swing.JButton();
        JButtonFAEdit = new javax.swing.JButton();
        JButtonFANew = new javax.swing.JButton();
        JLabelDebit = new javax.swing.JLabel();
        JLabelKredit = new javax.swing.JLabel();
        JLabelKetGol = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        JTabelTabelGolongan = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        dateChooserCombo2 = new datechooser.beans.DateChooserCombo();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        dateChooserCombo3 = new datechooser.beans.DateChooserCombo();
        JButtonDepreciationProcess = new javax.swing.JButton();

        button1.setLabel("button1");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Fixed Asset");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/logo.png"))); // NOI18N

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        kazaoCalendarDateInputAsset.setFormat("mm/yyyy");

        jLabel1.setText("Period :");

        JButtonFARefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Refresh.png"))); // NOI18N
        JButtonFARefresh.setText("Refesh");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kazaoCalendarDateInputAsset, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonFARefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(kazaoCalendarDateInputAsset, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(JButtonFARefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel44.setText("Asset No");

        jLabel45.setText("Gol");

        JTextAssetNo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTextAssetNo.setEnabled(false);

        JTextAssetKet.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel47.setText("Purchase Date");

        kazaoCalendarDateCreatePurchase.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JComboBoxInputGol.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTextPurchaseAmnt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextPurchaseAmnt.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel49.setText("Purchase Amnt");

        jLabel50.setText("Ket");

        jLabel51.setText("Name FA");

        JTextAssetInvoiceNo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel52.setText("Invoice No");

        JTextAssetName.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel46.setText("Usia");

        JTextInputAssetUsia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        JTextInputAssetUsia.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel48.setText("\\Bulan");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JTextPurchaseAmnt)
                    .addComponent(JComboBoxInputGol, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JTextAssetNo, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(JTextAssetInvoiceNo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel47)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kazaoCalendarDateCreatePurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JTextAssetKet, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(JTextAssetName, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JTextInputAssetUsia, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel48)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(kazaoCalendarDateCreatePurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextAssetNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextAssetInvoiceNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextAssetKet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JComboBoxInputGol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextInputAssetUsia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextPurchaseAmnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JTextAssetName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Summary"));

        jLabel34.setText("Purchase Amnt");

        jLabel35.setText("Acumm. Dep");

        jLabel36.setText("Depreciation");

        jLabel37.setText("Adjustment");

        jLabel38.setText("Book Value");

        JTextAssetPurchaseData.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextAssetPurchaseData.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTextAssetPurchaseData.setEnabled(false);

        JTextAssetDataDepreciationBulan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextAssetDataDepreciationBulan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTextAssetDataDepreciationBulan.setEnabled(false);

        JTextAssetAdjustment.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextAssetAdjustment.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTextAssetDataBookValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextAssetDataBookValue.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTextAssetDataBookValue.setEnabled(false);

        JTextAssetDataAccu.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextAssetDataAccu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTextAssetDataAccu.setEnabled(false);

        jLabel39.setText(":");

        jLabel40.setText(":");

        jLabel41.setText(":");

        jLabel42.setText(":");

        jLabel43.setText(":");

        JTextAssetDataDepreciation.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextAssetDataDepreciation.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(jLabel36)
                    .addComponent(jLabel35)
                    .addComponent(jLabel38)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JTextAssetAdjustment, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JTextAssetPurchaseData, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JTextAssetDataAccu)
                    .addComponent(JTextAssetDataBookValue)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(JTextAssetDataDepreciationBulan, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextAssetDataDepreciation)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(JTextAssetPurchaseData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(JTextAssetDataAccu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(JTextAssetDataDepreciationBulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41)
                    .addComponent(JTextAssetDataDepreciation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(JTextAssetAdjustment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(JTextAssetDataBookValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43)))
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Status"));

        GroupsStatusAsset

        .add(JRadioAssetNormal);
        JRadioAssetNormal.setText("Normal");

        GroupsStatusAsset.add(JRadioAssetSold);
        JRadioAssetSold.setText("Sold");

        GroupsStatusAsset.add(JRadioAssetClosed);
        JRadioAssetClosed.setText("Closed");

        GroupsStatusAsset.add(JRadioAssetLost);
        JRadioAssetLost.setText("Lost");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JRadioAssetNormal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JRadioAssetLost)
                .addGap(18, 18, 18)
                .addComponent(JRadioAssetClosed)
                .addGap(10, 10, 10)
                .addComponent(JRadioAssetSold, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(JRadioAssetNormal)
                .addComponent(JRadioAssetLost)
                .addComponent(JRadioAssetClosed)
                .addComponent(JRadioAssetSold))
        );

        JTableInputFixedAsset.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(JTableInputFixedAsset);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel33.setText("Search :");

        JTextAssetSearch.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextAssetSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(JTextAssetSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JButtonAssetSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Save.png"))); // NOI18N
        JButtonAssetSave.setText("Save");
        JButtonAssetSave.setEnabled(false);

        JButtonAssetEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Update.png"))); // NOI18N
        JButtonAssetEdit.setText("Edit");
        JButtonAssetEdit.setEnabled(false);

        JButtonAssetDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Delete.png"))); // NOI18N
        JButtonAssetDelete.setText("Delete");
        JButtonAssetDelete.setEnabled(false);

        JButtonAssetPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Print.png"))); // NOI18N
        JButtonAssetPrint.setText("Print");

        JButtonAssesNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/New.png"))); // NOI18N
        JButtonAssesNew.setText("New");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JButtonAssetSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JButtonAssetEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JButtonAssetDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonAssetPrint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonAssesNew)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(0, 4, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JButtonAssetPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(JButtonAssesNew, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JButtonAssetDelete, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(JButtonAssetEdit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(JButtonAssetSave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        JLabelAssetError.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        JLabelAssetError.setForeground(new java.awt.Color(255, 0, 0));
        JLabelAssetError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JLabelAssetError, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLabelAssetError, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 50, Short.MAX_VALUE)))
                .addContainerGap())
        );

        JTabbedPaneGol.addTab("Input Fixed Asset", jPanel3);

        jPanel6.setPreferredSize(new java.awt.Dimension(300, 515));

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        Gol.setName("Gol"); // NOI18N
        Gol.setText("Gol");

        JTextFieldFAGol.setEnabled(false);

        Gol1.setName("Gol"); // NOI18N
        Gol1.setText("Nama Golongan");

        JTextFieldFANamaGol.setEnabled(false);

        Gol2.setName("Usia"); // NOI18N
        Gol2.setText("Usia");

        JTextFieldFAUsia.setEnabled(false);

        Gol3.setName("Usia"); // NOI18N
        Gol3.setText("\\ Bulan");

        Debit.setName(""); // NOI18N
        Debit.setText("Debit");

        Gol5.setName("Usia"); // NOI18N
        Gol5.setText("Kredit");

        JComboBoxCOA1.setEnabled(false);
        JComboBoxCOA1.setModel(new javax.swing.DefaultComboBoxModel(app_search_data_fixed_asset.getData().toArray()));

        JComboBoxCOA2.setEnabled(false);

        jInternalFrame1.setClosable(true);
        jInternalFrame1.setIconifiable(true);
        jInternalFrame1.setMaximizable(true);
        jInternalFrame1.setTitle("Pembelian Bahan");

        jScrollPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTabelPesananBahan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(JTabelPesananBahan);

        JPanelInputPbBahan.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setText(" Bahan : ");

        jLabel3.setText("Qty : ");

        jLabel4.setText("Satuan :");

        JComboBoxSatuan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kg (Kilogram)", "g ( Gram )", "Sdm ( Sendok Makan )", "Sdt ( Sendok Teh)", "L ( Litter )" }));

        JButtonAddPb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Apply.png"))); // NOI18N
        JButtonAddPb.setText("Add");

        jLabel14.setText("Harga : ");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextPbBahan, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldPbHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldPbQty, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JComboBoxSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonAddPb, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(JTextPbBahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(JTextFieldPbQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(JComboBoxSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonAddPb)
                    .addComponent(jLabel14)
                    .addComponent(JTextFieldPbHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout JPanelInputPbBahanLayout = new javax.swing.GroupLayout(JPanelInputPbBahan);
        JPanelInputPbBahan.setLayout(JPanelInputPbBahanLayout);
        JPanelInputPbBahanLayout.setHorizontalGroup(
            JPanelInputPbBahanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelInputPbBahanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        JPanelInputPbBahanLayout.setVerticalGroup(
            JPanelInputPbBahanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        JPanelButtonSave.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JButtonPbSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Save.png"))); // NOI18N
        JButtonPbSave.setText("Save");
        JButtonPbSave.setEnabled(false);

        JButtonPbEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Update.png"))); // NOI18N
        JButtonPbEdit.setText("Edit");
        JButtonPbEdit.setEnabled(false);

        JButtonPbDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Delete.png"))); // NOI18N
        JButtonPbDelete.setText("Delete");
        JButtonPbDelete.setEnabled(false);

        JButtonPbBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Search.png"))); // NOI18N
        JButtonPbBrowse.setText("Browse");

        JButtonPbNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonPbNew.setText("New");

        JButtonPbPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPbPrint.setText("Print");
        JButtonPbPrint.setEnabled(false);

        javax.swing.GroupLayout JPanelButtonSaveLayout = new javax.swing.GroupLayout(JPanelButtonSave);
        JPanelButtonSave.setLayout(JPanelButtonSaveLayout);
        JPanelButtonSaveLayout.setHorizontalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSaveLayout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addComponent(JButtonPbSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbBrowse)
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
                    .addComponent(JButtonPbDelete)
                    .addComponent(JButtonPbBrowse)
                    .addComponent(JButtonPbNew)
                    .addComponent(JButtonPbPrint))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        JPanelPesananHeader.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTextFieldTransNo.setEditable(false);
        JTextFieldTransNo.setBackground(new java.awt.Color(204, 204, 204));

        jLabel5.setText("No Transaksi");

        jLabel6.setText("Supplier / Pasar ");

        jLabel7.setText("Tanggal");

        jLabel8.setText(":");

        jLabel9.setText(":");

        jLabel10.setText(":");

        jLabel15.setText("Pilih cara input pemesan :");

        JComboBoxPbCaraPesan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "...", "Manual", "Dari Resep" }));

        JLabelPemberitahuanPb.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout JPanelHeader2Layout = new javax.swing.GroupLayout(JPanelHeader2);
        JPanelHeader2.setLayout(JPanelHeader2Layout);
        JPanelHeader2Layout.setHorizontalGroup(
            JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeader2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader2Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kazaoCalendarDatePesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JComboBoxPbCaraPesan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPanelHeader2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLabelPemberitahuanPb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPanelHeader2Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPbPasar)))
                .addContainerGap())
        );
        JPanelHeader2Layout.setVerticalGroup(
            JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeader2Layout.createSequentialGroup()
                .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JLabelPemberitahuanPb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(JTextFieldTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel9)
                    .addComponent(JTextFieldPbPasar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(jLabel10))
                    .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(kazaoCalendarDatePesanan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(JComboBoxPbCaraPesan, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        JPanelHeaderTotal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel11.setText("Total Biaya");

        jLabel12.setText("Uang Muka");

        jLabel13.setText(":");

        jLabel16.setText(":");

        JTextFieldPbTotal.setEditable(false);
        JTextFieldPbTotal.setBackground(new java.awt.Color(204, 204, 204));
        JTextFieldPbTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextFieldPbTotal.setText("0");

        JFormattedTextFieldPbUangMuka.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        JFormattedTextFieldPbUangMuka.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout JPanelHeaderTotalLayout = new javax.swing.GroupLayout(JPanelHeaderTotal);
        JPanelHeaderTotal.setLayout(JPanelHeaderTotalLayout);
        JPanelHeaderTotalLayout.setHorizontalGroup(
            JPanelHeaderTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeaderTotalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelHeaderTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeaderTotalLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JFormattedTextFieldPbUangMuka))
                    .addGroup(JPanelHeaderTotalLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPbTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)))
                .addContainerGap())
        );
        JPanelHeaderTotalLayout.setVerticalGroup(
            JPanelHeaderTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeaderTotalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelHeaderTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13)
                    .addComponent(JTextFieldPbTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeaderTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel16)
                    .addComponent(JFormattedTextFieldPbUangMuka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout JPanelPesananHeaderLayout = new javax.swing.GroupLayout(JPanelPesananHeader);
        JPanelPesananHeader.setLayout(JPanelPesananHeaderLayout);
        JPanelPesananHeaderLayout.setHorizontalGroup(
            JPanelPesananHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelPesananHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPanelHeader2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelHeaderTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        JPanelPesananHeaderLayout.setVerticalGroup(
            JPanelPesananHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelPesananHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelPesananHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JPanelHeader2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelHeaderTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JPanelInputPbBahan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelPesananHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(JPanelButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelInputPbBahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelPesananHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(JPanelButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Pemesanan Bahan", jPanel8);

        jScrollPane2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTabelPenerimaanBahan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(JTabelPenerimaanBahan);

        JPanelPesananHeader1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTextFieldPenermaanTransNo.setEditable(false);
        JTextFieldPenermaanTransNo.setBackground(new java.awt.Color(204, 204, 204));

        jLabel17.setText("No Transaksi");

        jLabel18.setText("Supplier / Pasar ");

        jLabel19.setText("Tanggal PO");

        jLabel20.setText(":");

        jLabel21.setText(":");

        jLabel22.setText(":");

        JTextFieldPenerimaanPasar.setEditable(false);
        JTextFieldPenerimaanPasar.setBackground(new java.awt.Color(204, 204, 204));

        kazaoCalendarDatePenerimaanPO.setEditable(false);

        jLabel23.setText("No PO :");

        JLabelPemberitahuanPenerimaan.setForeground(new java.awt.Color(255, 0, 0));

        JTextTransNoPenerimaanUtkPesanan.setEditable(false);
        JTextTransNoPenerimaanUtkPesanan.setBackground(new java.awt.Color(204, 204, 204));

        JButtonOpenPesanan.setText("Open PO");

        jLabel27.setText("Tanggal WR");

        jLabel28.setText(":");

        kazaoCalendarDatePenerimaanWR.setEditable(false);

        label2.setText("Note");

        jLabel29.setText(":");

        javax.swing.GroupLayout JPanelHeader3Layout = new javax.swing.GroupLayout(JPanelHeader3);
        JPanelHeader3.setLayout(JPanelHeader3Layout);
        JPanelHeader3Layout.setHorizontalGroup(
            JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeader3Layout.createSequentialGroup()
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kazaoCalendarDatePenerimaanPO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextTransNoPenerimaanUtkPesanan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonOpenPesanan))
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(kazaoCalendarDatePenerimaanWR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(JTextFieldPenerimaanNote))
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPanelHeader3Layout.createSequentialGroup()
                                .addComponent(JTextFieldPenermaanTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JLabelPemberitahuanPenerimaan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(JTextFieldPenerimaanPasar))))
                .addContainerGap())
        );
        JPanelHeader3Layout.setVerticalGroup(
            JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeader3Layout.createSequentialGroup()
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JLabelPemberitahuanPenerimaan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17)
                        .addComponent(JTextFieldPenermaanTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20)))
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel21)))
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPenerimaanPasar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(jLabel22))
                    .addComponent(kazaoCalendarDatePenerimaanPO, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel23)
                        .addComponent(JTextTransNoPenerimaanUtkPesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JButtonOpenPesanan)))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelHeader3Layout.createSequentialGroup()
                        .addComponent(kazaoCalendarDatePenerimaanWR, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel29)
                        .addComponent(JTextFieldPenerimaanNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        JPanelHeaderTotal1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel24.setText("Total Biaya");

        jLabel25.setText("Uang Muka");

        jLabel26.setText(":");

        jLabel30.setText(":");

        JTextFieldPenerimaanTotal1.setEditable(false);
        JTextFieldPenerimaanTotal1.setBackground(new java.awt.Color(204, 204, 204));
        JTextFieldPenerimaanTotal1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextFieldPenerimaanTotal1.setText("0");

        jLabel31.setText("Kembalian");

        jLabel32.setText(":");

        JTextFieldPenerimaanUangMuka.setEditable(false);
        JTextFieldPenerimaanUangMuka.setBackground(new java.awt.Color(204, 204, 204));
        JTextFieldPenerimaanUangMuka.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextFieldPenerimaanUangMuka.setText("0");

        JTextFieldPenerimaanKembalian.setEditable(false);
        JTextFieldPenerimaanKembalian.setBackground(new java.awt.Color(204, 204, 204));
        JTextFieldPenerimaanKembalian.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextFieldPenerimaanKembalian.setText("0");
        JTextFieldPenerimaanKembalian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTextFieldPenerimaanKembalianActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JPanelHeaderTotal1Layout = new javax.swing.GroupLayout(JPanelHeaderTotal1);
        JPanelHeaderTotal1.setLayout(JPanelHeaderTotal1Layout);
        JPanelHeaderTotal1Layout.setHorizontalGroup(
            JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPenerimaanUangMuka, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                    .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPenerimaanKembalian, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                    .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPenerimaanTotal1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)))
                .addContainerGap())
        );
        JPanelHeaderTotal1Layout.setVerticalGroup(
            JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel26)
                    .addComponent(JTextFieldPenerimaanTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel30)
                    .addComponent(JTextFieldPenerimaanUangMuka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jLabel32)
                    .addComponent(JTextFieldPenerimaanKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout JPanelPesananHeader1Layout = new javax.swing.GroupLayout(JPanelPesananHeader1);
        JPanelPesananHeader1.setLayout(JPanelPesananHeader1Layout);
        JPanelPesananHeader1Layout.setHorizontalGroup(
            JPanelPesananHeader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelPesananHeader1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPanelHeader3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelHeaderTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        JPanelPesananHeader1Layout.setVerticalGroup(
            JPanelPesananHeader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelPesananHeader1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JPanelPesananHeader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JPanelHeaderTotal1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelHeader3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        JPanelButtonSave1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JButtonPenerimaanSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Save.png"))); // NOI18N
        JButtonPenerimaanSave.setText("Save");
        JButtonPenerimaanSave.setEnabled(false);

        JButtonPenerimaanEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Update.png"))); // NOI18N
        JButtonPenerimaanEdit.setText("Edit");
        JButtonPenerimaanEdit.setEnabled(false);

        JButtonPenerimaanDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Delete.png"))); // NOI18N
        JButtonPenerimaanDelete.setText("Delete");
        JButtonPenerimaanDelete.setEnabled(false);

        JButtonPenerimaanBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Search.png"))); // NOI18N
        JButtonPenerimaanBrowse.setText("Browse");

        JButtonPenerimaanNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonPenerimaanNew.setText("New");

        JButtonPenerimaanPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPenerimaanPrint.setText("Print");
        JButtonPenerimaanPrint.setEnabled(false);

        javax.swing.GroupLayout JPanelButtonSave1Layout = new javax.swing.GroupLayout(JPanelButtonSave1);
        JPanelButtonSave1.setLayout(JPanelButtonSave1Layout);
        JPanelButtonSave1Layout.setHorizontalGroup(
            JPanelButtonSave1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSave1Layout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addComponent(JButtonPenerimaanSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPenerimaanEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPenerimaanDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPenerimaanBrowse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPenerimaanNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPenerimaanPrint)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPanelButtonSave1Layout.setVerticalGroup(
            JPanelButtonSave1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSave1Layout.createSequentialGroup()
                .addGroup(JPanelButtonSave1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JButtonPenerimaanSave)
                    .addComponent(JButtonPenerimaanEdit)
                    .addComponent(JButtonPenerimaanDelete)
                    .addComponent(JButtonPenerimaanBrowse)
                    .addComponent(JButtonPenerimaanNew)
                    .addComponent(JButtonPenerimaanPrint))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
            .addComponent(JPanelButtonSave1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPanelPesananHeader1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelPesananHeader1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JPanelButtonSave1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Penerimaan Bahan", jPanel10);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        JButtonFASave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Save.png"))); // NOI18N
        JButtonFASave.setText("Save");
        JButtonFASave.setEnabled(false);

        JButtonFAEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Update.png"))); // NOI18N
        JButtonFAEdit.setText("Edit");
        JButtonFAEdit.setEnabled(false);

        JButtonFANew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonFANew.setText("New");

        JLabelDebit.setForeground(new java.awt.Color(255, 0, 0));

        JLabelKredit.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(JButtonFASave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonFAEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButtonFANew)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Gol2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(Gol, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(Gol1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(Gol5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(Debit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(JLabelDebit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(JLabelKredit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JComboBoxCOA2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(JTextFieldFAGol, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(JTextFieldFAUsia, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Gol3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(29, 29, 29))
                            .addComponent(JTextFieldFANamaGol, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JComboBoxCOA1, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 148, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 148, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Gol3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Gol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JTextFieldFAGol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(Gol1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JTextFieldFANamaGol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(Gol2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JTextFieldFAUsia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Debit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JLabelDebit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(JComboBoxCOA1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(Gol5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JComboBoxCOA2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JLabelKredit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JButtonFASave)
                    .addComponent(JButtonFAEdit)
                    .addComponent(JButtonFANew))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 107, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 107, Short.MAX_VALUE)))
        );

        JComboBoxCOA2.setModel(new javax.swing.DefaultComboBoxModel(app_search_data_fixed_asset.getData().toArray()));

        JLabelKetGol.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JLabelKetGol, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JLabelKetGol, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 136, Short.MAX_VALUE))
        );

        jSplitPane2.setLeftComponent(jPanel6);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTabelTabelGolongan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTabelTabelGolongan.setName("JTabelTabelGolongan"); // NOI18N
        jScrollPane3.setViewportView(JTabelTabelGolongan);

        jSplitPane2.setRightComponent(jScrollPane3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 867, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );

        JTabbedPaneGol.addTab("Tabel Golongan", jPanel2);

        jPanel15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

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
    dateChooserCombo2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
        (java.awt.Color)null,
        (java.awt.Color)null,
        (java.awt.Color)null,
        (java.awt.Color)null));
dateChooserCombo2.setLocale(new java.util.Locale("in", "ID", ""));

jLabel53.setText("Period");

jLabel54.setText("Until");

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
dateChooserCombo3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
(java.awt.Color)null,
(java.awt.Color)null,
(java.awt.Color)null,
(java.awt.Color)null));
dateChooserCombo3.setLocale(new java.util.Locale("in", "ID", ""));

JButtonDepreciationProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/OK.png"))); // NOI18N
JButtonDepreciationProcess.setText("Process");

javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
jPanel15.setLayout(jPanel15Layout);
jPanel15Layout.setHorizontalGroup(
    jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(jPanel15Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JButtonDepreciationProcess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateChooserCombo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(dateChooserCombo3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        .addContainerGap())
    );
    jPanel15Layout.setVerticalGroup(
        jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel15Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel53)
                .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel54)
                .addComponent(dateChooserCombo3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(JButtonDepreciationProcess)
            .addContainerGap(286, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel5Layout.createSequentialGroup()
            .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 654, Short.MAX_VALUE))
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    JTabbedPaneGol.addTab("Depreciation Process", jPanel5);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(JTabbedPaneGol)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(JTabbedPaneGol)
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JTextFieldPenerimaanKembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTextFieldPenerimaanKembalianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTextFieldPenerimaanKembalianActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Label Debit;
    private java.awt.Label Gol;
    private java.awt.Label Gol1;
    private java.awt.Label Gol2;
    private java.awt.Label Gol3;
    private java.awt.Label Gol5;
    private javax.swing.JButton JButtonAddPb;
    private javax.swing.JButton JButtonAssesNew;
    private javax.swing.JButton JButtonAssetDelete;
    private javax.swing.JButton JButtonAssetEdit;
    private javax.swing.JButton JButtonAssetPrint;
    private javax.swing.JButton JButtonAssetSave;
    private javax.swing.JButton JButtonDepreciationProcess;
    private javax.swing.JButton JButtonFAEdit;
    private javax.swing.JButton JButtonFANew;
    private javax.swing.JButton JButtonFARefresh;
    private javax.swing.JButton JButtonFASave;
    private javax.swing.JButton JButtonOpenPesanan;
    private javax.swing.JButton JButtonPbBrowse;
    private javax.swing.JButton JButtonPbDelete;
    private javax.swing.JButton JButtonPbEdit;
    private javax.swing.JButton JButtonPbNew;
    private javax.swing.JButton JButtonPbPrint;
    private javax.swing.JButton JButtonPbSave;
    private javax.swing.JButton JButtonPenerimaanBrowse;
    private javax.swing.JButton JButtonPenerimaanDelete;
    private javax.swing.JButton JButtonPenerimaanEdit;
    private javax.swing.JButton JButtonPenerimaanNew;
    private javax.swing.JButton JButtonPenerimaanPrint;
    private javax.swing.JButton JButtonPenerimaanSave;
    private javax.swing.JComboBox JComboBoxCOA1;
    private javax.swing.JComboBox JComboBoxCOA2;
    private javax.swing.JComboBox JComboBoxInputGol;
    private javax.swing.JComboBox JComboBoxPbCaraPesan;
    private javax.swing.JComboBox JComboBoxSatuan;
    private javax.swing.JFormattedTextField JFormattedTextFieldPbUangMuka;
    private javax.swing.JLabel JLabelAssetError;
    private javax.swing.JLabel JLabelDebit;
    private javax.swing.JLabel JLabelKetGol;
    private javax.swing.JLabel JLabelKredit;
    private javax.swing.JLabel JLabelPemberitahuanPb;
    private javax.swing.JLabel JLabelPemberitahuanPenerimaan;
    private javax.swing.JPanel JPanelButtonSave;
    private javax.swing.JPanel JPanelButtonSave1;
    private javax.swing.JPanel JPanelHeader2;
    private javax.swing.JPanel JPanelHeader3;
    private javax.swing.JPanel JPanelHeaderTotal;
    private javax.swing.JPanel JPanelHeaderTotal1;
    private javax.swing.JPanel JPanelInputPbBahan;
    private javax.swing.JPanel JPanelPesananHeader;
    private javax.swing.JPanel JPanelPesananHeader1;
    private javax.swing.JRadioButton JRadioAssetClosed;
    private javax.swing.JRadioButton JRadioAssetLost;
    private javax.swing.JRadioButton JRadioAssetNormal;
    private javax.swing.JRadioButton JRadioAssetSold;
    private javax.swing.JTabbedPane JTabbedPaneGol;
    private javax.swing.JTable JTabelPenerimaanBahan;
    private javax.swing.JTable JTabelPesananBahan;
    private javax.swing.JTable JTabelTabelGolongan;
    private javax.swing.JTable JTableInputFixedAsset;
    private javax.swing.JTextField JTextAssetAdjustment;
    private javax.swing.JTextField JTextAssetDataAccu;
    private javax.swing.JTextField JTextAssetDataBookValue;
    private javax.swing.JTextField JTextAssetDataDepreciation;
    private javax.swing.JTextField JTextAssetDataDepreciationBulan;
    private javax.swing.JTextField JTextAssetInvoiceNo;
    private javax.swing.JTextField JTextAssetKet;
    private javax.swing.JTextField JTextAssetName;
    private javax.swing.JTextField JTextAssetNo;
    private javax.swing.JTextField JTextAssetPurchaseData;
    private javax.swing.JTextField JTextAssetSearch;
    private java.awt.TextField JTextFieldFAGol;
    private java.awt.TextField JTextFieldFANamaGol;
    private java.awt.TextField JTextFieldFAUsia;
    private javax.swing.JTextField JTextFieldPbHarga;
    private javax.swing.JTextField JTextFieldPbPasar;
    private javax.swing.JTextField JTextFieldPbQty;
    private javax.swing.JTextField JTextFieldPbTotal;
    private javax.swing.JTextField JTextFieldPenerimaanKembalian;
    private javax.swing.JTextField JTextFieldPenerimaanNote;
    private javax.swing.JTextField JTextFieldPenerimaanPasar;
    private javax.swing.JTextField JTextFieldPenerimaanTotal1;
    private javax.swing.JTextField JTextFieldPenerimaanUangMuka;
    private javax.swing.JTextField JTextFieldPenermaanTransNo;
    private javax.swing.JTextField JTextFieldTransNo;
    private javax.swing.JTextField JTextInputAssetUsia;
    private javax.swing.JTextField JTextPbBahan;
    private javax.swing.JTextField JTextPurchaseAmnt;
    private javax.swing.JTextField JTextTransNoPenerimaanUtkPesanan;
    private java.awt.Button button1;
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private datechooser.beans.DateChooserCombo dateChooserCombo2;
    private datechooser.beans.DateChooserCombo dateChooserCombo3;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDateCreatePurchase;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDateInputAsset;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDatePenerimaanPO;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDatePenerimaanWR;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDatePesanan;
    private java.awt.Label label2;
    // End of variables declaration//GEN-END:variables
}
