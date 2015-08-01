/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;

/** A window with a custom paintComponent method. Used to
 *  illustrate that you can make a general-purpose method
 *  that can print any component, regardless of whether
 *  that component performs custom drawing.
 *  See the PrintUtilities class for the printComponent method
 *  that lets you print an arbitrary component with a single
 *  function call.
 *  7/99 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 */

public class PembelianBahanPrint extends JPanel {
  private int fontSize = 90;
  private String message = "Java 2D dfdddfffffffffd sfsds sdsdsd sdsdsd sdsdsd dsd";
  private int messageWidth;
  private String NamaPerusahaan = "PT BCS_PRO";
  private String CreateDate ;
  private String Judul      ="Pembelian";
  private String TransNo   ;
  private String NamaColum1 = "No";
  private String NamaColum2 = "Barang";
  private String NamaColum3 = "Qty";
  private String NamaColum4 = "Satuan";
  private String NamaColum5 = "Harga";
  private String NamaColum6 = "Tot Harga";
  private String NamaColum7 = "Jumlah";
  
  private SistemPro.KoneksiDatabase KD = new SistemPro.KoneksiDatabase();
  private Connection K = KD.createConnection();
  
  
  
  /*
   * Ukuran Kertas
   */
  public PembelianBahanPrint(String TransNo, String Date) {
    setBackground(Color.white);
    this.TransNo    = TransNo;
    this.CreateDate = Date;
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
    int width = 350*5/3;
    int height = 200*3;
    setPreferredSize(new Dimension(width, height));
  }

  /** Draws a black string with a tall angled "shadow"
   *  of the string behind it.
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    
    /*
     * Ukuran Huruf dan posisi huharus di mulai dimana
     */
    int x = messageWidth/10;
    int y = fontSize*5/2;
    g2d.translate(10,10);
    
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
    int c = 85;
        int baris;       
        ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT  trans_no ,qty,nama_bahan, satuan, harga_satuan, harga_tot from detail_trans where trans_no = '"+ TransNo + "' order by no asc  ");                     
               baris = HQ.getRow();
               
               int JumlahHarga = 0;
               while(HQ.next()  ){

                    //String No            = String.valueOf(HQ.getInt("no")).toString();
                    String NamaBahan     = HQ.getString("nama_bahan");
                    String Qty           = String.valueOf(HQ.getInt("qty")).toString();
                    String Satuan        = HQ.getString("satuan");
                    String Harga_Satuan  = String.valueOf(HQ.getInt("harga_satuan")).toString();
                    String Harga_Tot     = String.valueOf(HQ.getInt("harga_tot")).toString();
                    c += 20;
                    g2d.drawString(HQ.getRow() + "", 16, c);
                    g2d.drawString(NamaBahan + "", 40, c);
                    g2d.drawString(Qty, 220, c);
                    g2d.drawString(Satuan  + "", 260, c);
                    g2d.drawString("Rp. "+Harga_Satuan  + "", 370, c);
                    g2d.drawString(Harga_Tot  + "", 460, c);
                    JumlahHarga += Integer.valueOf(Harga_Tot).intValue();
                    
               }
                g2d.drawLine(290, c +10, 550, c +10);
                g2d.drawString("Jumlah Total :", 300, c + 30);
                g2d.drawString( JumlahHarga  + "", 460, c + 30);
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
           }
           
    //g2d.drawString(message, 0, 0);
    g2d.drawString(NamaPerusahaan, 10, 15);
    g2d.drawString(CreateDate, 10, 30);
    g2d.drawString(Judul, 10, 45);
    g2d.drawString("Trans No :", 300, 45);
    g2d.drawString(TransNo, 470, 45);
    g2d.drawString(NamaColum1, 15, 78);
    g2d.drawString(NamaColum2, 80, 78);
    g2d.drawString(NamaColum3, 220, 78);
    g2d.drawString(NamaColum4, 260, 78);
    g2d.drawString(NamaColum5, 370, 78);
    g2d.drawString(NamaColum6, 460, 78);
    //g2d.drawString(NamaColum7, 360, 78);
    
    /*
     * Garis Horisontal
     */
    g2d.setColor(Color.DARK_GRAY);
    g2d.drawLine(10, 60, 550, 60);
    g2d.drawLine(10, 85, 550, 85);
 
    
    /*
     * garis vertikal
     */
    g2d.drawLine(35, 60, 35, c + 10);
    
  }
  
 
}
