/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Funciones;

import com.itextpdf.text.BaseColor;
import java.io.File;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
    BasededatosManager bd=new BasededatosManager();
    PdfPCell vacio=new PdfPCell(new Paragraph(" "));
    
    public PedidoPDF(String id,java.awt.Image imagen,String directorio) throws FileNotFoundException, DocumentException, IOException{
        this.idpedido=id;
        this.directorio=directorio;
        this.archivo=new File(directorio);
        Document documento=new Document(PageSize.A6);
        FileOutputStream ficheroPdf=new FileOutputStream(archivo);
        PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
        documento.setMargins(0,0,1,0);
        documento.open();
        int width = imagen.getWidth(null);
        int height = imagen.getHeight(null);
        int type = BufferedImage.TYPE_3BYTE_BGR;
        BufferedImage bi = new BufferedImage(width, height, type);
        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(imagen, 0, 0, null);
        g2d.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        Image image=Image.getInstance(imageInByte);
        PdfPTable tablaimagen=new PdfPTable(1);
        tablaimagen.setWidths(new int[]{350});
        tablaimagen.addCell(image);
        documento.add(tablaimagen);
        documento.close();
        ejecutarPDF();
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
