/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

import javax.swing.JOptionPane;

/**
 *
 * @author LANTAI3
 */
public class ValidasiInputResep {
    
    private boolean Benar;
    
    public ValidasiInputResep(){
        
    }
    
    /*
     * Jika bernilai salah maka program akan lanjut ketahap berikutnya
     */
    public boolean GetValidasiInputResep2(){
        return Benar;
    }
    
    public void SetValidasiInputResep2(String TitleResep, String QtyPosrsi){ 
         
       
        if ("".equals(QtyPosrsi) ) {
            QtyPosrsi = "1";
        }
        //QtyPosrsi = QtyPosrsi.replaceAll("[/*\\-\\(\\)<>_\\=+\\.,:\";\'\\\\#\\$&\\^\\}\\{%~`\\|\\[\\]\\!\\?\\@a-zA-Z]", "1");
        
        int QtyPorsi    = Integer.valueOf(QtyPosrsi.replaceAll("[/*\\-\\(\\)<>_\\=+\\.,:\";\'\\\\#\\$&\\^\\}\\{%~`\\|\\[\\]\\!\\?\\@a-zA-Z]", "1")).intValue();
        if (!"".equals(TitleResep) && QtyPorsi > 0 ){
            this.Benar = false;
            
        }
        else if ("".equals(TitleResep)){
            JOptionPane.showMessageDialog(null, "Nama resep tidak ada", "Perhatian",JOptionPane.ERROR_MESSAGE);           
            this.Benar = true;
        }
        else if (QtyPorsi < 1){
            JOptionPane.showMessageDialog(null, "Jumlah porsi salah", "Perhatian",JOptionPane.ERROR_MESSAGE);
            this.Benar = true;
        }
        else if ((TitleResep) == null ){
            JOptionPane.showMessageDialog(null, "Data Null", "Perhatian",JOptionPane.ERROR_MESSAGE);
            this.Benar = true;
        }           
        else{
            JOptionPane.showMessageDialog(null, "Mohon diulang kembali", "Perhatian",JOptionPane.ERROR_MESSAGE);
            this.Benar = true;
        }     
    } 
}
