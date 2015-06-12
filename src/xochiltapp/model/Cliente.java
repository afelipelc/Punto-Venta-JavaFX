/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.model;

/**
 *
 * @author afelipelc
 */
public class Cliente {
    private int idCliente;
    private String nombre="";
    private String domicilio="";
    private String referenciaDomicilio="";
    private String telefono="";
    private String telefono2="";
    
    public Cliente()
    {
        
    }
    
    private Cliente(String nombre, String domicilio, String referencia, String telefono1, String telefono2)
    {
        this.nombre  = nombre;
        this.domicilio = domicilio;
        this.referenciaDomicilio = referencia;
        this.telefono = telefono1;
        this.telefono2 = telefono2;
    }

    /**
     * @return the idCliente
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the domicilio
     */
    public String getDomicilio() {
        return domicilio;
    }

    /**
     * @param domicilio the domicilio to set
     */
    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    /**
     * @return the referenciaDomicilio
     */
    public String getReferenciaDomicilio() {
        return referenciaDomicilio;
    }

    /**
     * @param referenciaDomicilio the referenciaDomicilio to set
     */
    public void setReferenciaDomicilio(String referenciaDomicilio) {
        this.referenciaDomicilio = referenciaDomicilio;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the telefono2
     */
    public String getTelefono2() {
        return telefono2;
    }

    /**
     * @param telefono2 the telefono2 to set
     */
    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }
    
    
}
