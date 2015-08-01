/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

import javax.swing.table.TableModel;

/**
 *
 * @author LANTAI3
 */
public class JTabelPerhitunganColum {
    public JTabelPerhitunganColum(){
        
    }
     private void reCalcurate(TableModel ml, int TOTAL_ROW, int  TOTAL_COLUMN ) {
    if (ml == null)
      return;
    double total = 0.0;
    for (int i = 0; i < TOTAL_ROW; i++) {
      total += ((Double) ml.getValueAt(i, TOTAL_COLUMN)).doubleValue();
    }
    //ml.setValueAt(new Double(total), TOTAL_ROW, TOTAL_COLUMN);
  }
}
