/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author LANTAI3
 */
public class ButtonJTable extends JButton implements TableCellRenderer {
    public ButtonJTable(String text){
        super (text);
        //setOpaque(true);
    }
    public Component getTableCellRendererComponent(JTable table, Object value,
    boolean isSelected, boolean hasFocus, int row, int column) {
        //setText((value == null) ? "yy"  + row: value.toString());
        return this;
        }
}


/*
 * Cara Kedua
 * Yang Asli 
 *
class ButtonRenderer2 extends JButton implements TableCellRenderer {
    public ButtonRenderer2(String text){
        super (text);
        //setOpaque(true);
    }
    public Component getTableCellRendererComponent(JTable table, Object value,
    boolean isSelected, boolean hasFocus, int row, int column) {
        //setText((value == null) ? "yy"  + row: value.toString());
        return this;
        }
    }

/*
 * 
 *

class ButtonEditor2 extends DefaultCellEditor {
    private String label;
    protected JButton button;
    DefaultTableModel ModelTabel2;
    JTable Tabel2;
    public ButtonEditor2(JCheckBox checkBox,  final DefaultTableModel ModelTabel,final JTable Tabel ) {
        super(checkBox);
        this.ModelTabel2 = ModelTabel;
        this.Tabel2 = Tabel;            
    }
    public Component getTableCellEditorComponent(JTable table, final Object value,
        boolean isSelected, final int row, int column) {
        label = (value == null) ? "Delete" + row: value.toString();
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                fireEditingStopped(); // agar tidak error saat di hapus jtable
                try {                      
                    int Pilih = JOptionPane.showConfirmDialog(null, "Apakah anda yakin untuk menghapus row ke : " + row + 1 + " ?", " Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if (Pilih == JOptionPane.YES_OPTION){
                            ModelTabel2.removeRow(row );                            
                     }
                     else if ( Pilih == JOptionPane.NO_OPTION){
  }
                }
                catch (Exception X){
                    JOptionPane.showMessageDialog(null," Tidak boleh di hapus semua" + row);          
                }
              }
        });
        Tabel2.repaint();
        button.setText(label);
        return button;
    }
    public Object getCellEditorValue() {
        return new String(label);
        }
    }
    * */