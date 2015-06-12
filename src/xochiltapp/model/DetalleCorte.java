/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.model;

/**
 *
 * @author afelipelc
 */
public class DetalleCorte {
    private String concepto;
    private int cantidad;
    private float ingreso;

    /**
     * @return the concepto
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * @param concepto the concepto to set
     */
    public void setConcepto(String concepto) {
        this.concepto = concepto;
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
     * @return the ingreso
     */
    public float getIngreso() {
        return ingreso;
    }

    /**
     * @param ingreso the ingreso to set
     */
    public void setIngreso(float ingreso) {
        this.ingreso = ingreso;
    }
}
