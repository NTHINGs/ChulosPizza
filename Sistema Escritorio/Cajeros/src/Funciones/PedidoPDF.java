/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Funciones;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import java.io.File;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author MauricioNTHINGs
 */
public class PedidoPDF {
    File archivo;
    FileChuser dir=new FileChuser();
    String directorio;
    Font f=new Font(FontFamily.HELVETICA,8,Font.NORMAL,BaseColor.BLACK);
    String idpedido;
    String cliente[];
    String vendedor[];
    String lineas[][];
    int cantidadlineas;
    String []totales;
    BasededatosManager bd=new BasededatosManager();
    PdfPCell vacio=new PdfPCell(new Paragraph(" "));
    
    public PedidoPDF(String id,String nombrecliente,String detalle[][],int cantidad) throws FileNotFoundException, DocumentException, IOException{
        try {
            this.idpedido=id;
            this.cliente=llenarVector("SELECT nombre,direccion,telefono FROM clientes WHERE nombre='"+nombrecliente+"';");
            this.vendedor=llenarVector("SELECT E.nombres, E.apellidos FROM Empleados E, pedidos P WHERE P.Empleados_idEmpleados=E.idEmpleados AND P.idpedidos='"+id+"';");
            this.lineas=detalle;
            this.cantidadlineas=cantidad;
            this.totales=llenarVector("SELECT total,iva,totalneto FROM pedidos WHERE idpedidos='"+idpedido+"';");
            this.directorio=dir.obtenerDirectorio(".pdf");
            this.archivo=new File(directorio);
            Document documento=new Document(PageSize.A6);
            FileOutputStream ficheroPdf=new FileOutputStream(archivo);
            PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
            documento.setMargins(0,0,1,0);
            documento.open();
            documento.add(tablaHeader());
            documento.add(tablaCliente());
            documento.add(tablaLineas());
            documento.add(tablaFooter());
            documento.close();
            FileInputStream pdf=new FileInputStream(archivo);
            int len = (int)archivo.length();
            String query = ("UPDATE pedidos SET ticket=? WHERE idpedidos='"+idpedido+"';");
            BasededatosManager bd=new BasededatosManager();
            PreparedStatement pstmt = bd.getConexion("chulospizza").prepareStatement(query);
            //method to insert a stream of bytes
            pstmt.setBinaryStream(1,pdf);
            pstmt.executeUpdate();
            ejecutarPDF();
        } catch (SQLException ex) {
            Logger.getLogger(PedidoPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    String[] llenarVector(String sql){
        String[] fila = null;
        try {
            ResultSet consulta=bd.consultar(sql);
            ResultSetMetaData rsmd=consulta.getMetaData();
            int columnas=rsmd.getColumnCount();
            fila=new String[columnas];
            while(consulta.next()){
                for(int a=0;a<fila.length;a++){
                    fila[a]=consulta.getString(a+1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PedidoPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fila;
    }
    PdfPTable tablaCliente(){
        PdfPTable tablacliente=new PdfPTable(2);
        tablacliente.setTotalWidth(500);
        PdfPCell celdanombrecliente=new PdfPCell(new Paragraph("Nombre Del Cliente: "+cliente[0]));
        tablacliente.addCell(celdanombrecliente);
        PdfPCell celdavendedor=new PdfPCell(new Paragraph("Vendedor: "+vendedor[0]+" "+vendedor[1]));
        tablacliente.addCell(celdavendedor);
        PdfPCell celdadireccion=new PdfPCell(new Paragraph("Direccion: "+cliente[1]));
        celdadireccion.setColspan(2);
        tablacliente.addCell(celdadireccion);
        PdfPCell celdatelefono=new PdfPCell(new Paragraph("Teléfono: "+cliente[2]));
        celdatelefono.setColspan(2);
        tablacliente.addCell(celdatelefono);
        vacio.setColspan(2);
        vacio.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
        tablacliente.addCell(vacio);
        return tablacliente;
    }
    
    PdfPTable tablaLineas(){
        PdfPTable tablalineas=new PdfPTable(2);
        PdfPCell celdanombrepizza=new PdfPCell(new Paragraph("Nombre Pizza",f));
        tablalineas.addCell(celdanombrepizza);
        PdfPCell celdapreciopizza=new PdfPCell(new Paragraph("Precio",f));
        tablalineas.addCell(celdapreciopizza);
        for(int a=0;a<cantidadlineas;a++){
            for(int b=0;b<2;b++){
                PdfPCell celda=new PdfPCell(new Paragraph(lineas[a][b],f));
                tablalineas.addCell(celda);
            }
        }
        vacio.setColspan(2);
        vacio.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
        tablalineas.addCell(vacio);
        return tablalineas;
    }
    
    PdfPTable tablaFooter(){
        PdfPTable tablafooter=new PdfPTable(2);
        PdfPTable cantidades=new PdfPTable(2);
        
        PdfPCell celdasubtotal=new PdfPCell(new Paragraph("Subtotal",f));
        cantidades.addCell(celdasubtotal);
        PdfPCell subtotalcantidad=new PdfPCell(new Paragraph("$"+totales[0],f));
        cantidades.addCell(subtotalcantidad);
        PdfPCell celdaiva=new PdfPCell(new Paragraph("I.V.A.",f));
        cantidades.addCell(celdaiva);
        PdfPCell ivacantidad=new PdfPCell(new Paragraph("$"+totales[1],f));
        cantidades.addCell(ivacantidad);
        PdfPCell celdatotal=new PdfPCell(new Paragraph("Total",f));
        cantidades.addCell(celdatotal);
        PdfPCell totalcantidad=new PdfPCell(new Paragraph("$"+totales[2],f));
        cantidades.addCell(totalcantidad);
        PdfPCell celdacantidades=new PdfPCell(cantidades);
        vacio=new PdfPCell(new Paragraph("!Que tenga un buen día!"));
        vacio.setColspan(1);
        vacio.setBorder(Rectangle.RIGHT|Rectangle.TOP|Rectangle.LEFT|Rectangle.BOTTOM);
        tablafooter.addCell(vacio);
        tablafooter.addCell(celdacantidades);
        return tablafooter;
    }
    PdfPTable tablaHeader() throws IOException, BadElementException{
            PdfPTable tablaheader=new PdfPTable(4);
            tablaheader.setTotalWidth(600);
            BasededatosManager bd=new BasededatosManager();
            try {
                ResultSet consulta=bd.consultar("SELECT logoempresa FROM configuracion");
                byte[] arreglo=null;
                while(consulta.next()){
                    arreglo=consulta.getBytes("logoempresa");
                }
                Image logo=Image.getInstance(arreglo);
                logo.scalePercent(15);
                PdfPCell celdalogo=new PdfPCell(logo);
                celdalogo.setBorderColor(BaseColor.WHITE);
                celdalogo.setColspan(4);
                celdalogo.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablaheader.addCell(celdalogo);
            }catch (SQLException ex) {
                Logger.getLogger(PedidoPDF.class.getName()).log(Level.SEVERE, null, ex);
            }
            Font a=new Font(FontFamily.HELVETICA,18,Font.BOLD,BaseColor.BLACK);
            PdfPCell celdatitulo = new PdfPCell(new Paragraph("PEDIDO PIZZAS",a));
            celdatitulo.setColspan(2);
            celdatitulo.setBorderColor(BaseColor.WHITE);
            tablaheader.addCell(celdatitulo);
            PdfPCell celdafecha = new PdfPCell(new Paragraph("Fecha: "+LocalDate.now(),f));
            celdafecha.setBorderColor(BaseColor.WHITE);
            tablaheader.addCell(celdafecha);
            PdfPCell celdapedido= new PdfPCell(new Paragraph("N° Pedido: "+idpedido,f));
            celdapedido.setBorderColor(BaseColor.WHITE);
            tablaheader.addCell(celdapedido);
            vacio.setColspan(4);
            vacio.setBorder(Rectangle.BOTTOM);
            tablaheader.addCell(vacio);
        return tablaheader;
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
            Logger.getLogger(PedidoPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
