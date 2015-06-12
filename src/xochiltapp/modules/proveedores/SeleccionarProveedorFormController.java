/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.proveedores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import xochiltapp.dialog.*;
import xochiltapp.datasource.ProveedoresDataSource;
import xochiltapp.model.Proveedor;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class SeleccionarProveedorFormController implements Initializable {

    
    @FXML
    TableView<Proveedor> proveedoresTable;
    @FXML
    TableColumn<Proveedor, Integer> claveProveedorCol;
    @FXML
    TableColumn<Proveedor, String> nombreProveedorCol;
    @FXML
    TableColumn<Proveedor, String> telefonoProveedorCol;
    @FXML
    TextField precioProveedor;
    @FXML
    Button cancelarBtn;
    @FXML
    Button aceptarBtn;
    Stage primaryStage;
    
    ProveedoresDataSource proveedoresDs = new ProveedoresDataSource();
    private Proveedor proveedor;
    private float precioProducto=0;
    
    boolean actionResult = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        claveProveedorCol.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("idProveedor"));
        nombreProveedorCol.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("Nombre"));
        telefonoProveedorCol.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("TelCasa"));
        
        this.CargarProveedores();
    }    
    
     private void CargarProveedores()
    {
        this.proveedoresTable.setItems(FXCollections.observableList(proveedoresDs.getProveedoresList()));
    }
     
     public void handleClickProveedores_Table(MouseEvent event)
    {
        if(this.proveedoresTable.getSelectionModel().getSelectedItem()!=null)
            this.proveedor = this.proveedoresTable.getSelectionModel().getSelectedItem();
        
        
        if(event.getClickCount() ==2)
        {
           this.precioProveedor.requestFocus();
        }
    }

    /**
     * @return the proveedor
     */
    public Proveedor getProveedor() {
        return proveedor;
    }
    
     public void handleAceptarBtn_Action(ActionEvent event)
    {
        primaryStage = (Stage) aceptarBtn.getScene().getWindow();
        if(this.proveedor == null)
        {
             FXDialog.showMessageDialog("Seleccione un Proveedor, o cancele esta acción.", "Atención:", Message.INFORMATION);
             actionResult = false;
             return;
        }
        
        if(this.precioProveedor.getText().equals("") || Float.valueOf(this.precioProveedor.getText()) == 0)
        {
           FXDialog.showMessageDialog("Ingrese el precio que el proveedor le da por este producto, o cancele esta acción.", "Atención:", Message.INFORMATION);
             actionResult = false;
             this.precioProveedor.requestFocus();
             return; 
        }
        
        this.precioProducto = Float.valueOf(this.precioProveedor.getText());
        this.proveedor.setPrecioProducto(precioProducto);
        actionResult = true;
        primaryStage.close();
        
        
    }

    /**
     * @return the precioProducto
     */
    public float getPrecioProducto() {
        return precioProducto;
    }
    
    
    public void handleCancelarBtn_Action(ActionEvent event)
    {
        primaryStage = (Stage) aceptarBtn.getScene().getWindow();
        actionResult = false;
        primaryStage.close();
    }
    
    public boolean ActionResult()
    {
        return actionResult;
    }
}
