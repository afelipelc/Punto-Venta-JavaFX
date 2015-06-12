/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.pedidorenta;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import xochiltapp.datasource.PedidosRentasDataSource;
import xochiltapp.model.PedidoRenta;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class PedidosRentasPendientesController implements Initializable {

    @FXML
    TableView<PedidoRenta> pedidosRentasTable;
    @FXML
    TableColumn<PedidoRenta, Long> idPedidoRCol;
    @FXML
    TableColumn<PedidoRenta, String> clienteCol;
    @FXML
    TableColumn<PedidoRenta, String> fechaSolicitudCol;
    @FXML
    TableColumn<PedidoRenta, String> fechaEntregaCol;
    @FXML
    TableColumn<PedidoRenta, String> estadoCol;
    @FXML
    TableColumn<PedidoRenta, String> tipoCol;
    @FXML
    TableColumn<PedidoRenta, Float> importeCol;
    private long idPedidoRenta;
    Stage primaryStage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        //set columns
        idPedidoRCol.setCellValueFactory(new PropertyValueFactory<PedidoRenta, Long>("idPedidoRenta"));
        clienteCol.setCellValueFactory(new PropertyValueFactory<PedidoRenta, String>("nombreCliente"));
        tipoCol.setCellValueFactory(new PropertyValueFactory<PedidoRenta, String>("tipo"));
        fechaSolicitudCol.setCellValueFactory(new PropertyValueFactory<PedidoRenta, String>("fechaSolicitudString"));
        fechaEntregaCol.setCellValueFactory(new PropertyValueFactory<PedidoRenta, String>("fechaEntregaString"));
        estadoCol.setCellValueFactory(new PropertyValueFactory<PedidoRenta, String>("estado"));
        importeCol.setCellValueFactory(new PropertyValueFactory<PedidoRenta, Float>("importe"));

        this.pedidosRentasTable.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                primaryStage = (Stage) pedidosRentasTable.getScene().getWindow();
                
                if (t.getCode() == KeyCode.ENTER) {
                    if (pedidosRentasTable.getSelectionModel().getSelectedItem() != null) {
                        idPedidoRenta = pedidosRentasTable.getSelectionModel().getSelectedItem().getIdPedidoRenta();
                        primaryStage.close();
                    }
                }else if(t.getCode() == KeyCode.ESCAPE)
                {
                    idPedidoRenta=0;
                    primaryStage.close();
                }
            }
        });
        
        pedidosRentasTable.setItems(FXCollections.observableList(PedidosRentasDataSource.pedidosRentaPendientes()));
        
        pedidosRentasTable.requestFocus();
    }

    /**
     * @return the idPedidoRenta
     */
    public long getIdPedidoRenta() {
        return idPedidoRenta;
    }

    /**
     * @param idPedidoRenta the idPedidoRenta to set
     */
    public void setIdPedidoRenta(long idPedidoRenta) {
        this.idPedidoRenta = idPedidoRenta;
    }
}
