/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.login;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import xochiltapp.MainApp;
import xochiltapp.datasource.EmpleadosDataSource;
import xochiltapp.model.Empleado;

/**
 *
 * @author afelipelc
 */
public class LoginController extends AnchorPane implements Initializable {
    /*
     ScreensController myController;
     public void setScreenParent(ScreensController screenParent){
     myController = screenParent;
     }
     */

    private MainApp application;

    public void setApp(MainApp application) {
        this.application = application;
    }
    @FXML
    TextField nombreUsuarioTxt;
    @FXML
    PasswordField passwordTxt;
    @FXML
    private Label notif;
    @FXML
    Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                autentificarEmpleado();
            }
        });
    }

    private void autentificarEmpleado() {
        //validar usuario
        if (nombreUsuarioTxt.getText().equals("")) {
            this.notif.setText("Ingresar nombre de usuario.");
            this.nombreUsuarioTxt.requestFocus();
            return;
        }

        if (passwordTxt.getText().equals("")) {
            this.notif.setText("Ingresar contraseña.");
            this.passwordTxt.requestFocus();
            return;
        }

        Empleado empleado = EmpleadosDataSource.validarEmpleado(nombreUsuarioTxt.getText(), passwordTxt.getText());

        if (empleado != null) {
            if (!empleado.isActivo()) {
                this.notif.setText("Emplado NO ACTIVO");
                return;
            }
            
            if(!empleado.isConsulta() && !empleado.isAdministrador() && !empleado.isVendedor())
            {
                this.notif.setText("Empleado sin permisos.");
                return;
            }
            
            application.setEmpleado(empleado);
            
            if (empleado.isAdministrador() || empleado.isAdministrador()) {
                application.gotoComprobarCaja();
            } else {
                application.gotoMainWindow();
            }
        } else {
            this.notif.setText("Usuario o Contraseña incorrectos.");
            this.nombreUsuarioTxt.setText("");
            this.passwordTxt.setText("");
            this.nombreUsuarioTxt.requestFocus();
        }
        //application.gotoProveedores();
    }
}
