/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.Colom_table;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author LANTAI3
 */
public class AlokasiWaktu extends javax.swing.JInternalFrame {
    
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah = new RenderingTengah();
    private DefaultTableModel TabelModelAlokasiWaktu;
    private SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    private Connection K = KD.createConnection();
    private int MenitBelanja = 0;
    private int MenitProduksi = 0;
    private int MenitPraProduksi = 0;
    private int MenitPacking = 0;
    private int MenitDilevery = 0;
    int Awal;
    int AwalInt;
    int TotalWaktu ;
    boolean Edit = false;
    
    /**
     * Creates new form AlokasiWaktu
     */
    private AlokasiWaktuView drawingPanel;
    public AlokasiWaktu() {
        
        initComponents();
        AksiAksi();
        //TampilanDataHasilAlokasi();
        TabelAlokasiWaktu();
        AmbilDataDariDatabase();    
    }

    private void HakAksesText(boolean Belanja, boolean PraProduksi, 
        boolean Produksi, boolean Packing,boolean Dilevery, boolean NamaProduksi){
        JTextBelanja.setEnabled(Belanja);
        JTextPraProduksi.setEnabled(PraProduksi);
        JTextProduksi.setEnabled(Produksi);
        JTextPacking.setEnabled(Packing);
        JTextDilevery.setEnabled(Dilevery);
        JTextNamaProduksi.setEnabled(NamaProduksi);
       
    }
    
    private void HakAksesTombol(boolean save, boolean edit, boolean delete, boolean CB){
        JButtonAlokasiWaktuRefresh.setEnabled(delete);
        JButtonAlokasiWaktuSave.setEnabled(save);
        JButtonAlokasiWaktuEdit.setEnabled(edit);
        JComboBoxBelanja.setEnabled(CB);
        JComboBoxPraProduksi.setEnabled(CB);
        JComboBoxDilevery.setEnabled(CB);
        JComboBoxProduksi.setEnabled(CB);
        JComboBoxPacking.setEnabled(CB);
    }
    
    private void AksiAksi(){
        
       JButtonAlokasiWaktuNew.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a){
                 HakAksesText(true,true, true, true,true, true);
                 HakAksesTombol(true, true, true, true);
                 JTextTotalWaktu.setText("");
                 JTextNamaProduksi.setText("");
                 JLabelAlokasiWaktuKeterangan.setText("");
                 
                 
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
               // JTextFieldDinamik(3);
                HakAksesText(false,false, false, false,false, false);
                HakAksesTombol(false, false, false, false);
                AmbilDataDariDatabase();
                JLabelAlokasiWaktuKeterangan.setText("");
                //JTextFieldDinamik();

            }
        });
        
         JTabelAlokasiWaktu.addMouseListener(new MouseAdapter() {
	      //  @Override
            @Override
	        public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    ViewDataAlokasiWaktu2();
                    JLabelAlokasiWaktuKeterangan.setText("");
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
        AntiHuruf. SetAntiAngkaPakeDataDoubel(JTextBelanja);
        AntiHuruf. SetAntiAngkaPakeDataDoubel(JTextPraProduksi);
        AntiHuruf. SetAntiAngkaPakeDataDoubel(JTextDilevery);
        AntiHuruf. SetAntiAngkaPakeDataDoubel(JTextProduksi);
        AntiHuruf. SetAntiAngkaPakeDataDoubel(JTextPacking);
        AntiHuruf. SetAntiAngkaPakeDataDoubel(JTextAlokasiInputWaktu);
        
        SetAntiKelebihanJamDanHarusPakeDataDouble(JComboBoxBelanja,JTextBelanja );
        SetAntiKelebihanJamDanHarusPakeDataDouble(JComboBoxPraProduksi, JTextPraProduksi);
        SetAntiKelebihanJamDanHarusPakeDataDouble(JComboBoxDilevery, JTextDilevery);
        SetAntiKelebihanJamDanHarusPakeDataDouble(JComboBoxProduksi, JTextProduksi);
        SetAntiKelebihanJamDanHarusPakeDataDouble(JComboBoxPacking, JTextPacking);
        //SetAntiKelebihanJamDanHarusPakeDataDouble2(JTextAlokasiInputWaktu);
        
        SetAntiKelebihanJamDanHarusPakeDataDouble3(  JButtonProsessAlokasiWaktu);
        
        /*
         * Fileter seleksi JCOMBOBOX
         */
        SeleksiMenitJamDanHari(JComboBoxBelanja,JTextBelanja);       
        SeleksiMenitJamDanHari(JComboBoxPraProduksi, JTextPraProduksi);
        SeleksiMenitJamDanHari(JComboBoxDilevery, JTextDilevery);
        SeleksiMenitJamDanHari(JComboBoxProduksi, JTextProduksi);
        SeleksiMenitJamDanHari(JComboBoxPacking, JTextPacking);
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
        
        int batas   = 6;
        if (lokasi2 < batas){
            JComboBox ComboWaktu    = new JComboBox();

            ComboWaktu.addItem("Menit");
            ComboWaktu.addItem("Jam");
            ComboWaktu.addItem("Hari");
            
            JTextField Waktu = new JTextField();
            
            JTextKeterangAlokasi.add(new JTextField());
            JTextFieldWaktu.add(Waktu);
            JComboWaktu.add(ComboWaktu);
            
            JTextKeterangAlokasi.get(JTextKeterangAlokasi.size() - 1).setLocation(getSize().width/85, getSize().height/22 + lokasi);
            JTextKeterangAlokasi.get(JTextKeterangAlokasi.size() - 1).setSize(120, 23);
            
            JTextFieldWaktu.get(JTextFieldWaktu.size() - 1).setLocation(getSize().width/7, getSize().height/22+ lokasi);
            JTextFieldWaktu.get(JTextFieldWaktu.size() - 1).setSize(50, 23);

            JComboWaktu.get(JComboWaktu.size() - 1).setLocation(getSize().width/5,getSize().height/22+ lokasi);
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
               SeleksiMenitJamDanHari(JComboWaktu.get(j), JTextFieldWaktu.get(j));
        
            }
        }
        lokasi +=28;
        lokasi2 +=1;
        repaint();
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
    
    private void PerhitunganTimeDuration(){

        String[] DataDuration = {JTextBelanja.getText(),
                                 JTextPraProduksi.getText(),
                                 JTextProduksi.getText(),
                                 JTextPacking.getText(),
                                 JTextDilevery.getText()
        };      
        
        int DataAwal           = ConvertJamDKeMenit(JTextAlokasiInputWaktu.getText());
        
        SelekseiMenitJamDanHari2(JComboBoxBelanja,JTextBelanja );
        SelekseiMenitJamDanHari2(JComboBoxPraProduksi, JTextPraProduksi);
        SelekseiMenitJamDanHari2(JComboBoxDilevery, JTextDilevery);
        SelekseiMenitJamDanHari2(JComboBoxProduksi, JTextProduksi);
        SelekseiMenitJamDanHari2(JComboBoxPacking, JTextPacking);
        /*
        int DataBelanja     = JTextBelanja.getText();
        int DataPProduksi   = ConvertJamDKeMenit(JTextPraProduksi.getText());
        int DataProduksi    = ConvertJamDKeMenit(JTextProduksi.getText());
        int DataPacking     = ConvertJamDKeMenit(JTextPacking.getText());
        int DataDilevery    = ConvertJamDKeMenit(JTextDilevery.getText());
        */
        int DataBelanjaFinish   = DataAwal +  MenitBelanja;
        int DataPProduksiFinish = DataBelanjaFinish + MenitPraProduksi;
        int DataProduksiFinish  = DataPProduksiFinish + MenitProduksi;
        int DataPackingFinish    = DataProduksiFinish + MenitPacking;
        int DataDileveryFinish  = DataPackingFinish    +  MenitDilevery;
        //ConvertMenitKeJam(DataPProduksiFinish);
        //System.out.println(ConvertMenitKeJamFinish(DataBelanjaFinish) +" zzz " );
        String DataAwalInput            = String.valueOf(DataAwal).toString();
        String DataStrBelanjaFinish     = ConvertMenitKeJamFinish(DataBelanjaFinish);
        String DataStrPProduksiFinish   = ConvertMenitKeJamFinish(DataPProduksiFinish);
        String DataStrProdukFinish      = ConvertMenitKeJamFinish(DataProduksiFinish);
        String DataStrPackingFinish     = ConvertMenitKeJamFinish(DataPackingFinish);
        String DataStrDileveryFinish    = ConvertMenitKeJamFinish(DataDileveryFinish);
        
    
        
        JPanelViewAlokasiWaktu.setLayout(new BorderLayout());
        JPanelViewAlokasiWaktu.setBackground(Color.white);   
        drawingPanel = new AlokasiWaktuView();
        drawingPanel.BelanjaStart     = RubahTitikMenjadiSamaDgn(JTextAlokasiInputWaktu.getText());
        drawingPanel.PProduksiStart   = RubahTitikMenjadiSamaDgn(DataStrBelanjaFinish);
        drawingPanel.ProduksiStart    = RubahTitikMenjadiSamaDgn(DataStrPProduksiFinish);
        drawingPanel.PackingStart     = RubahTitikMenjadiSamaDgn(DataStrProdukFinish);
        drawingPanel.DileveryStart    = RubahTitikMenjadiSamaDgn(DataStrPackingFinish);

        drawingPanel.BelanjaFinish    = RubahTitikMenjadiSamaDgn(DataStrBelanjaFinish);
        drawingPanel.PProduksiFinish  = RubahTitikMenjadiSamaDgn(DataStrPProduksiFinish);
        drawingPanel.ProduksiFinish   = RubahTitikMenjadiSamaDgn(DataStrProdukFinish);
        drawingPanel.PackingFinish    = RubahTitikMenjadiSamaDgn(DataStrPackingFinish);
        drawingPanel.DileveryFinish   = RubahTitikMenjadiSamaDgn(DataStrDileveryFinish);

        drawingPanel.BelanjaDuration  = RubahTitikMenjadiSamaDgn(JTextBelanja.getText()) + " " +JComboBoxBelanja.getSelectedItem();
        drawingPanel.PProduksiDuration= RubahTitikMenjadiSamaDgn(JTextPraProduksi.getText()) + " " +JComboBoxPraProduksi.getSelectedItem();
        drawingPanel.ProduksiDuration = RubahTitikMenjadiSamaDgn(JTextProduksi.getText()) + " " +JComboBoxProduksi.getSelectedItem();
        drawingPanel.PackingDuration  = RubahTitikMenjadiSamaDgn(JTextPacking.getText()) + " " +JComboBoxPacking.getSelectedItem();
        drawingPanel.DilveryDuration  = RubahTitikMenjadiSamaDgn(JTextDilevery.getText())+ " " +JComboBoxDilevery.getSelectedItem();
        
        /*
         *  jp.setPreferredSize(new Dimension(1500,1500));

            JScrollPane editorScroll = new JScrollPane(jp);

            jf.getContentPane().add(editorScroll);

            jf.setPreferredSize(new Dimension(300, 300));
            jf.setVisible(true);
         */
        //drawingPanel . revalidate();
       // pack();
        //drawingPanel.revalidate();
        //drawingPanel.repaint();
        //drawingPanel.remove(this);
        //JPanelViewAlokasiWaktu.add(drawingPanel, BorderLayout.CENTER);
        
        //JScrollPane panelPane = new JScrollPane(drawingPanel);
        
        //panelPane.setPreferredSize(new Dimension(200,200));
        //drawingPanel.setPreferredSize(new Dimension(350,400));
        JPanelViewAlokasiWaktu.add(drawingPanel, BorderLayout.CENTER);
        
        //panelPane.setViewportView(JPanelViewAlokasiWaktu);
        //this.getContentPane().add(panelPane);
        //this.pack();
        //drawingPanel.revalidate();
        //drawingPanel.repaint();
        //drawingPanel.set
        //drawingPanel.setLayout(null);
        
       // JScrollPane scrollPane = new JScrollPane();
       // JScrollBar scrollBar = new JScrollBar();//new JScrollBar(SwingConstants.HORIZONTAL);
       // scrollBar.setModel(scrollPane.getHorizontalScrollBar().getModel());
        
        //panel.add(textArea);
        //getContentPane().setLayout(null);
        // BoundedRangeModel brm = (BoundedRangeModel) drawingPanel;
        //panelPane.setModel(brm);
        //panelPane.repaint();
        
        // scrollPane.setViewportView(drawingPanel);
        //JPanelViewAlokasiWaktu.add(new JScrollPane(drawingPanel),BorderLayout.CENTER);
        //drawingPanel.setOpaque(false);
        //panelPane.scrollRectToVisible(drawingPanel.getBounds());
        //JPanelViewAlokasiWaktu.setLayout(new BorderLayout());
        
        // JPanelViewAlokasiWaktu.add(panelPane, BorderLayout.CENTER);
        
        //JPanelViewAlokasiWaktu.add(drawingPanel, BorderLayout.CENTER);
        //getContentPane().add(scrollPane);
        //add(drawingPanel);
        
        //JPanelViewAlokasiWaktu.setPreferredSize(new Dimension(300, 300));
        /*
         * Agar bisa repaint di grafifnya
         */
        /*
         * 

Just IMHO. Not sure it's 100% correct.

invalidate() marks the container as invalid. Means the content is somehow wrong and must be relayed out. But it's just a kind of mark/flag. It's possible that multiple invalid containers must be refreshed later.

validate() performs relayout. It means invalid content is asked for all the sizes and all the subcomponents' sizes are set to proper values by LayoutManager.

revalidate() is just sum of both. It marks the container as invalid and performs layout of the container.

         */
        
        drawingPanel.setVisible(true);
        //panelPane.revalidate();
        //JPanelViewAlokasiWaktu.getcon
        //drawingPanel.setVisible(true);
        drawingPanel.revalidate();
        drawingPanel.invalidate();
        //drawingPanel.validate();
        drawingPanel.repaint();
        //if (drawingPanel.revalidate()){
            
        //}
        //pack();
        //JPanelViewAlokasiWaktu.setVisible(true);
        //JPanelViewAlokasiWaktu.revalidate();
       //JPanelViewAlokasiWaktu.repaint();
//        panelPane.revalidate();        
       // panelPane.repaint();
        //JPanelViewAlokasiWaktu.repaint();
        //JPanelViewAlokasiWaktu.setVisible(false);
        
              //setLocationRelativeTo(null);
        //JPanelViewAlokasiWaktu.setVisible(true);
        //drawingPanel.removeAll();
        //drawingPanel.setVisible(true);

        /*
         * Agar bisa repaint di grafifnya
         */
       // drawingPanel.revalidate();
        //revalidate();
        //repaint();
         //pack();
            // System.out.println("xxxx");
       
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
    
    private void ConvertTotalWaktuKeMenitJamHari(String Data){
        String Hari = Data.replaceAll("Hari.*", "");
        String Jam  = Data.replaceAll(".*Hari", "");
        String Jam3 = Jam.replaceAll("Jam.*", "");
        String Menit= Data.replaceAll(".*Jam", "");
        String Menit2= Menit.replaceAll("Menit", "");
        
        Hari = Hari.replaceAll("[\\s]", "");
        Jam3 = Jam3.replaceAll("[\\s]", "");
        Menit2 = Menit2.replaceAll("[\\s]", "");
        
        //System.out.println(Hari + "-" + Jam3 + "-" + Menit2);
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
     
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT nama_produksi , waktu_belanja, waktu_praproduksi, waktu_produksi, waktu_packing, waktu_dilevery from header_alokasi_waktu  where nama_produksi ='"+ Data +"'");              

               while(HQ.next()  ){
                   String AlokasiBelanja        = HQ.getString("waktu_belanja");
                   String AlokasiPraProduksi    = HQ.getString("waktu_praproduksi");
                   String AlokasiProduksi       = HQ.getString("waktu_produksi");
                   String AlokasiPacking        = HQ.getString("waktu_packing");
                   String AlokasiDilevery       = HQ.getString("waktu_dilevery");
                   
                  // JTextTotalWaktu.setText(AlokasiWaktuAll);

                   ViewAlokasiWaktuSetJComboBox(AlokasiBelanja , JComboBoxBelanja );
                   ViewAlokasiWaktuSetJComboBox(AlokasiPraProduksi, JComboBoxPraProduksi );
                   ViewAlokasiWaktuSetJComboBox(AlokasiProduksi,JComboBoxProduksi );
                   ViewAlokasiWaktuSetJComboBox(AlokasiPacking, JComboBoxPacking);
                   ViewAlokasiWaktuSetJComboBox(AlokasiDilevery, JComboBoxDilevery);
                   
                   JTextBelanja.setText(ViewAlokasiWaktuSetJTextField(AlokasiBelanja));
                   JTextPraProduksi.setText(ViewAlokasiWaktuSetJTextField(AlokasiPraProduksi));
                   JTextProduksi.setText(ViewAlokasiWaktuSetJTextField(AlokasiProduksi));
                   JTextPacking.setText(ViewAlokasiWaktuSetJTextField(AlokasiPacking));
                   JTextDilevery.setText(ViewAlokasiWaktuSetJTextField(AlokasiDilevery));
                   JTextUntukMenghitungTotalWaktu();
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
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
                       JTextUntukMenghitungTotalWaktu();
                  }
                 else if (CB.getSelectedItem().equals("Jam")){
                    LimitCharInput (x.getText(), 2,  e);                            
                        if (ConvertJamDKeMenit(x.getText())  > 24 * 60 ){
                             x.setText("24");
                        }
                        JTextUntukMenghitungTotalWaktu();
                    }
                  JTextUntukMenghitungTotalWaktu();

                }
        });
    }
    
    /*
     * Input waktu biar tidak kelebihan
     */
    public void  SetAntiKelebihanJamDanHarusPakeDataDouble2 ( final JTextField x){
 
        x.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e)
                {
                  char c = e.getKeyChar();
                  try {
                      Awal  = Integer.valueOf(x.getText()).intValue();
                      
                  }
                  catch (Exception X){
                      Awal = 0;
                  }

                 LimitCharInput (x.getText(), 2,  e);                            
                    if (ConvertJamDKeMenit(x.getText())  > 24 * 60 ){
                         x.setText("24");
                    }
                    PerhitunganTimeDuration();
                }
        });       
    }
    
    public void  SetAntiKelebihanJamDanHarusPakeDataDouble3 (JButton X){ 
            X.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                      Awal  = Integer.valueOf(JTextAlokasiInputWaktu.getText()).intValue();
                      
                  }
                  catch (Exception X){
                      Awal = 0;
                  }
                if (ConvertJamDKeMenit(JTextAlokasiInputWaktu.getText())  > 24 * 60 ){
                        JTextAlokasiInputWaktu.setText("24");
                    }
                 PerhitunganTimeDuration();
                
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
    
    private void SeleksiMenitJamDanHari(final JComboBox CB, final JTextField TF){
        
        CB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               /*
                * Anti lebih dari 4 karakter
                */
                SelekseiMenitJamDanHari2(CB, TF);
                
            }
        });      
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
    
    private void SelekseiMenitJamDanHari2(final JComboBox CB, final JTextField TF){
        if (CB.getSelectedItem().equals("Jam")) {
                if (LimitCharInput (TF.getText(),5)  && Double.valueOf(RubahSamaDenganMenjadiDouble(TF.getText())).doubleValue() <= 24
                        ){
                    JComboBoxAgarTahuComponent(CB, TF);
                }
                 else{
                     TF.setText("0");
                 }    
                 }
                 else if (CB.getSelectedItem().equals("Menit") ){
                      if (LimitCharInput (TF.getText(),2) && Double.valueOf(RubahSamaDenganMenjadiDouble(TF.getText())).doubleValue() <= 60
                        ){
                         JComboBoxAgarTahuComponent(CB, TF);
                      }
                       else{
                          TF.setText("0");
                      }                
                 }
                 else if (CB.getSelectedItem().equals("Hari") ) {
                      if (LimitCharInput (TF.getText(),2)){
                       JComboBoxAgarTahuComponent(CB, TF);
                      }
                       else{
                          TF.setText("0");
                      }
                 }
                 else {
                     //OptionPane.showMessageDialog(null, "Data Error");
                     TF.setText("0");
                 }
 
    }
 
    private void TotalWaktu (){
        TotalWaktu  = MenitBelanja  + MenitPraProduksi + MenitProduksi  + MenitPacking  + MenitDilevery ;
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
    
    private void JTextUntukMenghitungTotalWaktu(){
        SelekseiMenitJamDanHari2(JComboBoxBelanja,JTextBelanja );
        SelekseiMenitJamDanHari2(JComboBoxPraProduksi, JTextPraProduksi);
        SelekseiMenitJamDanHari2(JComboBoxDilevery, JTextDilevery);
        SelekseiMenitJamDanHari2(JComboBoxProduksi, JTextProduksi);
        SelekseiMenitJamDanHari2(JComboBoxPacking, JTextPacking);
        TotalWaktu ();
    }
    
    private void JComboBoxAgarTahuComponent(final JComboBox CB, JTextField TF){
      JComponent component = CB;
        if(component instanceof JComboBox) {
            switch (component.getName()){
                case "Belanja" :                     
                    this.MenitBelanja       = HasilConvert(CB, JTextBelanja.getText() );                    
                    TotalWaktu ();
                    break;
                case "PraProduksi":
                    this.MenitPraProduksi   = HasilConvert(CB, JTextPraProduksi.getText() );
                    TotalWaktu ();
                    break;
                case "Produksi":
                    this.MenitProduksi      = HasilConvert(CB, JTextProduksi.getText() );
                    TotalWaktu ();
                    break;
                case "Packing":
                    this.MenitPacking       = HasilConvert(CB, JTextPacking.getText() );
                    TotalWaktu ();
                    break;
                case "Dilevery":
                    this.MenitDilevery      = HasilConvert(CB, JTextDilevery.getText() );
                    TotalWaktu ();
                    break;
                default :
                    System.exit(0);
                    break;                  
            }
        }        
    }
    
    /*
     * Setiap kali convert waktu hatrs melalui ini
     * Kemudian baru di lanjut keConvert Jam Ke Menit
     */
    private int HasilConvert(JComboBox CB, String Data ){
        if (CB.getSelectedItem().equals("Jam")) {
            return ConvertJamDKeMenit(Data);
                   
        }
        else if (CB.getSelectedItem().equals("Menit")){
            return ConvertMenitKeMenit( Data);

        }
        else {
            return ConvertHariKeMenit(Data);
        }
    }
    
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
    
    private boolean FilterSave(){
        if (JTextNamaProduksi.getText().equalsIgnoreCase("")
            || JTextBelanja.getText().equalsIgnoreCase("0")
            || JTextBelanja.getText().equalsIgnoreCase("")
            || JTextProduksi.getText().equalsIgnoreCase("0")
            || JTextProduksi.getText().equalsIgnoreCase("")
            || JTextPraProduksi.getText().equalsIgnoreCase("0")
            || JTextPraProduksi.getText().equalsIgnoreCase("")
            || JTextPacking.getText().equalsIgnoreCase("0")
            || JTextPacking.getText().equalsIgnoreCase("")
            || JTextDilevery.getText().equalsIgnoreCase("0")
            || JTextDilevery.getText().equalsIgnoreCase("")
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
               int Belanja          = MenitBelanja;
               int PraProduksi      = MenitPraProduksi;
               int Produksi         = MenitProduksi;
               int Packing          = MenitPacking ;
               int Dilevery         = MenitDilevery;
               String NamaProduksi  = JTextNamaProduksi.getText();  
               
               String SBelanja      = JTextBelanja.getText() +"-"+ JComboBoxBelanja.getSelectedIndex();
               String SPraProduksi  = JTextPraProduksi.getText() +"-"+ JComboBoxPraProduksi.getSelectedIndex();
               String SProduksi     = JTextProduksi.getText() + "-"+ JComboBoxProduksi.getSelectedIndex();
               String SPacking      = JTextPacking.getText() + "-"+ JComboBoxPacking.getSelectedIndex();
               String SDilevery     = JTextDilevery.getText() + "-"+ JComboBoxDilevery.getSelectedIndex();              
               
               EditDataAloakasi();
               
               try {
                       Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                       Stm.executeUpdate("INSERT INTO header_alokasi_waktu "
                               + "(nama_produksi, total_menit, total_waktu, jam_belanja, jam_praproduksi, jam_produksi, jam_packing, jam_dilevery, waktu_belanja, waktu_praproduksi, waktu_produksi, waktu_packing, waktu_dilevery, updated_date)VALUES ('"
                               + NamaProduksi + "','" + TotalWaktu  + "','" + JTextTotalWaktu.getText() + "','"
                               + Belanja + "','" +  PraProduksi + "','" +  Produksi + "','" 
                               + Packing+ "','" + Dilevery + "','" 
                               + SBelanja  + "','" + SPraProduksi  + "','" 
                               + SProduksi + "','" + SPacking  + "','" 
                               + SDilevery + "'," 
                               + " now())");     
                    
                       AmbilDataDariDatabase();
                    }
                    catch (Exception Ex){
                        JLabelAlokasiWaktuKeterangan.setText("Data Sudah Ada");
                        JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1224 : "  +Ex, " Error", JOptionPane.ERROR_MESSAGE);
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        JPanelViewAlokasiWaktu = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        JTextBelanja = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        JTextPraProduksi = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        JTextProduksi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        JTextPacking = new javax.swing.JTextField();
        JTextDilevery = new javax.swing.JTextField();
        JComboBoxBelanja = new javax.swing.JComboBox();
        JComboBoxPraProduksi = new javax.swing.JComboBox();
        JComboBoxProduksi = new javax.swing.JComboBox();
        JComboBoxPacking = new javax.swing.JComboBox();
        JComboBoxDilevery = new javax.swing.JComboBox();
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
                            .addComponent(JLabelAlokasiWaktuKeterangan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JTextAlokasiInputWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                                .addComponent(JButtonProsessAlokasiWaktu)))
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
                .addComponent(JLabelAlokasiWaktuKeterangan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N

        javax.swing.GroupLayout JPanelViewAlokasiWaktuLayout = new javax.swing.GroupLayout(JPanelViewAlokasiWaktu);
        JPanelViewAlokasiWaktu.setLayout(JPanelViewAlokasiWaktuLayout);
        JPanelViewAlokasiWaktuLayout.setHorizontalGroup(
            JPanelViewAlokasiWaktuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 283, Short.MAX_VALUE)
        );
        JPanelViewAlokasiWaktuLayout.setVerticalGroup(
            JPanelViewAlokasiWaktuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 379, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JPanelViewAlokasiWaktu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JPanelViewAlokasiWaktu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("View Alokasi Waktu", jPanel3);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N
        jLabel2.setText("Belanja ");

        JTextBelanja.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTextBelanja.setEnabled(false);
        JTextBelanja.setMargin(new java.awt.Insets(0, 0, 0, 0));
        JTextBelanja.setName("Belanja"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N
        jLabel3.setText("Pra Produksi");

        JTextPraProduksi.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTextPraProduksi.setEnabled(false);
        JTextPraProduksi.setMargin(new java.awt.Insets(0, 0, 0, 0));
        JTextPraProduksi.setName("PraProduksi"); // NOI18N

        jLabel4.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N
        jLabel4.setText("Produksi");

        JTextProduksi.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTextProduksi.setEnabled(false);
        JTextProduksi.setMargin(new java.awt.Insets(0, 0, 0, 0));
        JTextProduksi.setName("Produksi"); // NOI18N

        jLabel5.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N
        jLabel5.setText("Packing/Penataan");

        jLabel6.setFont(new java.awt.Font("Garamond", 0, 14)); // NOI18N
        jLabel6.setText("Dilevery");

        JTextPacking.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTextPacking.setEnabled(false);
        JTextPacking.setMargin(new java.awt.Insets(0, 0, 0, 0));
        JTextPacking.setName("Packing"); // NOI18N

        JTextDilevery.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JTextDilevery.setEnabled(false);
        JTextDilevery.setMargin(new java.awt.Insets(0, 0, 0, 0));
        JTextDilevery.setName("Dilevery"); // NOI18N

        JComboBoxBelanja.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Menit", "Jam", "Hari" }));
        JComboBoxBelanja.setEnabled(false);
        JComboBoxBelanja.setName("Belanja"); // NOI18N

        JComboBoxPraProduksi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Menit", "Jam", "Hari" }));
        JComboBoxPraProduksi.setEnabled(false);
        JComboBoxPraProduksi.setName("PraProduksi"); // NOI18N

        JComboBoxProduksi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Menit", "Jam", "Hari" }));
        JComboBoxProduksi.setEnabled(false);
        JComboBoxProduksi.setName("Produksi"); // NOI18N

        JComboBoxPacking.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Menit", "Jam", "Hari" }));
        JComboBoxPacking.setEnabled(false);
        JComboBoxPacking.setName("Packing"); // NOI18N

        JComboBoxDilevery.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Menit", "Jam", "Hari" }));
        JComboBoxDilevery.setEnabled(false);
        JComboBoxDilevery.setName("Dilevery"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(JTextDilevery, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JComboBoxDilevery, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(JTextProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JComboBoxProduksi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(JTextBelanja, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JComboBoxBelanja, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(JTextPraProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JComboBoxPraProduksi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(JTextPacking, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JComboBoxPacking, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JTextBelanja, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JComboBoxBelanja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(JTextPraProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JComboBoxPraProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(JTextProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JComboBoxProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(JTextPacking, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JComboBoxPacking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(JTextDilevery, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JComboBoxDilevery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        javax.swing.GroupLayout JPanelInputDinamisLayout = new javax.swing.GroupLayout(JPanelInputDinamis);
        JPanelInputDinamis.setLayout(JPanelInputDinamisLayout);
        JPanelInputDinamisLayout.setHorizontalGroup(
            JPanelInputDinamisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        JPanelInputDinamisLayout.setVerticalGroup(
            JPanelInputDinamisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 155, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(JPanelInputDinamis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPanelInputDinamis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Setup", jPanel6);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    private javax.swing.JButton JButtonProsessAlokasiWaktu;
    private javax.swing.JComboBox JComboBoxBelanja;
    private javax.swing.JComboBox JComboBoxDilevery;
    private javax.swing.JComboBox JComboBoxPacking;
    private javax.swing.JComboBox JComboBoxPraProduksi;
    private javax.swing.JComboBox JComboBoxProduksi;
    private javax.swing.JLabel JLabelAlokasiWaktuKeterangan;
    private javax.swing.JPanel JPanelInputDinamis;
    private javax.swing.JPanel JPanelViewAlokasiWaktu;
    private javax.swing.JTable JTabelAlokasiWaktu;
    private javax.swing.JTextField JTextAlokasiInputWaktu;
    private javax.swing.JTextField JTextBelanja;
    private javax.swing.JTextField JTextDilevery;
    private javax.swing.JTextField JTextNamaProduksi;
    private javax.swing.JTextField JTextPacking;
    private javax.swing.JTextField JTextPraProduksi;
    private javax.swing.JTextField JTextProduksi;
    private javax.swing.JTextField JTextTotalWaktu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
