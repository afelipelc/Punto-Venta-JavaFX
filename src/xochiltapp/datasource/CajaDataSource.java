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
import java.util.Date;
import java.util.List;
import xochiltapp.model.CorteCaja;
import xochiltapp.model.DetalleCorte;
import xochiltapp.model.ResumenIngreso;

/**
 *
 * @author afelipelc
 */
public final class CajaDataSource {

    static DBConnection dbConnection = new DBConnection();
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static boolean isCajaAbierta() {
        Statement statement = null;
        ResultSet rs = null;
        try {
            String sql = "select * from CorteCaja order by IdCorte desc limit 1";
            CorteCaja corte = new CorteCaja();
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(sql);

            if (rs.next()) {
                corte.setIdCorte(rs.getInt("IdCorte"));
                corte.setAbierta(rs.getBoolean("Estado"));
            }
            return corte.isAbierta();
        } catch (ClassNotFoundException | SQLException ex) {
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

    public static CorteCaja cajaActual() {
        Statement statement = null;
        ResultSet rs = null;
        try {
            String sql = "select * from CorteCaja order by IdCorte desc limit 1";
            CorteCaja corte = new CorteCaja();
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(sql);

            if (rs.next()) {
                corte.setIdCorte(rs.getInt("IdCorte"));
                corte.setAbierta(rs.getBoolean("Estado"));
                corte.setMontoInicial(rs.getFloat("MontoInicio"));
                corte.setMontoCierre(rs.getFloat("MontoCierre"));
                corte.setMontoIngreso(rs.getFloat("MontoIngreso"));
                corte.setEmpleadoAbre(rs.getString("EmpleadoAbre"));
                corte.setEmpleadoCierra(rs.getString("EmpleadoCierra"));
                try {
                    corte.setFechaHoraApertura(dateFormat.parse(rs.getString("FechaHoraApertura")));
                } catch (SQLException | ParseException ex) {
                    if (rs.getDate("FechaHoraApertura") != null) {
                        corte.setFechaHoraApertura(rs.getDate("FechaHoraApertura"));
                    }
                }

                try {
                    if (rs.getString("FechaHoraCierre") != null) {
                        corte.setFechaHoraCierre(dateFormat.parse(rs.getString("FechaHoraCierre")));
                    }
                } catch (SQLException | ParseException ex) {
                    if (rs.getDate("FechaHoraCierre") != null) {
                        corte.setFechaHoraCierre(rs.getDate("FechaHoraCierre"));
                    }
                }
            }
            return corte;
        } catch (ClassNotFoundException | SQLException ex) {
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

    public static CorteCaja abrirCorte(CorteCaja corte) {
        if (corte != null && corte.getIdCorte() == 0) {
            int idAsignado = insertCorte(corte);
            if (idAsignado > 0) {
                corte.setIdCorte(idAsignado);
                return corte;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static CorteCaja cerrarCorte(CorteCaja corte) {
        if (corte != null && corte.getIdCorte() > 0) {
            if (updateCorteCierre(corte)) {
                corte.setAbierta(false);
                corte.setFechaHoraCierre(new Date());
                return corte;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static int insertCorte(CorteCaja corte) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);

            String SQL = "INSERT INTO CorteCaja(FechaHoraApertura, MontoInicio, MontoCierre, EmpleadoAbre) values(now(), " + corte.getMontoInicial() + ", " + corte.getMontoInicial() + ", '" + corte.getEmpleadoAbre() + "')";

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

    private static boolean updateCorteCierre(CorteCaja corte) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = dbConnection.OpenConnection().createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);

            String SQL = "UPDATE CorteCaja set FechaHoraCierre = now(), Estado = false, MontoCierre = " + corte.getMontoCierre() + ", MontoIngreso=" + corte.getMontoIngreso() + ", EmpleadoCierra = '" + corte.getEmpleadoCierra() + "' where IdCorte = " + corte.getIdCorte() + " limit 1";

            //System.out.println(SQL);

            statement.executeUpdate(SQL);
            return true;

        } catch (ClassNotFoundException | SQLException ex) {
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

    public static List<DetalleCorte> getProductosVendidos(Date fechaInicio, Date fechaFin) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            String sql = "call ProductosVendidos('" + dateFormat.format(fechaInicio) + "','" + dateFormat.format(fechaFin) + "')";

            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(sql);

            List<DetalleCorte> detallesCorte = new ArrayList<>();
            while (rs.next()) {
                DetalleCorte item = new DetalleCorte();
                item.setConcepto(rs.getString("concepto"));
                item.setCantidad(rs.getInt("vendidos"));
                item.setIngreso(rs.getFloat("ImporteTotal"));
                detallesCorte.add(item);
            }
            return detallesCorte;
        } catch (ClassNotFoundException | SQLException ex) {
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

    public static ResumenIngreso getResumenIngreso(Date fechaInicio, Date fechaFin) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            String sql = "call ResumenIngresos('" + dateFormat.format(fechaInicio) + "','" + dateFormat.format(fechaFin) + "')";

            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(sql);

            ResumenIngreso resumenIngreso = new ResumenIngreso();
            if (rs.next()) {
                resumenIngreso.setPrimerIngreso(rs.getLong("PrimerIngreso"));
                resumenIngreso.setUltimoIngreso(rs.getLong("UltimoIngreso"));
                resumenIngreso.setIngresoTotal(rs.getFloat("IngresoTotal"));
                return resumenIngreso;
            } else {
                return null;
            }
        } catch (ClassNotFoundException | SQLException ex) {
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
    
    public static DetalleCorte getResumenIngresoPedidosRentas(Date fechaInicio, Date fechaFin) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            String sql = "call ResumenIngresosPedidosRentas('" + dateFormat.format(fechaInicio) + "','" + dateFormat.format(fechaFin) + "')";

            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(sql);

            DetalleCorte resumenIngreso = new DetalleCorte();
            resumenIngreso.setConcepto("PEDIDOS / RENTAS");
            if (rs.next()) {
                
                resumenIngreso.setCantidad(rs.getInt("Cantidad"));
                resumenIngreso.setIngreso(rs.getLong("IngresoTotal"));
                return resumenIngreso;
            } else {
                return resumenIngreso;
            }
        } catch (ClassNotFoundException | SQLException ex) {
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
