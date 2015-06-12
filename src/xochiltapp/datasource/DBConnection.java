/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import xochiltapp.dialog.*;

/**
 *
 * @author afelipelc
 */
public class DBConnection {
    
   private String usuario = "afelipelc";
   private String password = "asdf123";
   //private String driverClass = "com.mysql.jdbc.Driver";
   
   private String dbUrl = "jdbc:mysql://localhost/xochiltdb?";
   private Connection connectionDB;
   
   public Connection OpenConnection() throws ClassNotFoundException
   {
       try{           
           connectionDB = DriverManager.getConnection(dbUrl + "user=" + usuario +"&password="+password);
           return connectionDB;
       }
       catch(SQLException ex)
       {
          //System.out.println("SQLException: " + ex.getMessage());
          //System.out.println("SQLState: " + ex.getSQLState());
          //System.out.println("VendorError: " + ex.getErrorCode());
          
           FXDialog.showMessageDialog("No se ha podido establecer la conexión\ncon el almacén de datos.\n\nEjecute services.msc, localice el servicio MySQL.\nVerifique que el servicio está iniciado, si no lo está, inícielo\n y vuelva al intentar utilizar la aplicación.", "Sin conexión al almacén de datos", Message.ERROR);
          
          return null;
       }
       
   }
   
   public void CloseConnection()
   {
        try{
            if(connectionDB != null && !connectionDB.isClosed())
                connectionDB.close();
        }catch(SQLException ex)
        {
          FXDialog.showMessageDialog("MySQL Error: " + ex.getMessage(), "Al cerrar la conexión", Message.INFORMATION);
        }
   }
   
  
}