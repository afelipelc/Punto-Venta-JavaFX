/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.model;

import xochiltapp.datasource.InventarioDataSource;

/**
 *
 * @author afelipelc
 */
public class ComponenteArreglo {
    // fields, getters & setters

    private int idComponente;
    private int cantidad;
    private int idProducto;
    private Producto componente;
    private int idArreglo;
    private String nombreComponente;

    /**
     * @return the idComponente
     */
    public int getIdComponente() {
        return idComponente;
    }

    /**
     * @param idComponente the idComponente to set
     */
    public void setIdComponente(int idComponente) {
        this.idComponente = idComponente;
    }

    /**
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the componente
     */
    public Producto getComponente() {

        if (componente == null && idProducto > 0) {
            componente = InventarioDataSource.findProductoById(idProducto);
            nombreComponente = componente.getConcepto();
        }

        return componente;
    }

    /**
     * @param componente the componente to set
     */
    public void setComponente(Producto componente) {
        this.componente = componente;
        this.nombreComponente = componente.getConcepto();
    }

    /**
     * @return the idProducto
     */
    public int getIdProducto() {
        return idProducto;
    }

    /**
     * @param idProducto the idProducto to set
     */
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * @return the idArreglo
     */
    public int getIdArreglo() {
        return idArreglo;
    }

    /**
     * @param idArreglo the idArreglo to set
     */
    public void setIdArreglo(int idArreglo) {
        this.idArreglo = idArreglo;
    }

    /**
     * @return the nombreComponente
     */
    public String getNombreComponente() {
        if (nombreComponente != null) {
            return nombreComponente;
        } else {
            return getComponente().getConcepto();
        }
    }

    /**
     * @param nombreComponente the nombreComponente to set
     */
    public void setNombreComponente(String nombreComponente) {
        this.nombreComponente = nombreComponente;
    }
}
