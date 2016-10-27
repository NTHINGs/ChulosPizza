/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Funciones.BasededatosManager;
import Funciones.RendererTabla;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Misael
 * @author Mauricio
 */
public class PuntoVenta extends javax.swing.JInternalFrame {
    BasededatosManager bd=new BasededatosManager();
    final DefaultTableModel modelopedido=new DefaultTableModel();
    /**
     * Creates new form PuntoVenta
     */
    int idempleado;
    int idpedido;
    int contadorpizzas=0;
    String[] preciospedido;
    public PuntoVenta(int idempleado) {
        this.idempleado=idempleado;
        
        bd.ejecutar("INSERT INTO pedidos(`Empleados_idEmpleados`) VALUES("+this.idempleado+");");
        try {
            initComponents();
            ResultSet consultapedido=bd.consultar("SELECT AUTO_INCREMENT FROM information_schema.TABLES where TABLE_SCHEMA='chulospizza' and TABLE_NAME='pedidos';");
            while(consultapedido.next()){
                idpedido=(consultapedido.getInt(1))-1;
            }
            //TODO
            ResultSet consulta=bd.consultar("SELECT foto,nombre,precio FROM pizzas;");
            String[] nomcolumnas=new String[]{"Foto","Nombre Pizza","Precio"};
            byte[] image;
            jTable1.setRowHeight(73);
            DefaultTableModel modelo=new DefaultTableModel(){
                @Override
                public Class getColumnClass(int column)
                {
                    if (column == 0) return ImageIcon.class; 
                    return Object.class;
                }
            };
            //Se crean las columnas del modelo dependiendo de los parametros que se le manden al método
            for(int i=0; i<3;i++){
                modelo.addColumn(nomcolumnas[i]);
            }
            while (consulta.next()) {
                Object[] fila = new Object[3];
                    //El primer indice en un ResultSet es el 1 por lo que hay que sumar un
                    image = consulta.getBytes(1);
                    Image img = Toolkit.getDefaultToolkit().createImage(image);
                    ImageIcon icon =new ImageIcon(img.getScaledInstance(200, 73,java.awt.Image.SCALE_SMOOTH));
                    fila[0]=icon;
                    fila[1] = consulta.getObject(2);
                    fila[2]="$"+consulta.getObject(3);
                modelo.addRow(fila);
            }
            jTable1.setModel(modelo);
            
            jTable1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        JTable target = (JTable) e.getSource();
                        int row = target.getSelectedRow();
                        int column = target.getSelectedColumn();
                        // do some action if appropriate column
                        if(Integer.parseInt(numeropizzas.getText())>contadorpizzas){
                            try {
                                double precio=0;
                                ResultSet consultaprecios=bd.consultar("SELECT precio FROM pizzas WHERE nombre='"+target.getValueAt(row, 1)+"';");
                                while(consultaprecios.next()){
                                    precio=consultaprecios.getDouble(1);
                                }
                                total.setText(Double.toString(((Double.parseDouble(total.getText())+precio)*0.16)+(Double.parseDouble(total.getText())+precio)));
                                Object[]fila=new Object[]{target.getValueAt(row,1),precio};
                                modelopedido.addRow(fila);
                                contadorpizzas=contadorpizzas+1;
                            } catch (SQLException ex) {
                                Logger.getLogger(PuntoVenta.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{
                           if(Integer.parseInt(numeropizzas.getText())==1){
                            JOptionPane.showMessageDialog(null,"Ya elegiste la pizza","ERROR",JOptionPane.ERROR_MESSAGE);
                           }else{
                             JOptionPane.showMessageDialog(null,"Ya elegiste las "+numeropizzas.getText()+" pizzas","ERROR",JOptionPane.ERROR_MESSAGE);  
                           }
                        }
                    }
                }
            });
            modelopedido.addColumn("Pizza");
            modelopedido.addColumn("Precio");
            pedido.setModel(modelopedido);
            jTable1.getColumnModel().getColumn(0).setHeaderRenderer(new RendererTabla(new Color(255,204,0),new Color(204,0,0)));
            jTable1.getColumnModel().getColumn(1).setHeaderRenderer(new RendererTabla(new Color(255,204,0),new Color(204,0,0)));
            jTable1.getColumnModel().getColumn(2).setHeaderRenderer(new RendererTabla(new Color(255,204,0),new Color(204,0,0)));
            jTable1.getColumnModel().getColumn(1).setCellRenderer(new RendererTabla(new Color(204,0,0), new Color(255,204,0)));
            jTable1.getColumnModel().getColumn(2).setCellRenderer(new RendererTabla(new Color(204,0,0), new Color(255,204,0)));
            jTable1.setOpaque(false);
            jTable1.setSelectionBackground(new Color(255,204,0));
            jTable1.setSelectionForeground(new Color(204,0,0));
            pedido.getColumnModel().getColumn(0).setHeaderRenderer(new RendererTabla(new Color(255,204,0),new Color(204,0,0)));
            pedido.getColumnModel().getColumn(1).setHeaderRenderer(new RendererTabla(new Color(255,204,0),new Color(204,0,0)));
            pedido.getColumnModel().getColumn(0).setCellRenderer(new RendererTabla(new Color(204, 0, 0), new Color(255, 204, 0)));
            pedido.getColumnModel().getColumn(1).setCellRenderer(new RendererTabla(new Color(204, 0, 0), new Color(255, 204, 0)));
            pedido.setOpaque(false);
            pedido.setSelectionBackground( new Color(255,204,0));
            pedido.setSelectionForeground(new Color(204,0,0));
            
        } catch (SQLException ex) {
            Logger.getLogger(PuntoVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
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
        jButton13 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        numeropizzas = new javax.swing.JLabel();
        pizzas = new javax.swing.JLabel();
        cliente = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        total = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pedido = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Chulo's Pizza: Punto de Venta");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 0, 0));

        jPanel2.setBackground(new java.awt.Color(204, 0, 0));

        jButton13.setBackground(new java.awt.Color(255, 204, 0));
        jButton13.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jButton13.setText(">");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 204, 0));
        jButton5.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        jButton5.setText("Actualizar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 204, 0));
        jLabel1.setText("Cantidad:");

        jButton2.setBackground(new java.awt.Color(255, 204, 0));
        jButton2.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jButton2.setText("<");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 204, 0));
        jButton1.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jButton1.setText("Cliente");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        numeropizzas.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        numeropizzas.setForeground(new java.awt.Color(255, 204, 0));
        numeropizzas.setText("1");

        pizzas.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        pizzas.setForeground(new java.awt.Color(255, 204, 0));
        pizzas.setText("Pizza");

        cliente.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        cliente.setForeground(new java.awt.Color(255, 204, 0));
        cliente.setText("Nombre Cliente");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numeropizzas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pizzas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(numeropizzas)
                    .addComponent(jButton2)
                    .addComponent(jButton13)
                    .addComponent(jButton1)
                    .addComponent(cliente)
                    .addComponent(pizzas)
                    .addComponent(jButton5))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(204, 0, 0));

        total.setFont(new java.awt.Font("Comic Sans MS", 1, 36)); // NOI18N
        total.setForeground(new java.awt.Color(255, 204, 0));
        total.setText("0.00");

        jButton4.setBackground(new java.awt.Color(255, 204, 0));
        jButton4.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        jButton4.setText("Borrar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Comic Sans MS", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 204, 0));
        jLabel3.setText("$");

        pedido.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(pedido);

        jButton3.setBackground(new java.awt.Color(255, 204, 0));
        jButton3.setFont(new java.awt.Font("Comic Sans MS", 1, 38)); // NOI18N
        jButton3.setText("COBRAR");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 204, 0));
        jLabel4.setText("sin iva");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(total))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(108, 108, 108))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton4)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(total)
                    .addComponent(jLabel3)
                    .addComponent(jButton4)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(204, 0, 0));

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
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(74, 74, 74))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Clientes c=new Clientes(idempleado,idpedido);
        CajerosPrin.CajerosP.add(c);
        c.toFront();
        c.show();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        //COBRAR
        if(contadorpizzas<Integer.parseInt(numeropizzas.getText())){
            String piz="";
            if(Integer.parseInt(numeropizzas.getText())>1){
                piz="s";
            }
            JOptionPane.showMessageDialog(null,"Aún no eliges tu"+piz+" "+numeropizzas.getText()+" pizza"+piz,"ERROR",JOptionPane.ERROR_MESSAGE);
        }
        if(cliente.getText().equals("Nombre Cliente")){
            JOptionPane.showMessageDialog(null,"¡Elige al cliente!","ERROR",JOptionPane.ERROR_MESSAGE);
        }else{
            int opc=JOptionPane.showConfirmDialog(null, "¿El pedido es correcto?","CONFIRMA",JOptionPane.YES_NO_OPTION);
            if(opc==JOptionPane.YES_OPTION){         
                String[] pizzaspedido = new String[pedido.getRowCount()];
                preciospedido = new String[pedido.getRowCount()];
                for (int i = 0; i < pedido.getRowCount(); i++) {
                    pizzaspedido[i] = pedido.getValueAt(i, 0).toString();
                    preciospedido[i] = pedido.getValueAt(i, 1).toString();
                }
                double iva = Double.parseDouble(total.getText()) * 0.16;
                double totalneto = Double.parseDouble(total.getText()) + iva;
                Cobrar cobrar = new Cobrar(pizzaspedido, preciospedido, Double.parseDouble(total.getText()), iva, totalneto, numeropizzas.getText(), Integer.toString(idpedido), cliente.getText(), pedido.getRowCount());
                cobrar.setVisible(true);
                dispose();
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (numeropizzas.getText().equals("1")) {
            //No se puede decrementar mas el numero de pizzas
            JOptionPane.showMessageDialog(null,"¡No puedes pedir cero pizzas!","ERROR",JOptionPane.ERROR_MESSAGE);
        } else {
            if (numeropizzas.getText().equals("2")) {
                //Condicion para quitarle la "s" a "Pizzas" ya que regresa a singular
                pizzas.setText("Pizza");
            }
            //Decrementa en uno el numero de pizzas
            numeropizzas.setText(Integer.toString((Integer.parseInt(numeropizzas.getText()) - 1)));
            total.setText("0.00");
            contadorpizzas=0;
            try{
                for (int i = 0; i < jTable1.getRowCount(); i++) {
                    modelopedido.removeRow(0);
                }
            }catch(ArrayIndexOutOfBoundsException e){

            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        //Aumenta en uno el numero de pizzas
        numeropizzas.setText(Integer.toString((Integer.parseInt(numeropizzas.getText()) + 1)));
        //Agregar una "s" a "Pizza" para que se distinga que ya está en plural
        pizzas.setText("Pizzas");
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        total.setText("0.00");
        contadorpizzas=0;
        try{
        for (int i = 0; i < jTable1.getRowCount(); i++) {
           modelopedido.removeRow(0);
        }
        }catch(ArrayIndexOutOfBoundsException e){
            
        }
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            // TODO add your handling code here:
            ResultSet consultaclientes=bd.consultar("SELECT C.nombre FROM clientes C,pedidos P WHERE C.idclientes=P.clientes_idclientes AND P.idpedidos='"+idpedido+"';");
            while(consultaclientes.next()){
                cliente.setText(consultaclientes.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PuntoVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrar();
    }//GEN-LAST:event_formInternalFrameClosing
    
    public void cerrar() {
        Object[] opciones = {"Aceptar", "Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(rootPane, "Cerrar la ventana cancelará el pedido ", "CANCELAR PEDIDO",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opciones, "Aceptar");
        if (eleccion == JOptionPane.YES_OPTION) {
            bd.ejecutar("DELETE FROM pedidos WHERE idpedidos='"+idpedido+"';");
            bd.ejecutar("ALTER TABLE pedidos AUTO_INCREMENT='"+idpedido+"'");
            dispose();
        } else {
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cliente;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel numeropizzas;
    private javax.swing.JTable pedido;
    private javax.swing.JLabel pizzas;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
