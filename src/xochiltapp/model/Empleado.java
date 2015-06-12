/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author afelipelc
 */
public class Empleado {

    private int idEmpleado;
    private SimpleStringProperty nombre = new SimpleStringProperty();
    private String usuario;
    private String password;
    //private String nuevoPassword;
    private boolean administrador;
    private boolean vendedor;
    private boolean consulta;
    private SimpleBooleanProperty activo = new SimpleBooleanProperty();
    private SimpleStringProperty estaActivo = new SimpleStringProperty();

    /**
     * @return the idEmpleado
     */
    public int getIdEmpleado() {
        return idEmpleado;
    }

    /**
     * @param idEmpleado the idEmpleado to set
     */
    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
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
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the administrador
     */
    public boolean isAdministrador() {
        return administrador;
    }

    /**
     * @param administrador the administrador to set
     */
    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }

    /**
     * @return the vendedor
     */
    public boolean isVendedor() {
        return vendedor;
    }

    /**
     * @param vendedor the vendedor to set
     */
    public void setVendedor(boolean vendedor) {
        this.vendedor = vendedor;
    }

    /**
     * @return the consulta
     */
    public boolean isConsulta() {
        return consulta;
    }

    /**
     * @param consulta the consulta to set
     */
    public void setConsulta(boolean consulta) {
        this.consulta = consulta;
    }

    /**
     * @return the activo
     */
    public boolean isActivo() {
        return activo.get();
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(boolean activo) {
        this.activo.set(activo);

        if (activo) {
            this.estaActivo.set("Si");
        } else {
            this.estaActivo.set("No");
        }
    }

    public SimpleStringProperty nombreProperty() {
        return this.nombre;
    }

    public SimpleBooleanProperty activoProperty() {
        return this.activo;
    }

    /**
     * @return the estaActivo
     */
    public String getEstaActivo() {
        return estaActivo.get();
    }

    /**
     * @param estaActivo the estaActivo to set
     */
    public void setEstaActivo(String estaActivo) {
        this.estaActivo.set(estaActivo);
    }

    public SimpleStringProperty estaActivoProperty() {
        return this.estaActivo;
    }
}
