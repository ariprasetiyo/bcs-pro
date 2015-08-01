/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author LANTAI3
 */
public class ComponentHanyaAngka {
    
    public ComponentHanyaAngka(){
        
    }
    public void  SetAntiAngka(JTextField x){
        x.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e)
                {
                  char c = e.getKeyChar();
                  if (!((c >= '0') && (c <= '9') ||    
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE))) {
                        //getToolkit().beep();
                        e.consume();
                  }
                }
        });
    }
    
    public void  SetAntiAngkaLimit(final JTextField x, final int Limit){
        x.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e)
                {
                  String Data = x.getText();
                  int Jml  =Data.length();
                  char c = e.getKeyChar();
                  if (!((c >= '0') && (c <= '9') && (Jml < Limit)||    
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE))) {
                        //getToolkit().beep();
                        e.consume();
                  }
                }
        });
    }
    
    public void  SetAntiAngkaPakeDataDoubel (JTextField x){
        x.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e)
                {
                  char c = e.getKeyChar();
                  if (!( (c  == ':') || (c >= '0') && (c <= '9')  ||
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE)  )) {
                        //getToolkit().beep();
                        e.consume();
                  }
                  
                }
        });
    }

    public void SetAntiAngka(TextField x) {
       x.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e)
                {
                  char c = e.getKeyChar();
                  if (!((c >= '0') && (c <= '9') ||    
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE))) {
                        //getToolkit().beep();
                        e.consume();
                  }
                }
        });
    }

     public void LimitCharInput (final TextField x, final int Batas){
         x.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e)
                {
                    String Data = x.getText();
                    int Limit = Data.length();
                    if (Limit > Batas){
                        e.consume();  
                        x.setText("");
                    }
                }
        });
        
    }
    
    
        
}
