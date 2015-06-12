/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.model;

import java.util.List;
import xochiltapp.datasource.ArreglosDataSource;

/**
 *
 * @author afelipelc
 */
public class Arreglo extends Producto {

    private List<ComponenteArreglo> componentes = null;
    private List<Integer> componentesEliminar = null;
    private String foto;
    private String procedimiento;
    private String Nombre;

    /**
     * @return the componentes List Usar este método para manipular los
     * elementos Ejemplo: arreglo.getComponentes().Add(Componente item)
     * arreglo.getComponentes().Remove(Componente item)
     */
    public List<ComponenteArreglo> getComponentes() {
        //obtener de la BD (dataSource) y guardar en el campo si este es null
        //if(componentes == null)
        //obtener de la BD y guardar en campo (Ver class Producto)

        //devolver el campo
        if (componentes == null) {
            componentes = ArreglosDataSource.getComponentesArreglo(getIdProducto());

        }
        return componentes;
    }

    /**
     * @return the componentesEliminar List Usar este método para agregar los ID
     * de los componentes del arreglo a eliminar o quitar del arreglo (se usa
     * para eliminarlos de la BD al actualizar)
     */
    public List<Integer> ComponentesEliminar() {
        return getComponentesEliminar();
    }

    /**
     * @param componentes the componentes to set
     */
    public void setComponentes(List<ComponenteArreglo> componentes) {
        this.componentes = componentes;
    }

    /**
     * @return the componentesEliminar
     */
    public List<Integer> getComponentesEliminar() {
        return componentesEliminar;
    }

    /**
     * @param componentesEliminar the componentesEliminar to set
     */
    public void setComponentesEliminar(List<Integer> componentesEliminar) {
        this.componentesEliminar = componentesEliminar;
    }

    /**
     * @return the foto
     */
    @Override
    public String getFoto() {
        return foto;
    }

    /**
     * @param foto the foto to set
     */
    @Override
    public void setFoto(String foto) {
        this.foto = foto;
    }

    /**
     * @return the procedimiento
     */
    @Override
    public String getProcedimiento() {
        return procedimiento;
    }

    /**
     * @param procedimiento the procedimiento to set
     */
    @Override
    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    /**
     * @return the Nombre
     */
    public String getNombre() {
        return Nombre = getConcepto();
    }

    /**
     * @param Nombre the Nombre to set
     */
    public void setNombre(String Nombre) {
        setConcepto(Nombre);
        this.Nombre = Nombre;
    }
}
