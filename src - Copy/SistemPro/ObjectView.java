/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

/**
 *
 * @author LANTAI3
 */
public class ObjectView {
   
    private String NamaResep;
    private String PorsiResep; 
    private String TanggalBuat, AA,BB,CC,DD,EE,FF,GG, HH;
    public ObjectView(){
        
    }
    
    public String GetA(){
        return NamaResep;
    }
    
    public void SetA (String Data){
        this.NamaResep = Data;
    }
    
    public String GetB(){
        return PorsiResep;
    }
    public void SetB ( String Data){
        this.PorsiResep = Data;
    }
    public String GetC(){
        return TanggalBuat;
    }
    public void SetC ( String Data){
        this.TanggalBuat = Data;
    }
    
    public String GetD(){
        return DD;
    }
    
    public void SetD (String Data){
        this.DD = Data;
    }
    
    public String GetE(){
        return EE;
    }
    
    public void SetE (String Data){
        this.EE = Data;
    }
    
    public String GetF(){
        return FF;
    }
    
    public void SetF (String Data){
        this.FF = Data;
    }
    
    public String GetG(){
        return GG;
    }
    
    public void SetG (String Data){
        this.GG = Data;
    }
    public String GetH(){
        return HH;
    }
    
    public void SetH (String Data){
        this.HH = Data;
    }
}
