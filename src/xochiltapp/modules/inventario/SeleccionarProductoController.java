/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.inventario;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import xochiltapp.datasource.InventarioDataSource;
import xochiltapp.model.Producto;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class SeleccionarProductoController implements Initializable {

    Stage primaryStage;
    //InventarioDataSource inventarioDS = new InventarioDataSource();
    @FXML
    TextField buscarProductoText;
    @FXML
    Label notificLabel;
    @FXML
    Button cerrarButton;
    @FXML
    TableView<Producto> inventarioTable;
    @FXML
    TableColumn<Producto, Integer> ClaveCol;
    @FXML
    TableColumn<Producto, String> DescripcionCol;
    @FXML
    TableColumn<Producto, Float> PUCol;
    @FXML
    TableColumn<Producto, Integer> ExistsCol;
    private Producto productoSeleccionado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        //set columns
        ClaveCol.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("idProducto"));
        DescripcionCol.setCellValueFactory(new PropertyValueFactory<Producto, String>("concepto"));
        PUCol.setCellValueFactory(new PropertyValueFactory<Producto, Float>("precio"));
        ExistsCol.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("exitencias"));


        buscarProductoText.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent t) {
                notificLabel.setText("");
                if (!buscarProductoText.getText().equals("") && t.getCode() == KeyCode.ENTER) {
                    inventarioTable.requestFocus();
                } else {
                    Buscar(buscarProductoText.getText());
                }
            }
        });

        this.buscarProductoText.requestFocus();
        
        this.inventarioTable.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                primaryStage = (Stage) inventarioTable.getScene().getWindow();
                if (t.getCode() == KeyCode.ENTER) {
                    
                    //set selected and close
                    if (inventarioTable.getSelectionModel().getSelectedItem() != null) {
                        productoSeleccionado = inventarioTable.getSelectionModel().getSelectedItem();
                        primaryStage.close();
                    }
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    primaryStage.close();
                }
            }
        });

        this.inventarioTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                primaryStage = (Stage) inventarioTable.getScene().getWindow();
                if (t.getClickCount() == 2) {
                    //set selected and close
                    if (inventarioTable.getSelectionModel().getSelectedItem() != null) {
                        productoSeleccionado = inventarioTable.getSelectionModel().getSelectedItem();
                        primaryStage.close();
                    }
                }
            }
        });

        this.cerrarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                primaryStage = (Stage) buscarProductoText.getScene().getWindow();
                primaryStage.close();
            }
        });

        this.Buscar("");
        this.inventarioTable.requestFocus();
    }

    //Search Producto on Inventario
    private void Buscar(String q) {
        setTableviewData(InventarioDataSource.BuscarEnInventario(q, null, false));
    }

    private void setTableviewData(List<Producto> data) {
        try {
            if (data.isEmpty()) {
                this.notificLabel.setText("No se encontró ningún artículo.");
            }

            inventarioTable.setItems(FXCollections.observableList(data));
        } catch (Exception ex) {
            this.notificLabel.setText("Error al buscar producto");
        }
    }

    /**
     * @return the productoSeleccionado
     */
    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }
}
