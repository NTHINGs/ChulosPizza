/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;
import Funciones.BasededatosManager;
import Funciones.FileChuser;
import Funciones.PedidoPDF;
import Funciones.RendererTabla;
import Funciones.TicketPDF;
import com.itextpdf.text.DocumentException;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 *
 * @author Mauricio
 */
public class Repartidor extends javax.swing.JFrame {

    /**
     * Creates new form Repartidor
     */
    public static final int MIN_ZOOM = 0;
    public static final int MAX_ZOOM = 21;
    final Browser browser;
    double latitud=0, longitud=0;
    JFrame frame = new JFrame();
    /**
     * In map.html file default zoom value is set to 4.
     */
    private static int zoomValue = 4;
    String directorio;
    byte [] bytes;
    BrowserView browserView;
    BasededatosManager bd=new BasededatosManager();
    boolean bol;
    int idcliente = 0;
    public Repartidor() {
        browser = new Browser();
        try {
            initComponents();
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            entrega();
            browser.loadURL("http://nthings.me/maps.html");    
            if(bol==true){
                mapa();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Repartidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void entrega() throws SQLException{
        frame.dispose();
        browserView = new BrowserView(browser);
        browser.loadURL("http://nthings.me/maps.html");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bytes = bos.toByteArray();
        ResultSet consulta = bd.consultar("SELECT idpedidos,cantidad,totalneto,clientes_idclientes,ticket FROM pedidos WHERE entregado=0 AND estado=1");
        
        bol=true;
        if (consulta.first()) {
            id_pedido.setText(consulta.getString(1));
            numpizzas.setText(consulta.getString(2));
            totalneto.setText("$" + consulta.getString(3));
            idcliente = consulta.getInt(4);
            bytes = consulta.getBytes(5);
        } else {
            JOptionPane.showMessageDialog(null, "No hay pedidos por entregar", "HEY", JOptionPane.WARNING_MESSAGE);
            id_pedido.setText("XXX");
            numpizzas.setText("XXX");
            totalneto.setText("XXX");
            bol=false;
        }
        if(bol==true){
            ResultSet consultamapa = bd.consultar("SELECT latitud,longitud FROM clientes WHERE idclientes='" + idcliente + "'");
            if (consultamapa.first()) {
                this.latitud = consultamapa.getDouble("latitud");
                this.longitud = consultamapa.getDouble("longitud");
            }
            System.out.println(latitud + " " + longitud + "\nid:" + id_pedido.getText());

            ResultSet detalleventas = bd.consultar("SELECT P.nombre FROM pizzas P, detalleventas D WHERE D.pizzas_idpizzas=P.idpizzas AND D.pedidos_idpedidos='" + id_pedido.getText() + "';");
            String[] nomcolumnas = new String[]{"Pizzas"};
            jTable2.setModel(bd.llenarTabla(1, nomcolumnas, detalleventas));
            jTable2.getColumnModel().getColumn(0).setHeaderRenderer(new RendererTabla(new Color(255, 204, 0), new Color(204, 0, 0)));
            jTable2.getColumnModel().getColumn(0).setCellRenderer(new RendererTabla(new Color(204, 0, 0), new Color(255, 204, 0)));
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.add(browserView, BorderLayout.CENTER);
            frame.setSize(700, 500);
            frame.setLocationRelativeTo(null);
            frame.setAlwaysOnTop(true);
            mapa();
        }
    }
    
    public void ejecutarPDF(){
        String d = "";
        try {

            if ("/".equals(directorio.substring(0, 1))) {
                Runtime.getRuntime().exec("evince " + directorio);
            }
            if (":".equals(directorio.substring(1, 2))) {
                char signo = (char) 92;
                String[] arreglo = directorio.split("\\\\");
                for (int a = 0; a < arreglo.length; a++) {
                    d = d + arreglo[a] + "\\\\\\\\";
                }
                Desktop.getDesktop().open(new File(d));
            }
        } catch (IOException ex) {
            Logger.getLogger(Repartidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        id_pedido = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        numpizzas = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        totalneto = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 0, 0));

        id_pedido.setFont(new java.awt.Font("Comic Sans MS", 1, 36)); // NOI18N
        id_pedido.setForeground(new java.awt.Color(255, 204, 0));
        id_pedido.setText("XXX");

        jLabel3.setFont(new java.awt.Font("Comic Sans MS", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 204, 0));
        jLabel3.setText("Pizzas:");

        jLabel4.setFont(new java.awt.Font("Comic Sans MS", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 204, 0));
        jLabel4.setText("Pedido:");

        numpizzas.setFont(new java.awt.Font("Comic Sans MS", 1, 36)); // NOI18N
        numpizzas.setForeground(new java.awt.Color(255, 204, 0));
        numpizzas.setText("XXX");

        jButton1.setBackground(new java.awt.Color(255, 204, 0));
        jButton1.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jButton1.setText("Imprimir Ticket de Entrega");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 204, 0));
        jButton3.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jButton3.setText("Abrir Mapa");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(jTable2);

        jLabel6.setFont(new java.awt.Font("Comic Sans MS", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 204, 0));
        jLabel6.setText("Total:");

        totalneto.setFont(new java.awt.Font("Comic Sans MS", 1, 36)); // NOI18N
        totalneto.setForeground(new java.awt.Color(255, 204, 0));
        totalneto.setText("XXX");

        jButton4.setBackground(new java.awt.Color(255, 204, 0));
        jButton4.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jButton4.setText("ACTUALIZAR PEDIDOS");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 204, 0));
        jButton5.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jButton5.setText("POSICIONAR CLIENTE EN MAPA");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_pedido)
                        .addGap(257, 257, 257)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numpizzas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 203, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalneto)
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addComponent(jScrollPane3)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(id_pedido)
                    .addComponent(jLabel4)
                    .addComponent(numpizzas)
                    .addComponent(jLabel3)
                    .addComponent(totalneto)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:+
            Image image = browserView.getImage();
            FileChuser dir=new FileChuser();
            directorio=dir.obtenerDirectorio(".pdf");
            
            if(bytes==null){
                TicketPDF ticketpedido=new TicketPDF(id_pedido.getText(),idcliente,directorio);
            }else{
                File archivo = new File(directorio);
                FileOutputStream fos;
                fos = new FileOutputStream(archivo);
                fos.write(bytes);
                fos.flush();
                fos.close();
            }
            PedidoPDF ticketentrega = new PedidoPDF(id_pedido.getText(), image.getScaledInstance(350, 250, java.awt.Image.SCALE_SMOOTH), directorio + "mapa");
            bd.ejecutar("UPDATE pedidos SET entregado='1' WHERE idpedidos='" + id_pedido.getText() + "';");
            ejecutarPDF();
            actualizar();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Repartidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(Repartidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        mapa();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        actualizar();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        mapa();
    }//GEN-LAST:event_jButton5ActionPerformed

    void mapa(){
        frame.setVisible(true);
        if(latitud!=0 && longitud!=0){
            browser.executeJavaScript("var myLatlng = new google.maps.LatLng("+latitud+", "+longitud+");\n" +
                   "var marker = new google.maps.Marker({\n" +
                   "    position: myLatlng,\n" +
                   "    map: map,\n" +
                   "    title: 'Cliente'\n" +
                   "});");
        }else{
            JOptionPane.showMessageDialog(null, "Este cliente no tiene coordenadas gps", "HEY", JOptionPane.ERROR_MESSAGE);
            frame.dispose();
        }
    }
    
    void actualizar(){
        try {
            // TODO add your handling code here:
            entrega();
        } catch (SQLException ex) {
            Logger.getLogger(Repartidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel id_pedido;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel numpizzas;
    private javax.swing.JLabel totalneto;
    // End of variables declaration//GEN-END:variables
}
