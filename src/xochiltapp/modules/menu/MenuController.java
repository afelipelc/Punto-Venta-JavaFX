/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.menu;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import xochiltapp.MainApp;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class MenuController extends VBox {

    private MainApp application;

    
    @FXML
    Button ventasButton;
    @FXML
    Button pedidosButton;
    @FXML
    Button inventarioButton;
    @FXML
    Button proveedoresButton;
    
    
    /**
     * Initializes the controller class.
     */
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        // TODO
//    }    
    
    public MenuController(MainApp mainApp) {
        this.application = mainApp;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public void handleVentas_Button(ActionEvent event)
    {
        
    }
 
    public void handlePedidos_Button(ActionEvent event)
    {
        
    }
    
    public void handleInventario_Button(ActionEvent event)
    {
        //application.gotoInventario();
        //System.out.println("Presiono ir a Inventario");
    }
    
    public void handleProveedores_Button(ActionEvent event)
    {
        //application.gotoProveedores();
        //System.out.println("Presiono ir a Proveedores");
    }
}
