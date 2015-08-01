//package com.wordpress.ari.combofilter;
package SistemPro;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;


public class app_search1 implements KeyListener {

	private final JComboBox comboBox;

	private final Vector list = new Vector();

	private boolean shouldHide = false;

	public app_search1(JComboBox combo) {
		this.comboBox = combo;
		for(int i=0;i< comboBox.getModel().getSize();i++) {
			list.addElement((String)comboBox.getItemAt(i));
		}
	}

	@Override
	public void keyTyped(final KeyEvent e) {
		EventQueue.invokeLater(new Runnable() {
			@Override public void run() {
				String text = ((JTextField)e.getSource()).getText();
				if(text.length()==0) {
					setSuggestionModel(comboBox, new DefaultComboBoxModel(list), "");
					comboBox.hidePopup();
				}else{
					ComboBoxModel m = getSuggestedModel(list, text);
                                        System.out.println(text + " data asli 2");
					if(m.getSize()==0 || shouldHide) {
						comboBox.hidePopup();
					}else{
						setSuggestionModel(comboBox, m, text);
						comboBox.showPopup();
					}
				}
			}
		});
	}
	@Override
	public void keyPressed(KeyEvent e) {
		JTextField textField = (JTextField)e.getSource();
		String text = textField.getText();
		shouldHide = false;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			for(Object obj: list) {
				String s = (String) obj;
				if(s.startsWith(text)) {
					textField.setText(s);
					return;
				}
			}
			break;
		case KeyEvent.VK_ENTER:
			if(!list.contains(text)) {
				list.addElement(text);
				Collections.sort(list);
				setSuggestionModel(comboBox, getSuggestedModel(list, text), text);
			}
			shouldHide = true;
			break;
		case KeyEvent.VK_ESCAPE:
			shouldHide = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	private static void setSuggestionModel(
			JComboBox comboBox, ComboBoxModel mdl, String str) {
           // System.out.println(str + " data asli");
		comboBox.setModel(mdl);
		comboBox.setSelectedIndex(-1);
                
		((JTextField)comboBox.getEditor().getEditorComponent()).setText(str);
                
	}

	private static ComboBoxModel getSuggestedModel(
			Vector list, String text) {
		DefaultComboBoxModel m = new DefaultComboBoxModel();
		for(Object  obj :  list) {
			String s = (String) obj;
			if(s.toUpperCase().contains(text.toUpperCase()))
				m.addElement(s);
                       // System.out.println(text + " data asli 1");
		}
		return m;
	}
}

