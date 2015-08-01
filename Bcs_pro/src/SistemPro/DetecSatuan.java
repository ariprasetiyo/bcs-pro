/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author LANTAI3
 */
public class DetecSatuan {
    
        String AA,BB,CC,DD, EE;
        public DetecSatuan(){
            
        }
        
        /*
         * Tidak dipake
         */
        public void DetectSatuan4(String satuan, JComboBox JComboBoxSatuan ){
         /*
          * indek satuan 
           Kg ( Kilogram ),g ( Gram ),Sdm ( Sendok Makan ),Sdt ( Sendok Teh),L ( Litter ),Ml ( Mili Liter ),Bungkus,Biji,Buah,Ruas,Kemasan,Ikat,Batang,Lembar 13
          * Kg ( Kilogram )0
            g ( Gram )1
            Sdm ( Sendok Makan )2
            Sdt ( Sendok Teh)3
            L ( Litter )4
            Ml ( Mili Liter )5
            Bungkus 6
            Biji7
            Buah 8
            Ruas 9
            Kemasan110
            Ikat11
            Batang12
            Lembar 13
          */
        try {
            satuan = satuan.toLowerCase();
            satuan = satuan.replaceAll("\\s\\(.*", "");
            satuan = satuan.replaceAll("^[\\s]", ""); 
            boolean X = false;
            switch ( satuan ) {
                case "kg" :              
                    JComboBoxSatuan.setSelectedIndex(0);
                    X = true;
                 break;
                 case "g" : 
                    JComboBoxSatuan.setSelectedIndex(1);
                    X = true;
                     break;
                 case "sdm" :
                    JComboBoxSatuan.setSelectedIndex(2);
                    X = false;
                 break; 
                 case "sdt" :
                    JComboBoxSatuan.setSelectedIndex(3);
                    X = false;
                 break; 
                 case "l" :
                    JComboBoxSatuan.setSelectedIndex(4);
                    X = true;
                 break;
                   case "ml" :
                    JComboBoxSatuan.setSelectedIndex(5);
                    X = true;
                 break; 
                    case "bungkus" :
                    JComboBoxSatuan.setSelectedIndex(6);
                    X = false;
                 break; 
                    case "biji" :
                    JComboBoxSatuan.setSelectedIndex(7);
                    X = false;
                 break; 
                    case "buah" :
                    JComboBoxSatuan.setSelectedIndex(8);
                    X = false;
                 break; 
                    case "ruas" :
                    JComboBoxSatuan.setSelectedIndex(9);
                    X = false;
                 break; 
                    case "kemasan" :
                    JComboBoxSatuan.setSelectedIndex(10);
                    X = false;
                 break; 
                    case "ikat" :
                    JComboBoxSatuan.setSelectedIndex(11);
                    X = false;
                 break; 
                    case "batang" :
                    JComboBoxSatuan.setSelectedIndex(12);
                    X = false;
                 break;
                   case "lembar" :
                    JComboBoxSatuan.setSelectedIndex(13);
                    X = false;
                 break; 
                 default :
                     JOptionPane.showMessageDialog(null, "Satuan tidak tersedia di program");
                     X = false;
                 break;
           }
            JComboBoxSatuan.setEnabled(X);
            
        }
        catch(Exception X){
            
        }
         
    }
        
        /*
         * Mendeteksi apakah item itu bisa dikonvert atau tidak 
         * Settingan pada program
         */
        public  HashMap hashMap = new HashMap();
        public boolean  DetectConvert (String Item, Connection K){   
            try {
            Item = Item.replaceAll(".*--", "");  
            Item = Item.replaceAll("^[\\s]", "");  
            }
            catch (Exception X){}
            
           ResultSet HQ = null;
           try {
               Statement stm = K.createStatement();
               HQ = stm.executeQuery("SELECT *  from master_barang_convert_satuan where item_name ='"+Item+"'");              
               while(HQ.next()  ){
                   hashMap.put("qty tdk baku", HQ.getString("qty_tdk_baku"));
                   hashMap.put("satuan tdk baku", HQ.getString("satuan_tdk_baku"));
                   hashMap.put("qty baku", HQ.getString("qty_baku"));
                   hashMap.put("satuan baku", HQ.getString("satuan_baku"));
                   return true;
               }
           }
           catch (Exception ex){
                JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                return false;
           }
           return false;          
        }
        
        /*
         * Satu paket dengan atasnya
         * Difungsikan untuk kondisi item yang ada di master_barang_convert_satuan
         */
        public void IsiSatuanPadaJComboBox(JComboBox x, HashMap Map){
            String Satuan = (String)Map.get("satuan baku");
            x.removeAllItems();
            if (Satuan.equalsIgnoreCase("KG") ||Satuan.equalsIgnoreCase("G") ){
                x.addItem("KG");
                x.addItem("G");
                x.addItem( Map.get("satuan tdk baku"));
            }
            else if (Satuan.equalsIgnoreCase("L") || Satuan.equalsIgnoreCase("ML")){
                x.addItem("L");
                x.addItem("ML");
                x.addItem( Map.get("satuan tdk baku"));
            }
            else {
                JOptionPane.showMessageDialog(null  , "Error satuan");
            }
             x.setSelectedItem(Satuan);
        }
        /*
         * Satu paket dengan atasnya
         * Difungsikan untuk kondisi item yang tidak ada di master_barang_convert_satuan
         */
         public void IsiSatuanPadaJComboBox(JComboBox x, String Satuan){
            x.removeAllItems();
            try{
                if (Satuan.equalsIgnoreCase("KG")  || Satuan.equalsIgnoreCase("G")){
                x.addItem("KG");
                x.addItem("G");              
                }
                else if (Satuan.equalsIgnoreCase("L") || Satuan.equalsIgnoreCase("ML") ){
                    x.addItem("L");
                    x.addItem("ML");
                }
                else {
                    x.addItem(Satuan);
                }
                x.setSelectedItem(Satuan);

                }
                catch(Exception X){               
            }          
        }
    
   /*
    * Program softcode 
    * Untuk convert data dari data yang sudah ditentukan oleh user
    * dari data tidak baku menjadi baku
    * DetectIndex = 2 ( Satuan tidak baku Akan di convert ) 
    * 
    */
   public double ConvertSatuan(JComboBox x, JTextField y,HashMap hashMap, int DetectIndex){
        SistemPro.KomaToString ConvertAngka = new SistemPro.KomaToString();
        int a = x.getItemCount();
        double Perbadingan = 0, Perbadingan2 = 0;
        
        /*
         * Data dari program
         */
        Perbadingan = Double.valueOf(ConvertAngka.RupiahKeDoubel (y.getText())).doubleValue();

        if (a < 2){
            return ( (double) Math.round(Perbadingan * 100) / 100 );
        }
        else if (a <= 3) {
            
            /*
             * Fungsi convert tidak baku ke baku
             */
            CC = (String ) hashMap.get("qty tdk baku");
            DD = (String ) hashMap.get("qty baku");
            AA = (String ) hashMap.get("satuan baku");
            BB = (String ) hashMap.get("satuan tdk baku");
            
            //JOptionPane.showMessageDialog(null, a + " xxx " + BB + " " + AA + " " + x.getSelectedIndex() + " " + DetectIndex );
            if (DetectIndex == 2){
                JOptionPane.showMessageDialog(null, "Convert " + BB + " ke " + AA 
                        + "\n" + "Dengan ketentuan : \n"
                        + " => "+CC+ " " + BB + " = " + DD + " " + AA );
                
                /*
                 * Data dari database
                 */
                Perbadingan2 = Double.valueOf(CC).doubleValue() / Double.valueOf(DD).doubleValue();
                Perbadingan = Perbadingan / Perbadingan2;
                x.setSelectedItem(AA);
                return ( (double) Math.round(Perbadingan * 100) / 100 );
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Error satuan lebih dari 3");
        }
        return ( (double) Math.round(Perbadingan * 100) / 100 );
    }
        
        /*
         * Tidak dipake
         */
        public void ValidasiSatuan1 (String satuan, JComboBox JComboBoxResepSatuan){
         satuan = satuan.toLowerCase();
         satuan = satuan.replaceAll("\\s\\(.*", "");
         satuan = satuan.replaceAll("^[\\s]", ""); 

         switch ( satuan ) {
              case "kg" : 
              break;
              case "g" : 
              break;
              case "l" :
              break;
              case "ml" :
              break;
              default :
                 JOptionPane.showMessageDialog(null, "Tidak bisa dipilih");
                 JComboBoxResepSatuan.setSelectedIndex(0);
              break;
        }
    }
        
        /*
         * Tidak dipake
         */
    public boolean ValidasiSatuan3(String satuan){
        satuan = satuan.toLowerCase();
        satuan = satuan.replaceAll("\\s\\(.*", "");
        satuan = satuan.replaceAll("^[\\s]", ""); 
        boolean x;
        switch ( satuan ) {
             case "kg" :              
                 x = true;
              break;
              case "g" : 
                 x = true;
                  break;
              case "sdm" :
                  x = false;
              break; 
              case "sdt" :
                  x = false;
              break; 
              case "l" :
                  x = true;
              break;
                case "ml" :
                  x = true;
              break; 
                 case "bungkus" :
                  x = false;
              break; 
                 case "biji" :
               
                  x = false;
              break; 
                 case "buah" :
                 
                  x = false;
              break; 
                 case "ruas" :
                
                  x = false;
              break; 
                 case "kemasan" :
                 
                  x = false;
              break; 
                 case "ikat" :
                 
                  x = false;
              break; 
                 case "batang" :
           
                 x = false;
              break;
                case "lembar" :
                  x = false;
              break; 
              default :
                  JOptionPane.showMessageDialog(null, "Satuan tidak tersedia di program");
                   x = false;
              break;
        }
        return x;
    }
    
    /*
    public void AksiAksi(final JComboBox JComboBoxResepSatuan){
        JComboBoxResepSatuan.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                SistemPro.DetecSatuan SatuanSelect = new SistemPro.DetecSatuan();
                SatuanSelect.ValidasiSatuan ((String)JComboBoxResepSatuan.getSelectedItem(), JComboBoxResepSatuan);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
            
        });
        
        JComboBoxResepSatuan.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                SistemPro.DetecSatuan SatuanSelect = new SistemPro.DetecSatuan();
                SatuanSelect.ValidasiSatuan ((String)JComboBoxResepSatuan.getSelectedItem(),JComboBoxResepSatuan );
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
            
        });
    } */

}
