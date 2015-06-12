/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.compra;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import xochiltapp.datasource.InventarioDataSource;
import xochiltapp.model.Producto;
import xochiltapp.model.Proveedor;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class PrecioCompraProductoController implements Initializable {

    @FXML
    ComboBox<Proveedor> proveedoresCmb;
    @FXML
    TextField precioCompraText;
    @FXML
    Button aceptarButton;
    @FXML
    Button cancelarButton;
    @FXML
    Label notifLabel;
    Stage primaryStage;
    private Producto producto;
    private float precioProducto = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        this.proveedoresCmb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (proveedoresCmb.getSelectionModel().getSelectedItem() != null) {
                    proveedoresCmb.setButtonCell(new ListCell<Proveedor>() {
                    @Override
                    protected void updateItem(Proveedor t, boolean b) {
                        super.updateItem(t, b);
                        if(t!=null)
                            setText(t.getNombre());
                        else
                            setText("");
                    }
                });
                    precioCompraText.setText(proveedoresCmb.getSelectionModel().getSelectedItem().getPrecioProducto() + "");
                }
            }
        });

        this.proveedoresCmb.setCellFactory(new Callback<ListView<Proveedor>, ListCell<Proveedor>>() {
            @Override
            public ListCell<Proveedor> call(ListView<Proveedor> p) {
                final ListCell<Proveedor> cell = new ListCell<Proveedor>() {
                    @Override
                    protected void updateItem(Proveedor t, boolean b) {
                        super.updateItem(t, b);
                        if(t!=null)
                            setText(t.getNombre());
                        else
                            setText("");
                    }
                };
                return cell;
            }
        });

        this.proveedoresCmb.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    if (proveedoresCmb.getSelectionModel().getSelectedItem() != null) {
                        proveedoresCmb.setButtonCell(new ListCell<Proveedor>() {
                    @Override
                    protected void updateItem(Proveedor t, boolean b) {
                        super.updateItem(t, b);
                        if(t!=null)
                            setText(t.getNombre());
                        else
                            setText("");
                    }
                });
                        precioCompraText.requestFocus();
                    } else {
                        notifLabel.setText("Seleccione el proveedor");
                    }
                }
            }
        });

        this.precioCompraText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                try {
                    precioProducto = (float) Float.valueOf(precioCompraText.getText());
                } catch (Exception ex) {
                    precioCompraText.setText("");
                    precioProducto = 0;
                }
            }
        });

        this.aceptarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                procesarPrecio();
            }
        });

        this.cancelarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                precioProducto = 0;
                primaryStage.close();
            }
        });
    }

    private void procesarPrecio() {
        if (this.proveedoresCmb.getSelectionModel().getSelectedItem() == null) {
            this.notifLabel.setText("Seleccione proveedor.");
            this.proveedoresCmb.requestFocus();
        }

        if (this.precioProducto == 0) {
            this.notifLabel.setText("Ingrese el precio.");
            this.precioCompraText.setText("");
            this.precioCompraText.requestFocus();
            return;
        }

        this.proveedoresCmb.getSelectionModel().getSelectedItem().setPrecioProducto(precioProducto);
        //verificar que actualice el precio en BD
        InventarioDataSource.saveProducto(producto);
        primaryStage.close();
    }

    /**
     * @return the producto
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * @param producto the producto to set
     */
    public void setProducto(Producto producto) {
        this.producto = producto;

        //cargar proveedores del producto
        if (producto.getProveedores().size() > 0) {
            proveedoresCmb.setItems(FXCollections.observableList(this.producto.getProveedores()));
        } else {
            this.notifLabel.setText("Producto sin proveedores.");
            this.proveedoresCmb.setDisable(true);
            this.precioCompraText.setDisable(true);
            this.cancelarButton.requestFocus();
        }
        //proveedoresCmb.setCellFactory(new PropertyValueFactory<Proveedor, String>("nombre"));
        primaryStage = (Stage) aceptarButton.getScene().getWindow();
        proveedoresCmb.requestFocus();
    }

    /**
     * @return the precioProducto
     */
    public float getPrecioProducto() {
        return precioProducto;
    }
}
