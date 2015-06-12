/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.model;

/**
 *
 * @author afelipelc
 */
public class ResumenIngreso {
    private long primerIngreso;
    private long ultimoIngreso;
    private float ingresoTotal;

    /**
     * @return the primerIngreso
     */
    public long getPrimerIngreso() {
        return primerIngreso;
    }

    /**
     * @param primerIngreso the primerIngreso to set
     */
    public void setPrimerIngreso(long primerIngreso) {
        this.primerIngreso = primerIngreso;
    }

    /**
     * @return the ultimoIngreso
     */
    public long getUltimoIngreso() {
        return ultimoIngreso;
    }

    /**
     * @param ultimoIngreso the ultimoIngreso to set
     */
    public void setUltimoIngreso(long ultimoIngreso) {
        this.ultimoIngreso = ultimoIngreso;
    }

    /**
     * @return the ingresoTotal
     */
    public float getIngresoTotal() {
        return ingresoTotal;
    }

    /**
     * @param ingresoTotal the ingresoTotal to set
     */
    public void setIngresoTotal(float ingresoTotal) {
        this.ingresoTotal = ingresoTotal;
    }
}
