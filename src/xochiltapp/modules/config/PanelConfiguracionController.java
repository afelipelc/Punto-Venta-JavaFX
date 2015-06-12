/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.config;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import xochiltapp.model.Empleado;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class PanelConfiguracionController extends AnchorPane {

    @FXML
    AnchorPane contenedorPAP;
    @FXML
    Button empleadosButton;
    Empleado empleado;

    public PanelConfiguracionController(Empleado empleado, double ancho, double alto) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PanelConfiguracion.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.empleado = empleado;
        this.contenedorPAP.setPrefSize(ancho, alto);
        
        this.empleadosButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                abrirEmpleadosWin(((Node) t.getSource()).getScene().getWindow());
            }
        });
    }
    
    private void abrirEmpleadosWin(Window parent)
    {
        try {
                FXMLLoader fxmlLoader = new FXMLLoader(EmpleadosFormController.class.getResource("EmpleadosForm.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Administración de empleados");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(parent);
                EmpleadosFormController controllerPrecio = fxmlLoader.getController();
                
                controllerPrecio.setEmpleado(empleado);
                stage.showAndWait();

            } catch (Exception ex) {
                
            }
    }
}
