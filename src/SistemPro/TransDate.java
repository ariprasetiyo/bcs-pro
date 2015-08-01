/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;


/**
 *
 * @author LANTAI3
 */
public class TransDate {
    private int DataTanggal;
    private String Thn;
    private String Bln;
    private String Tg;
    
    public int GetTransDate(){
        return DataTanggal;
    }
    private String GetRbhTahun (){
        return Thn;
    }
    private String GetRbhBulan(){
        return Bln;
    }
    private String GetRbhTanggal(){
        return Tg;
    }
    public void SetTransDate (String Date){
        String RegexBln     = "/([0-9])?([0-9])?/[0-9][0-9]";
        String RegexTgl1    = "/([0-9])?([0-9])/";
        String RegexTgl2    = "/[0-9][0-9]";
        String RegexThn     = "([0-9])?([0-9])?/([0-9])?([0-9])?/";
      
        String Bulan        = Date.replaceAll(RegexBln, "");
        String Akali        = "/"+Date;
        String Tanggal1     = Akali.replaceAll(RegexTgl1, "");
        String Tanggal      = Tanggal1.replaceAll(RegexTgl2, "");
        String Tahun        = Date.replaceAll(RegexThn, "");
        
        if ( Integer.valueOf(Tahun).intValue() <= 19) {
            this.Thn    = "20" + Tahun;
        }
        else {
            this.Thn    = Tahun;
        }
        if (Integer.valueOf(Bulan).intValue() <= 9){
            this.Bln    = "0" + Bulan;
        }
        else {
            this.Bln    = Bulan;
        }
        if (Integer.valueOf(Tanggal).intValue() <= 9){
            this.Tg     = "0" + Tanggal;
        }
        else {
            this.Tg     = Tanggal;
        }
        String GabDate  = GetRbhTahun() + GetRbhBulan() + GetRbhTanggal();
        this.DataTanggal    = Integer.valueOf(GabDate).intValue();
        
    }
}

        
        /*
        TanggalSistem TglSis = new TanggalSistem();
        TglSis.SetTahunSis();
        TglSis.SetBulanSis();
        TglSis.SetTanggalSis();
        
        int SisTahun     = TglSis.GetTahunSis();
        int SisBulan     = TglSis.GetBulanSis();
        int SisTanggal   = TglSis.GetTanggalSis();
        JOptionPane.showMessageDialog(null, "Tahun = " + SisTahun + " bulan = " + SisBulan + " tanggal =" + SisTanggal);
        */
        