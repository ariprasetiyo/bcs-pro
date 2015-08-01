/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Cara Penggunaan
 * createTabButtonActionPerformed(java.awt.event.ActionEvent evt ,
 * JTabbedPane JTabbed, String Pesan, JPanel Panel, Dimension Kecil, Dimension Besar)
 * evt      = ActionEvent
 * Pesan    = Pesan/Judul JTabbed
 * Kecil    = Ukursan Jscroll ( new Dimension (200,200))
 * Besar    = Ukuran JPanel ( new Dimension (400,300))
 */
package SistemPro;

import com.jgoodies.looks.common.RGBGrayFilter;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

/**
 *
 * @author LANTAI3
 */
public class TambahTab {
    
    /*
     * private static final Icon CLOSE_TAB_ICON = new ImageIcon(TambahTab.class.getResource("../Gambar/GambarKecil/Close.png"));
       private static final Icon PAGE_ICON = new ImageIcon(TambahTab.class.getResource("../Gambar/GambarKecil/Updated2_Icon.png"));
     
       javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Load.png"))
    */
    private final Icon CLOSE_TAB_ICON = new ImageIcon(getClass().getResource("/Gambar/GambarKecil/Close.png"));
    private final Icon PAGE_ICON = new ImageIcon(getClass().getResource("/Gambar/GambarKecil/Updated2_Icon.png"));
    private int tabCount = 0;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane tabbedPane;
    
    public TambahTab(){
        
    }
     public  void addClosableTab(final JTabbedPane tabbedPane, final JComponent c, final String title ,final Icon icon) {
        // Add the tab to the pane without any label
        tabbedPane.addTab(null, c);
        int pos = tabbedPane.indexOfComponent(c);

        // Create a FlowLayout that will space things 5px apart
        FlowLayout f = new FlowLayout(FlowLayout.CENTER, 5, 0);

        // Make a small JPanel with the layout and make it non-opaque
        JPanel pnlTab = new JPanel(f);
        pnlTab.setOpaque(false);

        // Add a JLabel with title and the left-side tab icon
        JLabel lblTitle = new JLabel(title);
        lblTitle.setIcon(icon);

        // Create a JButton for the close tab button
        JButton btnClose = new JButton();
        btnClose.setOpaque(false);

        // Configure icon and rollover icon for button

        //FixBugClassPathImage(btnClose,CLOSE_TAB_ICON);
        btnClose.setRolloverIcon(CLOSE_TAB_ICON);
        btnClose.setRolloverEnabled(true);
        btnClose.setIcon(RGBGrayFilter.getDisabledIcon(btnClose, CLOSE_TAB_ICON));

        // Set border null so the button doesn't make the tab too big
        btnClose.setBorder(null);

        // Make sure the button can't get focus, otherwise it looks funny
        btnClose.setFocusable(false);

        // Put the panel together
        pnlTab.add(lblTitle);
        pnlTab.add(btnClose);

        // Add a thin border to keep the image below the top edge of the tab
        // when the tab is selected
        pnlTab.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        // Now assign the component for the tab
        tabbedPane.setTabComponentAt(pos, pnlTab);

        // Add the listener that removes the tab
     ActionListener listener = new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) {
         // The component parameter must be declared "final" so that it can be
         // referenced in the anonymous listener class like this.
         tabbedPane.remove(c);
       }
     };
     btnClose.addActionListener(listener);

     // Optionally bring the new tab to the front
     tabbedPane.setSelectedComponent(c);

     //-------------------------------------------------------------
     // Bonus: Adding a <Ctrl-W> keystroke binding to close the tab
     //-------------------------------------------------------------
     AbstractAction closeTabAction = new AbstractAction() {
       @Override
       public void actionPerformed(ActionEvent e) {
         tabbedPane.remove(c);
       }
     };

     // Create a keystroke
     KeyStroke controlW = KeyStroke.getKeyStroke("control W");

     // Get the appropriate input map using the JComponent constants.
     // This one works well when the component is a container. 
     InputMap inputMap = c.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

     // Add the key binding for the keystroke to the action name
     inputMap.put(controlW, "closeTab");

     // Now add a single binding for the action name to the anonymous action
     c.getActionMap().put("closeTab", closeTabAction);
  }
      public void FixBugClassPathImage (JButton btnClose, Icon CLOSE_TAB_ICON){
         btnClose.setRolloverIcon(CLOSE_TAB_ICON);
        btnClose.setRolloverEnabled(true);
        btnClose.setIcon(RGBGrayFilter.getDisabledIcon(btnClose, CLOSE_TAB_ICON));
     }
     
     public  void createTabButtonActionPerformed(java.awt.event.ActionEvent evt ,
             JTabbedPane JTabbed, String Pesan, JPanel Panel, Dimension Kecil, Dimension Besar) {                                                

    JScrollPane scrollPane = new JScrollPane(Panel);
    
    //scrollPane.setPreferredSize(Kecil);
    Panel.setPreferredSize(Besar);
    
    Icon icon = PAGE_ICON;
    addClosableTab(JTabbed, scrollPane,  Pesan , icon);
  }  
     
}
