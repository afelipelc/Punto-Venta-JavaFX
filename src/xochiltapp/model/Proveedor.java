/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.model;

import java.util.Date;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author afelipelc
 */
public class Proveedor {

    private int idProveedor;
    private SimpleStringProperty nombre = new SimpleStringProperty();
    private SimpleStringProperty telefono = new SimpleStringProperty();
    private SimpleStringProperty direccion = new SimpleStringProperty();
    private SimpleStringProperty telCasa = new SimpleStringProperty();
    private SimpleStringProperty telNegocio = new SimpleStringProperty();
    private SimpleStringProperty telcelular = new SimpleStringProperty();
    //utils for Proveedor - Producto relationship
    private SimpleFloatProperty precioProducto = new SimpleFloatProperty();
    private Date fechaActualizacionProd;
    private int idProveedorProducto;

    public Proveedor()
    {
        this.telefono.set("");
    }
    /**
     * @return the idProveedor
     */
    public int getIdProveedor() {
        return idProveedor;
    }

    /**
     * @param idProveedor the idProveedor to set
     */
    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre.get();
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono.get();
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion.get();
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion.set(direccion);
    }

    /**
     * @return the telCasa
     */
    public String getTelCasa() {
        return telCasa.get();
    }

    /**
     * @param telCasa the telCasa to set
     */
    public void setTelCasa(String telCasa) {
        this.telCasa.set(telCasa);
        if (telCasa != null && !telCasa.equals("")) {
            this.telefono.set(telefono.get() + " # " + telCasa + "  ");
        }
    }

    /**
     * @return the telNegocio
     */
    public String getTelNegocio() {
        return telNegocio.get();
    }

    /**
     * @param telNegocio the telNegocio to set
     */
    public void setTelNegocio(String telNegocio) {
        this.telNegocio.set(telNegocio);

        if (telNegocio != null && !telNegocio.equals("")) {
            this.telefono.set(telefono.get() + " # " + telNegocio + " | ");
        }
    }

    /**
     * @return the telcelular
     */
    public String getTelcelular() {
        return telcelular.get();
    }

    /**
     * @param telcelular the telcelular to set
     */
    public void setTelcelular(String telcelular) {
        this.telcelular.set(telcelular);
        if (telcelular != null && !telcelular.equals("")) {
            this.telefono.set(telefono.get() + " # " + telcelular + " | ");
        }
    }

    /**
     * @return the precioProducto
     */
    public float getPrecioProducto() {
        return precioProducto.get();
    }

    /**
     * @param precioProducto the precioProducto to set
     */
    public void setPrecioProducto(float precioProducto) {
        this.precioProducto.set(precioProducto);
    }

    /**
     * @return the fechaActualizacionProd
     */
    public Date getFechaActualizacionProd() {
        return fechaActualizacionProd;
    }

    /**
     * @param fechaActualizacionProd the fechaActualizacionProd to set
     */
    public void setFechaActualizacionProd(Date fechaActualizacionProd) {
        this.fechaActualizacionProd = fechaActualizacionProd;
    }

    /**
     * @return the idProveedorProducto
     */
    public int getIdProveedorProducto() {
        return idProveedorProducto;
    }

    /**
     * @param idProveedorProducto the idProveedorProducto to set
     */
    public void setIdProveedorProducto(int idProveedorProducto) {
        this.idProveedorProducto = idProveedorProducto;
    }

    public SimpleStringProperty nombreProperty() {
        return this.nombre;
    }

    public SimpleStringProperty direccionProperty() {
        return this.direccion;
    }

    public SimpleStringProperty telCasaProperty() {
        return this.telCasa;
    }

    public SimpleStringProperty telNegocioProperty() {
        return this.telNegocio;
    }

    public SimpleStringProperty telcelularProperty() {
        return this.telcelular;
    }
    
    public SimpleFloatProperty precioProductoProperty()
    {
        return this.precioProducto;
    }
}
