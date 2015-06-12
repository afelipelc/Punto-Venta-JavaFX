/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.inventario;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import xochiltapp.dialog.*;
import xochiltapp.datasource.InventarioDataSource;
import xochiltapp.model.Producto;
import xochiltapp.model.Proveedor;
import xochiltapp.modules.proveedores.SeleccionarProveedorFormController;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class ProductoFormController implements Initializable {

    @FXML
    TextField claveProductoTxt;
    @FXML
    TextField conceptoTxt;
    @FXML
    TextField precioUnitarioTxt;
    @FXML
    ComboBox categoriaCmb;
    @FXML
    TextField existenciasTxt;
    @FXML
    CheckBox activoChk;
    @FXML
    Label ultimaActualizacionLabel;
    @FXML
    Button quitarPSeleccionadoBtn;
    @FXML
    Button agregarProvBtn;
    @FXML
    Button cancelarBtn;
    @FXML
    Button aceptarBtn;
    @FXML
    TableView<Proveedor> proveedoresTbl;
    @FXML
    TableColumn<Proveedor, String> nombreCol;
    @FXML
    TableColumn<Proveedor, String> telefonoCol;
    @FXML
    TableColumn<Proveedor, Float> PrecioCol;
    Producto producto;
    Stage primaryStage;
    boolean actionResult = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //get stage
        categoriaCmb.getItems().addAll("Flores", "Arreglos", "Insumos", "Bases");

        activoChk.setSelected(true);
        quitarPSeleccionadoBtn.setDisable(true);
        agregarProvBtn.setDisable(true);

        nombreCol.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("nombre"));
        telefonoCol.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("telefono"));
        PrecioCol.setCellValueFactory(new PropertyValueFactory<Proveedor, Float>("precioProducto"));

        this.claveProductoTxt.setDisable(true);

        this.aceptarBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                doAceptarButtonAction();
            }
        });
        
        this.cancelarBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                doCancelarButtonAction();
            }
        });
        
        this.proveedoresTbl.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent t) {
                doProveedoresTblAction(t);
            }
            
        });
        
        this.precioUnitarioTxt.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent t) {
                try{
                    float precio = Float.valueOf(precioUnitarioTxt.getText());
                }catch(Exception ex)
                {
                    precioUnitarioTxt.setText("");
                }
            }
            
        });
    }

    public void setProducto(Producto producto) {
        //Todo
        this.producto = producto;
        //System.out.println(producto.getIdProducto() + " " + producto.getConcepto());
        loadProductoData();
        loadProveedoresTableData();
        this.agregarProvBtn.setDisable(false);
    }

    public boolean ActionResult() {
        return actionResult;
    }

    public Producto getProducto() {
        return producto;
    }

    private void loadProveedoresTableData() {
        try {
            if (producto != null) {
                proveedoresTbl.setItems(FXCollections.observableList(producto.getProveedores()));
            }
        } catch (Exception ex) {
            //System.err.println("Error: " + ex.getMessage());
        }
    }

    public void handleQuitarProveedorBtn_Action(ActionEvent event) {
        if (proveedoresTbl.getSelectionModel().getSelectedItem() != null) {
            //add idproveedor to List
            if (!producto.getProveedoresEliminar().contains(proveedoresTbl.getSelectionModel().getSelectedItem().getIdProveedor())) {
                producto.getProveedoresEliminar().add(proveedoresTbl.getSelectionModel().getSelectedItem().getIdProveedorProducto());
                producto.getProveedores().remove(proveedoresTbl.getSelectionModel().getSelectedItem());
                loadProveedoresTableData();
            }
        } else {
            //FXDialog.showMessageDialog("Seleccione el proveedor del producto a quitar.", "Producto guardado", Message.INFORMATION);
            quitarPSeleccionadoBtn.setDisable(true);
        }
    }

    public void handleAgregarProveedorBtn_Action(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SeleccionarProveedorFormController.class.getResource("SeleccionarProveedorForm.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Proveedor");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            SeleccionarProveedorFormController controllerp = fxmlLoader.getController();
            stage.showAndWait();
            if (controllerp.ActionResult() && controllerp.getProveedor() != null) {
                //set proveedor
                //find proveedor
                if (producto.getProveedores().size() > 0) {
                    boolean isProveedor = false;

                    for (Proveedor item : producto.getProveedores()) {
                        if (item.getIdProveedor() == controllerp.getProveedor().getIdProveedor()) {
                            isProveedor = true;
                            break;
                        }
                    }

                    if (isProveedor) {
                        FXDialog.showMessageDialog("El proveedor: " + controllerp.getProveedor().getNombre() + " ya se encuentra en la lista", "Producto guardado", Message.INFORMATION);
                    } else {
                        producto.getProveedores().add(controllerp.getProveedor());
                    }
                } else {
                    producto.getProveedores().add(controllerp.getProveedor());
                }

                loadProveedoresTableData();
            } else {
                FXDialog.showMessageDialog("No se pudo agregar el proveedor a la lista.", "Producto guardado", Message.INFORMATION);
            }
        } catch (Exception ex) {
         FXDialog.showMessageDialog("Error loading seleccionar proveedores: " + ex.getMessage() + " stack: " + ex.getCause(), "Producto no gurdado", Message.INFORMATION); 
         //System.out.println("Error loading seleccionar proveedores: " + ex.getMessage() + " stack: " + ex.getCause());
        }
    }

    public void doCancelarButtonAction() {
        //todo
        primaryStage = (Stage) aceptarBtn.getScene().getWindow();
        actionResult = false;
        primaryStage.close();
    }

    public void doAceptarButtonAction() {
        primaryStage = (Stage) aceptarBtn.getScene().getWindow();

        //set Producto data
        if (!setProductoData()) {
            return;
        }

        this.producto = InventarioDataSource.saveProducto(producto);
        if (producto != null) {
            FXDialog.showMessageDialog("El producto ha sido guardado.", "Producto guardado", Message.INFORMATION);
            actionResult = true;
            primaryStage.close();
        } else {
            FXDialog.showMessageDialog("Ocurrió un error al intentar guardar el producto.", "Error:", Message.ERROR);
            actionResult = false;
            primaryStage.close();
        }

    }

    public void handleCategoriaCmb_Action(ActionEvent event) {
        //todo
    }

    public void doProveedoresTblAction(MouseEvent event) {
        if (event.getClickCount() == 2) {
            //show change Precio window
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(EditarPrecioProveedorController.class.getResource("EditarPrecioProveedor.fxml"));

                //System.out.println("Cargando precio");
                
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Establecer precio del Proveedor");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) event.getSource()).getScene().getWindow());
                EditarPrecioProveedorController controllerPrecio = fxmlLoader.getController();
                Proveedor proveedorEdit = this.proveedoresTbl.getSelectionModel().getSelectedItem();
                int indexedit = proveedoresTbl.getItems().indexOf(proveedorEdit);
                controllerPrecio.setPrecioIngresado(proveedorEdit.getPrecioProducto());
                controllerPrecio.setConceptoProducto(producto.getConcepto());
                stage.showAndWait();
                if (controllerPrecio.isActionResult()) {
                  proveedorEdit.setPrecioProducto(controllerPrecio.getPrecioIngresado());
                  FXDialog.showMessageDialog("se establecio precio: "+ proveedorEdit.getPrecioProducto() +" para el producto  "+producto.getConcepto(), "Producto", Message.INFORMATION);
                  //System.out.println("se establecio precio: "+ proveedorEdit.getPrecioProducto() +" para el producto  "+producto.getConcepto());
                     //proveedoresTbl.getItems().clear();
                    //producto.getProveedores().set(indexedit, proveedorEdit);
                    proveedoresTbl.setItems(FXCollections.observableList(producto.getProveedores()));
                }

            } catch (Exception ex) {
              FXDialog.showMessageDialog("Error loading proveedore - precio producto: " + ex.getMessage() + " stack: " + ex.getCause(), "Producto", Message.INFORMATION);
              //System.out.println("Error loading proveedore - precio producto: " + ex.getMessage() + " stack: " + ex.getCause());
            }

        }else if (this.proveedoresTbl.getSelectionModel().getSelectedItem() != null) {
            quitarPSeleccionadoBtn.setDisable(false);
        }else
        {
            this.aceptarBtn.requestFocus();
        }
    }

    private boolean setProductoData() {
        if (this.conceptoTxt.getText().equals("")
                || this.categoriaCmb.getValue().toString().equals("Seleccionar")
                || this.precioUnitarioTxt.getText().equals("")) {
            FXDialog.showMessageDialog("Todos los datos son obligatorios", "Ingrese datos", Message.INFORMATION);
            return false;
        }

        if (producto == null) {
            producto = new Producto();
        }

        if (this.existenciasTxt.getText().equals("")) {
            this.existenciasTxt.setText("0");
        }

        this.producto.setConcepto(this.conceptoTxt.getText());
        this.producto.setCategoria(this.categoriaCmb.getValue().toString());
        this.producto.setPrecio(Float.valueOf(this.precioUnitarioTxt.getText()));
        this.producto.setExitencias(Integer.valueOf(this.existenciasTxt.getText()));
        this.producto.setActivo(this.activoChk.isSelected());

        //set proveedores

        return true;
    }

    private void loadProductoData() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.claveProductoTxt.setText(this.producto.getIdProducto() + "");
        this.conceptoTxt.setText(this.producto.getConcepto());
        this.categoriaCmb.setValue(this.producto.getCategoria());
        this.precioUnitarioTxt.setText(this.producto.getPrecio() + "");
        this.existenciasTxt.setText(this.producto.getExitencias() + "");
        this.activoChk.setSelected(this.producto.isActivo());
        this.ultimaActualizacionLabel.setText(format.format(producto.getUltimaActualizacion()));
    }
}
