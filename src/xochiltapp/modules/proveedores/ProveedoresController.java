/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.proveedores;

import java.io.IOException;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import xochiltapp.datasource.InventarioDataSource;
import xochiltapp.datasource.ProveedoresDataSource;
import xochiltapp.dialog.FXDialog;
import xochiltapp.dialog.Message;
import xochiltapp.model.Producto;
import xochiltapp.model.Proveedor;
import xochiltapp.modules.inventario.ProductoFormController;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class ProveedoresController extends BorderPane {

    
    @FXML
    BorderPane contenedorPBP;
    @FXML
    Button agregarProveedorButton;
    @FXML
    TableView<Proveedor> proveedoresTable;
    @FXML
    TableView<Producto> productosProveedorTable;
    @FXML
    Label totalProveedoresLabel;
    @FXML
    Label totalProductosProveedorLabel;
    
    @FXML
    TableColumn<Proveedor, Integer> claveProveedorCol;
    @FXML
    TableColumn<Proveedor, String> nombreProveedorCol;
    @FXML
    TableColumn<Proveedor, String> telefonoProveedorCol;
    
    @FXML
    TableColumn<Producto, Integer> claveProductoCol;
    
    @FXML
    TableColumn<Producto, String> conceptoProductoCol;
    @FXML
    TableColumn<Producto, Date> actualizacionProductoCol;
    
    
    ProveedoresDataSource proveedoresDs = new ProveedoresDataSource();
    InventarioDataSource inventarioDs = new InventarioDataSource();
    private int idProveedorSelected = 0;
    /**
     * Initializes the controller class.
     */
 public ProveedoresController(double ancho, double alto){

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Proveedores.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
            
        }
        
        contenedorPBP.setPrefSize(ancho, alto);
        
        claveProveedorCol.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("idProveedor"));
        nombreProveedorCol.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("Nombre"));
        telefonoProveedorCol.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("TelCasa"));
        
        claveProductoCol.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("idProducto"));
        conceptoProductoCol.setCellValueFactory(new PropertyValueFactory<Producto, String>("concepto"));
        actualizacionProductoCol.setCellValueFactory(new PropertyValueFactory<Producto, Date>("ultimaActualizacion"));
        
        this.CargarProveedores();
    }
    
    public void handleAgregarProveedor_Button(ActionEvent event) {
        try
        {
            Parent root = new FXMLLoader().load(ProveedorFormController.class.getResource("ProveedorForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Agregar nuevo Proveedor");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow()
            );
            stage.showAndWait();
            
            CargarProveedores();
            
        }catch(Exception ex)  {
          FXDialog.showMessageDialog("Error: " + ex.getMessage()+ " stack: " + ex.getCause(), "Agregar Proveedor", Message.INFORMATION);
           // System.out.println("Error: " + ex.getMessage()+ " stack: " + ex.getCause());
        }
    }
    
    public void handleClickProveedores_Table(MouseEvent event)   {
        if(event.getClickCount() ==2)
        {
           //open Proveedor form
                    try
                    {
                        FXMLLoader fxmlLoader = new FXMLLoader(ProveedorFormController.class.getResource("ProveedorForm.fxml"));
                        Parent root = (Parent)fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setTitle("Datos del Proveedor");
                        stage.setScene(new Scene(root));
                        stage.initModality(Modality.WINDOW_MODAL);
                        stage.initOwner(
                                ((Node) event.getSource()).getScene().getWindow()
                        );
                        
                        ProveedorFormController controller = fxmlLoader.getController();
                        Proveedor itemEdit = this.proveedoresTable.getSelectionModel().getSelectedItem();
                        controller.setProveedor(itemEdit);
                        stage.showAndWait();
                        if(controller.ActionResult())
                        {
                            CargarProveedores();
                        }
                        
                    }catch(Exception ex) {
                      FXDialog.showMessageDialog("Error loading proveedor form: " + ex.getMessage()+ " stack: " + ex.getCause(), "Tabla", Message.INFORMATION);
                        //System.out.println("Error loading proveedor form: " + ex.getMessage()+ " stack: " + ex.getCause());
                    }
        }else
        {
        if(this.proveedoresTable.getSelectionModel().getSelectedItem()!=null)
            this.CargarProductosProveedor(this.proveedoresTable.getSelectionModel().getSelectedItem());
        idProveedorSelected = proveedoresTable.getItems().indexOf(this.proveedoresTable.getSelectionModel().getSelectedItem());
        }
    }
    
    public void handleClickProductosProveedor_Table(MouseEvent event) {
        if(event.getClickCount() ==2)    {
            try{
                        FXMLLoader fxmlLoader = new FXMLLoader(ProductoFormController.class.getResource("ProductoForm.fxml"));
                        Parent root = (Parent)fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setTitle("Datos del Producto");
                        stage.setScene(new Scene(root));
                        stage.initModality(Modality.WINDOW_MODAL);
                        stage.initOwner(
                                ((Node) event.getSource()).getScene().getWindow()
                        );
                        
                        ProductoFormController controller = fxmlLoader.getController();
                        Producto itemEdit = this.productosProveedorTable.getSelectionModel().getSelectedItem();
                        
                        itemEdit = inventarioDs.findProductoById(itemEdit.getIdProducto());
                        controller.setProducto(itemEdit);
                        stage.showAndWait();
                        if(controller.ActionResult()){
                            CargarProductosProveedor(proveedoresTable.getItems().get(idProveedorSelected));
                        }
                        
                    }catch(Exception ex) {
                      FXDialog.showMessageDialog("Error loading producto form: " + ex.getMessage()+ " stack: " + ex.getCause(), "Table", Message.INFORMATION);
                      //  System.out.println("Error loading producto form: " + ex.getMessage()+ " stack: " + ex.getCause());
                    }
        }
     }
    
    private void CargarProveedores(){
        this.proveedoresTable.setItems(FXCollections.observableList(proveedoresDs.getProveedoresList()));
        this.totalProveedoresLabel.setText(this.proveedoresTable.getItems().size()+"");
    }
    
    private void CargarProductosProveedor(Proveedor proveedor) {
       this.productosProveedorTable.setItems(FXCollections.observableList(InventarioDataSource.getProductosByProveedor(proveedor.getIdProveedor())));
       this.totalProductosProveedorLabel.setText(this.productosProveedorTable.getItems().size()+"");
    }
}
