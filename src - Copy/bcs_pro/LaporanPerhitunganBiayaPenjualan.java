/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.PrintUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author LANTAI3
 */
public final class LaporanPerhitunganBiayaPenjualan extends  JPanel {
    
    private int fontSize = 90;
    private int messageWidth;
    private String NamaPerusahaan = "PT BCS_PRO";
    private String CreateDate ;
    private String Judul ="Perencaaan Laba Rugi";

    /*
     * Tabel
     */ 
    public String NoTransaksi;
    public String HargaJualPerPorsi;
    public String TotalFoodCost;
    public String JumlahPorsi;
    public String JudulResep;
    public String SatuanResep;
    public String HargaSatuan;
    public String HargaTotalSatuan;
    public String NamaBahan;
    public String TotalProduksi;
    public String BiayaKemasan;
    public String BiayaLain;   
    public String BebanLainnya;
    public String BebanPenyusutan ;       
    public String BebanTenagaKerja;       
    public String KenaikanYangDiHarapkan;

    private SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    private Connection K = KD.createConnection();

    private JButton printButton       = new JButton("Print");
    //private JButton SaveToGambar      = new JButton("Save");

    public boolean VorH;
    PrintView Ver;
    
    /*
     * Ukuran Kertas
     */
    public LaporanPerhitunganBiayaPenjualan() {
         
          SistemPro.TanggalSistem  Tgl = new SistemPro.TanggalSistem();
          setBackground(Color.white);
          this.CreateDate = Tgl.GetTglNow() + "-" +Tgl.GetBlnNow() + "-" +Tgl.GetThnNow() ;

          /*
           * Ukuran Kertas
           */
          int width = 50;
          int height = 200;
          setPreferredSize(new Dimension(width, height));
          AksiAksi();
          add(printButton);
//          add(SaveToGambar);
         //JOptionPane.showMessageDialog (null, "Error (3) sssssssssss");
          //Graphics g = null;
          //paintComponent(g);
     }
      @Override
    public void paintComponent(Graphics g) {       
       super.paintComponent(g);
       final Graphics2D g2d = (Graphics2D)g;
       //JOptionPane.showMessageDialog (null, "Error (7) sssssssssss");
       g2d.setFont(new Font("TimesRoma", Font.BOLD, 14));

       /*
        * Ukuran Huruf dan posisi huharus di mulai dimana
        */
       int x = messageWidth/10;
       int y = fontSize*5/2;
       g2d.translate(10,10);
       printButton.setBounds(1200,0,80,30);
//       SaveToGambar.setBounds(1300,0,80,30);
      // JOptionPane.showMessageDialog (null, "Error (45) sssssssssss");
       if (VorH){
          // JOptionPane.showMessageDialog (null, "Error (41) sssssssssss");
           Ver = new PrintView();
           Ver.VorH = VorH;
           PindahDataKeViewVertikal(Ver);
           // JOptionPane.showMessageDialog (null, "Error (4) sssssssssss");
           Ver.VertikalView(g2d);
           // JOptionPane.showMessageDialog (null, "Error (5) sssssssssss");
           //Ver.Vertikal(g2d);
       }
       else{
           PrintView Hor = new PrintView();
           Hor.VorH = VorH;
           Hor.Horizontal(g2d);
       } 
    } 
      
      private void SavToImage(Graphics2D DataG){
          int Tinggi    = 300;
          int Panjang   = 200;
          
          /*
           * Save ke Gambar
           */
          try {
              
              String   UrlAccesPath    =  String.valueOf(getClass().getResource("\\"+ JudulResep+".PNG")).toString();
              BufferedImage bi      = new BufferedImage(Panjang, Tinggi, BufferedImage.TYPE_INT_ARGB);
              DataG = bi.createGraphics();
              ImageIO.write(bi, "JPEG", new File(UrlAccesPath));
          }
          catch(Exception X){
              System.out.println("Error Laporan Rencana Kerja" + X);
              JOptionPane.showMessageDialog(null, "Error Laporan Rencana Kerja" + X);
          }
      }
    
    private void PindahDataKePrintVertikal(PrintFrame Ver){
        Ver.JudulResep              = JudulResep;
        Ver.JumlahPorsi             = JumlahPorsi;
        Ver.BiayaKemasan            = BiayaKemasan;
        Ver.BiayaLain               = BiayaLain;   
        Ver.BebanLainnya            = BebanLainnya;
        Ver.BebanPenyusutan         = BebanPenyusutan;       
        Ver.BebanTenagaKerja        = BebanTenagaKerja;       
        Ver.KenaikanYangDiHarapkan  = KenaikanYangDiHarapkan;
        Ver.NoTransaksi             = NoTransaksi;
    }

    private void AksiAksi(){

        printButton.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent E){
                 PrintFrame xx = new PrintFrame();
                 PindahDataKePrintVertikal(xx);
                 xx.Print2(VorH);
             }
         });
/*        SaveToGambar.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent E){
                 
             }
         });
 */       
    }

    /*
    private void AmbilDataBahanResep(final Graphics2D g2d, int AwalKiriBahan, 
            int AwalKiriQty1, int AwalKiriQty, int AwalKiriTotHarga, int JarakDariAtas){

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
         
          int baris;
          String JumlahPorsi2;
          String Qty1;
          String Qty;
          String Satuan1;
          String Satuan;
          String NamaBahan1;
          String TotalHrg;

          ResultSet HQ = null;
             try {
                 Statement stm = K.createStatement();
                 HQ = stm.executeQuery(
                      "SELECT detail_resep.qty_1_porsi, detail_resep.satuan_1_porsi, " +
                      "header_penerimaan.trans_no, header_penerimaan.judul_resep, " +
                      "detail_trans_penerimaan.trans_no, detail_trans_penerimaan.nama_bahan, "+
                      "detail_trans_penerimaan.qty, detail_trans_penerimaan.satuan " +
                      "detail_trans_penerimaan.harga_tot"+
                      "FROM detail_trans_penerimaan RIGHT JOIN header_penerimaan " +
                      "ON detail_trans_penerimaan.trans_no = header_penerimaan.trans_no " +
                      "LEFT JOIN detail_resep " +
                      "ON header_penerimaan.judul_resep = detail_resep.namresep" +
                      "WHERE detail_trans_penerimaan.trans_no = '"+ NoTransaksi+ "'" +
                      "AND detail_trans_penerimaan.nama_bahan = detail_resep.bahan");                   

                 baris = HQ.getRow();

                 int JumlahHarga = 0;
                 while(HQ.next()  ){

                      Qty             = HQ.getString("qty");
                      Qty1            = HQ.getString("qty_1_porsi");
                      Satuan1         = HQ.getString("satuan_1_porsi");
                      Satuan          = HQ.getString("satuan");
                      TotalHrg        = HQ.getString("harga_tot");
                      NamaBahan1      = HQ.getString("nama_bahan");

                      g2d.drawString(NamaBahan1 ,  AwalKiriBahan, JarakDariAtas );
                      g2d.drawString(Qty1 + " " + Satuan1 ,  AwalKiriQty1, JarakDariAtas );
                      g2d.drawString(Qty + " "+ Satuan ,  AwalKiriQty, JarakDariAtas );
                      g2d.drawString(TotalHrg ,  AwalKiriTotHarga, JarakDariAtas );

                 }             
             }
             catch (Exception ex){
                  JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                  ex.printStackTrace();
             }    
    }
*/
    private void PindahDataKeViewVertikal(PrintView Ver){
        Ver.JudulResep              = JudulResep;
        Ver.JumlahPorsi             = JumlahPorsi;
        Ver.BiayaKemasan            = BiayaKemasan;
        Ver.BiayaLain               = BiayaLain;   
        Ver.BebanLainnya            = BebanLainnya;
        Ver.BebanPenyusutan         = BebanPenyusutan;       
        Ver.BebanTenagaKerja        = BebanTenagaKerja;       
        Ver.KenaikanYangDiHarapkan  = KenaikanYangDiHarapkan;
        Ver.NoTransaksi             = NoTransaksi;
    }
       
  
  }

  class PrintFrame extends JFrame{
      
      String JudulResep;
      String JumlahPorsi;
      String BiayaKemasan;
      String BiayaLain;   
      String BebanLainnya;
      String BebanPenyusutan;       
      String BebanTenagaKerja;       
      String KenaikanYangDiHarapkan;
      String NoTransaksi;
        
      public PrintFrame(){
      }
      
      public void Print2(boolean VorH){
          PrintView PrintDiPrint = new PrintView();
          Container content = getContentPane();
          
          PindahDataKeVertikal(PrintDiPrint);
          
          PrintDiPrint.VorH = VorH;
          content.add(PrintDiPrint, BorderLayout.NORTH);
          pack();
          PrintUtilities.printComponent(PrintDiPrint);
      }
      
      private void PindahDataKeVertikal(PrintView Ver){
        Ver.JudulResep              = JudulResep;
        Ver.JumlahPorsi             = JumlahPorsi;
        Ver.BiayaKemasan            = BiayaKemasan;
        Ver.BiayaLain               = BiayaLain;   
        Ver.BebanLainnya            = BebanLainnya;
        Ver.BebanPenyusutan         = BebanPenyusutan;       
        Ver.BebanTenagaKerja        = BebanTenagaKerja;       
        Ver.KenaikanYangDiHarapkan  = KenaikanYangDiHarapkan;
        Ver.NoTransaksi             = NoTransaksi;
    }
       
  }

  //class AmbilDataNamaBahan
  class PrintView extends JPanel{
    private int fontSize = 90;
    private int messageWidth;
    private String NamaPerusahaan = "PT BCS_PRO";
    private String CreateDate ;
    private String Judul ="Rencan Kerja";
    private String TransNo ;

    /*
     * Tabel
     */ 
    public String HargaJualPerPorsi;
    public String TotalFoodCost;
    public String JumlahPorsi;
    public String JudulResep;
    public String SatuanResep;
    public String HargaSatuan;
    public String HargaTotalSatuan;
    public String NamaBahan;
    public String TotalProduksi;
    public String BiayaKemasan;
    public String BiayaLain;   
    public String BebanLainnya;
    public String BebanPenyusutan ;       
    public String BebanTenagaKerja;       
    public String KenaikanYangDiHarapkan;
    public String NoTransaksi;


    private SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
    private Connection K = KD.createConnection();

    public boolean VorH;

    public PrintView(){
          SistemPro.TanggalSistem  Tgl = new SistemPro.TanggalSistem();
          setBackground(Color.white);
          this.CreateDate = Tgl.GetTglNow() + "-" +Tgl.GetBlnNow() + "-" +Tgl.GetThnNow() ;

          /*
           * Ukuran Kertas
           */
          int width = 50;
          int height = 200;
          setPreferredSize(new Dimension(width, height));
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D)g;
        g2d.setFont(new Font("TimesRoma", Font.BOLD, 14));

        /*
         * Ukuran Huruf dan posisi huharus di mulai dimana
         */
        int x = messageWidth/10;
        int y = fontSize*5/2;
        g2d.translate(10,10);

        /*
         * Selalu print vertikal terus
         */
        if (true){
            Vertikal(g2d);
        }
        else{
            Horizontal(g2d);
        }
  }
    
    private int HitunganTotalProduksi(String TotalProduksi, String BiayaLainnya, String BiayaKemasan){
        if (BiayaLainnya.equals("") ){
            BiayaLainnya = "0";
        }
        if (BiayaKemasan.equals("")){
            BiayaKemasan = "0";
        } 
        if (TotalProduksi.equals("")){
            TotalProduksi = "0";
        }  
        return  Integer.valueOf(TotalProduksi).intValue() - Integer.valueOf(BiayaLainnya).intValue() -Integer.valueOf(BiayaKemasan).intValue();
    }
    
    double KenaikanYangDiHarapkanInt;
    double HasilSementaraHrgJual;
    
    private double HargaJualPerPorsi(int TotalAkhirProduksi, String KenaikanYangDiHarapakan1, String JumlahPorsi){

        if (KenaikanYangDiHarapakan1.equals("")){
            KenaikanYangDiHarapakan1 = "0";
        } 
        if (JumlahPorsi.equals("")){
            JumlahPorsi = "0";
        } 
        
        double KenaikanYangDiHarapakan  = Integer.valueOf(KenaikanYangDiHarapakan1).intValue() ;
        double IntPorsi                 = Integer.valueOf(JumlahPorsi).intValue() ;
              
        /*
        System.out.println(TotalAkhirProduksi + " + "+ KenaikanYangDiHarapakan/100 + " * "+  TotalAkhirProduksi + " = "+
                TotalAkhirProduksi + ( (KenaikanYangDiHarapakan/100 ) * TotalAkhirProduksi));
         */
        this.HasilSementaraHrgJual          =   (KenaikanYangDiHarapakan/100) * TotalAkhirProduksi;
        this.KenaikanYangDiHarapkanInt      =  HasilSementaraHrgJual + TotalAkhirProduksi;
        return   (KenaikanYangDiHarapkanInt  / IntPorsi);
    }
    
    private double LabaKotorDanLabaBersih(double TotalPendapatan , double TotalAkhirProduksi){
        return  (TotalPendapatan - TotalAkhirProduksi );
    }
    
    /*
     * Beban Beban all
     */
    private double BebanAll (String BebanPesen, double LabaKotor){
        return  (Double.valueOf(BebanPesen).doubleValue()/100 )* LabaKotor;
    }
    
    private double TotalBeban (double BebanSemua[]){
        double TotalBeban = 0;
        for (int a = 0; a < BebanSemua.length; a++){
            TotalBeban += BebanSemua[a];
        }
        return TotalBeban;
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
    
    int BarisDinamikJarakDariAtas;
    int AwalPosisiRow = 0;
    private void AmbilDataBahanResep(final Graphics2D g2d, String NoTransaksi ,int AwalKiriBahan, 
          int AwalKiriQty1, int AwalKiriQty, int AwalKiriTotHarga, int JarakDariAtas,
          int AwalRowPosisi,int JarakAntarRow, int AkhirRowPosisi, int JarakAntarRow2, int PosisiAngkaDariKiri){
          
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
        String JumlahPorsi2;
        String Qty1;
        String Qty;
        String Satuan1;
        String Satuan;
        String NamaBahan1;
        int TotalHrg1    = 0;
        int TotalHrg2   = 0;

        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery(
                    "SELECT detail_resep.qty_1_porsi, detail_resep.satuan_1_porsi, " +
                    "header_penerimaan.trans_no, header_penerimaan.judul_resep, " +
                    "detail_trans_penerimaan.trans_no, detail_trans_penerimaan.nama_bahan, "+
                    "detail_trans_penerimaan.qty, detail_trans_penerimaan.satuan, " +
                    "detail_trans_penerimaan.harga_tot "+
                    "FROM detail_trans_penerimaan RIGHT JOIN header_penerimaan " +
                    "ON detail_trans_penerimaan.trans_no = header_penerimaan.trans_no " +
                    "LEFT JOIN detail_resep " +
                    "ON header_penerimaan.judul_resep = detail_resep.namresep " +
                    "WHERE detail_trans_penerimaan.trans_no = '"+ NoTransaksi+ "'" +
                    "AND detail_trans_penerimaan.nama_bahan = detail_resep.bahan ");                   
               
               baris = HQ.getRow();                                          
               while(HQ.next()  ){
                   
                    Qty             = HQ.getString("qty");
                    Qty1            = HQ.getString("qty_1_porsi");
                    Satuan1         = HQ.getString("satuan_1_porsi");
                    Satuan          = HQ.getString("satuan");
                    TotalHrg1       = HQ.getInt("harga_tot");
                    NamaBahan1      = HQ.getString("nama_bahan");
                    
                    Satuan          = FilterKeterangnSatuan(Satuan);
                    Satuan1         = FilterKeterangnSatuan(Satuan1);
                    
                    int total_width = AwalKiriBahan + PosisiAngkaDariKiri; //img.getWidth();
                    int padding = 100;
                    int y =  JarakDariAtas;
                    String [] words = new String[]{""+TotalHrg1};
                    for(int i = 0; i < words.length; i++){
                         int actual_width = g2d.getFontMetrics().stringWidth(words[i]);
                         int x = total_width - actual_width - padding;
                         g2d.drawString(words[i], x, y += JarakAntarRow2);
                     }
                            
                    JarakDariAtas += JarakAntarRow2;                   
                    g2d.drawString(NamaBahan1 ,  AwalKiriBahan, JarakDariAtas );
                    g2d.drawString(Qty1 + " " + Satuan1 ,  AwalKiriQty1, JarakDariAtas );
                    g2d.drawString(Qty + " "+ Satuan ,  AwalKiriQty, JarakDariAtas );
                    //g2d.drawString(TotalHrg1+"" ,  AwalKiriTotHarga, JarakDariAtas );
                    g2d.drawLine(AwalRowPosisi, JarakDariAtas += JarakAntarRow, AkhirRowPosisi,JarakDariAtas += JarakAntarRow - JarakAntarRow);
                    
                    /*
                     * Hitung Total Produksi
                     */
                    TotalHrg2   = TotalHrg2 + TotalHrg1 ;
               }
               TotalProduksi    =   TotalHrg2+"";
               this.BarisDinamikJarakDariAtas = JarakDariAtas;
           }
           catch (Exception ex){
                System.out.println("Error (4) "+ ex);
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
           }    
  }
    /*
    private int HitungTotalProduksi(String Data){
       int DataInt;
       if (Data.equals("") ||Data.equals("null") ){
           Data    = "0";
           DataInt = Integer.valueOf(Data).intValue();
       }
       DataInt = Integer.valueOf(Data).intValue();

    }
    */
    private String FilterKeterangnSatuan(String Data){
        Data  = Data.replaceAll("\\(.*\\)", "");
        return Data;
    }
    
    public void VertikalView(final Graphics2D g2d){
        // JOptionPane.showMessageDialog (null, "Error (6) sssssssssss");
       /*
        * Setup posisi Table dan Data
        */
       int UkuranHuruf    = 12;
       int SettingAntarRow    = 20;
       int JarakAntarRow = SettingAntarRow;
       int AwalPosisiRow =40; // Jarak seluruh data dari atas
       int AwalPosisiRowGarisPinggir = AwalPosisiRow;
       int SettingTulisanJarakDenganBaris = 1;
       //int AwalPosisiAtasHPP   = 
       int AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;
       int JumlahBaris ;
       int AwalRowPosisi =350 ; // Posisi Geser Semua Data ke kanan // 250 Ukuran vie
       int AkhirRowPosisi  = 500 + AwalRowPosisi;

      /*
       * Isi data ke Tabel dari database
       */ 
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf + 4));
       g2d.drawString(NamaPerusahaan, AwalRowPosisi, 15);
       g2d.drawString(CreateDate, AwalRowPosisi, 30);
       g2d.drawString(Judul, AwalRowPosisi, 45);
       //g2d.drawString("Trans No :", 300, 45);
       //g2d.drawString(TransNo, 470, 45);
       int TinggiJudulColum = 80;
       g2d.drawString("", 7, TinggiJudulColum);

       int JarakDariAtas   = AwalPosisiRow += JarakAntarRow;

       /*
        * Judul judul Table
        */

       /*
        * Setting
        * Awal Posisi Vertikal ini harus ditambah
        * Horisontal harus hilang
        * Jika dirubah AwalRowPosisi = AwalRowPosisi+ 20; maka ini jg dirubah  AkhirRowPosisi = AkhirRowPosisi + 20;
        * AwalRowPosisi = AwalRowPosisi - 20; dan AkhirRowPosisi = AkhirRowPosisi + 20; harus sama
        */
       //AwalRowPosisi = AwalRowPosisi+ 40; 

       /*honeyf.16@gmail.com passw 08071992
        * Garis paling atas
        * Awal Posisi Vertikal ini harus ditambah
        * Horisontal harus hilang
        */
       AkhirRowPosisi = AkhirRowPosisi + 200;
       g2d.drawLine(AwalRowPosisi,  AwalPosisiRow , AkhirRowPosisi,AwalPosisiRow += AwalPosisiRow - AwalPosisiRow);

       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf + 2));
       g2d.drawString(JudulResep + "", AwalRowPosisi + 150, JarakDariAtas += JarakAntarRow - SettingTulisanJarakDenganBaris );
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       
       g2d.drawString("@Porsi", AwalRowPosisi + 25, JarakDariAtas += JarakAntarRow- SettingTulisanJarakDenganBaris);
       g2d.drawString("Harga Satuan", AwalRowPosisi + 610, JarakDariAtas += JarakAntarRow - JarakAntarRow - SettingTulisanJarakDenganBaris);

       g2d.drawString("Nama Bahan", AwalRowPosisi + 25, JarakDariAtas += JarakAntarRow  );
       g2d.drawString("Jumlah", AwalRowPosisi + 420, JarakDariAtas += JarakAntarRow - JarakAntarRow - SettingTulisanJarakDenganBaris );

       g2d.drawString(JumlahPorsi +" Porsi", AwalRowPosisi + 520, JarakDariAtas += ( JarakAntarRow - (JarakAntarRow * 2)) + SettingTulisanJarakDenganBaris );



       g2d.drawLine(AwalRowPosisi, AwalPosisiRow += JarakAntarRow, AkhirRowPosisi,AwalPosisiRow += JarakAntarRow - JarakAntarRow);

       /*
        * Header Tabel
        */
       JarakAntarRow = SettingAntarRow;
       AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;

       int GarisRowPisah   = AwalPosisiRow += JarakAntarRow;

       /*
        * Garis Horisontal
        * Pembuatan table
        * Setting
        */
       JarakAntarRow = SettingAntarRow;
       AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;
       JumlahBaris   = 10;

       int JarakAntarColumn = 100;
       int JumlahColum      = 6;
       int GarisDetailAwal = 0;
       int GarisDetailAkhir    = 0;
       int JarakDariAtasDatabase = 0;
       int AwalPosisiDatabase = 0;

       int AwalRowPosisi1 = 0;
       int JarakAntarRow1 = 1;       
       int AkhirRowPosisi1 = 0;

       for (int a = 0 ; a <= JumlahBaris; a++ ){

           /*
            * Data
            */
           if ((a < 1) ){
                g2d.drawLine(AwalRowPosisi, AwalPosisiRow += JarakAntarRow, AkhirRowPosisi,AwalPosisiRow += JarakAntarRow - JarakAntarRow);
                //AwalPosisiRow += JarakAntarRow;
               //g2d.drawString("Nama Bahan x", AwalRowPosisi + JarakAntarRow, AwalPosisiRow  - SettingTulisanJarakDenganBaris  );

           }
           if ((a >= 1) && (a <= JumlahBaris )){
                //AwalPosisiRow += JarakAntarRow;
               //g2d.drawString("Nama Bahan x", AwalRowPosisi + JarakAntarRow, AwalPosisiRow  - SettingTulisanJarakDenganBaris  );

           }      
           if (a == 1){
               JarakDariAtasDatabase  = AwalPosisiRow  - SettingTulisanJarakDenganBaris ;
               AwalPosisiDatabase     = AwalRowPosisi + JarakAntarRow;
                //AwalPosisiRow += JarakAntarRow;
               //g2d.drawString("Nama Bahan x", AwalRowPosisi + JarakAntarRow, AwalPosisiRow  - SettingTulisanJarakDenganBaris  );
           }       

           if (a == 0 ){
               GarisDetailAwal = AwalPosisiRow;
           }
           if (a == JumlahBaris ){
               GarisDetailAkhir = AwalPosisiRow;
           }

       }
       
       AmbilDataBahanResep(g2d,NoTransaksi ,AwalPosisiDatabase + 5, 
       AwalPosisiDatabase+ 400, AwalPosisiDatabase + 500, AwalPosisiDatabase +600, JarakDariAtasDatabase,
       AwalRowPosisi,JarakAntarRow1, AkhirRowPosisi, 20, 765);

        /*
        * Data Totalan
        */
       JarakDariAtas = BarisDinamikJarakDariAtas ;
       GarisDetailAkhir = BarisDinamikJarakDariAtas;
       SettingTulisanJarakDenganBaris = 1;
       
       
       int DataTotJarakAtas = JarakDariAtas += JarakAntarRow - SettingTulisanJarakDenganBaris;
       g2d.drawString("TOTAL FOOD Cost", AwalRowPosisi + 25, DataTotJarakAtas );
      // g2d.drawString(TotalProduksi +"", AwalRowPosisi + 610, JarakDariAtas +=JarakAntarRow - JarakAntarRow   - SettingTulisanJarakDenganBaris);
       g2d.drawString("Biaya Kemasan", AwalRowPosisi + 25, JarakDariAtas += JarakAntarRow - SettingTulisanJarakDenganBaris);
      // g2d.drawString(BiayaKemasan + "", AwalRowPosisi + 610,JarakDariAtas += JarakAntarRow - JarakAntarRow  - SettingTulisanJarakDenganBaris);
         
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Lain - lain", AwalRowPosisi + 25, JarakDariAtas += JarakAntarRow - SettingTulisanJarakDenganBaris );
       g2d.drawString("Total", AwalRowPosisi +25,JarakDariAtas += JarakAntarRow- SettingTulisanJarakDenganBaris );
       
       /*
        * Total akhir produksi
        */      
       int TotalAkhirProduksi   =   HitunganTotalProduksi(TotalProduksi, BiayaLain, BiayaKemasan);
       //g2d.drawString( TotalAkhirProduksi + "", AwalRowPosisi + 610,JarakDariAtas +=JarakAntarRow - JarakAntarRow  - SettingTulisanJarakDenganBaris );
       //g2d.drawString(BiayaLain + "", AwalRowPosisi +610, JarakDariAtas += JarakAntarRow -(JarakAntarRow * 2 )- SettingTulisanJarakDenganBaris);
      
       /*
        * Tulis dari Kanan
        */     
       //final BufferedImage img = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
       int total_width = AwalRowPosisi + 790; //img.getWidth();
       int padding = 100;
       int y =  DataTotJarakAtas - JarakAntarRow - SettingTulisanJarakDenganBaris;
       String [] words = new String[]{TotalProduksi, BiayaKemasan, BiayaLain,TotalAkhirProduksi+ ""};
       for(int i = 0; i < words.length; i++){
            int actual_width = g2d.getFontMetrics().stringWidth(words[i]);
            int x = total_width - actual_width - padding;
            g2d.drawString(words[i], x, y += 20);
        }


       JumlahBaris = 3;
      // AwalPosisiRow =x; 
       for (int a = 0 ; a <= JumlahBaris; a++ ){
           g2d.drawLine(AwalRowPosisi, BarisDinamikJarakDariAtas += JarakAntarRow , AkhirRowPosisi,BarisDinamikJarakDariAtas += JarakAntarRow - JarakAntarRow);  
       }
       AwalPosisiRow = BarisDinamikJarakDariAtas;
       
       /*
        * Pemisah untuk column
        * (Garis vertikal dan posisi, panjang dari, garis vertikan dan posisi, panjang ke)
        */
       AwalPosisiColum = AwalPosisiRowGarisPinggir +SettingAntarRow; // Mengikutin  int AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;
       int LetakColumAwal  =0;
       LetakColumAwal      = AwalRowPosisi + LetakColumAwal;
       int JarakColumKe1   = AkhirRowPosisi;
       int JarakColumKe2   = 400 + LetakColumAwal;
       int JarakColumKe3   = 500 + LetakColumAwal;
       int JarakColumKe4    = 600 + LetakColumAwal;

       /*
        * Vertikal 2 pinggir
        */
       g2d.drawLine(LetakColumAwal, AwalPosisiColum, LetakColumAwal, AwalPosisiRow);
       g2d.drawLine(JarakColumKe1, AwalPosisiColum, JarakColumKe1, AwalPosisiRow);

       g2d.drawLine(JarakColumKe2, GarisDetailAwal - JarakAntarRow, JarakColumKe2 , GarisDetailAkhir);
       g2d.drawLine(JarakColumKe3, GarisDetailAwal - ( JarakAntarRow * 2 ), JarakColumKe3 , GarisDetailAkhir);
       g2d.drawLine(JarakColumKe4, GarisDetailAwal - ( JarakAntarRow * 2 ), JarakColumKe4 , GarisDetailAkhir);


       /*
        * Garis sejalan yg pisah
        */   
       g2d.drawLine(AwalRowPosisi, GarisRowPisah,JarakColumKe3,GarisRowPisah);
       g2d.drawLine(JarakColumKe4, GarisRowPisah , AkhirRowPosisi,GarisRowPisah);




       /* 
        * ===============================================================================================
        */

       /*
        * Awal Posisi Vertikal ini harus ditambah
        * Horisontal harus hilang
        * 
        * AwalRowPosisi = AwalRowPosisi - 20; dan AkhirRowPosisi = AkhirRowPosisi + 20; harus sama
        * 
        */

       /*
        * Settingan
        */  
       AwalRowPosisi = AwalRowPosisi - 35;

       AkhirRowPosisi = AkhirRowPosisi + 20;
       AwalRowPosisi  = 25 +AwalRowPosisi;  // 50 +AkhirRowPosisi;  >( Horizontal )
       int AwalPosisiGaris = AwalRowPosisi - 25; // Vertikal
       int JarakDrAtasMurni2    = AwalPosisiRow + 20; // JarakDariAtas + 10; 
       //JarakDariAtas  = JarakDariAtas + 20;
       int JarakDrAtasMurni    = AwalPosisiRow + 25; // JarakDariAtas + 30;

       /*
        *  Vertical
        *  printButton.setBounds(AwalRowPosisi,0,80,30);
           VertiikalButton.setBounds(AwalRowPosisi + 100,0,80,30);
        */

       /*
        * Horisontal
        * printButton.setBounds(AwalRowPosisi,0,80,30);
        * VertiikalButton.setBounds(AwalRowPosisi + 100,0,80,30)
        */

       /*
        * Jarak antar paragraf
        */
       int SpaceParagraf = 10;

       /*
        * Vertikal
        */
       //printButton.setBounds(AwalRowPosisi + 1100,0,80,30);
       
       /*
        * Hitung Harga Jual Porsi
        */
       
       
       double HJualp    =   HargaJualPerPorsi(TotalAkhirProduksi, KenaikanYangDiHarapkan, JumlahPorsi);
       double LabaKotor1=  LabaKotorDanLabaBersih(KenaikanYangDiHarapkanInt, TotalAkhirProduksi);
       
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Harga Jual Perporsi      :", AwalRowPosisi , JarakDariAtas = JarakDrAtasMurni );
       g2d.drawString("Kenaikan Yang Diharapkan :", AwalRowPosisi, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Variabel cost + ( Kenaikan(%) x Variabel Cost )", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ TotalAkhirProduksi +" + ( "+ KenaikanYangDiHarapkan+" % ) x "+ TotalAkhirProduksi +" )", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ FormatDesimalString (KenaikanYangDiHarapkanInt), AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Harga Jual Perporsi ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= ( Variabel Cost + Mark Up ) \\ Jumlah Porsi ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= ( "+ FormatDesimalString (KenaikanYangDiHarapkanInt)+" ) \\ "+ JumlahPorsi, AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+FormatDesimalString (HJualp), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Laba Kotor ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Total Pendapatan - Variabel Cost ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+  FormatDesimalString (KenaikanYangDiHarapkanInt)+" - "+ FormatDesimalString (TotalAkhirProduksi ), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ FormatDesimalString (LabaKotor1), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       int GarisBwah   = JarakDariAtas += JarakAntarRow;

       AwalRowPosisi  = 350 +AwalRowPosisi; // Akhir Posisis Horizontal
       JarakDariAtas  = JarakDrAtasMurni;

       /*
        * Pindah Samping
        */
       double BebanTenaga     =   BebanAll(BebanTenagaKerja,LabaKotor1 );
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Beban - Beban ", AwalRowPosisi, JarakDariAtas);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("Upah Tenaga Kerja ", AwalRowPosisi, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= Beban Tenaga Kerja( % ) x Nominal", AwalRowPosisi +20 , JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+ BebanTenagaKerja + " % x " +FormatDesimalString (LabaKotor1),  AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= " + FormatDesimalString (BebanTenaga), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
     
       double B_Penyusutan    =   BebanAll(BebanPenyusutan,LabaKotor1 );
       g2d.drawString("Biaya Penyusutan Alat ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.drawString("= Beban Penyusutan % x Laba Kotor ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+ BebanPenyusutan+" % x " + FormatDesimalString (LabaKotor1), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ FormatDesimalString (B_Penyusutan), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       
       double BebanLain       =   BebanAll(BebanLainnya,LabaKotor1 );
       g2d.drawString("Biaya Lainnya ( (listrik, air, pajak, dsb)  ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf );
       g2d.drawString("= % x Laba Kotor ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+ BebanLainnya+ " % x "+FormatDesimalString (LabaKotor1), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ FormatDesimalString (BebanLain), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       AwalRowPosisi  = 250 +AwalRowPosisi; //600+ AkhirRowPosisi Horizontal
       JarakDariAtas  = JarakDrAtasMurni;
       
       double[] TotBeban    = {BebanTenaga,B_Penyusutan, BebanLain };
       double TotBeban2     = TotalBeban(TotBeban);
       double LabaBersih      =   LabaKotorDanLabaBersih(LabaKotor1, TotBeban2 );
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Laba Bersih",AwalRowPosisi, JarakDariAtas );
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Laba Kotor - Total Beban ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+ FormatDesimalString (LabaKotor1)+" - " + FormatDesimalString (TotBeban2), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= " + FormatDesimalString (LabaBersih), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       
       /*
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Total Beban",AwalRowPosisi, JarakDariAtas );
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Beban Tenaga Kerja - Beban Penyusutan - Beban Lainnya ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       */
       /*
        * Membuat garis Kotak untuk rumus
        * 
        * ( dari samping da, dari atas, panjang, dari atas )
        * Horisontal
        * g2d.drawLine(30 +AkhirRowPosisi , JarakDrAtasMurni2 - 10, AwalRowPosisi + 230 ,JarakDrAtasMurni2 -10);
          g2d.drawLine(30 +AkhirRowPosisi , GarisBwah, AwalRowPosisi + 230 ,GarisBwah );
        */
       //g2d.drawLine(AwalRowPosisi, JarakDariAtas, AkhirRowPosisi,AwalPosisiRow += JarakAntarRow - JarakAntarRow);
       g2d.drawLine(AwalPosisiGaris  , JarakDrAtasMurni2 - 10, AwalRowPosisi +20 + 150 ,JarakDrAtasMurni2 -10);
       g2d.drawLine(AwalPosisiGaris  , GarisBwah, AwalRowPosisi +20 + 150 ,GarisBwah );
       /*
       VertiikalButton.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent E){
                 Vertikal(g2d);
                 System.out.println("xxx");
             }
         }); */

    }

    public void Vertikal(final Graphics2D g2d){
       /*
        * Setup posisi Table dan Data
        */
       int UkuranHuruf    = 8;
       int SettingAntarRow    = 10;
       int JarakAntarRow = SettingAntarRow;
       int AwalPosisiRow =40; // Jarak seluruh data dari atas
       int AwalPosisiRowGarisPinggir = AwalPosisiRow;
       int SettingTulisanJarakDenganBaris = 1;
       //int AwalPosisiAtasHPP   = 
       int AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;
       int JumlahBaris ;
       int AwalRowPosisi = 6 ; // Posisi Geser Semua Data ke kanan // 250 Ukuran vie
       int AkhirRowPosisi  = 500 + AwalRowPosisi;

      /*
       * Isi data ke Tabel dari database
       */ 
       g2d.setFont(new Font("TimesRoma", Font.BOLD, 12));
       g2d.drawString(NamaPerusahaan, AwalRowPosisi, 15);
       g2d.drawString(CreateDate, AwalRowPosisi, 30);
       g2d.drawString(Judul, AwalRowPosisi, 45);
       //g2d.drawString("Trans No :", 300, 45);
       //g2d.drawString(TransNo, 470, 45);
       int TinggiJudulColum = 80;
       g2d.drawString("", 7, TinggiJudulColum);

       int JarakDariAtas   = AwalPosisiRow += JarakAntarRow;


       /*
        * Judul judul Table
        */

       /*
        * Awal Posisi Vertikal ini harus ditambah
        * Horisontal harus hilang
        * Jika dirubah AwalRowPosisi = AwalRowPosisi+ 20; maka ini jg dirubah  AkhirRowPosisi = AkhirRowPosisi + 20;
        * AwalRowPosisi = AwalRowPosisi - 20; dan AkhirRowPosisi = AkhirRowPosisi + 20; harus sama
        */
       AwalRowPosisi = AwalRowPosisi+ 20; 

       /*
        * Garis paling atas
        * Awal Posisi Vertikal ini harus ditambah
        * Horisontal harus hilang
        */
       AkhirRowPosisi = AkhirRowPosisi + 20;
       g2d.drawLine(AwalRowPosisi,  AwalPosisiRow , AkhirRowPosisi,AwalPosisiRow += AwalPosisiRow - AwalPosisiRow);

       g2d.setFont(new Font("TimesRoma", Font.BOLD, 10));
       g2d.drawString(JudulResep + "", AwalRowPosisi + 150, JarakDariAtas += JarakAntarRow - SettingTulisanJarakDenganBaris );
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));

       g2d.drawString("@Porsi", AwalRowPosisi + 25, JarakDariAtas += JarakAntarRow- SettingTulisanJarakDenganBaris);
       g2d.drawString("Harga Satuan", AwalRowPosisi + 425, JarakDariAtas += JarakAntarRow - JarakAntarRow - SettingTulisanJarakDenganBaris);

       g2d.drawString("Nama Bahan", AwalRowPosisi + 25, JarakDariAtas += JarakAntarRow  );
       g2d.drawString("Jumlah", AwalRowPosisi + 220, JarakDariAtas += JarakAntarRow - JarakAntarRow - SettingTulisanJarakDenganBaris );

       g2d.drawString(JumlahPorsi +" Porsi", AwalRowPosisi + 320, JarakDariAtas += ( JarakAntarRow - (JarakAntarRow * 2)) + SettingTulisanJarakDenganBaris );



       g2d.drawLine(AwalRowPosisi, AwalPosisiRow += JarakAntarRow, AkhirRowPosisi,AwalPosisiRow += JarakAntarRow - JarakAntarRow);

       /*
        * Header Tabel
        */
       JarakAntarRow = SettingAntarRow;
       AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;

       int GarisRowPisah   = AwalPosisiRow += JarakAntarRow;

       /*
        * Garis Horisontal
        * Pembuatan table
        * Setting
        */
       
       /*
        * Garis Horisontal
        * Pembuatan table
        * Setting
        */
       JarakAntarRow = SettingAntarRow;
       AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;
       JumlahBaris   = 10;

       /*
       JarakAntarRow = SettingAntarRow;
       AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;
       JumlahBaris   = 30;
       */
       
       int JarakAntarColumn = 100;
       int JumlahColum      = 6;
       int GarisDetailAwal = 0;
       int GarisDetailAkhir    = 0;
       
       int JarakDariAtasDatabase = 0;
       int AwalPosisiDatabase = 0;
       int AwalRowPosisi1 = 0;
       int JarakAntarRow1 = 1;       
       int AkhirRowPosisi1 = 0;

       for (int a = 0 ; a <= JumlahBaris; a++ ){

           /*
            * Data
            */
           if ((a < 1) ){
                g2d.drawLine(AwalRowPosisi, AwalPosisiRow += JarakAntarRow, AkhirRowPosisi,AwalPosisiRow += JarakAntarRow - JarakAntarRow);
                //AwalPosisiRow += JarakAntarRow;
               //g2d.drawString("Nama Bahan x", AwalRowPosisi + JarakAntarRow, AwalPosisiRow  - SettingTulisanJarakDenganBaris  );

           }
           if (a == 1){
               JarakDariAtasDatabase  = AwalPosisiRow  - SettingTulisanJarakDenganBaris ;
               AwalPosisiDatabase     = AwalRowPosisi + JarakAntarRow;
                //AwalPosisiRow += JarakAntarRow;
               //g2d.drawString("Nama Bahan x", AwalRowPosisi + JarakAntarRow, AwalPosisiRow  - SettingTulisanJarakDenganBaris  );
           }       

           if (a == 0 ){
               GarisDetailAwal = AwalPosisiRow;
           }
           if (a == JumlahBaris ){
               GarisDetailAkhir = AwalPosisiRow;
           }

       }
       /*
       final Graphics2D g2d, String NoTransaksi ,int AwalKiriBahan, 
          int AwalKiriQty1, int AwalKiriQty, int AwalKiriTotHarga, int JarakDariAtas,
          int AwalRowPosisi,int JarakAntarRow, int AkhirRowPosisi, int JarakAntarRow2, int PosisiAngkaDariKiri)
       */
       AmbilDataBahanResep(g2d,NoTransaksi ,AwalPosisiDatabase + 5, 
       AwalPosisiDatabase+ 210, AwalPosisiDatabase + 310, AwalPosisiDatabase +200, JarakDariAtasDatabase,
       AwalRowPosisi,JarakAntarRow1, AkhirRowPosisi, 10, 555);

        /*
        * Data Totalan
        */
       JarakDariAtas = BarisDinamikJarakDariAtas ;
       GarisDetailAkhir = BarisDinamikJarakDariAtas;
       
       //JarakDariAtas = AwalPosisiRow ;
       SettingTulisanJarakDenganBaris = 1;
       
       
       int DataTotJarakAtas = JarakDariAtas += JarakAntarRow - SettingTulisanJarakDenganBaris;
       g2d.drawString("TOTAL FOOD Cost", AwalRowPosisi + 25, DataTotJarakAtas );
      // g2d.drawString(TotalProduksi +"", AwalRowPosisi + 610, JarakDariAtas +=JarakAntarRow - JarakAntarRow   - SettingTulisanJarakDenganBaris);
       g2d.drawString("Biaya Kemasan", AwalRowPosisi + 25, JarakDariAtas += JarakAntarRow - SettingTulisanJarakDenganBaris);
      // g2d.drawString(BiayaKemasan + "", AwalRowPosisi + 610,JarakDariAtas += JarakAntarRow - JarakAntarRow  - SettingTulisanJarakDenganBaris);
         
       g2d.setFont(new Font("TimesRoma",Font.PLAIN, UkuranHuruf));
       g2d.drawString("Lain - lain", AwalRowPosisi + 25, JarakDariAtas += JarakAntarRow - SettingTulisanJarakDenganBaris );
       g2d.drawString("Total", AwalRowPosisi +25,JarakDariAtas += JarakAntarRow- SettingTulisanJarakDenganBaris );
   
       
      // int x =AwalPosisiRow;
        /*
        * Data Totalan
        *
       
       g2d.drawString("TOTAL FOOD Cost", AwalRowPosisi + 25, JarakDariAtas += JarakAntarRow - SettingTulisanJarakDenganBaris );
       //g2d.drawString("Nominal", AwalRowPosisi + 405, JarakDariAtas +=JarakAntarRow - JarakAntarRow   - SettingTulisanJarakDenganBaris);
       g2d.drawString("Biaya Kemasan", AwalRowPosisi + 25, JarakDariAtas += JarakAntarRow - SettingTulisanJarakDenganBaris);
       //g2d.drawString("Nominal", AwalRowPosisi + 405,JarakDariAtas += JarakAntarRow - JarakAntarRow  - SettingTulisanJarakDenganBaris);

       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("Lain - lain", AwalRowPosisi + 25, JarakDariAtas += JarakAntarRow - SettingTulisanJarakDenganBaris );
       g2d.drawString("Total", AwalRowPosisi +25,JarakDariAtas += JarakAntarRow- SettingTulisanJarakDenganBaris );


       g2d.drawString("Nominal", AwalRowPosisi + 405,JarakDariAtas +=JarakAntarRow - JarakAntarRow  - SettingTulisanJarakDenganBaris );
       g2d.drawString("Nominal", AwalRowPosisi +405, JarakDariAtas += JarakAntarRow -(JarakAntarRow * 2 )- SettingTulisanJarakDenganBaris);
*/
       /*
        * Total akhir produksi
        */      
       int TotalAkhirProduksi   =   HitunganTotalProduksi(TotalProduksi, BiayaLain, BiayaKemasan);
       //g2d.drawString( TotalAkhirProduksi + "", AwalRowPosisi + 610,JarakDariAtas +=JarakAntarRow - JarakAntarRow  - SettingTulisanJarakDenganBaris );
       //g2d.drawString(BiayaLain + "", AwalRowPosisi +610, JarakDariAtas += JarakAntarRow -(JarakAntarRow * 2 )- SettingTulisanJarakDenganBaris);
      
       /*
        * Tulis dari Kanan
        */     
       //final BufferedImage img = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
       int total_width = AwalRowPosisi + 570; //img.getWidth();
       int padding = 100;
       int y =  DataTotJarakAtas - JarakAntarRow  - SettingTulisanJarakDenganBaris;
       String [] words = new String[]{TotalProduksi, BiayaKemasan, BiayaLain,TotalAkhirProduksi+ ""};
       for(int i = 0; i < words.length; i++){
            int actual_width = g2d.getFontMetrics().stringWidth(words[i]);
            int x = total_width - actual_width - padding;
            g2d.drawString(words[i], x, y += 10);
        }

       JumlahBaris = 3;
      // AwalPosisiRow =x; 
       for (int a = 0 ; a <= JumlahBaris; a++ ){
           g2d.drawLine(AwalRowPosisi, BarisDinamikJarakDariAtas += JarakAntarRow , AkhirRowPosisi,BarisDinamikJarakDariAtas += JarakAntarRow - JarakAntarRow);  
       }
       AwalPosisiRow = BarisDinamikJarakDariAtas;
/*
       JumlahBaris = 3;
      // AwalPosisiRow =x; 
       for (int a = 0 ; a <= JumlahBaris; a++ ){
           g2d.drawLine(AwalRowPosisi, AwalPosisiRow += JarakAntarRow , AkhirRowPosisi,AwalPosisiRow += JarakAntarRow - JarakAntarRow);  
       }
*/
       
        /*
        * Pemisah untuk column
        * (Garis vertikal dan posisi, panjang dari, garis vertikan dan posisi, panjang ke)
        */


       /*
        * Pemisah untuk column
        * (Garis vertikal dan posisi, panjang dari, garis vertikan dan posisi, panjang ke)
        */
       AwalPosisiColum = AwalPosisiRowGarisPinggir +SettingAntarRow; // Mengikutin  int AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;
       int LetakColumAwal  =0;
       LetakColumAwal      = AwalRowPosisi + LetakColumAwal;
       int JarakColumKe1   = AkhirRowPosisi;
       int JarakColumKe2   = 200 + LetakColumAwal;
       int JarakColumKe3   = 300 + LetakColumAwal;
       int JarakColumKe4    = 400 + LetakColumAwal;

       /*
        * Vertikal 2 pinggir
        *
       g2d.drawLine(LetakColumAwal, AwalPosisiColum, LetakColumAwal, AwalPosisiRow);
       g2d.drawLine(JarakColumKe1, AwalPosisiColum, JarakColumKe1, AwalPosisiRow);

       g2d.drawLine(JarakColumKe2, GarisDetailAwal - JarakAntarRow, JarakColumKe2 , GarisDetailAkhir);
       g2d.drawLine(JarakColumKe3, GarisDetailAwal - ( JarakAntarRow * 2 ), JarakColumKe3 , GarisDetailAkhir);
       g2d.drawLine(JarakColumKe4, GarisDetailAwal - ( JarakAntarRow * 2 ), JarakColumKe4 , GarisDetailAkhir);


       /*
        * Garis sejalan yg pisah
        * 
       g2d.drawLine(AwalRowPosisi, GarisRowPisah,JarakColumKe3,GarisRowPisah);
       g2d.drawLine(JarakColumKe4, GarisRowPisah , AkhirRowPosisi,GarisRowPisah);
       */
       
       /*
        * Vertikal 2 pinggir Atas
        */
       g2d.drawLine(LetakColumAwal, AwalPosisiColum, LetakColumAwal, AwalPosisiRow);
       g2d.drawLine(JarakColumKe1, AwalPosisiColum, JarakColumKe1, AwalPosisiRow);

       g2d.drawLine(JarakColumKe2, GarisDetailAwal - JarakAntarRow, JarakColumKe2 , GarisDetailAkhir);
       g2d.drawLine(JarakColumKe3, GarisDetailAwal - ( JarakAntarRow * 2 ), JarakColumKe3 , GarisDetailAkhir);
       g2d.drawLine(JarakColumKe4, GarisDetailAwal - ( JarakAntarRow * 2 ), JarakColumKe4 , GarisDetailAkhir);


       /*
        * Garis sejalan yg pisah
        */   
       g2d.drawLine(AwalRowPosisi, GarisRowPisah,JarakColumKe3,GarisRowPisah);
       g2d.drawLine(JarakColumKe4, GarisRowPisah , AkhirRowPosisi,GarisRowPisah);

       /* 
        * ===============================================================================================
        */

       /*
        * Awal Posisi Vertikal ini harus ditambah
        * Horisontal harus hilang
        * 
        * AwalRowPosisi = AwalRowPosisi - 20; dan AkhirRowPosisi = AkhirRowPosisi + 20; harus sama
        * 
        */

       /*
        * Settingan
        */
       AwalRowPosisi = AwalRowPosisi - 20;

       AkhirRowPosisi = AkhirRowPosisi + 20;
       AwalRowPosisi  = 25 +AwalRowPosisi;  // 50 +AkhirRowPosisi;  >( Horizontal )
       int AwalPosisiGaris = AwalRowPosisi - 25; // Vertikal
       int JarakDrAtasMurni2    = AwalPosisiRow + 20; // JarakDariAtas + 10; 
       //JarakDariAtas  = JarakDariAtas + 20;
       int JarakDrAtasMurni    = BarisDinamikJarakDariAtas + 25; // JarakDariAtas + 30;

       /*
        *  Vertical
        *  printButton.setBounds(AwalRowPosisi,0,80,30);
           VertiikalButton.setBounds(AwalRowPosisi + 100,0,80,30);
        */

       /*
        * Horisontal
        * printButton.setBounds(AwalRowPosisi,0,80,30);
        * VertiikalButton.setBounds(AwalRowPosisi + 100,0,80,30)
        */

       /*
        * Jarak antar paragraf
        */
       int SpaceParagraf = 10;

       /*
        * Vertikal
        */
       //printButton.setBounds(AwalRowPosisi + 870,0,80,30);
       //JarakAntarRow  = 
       double HJualp    =   HargaJualPerPorsi(TotalAkhirProduksi, KenaikanYangDiHarapkan, JumlahPorsi);
       double LabaKotor1=  LabaKotorDanLabaBersih(KenaikanYangDiHarapkanInt, TotalAkhirProduksi);
       
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Harga Jual Perporsi      :", AwalRowPosisi , JarakDariAtas = JarakDrAtasMurni );
       g2d.drawString("Kenaikan Yang Diharapkan :", AwalRowPosisi, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Variabel cost + ( Kenaikan(%) x Variabel Cost )", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ TotalAkhirProduksi +" + ( "+ KenaikanYangDiHarapkan+" % ) x "+ TotalAkhirProduksi +" )", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ TotalAkhirProduksi +" + " + FormatDesimalString ( HasilSementaraHrgJual) +"", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ FormatDesimalString (KenaikanYangDiHarapkanInt), AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Harga Jual Perporsi ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= ( Variabel Cost + Mark Up ) \\ Jumlah Porsi ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= ( "+ FormatDesimalString (KenaikanYangDiHarapkanInt)+" ) \\ "+ JumlahPorsi, AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+FormatDesimalString ( HJualp), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Laba Kotor ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Total Pendapatan - Variabel Cost ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+ FormatDesimalString ( KenaikanYangDiHarapkanInt)+" - "+ TotalAkhirProduksi , AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ FormatDesimalString (LabaKotor1), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       int GarisBwah   = JarakDariAtas += JarakAntarRow;

       AwalRowPosisi  = 200 +AwalRowPosisi; // Akhir Posisis Horizontal
       JarakDariAtas  = JarakDrAtasMurni;

       /*
        * Pindah Samping
        */
       double BebanTenaga     =   BebanAll(BebanTenagaKerja,LabaKotor1 );
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Beban - Beban ", AwalRowPosisi, JarakDariAtas);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("Upah Tenaga Kerja ", AwalRowPosisi, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= Beban Tenaga Kerja( % ) x Nominal", AwalRowPosisi +20 , JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+ BebanTenagaKerja + " % x " +FormatDesimalString (LabaKotor1),  AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= " + FormatDesimalString (BebanTenaga), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
     
       double B_Penyusutan    =   BebanAll(BebanPenyusutan,LabaKotor1 );
       g2d.drawString("Biaya Penyusutan Alat ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.drawString("= Beban Penyusutan % x Laba Kotor ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+ BebanPenyusutan+" % x " + FormatDesimalString (LabaKotor1), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ FormatDesimalString (B_Penyusutan), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       
       double BebanLain       =   BebanAll(BebanLainnya,LabaKotor1 );
       g2d.drawString("Biaya Lainnya ( (listrik, air, pajak, dsb)  ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf );
       g2d.drawString("= % x Laba Kotor ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+ BebanLainnya+ " % x "+FormatDesimalString (LabaKotor1), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= "+ FormatDesimalString (BebanLain), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       AwalRowPosisi  = 160 +AwalRowPosisi; //600+ AkhirRowPosisi Horizontal
       JarakDariAtas  = JarakDrAtasMurni;
       
       double[] TotBeban    = {BebanTenaga,B_Penyusutan, BebanLain };
       double TotBeban2     = TotalBeban(TotBeban);
       double LabaBersih      =   LabaKotorDanLabaBersih(LabaKotor1, TotBeban2 );
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Laba Bersih",AwalRowPosisi, JarakDariAtas );
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Laba Kotor - Total Beban ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= "+ FormatDesimalString (LabaKotor1)+" - " + FormatDesimalString (TotBeban2), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= " + FormatDesimalString (LabaBersih), AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       
       /*
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Total Beban",AwalRowPosisi, JarakDariAtas );
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Beban Tenaga Kerja - Beban Penyusutan - Beban Lainnya ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       */
       /*
        * Membuat garis Kotak untuk rumus
        * 
        * ( dari samping da, dari atas, panjang, dari atas )
        * Horisontal
        * g2d.drawLine(30 +AkhirRowPosisi , JarakDrAtasMurni2 - 10, AwalRowPosisi + 230 ,JarakDrAtasMurni2 -10);
          g2d.drawLine(30 +AkhirRowPosisi , GarisBwah, AwalRowPosisi + 230 ,GarisBwah );
        */
       //g2d.drawLine(AwalRowPosisi, JarakDariAtas, AkhirRowPosisi,AwalPosisiRow += JarakAntarRow - JarakAntarRow);
       g2d.drawLine(AwalPosisiGaris  , JarakDrAtasMurni2 - 10, AwalRowPosisi +20 + 150 ,JarakDrAtasMurni2 -10);
       g2d.drawLine(AwalPosisiGaris  , GarisBwah, AwalRowPosisi +20 + 150 ,GarisBwah );
       /*
       VertiikalButton.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent E){
                 Vertikal(g2d);
                 System.out.println("xxx");
             }
         }); */
/*
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Harga Jual Perporsi      :", AwalRowPosisi , JarakDariAtas = JarakDrAtasMurni );
       g2d.drawString("Kenaikan Yang Diharapkan :", AwalRowPosisi, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Variabel cost + ( Kenaikan(%) x Variabel Cost )", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nomial + ( Kenaikan(%) x Nominal )", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nomial +  Nominal ", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nomial ", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Harga Jual Perporsi ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= ( Variabel Cost + Mark Up ) \\ Jumlah Porsi ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= ( Nominal  +Nominal ) \\ Jumlah Porsi ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Laba Kotor ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Total Pendapatan - Variabel Cost ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= Nominal - Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       int GarisBwah   = JarakDariAtas += JarakAntarRow;

       AwalRowPosisi  = 250 +AwalRowPosisi; // Akhir Posisis Horizontal
       JarakDariAtas  = JarakDrAtasMurni;





       /*
        * Pindah Samping
        *
       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Beban - Beban ", AwalRowPosisi, JarakDariAtas);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("Upah Tenaga Kerja ", AwalRowPosisi, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= % x Laba Kotor ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= % x Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       g2d.drawString("Biaya Penyusutan Alat ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.drawString("= % x Laba Kotor ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= % x Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       g2d.drawString("Biaya Lainnya ( (listrik, air, pajak, dsb)  ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf );
       g2d.drawString("= % x Laba Kotor ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= % x Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       AwalRowPosisi  = 150 +AwalRowPosisi; //600+ AkhirRowPosisi Horizontal
       JarakDariAtas  = JarakDrAtasMurni;

       g2d.setFont(new Font("TimesRoma", Font.BOLD, UkuranHuruf));
       g2d.drawString("Laba Bersih",AwalRowPosisi, JarakDariAtas );
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, UkuranHuruf));
       g2d.drawString("= Laba Kotor - Total Beban ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= Nominal - Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       /*
        * Membuat garis Kotak untuk rumus
        * 
        * ( dari samping da, dari atas, panjang, dari atas )
        * Horisontal
        * g2d.drawLine(30 +AkhirRowPosisi , JarakDrAtasMurni2 - 10, AwalRowPosisi + 230 ,JarakDrAtasMurni2 -10);
          g2d.drawLine(30 +AkhirRowPosisi , GarisBwah, AwalRowPosisi + 230 ,GarisBwah );
        */
       //g2d.drawLine(AwalRowPosisi, JarakDariAtas, AkhirRowPosisi,AwalPosisiRow += JarakAntarRow - JarakAntarRow);
       //g2d.drawLine(AwalPosisiGaris  , JarakDrAtasMurni2 - 10, AwalRowPosisi +20 + 100 ,JarakDrAtasMurni2 -10);
      // g2d.drawLine(AwalPosisiGaris  , GarisBwah, AwalRowPosisi +20 + 100 ,GarisBwah );
       /*
       VertiikalButton.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent E){
                 Vertikal(g2d);
                 System.out.println("xxx");
             }
         }); */
    }

    public void Horizontal(final Graphics2D g2d){
       /*
        * Setup posisi Table dan Data
        */    
       int JarakAntarRow = 50;
       int AwalPosisiRow = 5; // Jarak seluruh data dari atas
       //int AwalPosisiAtasHPP   = 
       int AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;
       int JumlahBaris ;
       int AwalRowPosisi = 75 ; // Posisi Geser Semua Data ke kanan
       int AkhirRowPosisi  = 600 + AwalRowPosisi;

      /*
       * Isi data ke Tabel dari database
       */ 
       g2d.drawString(NamaPerusahaan, AwalRowPosisi, 15);
       g2d.drawString(CreateDate, AwalRowPosisi, 30);
       g2d.drawString(Judul, AwalRowPosisi, 45);
       int TinggiJudulColum = 80;
       g2d.drawString("", 7, TinggiJudulColum);

       int JarakDariAtas   = AwalPosisiRow += JarakAntarRow;


       /*
        * Judul judul Table
        */

       g2d.setFont(new Font("TimesRoma", Font.BOLD, 20));
       g2d.drawString("Xxxxxxxxxx", AwalRowPosisi + 150, AwalPosisiRow +30 );
       g2d.setFont(new Font("TimesRoma", Font.BOLD, 14));

       g2d.drawString("@Porsi", AwalRowPosisi + 25, AwalPosisiRow +70 );
       g2d.drawString("Harga Satuan", AwalRowPosisi + 405, AwalPosisiRow +70 );

       g2d.drawString("Nama Bahan", AwalRowPosisi + 25, AwalPosisiRow +95 );
       g2d.drawString("Jumlah", AwalRowPosisi + 220, AwalPosisiRow +95 );

       g2d.drawString("100 Porsi", AwalRowPosisi + 320, AwalPosisiRow +77 );

       /*
        * Data Totalan
        */
       g2d.drawString("TOTAL FOOD Cost", AwalRowPosisi + 25, AwalPosisiRow +370 );
       g2d.drawString("Nominal", AwalRowPosisi + 405, AwalPosisiRow +370 );
       g2d.drawString("Total", AwalRowPosisi + 25, AwalPosisiRow +370 + 75);
       g2d.drawString("Nominal", AwalRowPosisi + 405, AwalPosisiRow +370 + 75);

       g2d.setFont(new Font("TimesRoma", Font.PLAIN, 14));
       g2d.drawString("Biaya Kemasan", AwalRowPosisi + 25, AwalPosisiRow +370 + 25 );
       g2d.drawString("Lain -lain", AwalRowPosisi +25, AwalPosisiRow +370 + 50 );


       g2d.drawString("Nominal", AwalRowPosisi + 405, AwalPosisiRow +370 + 25 );
       g2d.drawString("Nominal", AwalRowPosisi +405, AwalPosisiRow +370 + 50 );

       g2d.drawLine(AwalRowPosisi, JarakDariAtas, AkhirRowPosisi,AwalPosisiRow += JarakAntarRow - JarakAntarRow);
       g2d.drawLine(AwalRowPosisi, AwalPosisiRow += JarakAntarRow, AkhirRowPosisi,AwalPosisiRow += JarakAntarRow - JarakAntarRow);

       /*
        * Header Tabel
        */
       JarakAntarRow = 25;
       AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;

       int GarisRowPisah   = AwalPosisiRow += JarakAntarRow;

       /*
        * Garis Horisontal
        * Pembuatan table
        */
       JarakAntarRow = 25;
       AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;
       JumlahBaris   = 10;

       int JarakAntarColumn = 100;
       int JumlahColum      = 6;
       int GarisDetailAwal = 0;
       int GarisDetailAkhir    = 0;

       for (int a = 0 ; a <= JumlahBaris; a++ ){
           g2d.drawLine(AwalRowPosisi, AwalPosisiRow += JarakAntarRow, AkhirRowPosisi,AwalPosisiRow += JarakAntarRow - JarakAntarRow);

           /*
            * Data
            */
           if ((a >= 1) && (a <= JumlahBaris )){
               g2d.drawString("Nama Bahan x", AwalRowPosisi + JarakAntarRow, AwalPosisiRow - 5 );
           }        

           if (a == 0 ){
               GarisDetailAwal = AwalPosisiRow;
           }
           if (a == JumlahBaris ){
               GarisDetailAkhir = AwalPosisiRow;
           }

       }

       JumlahBaris = 3;
       for (int a = 0 ; a <= JumlahBaris; a++ ){
           g2d.drawLine(AwalRowPosisi, AwalPosisiRow += JarakAntarRow, AkhirRowPosisi,AwalPosisiRow += JarakAntarRow - JarakAntarRow);  
       }

       /*
        * Pemisah untuk column
        * (Garis vertikal dan posisi, panjang dari, garis vertikan dan posisi, panjang ke)
        */
       AwalPosisiColum = 55; // Mengikutin  int AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;
       int LetakColumAwal  =0;
       LetakColumAwal      = AwalRowPosisi + LetakColumAwal;
       int JarakColumKe1   = AkhirRowPosisi;
       int JarakColumKe2   = 200 + LetakColumAwal;
       int JarakColumKe3   = 300 + LetakColumAwal;
       int JarakColumKe4    = 400 + LetakColumAwal;

       g2d.drawLine(LetakColumAwal, AwalPosisiColum, LetakColumAwal, AwalPosisiRow);
       g2d.drawLine(JarakColumKe1, AwalPosisiColum, JarakColumKe1, AwalPosisiRow);

       g2d.drawLine(JarakColumKe2, GarisDetailAwal - JarakAntarRow, JarakColumKe2 , GarisDetailAkhir);
       g2d.drawLine(JarakColumKe3, GarisDetailAwal - ( JarakAntarRow * 2 ), JarakColumKe3 , GarisDetailAkhir);
       g2d.drawLine(JarakColumKe4, GarisDetailAwal - ( JarakAntarRow * 2 ), JarakColumKe4 , GarisDetailAkhir);


       /*
        * Garis sejalan yg pisah
        */   
       g2d.drawLine(AwalRowPosisi, GarisRowPisah,JarakColumKe3,GarisRowPisah);
       g2d.drawLine(JarakColumKe4, GarisRowPisah , AkhirRowPosisi,GarisRowPisah);




       /* 
        * ===============================================================================================
        */

       //AkhirRowPosisi = AkhirRowPosisi + 200;
       AwalRowPosisi  = 50 +AkhirRowPosisi;  // 50 +AkhirRowPosisi;  >( Horizontal )
       int JarakDrAtasMurni2   = JarakDariAtas + 10; // JarakDariAtas + 10; 
       //JarakDariAtas  = JarakDariAtas + 20;
       int JarakDrAtasMurni    = JarakDariAtas + 30;; // JarakDariAtas + 30;

       /*
        *  Vertical
        *  printButton.setBounds(AwalRowPosisi,0,80,30);
           VertiikalButton.setBounds(AwalRowPosisi + 100,0,80,30);
        */

       /*
        * Jarak antar paragraf
        */
       int SpaceParagraf = 25;

//       printButton.setBounds(AwalRowPosisi + 700,0,80,30);

       g2d.setFont(new Font("TimesRoma", Font.BOLD, 14));
       g2d.drawString("Harga Jual Perporsi      :", AwalRowPosisi , JarakDariAtas = JarakDrAtasMurni );
       g2d.drawString("Kenaikan Yang Diharapkan :", AwalRowPosisi, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.PLAIN, 14));
       g2d.drawString("= Variabel cost + ( Kenaikan(%) x Variabel Cost )", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nomial + ( Kenaikan(%) x Nominal )", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nomial +  Nominal ", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nomial ", AwalRowPosisi + 20, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.BOLD, 14));
       g2d.drawString("Harga Jual Perporsi ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, 14));
       g2d.drawString("= ( Variabel Cost + Mark Up ) \\ Jumlah Porsi ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= ( Nominal  +Nominal ) \\ Jumlah Porsi ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       g2d.setFont(new Font("TimesRoma", Font.BOLD, 14));
       g2d.drawString("Laba Kotor ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, 14));
       g2d.drawString("= Total Pendapatan - Variabel Cost ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= Nominal - Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       int GarisBwah   = JarakDariAtas += JarakAntarRow;

       AwalRowPosisi  = 400 +AkhirRowPosisi; // Akhir Posisis Horizontal
       JarakDariAtas  = JarakDrAtasMurni;

       /*
        * Pindah Samping
        */
       g2d.setFont(new Font("TimesRoma", Font.BOLD, 14));
       g2d.drawString("Beban - Beban ", AwalRowPosisi, JarakDariAtas);
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, 14));
       g2d.drawString("Upah Tenaga Kerja ", AwalRowPosisi, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= % x Laba Kotor ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= % x Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       g2d.drawString("Biaya Penyusutan Alat ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.drawString("= % x Laba Kotor ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= % x Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       g2d.drawString("Biaya Lainnya ( (listrik, air, pajak, dsb)  ", AwalRowPosisi, JarakDariAtas += JarakAntarRow + SpaceParagraf);
       g2d.drawString("= % x Laba Kotor ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= % x Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       AwalRowPosisi  = 600 +AkhirRowPosisi; //600+ AkhirRowPosisi Horizontal
       JarakDariAtas  = JarakDrAtasMurni;

       g2d.setFont(new Font("TimesRoma", Font.BOLD, 14));
       g2d.drawString("Laba Bersih",AwalRowPosisi, JarakDariAtas );
       g2d.setFont(new Font("TimesRoma", Font.PLAIN, 14));
       g2d.drawString("= Laba Kotor - Total Beban ", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow );
       g2d.drawString("= Nominal - Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);
       g2d.drawString("= Nominal", AwalRowPosisi +20, JarakDariAtas += JarakAntarRow);

       /*
        * Membuat garis Kotak
        * 
        * ( dari samping da, dari atas, panjang, dari atas )
        * Horisontal
        */
         g2d.drawLine(30 +AkhirRowPosisi , JarakDrAtasMurni2 - 10, AwalRowPosisi + 230 ,JarakDrAtasMurni2 -10);
         g2d.drawLine(30 +AkhirRowPosisi , GarisBwah, AwalRowPosisi + 230 ,GarisBwah );
        //PrintUtilities.printComponent(this);

    }
 }


    /*
    * 
    */