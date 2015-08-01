/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author LANTAI3
 */
public class koneksiMYSQL {
    KoneksiDatabase KD = new KoneksiDatabase();
    Connection K = KD.createConnection(); 
    
    public koneksiMYSQL(){
        
    }
    /*
     * Delete
     */
    public void MysqlDelete (String Query){
       try {
                Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                Stm.executeUpdate(Query);       
           }                
           catch (Exception X){ 
                    JOptionPane.showMessageDialog (null, "Tidak berhasil di save 2", "Error" , JOptionPane.ERROR_MESSAGE);
                    X.printStackTrace();
                }
    }
    
    /*
     * Update
     */
    public void MysqlUpdate (String QueryUpdateHeader,  String QueryDeleteDetail, String QueryInsertDetail, int JumlahListDetails){
         try {
           Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);           
           Stm.executeUpdate(QueryUpdateHeader);
           try {
                Statement StmDetail = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                Statement StmHapus = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                StmHapus.executeUpdate(QueryDeleteDetail);
                for (int a = 0; a <= JumlahListDetails; a++){                                       
                    StmDetail.executeUpdate(QueryInsertDetail); 
                }           
           }                
           catch (Exception X){ 
                    JOptionPane.showMessageDialog (null, "Tidak berhasil di update", "Error" , JOptionPane.ERROR_MESSAGE);
                    X.printStackTrace();
                }
        }
        catch (SQLException | HeadlessException X){ 
            JOptionPane.showMessageDialog (null, "Tidak berhasil di update", "Error" , JOptionPane.ERROR_MESSAGE);
            X.printStackTrace();
        } 
    }
    
    /*
     * Insert
     */
    public void MysqlInsert (String QueryHeader, String QueryDetail, int JumlahListDetails){
        try {
           Statement Stm = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           Stm.executeUpdate(QueryHeader);               
           try {
                Statement StmDetail = K.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                for (int a = 0; a <= JumlahListDetails; a++){                                       
                    StmDetail.executeUpdate(QueryDetail); 
                }           
           }                
           catch (Exception X){ 
                    JOptionPane.showMessageDialog (null, "Tidak berhasil di save 2", "Error" , JOptionPane.ERROR_MESSAGE);
                    X.printStackTrace();
                }
        }
        catch (SQLException | HeadlessException X){ 
            JOptionPane.showMessageDialog (null, "Tidak berhasil di save 1", "Error" , JOptionPane.ERROR_MESSAGE);
            X.printStackTrace();
        }
    }
}
