/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Cara penggunaan
 * txtSample.setDocument(new JTextFieldLimit(30));
 * txtSample.setDocument(new NumericDocument(0,false));
 * 
 */
package SistemPro;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JTextFieldLimit extends PlainDocument {
  private int limit;
  // optional uppercase conversion
  private boolean toUppercase = false;

  
  public JTextFieldLimit(int limit) {
   super();
   this.limit = limit;
   }

  public JTextFieldLimit(int limit, boolean upper) {
   super();
   this.limit = limit;
   toUppercase = upper;
   }


  public void insertString
    (int offset, String  str, AttributeSet attr)
      throws BadLocationException {
   if (str == null) return;

   if ((getLength() + str.length()) <= limit) {
     if (toUppercase) str = str.toUpperCase();
     super.insertString(offset, str, attr);
     }
   }
}