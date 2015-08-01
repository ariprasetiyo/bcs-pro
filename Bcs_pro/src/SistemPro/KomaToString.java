/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author LANTAI3
 */
public class KomaToString {
    String DataString;
    String DataKoma;
    String KomaStr;
    int DataInt;
    
    
    public String GetString (){
        return DataString;
    }
    public int GetIntTanpaKoma(){
        return DataInt;
    }
    public String GetKomaString(){
        return DataKoma;
    }
    public String GetKomaString2(){
        return KomaStr;
    }

    
    
    public void SetHapusKoma( String Data ){                
        //Merubah dari ada koma menjadi tidak koma dalam bentuk string untuk menghitung TotPrice dan memperoleh TotPriceDouble
        
        String RubahData    = Data.replaceAll(",", "");
        String RubahData2   = RubahData.replaceAll("\\.", "");
        this.DataString     = RubahData2;
        System.out.println(RubahData2);
        try{
            this.DataInt        = Integer.valueOf(RubahData2).intValue();
        }
        catch(Exception X){
            
        }

    }
    public void SetKomaDariStr ( String Data){
        java.text.DecimalFormat decimalFormat2 = new java.text.DecimalFormat("#,##0");
        String Data2 = String.valueOf(decimalFormat2.format(Data)).toString();
        this.KomaStr = Data2;
        //this.KomaInt = Integer.valueOf(Data2).intValue();
    }
    
    /*
     * Konsep
     * Digunakan pada waktu format bilangan 1.000,001 
     * Akan dirubah menjadi 1000.001
     */  
    public String RupiahKeDoubel (String Data){
        
         String A = String.format("%.2f", NilaiDataDouble2 (Data));
         return A.replaceAll(",", ".");
    }
    
    /*
     * Jumlah Koma bisa customize
     */
    public String RupiahKeDoubel (String Data, int JumlahKoma){
         double value =  NilaiDataDouble2(Data);
         String A = String.format("%."+ JumlahKoma+"f", value);
         NumberFormat format = NumberFormat.getNumberInstance();
         format.setMaximumFractionDigits(JumlahKoma);
         format.setGroupingUsed(false);
         A = format.format(value);
         A.replaceAll(",", ".");
         return A;
        
         //return A.replaceAll(",", ".");
    }
    
    public double NilaiDataDouble2 (String Data){
        if (Data.equals("")){
            Data= "0";
        }
         Data   = HapusTitik(Data);
         String RubahData    = Data.replaceAll(",", ".");
         return Double.valueOf(RubahData).doubleValue();
    }
    
    public String HapusTitik(String Data){
        String RubahData    = Data.replaceAll("\\.", "");
        return RubahData;
    }
    
    /*
     * Konsep rubah dari  1000.001 menjadi 1.000,001
     * Split = dipisah contoh
            String string = "004-034556";
            String[] parts = string.split("-");
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556
     * apend = ditambah di belakang
     * substring = dipotongan beberapa indek
     */
    
   public String NilaiRupiah(String Data){
        String RubahData    = Data.replaceAll("\\..*", "");
        String Data2        = Data.replaceAll(".*\\.", "");
        String formatRp=String.valueOf(RubahData);
        formatRp = nilaiBerkebalikan(formatRp);
        formatRp = sisipkanTitik(formatRp);
        formatRp = nilaiBerkebalikan(formatRp);
        if (Data.matches(".*\\..*")){
             return formatRp + "," + Data2;
        }
        else {
             return formatRp;
        }
    }
   public String NilaiRupiah(double Data3){
        String Data = String.valueOf(Data3).toString();
        String RubahData    = Data.replaceAll("\\..*", "");
        String Data2        = Data.replaceAll(".*\\.", "");
        String formatRp=String.valueOf(RubahData);
        formatRp = nilaiBerkebalikan(formatRp);
        formatRp = sisipkanTitik(formatRp);
        formatRp = nilaiBerkebalikan(formatRp);
        if (Data.matches(".*\\..*")){
             return formatRp + "," + Data2;
        }
        else {
             return formatRp;
        }
    }
    
    private static String nilaiBerkebalikan(String format) {
        String[] str = format.split("");
        format = "";
        for(int n=str.length-1; n>0; n--){
            format = format.concat(str[n]);
        }
        return format;
    }
 
    private static String sisipkanTitik(String format) {
        String[] str = format.split("");
        format = "";
        for(int n=1; n<str.length; n++){
        format = format.concat(str[n]);
            if(n%3==0){
                format = format.concat(".");
             }
        }
        if (format.matches(".*[\\.]$")){
           format = format.replaceAll("[\\.]$", "");
        }
        return format;
    }
 
    /*
     * Rounding desimal
     */    
    public String RoundingDesimal(double Data){
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
    public double RoundingDesimal (double Data, int a){
        DecimalFormat numberFormat = new DecimalFormat("##.00"); //#.##
        String Data2;
        Data2             = numberFormat.format(Data);
        if (Data2.contains(",")){
             return Double.valueOf(Data2.replaceAll(",", ".")).doubleValue();
        }       
       return Double.valueOf(Data2).doubleValue();
    }
    
    /*
     * Stand Alone merubah format yang masin 1.1E2 atau 1.1E-2 menjadi normal
     */
    public String FormatDesimalRubahE9 (double a){
        double value = a;
        String text = String.format("%.10f", value);
        //System.out.println(text); // 9012367890
        
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(10);
        format.setGroupingUsed(false);
        return format.format(value);
    }
    public String FormatDesimalRubahE9 (String b){
        double a =Double.valueOf(b).doubleValue();
        double value = a;
        String text = String.format("%.10f", value);
        //System.out.println(text); // 9012367890
        
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(10);
        format.setGroupingUsed(false);
        return format.format(value);
    }
    
    
}
