/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.ButtonJTableKeDua;
import SistemPro.Colom_table;
import SistemPro.ObjectPenerimaanDatatabse;
import SistemPro.PembelianObject;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.ResepObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Calendar;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author LANTAI3
 */
public class PembelianBahan extends javax.swing.JInternalFrame {
    
    private DefaultTableModel TabelModelPb;
    private DefaultTableModel TabelModelPenerimaan;
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah = new RenderingTengah();
    private String TglBuatResep;
    private String TglBuatWr;
    
    private SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    private Connection K = KD.createConnection();
    private boolean JTabelEdit;
    private boolean JTabelEditPenerimaan =false;
    /**
     * Creates new form PembelianBahan
     */
    public PembelianBahan() { 

        initComponents();
        AksiAksi();
        SetTampilanReset(false, true, true);
        SetTampilanUmum();
        TabelPesananBahan();
        if (JTabelPesananBahan.isEditing()) {             
             System.out.println("yyyyyyyyyyyyy ");
       JTabelPesananBahan.getCellEditor().stopCellEditing();
     }
        /*
         * Penerimaan Pesanan Bahan
         */
        AksiPenerimaanBahan();
        TabelPenerimaanBahan();
        KembalianPenerimaan();
             
    }
    

    private void Printerr(){  
    
        PembelianBahanPrintView PrintPembelian =    new PembelianBahanPrintView(JTextFieldTransNo.getText(), TglBuatResep);

    }
    private void AksiAksi(){
       
        
       
        JButtonPbBrowse.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {    
                SetTampilanReset(false, false, false);
                ViewPembelianYangSudahDiBuat();    
                SetTampilanJButton(false, true, true, true,true, true);
                CaraInput = true;
                
            }
        });
        JButtonPbNew.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SetTampilanAksi();
                SetTampilanReset(false, true, true);
                SetTampilanJButton(false, false, false, true,true, false);
                JPanelPesananHeader.setEnabled(true);
                CaraInput = false;
                CallPembelianView  = true;
            }
        });
        
        JButtonAddPb.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SetTampilanJButton(true, false, false, true,true, true);
                JPanelPesananHeader.setEnabled(true);
                JTabelPesananBahan.setEnabled(true);
                AddDiTabelPesananBahan();
            }
        });
        
         JComboBoxPbCaraPesan.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if ( JComboBoxPbCaraPesan.getSelectedItem().equals("Dari Resep") && CallPembelianView  == true){
                    ViewResepYangSudahDiBuat();
                    JComboBoxPbCaraPesan.setEnabled(false); 
                    JTabelPesananBahan.setEnabled(true);
                }
                else if (JComboBoxPbCaraPesan.getSelectedItem().equals("Manual" ) && CallPembelianView  == true){
                    SetTampilanAksi();
                    JComboBoxPbCaraPesan.setEnabled(false);
                }
                else if(JComboBoxPbCaraPesan.getSelectedItem().equals("Dari Resep") ||JComboBoxPbCaraPesan.getSelectedItem().equals("Manual" ) ){
                    
                }
                else  {
                    JOptionPane.showMessageDialog(null, "Pilih cara input \"Manual\" atau \"Dari Resep\" ?");          
                }
            }
        });
         
         JButtonPbSave.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){                
                 TabelResepSaveDatabase(); 
                 JPanelInputPbBahan.setVisible(false);
             }
         });
         
         JButtonPbPrint.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){                
                 SetTampilanJButton(false, false, false, true,true, true);
                 Printerr() ;     
             }
         });
         JButtonPbDelete.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){  
                 DeleteDataResep();
                 SetTampilanReset(false, false, false);
                 SetTampilanJButton(false, false, false, true,true, false);
                 JTabelPesananBahan.setEnabled(true);
                 JPanelPesananHeader.setEnabled(true);
                 SetTampilanJButton(true, false, false, true,true, false);
                 SetTampilanHeader(true);
       
             }
         });
         
         JButtonPbEdit.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){  
                SetTampilanJButton(true, false, false, true,true, false);
                JTabelEditPenerimaan = true;
                JTabelPesananBahan.setEnabled(true);
                SetTampilanHeader(true);
                JComboBoxPbCaraPesan.setEnabled(false);
             }
         });
    
         
         /*
          * Kejadian JTabel   JTabelPesananBahan untuk menghitung biaya
          */
        JTabelPesananBahan.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel =  JTabelPesananBahan.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //TableModelEvent tme = JTabelPesananBahan;
                 PerhitunganTotalPesanan2( TabelModelPb, "TabelModelPb");
            }
             public void tableChanged(TableModelEvent tme) {
                PerhitunganJumlahTotalPesanan( tme,  TabelModelPb, "TabelModelPb");
            }
         });   
        
        /*
         * Kejadian JTabel   JTabelPenerimaan untuk menghitung total biaya
         */
        JTabelPenerimaanBahan.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel2 =  JTabelPenerimaanBahan.getSelectionModel();
        cellSelectionModel2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //TableModelEvent tme = JTabelPesananBahan;
                 PerhitunganTotalPesanan2( TabelModelPenerimaan, "TabelModelPenerimaan");
                  KembalianPenerimaan();
            }
             public void tableChanged(TableModelEvent tme) {
                PerhitunganJumlahTotalPesanan( tme,  TabelModelPenerimaan, "TabelModelPenerimaan");
                 KembalianPenerimaan();
            }
             
         });
        
        /*
         * Bagian Penerimaan
         */
        JButtonPenerimaanSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
            }
        });
    }

   
    private void JTabelUntukPerhituanganAntarKolom(){
         int numRows     = JTabelPesananBahan.getSelectedRows().length;
         int AmbilRow    = JTabelPesananBahan.getSelectedRow() ;
         
         for(int i=0; i<numRows ; i++ ) {
                    
            /*
             * Ambil Data
             */

            String ColumHarga   = (String) TabelModelPb.getValueAt(AmbilRow, 4);
            String ColumQty     = (String) TabelModelPb.getValueAt(AmbilRow, 2);
            
            int ColumTotHrg  = Integer.valueOf(ColumHarga).intValue() * Integer.valueOf(ColumQty).intValue();
            TabelModelPb.setValueAt(ColumTotHrg, AmbilRow, 5);
        }
    }
    private void SetTampilanReset(boolean InputBahan, boolean JTabelPesanaaBahan, boolean JPanelHeader){
        JPanelInputPbBahan.setVisible(InputBahan);
        /*
         * Logika hapus semua data di jtable
         */
        //TabelModelOrder.getDataVector().removeAllElements();
        //TabelModelOrder.fireTableDataChanged();
        DefaultTableModel dtm = (DefaultTableModel) JTabelPesananBahan.getModel();
        dtm.setRowCount(0); 
        JTextPbBahan.setText("");
        JTextFieldPbHarga.setText("");
        JTextFieldPbQty.setText("");
        JTextFieldPbTotal.setText("0");
        JLabelPemberitahuanPb.setText("");
        JTextFieldPbPasar.setText("");
        JFormattedTextFieldPbUangMuka.setText("");
        JTextFieldTransNo.setText("");
        JComboBoxPbCaraPesan.setEnabled(true);
        this.JTabelEdit = false;
        JTabelPesananBahan.setEnabled(InputBahan);    
        SetTampilanHeader( JPanelHeader);
    }
    private void SetTampilanHeader(boolean JPanelHeader){
        JTextFieldPbPasar.setEnabled(JPanelHeader);
        JComboBoxPbCaraPesan.setEnabled(JPanelHeader);
        JFormattedTextFieldPbUangMuka.setEnabled(JPanelHeader);
        kazaoCalendarDatePesanan.setEnabled(JPanelHeader);
    }
    private void SetTampilanAksi(){
        JPanelInputPbBahan.setVisible(true);
        JComboBoxPbCaraPesan.setEnabled(true);  
    }
    private void SetTampilanUmum(){
        SistemPro.ComponentHanyaAngka AntiHuruf = new SistemPro.ComponentHanyaAngka();
        AntiHuruf.SetAntiAngka(JTextFieldPbHarga);
        AntiHuruf.SetAntiAngka(JTextFieldPbQty);

    }
    private void SetTampilanSetelahSave(){
        JTabelPesananBahan.setEnabled(false);
        JTextFieldPbPasar.setEnabled(false);
        JComboBoxPbCaraPesan.setEnabled(false);
        kazaoCalendarDatePesanan.setEnabled(false);
        JFormattedTextFieldPbUangMuka.setEnabled(false);
        JPanelInputPbBahan.setEnabled(false);
    }
    private void SetTampilanJButton(boolean Button, boolean Edit, boolean Delete, boolean Browse, boolean New, boolean Print){
        JButtonPbSave.setEnabled(Button);
        JButtonPbEdit.setEnabled(Edit);
        JButtonPbDelete.setEnabled(Delete);
        JButtonPbBrowse.setEnabled(Browse);
        JButtonPbNew.setEnabled(New);
        JButtonPbPrint.setEnabled(Print);
    }
    
    private void AddDiTabelPesananBahan(){
        String PbBahan      = JTextPbBahan.getText();
        String PbHarga      = JTextFieldPbHarga.getText();
        String PbQty        = JTextFieldPbQty.getText();
        String PbSatuan     = (String) JComboBoxSatuan.getSelectedItem();
        SistemPro.KomaToString Koma = new SistemPro.KomaToString();
        
        /*
         * Tidak ada koma
         */
        java.text.DecimalFormat decimalFormat2 = new java.text.DecimalFormat("#,##0");
        
        /*
         * Pehitungan grand total
         * Hapus Koma
         */
        PerhitunganTotalPesanan2( TabelModelPb, "TabelModelPb");
        
        JFormattedTextField JumlahPorsi  = new JFormattedTextField(NumberFormat.getIntegerInstance());
        
        SistemPro.NoUrut NoUrutan = new   SistemPro.NoUrut();
        
        /*
         * Set data no urut
         */
        int ax = JTabelPesananBahan.getRowCount() ;
        //JOptionPane.showMessageDialog(null, ax);
        if (ax == 0 ){
            NoUrutan.SetNoUrut(String.valueOf(ax).toString());
        }
        else if ( ax > 0) {
            ax = ax - 1;
            String ab = (String) JTabelPesananBahan.getValueAt(ax, 0);
            NoUrutan.SetNoUrut(ab);
        }
        
        /*
         * Validasi
         */       
        if ("".equals(PbBahan)){
             JOptionPane.showMessageDialog(null, "Data bahan tidak ada !", "Perhatian",JOptionPane.ERROR_MESSAGE);           
        }
        else if("".equals(PbBahan)){
            JOptionPane.showMessageDialog(null, "Data qty bahan tidak ada !", "Perhatian",JOptionPane.ERROR_MESSAGE);           
        }
        else if("".equalsIgnoreCase(PbHarga)){
            JOptionPane.showMessageDialog(null, "Data harga bahan tidak ada !", "Perhatian",JOptionPane.ERROR_MESSAGE);      
        }
            
        else {
            
            Object obj[] = new Object[10];        
            obj[0] = NoUrutan.GetNoUrut();
            obj[1] = PbBahan;         
            obj[2] = PbQty;
            obj[3] = PbSatuan;
            obj[4] = decimalFormat2.format(Integer.valueOf(PbHarga).intValue());
            obj[5] = decimalFormat2.format(Integer.valueOf(PbHarga).intValue() * Integer.valueOf(PbQty).intValue());
            
            TabelModelPb.addRow(obj);
        }       
    }
    private void TabelPesananBahan(){
        String header[] = {"No", "Bahan","Qty","Satuan", "Harga", "@Harga" ,"Action"};
        TabelModelPb = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                                if(colIndex == 1) {return true ;} //  nilai false agar tidak bisa di edit
                                if(colIndex == 6) {return true ;}
                                if(colIndex == 4) {return JTabelEdit ;}
                                if(colIndex == 2) {return JTabelEdit ;}
                                //if(colIndex == 3) {return EditKolomQty ;}
                                return false;   //Disallow the editing of any cell
                        }
        };
        JTabelPesananBahan.setModel(TabelModelPb);
       
        /*
         * Rata tengah atau kanan table
         */
        JTabelPesananBahan.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelPesananBahan.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        JTabelPesananBahan.getColumnModel().getColumn(4).setCellRenderer( tengah );
        JTabelPesananBahan.getColumnModel().getColumn(5).setCellRenderer( tengah );
        JTabelPesananBahan.getColumnModel().getColumn(6).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(3).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(7).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         */      
        int jarak_colom[] = {40,300,60,100,100, 100, 80};
        Colom_table ukuran_colom = new Colom_table();
        ukuran_colom.ukuran_colom(JTabelPesananBahan, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTabelPesananBahan.setName("Pesanan Barang");
        JTabelPesananBahan.getColumnModel().getColumn(6).setCellRenderer(  new ButtonJTable("Delete"));
        JTabelPesananBahan.getColumnModel().getColumn(6).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelPb, JTabelPesananBahan));
       
        /*
         * Disable drag colum tabel
         */       
        JTabelPesananBahan.getTableHeader().setReorderingAllowed(false);
        
        /*
         * Jika ada perubahan data pada cell jtable
         */
        TabelModelPb.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent tme) {
                PerhitunganJumlahTotalPesanan( tme, TabelModelPb, "TabelModelPb");
            }
        });
    }
    
    private void  PerhitunganJumlahTotalPesanan(TableModelEvent tme, DefaultTableModel ModelTabel, String NamaModel){
        if (tme.getType() == TableModelEvent.UPDATE) {
                    
            /*
             *  tme.getFirstRow() = row
             *  tme.getColumn() = colum
             */                 
            if ( tme.getColumn() == 4 || tme.getColumn() == 2 ){
                
               /*
                * Pake  koma
                */
                java.text.DecimalFormat decimalFormat2 = new java.text.DecimalFormat("#,##0");

                /*
                 * Filter karakter aneh
                 */
                String a = "[/*\\-\\(\\)<>_\\=+\\.,:\";\'\\\\#\\$&\\^\\}\\{%~`\\|\\[\\]\\!\\?\\@a-zA-Z]";

                String HargaJTabel = String.valueOf( ModelTabel.getValueAt(tme.getFirstRow(),tme.getColumn())).toString();
                String HargaQty    = (String) ModelTabel.getValueAt(tme.getFirstRow(),2);

                HargaJTabel = HargaJTabel.replaceAll(a, "x");
                HargaQty    = HargaQty.replaceAll(a, "x");

                if (HargaQty.contains("x") ||  HargaQty.equals("")  ) {
                    ModelTabel.setValueAt("0", tme.getFirstRow(), 2);
                    getToolkit().beep();
                }

                if (HargaJTabel.contains("x") ||  HargaJTabel.equals("")  ) {
                    ModelTabel.setValueAt("0", tme.getFirstRow(), tme.getColumn());
                    getToolkit().beep();
                }                       
                else {                            
                    int PesananHarga = Integer.valueOf(HargaJTabel).intValue();
                    int PesananQty   = Integer.valueOf(HargaQty ).intValue();
                    
                    /*
                     * Pesananan * qty
                     */
                    ModelTabel.setValueAt(String.valueOf(decimalFormat2.format(PesananHarga * PesananQty)).toString(), tme.getFirstRow(),5);   
                    PerhitunganTotalPesanan2(ModelTabel, NamaModel);
                }               
            }
        }
    }
    
    private void PerhitunganTotalPesanan2(DefaultTableModel ModelTabel, String NamaModel){
         int JumlahRowPesan= ModelTabel.getRowCount();
         
         /*
          * Pake  koma
          */
         java.text.DecimalFormat decimalFormat2 = new java.text.DecimalFormat("#,##0");
         
            int Tot = 0;
            for (int ab = 0 ; ab <= JumlahRowPesan - 1 ; ab++ ){
                                     
                if (ModelTabel.getValueAt(ab, 5) == null){
                    ModelTabel.setValueAt("0", ab, 5);
                }        
                if (ModelTabel.getValueAt(ab, 5).equals("")){
                   ModelTabel.setValueAt("0", ab, 5);
               }
                
                /*
                 * Menghitung jumlah yang harus di beli
                 * Dan Hapus Koma
                 */
                SistemPro.KomaToString Koma = new SistemPro.KomaToString(); 
                Koma.SetHapusKoma(String.valueOf(ModelTabel.getValueAt(ab, 5).toString()));
                Tot = Integer.valueOf(Koma.GetIntTanpaKoma()).intValue() + Tot;
            }
            if (NamaModel.equalsIgnoreCase("TabelModelPenerimaan")){
                JTextFieldPenerimaanTotal1.setText( String.valueOf(decimalFormat2.format(Tot)).toString() );
            }
            else if (NamaModel.equalsIgnoreCase("TabelModelPb")){
                JTextFieldPbTotal.setText( String.valueOf(decimalFormat2.format(Tot)).toString() );
            }
            
    }
    
    /*
     * Ambil JDialog Pembelian
     */
     boolean CallPembelianView = true;
     private void ViewPembelianYangSudahDiBuat(){
        PembelianBahanView CallPembelianBahan = new PembelianBahanView (new javax.swing.JFrame(), true);
        CallPembelianBahan.setVisible(true);
        
        PembelianObject Dt = CallPembelianBahan.GetTableData();  
        try{
        JLabelPemberitahuanPb.setText( Dt.GetPembelianNamaResep());
        
        if (Dt.GetPembelianNamaResep().equals("") || Dt.GetPembelianNamaResep().equals("null")){
            CallPembelianView = false;
            JComboBoxPbCaraPesan.setSelectedItem("Manual");
           
        }
        else {
            CallPembelianView = false;
            JComboBoxPbCaraPesan.setSelectedItem("Dari Resep");
            
        }
            JTextFieldTransNo.setText(Dt.GetPembelianTransNo());
            JTextFieldPbPasar.setText(Dt.GetPembelianSupplier());
            JTextFieldPbTotal.setText(Dt.GetPembelianTotalBiaya());
            JFormattedTextFieldPbUangMuka.setText(Dt.GetPembelianUangMuka());
        
           /*
            * Logika hapus semua data di jtable
            */
        
            DefaultTableModel dtm = (DefaultTableModel) JTabelPesananBahan.getModel();
            dtm.setRowCount(0); 
            AmbilDataDatabaseSetelahGetBrowsePembelian(Dt.GetPembelianTransNo(), TabelModelPb, "detail_trans");   

            /*
             * Set Tanggal di kazao
             */
            this. TglBuatResep     = Dt.GetPembelianTanggalBuat();
            SistemPro.TanggalSistem TglNow = new SistemPro.TanggalSistem(); 
            Calendar Tgl = Calendar.getInstance();

            Tgl.set(TglNow.ConvertTglBlnThnToTahun(TglBuatResep), TglNow.ConvertTglBlnThnToBulan(TglBuatResep) - 1, TglNow.ConvertTglBlnThnToTanggal(TglBuatResep));
            kazaoCalendarDatePesanan.setCalendar(Tgl);  
            
                    
        }  
        catch(Exception X){           
        }
    }
     
      private void AmbilDataDatabaseSetelahGetBrowsePembelian(String PilihData, DefaultTableModel DataModel, String TabelDatabaseName){
       
        /*
         * Isi data ke Tabel dari database
         */      
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT  trans_no, no ,qty,nama_bahan, satuan, harga_satuan, harga_tot from "+ TabelDatabaseName+ " where trans_no = '"+ PilihData + "' order by no asc  ");                     
               baris = HQ.getRow();
               
               while(HQ.next()  ){

                   String No            = String.valueOf(HQ.getInt("no")).toString();
                   String NamaBahan     = HQ.getString("nama_bahan");
                   String Qty           = String.valueOf(HQ.getInt("qty")).toString();
                   String Satuan        = HQ.getString("satuan");
                   String Harga_Satuan  =  String.valueOf(HQ.getInt("harga_satuan")).toString();
                   String Harga_Tot     =  String.valueOf(HQ.getInt("harga_tot")).toString();
                   
                   String[] add         = {No,NamaBahan,Qty,Satuan,Harga_Satuan, Harga_Tot};
                   DataModel.addRow(add);      
               }
                this.JTabelEdit = true;
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
       // JTabelResep.getColumnModel().getColumn(4).setCellRenderer(  new ButtonJTable("Delete"));
        //JTabelResep.getColumnModel().getColumn(4).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelOrder, JTabelResep));   

   }
    
    /*
     * Ambil JDialog resep
     */
    String GetVRNamaResep;
    private void ViewResepYangSudahDiBuat(){
        ResepView   ResepViewSekarang = new ResepView (new javax.swing.JFrame(), true);
        ResepViewSekarang.setVisible(true);
        
        ResepObject Dt = ResepViewSekarang.GetTableData();  
        try{
            this.GetVRNamaResep     = Dt.GetVRNamaResep();
            AmbilDataDatabaseSetelahGetBrowseResep(Dt.GetVRNamaResep());
            SetTampilanJButton(true, false, false, true,true, false);          
        }  
        catch(Exception X){           
        }
    }
    
     private void AmbilDataDatabaseSetelahGetBrowseResep(String PilihData){
       
        /*
         * Isi data ke Tabel dari database
         */      
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT namresep, no ,bahan,qty, satuan from detail_resep where namresep = '"+ PilihData + "' order by no asc  ");              
               baris = HQ.getRow();
               
               while(HQ.next()  ){

                   String No     = String.valueOf(HQ.getInt("no")).toString();
                   String Bahan  = HQ.getString("bahan");
                   String Qty    = String.valueOf(HQ.getInt("qty")).toString();
                   String Satuan = HQ.getString("satuan");
                   
                   String[] add         = {No,Bahan,Qty,Satuan,"", "0"};
                   TabelModelPb.addRow(add);      
               }
                this.JTabelEdit = true;
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
       // JTabelResep.getColumnModel().getColumn(4).setCellRenderer(  new ButtonJTable("Delete"));
        //JTabelResep.getColumnModel().getColumn(4).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelOrder, JTabelResep));   

   }
     private int NoUrut;
     private int NoUrut(){
         return NoUrut;
     }
     private String TransNoPesanan(){
         
       /*
        * Ambil tanggal dari kzao kalender
        */
        String date_po = kazaoCalendarDatePesanan.getKazaoCalendar().getShortDate();
        SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();

        N.SetKazaoToTglIndo(date_po);
        N.SetKazaoToBlnIndo(date_po);
        N.SetKazaoToThnIndo(date_po);

        String DatePo = N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
         /*
          * SELECT trans_no , periode FROM header_pembelian where periode  = WhereSyarat order by  key_no desc limit 0,1
          */        
         SistemPro.TransNo TN = new SistemPro.TransNo();
         TN.SetTransNoPo("P", "trans_no", "periode", "header_pembelian", DatePo);
         this.NoUrut = TN.GetNoUrut();
         return TN.GetTransNoPo();
     }
     private void Peringatan(String Peringatan){
         JOptionPane.showMessageDialog(null, Peringatan, "Kesalahan input data", JOptionPane.INFORMATION_MESSAGE);     
     }
     
     boolean CaraInput = false;
     private boolean ValidasiSebelumDiSave(){
         
        /*
         * Pake  koma
         */
        java.text.DecimalFormat decimalFormat2 = new java.text.DecimalFormat("#,##0");
         
        int Tot = 0;
        int JumlahRowPesan= TabelModelPb.getRowCount();
        for (int ab = 0 ; ab <= JumlahRowPesan - 1 ; ab++ ){

            if (TabelModelPb.getValueAt(ab, 4) == null){
                Peringatan("Nilai tabel baris ke :" + ab +" kolom ke : 4 Data Null");
                return false;
            }        
            if (TabelModelPb.getValueAt(ab, 4).equals("")){
                Peringatan("Nilai tabel baris ke :" + ab +" kolom ke : 4 Data Null");
               return false;
           }
             if (TabelModelPb.getValueAt(ab, 4).equals("0")){
                 Peringatan("Nilai tabel baris ke :" + ab +" kolom ke : 4 Data Null");
               return false;
           }
        }                 
            
         if (JTextFieldPbPasar.getText().equals("")){
             Peringatan("Data pasar tidak ada");
             return false;
         }
         else if(JComboBoxPbCaraPesan.getSelectedItem().equals("...")){
             Peringatan("Salah membuat Pesanan Barang");
             return CaraInput;
         }
         else if (JTextFieldPbTotal.getText().equals("0")){
             Peringatan("Data total kosong");
             return false;
         }
         else if (JFormattedTextFieldPbUangMuka.getText().equals("")){
             Peringatan("Data uang muka kosong");
             return false;       
         }
         else{
             return true;
         }
     }
     private void DeleteDataResep(){
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from header_pembelian where trans_no = '"+ JTextFieldTransNo.getText() + "'");
    }
     
     private void TabelResepSaveDatabase(){
         if (ValidasiSebelumDiSave() == true){
            boolean SaveAtauTidak;
            SistemPro.TransNo TN    = new SistemPro.TransNo();
            String TransNoP         = TransNoPesanan();
            System.out.println(TransNoP);
            System.out.println(NoUrut());
            /*
             * Save Header Resep
             */
            String NoTransPesanan   = JTextFieldTransNo.getText();
            String SupplierPesanan  = JTextFieldPbPasar.getText();
            String PesananTgl       = kazaoCalendarDatePesanan.getKazaoCalendar().getShortDate();

            SistemPro.TanggalSistem KazaoToIndo = new SistemPro.TanggalSistem();
            KazaoToIndo.SetKazaoToTglIndo(PesananTgl  );
            String UserTgl  = KazaoToIndo.GetTglIndoStrKazao();
            KazaoToIndo.SetKazaoToBlnIndo(PesananTgl  );
            String UserBln  = KazaoToIndo.GetBlnIndoStrKazao();
            KazaoToIndo.SetKazaoToThnIndo(PesananTgl );
            String UserThn = KazaoToIndo.GetThnIndoStKazao();
            String TanggalPesanan = UserTgl+"-"+UserBln+"-"+UserThn;
            String Periode          = UserThn+UserBln;

            try {
                    /*
                     * Hapus jika tidak bisa save di detail_resep
                     */
                    DeleteDataResep();

                    /*
                     * Hapus Koma
                     */
                    SistemPro.KomaToString Koma = new SistemPro.KomaToString(); 
                    
                    Koma.SetHapusKoma(JFormattedTextFieldPbUangMuka.getText() );
                    String  UangMuka    =   Koma.GetString();
                    
                    Koma.SetHapusKoma(JTextFieldPbTotal.getText());

                    /*
                     * Save Header
                     */
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    Stm.executeUpdate("INSERT INTO header_pembelian "
                            + "(key_no,trans_no,nama_resep, periode,supplier, total_biaya, uang_muka, created_date, update_date) "
                            + "VALUES('"
                            +  NoUrut() + "','"+TransNoP + "','"+GetVRNamaResep + "','"+Periode  + "','" + JTextFieldPbPasar.getText() + "','" 
                            + Koma.GetIntTanpaKoma() + "','"
                            + UangMuka + "','"+TanggalPesanan + "',"
                            + " now())");     

                    /*
                     * Save DetailPo to database Mysql
                     */
                 try {
                     int a = JTabelPesananBahan.getRowCount() ;
                     Statement stm = K.createStatement();

                     // dbStatement=con.createStatement();
                     for(int i=0;i< a;i++){
                         
                         /*
                          * Ada bug ketika nominal pake pemisah  (,) 
                          * Solusi , dihilangkan dulu ketika akan di save
                          */
                         
                         int no         =Integer.valueOf(JTabelPesananBahan.getValueAt(i, 0).toString()).intValue();
                         String bahan   =JTabelPesananBahan.getValueAt(i, 1).toString();
                         String qty     =JTabelPesananBahan.getValueAt(i, 2).toString();
                         String satuan  =JTabelPesananBahan.getValueAt(i, 3).toString();
                         
                         Koma.SetHapusKoma(JTabelPesananBahan.getValueAt(i, 4).toString());                        
                         String harga   =Koma.GetString();
                         
                         String tothrg = JTabelPesananBahan.getValueAt(i, 5).toString();

                         /*
                          * Hapus koma
                          */
                         Koma.SetHapusKoma(tothrg);
                         stm.executeUpdate("INSERT INTO detail_trans "
                                 + "( no,trans_no, periode, nama_bahan, qty, satuan, harga_satuan, harga_tot, created_date, update_date) VALUES ('"
                                 +no+"','"+ TransNoP+ "','"+ UserBln+ "','"
                                 +bahan+"','"+qty+"','"+satuan+"','"+harga+ "','"+Koma.GetIntTanpaKoma()+"','" + TanggalPesanan+"', now())");
                         }
                     JLabelPemberitahuanPb.setText("Berhasil Di Saved");
                     SetTampilanSetelahSave();
                     JTextFieldTransNo.setText(TransNoP );
                     JButtonPbSave.setEnabled(false);
                     JPanelPesananHeader.setEnabled(false);
                     SetTampilanJButton(false, false, false, true,true, true);
                 }
                 catch (Exception X){

                     /*
                      * Hapus jika tidak bisa save di detail_resep
                      */
                      DeleteDataResep();
                      JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1226 : "  +X, " Error", JOptionPane.ERROR_MESSAGE);
                    }           
                 }
                 catch (Exception Ex){
                    JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1224 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
                }              
             } 
         else{
             JLabelPemberitahuanPb.setText("Tidak berhasil di save");;
         }
    }
     
     /*
      * 
      * 
      * 
      * 
      * 
      * 
      * Bagian Penerimaan Bahan
      * All Code ada dibawah ini
      * 
      * 
      * 
      * 
      * 
      */
     private void AksiPenerimaanBahan(){
         JButtonOpenPesanan.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 ViewPembelianPesanan();
                 SetTampilanPenerimaanButtonSave(true, false, false, true, false, true, true, true, false);
             }
         });
         
         JButtonPenerimaanSave.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 //SetTampilanPenerimaanButtonSave(false, false, false, true, true, true);
                 PenerimaanDatabaseSave();
             }
         });
         
         JButtonPenerimaanNew.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 // Sava, edit, delete, browse,  print, new
                 //SetTampilanPenerimaanButtonSave(false, false, false, true, false, true);
                 SetResetPenerimaan();
             }
         });
         
         JButtonPenerimaanEdit.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 // Sava, edit, delete, browse,  print, new
                 SetTampilanPenerimaanButtonSave(true, false, false, true, false, true, true, true, false);
             }
         });
         
         JButtonPenerimaanDelete.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 // Sava, edit, delete, browse,  print, new
                 SetTampilanPenerimaanButtonSave(false, false, false, true, false, true, false, false, false);
                 DeleteDataPenerimaan();
             }
         });
         
         JButtonPenerimaanBrowse.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 ViewDataPembelianPenerimaan();
                 // Save, edit, delete, browse,  print, new
                 SetTampilanPenerimaanButtonSave(false, false, false, true, true, true, false, false, false);
             }
         });
         
         JButtonPenerimaanPrint.addActionListener(new ActionListener(){
             public void actionPerformed (ActionEvent e){
                 PrinterPenerimaan();
                 // Save, edit, delete, browse,  print, new
                 SetTampilanPenerimaanButtonSave(false, false, false, true, false, true, false, false, false);
             }
         });
         
    
         
     } 
     
    /*
     * Ambil JDialog Pembelian untuk penerimaan bahan
     */
     private String NamaResepPenerimaan;
     private void ViewPembelianPesanan(){
        PembelianBahanView CallPembelianBahan = new PembelianBahanView (new javax.swing.JFrame(), true);
        CallPembelianBahan.setVisible(true);
        
        PembelianObject Dt = CallPembelianBahan.GetTableData();  

        
        
        try{
            
            JTextTransNoPenerimaanUtkPesanan.setText(Dt.GetPembelianTransNo());
            JTextFieldPenerimaanPasar.setText(Dt.GetPembelianSupplier());
            JTextFieldPenerimaanUangMuka.setText(Dt.GetPembelianUangMuka());

            if ( !Dt.GetPembelianNamaResep().equalsIgnoreCase("null")){

               JLabelPemberitahuanPenerimaan.setText("Jenis Po Dari Resep");

            }
            else {

                JLabelPemberitahuanPenerimaan.setText("Jenis Po Manual");

            }
            
            NamaResepPenerimaan = Dt.GetPembelianNamaResep();
            
           /*
            * Logika hapus semua data di jtable
            */
        
            DefaultTableModel dtm = (DefaultTableModel) JTabelPenerimaanBahan.getModel();
            dtm.setRowCount(0); 
            AmbilDataDatabaseSetelahGetBrowsePembelian(Dt.GetPembelianTransNo(), TabelModelPenerimaan, "detail_trans");   

            /*
             * Set Tanggal di kazao
             */
            String TglBuatResep     = Dt.GetPembelianTanggalBuat();
            SistemPro.TanggalSistem TglNow = new SistemPro.TanggalSistem(); 
            Calendar Tgl = Calendar.getInstance();

            Tgl.set(TglNow.ConvertTglBlnThnToTahun(TglBuatResep), TglNow.ConvertTglBlnThnToBulan(TglBuatResep) - 1, TglNow.ConvertTglBlnThnToTanggal(TglBuatResep));
            kazaoCalendarDatePenerimaanPO.setCalendar(Tgl);  
            
            /*
             * agar colum qty dan harga bisa di edit
             */
            this.JTabelEditPenerimaan = true;
            
        }  
        catch(Exception X){           
        }
    }
     
     /*
     * Ambil JDialog Datababase Pemebelian
     */
     private void ViewDataPembelianPenerimaan(){
        PembelianBahanPenerimaanView CallPembelianBahan = new PembelianBahanPenerimaanView (new javax.swing.JFrame(), true);
        CallPembelianBahan.setVisible(true);
        
        ObjectPenerimaanDatatabse Dt = CallPembelianBahan.GetTableData();  
            
        try{
            
            JTextFieldPenerimaanTotal1.setText(Dt.GetPerimaanTotalBiaya());
            JLabelPemberitahuanPenerimaan.setText(Dt.GetPerimaanJudulResep());
            JTextTransNoPenerimaanUtkPesanan.setText(Dt.GetPerimaanNoPo());
            JTextFieldPenerimaanPasar.setText(Dt.GetPerimaanSupplier());
            JTextFieldPenerimaanUangMuka.setText(Dt.GetPerimaanUangMuka());
            JTextFieldPenerimaanKembalian.setText(Dt.GetPerimaanUangKembali());
            JTextFieldPenerimaanNote.setText(Dt.GetPerimaanNote());
            JTextFieldPenermaanTransNo.setText(Dt.GetPerimaanTransNo());
                           
            
           /*
            * Logika hapus semua data di jtable
            */       
            DefaultTableModel dtm = (DefaultTableModel) JTabelPenerimaanBahan.getModel();
            dtm.setRowCount(0); 
            
            AmbilDataDatabaseSetelahGetBrowsePembelian(Dt.GetPerimaanTransNo(), TabelModelPenerimaan, "detail_trans_penerimaan");   

            /*
             * Set Tanggal di kazao Penerimaan WR
             */
            this.TglBuatWr     = Dt.GetPerimaanCreateDateWr();
            SistemPro.TanggalSistem TglNow = new SistemPro.TanggalSistem(); 
            Calendar Tgl = Calendar.getInstance();

            Tgl.set(TglNow.ConvertTglBlnThnToTahun(TglBuatWr  ), TglNow.ConvertTglBlnThnToBulan(TglBuatWr  ) - 1, TglNow.ConvertTglBlnThnToTanggal(TglBuatWr  ));
            kazaoCalendarDatePenerimaanWR.setCalendar(Tgl);  
            
            /*
             * Set Tanggal di kazao Penerimaan PO
             */
            String TglBuatPerimaanPo     = Dt.GetPerimaanNoPo();
            Tgl.set(TglNow.ConvertTglBlnThnToTahun(TglBuatPerimaanPo), TglNow.ConvertTglBlnThnToBulan(TglBuatPerimaanPo ) - 1, TglNow.ConvertTglBlnThnToTanggal(TglBuatPerimaanPo ));
            kazaoCalendarDatePenerimaanPO.setCalendar(Tgl); 
            
            /*
             * agar colum qty dan harga bisa di edit
             */
            //this.JTabelEditPenerimaan = true;
            
        }  
        catch(Exception X){           
        }
    }
      private void TabelPenerimaanBahan(){
        String header[] = {"No", "Bahan","Qty","Satuan", "Harga", "@Harga" ,"Action"};
        TabelModelPenerimaan = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                                //if(colIndex == 1) {return true ;} //  nilai false agar tidak bisa di edit
                                //if(colIndex == 6) {return JTabelEditPenerimaan ;}
                                if(colIndex == 4) {return JTabelEditPenerimaan ;}
                                if(colIndex == 2) {return JTabelEditPenerimaan ;}
                                //if(colIndex == 3) {return EditKolomQty ;}
                                return false;   //Disallow the editing of any cell
                        }
        };
        JTabelPenerimaanBahan.setModel(TabelModelPenerimaan);
       
        /*
         * Rata tengah atau kanan table
         */
        JTabelPenerimaanBahan.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelPenerimaanBahan.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        JTabelPenerimaanBahan.getColumnModel().getColumn(4).setCellRenderer( tengah );
        JTabelPenerimaanBahan.getColumnModel().getColumn(5).setCellRenderer( tengah );
        JTabelPenerimaanBahan.getColumnModel().getColumn(6).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(3).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(7).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         */      
        int jarak_colom[] = {40,300,60,100,100, 100, 80};
        Colom_table ukuran_colom = new Colom_table();
        ukuran_colom.ukuran_colom(JTabelPenerimaanBahan, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTabelPenerimaanBahan.setName("Pesanan Barang");
        JTabelPenerimaanBahan.getColumnModel().getColumn(6).setCellRenderer(  new ButtonJTable("Delete"));
        JTabelPenerimaanBahan.getColumnModel().getColumn(6).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelPenerimaan, JTabelPenerimaanBahan));
       
        /*
         * Disable drag colum tabel
         */       
        JTabelPenerimaanBahan.getTableHeader().setReorderingAllowed(false);
        
        /*
         * Jika ada perubahan data pada cell jtable
         */
        TabelModelPenerimaan.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent tme) {
                PerhitunganJumlahTotalPesanan( tme,  TabelModelPenerimaan, "TabelModelPenerimaan");
            }
        });
    }
      private void  KembalianPenerimaan(){
          
           /*
            * Menghitung jumlah yang harus di beli
            * Dan Hapus Koma
            */
           SistemPro.KomaToString Koma = new SistemPro.KomaToString(); 
           Koma.SetHapusKoma(JTextFieldPenerimaanTotal1.getText());
           int PenerimaanTotalJumlah    = Koma.GetIntTanpaKoma();
           Koma.SetHapusKoma(JTextFieldPenerimaanUangMuka.getText());
           int PenerimaanKembalian      = Koma.GetIntTanpaKoma();
           
           /*
            * Pake  koma
            */
            java.text.DecimalFormat decimalFormat2 = new java.text.DecimalFormat("#,##0");
            
           JTextFieldPenerimaanKembalian.setText(
                   decimalFormat2.format(PenerimaanTotalJumlah - PenerimaanKembalian )
                 );
      }
      
      private void SetSetelahDiSavePenerimaan(boolean JTabel, boolean OpenPesanan, boolean Note) {
          SetTampilanPenerimaanButtonSave(false, false, false, true, false, true, false, false, true);
          JTabelPenerimaanBahan.setEnabled(JTabel);
          JButtonOpenPesanan.setEnabled(OpenPesanan);
          JTextFieldPenerimaanNote.setEnabled(Note);
      }
      
      private void SetResetPenerimaan(){
          SetSetelahDiSavePenerimaan(true, true, true);
          JTextFieldPenermaanTransNo.setText("");
          JTextFieldPenerimaanPasar.setText("");
          JTextTransNoPenerimaanUtkPesanan.setText("");
          JTextFieldPenerimaanTotal1.setText("0");
          JTextFieldPenerimaanUangMuka.setText("0");
          JTextFieldPenerimaanKembalian.setText("0");
          JTextFieldPenerimaanNote.setText("0");
          
          /*
           * Logika hapus semua data di jtable
           */
           DefaultTableModel dtm = (DefaultTableModel) JTabelPenerimaanBahan.getModel();
           dtm.setRowCount(0);
      }
      
      private void SetTampilanPenerimaanButtonSave(boolean Save, boolean Edit, boolean Delete, boolean Browse, boolean Print, boolean New, boolean Note, boolean Tabel, boolean OpenPesan){
          JButtonPenerimaanSave.setEnabled(Save);
          JButtonPenerimaanEdit.setEnabled(Edit);
          JButtonPenerimaanDelete.setEnabled(Delete);
          JButtonPenerimaanBrowse.setEnabled(Browse);
          JButtonPenerimaanPrint.setEnabled(Print);
          JButtonPenerimaanNew.setEnabled(New);
          JTextFieldPenerimaanNote.setEnabled(Note);
          JTabelPenerimaanBahan.setEnabled(Tabel);
          JButtonOpenPesanan.setEnabled(OpenPesan);
      }
      
      private String TransNoPenerimaan(){
         
         /*
        * Ambil tanggal dari kzao kalender
        */
        String date_wr = kazaoCalendarDatePesanan.getKazaoCalendar().getShortDate();
        SistemPro.TanggalSistem N = new SistemPro.TanggalSistem();

        N.SetKazaoToTglIndo(date_wr);
        N.SetKazaoToBlnIndo(date_wr);
        N.SetKazaoToThnIndo(date_wr);

        String DatePo = N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
         /*
          * SELECT trans_no , periode FROM header_pembelian where periode  = WhereSyarat order by  key_no desc limit 0,1
          */        
         SistemPro.TransNo TN = new SistemPro.TransNo();
         TN.SetTransNoPo("WR", "trans_no", "periode", "header_penerimaan", DatePo);
         this.NoUrut = TN.GetNoUrut();
         return TN.GetTransNoPo();
     }
      
     boolean CaraInputPenerimaan = false;
     private boolean ValidasiSebelumDiSavePenerimaan(){
         
        /*
         * Pake  koma
         */
        java.text.DecimalFormat decimalFormat2 = new java.text.DecimalFormat("#,##0");
         
        int Tot = 0;
        int JumlahRowPesan= TabelModelPenerimaan.getRowCount();
        for (int ab = 0 ; ab <= JumlahRowPesan - 1 ; ab++ ){

            if (TabelModelPenerimaan.getValueAt(ab, 4) == null){
                Peringatan("Nilai tabel baris ke :" + ab +" kolom ke : 4 Data Null");
                return false;
            }        
            if (TabelModelPenerimaan.getValueAt(ab, 4).equals("")){
                Peringatan("Nilai tabel baris ke :" + ab +" kolom ke : 4 Data Null");
               return false;
           }
             if (TabelModelPenerimaan.getValueAt(ab, 4).equals("0")){
                 Peringatan("Nilai tabel baris ke :" + ab +" kolom ke : 4 Data Null");
               return false;
           }
        }                 
            
         if (JTextTransNoPenerimaanUtkPesanan.getText().equals("")){
             Peringatan("Data Trans No tidak ada");
             return false;
         }
         
         else if (JTextFieldPenerimaanTotal1.getText().equals("0")){
             Peringatan("Data total biaya kosong");
             return false;
         }
         
         else{
             return true;
         }
     }
     
     private void DeleteDataPenerimaan(){
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from header_penerimaan where trans_no = '"+ JTextFieldPenermaanTransNo.getText() + "'");
    }
      private void PenerimaanDatabaseSave(){
            if (ValidasiSebelumDiSavePenerimaan() == true){
                boolean SaveAtauTidak;
                
               /*
                * Hapus Koma
                */
                SistemPro.KomaToString Koma = new SistemPro.KomaToString(); 
                
                
                SistemPro.TransNo TN    = new SistemPro.TransNo();
                String TransNoWr         =  TransNoPenerimaan();
                
                /*
                 * Save Header Penerimaan
                 */
                String NoTransPenerimaanPO  = JTextTransNoPenerimaanUtkPesanan.getText();
                String SupplierPenerimaan   = JTextFieldPenerimaanPasar.getText();
                String PerimaanTglPo        = kazaoCalendarDatePenerimaanPO.getKazaoCalendar().getShortDate();
                String PerimaanTglWr        = kazaoCalendarDatePenerimaanWR.getKazaoCalendar().getShortDate();
                String PenerimaanNote       = JTextFieldPenerimaanNote.getText();
                
                Koma.SetHapusKoma(JTextFieldPenerimaanTotal1.getText());
                int PenerimaanTotBiaya   = Koma.GetIntTanpaKoma();
                
                Koma.SetHapusKoma(JTextFieldPenerimaanUangMuka.getText());
                int PenerimaanUangMuka   =  Koma.GetIntTanpaKoma();
                
                Koma.SetHapusKoma(JTextFieldPenerimaanKembalian.getText());
                int PenerimaanKembali    = Koma.GetIntTanpaKoma();               
                /*
                 * Penerumaan Tanggal PO
                 */               
                SistemPro.TanggalSistem KazaoToIndo = new SistemPro.TanggalSistem();
                KazaoToIndo.SetKazaoToTglIndo(PerimaanTglPo  );
                String UserTgl  = KazaoToIndo.GetTglIndoStrKazao();
                KazaoToIndo.SetKazaoToBlnIndo(PerimaanTglPo  );
                String UserBln  = KazaoToIndo.GetBlnIndoStrKazao();
                KazaoToIndo.SetKazaoToThnIndo(PerimaanTglPo );
                String UserThn = KazaoToIndo.GetThnIndoStKazao();
                String TanggalPesanan = UserTgl+"-"+UserBln+"-"+UserThn;
                //String Periode          = UserThn+UserBln;
                
                /*
                 * Penerimaan Tanggal PO WR
                 */
                KazaoToIndo.SetKazaoToTglIndo(PerimaanTglWr  );
                String UserTglWR  = KazaoToIndo.GetTglIndoStrKazao();
                KazaoToIndo.SetKazaoToBlnIndo(PerimaanTglWr  );
                String UserBlnWR  = KazaoToIndo.GetBlnIndoStrKazao();
                KazaoToIndo.SetKazaoToThnIndo(PerimaanTglWr );
                String UserThnWR = KazaoToIndo.GetThnIndoStKazao();
                String TanggalPesananWR     = UserTglWR+"-"+UserBlnWR+"-"+UserThnWR;
                String PeriodeWR            = UserThnWR+UserBlnWR;
                
                try {
                        /*
                         * Hapus jika tidak bisa save di detail_resep
                         */
                       DeleteDataPenerimaan();
                       
                        /*
                         * Save Header
                         */
                        Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        Stm.executeUpdate("INSERT INTO header_penerimaan "
                                + "(key_no, trans_no,  judul_resep, periode, supplier, note, no_po, tanggal_po, total_biaya, uang_muka, uang_kembali, created_date, update_date)VALUES ('"
                                + NoUrut() 
                                + "','"+TransNoWr +"','"+ NamaResepPenerimaan+ "','"+PeriodeWR    + "','" + SupplierPenerimaan + "','" 
                                + PenerimaanNote + "','"+NoTransPenerimaanPO + "','"+ TanggalPesanan  +"','"                               
                                + PenerimaanTotBiaya + "','" 
                                + PenerimaanUangMuka + "','"+ PenerimaanKembali+ "','"+TanggalPesananWR + "',"
                                + " now())");     

                        /*
                                + PenerimaanUangMuka + "','"+ P
                         * Save DetailPo to database Mysql
                         */
                     try {
                         int a = JTabelPenerimaanBahan.getRowCount() ;
                         Statement stm = K.createStatement();

                         // dbStatement=con.createStatement();
                         for(int i=0;i< a;i++){

                             int no         =Integer.valueOf(JTabelPenerimaanBahan.getValueAt(i, 0).toString()).intValue();
                             String bahan   =JTabelPenerimaanBahan.getValueAt(i, 1).toString();
                             String qty     =JTabelPenerimaanBahan.getValueAt(i, 2).toString();
                             String satuan  =JTabelPenerimaanBahan.getValueAt(i, 3).toString();
                             String harga   =JTabelPenerimaanBahan.getValueAt(i, 4).toString();
                             String tothrg  =JTabelPenerimaanBahan.getValueAt(i, 5).toString();

                             /*
                              * Hapus koma
                              */
                             Koma.SetHapusKoma(tothrg);
                             stm.executeUpdate("INSERT INTO detail_trans_penerimaan "
                                     + "( no, trans_no, periode, nama_bahan, qty, satuan, harga_satuan, harga_tot, created_date, updated_date) VALUES ('"
                                     +no+"','"+ TransNoWr+ "','"+ PeriodeWR+ "','"
                                     +bahan+"','"+qty+"','"+satuan+"','"+harga+ "','"+Koma.GetIntTanpaKoma()+"','" + TanggalPesananWR+"', now())");
                             }
                         JLabelPemberitahuanPenerimaan.setText("Berhasil Di Saved");
                         SetSetelahDiSavePenerimaan(false  , false, false);
                         JTextFieldPenermaanTransNo.setText(TransNoWr );
                         SetTampilanPenerimaanButtonSave(false, false, false, true, true, true, false, false, false);
                         //JButtonPbSave.setEnabled(false);
                         //JPanelPesananHeader.setEnabled(false);
                         //SetTampilanJButton(false, false, false, true,true, true);
                     }
                     catch (Exception X){

                         /*
                          * Hapus jika tidak bisa save di detail_resep
                          */
                          DeleteDataPenerimaan();
                          JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1226 : "  +X, " Error", JOptionPane.ERROR_MESSAGE);
                        }           
                     }
                     catch (Exception Ex){
                        JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1224 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
                    }              
                 } 
             else{
                 JLabelPemberitahuanPb.setText("Tidak berhasil di save");;
             }
      }
      
      private void PrinterPenerimaan(){
         PembelianBahanPenerimanPrintView penerimaanPrint = new  PembelianBahanPenerimanPrintView( JTextFieldPenermaanTransNo.getText(), TglBuatWr);
      }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelPesananBahan = new javax.swing.JTable();
        JPanelInputPbBahan = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        JTextPbBahan = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        JTextFieldPbQty = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        JTextFieldPbPasar = new javax.swing.JTextField();
        kazaoCalendarDatePesanan = new org.kazao.calendar.KazaoCalendarDate();
        jLabel15 = new javax.swing.JLabel();
        JComboBoxPbCaraPesan = new javax.swing.JComboBox();
        JLabelPemberitahuanPb = new javax.swing.JLabel();
        JPanelHeaderTotal = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        JTextFieldPbTotal = new javax.swing.JTextField();
        JFormattedTextFieldPbUangMuka = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTabelPenerimaanBahan = new javax.swing.JTable();
        JPanelPesananHeader1 = new javax.swing.JPanel();
        JPanelHeader3 = new javax.swing.JPanel();
        JTextFieldPenermaanTransNo = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        JTextFieldPenerimaanPasar = new javax.swing.JTextField();
        kazaoCalendarDatePenerimaanPO = new org.kazao.calendar.KazaoCalendarDate();
        jLabel22 = new javax.swing.JLabel();
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
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        JTextFieldPenerimaanTotal1 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        JTextFieldPenerimaanUangMuka = new javax.swing.JTextField();
        JTextFieldPenerimaanKembalian = new javax.swing.JTextField();
        JPanelButtonSave1 = new javax.swing.JPanel();
        JButtonPenerimaanSave = new javax.swing.JButton();
        JButtonPenerimaanEdit = new javax.swing.JButton();
        JButtonPenerimaanDelete = new javax.swing.JButton();
        JButtonPenerimaanBrowse = new javax.swing.JButton();
        JButtonPenerimaanNew = new javax.swing.JButton();
        JButtonPenerimaanPrint = new javax.swing.JButton();

        jButton2.setText("jButton2");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Pembelian Bahan");

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

        jLabel1.setText(" Bahan : ");

        jLabel2.setText("Qty : ");

        jLabel3.setText("Satuan :");

        JComboBoxSatuan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kg (Kilogram)", "g ( Gram )", "Sdm ( Sendok Makan )", "Sdt ( Sendok Teh)", "L ( Litter )" }));

        JButtonAddPb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Apply.png"))); // NOI18N
        JButtonAddPb.setText("Add");

        jLabel14.setText("Harga : ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextPbBahan, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldPbHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldPbQty, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JComboBoxSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonAddPb, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(JTextPbBahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(JTextFieldPbQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
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
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        JPanelInputPbBahanLayout.setVerticalGroup(
            JPanelInputPbBahanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jLabel4.setText("No Transaksi");

        jLabel5.setText("Supplier / Pasar ");

        jLabel6.setText("Tanggal");

        jLabel7.setText(":");

        jLabel8.setText(":");

        jLabel9.setText(":");

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
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader2Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kazaoCalendarDatePesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JComboBoxPbCaraPesan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPanelHeader2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLabelPemberitahuanPb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPanelHeader2Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(jLabel4)
                        .addComponent(JTextFieldTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addComponent(JTextFieldPbPasar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jLabel9))
                    .addGroup(JPanelHeader2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(kazaoCalendarDatePesanan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(JComboBoxPbCaraPesan, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        JPanelHeaderTotal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel10.setText("Total Biaya");

        jLabel11.setText("Uang Muka");

        jLabel12.setText(":");

        jLabel13.setText(":");

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
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JFormattedTextFieldPbUangMuka))
                    .addGroup(JPanelHeaderTotalLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPbTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)))
                .addContainerGap())
        );
        JPanelHeaderTotalLayout.setVerticalGroup(
            JPanelHeaderTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeaderTotalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelHeaderTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(JTextFieldPbTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeaderTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13)
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JPanelInputPbBahan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelPesananHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(JPanelButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelInputPbBahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelPesananHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(JPanelButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Pemesanan Bahan", jPanel1);

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

        jLabel16.setText("No Transaksi");

        jLabel17.setText("Supplier / Pasar ");

        jLabel18.setText("Tanggal PO");

        jLabel19.setText(":");

        jLabel20.setText(":");

        jLabel21.setText(":");

        JTextFieldPenerimaanPasar.setEditable(false);
        JTextFieldPenerimaanPasar.setBackground(new java.awt.Color(204, 204, 204));

        kazaoCalendarDatePenerimaanPO.setEditable(false);

        jLabel22.setText("No PO :");

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
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kazaoCalendarDatePenerimaanPO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)
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
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(jLabel16)
                        .addComponent(JTextFieldPenermaanTransNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19)))
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel20)))
                    .addGroup(JPanelHeader3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPenerimaanPasar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(jLabel21))
                    .addComponent(kazaoCalendarDatePenerimaanPO, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(JPanelHeader3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel22)
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

        jLabel23.setText("Total Biaya");

        jLabel24.setText("Uang Muka");

        jLabel25.setText(":");

        jLabel26.setText(":");

        JTextFieldPenerimaanTotal1.setEditable(false);
        JTextFieldPenerimaanTotal1.setBackground(new java.awt.Color(204, 204, 204));
        JTextFieldPenerimaanTotal1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextFieldPenerimaanTotal1.setText("0");

        jLabel30.setText("Kembalian");

        jLabel31.setText(":");

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
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPenerimaanUangMuka, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                    .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPenerimaanKembalian, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                    .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTextFieldPenerimaanTotal1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)))
                .addContainerGap())
        );
        JPanelHeaderTotal1Layout.setVerticalGroup(
            JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelHeaderTotal1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel25)
                    .addComponent(JTextFieldPenerimaanTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel26)
                    .addComponent(JTextFieldPenerimaanUangMuka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelHeaderTotal1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31)
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
            .addComponent(JPanelButtonSave1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPanelPesananHeader1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelPesananHeader1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(JPanelButtonSave1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Penerimaan Bahan", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JTextFieldPenerimaanKembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTextFieldPenerimaanKembalianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTextFieldPenerimaanKembalianActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonAddPb;
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
    private javax.swing.JComboBox JComboBoxPbCaraPesan;
    private javax.swing.JComboBox JComboBoxSatuan;
    private javax.swing.JFormattedTextField JFormattedTextFieldPbUangMuka;
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
    private javax.swing.JTable JTabelPenerimaanBahan;
    private javax.swing.JTable JTabelPesananBahan;
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
    private javax.swing.JTextField JTextPbBahan;
    private javax.swing.JTextField JTextTransNoPenerimaanUtkPesanan;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDatePenerimaanPO;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDatePenerimaanWR;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDatePesanan;
    private java.awt.Label label2;
    // End of variables declaration//GEN-END:variables
}
