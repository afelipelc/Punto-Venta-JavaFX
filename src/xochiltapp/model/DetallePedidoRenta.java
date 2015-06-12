/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import xochiltapp.datasource.InventarioDataSource;

/**
 *
 * @author afelipelc
 */
public class DetallePedidoRenta {
    
    private SimpleLongProperty idDetallePedidoRenta = new SimpleLongProperty();
    private SimpleLongProperty idPedidoRenta = new SimpleLongProperty();
    private SimpleIntegerProperty idProducto = new SimpleIntegerProperty();
    private Producto producto;
    private SimpleStringProperty concepto = new SimpleStringProperty();
    private SimpleIntegerProperty cantidad = new SimpleIntegerProperty();
    private SimpleFloatProperty importe = new SimpleFloatProperty();
    private SimpleFloatProperty precioUnit = new SimpleFloatProperty();

    /**
     * @return the idDetalle
     */
    public long getIdDetallePedidoRenta() {
        return idDetallePedidoRenta.get();
    }

    /**
     * @param idDetalle the idDetalle to set
     */
    public void setIdDetallePedidoRenta(long idDetallePedidoRenta) {
        this.idDetallePedidoRenta.set(idDetallePedidoRenta);
    }

    /**
     * @return the idPedidoRenta
     */
    public long getIdPedidoRenta() {
        return idPedidoRenta.get();
    }

    /**
     * @param idVenta the idPedidoRenta to set
     */
    public void setIdPedidoRenta(long idPedidoRenta) {
        this.idPedidoRenta.set(idPedidoRenta);
    }

    /**
     * @return the idProducto
     */
    public int getIdProducto() {
        return idProducto.get();
    }

    /**
     * @param idProducto the idProducto to set
     */
    public void setIdProducto(int idProducto) {
        this.idProducto.set(idProducto);

    }

    /**
     * @return the producto
     */
    public Producto getProducto() {
        if (idProducto.get() > 0 && producto == null) {
            this.producto = InventarioDataSource.findProductoById(idProducto.get());
            if (this.producto != null) {
                this.setPrecioUnit(producto.getPrecio());
                this.concepto.set(producto.getConcepto());
            }
        }

        return producto;
    }

    /**
     * @param producto the producto to set
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
        if (this.producto != null) {
            this.setPrecioUnit(producto.getPrecio());
            this.idProducto.set(producto.getIdProducto());
            this.concepto.set(producto.getConcepto());
        }
    }

    /**
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad.get();
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
        if (this.producto != null) {
            this.setImporte(cantidad * precioUnit.get());
        }
    }

    /**
     * @return the importe
     */
    public float getImporte() {
        return importe.get();
    }

    /**
     * @param importe the importe to set
     */
    public void setImporte(float importe) {
        this.importe.set(importe);
    }

    /**
     * @return the concepto
     */
    public String getConcepto() {
        return concepto.get();
    }

    /**
     * @param concepto the concepto to set
     */
    public void setConcepto(String concepto) {
        this.concepto.set(concepto);
    }

    /**
     * @return the precioUnit
     */
    public float getPrecioUnit() {
        return precioUnit.get();
    }

    /**
     * @param precioUnit the precioUnit to set
     */
    public void setPrecioUnit(float precioUnit) {
        this.precioUnit.set(precioUnit);
    }

    //methods necesary for update TableView from objects
    public SimpleLongProperty idDetallePedidoRentaProperty() {
        return this.idDetallePedidoRenta;
    }

    public SimpleLongProperty idPedidoRentaProperty() {
        return this.idPedidoRenta;
    }

    public SimpleIntegerProperty idProductoProperty() {
        return this.idProducto;
    }

    public SimpleStringProperty conceptoProperty() {
        if(this.producto == null && this.getIdProducto() > 0)
        {
            setProducto(InventarioDataSource.findProductoById(getIdProducto()));
        }
        return this.concepto;
    }

    public SimpleIntegerProperty cantidadProperty() {
        return this.cantidad;
    }

    public SimpleFloatProperty importeProperty() {
        return this.importe;
    }

    public SimpleFloatProperty precioUnitProperty() {
        return this.precioUnit;
    }
}
