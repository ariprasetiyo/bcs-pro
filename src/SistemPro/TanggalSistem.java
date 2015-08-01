/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

/**
 *
 * @author LANTAI3
 * Jika Convert tanggal dari kazao atau datechooose
 * Maka format harus bulan/tangal/tahun
 */
public class TanggalSistem {
            int TanggalSis;
            int BulanSis;
            int TahunSis;
            int TglIndo;
            int BlnIndo;
            int ThnIndo;
            int TglSql;
            int BlnSql;
            int ThnSql;
            int IntDateTgl;
            int IntDateBln;
            int IntDateTahun;
            
            String StrDateTgl;
            String StrDateBln;
            String StrDateThn;
            String StrTglIndo;
            String StrBlnIndo;
            String StrThnIndo;
            String StrTglSql;
            String StrBlnSql;
            String StrThnSql;
            String StrTglSistem;
            String StrBlnSistem;
            String StrThnSistem;
            
            
            java.util.Date utilDate = new java.util.Date();
            java.sql.Date SqlDate1 = new java.sql.Date(utilDate.getTime());
            String SqlDate = SqlDate1.toString();
                        
            public int GetTanggalSis (){
                return TanggalSis;
            }
            public int GetBulanSis (){
                return BulanSis;
            }
            public int GetTahunSis(){
                return TahunSis;
            }
            
            public String GetTanggalSisString (){
                return StrTglSistem ;
            }
            public String GetBulanSisString (){
                return StrBlnSistem;
            }
            public String GetTahunSisString(){
                return StrThnSistem;
            }
            
            public int GetTglIndoIntKazao(){
                return TglIndo;
            }
            public int GetBulIndoIntKaza0 (){
                return BlnIndo;
            }
            public int GetThnIndoIntKazao (){
                return ThnIndo;
            }
            
            public String GetTglIndoStrKazao (){
                return StrTglIndo;
            }
            public String GetBlnIndoStrKazao(){
                return StrBlnIndo;
            }
            public String GetThnIndoStKazao(){
                return StrThnIndo;
            }
            
            public int GetTglIndoIntSql(){
                return TglSql;
            }
            public int GetBulIndoIntSql (){
                return BlnSql;
            }
            public int GetThnIndoIntSql (){
                return ThnSql;
            }
            
            public String GetTglIndoStrSql (){
                return StrTglSql;
            }
            public String GetBlnIndoStrSql(){
                return StrBlnSql;
            }
            public String GetThnIndoStrSql(){
                return StrThnSql;
            }
            
            /*
             * DateTanggalAll
             */
            public int GetTanggalIntDate(){
                return IntDateTgl;
            }
            public int GetBulanIntDate() {
                return IntDateBln;
            }
            public int GetTahunIntDate(){
                return IntDateTahun;
            }
            public  String GetTanggalStrDate(){
                return StrDateTgl;
            }
            public String GetBulanStrDate() {
                return StrDateBln;
            }
            public String GetTahunStrDate(){
                return StrDateThn;
            }
            
            /*
             * Langsung pangil dan dapatkan nilainya
             */
            public int GetTglNow(){
                SetTanggalSis ();
                return GetTanggalSis ();
            }
            public int GetBlnNow(){
                SetBulanSis ();
                return GetBulanSis ();
            }
            public int GetThnNow(){
                SetTahunSis ();
                return GetTahunSis ();
            }
            
            
            public void SetTanggalSis (){
                String RegexTanggal = "[2-9][0-9][0-9][0-9]-[0-9][0-9]-";
                String DataTanggalSis = SqlDate.replaceAll(RegexTanggal, ""); 
                this.StrTglSistem = DataTanggalSis;
                int Tanggal = Integer.valueOf(DataTanggalSis).intValue();
                this.TanggalSis = Tanggal;
            }
            public void SetBulanSis (){
                String RegexBulan1 = "[2-9][0-9][0-9][0-9]-";
                String RegexBulan2 = "-[0-9][0-9]";
                String DataBulan1Sis = SqlDate.replaceAll(RegexBulan1, "");
                String DataBulanSis = DataBulan1Sis.replaceAll(RegexBulan2, "");
                this.StrBlnSistem = DataBulanSis;
                int Bulan = Integer.valueOf(DataBulanSis).intValue();
                this.BulanSis = Bulan;
            }
            public void SetTahunSis (){
                String RegexTahun = "-[0-9][0-9]-[0-9][0-9]";
                String DataTahunSis = SqlDate.replaceAll(RegexTahun, "");
                this.StrThnSistem = DataTahunSis;
                int Thn = Integer.valueOf(DataTahunSis).intValue();
                this.TahunSis = Thn;
            }
            
            /*
             * converter tanggal kazao ke indonesia
             * format TanggalPo.getKazaoCalendar().getShortDate();
             * untuk tanggal bulan dan tahun sama formatnya
             */
            public void SetKazaoToTglIndo (String Data){
                String RegexTgl1 = "/[0-9][0-9][0-9][0-9]";
                String RegexTgl2 = "[0-9][0-9]/";
                Data = Data.replaceAll(RegexTgl1,"");
                String Data2 = Data.replaceAll(RegexTgl2, "");
                this.StrTglIndo = Data2;
                this.TglIndo  = Integer.valueOf(Data2).intValue();
            }
            public void SetKazaoToBlnIndo ( String Data){
                String RegexBln = "/[0-9][0-9]/[0-9][0-9][0-9][0-9]";
                Data = Data.replaceAll(RegexBln, "");
                this.StrBlnIndo = Data;
                this.BlnIndo = Integer.valueOf(Data).intValue();    
            }
            public void SetKazaoToThnIndo ( String Data){
                String RegexThn = "[0-9][0-9]/[0-9][0-9]/";
                Data = Data.replaceAll(RegexThn, "");
                this.StrThnIndo = Data;
                this.ThnIndo = Integer.valueOf(Data).intValue();
            } 
            
            /*
             * Convert seluruh tanggal
             * Digunakan untuk set seluruh tanggal di convert ke tangal, bulan, tahun
             */
            public void SetDate( String Data){
                String RTanggal = "[0-9][0-9][0-9][0-9]-[0-9][0-9]-";
                String RBulan1 = "[0-9][0-9][0-9][0-9]-";
                String RBulan2 = "-[0-9][0-9]";
                String RTahun = "-[0-9][0-9]-[0-9][0-9]";
                
                String Tgl = Data.replaceAll(RTanggal, "");
                String B1 = Data.replaceAll(RBulan1, "");
                String B2 = B1.replaceAll(RBulan2, "");
                String Thn = Data.replaceAll(RTahun, "");
                
                this.StrDateTgl = Tgl;
                this.StrDateBln = B2;
                this.StrDateThn = Thn;
                
                this.IntDateTgl = Integer.valueOf(Tgl).intValue();
                this.IntDateBln = Integer.valueOf(B2).intValue();
                this.IntDateTahun = Integer.valueOf(Thn).intValue();
                
            }
            
            /*
             * Converter Sql date to date indonesia
             */
            public void SetSqlToTglIndo (String Data){
                String RegexTanggal = "[0-9][0-9][0-9][0-9]-[0-9][0-9]-";
                Data = Data.replaceAll(RegexTanggal, "");   
                this.StrTglSql = Data;
                this.TglSql = Integer.valueOf(Data).intValue();
            }
            public void SetSqlToBlnIndo ( String Data){
                String RegexBln1 = "[0-9][0-9][0-9][0-9]-";
                String RegexBln2 = "-[0-9][0-9]";
                Data = Data.replaceAll(RegexBln1, "");
                String Data2 = Data.replaceAll(RegexBln2, "");
                this.StrBlnSql = Data2;  
                this.BlnSql = Integer.valueOf(Data2).intValue();
                
            }
            public void SetSqlToThnIndo ( String Data){
                String RegexThn = "-[0-9][0-9]-[0-9][0-9]";
                Data = Data.replaceAll(RegexThn, "");
                this.StrThnSql = Data;
                this.ThnSql = Integer.valueOf(Data).intValue();
            } 
            
            /*
             * Convert tanggal dengan format tgl-bln-thn di pecah persatuan
             */
            public int ConvertTglBlnThnToTanggal (String TglBulanTahun){
                TglBulanTahun = TglBulanTahun.replaceAll("-[0-9][0-9]-[0-9][0-9][0-9][0-9]", "");
                return Integer.valueOf(TglBulanTahun).intValue();
            }
            public int ConvertTglBlnThnToBulan (String TglBulanTahun){;
                String RegexHapusThn    = "-[0-9][0-9][0-9][0-9]";
                String RegexHapusTgl    = "[0-9][0-9]-";
                String TglBul           = TglBulanTahun.replaceAll(RegexHapusThn, "");
                String Data             = TglBul.replaceAll(RegexHapusTgl, "");
                return Integer.valueOf(Data).intValue();
            }
            public int ConvertTglBlnThnToTahun (String TglBulanTahun){
                TglBulanTahun   = TglBulanTahun.replaceAll("[0-9][0-9]-[0-9][0-9]-", "");
                return Integer.valueOf(TglBulanTahun).intValue();
            }
            
            /*
             * Convert tangal datechooser to pecah dari bulan/tahun
             * Menjadi 
             */
            
            
            /*
             * Convert dari date compuser yg dari database. format tgl/bln/thun 01/12/14
             */
            
            public int DecomposerDateTanggal(){
                return TanggalSis;
            }
            public int DecomposerDateTahun(){
                return TahunSis;
            }
            public int DecomposerDateBulan(){
                return BulanSis;
            }
            
            public void SetDecomposerDate (String B){

                String RegexTanggal = "/[0-9][0-9]/[0-9][0-9][0-9][0-9]";
                String Tanggal = B.replaceAll(RegexTanggal, "");   
                this.TanggalSis = Integer.valueOf(Tanggal).intValue();

                String RegTahun     = "[0-9][0-9]/[0-9][0-9]/";
                String Tahun = B.replaceAll(RegTahun, "");   
                this.TahunSis = Integer.valueOf(Tahun).intValue();
                
                String RegBulan1    = "^([0-9][0-9]/)";
                String RegBulan2    = "(/[0-9][0-9][0-9][0-9])$";
                String Bulan        = B.replaceAll(RegBulan1, "");  
                Bulan               = Bulan.replaceAll(RegBulan2, "");
                this.BulanSis = Integer.valueOf(Bulan).intValue();
            }
            
            /*
             * Decomposer formt awal 31 12 2014
             */
            public void SetDecomposerDate (String B, int TglBulanTahun2014 ){

               System.out.println(B +  " s "  );
                String RegexTanggal = "/[0-9][0-9]/[0-9][0-9][0-9][0-9]";
                String Tanggal = B.replaceAll(RegexTanggal, "");   
                this.TanggalSis = Integer.valueOf(Tanggal).intValue();
             
                
                String RegTahun1     = "[0-9][0-9]/[0-9][0-9]/";
                String Tahun = B.replaceAll(RegTahun1, "");  
                String RegTahun2     = "^([0-9][0-9])";
                Tahun = Tahun.replaceAll(RegTahun2, "");
                this.TahunSis = Integer.valueOf(Tahun).intValue();
                
                String RegBulan1    = "^([0-9][0-9]/)";
                String RegBulan2    = "(/[0-9][0-9][0-9][0-9])$";
                String Bulan        = B.replaceAll(RegBulan1, "");  
                Bulan               = Bulan.replaceAll(RegBulan2, "");
                this.BulanSis = Integer.valueOf(Bulan).intValue();
                
                System.out.println(TanggalSis  + " s " + BulanSis + " s " +TahunSis );
            }
            
            /*
             * Convert tanggal dengan format tgl-bln-thn di pecah persatuan
             */
            public String ConvertTgl_Bln_Thn_To_Tanggal_String (String TglBulanTahun){
                TglBulanTahun = TglBulanTahun.replaceAll("-[0-9][0-9]-[0-9][0-9][0-9][0-9]", "");
                return TglBulanTahun;
            }
            public String ConvertTgl_Bln_Thn_To_Bulan_String (String TglBulanTahun){;
                String RegexHapusThn    = "-[0-9][0-9][0-9][0-9]";
                String RegexHapusTgl    = "[0-9][0-9]-";
                String TglBul           = TglBulanTahun.replaceAll(RegexHapusThn, "");
                String Data             = TglBul.replaceAll(RegexHapusTgl, "");
                return Data;
            }
            public String ConvertTgl_Bln_Thn_To_Tahun_String (String TglBulanTahun){
                TglBulanTahun   = TglBulanTahun.replaceAll("[0-9][0-9]-[0-9][0-9]-", "");
                return TglBulanTahun;
            }
}
