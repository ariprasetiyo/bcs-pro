/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

/**
 *
 * @author LANTAI3
 */
public class PembelianObject {
    private String NamaResep;
    private String TransNo; 
    private String TanggalBuat;
    private String TotalBiaya;
    private String UangMuka;
    private String Supplier;
    
    public PembelianObject(){
        
    }
    
    public String GetPembelianNamaResep(){
        return NamaResep;
    }
    
    public void SetPembelianNamaResep (String Data){
        this.NamaResep = Data;
    }
    
    public String GetPembelianTransNo(){
        return TransNo;
    }
    public void SetPembelianTransNo ( String Data){
        this.TransNo = Data;
    }
    public String GetPembelianTanggalBuat(){
        return TanggalBuat;
    }
    public void SetPembelianTanggalBuat ( String Data){
        this.TanggalBuat = Data;
    }
    public String GetPembelianSupplier(){
        return Supplier;
    }
    public void SetPembelianSupplier ( String Data){
        this.Supplier = Data;
    }
    public String GetPembelianTotalBiaya(){
        return TotalBiaya;
    }
    public void SetPembelianTotalBiaya ( String Data){
        this.TotalBiaya = Data;
    }
    public String GetPembelianUangMuka(){
        return UangMuka;
    }
    public void SetPembelianUangMuka ( String Data){
        this.UangMuka = Data;
    }
    
}
