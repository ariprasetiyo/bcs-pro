/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

/**
 *
 * @author LANTAI3
 */
public class NoUrut {
    
     /*
     * Untuk menambah no urutu di table
     */
    public String no = "1";  
    public String GetNoUrut (){
        return no;
    }
    public void SetNoUrut ( String Data){
        int Data2 = Integer.valueOf(Data).intValue();
        Data2 = Data2 + 1;
        Data = String.valueOf(Data2).toString();
        this.no = Data;
    }
}
