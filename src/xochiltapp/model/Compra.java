/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author afelipelc
 */
public class Compra {
    private long idCompra;
    private Date fecha;
    private float importe;
    private String empleado;
    private List<DetalleCompra> detallesCompra = new ArrayList<>();

    /**
     * @return the idCompra
     */
    public long getIdCompra() {
        return idCompra;
    }

    /**
     * @param idCompra the idCompra to set
     */
    public void setIdCompra(long idCompra) {
        this.idCompra = idCompra;
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
     * @return the detallesCompra
     */
    public List<DetalleCompra> getDetallesCompra() {
        return detallesCompra;
    }

    /**
     * @param detallesCompra the detallesCompra to set
     */
    public void setDetallesCompra(List<DetalleCompra> detallesCompra) {
        this.detallesCompra = detallesCompra;
    }
    
    public void addDetalle(DetalleCompra detalle)
    {
        boolean existe = false;
        for(DetalleCompra item : this.detallesCompra)
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
            this.detallesCompra.add(detalle);
    }
    
}
