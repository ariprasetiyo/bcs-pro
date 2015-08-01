/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemPro;

import javax.swing.JTable;
 
@SuppressWarnings("serial")
public class TableCheckBox extends JTable
{
@SuppressWarnings({ "unchecked", "rawtypes" })
public Class getColumnClass(int kolom)
{
switch(kolom)
{
case 0 :
return String.class; //Kolom Tabel 0 menggunakan type data String
case 1 :
return String.class; //Kolom Tabel 1 menggunakan type data String
case 2 :
return String.class; //Kolom Tabel 2 menggunakan type data String
case 3 :
return Boolean.class; //Kolom Tabel 3 menggunakan type data Boolean, disini akan otomatis membuat tampilan CheckBox pada kolom tabel
default :
return String.class;
}
}
}