/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LANTAI3
 */
public class ButtonJTableKeDua extends DefaultCellEditor {
    private String label;
    protected JButton button;
    DefaultTableModel ModelTabel2;
    JTable Tabel2;
    public ButtonJTableKeDua(JCheckBox checkBox,  final DefaultTableModel ModelTabel,final JTable Tabel ) {
        super(checkBox);
        this.ModelTabel2 = ModelTabel;
        this.Tabel2 = Tabel; 

    }
       
    public Component getTableCellEditorComponent(final JTable table, final Object value,
        boolean isSelected, final int row, int column) {
        label = (value == null) ? "Delete" + row: value.toString();
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                fireEditingStopped(); // agar tidak error saat di hapus jtable         
                try { 

                    int Pilih = JOptionPane.showConfirmDialog(null, "Apakah anda yakin untuk menghapusnya ?" , " Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (Pilih == JOptionPane.YES_OPTION){
                                                     
                           /*
                            * Khusus pesanan barang
                            */
                           
                           if (table.getName().equalsIgnoreCase("Pesanan Barang")){
                              // ModelTabel2.getValueAt(row, Pilih)
                           }
                           else if (table.getName().equalsIgnoreCase("JTabelTabelGolongan")){
                              // ModelTabel2.getValueAt(row, Pilih)                            
                              DeleteDataPenerimaan("sys_gol_asset",  (String)ModelTabel2.getValueAt(row , 1));
                           }
                           
                           ModelTabel2.removeRow(row ); 
                           
                     }
                     else if ( Pilih == JOptionPane.NO_OPTION){
                    }
                }
                catch (Exception X){
                    //JOptionPane.showMessageDialog(null," Tidak boleh di hapus semua" + row);          
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
    private void DeleteDataPenerimaan(String NamaTabel , String DataSyarat){
        SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
        MYSQL.MysqlDelete("delete from "+ NamaTabel + " where gol = '"+DataSyarat+ "'");
    }
    
}
