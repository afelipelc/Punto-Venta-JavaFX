/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.caja;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import xochiltapp.dialog.*;
import xochiltapp.MainApp;
import xochiltapp.datasource.CajaDataSource;
import xochiltapp.model.CorteCaja;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class AbrirCajaController implements Initializable {

    @FXML
    TextField saldoInicialText;
    @FXML
    Button aceptarButton;
    @FXML
    Button cancelarButton;
    private MainApp application;

    public void setApp(MainApp application) {
        this.application = application;
    }
    float importe;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.aceptarButton.setDisable(true);
        this.saldoInicialText.requestFocus();

        this.saldoInicialText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {

                try {
                    if (saldoInicialText.getText().equals("")) {
                        importe = 0;
                        aceptarButton.setDisable(true);
                    } else {
                        importe = Float.valueOf(saldoInicialText.getText());
                        aceptarButton.setDisable(false);
                    }
                } catch (NumberFormatException ex) {
                    importe = 0;
                    saldoInicialText.setText("");
                    saldoInicialText.setPromptText("Ingresar números.");
                }
            }
        });

        this.aceptarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                IniciarCaja();
            }
        });

        this.cancelarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                application.getStage().close();
            }
        });

    }

    private void IniciarCaja() {
        if (saldoInicialText.getText().equals("")) {
            importe = 0;
            aceptarButton.setDisable(true);
            this.saldoInicialText.setPromptText("Ingrese el Saldo");
            this.saldoInicialText.requestFocus();
            return;
        }

        try {
            importe = Float.valueOf(saldoInicialText.getText());
        } catch (NumberFormatException ex) {
            importe = 0;
            saldoInicialText.setText("");
            saldoInicialText.setPromptText("Ingresar números.");
            saldoInicialText.requestFocus();
            return;
        }

        CorteCaja nuevoCorte = new CorteCaja();
        nuevoCorte.setMontoInicial(importe);
        nuevoCorte.setMontoCierre(importe);
        nuevoCorte.setEmpleadoAbre(application.getEmpleado().getNombre());
        nuevoCorte = CajaDataSource.abrirCorte(nuevoCorte);
        if (nuevoCorte == null) {
            FXDialog.showMessageDialog("Ha ocurrido un error al intentar\nabrir el corte de caja.\n\nRequiere soporte técnico.", "ERROR: iniciar Corte", Message.INFORMATION);
            application.getStage().close();
            return;
        }
        application.gotoMainWindow();
    }
}
