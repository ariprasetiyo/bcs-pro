/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

public  class Colom_table {
        public void ukuran_colom (JTable name,  int[] lebar) {
            int jumlah = name.getColumnCount();
            //atau
            //int jumlah = name.getColumnCount()-1;
            name.setAutoResizeMode(name.AUTO_RESIZE_OFF);
            for (int a= 0; a < jumlah; a++ ) {
                TableColumn column = name.getColumnModel().getColumn(a);
                column.setPreferredWidth(lebar[a]);
            }
        }
    }