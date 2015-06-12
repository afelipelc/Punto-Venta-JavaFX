/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.datasource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import xochiltapp.model.Producto;
import xochiltapp.model.Proveedor;

/**
 *
 * @author afelipelc
 */
public final class InventarioDataSource {

    static DBConnection dbConnection = new DBConnection();
    //private Statement statement = null;
    //ResultSet rs = null;

    /**
     *
     * @return Devuelve el inventario completo
     */
    public static List<Producto> Inventario(boolean todos) {
        if (todos) {
            return processListResult("SELECT * FROM Producto");
        } else {
            return processListResult("SELECT * FROM Producto where activo= true");
        }
    }

    /**
     * *
     * Busca productos en el inventario
     *
     * @param valor Criterio a buscar
     * @param categoria en donde buscar
     * @return lista de productos encontrados
     */
    public static List<Producto> BuscarEnInventario(String valor, String categoria, boolean todos) {


        if (categoria == null || categoria.equals("Inventario")) {
            if (todos) {
                return processListResult("SELECT * FROM Producto where Concepto like '%" + valor + "%' or Descripcion like '%" + valor + "%'");
            } else {
                return processListResult("SELECT * FROM Producto where Concepto like '%" + valor + "%' or Descripcion like '%" + valor + "%'  and activo = true");
            }
        } else {
            if (todos) {
                return processListResult("SELECT * FROM Producto where Categoria='" + categoria + "' and Concepto like '%" + valor + "%' or Descripcion like '%" + valor + "%'");
            } else {
                return processListResult("SELECT * FROM Producto where Categoria='" + categoria + "' and Concepto like '%" + valor + "%' or Descripcion like '%" + valor + "%' and activo = true");
            }
        }
    }

    /**
     * *
     * Método que obtiene la lista de productos ofertados por un proveedor
     *
     * @param idProveedor
     * @return productos que oferta el provedor
     */
    public static List<Producto> getProductosByProveedor(Integer idProveedor) {
        String SQL = "SELECT "
                + " producto.IdProducto, producto.Concepto, producto.Descripcion, producto.Categoria, proveedorproducto.Precio, producto.Existencias, producto.Activo, producto.Foto, producto.Procedimiento, proveedorproducto.FechaActualizacion as UltimaActualizacion "
                + " FROM Producto, proveedorproducto where "
                + " producto.idProducto = proveedorproducto.idProducto "
                + " and "
                + " proveedorproducto.idProveedor =" + idProveedor;
        //System.out.println(SQL);
        return processListResult(SQL);
    }

    /**
     *
     * @param q Consulta SQL a ejecutar sobre la tabla Producto
     * @return
     */
    private static List<Producto> processListResult(String q) {
        List<Producto> listaProductos = new ArrayList<>();
        //System.out.println("Productos DSln 76: " + q);
        Statement statement = null;
        ResultSet rs = null;

        try {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(q);

            while (rs.next()) {
                Producto item = new Producto();
                item.setIdProducto(rs.getInt("IdProducto"));
                item.setConcepto(rs.getString("Concepto"));
                item.setDescripcion(rs.getString("Descripcion"));
                item.setCategoria(rs.getString("Categoria"));
                item.setPrecio(rs.getFloat("Precio"));
                item.setExitencias(rs.getInt("Existencias"));
                item.setActivo(rs.getBoolean("Activo"));
                item.setUltimaActualizacion(rs.getDate("UltimaActualizacion"));
                item.setFoto(rs.getString("Foto"));
                item.setProcedimiento(rs.getString("Procedimiento"));

                listaProductos.add(item);
            }

            return listaProductos;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.print("Error, process Result Productos: " + ex.getMessage());
            return null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }

            dbConnection.CloseConnection();
        }
    }

    /**
     * *
     * Obtener un producto por ID
     *
     * @param id del producto
     * @return producto encontrado
     */
    public static Producto findProductoById(Integer id) {
        Statement statement = null;
        ResultSet rs = null;

        try {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery("select * from Producto where IdProducto = " + id);

            Producto item = new Producto();

            if (rs.next()) {

                item.setIdProducto(rs.getInt("IdProducto"));
                item.setConcepto(rs.getString("Concepto"));
                item.setDescripcion(rs.getString("Descripcion"));
                item.setCategoria(rs.getString("Categoria"));
                item.setPrecio(rs.getFloat("Precio"));
                item.setExitencias(rs.getInt("Existencias"));
                item.setActivo(rs.getBoolean("Activo"));
                item.setUltimaActualizacion(rs.getDate("UltimaActualizacion"));
                item.setFoto(rs.getString("Foto"));
                item.setProcedimiento(rs.getString("Procedimiento"));
            }

            return item;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.print("Error: " + ex.getMessage());
            return null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }

            dbConnection.CloseConnection();
        }
    }

    /**
     * Método que se encarga de Guardar el estado de un Producto Para Insertar
     * nuevo o Actualzar existente, utilice este método
     *
     * @param producto Objeto producto a guardar
     * @return Producto guardado (insertado o actualizado). Si devuelve null,
     * falló la acción
     */
    public static Producto saveProducto(Producto producto) {
        if (producto != null && producto.getIdProducto() == 0) {
            List<Proveedor> proveedoresProducto = null;
            if (producto.getProveedores().size() > 0) {
                proveedoresProducto = producto.getProveedores();
            }

            Integer idAsignado = insertProducto(producto);
            if (idAsignado > 0) {

                if (proveedoresProducto != null && proveedoresProducto.size() > 0) {
                    producto = findProductoById(idAsignado);
                    producto.setProveedores(proveedoresProducto);
                    saveNuevosProveedoresProducto(producto);
                    producto.setProveedores(null); //quitar todos para volver a cargar
                }
                return producto;
            } else {
                return null;
            }
        } else if (producto != null && producto.getIdProducto() > 0) {
            if (updateProducto(producto)) {
                //save Proveedores
                saveNuevosProveedoresProducto(producto);
                updatePreciosProveedores(producto);
                if (producto.getProveedoresEliminar().size() > 0) {
                    deleteProveedoresProducto(producto);
                }

                //after all, return saved producto
                return producto;
            } else {
                return null;
            }
        } else {
            return null; //ninguna de las anteriores
        }
    }

    /**
     * Método que se encarga de registrar los proveedores del producto guardado
     *
     * @param producto
     */
    private static void saveNuevosProveedoresProducto(Producto producto) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);

            for (Proveedor item : producto.getProveedores()) {
                if (item.getIdProveedorProducto() == 0) {
                    String SQL = "INSERT INTO ProveedorProducto "
                            + "values(null, " + item.getIdProveedor() + ", "
                            + producto.getIdProducto() + ", " + item.getPrecioProducto() + ", "
                            + " now())";
                    statement.executeUpdate(SQL);
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.print("Error: " + ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }

            dbConnection.CloseConnection();
        }
    }

    /**
     * Método que elimina la relacion entre Proveedor y Productos
     *
     * @param producto a actualizar en sus proveedores
     */
    private static void deleteProveedoresProducto(Producto producto) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);

            for (Integer item : producto.getProveedoresEliminar()) {
                String SQL = "DELETE from ProveedorProducto where IdProveedorProducto = " + item + " limit 1";
                statement.executeUpdate(SQL);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            //System.out.print("Error: " + ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                } // ignore

            }

            dbConnection.CloseConnection();
        }
    }

    /**
     * Método que actualia los precios de los proveedores
     *
     * @param producto
     */
    private static void updatePreciosProveedores(Producto producto) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);

            for (Proveedor item : producto.getProveedores()) {
                if (item.getIdProveedorProducto() != 0) {
                    String SQL = "UPDATE ProveedorProducto "
                            + " SET Precio =" + item.getPrecioProducto() + ", "
                            + " FechaActualizacion = now() Where IdProveedorProducto = " + item.getIdProveedorProducto() + " "
                            + "and "
                            + "Precio != " + item.getPrecioProducto() + " "
                            + "limit 1";

                    //System.out.println(SQL);
                    statement.executeUpdate(SQL);
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            //System.out.print("Error: " + ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }

            dbConnection.CloseConnection();
        }
    }

    /**
     * Método que se encarga de insertar un producto como nuevo
     *
     * @param producto a insertar
     * @return Id asignado al producto
     */
    private static int insertProducto(Producto producto) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            String SQL = "INSERT INTO Producto(Concepto, Descripcion, Categoria, Precio, UltimaActualizacion) values('" + producto.getConcepto() + "', '" + producto.getDescripcion() + "','" + producto.getCategoria() + "', " + producto.getPrecio() + ", now())";

            statement.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);

            int autoIncKeyFromApi = -1;
            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                autoIncKeyFromApi = rs.getInt(1);
            } else {
                autoIncKeyFromApi = 0;
            }
            rs.close();
            rs = null;

            return autoIncKeyFromApi;

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.print("Error: " + ex.getMessage());
            return 0;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }

            dbConnection.CloseConnection();
        }

    }

    /**
     * Método que se encarga de actualizar un producto existente
     *
     * @param producto a actualizar
     * @return Resultado de la acción
     */
    private static boolean updateProducto(Producto producto) {
        Statement statement = null;
        ResultSet rs = null;

        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            String SQL = "UPDATE Producto set "
                    + " Concepto='" + producto.getConcepto() + "', "
                    + " Descripcion = '" + producto.getDescripcion() + "', "
                    + " Categoria='" + producto.getCategoria() + "', "
                    + " Precio = " + producto.getPrecio() + ", "
                    + " Existencias = " + producto.getExitencias() + ", "
                    + " Activo = " + producto.isActivo() + ", "
                    + " UltimaActualizacion = now() "
                    + " where IdProducto = " + producto.getIdProducto() + " limit 1";

            statement.executeUpdate(SQL);
            return true;

        } catch (ClassNotFoundException | SQLException ex) {
            //System.out.print("Error: " + ex.getMessage());
            return false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                } // ignore
            }

            dbConnection.CloseConnection();
        }
    }

}
