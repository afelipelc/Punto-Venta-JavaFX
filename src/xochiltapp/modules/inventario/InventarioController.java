/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.inventario;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import xochiltapp.datasource.InventarioDataSource;
import xochiltapp.model.Producto;
import xochiltapp.model.Proveedor;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class InventarioController extends BorderPane {

    ObservableList<Producto> inventarioProductos = null;
    //View items
    @FXML
    BorderPane contenedorPBP;
    @FXML
    TextField buscarProductoTxt;
    @FXML
    ComboBox buscarEnCmb;
    @FXML
    Button agregarNuevoProductoBtn;
    @FXML
    TableView<Producto> inventarioTable;
    @FXML
    TableView<Proveedor> proveedoresProductoTbl;
    @FXML
    Label totalProductos;
    @FXML
    TableColumn<Producto, Integer> ClaveCol;
    @FXML
    TableColumn<Producto, String> DescripcionCol;
    @FXML
    TableColumn<Producto, Float> PUCol;
    @FXML
    TableColumn<Producto, Integer> ExistsCol;
    @FXML
    TableColumn<Producto, String> ActivoCol;
    @FXML
    TableColumn<Producto, Date> ActualizadoCol;
    @FXML
    TableColumn<Proveedor, String> nombreProveedorCol;
    @FXML
    TableColumn<Proveedor, Float> precioProveedorCol;
    @FXML
    TableColumn<Proveedor, String> telefonoProveedorCol;

    public void setFocus(Event ev) {
        EventHandler eventFocus = new EventHandler() {
            @Override
            public void handle(Event t) {
                buscarProductoTxt.requestFocus();
            }
        };
        eventFocus.handle(ev);
    }

    /**
     * Initializes the controller class.
     */
    public InventarioController(double ancho, double alto) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Inventario.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        contenedorPBP.setPrefSize(ancho, alto);
        // TODO
        buscarEnCmb.getItems().addAll("Inventario", "Flores", "Arreglos", "Insumos", "Bases");
        buscarEnCmb.setValue("Inventario");
        //set columns
        ClaveCol.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("idProducto"));
        DescripcionCol.setCellValueFactory(new PropertyValueFactory<Producto, String>("concepto"));
        PUCol.setCellValueFactory(new PropertyValueFactory<Producto, Float>("precio"));
        ExistsCol.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("exitencias"));
        ActivoCol.setCellValueFactory(new PropertyValueFactory<Producto, String>("estaActivo"));
        ActualizadoCol.setCellValueFactory(new PropertyValueFactory<Producto, Date>("ultimaActualizacion"));
        //proveedor colls
        nombreProveedorCol.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("nombre"));
        telefonoProveedorCol.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("telefono"));
        precioProveedorCol.setCellValueFactory(new PropertyValueFactory<Proveedor, Float>("precioProducto"));
        //cargar inventario
        setTableviewData(InventarioDataSource.Inventario(true));
        //set onKeyPress text field
        buscarProductoTxt.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent t) {
                if (t.getCode().equals(KeyCode.ENTER)) {
                    Buscar(buscarProductoTxt.getText(), buscarEnCmb.getValue().toString());
                }
            }
        });

        this.inventarioTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    doEditarProductoAction(((Node) t.getSource()).getScene().getWindow());
                } else {
                    doProveedoresProducto();
                }
            }
        });
        this.inventarioTable.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    doEditarProductoAction(((Node) t.getSource()).getScene().getWindow());
                } else if (t.getCode() == KeyCode.UP || t.getCode() == KeyCode.DOWN) {
                    doProveedoresProducto();
                }
            }
        });
        this.agregarNuevoProductoBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                doAgregarProductoAction(((Node) t.getSource()).getScene().getWindow());
            }
        });
        this.agregarNuevoProductoBtn.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.TAB && inventarioTable.getItems().size() > 0) {
                    inventarioTable.requestFocus();
                    inventarioTable.focusModelProperty().get().focus(new TablePosition(inventarioTable, 0, ClaveCol));
                    inventarioTable.focusModelProperty().get().focusBelowCell();
                    inventarioTable.getSelectionModel().select(0);

                }
            }
        });

        this.buscarEnCmb.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                doBuscarEnCMBAction();
            }
        });
    }

    private void doEditarProductoAction(Window parent) {
        //open Producto form
        try {
            //get selected Producto
            FXMLLoader fxmlLoader = new FXMLLoader(ProductoFormController.class.getResource("ProductoForm.fxml"));
            //AnchorPane root = (AnchorPane) fxmlLoader.getRoot();
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Datos del Producto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            //stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.initOwner(parent);
            ProductoFormController controller = fxmlLoader.getController();
            Producto itemEdit = inventarioTable.getSelectionModel().getSelectedItem();
            int indexedit = inventarioTable.getItems().indexOf(itemEdit);
            //System.out.println("seleccionado " + indexedit);
            controller.setProducto(itemEdit);
            stage.showAndWait();
            if (controller.ActionResult()) {
                itemEdit = controller.getProducto();
                System.out.println("Actualizado: " + itemEdit.getConcepto());
                inventarioTable.getItems().set(indexedit, itemEdit);
            }
        } catch (Exception ex) {
            //System.out.println("Error loading proveedores producto: " + ex.getMessage() + " stack: " + ex.getCause());
        }
    }

    private void doProveedoresProducto() {
        try {
            proveedoresProductoTbl.setItems(FXCollections.observableList(inventarioTable.getSelectionModel().getSelectedItem().getProveedores()));
        } catch (Exception ex) {
            //nothing  
        }
    }

    public void doBuscarEnCMBAction() {
        //search producto value on category
        if (!buscarProductoTxt.getText().equals("")) {
            Buscar(buscarProductoTxt.getText(), buscarEnCmb.getValue().toString());
        }
    }
    @FXML
    public void doAgregarProductoAction(Window parent) {
        try {
            Parent root = FXMLLoader.load(ProductoFormController.class.getResource("ProductoForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Agregar nuevo Producto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            //stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.initOwner(parent);
            stage.showAndWait();
            
            setTableviewData(InventarioDataSource.Inventario(true));
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + " stack: " + ex.getCause());
        }
    }

    //Search Producto on Inventario
    private void Buscar(String q, String categoria) {
        //System.out.println("Buscando producto que contenga : " + q
        //        + ", en la categoria: " + categoria);
        //buscarProductoTxt.setText("");

        setTableviewData(InventarioDataSource.BuscarEnInventario(q, categoria, true));
    }

    private void setTableviewData(List<Producto> data) {
        try {
            inventarioTable.setItems(FXCollections.observableList(data));
            totalProductos.setText("" + inventarioTable.getItems().size());
        } catch (Exception ex) {
            totalProductos.setText("0");
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
