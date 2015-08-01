/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.PrintUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author LANTAI3
 */
public class AlokasiWaktuView extends JPanel {
    
  private int fontSize = 90;
  private int messageWidth;
  private String NamaPerusahaan = "PT BCS_PRO";
  private String CreateDate ;
  private String Judul ="Perhitungan Alokasi Waktu";
  private String TransNo ;
  private String NamaColum1 = "No";
  private String NamaColum2 = "Start Time";
  private String NamaColum3 = "Finish";
  private String NamaColum4 = "Duration";
  
  public String BelanjaStart;
  public String BelanjaFinish;
  public String BelanjaDuration;
 
  public String PProduksiStart;
  public String PProduksiFinish;
  public String PProduksiDuration;
  
  public String ProduksiStart;
  public String ProduksiFinish;
  public String ProduksiDuration;
  
  public String PackingStart;
  public String PackingFinish;
  public String PackingDuration;
  
  public String DileveryStart;
  public String DileveryFinish;
  public String DilveryDuration;
 
  //private String NamaColum5 = "Harga";
  //private String NamaColum6 = "Tot";
  //private String NamaColum7 = "Jumlah";
  
  private SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
  private Connection K = KD.createConnection();
  private JButton printButton       = new JButton("Print");
  
  
  /*
   * Ukuran Kertas
   */
  public AlokasiWaktuView() {
      
      //JScrollPane hSPane = new JScrollPane(this);
      //hSPane.setPreferredSize(new Dimension(350,400));
      //hSPane.add(this);
     // add(hSPane);
    
    SistemPro.TanggalSistem  Tgl = new SistemPro.TanggalSistem();
    setBackground(Color.white);
    this.CreateDate = Tgl.GetTglNow() + "-" +Tgl.GetBlnNow() + "-" +Tgl.GetThnNow() ;
    
    add(printButton, BorderLayout.EAST);
    
            /*
     * Ngikutin tampilan
     */
    
    /*
    Font font = new Font("Serif", Font.PLAIN, fontSize);
    setFont(font);
    FontMetrics metrics = getFontMetrics(font);
    messageWidth = metrics.stringWidth(message);
    */
    
    /*
    int width = messageWidth*5/3;
    int height = fontSize*3;
     */
    
    /*
     * Ukuran Kertas
     */
    int width = 50;
    int height = 200;
    setPreferredSize(new Dimension(width, height));
    printButton.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent E){
                         
              Aksi();
          }
        }
    );
  }
  
  private void Aksi(){
         PrintUtilities.printComponent(this);   
  }
  
  

  /** Draws a black string with a tall angled "shadow"
   *  of the string behind it.
   */
    @Override
  public void paintComponent(Graphics g) {
       
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    
    /*
     * Ukuran Huruf dan posisi huharus di mulai dimana
     */
    int x = messageWidth/10;
    int y = fontSize*5/2;
    g2d.translate(10,10);
    printButton.setBounds(200, 10, 60, 25);
    /*
     * Bayangan
     * 
    g2d.setPaint(Color.lightGray);
    AffineTransform origTransform = g2d.getTransform();
    g2d.shear(-0.95, 0);
    g2d.scale(1, 3);    g2d.drawString(message, 0, 0);
    */
    int b= 85;
    //int Garis1 = 85 ;
   // for (int a = 0; a < 15 ; a++){
       // b += 20;
        //Garis1 += 15;
        //  g2d.drawString(a + "", 16, b);
          
          //g2d.drawLine(10, Garis1, 350, Garis1);
  //  }
    
   /*
    * Isi data ke Tabel dari database
    */ 

           
    //g2d.drawString(message, 0, 0);
    g2d.drawString(NamaPerusahaan, 5, 15);
    g2d.drawString(CreateDate, 5, 30);
    g2d.drawString(Judul, 5, 45);
    //g2d.drawString("Trans No :", 300, 45);
    //g2d.drawString(TransNo, 470, 45);
    int TinggiJudulColum = 80;
    g2d.drawString("Ket ", 7, TinggiJudulColum);
    g2d.drawString(NamaColum2, 40, TinggiJudulColum);
    g2d.drawString(NamaColum3, 110, TinggiJudulColum);
    g2d.drawString(NamaColum4, 180, TinggiJudulColum);
    //g2d.drawString(NamaColum5, 370, 78);
    //g2d.drawString(NamaColum6, 460, 78);
    //g2d.drawString(NamaColum7, 360, 78);
    
    /*
     * Di tampilkan perhitungan waktu
     * Berisi data
     */
    int TinggiBarisIsi = 107;
    g2d.drawString("B", 10, TinggiBarisIsi);
    g2d.drawString(BelanjaStart+"", 40, TinggiBarisIsi);
    g2d.drawString(BelanjaFinish +"", 110, TinggiBarisIsi);
    g2d.drawString(BelanjaDuration +"", 180, TinggiBarisIsi);
    
    TinggiBarisIsi = TinggiBarisIsi+ 25;
    g2d.drawString("PP", 10, TinggiBarisIsi);
    g2d.drawString(PProduksiStart+"", 40, TinggiBarisIsi);
    g2d.drawString(PProduksiFinish+"", 110, TinggiBarisIsi);
    g2d.drawString(PProduksiDuration+"", 180, TinggiBarisIsi);
    
    TinggiBarisIsi = TinggiBarisIsi+ 25;
    g2d.drawString("P", 10, TinggiBarisIsi);
    g2d.drawString(ProduksiStart+"", 40, TinggiBarisIsi);
    g2d.drawString(ProduksiFinish+"", 110, TinggiBarisIsi);
    g2d.drawString(ProduksiDuration+"", 180, TinggiBarisIsi);
    
    TinggiBarisIsi = TinggiBarisIsi+ 25;
    g2d.drawString("PK", 10, TinggiBarisIsi);
    g2d.drawString(PackingStart+"", 40, TinggiBarisIsi);
    g2d.drawString(PackingFinish+"", 110, TinggiBarisIsi);
    g2d.drawString(PackingDuration +"", 180, TinggiBarisIsi);
    
    TinggiBarisIsi = TinggiBarisIsi+ 25;
    g2d.drawString("D", 10, TinggiBarisIsi);
    g2d.drawString(DileveryStart+"", 40, TinggiBarisIsi);
    g2d.drawString(DileveryFinish+"", 110, TinggiBarisIsi);
    g2d.drawString(DilveryDuration+ "", 180, TinggiBarisIsi);
    
    /*
     * Keterangn
     */
    int PosisiKiriKet = 5;
    TinggiBarisIsi = TinggiBarisIsi+ 35;
    g2d.drawString("Keterangan :", PosisiKiriKet, TinggiBarisIsi);
    TinggiBarisIsi = TinggiBarisIsi+ 20;
    g2d.drawString("1. B", PosisiKiriKet + 13, TinggiBarisIsi);
    g2d.drawString("= Belanja", PosisiKiriKet + 70, TinggiBarisIsi);
    TinggiBarisIsi = TinggiBarisIsi+ 20;
    g2d.drawString("2. PP", PosisiKiriKet + 13, TinggiBarisIsi);
    g2d.drawString("= PraProduksi", PosisiKiriKet + 70, TinggiBarisIsi);
    TinggiBarisIsi = TinggiBarisIsi+ 20;
    g2d.drawString("3. P", PosisiKiriKet + 13, TinggiBarisIsi);
    g2d.drawString("= Produksi", PosisiKiriKet + 70, TinggiBarisIsi);
    TinggiBarisIsi = TinggiBarisIsi+ 20;
    g2d.drawString("4. PK", PosisiKiriKet + 13, TinggiBarisIsi);
    g2d.drawString("= Packing/Penataan", PosisiKiriKet + 70, TinggiBarisIsi);
    TinggiBarisIsi = TinggiBarisIsi+ 20;
    g2d.drawString("5. D", PosisiKiriKet + 13, TinggiBarisIsi);
    g2d.drawString("= Dilevery", PosisiKiriKet + 70, TinggiBarisIsi);

    
    /*
     * Garis Horisontal
     * Pembuatan table
     */
    int JarakAntarRow = 25;
    int AwalPosisiRow = 40;
    int AwalPosisiColum = AwalPosisiRow + JarakAntarRow ;
    int JumlahBaris   = 6;
    
    int AwalPosisiColumn = 5;
    int JarakAntarColumn = 100;
    int JumlahColum      = 6;
    for (int a = 0 ; a <= JumlahBaris; a++ ){
        g2d.drawLine(5, AwalPosisiRow += JarakAntarRow, 250,AwalPosisiRow += JarakAntarRow - JarakAntarRow);
    }
    /*
     * Pemisah untuk column
     * (Garis vertikal dan posisi, panjang dari, garis vertikan dan posisi, panjang ke)
     */
    
    g2d.drawLine(30, AwalPosisiColum, 30, AwalPosisiRow);
   
    
    /*
     * Untuk column
     */
    /*
    g2d.setColor(Color.DARK_GRAY);
    for (int a= 0; a<= JumlahColum; a++){
        g2d.drawLine(AwalPosisiColumn += JarakAntarColumn , 5JarakAntarColumn, AwalPosisiColumn += JarakAntarColumn - JarakAntarColumn, 50);
    }
    * */
    
    
    //g2d.drawLine(5, 85, 250, 85);
    //g2d.drawLine(5, 85, 250, 85);
 
  }    
}

