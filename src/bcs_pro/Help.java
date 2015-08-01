/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bcs_pro;

import PDF.PdfViewer;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author LANTAI3
 */
public class Help extends javax.swing.JInternalFrame {

    /**
     * Creates new form Help
     */
    public Help() {
        initComponents();
        Aksi();
    }

    private void Aksi(){
        jTree1.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent E) {
            String node = E.getNewLeadSelectionPath().getLastPathComponent().toString();
            /*
             * Membuat Resep
  Membuat Daftar Pembelanjaan Bahan
  Membuat File Pemesanan
  Membuat Koreksi Penerimaan Bahan
  Membuat Rencana Alokasi Waktu
  Membuat Rencana Inventaris Alat
  Membuat Daftar Rancangan Harga
  Membuat Transaksi Penjualan
  Membuat Data Biaya
  Membuat Laporan Laba Rugi

             */
            if( node.equals("Membuat Resep") ) {
                PangilHelp( E, HelpResepLink(), "Resep");
            } 
            else if (node.equals("Install Program Bcs Pro")){
                PangilHelp (E, HelpLinkInstalasi(), "panduan3");
            }
            else if( node.equals("Panduan Menjalankan Program") ) {
              PangilHelp (E,HelpLinkPenggunaan(), "Panduan Menjalankan Program");
            }
             else if( node.equals("Membuat Daftar Pembelanjaan Bahan") ) {
              PangilHelp (E,HelpLinkPembelanjaan(), "Membuat Daftar Pembelanjaan Bahan");
            }
             else if( node.equals("Membuat File Pemesanan") ) {
              PangilHelp (E,HelpLinkFilePemesanan(), "Membuat File Pemesanan");
            }
             else if( node.equals("Membuat Koreksi Penerimaan Bahan") ) {
              PangilHelp (E,HelpLinkPenerimaanBahan(), "Membuat Koreksi Penerimaan Bahan");
            } 
             else if( node.equals("Membuat Rencana Alokasi Waktu") ) {
              PangilHelp (E,AlokasiWaktu(), "Membuat Rencana Alokasi Waktu");
            }
             else if( node.equals("Membuat Rencana Inventaris Alat") ) {
              PangilHelp (E,InventararisAlat(), "Membuat Rencana Inventaris Alat");
            }
             else if( node.equals("Membuat Daftar Rancangan Harga") ) {
              PangilHelp (E,HelpLinkRancanganHarga(), "Membuat Daftar Rancangan Harga");
            }
             else if( node.equals("Membuat Transaksi Penjualan") ) {
              PangilHelp (E,Penjualan(), "Membuat Transaksi Penjualan");
            }
             else if( node.equals("Membuat Data Biaya") ) {
              PangilHelp (E,DataBiaya(), "Membuat Data Biaya");
            }
             else if( node.equals("Membuat Laporan Laba Rugi") ) {
              PangilHelp (E,LabaRugi(), "Membuat Laporan Laba Rugi");
            }  
        }
      });
    }
    int NoJTabbed;
    private void PangilHelp(TreeSelectionEvent E, String Link, String Judul){
        /*
         * Screen Size
         * Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         */
             try {
           // long heapSize = Runtime.getRuntime().totalMemory();
            //load a pdf from a byte buffer
            File file = new File(Link);
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            final PDFFile pdffile = new PDFFile(buf);
            PdfViewer pdfViewer = new PdfViewer();
    
            pdfViewer.setPDFFile(pdffile);
            
            SistemPro.TambahTab TabNewViewData = new SistemPro.TambahTab();
            TabNewViewData.createTabButtonActionPerformed(E, jTabbedPane1,
                                  Judul  , pdfViewer,
                                    // ( Panjang, Tinggi )
                                    new Dimension(0, 0), new Dimension(800, 800));
            
            /*
             * Letakan harus ada dalam menampilkan PDF
             */
            
            pack();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            this.setSize(screenSize);
            
            PDFPage page = pdffile.getPage(1);
            pdfViewer.getPagePanel().showPage(page);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    String folder;
    private String HelpResepLink() {
        folder = System.getProperty("user.dir") + File.separator + "Panduan\\MEMBUAT RESEP.pdf";
        return folder ;
    }
    private String HelpLinkInstalasi() {
        folder = System.getProperty("user.dir") + File.separator + "Panduan\\panduaninstalasi.pdf";
        return folder ;
    }
    private String HelpLinkPenggunaan() {
        folder = System.getProperty("user.dir") + File.separator + "Panduan\\panduan3.pdf";
        return folder ;
    }
    
    //MEMBUAT DAFTAR PEMBELANJAAN BAHAN.pdf
    private String HelpLinkPembelanjaan() {
        folder = System.getProperty("user.dir") + File.separator + "Panduan\\MEMBUAT DAFTAR PEMBELANJAAN BAHAN.pdf";
        return folder ;
    }
    //MEMBUAT DAFTAR RANCANGAN HARGA
    private String HelpLinkRancanganHarga() {
        folder = System.getProperty("user.dir") + File.separator + "Panduan\\MEMBUAT DAFTAR RANCANGAN HARGA.pdf";
        return folder ;
    }
    private String HelpLinkFilePemesanan () {
        folder = System.getProperty("user.dir") + File.separator + "Panduan\\MEMBUAT FILE PEMESANAN.pdf";
        return folder ;
    }
    
    private String HelpLinkPenerimaanBahan() {
        folder = System.getProperty("user.dir") + File.separator + "Panduan\\MEMBUAT KOREKSI PENERIMAAN BAHAN.pdf";
        return folder ;
    }
    
    private String AlokasiWaktu(){
        folder = System.getProperty("user.dir") + File.separator + "Panduan\\MEMBUAT RENCANA ALOKASI WAKTU.pdf";
        return folder ;
    }
    private String InventararisAlat(){
        folder = System.getProperty("user.dir") + File.separator + "Panduan\\MEMBUAT RENCANA INVENTARIS ALAT.pdf";
        return folder ;
    }
    private String Penjualan(){
        folder = System.getProperty("user.dir") + File.separator + "Panduan\\MEMBUAT TRANSAKSI PENJUALAN.pdf";
        return folder ;
    }
    private String DataBiaya(){
        folder = System.getProperty("user.dir") + File.separator + "Panduan\\MEMBUAT DATA BIAYA.pdf";
        return folder ;
    }
    private String LabaRugi(){
        folder = System.getProperty("user.dir") + File.separator + "Panduan\\MEMBUAT LAPORAN LABA.pdf";
        return folder ;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Bantuan");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/logo.png"))); // NOI18N

        jTree1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Help");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Tahap Awal Penggunaan Program");
        javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Install Program Bcs Pro");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Panduan Menjalankan Program");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Panduan - Panduan");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Membuat Resep");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Membuat Daftar Pembelanjaan Bahan");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Membuat File Pemesanan");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Membuat Koreksi Penerimaan Bahan");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Membuat Rencana Alokasi Waktu");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Membuat Rencana Inventaris Alat");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Membuat Daftar Rancangan Harga");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Membuat Transaksi Penjualan");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Membuat Data Biaya");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Membuat Laporan Laba Rugi");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(jTree1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
