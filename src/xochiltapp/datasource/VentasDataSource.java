/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.datasource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import xochiltapp.model.DetalleVenta;
import xochiltapp.model.Venta;

/**
 *
 * @author afelipelc
 */
public final class VentasDataSource {

    static DBConnection dbConnection = new DBConnection();

    public static Venta saveVenta(Venta venta) {
//        List<DetalleVenta> detallesVenta = null;
//        if (venta.getDetallesVenta().size() > 0) {
//            detallesVenta = venta.getDetallesVenta();
//        }

        long idAsignado;
        idAsignado = insertVenta(venta);
        if (idAsignado > 0) {
            venta.setIdVenta(idAsignado);
            insertDetallesVenta(venta);
            return venta;
        } else {
            return null;
        }
    }

    private static long insertVenta(Venta venta) {
        Statement statement = null;
        ResultSet rs = null;

        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            String SQL = "INSERT INTO Venta(Fecha, Importe, Empleado) values(now()," + venta.getImporte() + ",'" + venta.getEmpleado() + "')";

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
                rs = null;
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                } // ignore
                statement = null;
            }

            dbConnection.CloseConnection();
        }
    }

    private static void insertDetallesVenta(Venta venta) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);

            for (DetalleVenta item : venta.getDetallesVenta()) {
                item.setIdVenta(venta.getIdVenta());


                String SQL = "INSERT INTO DetallesVenta "
                        + "values(null, " + item.getIdVenta() + ", " + item.getIdProducto() + ", " + item.getPrecioUnit() + ", " + item.getCantidad() + "," + item.getImporte() + ")";
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
