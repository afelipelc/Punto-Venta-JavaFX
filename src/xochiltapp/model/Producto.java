package xochiltapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import xochiltapp.datasource.ProveedoresDataSource;

/**
 *
 * @author afelipelc
 */
public class Producto {

    private int idProducto;
    private SimpleStringProperty concepto = new SimpleStringProperty();
    private SimpleStringProperty descripcion = new SimpleStringProperty();
    private SimpleStringProperty categoria = new SimpleStringProperty();
    private SimpleFloatProperty precio = new SimpleFloatProperty();
    private SimpleIntegerProperty exitencias = new SimpleIntegerProperty();
    private SimpleBooleanProperty activo = new SimpleBooleanProperty();
    private SimpleStringProperty estaActivo = new SimpleStringProperty();
    private Date ultimaActualizacion;
    private String foto;
    private String procedimiento;
    private List<Proveedor> proveedores = null;
    private List<Integer> proveedoresEliminar = new ArrayList<>();

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
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion.get();
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    /**
     * @return the categoria
     */
    public String getCategoria() {
        return categoria.get();
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(String categoria) {
        this.categoria.set(categoria);
    }

    /**
     * @return the precio
     */
    public float getPrecio() {
        return precio.get();
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(float precio) {
        this.precio.set(precio);
    }

    /**
     * @return the exitencias
     */
    public int getExitencias() {
        return exitencias.get();
    }

    /**
     * @param exitencias the exitencias to set
     */
    public void setExitencias(int exitencias) {
        this.exitencias.set(exitencias);
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

    /**
     * @return the ultimaActualizacion
     */
    public Date getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    /**
     * @param ultimaActualizacion the ultimaActualizacion to set
     */
    public void setUltimaActualizacion(Date ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    /**
     * @return the foto
     */
    public String getFoto() {
        return foto;
    }

    /**
     * @param foto the foto to set
     */
    public void setFoto(String foto) {
        this.foto = foto;
    }

    /**
     * @return the procedimiento
     */
    public String getProcedimiento() {
        return procedimiento;
    }

    /**
     * @param procedimiento the procedimiento to set
     */
    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
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

    public List<Proveedor> getProveedores() {
        if (proveedores == null) {
            proveedores = new ProveedoresDataSource().getProveedoresProducto(idProducto);
        }

        return proveedores;
    }

    /**
     * @return the proveedoresEliminar
     */
    public List<Integer> getProveedoresEliminar() {
        return proveedoresEliminar;
    }

    /**
     * @param proveedores the proveedores to set
     */
    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    public SimpleStringProperty conceptoProperty() {
        return this.concepto;
    }

    public SimpleStringProperty descripcionProperty() {
        return this.descripcion;
    }

    public SimpleStringProperty categoriaProperty() {
        return this.categoria;
    }

    public SimpleFloatProperty precioProperty() {
        return this.precio;
    }

    public SimpleIntegerProperty exitenciasProperty() {
        return this.exitencias;
    }

    public SimpleBooleanProperty activoProperty() {
        return this.activo;
    }
    
    public SimpleStringProperty estaActivoProperty() {
        return this.estaActivo;
    }
}
