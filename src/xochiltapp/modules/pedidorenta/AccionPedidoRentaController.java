/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.pedidorenta;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import xochiltapp.dialog.*;
import xochiltapp.model.PedidoRenta;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class AccionPedidoRentaController implements Initializable {

    @FXML
    Button actualizarButton;
    @FXML
    Button enviarButton;
    @FXML
    Button entregadoButton;
    @FXML
    Button devueltoButton;
    @FXML
    Button ningunoButton;
    @FXML
    Label tituloLabel;
    AccionPedido accionSeleccionada;

    Stage primaryStage;
    public AccionPedido getAccion() {
        return this.accionSeleccionada;
    }

    public void setPedidoRenta(PedidoRenta pedidoRenta) {
        if (pedidoRenta == null) {
            this.accionSeleccionada = AccionPedido.Error;
            return;
        }

        if (pedidoRenta.isPedido()) {
            this.tituloLabel.setText(this.tituloLabel.getText().replace("[tipo]", "este Pedido"));
            this.devueltoButton.setDisable(true);
            this.devueltoButton.setVisible(false);
        } else if (pedidoRenta.isPedido()) {
            this.tituloLabel.setText(this.tituloLabel.getText().replace("[tipo]", "esta Renta"));
            this.devueltoButton.setDisable(!(pedidoRenta.isEnviado() && pedidoRenta.isEntregado()));
            
        }

        this.enviarButton.setDisable(pedidoRenta.isEnviado());
        this.actualizarButton.setDisable(pedidoRenta.isEnviado());
        this.entregadoButton.setDisable(!pedidoRenta.isEnviado());
        
        primaryStage = (Stage) this.tituloLabel.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.actualizarButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                accionSeleccionada = AccionPedido.Actualizar;
                primaryStage.close();
            }
        });
        
        this.enviarButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (!Confirmar("¿Confirma marcar el Pedido/Renta \ncomo Enviado para su entrega?")) {
                    return;
                }
                accionSeleccionada = AccionPedido.Enviado;
                primaryStage.close();
            }
        });
        
        this.entregadoButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (!Confirmar("¿Confirma marcar el Pedido/Renta \ncomo Entregado?")) {
                    return;
                }
                accionSeleccionada = AccionPedido.Entregado;
                primaryStage.close();
            }
        });
        
        this.devueltoButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (!Confirmar("¿Confirma marcar la Renta como Devuelta, \nlas partes han regresado al establecimiento?")) {
                    return;
                }
                accionSeleccionada = AccionPedido.Devuelto;
                primaryStage.close();
            }
        });
        
        this.ningunoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                accionSeleccionada = AccionPedido.Ninguno;
                primaryStage.close();
            }
        });
    }
    
    private boolean Confirmar(String mensaje)
    {
        return FXDialog.showConfirmDialog(mensaje,"Confirmar acción", ConfirmationType.YES_NO_OPTION);
    }
}
