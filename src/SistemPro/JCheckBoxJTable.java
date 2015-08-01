/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author LANTAI3
 */
public class JCheckBoxJTable extends JCheckBox implements TableCellRenderer {
   public JCheckBoxJTable(String text){
        super (text);
        //setOpaque(true);
    }
    public Component getTableCellRendererComponent(JTable table, Object value,
    boolean isSelected, boolean hasFocus, int row, int column) {
        //setText((value == null) ? "yy"  + row: value.toString());
        return this;
        } 
}
