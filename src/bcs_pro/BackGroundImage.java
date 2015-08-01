/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

 
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
 
/**
 *
 * @author McC
 */
public class BackGroundImage implements Border{
    public BufferedImage back;
 
    public BackGroundImage(){
        try {
            URL imagePath = new URL(getClass().getResource("/Gambar/BACKGROUND.jpg").toString());
            back = ImageIO.read(imagePath);
        } catch (Exception ex) {
             
        }
    }
    
    private String folder2;
    private String LokasiGambar2() {
        folder2 = System.getProperty("user.dir") + File.separator + "Gambar/background.jpg";
        return folder2 ;
    }
 
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawImage(back, (x + (width - back.getWidth())/2),(y + (height - back.getHeight())/2), null);
    }
 
    public Insets getBorderInsets(Component c) {
        return new Insets(0,0,0,0);
    }
 
    public boolean isBorderOpaque() {
        return false;
    }
}
 