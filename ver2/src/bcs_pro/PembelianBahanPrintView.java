/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import SistemPro.PrintUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class PembelianBahanPrintView  extends JFrame {

  public static void main(String[] args) {
    new PembelianBahanPrintView("xxx", "xxx");
  }
  

  public PembelianBahanPrintView(String TransNo, String Date) {
    super("View Pembelian");
    //System.out.println( Dtt.GetPrintPembelianTransNo());
    //WindowUtilities.setNativeLookAndFeel();
    //addWindowListener(new ExitListener());
    Container content = getContentPane();
    JButton printButton = new JButton("Print");
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.white);
    buttonPanel.add(printButton);
    content.add(buttonPanel, BorderLayout.SOUTH);
    final PembelianBahanPrint drawingPanel = new PembelianBahanPrint(TransNo, Date);
    content.add(drawingPanel, BorderLayout.NORTH);
    pack();
    setVisible(true);
    printButton.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent e) {
              PrintUtilities.printComponent(drawingPanel);
          }
    });
    
  }


  
}