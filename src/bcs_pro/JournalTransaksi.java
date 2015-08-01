
package bcs_pro;

import SistemPro.ButtonJTable;
import SistemPro.ButtonJTableKeDua;
import SistemPro.RenderingKanan;
import SistemPro.RenderingTengah;
import SistemPro.SatuUntukSemua;
import SistemPro.app_search_data_chart_of_account;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class JournalTransaksi extends javax.swing.JInternalFrame {

    /**
     * Creates new form JournalTransaksi
     */
    SatuUntukSemua Satu = new SatuUntukSemua();
    private SistemPro.TanggalSistem N       = new SistemPro.TanggalSistem();
    private SistemPro.TransNo       TransNoLocal = new SistemPro.TransNo ();
    private TableCellRenderer kanan = new RenderingKanan();
    private TableCellRenderer tengah= new RenderingTengah();
    private DefaultTableModel   Modeltabel2 = new DefaultTableModel(); 
    private SistemPro.KoneksiDatabase KD    = new SistemPro.KoneksiDatabase();
    private Connection              K       = KD.createConnection();
    String Semua;
    String TransNo, ChartNo, ChartName, NoteString, Tanggal, Period;
            double Amount;
    private boolean EditBoleh2 = false, Refresh = false, ButtonDelete = false;
    private  SistemPro.ComponentHanyaAngka HanyaAngka = new SistemPro.ComponentHanyaAngka();
    private SistemPro.KomaToString ConvertAngka = new SistemPro.KomaToString();
    int DataAwalTabel, DataAkirTabel;
    
    public JournalTransaksi() {
        initComponents();
        Tanggal();
        Tabel2();
        Aksi();
        AmbilHeaderTabel(AmbilDataPeriodSistem());
        DataAwalTabel =  JTabelJournal.getRowCount();
    }
    
    private void Aksi(){
        Add.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        JButtonPbNew.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
               /*
                * Save, edit, delete, new, print, add, iput menu
                */
                 boolean[] EnableDisable = {false, false, false, true, true, true, true};
                 Buttonn(EnableDisable);
                 AmountJournal.setText("");
                 Note.setText("");
                 EnableComponent();
            }
        });
        Add.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
               /*
                * Save, edit, delete, new, print, add, iput menu
                */
                 boolean[] EnableDisable = {true, false, false, true, true, true, true};
                 Buttonn(EnableDisable);
                 AddMenu();
            }
        });
        JButtonPbSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
               /*
                * Save, edit, delete, new, print, add, iput menu
                */
                 boolean[] EnableDisable = {false, false, false, true, true, false, false};
                 Buttonn(EnableDisable);
                 
                 DisableComponent();
                 
                 JTextFieldSearch.setText("");
                 SearchTable(JTabelJournal,Modeltabel2);
                 
                 try {
                 Thread.sleep(1000);
                 } 
                 catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                 }
                 
                 SaveDatabase();
                 
            }
        });
        
        JButtonRefresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
               Refresh = true;
               AmbilHeaderTabel(AmbilDataPeriodKazao());
            }
        });
        
        JButtonPbDelete.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
            JPasswordField pass = new JPasswordField();
            Object[] Object ={
              "Masukan password untuk menghapus data ", pass
            };

            int Pilih = JOptionPane.showConfirmDialog(null , Object , "Input data master barang ", JOptionPane.OK_CANCEL_OPTION);
            if (Pilih == JOptionPane.OK_OPTION){
                if (pass.getText().equalsIgnoreCase("AriHanif")){
                     Satu.DeleteData(JTabelJournal.getValueAt(JTabelJournal.getSelectedRow(), 1).toString(), "header_journal_transaksi");
                     AmbilHeaderTabel(AmbilDataPeriodSistem());
                }
               }     
            }
        });
        
                 
        HanyaAngka.SetAntiAngka(AmountJournal);
        
    }
    private void Tanggal(){
        /*
         * Tampilan bagian Depreciation Procces
         * Perid yang diset ke bulan dan Tahun
         */
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
        dateChooserCombo1.setDateFormat(dt1);        
    }
    private void AddMenu(){
        if ( FilterInputMenu() ){
            ButtonDelete = true;
            String ChartNo, ChartName;
            String Dated = dateChooserCombo1.getText();
            SistemPro.NoUrut NoUrutan = new   SistemPro.NoUrut();       
            Object obj[] = new Object[9];    
            Semua =  (String) InputMenu.getSelectedItem();
            ChartName =  Satu.AmbilBelakang(Semua, null);
            ChartNo =  Satu.AmbilDepan(Semua, null);
            obj[0] = "-";
            obj[1] = TransNoLocal.TransNoLocal ("JM", JTabelJournal, Dated);
            obj[2] = ChartNo; 
            obj[3] = ChartName;        
            obj[4] = ConvertAngka.NilaiRupiah(AmountJournal.getText());
            obj[5] = Note.getText();
            obj[6] = Dated;
            obj[7] = AmbilDataPeriodDhooser();
            Modeltabel2.addRow(obj); 
            
            DataAkirTabel = JTabelJournal.getRowCount(); 
            
            AmountJournal.setText("");
            Note.setText("");
            
           /*
            * Memasukan tombol ke jtable
            */
           JTabelJournal.getColumnModel().getColumn(8).setCellRenderer(  new ButtonJTable("Delete"));
           JTabelJournal.getColumnModel().getColumn(8).setCellEditor( new ButtonJTableKeDua(new JCheckBox(),Modeltabel2, JTabelJournal));
           TotalJournal();
        }       
    }
    private void TotalJournal(){
        double Tot;
        int JumlahRowPesan= Modeltabel2.getRowCount();
        Tot = TotalPerhitungan (Modeltabel2, JumlahRowPesan, 4);
        TotalBiayaPerbulan.setText( ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Tot))) ;
    }
    String AA;
    double TotalPerhitungan (DefaultTableModel ModelTabel, int JumlahRowPesan, int AmbilKolomBerapa){
        double Tot = 0;
        for (int ab = 0 ; ab <= JumlahRowPesan - 1 ; ab++ ){

            if (ModelTabel.getValueAt(ab, AmbilKolomBerapa) == null){
                ModelTabel.setValueAt("0", ab, AmbilKolomBerapa);
            }        
            if (ModelTabel.getValueAt(ab, AmbilKolomBerapa).equals("")){
               ModelTabel.setValueAt("0", ab, AmbilKolomBerapa);
           }

            /*
             * Menghitung jumlah yang harus di beli
             * Dan Hapus Koma
             */
            try {
               AA = ConvertAngka.RupiahKeDoubel(String.valueOf(Modeltabel2.getValueAt(ab, AmbilKolomBerapa).toString()));
               Tot = Double.valueOf(AA).doubleValue() + Tot; 
            }
            catch (Exception x){
                JOptionPane.showMessageDialog(null, "Error Input Data");
                ModelTabel.setValueAt("0", ab, AmbilKolomBerapa);
            }
            
        }
        return Tot;
    }
    
    
    /*
     * Ambil data period dari DD_MM_YYYY menjadi YYYY-MM-DD
     * Ke String
     */
    private String AmbilDataPeriodDhooser(){
        String Tgl1  = dateChooserCombo1.getText();
        String Period1  = N.ConvertTgl_Bln_Thn_To_Tahun_String(Tgl1) + "" + N.ConvertTgl_Bln_Thn_To_Bulan_String(Tgl1) ;
        return Period1;
    }
    
    private String AmbilDataPeriodKazao(){
        String PeriodRefresh   = kazaoCalendarDateInputAsset.getKazaoCalendar().getShortDate();
        
        N.SetKazaoToBlnIndo(PeriodRefresh);
        N.SetKazaoToThnIndo(PeriodRefresh);

        PeriodRefresh = N.GetThnIndoStKazao()+N.GetBlnIndoStrKazao();
        return PeriodRefresh;
    }
    private String AmbilDataPeriodSistem(){
       N.SetTahunSis();
       N.SetBulanSis();
       
       int Bln = N.GetBulanSis();
       String bln ;

       if (Bln  < 10 ){
            bln = "0"+Bln;
       }
       else {
           bln = Bln+"";
       }
        
       return (N.GetTahunSis() + "" + bln );
    }
    
    
    private boolean FilterInputMenu(){
        String[] DataPesanError = {"Tidak ada nominal yang diisi", "Mohon isi data keterangan pengeluaran"};
        JTextField [] Data = {AmountJournal, Note};
        return  Satu.ValidasiData(Data, DataPesanError);

    }
    
    private void Buttonn (boolean[] Button){
        JButtonPbSave.setEnabled(Button[0]);
        JButtonPbEdit.setEnabled(Button[1]);

        JButtonPbNew.setEnabled(Button[3]);
        JButtonPbPrint.setEnabled(false);
        Add.setEnabled(Button[5]);
        InputMenu.setEnabled(Button[6]);
    }
    private void ResetTampilan( ){
        JTextField[] Field = {AmountJournal, Note, TotalBiayaPerbulan};  
        Satu.ResetTampilan(Field);
        Satu.HapusDataJTabel(JTabelJournal);
    }
    private void DisableComponent(){
        Component[] Data = {InputMenu,AmountJournal, Note};
        boolean[] TutupTidak = {false, false, false};
        Satu.LogikaComponent(Data, TutupTidak);
    }
     private void EnableComponent(){
        Component[] Data = {InputMenu,AmountJournal, Note};
        boolean[] TutupTidak = {true, true, true};
        Satu.LogikaComponent(Data, TutupTidak);
    }
    private void Tabel2(){
        /*
         * trans_no, nama_pemesan, porsi, jenis_pesanan, created_date, periode
         */
        String header[] = {"No","trans no", "Chart No", "Chart Name","Amount", "Note", "Tanggal", "Period","Action"};
        Modeltabel2 = new DefaultTableModel(null,header) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                        //nilai false agar tidak bisa di edit
                         for (int a = DataAwalTabel ; a < DataAkirTabel ; a++ ){
                             if (colIndex == 8 && rowIndex == a) {
                                return ButtonDelete;  
                              }
                         }
                        
                        return false;   //Disallow the editing of any cell
                 }
        };
        JTabelJournal.setModel(Modeltabel2);
        Tabel2 (JTabelJournal, Modeltabel2);
    }
    public void SearchTable(final JTable JTabelBarang, final DefaultTableModel Modeltabel){
         String text = JTextFieldSearch.getText();
               final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Modeltabel);
                JTabelBarang.setRowSorter(sorter);
               if (text.length() == 0) {
                 sorter.setRowFilter(null);
               } else {
                 try {
                   sorter.setRowFilter(
                       RowFilter.regexFilter("(?i)"+text));
                       //System.out.println(sorter.getViewRowCount());
                 } catch (PatternSyntaxException pse) {
                   System.err.println("Bad regex pattern");
                 }
               }
     }
      private void Tabel2 (final JTable JTabelBarang, final DefaultTableModel Modeltabel  ){

       /*
        * Membuat sort pada tabel
        * Search Data
        * SearchTable(JTabelConvertSatuan,Modeltabel);
        */     
        EditBoleh2 = true;
        JTextFieldSearch.addKeyListener(new KeyListener(){
            @Override
            public void keyReleased(KeyEvent e) {
               SearchTable(JTabelBarang,Modeltabel);
               if (JTextFieldSearch.getText().equals("")){
                    EditBoleh2 = true;
               }
               else{
                    EditBoleh2 = false;
               }    
            }     
            @Override
            public void keyTyped(KeyEvent e) {
                }
           @Override
           public void keyPressed(KeyEvent e) {
            }
        });

        /*
         * Rata tengah atau kanan table
         */
        JTabelJournal.getColumnModel().getColumn(0).setCellRenderer( tengah );
        JTabelJournal.getColumnModel().getColumn(1).setCellRenderer( tengah ); 
        JTabelJournal.getColumnModel().getColumn(2).setCellRenderer( tengah );
        JTabelJournal.getColumnModel().getColumn(3).setCellRenderer( tengah ); 
        JTabelJournal.getColumnModel().getColumn(4).setCellRenderer( kanan ); 
        JTabelJournal.getColumnModel().getColumn(5).setCellRenderer( tengah ); 
        JTabelJournal.getColumnModel().getColumn(6).setCellRenderer( tengah );
        JTabelJournal.getColumnModel().getColumn(7).setCellRenderer( tengah );
        JTabelJournal.getColumnModel().getColumn(7).setCellRenderer( tengah );
        
        /*
         * Ukuran table JTabelResep
         * "No", "No Pegawai","Nama", "Tanggal Cuti", "Jumlah Cuti", "Sisa Cuti", "Kepeluan", "Create Date", "Action"
         */      
        int jarak_colom[] = {40,100,70,300,100, 200,80,80, 100 };
        SistemPro.Colom_table ukuran_colom = new SistemPro.Colom_table();
        ukuran_colom.ukuran_colom(JTabelJournal, jarak_colom);
       
        /*
         * Disable drag colum tabel
         */       
        JTabelJournal.getTableHeader().setReorderingAllowed(false); 
     }
       
    
    private void SaveDatabase(){
       /*
        * Save DetailPo to database Mysql
        */
        try {
            SistemPro.koneksiMYSQL MYSQL = new SistemPro.koneksiMYSQL();
            MYSQL.MysqlDelete("delete from header_journal_transaksi");
        
            int a = JTabelJournal.getRowCount() ;
            Statement stm = K.createStatement();

            // dbStatement=con.createStatement();

            for(int i=0;i < a ;i++){

                TransNo   = JTabelJournal.getValueAt(i, 1).toString();
                ChartNo   = JTabelJournal.getValueAt(i, 2).toString();
                ChartName = JTabelJournal.getValueAt(i, 3).toString();
                Amount    = Satu.BersihDataKeDoubel(ConvertAngka.RoundingDesimal(ConvertAngka.RupiahKeDoubel(JTabelJournal.getValueAt(i, 4).toString())));
                NoteString      = JTabelJournal.getValueAt(i, 5).toString();
                Tanggal   = JTabelJournal.getValueAt(i, 6).toString();
                Period    = JTabelJournal.getValueAt(i, 7).toString();

                stm.executeUpdate("INSERT INTO header_journal_transaksi "
                        + "(trans_no ,chart_no ,chart_name ,amount, note, tanggal, period, updated_date) VALUES ('"
                        + TransNo+"','"+ ChartNo+ "','"+ ChartName+ "','"+Amount 
                        + "','"+NoteString 
                        + "','"+Tanggal 
                        + "','"+Period 
                        + "', now() "
                        +")");
            }
            /*
             * Untuk disable button pada tabel
             */
            ButtonDelete = false;
            
            LabelKeterangan.setText("Berhasil Di Saved");
            JButtonPbSave.setEnabled(false);
            ResetTampilan( );
            if (Refresh == true){
                AmbilHeaderTabel(AmbilDataPeriodKazao());
            }
            else {
                AmbilHeaderTabel(AmbilDataPeriodSistem());
            }
            Refresh = false;
            DisableComponent();
     }
     catch (Exception X){

          if (X.toString().contains("PRIMARY")){
             JOptionPane.showMessageDialog(null, "Data ada yang sama ");
         }
         else {
             JOptionPane.showMessageDialog(null,  " error : 3234553234 : "  +X, " Error", JOptionPane.ERROR_MESSAGE);
             Logger.getLogger(PembelianBahan.class.getName()).log(Level.SEVERE, null, X);
         }  
        }                         
    }
     /*
     * Pilihan 
     * 0 = Ambil List penjulan
     * Period
     * 1 = dopubel klik penjualan / data Lengkap
     * TransNo
     */
    
    private void AmbilHeaderTabel(String PeriodAtauTransNo){        
            ResultSet HQ2 = null;
               try {
                   Statement stm2 = K.createStatement();
                    System.out.println(PeriodAtauTransNo);
                    Satu.HapusDataJTabel(JTabelJournal);

                    HQ2 = stm2.executeQuery("select trans_no, chart_no, chart_name, amount, tanggal, note, period "
                      + " from header_journal_transaksi "
                      + " where period = '" + PeriodAtauTransNo + "'");
        
                   while(HQ2.next()  ){
                        TransNo         = HQ2.getString("trans_no");
                        ChartNo         = HQ2.getString("chart_no");
                        ChartName       = HQ2.getString("chart_name");
                        Amount          = HQ2.getDouble("amount");
                        NoteString      = HQ2.getString("note");
                        Tanggal         = HQ2.getString("tanggal");
                        Period          = HQ2.getString("period");

                        String[] add         = {HQ2.getRow()+ "",TransNo, ChartNo ,ChartName, ConvertAngka.NilaiRupiah(ConvertAngka.FormatDesimalRubahE9(Amount)), NoteString, Tanggal, Period };
                        Modeltabel2.addRow(add); 
                   }                    
               }
               catch (Exception ex){
                    JOptionPane.showMessageDialog (null, "Error (4)"+ ex, "Error" , JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
               }  
               TotalJournal();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooserPanel1 = new datechooser.beans.DateChooserPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTabelJournal = new javax.swing.JTable();
        JPanelButtonSave = new javax.swing.JPanel();
        JButtonPbSave = new javax.swing.JButton();
        JButtonPbEdit = new javax.swing.JButton();
        JButtonPbDelete = new javax.swing.JButton();
        JButtonPbNew = new javax.swing.JButton();
        JButtonPbPrint = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        InputMenu = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        Add = new javax.swing.JButton();
        AmountJournal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Note = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        jPanel2 = new javax.swing.JPanel();
        TotalBiayaPerbulan = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        LabelKeterangan = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        JTextFieldSearch = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        JButtonRefresh = new javax.swing.JButton();
        kazaoCalendarDateInputAsset = new org.kazao.calendar.KazaoCalendarDate();
        jLabel19 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Journal Transaksi");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/logo.png"))); // NOI18N

        JTabelJournal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(JTabelJournal);

        JPanelButtonSave.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JButtonPbSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Save.png"))); // NOI18N
        JButtonPbSave.setText("Save");
        JButtonPbSave.setEnabled(false);

        JButtonPbEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Update.png"))); // NOI18N
        JButtonPbEdit.setText("Edit");
        JButtonPbEdit.setEnabled(false);

        JButtonPbDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Delete.png"))); // NOI18N
        JButtonPbDelete.setText("Delete");

        JButtonPbNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/New document.png"))); // NOI18N
        JButtonPbNew.setText("New");

        JButtonPbPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/Print.png"))); // NOI18N
        JButtonPbPrint.setText("Print");
        JButtonPbPrint.setEnabled(false);

        javax.swing.GroupLayout JPanelButtonSaveLayout = new javax.swing.GroupLayout(JPanelButtonSave);
        JPanelButtonSave.setLayout(JPanelButtonSaveLayout);
        JPanelButtonSaveLayout.setHorizontalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelButtonSaveLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JButtonPbSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbDelete)
                .addGap(5, 5, 5)
                .addComponent(JButtonPbNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JButtonPbPrint)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPanelButtonSaveLayout.setVerticalGroup(
            JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelButtonSaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(JButtonPbSave)
                .addComponent(JButtonPbEdit)
                .addComponent(JButtonPbDelete)
                .addComponent(JButtonPbNew)
                .addComponent(JButtonPbPrint))
        );

        InputMenu.setEditable(true);
        InputMenu.setModel(new javax.swing.DefaultComboBoxModel(app_search_data_chart_of_account.getData().toArray()));
        InputMenu.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("Type Chart");

        Add.setText("Add");
        Add.setEnabled(false);

        AmountJournal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        AmountJournal.setEnabled(false);

        jLabel1.setText("Rp. ");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Note");

        Note.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setText("Tanggal");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setText(":");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText(":");

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel7.setText(":");

        dateChooserCombo1.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(InputMenu, 0, 271, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(AmountJournal, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(Add)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(Note))))))
            .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(InputMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel2)
                .addComponent(AmountJournal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel1)
                .addComponent(jLabel5))
            .addGap(5, 5, 5)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(Note, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7))
            .addGap(5, 5, 5)
            .addComponent(Add))
    );

    TotalBiayaPerbulan.setEditable(false);
    TotalBiayaPerbulan.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabel8.setText("Total Biaya - Biaya :");

    LabelKeterangan.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
    LabelKeterangan.setForeground(new java.awt.Color(255, 0, 51));
    LabelKeterangan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(LabelKeterangan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(TotalBiayaPerbulan, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(0, 20, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel8)
                .addComponent(TotalBiayaPerbulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(LabelKeterangan, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel9.setText("JOURNAL TRANSAKSI");

    jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

    jLabel18.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabel18.setText("Search data  :");

    JButtonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/GambarKecil/Refresh.png"))); // NOI18N
    JButtonRefresh.setText("Refresh");

    kazaoCalendarDateInputAsset.setFormat("mm/yyyy");

    jLabel19.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabel19.setText("Periode  :");

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel18)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(kazaoCalendarDateInputAsset, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(JButtonRefresh)
            .addContainerGap())
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel5Layout.createSequentialGroup()
            .addGap(9, 9, 9)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(kazaoCalendarDateInputAsset, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(JTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addComponent(JButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(JPanelButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addComponent(jScrollPane1)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(JPanelButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add;
    private javax.swing.JTextField AmountJournal;
    private javax.swing.JComboBox InputMenu;
    private javax.swing.JButton JButtonPbDelete;
    private javax.swing.JButton JButtonPbEdit;
    private javax.swing.JButton JButtonPbNew;
    private javax.swing.JButton JButtonPbPrint;
    private javax.swing.JButton JButtonPbSave;
    private javax.swing.JButton JButtonRefresh;
    private javax.swing.JPanel JPanelButtonSave;
    private javax.swing.JTable JTabelJournal;
    private javax.swing.JTextField JTextFieldSearch;
    private javax.swing.JLabel LabelKeterangan;
    private javax.swing.JTextField Note;
    private javax.swing.JTextField TotalBiayaPerbulan;
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private datechooser.beans.DateChooserPanel dateChooserPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private org.kazao.calendar.KazaoCalendarDate kazaoCalendarDateInputAsset;
    // End of variables declaration//GEN-END:variables
}
