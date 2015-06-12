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
import xochiltapp.model.Cliente;

/**
 *
 * @author afelipelc
 */
public final class ClientesDataSource {

    static DBConnection dbConnection = new DBConnection();

    public static List<Cliente> buscarCliente(String q) {
        String query = "select * from cliente where nombre = '" + q + "'";
        return processListClientesResult(query);
    }

    public static Cliente findClienteById(int idCliente)
    {
        Statement statement = null;
        ResultSet rs = null;
        try {
            
            Cliente item = null;
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery("select * from cliente where idCliente = " + idCliente);

            if (rs.next()) {
                item = new Cliente();
                item.setIdCliente(rs.getInt("idCliente"));
                item.setNombre(rs.getString("Nombre"));
                item.setDomicilio(rs.getString("Direccion"));
                item.setReferenciaDomicilio(rs.getString("Referencia"));
                item.setTelefono(rs.getString("Telefono1"));
                item.setTelefono2(rs.getString("Telefono2"));
            }
            
            return item;
        }catch (ClassNotFoundException | SQLException ex) {
            //System.out.print("Error: " + ex.getMessage());
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
    
    private static List<Cliente> processListClientesResult(String q) {
        Statement statement = null;
        ResultSet rs = null;

        List<Cliente> listaClientes = new ArrayList<>();

        try {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(q);

            while (rs.next()) {
                Cliente item = new Cliente();
                item.setIdCliente(rs.getInt("IdCliente"));
                item.setNombre(rs.getString("Nombre"));
                item.setDomicilio(rs.getString("Direccion"));
                item.setReferenciaDomicilio(rs.getString("Referencia"));
                item.setTelefono(rs.getString("Telefono1"));
                item.setTelefono2(rs.getString("Telefono2"));

                listaClientes.add(item);
            }

            return listaClientes;
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

    public static Cliente guardarCliente(Cliente cliente) {
        if (cliente != null && cliente.getIdCliente() == 0) {
            int idAsignado = insertCliente(cliente);
            if (idAsignado > 0) {
                cliente.setIdCliente(idAsignado);
                return cliente;
            } else {
                return null;
            }
        } else if (cliente != null && cliente.getIdCliente() > 0) {
            if (updateCliente(cliente)) {
                return cliente;
            } else {
                return null;
            }
        } else {
            return null; //ninguna de las anteriores
        }
    }

    private static int insertCliente(Cliente cliente) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            String SQL = "INSERT INTO Cliente(Nombre, Direccion, Referencia, Telefono1, Telefono2) values('" + cliente.getNombre() + "', '" + cliente.getDomicilio() + "','" + cliente.getReferenciaDomicilio() + "',  '" + cliente.getTelefono() + "','" + cliente.getTelefono2() + "')";

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

    private static boolean updateCliente(Cliente cliente) {
        Statement statement = null;

        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            String SQL = "UPDATE Cliente set "
                    + " Nombre='" + cliente.getNombre() + "', "
                    + " Direccion = '" + cliente.getDomicilio() + "', "
                    + " Referencia='" + cliente.getReferenciaDomicilio() + "', "
                    + " Telefono1 = '" + cliente.getTelefono() + "', "
                    + " Telefono2 = '" + cliente.getTelefono2() + "' "
                    + " where IdCliente = " + cliente.getIdCliente() + " limit 1";
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
