/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.utils.ticket;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import xochiltapp.model.Compra;
import xochiltapp.model.CorteCaja;
import xochiltapp.model.DetalleCompra;
import xochiltapp.model.DetalleCorte;
import xochiltapp.model.DetallePedidoRenta;
import xochiltapp.model.DetalleVenta;
import xochiltapp.model.PedidoRenta;
import xochiltapp.model.Venta;
import xochiltapp.dialog.*;

/**
 *
 * @author afelipelc
 */
public final class GeneraTicket {

    static String imagen = "ticket\\logo.jpg";
    static String nombreArchivo = "ticket\\printticket.pdf";
    static Font fuente = new Font(Font.FontFamily.HELVETICA, 9);
    static Font fuenteDos = new Font(Font.FontFamily.HELVETICA, 11);
    static Font negrita = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    static Font negritaDos = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static boolean TicketVenta(Venta venta, float suPago, float suCambio) {
        try {
            //Tabla de contenidos
            float altotabla = 125f;
            float[] columnas = {30, 110, 50};
            PdfPTable table = new PdfPTable(3);
            //able.DefaultCell.Border = 0;
            table.setHorizontalAlignment(0);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(columnas, new Rectangle(190f, 20f));

            PdfPCell cellderecha = new PdfPCell();
            cellderecha.setHorizontalAlignment(2); //0=Left, 1=Centre, 2=Right
            cellderecha.setBorder(0);
            cellderecha.setLeading(0.5f, 0.8f);
            cellderecha.setPaddingRight(4f);

            PdfPCell cellizquierda = new PdfPCell();
            cellizquierda.setLeading(0.5f, 0.8f);
            cellizquierda.setBorder(0);
            table.addCell(new Phrase("Cant :", fuente));
            table.addCell(new Phrase("Descripción", fuente));
            table.addCell(new Phrase("Importe $", fuente));

            //aqui agregar cada elemento de la orden
            String descripcionprod = "";
            for (DetalleVenta item : venta.getDetallesVenta()) {
                cellderecha.setPhrase(new Phrase(item.getCantidad() + "", fuente));
                table.addCell(cellderecha);
                if (item.getProducto().getCategoria().equals("Arreglos")) {
                    descripcionprod = "Arreglo " + item.getProducto().getConcepto();
                } else {
                    descripcionprod = item.getProducto().getConcepto();
                }

                //si rebasa mas de una linea la descripcion
                if (descripcionprod.length() > 19) {
                    altotabla += 12;
                }
                cellizquierda.setPhrase(new Phrase(descripcionprod, fuente));
                table.addCell(cellizquierda);
                cellderecha.setPhrase(new Phrase(DecimalFormat.getCurrencyInstance().format(item.getImporte()), fuente));
                table.addCell(cellderecha);
            }
            //obtener el alto de la tabla
            altotabla += table.getRows().size() * 12;

            //dar forma al papel
            // 10> margin top, alto tabla detalles, 50 > alto tabla totales
            // 16>> nota de Gracias
            //12 * cada nueva linea agregada
            float altopapel = 10f + altotabla + 50f + 16f;
            float auxresto = 0f;
            //System.out.println("Alto talbla " + altopapel);
            //esto era para dar tamaño ideal
/*
             if (altopapel < 830) {
             auxresto = 830 - altopapel;
             altopapel += auxresto;
             }
             */

            Document document = new Document(new Rectangle(200f, altopapel));
            document.setMargins(3, 2, 10, 3);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));

            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //writer.setMargins(0, 0, 0, 1);
            //PdfWriter.getInstance(document, baos);
            document.open();            
            Image logo = Image.getInstance(imagen);
            logo.scalePercent(10, 10);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            Paragraph encabezado = new Paragraph("Domicilio aqui", fuente);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setLeading(1f, 1f);
            document.add(encabezado);
            Paragraph textoizquierda = new Paragraph("No. venta: " + venta.getIdVenta() + "", fuente);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);

            textoizquierda = new Paragraph("Fecha: " + dateFormat.format(venta.getFecha()), fuente);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);

            encabezado = new Paragraph("________________________________________________________________________________", new Font(Font.FontFamily.COURIER, 4, Font.BOLD));
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setLeading(1f, 1f);
            document.add(encabezado);

            //agregar tabla de detalles
            document.add(table);
            document.add(encabezado); //agregar otra linea

            //tabla de totales
            float[] ctotales = {70, 10, 50};
            PdfPTable totales = new PdfPTable(3);
            Rectangle rectotales = new Rectangle(130f, 50f);
            totales.setWidthPercentage(ctotales, rectotales);
            totales.setHorizontalAlignment(2);
            totales.getDefaultCell().setBorder(0); //  .setDefaultCell.Border = 0;
            cellizquierda.setPhrase(new Phrase("TOTAL:", fuente));
            totales.addCell(cellizquierda);
            totales.addCell("");
            cellderecha.setPhrase(new Phrase(DecimalFormat.getCurrencyInstance().format(venta.getImporte()), fuente));
            totales.addCell(cellderecha);
            //su pago
            cellizquierda.setPhrase(new Phrase("Su pago:", fuente));
            totales.addCell(cellizquierda);
            totales.addCell("");
            cellderecha.setPhrase(new Phrase(DecimalFormat.getCurrencyInstance().format(suPago), fuente));
            totales.addCell(cellderecha);
            //su cambio
            cellizquierda.setPhrase(new Phrase("Su cambio:", fuente));
            totales.addCell(cellizquierda);
            totales.addCell("");
            cellderecha.setPhrase(new Phrase(DecimalFormat.getCurrencyInstance().format(suCambio), fuente));
            totales.addCell(cellderecha);
            document.add(totales);

            encabezado = new Paragraph("\n¡GRACIAS, VUELVA PRONTO!", fuente);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setLeading(1f, 1f);
            document.add(encabezado);

            //corte papel
            Paragraph Corte = new Paragraph("", fuente);
            Corte.setLeading(0.5f, 0.5f);
            document.add(Corte);
            //cerrar archivo
            document.close();
            writer.close();

            //System.out.println("Alto doc: " + document.getPageSize().getHeight()+"");
            /*
             FileInputStream psStream = null;  
             try {  
             psStream = new FileInputStream(nombreArchivo);  
             } catch (FileNotFoundException ffne) {  
             ffne.printStackTrace();  
             }  
             if (psStream == null) {  
             return false;  
             }  
            
             DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
             PrintService service = PrintServiceLookup.lookupDefaultPrintService();
             Doc myDoc = new SimpleDoc(psStream, flavor, null);               
             DocPrintJob job = service.createPrintJob();

             PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
             DocAttributeSet das = new HashDocAttributeSet();
             pras.add(new Copies(1));
            
             try {
             job.print(myDoc, pras);
             } catch (PrintException ex) {
             Logger.getLogger(GeneraTicket.class.getName()).log(Level.SEVERE, null, ex);
             }
             */
            return true;
        } catch (DocumentException | IOException ex) {
          FXDialog.showMessageDialog("Error: " + ex.getMessage() + " >> " + ex.getStackTrace(), "Documento", Message.ERROR);
          return false;
        }
    }

    public static boolean TicketPedidoRenta(PedidoRenta pedidoRenta, float suPago, float suCambio) {
        try {
            //Tabla de contenidos
            float altotabla = 125f;
            float[] columnas = {30, 110, 50};
            PdfPTable table = new PdfPTable(3);
            //table.DefaultCell.Border = 0;
            table.setHorizontalAlignment(0);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(columnas, new Rectangle(190f, 20f));

            PdfPCell cellderecha = new PdfPCell();
            cellderecha.setHorizontalAlignment(2); //0=Left, 1=Centre, 2=Right
            cellderecha.setBorder(0);
            cellderecha.setLeading(0.5f, 0.8f);
            cellderecha.setPaddingRight(4f);

            PdfPCell cellizquierda = new PdfPCell();
            cellizquierda.setLeading(0.5f, 0.8f);
            cellizquierda.setBorder(0);

            table.addCell(new Phrase("Cant", fuenteDos));
            table.addCell(new Phrase("Descripción", fuenteDos));
            PdfPCell celImporte = new PdfPCell(new Phrase("Importe", fuenteDos));
            celImporte.setHorizontalAlignment(2);
            celImporte.setBorder(0);
            table.addCell(celImporte);

            //aqui agregar cada elemento de la orden
            String descripcionprod = "";
            for (DetallePedidoRenta item : pedidoRenta.getDetallesPedidoRenta()) {
                cellderecha.setPhrase(new Phrase(item.getCantidad() + "", fuenteDos));
                table.addCell(cellderecha);
                if (item.getProducto().getCategoria().equals("Arreglos")) {
                    descripcionprod = "Arreglo " + item.getProducto().getConcepto();
                } else if (item.getProducto().getCategoria().equals("Bases")) {
                    descripcionprod = "Base " + item.getProducto().getConcepto();
                } else {
                    descripcionprod = item.getProducto().getConcepto();
                }
                //si rebasa mas de una linea la descripcion
                if (descripcionprod.length() > 19) {
                    altotabla += 12;
                }
                cellizquierda.setPhrase(new Phrase(descripcionprod, fuenteDos));
                table.addCell(cellizquierda);
                cellderecha.setPhrase(new Phrase(DecimalFormat.getCurrencyInstance().format(item.getImporte()), fuenteDos));
                table.addCell(cellderecha);
            }
            //obtener el alto de la tabla
            altotabla += table.getRows().size() * 12;
            if (pedidoRenta.isPedido()) {
                altotabla += 13;
            }
            //dar forma al papel
            // 10> margin top, alto tabla detalles, 50 > alto tabla totales
            // 30>> nota de firma y aviso

            //12 * cada nueva linea agregada
            float altopapel = 10f + altotabla + 50f + 30f + 62f;
            //float auxresto = 0f;
            //System.out.println("Alto talbla " + altopapel);
            //esto era para dar tama;o ideal
/*
             if (altopapel < 830) {
             auxresto = 830 - altopapel;
             altopapel += auxresto;
             }
             */
            Document document = new Document(PageSize.LETTER, 30, 30, 20, 30);
            //document.setMargins(1, 2, 10, 1);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //writer.setMargins(0, 0, 0, 1);
            //PdfWriter.getInstance(document, baos);
            document.open();
            Image logo = Image.getInstance(imagen);
            logo.scalePercent(15, 15);
            logo.setAlignment(Element.ALIGN_RIGHT);
            document.add(logo);
            Paragraph encabezado = new Paragraph("Bernardo Moral García, Tel: 244-445-6230, cel: 244-120-6815 ", fuenteDos);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setLeading(1f, 1f);
            document.add(encabezado);
            String tipo = "";
            if (pedidoRenta.isPedido()) {
                tipo = "Pedido";
            }
            if (pedidoRenta.isRenta()) {
                tipo = "Renta";
            }
            Paragraph textoizquierda = new Paragraph("No. " + tipo + ": " + pedidoRenta.getIdPedidoRenta() + "", fuenteDos);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);

            textoizquierda = new Paragraph("Cliente: " + pedidoRenta.getCliente().getNombre(), fuenteDos);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);
            textoizquierda = new Paragraph("Dom. Entrega: " + pedidoRenta.getDireccionEntrega(), fuenteDos);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);

            if (pedidoRenta.isPedido()) {
                textoizquierda = new Paragraph("Recibe: " + pedidoRenta.getNombreRecibe(), fuenteDos);
                textoizquierda.setLeading(1f, 1f);
                document.add(textoizquierda);
            }
            textoizquierda = new Paragraph("Solicitud: " + dateFormat.format(pedidoRenta.getFechaSolicitud()), fuenteDos);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);
            if (pedidoRenta.getFechaEntrega() != null) {
                textoizquierda = new Paragraph("Entregar: " + dateFormat.format(pedidoRenta.getFechaEntrega()), fuenteDos);
                textoizquierda.setLeading(1f, 1f);
                document.add(textoizquierda);
            }
            //System.out.print(dateFormat.format(pedidoRenta.getFechaEntrega()));
            String estado = "Pendiente de entrega";
            if (pedidoRenta.isEnviado()) {
                estado = "Enviado para entrega";
            }
            if (pedidoRenta.isEntregado()) {
                estado = "Entregado";
            }
            if (pedidoRenta.isDevuelto()) {
                estado = "Entregado";
            }
            if (pedidoRenta.isCancelado()) {
                estado = "Cancelado";
            }
            textoizquierda = new Paragraph("Estado: " + estado, fuenteDos);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);
            if (pedidoRenta.getFechaEntregado() != null) {
                textoizquierda = new Paragraph("Entregado el: " + dateFormat.format(pedidoRenta.getFechaEntrega()), fuenteDos);
                textoizquierda.setLeading(1f, 1f);
                document.add(textoizquierda);
            }
            encabezado = new Paragraph("________________________________________________________________________________", new Font(Font.FontFamily.COURIER, 4, Font.BOLD));
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setLeading(1f, 1f);
            document.add(encabezado);
            //agregar tabla de detalles
            document.add(table);
            document.add(encabezado); //agregar otra linea
            //tabla de totales
            float[] ctotales = {50, 20};
            PdfPTable totales = new PdfPTable(2);
            Rectangle rectotales = new Rectangle(130f, 50f);
            totales.setWidthPercentage(ctotales, rectotales);
            totales.setHorizontalAlignment(2);
            totales.getDefaultCell().setBorder(0); //  .setDefaultCell.Border = 0;
            cellizquierda.setPhrase(new Phrase("TOTAL:", negritaDos));
            cellizquierda.setHorizontalAlignment(2);
            totales.addCell(cellizquierda);
            cellderecha.setPhrase(new Phrase(DecimalFormat.getCurrencyInstance().format(pedidoRenta.getImporte()), fuenteDos));
            totales.addCell(cellderecha);
            //su abono
            cellizquierda.setPhrase(new Phrase("Su Abono:", fuenteDos));
            totales.addCell(cellizquierda);
            cellderecha.setPhrase(new Phrase(DecimalFormat.getCurrencyInstance().format(pedidoRenta.getAbono()), fuenteDos));
            totales.addCell(cellderecha);
            //su adeudo
            cellizquierda.setPhrase(new Phrase("A cuenta:", fuenteDos));
            totales.addCell(cellizquierda);
            cellderecha.setPhrase(new Phrase(DecimalFormat.getCurrencyInstance().format((pedidoRenta.getImporte() - pedidoRenta.getAbono())), fuente));
            totales.addCell(cellderecha);
            //su cambio
            cellizquierda.setPhrase(new Phrase("Su cambio:", fuenteDos));
            totales.addCell(cellizquierda);
            cellderecha.setPhrase(new Phrase(DecimalFormat.getCurrencyInstance().format(suCambio), fuenteDos));
            totales.addCell(cellderecha);
            document.add(totales);
//            encabezado = new Paragraph("\nFIRMA DEL CLIENTE", fuente);
//            encabezado.setAlignment(Element.ALIGN_CENTER);
//            encabezado.setLeading(1f, 1f);
//            document.add(encabezado);

//            encabezado = new Paragraph("\nPresente este comprobante al recibir su " + tipo.toLowerCase() + ".", fuente);
//            encabezado.setAlignment(Element.ALIGN_CENTER);
//            encabezado.setLeading(1f, 1f);
//            document.add(encabezado);
            encabezado = new Paragraph("\n\nNOTA: "+pedidoRenta.getNota(), fuenteDos);
            encabezado.setAlignment(Element.ALIGN_JUSTIFIED);
            encabezado.setLeading(1f, 1f);
            document.add(encabezado);
            //corte papel
            Paragraph Corte = new Paragraph("", fuenteDos);
            Corte.setLeading(0.5f, 0.5f);
            document.add(Corte);
            //cerrar archivo
            document.close();
            writer.close();
            //System.out.println("Alto doc: " + document.getPageSize().getHeight()+"");
            /*
             FileInputStream psStream = null;  
             try {  
             psStream = new FileInputStream(nombreArchivo);  
             } catch (FileNotFoundException ffne) {  
             ffne.printStackTrace();  
             }  
             if (psStream == null) {  
             return false;  
             }              
             DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
             PrintService service =
             PrintServiceLookup.lookupDefaultPrintService();
             Doc myDoc = new SimpleDoc(psStream, flavor, null);              
             DocPrintJob job = service.createPrintJob();
             PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
             DocAttributeSet das = new HashDocAttributeSet();
             pras.add(new Copies(1));            
             try {
             job.print(myDoc, pras);
             } catch (PrintException ex) {
             Logger.getLogger(GeneraTicket.class.getName()).log(Level.SEVERE, null, ex);
             }
             */
            return true;
        } catch (DocumentException | IOException ex) {
          FXDialog.showMessageDialog("Error: " + ex.getMessage() + " >> " + ex.getStackTrace(), "Documento", Message.ERROR);
          return false;
        }
    }

    public static boolean TicketCompra(Compra compra) {
        try {
            //Tabla de contenidos
            float altotabla = 125f;
            float[] columnas = {30, 110, 50};
            PdfPTable table = new PdfPTable(3);
            //able.DefaultCell.Border = 0;
            table.setHorizontalAlignment(0);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(columnas, new Rectangle(190f, 20f));
            PdfPCell cellderecha = new PdfPCell();
            cellderecha.setHorizontalAlignment(2); //0=Left, 1=Centre, 2=Right
            cellderecha.setBorder(0);
            cellderecha.setLeading(0.5f, 0.8f);
            cellderecha.setPaddingRight(4f);
            PdfPCell cellizquierda = new PdfPCell();
            cellizquierda.setLeading(0.5f, 0.8f);
            cellizquierda.setBorder(0);
            table.addCell(new Phrase("Cant", fuente));
            table.addCell(new Phrase("Descripción", fuente));
            table.addCell(new Phrase("Importe", fuente));
            //aqui agregar cada elemento de la orden
            String descripcionprod = "";
            for (DetalleCompra item : compra.getDetallesCompra()) {
                cellderecha.setPhrase(new Phrase(item.getCantidad() + "", fuente));
                table.addCell(cellderecha);
                if (item.getProducto().getCategoria().equals("Arreglos")) {
                    descripcionprod = "Arreglo " + item.getProducto().getConcepto();
                } else {
                    descripcionprod = item.getProducto().getConcepto();
                }
                //si rebasa mas de una linea la descripcion
                if (descripcionprod.length() > 19) {
                    altotabla += 12;
                }
                cellizquierda.setPhrase(new Phrase(descripcionprod, fuente));
                table.addCell(cellizquierda);
                cellderecha.setPhrase(new Phrase(DecimalFormat.getCurrencyInstance().format(item.getImporte()), fuente));
                table.addCell(cellderecha);
            }
            //obtener el alto de la tabla
            altotabla += table.getRows().size() * 12;
            //dar forma al papel
            // 10> margin top, alto tabla detalles, 50 > alto tabla totales
            // 16>> nota de Gracias
            //12 * cada nueva linea agregada
            float altopapel = 10f + altotabla + 50f + 16f;
            float auxresto = 0f;
            //System.out.println("Alto talbla " + altopapel);
            //esto era para dar tama;o ideal
/*
             if (altopapel < 830) {
             auxresto = 830 - altopapel;
             altopapel += auxresto;
             }
             */
            Document document = new Document(new Rectangle(200f, altopapel));
            document.setMargins(1, 2, 10, 1);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //writer.setMargins(0, 0, 0, 1);
            //PdfWriter.getInstance(document, baos);
            document.open();
            Image logo = Image.getInstance(imagen);
            logo.scalePercent(10, 10);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
            Paragraph encabezado = new Paragraph("Domicilio aqui", fuente);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setLeading(1f, 1f);
            document.add(encabezado);
            Paragraph textoizquierda = new Paragraph("No. compra: " + compra.getIdCompra() + "", fuente);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);
            textoizquierda = new Paragraph("Fecha: " + dateFormat.format(compra.getFecha()), fuente);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);
            encabezado = new Paragraph("________________________________________________________________________________", new Font(Font.FontFamily.COURIER, 4, Font.BOLD));
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setLeading(1f, 1f);
            document.add(encabezado);
            //agregar tabla de detalles
            document.add(table);
            document.add(encabezado); //agregar otra linea
            //tabla de totales
            float[] ctotales = {70, 10, 50};
            PdfPTable totales = new PdfPTable(3);
            Rectangle rectotales = new Rectangle(130f, 50f);
            totales.setWidthPercentage(ctotales, rectotales);
            totales.setHorizontalAlignment(2);
            totales.getDefaultCell().setBorder(0); //  .setDefaultCell.Border = 0;
            cellizquierda.setPhrase(new Phrase("TOTAL:", fuente));
            totales.addCell(cellizquierda);
            totales.addCell("");
            cellderecha.setPhrase(new Phrase(DecimalFormat.getCurrencyInstance().format(compra.getImporte()), fuente));
            totales.addCell(cellderecha);
            document.add(totales);
            encabezado = new Paragraph("\nCONTROL INTERNO", fuente);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setLeading(1f, 1f);
            document.add(encabezado);
            //corte papel
            Paragraph Corte = new Paragraph("", fuente);
            Corte.setLeading(0.5f, 0.5f);
            document.add(Corte);
            //cerrar archivo
            document.close();
            writer.close();
            //System.out.println("Alto doc: " + document.getPageSize().getHeight()+"");
            
             FileInputStream psStream = null;  
             try {  
             psStream = new FileInputStream(nombreArchivo);  
             } catch (FileNotFoundException ffne) {  
             ffne.printStackTrace();  
             }  
             if (psStream == null) {  
             return false;  
             }              
             DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
             PrintService service =
             PrintServiceLookup.lookupDefaultPrintService();
             Doc myDoc = new SimpleDoc(psStream, flavor, null);               
             DocPrintJob job = service.createPrintJob();
             PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
             DocAttributeSet das = new HashDocAttributeSet();
             pras.add(new Copies(1));            
             try {
             job.print(myDoc, pras);
             } catch (PrintException ex) {
             Logger.getLogger(GeneraTicket.class.getName()).log(Level.SEVERE, null, ex);
             }
             
            return true;
        } catch (DocumentException | IOException ex) {
          FXDialog.showMessageDialog("Error: " + ex.getMessage() + " >> " + ex.getStackTrace(), "Documento", Message.ERROR);
          return false;
        }
    }

    public static boolean TicketCorte(CorteCaja corteCaja, List<DetalleCorte> detalles) {
        try {
            //Tabla de contenidos
            float altotabla = 165f;
            float[] columnas = {30, 110, 50};
            PdfPTable table = new PdfPTable(3);
            //able.DefaultCell.Border = 0;
            table.setHorizontalAlignment(0);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(columnas, new Rectangle(190f, 20f));
            PdfPCell cellderecha = new PdfPCell();
            cellderecha.setHorizontalAlignment(2); //0=Left, 1=Centre, 2=Right
            cellderecha.setBorder(0);
            cellderecha.setLeading(0.5f, 0.8f);
            cellderecha.setPaddingRight(4f);
            PdfPCell cellizquierda = new PdfPCell();
            cellizquierda.setLeading(0.5f, 0.8f);
            cellizquierda.setBorder(0);
            table.addCell(new Phrase("Cant", fuente));
            table.addCell(new Phrase("Concepto", fuente));
            table.addCell(new Phrase("Ingreso", fuente));
            //aqui agregar cada elemento de la orden
            String descripcionprod = "";
            for (DetalleCorte item : detalles) {
                cellderecha.setPhrase(new Phrase(item.getCantidad() + "", fuente));
                table.addCell(cellderecha);
                descripcionprod = item.getConcepto();
                //si rebasa mas de una linea la descripcion
                if (descripcionprod.length() > 19) {
                    altotabla += 12;
                }
                cellizquierda.setPhrase(new Phrase(descripcionprod, fuente));
                table.addCell(cellizquierda);
                cellderecha.setPhrase(new Phrase(DecimalFormat.getCurrencyInstance().format(item.getIngreso()), fuente));
                table.addCell(cellderecha);
            }
            //obtener el alto de la tabla
            altotabla += table.getRows().size() * 12;
            //dar forma al papel
            // 10> margin top, alto tabla detalles, 50 > alto tabla totales
            // 16>> nota de Gracias
            //12 * cada nueva linea agregada
            float altopapel = 10f + altotabla + 50f + 16f;
            float auxresto = 0f;
            //System.out.println("Alto talbla " + altopapel);
            //esto era para dar tama;o ideal
/*
             if (altopapel < 830) {
             auxresto = 830 - altopapel;
             altopapel += auxresto;
             }
             */
            Document document = new Document(new Rectangle(200f, altopapel));
            document.setMargins(1, 2, 10, 1);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));            
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //writer.setMargins(0, 0, 0, 1);
            //PdfWriter.getInstance(document, baos);
            document.open();
            Image logo = Image.getInstance(imagen);
            logo.scalePercent(10, 10);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
            Paragraph encabezado = new Paragraph("Domicilio aqui", fuente);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setLeading(1f, 1f);
            document.add(encabezado);
            Paragraph textoizquierda = new Paragraph("No. Corte: " + corteCaja.getIdCorte() + "", fuente);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);
            textoizquierda = new Paragraph("Apertura: " + dateFormat.format(corteCaja.getFechaHoraApertura()), fuente);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);
            textoizquierda = new Paragraph("Cierre: " + dateFormat.format(corteCaja.getFechaHoraCierre()), fuente);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);
            textoizquierda = new Paragraph("Empleado abre: " + corteCaja.getEmpleadoAbre(), fuente);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);
            textoizquierda = new Paragraph("Empleado cierra: " + corteCaja.getEmpleadoCierra(), fuente);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);
            encabezado = new Paragraph("________________________________________________________________________________", new Font(Font.FontFamily.COURIER, 4, Font.BOLD));
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setLeading(1f, 1f);
            document.add(encabezado);
            textoizquierda = new Paragraph("Monto Inicio: " + DecimalFormat.getCurrencyInstance().format(corteCaja.getMontoInicial()), negrita);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);
            textoizquierda = new Paragraph("Ingreso: " + DecimalFormat.getCurrencyInstance().format(corteCaja.getMontoIngreso()), negrita);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);
            textoizquierda = new Paragraph("Monto Cierre: " + DecimalFormat.getCurrencyInstance().format(corteCaja.getMontoCierre()), negrita);
            textoizquierda.setLeading(1f, 1f);
            document.add(textoizquierda);
            document.add(encabezado); //agregar otra linea
            //agregar tabla de detalles
            document.add(table);
            document.add(encabezado); //agregar otra linea
            //tabla de totales
            float[] ctotales = {70, 10, 50};
            PdfPTable totales = new PdfPTable(3);
            Rectangle rectotales = new Rectangle(130f, 50f);
            totales.setWidthPercentage(ctotales, rectotales);
            totales.setHorizontalAlignment(2);
            totales.getDefaultCell().setBorder(0); //  .setDefaultCell.Border = 0;
            cellizquierda.setPhrase(new Phrase("Ingreso Total:", fuente));
            totales.addCell(cellizquierda);
            totales.addCell("");
            cellderecha.setPhrase(new Phrase(DecimalFormat.getCurrencyInstance().format(corteCaja.getMontoIngreso()), fuente));
            totales.addCell(cellderecha);
            document.add(totales);
            encabezado = new Paragraph("\nCONTROL INTERNO", fuente);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setLeading(1f, 1f);
            document.add(encabezado);
            //corte papel
            Paragraph Corte = new Paragraph("", fuente);
            Corte.setLeading(0.5f, 0.5f);
            document.add(Corte);
            //cerrar archivo
            document.close();
            writer.close();
            //System.out.println("Alto doc: " + document.getPageSize().getHeight()+"");
            /*
             FileInputStream psStream = null;  
             try {  
             psStream = new FileInputStream(nombreArchivo);  
             } catch (FileNotFoundException ffne) {  
             ffne.printStackTrace();  
             }  
             if (psStream == null) {  
             return false;  
             }              
             DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
             PrintService service =
             PrintServiceLookup.lookupDefaultPrintService();
             Doc myDoc = new SimpleDoc(psStream, flavor, null);               
             DocPrintJob job = service.createPrintJob();
             PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
             DocAttributeSet das = new HashDocAttributeSet();
             pras.add(new Copies(1));            
             try {
             job.print(myDoc, pras);
             } catch (PrintException ex) {
             Logger.getLogger(GeneraTicket.class.getName()).log(Level.SEVERE, null, ex);
             }
             */
            return true;
        } catch (DocumentException | IOException ex) {
          FXDialog.showMessageDialog("Error: " + ex.getMessage() + " >> " + ex.getStackTrace(), "Documentos", Message.ERROR);
          return false;
        }
    }
    public static void printTicket(){
     FileInputStream psStream = null;  
      try {  
         psStream = new FileInputStream(nombreArchivo);  
      } catch (FileNotFoundException ffne) {  
         ffne.printStackTrace();  
      }  
      if (psStream == null) {  
        FXDialog.showMessageDialog("No esta el archivo", "Imprimir", Message.INFORMATION);
      }              
      DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
      PrintService service = (PrintService) PrintServiceLookup.lookupDefaultPrintService();
      Doc myDoc = new SimpleDoc(psStream, flavor, null);
      DocPrintJob job = service.createPrintJob();
      PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
      DocAttributeSet das = new HashDocAttributeSet();
      pras.add(new Copies(1));            
      try {
        job.print(myDoc, pras);
      } catch (PrintException ex) {
        Logger.getLogger(GeneraTicket.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
}
