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
import xochiltapp.model.Proveedor;

/**
 *
 * @author afelipelc
 */
public class ProveedoresDataSource {
    
    DBConnection dbConnection = new DBConnection();
    private Statement statement = null;
    ResultSet rs = null;
    
    public List<Proveedor> getProveedoresProducto(int idProducto)
    {
        //List<Proveedor> listaProveedores = new ArrayList<>();
        //retrieve from database
        String SQL = "select " +
            "proveedor.IdProveedor, " +
            "proveedor.Nombre, " +
            "proveedor.Direccion, " +
            "proveedor.TelCasa, " +
            "proveedor.TelNegocio, " +
            "proveedor.Celular, " +
            "proveedorproducto.Precio, " +
            "proveedorproducto.FechaActualizacion, " +
            "proveedorproducto.IdProveedorProducto "+
            "from proveedor, proveedorproducto " +
            "where " +
            "proveedor.IdProveedor = proveedorproducto.IdProveedor " +
            "and " +
            "proveedorproducto.IdProducto = " + idProducto;
        
        return processListProveedorAndPrecioResult(SQL);
    }
    
    public List<Proveedor> getProveedoresList()
    {
        return processListResult("select * from Proveedor");
    }
    
    private List<Proveedor> processListResult(String q)
    {
        List<Proveedor> listaProveedores = new ArrayList<>();
        
        try
        {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(q);
            
            while (rs.next()) {
                Proveedor item = new Proveedor();
                item.setIdProveedor(rs.getInt("IdProveedor"));
                item.setNombre(rs.getString("Nombre"));
                item.setDireccion(rs.getString("Direccion"));
                item.setTelCasa(rs.getString("TelCasa"));
                item.setTelNegocio(rs.getString("TelNegocio"));
                item.setTelcelular(rs.getString("Celular"));
                
                listaProveedores.add(item);
            }
            
            return listaProveedores;
        }catch(ClassNotFoundException | SQLException ex)
        {
            System.out.print("Error, process Result Proveedores: " + ex.getMessage());
            return null;
        }
        finally
        {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore
                    rs = null;
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) { } // ignore
                    statement = null;
            }
            
            dbConnection.CloseConnection();
        }
    }
    
    private List<Proveedor> processListProveedorAndPrecioResult(String q)
    {
        List<Proveedor> listaProveedores = new ArrayList<>();
        
        try
        {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(q);
            
            while (rs.next()) {
                Proveedor item = new Proveedor();
                item.setIdProveedor(rs.getInt("IdProveedor"));
                item.setNombre(rs.getString("Nombre"));
                item.setDireccion(rs.getString("Direccion"));
                item.setTelCasa(rs.getString("TelCasa"));
                item.setTelNegocio(rs.getString("TelNegocio"));
                item.setTelcelular(rs.getString("Celular"));
                
                item.setPrecioProducto(rs.getFloat("Precio"));
                item.setFechaActualizacionProd(rs.getDate("FechaActualizacion"));
                item.setIdProveedorProducto(rs.getInt("IdProveedorProducto"));
                
                listaProveedores.add(item);
            }
            
            return listaProveedores;
        }catch(ClassNotFoundException | SQLException ex)
        {
            System.out.print("Error, process Result Proveedores: " + ex.getMessage());
            return null;
        }
        finally
        {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore
                    rs = null;
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) { } // ignore
                    statement = null;
            }
            
            dbConnection.CloseConnection();
        }
    }
    
    public Proveedor saveProveedor(Proveedor proveedor)
    {
        if(proveedor != null && proveedor.getIdProveedor() == 0)
        {
            Integer idAsignado = insertProveedor(proveedor);
            if(idAsignado>0)
            {
                //return findProveedorById(idAsignado);
                proveedor.setIdProveedor(idAsignado);
                return proveedor;
            }else
                return null;
        }
        else if(proveedor!=null && proveedor.getIdProveedor() > 0)
        {
            if(updateProveedor(proveedor))
                return proveedor;
            else
                return null;
        }
        else
            return null; //ninguna de las anteriores
    }
    
    private int insertProveedor(Proveedor proveedor)
    {
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            String SQL = "INSERT INTO Proveedor(Nombre, Direccion, TelCasa, TelNegocio, Celular) values('"+proveedor.getNombre()+"', '"+proveedor.getDireccion()+"','"+proveedor.getTelCasa()+"',  '"+proveedor.getTelNegocio()+"','"+proveedor.getTelcelular()+"')";
            
            //System.out.println(SQL);
            
            statement.executeUpdate(SQL,Statement.RETURN_GENERATED_KEYS);
            
            int autoIncKeyFromApi = -1;
            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                autoIncKeyFromApi = rs.getInt(1);
            } else {
                autoIncKeyFromApi=0;
            }
            rs.close();
            rs = null;
            
            return autoIncKeyFromApi;
            
        }catch(ClassNotFoundException | SQLException ex)
        {
            //System.out.print("Error inserting Proveedor: " + ex.getMessage());
            return 0;
        }
        finally
        {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore
                    rs = null;
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) { } // ignore
                    statement = null;
            }
            
            dbConnection.CloseConnection();
        }
    }
    
    private boolean updateProveedor(Proveedor proveedor)
    {
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            String SQL = "UPDATE Proveedor set "
                    + " Nombre='"+proveedor.getNombre()+"', "
                    + " Direccion = '"+proveedor.getDireccion()+"', "
                    + " TelCasa='"+proveedor.getTelCasa()+"', "
                    + " TelNegocio = '"+proveedor.getTelNegocio()+"', "
                    + " Celular = '"+proveedor.getTelcelular()+"' "
                    + " where IdProveedor = "+proveedor.getIdProveedor()+" limit 1";
            //System.out.println(SQL);
            statement.executeUpdate(SQL);
            return true;
            
        }catch(ClassNotFoundException | SQLException ex)
        {
            //System.out.print("Error: " + ex.getMessage());
            return false;
        }
        finally
        {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) { } // ignore
                    statement = null;
            }
            
            dbConnection.CloseConnection();
        }   
    }

    public Proveedor findProveedorById(Integer id) {
        try {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery("select * from Proveedor where IdProveedor = " + id);
            
            Proveedor item = new Proveedor();
            
            if (rs.next()) {
                item.setIdProveedor(rs.getInt("IdProveedor"));
                item.setNombre(rs.getString("Nombre"));
                item.setDireccion(rs.getString("Direccion"));
                item.setTelCasa(rs.getString("TelCasa"));
                item.setTelNegocio(rs.getString("TelNegocio"));
                item.setTelcelular(rs.getString("Celular"));
            }
                       
            return item;
        }catch(ClassNotFoundException | SQLException ex)
        {
            //System.out.print("Error: " + ex.getMessage());
            return null;
        }
        finally
        {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore
                    rs = null;
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) { } // ignore
                    statement = null;
            }
            
            dbConnection.CloseConnection();
        }
    }
    
}
