/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.datasource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import xochiltapp.model.Compra;
import xochiltapp.model.DetalleCompra;

/**
 *
 * @author afelipelc
 */
public final class ComprasDataSource {
    static DBConnection dbConnection = new DBConnection();
    
    public static Compra saveCompra(Compra compra)
    {
        long idAsignado = insertCompra(compra);
        if (idAsignado > 0) {
            compra.setIdCompra(idAsignado);
            insertDetallesCompra(compra);
            return compra;
        } else {
            return null;
        }
    }
    
    private static long insertCompra(Compra compra) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            String SQL = "INSERT INTO Compra(Fecha, Importe, Empleado) values(now()," + compra.getImporte() + ",'" + compra.getEmpleado() + "')";

            statement.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);

            long autoIncKeyFromApi = -1;
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
    
    private static void insertDetallesCompra(Compra compra) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);

            for (DetalleCompra item : compra.getDetallesCompra()) {
                item.setIdCompra(compra.getIdCompra());


                String SQL = "INSERT INTO DetallesCompra "
                        + "values(null, " + item.getIdCompra() + ", " + item.getIdProducto() + ", " + item.getPrecioUnit() + ", " + item.getCantidad() + "," + item.getImporte() + ")";
                statement.executeUpdate(SQL);
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
}
