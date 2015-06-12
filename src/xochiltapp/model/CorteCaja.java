/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.model;

import java.util.Date;

/**
 *
 * @author afelipelc
 */
public class CorteCaja {
    private int idCorte;
    private float montoInicial;
    private float montoCierre;
    private float montoIngreso;
    private String empleadoAbre;
    private String empleadoCierra;
    private Date fechaHoraApertura;
    private Date fechaHoraCierre;
    private boolean abierta;

    /**
     * @return the idCorte
     */
    public int getIdCorte() {
        return idCorte;
    }

    /**
     * @param idCorte the idCorte to set
     */
    public void setIdCorte(int idCorte) {
        this.idCorte = idCorte;
    }

    /**
     * @return the montoInicial
     */
    public float getMontoInicial() {
        return montoInicial;
    }

    /**
     * @param montoInicial the montoInicial to set
     */
    public void setMontoInicial(float montoInicial) {
        this.montoInicial = montoInicial;
    }

    /**
     * @return the montoFinal
     */
    public float getMontoCierre() {
        return montoCierre;
    }

    /**
     * @param montoFinal the montoFinal to set
     */
    public void setMontoCierre(float montoCierre) {
        this.montoCierre = montoCierre;
    }

    /**
     * @return the montoIngreso
     */
    public float getMontoIngreso() {
        return montoIngreso;
    }

    /**
     * @param montoIngreso the montoIngreso to set
     */
    public void setMontoIngreso(float montoIngreso) {
        this.montoIngreso = montoIngreso;
    }

    /**
     * @return the empleadoAbre
     */
    public String getEmpleadoAbre() {
        return empleadoAbre;
    }

    /**
     * @param empleadoAbre the empleadoAbre to set
     */
    public void setEmpleadoAbre(String empleadoAbre) {
        this.empleadoAbre = empleadoAbre;
    }

    /**
     * @return the empleadoCierra
     */
    public String getEmpleadoCierra() {
        return empleadoCierra;
    }

    /**
     * @param empleadoCierra the empleadoCierra to set
     */
    public void setEmpleadoCierra(String empleadoCierra) {
        this.empleadoCierra = empleadoCierra;
    }

    /**
     * @return the fechaHoraApertura
     */
    public Date getFechaHoraApertura() {
        return fechaHoraApertura;
    }

    /**
     * @param fechaHoraApertura the fechaHoraApertura to set
     */
    public void setFechaHoraApertura(Date fechaHoraApertura) {
        this.fechaHoraApertura = fechaHoraApertura;
    }

    /**
     * @return the fechaHoraCierre
     */
    public Date getFechaHoraCierre() {
        return fechaHoraCierre;
    }

    /**
     * @param fechaHoraCierre the fechaHoraCierre to set
     */
    public void setFechaHoraCierre(Date fechaHoraCierre) {
        this.fechaHoraCierre = fechaHoraCierre;
    }

    /**
     * @return the abierta
     */
    public boolean isAbierta() {
        return abierta;
    }

    /**
     * @param abierta the abierta to set
     */
    public void setAbierta(boolean abierta) {
        this.abierta = abierta;
    }
}
