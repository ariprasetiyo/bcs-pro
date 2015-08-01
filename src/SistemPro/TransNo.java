/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author LANTAI3
 * cara penggunaan
 * buat tabel di database harus ada period ( 201301) dan key_no
 */
public class TransNo {
   
   
    
    public String TransNo;
    int Nourut;
    public int GetNoUrut (){
        //System.out.println(Nourut + "xxxxxxxxxxxxxxx");
        return Nourut;
    }
    public String GetTransNoPo (){
        return TransNo;
    }
    public void SetTransNoPo ( String JnsData, String SelectKolomTransNo, String SelectTabelPeriod, String FromTabel, String WhereSyarat){
        
        KoneksiDatabase KoneksiDb = new KoneksiDatabase();
        Connection koneksi = KoneksiDb.createConnection();
        ResultSet hasilquery = null;

            //JOptionPane.showMessageDialog(null,WhereSyarat+ " where syarat ");
            TanggalSistem ConvertTgl = new TanggalSistem();
            ConvertTgl.SetDate(WhereSyarat);
            String  Tahun   = ConvertTgl.GetTahunStrDate();
            String Tanggal  = String.valueOf(ConvertTgl.GetTanggalIntDate()).toString();
            String Bulan    = String.valueOf(ConvertTgl.GetBulanIntDate()).toString();
            
            //String RegexTransNo = JnsData+ConvertTgl.GetThnIndoStKazao()+String.valueOf(ConvertTgl.GetTglIndoIntKazao()).toString()+ConvertTgl.GetBlnIndoStrKazao();
            //JOptionPane.showMessageDialog(null,RegexTransNo2+ " RegexTransNo ");
            try {
                Statement stm = koneksi.createStatement();
                //
                //SELECT * FROM  tbheaderpo where date_po = '2013-06-12'  order by trans_no desc limit 0,1
                //select * from tbheaderpo where period = 'asas'  order by period desc LIMIT 0,1
                hasilquery = stm.executeQuery("SELECT "+SelectKolomTransNo+" , "+SelectTabelPeriod+" FROM "+ FromTabel
                        +" where "+ SelectTabelPeriod +" = '"+ WhereSyarat +"' order by "
                        + " key_no desc limit 0,1");
                //TN.SetTransNoPo("PO", "trans_no", "date_po", "tbheaderpo", GabTgl);
               // hasilquery = stm.executeQuery("SELECT * FROM  tbheaderpo where date_po = '2013-06-13'  order by trans_no desc limit 0,1");
                String HasilData = "0";
                int Tambah = 0;
                
                /*
                 * Convert tanggal sistem dari wheresyarat = kazaocalender
                 * format PO tahun-tanggal-bulan
                 */
                while(hasilquery.next()){

                    HasilData = hasilquery.getString(SelectKolomTransNo);
                    //JOptionPane.showMessageDialog(null,HasilData + " hasil data 1x ");
                    // ambil number dari trans_no filter
                    HasilData = HasilData.replaceAll(JnsData+WhereSyarat, "");
                    //JOptionPane.showMessageDialog(null,HasilData + " hasil data 2 ");
                    //System.out.println(HasilData + " xxx ");
                    Tambah = Integer.valueOf(HasilData).intValue();
                }
                Tambah = Tambah + 1;
                
                this.Nourut = Tambah;
                System.out.println(Tambah);
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(null, " TransNo.java Error : 3111 : "+ ex, "Error ", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            //System.out.print(JnsData+" "+Tahun+" "+Tanggal+" "+Bulan+" "+ String.valueOf(GetNoUrut()).toString());
      String Gab = JnsData+WhereSyarat+ String.valueOf(GetNoUrut()).toString();  
      this.TransNo = Gab;
    }
    
    /*
     * Dalam membuat transnolocal 
     * dibutuhkan dcompuser dengan format tanggal-bulan-tahun
     */
   public String TransNoLocal (String JudulTransaksi, JTable JTabelJournal, String  Dated){
       
       TanggalSistem N       = new TanggalSistem();
       
       int Row = JTabelJournal.getRowCount();
       Row = Row + 1;
       String Bulan  , Tahun, All; 
       int BulanInt, TahunInt;
       /*
        * Panggil Tanggal sekarang
        */
       N.SetTahunSis();
       N.SetBulanSis();
       
       Bulan        = N.ConvertTgl_Bln_Thn_To_Bulan_String(Dated);
       Tahun        = N.ConvertTgl_Bln_Thn_To_Tahun_String(Dated);
       
       BulanInt = Integer.valueOf(Bulan).intValue();
       TahunInt = Integer.valueOf(Tahun).intValue();
       
       /*
        * Bulan Computer
        */
       if ( ( N.GetBulanSis() == BulanInt ) && ( N.GetTahunSis() == TahunInt) ){
           return JudulTransaksi + Tahun + Bulan + ( Row + 1 );
       }
       else {
           JOptionPane.showMessageDialog(null, "Bulan dan tahun harus sama dengan sekarang");
       }
       return "xxx";
       
   }
}
