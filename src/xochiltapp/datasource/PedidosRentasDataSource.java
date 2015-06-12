/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.datasource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import xochiltapp.model.DetallePedidoRenta;
import xochiltapp.model.PedidoRenta;

/**
 *
 * @author afelipelc
 */
public final class PedidosRentasDataSource {

    static DBConnection dbConnection = new DBConnection();
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static List<PedidoRenta> pedidosRentaPendientes() {
        Statement statement = null;
        ResultSet rs = null;
        String SQL = "select * from PedidoRenta "
                + "where (cancelado = false and (enviado = false or entregado = false)) "
                + "	or (renta=true and devuelto = false and cancelado = false)";
        List<PedidoRenta> pedidosRentas = new ArrayList<>();

        try {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(SQL);

            while (rs.next()) {
                PedidoRenta item = new PedidoRenta();
                item.setIdPedidoRenta(rs.getLong("IdPedidoRenta"));
                item.setIdCliente(rs.getInt("IdCliente"));
                item.setPedido(rs.getBoolean("Pedido"));
                item.setRenta(rs.getBoolean("Renta"));
                try {
                    item.setFechaSolicitud(dateFormat.parse(rs.getString("FechaSolicitud")));
                } catch (SQLException | ParseException ex) {
                    if (rs.getDate("FechaSolicitud") != null) {
                        item.setFechaSolicitud(rs.getDate("FechaSolicitud"));
                    }
                }
                try {
                    if (rs.getString("FechaHoraEntrega") != null) {
                        item.setFechaEntrega(dateFormat.parse(rs.getString("FechaHoraEntrega")));
                    }

                } catch (SQLException | ParseException ex) {
                    if (rs.getDate("FechaHoraEntrega") != null) {
                        item.setFechaEntrega(rs.getDate("FechaHoraEntrega"));
                    }
                }

                item.setEnviado(rs.getBoolean("Enviado"));

                item.setEntregado(rs.getBoolean("Entregado"));
                item.setDevuelto(rs.getBoolean("Devuelto"));
                item.setCancelado(rs.getBoolean("Cancelado"));
                item.setPagado(rs.getBoolean("Pagado"));
                item.setImporte(rs.getFloat("Importe"));

                pedidosRentas.add(item);
            }

            return pedidosRentas;
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

    public static PedidoRenta guardarApartadoPedido(PedidoRenta objeto) {
        if (objeto != null) {
            //guardar cliente
            if (objeto.getCliente() != null) {
                objeto.setCliente(ClientesDataSource.guardarCliente(objeto.getCliente()));
            }

            if (objeto.getCliente() == null) {
                //error
                return null;
            }
        }

        if (objeto != null && objeto.getIdPedidoRenta() == 0) {
            long idAsignado = insertarPedidoRenta(objeto);
            if (idAsignado > 0) {
                objeto.setIdPedidoRenta(idAsignado);
                saveDetallesPedidoRenta(objeto);
                return objeto;
            } else {
                return null;
            }
        } else if (objeto != null && objeto.getIdPedidoRenta() > 0) {
            if (updatePedidoRenta(objeto)) {
                saveDetallesPedidoRenta(objeto);
                if (objeto.getDetallesEliminar().size() > 0) {
                    deleteDetallesPedidoRenta(objeto);
                }
                return objeto;
            } else {
                return null;
            }
        } else {
            return null; //ninguna de las anteriores
        }
    }

    private static long insertarPedidoRenta(PedidoRenta objeto) {
        Statement statement = null;
        ResultSet rs = null;

        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);

            String SQL = "INSERT INTO PedidoRenta values(null," + objeto.getCliente().getIdCliente() + "," + objeto.isPedido() + "," + objeto.isRenta() + ",'" + objeto.getDireccionCliente() + "','" + objeto.getDireccionEntrega() + "','" + objeto.getNombreRecibe() + "',now(),'" + dateFormat.format(objeto.getFechaEntrega()) + "',null,null,false, false, false, false," + objeto.isPagado() + "," + objeto.getImporte() + "," + objeto.getAbono() + ",'" + objeto.getEmpleado() + "','" + objeto.getNota() + "')";

            System.out.println(SQL);

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

    private static void saveDetallesPedidoRenta(PedidoRenta objeto) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);

            for (DetallePedidoRenta item : objeto.getDetallesPedidoRenta()) {
                String SQL = "";
                if (item.getIdDetallePedidoRenta() == 0) {
                    item.setIdPedidoRenta(objeto.getIdPedidoRenta());


                    SQL = "INSERT INTO DetallesPedidoRenta "
                            + "values(null, " + item.getIdPedidoRenta() + ", " + item.getIdProducto() + ", " + item.getCantidad() + ", " + item.getPrecioUnit() + "," + item.getImporte() + ")";
                } else if (item.getIdDetallePedidoRenta() > 0) {
                    SQL = "UPDATE DetallesPedidoRenta "
                            + "set  Cantidad = " + item.getCantidad() + ",  importe = " + item.getImporte() + " where idDetallePedido = " + item.getIdDetallePedidoRenta() + " limit 1";
                }
                if (!SQL.equals("")) {
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

    private static void deleteDetallesPedidoRenta(PedidoRenta objeto) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);

            for (long item : objeto.getDetallesEliminar()) {
                String SQL = "DELETE from DetallesPedidoRenta "
                        + " where idDetallePedido = " + item + " limit 1";
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
    //solo actualizar los datos del pedido

    private static boolean updatePedidoRenta(PedidoRenta objeto) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
            String SQL = "UPDATE PedidoRenta set ";
            if (objeto.getFechaEntregado() != null) {
                SQL += " FechaHoraEntregado='" + dateFormat.format(objeto.getFechaEntregado()) + "', ";
            }
            if (objeto.getFechaEnviado() != null) {
                SQL += " FechaHoraEnviado='" + dateFormat.format(objeto.getFechaEnviado()) + "', ";
            }
            if (objeto.getFechaEntrega() != null) {
                SQL += " FechaHoraEntrega='" + dateFormat.format(objeto.getFechaEntrega()) + "', ";
            }
            if (!objeto.isEnviado()) {
                SQL += " Recibe = '" + objeto.getNombreRecibe() + "', "
                        + " DirEntrega = '" + objeto.getDireccionEntrega() + "', ";
            }

            SQL += " Enviado =" + objeto.isEnviado() + ", "
                    + " Entregado=" + objeto.isEntregado() + ", "
                    + " Devuelto = " + objeto.isDevuelto() + ", "
                    + " Cancelado = " + objeto.isCancelado() + ", "
                    + " Importe = " + objeto.getImporte() + ", "
                    + " Pagado = " + objeto.isPagado() + ", "
                    + " Deposito = " + objeto.getAbono() + ", "
                    + " Nota = '" + objeto.getNota() + "' "
                    + " where IdPedidoRenta = " + objeto.getIdPedidoRenta() + " limit 1";

            //System.out.println(SQL);
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

    public static PedidoRenta findPedidoRentaById(long id) {
        String SQL = "SELECT "
                + " * "
                + " FROM pedidorenta where "
                + " idPedidoRenta = " + id;
        //System.out.println(SQL);

        PedidoRenta item = new PedidoRenta();

        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(SQL);

            if (rs.next()) {
                item.setIdPedidoRenta(rs.getInt("IdPedidoRenta"));
                item.setIdCliente(rs.getInt("IdCliente"));
                item.setPedido(rs.getBoolean("Pedido"));
                item.setRenta(rs.getBoolean("Renta"));
                item.setDireccionCliente(rs.getString("DirCliente"));
                item.setDireccionEntrega(rs.getString("DirEntrega"));
                item.setNombreRecibe(rs.getString("Recibe"));
                try {
                    item.setFechaSolicitud(dateFormat.parse(rs.getString("FechaSolicitud")));
                } catch (SQLException | ParseException ex) {
                    if (rs != null && rs.getDate("FechaSolicitud") != null) {
                        item.setFechaSolicitud(rs.getDate("FechaSolicitud"));
                    }
                }
                try {
                    if (rs != null && rs.getString("FechaHoraEntrega") != null) {
                        item.setFechaEntrega(dateFormat.parse(rs.getString("FechaHoraEntrega")));
                    }

                } catch (SQLException | ParseException ex) {
                    if (rs != null && rs.getDate("FechaHoraEntrega") != null) {
                        item.setFechaEntrega(rs.getDate("FechaHoraEntrega"));
                    }
                }
                try {
                    if (rs.getString("FechaHoraEntregado") != null) {
                        item.setFechaEntregado(dateFormat.parse(rs.getString("FechaHoraEntregado")));
                    }
                } catch (SQLException | ParseException ex) {
                    if (rs != null && rs.getDate("FechaHoraEntrega") != null) {
                        item.setFechaEntrega(rs.getDate("FechaHoraEntrega"));
                    }
                }

                //System.out.println(dateFormat.format(item.getFechaEntrega()));
                
                item.setEnviado(rs.getBoolean("Enviado"));
                item.setEntregado(rs.getBoolean("Entregado"));
                item.setDevuelto(rs.getBoolean("Devuelto"));
                item.setCancelado(rs.getBoolean("Cancelado"));
                item.setPagado(rs.getBoolean("Pagado"));
                item.setImporte(rs.getFloat("Importe"));
                item.setAbono(rs.getFloat("Deposito"));
                item.setEmpleado(rs.getString("Empleado"));
                item.setNota(rs.getString("Nota"));
            }
            if (item.getIdPedidoRenta() > 0) {
                //obtener los detalles
                item.setDetallesPedidoRenta(processListDetallesResult("SELECT * from detallespedidorenta where idpedidorenta = " + item.getIdPedidoRenta()));
            }

            return item;

        } catch (ClassNotFoundException | SQLException ex) {
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

    private static List<DetallePedidoRenta> processListDetallesResult(String q) {
        Statement statement = null;
        ResultSet rs = null;

        List<DetallePedidoRenta> listaDetalles = new ArrayList<>();

        try {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(q);

            while (rs.next()) {
                DetallePedidoRenta item = new DetallePedidoRenta();
                item.setIdDetallePedidoRenta(rs.getLong("IdDetallePedido"));
                item.setIdPedidoRenta(rs.getLong("IdPedidoRenta"));
                item.setIdProducto(rs.getInt("IdProducto"));
                item.setCantidad(rs.getInt("Cantidad"));
                item.setPrecioUnit(rs.getFloat("PUnitario"));
                item.setImporte(rs.getFloat("Importe"));

                listaDetalles.add(item);
            }

            return listaDetalles;
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
}
