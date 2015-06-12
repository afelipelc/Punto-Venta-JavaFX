/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.proveedores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import xochiltapp.dialog.*;
import xochiltapp.datasource.ProveedoresDataSource;
import xochiltapp.model.Proveedor;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class ProveedorFormController implements Initializable {

    @FXML
    TextField claveProveedorTxt;
    @FXML
    TextField nombreProveedorTxt;
    @FXML
    TextField direccionProveedorTxt;
    @FXML
    TextField telCasaProveedorTxt;
    @FXML
    TextField telNegocioProveedorTxt;
    @FXML
    TextField telCeleProveedorTxt;
    @FXML
    Button cancelarBtn;
    @FXML
    Button aceptarBtn;
    
    ProveedoresDataSource proveedoresDS = new ProveedoresDataSource();
    private Proveedor proveedor;
    Stage primaryStage;
    boolean actionResult = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.claveProveedorTxt.setDisable(true);
        
    }

    /**
     * @return the proveedor
     */
    public Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * @param proveedor the proveedor to set
     */
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
        loadProveedorData();
    }
    
    public boolean ActionResult()
    {
        return actionResult;
    }
    
    private boolean setProveedorData()
    {
        if((nombreProveedorTxt.getText().equals("") || 
                direccionProveedorTxt.getText().equals("")) &&  
                (!telCasaProveedorTxt.getText().equals("") || !telNegocioProveedorTxt.getText().equals("") || !telCeleProveedorTxt.getText().equals("")))
        {
            FXDialog.showMessageDialog("Ingrese Nombre, Dirección y por lo menos un Teléfono.", "Ingrese datos", Message.INFORMATION);
            return false;
        }
        
        if(proveedor == null)
            proveedor = new Proveedor();
        
        this.proveedor.setNombre(this.nombreProveedorTxt.getText());
        this.proveedor.setDireccion(this.direccionProveedorTxt.getText());
        
        String infotel = "";
        
        infotel = this.telCasaProveedorTxt.getText();
        if(this.telCasaProveedorTxt.getText() == null)
            infotel = "";    
        this.proveedor.setTelCasa(infotel);
        
        infotel = this.telNegocioProveedorTxt.getText();
        if(this.telNegocioProveedorTxt.getText() == null)
            infotel = "";
        this.proveedor.setTelNegocio(infotel);
        
        infotel = this.telCeleProveedorTxt.getText();
        if(this.telCeleProveedorTxt.getText() == null)
            infotel = "";
        this.proveedor.setTelcelular(infotel);
        
        return true;
    }
    
    private void loadProveedorData()
    {
        this.claveProveedorTxt.setText(proveedor.getIdProveedor()+"");
        this.nombreProveedorTxt.setText(proveedor.getNombre());
        this.direccionProveedorTxt.setText(proveedor.getDireccion());
        this.telCasaProveedorTxt.setText(proveedor.getTelCasa());
        this.telNegocioProveedorTxt.setText(proveedor.getTelNegocio());
        this.telCeleProveedorTxt.setText(proveedor.getTelcelular());
    }
    
    public void handleAceptarBtn_Action(ActionEvent event) {
        primaryStage = (Stage) aceptarBtn.getScene().getWindow();
        
        //set Proveedor data
        if(!setProveedorData())
            return;
        
        this.proveedor = proveedoresDS.saveProveedor(proveedor);
        if(proveedor!=null)
        {
            FXDialog.showMessageDialog("El proveedor ha sido guardado.", "Proveedor guardado", Message.INFORMATION);
            actionResult = true;
            primaryStage.close();
        }else {
            FXDialog.showMessageDialog("Ocurrió un error al intentar guardar el proveedor.", "Error:", Message.ERROR);
            actionResult = false;
            primaryStage.close();
        }
    }
    
    public void handleCancelarBtn_Action(ActionEvent event) {
        primaryStage = (Stage) aceptarBtn.getScene().getWindow();
        actionResult = false;
        primaryStage.close();
    }
}
