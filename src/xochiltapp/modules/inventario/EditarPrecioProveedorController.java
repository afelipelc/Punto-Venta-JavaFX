/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.inventario;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import xochiltapp.dialog.*;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class EditarPrecioProveedorController implements Initializable {

    @FXML
    TextField conceptoText;
    @FXML
    TextField precioProveedorText;
    @FXML
    Button aceptarButton;
    @FXML
    Button cancelarButton;
    Stage primaryStage;
    private float precioIngresado = 0;
    private String conceptoProducto = "";
    private boolean actionResult = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.precioProveedorText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                try {
                    float precioIn = Float.valueOf(precioProveedorText.getText());
                } catch (Exception ex) {
                    precioProveedorText.setText("");
                }
            }
        });
        this.aceptarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                primaryStage = (Stage) aceptarButton.getScene().getWindow();

                if (precioProveedorText.getText().equals("")) {
                    FXDialog.showMessageDialog("Ingrese el precio", "Ingresar precio", Message.INFORMATION);
                    return;
                }

                try {
                    precioIngresado = Float.valueOf(precioProveedorText.getText());
                } catch (Exception ex) {
                    FXDialog.showMessageDialog("El precio debe ser un valor mayor que 0", "Ingresar precio", Message.INFORMATION);
                    return;
                }

                if (precioIngresado == 0) {
                    FXDialog.showMessageDialog("El precio debe ser un valor mayor que 0", "Ingresar precio", Message.INFORMATION);
                    return;
                }

                actionResult = true;
                primaryStage.close();
            }
        });

        this.cancelarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (primaryStage == null) {
                    primaryStage = (Stage) aceptarButton.getScene().getWindow();
                }

                actionResult = false;
                primaryStage.close();
            }
        });
    }

    /**
     * @return the precioIngresado
     */
    public float getPrecioIngresado() {
        return precioIngresado;
    }

    /**
     * @param precioIngresado the precioIngresado to set
     */
    public void setPrecioIngresado(float precioIngresado) {
        this.precioIngresado = precioIngresado;

        this.precioProveedorText.setText(this.precioIngresado + "");
    }

    /**
     * @return the actionResult
     */
    public boolean isActionResult() {
        return actionResult;
    }

    public void handleAceptar_Button() {
        primaryStage = (Stage) aceptarButton.getScene().getWindow();

        if (precioProveedorText.getText().equals("")) {
            FXDialog.showMessageDialog("Ingrese el precio", "Ingresar precio", Message.INFORMATION);
            return;
        }

        try {
            this.precioIngresado = Float.valueOf(precioProveedorText.getText());
        } catch (Exception ex) {
            FXDialog.showMessageDialog("El precio debe ser un valor mayor que 0", "Ingresar precio", Message.INFORMATION);
            return;
        }

        if (this.precioIngresado == 0) {
            FXDialog.showMessageDialog("El precio debe ser un valor mayor que 0", "Ingresar precio", Message.INFORMATION);
            return;
        }

        this.actionResult = true;
        this.primaryStage.close();
    }

    public void handleCancelar_Button() {
        if (primaryStage == null) {
            primaryStage = (Stage) aceptarButton.getScene().getWindow();
        }

        this.actionResult = false;
        this.primaryStage.close();
    }

    /**
     * @return the conceptoProducto
     */
    public String getConceptoProducto() {
        return conceptoProducto;
    }

    /**
     * @param conceptoProducto the conceptoProducto to set
     */
    public void setConceptoProducto(String conceptoProducto) {
        this.conceptoProducto = conceptoProducto;
        this.conceptoText.setText(conceptoProducto);
    }
}
