/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author afelipelc
 */
public class Venta {

    private long idVenta;
    private Date fecha;
    private float importe;
    private String empleado;
    private List<DetalleVenta> detallesVenta = new ArrayList<>();

    /**
     * @return the idVenta
     */
    public long getIdVenta() {
        return idVenta;
    }

    /**
     * @param idVenta the idVenta to set
     */
    public void setIdVenta(long idVenta) {
        this.idVenta = idVenta;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the importe
     */
    public float getImporte() {
        return importe;
    }

    /**
     * @param importe the importe to set
     */
    public void setImporte(float importe) {
        this.importe = importe;
    }

    /**
     * @return the empleado
     */
    public String getEmpleado() {
        return empleado;
    }

    /**
     * @param empleado the empleado to set
     */
    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    /**
     * @return the detallesVenta
     */
    public List<DetalleVenta> getDetallesVenta() {
        return detallesVenta;
    }

    /**
     * @param detallesVenta the detallesVenta to set
     */
    public void setDetallesVenta(List<DetalleVenta> detallesVenta) {
        this.detallesVenta = detallesVenta;
    }
    
    public void addDetalle(DetalleVenta detalle)
    {
        boolean existe = false;
        for(DetalleVenta item : this.detallesVenta)
        {
            if(item.getIdProducto() == detalle.getIdProducto())
            {
               //agregar
                item.setCantidad(item.getCantidad() + detalle.getCantidad());
                existe = true;
                break;
                
            }
        }
        if(!existe)
            this.detallesVenta.add(detalle);
    }
}
