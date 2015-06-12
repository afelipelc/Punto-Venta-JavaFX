/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.catalogo;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import xochiltapp.dialog.FXDialog;
import xochiltapp.dialog.Message;
import xochiltapp.model.Arreglo;

/**
 * FXML Controller class
 *
 * @author ELIBETH
 */
public class NewCatalogoController extends AnchorPane {

    @FXML
    Button itemButton;
    @FXML
    ImageView imageItem;
    @FXML
    Label nombreItem;
    @FXML
    Label precioItem;
    Arreglo arregloMostrar;

    /**
     * Initializes the controller class.
     */
    public NewCatalogoController(Arreglo arreglo) {
        this.arregloMostrar = arreglo;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewCatalogo.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        //poner la imagen el ImageView
        //poner los datos en el elemento
        //{foto}, {nombre} y {precio}
        datosArreglo();

        itemButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    //abrir para editar
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(ArregloFormController.class.getResource("ArregloForm.fxml"));

                        //AnchorPane root = (AnchorPane) fxmlLoader.getRoot();
                        Parent root = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setTitle("Datos del Arreglo");
                        stage.setScene(new Scene(root));
                        //stage.initModality(Modality.WINDOW_MODAL);
                        stage.initOwner(((Node) t.getSource()).getScene().getWindow());
                        stage.initOwner(((Node) t.getSource()).getScene().getWindow());

                        ArregloFormController controller = fxmlLoader.getController();

                        controller.setArreglo(arregloMostrar);
                        stage.showAndWait();
                        if (controller.ActionResult()) {
                            arregloMostrar = controller.getArreglo();
                            datosArreglo();
                        }
                    } catch (Exception ex) {
                    }
                } else {
                 FXDialog.showMessageDialog( "Ha seleccionado el arreglo " + arregloMostrar.getFoto() + " que se llama: " + arregloMostrar.getNombre(), "Aviso", Message.INFORMATION);
                    //System.out.println("Ha seleccionado el arreglo " + arregloMostrar.getFoto() + " que se llama: " + arregloMostrar.getNombre());
                }
            }
        });
    }

    private void datosArreglo() {
        if (arregloMostrar != null) {
            if (arregloMostrar.getFoto() != null) {
                //Obtener la ruta de la imagen como archivo
                File imgfile = new File("Images/" + arregloMostrar.getFoto());
                //System.out.println("File:"+ imgFile.toURI().toString());
                Image imagen = new Image(imgfile.toURI().toString());
                imageItem.setImage(imagen);
            }
            if (arregloMostrar.getNombre() != null) {
                this.nombreItem.setText(arregloMostrar.getConcepto());
            }

            this.precioItem.setText(DecimalFormat.getCurrencyInstance().format(arregloMostrar.getPrecio()));
        }
    }
}