/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.catalogo;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import xochiltapp.datasource.ArreglosDataSource;
import xochiltapp.model.Arreglo;
import xochiltapp.model.Empleado;

/**
 * FXML Controller class
 *
 * @author ELIBETH
 */
public class CatalogoController extends AnchorPane {

    @FXML
    AnchorPane ContenedorCatalogoAP;
    @FXML
    FlowPane contenedorItems;
    @FXML
    Button botonAgregar;
    Empleado empleado;
    /**
     * Initializes the controller class.
     */
    public CatalogoController(Empleado empleado,double ancho, double alto) {
        this.empleado = empleado;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Catalogo.fxml"));
        //this.prefHeight(720);
        //this.prefWidth(930);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        ContenedorCatalogoAP.setPrefSize(ancho, alto);
        //CARGAR EL CATALOGO DE ARREGLOS
        //Pedir los arreglos de la BD usando el DATASOURCE DE INVENTARIO
        //ejemplo de datos temporales
        //Cargar lista temporal de elementos
//        for (int i = 1; i <= 6; i++) {
//            Arreglo arreglo = new Arreglo();
//            arreglo.setIdProducto(i);
//            //la extencion de la imagen debe coincidir con la del 
//            //archivo, el nombre debe ser guardado completo en la BD
//            arreglo.setFoto("img" + i + ".jpg");
//            arreglo.setNombre("Arreglo" + i);
//            arreglo.setPrecio(i + 20);
//            
//            NewCatalogoController arregloItem = new NewCatalogoController(arreglo);
//            //Agregarlo al contenedor
//            contenedorItems.getChildren().add(arregloItem);
//        }

        for (Arreglo item : ArreglosDataSource.listaArreglos()) {
            NewCatalogoController arregloItem = new NewCatalogoController(item);
            contenedorItems.getChildren().add(arregloItem);
        }

        this.botonAgregar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                agregarArregloAction(((Node) t.getSource()).getScene().getWindow());
            }
        });
    }

    public void agregarArregloAction(Window parent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ArregloFormController.class.getResource("ArregloForm.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar arreglo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parent);

            ArregloFormController controller = fxmlLoader.getController();
            stage.showAndWait();
            if (controller.actionResult) {
                NewCatalogoController arregloItem = new NewCatalogoController(controller.getArreglo());
                //Agregarlo al contenedor
                contenedorItems.getChildren().add(1, arregloItem);
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + " stack: " + ex.getCause());
        }
    }
}
