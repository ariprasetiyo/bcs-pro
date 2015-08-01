/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.ComponentHanyaAngka;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.ValidasiInputResep;
import SistemPro.app_search1;
import SistemPro.app_search_data_beban;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import SistemPro.SatuUntukSemua;
/**
 *
 * @author LANTAI3
 */
public class BebanMetode3Panel extends javax.swing.JPanel {
    
    private ButtonGroup GroupsStatusAsset = new ButtonGroup();
    private SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    private Connection K = KD.createConnection();
    LaporanPerhitunganBiayaPenjualan BiayaPenjualan;
    DefaultTableModel   Modeltabel  = new DefaultTableModel();
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();
    private SistemPro.KomaToString ConvertAngka = new SistemPro.KomaToString();
    ComponentHanyaAngka AntiHuruf   = new ComponentHanyaAngka();
    SatuUntukSemua Satu = new SatuUntukSemua();
    
    String AA;
    boolean AAA = false;

    /**
     * Creates new form BebanMetode3Panel
     */
    public BebanMetode3Panel() {
        initComponents();
        AksiAksi();
        JTabelBiaya.setVisible(false);
    }
    
     int NoJTabbed = 1;
    private void AksiAksi(){
        JComboBoxFoodCost.setSelectedIndex(-1);
        final JTextField JTextFieldItem = (JTextField)JComboBoxFoodCost.getEditor().getEditorComponent();
        JTextFieldItem.setText("");
        JTextFieldItem.addKeyListener(new app_search1(JComboBoxFoodCost));
        
        JComboBoxFoodCost.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //ResetTampilan();
                
                AmbilDataUntukPorsi();   
                new javax.swing.DefaultComboBoxModel( app_search_data_beban.getData().toArray());
                
               JTabelBiaya.setVisible(true);
                     Tabel(JLabelBebanPorsi.getText());
                     /*
                      * Ambil data transaksi saja
                      */
                      String DataNoTransaksi      = (String) JComboBoxFoodCost.getSelectedItem();
                      DataNoTransaksi             = Satu.FilterNamaTransaksiSaja(DataNoTransaksi, JLabelBebanKet);
                      AmbilDataBahanResep(DataNoTransaksi);
                
                if (JComboBoxFoodCost.isPopupVisible()){
                    JComboBoxFoodCost.setEditable(false);
                }
                
            }
        });
        jButton1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ResetTampilan();               
            }
        });
        
        JComboBoxFoodCost.addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e) {
                JComboBoxFoodCost.setEditable(true);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {             
            }
            @Override
            public void mouseEntered(MouseEvent e) {             
            }
            @Override
            public void mouseExited(MouseEvent e) {            
            }
        });
        
        JButtonProses1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent E){                       
                String[] DataPesanErro  = {"Kenaikan Yang DiHarapkan","Produksi 1 Bulan", "LPG", "BBM", "Biaya Listrik", "Biaya Air", "Tenaga Kerja Langsung","Jumlah Tenaga Kerja Langsung", "Gaji Karyawan", "Penyusutan Alat",
                "Biaya Sewa","Biaya Alat Kantor","Bahan Pembantu"
               };
                JTextField[] TextCek = {JTextFieldPersenKeuntungan, PesananPerbulan, LPG, BBM, BiayaListrik, BiayaAir, TenagaKerjaLangsung, JumlahTenagaKerjaLangsung, GajiKaryawan, PenyusutanAlat,
                BiayaSewa,BiayaAlatKantor,BahanPembantu};
                
                ValidasiData(TextCek,DataPesanErro );                        
                SendBebanMetode3Report( E);
            }
        });


        AntiHuruf.SetAntiAngkaLimit(JTextFieldPersenKeuntungan, 3);
        AntiHuruf.SetAntiAngka(PesananPerbulan);
        AntiHuruf.SetAntiAngkaPakeKoma(LPG);
        AntiHuruf.SetAntiAngkaPakeKoma(BBM);
        AntiHuruf.SetAntiAngka(BiayaListrik);
        AntiHuruf.SetAntiAngka(TenagaKerjaLangsung);
        AntiHuruf.SetAntiAngka(BiayaAir);
        AntiHuruf.SetAntiAngka(GajiKaryawan);
        AntiHuruf.SetAntiAngkaPakeKoma(PenyusutanAlat);
        AntiHuruf.SetAntiAngka(BiayaSewa);
        AntiHuruf.SetAntiAngka(BiayaAlatKantor);
       
    }
    
    
    String aPesananPerbulan,
            aLPG, 
            aBBM ,
            aBiayaListrik, 
            aBiayaAir, 
            aenagaKerjaLangsung,
            aGajiKaryawan, 
            aPenyusutanAlat, 
            aBiayaSewa, 
            aBiayaAlatKantor, 
            aJLabelBebanPorsi,
            aBiayaBBM, xxxSolar, xxxAlatKantor, xxxPemeliharaanGedung,
            aPorsiSekarang, aTenagaKerjaLangsung, aJumlahTenagaKerjaLangsung, aLabaDiHarapkan, aBahanPembantu ;
    double HitLpg = 0, HitBensin, HasilListrik, HasilAir, HasilTenagaLangsung, HasilTidakTenagaLangsung,
            HasilPenyusutan;
    String[] Label = new String[30];
    private void SendBebanMetode3Report(ActionEvent E){
            HashMap hashMap = new HashMap();
            ISIData();
            Sementara();
            PrintJasper(E, reportparametermap1);
            PrintJasper2(E, reportparametermap2);
            /*
            BebanMetode3Panel multipages = new BebanMetode3Panel();  
            JasperPrint allpages = null;  
                try {
                    allpages = multipages.getLinkedStaticpages();
                } catch (IOException ex) {
                    Logger.getLogger(BebanMetode3Panel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JRException ex) {
                    Logger.getLogger(BebanMetode3Panel.class.getName()).log(Level.SEVERE, null, ex);
                }
                
          JRViewer viewer = new JRViewer(allpages); 
          //JasperViewer viewer = new JasperViewer(allpages, false);
          BebanMetode3RepotPanel ccc = new BebanMetode3RepotPanel(viewer);
                    
          SistemPro.TambahTab TabNewViewData = new SistemPro.TambahTab();
          TabNewViewData.createTabButtonActionPerformed(E, jTabbedPaneLihatProses,
                                    "View Report " + NoJTabbed, ccc,
                                   // ( Panjang, Tinggi )
                                    new Dimension(300, 300), new Dimension(1000, 1000));
           NoJTabbed++; 
        
           viewer.setVisible(true); */
       
    }
    
    
    private void ISIData(){
            aPesananPerbulan = PesananPerbulan.getText();
            aLPG = LPG.getText();
            aBBM =  BBM.getText();
            aBiayaListrik= BiayaListrik.getText();
            aBiayaAir= BiayaAir.getText();
            aTenagaKerjaLangsung = TenagaKerjaLangsung.getText();
            aGajiKaryawan= GajiKaryawan.getText();
            aPenyusutanAlat= PenyusutanAlat.getText();
            aBiayaSewa= BiayaSewa.getText();
            aBiayaAlatKantor= BiayaAlatKantor.getText();
            aJumlahTenagaKerjaLangsung = JumlahTenagaKerjaLangsung.getText();
            aLabaDiHarapkan = JTextFieldPersenKeuntungan.getText();
            aBahanPembantu = BahanPembantu.getText();
            /*
             * Buat Rumu
             * (total harga LPG pada database /total pesanan porsi dlm 1 bulan) x jml porsi pesanan sekarang          
             * = (Harga LPG / Pesanan porsi 1 bulan) x jml porsi pesanan sekarang 
             * ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(KenaikanYangDiHarapkanInt)) 
             */
            
            /*
             * LPG
             */
            aPorsiSekarang = JLabelBebanPorsi.getText().replaceAll("\\s.*", "");
            Label[1] = "= ( "+ ConvertAngka.NilaiRupiah(aLPG )+ " / " + ConvertAngka.NilaiRupiah(aPesananPerbulan ) + " ) x " + ConvertAngka.NilaiRupiah(aPorsiSekarang);                    
            HitLpg = (Satu.BersihDataKeDoubel(aLPG) / Satu.BersihDataKeInt(aPesananPerbulan))* Satu.BersihDataKeInt(aPorsiSekarang);
            Label[2] = "= " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(HitLpg));
            
            /*
             * Biaya Bensin
             */
            HitBensin = Satu.BersihDataKeDoubel(aBBM) ;
            xxxSolar = "= ( "+ ConvertAngka.NilaiRupiah(aBBM )+ " / " + ConvertAngka.NilaiRupiah(aPesananPerbulan ) + " ) x " + ConvertAngka.NilaiRupiah(aPorsiSekarang); 
            double HitBiayaBensi = (Satu.BersihDataKeDoubel(aBBM) / Satu.BersihDataKeInt(aPesananPerbulan))* Satu.BersihDataKeInt(aPorsiSekarang);
            Label[3] = "= " + ConvertAngka.NilaiRupiah(HitBiayaBensi);
            HitBensin = HitBiayaBensi;
            
            /*
             * Biaya Umum
             * Listrik
                = (total listrik per bulan / total pesanan porsi dalam 1 bln)* jml porsi pesanan sekarang
                = (300.000/3000) x 200
                = 60.000
                Air
                = …………………….. (sama diatas)
             */
            Label[4] = "( " +  ConvertAngka.NilaiRupiah(aBiayaListrik) + " / "+ ConvertAngka.NilaiRupiah(aPesananPerbulan )+ " ) x " + ConvertAngka.NilaiRupiah(aPorsiSekarang) ;
            HasilListrik = (Satu.BersihDataKeDoubel(aBiayaListrik) / Satu.BersihDataKeInt(aPesananPerbulan))* Satu.BersihDataKeInt(aPorsiSekarang);
            Label[5] =  "= " +ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(HasilListrik));
            
            /*
             * Biaya air
             */
            Label[6] = "= ( " +  ConvertAngka.NilaiRupiah(aBiayaAir) + " / "+ ConvertAngka.NilaiRupiah(aPesananPerbulan) + " ) x " + ConvertAngka.NilaiRupiah(aPorsiSekarang) ;
            HasilAir = (Satu.BersihDataKeDoubel(aBiayaAir) / Satu.BersihDataKeInt(aPesananPerbulan))* Satu.BersihDataKeInt(aPorsiSekarang);
            Label[7] = "= "+ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(HasilAir));
            
            /*
             * BIAYA TENAGA KERJA
                Tenaga kerja langsung
                = upah per porsi xjumlah pesanan sekarang x jmlah orang (ex :3 org)
                = 500 x 200 x 3
                = 300.000
                Tenaga kerja tidak langsung
                = (total gaji karyawan / total pesanan porsi dalam 1 bln)* jml porsi pesanan sekarang
                = (900.000/3000) *200
                = 180.000
                * aPesananPerbulan,
             */
            Label[8] = "= " +aTenagaKerjaLangsung + " x "+ConvertAngka.NilaiRupiah(aPorsiSekarang )+ " x " + ConvertAngka.NilaiRupiah(aJumlahTenagaKerjaLangsung) ;
            HasilTenagaLangsung = (Satu.BersihDataKeDoubel(aTenagaKerjaLangsung) * Satu.BersihDataKeInt(aPorsiSekarang))* Satu.BersihDataKeInt(aJumlahTenagaKerjaLangsung);
            Label[9] = "= " +ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(HasilTenagaLangsung));
            
            /*
             * Tenga krja tidak langsung
             */
            Label[10] = "= ( " + ConvertAngka.NilaiRupiah(aGajiKaryawan) + " / "+ ConvertAngka.NilaiRupiah(aPesananPerbulan )+ " ) x " +ConvertAngka.NilaiRupiah( aPorsiSekarang) ;
            HasilTidakTenagaLangsung = (Satu.BersihDataKeDoubel(aGajiKaryawan) / Satu.BersihDataKeInt(aPesananPerbulan))* Satu.BersihDataKeInt(aPorsiSekarang);
            Label[11] = "= "+ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(HasilTidakTenagaLangsung));
            
            /*
             * BIAYA TIDAK LANGSUNG
            Biaya penyusutan alat
            = (penyusutan alat 1 bln / total pesanan porsi dalam 1 bln)* jml porsi pesanan sekarang
            = (200.000/3000) * 200
            = 40.000
            Biaya sewa dan pemiharaan gedung
            (sama seperti diatas) ex : 25000
            Alat kantor
             */
            Label[12] = "= ( " + ConvertAngka.NilaiRupiah(aPenyusutanAlat) + " / "+ConvertAngka.NilaiRupiah(aPesananPerbulan) + " ) x " + ConvertAngka.NilaiRupiah(aPorsiSekarang );
            HasilPenyusutan = (Satu.BersihDataKeDoubel(aPenyusutanAlat) / Satu.BersihDataKeInt(aPesananPerbulan))* Satu.BersihDataKeInt(aPorsiSekarang);
            Label[13] = "= " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(HasilPenyusutan));
            
            /*
             * Biaya Sewa
             */
            double DSewa =  Satu.BersihDataKeDoubel(aBiayaSewa) ;
            xxxPemeliharaanGedung = "= ( " + ConvertAngka.NilaiRupiah(DSewa) + " / "+ConvertAngka.NilaiRupiah(aPesananPerbulan) + " ) x " + ConvertAngka.NilaiRupiah(aPorsiSekarang );            
            double Sewa2 = (Satu.BersihDataKeDoubel(ConvertAngka.RoundingDesimal(aBiayaSewa)) / Satu.BersihDataKeInt(aPesananPerbulan))* Satu.BersihDataKeInt(aPorsiSekarang);
            Label[14] ="= " +  ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(Sewa2));
            DSewa       =  Sewa2;
            
            /*
             * Biaya Alat Kantor
             */
            double DKantor =  Satu.BersihDataKeDoubel(aBiayaAlatKantor) ;
            xxxAlatKantor = "= ( " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(DKantor)) + " / "+ConvertAngka.NilaiRupiah(aPesananPerbulan) + " ) x " + ConvertAngka.NilaiRupiah(aPorsiSekarang );            
            double xx = (Satu.BersihDataKeDoubel(ConvertAngka.RoundingDesimal(aBiayaAlatKantor)) / Satu.BersihDataKeInt(aPesananPerbulan))* Satu.BersihDataKeInt(aPorsiSekarang);
            Label[15] ="= " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(xx));
            DKantor     = xx;
            /*
             * BIAYA PRODUKSI KESELURUHAN :
            BIAYA LANGSUNG ………………………… (food cost+biaya bahan bakar+biaya umum + biaya tenaga kerja)
            BIAYA TIDAK LANGSUNG ………………(biaya penyusutan alat+sewa dan pemeliharaan gedung+alat kantor)													+
                                            Ex : …………………. 		Rp.1.500.000,-
             */
            double TotalBBM         = HitBensin + HitLpg;
            double TotalBiayaUmum   = HasilListrik + HasilAir;
            double TotalTenagaKerja = HasilTenagaLangsung + HasilTidakTenagaLangsung;
            double TotalBiayaLangsung   = Satu.BersihDataKeDoubel(TotalBiaya) + TotalBBM + TotalBiayaUmum + TotalTenagaKerja;
            
            Label[17] = "= ( " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(TotalAkhirProduksi())) + " + " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(TotalBBM)) 
                    + " + " +  ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(TotalBiayaUmum)) 
                    + " + " +  ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(TotalTenagaKerja)) + " ) " ;
            Label[18] ="= "+ ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(TotalBiayaLangsung));
            
            /*
             * Biaya tdak langsung
             */
            Label[19] = "= ( " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(HasilPenyusutan)) 
                    + " + " +  ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(DSewa)) 
                    + " + " +  ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(DKantor)) + " ) " ;
            double TotalTdkBiayaLangsung   = HasilPenyusutan + DSewa + DKantor;
            Label[20 ] ="= "+ ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(TotalTdkBiayaLangsung));
            
            /*
             * Total
             */
            double TotalBiayaProduksi = TotalBiayaLangsung + TotalTdkBiayaLangsung;
            Label[21] ="= " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(TotalBiayaProduksi));
            
            /*
             * Laba yang diharapkan : 20%
                = 20% x BIAYA PRODUKSI KESELURUHAN
                = 20% x Rp. 1.500.000,-
                = Rp. 300.000
             */
            double LabaDiHarapkan = ( Satu.BersihDataKeDoubel(aLabaDiHarapkan) / 100 ) * TotalBiayaProduksi;
            Label[22] = "= " + ConvertAngka.NilaiRupiah(aLabaDiHarapkan) + " % x " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(TotalBiayaProduksi));
            Label[23] = "= " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(LabaDiHarapkan));
            
            /*
             * Harga Jual Per Porsi :
            = (Biaya Produksi keseluruhan + Laba yang diharapkan) /jumlah porsi 
            = (Rp.1.500.000+300.000)/200
            = 1.800.000 / 200
            = Rp. 9.000
             */          
            double HargaJualPerPorsi =(TotalBiayaProduksi +LabaDiHarapkan ) / Satu.BersihDataKeInt(aPorsiSekarang) ;
            Label[24] = "= ( " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(TotalBiayaProduksi)) + " + " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(LabaDiHarapkan) ) +
                    " ) / " + Satu.BersihDataKeInt(aPorsiSekarang) ;
            double PerOleh = TotalBiayaProduksi +LabaDiHarapkan;
            Label[25] = "= " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(PerOleh ))+ " / " + aPorsiSekarang ;
            Label[26] = "= " + ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(HargaJualPerPorsi));
    }
    
    private void Sementara(){
         String DataNoTransaksi      =    (String) JComboBoxFoodCost.getSelectedItem();
        reportparametermap1.put("JudulPorsi", Satu.FilterNamaResepSaja(DataNoTransaksi, JLabelBebanKet)); 
        //Food Cost
        reportparametermap1.put("TotalFoodCost", ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(TotalBiaya)));  
        //Total Food Cost
        reportparametermap1.put("TotalPro", ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(TotalAkhirProduksi() ))+ "");  
        reportparametermap1.put("JumlahPorsi", JLabelBebanPorsi.getText());  
        reportparametermap1.put("Kenaikan", aLabaDiHarapkan);  
        /*
         * Bahan Pemantu
         * aPesananPerbulan = PesananPerbulan.getText();
            aLPG = LPG.getText();
            aBBM =  BBM.getText();
            aBiayaListrik= BiayaListrik.getText();
            aBiayaAir= BiayaAir.getText();
            aTenagaKerjaLangsung = TenagaKerjaLangsung.getText();
            aGajiKaryawan= GajiKaryawan.getText();
            aPenyusutanAlat= PenyusutanAlat.getText();
            aBiayaSewa= BiayaSewa.getText();
            aBiayaAlatKantor= BiayaAlatKantor.getText();
            aJumlahTenagaKerjaLangsung = JumlahTenagaKerjaLangsung.getText();
            aLabaDiHarapkan = JTextFieldPersenKeuntungan.getText();
            aBahanPembantu = BahanPembantu.getText();
         */
        reportparametermap1.put("BiayaKemasan",  ConvertAngka.NilaiRupiah(ConvertAngka.RoundingDesimal(aBahanPembantu)));  
        reportparametermap1.put("HitLPG1", Label[1]);  
        reportparametermap1.put("HItLPG2", Label[2]);  
        reportparametermap1.put("Solar", Label[3]); 
        reportparametermap1.put("xxxSolar", xxxSolar );
        reportparametermap1.put("HitListrik1", Label[4]);  
        reportparametermap1.put("HitListrik2", Label[5]);  
        reportparametermap1.put("HitAir1", Label[6]);  
        reportparametermap1.put("HitAir2", Label[7]);  
        reportparametermap1.put("HitTenagaKerja1", Label[8]);  
        reportparametermap1.put("HitTenagaKerja2", Label[9]);  
        reportparametermap1.put("HitTenagaKerjaTidakLangsung1", Label[10]);  
        reportparametermap1.put("HitTenagaKerjaTidakLangsung2", Label[11]);  
        
        reportparametermap2.put("HitPenyusutan1", Label[12]);  
        reportparametermap2.put("HitPenyusutan2", Label[13]);
        reportparametermap2.put("HitSewa1", Label[14]);
        reportparametermap2.put("HitKantor1", Label[15]);  
        reportparametermap2.put("HitBiayaLangsung1", Label[17]);  
        reportparametermap2.put("HitBiayaLangsung2", Label[18]);  
        reportparametermap2.put("HitBiayaTidakLangsung1", Label[19]);  
        reportparametermap2.put("HitBiayaTidakLangsung2", Label[20]);  
        reportparametermap2.put("HitBiayaProduksi",Label[21]);  
        reportparametermap2.put("HitLaba1", Label[22]);  
        reportparametermap2.put("HitLaba2", Label[23]);  
        reportparametermap2.put("HitHarga1", Label[24]);  
        reportparametermap2.put("HitHarga2", Label[25]);  
        reportparametermap2.put("HitHarga3", Label[26]);  
        reportparametermap2.put("JudulPorsi",Satu. FilterNamaResepSaja(DataNoTransaksi, JLabelBebanKet)); 
        reportparametermap2.put("Kenaikan", aLabaDiHarapkan); 
        reportparametermap2.put("xxxBiayaSewa", xxxPemeliharaanGedung); 
        reportparametermap2.put("xxxBiayaAlatKantor", xxxAlatKantor); 
        
        //parametersMap.put("reportparametermap1", reportparametermap1);  
        //parametersMap.put("reportparametermap2", reportparametermap2);  
    }
    
      /*
      * Yang ke 4
      * Print Jasper 2 Halaman
      */     
    Map parametersMap = new HashMap();  
    HashMap reportparametermap1 = new HashMap();  
    HashMap reportparametermap2 = new HashMap();
      private Map fillReportParameters() {  
        ISIData();
        
        String DataNoTransaksi      =    (String) JComboBoxFoodCost.getSelectedItem();
        reportparametermap1.put("JudulPorsi", Satu.FilterNamaResepSaja(DataNoTransaksi, JLabelBebanKet)); 
        //Food Cost
        reportparametermap1.put("TotalFoodCost", TotalProduksi);  
        //Total Food Cost
        reportparametermap1.put("TotalPro", ConvertAngka.NilaiRupiah(TotalAkhirProduksi() )+ "");  
        reportparametermap1.put("JumlahPorsi", JLabelBebanPorsi.getText());  
        reportparametermap1.put("Kenaikan", aLabaDiHarapkan);  
        /*
         * Bahan Pemantu
         * aPesananPerbulan = PesananPerbulan.getText();
            aLPG = LPG.getText();
            aBBM =  BBM.getText();
            aBiayaListrik= BiayaListrik.getText();
            aBiayaAir= BiayaAir.getText();
            aTenagaKerjaLangsung = TenagaKerjaLangsung.getText();
            aGajiKaryawan= GajiKaryawan.getText();
            aPenyusutanAlat= PenyusutanAlat.getText();
            aBiayaSewa= BiayaSewa.getText();
            aBiayaAlatKantor= BiayaAlatKantor.getText();
            aJumlahTenagaKerjaLangsung = JumlahTenagaKerjaLangsung.getText();
            aLabaDiHarapkan = JTextFieldPersenKeuntungan.getText();
            aBahanPembantu = BahanPembantu.getText();
         */
        reportparametermap1.put("BiayaKemasan", aBahanPembantu);  
        reportparametermap1.put("HitLPG1", Label[1]);  
        reportparametermap1.put("HItLPG2", Label[2]);  
        reportparametermap1.put("Solar", Label[3]);  
        reportparametermap1.put("HitListrik1", Label[4]);  
        reportparametermap1.put("HitListrik2", Label[5]);  
        reportparametermap1.put("HitAir1", Label[6]);  
        reportparametermap1.put("HitAir2", Label[7]);  
        reportparametermap1.put("HitTenagaKerja1", Label[8]);  
        reportparametermap1.put("HitTenagaKerja12", Label[9]);  
        reportparametermap1.put("HitTenagaKerjaTidakLangsung1", Label[10]);  
        reportparametermap1.put("HitTenagaKerjaTidakLangsung2", Label[11]);  
        
        reportparametermap2.put("HitPenyusutan1", Label[12]);  
        reportparametermap2.put("HitPenyusutan2", Label[13]);
        reportparametermap2.put("HitSewa1", Label[14]);
        reportparametermap2.put("HitKantor1", Label[15]);  
        reportparametermap2.put("HitBiayaLangsung1", Label[17]);  
        reportparametermap2.put("HitBiayaLangsung2", Label[18]);  
        reportparametermap2.put("HitBiayaTidakLangsung1", Label[19]);  
        reportparametermap2.put("HitBiayaTidakLangsung2", Label[20]);  
        reportparametermap2.put("HitBiayaProduksi",Label[21]);  
        reportparametermap2.put("HitLaba1", Label[22]);  
        reportparametermap2.put("HitLaba2", Label[23]);  
        reportparametermap2.put("HitHarga1", Label[24]);  
        reportparametermap2.put("HitHarga2", Label[25]);  
        reportparametermap2.put("HitHarga3", Label[26]);  
        reportparametermap2.put("JudulPorsi", Satu.FilterNamaResepSaja(DataNoTransaksi, JLabelBebanKet));   
                  
        parametersMap.put("reportparametermap1", reportparametermap1);  
        parametersMap.put("reportparametermap2", reportparametermap2);  

  
        return parametersMap;  
    } 
      /*
       * Utama pemangil report
       */
       private JasperPrint getLinkedStaticpages() throws IOException, JRException {  
           
        Map page1param, page2param ;  
        
        page1param = (Map) fillReportParameters().get("reportparametermap1");  
        page2param = (Map) fillReportParameters().get("reportparametermap2");  
      
         /* 
        String x        = System.getProperty("user.dir")+"\\src\\Report\\ReportFormPelamar.jrxml";
        String x2       = System.getProperty("user.dir")+"\\src\\Report\\ReportFormPelamar2.jrxml";
        String x3       = System.getProperty("user.dir")+"\\src\\Report\\ReportFormPelamar3.jrxml";
        String x4       = System.getProperty("user.dir")+"\\src\\Report\\ReportFormPelamar4.jrxml";
        * 
        * String x    = System.getProperty("user.dir")+"\\Report\\ReportPelamar.jrxml";   
        */
        String x        = System.getProperty("user.dir")+"\\ReportJasper\\ReportLaporanPerhitunganBiaya3.jrxml";
        String x2       = System.getProperty("user.dir")+"\\ReportJasper\\ReportLaporanPerhitunganBiaya32.jrxml";
        
         //JOptionPane.showMessageDialog(null, x);
                    
        JasperPrint CetakKe1    = multipageLinking(fillJasperPrint(x  , page1param, false), fillJasperPrint(x2, page2param, false));  
         
        return CetakKe1;  
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
      private void viewer1(JasperPrint page1, ActionEvent E) {  
          JRViewer viewer = new JRViewer(page1); 
          //JasperViewer viewer = new JasperViewer(page1, false);
          

         
        //viewer.setVisible(true);  
    }
      
      /*
       * Yang ke dua
       */
       private JasperPrint fillJasperPrint(String jasperpath, Map parametersMap, boolean Apa)   
            throws IOException, JRException {  
           

           /*
            JasperPrint jasperprint = new JasperPrint();  
            jasperprint = JasperFillManager.fillReport(getClass().  
            getResource(jasperpath).openStream(), parametersMap);  
            */
            //

            JasperDesign jasperDesign = null;
            JasperPrint jasperPrint = null ;
            JasperReport jasperReport;

            jasperDesign    = JRXmlLoader.load(jasperpath);
            jasperReport    = JasperCompileManager.compileReport(jasperDesign);
            
            if(Apa == true){
                DefaultTableModel de = (DefaultTableModel)JTabelBiaya.getModel();

                JRTableModelDataSource dataSource = new JRTableModelDataSource(de);
                
                jasperPrint     = JasperFillManager.fillReport(jasperReport, parametersMap, dataSource);
            }
            else {
                jasperPrint     = JasperFillManager.fillReport(jasperReport, parametersMap, new    JREmptyDataSource());
            }
            
            

            return jasperPrint;  
    }  
    

    private void PrintJasper(ActionEvent E,HashMap hashMap){
        
        String DataNoTransaksi      =    (String) JComboBoxFoodCost.getSelectedItem();
        DefaultTableModel de = (DefaultTableModel)JTabelBiaya.getModel();

        JRTableModelDataSource dataSource = new JRTableModelDataSource(de);

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        
        //hashMap.put("JudulPorsi", FilterNamaResepSaja(DataNoTransaksi));
        
        try {

            String x    = System.getProperty("user.dir")+"\\ReportJasper\\ReportLaporanPerhitunganBiaya3.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, dataSource);
            //JasperViewer.viewReport(jasperPrint, false);
            
            JRViewer vw = new JRViewer(jasperPrint);

            BebanMetode3RepotPanel ccc = new BebanMetode3RepotPanel(vw);
                    
            SistemPro.TambahTab TabNewViewData = new SistemPro.TambahTab();
            TabNewViewData.createTabButtonActionPerformed(E, jTabbedPaneLihatProses,
                                    "Report A " + NoJTabbed, ccc,
                                    // ( Panjang, Tinggi )
                                    new Dimension(30, 20), new Dimension(2000, 2000));
            NoJTabbed++; 
            

        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }  
    }
    
    private void PrintJasper2(ActionEvent E,HashMap hashMap){
        
        String DataNoTransaksi      =    (String) JComboBoxFoodCost.getSelectedItem();
        DefaultTableModel de = (DefaultTableModel)JTabelBiaya.getModel();

        JRTableModelDataSource dataSource = new JRTableModelDataSource(de);

        JasperDesign jasperDesign = null;
        JasperPrint jasperPrint = null ;
        JasperReport jasperReport;
        
        //hashMap.put("JudulPorsi", FilterNamaResepSaja(DataNoTransaksi));
        
        try {

            String x    = System.getProperty("user.dir")+"\\ReportJasper\\ReportLaporanPerhitunganBiaya32.jrxml";                   
            jasperDesign = JRXmlLoader.load(x);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, new    JREmptyDataSource());
            //JasperViewer.viewReport(jasperPrint, false);
            
            JRViewer vw = new JRViewer(jasperPrint);

            BebanMetode3RepotPanel ccc = new BebanMetode3RepotPanel(vw);
                    
                    SistemPro.TambahTab TabNewViewData = new SistemPro.TambahTab();
            TabNewViewData.createTabButtonActionPerformed(E, jTabbedPaneLihatProses,
                                    "Report B " + NoJTabbed, ccc,
                                    // ( Panjang, Tinggi )
                                    new Dimension(30, 20), new Dimension(2000, 2000));
            NoJTabbed++; 
            

        } catch (JRException ee) {
            JOptionPane.showMessageDialog(null, ee);
          ee.printStackTrace();
       }  
    }
    
    
    
    private void ResetTampilan(){
        JTextField[] Data = {

            JTextFieldPersenKeuntungan, PesananPerbulan, LPG, BBM, BiayaListrik, BiayaAir, TenagaKerjaLangsung, GajiKaryawan, PenyusutanAlat,
            BiayaSewa,BiayaAlatKantor,BahanPembantu, JumlahTenagaKerjaLangsung
            };       
        JLabel[] DataLabel = {
          JLabelBebanKet, JLabelBebanPorsi

        };
        
        Satu.ResetTampilan(Data, DataLabel);
        Satu.HapusDataJTabel(JTabelBiaya);

    }
    
    private void ValidasiData(JTextField[] Data, String DataPesanError[]){
        if (Data.length == DataPesanError.length){
            for (int a = 0; a < Data.length; a++){
                if (Data[a].getText().equals("")){
                     JOptionPane.showMessageDialog(null, "Data kosong : " + DataPesanError[a] + " dirubah ke 0 ");
                     Data[a].setText("0");
                     continue;
                }
            }           
        }
        else {
            System.out.println("tidak sama data length");
            System.exit(1);
        }
    }

    
    int Porsi2 ;
    private void AmbilDataUntukPorsi(){
        String DataResep    =   (String) JComboBoxFoodCost.getSelectedItem();       
        int baris;
        String JumlahPorsiText = null, JumlahSatuan = null;
        
        
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               int JumlahHarga = 0;
               JLabelBebanPorsi.setText("");  
               if (DataResep.contains("-1")){
                   DataResep           = Satu.FilterTransNoSaja(DataResep, JLabelBebanKet);
                   
                   /*
                    * SELECT sum(b.qty) * a.porsi as total_porsi_pesanan FROM header_pesanan a 
                    * inner join detail_pesanan b on a.trans_no = b.trans_no 
                    * where a.nama_pemesan = 'bp ari'
                    *
                    //JOptionPane.showMessageDialog(null, "xxx " + DataResep);
                    DataResep = DataResep.replaceAll("(-1)$", "");
                    HQ = stm.executeQuery("SELECT sum(b.qty) * a.porsi as porsi_resep "
                            + "FROM header_pesanan a inner join detail_pesanan b on a.trans_no = b.trans_no "
                            + "where a.nama_pemesan = '"+ DataResep + "'");*/
                    HQ = stm.executeQuery("select porsi as porsi_resep , status_pesanan from header_penerimaan where trans_no = '"+ DataResep + "'");
                    JLabelBebanKet.setText("Data Dari Pesanan");
                     while(HQ.next()  ){
                        JumlahPorsiText         = HQ.getString("porsi_resep");        
                        JumlahSatuan            = HQ.getString("status_pesanan");  
                    }
                     Porsi2 = Integer.valueOf(JumlahPorsiText).intValue();
                     JLabelBebanPorsi.setText(JumlahPorsiText + " " + JumlahSatuan ); 
               }
               else {
                    DataResep           = Satu.FilterNamaResepSaja(DataResep, JLabelBebanKet);
                    HQ = stm.executeQuery("SELECT  porsi_resep, nama_resep from header_resep where nama_resep ='"+ DataResep + "'");
                    JLabelBebanKet.setText("Data Dari Resep");
                    while(HQ.next()  ){
                        JumlahPorsiText         = HQ.getString("porsi_resep");                           
                    }
                    JLabelBebanPorsi.setText(JumlahPorsiText + " Porsi"); 
                   
                    
               }
               
               if (DataResep.equals("null") ){
                   InputJikaTotalFoodCostNull();
               }
               else{
                    Porsi2 = Integer.valueOf(JumlahPorsiText).intValue();
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }    
    }
    
    
    private void InputJikaTotalFoodCostNull(){
        JTextField NamaResep    = new JTextField();
        JTextField JumlahPorsi = new JTextField();
        AntiHuruf.SetAntiAngkaLimit(JumlahPorsi,12);
        
        Object[] Object ={
        "Masukan Nama Resep Baru   : ", NamaResep,
        "Masukan Jumlah Porsi      : ", JumlahPorsi
        };

        int Pilih = JOptionPane.showConfirmDialog(null , Object , "Masukan Data ", JOptionPane.OK_CANCEL_OPTION);

        if (Pilih == JOptionPane.OK_OPTION){
            try {
                 //String NamaResep = JOptionPane.showInputDialog("Input Nama Resep :");
                String TitleResep = NamaResep.getText();
                String QtyPosrsi  = JumlahPorsi.getText();

                SistemPro.ValidasiInputResep ValidasiResep = new  ValidasiInputResep() ;
                ValidasiResep.SetValidasiInputResep2(TitleResep, QtyPosrsi);
                boolean BenarValidasiResep = ValidasiResep.GetValidasiInputResep2();

                if (BenarValidasiResep == false){
                    String a = "[/*\\-\\(\\)<>_\\=+\\.,:\";\'\\\\#\\$&\\^\\}\\{%~`\\|\\[\\]\\!\\?\\@a-zA-Z]";
                    QtyPosrsi = QtyPosrsi.replaceAll(a, "");
                     Porsi2 = Integer.valueOf(QtyPosrsi).intValue();
                    JLabelBebanPorsi.setText(QtyPosrsi + " Porsi");
                    JLabelBebanKet.setText("Data Manual");
                }
          }
          catch (Exception X){
              JOptionPane.showMessageDialog(null,  "form_purchase.java : error : 1227 : "  +X, " Error delete", JOptionPane.ERROR_MESSAGE);
              X.printStackTrace();
          } 
        }      
    }
    
    /*
     * Bagian Tabel
     */
     private void Tabel(String Porsi){
        
        String header[] = {"No", "Nama Bahan", "1 Porsi",Porsi , "Harga Satuan"};
        Modeltabel = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                        //if(colIndex == 9) {return true ;} 
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTabelBiaya.setModel(Modeltabel);
        
       /*
        * Membuat sort pada tabel
        * Search Data
        */     
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel);
        JTabelBiaya.setRowSorter(sorter);
        
        /*
         * Rata tengah atau kanan table
         */
        JTabelBiaya.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelBiaya.getColumnModel().getColumn(2).setCellRenderer( tengah ); 
        JTabelBiaya.getColumnModel().getColumn(1).setCellRenderer( tengah );
        JTabelBiaya.getColumnModel().getColumn(3).setCellRenderer( tengah );
        JTabelBiaya.getColumnModel().getColumn(4).setCellRenderer( kanan ); 
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,250,100,150, 150};
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTabelBiaya, jarak_colom);
        
        /*
         * Memasukan tombol ke jtable
        JTabelBiaya.getColumnModel().getColumn(9).setCellRenderer(  new ButtonJTable("Delete"));
        JTabelBiaya.getColumnModel().getColumn(9).setCellEditor( new  menu_cuti.ButtonJTableKeDuaCuti(new JCheckBox(),Modeltabel, JTabelBiaya));
       
        /*
         * Disable drag colum tabel
         */       
        JTabelBiaya.getTableHeader().setReorderingAllowed(false);       
    }
     
     private String FilterKeterangnSatuan(String Data){
        Data  = Data.replaceAll("\\(.*\\)", "");
        return Data;
    }
     public String TotalBiaya = "0", TotalProduksi      = "0";
     public void AmbilDataBahanResep(String NoTransaksi ){
          
      /*
       * Query 3 join untuk ambil data qty_1_porsi, satuan_1_porsi, nama bahan, dan trans_no
         SELECT detail_resep.qty_1_porsi, detail_resep.satuan_1_porsi, 
                header_penerimaan.trans_no, header_penerimaan.judul_resep, 
                detail_trans_penerimaan.trans_no, detail_trans_penerimaan.nama_bahan, detail_trans_penerimaan.qty, detail_trans_penerimaan.satuan
         FROM detail_trans_penerimaan RIGHT JOIN header_penerimaan 
                ON detail_trans_penerimaan.trans_no = header_penerimaan.trans_no
            LEFT JOIN detail_resep 
                ON header_penerimaan.judul_resep = detail_resep.namresep
         WHERE detail_trans_penerimaan.trans_no = 'WR2014011'
         AND detail_trans_penerimaan.nama_bahan = detail_resep.bahan
       */
        int baris;
        String JumlahPorsi2 ;
        String Qty1;
        String Qty;
        String Satuan1;
        String Satuan;
        String NamaBahan1;
        double TotalHrg1 ;
        double TotalHrg2 = 0.0 ;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               
               if (JLabelBebanKet.getText().equalsIgnoreCase("Data Dari Resep")) {

                    AA = "SELECT detail_resep.qty_1_porsi, detail_resep.satuan_1_porsi, " +
                    "header_penerimaan.trans_no, "
                    + "header_penerimaan.judul_resep, "
                    + "header_penerimaan.total_biaya, "
                    + "header_penerimaan.total_rill, " 
                    + "detail_trans_penerimaan.trans_no, "
                    + "detail_trans_penerimaan.nama_bahan, "
                    + "detail_trans_penerimaan.qty, "
                    + "detail_trans_penerimaan.satuan, " 
                    + "detail_trans_penerimaan.harga_rill "
                    + "FROM detail_trans_penerimaan RIGHT JOIN header_penerimaan " +
                    "ON detail_trans_penerimaan.trans_no = header_penerimaan.trans_no " +
                    "LEFT JOIN detail_resep " +
                    "ON header_penerimaan.judul_resep = detail_resep.namresep " +
                    "WHERE detail_trans_penerimaan.trans_no = '"+ NoTransaksi+ "'" +
                    " group by detail_trans_penerimaan.nama_bahan  ";
                  
               }
               else {
                   /*
                    * Cara ambil resep dari pesanan
                    * header_penerimaan.judul_resep = header_pesanan.nama_pemesan
                    * header_pesanan.trans_no = detail_pesanan.trans_no
                    * detail_pesanan.nama_menu = detail_resep.namresep
                    * 
                    * SET @v1 := (SELECT COUNT(*) FROM user_rating);
                      SELECT @v1;
                      
                      SET @detailpesanan := (select nama_pemesan, trans_no from header_pesanan where nama_pemesan ='');
                      select @detailpesanan;
                      * 
                      select * from header_penerimaan hp inner join header_pesanan he on hp.judul_resep = he.nama_pemesan
                      inner join detail_pesanan dp on he.trans_no = dp.trans_no where hp.judul_resep = '' )
                    */ 
                   
                   AA = "select hp.total_biaya, dp.qty, dp.satuan, hp.total_rill, dp.harga_rill, dp.nama_bahan "
                           + " from header_penerimaan hp inner join detail_trans_penerimaan dp "
                           + "on hp.trans_no = dp.trans_no "
                           + "where hp.trans_no ='"+ NoTransaksi+ "'";
                   AAA = true;
               }
               HQ = stm.executeQuery(AA );                                
               baris = HQ.getRow();                                          
               while(HQ.next()  ){
                   if (AAA){
                        Qty             = HQ.getString("qty");
                        Qty1            = "";
                        Satuan1         = "";
                        Satuan          = HQ.getString("satuan");
                   }
                   else {
                        Qty             = HQ.getString("qty");
                        Qty1            = HQ.getString("qty_1_porsi");
                        Satuan1         = HQ.getString("satuan_1_porsi");
                        Satuan          = HQ.getString("satuan");                      
                   }           
                    TotalHrg1       = HQ.getDouble("harga_rill");
                    NamaBahan1      = HQ.getString("nama_bahan");               
                    Satuan          = FilterKeterangnSatuan(Satuan);
                    Satuan1         = FilterKeterangnSatuan(Satuan1);
                    this.TotalBiaya  = HQ.getString("total_rill");
                    System.out.println(TotalBiaya + " xxxxxxxxxxxxxxxxxxxxxx");
                   
                    /*
                     * Hitung Total Produksi
                     * "No", "Nama Bahan", "Jumlah","Porsi", "Harga Satuan"
                     */
                    TotalHrg2   = TotalHrg2 + TotalHrg1 ;                    
                    String[] add         = {String.valueOf(HQ.getRow()).toString(), NamaBahan1, Qty1 + " " + Satuan1, ConvertAngka.NilaiRupiah(Qty) +" " + Satuan, ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(TotalHrg1)) + "" };
                    
                    Modeltabel.addRow(add);
               }
               this.TotalProduksi    =   TotalHrg2+"";
           }
           catch (Exception ex){
                System.out.println("Error (4466) "+ ex);
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
           }    
           JTabelBiaya.setModel(Modeltabel);
  }

     
     public double  TotalAkhirProduksi(){
         double TotalAkhirProduksi   =   HitunganTotalProduksi(TotalBiaya,BahanPembantu.getText(), "0");
         return TotalAkhirProduksi;
     }
     
     /*
      * Total food cost
      */
      public double HitunganTotalProduksi(String TotalProduksi, String BiayaLainnya, String BiayaKemasan){
        if (BiayaLainnya.equals("") ){
            BiayaLainnya = "0";
        }
        if (BiayaKemasan.equals("")){
            BiayaKemasan = "0";
        } 
        if (TotalProduksi.equals("")){
            TotalProduksi = "0";
        }  
        return  Double.valueOf(TotalProduksi).doubleValue() + Double.valueOf(BiayaLainnya).doubleValue() + Double.valueOf(BiayaKemasan).doubleValue();
    }

    

    
    private double FormatDesimal (double Data){
        DecimalFormat numberFormat = new DecimalFormat("##.00"); //#.##
        String Data2;
        Data2             = numberFormat.format(Data);
        if (Data2.contains(",")){
             return Double.valueOf(Data2.replaceAll(",", ".")).doubleValue();
        }       
       return Double.valueOf(Data2).doubleValue();
    } 
    
    private String FormatDesimalString(double Data){
        DecimalFormat numberFormat  = new DecimalFormat("#.00"); //#.##
        String Data2                = numberFormat.format(Data);
         //int decimalPlace = 2;
        //BigDecimal bd = new BigDecimal(Data);
        //bd = bd.setScale(decimalPlace,BigDecimal.ROUND_UP);
        if (Data2.contains(",")){
            
            Data2   = Data2.replaceAll(",", ".");
            return Data2;
        }       
       return Data2;    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPaneLihatProses = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        JTextFieldPersenKeuntungan = new javax.swing.JTextField();
        JComboBoxFoodCost = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        JLabelBebanKet = new javax.swing.JLabel();
        JLabelBebanPorsi = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelBiaya = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        LPG = new javax.swing.JTextField();
        BBM = new javax.swing.JTextField();
        BiayaListrik = new javax.swing.JTextField();
        BiayaAir = new javax.swing.JTextField();
        TenagaKerjaLangsung = new javax.swing.JTextField();
        GajiKaryawan = new javax.swing.JTextField();
        PenyusutanAlat = new javax.swing.JTextField();
        BiayaSewa = new javax.swing.JTextField();
        BiayaAlatKantor = new javax.swing.JTextField();
        JButtonProses1 = new javax.swing.JButton();
        JumlahTenagaKerjaLangsung = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        BahanPembantu = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        PesananPerbulan = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Pilih Total Food Cost");

        jLabel2.setText("Laba di harapkan");

        JTextFieldPersenKeuntungan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JTextFieldPersenKeuntungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTextFieldPersenKeuntunganActionPerformed(evt);
            }
        });

        JComboBoxFoodCost.setEditable(true);
        JComboBoxFoodCost.setModel(new javax.swing.DefaultComboBoxModel( app_search_data_beban.getData().toArray()));

        jLabel3.setText("%");

        JLabelBebanKet.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        JLabelBebanKet.setForeground(new java.awt.Color(255, 51, 51));

        JLabelBebanPorsi.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        JLabelBebanPorsi.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(51, 51, 255));
        jLabel54.setText("Rencana Kerja Metode Profesional");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel54)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JLabelBebanKet, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JLabelBebanPorsi, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JComboBoxFoodCost, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTextFieldPersenKeuntungan, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(8, 8, 8))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JLabelBebanKet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JComboBoxFoodCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JTextFieldPersenKeuntungan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JLabelBebanPorsi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                .addContainerGap())
        );

        JTabelBiaya.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTabelBiaya.setToolTipText("");
        jScrollPane1.setViewportView(JTabelBiaya);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Biaya-Biaya Per Satu Bulan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, java.awt.Color.blue));

        jLabel7.setText("Bahan Bakar");

        jLabel8.setText("A. LPG");

        jLabel9.setText("B. BBM");

        jLabel10.setText("Listrik");

        jLabel11.setText("Air ");

        jLabel12.setText("Tenaga Kerja Langsung ");

        jLabel13.setText("Gaji Karyawan ");

        jLabel21.setText("Penyusutan Alat ");

        jLabel24.setText("Sewa dan Pemeliharaan Gedung ");

        jLabel28.setText("Alat Kantor ");

        jLabel29.setText("Rp.");

        jLabel30.setText("Rp.");

        jLabel31.setText("Rp.");

        jLabel38.setText("Rp.");

        jLabel48.setText("Rp.");

        jLabel49.setText("Rp.");

        jLabel50.setText("Rp.");

        jLabel51.setText("Rp.");

        jLabel52.setText("Rp.");

        LPG.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        BBM.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        BiayaListrik.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        BiayaAir.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        TenagaKerjaLangsung.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TenagaKerjaLangsung.setToolTipText("Upah Karyawan");

        GajiKaryawan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        PenyusutanAlat.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        BiayaSewa.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        BiayaAlatKantor.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        JButtonProses1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Load.png"))); // NOI18N
        JButtonProses1.setText("Proses");

        JumlahTenagaKerjaLangsung.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        JumlahTenagaKerjaLangsung.setToolTipText("Jumlah Karyawan");

        jLabel32.setText("Bahan Pembantu");

        BahanPembantu.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel33.setText("Rp.");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Refresh.png"))); // NOI18N
        jButton1.setText("Reset");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel51))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel52))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel50))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel49))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel38))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel48))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel31))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel29))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel28)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(14, 14, 14)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(TenagaKerjaLangsung, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JumlahTenagaKerjaLangsung, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(LPG)
                            .addComponent(BBM)
                            .addComponent(BiayaListrik)
                            .addComponent(BiayaAir)
                            .addComponent(GajiKaryawan)
                            .addComponent(PenyusutanAlat)
                            .addComponent(BiayaSewa)
                            .addComponent(BiayaAlatKantor)
                            .addComponent(BahanPembantu, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(JButtonProses1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LPG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BBM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BiayaListrik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BiayaAir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TenagaKerjaLangsung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JumlahTenagaKerjaLangsung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(GajiKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PenyusutanAlat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BiayaSewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BiayaAlatKantor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BahanPembantu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(JButtonProses1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setText("Jumlah Produksi");

        PesananPerbulan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel5.setText("Porsi");

        jLabel6.setText(":");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PesananPerbulan, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 2, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PesananPerbulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1081, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 434, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPaneLihatProses.addTab("Rencana Kerja Metode Profesional", jPanel6);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneLihatProses, javax.swing.GroupLayout.DEFAULT_SIZE, 1076, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneLihatProses, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void JTextFieldPersenKeuntunganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTextFieldPersenKeuntunganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTextFieldPersenKeuntunganActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BBM;
    private javax.swing.JTextField BahanPembantu;
    private javax.swing.JTextField BiayaAir;
    private javax.swing.JTextField BiayaAlatKantor;
    private javax.swing.JTextField BiayaListrik;
    private javax.swing.JTextField BiayaSewa;
    private javax.swing.JTextField GajiKaryawan;
    private javax.swing.JButton JButtonProses1;
    private javax.swing.JComboBox JComboBoxFoodCost;
    private javax.swing.JLabel JLabelBebanKet;
    private javax.swing.JLabel JLabelBebanPorsi;
    public javax.swing.JTable JTabelBiaya;
    private javax.swing.JTextField JTextFieldPersenKeuntungan;
    private javax.swing.JTextField JumlahTenagaKerjaLangsung;
    private javax.swing.JTextField LPG;
    private javax.swing.JTextField PenyusutanAlat;
    private javax.swing.JTextField PesananPerbulan;
    private javax.swing.JTextField TenagaKerjaLangsung;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPaneLihatProses;
    // End of variables declaration//GEN-END:variables
}
