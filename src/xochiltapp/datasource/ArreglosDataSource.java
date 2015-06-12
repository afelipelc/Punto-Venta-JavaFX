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
import static xochiltapp.datasource.InventarioDataSource.dbConnection;
import xochiltapp.model.Arreglo;
import xochiltapp.model.ComponenteArreglo;

/**
 *
 * @author afelipelc
 */
public final class ArreglosDataSource {

    static DBConnection dbConnection = new DBConnection();

    public static List<Arreglo> listaArreglos() {
        return processListResult("Select * from producto where categoria='Arreglos' and activo = true");
    }

    public static Arreglo findArregloById(int idArreglo) {
        return (Arreglo) InventarioDataSource.findProductoById(idArreglo);
    }

    public static String[] getListaImagenes(String categoria) {
        Statement statement = null;
        ResultSet rs = null;
        List<String> nombreimagenes = new ArrayList();
        try {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery("select Foto from Producto where Categoria = '" + categoria + "' and Foto != ''");

          // System.out.println("select Foto from Producto where Categoria = '" + categoria + "' and Foto != ''");
            while(rs.next()) {
                nombreimagenes.add(rs.getString("Foto"));
            }

//            System.out.println("Fotos:");
//            for(String item : nombreimagenes)
//                System.out.println(item);
//            
            return (String[]) nombreimagenes.toArray(new String[0]);

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

    public static Arreglo findArregloByImage(String nombreImagen) {

        Statement statement = null;
        ResultSet rs = null;
        Arreglo item = null;
        try {


            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery("select * from Producto where Foto = '" + nombreImagen + "' limit 1");

            if (rs.next()) {
                item = new Arreglo();

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

            return item != null ? item : null;

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

    public static Arreglo saveArreglo(Arreglo arreglo) {

        if (arreglo != null && arreglo.getIdProducto() == 0) {
            List<ComponenteArreglo> componentesArreglo = null;
            if (arreglo.getProveedores().size() > 0) {
                componentesArreglo = arreglo.getComponentes();
            }

            Integer idAsignado = insertArreglo(arreglo);
            if (idAsignado > 0) {

                if (componentesArreglo != null && componentesArreglo.size() > 0) {
                    arreglo = findArregloById(idAsignado);
                    arreglo.setComponentes(componentesArreglo);
                    saveNuevosComponentesArreglo(arreglo);
                    arreglo.setComponentes(null); //quitar todos para volver a cargar
                }
                return arreglo;
            } else {
                return null;
            }
        } else if (arreglo != null && arreglo.getIdProducto() > 0) {
            if (updateArreglo(arreglo)) {
                //save Proveedores
                saveNuevosComponentesArreglo(arreglo);
                if (arreglo.getProveedoresEliminar().size() > 0) {
                    deleteComponentesArreglo(arreglo);
                }

                return arreglo;
            } else {
                return null;
            }
        } else {
            return null; //ninguna de las anteriores
        }
    }

    private static int insertArreglo(Arreglo arreglo) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            String SQL = "INSERT INTO Producto(Concepto, Descripcion, Categoria, Precio, UltimaActualizacion, Foto, Procedimiento) values('" + arreglo.getConcepto() + "', '" + arreglo.getDescripcion() + "','" + arreglo.getCategoria() + "', " + arreglo.getPrecio() + ", now(),'" + arreglo.getFoto() + "','" + arreglo.getProcedimiento() + "')";

            System.out.println(SQL);

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
            //System.out.print("Error: " + ex.getMessage());
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

    private static boolean updateArreglo(Arreglo arreglo) {
        Statement statement = null;
        ResultSet rs = null;

        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            String SQL = "UPDATE Producto set "
                    + " Concepto='" + arreglo.getConcepto() + "', "
                    + " Descripcion = '" + arreglo.getDescripcion() + "', "
                    + " Precio = " + arreglo.getPrecio() + ", "
                    + " Activo = " + arreglo.isActivo() + ", "
                    + " UltimaActualizacion = now(), "
                    + " Foto = '" + arreglo.getFoto() + "', "
                    + " Procedimiento = '" + arreglo.getProcedimiento() + "', "
                    + " where IdProducto = " + arreglo.getIdProducto() + " limit 1";

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

    private static List<Arreglo> processListResult(String q) {
        List<Arreglo> listaArreglos = new ArrayList<>();
        //System.out.println("Productos DSln 76: " + q);
        Statement statement = null;
        ResultSet rs = null;

        try {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(q);

            while (rs.next()) {
                Arreglo item = new Arreglo();
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

                listaArreglos.add(item);
            }

            return listaArreglos;
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

    public static List<ComponenteArreglo> getComponentesArreglo(int idArreglo) {
        return processListComponentesArregloResult("Select * from componentesArreglo where idArreglo = " + idArreglo);
    }

    private static List<ComponenteArreglo> processListComponentesArregloResult(String q) {
        Statement statement = null;
        ResultSet rs = null;

        List<ComponenteArreglo> listaComponentes = new ArrayList<>();

        try {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(q);

            while (rs.next()) {
                ComponenteArreglo item = new ComponenteArreglo();
                item.setIdComponente(rs.getInt("IdComponenteArreglo"));
                item.setCantidad(rs.getInt("Cantidad"));
                item.setIdProducto(rs.getInt("IdProducto"));

                listaComponentes.add(item);
            }

            return listaComponentes;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.print("Error, process Result Cliente: " + ex.getMessage());
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

    private static void saveNuevosComponentesArreglo(Arreglo arreglo) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);

            for (ComponenteArreglo item : arreglo.getComponentes()) {
                if (item.getIdComponente() == 0) {
                    String SQL = "INSERT INTO ComponentesArreglo "
                            + "values(null, " + item.getIdArreglo() + ", "
                            + item.getIdProducto() + ", " + item.getCantidad() + ")";
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

    private static void deleteComponentesArreglo(Arreglo arreglo) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);

            for (Integer item : arreglo.getProveedoresEliminar()) {
                String SQL = "DELETE from ComponentesArreglo where IdComponenteArreglo = " + item + " limit 1";
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
}
