package SistemPro;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D; 

@SuppressWarnings("serial")
public class ClPanelTransparant extends JPanel
{
	private Color clr;
	
	public ClPanelTransparant()
	{
		setOpaque(false);
		clr = new Color(getBackground().getRed(), getBackground().getRed(), getBackground().getGreen(),70);
	}	
	
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
		clr = new Color(getBackground().getRed(), getBackground().getRed(), getBackground().getGreen(),70);
		repaint();
	}
	
	protected void paintComponent(Graphics graph)
	{
		super.paintComponent(graph);
		Graphics2D g2d = (Graphics2D) graph.create();
		g2d.setColor(clr);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.dispose();
	}
	
}