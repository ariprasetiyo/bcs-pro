package SistemPro;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

@SuppressWarnings("serial")
public class ClButtonTransparant extends JButton{
	  private static final Color TL = new Color(1f,1f,1f,.2f);
	  private static final Color BR = new Color(0f,0f,0f,.9f);
	  private static final Color ST = new Color(1f,1f,1f,.3f);
          /*
           * Warna over tombol
           */
	  private static final Color SB = new Color(0f,1f,1f,.2f);
	  private Color ssc;
	  private Color bgc;
	  private int r = 8;
	  public ClButtonTransparant(String text) {
	    super(text);
	  }
	  public ClButtonTransparant(String text, Icon icon) {
	    super(text, icon);
	  }
	  @Override public void updateUI() {
	    super.updateUI();
	    setContentAreaFilled(false);
	    setFocusPainted(false);
	    setOpaque(false);
	    setForeground(Color.WHITE);
	  }
	  @Override protected void paintComponent(Graphics g) {
	    int x = 0;
	    int y = 0;
	    int w = getWidth();
	    int h = getHeight();
	    Graphics2D g2 = (Graphics2D)g.create();
	    g2.setRenderingHint(
	      RenderingHints.KEY_ANTIALIASING,
	      RenderingHints.VALUE_ANTIALIAS_ON);
	    Shape area = new RoundRectangle2D.Float(x, y, w-1, h-1, r, r);
	    ssc = TL;
	    bgc = BR;
	    ButtonModel m = getModel();
	    if(m.isPressed()) {
	      ssc = SB;
	      bgc = ST;
	    }else if(m.isRollover()) {
	      ssc = ST;
	      bgc = SB;
	    }
	    g2.setPaint(new GradientPaint(x, y, ssc, x, y+h, bgc, true));
	    g2.fill(area);
	    g2.setPaint(BR);
	    g2.draw(area);
	    g2.dispose();
	    super.paintComponent(g);
	  }
	}
