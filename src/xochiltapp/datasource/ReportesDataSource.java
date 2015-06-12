/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.datasource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import xochiltapp.model.ReporteModel;

/**
 *
 * @author afelipelc
 */
public final class ReportesDataSource {

    static DBConnection dbConnection = new DBConnection();
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static List<ReporteModel> saldoFecha(Date fecha) {
        return processListReporteResult("call SaldoDia('" + dateFormat2.format(fecha) + "')", 1, dateFormat.format(fecha));
    }

    public static List<ReporteModel> saldoSemana(Date fecha) {
        return processListReporteResult("call SaldoSemana('" + dateFormat2.format(fecha) + "')", 2, null);
    }

    public static List<ReporteModel> saldoMes(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);

        return processListReporteResult("call SaldoMes('" + calendario.get(Calendar.YEAR) + (calendario.get(Calendar.MONTH)+1) + "')", 3, null);
    }

    public static List<ReporteModel> saldoAnios(int inicio, int aniofin) {

        return processListReporteResult("call SaldoMesAnios(" + inicio + "," + aniofin + ")", 4, null);
    }

    private static List<ReporteModel> processListReporteResult(String q, int TipoVer, String tiempo) {
        Statement statement = null;
        ResultSet rs = null;

        List<ReporteModel> listaItems = new ArrayList<>();
        try {
            statement = dbConnection.OpenConnection().createStatement();
            rs = statement.executeQuery(q);

            while (rs.next()) {
                ReporteModel item = new ReporteModel();
                if (TipoVer == 1) {
                    //dia
                    item.setPeriodo(tiempo);
                } else if (TipoVer == 2) {
                    //semana
                    item.setPeriodo(rs.getString("Dia"));
                } else if (TipoVer == 3) {
                    //mes
                    item.setPeriodo(rs.getString("DiasSemana"));
                } else if (TipoVer == 4) {
                    //anio
                    Calendar calendario = Calendar.getInstance();
                    //calendario.set(rs.getInt("anio"), rs.getInt("mes"), 1);
                    calendario.set(Calendar.YEAR, rs.getInt("anio"));
                    calendario.set(Calendar.MONTH, (rs.getInt("mes")-1));
                    calendario.set(Calendar.DAY_OF_MONTH, 1);
                    
                    String nameMonth = new SimpleDateFormat("MMMMM").format(calendario.getTime());
                    item.setPeriodo(nameMonth + "-" + rs.getInt("anio"));
                }
                //item.setConcepto(concepto);
                item.setIngreso(rs.getFloat("IngresoTotal"));
                item.setEgreso(rs.getFloat("EgresoTotal"));

                item.setSaldo(item.getIngreso() - item.getEgreso());
                
                listaItems.add(item);
            }

            return listaItems;
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
