/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

/**
 *
 * @author LANTAI3
 */
public class ResepObject {
   
    private String NamaResep;
    private String PorsiResep; 
    private String TanggalBuat;
    public ResepObject(){
        
    }
    
    public String GetVRNamaResep(){
        return NamaResep;
    }
    
    public void SetVRNamaResep (String Data){
        this.NamaResep = Data;
    }
    
    public String GetVRPorsiResep(){
        return PorsiResep;
    }
    public void SetVRPorsiResep ( String Data){
        this.PorsiResep = Data;
    }
    public String GetVRTanggalBuat(){
        return TanggalBuat;
    }
    public void SetVRTanggalBuat ( String Data){
        this.TanggalBuat = Data;
    }
    
}
