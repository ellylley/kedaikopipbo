/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedaikopipbo;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kedaikopipbo.koneksi;
import kedaikopipbo.usesession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class pesan extends javax.swing.JFrame {

    DefaultTableModel table = new DefaultTableModel();
    
    public pesan() {
      initComponents();
        Connection conn = null;
        try {
            conn = koneksi.configDB(); // Mengambil koneksi dari metode configDB()
        } catch (SQLException ex) {
            // Tangani pengecualian jika diperlukan
            ex.printStackTrace();
        }
        totalnya();
        tanggal();

        tb_keranjang.setModel(table);
        table.addColumn("ID Pesanan");
        table.addColumn("Tanggal Pesanan");
        table.addColumn("Nama Produk");
        table.addColumn("Catatan");
        table.addColumn("Jumlah");
        table.addColumn("Harga");
        table.addColumn("Total Harga");

        tampilData();
    }
    
    

    public void tanggal() {
        Date now = new Date();
        tanggal_pesanan.setDate(now);
    }

    private void tampilData() {
        //untuk mengahapus baris setelah input
        int row = tb_keranjang.getRowCount();
        for (int a = 0; a < row; a++) {
            table.removeRow(0);
        }

        String query = "SELECT * FROM `keranjang` join produk on keranjang.id_produk=produk.id_produk ";
        String procedures = "CALL `total_harga_pesanan`()";

        try {
            Connection connect = null;
            try {
                connect = koneksi.configDB();
            } catch (SQLException ex) {
                // Tangani pengecualian jika diperlukan
                ex.printStackTrace();
            }
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query

            while (rslt.next()) {
                //menampung data sementara

                String id_pesanan = rslt.getString("id_pesanan");
                String tanggal = rslt.getString("tgl_pesanan");
              
                String id_produk = rslt.getString("nama_produk");
                String catatan = rslt.getString("catatan");
                String jumlah = rslt.getString("jumlah");
                String harga = rslt.getString("harga");
                String total = rslt.getString("total_harga");
                 

                //masukan semua data kedalam array
                String[] data = {id_pesanan, tanggal, id_produk, catatan, jumlah, harga, total};
                //menambahakan baris sesuai dengan data yang tersimpan diarray
                table.addRow(data);
            }
            //mengeset nilai yang ditampung agar muncul di table
            tb_keranjang.setModel(table);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void clear() {
        txt_kodebarang2.setText(null);
        txt_namabarang2.setText(null);
        txt_harga2.setText(null);
        txt_jumlah2.setText(null);
        txt_totalharga.setText(null);
        txt_catatan.setText(null);
        txt_jenisp.setSelectedItem(null); 
         txt_totalharga2.setText(null);

    }

    private void jenisp() {
    try {
        String sql = "SELECT * FROM pesanan";
        java.sql.Connection con = koneksi.configDB(); // Use configDB() to get the connection
        java.sql.Statement stm = con.createStatement();
        java.sql.ResultSet res = stm.executeQuery(sql);
        while (res.next()) {
            txt_jenisp.addItem(res.getString("jenis_pesanan"));
        }
    } catch (Exception e) {
        e.printStackTrace(); // Print stack trace to help debug any issues
    }
}

    private void keranjang() {
        String kode = txt_kodebarang2.getText();
        String nama = txt_namabarang2.getText();
        String harga = txt_harga2.getText();
        String jumlah = txt_jumlah2.getText();
        String total = txt_totalharga.getText();
      String catatan = txt_catatan.getText();
      String jenisp = (String) txt_jenisp.getSelectedItem();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = String.valueOf(date.format(tanggal_pesanan.getDate()));
       

        // Generate kode pesanan secara otomatis
       

        // Panggil koneksi
        Connection connect = null;
        try {
            connect = koneksi.configDB();
        } catch (SQLException ex) {
            // Tangani pengecualian jika diperlukan
            ex.printStackTrace();
        }

        // Query untuk memasukkan data
        String query = "INSERT INTO `keranjang` (`id_pesanan`, `id_produk`, `tgl_pesanan`, `jumlah`,`catatan`, `harga`, `total_harga`) "
                + "VALUES (NULL, '" + kode + "', '" + tanggal + "','" + jumlah + "', '" + catatan + "', '" + harga + "', '" + total + "')";

        try {
            // Menyiapkan statement untuk dieksekusi
            PreparedStatement ps = connect.prepareStatement(query);
            ps.executeUpdate(); // Menggunakan executeUpdate tanpa parameter
            JOptionPane.showMessageDialog(null, "Data Masuk Ke-Keranjang");

        } catch (SQLException | HeadlessException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan");

        } finally {
            tampilData();
            clear();

        }
        totalnya();
    }



    private void hapusData() {
        //ambill data no pendaftaran
        int i = tb_keranjang.getSelectedRow();

        String kode = table.getValueAt(i, 0).toString();

        Connection connect = null;
        try {
            connect = koneksi.configDB();
        } catch (SQLException ex) {
            // Tangani pengecualian jika diperlukan
            ex.printStackTrace();
        }

        String query = "DELETE FROM `keranjang` WHERE `keranjang`.`id_pesanan` = '" + kode + "' ";
        try {
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(query);
            ps.execute();
        } catch (SQLException | HeadlessException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Dihapus");
        } finally {
            tampilData();
            clear();
        }
        totalnya();
    }

    private void totalnya() {
       String procedures = "CALL `total_harga_transaksi`()";

        try {
            Connection connect = koneksi.configDB();//memanggil koneksi
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(procedures);//menjalanakn query\
            while (rslt.next()) {
                txt_totalharga2.setText(rslt.getString(1));
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void total() {
        String harga = txt_harga2.getText();
        String jumlah = txt_jumlah2.getText();

        int hargaa = Integer.parseInt(harga);
        try {
            int jumlahh = Integer.parseInt(jumlah);

            int total = hargaa * jumlahh;
            String total_harga = Integer.toString(total);

            txt_totalharga.setText(total_harga);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Only Number");
            txt_jumlah2.setText(null);
        }
    }

//   private void reset() {
//        txt_uang.setText(null);
//    }

 private void pesan() {
//        
insertToTransaksi();
//
//          
   }

   private void insertToTransaksi() {
    Connection connect = null;
    Statement sttmnt = null;
    ResultSet rslt = null;

    try {
        connect = koneksi.configDB();
        sttmnt = connect.createStatement();
        rslt = sttmnt.executeQuery("SELECT * FROM keranjang");

        // Simpan hasil query dalam ArrayList
        List<Map<String, String>> dataKeranjang = new ArrayList<>();
        while (rslt.next()) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put("id_produk", rslt.getString("id_produk"));
            rowData.put("tgl_pesanan", rslt.getString("tgl_pesanan"));
            rowData.put("jumlah", rslt.getString("jumlah"));
            rowData.put("catatan", rslt.getString("catatan"));
            rowData.put("harga", rslt.getString("harga"));
            rowData.put("total_harga", rslt.getString("total_harga"));
            
            dataKeranjang.add(rowData);
        }

        // Mengambil waktu saat ini untuk kode pesanan
        String waktuSekarang = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String kodePesanan = generateKodePesanan(waktuSekarang, connect);

        // Loop melalui hasil query dan masukkan ke tabel transaksi
        for (Map<String, String> rowData : dataKeranjang) {
            String id_produk = rowData.get("id_produk");
            String tanggal = rowData.get("tgl_pesanan");
            String jumlah = rowData.get("jumlah");
            String catatan = rowData.get("catatan");
            String harga = rowData.get("harga");
            String total = rowData.get("total_harga");
            String jenisp = (String) txt_jenisp.getSelectedItem();

            // Membuat query untuk memasukkan data ke tabel transaksi
            String query = "INSERT INTO `pesanan` (`id_pesanan`, `kode_pesanan`, `tgl_pesanan`, `id_produk`, `catatan`, `jumlah`, `harga`, `total_harga`, `status`,`status_bayar`, `jenis_pesanan`) "
                    + "VALUES (NULL, '" + kodePesanan + "', '" + tanggal + "', '" + id_produk + "','" + catatan + "','" + jumlah + "', '" + harga + "', '" + total + "', 'Not Yet', 'Belum Terbayar',  '" + jenisp + "')";

            // Eksekusi query
            sttmnt.executeUpdate(query);
        }

        JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");

    } catch (SQLException e) {
        System.out.println(e.getMessage());
        JOptionPane.showMessageDialog(null, "Data Gagal Disimpan");

    } finally {
        // Tutup ResultSet, Statement, dan Connection
        try {
            if (rslt != null) rslt.close();
            if (sttmnt != null) sttmnt.close();
            if (connect != null) connect.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // Panggil metode clear() jika diperlukan
        tampilData();
        clear();
    }
}

// Fungsi untuk menghasilkan kode pesanan berdasarkan waktu saat ini
private String generateKodePesanan(String waktuSekarang, Connection connect) throws SQLException {
    Statement sttmnt = null;
    ResultSet rslt = null;
    String kodePesanan = "";

    try {
        sttmnt = connect.createStatement();

        // Ambil nomor urut terakhir untuk waktu yang sama
        String query = "SELECT COUNT(*) AS total FROM pesanan WHERE tgl_pesanan = '" + waktuSekarang + "'";
        rslt = sttmnt.executeQuery(query);
        int nomorUrut = 1;
        if (rslt.next()) {
            nomorUrut = rslt.getInt("total") + 1;
        }

        // Format kode pesanan: tanggal + nomor urut (misal: 20240903-001)
        String formattedNumber = String.format("%03d", nomorUrut);
        kodePesanan = waktuSekarang.replace("-", "").replace(":", "").replace(" ", "") + "-" + formattedNumber;
        
    } finally {
        if (rslt != null) rslt.close();
        if (sttmnt != null) sttmnt.close();
    }

    return kodePesanan;
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        txt_kodebarang2 = new javax.swing.JTextField();
        txt_namabarang2 = new javax.swing.JTextField();
        txt_harga2 = new javax.swing.JTextField();
        txt_jumlah2 = new javax.swing.JTextField();
        txt_totalharga = new javax.swing.JTextField();
        txt_catatan = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_keranjang = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        txt_totalharga2 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        tanggal_pesanan = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        txt_jenisp = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); // NOI18N
        jLabel1.setText("PESAN");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("SEARCH DATA");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        txt_kodebarang2.setEditable(false);
        txt_kodebarang2.setBackground(new java.awt.Color(204, 204, 204));

        txt_namabarang2.setEditable(false);
        txt_namabarang2.setBackground(new java.awt.Color(204, 204, 204));

        txt_harga2.setEditable(false);
        txt_harga2.setBackground(new java.awt.Color(204, 204, 204));

        txt_jumlah2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_jumlah2ActionPerformed(evt);
            }
        });
        txt_jumlah2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_jumlah2KeyReleased(evt);
            }
        });

        txt_totalharga.setEditable(false);
        txt_totalharga.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("NAMA BARANG");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("JUMLAH");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("HARGA");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("TOTAL HARGA");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("CATATAN");

        tb_keranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tb_keranjang);

        jButton2.setText("DELETE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        txt_totalharga2.setEditable(false);
        txt_totalharga2.setBackground(new java.awt.Color(204, 204, 204));

        jButton4.setText("PESAN");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("ADD");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        tanggal_pesanan.setDateFormatString("dd-MM-yyyy");
        tanggal_pesanan.setEnabled(false);
        tanggal_pesanan.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("JENIS PESANAN");

        txt_jenisp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dine in", "Take Away" }));

        jButton3.setText("BACK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addComponent(txt_kodebarang2, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(tanggal_pesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txt_namabarang2, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(txt_harga2, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_jumlah2, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(txt_totalharga, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(txt_catatan, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txt_jenisp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(289, 289, 289)
                        .addComponent(txt_totalharga2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tanggal_pesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(txt_kodebarang2))
                .addGap(41, 41, 41)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txt_namabarang2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_harga2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_jumlah2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_totalharga, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_catatan, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jenisp, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(txt_totalharga2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_jumlah2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_jumlah2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_jumlah2ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
 new menu().setVisible(true);
//        this.setVisible(false);
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1MouseClicked

    private void txt_jumlah2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jumlah2KeyReleased
total();        // TODO add your handling code here:
    }//GEN-LAST:event_txt_jumlah2KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
   hapusData();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
 keranjang();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
pesan();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
  new dashboardwaitress().setVisible(true);
        dispose();          // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(pesan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(pesan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(pesan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(pesan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new pesan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser tanggal_pesanan;
    private javax.swing.JTable tb_keranjang;
    private javax.swing.JTextField txt_catatan;
    javax.swing.JTextField txt_harga2;
    private javax.swing.JComboBox<String> txt_jenisp;
    private javax.swing.JTextField txt_jumlah2;
    javax.swing.JTextField txt_kodebarang2;
    javax.swing.JTextField txt_namabarang2;
    private javax.swing.JTextField txt_totalharga;
    private javax.swing.JTextField txt_totalharga2;
    // End of variables declaration//GEN-END:variables
}
