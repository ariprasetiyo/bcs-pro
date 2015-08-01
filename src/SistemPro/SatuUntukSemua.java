/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LANTAI3
 */
public class SatuUntukSemua {
    public SatuUntukSemua(){
        
    }
    public void ResetTampilan(JTextField[] Field){
        for (int a = 0; a < Field.length; a++){
            Field[a].setText("");
        }
    }
    public void ResetTampilan(JTextField[] Field, JLabel[] Label){
        for (int a = 0; a < Field.length; a++){
            Field[a].setText("");
        }
        for (int a = 0; a < Label.length; a++){
            Label[a].setText("");
        }
    }
    
    public void HapusDataJTabel(JTable Data){
        /*
         * Logika hapus semua data di jtable
         */
        DefaultTableModel dtm = (DefaultTableModel) Data.getModel();
        dtm.setRowCount(0); 
     }
    
    public void DeleteData(String Data, String Table){       
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from "+ Table+ " where trans_no = '"+ Data + "'");
        //DeleteGambar (Data);       
     } 
    
    /*
     * Untuk Cek Data Kosong
     */
    public void ValidasiData(JTextField[] Data, String DataPesanError[], String[] DiRubahKe){
        if (Data.length == DataPesanError.length){
            for (int a = 0; a < Data.length; a++){
                if (Data[a].getText().equals("")){
                     JOptionPane.showMessageDialog(null, "Data error : " + DataPesanError[a] );
                     Data[a].setText(DiRubahKe[a]);
                     continue;
                }
            }           
        }
        else {
            System.out.println("tidak sama data length");
            System.exit(1);
        }
    }
    public boolean ValidasiData(JTextField[] Data, String DataPesanError[]){
        if (Data.length == DataPesanError.length){
            for (int a = 0; a < Data.length; a++){
                if (Data[a].getText().equals("")){
                     JOptionPane.showMessageDialog(null, "Data error : " + DataPesanError[a] );
                     return false;
                }
            }           
        }
        else {
            System.out.println("tidak sama data length");
            System.exit(1);
        }
        return true;
    }
    
    /*
     * Untuk Transaksi misal WR921921 --  Nama
     */
     public String FilterNamaTransaksiSaja(String Data, JLabel Keterangan){
        try {
            String PartRegex = "(.*)?(\\s)*(--)+(\\s)*";
            Data   = Data.replaceAll( PartRegex, "");
            return Data;
        }
        catch (Exception  x){
            Keterangan.setText("Tidak Bisa Di Filter");
        }
        return "xxx";   
    }
    
    
    public String FilterNamaResepSaja(String Data, JLabel Keterangan){
        try {
            String PartRegex = "(\\s)*(--)+(\\s)*(.*)?";
            Data   = Data.replaceAll( PartRegex, "");
            return Data;
        }
        catch (Exception  x){
            Keterangan.setText("Tidak Bisa Di Filter");
        }
        return "xxx";   
    }
    public String FilterTransNoSaja(String Data, JLabel Keterangan){
        try {
            String PartRegex = ".*(\\s)*(--)+(\\s)";
            Data   = Data.replaceAll( PartRegex, "");
            return Data;
        }
        catch (Exception  x){
            Keterangan.setText("Tidak Bisa Di Filter");
        }
        return "xxx";   
    }
    /*
     * Yang di atas asalah semua
     */
    public String AmbilBelakang(String Data, JLabel Keterangan){
        try {
            String PartRegex = ".*(\\s)(--)+(\\s)";
            Data   = Data.replaceAll( PartRegex, "");
            return Data;
        }
        catch (Exception  x){
            Keterangan.setText("Tidak Bisa Di Filter");
        }
        return "xxx";   
    }
    public String AmbilDepan(String Data, JLabel Keterangan){
        try {
            String PartRegex = "(\\s)(--)+(\\s).*";
            Data   = Data.replaceAll( PartRegex, "");
            return Data;
        }
        catch (Exception  x){
            Keterangan.setText("Tidak Bisa Di Filter");
        }
        return "xxx";   
    }
     
    
    /*
     * Merubah String menjadi Data double dan Integer
     */
    public double BersihDataKeDoubel(String Data){
        if (Data.equals("null") || Data.equals("") ){
            Data = "0";
        }
        
        Data = Data.replaceAll("\\s.*", "");
        return Double.valueOf(Data).doubleValue();
    }
    public int BersihDataKeInt (String Data){
        if (Data.equals("null") || Data.equals("") ){
            Data = "0";
        }
        
        Data = Data.replaceAll("\\s.*", "");
        return Integer.valueOf(Data).intValue();
    }
    
    /*
     * Logika tutup dan buka tombol
     */
    public void LogikaComponent(Component[]  Field, boolean[] TutupTidak){
        for (int a = 0; a < Field.length; a++){
            Field[a].setEnabled(TutupTidak[a]);
            continue;
        }
    }
    
}
