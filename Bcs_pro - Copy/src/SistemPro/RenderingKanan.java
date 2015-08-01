/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;


import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class RenderingKanan extends DefaultTableCellRenderer{
    public Component getTableCellRendererComponent(JTable table, Object value,
    boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column)
        ;
        setHorizontalAlignment(SwingConstants.RIGHT);
        return this;
    }
}

/* Cara penggunaan
 * 
 *
 * public class laporan extends javax.swing.JFrame {
public TableCellRenderer kanan = new RenderingKanan();
public TableCellRenderer tengah = new RenderingTengah();

public laporan() {
initComponents();
tabel.getColumnModel().getColumn(3).setCellRenderer( kanan );
tabel.getColumnModel().getColumn(0).setCellRenderer( tengah );
tabel.getColumnModel().getColumn(1).setCellRenderer( tengah );
}
 */