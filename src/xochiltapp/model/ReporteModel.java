/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.model;

import java.text.DecimalFormat;

/**
 *
 * @author afelipelc
 */
public class ReporteModel {

    private String periodo;
    private String concepto;
    private float ingreso;
    private float egreso;
    private float saldo;

    private String ingresoSt;
    private String egresoSt;
    private String saldoSt;
    
    /**
     * @return the periodo
     */
    public String getPeriodo() {
        return periodo;
    }

    /**
     * @param periodo the periodo to set
     */
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

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
        this.ingresoSt = DecimalFormat.getCurrencyInstance().format(ingreso);
    }

    /**
     * @return the egreso
     */
    public float getEgreso() {
        return egreso;
    }

    /**
     * @param egreso the egreso to set
     */
    public void setEgreso(float egreso) {
        this.egreso = egreso;
        this.egresoSt = DecimalFormat.getCurrencyInstance().format(egreso);
    }

    /**
     * @return the saldo
     */
    public float getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(float saldo) {
        this.saldo = saldo;
        this.saldoSt = DecimalFormat.getCurrencyInstance().format(saldo);
    }

    /**
     * @return the ingresoSt
     */
    public String getIngresoSt() {
        return ingresoSt;
    }

    /**
     * @return the egresoSt
     */
    public String getEgresoSt() {
        return egresoSt;
    }

    /**
     * @return the saldoSt
     */
    public String getSaldoSt() {
        return saldoSt;
    }
}
