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

/**
 *
 * @author LANTAI3
 */
public class PembelianBahanPenerimanPrintView extends JFrame {
    
    private JButton printButton = new JButton("Print");
    private PembelianBahanPenerimanPrint drawingPanel;
    
    public static void main(String[] args) {
        
    new PembelianBahanPenerimanPrintView("xxx", "xxx");
  }
  

  public PembelianBahanPenerimanPrintView(String TransNo, String Date) {
      super("View Pembelian WR");
      Aksi ();
    
      Container content = getContentPane();   
      JPanel buttonPanel = new JPanel();
      buttonPanel.setBackground(Color.white);
      buttonPanel.add(printButton);
      content.add(buttonPanel, BorderLayout.SOUTH);
      drawingPanel = new PembelianBahanPenerimanPrint(TransNo, Date);;
      content.add(drawingPanel, BorderLayout.NORTH);
      pack();
      setVisible(true);
    
  }
  private void Aksi (){
      printButton.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed (ActionEvent e){
              PrintUtilities.printComponent(drawingPanel);
          }
      });
  }
}
