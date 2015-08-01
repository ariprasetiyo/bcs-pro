/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.Colom_table;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
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
public class AlokasiWaktu3 extends javax.swing.JInternalFrame {
    
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah = new RenderingTengah();
    private DefaultTableModel TabelModelAlokasiWaktu;
    private DefaultTableModel TabelModelView;
    private SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    private Connection K = KD.createConnection();

    int Awal;
    int AwalInt;
    int TotalWaktu = 0 ;
    boolean Edit = false;
    String A,B,C, D, F;
    
    /**
     * Creates new form AlokasiWaktu
     */
    public AlokasiWaktu3() {
        
        initComponents();
        AksiAksi();
        //TampilanDataHasilAlokasi();
        TabelAlokasiWaktu();
        AmbilDataDariDatabase();    
        TabelView();
    }

    
    private void HakAksesText(boolean Belanja, boolean PraProduksi, 
        boolean Produksi, boolean Packing,boolean Dilevery, boolean NamaProduksi){

        JTextNamaProduksi.setEnabled(NamaProduksi);
       
    }
    
    private void HakAksesTombol(boolean save, boolean edit, boolean delete, boolean CB){
        JButtonAlokasiWaktuRefresh.setEnabled(delete);
        JButtonAlokasiWaktuSave.setEnabled(save);
        JButtonAlokasiWaktuEdit.setEnabled(edit);

    }
    
    private void AksiAksi(){
        
       JButtonAlokasiWaktuNew.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a){
                 HakAksesText(true,true, true, true,true, true);
                 HakAksesTombol(true, true, true, true);
                 JTextTotalWaktu.setText("");
                 JTextNamaProduksi.setText("");
                 JLabelAlokasiWaktuKeterangan.setText("");
                 //JPanelInputDinamis.remove(1);
                 //pack();                
            }
        });
        
        JButtonAlokasiWaktuSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a){
                 HakAksesText(false,false, false, false,false, false);
                 HakAksesTombol(false, true, true, false);
                 JTextFieldDinamikAmbilData();
            }
        });
        
        JButtonAlokasiWaktuEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a){
                HakAksesText(true,true, true, true,true, true);
                HakAksesTombol(true, false, false, true);
                Edit = true;
            }
        });
        
        JButtonAlokasiWaktuRefresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a){
                HakAksesText(false,false, false, false,false, false);
                HakAksesTombol(false, false, false, false);
                AmbilDataDariDatabase();
                JLabelAlokasiWaktuKeterangan.setText("");
            }
        });
        
        JButtonPrint.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a){
                  PrintJasper();
            }
        });
        
         JTabelAlokasiWaktu.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    //JPanelInputDinamis.removeAll();
                    //JPanelInputDinamis.revalidate();
                    //getContainer().removeAll();
                    F = (String) TabelModelAlokasiWaktu.getValueAt(JTabelAlokasiWaktu.getSelectedRow(), 0);

                    ViewDataAlokasiWaktu2();
                    JLabelAlokasiWaktuKeterangan.setText("");
                    jTabbedPane1.setSelectedIndex(1);
                    //JPanelInputDinamis.repaint();
                }
            }   
        });
         
         JButtonAlokasiWaktuAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a){ 

                JTextFieldDinamik();
                //JTextFieldDinamikAmbilData();
            }
        });
               
        /*
         * Anti huruf
         */
        SistemPro.ComponentHanyaAngka AntiHuruf = new SistemPro.ComponentHanyaAngka();
        AntiHuruf. SetAntiAngkaPakeDataDoubel(JTextAlokasiInputWaktu);
        
    
        //SetAntiKelebihanJamDanHarusPakeDataDouble2(JTextAlokasiInputWaktu);
        
        //SetAntiKelebihanJamDanHarusPakeDataDouble3(  JButtonProsessAlokasiWaktu);
        JButtonProsessAlokasiWaktu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a){ 
                    PerhitunganWaktu();
                     jTabbedPane1.setSelectedIndex(1);
                     JButtonPrint.setEnabled(true);
            }
        });
       
        /*
         * Fileter seleksi JCOMBOBOX
         */

        //g.setVisible(true);
        //g.setLocation(getSize().width/12, getSize().height/16);
        //g.setSize(60, 40);
        //JPanelInputDinamis.setVisible(true);
        //JPanelInputDinamis.add(g, BorderLayout.CENTER);
        //add(JPanelInputDinamis,  BorderLayout.CENTER);
        //JTextFieldDinamik();
    }

    List<JTextField> JTextKeterangAlokasi   = new ArrayList<>();
    List<JTextField> JTextFieldWaktu        = new ArrayList<>();
    List<JComboBox> JComboWaktu             = new ArrayList<>();
    
    //JTextField g=new JTextField("xxx");
    int lokasi = 0;
    int lokasi2 = 0;
    private void JTextFieldDinamik(){
        
        int batas   = 20;
        if (lokasi2 < batas){
            
            JComboBox ComboWaktu    = new JComboBox();

            ComboWaktu.addItem("Menit");
            ComboWaktu.addItem("Jam");
            ComboWaktu.addItem("Hari");
            
            JTextField Waktu = new JTextField("0");
            ComboWaktu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a){ 
                TotalWaktu ();
                }
            });
            JTextKeterangAlokasi.add(new JTextField());
            JTextFieldWaktu.add(Waktu);
            JComboWaktu.add(ComboWaktu);
    
            //JPanelInputDinamis.remove(0);
            //Waktu.removeAll();
            //ComboWaktu.removeAll();
            //removeAll();
            //JPanelInputDinamis.remove(this);
            //JPanelInputDinamis.revalidate();
            //JPanelInputDinamis.repaint();
            //JPanelInputDinamis.setSize(10, 10);
            
            JTextKeterangAlokasi.get(JTextKeterangAlokasi.size() - 1).setLocation(10, getSize().height/22 + lokasi);
            JTextKeterangAlokasi.get(JTextKeterangAlokasi.size() - 1).setSize(120, 23);
            
            JTextFieldWaktu.get(JTextFieldWaktu.size() - 1).setLocation(140, getSize().height/22+ lokasi);
            JTextFieldWaktu.get(JTextFieldWaktu.size() - 1).setSize(50, 23);

            JComboWaktu.get(JComboWaktu.size() - 1).setLocation(190,getSize().height/22+ lokasi);
            JComboWaktu.get(JComboWaktu.size() - 1).setSize(71, 23);
            
            SistemPro.ComponentHanyaAngka AntiHuruf = new SistemPro.ComponentHanyaAngka();

            for(int j = 0; j <JTextKeterangAlokasi.size(); j++){
                JPanelInputDinamis.add(JTextKeterangAlokasi.get(j));
                JPanelInputDinamis.add(JTextFieldWaktu.get(j));
                JPanelInputDinamis.add(JComboWaktu.get(j));
                
               /*
                * Anti huruf
                */              
               AntiHuruf. SetAntiAngkaPakeDataDoubel(JTextFieldWaktu.get(j));
               SetAntiKelebihanJamDanHarusPakeDataDouble(JComboWaktu.get(j),JTextFieldWaktu.get(j) );
               //SeleksiMenitJamDanHari(JComboWaktu.get(j), JTextFieldWaktu.get(j));
        
            }
            //Waktu.removeAll();
        }
        lokasi +=28;
        lokasi2 +=1;      
        revalidate();
        repaint();
        TotalWaktu ();
    }
    
    private void PrintJasper(){
        DefaultTableModel de = (DefaultTableModel)JTabelView.getModel();

        JRTableModelDataSource dataSource = new JRTableModelDataSource(de);

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        HashMap hashMap = new HashMap();
        
        hashMap.put("NamaKegiatan", F);

        
        try {

            String x    = System.getProperty("user.dir")+"\\ReportJasper\\ReportPerhitunganWaktu.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
            JasperViewer.viewReport(jasperPrint, false);                           
        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }  
    }
    
    private void JTextFieldDinamikAmbilData(){
       String NamaProduksi          = JTextNamaProduksi.getText();  
       EditDataAloakasi();
       try {
           
           SaveDatabaseDataAlokasiWaktu();
           /*
            * Save Detailnya
            */
           Statement Stm   = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            for (int a = 0 ; a <  JTextKeterangAlokasi.size(); a++ ){
                
               if ( ( FilterSave2(JTextFieldWaktu.get(a))  ==  true ) && ( FilterSave2(JTextKeterangAlokasi.get(a)) == true ) ){

                    String SWaktu   = JTextFieldWaktu.get(a).getText() +"-"+ JComboWaktu.get(a).getSelectedIndex();
                    
                    Stm.executeUpdate("INSERT INTO detail_alokasi_waktu "
                       + "( nama_produksi, kegiatan, waktu )VALUES ('"
                       + NamaProduksi + "','" + JTextKeterangAlokasi.get(a).getText()  + "','" + SWaktu + "' )" );  
               }
               else {
                    JLabelAlokasiWaktuKeterangan.setText("Data Ada yang Kosong");
                    break;
                }
            }
               AmbilDataDariDatabase(); 
            }
            catch (Exception Ex){
                JLabelAlokasiWaktuKeterangan.setText("Data Sudah Ada"); 
                JOptionPane.showMessageDialog(null, Ex);
         }              
    }
    private void HapusDataJTabel(JTable Tabel ){
        /*
         * Logika hapus semua data di jtable
         */
        DefaultTableModel dtm = (DefaultTableModel) Tabel.getModel();
        dtm.setRowCount(0); 
     }

    private String ConvertMenitKeJamFinish(int Data){
        int Sisa1  ;
        int BrpHari;
        int Menit ;
        int Jam;
        int SisaMenit = 0;

        BrpHari  = Data /( 24 * 60 );       
        Sisa1           = Data - (BrpHari * 24 * 60);            
        Jam             = Sisa1 /60;
        Menit           = Sisa1 - ( Jam * 60);
        return ConvertMenitKeJam2(Menit, BrpHari, Jam, Menit);
    }
    
    private String ConvertMenitKeJam2(int SisaMenit, int BrpHari, int Jam, int Menit){
        if (SisaMenit == 0 && BrpHari <= 0   ){
            return  Jam + ":00" ;
        }

        else if (SisaMenit == 0 && BrpHari > 0 ){
            return  Jam + ":00"+" H + "+BrpHari;
        }

        else if (SisaMenit != 0 && BrpHari > 0){
            return  Jam + ":"+Menit +" H + "+BrpHari;
        }                             
        return  Jam + ":" + Menit;
    }
    
    private void ViewDataAlokasiWaktu2(){
        int AmbilRow            = JTabelAlokasiWaktu.getSelectedRow() ;
        String DataNamaProduksi = (String) TabelModelAlokasiWaktu.getValueAt(AmbilRow , 1);
        String DataWaktuTot     = (String) TabelModelAlokasiWaktu.getValueAt(AmbilRow , 3);
        //JTextTotalWaktu.setText(DataWaktuTot);
        JTextNamaProduksi.setText(DataNamaProduksi); 
        AmbilDataDariDatabaseViewDataAlokasiWaktu(DataNamaProduksi);            
    }
    
    private void ViewAlokasiWaktuSetJComboBox(String Data, JComboBox CB){
        String AMenit   = Data.replaceAll(".*-", "");
        CB.setSelectedIndex(Integer.valueOf(AMenit).intValue());
    }
    
    private String ViewAlokasiWaktuSetJTextField(String Data){
         String AMenit   = Data.replaceAll("-.*", "");
         return AMenit;
    }
    
    private void AmbilDataDariDatabaseViewDataAlokasiWaktu(String Data){
          
           ResultSet HQ2 = null;
           HapusDataJTabel(JTabelView);
           try {
                //JPanelInputDinamis.removeAll();
                Statement stm2 = K.createStatement();
                HQ2= stm2.executeQuery("SELECT nama_produksi ,kegiatan, waktu"
                        + " from detail_alokasi_waktu"
                        + "  where nama_produksi ='"+ Data +"'");              
               int a = 0;
                while(HQ2.next()  ){
                    a   = HQ2.getRow();
                    A   = HQ2.getString("nama_produksi");
                    B   = HQ2.getString("kegiatan");
                    C   = HQ2.getString("waktu");  
                    
                    JTextFieldDinamik(); 

                    JTextKeterangAlokasi.get(a - 1).setText(B);
                    D   = ViewAlokasiWaktuSetJTextField(C);
                    JTextFieldWaktu.get(a - 1).setText(D);
                    ViewAlokasiWaktuSetJComboBox(C,  JComboWaktu.get(a - 1));
                    String[] xx = {B,"" , "",D + "" + ConvertNominalKeWaktu(C)};
                    TabelModelView.addRow(xx); 
                   
               }
                
                /*
                 * Agar data jika di view menjadi nol pada jtextfield
                 */
                JTextTotalWaktu.setText(TabelModelAlokasiWaktu.getValueAt(JTabelAlokasiWaktu.getSelectedRow(), 3) + "");
                for (int x = a; x < 100 ; x++){
                    try{
                        JTextKeterangAlokasi.get(x).setText("");
                        JTextFieldWaktu.get(x).setText("0");
                        JComboWaktu.get(x).setSelectedIndex(0);
                    }
                    catch(Exception xy){
                        break;
                    }
                    
                }    
           }
            catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
     }
      
   /*
    * Data Baru Convert Nominal ke jam
    */
    private String ConvertNominalKeWaktu(String Data){
        if (Data.contains("-1")){
            return " Jam";
        }
        else if (Data.contains("-0")){
            return " Menit";
        }
        else if (Data.contains("-2")){
            return " Hari";
        }
        else {
            return " Data error";
        }      
    }
    
    private int ConvertHariJamKeMenit(String Data){
        if (Data.contains("Jam")){
            if (Data.contains(":")){
                A = Data.replaceAll(".*:", "");
                B = Data.replaceAll(":.*", "");
                
                A = A.replaceAll("Jam", "");
                B = B.replaceAll("Jam", "");
                
                A = A.replaceAll("(\\s)*", "");
                B = B.replaceAll("(\\s)*", "");
                
                a = Integer.valueOf(B).intValue();
                a = a * 60;
                c = Integer.valueOf(A).intValue();
                System.out.println(a + c + " Jam 1" + a + " " + c);
                return (a + c);
            }
            else {
                Data = Data.replaceAll("(\\s)*", "");
                Data = Data.replaceAll("Jam", "");
                System.out.println(Data+ " Jam 2");
                return ( Integer.valueOf(Data).intValue() * 60 );
            }           
        }
        else if (Data.contains("Hari")){
            Data = Data.replaceAll("(\\s)*", "");
            Data = Data.replaceAll("Hari", "");
            System.out.println(Data+ " Hari 1");
            return (Integer.valueOf(Data).intValue() * ( 60 * 24 ));
        }
        else if  (Data.contains("Menit")){
            Data = Data.replaceAll("Menit", "");
            Data = Data.replaceAll("(\\s)*", "");
            System.out.println(Data+ " Menit 1");
            return (Integer.valueOf(Data).intValue()  );
        }
        else {
            JOptionPane.showMessageDialog(null, "Data error : 122576");
            return 0;
        }
    }
    
      private String ConvertMenitKeJamFinish3(int Data){
        int Sisa1  ;
        int BrpHari;
        int Menit ;
        int Jam;
        int SisaMenit = 0;

        BrpHari  = Data /( 24 * 60 );       
        Sisa1           = Data - (BrpHari * 24 * 60);            
        Jam             = Sisa1 /60;
        Menit           = Sisa1 - ( Jam * 60);
        return ConvertMenitKeJam2(Menit, BrpHari, Jam, Menit);
    }
    
    private String ConvertMenitKeJam3(int SisaMenit, int BrpHari, int Jam, int Menit){
        if (SisaMenit == 0 && BrpHari <= 0   ){
            return  Jam + ":00" ;
        }

        else if (SisaMenit == 0 && BrpHari > 0 ){
            return  Jam + ":00"+" H + "+BrpHari;
        }

        else if (SisaMenit != 0 && BrpHari > 0){
            return  Jam + ":"+Menit +" H + "+BrpHari;
        }                             
        return  Jam + ":" + Menit;
    }
    
    int a,c,d;
    private void PerhitunganWaktu(){
        a = ConvertJamKeMenitB(JTextAlokasiInputWaktu.getText());
        
        TabelModelView.setValueAt(ConvertMenitKeJamFinish3( a ), 0, 1);
        
        d = JTabelView.getRowCount();
        int DataRubah;
        int DataStart = 0;
        for (int z = 0; z < d ; z++){
            /*
             * Input alokasi waktu harus ada format jam
             */
            
             if (z == 0){
                 DataStart      = ConvertHariJamKeMenit( a +"Menit");             
             }
             
             int DataDuration   = ConvertHariJamKeMenit( (String ) TabelModelView.getValueAt(z, 3));
             int DataFinish     = DataStart + DataDuration;
             //System.out.println(" data start : " + DataStart + " data Finish: " + DataFinish+  " data duration : " + DataDuration);
             B = ConvertMenitKeJamFinish3(DataFinish);
             
             TabelModelView.setValueAt(B, z, 2);
             if (z < d - 1){
                 TabelModelView.setValueAt(B, z + 1, 1);
                 DataStart = DataFinish ;
             }
        }
    }
    
     private int ConvertJamKeMenitB(String DataJam ){
        DataJam = RubahSamaDenganMenjadiDoubleStr(DataJam);
        if (!DataJam.contains(".")){
             DataJam = DataJam+".0";
        }

        String  DataMenit  = DataJam.replaceAll(".*\\.","");
        if (DataMenit.equalsIgnoreCase("") ||DataMenit.equalsIgnoreCase("0") ){
            DataMenit = "0";
        }
        int DataMenitInt   = Integer.valueOf(DataMenit).intValue();
        if (DataMenitInt >= 0 && DataMenitInt <= 60){
            DataJam            = DataJam.replaceAll("\\..*", "");            
            int HasilMenit     = Integer.valueOf(DataJam).intValue() * 60 ;
            int HasilAllMenit  = Integer.valueOf(DataMenit).intValue() + HasilMenit;
            return HasilAllMenit;  
        }  
         return DataMenitInt * 60;
    }
    
    private int  ConvertHariKeMenitB(String DataHari){          
        try{
            int HasilMenit  =  Integer.valueOf(DataHari).intValue() * 60 * 24 ;
           return HasilMenit;
       }
       catch (Exception X){
           return 0;
       }
    }
    
    private int ConvertMenitKeMenitB(  String Menit){
        try{
            return Integer.valueOf(Menit).intValue();
        }
        catch (Exception X){
            return 0;
        }
             
    }
  
    public void  SetAntiKelebihanJamDanHarusPakeDataDouble (final JComboBox CB , final JTextField x){
        x.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e)
                {
                  char c = e.getKeyChar();
                  //System.out.println(c);
                  try {
                      Awal  = Integer.valueOf(x.getText()).intValue();
                      
                  }
                  catch (Exception X){
                      Awal = 0;
                  }

                 if (CB.getSelectedItem().equals("Menit")){
                    LimitCharInput (x.getText(), 2,  e);
                      if (Awal > 60 ){
                           x.setText("60");

                      }
                       TotalWaktu ();
                  }
                 else if (CB.getSelectedItem().equals("Jam")){
                    LimitCharInput (x.getText(), 2,  e);                            
                        if (ConvertJamDKeMenit(x.getText())  > 24 * 60 ){
                             x.setText("24");
                        }
                       TotalWaktu ();
                    }
                  TotalWaktu ();
                }
        });
    }

    private void LimitCharInput (String Data, int Batas, KeyEvent e){
        int Limit = Data.length();
        if (Limit > Batas){
            e.consume();
        }
    }
    
    private boolean LimitCharInput (String Data, int Batas){
        int Limit = Data.length();
        if (Limit <= 0 || Data.equalsIgnoreCase("")){
            //JOptionPane.showMessageDialog(null, "Data kosong");
            return false;
        }
        if (Limit <= Batas){           
            return true;
        }
        else {
            //JOptionPane.showMessageDialog(null, "Data berlebih");
            return false;
        }
    }

    private double RubahSamaDenganMenjadiDouble(String Data){
        Data    = Data.replaceAll("\\:", ".");
        return Double.valueOf(Data).doubleValue();
   }
    private String RubahSamaDenganMenjadiDoubleStr(String Data){
        Data    = Data.replaceAll("\\:", ".");
        return Data;
   }
    private String RubahTitikMenjadiSamaDgn(String Data){
        Data    = Data.replaceAll("\\.", ":");
        return Data;
   }

    private void TotalWaktu (){
        int ab = 0;
        for (a = 0; a < JComboWaktu.size(); a++ ){
             ab = ConvertHariJamKeMenit(JTextFieldWaktu.get(a).getText() +  JComboWaktu.get(a).getSelectedItem()) + ab;
        }
  
        TotalWaktu  = ab ;
        int Sisa1   ;
        int BrpHari;
        int Menit ;
        int Jam;
        
        BrpHari  = TotalWaktu /( 24 * 60 );      
        Sisa1    = TotalWaktu - (BrpHari * 24 * 60);            
        Jam      = Sisa1 /60;
        Menit    = Sisa1 - ( Jam * 60);

        JTextTotalWaktu.setText(String.valueOf("  " +BrpHari + " Hari  "+ Jam+ " Jam  " + Menit + " Menit").toString());
    }
    
    private int ValidasiNullData(String Data){
        if (Data.equalsIgnoreCase("")){
            return 0;
        }
        return Integer.valueOf(Data).intValue();
    }
    


    /*
     * Setiap kali convert waktu hatrs melalui ini
     * Kemudian baru di lanjut keConvert Jam Ke Menit
     */
    
     private int ConvertJamDKeMenit(String DataJam ){
        DataJam = RubahSamaDenganMenjadiDoubleStr(DataJam);
        if (!DataJam.contains(".")){
             DataJam = DataJam+".0";
        }

        String  DataMenit  = DataJam.replaceAll(".*\\.","");
        if (DataMenit.equalsIgnoreCase("") ||DataMenit.equalsIgnoreCase("0") ){
            DataMenit = "0";
        }
        int DataMenitInt   = Integer.valueOf(DataMenit).intValue();
        if (DataMenitInt >= 0 && DataMenitInt <= 60){
            DataJam            = DataJam.replaceAll("\\..*", "");            
            int HasilMenit     = Integer.valueOf(DataJam).intValue() * 60 ;
            int HasilAllMenit  = Integer.valueOf(DataMenit).intValue() + HasilMenit;
            return HasilAllMenit;  
        }  
         return DataMenitInt * 60;
    }
    
    private int  ConvertHariKeMenit(String DataHari){          
        try{
            int HasilMenit  =  Integer.valueOf(DataHari).intValue() * 60 * 24 ;
           return HasilMenit;
       }
       catch (Exception X){
           return 0;
       }
    }
    
    private int ConvertMenitKeMenit(  String Menit){
        try{
            return Integer.valueOf(Menit).intValue();
        }
        catch (Exception X){
            return 0;
        }
             
    }
    
    private void TabelAlokasiWaktu(){
        
        String header[] = {"No", "Nama Produksi","Total Menit", "Total Waktu", "Create Date", "Action"};
        TabelModelAlokasiWaktu = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 5) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTabelAlokasiWaktu.setModel(TabelModelAlokasiWaktu);
        
        /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(TabelModelAlokasiWaktu);
        JTabelAlokasiWaktu.setRowSorter(sorter);
       
        /*
         * Rata tengah atau kanan table
         */
        JTabelAlokasiWaktu.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelAlokasiWaktu.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        
        /*
         * Ukuran table JTabelResep
         */      
        int jarak_colom[] = {40,200,60, 150,180, 100, };
        Colom_table ukuran_colom = new Colom_table();
        ukuran_colom.ukuran_colom(JTabelAlokasiWaktu, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
         */
        JTabelAlokasiWaktu.getColumnModel().getColumn(5).setCellRenderer(  new ButtonJTable("Delete"));
        JTabelAlokasiWaktu.getColumnModel().getColumn(5).setCellEditor( new  ButtonJTableKeDuaAlokasiWaktu(new JCheckBox(),TabelModelAlokasiWaktu, JTabelAlokasiWaktu));
       
        /*
         * Disable drag colum tabel
         */       
        JTabelAlokasiWaktu.getTableHeader().setReorderingAllowed(false); 
        
    }
    
    private void TabelView(){
        
        String header[] = { "Nama Kegiatan","Start", "Finish", "Duration"};
        TabelModelView = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        if(colIndex == 5) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTabelView.setModel(TabelModelView);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(TabelModelView);
        JTabelView.setRowSorter(sorter);
       
        /*
         * Rata tengah atau kanan table
         */
        JTabelView.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelView.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        
        /*
         * Ukuran table JTabelResep
         */      
        int jarak_colom[] = {100,60, 60,60 };
        Colom_table ukuran_colom = new Colom_table();
        ukuran_colom.ukuran_colom(JTabelView, jarak_colom);

        /*
         * Disable drag colum tabel
         */       
        JTabelView.getTableHeader().setReorderingAllowed(false); 
        
    }
    
    private boolean FilterSave(){
        if (JTextNamaProduksi.getText().equalsIgnoreCase("")      
            || JTextTotalWaktu.getText().equalsIgnoreCase("")){
            return false;
        }
        else{
            return true;
        }
    }
    
    private boolean FilterSave2(JTextField data){
        if (data.getText().equalsIgnoreCase("")
            || data.getText().equalsIgnoreCase("0") ){
            return false;
        }
        else{
            return true;
        }
    }
    
     private void DeleteDataResep(){
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from header_alokasi_waktu where nama_produksi = '"+ JTextNamaProduksi.getText() + "'");
        AmbilDataDariDatabase();
     }
     
     private void EditDataAloakasi(){
         if (Edit == true){
             DeleteDataResep();
         }
     }
     
    private void SaveDatabaseDataAlokasiWaktu(){
        if (FilterSave() == true){

               String NamaProduksi  = JTextNamaProduksi.getText();  

               EditDataAloakasi();
               
               try {
                       Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                       Stm.executeUpdate("INSERT INTO header_alokasi_waktu "
                               + "(nama_produksi, total_menit, total_waktu,  updated_date)VALUES ('"
                               + NamaProduksi + "','" + TotalWaktu  + "','" + JTextTotalWaktu.getText() + "',"
                               +  " now())");     
                    
                       AmbilDataDariDatabase();
                    }
                    catch (Exception Ex){
                        JLabelAlokasiWaktuKeterangan.setText("Data Sudah Ada");
                        JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 12245 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
                 }              
            }
        else{
            JLabelAlokasiWaktuKeterangan.setText("Tidak Berhasil Di Save");
        }
       
     } 
    
    private void HapusDataJTabel(){
        /*
         * Logika hapus semua data di jtable
         */
        DefaultTableModel dtm = (DefaultTableModel) JTabelAlokasiWaktu.getModel();
        dtm.setRowCount(0); 
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
               HQ = stm.executeQuery("SELECT nama_produksi, total_menit,total_waktu,updated_date   from header_alokasi_waktu order by updated_date desc  ");              
               baris = HQ.getRow();

               while(HQ.next()  ){

                   String AlokasiNamaProduksi   = HQ.getString("nama_produksi");
                   String AlokasiTotalJam       = String.valueOf(HQ.getInt("total_menit")).toString();
                   String AlokasiUpdated        = HQ.getString("updated_date");
                   String AlokasiWaktu          = HQ.getString("total_waktu");    
    
                   String[] add         = {String.valueOf(HQ.getRow()).toString() , AlokasiNamaProduksi,  AlokasiTotalJam,
                   AlokasiWaktu, AlokasiUpdated};
                   TabelModelAlokasiWaktu.addRow(add);      
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
           
        JTabelAlokasiWaktu.setModel(TabelModelAlokasiWaktu);
        
     }

     /*
      * Hapus data dari Tabel
      */
     
class ButtonJTableKeDuaAlokasiWaktu extends DefaultCellEditor {
    private String label;
    protected JButton button;
    DefaultTableModel ModelTabel2;
    JTable Tabel2;
    public ButtonJTableKeDuaAlokasiWaktu(JCheckBox checkBox,  final DefaultTableModel ModelTabel,final JTable Tabel ) {
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
        MYSQL.MysqlDelete("delete from header_alokasi_waktu where nama_produksi = '"+ Data + "'");
        AmbilDataDariDatabase();
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

        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        JTextAlokasiInputWaktu = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        JTextTotalWaktu = new javax.swing.JTextField();
        JLabelAlokasiWaktuKeterangan = new javax.swing.JLabel();
        JButtonProsessAlokasiWaktu = new javax.swing.JButton();
        JButtonPrint = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        JButtonAlokasiWaktuSave = new java.awt.Button();
        JButtonAlokasiWaktuEdit = new java.awt.Button();
        JButtonAlokasiWaktuRefresh = new java.awt.Button();
        JButtonAlokasiWaktuNew = new java.awt.Button();
        JButtonAlokasiWaktuAdd = new java.awt.Button();
        jPanel9 = new javax.swing.JPanel();
        JTextNamaProduksi = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        JPanelInputDinamis = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTabelView = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelAlokasiWaktu = new javax.swing.JTable();

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

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/logo.png"))); // NOI18N

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N
        jLabel1.setText("Input Jam :");

        jLabel8.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N
        jLabel8.setText("Total  Waktu");

        JTextTotalWaktu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTextTotalWaktu.setEnabled(false);
        JTextTotalWaktu.setMargin(new java.awt.Insets(0, 0, 0, 0));
        JTextTotalWaktu.setName(""); // NOI18N

        JLabelAlokasiWaktuKeterangan.setForeground(new java.awt.Color(255, 0, 0));
        JLabelAlokasiWaktuKeterangan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabelAlokasiWaktuKeterangan.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        JButtonProsessAlokasiWaktu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Load.png"))); // NOI18N
        JButtonProsessAlokasiWaktu.setText("Proses");

        JButtonPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Print.png"))); // NOI18N
        JButtonPrint.setText("Print");
        JButtonPrint.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JTextTotalWaktu))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JTextAlokasiInputWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                                .addComponent(JButtonProsessAlokasiWaktu))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(JLabelAlokasiWaktuKeterangan, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JButtonPrint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JTextAlokasiInputWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(JButtonProsessAlokasiWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(JTextTotalWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JLabelAlokasiWaktuKeterangan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jTabbedPane1.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N

        JButtonAlokasiWaktuSave.setLabel("Save");

        JButtonAlokasiWaktuEdit.setLabel("Edit");

        JButtonAlokasiWaktuRefresh.setActionCommand("Refresh");
        JButtonAlokasiWaktuRefresh.setLabel("Refresh");

        JButtonAlokasiWaktuNew.setLabel("New");

        JButtonAlokasiWaktuAdd.setLabel("Add");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JButtonAlokasiWaktuAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(JButtonAlokasiWaktuSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonAlokasiWaktuEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonAlokasiWaktuRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonAlokasiWaktuNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JButtonAlokasiWaktuSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(JButtonAlokasiWaktuEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(JButtonAlokasiWaktuRefresh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(JButtonAlokasiWaktuNew, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(JButtonAlokasiWaktuAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        JTextNamaProduksi.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTextNamaProduksi.setEnabled(false);
        JTextNamaProduksi.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel7.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N
        jLabel7.setText("Nama Produksi");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextNamaProduksi)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(JTextNamaProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7))
        );

        JPanelInputDinamis.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout JPanelInputDinamisLayout = new javax.swing.GroupLayout(JPanelInputDinamis);
        JPanelInputDinamis.setLayout(JPanelInputDinamisLayout);
        JPanelInputDinamisLayout.setHorizontalGroup(
            JPanelInputDinamisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        JPanelInputDinamisLayout.setVerticalGroup(
            JPanelInputDinamisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 369, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JPanelInputDinamis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelInputDinamis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Setup", jPanel6);

        JTabelView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(JTabelView);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("View Tabel", jPanel10);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 1, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        JTabelAlokasiWaktu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(JTabelAlokasiWaktu);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button JButtonAlokasiWaktuAdd;
    private java.awt.Button JButtonAlokasiWaktuEdit;
    private java.awt.Button JButtonAlokasiWaktuNew;
    private java.awt.Button JButtonAlokasiWaktuRefresh;
    private java.awt.Button JButtonAlokasiWaktuSave;
    private javax.swing.JButton JButtonPrint;
    private javax.swing.JButton JButtonProsessAlokasiWaktu;
    private javax.swing.JLabel JLabelAlokasiWaktuKeterangan;
    private javax.swing.JPanel JPanelInputDinamis;
    private javax.swing.JTable JTabelAlokasiWaktu;
    private javax.swing.JTable JTabelView;
    private javax.swing.JTextField JTextAlokasiInputWaktu;
    private javax.swing.JTextField JTextNamaProduksi;
    private javax.swing.JTextField JTextTotalWaktu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
