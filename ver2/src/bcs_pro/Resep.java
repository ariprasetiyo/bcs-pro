/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.ButtonJTableKeDua;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.Colom_table;
import SistemPro.ResepObject;
import SistemPro.ValidasiInputResep;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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
public class Resep extends javax.swing.JInternalFrame {
    
    private DefaultTableModel TabelModelOrder;
    
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah = new RenderingTengah();
    
    SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    Connection K = KD.createConnection();
    String NamaResep2;

    /**
     * Creates new form Resep
     */
    public Resep(String NamaResep, String QtyPorsi) {      
        initComponents();
        SetTampilanComponent();
        JPanelInputBahan.setVisible(false);
        this.setTitle("Membuat resep : " + NamaResep);
        this.NamaResep2 = NamaResep;
        AksiResep();
        LabelJumlahPorsi.setText(QtyPorsi);
        JLabelPerJumlahPorsi.setText(QtyPorsi);
        TabelResep();
        SetResetTampilanResep();
        //Tampilan();
    }
    
    //private void Tampilan(){
        //add(JComboBoxResepSatuan, 1, 0);
        //JComboBoxResepSatuan
     //   JPanelAddResep.setOpaque(false);
        //LabelTanggal.repaint();
   // }
    
    private void AksiResep(){
        
        /*
         * Action jika di add
         */
        JButtonResepAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                AddTabelDiTabelResep();
            }
        });
        
        /*
         * Action qty bahan resep agar tidak bisa di input String
         */ 
        SistemPro.ComponentHanyaAngka AntiAngka = new SistemPro.ComponentHanyaAngka();
        AntiAngka.SetAntiAngka(JTextFieldResepQty);
        AntiAngka.SetAntiAngka(JTextFieldPorsi);
        AntiAngka.SetAntiAngka(JTextFieldResepQty1Porsi);
        
        /*
         * Action jika resep di save
         */
        JButtonResepSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
               TabelResepSaveDatabase();
               JTabelResep.setEnabled(false);              
            }
        });
        
        JButtonResepEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
               SetTampilanButtonSaveDll(true, false, false , true, true);
                JTabelResep.setEnabled(true);  
            }
        });
        
        JButtonResepDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
               SetTampilanButtonSaveDll(false, false, false , true, true);
               int Pilih = JOptionPane.showConfirmDialog(null, " Anda yakin resep : " + NamaResep2 + " di hapus ?", "Hapus", JOptionPane.YES_NO_OPTION);
               if (Pilih == JOptionPane.YES_OPTION){
                        try {
                            DeleteDataResep();
                        }
                        catch (Exception X){
                            JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1227 : "  +X, " Error delete", JOptionPane.ERROR_MESSAGE);
                            X.printStackTrace();
                        } 
                 }
                 else if ( Pilih == JOptionPane.NO_OPTION){                     
                 } 

                SetResetTampilanResep();
                JTabelResep.setEnabled(false);  
            }
        });
        
        JButtonPrintResep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                DefaultTableModel de = (DefaultTableModel)JTabelResep.getModel();
                             
                JRTableModelDataSource dataSource = new JRTableModelDataSource(de);
                
                JasperDesign jasperDesign = null;
                JasperPrint jasperPrint = null ;
                JasperReport jasperReport;
                HashMap hashMap = new HashMap();
                hashMap.put("NamaResep", NamaResep2);
                hashMap.put("JumlahPorsi", LabelJumlahPorsi.getText());
                try {
                    /*
                     * Beberapa cara manggil report 
                     * Path currentRelativePath = Paths.get("");
                    String s = currentRelativePath.toAbsolutePath().toString();
                    System.out.println("Current relative path is: " + s);
                     */
                    //\\ReportJasper\\ReportResepBahan.jrxml"
                    //String x = System.getProperties().getProperty("java.class.path").split(";")[System.getProperties().getProperty("java.class.path").split(";").length - 1] + "\\ReportJasper\\ReportResepBahan.jrxml";
                    String x    = System.getProperty("user.dir")+"\\ReportJasper\\ReportResepBahan.jrxml";
                    //String cc   =  System.getProperty("user.dir");
                    //JOptionPane.showMessageDialog( null, x);    
                    // JOptionPane.showMessageDialog( null, this.getClass().getClassLoader().getResource("").getPath());
                    
                    jasperDesign = JRXmlLoader.load(x);
                    jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
                    
                    //java.awt.Container cont = jasperPrint.getContentPane();
                    //jPanel7.getcon(jasperPrint);
                    JasperViewer.viewReport(jasperPrint, false);
                           
                } catch (JRException ee) {
                    JTextFieldResepBahan.setText(ee + "");
                    JOptionPane.showMessageDialog(null, ee);
                  ee.printStackTrace();
               }  
            }
        });
        
        JButtonResepNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
               SetTampilanButtonSaveDll(true, false, false , true, true);
               SetTampilanJPanelResep(false, true, false, false);
               SetResetTampilanResep();
                JTabelResep.setEnabled(true);  
            }
        });
        JButtonTambahResep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                ValidasiResep();
            }
        });
        JButtonResepBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                SetTampilanButtonSaveDll(true,true,true,true, true);
                SetResetTampilanResep();
                ViewResepYangSudahDiBuat();
                 JTabelResep.setEnabled(false);  
            }
        });
    }

    private void ValidasiResep(){
        SistemPro.ValidasiInputResep ValidasiResep = new  ValidasiInputResep() ;
            
        ValidasiResep.SetValidasiInputResep2(JTextFieldJudulResep.getText(), JTextFieldPorsi.getText());
        boolean BenarValidasiResep = ValidasiResep.GetValidasiInputResep2();
        if (BenarValidasiResep == false){
            SetTampilanButtonSaveDll(true, false, false , true, true);
            SetTampilanJPanelResep(true, false, true, true);
             this.setTitle("Membuat resep : " + JTextFieldJudulResep.getText());
             this.NamaResep2 = JTextFieldJudulResep.getText();
             LabelJumlahPorsi.setText(JTextFieldPorsi.getText());
             JLabelPerJumlahPorsi.setText(JTextFieldPorsi.getText());
        }
        
        
    }
    
    private void SetResetTampilanResep(){
            
        /*
         * Logika hapus semua data di jtable
         */
        //TabelModelOrder.getDataVector().removeAllElements();
        //TabelModelOrder.fireTableDataChanged();
        DefaultTableModel dtm = (DefaultTableModel) JTabelResep.getModel();
        dtm.setRowCount(0); 
        SetResetText();       
    }
    
    private void SetResetText(){
        JTextFieldJudulResep.setText("");
        JTextFieldPorsi.setText("");
        StatusSaveResep.setText("");
        JTextFieldResepBahan.setText("");
        JTextFieldResepQty.setText("");
        JTextFieldResepQty1Porsi.setText("");
    }
    
    private void SetTampilanComponent() {
        
        /*
         * Static JPanel
         * Di Gunakan agar jika ada penambahan componet tidak berubah
         * biar tidak bergerak
         */
        Dimension dimension = new Dimension(10, 30);
        kazaoCalendarDateResep.setMaximumSize(dimension);
        kazaoCalendarDateResep.setMinimumSize(dimension);
        kazaoCalendarDateResep.setPreferredSize(dimension);
        kazaoCalendarDateResep.setFormat("dd-mm-yyy");
    }
    
    private void SetTampilanJPanelResep(boolean AddResep, boolean InputBahan, boolean PerhitunganResep, boolean ButtonSave){
        JPanelAddResepBesaran.setVisible(AddResep);
        JPanelInputBahan.setVisible(InputBahan);
        JPanelPerhitunganResep.setVisible(PerhitunganResep);
        JPanelButtonSave.setVisible(ButtonSave);
    }
    
    private void SetTampilanButtonSaveDll(boolean Save, boolean Edit, boolean Delete, boolean Browse, boolean New){
        JButtonResepEdit.setEnabled(Edit);
        JButtonResepSave.setEnabled(Save);
        JButtonResepDelete.setEnabled(Delete);
        JButtonResepBrowse.setEnabled(Browse);
        JButtonResepNew.setEnabled(New);
    }
    
    private void DeleteDataResep(){
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from header_resep where nama_resep = '"+ NamaResep2 + "'");
    }
    
    private void TabelResepSaveDatabase(){
        boolean SaveAtauTidak;
        
        /*
         * Save Header Resep
         */
        String ResepPorsi   = LabelJumlahPorsi.getText();
        String ResepTgl     = kazaoCalendarDateResep.getKazaoCalendar().getShortDate();
        
        SistemPro.TanggalSistem KazaoToIndo = new SistemPro.TanggalSistem();
        KazaoToIndo.SetKazaoToTglIndo(ResepTgl );
        String UserTgl  = KazaoToIndo.GetTglIndoStrKazao();
        KazaoToIndo.SetKazaoToBlnIndo(ResepTgl );
        String UserBln  = KazaoToIndo.GetBlnIndoStrKazao();
        KazaoToIndo.SetKazaoToThnIndo(ResepTgl );
        String UserThn = KazaoToIndo.GetThnIndoStKazao();
        String TanggalResep = UserTgl+"-"+UserBln+"-"+UserThn;
        
        try {
                /*
                 * Hapus jika tidak bisa save di detail_resep
                 */
                    DeleteDataResep();
                    
                /*
                 * Save Header
                 */
                    Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    Stm.executeUpdate("INSERT INTO header_resep (nama_resep,porsi_resep,created_date,update_date) VALUES('"
                                + NamaResep2 + "','"+ ResepPorsi + "','" + TanggalResep + "'," + " now())");     
                        
                  /*
                   * Save DetailPo to database Mysql
                   */
                 try {
                     int a = JTabelResep.getRowCount() ;
                     Statement stm = K.createStatement();

                     // dbStatement=con.createStatement();
                     for(int i=0;i< a;i++){
 
                         int no         =Integer.valueOf(JTabelResep.getValueAt(i, 0).toString()).intValue();
                         String bahan   =JTabelResep.getValueAt(i, 1).toString();
                         String qty1    =JTabelResep.getValueAt(i, 2).toString();
                         String satuan1 =JTabelResep.getValueAt(i, 3).toString();
                         String satuan  =JTabelResep.getValueAt(i, 5).toString();
                         String qty     =JTabelResep.getValueAt(i, 4).toString();
                         
                         /*
                         JOptionPane.showMessageDialog(null, period+"','"+KeyNoDT+"','"+no+"','"+trans_no+"','"+itempart+"','"+itemname+"','"+qty+"','"
                                 +"','"+price+"','"+totprice+"','"+unit+"','"+bth+"','"+ket+"','"+TransDate+"','"+userDT+"','"+Updated);
                          */
                         stm.executeUpdate("INSERT INTO detail_resep ( no,namresep, bahan,qty_1_porsi, satuan_1_porsi, qty, satuan, created_date, update_date) VALUES ('"
                                 +no+"','"+ NamaResep2+ "','"+bahan+"','"+qty1+"','"+satuan1+"','"+qty+"','"+satuan+"','"+TanggalResep+"', now())");
                         }
                     StatusSaveResep.setText("BERHASIL DI SAVE");
                     SetTampilanButtonSaveDll(false, false, false , true, true);
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
    
    private void ViewResepYangSudahDiBuat(){
        ResepView   ResepViewSekarang = new ResepView (new javax.swing.JFrame(), true);
        ResepViewSekarang.setVisible(true);
        
        ResepObject Dt = ResepViewSekarang.GetTableData();
        
        /*
        JTextOpenPo.setText(Dt.GetVPTransNo());
        JTextSupplierWr.setText(Dt.GetVPSuplier());
        JTextAdressWr1.setText(Dt.GetVPSuplierAddress());
        //JTextDeliveryToPo.setText(Dt.GetVPDepartementsAddress());
        */
        LabelJumlahPorsi.setText(Dt.GetVRPorsiResep());
        JLabelPerJumlahPorsi.setText(Dt.GetVRPorsiResep());
        this.setTitle("Membuat Resep : " + Dt.GetVRNamaResep());
        String TglBuatResep     = Dt.GetVRTanggalBuat();
        
        /*
         * Set Tanggal di kazao
         */
        SistemPro.TanggalSistem TglNow = new SistemPro.TanggalSistem(); 
        Calendar Tgl = Calendar.getInstance();
        
        Tgl.set(TglNow.ConvertTglBlnThnToTahun(TglBuatResep), TglNow.ConvertTglBlnThnToBulan(TglBuatResep) - 1, TglNow.ConvertTglBlnThnToTanggal(TglBuatResep));
       // Tgl.set(Calendar.MONTH, TglNow.ConvertTglBlnThnToBulan(TglBuatResep)); // di java range bulan 0..11
        //Tgl.set(Calendar.DATE, TglNow.ConvertTglBlnThnToTanggal(TglBuatResep));
        //Tgl.set(2014,0,11);
        //Tgl.clear();
        
        kazaoCalendarDateResep.setCalendar(Tgl);  
        this.NamaResep2 = Dt.GetVRNamaResep();
        AmbilDataDatabaseSetelahGetBrowse(Dt.GetVRNamaResep());
    }
    
   private void AmbilDataDatabaseSetelahGetBrowse(String PilihData){
       
        /*
         * Isi data ke Tabel dari database
         */      
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT namresep, no ,bahan,qty_1_porsi, satuan_1_porsi, qty, satuan from detail_resep where namresep = '"+ PilihData + "' order by no asc  ");              
               baris = HQ.getRow();

               while(HQ.next()  ){

                   String No     = String.valueOf(HQ.getInt("no")).toString();
                   String Bahan  = HQ.getString("bahan");
                   String Qty1   = HQ.getString("qty_1_porsi");
                   String Satuan1= HQ.getString("satuan_1_porsi");
                   String Qty    = String.valueOf(HQ.getInt("qty")).toString();
                   String Satuan = HQ.getString("satuan");
                   
                   String[] add         = {No , Bahan,Qty1, Satuan1,Qty,Satuan};
                   TabelModelOrder.addRow(add);      
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
       // JTabelResep.getColumnModel().getColumn(4).setCellRenderer(  new ButtonJTable("Delete"));
        //JTabelResep.getColumnModel().getColumn(4).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelOrder, JTabelResep));   

   }
    
    private void TabelResep() {
        String header[] = {"No", "Bahan","Qty 1 Porsi", "Satuan 1 Porsi","Qty","Satuan", "Action"};
        TabelModelOrder = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                                if(colIndex == 1) {return true ;} //  nilai false agar tidak bisa di edit
                                if(colIndex == 6) {return true ;}
                                //if(colIndex == 3) {return EditKolomQty ;}
                                return false;   //Disallow the editing of any cell
                        }
        };
        JTabelResep.setModel(TabelModelOrder);
       
        /*
         * Rata tengah atau kanan table
         */
        JTabelResep.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelResep.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        JTabelResep.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
        JTabelResep.getColumnModel().getColumn(4).setCellRenderer( tengah );
        JTabelResep.getColumnModel().getColumn(5).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(3).setCellRenderer( tengah );
        //JTabelResep.getColumnModel().getColumn(7).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         */      
        int jarak_colom[] = {40,342,60,200,60,200, 80};
        Colom_table ukuran_colom = new Colom_table();
        ukuran_colom.ukuran_colom(JTabelResep, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTabelResep.setName("Tabel Resep");
        JTabelResep.getColumnModel().getColumn(6).setCellRenderer(  new ButtonJTable("Delete"));
        JTabelResep.getColumnModel().getColumn(6).setCellEditor(  new ButtonJTableKeDua(new JCheckBox(),TabelModelOrder, JTabelResep));
       
        /*
         * Disable drag colum tabel
         */       
        JTabelResep.getTableHeader().setReorderingAllowed(false);
    }
    
    private void AddTabelDiTabelResep(){
        
        String ResepBahan   = JTextFieldResepBahan.getText();
        String ResepQty     = JTextFieldResepQty.getText();
        String ResepSatuan  = (String) JComboBoxResepSatuan.getSelectedItem();
        
        String Resep1Satuan = (String)JComboBoxResepSatuan1Porsi.getSelectedItem();
        String Resep1Qty    = JTextFieldResepQty1Porsi.getText();
                
        JFormattedTextField JumlahPorsi  = new JFormattedTextField(NumberFormat.getIntegerInstance());
        
        SistemPro.NoUrut NoUrutan = new   SistemPro.NoUrut();
        
        /*
         * Set data no urut
         */
        int ax = JTabelResep.getRowCount() ;
        //JOptionPane.showMessageDialog(null, ax);
        if (ax == 0 ){
            NoUrutan.SetNoUrut(String.valueOf(ax).toString());
        }
        else if ( ax > 0) {
            ax = ax - 1;
            String ab = (String) JTabelResep.getValueAt(ax, 0);
            NoUrutan.SetNoUrut(ab);
        }
        
        /*
         * Validasi qty 100 porsi
         */       
        if ("".equals(ResepBahan)){
             JOptionPane.showMessageDialog(null, "Data bahan tidak ada !", "Perhatian",JOptionPane.ERROR_MESSAGE);           
        }
        else if("".equals(ResepQty)){
            JOptionPane.showMessageDialog(null, "Data qty bahan tidak ada !", "Perhatian",JOptionPane.ERROR_MESSAGE);           
        }
        else if("".equals(Resep1Qty)){
            JOptionPane.showMessageDialog(null, "Data qty bahan tidak ada !", "Perhatian",JOptionPane.ERROR_MESSAGE);            
        }
        else {
            Object obj[] = new Object[10];        
            obj[0] = NoUrutan.GetNoUrut();
            obj[1] = ResepBahan;
            obj[2] = Resep1Qty;
            obj[3] = Resep1Satuan;
            obj[4] = ResepQty;
            obj[5] = ResepSatuan;
            TabelModelOrder.addRow(obj);
            SetResetText();
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

        jComboBox1 = new javax.swing.JComboBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelResep = new javax.swing.JTable();
        JPanelAddResepBesaran = new javax.swing.JPanel();
        JPanelAddResep = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        JTextFieldResepBahan = new javax.swing.JTextField();
        JComboBoxResepSatuan = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        JTextFieldResepQty = new javax.swing.JTextField();
        JButtonResepAdd = new javax.swing.JButton();
        JLabelPerJumlahPorsi = new javax.swing.JLabel();
        JLabelPerJumlahPorsi1 = new javax.swing.JLabel();
        JLabelPerJumlahPorsi2 = new javax.swing.JLabel();
        JLabelPerJumlahPorsi3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        JLabelPerJumlahPorsi5 = new javax.swing.JLabel();
        JTextFieldResepQty1Porsi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        JComboBoxResepSatuan1Porsi = new javax.swing.JComboBox();
        JPanelButtonSave = new javax.swing.JPanel();
        JButtonResepSave = new javax.swing.JButton();
        JButtonResepEdit = new javax.swing.JButton();
        JButtonResepDelete = new javax.swing.JButton();
        JButtonResepBrowse = new javax.swing.JButton();
        JButtonResepNew = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        JButtonPrintResep = new javax.swing.JButton();
        JPanelPerhitunganResep = new javax.swing.JPanel();
        label2 = new java.awt.Label();
        LabelJumlahPorsi = new java.awt.Label();
        kazaoCalendarDateResep = new org.kazao.calendar.KazaoCalendarDate();
        StatusSaveResep = new java.awt.Label();
        jLabel7 = new javax.swing.JLabel();
        JPanelInputBahan = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        JTextFieldJudulResep = new javax.swing.JTextField();
        label1 = new java.awt.Label();
        JTextFieldPorsi = new javax.swing.JTextField();
        JButtonTambahResep = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jCheckBox1.setText("jCheckBox1");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setAutoscrolls(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jScrollPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JTabelResep.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(JTabelResep);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        JPanelAddResepBesaran.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Nama Bahan Resep");

        jLabel2.setText("   Satuan");

        JComboBoxResepSatuan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kg (Kilogram)", "g ( Gram )", "Sdm ( Sendok Makan )", "Sdt ( Sendok Teh)", "L ( Litter )" }));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Qty Per");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        JButtonResepAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Apply.png"))); // NOI18N
        JButtonResepAdd.setText("Add");

        JLabelPerJumlahPorsi.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        JLabelPerJumlahPorsi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabelPerJumlahPorsi.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        JLabelPerJumlahPorsi1.setText("Porsi   ");

        JLabelPerJumlahPorsi2.setText(":");

        JLabelPerJumlahPorsi3.setText(":");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Qty Per  1 Porsi");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        JLabelPerJumlahPorsi5.setText(":");

        jLabel5.setText("   Satuan");

        JComboBoxResepSatuan1Porsi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kg (Kilogram)", "g ( Gram )", "Sdm ( Sendok Makan )", "Sdt ( Sendok Teh)", "L ( Litter )" }));

        javax.swing.GroupLayout JPanelAddResepLayout = new javax.swing.GroupLayout(JPanelAddResep);
        JPanelAddResep.setLayout(JPanelAddResepLayout);
        JPanelAddResepLayout.setHorizontalGroup(
            JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelAddResepLayout.createSequentialGroup()
                .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPanelAddResepLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLabelPerJumlahPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLabelPerJumlahPorsi1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(JLabelPerJumlahPorsi2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JLabelPerJumlahPorsi5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(JLabelPerJumlahPorsi3, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelAddResepLayout.createSequentialGroup()
                        .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPanelAddResepLayout.createSequentialGroup()
                                .addComponent(JTextFieldResepQty, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JComboBoxResepSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JPanelAddResepLayout.createSequentialGroup()
                                .addComponent(JTextFieldResepQty1Porsi, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JComboBoxResepSatuan1Porsi, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JButtonResepAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JTextFieldResepBahan)))
        );
        JPanelAddResepLayout.setVerticalGroup(
            JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelAddResepLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(JTextFieldResepBahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JLabelPerJumlahPorsi3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JButtonResepAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(JPanelAddResepLayout.createSequentialGroup()
                        .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(JLabelPerJumlahPorsi1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JLabelPerJumlahPorsi2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JTextFieldResepQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)
                                .addComponent(JComboBoxResepSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(JLabelPerJumlahPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(JComboBoxResepSatuan1Porsi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JPanelAddResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JLabelPerJumlahPorsi5)
                                .addComponent(JTextFieldResepQty1Porsi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(3, 3, 3))
        );

        javax.swing.GroupLayout JPanelAddResepBesaranLayout = new javax.swing.GroupLayout(JPanelAddResepBesaran);
        JPanelAddResepBesaran.setLayout(JPanelAddResepBesaranLayout);
        JPanelAddResepBesaranLayout.setHorizontalGroup(
            JPanelAddResepBesaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelAddResepBesaranLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPanelAddResep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        JPanelAddResepBesaranLayout.setVerticalGroup(
            JPanelAddResepBesaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelAddResepBesaranLayout.createSequentialGroup()
                .addComponent(JPanelAddResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        JPanelButtonSave.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JButtonResepSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Save.png"))); // NOI18N
        JButtonResepSave.setText("Save");

        JButtonResepEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Update.png"))); // NOI18N
        JButtonResepEdit.setText("Edit");
        JButtonResepEdit.setEnabled(false);

        JButtonResepDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Delete.png"))); // NOI18N
        JButtonResepDelete.setText("Delete");
        JButtonResepDelete.setEnabled(false);

        JButtonResepBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Search.png"))); // NOI18N
        JButtonResepBrowse.setText("Browse");

        JButtonResepNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonResepNew.setText("New");

        jPanel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 87, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        JButtonPrintResep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPrintResep.setText("Print");

        javax.swing.GroupLayout JPanelButtonSaveLayout = new javax.swing.GroupLayout(JPanelButtonSave);
        JPanelButtonSave.setLayout(JPanelButtonSaveLayout);
        JPanelButtonSaveLayout.setHorizontalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSaveLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 38, Short.MAX_VALUE)
                .addComponent(JButtonResepSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonResepEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonResepDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonResepBrowse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonResepNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPrintResep)
                .addContainerGap(80, Short.MAX_VALUE))
        );
        JPanelButtonSaveLayout.setVerticalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(JPanelButtonSaveLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JButtonResepSave)
                    .addComponent(JButtonResepEdit)
                    .addComponent(JButtonResepDelete)
                    .addComponent(JButtonResepBrowse)
                    .addComponent(JButtonResepNew)
                    .addComponent(JButtonPrintResep))
                .addContainerGap())
        );

        label2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        label2.setText("Perhitungan Porsi :");

        LabelJumlahPorsi.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        StatusSaveResep.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        StatusSaveResep.setForeground(new java.awt.Color(255, 51, 0));

        jLabel7.setText("Tanggal");

        javax.swing.GroupLayout JPanelPerhitunganResepLayout = new javax.swing.GroupLayout(JPanelPerhitunganResep);
        JPanelPerhitunganResep.setLayout(JPanelPerhitunganResepLayout);
        JPanelPerhitunganResepLayout.setHorizontalGroup(
            JPanelPerhitunganResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelPerhitunganResepLayout.createSequentialGroup()
                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelJumlahPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(kazaoCalendarDateResep, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(126, 126, 126)
                .addComponent(StatusSaveResep, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        JPanelPerhitunganResepLayout.setVerticalGroup(
            JPanelPerhitunganResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(StatusSaveResep, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelPerhitunganResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel7)
                .addGroup(JPanelPerhitunganResepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(kazaoCalendarDateResep, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LabelJumlahPorsi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        JPanelInputBahan.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setText("Masukan nama resep :");

        label1.setText("Jumlah porsi :");

        JButtonTambahResep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Apply.png"))); // NOI18N
        JButtonTambahResep.setText("Tambah Resep");

        javax.swing.GroupLayout JPanelInputBahanLayout = new javax.swing.GroupLayout(JPanelInputBahan);
        JPanelInputBahan.setLayout(JPanelInputBahanLayout);
        JPanelInputBahanLayout.setHorizontalGroup(
            JPanelInputBahanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelInputBahanLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JTextFieldJudulResep, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(JButtonTambahResep))
        );
        JPanelInputBahanLayout.setVerticalGroup(
            JPanelInputBahanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelInputBahanLayout.createSequentialGroup()
                .addGroup(JPanelInputBahanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JButtonTambahResep)
                    .addGroup(JPanelInputBahanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(JTextFieldJudulResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextFieldPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JPanelAddResepBesaran, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelPerhitunganResep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelInputBahan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelAddResepBesaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelInputBahan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelPerhitunganResep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonPrintResep;
    private javax.swing.JButton JButtonResepAdd;
    private javax.swing.JButton JButtonResepBrowse;
    private javax.swing.JButton JButtonResepDelete;
    private javax.swing.JButton JButtonResepEdit;
    private javax.swing.JButton JButtonResepNew;
    private javax.swing.JButton JButtonResepSave;
    private javax.swing.JButton JButtonTambahResep;
    private javax.swing.JComboBox JComboBoxResepSatuan;
    private javax.swing.JComboBox JComboBoxResepSatuan1Porsi;
    private javax.swing.JLabel JLabelPerJumlahPorsi;
    private javax.swing.JLabel JLabelPerJumlahPorsi1;
    private javax.swing.JLabel JLabelPerJumlahPorsi2;
    private javax.swing.JLabel JLabelPerJumlahPorsi3;
    private javax.swing.JLabel JLabelPerJumlahPorsi5;
    private javax.swing.JPanel JPanelAddResep;
    private javax.swing.JPanel JPanelAddResepBesaran;
    private javax.swing.JPanel JPanelButtonSave;
    private javax.swing.JPanel JPanelInputBahan;
    private javax.swing.JPanel JPanelPerhitunganResep;
    private javax.swing.JTable JTabelResep;
    private javax.swing.JTextField JTextFieldJudulResep;
    private javax.swing.JTextField JTextFieldPorsi;
    private javax.swing.JTextField JTextFieldResepBahan;
    private javax.swing.JTextField JTextFieldResepQty;
    private javax.swing.JTextField JTextFieldResepQty1Porsi;
    private java.awt.Label LabelJumlahPorsi;
    private java.awt.Label StatusSaveResep;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDateResep;
    private java.awt.Label label1;
    private java.awt.Label label2;
    // End of variables declaration//GEN-END:variables
}