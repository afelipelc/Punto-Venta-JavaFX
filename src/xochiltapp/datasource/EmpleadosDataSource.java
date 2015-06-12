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
import xochiltapp.model.Empleado;

/**
 *
 * @author afelipelc
 */
public final class EmpleadosDataSource {

    static DBConnection dbConnection = new DBConnection();

    public static Empleado validarEmpleado(String user, String passwd) {
        String query = "select * from empleado where usuario = '" + user + "' and password= Password('" + passwd + "') limit 1";

        //System.out.println(query);
        Statement statement = null;
        ResultSet rs = null;
        Empleado item = null;
        try {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(query);

            if (rs.next()) {
                item = new Empleado();
                item.setIdEmpleado(rs.getInt("IdEmpleado"));
                item.setNombre(rs.getString("Nombre"));
                item.setUsuario(rs.getString("Usuario"));
                //item.setPassword(rs.getString("Password"));
                item.setAdministrador(rs.getBoolean("Administrador"));
                item.setVendedor(rs.getBoolean("Vendedor"));
                item.setConsulta(rs.getBoolean("Consulta"));
                item.setActivo(rs.getBoolean("Activo"));
            }
            return item;
        } catch (Exception ex) {
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

    public static List<Empleado> getListaEmpleados(Empleado empleado) {
        if (empleado != null && empleado.isAdministrador()) {
            //return processListEmpleadosResult("SELECT * from Empleado where IdEmpleado != " + empleado.getIdEmpleado());
            return processListEmpleadosResult("SELECT * from Empleado");
        } else {
            return null;
        }
    }

    private static List<Empleado> processListEmpleadosResult(String q) {
        Statement statement = null;
        ResultSet rs = null;

        List<Empleado> listaEmpleados = new ArrayList<>();

        try {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(q);

            while (rs.next()) {
                Empleado item = new Empleado();
                item.setIdEmpleado(rs.getInt("IdEmpleado"));
                item.setNombre(rs.getString("Nombre"));
                item.setUsuario(rs.getString("Usuario"));
                //item.setPassword(rs.getString("Password"));
                item.setAdministrador(rs.getBoolean("Administrador"));
                item.setVendedor(rs.getBoolean("Vendedor"));
                item.setConsulta(rs.getBoolean("Consulta"));
                item.setActivo(rs.getBoolean("Activo"));

                listaEmpleados.add(item);
            }

            return listaEmpleados;
        } catch (ClassNotFoundException | SQLException ex) {
            //System.out.print("Error, process Result Cliente: " + ex.getMessage());
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

    public static Empleado guardarEmpleado(Empleado empleado) {
        if (empleado != null && empleado.getIdEmpleado() == 0) {
            int idAsignado = insertEmpleado(empleado);
            if (idAsignado > 0) {
                empleado.setIdEmpleado(idAsignado);
                empleado.setPassword(null);
                return empleado;
            } else {
                return null;
            }
        } else if (empleado != null && empleado.getIdEmpleado() > 0) {
            if (updateEmpleado(empleado)) {
                return empleado;
            } else {
                return null;
            }
        } else {
            return null; //ninguna de las anteriores
        }
    }

    private static int insertEmpleado(Empleado empleado) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            String SQL = "INSERT INTO Empleado(Nombre, Usuario, Password, Administrador, Vendedor, Consulta) values('" + empleado.getNombre() + "', '" + empleado.getUsuario() + "',Password('" + empleado.getPassword() + "'),  " + empleado.isAdministrador() + "," + empleado.isVendedor() + ", " + empleado.isConsulta() + ")";

            //System.out.println(SQL);

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
            //System.out.print("Error inserting Proveedor: " + ex.getMessage());
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

    private static boolean updateEmpleado(Empleado empleado) {
        Statement statement = null;

        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            String SQL = "UPDATE Empleado set ";

            if (empleado.getPassword() != null && !empleado.getPassword().equals("")) {
                SQL += " Password= Password('" + empleado.getPassword() + "'), ";
            }
            //no se cambia el nombre de usuario
            SQL += " Nombre='" + empleado.getNombre() + "', "
                    + " Administrador=" + empleado.isAdministrador() + ", "
                    + " Vendedor = " + empleado.isVendedor() + ", "
                    + " Consulta = " + empleado.isConsulta() + ", "
                    + " Activo = " + empleado.isActivo() + " "
                    + " where IdEmpleado = " + empleado.getIdEmpleado() + " limit 1";
            //System.out.println(SQL);
            statement.executeUpdate(SQL);
            return true;

        } catch (ClassNotFoundException | SQLException ex) {
            //System.out.print("Error: " + ex.getMessage());
            return false;
        } finally {

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
